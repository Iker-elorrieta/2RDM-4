package modelo;

public enum EstadoReunionEU {
    ONARTZEKE("Onartzeke"),
    ONARTUTA("Onartuta"),
    EZEZTAZUTA("Ezeztatuta"),
    GATASKA("Gatazka");

    private final String descripcion;

    EstadoReunionEU(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
