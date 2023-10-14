package ru.vsu.fitnesshelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.util.ArrayList;
import android.content.SharedPreferences;

public class BmiInfoActivity extends AppCompatActivity {

    private TextView textViewBmiInfo;
    private ListView listViewBmiCategories;
    private TextView textViewBmiRange;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_info);

        textViewBmiInfo = findViewById(R.id.textViewBmiInfo);
        listViewBmiCategories = findViewById(R.id.listViewBmiCategories);
        textViewBmiRange = findViewById(R.id.textViewBmiRange);
        buttonBack = findViewById(R.id.buttonBack);


        SharedPreferences prefs = getSharedPreferences("FitnessAppPrefs", MODE_PRIVATE);
        String heightText = prefs.getString("height", "");
        String weightText = prefs.getString("weight", "");
        double height = Double.parseDouble(heightText);
        double weight = Double.parseDouble(weightText);
        double bmi = calculateBMI(height, weight);


        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String bmiInfoText = "Рост: " + height + " см\nВес: " + weight + " кг\nИМТ: " + decimalFormat.format(bmi);
        textViewBmiInfo.setText(bmiInfoText);


        ArrayList<String> bmiCategories = new ArrayList<>();
        bmiCategories.add("ИМТ менее 18.5: Недовес");
        bmiCategories.add("ИМТ 18.5 - 24.9: Нормальный вес");
        bmiCategories.add("ИМТ 25.0 - 29.9: Избыточный вес");
        bmiCategories.add("ИМТ 30.0 - 34.9: Ожирение I степени");
        bmiCategories.add("ИМТ 35.0 - 39.9: Ожирение II степени");
        bmiCategories.add("ИМТ 40.0 и более: Ожирение III степени");

        ArrayAdapter<String> bmiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bmiCategories);
        listViewBmiCategories.setAdapter(bmiAdapter);


        String bmiRangeText = getBmiRange(bmi);
        textViewBmiRange.setText("Ваш ИМТ попадает в диапазон: " + bmiRangeText);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Закрываем текущую активность и возвращаемся к предыдущей
            }
        });
    }

    private double calculateBMI(double height, double weight) {
        double heightInMeters = height / 100.0; // Перевод роста в метры
        return weight / (heightInMeters * heightInMeters);
    }

    private String getBmiRange(double bmi) {
        if (bmi < 18.5) {
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
}
