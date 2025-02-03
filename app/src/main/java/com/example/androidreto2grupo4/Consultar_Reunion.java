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
import androidx.appcompat.app.AppCompatActivity;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import modelo.Centros;
import modelo.ReunionDto;

public class Consultar_Reunion extends AppCompatActivity {
    private TableLayout tablaHorario;
    private TableLayout tablaLeyenda;
    private Map<String, String> abreviaturas = new HashMap<>();
    private Button btnVoler;
    private DataOutputStream dos;
    ArrayList<ReunionDto> reuniones;
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
                // Obtener reuniones
                dos.writeInt(16);
                dos.flush();
                dos.writeInt(usuarioId);
                dos.flush();
                reuniones = (ArrayList<ReunionDto>) ois.readObject();
                String[][] reunionesModelo = new ReunionDto().getModeloReuniones(reuniones);

                // Obtener horario del profesor
                dos.writeInt(2);
                dos.flush();
                dos.writeInt(usuarioId);
                dos.flush();
                String[][] horarioP = (String[][]) ois.readObject();

                // Combinar horarios y obtener IDs de reuniones
                String[][] horarioJuntado = juntarHorarios(reunionesModelo, horarioP);

                // Llenar la tabla con datos y IDs
                runOnUiThread(() -> llenarTabla(horarioJuntado));
            } catch (IOException | ClassNotFoundException e) {
                Log.e("ConsultarReunion", "Error al cargar reuniones", e);
                runOnUiThread(() -> Toast.makeText(this, "Error al cargar las reuniones", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }


    private String[][] juntarHorarios(String[][] reunionesModelo, String[][] horario) {
        int filas = horario.length;
        int columnas = horario[0].length;

        String[][] resultado = new String[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                String clase = horario[i][j];
                String reunion = reunionesModelo[i][j];

                if (!clase.isEmpty() && !reunion.isEmpty()) {
                    resultado[i][j] =  reunion + " / " + clase ;
                } else if (!clase.isEmpty()) {
                    resultado[i][j] = clase;
                } else if (!reunion.isEmpty()) {
                    resultado[i][j] = reunion;
                } else {
                    resultado[i][j] = "";
                }
            }
        }

        return resultado;
    }
    private int[][] juntarIdsReuniones(ArrayList<ReunionDto> reuniones, String[][] reunionesModelo) {
        int filas = reunionesModelo.length;
        int columnas = reunionesModelo[0].length;
        int[][] idsReuniones = new int[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                String nombreReunion = reunionesModelo[i][j];

                for (ReunionDto reunion : reuniones) {
                    Log.d("Ids R", "Texto " + nombreReunion);

                    if (reunion.getTitulo().equals(nombreReunion)) {
                        idsReuniones[i][j] = reunion.getIdReunion();
                        Log.d("Ids R", "Id " +idsReuniones  );
                        break; // Salimos al encontrar la primera coincidencia
                    }
                }
            }
        }

        return idsReuniones;
    }
    private void llenarTabla(String[][] horario) {
        for (int i = 0; i < horario.length; i++) {
            TableRow fila = new TableRow(this);
            for (int j = 0; j < horario[i].length; j++) {
                TextView celda = new TextView(this);
                String asignatura = horario[i][j];
                int idReunion;
                if(asignatura.contains("id:")){
                    String[] partes = asignatura.split("id:");
                    idReunion = Integer.parseInt( partes[0]);
                    asignatura =partes[1];
                } else {
                    idReunion = 0;
                }

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

    Log.d("Contende", " " + idReunion);
                if (idReunion > 0) { // Solo agregamos el evento si hay un ID válido
                    celda.setOnClickListener(view -> {
                        Log.d("ConsultarReunion", "Redirigiendo con ID Reunion: " + idReunion);
                        Intent i2 = new Intent(Consultar_Reunion.this, InformacionReunion.class);
                        i2.putExtra("idLogin", usuarioId);
                        i2.putExtra("tipoLogin", tipo);
                        i2.putExtra("centros", centros);
                        i2.putExtra("idReunion", idReunion);
                        startActivity(i2);
                    });
                }

                fila.addView(celda);
            }
            tablaHorario.addView(fila);
        }

        llenarLeyenda();
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
