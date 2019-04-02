package com.example.mymap;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Restaurants extends AppCompatActivity  {

    private ListView mListView;
    public static final String lat3 = "com.example.myfirstapp.MESSAGE4";
    public static final String long3 = "com.example.myfirstapp.MESSAGE5";

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
        setContentView(R.layout.activity_restaurants);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button onMap=(Button) findViewById(R.id.onMap) ;

        Log.d("Restaurants","test message");

        Intent intent = getIntent();
        String longitude = intent.getStringExtra(lab3Map.long1);
        String latitude = intent.getStringExtra(lab3Map.lat1);

        if(longitude==null && latitude==null){

            longitude = intent.getStringExtra(MapsActivity2.long2);
            latitude = intent.getStringExtra(MapsActivity2.lat2);

        }


        final String finalLongitude = longitude;
        final String finalLatitude = latitude;

        onMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Restaurants.this,Restaurants_on_Map.class);
                intent.putExtra(long3, finalLongitude);
                intent.putExtra(lat3, finalLatitude);
                Log.i("MyAct2 msg4", finalLatitude +finalLongitude);
                startActivity(intent);

            }
        });

        Log.d("Restaurants",longitude.toString()+latitude.toString());

        Double lng = Double.parseDouble(longitude);
        Double lat = Double.parseDouble(latitude);
        int radius = 1000;


        ArrayList<Place> list = search(lat, lng, radius);

        Collections.sort(list, new Comparator<Place>() {
            @Override
            public int compare(Place lhs, Place rhs) {
                return rhs.rating.compareTo(lhs.rating);
            }
        });

        if (list != null)
        {
            mListView = (ListView) findViewById(R.id.listView);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_selectable_list_item, list);
            mListView.setAdapter(adapter);

        }



    }


    public static ArrayList<Place> search(double lat, double lng, int radius) {
        ArrayList<Place> resultList = null;

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
            resultList = new ArrayList<Place>(predsJsonArray.length());
            int n=predsJsonArray.length();
            Log.d("Total Results","Total Results"+(n));
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


    ///----------------------------------------------///



    //Value Object for the ArrayList
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
            return rating +" |-> "+name ; //This is what returns the name of each restaurant for array list
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
