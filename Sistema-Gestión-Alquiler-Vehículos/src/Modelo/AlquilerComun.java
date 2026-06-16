package Modelo;

import java.time.LocalDate;

public class AlquilerComun extends Alquiler {

    public AlquilerComun(int idAlquiler, Cliente cliente, Vehiculo vehiculo,
                         LocalDate fechaInicio, LocalDate fechaDevolucionEstimada,
                         double señaAbonada) {
        super(idAlquiler, cliente, vehiculo, fechaInicio, fechaDevolucionEstimada, señaAbonada);
    }

    @Override
    public double calcularImporteTotal() {
        double costoExcedente = 0.0;
        if (this.getVehiculo().getCostoKmExcedente() != null) {
            costoExcedente = this.getKilometrosExcedentes() * this.getVehiculo().getCostoKmExcedente();
        }
        return this.calcularImporteBase() + costoExcedente;
    }
    @Override
    public double obtenerPorcentaje() { 
        return 0.0;
    }
}