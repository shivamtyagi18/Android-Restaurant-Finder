package com.example.mymap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Restaurants_on_Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //private GoogleMap mMap;

    private static final String API_KEY = "AIzaSyAIQEsBVFDfToToj6PY4LA7_aPYA78nric";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json?";
    private static final String LOG_TAG = "ListRest";
    private static final String RANK = "rating";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_on__map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

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
            Intent intent = getIntent();
            String longitude = intent.getStringExtra(Restaurants.long3);
            String latitude = intent.getStringExtra(Restaurants.lat3);

            Log.e("test--", longitude + latitude);

            Double lng = Double.parseDouble(longitude);
            Double lat = Double.parseDouble(latitude);
            int radius = 1000;


            ArrayList<Restaurants_on_Map.Place> list = search(lat, lng, radius);

            Log.d("count", list.toString());

            if (list != null)
            {
                for (int i = 0; i < list.size(); i++) {
                    Log.d("item", list.get(i).latitude.toString());
                    LatLng latLng=new LatLng( list.get(i).latitude, list.get(i).longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(list.get(i).name).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                }

            }

            mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Source"));


            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));

        }
    }

//    public void setMarker(double x, double y){
//        LatLng latLng=new LatLng(x,y);
//        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//
//    }


    public static ArrayList<Restaurants_on_Map.Place> search(double lat, double lng, int radius) {
        ArrayList<Restaurants_on_Map.Place> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("location=" + String.valueOf(lat) + "," + String.valueOf(lng));
            sb.append("&radius=" + String.valueOf(radius));
            sb.append("&type=restaurant");
            sb.append("&key=" + API_KEY);
            //sb.append("&rankby=distance");

            Log.e(LOG_TAG, sb.toString());

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the descriptions from the results
            resultList = new ArrayList<Restaurants_on_Map.Place>(predsJsonArray.length());
            int n=predsJsonArray.length();
            Log.d("Total Results","Total Results on map"+(n));

            for (int i = 0; i < predsJsonArray.length(); i++) {

                JSONObject jsonResult = predsJsonArray.getJSONObject(i);

                // JSONArray geometry = jsonResult.getJSONArray("geometry");
                JSONObject geometry = jsonResult.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                // JSONArray data = location.getJSONArray("location");

                Place place = new Place();
                place.reference = predsJsonArray.getJSONObject(i).getString("reference");
                place.name = predsJsonArray.getJSONObject(i).getString("name");
                place.rating = predsJsonArray.getJSONObject(i).getString("rating");

                place.latitude = location.getDouble("lat");
                place.longitude = location.getDouble("lng");

                resultList.add(place);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
        }

        return resultList;
    }


    public static class Place {

        private String reference;
        private String name;
        private String rating;
        private Double latitude;
        private Double longitude;

        public Place(){
            super();
        }
        @Override
        public String toString(){
            return this.name + "\t" + rating +"\n"+ latitude+"\t/"+longitude; //This is what returns the name of each restaurant for array list
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
