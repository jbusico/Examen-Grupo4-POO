import Controladores.ClienteController;
import Enums.EstadoCliente;
import Modelo.Cliente;

public class ClienteControllerTest {

    private static int pasados = 0;
    private static int fallados = 0;

    public static void main(String[] args) {
        testRegistrarClienteOK();
        testRegistrarClienteDuplicadoLanzaExcepcion();
        testRegistrarClienteSinDniLanzaExcepcion();
        testClienteQuedaActivo();
        testCambiarEstadoCliente();

        System.out.println("\n=== Resultado: " + pasados + " pasados, " + fallados + " fallados ===");
    }

    // ─── Tests ────────────────────────────────────────────────────────────────

    static void testRegistrarClienteOK() {
        ClienteController controller = resetController();
        try {
            Cliente cliente = controller.registrarCliente(
                    "12345678", "Juan García", "1123456789", "juan@mail.com", "Calle 123", "SISTEMA");

            afirmar(cliente != null, "El cliente no debería ser null");
            afirmar("12345678".equals(cliente.getDniCuit()), "DNI incorrecto");
            afirmar("Juan García".equals(cliente.getNombre()), "Nombre incorrecto");
            pasar("testRegistrarClienteOK");
        } catch (Exception e) {
            fallar("testRegistrarClienteOK", e.getMessage());
        }
    }

    static void testRegistrarClienteDuplicadoLanzaExcepcion() {
        ClienteController controller = resetController();
        try {
            controller.registrarCliente("99999999", "Ana", "111", "ana@mail.com", "Av 1", "SISTEMA");
            controller.registrarCliente("99999999", "Ana Duplicada", "222", "ana2@mail.com", "Av 2", "SISTEMA");
            fallar("testRegistrarClienteDuplicadoLanzaExcepcion", "Debería haber lanzado excepción");
        } catch (Exception e) {
            pasar("testRegistrarClienteDuplicadoLanzaExcepcion");
        }
    }

    static void testRegistrarClienteSinDniLanzaExcepcion() {
        ClienteController controller = resetController();
        try {
            controller.registrarCliente("", "Sin DNI", "000", "x@mail.com", "Calle 0", "SISTEMA");
            fallar("testRegistrarClienteSinDniLanzaExcepcion", "Debería haber lanzado excepción");
        } catch (Exception e) {
            pasar("testRegistrarClienteSinDniLanzaExcepcion");
        }
    }

    static void testClienteQuedaActivo() {
        ClienteController controller = resetController();
        try {
            Cliente cliente = controller.registrarCliente(
                    "55555555", "María López", "1199999999", "maria@mail.com", "Ruta 9", "SISTEMA");

            afirmar(EstadoCliente.ACTIVO == cliente.getEstado(), "El cliente debería estar ACTIVO");
            pasar("testClienteQuedaActivo");
        } catch (Exception e) {
            fallar("testClienteQuedaActivo", e.getMessage());
        }
    }

    static void testCambiarEstadoCliente() {
        ClienteController controller = resetController();
        try {
            controller.registrarCliente("77777777", "Carlos", "1100000000", "carlos@mail.com", "Belgrano 1", "SISTEMA");
            controller.cambiarEstadoCliente("77777777", EstadoCliente.INACTIVO, "SISTEMA");

            Cliente cliente = controller.obtenerClientePorDni("77777777").orElse(null);
            afirmar(cliente != null, "El cliente no debería ser null");
            afirmar(EstadoCliente.INACTIVO == cliente.getEstado(), "El cliente debería estar INACTIVO");
            pasar("testCambiarEstadoCliente");
        } catch (Exception e) {
            fallar("testCambiarEstadoCliente", e.getMessage());
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private static ClienteController resetController() {
        ClienteController c = ClienteController.getInstance();
        c.resetParaTests();
        return c;
    }

    private static void afirmar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }

    private static void pasar(String nombre) {
        pasados++;
        System.out.println("  PASÓ  " + nombre);
    }

    private static void fallar(String nombre, String motivo) {
        fallados++;
        System.out.println("  FALLÓ " + nombre + " → " + motivo);
    }
}
