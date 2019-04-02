package com.example.mymap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    LocationManager locationManager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
//        Log.d("Connection issue1",netInfo.getDetailedState().toString());
        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){


            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                    .coordinatorLayout);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Check your Network settings", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    startActivity(intent);

                }
            }).show();
            //Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            Log.d("Connection issue2","Check Network Settings");
            return;
        }else{

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

        }




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

            Log.d("Connection issue1","Check Internet Connetion");
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                    .coordinatorLayout);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Check your Location settings", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    startActivity(intent);

                }
            }).show();
            return;
        }
        else{
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(true);

        }

    }


    public void onMapSearch(View view) {

//        mMap.clear();
//        EditText locationSearch = (EditText) findViewById(R.id.editText);
//        EditText destinationSearch = (EditText) findViewById(R.id.editText1);
//        String location = locationSearch.getText().toString();
//        String destination = destinationSearch.getText().toString();
//        List<Address>addressList = null;
//        List<Address>addressList1 = null;
//        if ((location != null || !location.equals("")) && (destination != null || !destination.equals(""))) {
//            Geocoder geocoder = new Geocoder(this);
//            try {
//                addressList = geocoder.getFromLocationName(location, 1);
//                addressList1 = geocoder.getFromLocationName(destination, 1);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if(addressList!=null && addressList1!=null){
//                Address address = addressList.get(0);
//                Address address1 = addressList1.get(0);
//
//                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                LatLng latLng1 = new LatLng(address1.getLatitude(), address1.getLongitude());
//
//                Log.i("msg2",latLng.toString());
//
//                String url1="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latLng+"&radius=1500&type=restaurant&key=AIzaSyAIQEsBVFDfToToj6PY4LA7_aPYA78nric&rank=distance";
//                String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+latLng+"&destination="+latLng1+"&key=AIzaSyAIQEsBVFDfToToj6PY4LA7_aPYA78nric";
//
////                Uri gmmIntentUri = Uri.parse(url1);
////                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
////                mapIntent.setPackage("com.google.android.apps.maps");
////                startActivity(mapIntent);
//
//                mMap.addMarker(new MarkerOptions().position(latLng).title("Source").draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                mMap.addMarker(new MarkerOptions().position(latLng1).title("Destination"));
//                //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1,10.2f));
//
//                Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
//                    .clickable(true)
//                    .add(latLng,latLng1));
//
//            }else{
//                Log.i("msg1","Enter Valid Places");
//            }
//
//
//
////            GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
////            Object[] toPass = new Object[2];
////            toPass[0] = googleMap;
////            toPass[1] = googlePlacesUrl.toString();
////            googlePlacesReadTask.execute(toPass);
//        }

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "You selected custom search", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapsActivity.this,lab3Map.class);
                startActivity(intent);

            }
        }).show();

//        Intent intent = new Intent(this,lab3Map.class);
//        // startActivity(intent);
//        startActivity(intent);


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






    public void onDirectionSearch(View view) {

//        mMap.clear();
//        EditText locationSearch = (EditText) findViewById(R.id.editText);
//        EditText destinationSearch = (EditText) findViewById(R.id.editText1);
//        String location = locationSearch.getText().toString();
//        String destination = destinationSearch.getText().toString();
//        List<Address>addressList = null;
//        List<Address>addressList1 = null;
//
//        if ((location != null || !location.equals("")) && (destination != null || !destination.equals(""))) {
//            Geocoder geocoder = new Geocoder(this);
//            try {
//                addressList = geocoder.getFromLocationName(location, 1);
//                addressList1 = geocoder.getFromLocationName(destination, 1);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Address address = addressList.get(0);
//            Address address1 = addressList1.get(0);
//
//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//            LatLng latLng1 = new LatLng(address1.getLatitude(), address1.getLongitude());
//
//            String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+latLng+"&destination="+latLng1+"&key=AIzaSyANKkguEkfMX6YphU43O7sKoEG5nBh8KrM";
//
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Source"));
//            mMap.addMarker(new MarkerOptions().position(latLng1).title("Destination"));
//            //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1,10.2f));
//        }




        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "You selected current location search", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapsActivity.this,MapsActivity2.class);
                startActivity(intent);

            }
        }).show();


//        //Intent intent = new Intent(this,lab3Map.class);
//        Intent intent = new Intent(this,MapsActivity2.class);
//        // startActivity(intent);
//        startActivity(intent);

    }




    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
           // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
           // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    public static class PlaceholderFragment1 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment1() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment1 newInstance(int sectionNumber) {
            PlaceholderFragment1 fragment = new PlaceholderFragment1();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment1, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment=null;
            switch(position){
                case 0 : fragment=PlaceholderFragment.newInstance(position);
                break;
                case 1 : fragment=PlaceholderFragment1.newInstance(position);
                break;
            }
            return fragment;
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }




}
