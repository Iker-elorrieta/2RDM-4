package com.example.androidreto2grupo4;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import modelo.Centros;
import modelo.EstadoReunionES;
import modelo.EstadoReunionEU;
import modelo.ReunionDto;
import modelo.Users;

public class Crear_Reunion extends AppCompatActivity {
    private EditText etTitulo, etAsunto, etAula, etIdAlumnoProfesor;
    private TextView fechaHoraActual, alumnoProfesorId, estado, fecha, txtAlumnoOProfesor;
    private Button btnCalendario, btnCancelar, btnAceptar;
    private int id, dia, mes, anho, tipoUsuario;
    private Spinner spinnerCentro, spinnerAlumnoProfesor;
    private ArrayList<Centros> centros;
    private ArrayList<Users> usuariosConLosQueSePuedeReuinir;

    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private DataInputStream dis;
    private ObjectInputStream ois;

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
        setContentView(R.layout.activity_crear_reunion);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        id = getIntent().getIntExtra("idLogin", -1); // ID del usuario logueado
        tipoUsuario = getIntent().getIntExtra("tipoLogin", -1); // Tipo del usuario (Alumno o Profesor)
        centros = (ArrayList<Centros>) getIntent().getSerializableExtra("centros");

        inicializarVariables();

        Log.d("Tipo", "Tipo " + tipoUsuario);
        spinerCentros();
        cargarUsuarios(tipoUsuario); // Nuevo método para cargar alumnos/profesores
        getFechaHoraActual();

