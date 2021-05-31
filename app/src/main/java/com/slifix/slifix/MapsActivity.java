package com.slifix.slifix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity {
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    GoogleMap map = null;
    MarkerOptions options;
    Button search;
    LatLng latLng;
    Geocoder geocoder;
    EditText sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (DataManager.getAuthToken() == null){
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (DataManager.getUserLatitude () != null){
            startActivity (new Intent (getApplicationContext (),FoodDashboard.class));
            finish ();
        }else {
            Toast.makeText (this, "Top On A Location To Select", Toast.LENGTH_SHORT).show ();
        }
        search = findViewById(R.id.locSrch);

        sp = findViewById(R.id.searchPlaceEt);
        if (options != null){
            map.clear();
        }
        client = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();
    }
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                supportMapFragment.getMapAsync(googleMap -> {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.clear();
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    setMapLongClick(googleMap);
                    search.setOnClickListener(v -> geoLocate (googleMap));
                });
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
    private void geoLocate(GoogleMap googleMap){
        geocoder = new Geocoder(getApplicationContext());
        List<Address> list;
        try {
            list = geocoder.getFromLocationName(sp.getText().toString(),1);
            if (list.size() == 0){
                Toast.makeText(this, "No Location Found", Toast.LENGTH_SHORT).show();
            }else {
                double lg = list.get (0).getLongitude ();
                double lt = list.get (0).getLatitude ();
                latLng = new LatLng(lt, lg);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapClickListener(latLng -> {
            map.clear();
            finish ();
            String snippet = String.format(Locale.getDefault(),
                    "Lat: %1$.5f, Long: %2$.5f",
                    latLng.latitude,
                    latLng.longitude);

            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Selected Location")
                    .snippet(snippet));
            DataManager.setUserLongitude (String.valueOf(latLng.longitude));
            DataManager.setUserLatitude (String.valueOf(latLng.latitude));
            Geocoder geo = new Geocoder (getApplicationContext (),Locale.getDefault ());
            List<Address> addresses;
            try {
                addresses = geo.getFromLocation (latLng.latitude,latLng.longitude,1);
                DataManager.setUserLocation(addresses.get (0).getAddressLine (0));
            }catch (Exception e){
                e.printStackTrace ();
            }
            Intent it = new Intent(getApplicationContext(), FoodDashboard.class);
            startActivity(it);
        });
    }

}