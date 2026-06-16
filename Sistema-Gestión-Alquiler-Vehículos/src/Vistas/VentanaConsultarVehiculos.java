package Vistas;

import Controladores.AlquilerController;
import Enums.TipoVehiculo;
import Modelo.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class VentanaConsultarVehiculos extends JFrame {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JComboBox<TipoVehiculo> cmbTipo;
    private DefaultTableModel modeloTabla;

    public VentanaConsultarVehiculos() {
        setTitle("Consultar Vehículos Disponibles");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(680, 460);
        setLocationRelativeTo(null);

        // ── Panel de filtros ──────────────────────────────────────────────────
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros de búsqueda"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelFiltros.add(new JLabel("Fecha inicio (dd/MM/yyyy):"), gbc);
        txtFechaInicio = new JTextField(12);
        gbc.gridx = 1;
        panelFiltros.add(txtFechaInicio, gbc);

        gbc.gridx = 2;
        panelFiltros.add(new JLabel("Fecha fin (dd/MM/yyyy):"), gbc);
        txtFechaFin = new JTextField(12);
        gbc.gridx = 3;
        panelFiltros.add(txtFechaFin, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFiltros.add(new JLabel("Tipo de vehículo:"), gbc);
        cmbTipo = new JComboBox<>(TipoVehiculo.values());
        gbc.gridx = 1;
        panelFiltros.add(cmbTipo, gbc);

        JButton btnBuscar = new JButton("Buscar");
        gbc.gridx = 3; gbc.gridy = 1;
        panelFiltros.add(btnBuscar, gbc);

        // ── Tabla de resultados ───────────────────────────────────────────────
        String[] columnas = {"Patente", "Marca", "Modelo", "Año", "Tipo", "Combustible", "Valor/día"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(tabla);

        // ── Layout principal ──────────────────────────────────────────────────
        setLayout(new BorderLayout(8, 8));
        add(panelFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.add(btnCerrar);
        add(panelSur, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscar());
    }

    private void buscar() {
        String txtInicio = txtFechaInicio.getText().trim();
        String txtFin    = txtFechaFin.getText().trim();

        if (txtInicio.isEmpty() || txtFin.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingresá ambas fechas para buscar.",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fechaInicio;
        LocalDate fechaFin;
        try {
            fechaInicio = LocalDate.parse(txtInicio, FORMATO);
            fechaFin    = LocalDate.parse(txtFin, FORMATO);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Usá dd/MM/yyyy.",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!fechaFin.isAfter(fechaInicio)) {
            JOptionPane.showMessageDialog(this,
                    "La fecha de fin debe ser posterior a la fecha de inicio.",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TipoVehiculo tipo = (TipoVehiculo) cmbTipo.getSelectedItem();

        List<Vehiculo> resultado = AlquilerController.getInstance()
                .consultarVehiculosDisponibles(fechaInicio, fechaFin, tipo);

        modeloTabla.setRowCount(0);
        for (Vehiculo v : resultado) {
            modeloTabla.addRow(new Object[]{
                v.getPatente(),
                v.getMarca(),
                v.getModelo(),
                v.getAño(),
                v.getTipoVehiculo(),
                v.getTipoCombustible(),
                String.format("$%.2f", v.getValorDiario())
            });
        }

        if (resultado.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay vehículos disponibles para el período y tipo seleccionados.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
