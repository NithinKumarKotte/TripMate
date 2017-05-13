package com.example.d27sa.tripmate;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/*import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Distance;
import Modules.Duration;
import Modules.Route;*/

import static com.example.d27sa.tripmate.R.id.tvDuration;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private Button btnFindPath;
    private Button currentlocation;

    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Marker> waypointMarkers = new ArrayList<>();

    private List<LatLng> originLatLng = new ArrayList<>();
    private List<String> destinationAddress= new ArrayList<>();
    private List<String> waypointAddress = new ArrayList<>();

    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static int MY_PERMISSIONS_REQUEST_LOCATION = 34;
    //private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location curLocation;
    private ArrayList<String> mylist= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String list="";
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);
        currentlocation = (Button) findViewById(R.id.currentlocation);
        mylist=getIntent().getStringArrayListExtra("note");
        etDestination.setText(mylist.get(0));
        for(int i= mylist.size()-1;i>0;i--){
            list=mylist.get(i)+"|"+list;
        }

        etOrigin.setText(list.substring(0,list.lastIndexOf("|")));
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

    }


    private void sendRequest() {

        double currentLatitude = curLocation.getLatitude();
        double currentLongitude = curLocation.getLongitude();
        String origin = currentLatitude +","+ currentLongitude; //etOrigin.getText().toString();
        String waypoint = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        System.out.println(origin);
        System.out.println(waypoint);
        System.out.println(destination);
        /*if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination, waypoint).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //LatLng hcmus = new LatLng(10.762963, 106.682394);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
        //originMarkers.add(mMap.addMarker(new MarkerOptions()
        //        .title("Đại học Khoa học tự nhiên")
        //      .position(hcmus)));

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
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("Location services Connected");
        // Here, thisActivity is the current activity
        Location location;
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            //curLocation = new Location("NULL");
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            curLocation = location;
            if(curLocation==null){
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            else{
                handleNewLocation(location);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    private void handleNewLocation(Location location) {
        System.out.println(location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        System.out.println("Lat:"+currentLatitude+"");
        System.out.println("Lng:"+currentLongitude+"");
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!"+" GPS:"+"Lat: "+currentLatitude+" Lng: "+currentLongitude);
        //mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
    }


    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Location services suspended. Please reconnect");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Location services connection failed with some code");
            //Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        handleNewLocation(location);
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    public void CurrentLocation(View view){
        int index = waypointAddress.size();
        String waypoint = new String();
        for(int i=0;i<index;i++){
            waypoint = waypoint+waypointAddress.get(i).toString()+"/";
        }
        System.out.println(waypoint);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                //Uri.parse("https://www.google.com/maps/dir/38.8321459,-77.3069551/3313+Preserve+Oaks+Ct,+Fairfax,+VA+22030/Circle+Towers,+9335+Lee+Hwy,+Fairfax,+VA+22031"));
                Uri.parse("https://www.google.com/maps/dir/"+originLatLng.get(0).latitude+","+originLatLng.get(0).longitude+"/"+waypoint+destinationAddress));
        //(lat,lng)
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }


    @Override
    public void onDirectionFinderSuccess(ArrayList<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        waypointMarkers = new ArrayList<>();

        originLatLng = new ArrayList<>();
        destinationAddress = new ArrayList<>();
        waypointAddress = new ArrayList<>();

        Distance distance  = new Distance("0",0);
        Duration duration = new Duration("0",0);


        //System.out.println();
        int length=routes.size();


        System.out.println("Length:"+length);
        //for (Route route : routes)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(routes.get(0).startLocation, 10));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                .title(routes.get(0).startAddress)
                .position(routes.get(0).startLocation)));
        originLatLng.add(routes.get(0).startLocation);

        if(length>1) {
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(routes.get(length - 1).endAddress)
                    .position(routes.get(length - 1).endLocation)));
            destinationAddress.add(routes.get(length-1).endAddress);
        }
        if(length==1)
        {
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(routes.get(0).endAddress)
                    .position(routes.get(0).endLocation)));
            destinationAddress.add(routes.get(0).endAddress);
        }

        if(length > 1){
            for (int i=1;i<length;i++) {
                waypointMarkers.add(mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker())
                        .title(routes.get(i).startAddress)
                        .position(routes.get(i).startLocation)));
            }

            for (int j=1;j<length;j++){
                waypointAddress.add(routes.get(j).startAddress);
            }
        }

        for(int j=0;j<length;j++){
            distance.value = distance.value + routes.get(j).distance.value;
        }

        distance.value /= 1609.34;  //converting to miles
        distance.text = distance.value+" mi";
        ((TextView) findViewById(R.id.tvDistance)).setText(distance.text);

        for(int j=0;j<length;j++){
            duration.value = duration.value + routes.get(j).duration.value;
        }
        duration.value /= 60;
        duration.text = duration.value+" mins";
        ((TextView) findViewById(tvDuration)).setText(duration.text);


        for (Route route : routes) {
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 10));

            //System.out.println("Start: "+route.startAddress);
            //System.out.println("Stop: "+route.endAddress);

            //var markerSize = Maps.StaticMap.MarkerSize.MID;
            //var markerColor = Maps.StaticMap.Color.GREEN
            //var markerLetterCode = 'A'.charCodeAt();
            /*
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));


            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            */
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}

