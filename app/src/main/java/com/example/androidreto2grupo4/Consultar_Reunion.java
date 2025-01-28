package com.example.androidreto2grupo4;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidreto2grupo4.Day.CalendarAdapter;
import com.example.androidreto2grupo4.Day.Day;
import com.example.androidreto2grupo4.Day.DaysAdapter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import modelo.Reuniones;

public class Consultar_Reunion extends AppCompatActivity {

    private RecyclerView daysRecyclerView, calendarRecyclerView;
    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private DataInputStream dis;
    private ObjectInputStream ois;
    private ArrayList<Reuniones> reuniones;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            dos = ServerConection.getInstance().getDataOutputStream();
            dis = ServerConection.getInstance().getDataInputStream();

            ois = ServerConection.getInstance().getObjectInputStream();

            oos = ServerConection.getInstance().getObjectOutputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_reunion);
        id = getIntent().getIntExtra("idLogin", -1);

        // Configurar RecyclerView para la barra de días
        daysRecyclerView = findViewById(R.id.days_recycler_view);
        daysRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<Day> days = getDays();
        DaysAdapter daysAdapter = new DaysAdapter(days);
        daysRecyclerView.setAdapter(daysAdapter);

        // Configurar RecyclerView para las horas
        calendarRecyclerView = findViewById(R.id.calendar_recycler_view);
        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> hours = getHours();
        CalendarAdapter calendarAdapter = new CalendarAdapter(hours);
        calendarRecyclerView.setAdapter(calendarAdapter);
        verReuniones (id);
    }

    // Método para generar días (Lunes a Domingo)
    private ArrayList<Day> getDays() {
        ArrayList<Day> days = new ArrayList<>();
        days.add(new Day("L", "13"));
        days.add(new Day("Ma", "14"));
        days.add(new Day("Mi", "15"));
        days.add(new Day("J", "16"));
        days.add(new Day("V", "17"));
        days.add(new Day("S", "18"));
        days.add(new Day("D", "19"));
        return days;
    }

    // Método para generar horas (8am a 8pm)
    private ArrayList<String> getHours() {
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 8; i <= 20; i++) {
            hours.add(String.valueOf(i));
        }
        return hours;
    }

    private void verReuniones(int idUsuario) {
        new Thread(() -> {
            try {
                Log.d("Consultar_Reunion", "Sending request to server for meetings");

                dos.writeInt(4);
                dos.flush();
                Log.d("Consultar_Reunion", "Sent request code: 4");

                dos.writeInt(id);
                dos.flush();
                Log.d("Consultar_Reunion", "Sent user ID: " + id);

                reuniones = (ArrayList<Reuniones>) ois.readObject();
                Log.d("Consultar_Reunion", "Received reuniones: " + (reuniones != null ? reuniones.size() : "null"));

                if (reuniones != null && !reuniones.isEmpty()) {
                    for (Reuniones r : reuniones) {
                        Log.d("Consultar_Reunion", "Meeting: " + r.toString());
                    }
                } else {
                    Log.d("Consultar_Reunion", "No meetings found.");
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                Log.e("Consultar_Reunion", "Error fetching meetings", e);
            }
        }).start();
    }


}