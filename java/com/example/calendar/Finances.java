package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Finances extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);
    }

    public void goToSettings(View v) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
    public void goToCalendar (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}