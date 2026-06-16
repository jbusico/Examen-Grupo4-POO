package Modelo;

import Enums.EstadoAlquiler;
import Enums.EstadoVehiculo;
import Enums.TipoCombustible;
import Enums.TipoVehiculo;
import java.time.LocalDate;
import java.util.List;

public class Vehiculo {
    private String patente;
    private String marca;
    private String modelo;
    private int año;
    private int kilometraje;
    private EstadoVehiculo estado;
    private double valorDiario;
    private TipoCombustible tipoCombustible;
    private TipoVehiculo tipoVehiculo;
    private Integer kmMaximoDiario;
    private Double costoKmExcedente;

    public Vehiculo(String patente, String marca, String modelo, int año,
            int kilometraje, double valorDiario,
            TipoCombustible tipoCombustible, TipoVehiculo tipoVehiculo,
            Integer kmMaximoDiario, Double costoKmExcedente) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.kilometraje = kilometraje;
        this.valorDiario = valorDiario;
        this.tipoCombustible = tipoCombustible;
        this.tipoVehiculo = tipoVehiculo;
        this.estado = EstadoVehiculo.DISPONIBLE;
        this.kmMaximoDiario = kmMaximoDiario;
        this.costoKmExcedente = costoKmExcedente;
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAño() {
        return año;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    public double getValorDiario() {
        return valorDiario;
    }

    public TipoCombustible getTipoCombustible() {
        return tipoCombustible;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public Integer getKmMaximoDiario() {
        return kmMaximoDiario;
    }

    public Double getCostoKmExcedente() {
        return costoKmExcedente;
    }

    public void cambiarEstado(EstadoVehiculo nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void actualizarKilometraje(int nuevoKilometraje) {
        this.kilometraje = nuevoKilometraje;
    }

    public boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin, List<Alquiler> alquileres) {
        if (this.estado != EstadoVehiculo.DISPONIBLE) return false;
        for (Alquiler a : alquileres) {
            if (a.correspondeAVehiculo(this)
                    && a.getEstado() != EstadoAlquiler.CANCELADO
                    && a.getEstado() != EstadoAlquiler.FINALIZADO
                    && a.seSuperpone(fechaInicio, fechaFin)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "patente='" + patente + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", estado=" + estado +
                '}';
    }
}