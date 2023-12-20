package com.example.calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;


public class NewActivity extends AppCompatActivity {

    private EditText noteEditText;
    private Button saveButton;
    private ImageButton buttonMicrophone;
    private NoteAdapter noteAdapter;
    private RecyclerView recyclerView;
    private String selectedDate;
    private DatabaseHelper databaseHelper;
    // Идентификатор уведомления
    protected static final int NOTIFY_ID = 101;

    // Идентификатор канала
    protected static final String CHANNEL_ID = "Cat_channel";
    private static final int RECOGNIZER_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);
        buttonMicrophone = findViewById(R.id.buttonMicrophone);
        recyclerView = findViewById(R.id.recyclerView);


        databaseHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(v -> showTimePickerDialog());

        selectedDate = getIntent().getStringExtra("selectedDate");
        setTitle("Дела на " + selectedDate);

        List<Note> noteList = databaseHelper.getAllNotesForDate(selectedDate); // Используйте переданную дату
        noteAdapter = new NoteAdapter(this, noteList);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Назовите событие...");
                startActivityForResult(intent, RECOGNIZER_RESULT);

            }
        });

    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + minute;
                        saveNoteToDatabase(selectedTime);
                    }
                }, hour, minute, true);

        // Отображение диалога выбора времени
        timePickerDialog.show();
    }

    private void saveNoteToDatabase(String selectedTime) {
        Calendar notificationTime = Calendar.getInstance();
        String note = noteEditText.getText().toString().trim();
        String title = "Title";
        String selectedDate = getIntent().getStringExtra("selectedDate");

        long result = databaseHelper.addNoteWithTime(note, title, selectedTime, selectedDate);

        if (result != -1) {
            Toast.makeText(NewActivity.this, "Заметка сохранена", Toast.LENGTH_SHORT).show();
            List<Note> updatedNoteList = databaseHelper.getAllNotesForDate(selectedDate);
            noteAdapter.setNoteList(updatedNoteList);
            finish();
        } else {
            Toast.makeText(NewActivity.this, "Ошибка при сохранении заметки", Toast.LENGTH_SHORT).show();
        }
        // Разбиваем строку времени на часы и минуты
        String[] timeParts = selectedTime.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        long currentTime = System.currentTimeMillis();
        long fifteenMinutesAgo = currentTime - (15 * 60 * 1000); // Вычитаем 15 минут от текущего времени
        notificationTime.setTimeInMillis(fifteenMinutesAgo);

        // Создаем и планируем уведомление
        scheduleNotification(notificationTime.getTimeInMillis());
    }

    private void scheduleNotification(long time) {
        // Создаем интент для отображения уведомления
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, getNotification());

        // Создаем пендинг-интент
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Получаем менеджер уведомлений
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Планируем уведомление
        alarmManager.set(AlarmManager.RTC, time, pendingIntent);
    }

    private Notification getNotification() {
        // Создаем уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Напоминание")
                .setContentText("Пора покормить кота")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Возвращаем уведомление
        return builder.build();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        String text = matches.get(0).toString();
        noteEditText.setText(text);

        super.onActivityResult(requestCode, resultCode, data);
    }

}