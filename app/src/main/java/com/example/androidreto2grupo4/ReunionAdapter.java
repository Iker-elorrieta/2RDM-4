package com.example.androidreto2grupo4;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ReunionAdapter extends RecyclerView.Adapter<ReunionAdapter.ReunionViewHolder> {
    private ArrayList<ArrayList<String>> listaReuniones;
    private final Context context;
    private final OnReunionActionListener listener;

    public interface OnReunionActionListener {
        void onAceptar(int idReunion);
        void onRechazar(int idReunion);
    }

    public ReunionAdapter(Context context, ArrayList<ArrayList<String>> listaReuniones, OnReunionActionListener listener) {
        this.context = context;
        this.listaReuniones = listaReuniones;
        this.listener = listener;
    }

    // Method to update the list of meetings
    public void setReuniones(ArrayList<ArrayList<String>> nuevasReuniones) {
        this.listaReuniones = nuevasReuniones;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReunionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reunion, parent, false);
        return new ReunionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReunionViewHolder holder, int position) {
        ArrayList<String> reunion = listaReuniones.get(position);
        holder.txtNombreReunion.setText(reunion.get(1));

        // Set up the buttons for accepting and rejecting
        holder.btnAceptar.setOnClickListener(v -> listener.onAceptar(Integer.parseInt( reunion.get(0))));
        holder.btnRechazar.setOnClickListener(v -> listener.onRechazar(Integer.parseInt( reunion.get(0))));
    }

    @Override
    public int getItemCount() {
        return listaReuniones.size();
    }

    // ViewHolder class to hold the views
    public static class ReunionViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreReunion;
        Button btnAceptar, btnRechazar;

        public ReunionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreReunion = itemView.findViewById(R.id.txtNombreReunion);
            btnAceptar = itemView.findViewById(R.id.btnAceptar);
            btnRechazar = itemView.findViewById(R.id.btnRechazar);
        }
    }
}