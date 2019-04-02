package com.example.mymap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback  {

    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    static final int REQUEST_PLACES = 1;
    public static final String lat2 = "com.example.myfirstapp.MESSAGE2";
    public static final String long2 = "com.example.myfirstapp.MESSAGE3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        client = LocationServices.getFusedLocationProviderClient(this);
        Button button= (Button) findViewById(R.id.button2);
        Button button3= (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.getLastLocation().addOnSuccessListener(MapsActivity2.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        if(location!=null){
                            EditText editText1=findViewById(R.id.address);
                            String address = getCityName(latLng);
                            editText1.setText(address);
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Current"));
                        }


                    }
                });
            }
        });



        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.getLastLocation().addOnSuccessListener(MapsActivity2.this, new OnSuccessListener<Location>() {

                   JSONObject places;
                    @Override
                    public void onSuccess(Location location) {

                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        final String url1="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latLng.latitude+","+latLng.longitude+"&radius=1500&type=restaurant&key=AIzaSyAIQEsBVFDfToToj6PY4LA7_aPYA78nric&rank=distance";
                        Log.d("mylog", url1);

                        Intent intent = new Intent(MapsActivity2.this,Restaurants.class);
                        //intent.putExtra(EXTRA_MESSAGE, latLng);

                        Double longitude = latLng.longitude;
                        Double latitude = latLng.latitude;
                        String longit = Double.toString(longitude);
                        String lat = Double.toString(latitude);
                        intent.putExtra(long2, longit);
                        intent.putExtra(lat2, lat);
                        Log.i("MyAct2 msg4",lat+longit);
                        startActivity(intent);

//                        if(location!=null){
//                            Uri gmmIntentUri = Uri.parse("geo:"+latLng.latitude+","+latLng.longitude+"?q=restaurants"); //to open places in Google maps
//                           // Uri gmmIntentUri = Uri.parse(url1);
//                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                            startActivity(mapIntent);
//
//                        }

                    }
                });
            }
        });

    }


    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(MapsActivity2.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getAddressLine(0);
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);

            Address address1 = addresses.get(0);
            LatLng latLng = new LatLng(address1.getLatitude(), address1.getLongitude());

           String url1="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latLng.latitude+","+latLng.longitude+"&radius=1500&type=restaurant&key=AIzaSyAIQEsBVFDfToToj6PY4LA7_aPYA78nric&rank=distance";
          //  String url1="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.7749,-122.4194&radius=1500&type=restaurant&key=AIzaSyAIQEsBVFDfToToj6PY4LA7_aPYA78nric&rank=distance";
           // String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+latLng+"&destination="+latLng1+"&key=AIzaSyAIQEsBVFDfToToj6PY4LA7_aPYA78nric";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        else{
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).draggable(true).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    public void getCoordinates(View view) {

        LatLng delhi = new LatLng(10, 10);

        mMap.addMarker(new MarkerOptions()
                .position(delhi)
                .title("Delhi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));



    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


}

