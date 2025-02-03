package com.example.androidreto2grupo4;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import modelo.Centros;

public class Reuniones_Pendientes extends AppCompatActivity {
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ArrayList<ArrayList<String>> listaReuniones = new ArrayList<>();
    private ReunionAdapter adapter;
    private RecyclerView recyclerViewReuniones;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(); // ExecutorService para manejar hilos
    ArrayList<Centros> centros;
    int idLogin;
    int tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reuniones_pendientes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        idLogin = getIntent().getIntExtra("idLogin", -1); // -1 como valor predeterminado si no se envió el ID
        tipo = getIntent().getIntExtra("tipoLogin", -1); // -1 como valor predeterminado si no se envió el ID
        centros = (ArrayList<Centros>) getIntent().getSerializableExtra("centros");

        recyclerViewReuniones = findViewById(R.id.recyclerViewReuniones);
        recyclerViewReuniones.setLayoutManager(new LinearLayoutManager(this));

        // Creación del adaptador con el listener
        adapter = new ReunionAdapter(this, listaReuniones, new ReunionAdapter.OnReunionActionListener() {
            @Override
            public void onAceptar(int idReunion) {
                aceptarReunion(idReunion);
            }

            @Override
            public void onRechazar(int idReunion) {
                rechazarReunion(idReunion);
            }
        });
        recyclerViewReuniones.setAdapter(adapter);


        cargarReuniones(); // Cargar reuniones cuando se inicia la actividad
    }

    // Método para cargar las reuniones desde el servidor
    private void cargarReuniones() {
        executorService.execute(() -> {
            try {
                dos = ServerConection.getInstance().getDataOutputStream();
                ois = ServerConection.getInstance().getObjectInputStream();

                dos.writeInt(17); // Código para obtener reuniones pendientes
                dos.flush();
                dos.writeInt(idLogin);
                dos.flush();
                listaReuniones = (ArrayList<ArrayList<String>>) ois.readObject();
                Log.d("Reuniones_Pendientes", "Reuniones recibidas: " + listaReuniones.size());

                runOnUiThread(() -> {
                    adapter.setReuniones(listaReuniones);
                    adapter.notifyDataSetChanged();
                });

            } catch (IOException | ClassNotFoundException e) {
                Log.e("Reuniones_Pendientes", "Error al cargar reuniones: ", e);
                runOnUiThread(() -> Toast.makeText(this, "No se pudieron cargar las reuniones. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    // Método para aceptar una reunión
    private void aceptarReunion(int idReunion) {
        actualizarEstadoReunion(idReunion, "aceptada");
    }

    // Método para rechazar una reunión
    private void rechazarReunion(int idReunion) {
        actualizarEstadoReunion(idReunion, "denegada");
    }

    // Método para actualizar el estado de una reunión
    private void actualizarEstadoReunion(int idReunion, String estado) {
        executorService.execute(() -> {
            try {
                dos.writeInt(5); // Código para actualizar estado de reunión
                dos.flush();

                dos.writeInt(idReunion);
                dos.flush();

                dos.writeUTF(estado);
                dos.flush();
                for (int i = 0; i < listaReuniones.size(); i++) {
                    if (idReunion == Integer.parseInt(listaReuniones.get(i).get(0))) {
                        listaReuniones.remove(i);
                    }
                }

                runOnUiThread(() -> {
                    Toast.makeText(this, "Reunión " + estado, Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged(); // Actualizar la vista solo de los elementos modificados
                });

            } catch (IOException e) {
                Log.e("Reuniones_Pendientes", "Error al actualizar reunión: ", e);
            }
        });
    }
}