package com.example.ergasia1;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ergasia1.databinding.ActivityMapsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Marker ston peiraia
        LatLng piraeus = new LatLng(37.941642,23.653004);
        // Metakinisi tou xarth na deixnei ton peiraia
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(piraeus, 15));

        // Syndesh me th vash firebase
        database = FirebaseDatabase.getInstance("https://ergasia1-e2bb6-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("eventList");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Loop sto list sthn firebase wste na paroume ola ta apothikevmena object
                for (DataSnapshot eventSnap : snapshot.getChildren()){
                    Event event = eventSnap.getValue(Event.class);
                    assert event != null;
                    LatLng point = new LatLng(event.latitude, event.longtitude);
                    if (event.acceleration) {
                        // Periptwsh epitaxynshs ( mple marker , analogos titlos )
                        mMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title("Epitaxynsh " + event.date)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    } else {
                        // Periptwsh frenarismatos ( kokkino marker , analogos titlos )
                        mMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title("Frenarisma " + event.date)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }
}