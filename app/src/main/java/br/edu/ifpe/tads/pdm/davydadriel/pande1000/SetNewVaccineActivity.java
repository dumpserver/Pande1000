package br.edu.ifpe.tads.pdm.davydadriel.pande1000;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.vaccine_spinner);
        //create a list of items for the spinner.
        String[] items = new String[]{
            "Pfizer",
            "CoronaVac",
            "Johnson",
            "AstraZeneca",
        };
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
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
//        EditText vaccine_name = findViewById(R.id.vaccine_name);
        Spinner spinner = (Spinner) findViewById(R.id.vaccine_spinner);
        String vaccine_name = spinner.getSelectedItem().toString();

        // Get current date.
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        drChat.push().setValue(new Vaccine(
            fbUser.getUid(),
//            vaccine_name.getText().toString(),
            vaccine_name,
            currentDate,
            latitude,
            longitude));

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }
}