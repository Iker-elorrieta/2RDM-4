package com.example.androidreto2grupo4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import modelo.Centros;
import modelo.ReunionDto;
import modelo.Users;


public class InformacionReunion extends AppCompatActivity {
    TextView titulo;
    EditText texto;
    CardView btnAtras;
    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private DataInputStream dis;
    private ObjectInputStream ois;
    int usuarioId,idReunion, tipo;
    ArrayList<Centros> centros;


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

        inicializarVariable();

        Log.d("InformacionReunion", "Id de la reunion " + idReunion);
        titulo.setText("Informacion Reunion");
        cargarInfo();

        btnAtras.setOnClickListener(view -> {
            Intent i = new Intent(InformacionReunion.this, PaginaPrincipal.class);
            i.putExtra("idLogin", usuarioId);

            i.putExtra("tipoLogin", tipo);
            i.putExtra("centros", centros);
            startActivity(i);
        });
    }

    private void inicializarVariable() {
        usuarioId = getIntent().getIntExtra("idLogin", -1);
        tipo = getIntent().getIntExtra("tipoLogin", -1); // -1 como valor predeterminado si no se envió el ID
        centros = (ArrayList<Centros>) getIntent().getSerializableExtra("centros");
        idReunion= getIntent().getIntExtra("idReunion", -1);

        titulo = findViewById(R.id.tituloV);
        btnAtras = findViewById(R.id.cardviewAtras);
        texto = findViewById(R.id.nombreInfoPersonal);

    }

    private void cargarInfo() {
        new Thread(() -> {
            try {
                dos.writeInt(17); // Opcion para obtenr Informacion Reunion
                dos.flush();
                dos.writeInt(idReunion); // Opcion para obtenr Informacion Reunion
                dos.flush();

                ReunionDto r = (ReunionDto) ois.readObject();

                String  datosReunion = r.toString();
                Log.d("InformacionReunion", "Informacion datos " + datosReunion);

                Log.d("InformacionReunion", "Id a nivel del InformacionP " + idReunion);

                runOnUiThread(() -> {
                        texto.setText(datosReunion);

                });

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al cargar usuarios: " + e.getMessage(), Toast.LENGTH_LONG).show());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }


    private void volverAtras() {
        btnAtras.setOnClickListener(view -> finish());
    }


}