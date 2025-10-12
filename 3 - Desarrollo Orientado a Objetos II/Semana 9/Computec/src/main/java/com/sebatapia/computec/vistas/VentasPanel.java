package com.sebatapia.computec.vistas;

import com.sebatapia.computec.command.AgregarItemCommand;
import com.sebatapia.computec.command.VentaManager;
import com.sebatapia.computec.controladores.ClienteControlador;
import com.sebatapia.computec.controladores.EquipoControlador;
import com.sebatapia.computec.controladores.VentaControlador;
import com.sebatapia.computec.decorator.DescuentoTerceraEdad;
import com.sebatapia.computec.decorator.PrecioBase;
import com.sebatapia.computec.interfaces.Descuento;
import com.sebatapia.computec.modelos.Cliente;
import com.sebatapia.computec.modelos.Equipo;
import com.sebatapia.computec.modelos.Venta;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VentasPanel extends JPanel {

    // --- Controladores y Gestores ---
    private final ClienteControlador clienteControlador;
    private final EquipoControlador equipoControlador;
    private final VentaControlador ventaControlador;
    private final VentaManager ventaManager;

    // --- Componentes de la UI ---
    private JTextField txtRutCliente;
    private JButton btnBuscarCliente;
    private JLabel lblNombreCliente, lblCorreoCliente;

    private JTable tablaEquipos;
    private DefaultTableModel modeloTablaEquipos;

    private JLabel lblEquipoSeleccionado, lblPrecioFinal;
    private JCheckBox chkDescuento;
    private JButton btnRegistrarVenta, btnNuevaVenta;

    public VentasPanel() {
        // Inicialización de la lógica de negocio
        this.clienteControlador = new ClienteControlador();
        this.equipoControlador = new EquipoControlador();
        this.ventaControlador = new VentaControlador();
        this.ventaManager = new VentaManager();

        // Configuración del layout principal
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Creación de los sub-paneles
        JPanel panelCliente = crearPanelCliente();
        JPanel panelEquipos = crearPanelEquipos();
        JPanel panelAcciones = crearPanelAcciones();

        // Añadir sub-paneles al panel principal
        add(panelCliente, BorderLayout.NORTH);
        add(panelEquipos, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);

        // Configurar los listeners de eventos
        configurarEventos();

        // Establecer el estado inicial de la UI
        establecerEstadoInicial();
    }

    // --- Métodos de Creación de la UI ---
    private JPanel crearPanelCliente() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Paso 1: Seleccionar Cliente"));

        panel.add(new JLabel("RUT Cliente:"));
        txtRutCliente = new JTextField(12);
        panel.add(txtRutCliente);
        btnBuscarCliente = new JButton("Buscar Cliente");
        panel.add(btnBuscarCliente);

        lblNombreCliente = new JLabel("Cliente no seleccionado");
        lblNombreCliente.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblNombreCliente);

        lblCorreoCliente = new JLabel();
        panel.add(lblCorreoCliente);

        return panel;
    }

    private JPanel crearPanelEquipos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Paso 2: Seleccionar Equipo"));

        modeloTablaEquipos = new DefaultTableModel(new Object[]{"ID", "Modelo", "Tipo", "CPU", "RAM", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        tablaEquipos = new JTable(modeloTablaEquipos);
        tablaEquipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(tablaEquipos), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Paso 3: Resumen y Registro"));

        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblEquipoSeleccionado = new JLabel("Equipo no seleccionado.");
        lblPrecioFinal = new JLabel("Precio Final: $0");
        lblPrecioFinal.setFont(new Font("Arial", Font.BOLD, 16));
        chkDescuento = new JCheckBox("Aplicar Descuento Tercera Edad");
        panelResumen.add(lblEquipoSeleccionado);
        panelResumen.add(chkDescuento);
        panelResumen.add(lblPrecioFinal);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRegistrarVenta = new JButton("Registrar Venta");
        btnNuevaVenta = new JButton("Nueva Venta / Limpiar");
        panelBotones.add(btnNuevaVenta);
        panelBotones.add(btnRegistrarVenta);

        panel.add(panelResumen, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.EAST);
        return panel;
    }

    // --- Lógica de Eventos ---
    private void configurarEventos() {
        btnBuscarCliente.addActionListener(e -> buscarCliente());
        btnNuevaVenta.addActionListener(e -> establecerEstadoInicial());
        btnRegistrarVenta.addActionListener(e -> registrarVenta());
        chkDescuento.addActionListener(e -> actualizarResumenVenta());

        tablaEquipos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaEquipos.getSelectedRow() != -1) {
                seleccionarEquipo();
            }
        });
    }
    
    // --- Métodos de Lógica de la Vista ---
    private void buscarCliente() {
        String rut = txtRutCliente.getText().trim();
        if (rut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un RUT.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = clienteControlador.buscarClientePorRut(rut);
        if (cliente != null) {
            ventaManager.seleccionarCliente(cliente);
            lblNombreCliente.setText("Cliente: " + cliente.getNombreCompleto());
            lblCorreoCliente.setText("(" + cliente.getCorreoElectronico() + ")");
            // Bloquear selección de cliente y habilitar selección de equipo
            txtRutCliente.setEnabled(false);
            btnBuscarCliente.setEnabled(false);
            tablaEquipos.setEnabled(true);
            chkDescuento.setEnabled(true);
            cargarEquiposEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEquiposEnTabla() {
        modeloTablaEquipos.setRowCount(0); // Limpiar tabla
        List<Equipo> equipos = equipoControlador.listarEquipos();
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CL"));
        for (Equipo equipo : equipos) {
            modeloTablaEquipos.addRow(new Object[]{
                    equipo.getIdEquipo(),
                    equipo.getModelo(),
                    equipo.getTipoEquipo(),
                    equipo.getCpu(),
                    equipo.getRamGB() + " GB",
                    formatoMoneda.format(equipo.getPrecio())
            });
        }
    }
    
    private void seleccionarEquipo() {
        int filaSeleccionada = tablaEquipos.getSelectedRow();
        int idEquipo = (int) modeloTablaEquipos.getValueAt(filaSeleccionada, 0);
        // Buscamos el objeto completo del equipo
        Equipo equipoSeleccionado = equipoControlador.listarEquipos().stream()
                .filter(eq -> eq.getIdEquipo() == idEquipo)
                .findFirst()
                .orElse(null);
        if (equipoSeleccionado != null) {
            // Usamos el patrón Command para agregar el item
            new AgregarItemCommand(ventaManager, equipoSeleccionado).execute();
            actualizarResumenVenta();
            btnRegistrarVenta.setEnabled(true);
        }
    }

    private void actualizarResumenVenta() {
        Equipo equipo = ventaManager.getEquipoSeleccionado();
        Cliente cliente = ventaManager.getClienteSeleccionado();
        if (equipo == null) return;

        lblEquipoSeleccionado.setText("Equipo: " + equipo.getModelo());

        // Usamos el patrón Decorator para calcular el precio
        Descuento calculoPrecio = new PrecioBase();
        if (chkDescuento.isSelected() && cliente != null) {
            calculoPrecio = new DescuentoTerceraEdad(calculoPrecio, cliente);
        }
        double precioFinal = calculoPrecio.calcularPrecioFinal(equipo.getPrecio());
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CL"));
        lblPrecioFinal.setText("Precio Final: " + formatoMoneda.format(precioFinal));
    }

    private void registrarVenta() {
        Venta nuevaVenta = ventaManager.crearVenta();
        if (nuevaVenta == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente y un equipo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Asignamos el precio final calculado con el decorador
        double precioFinalCalculado = Double.parseDouble(lblPrecioFinal.getText().replaceAll("[^\\d,]", "").replace(",", "."));
        nuevaVenta.setPrecioFinal(precioFinalCalculado);
        boolean exito = ventaControlador.registrarVenta(nuevaVenta);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Venta registrada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            establecerEstadoInicial();
        } else {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al registrar la venta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void establecerEstadoInicial() {
        // Limpiar el estado en el VentaManager
        ventaManager.limpiarVentaActual();

        // Restablecer componentes de cliente
        txtRutCliente.setText("");
        txtRutCliente.setEnabled(true);
        btnBuscarCliente.setEnabled(true);
        lblNombreCliente.setText("Cliente no seleccionado");
        lblCorreoCliente.setText("");

        // Restablecer componentes de equipo
        modeloTablaEquipos.setRowCount(0);
        tablaEquipos.setEnabled(false);

        // Restablecer componentes de acciones
        lblEquipoSeleccionado.setText("Equipo no seleccionado.");
        lblPrecioFinal.setText("Precio Final: $0");
        chkDescuento.setSelected(false);
        chkDescuento.setEnabled(false);
        btnRegistrarVenta.setEnabled(false);
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
