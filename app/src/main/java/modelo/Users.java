package modelo;

import java.io.Serializable;

public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String email;
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private String dni;
    private String direccion;
    private Integer telefono1;
    private Integer telefono2;
    private int tipoId;
    private byte[] argazkia; // Para almacenar el campo longblob

    // Constructor vacío
    public Users() {
    }

    private String cicloNombre;
    private int curso;

    public Users(int id, String email, String username, String password, String nombre, String apellidos,
                 String dni, String direccion, Integer telefono1, Integer telefono2, byte[] argazkia,
                 String cicloNombre, int curso) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.direccion = direccion;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.argazkia = argazkia;
        this.cicloNombre = cicloNombre;
        this.curso = curso;
    }

    public String getCicloNombre() {
        return cicloNombre;
    }

    public void setCicloNombre(String cicloNombre) {
        this.cicloNombre = cicloNombre;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }


    // Constructor con todos los campos
    public Users(int id, String email, String username, String password, String nombre, String apellidos,
                 String dni, String direccion, Integer telefono1, Integer telefono2, int tipoId, byte[] argazkia) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.direccion = direccion;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.tipoId = tipoId;
        this.argazkia = argazkia;
    }

    public String informacionPersonalP() {
        return
                "Email: " + email + "\n" +
                "Nombre: " + nombre + "\n" +
                "Apellidos: " + apellidos + "\n" +
                "DNI: " + dni + "\n" +
                "Dirección: " + direccion + "\n" +
                "Teléfono 1: " + telefono1 + "\n" ;
    }

    public String informacionPersonalA() {
        return
                "Email: " + email + "\n" +
                        "Nombre: " + nombre + "\n" +
                        "Apellidos: " + apellidos + "\n" +
                        "DNI: " + dni + "\n" +
                        "Dirección: " + direccion + "\n" +
                        "Teléfono 1: " + telefono1 + "\n"+
                        "Ciclo: " + cicloNombre + "\n" +
                        "Curso : " + curso + "\n"
                ;
    }
        // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(Integer telefono1) {
        this.telefono1 = telefono1;
    }

    public Integer getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(Integer telefono2) {
        this.telefono2 = telefono2;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public byte[] getArgazkia() {
        return argazkia;
    }

    public void setArgazkia(byte[] argazkia) {
        this.argazkia = argazkia;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", dni='" + dni + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono1=" + telefono1 +
                ", telefono2=" + telefono2 +
                ", tipoId=" + tipoId +
                ", argazkia=" + (argazkia != null ? "[binary data]" : "null") +
                '}';
    }
}
