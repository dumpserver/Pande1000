package br.edu.ifpe.tads.pdm.davydadriel.pande1000;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.Vaccine;

public class SetNewVaccineActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuthListener authListener;

    DatabaseReference drUser;
    DatabaseReference drChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_vaccine);

        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authListener);
    }

    public void addNewVaccine(View view) {
        FusedLocationProviderClient fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(this, location -> {
            if(location!=null) {
                this.sendToBase(
                    view,
                    location.getLatitude(),
                    location.getLongitude()
                );
            }
        });
    }

    private void sendToBase(View view, Double latitude, Double longitude){
        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
        FirebaseUser fbUser = mAuth.getCurrentUser();
        drUser = fbDB.getReference("users/" + fbUser.getUid());
        drChat = fbDB.getReference("vaccines");
        EditText vaccine_name = findViewById(R.id.vaccine_name);

        // Get current date.
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        drChat.push().setValue(new Vaccine(
            fbUser.getUid(),
            vaccine_name.getText().toString(),
            currentDate,
            latitude,
            longitude));

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }
}