package com.example.androidreto2grupo4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import modelo.Profesor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Buscar_Horario_Profesor extends AppCompatActivity {

    private TableLayout tablaHorario;
    private TableLayout tablaLeyenda;
    private Map<String, String> abreviaturas = new HashMap<>();
    private Button btnVolver, btnAceptar;
    private Spinner listaProfesores;
    int id,tipo;
    private DataOutputStream dos;

    private ObjectInputStream ois;
    private ArrayList<Profesor>profesors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            dos = ServerConection.getInstance().getDataOutputStream();
            ois = ServerConection.getInstance().getObjectInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_horario_profesor);
        inicializarVariables();
        id = getIntent().getIntExtra("idLogin", -1);
        tipo = getIntent().getIntExtra("tipoLogin", -1); // -1 como valor predeterminado si no se envió el ID

        // Cargar la lista de profesores
        cargarListaProfesores();

        btnAceptar.setOnClickListener(view -> {
            Profesor profesorSeleccionado = (Profesor) listaProfesores.getSelectedItem();
            if (profesorSeleccionado != null) {
                int idProfesor = profesorSeleccionado.getId();
                cargarHorario(idProfesor);
            } else {
                Toast.makeText(this, "Por favor selecciona un profesor", Toast.LENGTH_SHORT).show();
            }
        });

        btnVolver.setOnClickListener(view -> {
            startActivity(new Intent(Buscar_Horario_Profesor.this, PaginaPrincipal.class));
        });
    }

    private void inicializarVariables() {
        tablaHorario = findViewById(R.id.tablaHorario);
        tablaLeyenda = findViewById(R.id.tablaLeyenda);
        btnVolver = findViewById(R.id.volver);
        btnAceptar = findViewById(R.id.aceptar);
        listaProfesores = findViewById(R.id.listaProfesores);
    }

    private void cargarHorario(int IDProfesorBuscar) {
        new Thread(() -> {
            try {
                ServerConection serverConnection = ServerConection.getInstance();

                dos.writeInt(2); // Opción para "ver horario"
                dos.writeInt(IDProfesorBuscar); // Enviar ID del profesor
                dos.flush();

                String[][] horario = (String[][]) ois.readObject();

                runOnUiThread(() -> llenarTabla(horario));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al cargar el horario", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void cargarListaProfesores() {
        new Thread(() -> {
            try {
                ServerConection serverConnection = ServerConection.getInstance();

                Log.d("CargaProfesores", "Enviando solicitud para cargar profesores...");


                    dos.writeInt(3); // Opción para "ver otros horarios"
                    dos.flush();
                    dos.flush();

                    dos.writeInt((tipo==3)?id:0); // Enviar ID del usuario actual si no es profesor enviamos un 0
                    dos.flush();

                    Log.d("CargaProfesores", "Esperando respuesta del servidor...");
                    profesors = (ArrayList<Profesor>) ois.readObject(); // Recibimos la lista de profesores como ArrayList<Profesor>

                Log.d("CargaProfesores", "Lista de profesores recibida, tamaño: " + profesors.size());
                runOnUiThread(() -> llenarSpinnerProfesores(profesors)); // Llamamos al método de llenar el spinner
            } catch (IOException e) {
                Log.e("CargaProfesores", "Error de entrada/salida", e);
                runOnUiThread(() -> Toast.makeText(this, "Error al cargar la lista de profesores", Toast.LENGTH_SHORT).show());
            } catch (ClassNotFoundException e) {
                Log.e("CargaProfesores", "Error al convertir la respuesta del servidor", e);
                runOnUiThread(() -> Toast.makeText(this, "Error de formato en la respuesta del servidor", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                Log.e("CargaProfesores", "Error inesperado", e);
                runOnUiThread(() -> Toast.makeText(this, "Ocurrió un error inesperado", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }


    private void llenarSpinnerProfesores(ArrayList<Profesor> profesoresList) {
        // Crea el adaptador usando la lista de profesores
        ArrayAdapter<Profesor> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, profesoresList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Configura el adaptador en el Spinner
        listaProfesores.setAdapter(adapter);
    }
    private String generarAbreviatura(String asignatura) {
        String abreviatura = "";
        String[] palabras = asignatura.trim().split(" ");
        for (String palabra : palabras) {
            if (!palabra.equalsIgnoreCase("a") && !palabra.equalsIgnoreCase("y") && !palabra.equalsIgnoreCase("de") && !palabra.isEmpty()) {
                abreviatura += palabra.charAt(0);
            }
        }
        return abreviatura.toUpperCase();
    }

    private void llenarTabla(String[][] horario) {
        if (tablaHorario.getChildCount() > 1) {
            tablaHorario.removeViews(1, tablaHorario.getChildCount() - 1);
        }
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

        llenarLeyenda();
    }

    private void llenarLeyenda() {
        tablaLeyenda.removeAllViews();
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