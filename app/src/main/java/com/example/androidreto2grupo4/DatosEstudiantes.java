package com.example.androidreto2grupo4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import modelo.Ciclos;
import modelo.Users;

public class DatosEstudiantes extends AppCompatActivity {

    private DataOutputStream dos;
    private ObjectInputStream ois;
    private int id;
    private ArrayList<Users> listaUsers = new ArrayList<>();
    private UserAdapter adapter;
    private ArrayList<Ciclos> listaCiclos = new ArrayList<>();
    private ArrayList<String> listaCursos = new ArrayList<>();
    private ArrayAdapter<String> adapterCiclos, adapterCursos;
    private Spinner spinnerCiclo, spinnerCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_estudiantes);

        // Inicialización de componentes
        spinnerCiclo = findViewById(R.id.spinnerCiclo);
        spinnerCurso = findViewById(R.id.spinnerCurso);
        Button btnAtras = findViewById(R.id.btnAtras);
        RecyclerView recyclerViewEstudiantes = findViewById(R.id.recyclerViewEstudiantes);

        // Set up RecyclerView
        recyclerViewEstudiantes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, listaUsers);  // Inicializamos el adaptador
        recyclerViewEstudiantes.setAdapter(adapter); // Configuramos el adapter en el RecyclerView

        id = getIntent().getIntExtra("idLogin", -1);

        // Acción del botón Atras
        btnAtras.setOnClickListener(view -> {
            Intent menuProfesor = new Intent(DatosEstudiantes.this, PaginaPrincipal.class);
            startActivity(menuProfesor);
        });

        // Carga de datos
        cargarDatos();
    }

    private void cargarDatos() {
        new Thread(() -> {
            try {
                // Inicializamos los flujos de entrada y salida
                dos = ServerConection.getInstance().getDataOutputStream();
                ois = ServerConection.getInstance().getObjectInputStream();

                // Solicitar lista de usuarios
                dos.writeInt(11); // Comando para obtener usuarios
                dos.flush();

                dos.writeInt(id); // Enviamos el ID del usuario logueado
                dos.flush();

                // Recibimos la lista de usuarios
                listaUsers = (ArrayList<Users>) ois.readObject();
                Log.d("UsuariosRecibidos", "Número de usuarios recibidos: " + listaUsers.size());

                // Solicitar lista de ciclos
                dos.writeInt(14); // Comando para obtener ciclos
                dos.flush();

                listaCiclos = (ArrayList<Ciclos>) ois.readObject();
                Log.d("CiclosRecibidos", "Número de ciclos recibidos: " + listaCiclos.size());

                ArrayList<String> nombresCiclos = new ArrayList<>();
                for (Ciclos ciclo : listaCiclos) {
                    nombresCiclos.add(ciclo.getNombre());
                }

                listaCursos = new ArrayList<>();
                listaCursos.add("1");
                listaCursos.add("2");

                // Actualizamos la UI en el hilo principal
                runOnUiThread(() -> {
                    // Actualizamos el adaptador del RecyclerView
                    adapter.setUsers(listaUsers);
                    adapter.notifyDataSetChanged();

                    // Configuramos los spinners
                    adapterCiclos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresCiclos);
                    adapterCiclos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCiclo.setAdapter(adapterCiclos);

                    adapterCursos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaCursos);
                    adapterCursos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCurso.setAdapter(adapterCursos);
                });

            } catch (IOException | ClassNotFoundException e) {
                Log.e("ErrorAlcargarDatos", "Error al cargar datos: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