        btnAceptar.setOnClickListener(view -> aceptar());
        btnCancelar.setOnClickListener(view -> {
                Intent i = new Intent(Crear_Reunion.this, PaginaPrincipal.class);
                i.putExtra("idLogin",id);
                i.putExtra("tipoLogin", tipoUsuario);
                i.putExtra("centros", centros);
                startActivity(i);
        });
        btnCalendario.setOnClickListener(v -> {
            final Calendar calendario = Calendar.getInstance();
            dia = calendario.get(Calendar.DAY_OF_MONTH);
            mes = calendario.get(Calendar.MONTH);
            anho = calendario.get(Calendar.YEAR);

            new DatePickerDialog(Crear_Reunion.this, (view, diaSeleccionado, mesSeleccionado, anhoSeleccionado) -> fecha.setText(String.format("%02d/%02d/%d", diaSeleccionado, mesSeleccionado + 1, anhoSeleccionado)), anho, mes, dia).show();
        });
    }

    private void cargarUsuarios(int tipoUsuario) {
        new Thread(() -> {
            try {
                dos.writeInt(7); // Opcion para "obtenerProfesoresAlumno"
                dos.flush();

                dos.writeInt(tipoUsuario); // Tipo de usuario: 3 profesor o 4 alumno
                dos.flush();
                usuariosConLosQueSePuedeReuinir = (ArrayList<Users>) ois.readObject();
                runOnUiThread(() -> {
                    ArrayList<String> nombresUsuarios = new ArrayList<>();
                    for (Users usuario : usuariosConLosQueSePuedeReuinir) {
                        nombresUsuarios.add(usuario.getNombre() + " " + usuario.getApellidos());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresUsuarios);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAlumnoProfesor.setAdapter(adapter);

                });
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                Log.e("ErrorCargaUsuarios", "Error al cargar usuarios: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(this, "Error al cargar usuarios: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }


    private void getFechaHoraActual() {
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaHora = formatoFecha.format(calendario.getTime());

        fechaHoraActual.setText(fechaHora);
    }

    private void inicializarVariables() {
        etTitulo = findViewById(R.id.etTitulo);
        etAsunto = findViewById(R.id.etAsunto);
        etAula = findViewById(R.id.etAula);

        fechaHoraActual = findViewById(R.id.fecha_hora_actual);
        alumnoProfesorId = findViewById(R.id.alumnoProfesor_id);
        estado = findViewById(R.id.estado);
        fecha = findViewById(R.id.fecha);
        txtAlumnoOProfesor = findViewById(R.id.txtAlumnoOProfesor);
        btnCalendario = findViewById(R.id.calendario);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnAceptar = findViewById(R.id.btnAceptar);
        spinnerCentro = findViewById(R.id.spinnerCentro);
        spinnerAlumnoProfesor = findViewById(R.id.spinnerAlumnoProfesor);

        txtAlumnoOProfesor.setText(tipoUsuario != 3 ? "Profesor" : "Alumno");
        TextView alumno = findViewById(R.id.alumnoProfesor_id);
        alumno.setText("ID Usuario " + id);
    }

    private void spinerCentros() {
        ArrayList<String> centroNames = new ArrayList<>();
        int posicionInicial = -1;

        for (int i = 0; i < centros.size(); i++) {
            Centros centro = centros.get(i);
            centroNames.add(centro.getNombre());

            // Buscar la posición de que queremos que sea laa inicail
            if (centro.getNombre().equalsIgnoreCase("ELORRIETA-ERREKA MARI")) {
                posicionInicial = i;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, centroNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCentro.setAdapter(adapter);

        // Setear la posición inicial si fue encontrada
        if (posicionInicial != -1) {
            spinnerCentro.setSelection(posicionInicial);
        }
    }

    private void aceptar() {
        Log.d("Aceptar", "Método Aceptar llamado.");

        String titulo = etTitulo.getText().toString().trim();
        String asunto = etAsunto.getText().toString().trim();
        String aula = etAula.getText().toString().trim();
        String selectedDate = fecha.getText().toString().trim();
        int idCentro = centros.get(spinnerCentro.getSelectedItemPosition()).getIdCentro(); // Obtenemos atraves de la posicion del spiner

        // Todos los campos están completos
        if (titulo.isEmpty() || asunto.isEmpty() || aula.isEmpty() ) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Por favor, Selecciona una fecha.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Formato de fecha y que sea fecha futura
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.setTime(sdf.parse(selectedDate));

        } catch (Exception e) {
            Toast.makeText(this, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }

        //Por defecto estado pendiente
        String estado = EstadoReunionES.PENDIENTE.toString();
        String estadoEus = EstadoReunionEU.ONARTZEKE.toString();
// Generamos los campos alumnoID y profesorID, asi funciona el constructor por defecto que hay en eclipse pues lo rellenamos
        Users alumnoId = null;
        Users profesorId = null;

        if (tipoUsuario == 3) { // Si es profesor
            // El ID del profesor es el del usuario logueado (id)
            profesorId = new Users();
            profesorId.setId(id);

            // El ID del alumno lo obtenemos del arraylist a traves de la posicion del spinner
            alumnoId = new Users();
            alumnoId.setId(usuariosConLosQueSePuedeReuinir.get(spinnerAlumnoProfesor.getSelectedItemPosition()).getId());

        } else if (tipoUsuario == 4) { // Si es alumno
            // El ID del alumno es el del usuario logueado (id)
            alumnoId = new Users();
            alumnoId.setId(id);

            // El ID del profesor lo obtenemos del arraylist a traves de la posicion del spinner
            profesorId = new Users();
            profesorId.setId(usuariosConLosQueSePuedeReuinir.get(spinnerAlumnoProfesor.getSelectedItemPosition()).getId());
        }

        // Fecha seleccionada a formato Timestamp
        SimpleDateFormat sdfWithTime = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdfWithTime.parse(selectedDate + " 00:00")); // Usar la fecha seleccionada, y ajustarla a las 00:00 de ese día
            Timestamp fechaTimestamp = new Timestamp(calendar.getTimeInMillis());

            // Conexcion con el servidor para añadirla
     //       ReunionDto nuevaReunion = new ReunionDto(profesorId, alumnoId, estado, estadoEus, String.valueOf(idCentro), titulo, asunto, aula, fechaTimestamp);

          //  enviarReunionAlServidor(nuevaReunion);

                Intent i = new Intent(Crear_Reunion.this, PaginaPrincipal.class);
                i.putExtra("idLogin", id);
                i.putExtra("tipoLogin", tipoUsuario);
                i.putExtra("centros", centros);
                startActivity(i);

        } catch (Exception e) {
            Toast.makeText(this, "Error al procesar la fecha y hora.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void enviarReunionAlServidor(ReunionDto reunion) {
        new Thread(() -> {
            try {
                dos.writeInt(8); // Opción Crear reunion
                dos.flush();

                oos.writeObject(reunion); // Enviar objeto
                oos.flush();

                String texto = dis.readUTF(); //  confirmación del servidor
                Log.d("Mensaje", "Mensaje servidor " + texto);

                runOnUiThread(() -> {
                    Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Reunión creada con éxito.", Toast.LENGTH_SHORT).show();
                    finish();  // Cerrar la actividad después de la confirmación sino falla
                });
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al crear la reunión: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }


}
