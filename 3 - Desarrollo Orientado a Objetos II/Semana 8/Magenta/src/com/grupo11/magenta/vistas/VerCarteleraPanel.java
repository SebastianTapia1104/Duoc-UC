package com.grupo11.magenta.vistas;

import com.grupo11.magenta.modelos.Cartelera;
import com.grupo11.magenta.modelos.Pelicula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VerCarteleraPanel extends JPanel {

    private final Cartelera cartelera = new Cartelera();
    private JTable tablaPeliculas;
    private DefaultTableModel tablaModelo;
    
    // Componentes de filtro
    private JComboBox<String> generoComboBox;
    private JTextField anioInicioField = new JTextField(5);
    private JTextField anioFinField = new JTextField(5);
    private JButton filtrarGeneroButton = new JButton("Filtrar Género");
    private JButton filtrarAniosButton = new JButton("Filtrar Años"); 
    private JButton limpiarFiltrosButton = new JButton("Limpiar Filtros"); // El botón que necesitas

    public VerCarteleraPanel() {
        setLayout(new BorderLayout());
        
        // 1. Panel de Controles (Filtros)
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5)); // Ajustamos el FlowLayout
        
        // Configuración de JComboBox de Géneros
        String[] generos = {"-- Seleccionar Género --", "Comedia", "Drama", "Accion", "Ciencia ficcion", "Terror", "Animacion", "Aventura", "Musical"};
        generoComboBox = new JComboBox<>(generos);
        
        // Añadir componentes al Panel de Controles
        controlPanel.add(new JLabel("Filtrar por Género:"));
        controlPanel.add(generoComboBox);
        controlPanel.add(filtrarGeneroButton);
        
        // Separador visual (ayuda a la organización)
        controlPanel.add(new JSeparator(SwingConstants.VERTICAL));
        
        // Componentes del filtro por Años
        controlPanel.add(new JLabel("Rango de Años (Desde):"));
        controlPanel.add(anioInicioField);
        controlPanel.add(new JLabel("Hasta:"));
        controlPanel.add(anioFinField);
        controlPanel.add(filtrarAniosButton);
        
        // AÑADIR EL BOTÓN DE LIMPIAR FILTROS
        controlPanel.add(limpiarFiltrosButton);
        
        add(controlPanel, BorderLayout.NORTH); 

        // 2. JTable para el Listado de Películas
        String[] columnas = {"ID", "Título", "Director", "Año", "Duración", "Género"};
        tablaModelo = new DefaultTableModel(columnas, 0);
        tablaPeliculas = new JTable(tablaModelo);
        
        JScrollPane scrollPane = new JScrollPane(tablaPeliculas);
        add(scrollPane, BorderLayout.CENTER);
        
        // 3. Asignar Listeners
        filtrarGeneroButton.addActionListener(e -> filtrarPorGenero());
        filtrarAniosButton.addActionListener(e -> filtrarPorAnios());
        limpiarFiltrosButton.addActionListener(e -> limpiarYMostrarTodo()); 
        
        cargarDatosTabla();
    }

    /**
     * Llama al controlador para obtener TODAS las películas y las muestra.
     */
    public void cargarDatosTabla() {
        cargarDatosTabla(cartelera.obtenerPeliculas());
    }
    
    /**
     * Método auxiliar para poblar el JTable con una lista específica de películas.
     */
    private void cargarDatosTabla(List<Pelicula> peliculas) {
        tablaModelo.setRowCount(0); // Limpiar filas existentes
        for (Pelicula p : peliculas) {
            Object[] fila = { 
                p.getId(), 
                p.getTitulo(), 
                p.getDirector(), 
                p.getAnio(), 
                p.getDuracion(), 
                p.getGenero() 
            };
            tablaModelo.addRow(fila);
        }
    }

    /**
     * Limpia los campos de filtro y recarga la tabla con todas las películas.
     */
    private void limpiarYMostrarTodo() {
        // Limpiar elementos visuales de filtro
        generoComboBox.setSelectedIndex(0);
        anioInicioField.setText("");
        anioFinField.setText("");
        
        // Recargar la tabla sin filtros
        cargarDatosTabla(); 
    }

    private void filtrarPorGenero() {
        String generoSeleccionado = (String) generoComboBox.getSelectedItem();
        
        if (generoSeleccionado == null || generoSeleccionado.equals("-- Seleccionar Género --")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un género válido.", "Error de Filtro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Pelicula> resultados = cartelera.filtrarPorGenero(generoSeleccionado);
        cargarDatosTabla(resultados);
    }
    
    private void filtrarPorAnios() {
        String inicioStr = anioInicioField.getText().trim();
        String finStr = anioFinField.getText().trim();

        if (inicioStr.isEmpty() && finStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar al menos un valor de año (Desde o Hasta).", "Error de Filtro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int anioInicio = inicioStr.isEmpty() ? 0 : Integer.parseInt(inicioStr);
            int anioFin = finStr.isEmpty() ? 9999 : Integer.parseInt(finStr);

            if (anioInicio > anioFin) {
                JOptionPane.showMessageDialog(this, "El año de inicio no puede ser mayor al año final.", "Error de Rango", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<Pelicula> resultados = cartelera.filtrarPorRangoAnios(anioInicio, anioFin);
            cargarDatosTabla(resultados);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese años válidos (números enteros).", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
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
