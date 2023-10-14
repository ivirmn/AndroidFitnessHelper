package ru.vsu.fitnesshelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class CalorieCalculationActivity extends AppCompatActivity {

    private RadioGroup radioGroupActivity;
    private Button buttonCalculateCalories;
    private TextView textViewCaloriesResult;
    private Button buttonBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_calculation);

        radioGroupActivity = findViewById(R.id.radioGroupActivity);
        buttonCalculateCalories = findViewById(R.id.buttonCalculateCalories);
        textViewCaloriesResult = findViewById(R.id.textViewCaloriesResult);
        buttonBackToMain = findViewById(R.id.buttonBackToMain);

        buttonCalculateCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRadioButtonId = radioGroupActivity.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

                if (selectedRadioButton != null) {
                    String selectedActivity = selectedRadioButton.getText().toString();

                    String heightStr = getHeightFromSharedPreferences();
                    String weightStr = getWeightFromSharedPreferences();

                    double height = Double.parseDouble(heightStr);
                    double weight = Double.parseDouble(weightStr);

                    double bmi = getBmiFromSharedPreferences();
                    boolean isBodybuilder = getBodybuilderStatusFromSharedPreferences();

                    double adjustedBmi = adjustBmi(height, weight, bmi, isBodybuilder);

                    double calculatedCalories = calculateCalories(adjustedBmi, bmi, selectedActivity);
                    double calculatedWaterIntake = calculateWaterIntake(weight, adjustedBmi, bmi, selectedRadioButtonId);

                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    String caloriesResultText = "Примерное количество калорий на сегодня: " + decimalFormat.format(calculatedCalories) + " ккал";
                    String waterIntakeText = "Примерное количество воды на сегодня: " + decimalFormat.format(calculatedWaterIntake) + " литров";
                    textViewCaloriesResult.setText(caloriesResultText + "\n" + waterIntakeText);

                    saveCaloriesResult(calculatedCalories);
                }
            }
        });

        buttonBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String getHeightFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("FitnessAppPrefs", MODE_PRIVATE);
        return prefs.getString("height", "0.0");
    }

    private String getWeightFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("FitnessAppPrefs", MODE_PRIVATE);
        return prefs.getString("weight", "0.0");
    }

    private double getBmiFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("FitnessAppPrefs", MODE_PRIVATE);
        return prefs.getFloat("bmi", 0.0f);
    }

    private boolean getBodybuilderStatusFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("FitnessAppPrefs", MODE_PRIVATE);
        return prefs.getBoolean("is_bodybuilder", false);
    }

    private double adjustBmi(double height, double weight, double bmi, boolean isBodybuilder) {
        double adjustedBmi = bmi;

        double activityCoefficient = getActivityCoefficient(isBodybuilder);
        adjustedBmi *= activityCoefficient;

        double baseHeight = 175.0;
        double baseBmi = 24.5;

        double heightChangeFactor = Math.pow(1.015, Math.abs(baseHeight - height));  // Используем значение больше 1, но берем модуль разницы
        double bmiChangeFactor = Math.pow(0.95, Math.abs(bmi - baseBmi));  // Используем значение больше 1, но берем модуль разницы

        if (height < baseHeight) {
            adjustedBmi /= heightChangeFactor;
        } else {
            adjustedBmi *= heightChangeFactor;
        }

        if (bmi < baseBmi) {
            adjustedBmi /= bmiChangeFactor;
        } else {
            adjustedBmi *= bmiChangeFactor;
        }

        return adjustedBmi;
    }

    private double getActivityCoefficient(boolean isBodybuilder) {
        if (isBodybuilder) {
            return 1.2;
        }

        int selectedRadioButtonId = radioGroupActivity.getCheckedRadioButtonId();
        switch (selectedRadioButtonId) {
            case R.id.radioButtonSedentary:
                return 0.75;
            case R.id.radioButtonLight:
                return 0.9;
            case R.id.radioButtonModerate:
                return 1;
            case R.id.radioButtonActive:
                return 1.1;
            case R.id.radioButtonVeryActive:
                return 1.25;
            default:
                return 1.0;
        }
    }

    private double calculateCalories(double adjustedBmi, double bmi, String activity) {
        double baseCalories = 2500.0;
        double calculatedCalories = adjustedBmi * baseCalories / bmi;

        return calculatedCalories;
    }

    private double calculateWaterIntake(double weight, double adjustedBmi, double bmi, int selectedRadioButtonId) {
        double waterCoefficient;
        switch (selectedRadioButtonId) {
            case R.id.radioButtonSedentary:
                waterCoefficient = 1;
                break;
            case R.id.radioButtonLight:
                waterCoefficient = 1;
                break;
            case R.id.radioButtonModerate:
                waterCoefficient = 1;
                break;
            case R.id.radioButtonActive:
                waterCoefficient = 1.1;
                break;
            case R.id.radioButtonVeryActive:
                waterCoefficient = 1.25;
                break;
            default:
                waterCoefficient = 1.0;
                break;
        }

        double baseWaterIntake = 2.0;
        double calculatedWaterIntake = (adjustedBmi / bmi) * waterCoefficient * baseWaterIntake;

        return calculatedWaterIntake;
    }

    private void saveCaloriesResult(double calories) {
        SharedPreferences.Editor editor = getSharedPreferences("FitnessAppPrefs", MODE_PRIVATE).edit();
        editor.putFloat("calories", (float) calories);
        editor.apply();
    }
}
