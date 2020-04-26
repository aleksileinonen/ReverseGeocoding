package com.example.reversegeocoding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import android.location.Geocoder;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {



    private FusedLocationProviderClient fusedLocationClient;
    private TextView locationText;
    private TextView cityText;
    private double locLatitude, locLongitude;
    private static DecimalFormat df = new DecimalFormat("0.00");
    private int locationRequestCode = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.locationText = (TextView) findViewById(R.id.locationText);
        this.cityText = (TextView) findViewById(R.id.cityCountry);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);

        } else {

            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {

                @Override
                public void onSuccess(Location location) {

                    if (location != null) {
                        locLatitude = location.getLatitude();
                        locLongitude = location.getLongitude();
                        String strLongitude = (df.format(locLongitude));
                        String strLatitude = (df.format(locLatitude));
                        locationText.setText(strLatitude + ", " + strLongitude);
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> listAddresses = geocoder.getFromLocation(locLatitude, locLongitude, 1);
                            if (null != listAddresses && listAddresses.size() > 0) {
                                String cityLocation = listAddresses.get(0).getAddressLine(0);
                                cityText.setText(cityLocation);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            });
        }
    }

}



