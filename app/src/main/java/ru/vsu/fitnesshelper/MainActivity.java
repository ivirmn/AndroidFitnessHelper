package ru.vsu.fitnesshelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView textViewInfo;
    private Button buttonGoToCalorieCalculation;
    private Button buttonEditHeightWeight;
    private Button buttonSearchUser;
    private TextView adviceTextView;
    String[] adviceArray = {
            "Пейте 2 литра воды в день.",
            "Летом не забывайте принимать солнечные ванны: лучшее время с 8 до 11 утра и с 3 дня до 6 вечера.",
            "Ежедневно делайте утреннюю гимнастику.",
            "Изучите новый рецепт и приготовьте его сегодня.",
            "Прогуливайтесь на свежем воздухе каждый день.",
            "Занимайтесь йогой для улучшения гибкости и силы.",
            "Планируйте свои задачи на завтра перед сном.",
            "Отдыхайте 10-15 минут каждый час, чтобы поддерживать продуктивность.",
            "Изучите новый навык: например, играйте на музыкальном инструменте.",
            "Слушайте музыку, которая поднимает вам настроение.",
            "Посвятите время чтению хорошей книги.",
            "Устройте день без социальных медиа и телефона.",
            "Занимайтесь активным отдыхом на свежем воздухе: велосипед, прогулки, бег.",
            "Изучите иностранный язык.",
            "Летом не забывайте про солнечные ванны! В ясный день не рекомендуется загорать с 11 до 16ч, а также следует подумать о солнцезащите",
            "Занимайтесь медитацией для улучшения психического здоровья.",
            "Учитеся рисовать или рисуйте что-то креативное.",
            "Планируйте свой бюджет и экономьте на что-то важное для вас.",
            "Сделайте что-то доброе для кого-то без ожидания взамен.",
            "Соблюдайте режим сна: 7-9 часов сна в ночь.",
            "Участвуйте в благотворительных акциях.",
            "Примите ванну с ароматическими маслами для расслабления.",
            "Посадите растение в своем доме и следите за его ростом.",
            "Изучите основы кулинарии и готовьте вкусные блюда самостоятельно.",
            "Практикуйте глубокое дыхание для уменьшения стресса.",
            "Проведите выходной день без каких-либо планов и просто отдыхайте.",
            "Запишите свои мысли и чувства в дневнике.",
            "Смотрите вдохновляющий фильм или документальный фильм.",
            "Проведите вечер с близкими друзьями или семьей.",
            "Займитесь спортом, который вам нравится: плавание, теннис, фитнес и т. д.",
            "Путешествуйте в новые места и знакомьтесь с новыми культурами.",
            "Проведите время на природе: пикник, кемпинг, горный поход.",
            "Занимайтесь творческой деятельностью: рисуйте, пишите стихи, лепите из глины.",
            "Проведите выходной день без использования техники.",
            "Планируйте свои цели и стремитесь к их достижению.",
            "Примите участие в волонтёрской деятельности.",
            "Составьте свой плейлист с мотивирующей музыкой"
    };

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewInfo = findViewById(R.id.textViewInfo);
        buttonGoToCalorieCalculation = findViewById(R.id.buttonGoToCalorieCalculation);
        buttonEditHeightWeight = findViewById(R.id.buttonEditHeightWeight);
        buttonSearchUser = findViewById(R.id.buttonSearchUser);
        adviceTextView = findViewById(R.id.adviceTextView);


        displayRandomAdvice();

        SharedPreferences prefs = getSharedPreferences("FitnessAppPrefs", MODE_PRIVATE);
        String heightText = prefs.getString("height", "Пусто");
        String weightText = prefs.getString("weight", "Пусто");
        float bmi = prefs.getFloat("bmi", 0.0f); // Получение ИМТ из SharedPreferences
        String bmiStatus = prefs.getString("bmi_category", "Неизвестно"); // Получение состояния ИМТ из SharedPreferences


        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String timeOfDay = getTimeOfDay();


        String infoText = timeOfDay + "\nВаш рост " + heightText + " см, вес " + weightText + " кг, ИМТ " + bmi + " (" + bmiStatus + "), дата " + dateTime;
        textViewInfo.setText(infoText);

        buttonGoToCalorieCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent(MainActivity.this, CalorieCalculationActivity.class);
                startActivity(intent);
            }
        });

        buttonEditHeightWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent(MainActivity.this, InputDataActivity.class);
                startActivity(intent);
            }
        });
        Button buttonMyGoals = findViewById(R.id.buttonMyGoals);
        buttonMyGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                // Переход на экран "Мои цели" при нажатии на кнопку
                Intent intent = new Intent(MainActivity.this, ActivitiesListActivity.class);
                startActivity(intent);
            }
        });
        Button buttonShare = findViewById(R.id.buttonShare);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                shareTextWithIMT(); // Вызов метода для отправки сообщения
            }
        });
        Button buttonLogin = findViewById(R.id.buttonLogin); // buttonRegister - это ID кнопки на экране
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    // Если пользователь вошел, перейдите в AccountActivity
                    Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(intent);
                } else {
                    // Если пользователь не вошел, перейдите в LoginActivity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        buttonSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                Intent intent = new Intent(MainActivity.this, SearchUserActivity.class);
                startActivity(intent);
            }
        });


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Button buttonLogout = findViewById(R.id.buttonLogout);
        if (user != null) {
            // Если пользователь вошел, покажите кнопку выхода
            buttonLogout.setVisibility(View.VISIBLE);
        } else {
            // Если пользователь не вошел, скройте кнопку выхода
            buttonLogout.setVisibility(View.GONE);
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();


                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void displayRandomAdvice() {
        Random random = new Random();
        int randomIndex = random.nextInt(adviceArray.length);
        String randomAdvice = adviceArray[randomIndex];
        adviceTextView.setText("Совет на сегодня: " + randomAdvice);
    }
    private String getTimeOfDay() {

        String hour = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        if (hour != null) {
            int hourOfDay = Integer.parseInt(hour);
            if (hourOfDay >= 0 && hourOfDay < 6) {
                return "Доброй ночи";
            } else if (hourOfDay >= 6 && hourOfDay < 12) {
                return "Доброе утро";
            } else if (hourOfDay >= 12 && hourOfDay < 18) {
                return "Добрый день";
            } else {
                return "Добрый вечер";
            }
        }
        return "";
    }

    private void setBmiColor(float bmi, TextView textView) {
        int colorResId;

        if (bmi < 16.0 || bmi >= 35.0) {

            colorResId = R.color.red;
        } else if (bmi >= 16.0 && bmi < 18.5 || bmi >= 30.0 && bmi < 35.0) {

            colorResId = R.color.orange;
        } else {

            colorResId = R.color.black;
        }


        int textColor = getResources().getColor(colorResId);
        textView.setTextColor(textColor);
    }
    private void shareTextWithIMT() {

        SharedPreferences prefs = getSharedPreferences("FitnessAppPrefs", MODE_PRIVATE);
        float bmi = prefs.getFloat("bmi", 0.0f);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        String message;

        if (user != null) {
            String userId = user.getUid();
            message = "Я слежу за своим здоровьем и уже зарегистрировался в FitnessHelper! Мой ИМТ: " + bmi +
                    ". Мой ID: " + userId + " Скачай и ты!";
        } else {
            message = "Я слежу за своим здоровьем и уже скачал FitnessHelper! Мой ИМТ: " + bmi +
                    ". Скачай и ты!";
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");


        startActivity(Intent.createChooser(sendIntent, "Поделиться с помощью"));
    }



}

