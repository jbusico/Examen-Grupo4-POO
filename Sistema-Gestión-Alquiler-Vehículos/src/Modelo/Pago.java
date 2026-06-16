package Modelo;

import Enums.EstadoPago;
import Enums.MedioPago;
import Enums.TipoPago;
import java.time.LocalDate;

public class Pago {
    private int id;
    private LocalDate fecha;
    private double importe;
    private MedioPago medioPago;
    private EstadoPago estado;
    private String usuarioRegistrador;
    private TipoPago tipoPago;

    public Pago(int id, double importe, MedioPago medioPago, String usuarioRegistrador, TipoPago tipoPago) {
        this.id = id;
        this.fecha = LocalDate.now();
        this.importe = importe;
        this.medioPago = medioPago;
        this.estado = EstadoPago.REGISTRADO;
        this.usuarioRegistrador = usuarioRegistrador;
        this.tipoPago = tipoPago;
    }

    public void confirmar() {
        this.estado = EstadoPago.CONFIRMADO;
    }

    public void anular() {
        this.estado = EstadoPago.ANULADO;
    }

    public boolean esSeniaConfirmada() {
        return tipoPago == TipoPago.SENIA && estado == EstadoPago.CONFIRMADO;
    }

    public int getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getImporte() {
        return importe;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public String getUsuarioRegistrador() {
        return usuarioRegistrador;
    }

    public TipoPago getTipoPago() {
        return tipoPago;
    }
}