package com.example.mymap;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class lab3Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String lat1 = "com.example.myfirstapp.MESSAGE";
    public static final String long1 = "com.example.myfirstapp.MESSAGE1";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
//
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
            mMap.getUiSettings().setScrollGesturesEnabled(true);

        }

    }


    public void onMapSearch1(View view) {

        mMap.clear();
        EditText locationSearch = (EditText) findViewById(R.id.location);

        String location = locationSearch.getText().toString();

        List<Address>addressList = null;

        if ((location != null)) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList!=null){
                Address address = addressList.get(0);  // address of current location
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                Log.i("lab3 msg2",latLng.toString());

                mMap.addMarker(new MarkerOptions().position(latLng).title("Source").draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.2f));

            }else{
                Log.i("lab3 msg1","Enter Valid Places");
            }

        }

    }



    public void searchRestaurant(View view) {

        mMap.clear();
        EditText locationSearch = (EditText) findViewById(R.id.location);

        String location = locationSearch.getText().toString();

        List<Address>addressList = null;

        if ((location != null)) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList!=null){
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                Log.i("lab3 msg3",latLng.toString());
                Uri gmmIntentUri = Uri.parse("geo:"+latLng.latitude+","+latLng.longitude+"?q=restaurants"); //to open places in Google maps
                // Uri gmmIntentUri = Uri.parse(url1);
                //Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri); // to open in browser JSON data
               // startActivity(mapIntent);

                Intent intent = new Intent(this,Restaurants.class);
                Double longitude = latLng.longitude;
                Double latitude = latLng.latitude;
                String longit = Double.toString(longitude);
                String lat = Double.toString(latitude);
                intent.putExtra(long1, longit);
                intent.putExtra(lat1, lat);
                Log.i("lab3 msg4",lat+longit);
                startActivity(intent);

            }else{
                Log.i("lab3 msg5","Enter Valid Places");
            }

        }

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
