package Modelo;

import Enums.EstadoAlquiler;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Alquiler {
    private int idAlquiler;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucionEstimada;
    private LocalDate fechaDevolucionReal;
    private EstadoAlquiler estado;
    private double señaAbonada;
    private List<Pago> pagos;
    private int kilometrajeInicial;
    private int kilometrajeFinal;

    public Alquiler(int idAlquiler, Cliente cliente, Vehiculo vehiculo,
            LocalDate fechaInicio, LocalDate fechaDevolucionEstimada,
            double señaAbonada) {
        this.idAlquiler = idAlquiler;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fechaInicio = fechaInicio;
        this.fechaDevolucionEstimada = fechaDevolucionEstimada;
        this.señaAbonada = señaAbonada;
        this.estado = EstadoAlquiler.INGRESADO;
        this.pagos = new ArrayList<>();
        this.kilometrajeInicial = vehiculo.getKilometraje();
    }


    public double calcularImporteBase() {
        return this.getCantidadDias() * this.getVehiculo().getValorDiario();
    }

    public abstract double calcularImporteTotal();

    protected abstract double obtenerPorcentaje();

    public int getCantidadDias() {
        if (fechaDevolucionReal != null) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaDevolucionReal);
        }
        return (int) java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaDevolucionEstimada);
    }

    public int getKilometrosExcedentes() {
        if (kilometrajeFinal == 0 || vehiculo.getKmMaximoDiario() == null) {
            return 0;
        }
        int kmMaximoTotal = vehiculo.getKmMaximoDiario() * getCantidadDias();
        int kmRecorridos = kilometrajeFinal - kilometrajeInicial;
        return Math.max(0, kmRecorridos - kmMaximoTotal);
    }

    public int getIdAlquiler() {
        return idAlquiler;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaDevolucionEstimada() {
        return fechaDevolucionEstimada;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public EstadoAlquiler getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlquiler estado) {
        this.estado = estado;
    }

    public double getSeñaAbonada() {
        return señaAbonada;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void agregarPago(Pago pago) {
        pagos.add(pago);
    }

    public int getKilometrajeInicial() {
        return kilometrajeInicial;
    }

    public int getKilometrajeFinal() {
        return kilometrajeFinal;
    }

    public void setKilometrajeFinal(int kilometrajeFinal) {
        this.kilometrajeFinal = kilometrajeFinal;
    }
}
