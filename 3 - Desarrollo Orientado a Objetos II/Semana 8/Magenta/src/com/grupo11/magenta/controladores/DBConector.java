package com.grupo11.magenta.controladores;

/* Importamos las clases necesarias para la conexión con base de datos MySQL usando JDBC */
import java.sql.Connection;     // Representa la conexión activa con la base de datos.
import java.sql.DriverManager; // Clase que gestiona los controladores JDBC y permite obtener conexiones.
import java.sql.SQLException;  // Excepción que se lanza si hay errores al interactuar con la base de datos.

/* Clase pública que gestiona la conexión a la base de datos */
public class DBConector {

    private static final String URL = "jdbc:mysql://localhost:3306/Cine_DB";

    // Usuario con el que se accederá a la base de datos
    private static final String USER = "root";

    // Contraseña correspondiente al usuario de la base de datos
    private static final String PASSWORD = "admin1104";

    /**
     * Método estático que devuelve un objeto Connection
     * Lanza SQLException si ocurre un error al conectarse.
     * @return 
     * @throws java.sql.SQLException
     */
    public static Connection getConnection() throws SQLException {
        // Usamos DriverManager para obtener la conexión con los parámetros definidos arriba.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Método de prueba para verificar si la conexión a la base de datos funciona correctamente.
     * No lanza error hacia fuera, sino que imprime el resultado en consola.
     */
    public static void test() {
        // Se usa un bloque try-with-resources para asegurarse que la conexión se cierre automáticamente.
        try (Connection ignored = getConnection()) {
            // Si la conexión fue exitosa, imprimimos mensaje positivo.
            System.out.println("✅ Conexión OK (DatabaseConnection.test)");
        } catch (SQLException e) {
            // Si falló la conexión, mostramos el mensaje de error.
            System.out.println("❌ No se pudo conectar: " + e.getMessage());
        }
    }

}