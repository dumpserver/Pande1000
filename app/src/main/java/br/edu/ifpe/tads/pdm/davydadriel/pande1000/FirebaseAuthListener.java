package br.edu.ifpe.tads.pdm.davydadriel.pande1000;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthListener implements FirebaseAuth.AuthStateListener {
    private final Activity activity;
    public FirebaseAuthListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Intent intent = null;

        if ((user == null) && (activity instanceof SetNewVaccineActivity || activity instanceof MyVaccines)) {
            intent = new Intent(activity, SignInActivity.class);
        }

        if ((user != null) && (activity instanceof SignUpActivity || activity instanceof  SignInActivity)) {
            intent = new Intent(activity, SetNewVaccineActivity.class);
        }

        if (intent != null) {
            activity.startActivity(intent);
            activity.finish();
        }
    }
}