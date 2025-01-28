package com.example.androidreto2grupo4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import modelo.Centros;
import modelo.Users;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Login extends AppCompatActivity {
    private static final String SERVER_IP = "192.168.56.1";
    private static final int SERVER_PORT = 2000;
    private static final String TAG = "SocketClient";
    private static Users usuarioLogeado = null;
    EditText etUsuario, etContrasenha;
    Button btnLogin;
    TextView btnEs;
    TextView btnEU;
    SharedPreferences sharedPreferences;
    private ArrayList<Centros> centros = new ArrayList<Centros>();

    // Variables para la conexión con Servidor
    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private DataInputStream dis;
    private ObjectInputStream ois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarVariables();

        // Conexión con el servidor en un hilo de fondo
        new Thread(() -> {
            try {
                concectarConServidor();
            } catch (IOException e) {
                Log.e(TAG, "Error al conectar con el servidor", e);
                runOnUiThread(() -> Toast.makeText(Login.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

        btnLogin.setOnClickListener(view -> {
            try {
                realizarLogin(etUsuario, etContrasenha);
            } catch (IOException e) {
                Log.e(TAG, "Error al realizar login", e);
            }
        });

        etContrasenha.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                try {
                    realizarLogin(etUsuario, etContrasenha);
                } catch (IOException e) {
                    Log.e(TAG, "Error al realizar login", e);
                }
                Toast.makeText(this, "Enter pulsado en contraseña", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });


        sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE);


        // Llamar a cambiarIdio1mas para configurar el listener del botón
        cambiarAEspanol();
        cambiarAEuskera();
        TextView textViewOlvidar = findViewById(R.id.textViewOlvidar);
        textViewOlvidar.setOnClickListener(v -> mostrarDialogoCorreo());
    }

    private void mostrarDialogoCorreo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar Contraseña");

        // Crear un EditText para que el usuario ingrese su correo
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        builder.setPositiveButton("Enviar", (dialog, which) -> {
            String correo = input.getText().toString();
            if (!correo.isEmpty()) {
                String nuevaContrasena = generarContrasenaAleatoria();
                // Enviar correo con la nueva contraseña
                enviarCorreo(correo, nuevaContrasena);
                Toast.makeText(Login.this, "Se ha enviado la nueva contraseña a tu correo.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Login.this, "Por favor ingresa un correo válido.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private String generarContrasenaAleatoria() {
        Random random = new Random();

        return "Provisional";
    }

    private void enviarCorreo(String destinatario, String nuevaContrasena) {
        // Define la cuenta de correo predefinida
        String correoPredefinido = "eneko.tejerosa@elorrieta-errekamari.com";
        String contrasenaPredefinida = "ubtd upcv lllq srzs";

        // Instancia de MailSender
        MailSender mailSender = new MailSender(correoPredefinido, contrasenaPredefinida);

        // Tarea en segundo plano para enviar el correo (para evitar bloquear la UI)
        new Thread(() -> {
            try {
                mailSender.enviarCorreo(
                        destinatario,
                        "Recuperación de Contraseña",
                        "Tu nueva contraseña es: " + nuevaContrasena
                );
                runOnUiThread(() -> Toast.makeText(this, "Correo enviado correctamente.", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al enviar el correo.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }



    private void inicializarVariables() {
        btnEU =findViewById(R.id.textViewEU);
        btnEs = findViewById(R.id.textViewES);
        btnLogin = findViewById(R.id.btnLogin);
        etUsuario = findViewById(R.id.editTextUsuarioL);
        etContrasenha = findViewById(R.id.editTextPasswordL);
    }

    private void realizarLogin(EditText etUsuario, EditText etContrasenha) throws IOException {
        String usuario = etUsuario.getText().toString();
        String contra = etContrasenha.getText().toString();
        if (!usuario.isEmpty() && !contra.isEmpty()) {
            new Thread(() -> {
                try {
                    String contrasenaCifrada = "";

                    // Enviar solicitud de login
                    dos.writeInt(13); // Indicar que se trata de una solicitud de login
                    dos.flush();

                    dos.writeUTF(usuario);
                    dos.flush();

                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA");
                        byte dataBytes[] = contra.getBytes();
                        md.update(dataBytes);
                        byte resumen[] = md.digest();
                        contrasenaCifrada = new String(resumen);

                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                 //   dos.writeUTF(contrasenaCifrada);
                    dos.writeUTF(contra);

                    dos.flush();

                    int id = (int) dis.readInt(); // Recibimos el ID del usuario
                    int tipo = (int) dis.readInt(); // Recibimos el tipo de usuario
                    Log.d(TAG, "ID " + id);

                    if(id!=0){
                        // Si el tipo de usuario es distinto de 3 (por ejemplo, alumno), redirigimos a la página correspondiente
                        if (tipo == 3 || tipo == 4) {
                            usuarioLogeado = new Users();
                            usuarioLogeado.setNombre("hola"); // Puedes mejorar la asignación de nombre real
                            usuarioLogeado.setEmail("@123"); // Lo mismo para el email
                            Intent intentPPC = new Intent(Login.this, PaginaPrincipal.class);
                            intentPPC.putExtra("usuarioLogeado", usuarioLogeado);
                            intentPPC.putExtra("idLogin", id);
                            intentPPC.putExtra("tipoLogin", tipo);
                            intentPPC.putExtra("centros", centros);

                            startActivity(intentPPC);
                        }

                    } else {
                        runOnUiThread(() -> Toast.makeText(Login.this, "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show());
                    }

                } catch (IOException e) {
                    Log.e(TAG, "Error al enviar o recibir datos de login", e);
                    runOnUiThread(() -> Toast.makeText(Login.this, "Error al realizar el login", Toast.LENGTH_SHORT).show());
                }
            }).start();
        } else {
            Toast.makeText(this, getString(R.string.toast_llena_campos), Toast.LENGTH_SHORT).show();
        }
    }


    private void concectarConServidor() throws IOException, ClassNotFoundException {
        ServerConection.getInstance();
        dos = ServerConection.getInstance().getDataOutputStream();
        dis = ServerConection.getInstance().getDataInputStream();
        ois = ServerConection.getInstance().getObjectInputStream();
        oos = ServerConection.getInstance().getObjectOutputStream();

        centros = (ArrayList<Centros>) ois.readObject();
        Log.d(TAG, "Tamaño del array: " + centros.size());
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

        // Reiniciar la actividad para aplicar el cambio de idioma
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void cambiarAEuskera() {
        btnEU.setOnClickListener(new View.OnClickListener() {  // Botón específico para inglés
            @Override
            public void onClick(View v) {
                cambiarIdioma("eu"); // Cambia el idioma al inglés
            }
        });
    }

    private void cambiarAEspanol() {
        btnEs.setOnClickListener(new View.OnClickListener() {  // Botón específico para español
            @Override
            public void onClick(View v) {
                cambiarIdioma("es"); // Cambia el idioma al español
            }
        });
    }
}
