package com.example.androidreto2grupo4.Day;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidreto2grupo4.R;

import java.util.ArrayList;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DayViewHolder> {

    private ArrayList<Day> days;

    public DaysAdapter(ArrayList<Day> days) {
        this.days = days;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Day day = days.get(position);
        holder.dayLetter.setText(day.getLetter());
        holder.dayNumber.setText(day.getNumber());
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView dayLetter, dayNumber;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dayLetter = itemView.findViewById(R.id.day_letter);
            dayNumber = itemView.findViewById(R.id.day_number);
        }
    }
}