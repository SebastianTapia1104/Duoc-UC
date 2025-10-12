// package com.sebatapia.computec.vistas;
package com.sebatapia.computec.vistas;

import com.sebatapia.computec.controladores.ClienteControlador;
import com.sebatapia.computec.controladores.VentaControlador;
import com.sebatapia.computec.modelos.Cliente;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Vector;

public class ClientesPanel extends JPanel {

    private final ClienteControlador clienteControlador;
    private final VentaControlador ventaControlador;

    // --- Componentes Pestaña 1: Resumen ---
    private JTable tablaClientes;
    private DefaultTableModel modeloTablaClientes;
    private JTextField txtFiltro;
    private JComboBox<String> comboFiltro;

    // --- Componentes Pestaña 2: Crear ---
    private JTextField txtRutCrear, txtNombreCrear, txtDireccionCrear, txtComunaCrear, txtCorreoCrear, txtTelefonoCrear, txtFechaNacimientoCrear;

    // --- Componentes Pestaña 3: Actualizar ---
    private JTextField txtRutBuscarActualizar, txtNombreActualizar, txtDireccionActualizar, txtComunaActualizar, txtCorreoActualizar, txtTelefonoActualizar, txtFechaNacimientoActualizar;
    private JButton btnBuscarActualizar, btnActualizar;
    private JPanel panelFormularioActualizar;
    private Cliente clienteParaActualizar; // Almacena el cliente que se está editando

    // --- Componentes Pestaña 4: Eliminar ---
    private JTextField txtRutEliminar;
    private JButton btnEliminar;

    public ClientesPanel() {
        this.clienteControlador = new ClienteControlador();
        this.ventaControlador = new VentaControlador();
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Resumen de Clientes", crearPanelResumen());
        tabbedPane.addTab("Crear Cliente", crearPanelCrear());
        tabbedPane.addTab("Actualizar Cliente", crearPanelActualizar());
        tabbedPane.addTab("Eliminar Cliente", crearPanelEliminar());

        add(tabbedPane, BorderLayout.CENTER);
        
        cargarClientesEnTabla();
    }

    // --- Pestaña 1: Resumen de Clientes (Código sin cambios) ---
    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboFiltro = new JComboBox<>(new String[]{"Nombre", "RUT", "Comuna", "Correo"});
        txtFiltro = new JTextField(20);
        JButton btnFiltrar = new JButton("Filtrar");
        panelFiltros.add(new JLabel("Filtrar por:"));
        panelFiltros.add(comboFiltro);
        panelFiltros.add(txtFiltro);
        panelFiltros.add(btnFiltrar);

