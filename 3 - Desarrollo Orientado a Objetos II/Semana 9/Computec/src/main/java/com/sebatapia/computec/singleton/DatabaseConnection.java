package com.sebatapia.computec.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private final String URL = "jdbc:mysql://localhost:3306/computec";
    private final String USER = "root"; 
    private final String PASSWORD = "admin1104";

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // Maneja la excepción adecuadamente
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
    
    public static void test() {
        try {
            Connection conn = getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión OK (DatabaseConnection.test)");
            } else {
                 // Si conn es nulo o está cerrada, significa que el constructor falló.
                System.out.println("❌ No se pudo conectar: La conexión es nula o está cerrada. Revisa credenciales y estado de MySQL.");
            }
        } catch (SQLException e) {
            // Esto captura errores si la conexión se invalidó después de su creación.
            System.out.println("❌ Error de SQL al verificar la conexión: " + e.getMessage());
        }
    }
}