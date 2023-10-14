package ru.vsu.fitnesshelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SearchUserActivity extends AppCompatActivity {

    private EditText editTextUserId;
    private Button buttonSearch;
    private TextView textViewResult;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        editTextUserId = findViewById(R.id.editTextUserId);
        buttonSearch = findViewById(R.id.buttonSearch);
        textViewResult = findViewById(R.id.textViewResult);
        buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = editTextUserId.getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users")
                        .document(userId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        String userId = document.getId();
                                        float height = document.getDouble("height").floatValue();
                                        float weight = document.getDouble("weight").floatValue();
                                        float bmi = calculateBMI(height, weight);


                                        String resultText = "ID: " + userId + "\nРост: " + height + " см\nВес: " + weight + " кг\nИМТ: " + bmi;
                                        textViewResult.setText(resultText);
                                    } else {

                                        textViewResult.setText("Пользователь не найден");
                                    }
                                } else {

                                    textViewResult.setText("Ошибка при поиске. Повторите снова.");
                                }
                            }
                        });
            }
        });
    }

    private float calculateBMI(float height, float weight) {
        float heightInMeters = height / 100.0f;
        return weight / (heightInMeters * heightInMeters);
    }
}