        modeloTablaClientes = new DefaultTableModel(new String[]{"RUT", "Nombre Completo", "Dirección", "Comuna", "Correo", "Teléfono"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaClientes = new JTable(modeloTablaClientes);
        panel.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
        panel.add(panelFiltros, BorderLayout.NORTH);

        final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTablaClientes);
        tablaClientes.setRowSorter(sorter);
        btnFiltrar.addActionListener(e -> {
            String texto = txtFiltro.getText();
            int columna = comboFiltro.getSelectedIndex() == 1 ? 0 : comboFiltro.getSelectedIndex() + 1;
            if (texto.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, columna));
            }
        });
        return panel;
    }

    private void cargarClientesEnTabla() {
        modeloTablaClientes.setRowCount(0);
        List<Cliente> clientes = clienteControlador.listarClientes();
        for (Cliente cliente : clientes) {
            modeloTablaClientes.addRow(new Object[]{
                cliente.getRut(),
                cliente.getNombreCompleto(),
                cliente.getDireccion(),
                cliente.getComuna(),
                cliente.getCorreoElectronico(),
                cliente.getTelefono()
            });
        }
    }

    // --- Pestaña 2: Crear Cliente (Código sin cambios) ---
    private JPanel crearPanelCrear() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        txtRutCrear = new JTextField(20);
        txtNombreCrear = new JTextField(20);
        txtDireccionCrear = new JTextField(20);
        txtComunaCrear = new JTextField(20);
        txtCorreoCrear = new JTextField(20);
        txtTelefonoCrear = new JTextField(20);
        txtFechaNacimientoCrear = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("RUT:"), gbc);
        gbc.gridx = 1; panel.add(txtRutCrear, gbc);
        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1; panel.add(txtNombreCrear, gbc);
        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; panel.add(txtDireccionCrear, gbc);
        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Comuna:"), gbc);
        gbc.gridx = 1; panel.add(txtComunaCrear, gbc);
        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1; panel.add(txtCorreoCrear, gbc);
        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; panel.add(txtTelefonoCrear, gbc);
        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Fecha Nacimiento (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1; panel.add(txtFechaNacimientoCrear, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton btnCrear = new JButton("Guardar Cliente");
        panel.add(btnCrear, gbc);
        btnCrear.addActionListener(e -> crearCliente());

        return panel;
    }

    private void crearCliente() {
        if (txtRutCrear.getText().trim().isEmpty() || txtNombreCrear.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "RUT y Nombre son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Cliente nuevoCliente = new Cliente(
                txtRutCrear.getText().trim(), txtNombreCrear.getText().trim(), txtDireccionCrear.getText().trim(),
                txtComunaCrear.getText().trim(), txtCorreoCrear.getText().trim(), txtTelefonoCrear.getText().trim(),
                LocalDate.parse(txtFechaNacimientoCrear.getText().trim())
            );
            if (clienteControlador.registrarCliente(nuevoCliente)) {
                JOptionPane.showMessageDialog(this, "Cliente creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarClientesEnTabla();
                txtRutCrear.setText(""); txtNombreCrear.setText(""); txtDireccionCrear.setText(""); txtComunaCrear.setText("");
                txtCorreoCrear.setText(""); txtTelefonoCrear.setText(""); txtFechaNacimientoCrear.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear el cliente. Verifique si el RUT ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use AAAA-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Pestaña 3: Actualizar Cliente (VERSIÓN COMPLETA) ---
    private JPanel crearPanelActualizar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("RUT del Cliente a Actualizar:"));
        txtRutBuscarActualizar = new JTextField(15);
        panelBusqueda.add(txtRutBuscarActualizar);
        btnBuscarActualizar = new JButton("Buscar");
        panelBusqueda.add(btnBuscarActualizar);

        panelFormularioActualizar = new JPanel(new GridBagLayout());
        panelFormularioActualizar.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        panelFormularioActualizar.setVisible(false); // Oculto hasta que se encuentre un cliente
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // --- INICIO DE CÓDIGO COMPLETADO ---
        txtNombreActualizar = new JTextField(20);
        txtDireccionActualizar = new JTextField(20);
        txtComunaActualizar = new JTextField(20);
        txtCorreoActualizar = new JTextField(20);
        txtTelefonoActualizar = new JTextField(20);
        txtFechaNacimientoActualizar = new JTextField(20);
        btnActualizar = new JButton("Actualizar Datos");

        gbc.gridx = 0; gbc.gridy = 0; panelFormularioActualizar.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1; panelFormularioActualizar.add(txtNombreActualizar, gbc);
        gbc.gridy++; gbc.gridx = 0; panelFormularioActualizar.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; panelFormularioActualizar.add(txtDireccionActualizar, gbc);
        gbc.gridy++; gbc.gridx = 0; panelFormularioActualizar.add(new JLabel("Comuna:"), gbc);
        gbc.gridx = 1; panelFormularioActualizar.add(txtComunaActualizar, gbc);
        gbc.gridy++; gbc.gridx = 0; panelFormularioActualizar.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1; panelFormularioActualizar.add(txtCorreoActualizar, gbc);
        gbc.gridy++; gbc.gridx = 0; panelFormularioActualizar.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; panelFormularioActualizar.add(txtTelefonoActualizar, gbc);
        gbc.gridy++; gbc.gridx = 0; panelFormularioActualizar.add(new JLabel("Fecha Nacimiento (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1; panelFormularioActualizar.add(txtFechaNacimientoActualizar, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panelFormularioActualizar.add(btnActualizar, gbc);
        // --- FIN DE CÓDIGO COMPLETADO ---

        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(panelFormularioActualizar, BorderLayout.CENTER);

        btnBuscarActualizar.addActionListener(e -> buscarParaActualizar());
        btnActualizar.addActionListener(e -> actualizarCliente());
        return panel;
    }

    private void buscarParaActualizar() {
        String rut = txtRutBuscarActualizar.getText().trim();
        clienteParaActualizar = clienteControlador.buscarClientePorRut(rut);
        if (clienteParaActualizar != null) {
            panelFormularioActualizar.setVisible(true);
            // --- INICIO DE CÓDIGO COMPLETADO ---
            txtNombreActualizar.setText(clienteParaActualizar.getNombreCompleto());
            txtDireccionActualizar.setText(clienteParaActualizar.getDireccion());
            txtComunaActualizar.setText(clienteParaActualizar.getComuna());
            txtCorreoActualizar.setText(clienteParaActualizar.getCorreoElectronico());
            txtTelefonoActualizar.setText(clienteParaActualizar.getTelefono());
            txtFechaNacimientoActualizar.setText(clienteParaActualizar.getFechaNacimiento().toString());
            // --- FIN DE CÓDIGO COMPLETADO ---
            this.revalidate();
            this.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            panelFormularioActualizar.setVisible(false);
        }
    }

    private void actualizarCliente() {
        if (txtNombreActualizar.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // --- INICIO DE CÓDIGO COMPLETADO ---
            clienteParaActualizar.setNombreCompleto(txtNombreActualizar.getText().trim());
            clienteParaActualizar.setDireccion(txtDireccionActualizar.getText().trim());
            clienteParaActualizar.setComuna(txtComunaActualizar.getText().trim());
            clienteParaActualizar.setCorreoElectronico(txtCorreoActualizar.getText().trim());
            clienteParaActualizar.setTelefono(txtTelefonoActualizar.getText().trim());
            clienteParaActualizar.setFechaNacimiento(LocalDate.parse(txtFechaNacimientoActualizar.getText().trim()));
            // --- FIN DE CÓDIGO COMPLETADO ---

            if (clienteControlador.actualizarCliente(clienteParaActualizar)) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarClientesEnTabla();
                panelFormularioActualizar.setVisible(false);
                txtRutBuscarActualizar.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use AAAA-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Pestaña 4: Eliminar Cliente (Código sin cambios) ---
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        panel.add(new JLabel("RUT del Cliente a Eliminar:"));
        txtRutEliminar = new JTextField(15);
        panel.add(txtRutEliminar);
        btnEliminar = new JButton("Eliminar Cliente");
        panel.add(btnEliminar);

        btnEliminar.addActionListener(e -> eliminarCliente());
        return panel;
    }

    private void eliminarCliente() {
        String rut = txtRutEliminar.getText().trim();
        if (rut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un RUT.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = clienteControlador.buscarClientePorRut(rut);
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "El cliente con el RUT ingresado no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        /*
         * Regla de negocio: Verificar si el cliente tiene ventas asociadas.
         * Para una implementación robusta, se necesitaría un método en VentaControlador
         * como `tieneVentas(String rutCliente)` que haga un `SELECT COUNT(*)` en la BD.
         * Aquí simulamos esa comprobación buscando en el reporte.
        */
        boolean tieneVentas = ventaControlador.generarReporteVentas("Todos").stream()
                               .anyMatch(row -> {
                                   // Asumiendo que row[1] es el nombre del cliente.
                                   // Una comparación por RUT sería más segura si el reporte lo incluyera.
                                   return cliente.getNombreCompleto().equals(row[1]);
                               });

        if (tieneVentas) {
            JOptionPane.showMessageDialog(this, "No se puede eliminar. El cliente tiene ventas registradas.", "Operación Denegada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea eliminar al cliente '" + cliente.getNombreCompleto() + "'?\nEsta acción no se puede deshacer.",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (clienteControlador.eliminarCliente(rut)) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarClientesEnTabla();
                txtRutEliminar.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al eliminar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
