package com.example.androidreto2grupo4;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidreto2grupo4.UserAdapter;
import com.example.androidreto2grupo4.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import modelo.Users;
public class DatosEstudiantes extends AppCompatActivity {

    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private DataInputStream dis;
    private ObjectInputStream ois;
    private int id;
    private ArrayList<Users> listaUsers = new ArrayList<>();
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_datos_estudiantes);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        id = getIntent().getIntExtra("idLogin", -1);

        Button btnAtras = findViewById(R.id.btnAtras);
        RecyclerView recyclerViewEstudiantes = findViewById(R.id.recyclerViewEstudiantes);
        recyclerViewEstudiantes.setLayoutManager(new LinearLayoutManager(this));



        btnAtras.setOnClickListener(view -> {
            Intent menuProfesor = new Intent(DatosEstudiantes.this, PaginaPrincipal.class);
            startActivity(menuProfesor);
        });

        new Thread(() -> {
            try {
                dos = ServerConection.getInstance().getDataOutputStream();
                dis = ServerConection.getInstance().getDataInputStream();
                ois = ServerConection.getInstance().getObjectInputStream();
                oos = ServerConection.getInstance().getObjectOutputStream();

                // Load users after the connection is established
                cargarUsuarios();

                adapter = new UserAdapter(this, listaUsers);
                recyclerViewEstudiantes.setAdapter(adapter);

            } catch (IOException e) {
                Log.e("ErrorStreams", "Error initializing streams: " + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error connecting to server", Toast.LENGTH_LONG).show();
                    finish(); // Exit activity if connection fails
                });
            }
        }).start();
    }




    private void cargarUsuarios() {
        new Thread(() -> {
            try {
                // Enviar el comando y el ID al servidor
                dos.writeInt(11); // Comando para obtener usuarios
                dos.flush();

                dos.writeInt(id); // ID del usuario logueado
                dos.flush();

                // Leer la lista de usuarios enviada por el servidor
                listaUsers = (ArrayList<Users>) ois.readObject();
                Log.d("Tamano", "Número de alumnos: " + listaUsers.size());

                runOnUiThread(() -> {
                    adapter.setUsers(listaUsers);
                    adapter.notifyDataSetChanged();
                });
            } catch (IOException | ClassNotFoundException e) {
                Log.e("ErrorCargaUsuarios", "Error al cargar usuarios: " + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error al cargar usuarios: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}
