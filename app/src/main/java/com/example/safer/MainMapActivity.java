package com.example.safer;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.ProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.List;


public class MainMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private SupportMapFragment mapFragment;
    private LocationCallback mLocationCallback;
    private List<Polyline> polylines;
    private final int REQUEST_CODE = 20;
    Marker mCurrLocationMarker;
    private int MAP_LOADED = 0;
    int myId = 0;

    private BottomNavigationView bottomNavigationView;
    private ProgressIndicator progressIndicator;
    private View darkenView;
    private FirebaseDatabase myFirebaseDatabase;
    private DatabaseReference myFDref;

    private static final String TAG = "molly_debugging";
    //private static final String TAG = MainMapActivity.class.getSimpleName();

    // widgets
    AutoCompleteTextView mSearchText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);

        polylines = new ArrayList<>();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressIndicator = (ProgressIndicator) findViewById(R.id.progressIndicator);
        progressIndicator.show();

        darkenView = (View) findViewById(R.id.darkenView);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_map);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_danger_list:
                        // do something here
                        intent = new Intent(MainMapActivity.this, DangerListActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_map:
                        // do something here
                        return true;
                    case R.id.action_post_danger:
                        // do something here
                        intent = new Intent(MainMapActivity.this, PostDangerActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_profile:
                        intent = new Intent(MainMapActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return true;
                }
            }
        });

        // display icons
        displayIcon();

        // notification system
        myFDref = FirebaseDatabase.getInstance().getReference().child("Danger");
        myFDref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                notification();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder ntfBuilder = new NotificationCompat.Builder(this, "n")
                .setContentTitle("Safer App")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setAutoCancel(true)
                .setContentText("Here's a new posted danger");

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, ntfBuilder.build());
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient == null) {
            Log.i(TAG, "this is null right now");
        }
        if (mLocationCallback == null) {
            Log.i(TAG, "this is also null right now");
        }

        //stop location updates when Activity is no longer active
//        if (mFusedLocationClient != null) {
//            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//        }
        if (mFusedLocationClient != null && mLocationCallback != null) {
            try {
                Log.i(TAG, "trying");
                final Task<Void> voidTask = mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                if (voidTask.isSuccessful()) {
                    Log.d(TAG, "StopLocation updates successful! ");
                } else {
                    Log.d(TAG, "StopLocation updates unsuccessful! " + voidTask.toString());
                }
            } catch (SecurityException exp) {
                Log.d(TAG, " Security exception while removeLocationUpdates");
            }
        } else {

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Log.i(TAG, "map is ready");
        // -------------- Auto requests for locations (No need to change this part) --------------
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //every 1 second, get a new location request
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //use a log of battery, but ensure great accuracy
        // initialize search bar

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    Log.i(TAG, "found a null object");
//                }
                List<Location> locationList = locationResult.getLocations();

                for (Location location : locationResult.getLocations()) {

                    if (getApplicationContext() != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        }
                    }
                }
            }
        };


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "request");
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermission();
            }
        }

        // -------------- MAP STYLE --------------

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        // --------------Set a fixed pin on map --------------

        LatLng MainQuad = new LatLng(41.7885889, -87.5997399);
        LatLng UPC = new LatLng(41.7951455, -87.5914046);
        mMap.addMarker(new MarkerOptions().position(MainQuad).title("Marker in UChicago"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo((float) 14.2));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UPC));
        mMap.setOnMapLoadedCallback(this);
        //getDeviceLocation();
    }

    // -------------- Some default settings for Google map API. No changes are needed for the following part. --------------

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(MainMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    // -------------- Some default settings for Google map API. No changes are needed for the following part. --------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }


    }


    @Override
    protected void onStop() {
        super.onStop();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driverAvailable");

        }
    }

    // Converts a vector drawable to a bitmap
    private BitmapDescriptor getBitmap(int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(this, vectorResId);
        Bitmap bitmap = Bitmap.createBitmap(70, 70, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // Get all data from database
    private void displayIcon() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Danger");
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DangerHelperClass danger = snapshot.getValue(DangerHelperClass.class);
                    String title = danger.getTitle();
                    String time = danger.getTime();
                    String description = danger.getDescription();
                    String location = danger.getLocation();
                    String imageUrl = danger.getImageUrl();
                    String category = danger.getCategory();
                    String userId = danger.getUserId();
                    double latitude = danger.getLatitude();
                    double longitude = danger.getLongitude();
                    Log.i(TAG, category);
                    Log.i(TAG, category);
                    LatLng dangerLocation = new LatLng(latitude, longitude);
                    if (category.equals("Robbery")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(dangerLocation)
                                .title(title)
                                .icon(getBitmap(R.drawable.ic_handcuffs))
                                .snippet(location + "\n" + time)
                        );
                    } else if (category.equals("Arrest")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(dangerLocation)
                                .title(title)
                                .icon(getBitmap(R.drawable.ic_police_line))
                                .snippet(location + "\n" + time)
                        );
                    }


                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {

                            LinearLayout info = new LinearLayout(MainMapActivity.this);
                            info.setOrientation(LinearLayout.VERTICAL);

                            TextView title = new TextView(MainMapActivity.this);
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.CENTER);
                            title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            TextView snippet = new TextView(MainMapActivity.this);
                            snippet.setTextColor(Color.GRAY);
                            snippet.setText(marker.getSnippet());

                            info.addView(title);
                            info.addView(snippet);

                            return info;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        //https:developers.google.com/maps/documentation/android-sdk/marker
    }


    @Override
    public void onMapLoaded() {
        progressIndicator.hide();
        darkenView.animate()
                .alpha(0f)
                .setDuration(2)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        darkenView.setVisibility(View.GONE);
                    }
                });
        Toast.makeText(MainMapActivity.this, "Map Loaded!", Toast.LENGTH_SHORT).show();
        intializeSearch();
    }

    private void intializeSearch() {
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER) {
                    //execute our method for searching
                    Log.i(TAG, "starting search");
                    geoLocate();
                    return true;
                }
                return false;
            }
        });
    }

    private void geoLocate() {
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MainMapActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.i(TAG, "geoLocation: IOException: " + e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.i(TAG, "geolocate: found a location: " + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 15f);
        }
    }

    private void getDeviceLocation() {
        Log.i(TAG, "getDeviceLocation");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Task location = mFusedLocationClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Location currentLocation = (Location) task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),15f);
                        Log.i(TAG, "on complete: found location!");
                    }
                }
            });
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        //Move the camera to the user's location and zoom in!
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}