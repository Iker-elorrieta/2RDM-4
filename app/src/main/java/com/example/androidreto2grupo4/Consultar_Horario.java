
package com.example.androidreto2grupo4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Consultar_Horario extends AppCompatActivity {
    private TableLayout tablaHorario;
    private TableLayout tablaLeyenda;
    private Map<String, String> abreviaturas = new HashMap<>();
    private Button btnVoler;
    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private DataInputStream dis;
    private ObjectInputStream ois;
    int tipo, usuarioId;

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
        setContentView(R.layout.activity_consultar_horario);
        inicializarVariables();
        usuarioId = getIntent().getIntExtra("idLogin", -1);
        tipo = getIntent().getIntExtra("tipoLogin", -1); // -1 como valor predeterminado si no se envió el ID

        // Cargar el horario
        cargarHorario();
        btnVoler.setOnClickListener(view -> {
            startActivity(new Intent(Consultar_Horario.this, PaginaPrincipal.class));
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
                // Solicita el horario
                String[][] horario ;
                //Tipo profesor
                if (tipo == 3) {
                    dos.writeInt(2);
                    dos.flush();
                    dos.writeInt(usuarioId);
                    dos.flush();
                    horario  = (String[][]) ois.readObject();
                }else{
                    dos.writeInt(12);
                    dos.flush();
                    dos.writeInt(usuarioId);
                    dos.flush();
                    horario  = (String[][]) ois.readObject();
                }

                runOnUiThread(() -> llenarTabla(horario));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al cargar el horario", Toast.LENGTH_SHORT).show());
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
                String abreviado = asignatura;
                if (j != 0) {
                    abreviado = generarAbreviatura(asignatura);
                    abreviaturas.put(asignatura, abreviado);
                }
                celda.setText(asignatura != null ? abreviado : "");

                celda.setPadding(8, 8, 8, 8);
                celda.setGravity(Gravity.CENTER);
                celda.setBackgroundColor(i % 2 == 0 ? Color.LTGRAY : Color.WHITE);
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
