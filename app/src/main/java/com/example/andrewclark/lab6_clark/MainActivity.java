package com.example.andrewclark.lab6_clark;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private Button locButton;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;



    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();



    }


    @Override
    public void onConnected(Bundle bundle) {

        boolean permissionGranted = checkLocationPermission();

        if ( permissionGranted ) {
            this.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }

    }

    @Override
    public void onConnectionSuspended(int cause) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        locButton = (Button) findViewById(R.id.myLocButt);


        locButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                if(checkLocationPermission() == true) {
                    Intent mapIntent = new Intent(MainActivity.this, MapsActivity.class);
                    mapIntent.putExtra("Location",mLastLocation);
                    startActivity(mapIntent);
                }
                
            }
        });



    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission. ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            // Request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    }

                } else {

                    // permission denied. Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }



}