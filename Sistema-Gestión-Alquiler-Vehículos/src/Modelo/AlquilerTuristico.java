package Modelo;

import java.time.LocalDate;

public class AlquilerTuristico extends Alquiler {

    private double porcentajeRecargo;

    public AlquilerTuristico(int idAlquiler, Cliente cliente, Vehiculo vehiculo,
                             LocalDate fechaInicio, LocalDate fechaDevolucionEstimada,
                             double señaAbonada) {
        super(idAlquiler, cliente, vehiculo, fechaInicio, fechaDevolucionEstimada, señaAbonada);
        this.porcentajeRecargo = 0.15; 
    }


    @Override
    public double calcularImporteTotal() {

        double importeBase = this.calcularImporteBase();

        double recargo = importeBase * this.obtenerPorcentaje();
        double importeConRecargo = importeBase + recargo;

        double costoExcedente = 0.0;
        if (this.getVehiculo().getCostoKmExcedente() != null) {
            costoExcedente = this.getKilometrosExcedentes() * this.getVehiculo().getCostoKmExcedente();
        }


        return importeConRecargo + costoExcedente;
    }

    @Override
    public double obtenerPorcentaje() {
        return this.porcentajeRecargo;
    }

    public double getPorcentajeRecargo() {
        return porcentajeRecargo;
    }

    public void setPorcentajeRecargo(double porcentajeRecargo) {
        this.porcentajeRecargo = porcentajeRecargo;
    }
}