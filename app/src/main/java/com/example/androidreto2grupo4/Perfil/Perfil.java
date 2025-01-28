package com.example.androidreto2grupo4.Perfil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import modelo.Users;
import com.example.androidreto2grupo4.R;
import com.example.androidreto2grupo4.ServerConection;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

public class Perfil extends AppCompatActivity {

    TextView nombre;
    CardView btnAtras;
    CardView btnInfoPersonal;
    String es = "es";
    String eu = "eu";
    Button btnIngles;
    Button btnEspanol;
    Users usuarioLogeado;
    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private DataInputStream dis;
    private ObjectInputStream ois;
    CardView btnFoto;
    ImageView imagen;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri photoURI;
    private String currentPhotoPath;
int id,tipoUsuario;
    @SuppressLint("WrongViewCast")
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
        setContentView(R.layout.activity_perfil);
        id = getIntent().getIntExtra("idLogin", -1); // ID del usuario logueado
        tipoUsuario = getIntent().getIntExtra("tipoLogin", -1); // Tipo del usuario (Alumno o Profesor)
        // Inicializar botonIdioma después de setContentView
        btnIngles = findViewById(R.id.btnIngles);
        btnEspanol = findViewById(R.id.btnEspanol);
        btnFoto = findViewById(R.id.btnFoto);
        imagen = findViewById(R.id.foto);
        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE);

        // Llamar a los métodos de configuración de la actividad
        volverAtras();
        cargarInfo();
        informacionPersonal();

        // Llamar a cambiarIdio1mas para configurar el listener del botón
        cambiarAEspanol();
        cambiarAEuskera();
        btnFoto.setOnClickListener(view -> abrirCamara());

    }


    private void abrirCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create a file to save the photo
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                // Get URI for the photo file
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.androidreto2grupo4.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create a unique file name for the image
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the image from the URI (full-size)
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            imagen.setImageBitmap(imageBitmap);
        }
    }


    private void cargarInfo() {
        nombre = findViewById(R.id.textViewNombrePerfil);
        usuarioLogeado = (Users) getIntent().getSerializableExtra("usuarioLogeado");
        nombre.setText(getIntent().getExtras().getString("nombre"));
    }

    private void informacionPersonal() {
        btnInfoPersonal = findViewById(R.id.cardViewInfoPersonal);
        btnInfoPersonal.setOnClickListener(view -> {
            Intent intentInfoPersonal = new Intent(this, InformacionPersonal.class);
            intentInfoPersonal.putExtra("idLogin", id);
            intentInfoPersonal.putExtra("tipoLogin", tipoUsuario);
            Log.d("ID", "Id a nivel del Perfil " + id);
            startActivity(intentInfoPersonal);
        });
    }



    private void volverAtras() {
        btnAtras = findViewById(R.id.cardviewAtras);
        btnAtras.setOnClickListener(view -> finish());
    }



    private void cambiarIdioma(String nuevoIdioma) {
        Locale locale = new Locale(nuevoIdioma);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Guardar el idioma seleccionado en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idioma", nuevoIdioma);
        editor.apply();

        // Enviar la preferencia de idioma al servidor

        new Thread(() -> {
            try {
                dos.writeInt(9); // Enviar opción 9 para actualizar idioma
                dos.flush();
                dos.writeInt(usuarioLogeado.getId()); // Enviar ID del usuario
                dos.flush();
                dos.writeUTF(nuevoIdioma); // Enviar el idioma seleccionado
                dos.flush();

                String respuesta = dis.readUTF(); // Recibir respuesta del servidor
                Log.d("Mensaje", "Respuesta del servidor: " + respuesta);
                Toast.makeText(Perfil.this, respuesta, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Perfil.this, "Error al cambiar idioma.", Toast.LENGTH_LONG).show();
            }
        }).start();
         //Que me explique iker
    }


    private void cambiarAEuskera() {
        btnIngles.setOnClickListener(new View.OnClickListener() {  // Botón específico para inglés
            @Override
            public void onClick(View v) {
                cambiarIdioma("eu"); // Cambia el idioma al inglés
            }
        });
    }

    private void cambiarAEspanol() {
        btnEspanol.setOnClickListener(new View.OnClickListener() {  // Botón específico para español
            @Override
            public void onClick(View v) {
                cambiarIdioma("es"); // Cambia el idioma al español
            }
        });
    }


}