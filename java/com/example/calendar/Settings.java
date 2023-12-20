package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Settings extends AppCompatActivity {
    private EditText txtShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        txtShow = (EditText) findViewById(R.id.editTextPersonName);

        try {
            FileInputStream fileInput = openFileInput("userName.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            StringBuffer strBuffer = new StringBuffer();
            String Lines;
            while ((Lines = buffer.readLine()) != null) {
                strBuffer.append(Lines);

            }
            txtShow.setText(strBuffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goToCalendar (View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToFinance(View v) {
        Intent intent = new Intent(this, Finances.class);
        startActivity(intent);
    }

}