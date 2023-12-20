package com.example.calendar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes_database";
    private static final String TABLE_NOTES = "notes";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public long addNoteWithTime(String noteText, String title, String time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_CONTENT, noteText);
        values.put(KEY_TIME, time);
        values.put(KEY_DATE, date);

        long result = db.insert(TABLE_NOTES, null, values);
        db.close();
        return result;


}



    @SuppressLint("Range")
    public List<Note> getAllNotesForDate(String selectedDate) {
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES + " WHERE " + KEY_DATE + " = ?", new String[]{selectedDate});

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                note.setNoteText(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                note.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return noteList;
    }

    public void deleteNoteById(int noteId) {
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(TABLE_NOTES,KEY_ID+"=?",new String[]{String.valueOf(noteId)});
        db.close();
    }
}




