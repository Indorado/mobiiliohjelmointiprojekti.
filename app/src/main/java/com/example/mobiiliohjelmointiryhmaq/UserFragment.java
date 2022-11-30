package com.example.mobiiliohjelmointiryhmaq;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class UserFragment  extends AppCompatActivity {
    private Button edit;
    private EditText town;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);
        town = findViewById(R.id.idUserTown);
        Button edit = findViewById(R.id.edit_button);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTown();
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void updateTown() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userTown = town.getText().toString();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userTown)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User location updated.");

                        }
                    }
                });
        Intent intent = new Intent(this, FirstFragment.class);
        startActivity(intent);
    }
}
