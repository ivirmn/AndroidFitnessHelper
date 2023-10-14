package ru.vsu.fitnesshelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountActivity extends AppCompatActivity {

    private TextView textViewUserEmail;
    private TextView textViewUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        textViewUserID = findViewById(R.id.textViewUserID);



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            String userEmail = user.getEmail();
            textViewUserEmail.setText("Email: " + userEmail);


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {

                        });
                    };
        }


        };


