package com.example.androidreto2grupo4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import modelo.Centros;
import modelo.Users;

import com.example.androidreto2grupo4.Perfil.Perfil;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class PaginaPrincipal extends AppCompatActivity {

    private TextView holaUsuario;
    private CardView btnOtrosHorarios;
    private CardView btnCrearReunion;
    private CardView consultarHorario;
    private CardView consultarReuniones, alumnosProfe;
    private ImageButton btnMenu;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView usuarioMenu;
    private TextView emailMenu;
    private Users usuarioLogeado;
    private String nombre;
    ArrayList<Centros> centros;
    int idLogin ;
    int tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_principal);

        // Recupera los datos del Intent y formatea el nombre

           idLogin = getIntent().getIntExtra("idLogin", -1); // -1 como valor predeterminado si no se envió el ID
           tipo = getIntent().getIntExtra("tipoLogin", -1); // -1 como valor predeterminado si no se envió el ID
           centros = (ArrayList<Centros>) getIntent().getSerializableExtra("centros");

           usuarioLogeado = (Users) getIntent().getSerializableExtra("usuarioLogeado");
           nombre = "introducir un nombre";

        // Inicialización de vistas
        initializeViews();
        // Configuración del NavigationView y sus elementos
        setupNavigationView();

        consultarHorario.setOnClickListener(view -> {
            Intent intentPPC = new Intent(PaginaPrincipal.this, Consultar_Horario.class);
            intentPPC.putExtra("idLogin", idLogin);
            intentPPC.putExtra("tipoLogin", tipo);
            intentPPC.putExtra("centros", centros);
            startActivity(intentPPC);

        });


        btnOtrosHorarios.setOnClickListener(view -> {
            Intent intentPPC = new Intent(PaginaPrincipal.this, Buscar_Horario_Profesor.class);
            intentPPC.putExtra("idLogin", idLogin);
            intentPPC.putExtra("tipoLogin", tipo);
            intentPPC.putExtra("centros", centros);
            startActivity(intentPPC);
        });

        consultarReuniones.setOnClickListener(view -> {
            Intent intentPPC = new Intent(PaginaPrincipal.this, Consultar_Reunion.class);
            intentPPC.putExtra("idLogin", idLogin);
            intentPPC.putExtra("tipoLogin", tipo);
            intentPPC.putExtra("centros", centros);
            startActivity(intentPPC);
        });

        btnCrearReunion.setOnClickListener(view -> {
            Intent intentPPC = new Intent(PaginaPrincipal.this, Crear_Reunion.class);
            intentPPC.putExtra("idLogin", idLogin);
            intentPPC.putExtra("centros", centros);
            intentPPC.putExtra("tipoLogin", tipo);
            startActivity(intentPPC);
        });

        alumnosProfe.setOnClickListener(view -> {
            Intent intentPPC = new Intent(PaginaPrincipal.this, DatosEstudiantes.class);
            intentPPC.putExtra("idLogin", idLogin);
            intentPPC.putExtra("tipoLogin", tipo);
            intentPPC.putExtra("centros", centros);
            startActivity(intentPPC);
        });

    }

    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        btnMenu = findViewById(R.id.imageButtonMenu);
        holaUsuario = findViewById(R.id.textViewHolaUsuario);
        holaUsuario.append(nombre);
        btnCrearReunion = findViewById(R.id.cardViewBtnCrear);
        consultarHorario = findViewById(R.id.cardViewConsultarHorario);
        btnOtrosHorarios = findViewById(R.id.cardViewOtrosHorarios);
        consultarReuniones = findViewById(R.id.cardViewConsultarReunion);
        alumnosProfe = findViewById(R.id.btnAlumnosProfe);
        btnMenu.setOnClickListener(view -> {
            usuarioMenu = findViewById(R.id.textViewUsuarioMenu);
            emailMenu = findViewById(R.id.textViewEmailMenu);
            usuarioMenu.setText(nombre);
            emailMenu.setText(usuarioLogeado.getEmail().toString());

            // Alterna el estado del DrawerLayout
            if (!drawerLayout.isDrawerOpen(findViewById(R.id.nav_view))) {
                drawerLayout.openDrawer(findViewById(R.id.nav_view));
            } else {
                drawerLayout.closeDrawer(findViewById(R.id.nav_view));
            }
        });


    }

    private void setupNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavigationItemSelected(item);
                return true;
            }
        });
    }

    private void handleNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_account) {
            Intent intentPerfil = new Intent(this, Perfil.class);
            intentPerfil.putExtra("nombre", nombre);
            intentPerfil.putExtra("usuarioLogeado", usuarioLogeado);


            intentPerfil.putExtra("idLogin", idLogin);
            intentPerfil.putExtra("tipoLogin", tipo);
            startActivity(intentPerfil);
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Configuración seleccionada", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            finish();
        }
        drawerLayout.closeDrawers();
    }


}