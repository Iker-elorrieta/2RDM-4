package modelo;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class ReunionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idReunion;
    private Integer usersByProfesorId;  // Changed to Integer (ID of Profesor)
    private Integer usersByAlumnoId;    // Changed to Integer (ID of Alumno)
    private String nombreP;              // Name of the Profesor
    private String nombreA;              // Name of the Alumno
    private String estado;
    private String estadoEus;
    private String idCentro;
    private String titulo;
    private String asunto;
    private String aula;
    private Timestamp fecha;

    public ReunionDto() {
    }

    public ReunionDto(Integer usersByProfesorId, Integer usersByAlumnoId, String nombreP, String nombreA) {
        this.usersByProfesorId = usersByProfesorId;
        this.usersByAlumnoId = usersByAlumnoId;
        this.nombreP = nombreP;
        this.nombreA = nombreA;
    }

    public ReunionDto(Integer usersByProfesorId, Integer usersByAlumnoId, String nombreP, String nombreA, String estado,
                      String estadoEus, String idCentro, String titulo, String asunto, String aula, Timestamp fecha) {
        this.usersByProfesorId = usersByProfesorId;
        this.usersByAlumnoId = usersByAlumnoId;
        this.nombreP = nombreP;
        this.nombreA = nombreA;
        this.estado = estado;
        this.estadoEus = estadoEus;
        this.idCentro = idCentro;
        this.titulo = titulo;
        this.asunto = asunto;
        this.aula = aula;
        this.fecha = fecha;
    }

    public Integer getIdReunion() {
        return idReunion;
    }

    public void setIdReunion(Integer idReunion) {
        this.idReunion = idReunion;
    }

    public Integer getUsersByProfesorId() {
        return usersByProfesorId;
    }

    public void setUsersByProfesorId(Integer usersByProfesorId) {
        this.usersByProfesorId = usersByProfesorId;
    }

    public Integer getUsersByAlumnoId() {
        return usersByAlumnoId;
    }

    public void setUsersByAlumnoId(Integer usersByAlumnoId) {
        this.usersByAlumnoId = usersByAlumnoId;
    }

    public String getNombreP() {
        return nombreP;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public String getNombreA() {
        return nombreA;
    }

    public void setNombreA(String nombreA) {
        this.nombreA = nombreA;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoEus() {
        return estadoEus;
    }

    public void setEstadoEus(String estadoEus) {
        this.estadoEus = estadoEus;
    }

    public String getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(String idCentro) {
        this.idCentro = idCentro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }



@SuppressLint("NewApi")
    public String[][] getModeloReuniones(ArrayList<ReunionDto> reuniones) {
        // TODO Auto-generated method stub
        String[][] planSemanal = { { "1ra", "", "", "", "", "" }, { "2da", "", "", "", "", "" },
                { "3ra", "", "", "", "", "" }, { "4ta", "", "", "", "", "" }, { "5ta", "", "", "", "", "" } };

        for (int i = 0; i < reuniones.size(); i++) {
            ReunionDto reunion = (ReunionDto) reuniones.get(i);

        LocalDateTime fechaReunion = reunion.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            int hora = consegiorHora(fechaReunion.getHour());
            int diaSemana = fechaReunion.getDayOfWeek().getValue(); // Lunes = 1, Domingo = 7

            planSemanal[hora][diaSemana] = reunion.getIdReunion() + ";" + reunion.getTitulo() + ponerEstado(reunion.getEstado());
        }


        return planSemanal;
    }
    private String ponerEstado(String estado) {
        // TODO Auto-generated method stub

        if (estado.equalsIgnoreCase("pendiente")) {
            estado = "-P";
        } else if (estado.equalsIgnoreCase("aceptada")) {
            estado = "-C";
        } else if (estado.equalsIgnoreCase("denegada")) {
            estado = "-R";
        } else if (estado.equalsIgnoreCase("conflicto")) {
            estado = "-E";
        }
        return estado;
    }

    private int consegiorHora(int hora) {
        // TODO Auto-generated method stub
        if (hora == 8) {
            hora = 0;
        } else if (hora == 9) {
            hora = 1;
        } else if (hora == 10) {
            hora = 2;
        } else if (hora == 11) {
            hora = 3;
        } else if (hora == 12) {
            hora = 4;
        }
        return hora;
    }

}
