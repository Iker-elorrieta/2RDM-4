package com.example.androidreto2grupo4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.Centros;
import modelo.Reuniones;

public class Consultar_Reunion extends AppCompatActivity {
    private TableLayout tablaHorario;
    private TableLayout tablaLeyenda;
    private Map<String, String> abreviaturas = new HashMap<>();
    private Button btnVoler;
    private DataOutputStream dos;

    private ObjectInputStream ois;
    int tipo, usuarioId;
    ArrayList<Centros> centros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            dos = ServerConection.getInstance().getDataOutputStream();
            ois = ServerConection.getInstance().getObjectInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_horario);
        inicializarVariables();
        usuarioId = getIntent().getIntExtra("idLogin", -1);
        tipo = getIntent().getIntExtra("tipoLogin", -1); // -1 como valor predeterminado si no se envió el ID
        centros = (ArrayList<Centros>) getIntent().getSerializableExtra("centros");
        // Cargar el horario
        cargarHorario();

        btnVoler.setOnClickListener(view -> {
            Intent i = new Intent(Consultar_Reunion.this, PaginaPrincipal.class);
            i.putExtra("idLogin", usuarioId);
            i.putExtra("tipoLogin", tipo);
            i.putExtra("centros", centros);
            startActivity(i);
        });
    }

    private void inicializarVariables() {
        tablaHorario = findViewById(R.id.tablaHorario);
        tablaLeyenda = findViewById(R.id.tablaLeyenda);
        btnVoler = findViewById(R.id.volver);
    }

    private void cargarHorario() {
        new Thread(() -> {
            try {
                dos.writeInt(4);
                dos.flush();
                dos.writeInt(usuarioId);
                dos.flush();

                String[][] horario = (String[][]) ois.readObject();
                runOnUiThread(() -> llenarTabla(horario));
            } catch (IOException | ClassNotFoundException e) {
                Log.e("ConsultarReunion", "Error al cargar reuniones", e);
                runOnUiThread(() -> Toast.makeText(this, "Error al cargar el Reunion", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }


    private String generarAbreviatura(String asignatura) {

        String abreviatura = "";
        String[] palabras = asignatura.trim().split(" ");
        for (String palabra : palabras) {
            if (!palabra.equalsIgnoreCase("a") && !palabra.equalsIgnoreCase("y") && !palabra.equalsIgnoreCase("de")
                    && !palabra.isEmpty()) {
                abreviatura += palabra.charAt(0);
            }
        }
        return abreviatura.toUpperCase();
    }

    private void llenarTabla(String[][] horario) {
        for (int i = 0; i < horario.length; i++) {
            TableRow fila = new TableRow(this);
            for (int j = 0; j < horario[i].length; j++) {
                TextView celda = new TextView(this);
                String asignatura = horario[i][j];


                celda.setPadding(8, 8, 8, 8);
                celda.setGravity(Gravity.CENTER);
                celda.setBackgroundColor(i % 2 == 0 ? Color.LTGRAY : Color.WHITE);

                if (asignatura.contains("-R")) {
                    celda.setBackgroundColor(Color.RED);
                } else if (asignatura.contains("-C")) {
                    celda.setBackgroundColor(Color.GREEN);
                } else if (asignatura.contains("-P")) {
                    celda.setBackgroundColor(Color.GRAY);
                } else if (asignatura.contains("-E")) {
                    celda.setBackgroundColor(Color.YELLOW);
                }

                String abreviado = asignatura;
                if (j != 0) {
                    abreviado = generarAbreviatura(asignatura);
                    abreviaturas.put(asignatura, abreviado);
                }
                celda.setText(asignatura != null ? abreviado : "");
                if (asignatura.contains("-")){
                    celda.setOnClickListener(view -> {
                        Intent i2 = new Intent(Consultar_Reunion.this, PaginaPrincipal.class);

                        startActivity(i2);

                    });
                }

                fila.addView(celda);


            }
            tablaHorario.addView(fila);
        }

        // Llenar la leyenda
        llenarLeyenda();
    }

    private void llenarLeyenda() {
        for (Map.Entry<String, String> entry : abreviaturas.entrySet()) {
            TableRow fila = new TableRow(this);

            TextView asignatura = new TextView(this);
            asignatura.setText(entry.getKey());
            asignatura.setPadding(8, 8, 8, 8);

            TextView abreviacion = new TextView(this);
            abreviacion.setText(entry.getValue());
            abreviacion.setPadding(8, 8, 8, 8);
            abreviacion.setGravity(Gravity.CENTER);

            fila.addView(asignatura);
            fila.addView(abreviacion);
            tablaLeyenda.addView(fila);
        }
    }

}
