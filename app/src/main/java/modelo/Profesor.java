package modelo;

import java.io.Serializable;
public class Profesor implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Profesor(int id, String nombre) {
        super();
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return   nombre ;
    }
}