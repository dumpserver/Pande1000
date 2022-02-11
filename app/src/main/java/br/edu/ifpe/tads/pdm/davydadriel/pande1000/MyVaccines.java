package br.edu.ifpe.tads.pdm.davydadriel.pande1000;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import model.Vaccine;

public class MyVaccines extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuthListener authListener;
    DatabaseReference drVaccines;
    List<String> vaccines = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vaccines);

        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);

        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();

        drVaccines = fbDB.getReference("vaccines");

        FirebaseUser fbUser = mAuth.getCurrentUser();

        drVaccines.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // todo: refactory for only set.
                Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);
                if(vaccine.getUserId().equals(fbUser.getUid())){
                    MyVaccines.this.vaccines.add(vaccine.getName()+" Em: "+vaccine.getDate());
                    MyVaccines.this.mountList();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // todo: refactory for only set.
                Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);
                if(vaccine.getUserId().equals(fbUser.getUid())){
                    MyVaccines.this.vaccines.add(vaccine.getName()+" Em: "+vaccine.getDate());
                    MyVaccines.this.mountList();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    private void mountList() {
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, this.vaccines)
        );
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
}