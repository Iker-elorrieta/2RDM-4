package com.example.androidreto2grupo4.Day;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidreto2grupo4.R;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.HourViewHolder> {

    private ArrayList<String> hours;

    public CalendarAdapter(ArrayList<String> hours) {
        this.hours = hours;
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hour, parent, false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        holder.hourLabel.setText(hours.get(position));
    }

    @Override
    public int getItemCount() {
        return hours.size();
    }

    public static class HourViewHolder extends RecyclerView.ViewHolder {
        TextView hourLabel;

        public HourViewHolder(@NonNull View itemView) {
            super(itemView);
            hourLabel = itemView.findViewById(R.id.hour_label);
        }
    }
}