package com.example.calendar;

public class Note {
    private int id;
    private String noteText;
    private String time;
    private String title;
    private String date;

    public Note() {
        // Пустой конструктор
    }


    public int getId(){
        return id;
    }
    public Note(int id, String noteText, String time) {
        this.id = id;
        this.noteText = noteText;
        this.time = time;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {this.date=date;}
}

