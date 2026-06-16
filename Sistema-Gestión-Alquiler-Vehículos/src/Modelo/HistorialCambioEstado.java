package Modelo;

import java.time.LocalDate;

public class HistorialCambioEstado {
    private int id;
    private LocalDate fecha;
    private String estadoAnterior;
    private String estadoNuevo;
    private String tipoEntidad;
    private String referencia;
    private String usuarioResponsable;

    public HistorialCambioEstado(int id, LocalDate fecha, String estadoAnterior,
            String estadoNuevo, String tipoEntidad,
            String referencia, String usuarioResponsable) {
        this.id = id;
        this.fecha = fecha;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.tipoEntidad = tipoEntidad;
        this.referencia = referencia;
        this.usuarioResponsable = usuarioResponsable;
    }

    // GETTERS
    public int getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public String getTipoEntidad() {
        return tipoEntidad;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    @Override
    public String toString() {
        return "HistorialCambioEstado{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", estadoAnterior='" + estadoAnterior + '\'' +
                ", estadoNuevo='" + estadoNuevo + '\'' +
                ", tipoEntidad='" + tipoEntidad + '\'' +
                ", referencia='" + referencia + '\'' +
                ", usuarioResponsable='" + usuarioResponsable + '\'' +
                '}';
    }
}
