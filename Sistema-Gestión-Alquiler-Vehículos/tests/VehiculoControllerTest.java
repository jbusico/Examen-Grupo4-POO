import Controladores.VehiculoController;
import Enums.EstadoVehiculo;
import Enums.TipoCombustible;
import Enums.TipoVehiculo;
import Modelo.Vehiculo;

public class VehiculoControllerTest {

    private static int pasados = 0;
    private static int fallados = 0;

    public static void main(String[] args) {
        testRegistrarVehiculoOK();
        testRegistrarVehiculoDuplicadoLanzaExcepcion();
        testRegistrarVehiculoSinPatenteLanzaExcepcion();
        testVehiculoQuedaDisponible();
        testCambiarEstadoVehiculo();

        System.out.println("\n=== Resultado: " + pasados + " pasados, " + fallados + " fallados ===");
    }

    // ─── Tests ────────────────────────────────────────────────────────────────

    static void testRegistrarVehiculoOK() {
        VehiculoController controller = resetController();
        try {
            Vehiculo vehiculo = controller.registrarVehiculo(
                    "ABC123", "Toyota", "Corolla", 2023, 1000,
                    150.0, TipoCombustible.NAFTA, TipoVehiculo.AUTO, 200, 5.0, "SISTEMA");

            afirmar(vehiculo != null, "El vehículo no debería ser null");
            afirmar("ABC123".equals(vehiculo.getPatente()), "Patente incorrecta");
            afirmar("Toyota".equals(vehiculo.getMarca()), "Marca incorrecta");
            pasar("testRegistrarVehiculoOK");
        } catch (Exception e) {
            fallar("testRegistrarVehiculoOK", e.getMessage());
        }
    }

    static void testRegistrarVehiculoDuplicadoLanzaExcepcion() {
        VehiculoController controller = resetController();
        try {
            controller.registrarVehiculo("DUP001", "Ford", "Focus", 2022, 500,
                    100.0, TipoCombustible.NAFTA, TipoVehiculo.AUTO, null, null, "SISTEMA");
            controller.registrarVehiculo("DUP001", "Honda", "Civic", 2021, 300,
                    90.0, TipoCombustible.NAFTA, TipoVehiculo.AUTO, null, null, "SISTEMA");
            fallar("testRegistrarVehiculoDuplicadoLanzaExcepcion", "Debería haber lanzado excepción");
        } catch (Exception e) {
            pasar("testRegistrarVehiculoDuplicadoLanzaExcepcion");
        }
    }

    static void testRegistrarVehiculoSinPatenteLanzaExcepcion() {
        VehiculoController controller = resetController();
        try {
            controller.registrarVehiculo("", "Ford", "Focus", 2022, 500,
                    100.0, TipoCombustible.NAFTA, TipoVehiculo.AUTO, null, null, "SISTEMA");
            fallar("testRegistrarVehiculoSinPatenteLanzaExcepcion", "Debería haber lanzado excepción");
        } catch (Exception e) {
            pasar("testRegistrarVehiculoSinPatenteLanzaExcepcion");
        }
    }

    static void testVehiculoQuedaDisponible() {
        VehiculoController controller = resetController();
        try {
            Vehiculo vehiculo = controller.registrarVehiculo(
                    "XYZ789", "Chevrolet", "Cruze", 2023, 2000,
                    120.0, TipoCombustible.DIESEL, TipoVehiculo.AUTO, null, null, "SISTEMA");

            afirmar(EstadoVehiculo.DISPONIBLE == vehiculo.getEstado(),
                    "El vehículo debería quedar en estado DISPONIBLE");
            pasar("testVehiculoQuedaDisponible");
        } catch (Exception e) {
            fallar("testVehiculoQuedaDisponible", e.getMessage());
        }
    }

    static void testCambiarEstadoVehiculo() {
        VehiculoController controller = resetController();
        try {
            controller.registrarVehiculo("MNT001", "Renault", "Sandero", 2020, 5000,
                    80.0, TipoCombustible.GNC, TipoVehiculo.AUTO, null, null, "SISTEMA");
            controller.cambiarEstadoVehiculo("MNT001", EstadoVehiculo.MANTENIMIENTO, "SISTEMA");

            Vehiculo vehiculo = controller.obtenerVehiculoPorPatente("MNT001").orElse(null);
            afirmar(vehiculo != null, "El vehículo no debería ser null");
            afirmar(EstadoVehiculo.MANTENIMIENTO == vehiculo.getEstado(),
                    "El vehículo debería estar en MANTENIMIENTO");
            pasar("testCambiarEstadoVehiculo");
        } catch (Exception e) {
            fallar("testCambiarEstadoVehiculo", e.getMessage());
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private static VehiculoController resetController() {
        VehiculoController c = VehiculoController.getInstance();
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
