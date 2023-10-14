package ru.vsu.fitnesshelper;
import java.util.HashMap;
import java.util.Random;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegistrationActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Регистрация пользователя с использованием Firebase
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(RegistrationActivity.this, "Регистрация выполнена успешно", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Закрываем RegistrationActivity
                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                                Toast.makeText(RegistrationActivity.this, "Пользователь с таким email уже зарегистрирован", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(RegistrationActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        Intent mainIntent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }


                };


