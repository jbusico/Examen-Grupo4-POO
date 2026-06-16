package Vistas;

import Controladores.ClienteController;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistrarCliente extends JFrame {

    private JTextField txtDniCuit;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtDireccion;
    private JTextField txtUsuario;

    public VentanaRegistrarCliente() {
        setTitle("Registrar Cliente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 340);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtDniCuit   = agregarCampo(panel, gbc, 0, "DNI / CUIT:");
        txtNombre    = agregarCampo(panel, gbc, 1, "Nombre:");
        txtTelefono  = agregarCampo(panel, gbc, 2, "Teléfono:");
        txtEmail     = agregarCampo(panel, gbc, 3, "Email:");
        txtDireccion = agregarCampo(panel, gbc, 4, "Dirección:");
        txtUsuario   = agregarCampo(panel, gbc, 5, "Usuario:");

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar  = new JButton("Cancelar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(14, 5, 5, 5);
        panel.add(panelBotones, gbc);

        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);
    }

    private JTextField agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(etiqueta), gbc);

        JTextField campo = new JTextField(20);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campo, gbc);
        return campo;
    }

    private void registrar() {
        String dniCuit   = txtDniCuit.getText().trim();
        String nombre    = txtNombre.getText().trim();
        String telefono  = txtTelefono.getText().trim();
        String email     = txtEmail.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String usuario   = txtUsuario.getText().trim();

        if (dniCuit.isEmpty() || nombre.isEmpty() || telefono.isEmpty()
                || email.isEmpty() || direccion.isEmpty() || usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios.",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ClienteController.getInstance()
                    .registrarCliente(dniCuit, nombre, telefono, email, direccion, usuario);

            JOptionPane.showMessageDialog(this,
                    "Cliente registrado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtDniCuit.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
        txtUsuario.setText("");
        txtDniCuit.requestFocus();
    }
}
