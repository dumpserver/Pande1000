package br.edu.ifpe.tads.pdm.davydadriel.pande1000;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.User;

public class SignUpActivity extends AppCompatActivity {


    EditText edEmail;
    EditText edPassword;
    EditText edName;

    FirebaseAuth mAuth;
    FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.edEmail = findViewById(R.id.edit_email);
        this.edPassword = findViewById(R.id.edit_password);
        this.edName = findViewById(R.id.edit_name);

        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);

    }

    public void buttonSignUpClick(View view) {
        final String name = edName.getText().toString();
        final String email = edEmail.getText().toString();
        final String password = edPassword.getText().toString();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    String msg = task.isSuccessful() ? "SIGN UP OK!": "SIGN UP ERROR!";
                    Toast.makeText(SignUpActivity.this, msg,
                            Toast.LENGTH_SHORT).show();

                    if (task.isSuccessful()) {
                        User tempUser = new User(name, email);
                        DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("users");

                        drUsers.child(mAuth.getCurrentUser().getUid()).setValue(tempUser);
                    }
                });
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