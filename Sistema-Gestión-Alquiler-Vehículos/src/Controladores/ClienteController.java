package Controladores;

import Modelo.Cliente;
import Modelo.HistorialCambioEstado;
import Enums.EstadoCliente;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteController {
    private List<Cliente> clientes;
    private List<HistorialCambioEstado> historial;
    private int contadorHistorial;

    private static ClienteController INSTANCE;

    private ClienteController() {
        this.clientes = new ArrayList<>();
        this.historial = new ArrayList<>();
        this.contadorHistorial = 0;
    }

    public static synchronized ClienteController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClienteController();
        }
        return INSTANCE;
    }

    /**
     * UC1: Registrar un nuevo cliente
     */
    public Cliente registrarCliente(String dniCuit, String nombre,
            String telefono, String email,
            String direccion, String usuario) throws Exception {

        // Validar que el DNI/CUIT no exista
        if (existeClienteConDni(dniCuit)) {
            throw new Exception("Error: Ya existe un cliente con DNI/CUIT: " + dniCuit);
        }

        // Validar datos obligatorios
        if (dniCuit == null || dniCuit.isEmpty() || nombre == null || nombre.isEmpty()) {
            throw new Exception("Error: DNI/CUIT y nombre son obligatorios");
        }

        // Crear nuevo cliente
        Cliente cliente = new Cliente(dniCuit, nombre, telefono, email, direccion);

        // Agregar a la lista
        clientes.add(cliente);

        // Registrar en el historial
        registrarCambioEstado("*", EstadoCliente.ACTIVO.toString(),
                "CLIENTE", dniCuit, usuario);

        return cliente;
    }

    /**
     * Obtener un cliente por DNI/CUIT
     */
    public Optional<Cliente> obtenerClientePorDni(String dniCuit) {
        return clientes.stream()
                .filter(c -> c.getDniCuit().equals(dniCuit))
                .findFirst();
    }

    /**
     * Obtener todos los clientes
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(clientes);
    }

    /**
     * Cambiar estado de un cliente
     */
    public void cambiarEstadoCliente(String dniCuit, EstadoCliente nuevoEstado, String usuario) throws Exception {
        Optional<Cliente> clienteOpt = obtenerClientePorDni(dniCuit);

        if (!clienteOpt.isPresent()) {
            throw new Exception("Error: No existe cliente con DNI/CUIT: " + dniCuit);
        }

        Cliente cliente = clienteOpt.get();
        EstadoCliente estadoAnterior = cliente.getEstado();

        cliente.setEstado(nuevoEstado);

        // Registrar en el historial
        registrarCambioEstado(estadoAnterior.toString(), nuevoEstado.toString(),
                "CLIENTE", dniCuit, usuario);
    }

    /**
     * Verificar si existe un cliente con un DNI/CUIT
     */
    private boolean existeClienteConDni(String dniCuit) {
        return clientes.stream().anyMatch(c -> c.getDniCuit().equals(dniCuit));
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

    public void resetParaTests() {
        clientes.clear();
        historial.clear();
        contadorHistorial = 0;
    }
}
