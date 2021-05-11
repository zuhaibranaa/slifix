package com.slifix.slifix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity {
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    GoogleMap map = null;
    MarkerOptions options;
    ImageButton geolocate;
    Button search;
    LatLng latLng;
    String lati,longi;
    Geocoder geocoder;
    EditText sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        search = (Button) findViewById(R.id.locSrch);
        geolocate = (ImageButton) findViewById(R.id.geolocate);
        sp = (EditText) findViewById(R.id.searchPlaceEt);
        if (options != null){
            map.clear();
        }
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }
    private void geolocate(GoogleMap googleMap){
        geocoder = new Geocoder(getApplicationContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(sp.getText().toString(),1);
            if (list.size() == 0){
                Toast.makeText(this, "No Location Found", Toast.LENGTH_SHORT).show();
            }else {
                googleMap.clear();
                Double lg = Double.valueOf(list.get(0).getLongitude());
                Double lt = Double.valueOf(list.get(0).getLatitude());
                latLng = new LatLng(lt, lg);
                lati = String.valueOf(latLng.latitude);
                longi = String.valueOf(latLng.longitude);
                options = new MarkerOptions().position(latLng).title(list.get(0).getAddressLine(0));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                googleMap.addMarker(options);
                Toast.makeText(this, "Location Found", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(getApplicationContext(),FoodDashboard.class);
                startActivity(it);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                map.clear();
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);

                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Selected Location")
                        .snippet(snippet));
            }
        });
    }
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {

                            setMapLongClick(googleMap);
                            search.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    geolocate(googleMap);
                                }
                            });
                            geolocate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getcl(googleMap,location);
                                }
                            });
                        }
                    });
                }
            }
        });
    }
    private void getcl(GoogleMap googleMap,Location location) {
        googleMap.clear();
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Toast.makeText(MapsActivity.this, latLng.toString(), Toast.LENGTH_SHORT).show();
        options = new MarkerOptions().position(latLng).title("Current Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(options);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
}