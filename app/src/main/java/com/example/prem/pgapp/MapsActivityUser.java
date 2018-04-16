package com.example.prem.pgapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivityUser extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String landmark,pgkey=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_user);
        pgkey=getIntent().getExtras().getString("pgkey");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        landmark=getIntent().getExtras().getString("landmark");
        System.out.println(landmark);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.owner_home_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SinglePGActivity.class);
        intent.putExtra("pgkey",pgkey);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        String location=landmark+",Allahabad";
        Geocoder geocoder=new Geocoder(this);
        List<Address> addressList=null;
        try {
            addressList= geocoder.getFromLocationName(location,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address=addressList.get(0);
        LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Pg is located near this landmark"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        float zoomLevel = 14.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }
}
