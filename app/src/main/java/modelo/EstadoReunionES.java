package modelo;

public enum EstadoReunionES {
    PENDIENTE("Pendiente"),
    ACEPTADA("Aceptada"),
    DENEGADA("Denegada"),
    CONFLICTO("Conflicto");

    private final String descripcion;
    EstadoReunionES(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getDescripcion() {
        return descripcion;
    }
}
