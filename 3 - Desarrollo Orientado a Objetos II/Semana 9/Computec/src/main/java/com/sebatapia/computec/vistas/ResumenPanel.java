// package com.sebatapia.computec.vistas;
package com.sebatapia.computec.vistas;

import com.sebatapia.computec.controladores.ClienteControlador;
import com.sebatapia.computec.controladores.VentaControlador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 * Panel que muestra un resumen o dashboard con los principales indicadores
 * de negocio, como total recaudado, número de clientes y ventas por categoría.
 */
public class ResumenPanel extends JPanel {

    // --- Controladores para acceder a la lógica de negocio ---
    private final VentaControlador ventaControlador;
    private final ClienteControlador clienteControlador;

    // --- JLabels que mostrarán los valores dinámicos ---
    private JLabel lblTotalRecaudadoValor;
    private JLabel lblTotalClientesValor;
    private JLabel lblTotalVentasValor;
    private JLabel lblVentasLaptopsValor;
    private JLabel lblVentasDesktopsValor;

    public ResumenPanel() {
        // Inicializamos los controladores
        this.ventaControlador = new VentaControlador();
        this.clienteControlador = new ClienteControlador();

        // --- Configuración del Layout principal del panel ---
        // GridLayout con 3 filas, 2 columnas, y espaciado de 20px
        setLayout(new GridLayout(3, 2, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen exterior

        // --- Inicialización de los JLabels ---
        // Usamos una fuente grande para que los números destaquen
        Font valorFont = new Font("Arial", Font.BOLD, 32);
        lblTotalRecaudadoValor = new JLabel("...", SwingConstants.CENTER);
        lblTotalRecaudadoValor.setFont(valorFont);
        
        lblTotalClientesValor = new JLabel("...", SwingConstants.CENTER);
        lblTotalClientesValor.setFont(valorFont);

        lblTotalVentasValor = new JLabel("...", SwingConstants.CENTER);
        lblTotalVentasValor.setFont(valorFont);

        lblVentasLaptopsValor = new JLabel("...", SwingConstants.CENTER);
        lblVentasLaptopsValor.setFont(valorFont);

        lblVentasDesktopsValor = new JLabel("...", SwingConstants.CENTER);
        lblVentasDesktopsValor.setFont(valorFont);

        // --- Creación y adición de los 5 cuadros de resumen ---
        add(crearCuadroResumen("Total Recaudado", lblTotalRecaudadoValor));
        add(crearCuadroResumen("Total Clientes Registrados", lblTotalClientesValor));
        add(crearCuadroResumen("Total Ventas Realizadas", lblTotalVentasValor));
        add(crearCuadroResumen("Ventas de Laptops", lblVentasLaptopsValor));
        add(crearCuadroResumen("Ventas de Desktops", lblVentasDesktopsValor));

        // --- Cargar los datos iniciales ---
        actualizarDatos();
    }

    /**
     * Método fábrica para crear un panel estilizado para cada indicador.
     * @param titulo El título que se mostrará en el borde del panel.
     * @param labelValor El JLabel que contendrá el valor numérico.
     * @return Un JPanel estilizado y listo para ser agregado al layout principal.
     */
    private JPanel crearCuadroResumen(String titulo, JLabel labelValor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            titulo,
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        panel.add(labelValor, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Obtiene los datos más recientes de los controladores y actualiza los JLabels.
     * Este método debe ser público para poder llamarlo desde fuera cuando sea necesario.
     */
    public void actualizarDatos() {
        // --- Obtener estadísticas generales de ventas ---
        Map<String, Object> estadisticas = ventaControlador.obtenerEstadisticasVentas();
        int totalVentas = (int) estadisticas.getOrDefault("total_ventas", 0);
        double montoTotal = (double) estadisticas.getOrDefault("monto_total", 0.0);
        
        // Formateador para moneda local (CLP)
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CL"));
        lblTotalRecaudadoValor.setText(formatoMoneda.format(montoTotal));
        lblTotalVentasValor.setText(String.valueOf(totalVentas));

        // --- Obtener total de clientes ---
        int totalClientes = clienteControlador.listarClientes().size();
        lblTotalClientesValor.setText(String.valueOf(totalClientes));

        // --- Obtener ventas por tipo de equipo ---
        // Se cuenta el tamaño de la lista devuelta por el reporte filtrado
        int ventasLaptops = ventaControlador.generarReporteVentas("Laptop").size();
        int ventasDesktops = ventaControlador.generarReporteVentas("Desktop").size();
        lblVentasLaptopsValor.setText(String.valueOf(ventasLaptops));
        lblVentasDesktopsValor.setText(String.valueOf(ventasDesktops));
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
