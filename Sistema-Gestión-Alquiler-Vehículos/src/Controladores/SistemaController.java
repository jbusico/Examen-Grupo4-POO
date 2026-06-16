package Controladores;

import Enums.EstadoAlquiler;
import Enums.EstadoVehiculo;
import Enums.MedioPago;
import Enums.TipoCombustible;
import Enums.TipoEntidad;
import Enums.TipoPago;
import Enums.TipoVehiculo;
import Modelo.Alquiler;
import Modelo.Cliente;
import Modelo.HistorialCambioEstado;
import Modelo.Pago;
import Modelo.Vehiculo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SistemaController {

    private static SistemaController instancia;

    private final List<Cliente> clientes;
    private final List<Vehiculo> vehiculos;
    private final List<Alquiler> alquileres;
    private final List<HistorialCambioEstado> historiales;

    private int contadorAlquiler = 1;
    private int contadorPago = 1;
    private int contadorHistorial = 1;

    private SistemaController() {
        clientes = new ArrayList<>();
        vehiculos = new ArrayList<>();
        alquileres = new ArrayList<>();
        historiales = new ArrayList<>();
    }

    public static SistemaController getInstancia() {
        if (instancia == null) {
            instancia = new SistemaController();
        }
        return instancia;
    }

    // ─── UC1: Registrar Cliente ───────────────────────────────────────────────

    public Cliente registrarCliente(String dniCuit, String nombre, String telefono,
            String email, String direccion, String usuario) {
        for (Cliente c : clientes) {
            if (c.getDniCuit().equals(dniCuit)) {
                throw new IllegalArgumentException("Ya existe un cliente con ese DNI/CUIT: " + dniCuit);
            }
        }

        Cliente cliente = new Cliente(dniCuit, nombre, telefono, email, direccion);
        cliente.activar();
        clientes.add(cliente);

        registrarHistorial("-", "ACTIVO", TipoEntidad.CLIENTE, dniCuit, usuario);
        return cliente;
    }

    // ─── UC2: Registrar Vehículo ──────────────────────────────────────────────

    public Vehiculo registrarVehiculo(String patente, String marca, String modelo, int anio,
            int kilometraje, double valorDiario, TipoVehiculo tipoVehiculo,
            TipoCombustible tipoCombustible, Integer kmIncluidosPorDia,
            Double costoKmExcedente, String usuario) {
        for (Vehiculo v : vehiculos) {
            if (v.getPatente().equals(patente)) {
                throw new IllegalArgumentException("Ya existe un vehículo con esa patente: " + patente);
            }
        }

        Vehiculo vehiculo = new Vehiculo(patente, marca, modelo, anio, kilometraje,
                valorDiario, tipoCombustible, tipoVehiculo, kmIncluidosPorDia, costoKmExcedente);
        vehiculo.cambiarEstado(EstadoVehiculo.DISPONIBLE);
        vehiculos.add(vehiculo);

        registrarHistorial("-", "DISPONIBLE", TipoEntidad.VEHICULO, patente, usuario);
        return vehiculo;
    }

    // ─── UC3: Solicitar Alquiler ──────────────────────────────────────────────

    public Alquiler solicitarAlquiler(String dniCuit, String patente, LocalDate fechaInicio,
            LocalDate fechaDevolucionEstimada, String tipoAlquiler, String usuario) {
        Cliente cliente = buscarCliente(dniCuit);
        Vehiculo vehiculo = buscarVehiculo(patente);

        if (!vehiculo.estaDisponible(fechaInicio, fechaDevolucionEstimada, alquileres)) {
            throw new IllegalStateException("El vehículo no está disponible para el período solicitado.");
        }

        Alquiler alquiler = crearAlquiler(tipoAlquiler, contadorAlquiler++, cliente, vehiculo,
                fechaInicio, fechaDevolucionEstimada);
        alquiler.cambiarEstado(EstadoAlquiler.INGRESADO);
        alquileres.add(alquiler);

        registrarHistorial("-", "INGRESADO", TipoEntidad.ALQUILER,
                String.valueOf(alquiler.getIdAlquiler()), usuario);
        return alquiler;
    }

    // TODO: cuando tus compañeros hagan push de las subclases, reemplazar este método
    // con los imports correspondientes y el switch completo.
    private Alquiler crearAlquiler(String tipoAlquiler, int id, Cliente cliente, Vehiculo vehiculo,
            LocalDate fechaInicio, LocalDate fechaFin) {
        throw new UnsupportedOperationException(
                "Las subclases de Alquiler todavía no están integradas. "
                + "Tipo solicitado: " + tipoAlquiler);

        // Una vez que tus compañeros suban el código, reemplazar por:
        // switch (tipoAlquiler.toUpperCase()) {
        //     case "COMUN":       return new AlquilerComun(id, cliente, vehiculo, fechaInicio, fechaFin, 0);
        //     case "CORPORATIVO": return new AlquilerCorporativo(id, cliente, vehiculo, fechaInicio, fechaFin, 0);
        //     case "TURISTICO":   return new AlquilerTuristico(id, cliente, vehiculo, fechaInicio, fechaFin, 0);
        //     default: throw new IllegalArgumentException("Tipo desconocido: " + tipoAlquiler);
        // }
    }

    // ─── Confirmar Alquiler con Seña ──────────────────────────────────────────

    public Pago confirmarAlquilerConSenia(int idAlquiler, double importe,
            MedioPago medioPago, String usuario) {
        Alquiler alquiler = buscarAlquiler(idAlquiler);

        Pago senia = new Pago(contadorPago++, importe, medioPago, usuario, TipoPago.SENIA);
        senia.confirmar();
        alquiler.agregarPago(senia);
        alquiler.cambiarEstado(EstadoAlquiler.CONFIRMADO);

        registrarHistorial("INGRESADO", "CONFIRMADO", TipoEntidad.ALQUILER,
                String.valueOf(idAlquiler), usuario);
        return senia;
    }

    // ─── UC4: Finalizar Alquiler ──────────────────────────────────────────────

    public double finalizarAlquiler(int idAlquiler, int kilometrajeFinal,
            LocalDate fechaDevolucionReal, String usuario) {
        Alquiler alquiler = buscarAlquiler(idAlquiler);

        alquiler.getVehiculo().actualizarKilometraje(kilometrajeFinal);
        alquiler.setKilometrajeFinal(kilometrajeFinal);
        alquiler.setFechaDevolucionReal(fechaDevolucionReal);

        double importeTotal = alquiler.calcularImporteTotal();

        double seniaAbonada = 0;
        for (Pago p : alquiler.getPagos()) {
            if (p.esSeniaConfirmada()) {
                seniaAbonada += p.getImporte();
            }
        }

        double saldoPendiente = importeTotal - seniaAbonada;
        alquiler.cambiarEstado(EstadoAlquiler.FINALIZADO);

        registrarHistorial("EN_CURSO", "FINALIZADO", TipoEntidad.ALQUILER,
                String.valueOf(idAlquiler), usuario);
        return saldoPendiente;
    }

    // ─── UC5: Consultar Vehículos Disponibles ─────────────────────────────────

    public List<Vehiculo> consultarVehiculosDisponibles(LocalDate fechaInicio, LocalDate fechaFin,
            TipoVehiculo tipoVehiculo) {
        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            if (v.getTipoVehiculo() == tipoVehiculo
                    && v.estaDisponible(fechaInicio, fechaFin, alquileres)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    // ─── Consultas adicionales del enunciado ─────────────────────────────────

    public double totalRecaudadoPorPeriodo(LocalDate desde, LocalDate hasta) {
        double total = 0;
        for (Alquiler a : alquileres) {
            if (a.getEstado() == EstadoAlquiler.FINALIZADO
                    && a.getFechaDevolucionReal() != null
                    && !a.getFechaDevolucionReal().isBefore(desde)
                    && !a.getFechaDevolucionReal().isAfter(hasta)) {
                total += a.calcularImporteTotal();
            }
        }
        return total;
    }

    public List<Alquiler> alquileresConfirmadosPorCliente(String dniCuit) {
        List<Alquiler> resultado = new ArrayList<>();
        for (Alquiler a : alquileres) {
            if (a.getCliente().getDniCuit().equals(dniCuit)
                    && a.getEstado() == EstadoAlquiler.CONFIRMADO) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    // ─── Helpers privados ─────────────────────────────────────────────────────

    private Cliente buscarCliente(String dniCuit) {
        for (Cliente c : clientes) {
            if (c.getDniCuit().equals(dniCuit)) return c;
        }
        throw new IllegalArgumentException("Cliente no encontrado: " + dniCuit);
    }

    private Vehiculo buscarVehiculo(String patente) {
        for (Vehiculo v : vehiculos) {
            if (v.getPatente().equals(patente)) return v;
        }
        throw new IllegalArgumentException("Vehículo no encontrado: " + patente);
    }

    private Alquiler buscarAlquiler(int id) {
        for (Alquiler a : alquileres) {
            if (a.getIdAlquiler() == id) return a;
        }
        throw new IllegalArgumentException("Alquiler no encontrado: " + id);
    }

    private void registrarHistorial(String estadoAnterior, String estadoNuevo,
            TipoEntidad tipoEntidad, String referencia, String usuario) {
        HistorialCambioEstado h = new HistorialCambioEstado(
                contadorHistorial++, LocalDate.now(),
                estadoAnterior, estadoNuevo,
                tipoEntidad.name(), referencia, usuario);
        historiales.add(h);
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public List<Cliente> getClientes() { return clientes; }
    public List<Vehiculo> getVehiculos() { return vehiculos; }
    public List<Alquiler> getAlquileres() { return alquileres; }
    public List<HistorialCambioEstado> getHistoriales() { return historiales; }
}
