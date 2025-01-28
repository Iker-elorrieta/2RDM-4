package com.example.androidreto2grupo4.Perfil;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import modelo.Users;
import com.example.androidreto2grupo4.R;
import com.example.androidreto2grupo4.ServerConection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class InformacionPersonal extends AppCompatActivity {

    EditText texto;
    CardView btnAtras;
    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private DataInputStream dis;
    private ObjectInputStream ois;
    int idUsuario, tipo;




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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion_personal);

        idUsuario = getIntent().getIntExtra("idLogin", -1); // ID del usuario logueado
        tipo = getIntent().getIntExtra("tipoLogin", -1); // Tipo del usuario (Alumno o Profesor)
        Log.d("ID", "Id a nivel del InformacionP " + idUsuario);

        cargarInfo();

    volverAtras();



    }

    private void cargarInfo() {
        texto = findViewById(R.id.nombreInfoPersonal);
        new Thread(() -> {
            try {
                dos.writeInt(10); // Opcion para "obtenerPerfil"
                dos.flush();

                dos.writeInt(idUsuario);
                dos.flush();

                dos.writeInt(tipo); // Tipo de usuario: 3 profesor o 4 alumno
                dos.flush();

                Users datosUser = (Users) ois.readObject();

                runOnUiThread(() -> {
                    if (tipo == 3) {
                        texto.setText(datosUser.informacionPersonalP());

                    } else {
                        texto.setText(datosUser.informacionPersonalA());

                    }
                });

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al cargar usuarios: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }



    private void volverAtras() {
        btnAtras = findViewById(R.id.cardviewAtras);
        btnAtras.setOnClickListener(view -> finish());
    }


}