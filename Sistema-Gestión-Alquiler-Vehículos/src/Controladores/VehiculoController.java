package Controladores;

import Modelo.Vehiculo;
import Modelo.HistorialCambioEstado;
import Enums.EstadoVehiculo;
import Enums.TipoCombustible;
import Enums.TipoVehiculo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehiculoController {
    private List<Vehiculo> vehiculos;
    private List<HistorialCambioEstado> historial;
    private int contadorHistorial;

    private static VehiculoController INSTANCE;

    private VehiculoController() {
        this.vehiculos = new ArrayList<>();
        this.historial = new ArrayList<>();
        this.contadorHistorial = 0;
    }

    public static synchronized VehiculoController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VehiculoController();
        }
        return INSTANCE;
    }

    /**
     * UC2: Registrar un nuevo vehículo
     */
    public Vehiculo registrarVehiculo(String patente, String marca, String modelo,
            int año, int kilometraje, double valorDiario,
            TipoCombustible tipoCombustible, TipoVehiculo tipoVehiculo,
            Integer kmMaximoDiario, Double costoKmExcedente,
            String usuario) throws Exception {

        // Validar que la patente no exista
        if (existeVehiculoConPatente(patente)) {
            throw new Exception("Error: Ya existe un vehículo con patente: " + patente);
        }

        // Validar datos obligatorios
        if (patente == null || patente.isEmpty()) {
            throw new Exception("Error: La patente es obligatoria");
        }

        // Crear nuevo vehículo
        Vehiculo vehiculo = new Vehiculo(patente, marca, modelo, año, kilometraje,
                valorDiario, tipoCombustible, tipoVehiculo, kmMaximoDiario, costoKmExcedente);

        // Agregar a la lista
        vehiculos.add(vehiculo);

        // Cambiar estado a DISPONIBLE
        cambiarEstadoVehiculo(patente, EstadoVehiculo.DISPONIBLE, usuario);

        return vehiculo;
    }

    /**
     * Obtener un vehículo por patente
     */
    public Optional<Vehiculo> obtenerVehiculoPorPatente(String patente) {
        return vehiculos.stream()
                .filter(v -> v.getPatente().equals(patente))
                .findFirst();
    }

    /**
     * Obtener todos los vehículos
     */
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    /**
     * Obtener vehículos por tipo
     */
    public List<Vehiculo> obtenerVehiculosPorTipo(TipoVehiculo tipo) {
        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            if (v.getTipoVehiculo() == tipo) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    /**
     * Cambiar estado de un vehículo
     */
    public void cambiarEstadoVehiculo(String patente, EstadoVehiculo nuevoEstado, String usuario) throws Exception {
        Optional<Vehiculo> vehiculoOpt = obtenerVehiculoPorPatente(patente);

        if (!vehiculoOpt.isPresent()) {
            throw new Exception("Error: No existe vehículo con patente: " + patente);
        }

        Vehiculo vehiculo = vehiculoOpt.get();
        EstadoVehiculo estadoAnterior = vehiculo.getEstado();

        vehiculo.setEstado(nuevoEstado);

        // Registrar en el historial
        registrarCambioEstado(estadoAnterior.toString(), nuevoEstado.toString(),
                "VEHICULO", patente, usuario);
    }

    /**
     * Verificar si existe un vehículo con una patente
     */
    private boolean existeVehiculoConPatente(String patente) {
        return vehiculos.stream().anyMatch(v -> v.getPatente().equals(patente));
    }

    /**
     * Registrar cambio de estado en el historial
     */
    private void registrarCambioEstado(String estadoAnterior, String estadoNuevo,
            String tipoEntidad, String referencia, String usuario) {
        contadorHistorial++;
        HistorialCambioEstado cambio = new HistorialCambioEstado(
                contadorHistorial, LocalDate.now(), estadoAnterior, estadoNuevo,
                tipoEntidad, referencia, usuario);
        historial.add(cambio);
    }

    /**
     * Obtener historial de cambios
     */
    public List<HistorialCambioEstado> obtenerHistorial() {
        return new ArrayList<>(historial);
    }
}
