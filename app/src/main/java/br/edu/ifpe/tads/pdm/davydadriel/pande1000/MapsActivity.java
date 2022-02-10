package br.edu.ifpe.tads.pdm.davydadriel.pande1000;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Vaccine;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final static int FINE_LOCATION_REQUEST = 1;
    private boolean fine_location;
    FirebaseAuth mAuth;
    FirebaseAuthListener authListener;
    DatabaseReference drVaccines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        requestPermission();


        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng recife = new LatLng(-8.05, -34.9);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(recife));

        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();


        drVaccines = fbDB.getReference("vaccines");

        drVaccines.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("ChildAdded");
                Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);
                System.out.println(vaccine.getLatitude());
                System.out.println(vaccine.getLongitude());
                System.out.println(vaccine.getName());
                mMap = googleMap;
                LatLng recife = new LatLng(vaccine.getLatitude(), vaccine.getLongitude());
//                LatLng caruaru = new LatLng(-8.27, -35.98);
//                LatLng joaopessoa = new LatLng(-7.12, -34.84);

                mMap.addMarker( new MarkerOptions().
                        position(recife).
                        title("Recife").
                        icon(BitmapDescriptorFactory.defaultMarker(35)));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(recife));
//                mMap.addMarker( new MarkerOptions().
//                        position(caruaru).
//                        title("Caruaru").
//                        icon(BitmapDescriptorFactory.defaultMarker(120)));
//                mMap.addMarker( new MarkerOptions().
//                        position(joaopessoa).
//                        title("João Pessoa").
//                        icon(BitmapDescriptorFactory.defaultMarker(230)));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);
                System.out.println("minhas coisas");
                System.out.println(vaccine.getName());

                LatLng vacineLatLng = new LatLng(vaccine.getLatitude(), vaccine.getLongitude());
                mMap.addMarker( new MarkerOptions().
                        position(vacineLatLng).
                        title(vaccine.getName()).
                        icon(BitmapDescriptorFactory.defaultMarker(35)));
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    private void requestPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        this.fine_location = (permissionCheck == PackageManager.PERMISSION_GRANTED);
        if (this.fine_location) return;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                FINE_LOCATION_REQUEST);

        if (mMap != null) {
            mMap.setMyLocationEnabled(this.fine_location);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authListener);
    }

    public void setNewVaccine(View view) {
        Intent intent = new Intent(this, SetNewVaccineActivity.class);
        startActivity(intent);
    }
}