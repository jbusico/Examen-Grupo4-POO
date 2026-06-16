package Vistas;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Alquiler de Vehículos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0;

        JLabel titulo = new JLabel("Gestión de Alquiler de Vehículos", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(titulo, gbc);

        gbc.insets = new Insets(6, 0, 6, 0);

        JButton btnRegistrarCliente = new JButton("Registrar Cliente");
        gbc.gridy = 1;
        panel.add(btnRegistrarCliente, gbc);

        JButton btnConsultarVehiculos = new JButton("Consultar Vehículos Disponibles");
        gbc.gridy = 2;
        panel.add(btnConsultarVehiculos, gbc);

        JButton btnSalir = new JButton("Salir");
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(btnSalir, gbc);

        btnRegistrarCliente.addActionListener(e -> {
            new VentanaRegistrarCliente().setVisible(true);
        });

        btnConsultarVehiculos.addActionListener(e -> {
            new VentanaConsultarVehiculos().setVisible(true);
        });

        btnSalir.addActionListener(e -> System.exit(0));

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
