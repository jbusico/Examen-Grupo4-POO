package Controladores;

import Modelo.*;
import Enums.EstadoAlquiler;
import Enums.TipoVehiculo;
import Enums.EstadoVehiculo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlquilerController {
    private List<Alquiler> alquileres;
    private List<HistorialCambioEstado> historial;
    private int contadorAlquiler;
    private int contadorHistorial;

    private static AlquilerController INSTANCE;

    private AlquilerController() {
        this.alquileres = new ArrayList<>();
        this.historial = new ArrayList<>();
        this.contadorAlquiler = 0;
        this.contadorHistorial = 0;
    }

    public static synchronized AlquilerController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AlquilerController();
        }
        return INSTANCE;
    }

    /**
     * UC3: Solicitar un alquiler
     * Crea una instancia de Alquiler según el tipo especificado
     */
    public Alquiler solicitarAlquiler(Cliente cliente, Vehiculo vehiculo,
            LocalDate fechaInicio, LocalDate fechaDevolucion,
            String tipoAlquiler, String usuario) throws Exception {

        // Validar cliente
        if (cliente == null) {
            throw new Exception("Error: Cliente no puede ser nulo");
        }

        // Validar vehículo
        if (vehiculo == null) {
            throw new Exception("Error: Vehículo no puede ser nulo");
        }

        // Validar disponibilidad del vehículo
        if (!estaVehiculoDisponible(vehiculo, fechaInicio, fechaDevolucion)) {
            throw new Exception("Error: El vehículo no está disponible en el período solicitado");
        }

        // Validar fechas
        if (fechaInicio.isBefore(LocalDate.now()) || fechaDevolucion.isBefore(fechaInicio)) {
            throw new Exception("Error: Fechas inválidas");
        }

        // Crear alquiler según el tipo
        Alquiler alquiler;
        contadorAlquiler++;

        // ============================================================================
        // SECCIÓN: INSTANCIACIÓN DE CLASES HIJO DE ALQUILER
        // ============================================================================
        // Aquí se crean las instancias de Alquiler según el tipo
        // Las clases AlquilerComun, AlquilerCorporativo y AlquilerTuristico
        // deben estar en el paquete Modelo
        // ============================================================================

        if ("COMUN".equalsIgnoreCase(tipoAlquiler)) {
            // TODO: Cuando AlquilerComun.java esté creada, descomenta esta línea:
            // alquiler = new AlquilerComun(contadorAlquiler, cliente, vehiculo,
            // fechaInicio, fechaDevolucion, 0);
            alquiler = null; // TEMPORAL

        } else if ("CORPORATIVO".equalsIgnoreCase(tipoAlquiler)) {
            // TODO: Cuando AlquilerCorporativo.java esté creada, descomenta esta línea:
            // alquiler = new AlquilerCorporativo(contadorAlquiler, cliente, vehiculo,
            // fechaInicio, fechaDevolucion, 0);
            alquiler = null; // TEMPORAL

        } else if ("TURISTICO".equalsIgnoreCase(tipoAlquiler)) {
            // TODO: Cuando AlquilerTuristico.java esté creada, descomenta esta línea:
            // alquiler = new AlquilerTuristico(contadorAlquiler, cliente, vehiculo,
            // fechaInicio, fechaDevolucion, 0);
            alquiler = null; // TEMPORAL

        } else {
            throw new Exception("Error: Tipo de alquiler inválido: " + tipoAlquiler);
        }

        // ============================================================================
        // FIN SECCIÓN: INSTANCIACIÓN DE CLASES HIJO DE ALQUILER
        // ============================================================================

        if (alquiler == null) {
            throw new Exception("Error: No se pudo crear el alquiler");
        }

        // Establecer estado como INGRESADO
        alquiler.setEstado(EstadoAlquiler.INGRESADO);

        // Agregar a la lista
        alquileres.add(alquiler);

        // Registrar en historial
        registrarCambioEstado("*", EstadoAlquiler.INGRESADO.toString(),
                "ALQUILER", String.valueOf(alquiler.getIdAlquiler()), usuario);

        return alquiler;
    }

    /**
     * Confirmar la reserva del vehículo (después de pagar la seña)
     */
    public void confirmarAlquiler(int idAlquiler, double montoSeña, String usuario) throws Exception {
        Alquiler alquiler = obtenerAlquilerPorId(idAlquiler)
                .orElseThrow(() -> new Exception("Error: Alquiler no encontrado"));

        if (alquiler.getEstado() != EstadoAlquiler.INGRESADO) {
            throw new Exception("Error: El alquiler debe estar en estado INGRESADO para confirmarse");
        }

        // Cambiar estado a CONFIRMADO
        cambiarEstadoAlquiler(idAlquiler, EstadoAlquiler.CONFIRMADO, usuario);
    }

    /**
     * UC4: Finalizar alquiler y calcular saldo
     * Calcula el monto final considerando recargos, descuentos y km excedentes
     */
    public double finalizarAlquilerYCalcularSaldo(int idAlquiler, int kilometrajeFinal,
            LocalDate fechaDevolucionReal,
            String usuario) throws Exception {

        Alquiler alquiler = obtenerAlquilerPorId(idAlquiler)
                .orElseThrow(() -> new Exception("Error: Alquiler no encontrado"));

        if (alquiler.getEstado() != EstadoAlquiler.EN_CURSO) {
            throw new Exception("Error: El alquiler debe estar en estado EN_CURSO");
        }

        // Actualizar datos de devolución
        alquiler.setFechaDevolucionReal(fechaDevolucionReal);
        alquiler.setKilometrajeFinal(kilometrajeFinal);
        alquiler.getVehiculo().setKilometraje(kilometrajeFinal);

        // Calcular importe total
        // NOTA: El cálculo varía según el tipo de alquiler (Común, Corporativo,
        // Turístico)
        // Cada subclase de Alquiler implementa calcularImporteTotal() diferente
        double importeTotal = alquiler.calcularImporteTotal();
        double importePago = importeTotal - alquiler.getSeñaAbonada();

        // Cambiar estado a FINALIZADO
        cambiarEstadoAlquiler(idAlquiler, EstadoAlquiler.FINALIZADO, usuario);

        // Cambiar estado del vehículo a DISPONIBLE
        VehiculoController.getInstance().cambiarEstadoVehiculo(
                alquiler.getVehiculo().getPatente(),
                EstadoVehiculo.DISPONIBLE,
                usuario);

        return importePago;
    }

    /**
     * Cambiar estado de un alquiler
     */
    private void cambiarEstadoAlquiler(int idAlquiler, EstadoAlquiler nuevoEstado, String usuario) throws Exception {
        Alquiler alquiler = obtenerAlquilerPorId(idAlquiler)
                .orElseThrow(() -> new Exception("Error: Alquiler no encontrado"));

        EstadoAlquiler estadoAnterior = alquiler.getEstado();
        alquiler.setEstado(nuevoEstado);

        registrarCambioEstado(estadoAnterior.toString(), nuevoEstado.toString(),
                "ALQUILER", String.valueOf(idAlquiler), usuario);
    }

    /**
     * Obtener alquiler por ID
     */
    public Optional<Alquiler> obtenerAlquilerPorId(int idAlquiler) {
        return alquileres.stream()
                .filter(a -> a.getIdAlquiler() == idAlquiler)
                .findFirst();
    }

    /**
     * Obtener alquileres confirmados de un cliente
     */
    public List<Alquiler> obtenerAlquilersConfirmadosDelCliente(Cliente cliente) {
        List<Alquiler> resultado = new ArrayList<>();
        for (Alquiler a : alquileres) {
            if (a.getCliente().getDniCuit().equals(cliente.getDniCuit()) &&
                    a.getEstado() == EstadoAlquiler.CONFIRMADO) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    /**
     * UC5: Consultar vehículos disponibles
     * Retorna lista de vehículos disponibles en un período y tipo específico
     */
    public List<Vehiculo> consultarVehiculosDisponibles(LocalDate fechaInicio, LocalDate fechaFin,
            TipoVehiculo tipoVehiculo) {
        VehiculoController vehiculoController = VehiculoController.getInstance();
        List<Vehiculo> vehiculosDelTipo = vehiculoController.obtenerVehiculosPorTipo(tipoVehiculo);

        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo v : vehiculosDelTipo) {
            if (estaVehiculoDisponible(v, fechaInicio, fechaFin)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    /**
     * Verificar si un vehículo está disponible en un período determinado
     */
    private boolean estaVehiculoDisponible(Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaDevolucion) {
        for (Alquiler a : alquileres) {
            if (!a.getVehiculo().getPatente().equals(vehiculo.getPatente())) {
                continue;
            }

            if (a.getEstado() == EstadoAlquiler.CANCELADO || a.getEstado() == EstadoAlquiler.FINALIZADO) {
                continue;
            }

            // Verificar solapamiento de fechas
            if (!(fechaDevolucion.isBefore(a.getFechaInicio()) ||
                    fechaInicio.isAfter(a.getFechaDevolucionEstimada()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Registrar cambio de estado en el historial de auditoría
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
     * Obtener historial de cambios de estado
     */
    public List<HistorialCambioEstado> obtenerHistorial() {
        return new ArrayList<>(historial);
    }

    /**
     * Obtener todos los alquileres registrados
     */
    public List<Alquiler> obtenerTodosLosAlquileres() {
        return new ArrayList<>(alquileres);
    }
}
