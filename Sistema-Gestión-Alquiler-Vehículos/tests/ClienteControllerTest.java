package tests;

import Modelo.Cliente;
import Enums.EstadoCliente;
import Controladores.ClienteController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteControllerTest {

    ClienteController controller;

    @BeforeEach
    void setUp() {
        controller = ClienteController.getInstance();
    }

    @Test
    void registrarClienteOKTest() throws Exception {
        String dniCuit = "12345678";
        String nombre = "Juan García";
        String telefono = "1123456789";
        String email = "juan@example.com";
        String direccion = "Calle 123";

        Cliente cliente = controller.registrarCliente(dniCuit, nombre, telefono, email, direccion, "SISTEMA");

        assertNotNull(cliente);
        assertEquals(dniCuit, cliente.getDniCuit());
        assertEquals(nombre, cliente.getNombre());
        assertEquals(EstadoCliente.ACTIVO, cliente.getEstado());
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }
}