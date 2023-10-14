package ru.vsu.fitnesshelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth; // Импорт Firebase Auth
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;
import android.util.Log; // Импорт Log
import java.util.HashMap;
import java.util.Map;

public class InputDataActivity extends AppCompatActivity {

    private EditText editTextHeight;
    private EditText editTextWeight;
    private Button buttonSubmit;


    private static final String PREFS_NAME = "FitnessAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        buttonSubmit = findViewById(R.id.buttonSubmit);


        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editTextHeight.setText(prefs.getString("height", ""));
        editTextWeight.setText(prefs.getString("weight", ""));

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String heightText = editTextHeight.getText().toString();
                String weightText = editTextWeight.getText().toString();

                if (heightText.isEmpty()) {
                    editTextHeight.setError("Введите рост");
                    return;
                }

                if (weightText.isEmpty()) {
                    editTextWeight.setError("Введите вес");
                    return;
                }

                float height = Float.parseFloat(heightText);
                float weight = Float.parseFloat(weightText);
                float bmi = calculateBMI(height, weight);
                String bmiCategory = getBmiCategory(bmi);


                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("height", heightText);
                editor.putString("weight", weightText);
                editor.putFloat("bmi", bmi);
                editor.putString("bmi_category", bmiCategory);
                editor.apply();

                // Сохранение данных в Firebase Firestore
                saveUserDataToFirestore(height, weight);

                // Переход на главный экран MainActivity
                Intent intent = new Intent(InputDataActivity.this, MainActivity.class);
                startActivity(intent);
            }

            private float calculateBMI(float height, float weight) {
                float heightInMeters = height / 100.0f;
                return weight / (heightInMeters * heightInMeters);
            }

            private String getBmiCategory(float bmi) {
                if (bmi < 16) {
                    return "Анорексия";
                } else if (bmi >= 16 && bmi < 18.5) {
                    return "Недовес";
                } else if (bmi >= 18.5 && bmi < 25.0) {
                    return "Нормальный вес";
                } else if (bmi >= 25.0 && bmi < 30.0) {
                    return "Избыточный вес";
                } else if (bmi >= 30.0 && bmi < 35.0) {
                    return "Ожирение I степени";
                } else if (bmi >= 35.0 && bmi < 40.0) {
                    return "Ожирение II степени";
                } else {
                    return "Ожирение III степени";
                }
            }

            private void saveUserDataToFirestore(float height, float weight) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();


                    DocumentReference userDocRef = db.collection("users").document(userId);


                    Map<String, Object> userData = new HashMap<>();
                    userData.put("height", height);
                    userData.put("weight", weight);


                    userDocRef.set(userData)
                            .addOnSuccessListener(aVoid -> {
                                // Данные успешно добавлены
                                Log.d("Firestore", "Данные успешно добавлены в Firestore");
                            })
                            .addOnFailureListener(e -> {
                                // Ошибка при добавлении данных
                                Log.w("Firestore", "Ошибка при добавлении данных в Firestore", e);
                            });
                }
            }
        });
    }
}
