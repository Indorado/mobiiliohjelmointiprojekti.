package com.example.mobiiliohjelmointiryhmaq;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInFragment extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Sijaintitiedot pyydetään vain ensimmäisen kerran sovelluksen käynnistämisen yhteydessä
        accessLocation();

        if (user != null) {
            startActivity(new Intent(LogInFragment.this,FirstFragment.class));
            Toast.makeText(LogInFragment.this, "Logged in", Toast.LENGTH_SHORT).show();
        } else {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = loginEmail.getText().toString();
                    String pass = loginPassword.getText().toString();
                    if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        if (!pass.isEmpty()) {
                            auth.signInWithEmailAndPassword(email, pass)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Toast.makeText(LogInFragment.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LogInFragment.this, FirstFragment.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LogInFragment.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            loginPassword.setError("Password cannot be empty");
                        }
                    } else if(email.isEmpty()) {
                        loginEmail.setError("Email cannot be empty");
                    } else {
                        loginEmail.setError("Please enter valid email");
                    }
                }
            });
        }

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInFragment.this,SignUpFragment.class));
            }
        });
    }

    // Sijaintitiedot
    public void accessLocation() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                        .RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        fineLocationGranted = result.getOrDefault(
                                android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                    }
                    Boolean coarseLocationGranted = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        coarseLocationGranted = result.getOrDefault(
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,false);
                    }
                    if (fineLocationGranted != null && fineLocationGranted) {
                        // Precise location access granted.
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        // Only approximate location access granted.
                    } else {
                        // No location access granted.
                    }
                });

        locationPermissionRequest.launch(new String[] {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }
}
