package com.grupo11.magenta.vistas;

import com.grupo11.magenta.interfaces.RefreshListener;
import com.grupo11.magenta.modelos.Cartelera;
import com.grupo11.magenta.modelos.Pelicula;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ActualizarPeliculaPanel extends JPanel {

    private final Cartelera cartelera = new Cartelera();
    private final RefreshListener listener;

    // Campos de control
    private JTextField idField = new JTextField(5);
    private JTextField tituloField = new JTextField(20);
    private JTextField directorField = new JTextField(20);
    private JTextField anioField = new JTextField(10);
    private JTextField duracionField = new JTextField(10);
    private JComboBox<String> generoComboBox;
    
    // Botones
    private JButton buscarButton = new JButton("Buscar ID");
    private JButton actualizarButton = new JButton("Actualizar Película");
    private JButton limpiarButton = new JButton("Limpiar Campos");

    public ActualizarPeliculaPanel(RefreshListener listener) {
        this.listener = listener;

        String[] generos = {"Comedia", "Drama", "Accion", "Ciencia ficcion", "Terror", "Animacion", "Aventura", "Musical"};
        generoComboBox = new JComboBox<>(generos);
        
        // Inicialmente deshabilitar los campos de datos y el botón de actualizar
        setCamposEditables(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 0: Búsqueda
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("ID a buscar:"), gbc);
        gbc.gridx = 1;
        add(idField, gbc);
        gbc.gridx = 2;
        add(buscarButton, gbc);

        // Fila 1 a 5: Campos de actualización (Datos de la película)
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; add(tituloField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; add(new JLabel("Director:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; add(directorField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; add(new JLabel("Año:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; add(anioField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; add(new JLabel("Duración (min):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; add(duracionField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; add(new JLabel("Género:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; add(generoComboBox, gbc);

        // Fila 6: Botones de Acción
        gbc.gridy = 6;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(actualizarButton, gbc);
        gbc.gridx = 2;
        add(limpiarButton, gbc);

        // Añadir ActionListeners
        buscarButton.addActionListener(e -> buscarPelicula());
        actualizarButton.addActionListener(e -> actualizarPelicula());
        limpiarButton.addActionListener(e -> limpiarCampos());
    }

    private void setCamposEditables(boolean editable) {
        tituloField.setEditable(editable);
        directorField.setEditable(editable);
        anioField.setEditable(editable);
        duracionField.setEditable(editable);
        generoComboBox.setEnabled(editable);
        actualizarButton.setEnabled(editable);
    }
    
    private void limpiarCampos() {
        idField.setText("");
        tituloField.setText("");
        directorField.setText("");
        anioField.setText("");
        duracionField.setText("");
        generoComboBox.setSelectedIndex(0);
        setCamposEditables(false);
    }

    private void buscarPelicula() {
        try {
            int id = Integer.parseInt(idField.getText());
            Pelicula p = cartelera.obtenerPeliculaPorId(id);

            if (p != null) {
                // Cargar datos en los campos
                tituloField.setText(p.getTitulo());
                directorField.setText(p.getDirector());
                anioField.setText(String.valueOf(p.getAnio()));
                duracionField.setText(String.valueOf(p.getDuracion()));
                generoComboBox.setSelectedItem(p.getGenero());
                setCamposEditables(true);
                JOptionPane.showMessageDialog(this, "Película encontrada. Puede modificar los datos.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                limpiarCampos(); // Limpia y deshabilita si no la encuentra
                JOptionPane.showMessageDialog(this, "❌ Película con ID " + id + " no existe.", "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarPelicula() {
        try {
            // 1. Validar campos vacíos
            if (tituloField.getText().isEmpty() || directorField.getText().isEmpty() || 
                anioField.getText().isEmpty() || duracionField.getText().isEmpty() || 
                Objects.isNull(generoComboBox.getSelectedItem())) {
                JOptionPane.showMessageDialog(this, "Todos los campos deben estar llenos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Obtener datos
            int id = Integer.parseInt(idField.getText()); // Usar el ID buscado
            String titulo = tituloField.getText();
            String director = directorField.getText();
            int anio = Integer.parseInt(anioField.getText());
            int duracion = Integer.parseInt(duracionField.getText());
            String genero = (String) generoComboBox.getSelectedItem();

            // 3. Crear y actualizar
            Pelicula peliculaActualizada = new Pelicula(id, titulo, director, anio, duracion, genero);
            cartelera.actualizarPelicula(peliculaActualizada);
            
            JOptionPane.showMessageDialog(this, "✅ Película actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            if (listener != null) {
                listener.onRefreshNeeded();
            }
            limpiarCampos(); // Limpiar después de la actualización exitosa

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID, año y duración deben ser números válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
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
