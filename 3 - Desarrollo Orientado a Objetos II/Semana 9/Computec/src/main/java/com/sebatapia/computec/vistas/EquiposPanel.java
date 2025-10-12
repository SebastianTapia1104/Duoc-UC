package com.sebatapia.computec.vistas;

import com.sebatapia.computec.controladores.EquipoControlador;
import com.sebatapia.computec.controladores.VentaControlador;
import com.sebatapia.computec.modelos.Desktop;
import com.sebatapia.computec.modelos.Equipo;
import com.sebatapia.computec.modelos.Laptop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class EquiposPanel extends JPanel {

    private final EquipoControlador equipoControlador;
    private final VentaControlador ventaControlador;

    // --- Componentes Pestaña 1: Resumen ---
    private JTable tablaEquipos;
    private DefaultTableModel modeloTablaEquipos;
    private JTextField txtFiltroResumen;
    private JComboBox<String> comboFiltroResumen;

    // --- Componentes Pestaña 2: Crear ---
    private JTextField txtModeloCrear, txtCpuCrear, txtDiscoDuroCrear, txtRamCrear, txtPrecioCrear;
    private JRadioButton radioLaptopCrear, radioDesktopCrear;
    private JPanel panelCamposEspecificosCrear;
    private JPanel panelLaptopCrear, panelDesktopCrear;
    private JTextField txtPantallaLaptop, txtUsbLaptop;
    private JCheckBox chkTouchLaptop;
    private JTextField txtFuenteDesktop, txtFormaDesktop;

    // --- Componentes Pestaña 3: Actualizar ---
    private JTextField txtIdBuscarActualizar;
    private JPanel panelFormularioActualizar;
    private Equipo equipoParaActualizar;
    private JTextField txtModeloAct, txtCpuAct, txtDiscoDuroAct, txtRamAct, txtPrecioAct;
    private JRadioButton radioLaptopAct, radioDesktopAct;
    private JPanel panelCamposEspecificosAct, panelLaptopAct, panelDesktopAct;
    private JTextField txtPantallaLaptopAct, txtUsbLaptopAct;
    private JCheckBox chkTouchLaptopAct;
    private JTextField txtFuenteDesktopAct, txtFormaDesktopAct;
    private JButton btnActualizar;
    
    // --- Componentes Pestaña 4: Eliminar ---
    private JTextField txtIdEliminar;
    private JButton btnEliminar;

    public EquiposPanel() {
        this.equipoControlador = new EquipoControlador();
        this.ventaControlador = new VentaControlador();
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Resumen de Equipos", crearPanelResumen());
        tabbedPane.addTab("Crear Equipo", crearPanelCrear());
        tabbedPane.addTab("Actualizar Equipo", crearPanelActualizar());
        tabbedPane.addTab("Eliminar Equipo", crearPanelEliminar());

        add(tabbedPane, BorderLayout.CENTER);
        
        cargarEquiposEnTabla();
    }
    
    // --- Pestaña 1: Resumen de Equipos ---
    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboFiltroResumen = new JComboBox<>(new String[]{"Modelo", "Tipo", "CPU"});
        txtFiltroResumen = new JTextField(20);
        JButton btnFiltrar = new JButton("Filtrar");
        panelFiltros.add(new JLabel("Filtrar por:"));
        panelFiltros.add(comboFiltroResumen);
        panelFiltros.add(txtFiltroResumen);
        panelFiltros.add(btnFiltrar);

        modeloTablaEquipos = new DefaultTableModel(new String[]{"ID", "Modelo", "Tipo", "CPU", "RAM", "Disco Duro", "Precio", "Detalles"}, 0){
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaEquipos = new JTable(modeloTablaEquipos);
        panel.add(new JScrollPane(tablaEquipos), BorderLayout.CENTER);
        panel.add(panelFiltros, BorderLayout.NORTH);

        final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTablaEquipos);
        tablaEquipos.setRowSorter(sorter);
        btnFiltrar.addActionListener(e -> {
            String texto = txtFiltroResumen.getText();
            if (texto.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                int col = comboFiltroResumen.getSelectedIndex() + 1;
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, col));
            }
        });

        return panel;
    }

    private void cargarEquiposEnTabla() {
        modeloTablaEquipos.setRowCount(0);
        List<Equipo> equipos = equipoControlador.listarEquipos();
        for (Equipo equipo : equipos) {
            modeloTablaEquipos.addRow(new Object[]{
                equipo.getIdEquipo(),
                equipo.getModelo(),
                equipo.getTipoEquipo(),
                equipo.getCpu(),
                equipo.getRamGB() + " GB",
                equipo.getDiscoDuroGB() + " GB",
                equipo.getPrecio(),
                equipo.getDetalleEspecifico()
            });
        }
    }

    // --- Pestaña 2: Crear Equipo ---
    private JPanel crearPanelCrear() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelComun = new JPanel(new GridBagLayout());
        panelComun.setBorder(BorderFactory.createTitledBorder("Datos Generales"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        radioLaptopCrear = new JRadioButton("Laptop", true);
        radioDesktopCrear = new JRadioButton("Desktop");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(radioLaptopCrear);
        grupoTipo.add(radioDesktopCrear);
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTipo.add(new JLabel("Tipo de Equipo:"));
        panelTipo.add(radioLaptopCrear);
        panelTipo.add(radioDesktopCrear);

        txtModeloCrear = new JTextField(20);
        txtCpuCrear = new JTextField(20);
        txtDiscoDuroCrear = new JTextField(20);
        txtRamCrear = new JTextField(20);
        txtPrecioCrear = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panelComun.add(panelTipo, gbc); gbc.gridwidth = 1;
        gbc.gridy++; gbc.gridx = 0; panelComun.add(new JLabel("Modelo:"), gbc); gbc.gridx = 1; panelComun.add(txtModeloCrear, gbc);
        gbc.gridy++; gbc.gridx = 0; panelComun.add(new JLabel("CPU:"), gbc); gbc.gridx = 1; panelComun.add(txtCpuCrear, gbc);
        gbc.gridy++; gbc.gridx = 0; panelComun.add(new JLabel("Disco Duro (GB):"), gbc); gbc.gridx = 1; panelComun.add(txtDiscoDuroCrear, gbc);
        gbc.gridy++; gbc.gridx = 0; panelComun.add(new JLabel("RAM (GB):"), gbc); gbc.gridx = 1; panelComun.add(txtRamCrear, gbc);
        gbc.gridy++; gbc.gridx = 0; panelComun.add(new JLabel("Precio:"), gbc); gbc.gridx = 1; panelComun.add(txtPrecioCrear, gbc);

        crearPanelesEspecificos();
        panelCamposEspecificosCrear = new JPanel(new CardLayout());
        panelCamposEspecificosCrear.add(panelLaptopCrear, "Laptop");
        panelCamposEspecificosCrear.add(panelDesktopCrear, "Desktop");

        JButton btnGuardar = new JButton("Guardar Equipo");
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnGuardar);

        panelPrincipal.add(panelComun, BorderLayout.NORTH);
        panelPrincipal.add(panelCamposEspecificosCrear, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        CardLayout cl = (CardLayout)(panelCamposEspecificosCrear.getLayout());
        radioLaptopCrear.addActionListener(e -> cl.show(panelCamposEspecificosCrear, "Laptop"));
        radioDesktopCrear.addActionListener(e -> cl.show(panelCamposEspecificosCrear, "Desktop"));
        btnGuardar.addActionListener(e -> guardarEquipo());

        return panelPrincipal;
    }

    private void crearPanelesEspecificos() {
        panelLaptopCrear = new JPanel(new GridLayout(3, 2, 5, 5));
        panelLaptopCrear.setBorder(BorderFactory.createTitledBorder("Datos de Laptop"));
        txtPantallaLaptop = new JTextField(10);
        chkTouchLaptop = new JCheckBox("Es Táctil");
        txtUsbLaptop = new JTextField(10);
        panelLaptopCrear.add(new JLabel("Tamaño Pantalla (\"):")); panelLaptopCrear.add(txtPantallaLaptop);
        panelLaptopCrear.add(new JLabel("")); panelLaptopCrear.add(chkTouchLaptop);
        panelLaptopCrear.add(new JLabel("Puertos USB:")); panelLaptopCrear.add(txtUsbLaptop);

        panelDesktopCrear = new JPanel(new GridLayout(2, 2, 5, 5));
        panelDesktopCrear.setBorder(BorderFactory.createTitledBorder("Datos de Desktop"));
        txtFuenteDesktop = new JTextField(10);
        txtFormaDesktop = new JTextField(10);
        panelDesktopCrear.add(new JLabel("Fuente de Poder:")); panelDesktopCrear.add(txtFuenteDesktop);
        panelDesktopCrear.add(new JLabel("Factor de Forma:")); panelDesktopCrear.add(txtFormaDesktop);
    }

    private void guardarEquipo() {
        try {
            String modelo = txtModeloCrear.getText();
            String cpu = txtCpuCrear.getText();
            int disco = Integer.parseInt(txtDiscoDuroCrear.getText());
            int ram = Integer.parseInt(txtRamCrear.getText());
            double precio = Double.parseDouble(txtPrecioCrear.getText());
            Equipo nuevoEquipo;
            if (radioLaptopCrear.isSelected()) {
                double pantalla = Double.parseDouble(txtPantallaLaptop.getText());
                boolean esTouch = chkTouchLaptop.isSelected();
                int usb = Integer.parseInt(txtUsbLaptop.getText());
                nuevoEquipo = new Laptop(modelo, cpu, disco, ram, precio, pantalla, esTouch, usb);
            } else {
                String fuente = txtFuenteDesktop.getText();
                String forma = txtFormaDesktop.getText();
                nuevoEquipo = new Desktop(modelo, cpu, disco, ram, precio, fuente, forma);
            }
            if (equipoControlador.registrarEquipo(nuevoEquipo)) {
                JOptionPane.showMessageDialog(this, "Equipo registrado con éxito. ID: " + nuevoEquipo.getIdEquipo(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarEquiposEnTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el equipo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos en los campos correspondientes.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Pestaña 3: Actualizar Equipo ---
    private JPanel crearPanelActualizar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("ID del Equipo a Actualizar:"));
        txtIdBuscarActualizar = new JTextField(10);
        panelBusqueda.add(txtIdBuscarActualizar);
        JButton btnBuscar = new JButton("Buscar Equipo");
        panelBusqueda.add(btnBuscar);
        
        panelFormularioActualizar = crearFormularioActualizar();
        panelFormularioActualizar.setVisible(false);

        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(panelFormularioActualizar, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> buscarParaActualizar());
        
        return panel;
    }

    private JPanel crearFormularioActualizar() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelComun = new JPanel(new GridBagLayout());
        panelComun.setBorder(BorderFactory.createTitledBorder("Datos del Equipo Encontrado"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        radioLaptopAct = new JRadioButton("Laptop", true);
        radioDesktopAct = new JRadioButton("Desktop");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(radioLaptopAct);
        grupoTipo.add(radioDesktopAct);
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTipo.add(new JLabel("Tipo de Equipo:"));
        panelTipo.add(radioLaptopAct);
        panelTipo.add(radioDesktopAct);

        txtModeloAct = new JTextField(20);
        txtCpuAct = new JTextField(20);
        txtDiscoDuroAct = new JTextField(20);
        txtRamAct = new JTextField(20);
        txtPrecioAct = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panelComun.add(panelTipo, gbc); gbc.gridwidth = 1;
        gbc.gridy++; gbc.gridx = 0; panelComun.add(new JLabel("Modelo:"), gbc); gbc.gridx = 1; panelComun.add(txtModeloAct, gbc);
        gbc.gridy++; gbc.gridx = 0; panelComun.add(new JLabel("CPU:"), gbc); gbc.gridx = 1; panelComun.add(txtCpuAct, gbc);
        gbc.gridy++; gbc.gridx = 0; panelComun.add(new JLabel("Disco Duro (GB):"), gbc); gbc.gridx = 1; panelComun.add(txtDiscoDuroAct, gbc);
        gbc.gridy++; gbc.gridx = 0; panelComun.add(new JLabel("RAM (GB):"), gbc); gbc.gridx = 1; panelComun.add(txtRamAct, gbc);
        gbc.gridy++; gbc.gridx = 0; panelComun.add(new JLabel("Precio:"), gbc); gbc.gridx = 1; panelComun.add(txtPrecioAct, gbc);

        panelLaptopAct = new JPanel(new GridLayout(3, 2, 5, 5));
        panelLaptopAct.setBorder(BorderFactory.createTitledBorder("Datos de Laptop"));
        txtPantallaLaptopAct = new JTextField(10);
        chkTouchLaptopAct = new JCheckBox("Es Táctil");
        txtUsbLaptopAct = new JTextField(10);
        panelLaptopAct.add(new JLabel("Tamaño Pantalla (\"):")); panelLaptopAct.add(txtPantallaLaptopAct);
        panelLaptopAct.add(new JLabel("")); panelLaptopAct.add(chkTouchLaptopAct);
        panelLaptopAct.add(new JLabel("Puertos USB:")); panelLaptopAct.add(txtUsbLaptopAct);

        panelDesktopAct = new JPanel(new GridLayout(2, 2, 5, 5));
        panelDesktopAct.setBorder(BorderFactory.createTitledBorder("Datos de Desktop"));
        txtFuenteDesktopAct = new JTextField(10);
        txtFormaDesktopAct = new JTextField(10);
        panelDesktopAct.add(new JLabel("Fuente de Poder:")); panelDesktopAct.add(txtFuenteDesktopAct);
        panelDesktopAct.add(new JLabel("Factor de Forma:")); panelDesktopAct.add(txtFormaDesktopAct);

        panelCamposEspecificosAct = new JPanel(new CardLayout());
        panelCamposEspecificosAct.add(panelLaptopAct, "Laptop");
        panelCamposEspecificosAct.add(panelDesktopAct, "Desktop");

        btnActualizar = new JButton("Actualizar Equipo");
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnActualizar);

        panelPrincipal.add(panelComun, BorderLayout.NORTH);
        panelPrincipal.add(panelCamposEspecificosAct, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        CardLayout cl = (CardLayout)(panelCamposEspecificosAct.getLayout());
        radioLaptopAct.addActionListener(e -> cl.show(panelCamposEspecificosAct, "Laptop"));
        radioDesktopAct.addActionListener(e -> cl.show(panelCamposEspecificosAct, "Desktop"));
        btnActualizar.addActionListener(e -> actualizarEquipo());

        return panelPrincipal;
    }
    
    private void buscarParaActualizar() {
        try {
            int id = Integer.parseInt(txtIdBuscarActualizar.getText().trim());
            equipoParaActualizar = equipoControlador.buscarEquipoPorId(id);

            if (equipoParaActualizar != null) {
                txtModeloAct.setText(equipoParaActualizar.getModelo());
                txtCpuAct.setText(equipoParaActualizar.getCpu());
                txtDiscoDuroAct.setText(String.valueOf(equipoParaActualizar.getDiscoDuroGB()));
                txtRamAct.setText(String.valueOf(equipoParaActualizar.getRamGB()));
                txtPrecioAct.setText(String.valueOf(equipoParaActualizar.getPrecio()));

                CardLayout cl = (CardLayout)(panelCamposEspecificosAct.getLayout());
                if (equipoParaActualizar instanceof Laptop) {
                    radioLaptopAct.setSelected(true);
                    Laptop laptop = (Laptop) equipoParaActualizar;
                    txtPantallaLaptopAct.setText(String.valueOf(laptop.getTamanoPantalla()));
                    chkTouchLaptopAct.setSelected(laptop.isEsTouch());
                    txtUsbLaptopAct.setText(String.valueOf(laptop.getPuertosUSB()));
                    cl.show(panelCamposEspecificosAct, "Laptop");
                } else if (equipoParaActualizar instanceof Desktop) {
                    radioDesktopAct.setSelected(true);
                    Desktop desktop = (Desktop) equipoParaActualizar;
                    txtFuenteDesktopAct.setText(desktop.getPotenciaFuente());
                    txtFormaDesktopAct.setText(desktop.getFactorForma());
                    cl.show(panelCamposEspecificosAct, "Desktop");
                }
                
                panelFormularioActualizar.setVisible(true);
                this.revalidate();
                this.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Equipo con ID " + id + " no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                panelFormularioActualizar.setVisible(false);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEquipo() {
        try {
            equipoParaActualizar.setModelo(txtModeloAct.getText());
            equipoParaActualizar.setCpu(txtCpuAct.getText());
            equipoParaActualizar.setDiscoDuroGB(Integer.parseInt(txtDiscoDuroAct.getText()));
            equipoParaActualizar.setRamGB(Integer.parseInt(txtRamAct.getText()));
            equipoParaActualizar.setPrecio(Double.parseDouble(txtPrecioAct.getText()));

            if (radioLaptopAct.isSelected()) {
                equipoParaActualizar.setTipoEquipo("Laptop");
                Laptop laptop = (Laptop) equipoParaActualizar;
                laptop.setTamanoPantalla(Double.parseDouble(txtPantallaLaptopAct.getText()));
                laptop.setEsTouch(chkTouchLaptopAct.isSelected());
                laptop.setPuertosUSB(Integer.parseInt(txtUsbLaptopAct.getText()));
            } else {
                equipoParaActualizar.setTipoEquipo("Desktop");
                Desktop desktop = (Desktop) equipoParaActualizar;
                desktop.setPotenciaFuente(txtFuenteDesktopAct.getText());
                desktop.setFactorForma(txtFormaDesktopAct.getText());
            }

            if (equipoControlador.actualizarEquipo(equipoParaActualizar)) {
                JOptionPane.showMessageDialog(this, "Equipo actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarEquiposEnTabla();
                panelFormularioActualizar.setVisible(false);
                txtIdBuscarActualizar.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el equipo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Datos numéricos inválidos. Por favor, revise los campos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // --- Pestaña 4: Eliminar Equipo ---
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        panel.add(new JLabel("ID del Equipo a Eliminar:"));
        txtIdEliminar = new JTextField(10);
        panel.add(txtIdEliminar);
        btnEliminar = new JButton("Eliminar Equipo");
        panel.add(btnEliminar);
        
        btnEliminar.addActionListener(e -> eliminarEquipo());

        return panel;
    }
    
    private void eliminarEquipo() {
        String idTexto = txtIdEliminar.getText().trim();
        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID de equipo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);
            
            // Lógica de negocio para no eliminar equipos vendidos
            boolean haSidoVendido = false; // Implementar la lógica real con VentaControlador
            if (haSidoVendido) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar. Este equipo figura en una o más ventas.", "Operación Denegada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de que desea eliminar el equipo con ID " + id + "?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (equipoControlador.eliminarEquipo(id)) {
                    JOptionPane.showMessageDialog(this, "Equipo eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarEquiposEnTabla();
                    txtIdEliminar.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el equipo. Verifique que el ID existe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
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
