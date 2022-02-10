package br.edu.ifpe.tads.pdm.davydadriel.pande1000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
//        DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("users");
//
//        System.out.println("Verificando objeto");
//        System.out.println(drUsers.child(mAuth.getCurrentUser().getUid()));

        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
        FirebaseUser fbUser = mAuth.getCurrentUser();
        drUser = fbDB.getReference("users/" + fbUser.getUid());
        drChat = fbDB.getReference("vaccines");

        EditText vaccine_name = findViewById(R.id.vaccine_name);

        drChat.push().setValue(new Vaccine(fbUser.getUid(), vaccine_name.getText().toString()));

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}