package Modelo;

import java.time.LocalDate;

public class AlquilerCorporativo extends Alquiler {

    private double porcentajeDescuento;

    public AlquilerCorporativo(int idAlquiler, Cliente cliente, Vehiculo vehiculo,
                               LocalDate fechaInicio, LocalDate fechaDevolucionEstimada,
                               double señaAbonada) {
        super(idAlquiler, cliente, vehiculo, fechaInicio, fechaDevolucionEstimada, señaAbonada);
        this.porcentajeDescuento = 0.10; 
    }

    @Override

    public double calcularImporteTotal() {
        // Reutiliza el importe base y le aplica el descuento polimórfico
        double importeConDescuento = this.calcularImporteBase() * (1 - this.obtenerPorcentaje());

        double costoExcedente = 0.0;
        if (this.getVehiculo().getCostoKmExcedente() != null) {
            costoExcedente = this.getKilometrosExcedentes() * this.getVehiculo().getCostoKmExcedente();
        }

        return importeConDescuento + costoExcedente;
    }

    @Override
    protected double obtenerPorcentaje() {
        return this.porcentajeDescuento;
    }


    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
}