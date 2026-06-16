import Controladores.AlquilerController;
import Controladores.ClienteController;
import Controladores.VehiculoController;
import Enums.TipoCombustible;
import Enums.TipoVehiculo;
import Modelo.Alquiler;
import Modelo.AlquilerComun;
import Modelo.AlquilerCorporativo;
import Modelo.AlquilerTuristico;
import Modelo.Cliente;
import Modelo.Vehiculo;

import java.time.LocalDate;

public class AlquilerControllerTest {

    private static int pasados = 0;
    private static int fallados = 0;

    public static void main(String[] args) {
        testSolicitarAlquilerComun();
        testSolicitarAlquilerCorporativo();
        testSolicitarAlquilerTuristico();
        testVehiculoNoDisponibleEnPeriodoSolapado();
        testCalcularImportesSegunTipo();

        System.out.println("\n=== Resultado: " + pasados + " pasados, " + fallados + " fallados ===");
    }

    // ─── Tests ────────────────────────────────────────────────────────────────

    static void testSolicitarAlquilerComun() {
        resetControllers();
        try {
            Cliente cliente = ClienteController.getInstance()
                    .registrarCliente("11111111", "Juan", "111", "j@mail.com", "Calle 1", "SISTEMA");
            Vehiculo vehiculo = VehiculoController.getInstance()
                    .registrarVehiculo("COM001", "Toyota", "Corolla", 2023, 1000,
                            100.0, TipoCombustible.NAFTA, TipoVehiculo.AUTO, 200, 5.0, "SISTEMA");

            LocalDate inicio = LocalDate.now().plusDays(1);
            LocalDate fin = LocalDate.now().plusDays(6);
            Alquiler alquiler = AlquilerController.getInstance()
                    .solicitarAlquiler(cliente, vehiculo, inicio, fin, "COMUN", "SISTEMA");

            afirmar(alquiler != null, "El alquiler no debería ser null");
            afirmar(alquiler instanceof AlquilerComun, "Debería ser AlquilerComun");
            pasar("testSolicitarAlquilerComun");
        } catch (Exception e) {
            fallar("testSolicitarAlquilerComun", e.getMessage());
        }
    }

    static void testSolicitarAlquilerCorporativo() {
        resetControllers();
        try {
            Cliente cliente = ClienteController.getInstance()
                    .registrarCliente("22222222", "Empresa SA", "222", "e@mail.com", "Av 2", "SISTEMA");
            Vehiculo vehiculo = VehiculoController.getInstance()
                    .registrarVehiculo("CORP01", "Ford", "Focus", 2022, 500,
                            100.0, TipoCombustible.NAFTA, TipoVehiculo.AUTO, 200, 5.0, "SISTEMA");

            LocalDate inicio = LocalDate.now().plusDays(1);
            LocalDate fin = LocalDate.now().plusDays(6);
            Alquiler alquiler = AlquilerController.getInstance()
                    .solicitarAlquiler(cliente, vehiculo, inicio, fin, "CORPORATIVO", "SISTEMA");

            afirmar(alquiler != null, "El alquiler no debería ser null");
            afirmar(alquiler instanceof AlquilerCorporativo, "Debería ser AlquilerCorporativo");
            pasar("testSolicitarAlquilerCorporativo");
        } catch (Exception e) {
            fallar("testSolicitarAlquilerCorporativo", e.getMessage());
        }
    }

    static void testSolicitarAlquilerTuristico() {
        resetControllers();
        try {
            Cliente cliente = ClienteController.getInstance()
                    .registrarCliente("33333333", "María", "333", "m@mail.com", "Ruta 3", "SISTEMA");
            Vehiculo vehiculo = VehiculoController.getInstance()
                    .registrarVehiculo("TUR01", "Chevrolet", "Cruze", 2023, 800,
                            100.0, TipoCombustible.NAFTA, TipoVehiculo.AUTO, 200, 5.0, "SISTEMA");

            LocalDate inicio = LocalDate.now().plusDays(1);
            LocalDate fin = LocalDate.now().plusDays(6);
            Alquiler alquiler = AlquilerController.getInstance()
                    .solicitarAlquiler(cliente, vehiculo, inicio, fin, "TURISTICO", "SISTEMA");

            afirmar(alquiler != null, "El alquiler no debería ser null");
            afirmar(alquiler instanceof AlquilerTuristico, "Debería ser AlquilerTuristico");
            pasar("testSolicitarAlquilerTuristico");
        } catch (Exception e) {
            fallar("testSolicitarAlquilerTuristico", e.getMessage());
        }
    }

