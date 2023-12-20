package com.example.calendar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private  List<Note> noteList;
    private  Context context;

    public void removeNoteAtPosition(int position) {
        noteList.remove(position);
        notifyItemRemoved(position);
    }

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList =  noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList.clear();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        List<Note> updatedNoteList = databaseHelper.getAllNotesForDate("selected date");
        this.noteList.addAll(updatedNoteList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.bind(note);
        holder.timeTextView.setOnClickListener(v -> {
            openTimePicker(note, holder.timeTextView);
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(holder.getAdapterPosition());
                    return true;
                }
                return false;
            }
        });
    }

    private void openTimePicker(Note note, TextView timeTextView) {
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            String selectedTime = hourOfDay + ":" + minute;
            note.setTime(selectedTime);
            timeTextView.setText(selectedTime);
        };

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, timeSetListener, hour, minute, true);
        timePickerDialog.show();
    }



    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public int getNoteIdAtPosition(int position) {
        if (position >= 0 && position < noteList.size()) {
            return noteList.get(position).getId();
        } else {
            return -1; // РёР»Рё РґСЂСѓРіРѕРµ Р·РЅР°С‡РµРЅРёРµ, РєРѕС‚РѕСЂРѕРµ СѓРєР°Р·С‹РІР°РµС‚ РЅР° РѕС€РёР±РєСѓ
        }
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView noteTextView;
        private TextView timeTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.noteTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }

        public void bind(Note note) {
            noteTextView.setText(note.getNoteText());
            timeTextView.setText(note.getTime());
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }


}


