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

        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);

        requestPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();

        // Include vaccines in map.
        drVaccines = fbDB.getReference("vaccines");

        drVaccines.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);

                LatLng vacineLatLng = new LatLng(vaccine.getLatitude(), vaccine.getLongitude());
                mMap.addMarker(
                        new MarkerOptions().
                                position(vacineLatLng).
                                title(vaccine.getName()).
                                icon(BitmapDescriptorFactory.defaultMarker(125)));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);

                LatLng vacineLatLng = new LatLng(vaccine.getLatitude(), vaccine.getLongitude());
                mMap.addMarker(
                        new MarkerOptions().
                            position(vacineLatLng).
                            title(vaccine.getName()).
                            icon(BitmapDescriptorFactory.defaultMarker(125)));
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
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = (grantResults.length > 0) &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED);

        this.fine_location = (requestCode == FINE_LOCATION_REQUEST) && granted;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);

        // Check Current User
        if(mAuth.getCurrentUser() != null){
            // Enable logout button.
            findViewById(R.id.button_logout).setEnabled(true);
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

    public void logoutUser(View view) {
        mAuth.signOut();
        findViewById(R.id.button_logout).setEnabled(false);
    }
}