    static void testVehiculoNoDisponibleEnPeriodoSolapado() {
        resetControllers();
        try {
            Cliente cliente = ClienteController.getInstance()
                    .registrarCliente("44444444", "Carlos", "444", "c@mail.com", "Belgrano 4", "SISTEMA");
            Vehiculo vehiculo = VehiculoController.getInstance()
                    .registrarVehiculo("OCUP01", "Honda", "Civic", 2021, 300,
                            90.0, TipoCombustible.NAFTA, TipoVehiculo.AUTO, null, null, "SISTEMA");

            LocalDate inicio = LocalDate.now().plusDays(5);
            LocalDate fin = LocalDate.now().plusDays(10);
            AlquilerController.getInstance()
                    .solicitarAlquiler(cliente, vehiculo, inicio, fin, "COMUN", "SISTEMA");

            // Intentar alquilar el mismo vehículo en un período solapado
            AlquilerController.getInstance()
                    .solicitarAlquiler(cliente, vehiculo, LocalDate.now().plusDays(7),
                            LocalDate.now().plusDays(12), "COMUN", "SISTEMA");

            fallar("testVehiculoNoDisponibleEnPeriodoSolapado", "Debería haber lanzado excepción");
        } catch (Exception e) {
            pasar("testVehiculoNoDisponibleEnPeriodoSolapado");
        }
    }

    static void testCalcularImportesSegunTipo() {
        resetControllers();
        try {
            Cliente cliente = ClienteController.getInstance()
                    .registrarCliente("55555555", "Test", "555", "t@mail.com", "Test 5", "SISTEMA");

            Vehiculo v1 = VehiculoController.getInstance()
                    .registrarVehiculo("PRECIO01", "Toyota", "Corolla", 2023, 1000,
                            100.0, TipoCombustible.NAFTA, TipoVehiculo.AUTO, 200, 5.0, "SISTEMA");
            Vehiculo v2 = VehiculoController.getInstance()
                    .registrarVehiculo("PRECIO02", "Toyota", "Corolla", 2023, 1000,
                            100.0, TipoCombustible.NAFTA, TipoVehiculo.AUTO, 200, 5.0, "SISTEMA");
            Vehiculo v3 = VehiculoController.getInstance()
                    .registrarVehiculo("PRECIO03", "Toyota", "Corolla", 2023, 1000,
                            100.0, TipoCombustible.NAFTA, TipoVehiculo.AUTO, 200, 5.0, "SISTEMA");

            // 5 días × $100 = $500 base, km dentro del límite → sin excedentes
            LocalDate inicio = LocalDate.now().plusDays(1);
            LocalDate fin = LocalDate.now().plusDays(6);

            Alquiler comun = AlquilerController.getInstance()
                    .solicitarAlquiler(cliente, v1, inicio, fin, "COMUN", "SISTEMA");
            Alquiler corporativo = AlquilerController.getInstance()
                    .solicitarAlquiler(cliente, v2, inicio, fin, "CORPORATIVO", "SISTEMA");
            Alquiler turistico = AlquilerController.getInstance()
                    .solicitarAlquiler(cliente, v3, inicio, fin, "TURISTICO", "SISTEMA");

            comun.setKilometrajeFinal(1200);       // 200 km recorridos < 1000 km max → sin excedentes
            corporativo.setKilometrajeFinal(1200);
            turistico.setKilometrajeFinal(1200);

            afirmar(Math.abs(comun.calcularImporteTotal() - 500.0) < 0.01,
                    "Común debería ser $500, fue: " + comun.calcularImporteTotal());
            afirmar(Math.abs(corporativo.calcularImporteTotal() - 450.0) < 0.01,
                    "Corporativo debería ser $450, fue: " + corporativo.calcularImporteTotal());
            afirmar(Math.abs(turistico.calcularImporteTotal() - 575.0) < 0.01,
                    "Turístico debería ser $575, fue: " + turistico.calcularImporteTotal());
            pasar("testCalcularImportesSegunTipo");
        } catch (Exception e) {
            fallar("testCalcularImportesSegunTipo", e.getMessage());
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private static void resetControllers() {
        AlquilerController.getInstance().resetParaTests();
        ClienteController.getInstance().resetParaTests();
        VehiculoController.getInstance().resetParaTests();
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
