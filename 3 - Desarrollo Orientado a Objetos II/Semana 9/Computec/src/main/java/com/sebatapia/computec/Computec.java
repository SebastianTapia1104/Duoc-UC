package com.sebatapia.computec;

import com.sebatapia.computec.singleton.DatabaseConnection;
import com.sebatapia.computec.vistas.MainFrame; // Vista principal

public class Computec {
    
    public static void main(String[] args) {
        System.out.println("--- Iniciando Sistema Computec ---");

        // 1. Ejecutar el test de la conexión a la base de datos (Singleton)
        System.out.println("Verificando Conexión a Base de Datos:");
        DatabaseConnection.test(); // Esto imprimirá el resultado de la prueba en la consola
        System.out.println("---------------------------------");

        // 2. Iniciar la Interfaz de Usuario en el Event Dispatch Thread (EDT)
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Configuración opcional del Look and Feel
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                // Si falla el Look and Feel, la aplicación usa el predeterminado.
                System.err.println("No se pudo establecer el Look and Feel Nimbus.");
            }
            
            // Crear y mostrar la ventana principal (MainFrame)
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}