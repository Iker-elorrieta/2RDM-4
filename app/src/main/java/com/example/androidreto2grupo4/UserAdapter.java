package com.example.androidreto2grupo4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;


import java.util.List;

import modelo.Users;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    // CLASE PARA PODER INSERTAR LOS DATOS EN EL RECYCLERVIEW
    private List<Users> listaUSer; // Cambiamos a mutable para poder actualizar la lista
    private final Context context;

    public UserAdapter(Context context, List<Users> listaUSer) {
        this.context = context;
        this.listaUSer = listaUSer;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista_item_personas, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = listaUSer.get(position);

        // Info del usuario
        holder.nombrePersona.setText(user.getNombre());
        holder.apellidoPersona.setText(user.getApellidos());

        // Cargar la imagen
        Glide.with(context)
                .load(user.getArgazkia()) // Obtenemos la URL de la imagen
                .placeholder(R.drawable.defecto_img) // Imagen placeholder si no está disponible
                .error(R.drawable.error) // Imagen de error si falla
                .into(holder.fotoPersona);
    }

    @Override
    public int getItemCount() {
        return listaUSer.size();
    }

    // Método para actualizar la lista de usuarios
    public void setUsers(List<Users> nuevaListaUsers) {
        this.listaUSer = nuevaListaUsers; // Actualizar la referencia a la lista
        notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
    }

    // Clase ViewHolder para gestionar las vistas de cada ítem
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePersona, apellidoPersona;
        ImageView fotoPersona;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrePersona = itemView.findViewById(R.id.nombrePersona);
            apellidoPersona = itemView.findViewById(R.id.apellidoPersona);
            fotoPersona = itemView.findViewById(R.id.fotoPersona);
        }
    }
}
