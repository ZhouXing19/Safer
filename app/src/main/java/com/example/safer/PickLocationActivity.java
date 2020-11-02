package com.example.safer;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PickLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FloatingActionButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        submitButton = (FloatingActionButton) findViewById(R.id.submitButton);

        // Add a marker in Sydney and move the camera
        LatLng MainQuad = new LatLng(41.7885889, -87.5997399);
        LatLng UPC = new LatLng(41.7951455, -87.5914046);
        mMap.addMarker(new MarkerOptions().position(MainQuad).title("Marker in UChicago"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo((float)14.2));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UPC));

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng myFocus = mMap.getCameraPosition().target;
                Log.w("PickLocationActivity", Double.toString(myFocus.latitude)+Double.toString(myFocus.longitude));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng myFocus = mMap.getCameraPosition().target;
                double[] SelectedLocation = {myFocus.latitude, myFocus.longitude};
                Log.w("Pick!!LocationActivity", Double.toString(SelectedLocation[0])+Double.toString(SelectedLocation[1]));
                Intent data = new Intent();
                data.putExtra("SelectedLocation", SelectedLocation);
                data.putExtra("demoStr", "helloworld");
                setResult(RESULT_OK, data);
                finish();

            }
        });


    }




}