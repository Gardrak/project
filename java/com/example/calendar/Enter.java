package com.example.calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileOutputStream;
import java.io.IOException;

public class Enter extends AppCompatActivity {
    private EditText edit;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String REGISTERED_KEY = "registered";
    private static final String USERNAME_KEY = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Проверяем, было ли уже введено имя
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean registered = settings.getBoolean(REGISTERED_KEY, false);

        if (registered) {
            // Пользователь уже ввел имя, переходим на основной экран
            goToMainActivity();
        } else {
            // Пользователь еще не вводил имя, показываем экран с вводом
            setContentView(R.layout.activity_enter);
            edit = findViewById(R.id.editTextPersonName);
        }
    }

    public void write(View view) {
        String name = edit.getText().toString();
        try {
            FileOutputStream fileOutput = openFileOutput("userName.txt", MODE_PRIVATE);
            fileOutput.write(name.getBytes());
            fileOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Пользователь зарегистрирован, сохраняем состояние в SharedPreferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(REGISTERED_KEY, true);
        editor.putString(USERNAME_KEY, name);
        editor.apply();

        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Закрываем текущую активность
    }
}