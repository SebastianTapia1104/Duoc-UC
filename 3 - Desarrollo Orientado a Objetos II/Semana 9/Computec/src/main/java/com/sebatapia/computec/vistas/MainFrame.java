package com.sebatapia.computec.vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.border.Border;

/**
 * Ventana Principal de la Aplicación Computec.
 * Contiene un menú para navegar entre los diferentes módulos (JPanels)
 * utilizando un CardLayout. Esta es la clase que se ejecuta para iniciar el programa.
 */
public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Declaración de los 5 paneles principales (las clases reales)
    private ResumenPanel resumenPanel;
    private ClientesPanel clientesPanel;
    private EquiposPanel equiposPanel;
    private VentasPanel ventaPanel;
    private ReportesPanel reportesPanel;

    public MainFrame() {
        // --- 1. Configuración básica del JFrame ---
        setTitle("Sistema de Gestión y Ventas - Computec");
        setSize(950, 750); // Un poco más grande para acomodar los paneles
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en la pantalla
        setLayout(new BorderLayout());

        // --- 2. Creación del Menú de Navegación ---
        crearMenuBar();

        // --- 3. Configuración del Panel Central con CardLayout ---
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // --- 4. Inicialización y adición de los paneles al CardLayout ---
        inicializarPaneles();
        
        // --- 5. Añadir el panel principal al JFrame ---
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Crea y configura la barra de menú superior para la navegación.
     */
    private void crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // --- 1. Definimos el estilo para los botones del menú ---
        Font menuFont = new Font("Segoe UI", Font.BOLD, 14); // Fuente más grande y legible
        // El borde vacío añade un relleno: 5px arriba/abajo, 10px izquierda/derecha
        Border menuPadding = BorderFactory.createEmptyBorder(5, 10, 5, 10);

        // --- 2. Creamos los JMenus y aplicamos el nuevo estilo ---
        JMenu menuResumen = new JMenu("Resumen");
        menuResumen.setFont(menuFont);
        menuResumen.setBorder(menuPadding);

        JMenu menuClientes = new JMenu("Clientes");
        menuClientes.setFont(menuFont);
        menuClientes.setBorder(menuPadding);

        JMenu menuEquipos = new JMenu("Equipos");
        menuEquipos.setFont(menuFont);
        menuEquipos.setBorder(menuPadding);

        JMenu menuVenta = new JMenu("Venta");
        menuVenta.setFont(menuFont);
        menuVenta.setBorder(menuPadding);

        JMenu menuReportes = new JMenu("Reportes");
        menuReportes.setFont(menuFont);
        menuReportes.setBorder(menuPadding);

        // --- 3. Asignamos los listeners para cambiar de panel ---
        menuResumen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "Resumen");
            }
        });

        menuClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "Clientes");
            }
        });

        menuEquipos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "Equipos");
            }
        });

        menuVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "Venta");
            }
        });

        menuReportes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "Reportes");
            }
        });

        // --- 4. Añadimos los menús a la barra ---
        menuBar.add(menuResumen);
        menuBar.add(menuClientes);
        menuBar.add(menuEquipos);
        menuBar.add(menuVenta);
        menuBar.add(menuReportes);

        setJMenuBar(menuBar);
    }

    /**
     * Instancia cada panel, le asigna listeners para actualización dinámica
     * y los agrega al contenedor principal con CardLayout.
     */
    private void inicializarPaneles() {
        resumenPanel = new ResumenPanel();
        clientesPanel = new ClientesPanel();
        equiposPanel = new EquiposPanel();
        ventaPanel = new VentasPanel();
        reportesPanel = new ReportesPanel();

        // Listener para el panel Resumen: actualiza los datos cada vez que se muestra.
        resumenPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent evt) {
                resumenPanel.actualizarDatos();
            }
        });

        // Listener para el panel Reportes: actualiza el reporte cada vez que se muestra.
        reportesPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent evt) {
                reportesPanel.generarReporte();
            }
        });

        // Agregamos los paneles al contenedor principal con un nombre único
        mainPanel.add(resumenPanel, "Resumen");
        mainPanel.add(clientesPanel, "Clientes");
        mainPanel.add(equiposPanel, "Equipos");
        mainPanel.add(ventaPanel, "Venta");
        mainPanel.add(reportesPanel, "Reportes");

        // Mostramos el panel de resumen por defecto al iniciar
        cardLayout.show(mainPanel, "Resumen");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
