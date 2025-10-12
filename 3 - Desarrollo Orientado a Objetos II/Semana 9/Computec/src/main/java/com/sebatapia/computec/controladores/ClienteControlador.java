// package com.sebatapia.computec.controladores;
package com.sebatapia.computec.controladores;

import com.sebatapia.computec.modelos.Cliente;
import com.sebatapia.computec.singleton.DatabaseConnection;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador: Gestiona la lógica de negocio y las operaciones CRUD para los Clientes.
 * Se comunica con la base de datos a través de procedimientos almacenados.
 */
public class ClienteControlador {

    private final Connection connection;

    public ClienteControlador() {
        // Obtiene la instancia única de la conexión a la base de datos
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Registra un nuevo cliente en la base de datos.
     * @param cliente El objeto Cliente con los datos a guardar.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registrarCliente(Cliente cliente) {
        // Llama al procedimiento almacenado sp_registrarCliente
        String sql = "{CALL sp_registrarCliente(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, cliente.getRut());
            stmt.setString(2, cliente.getNombreCompleto());
            stmt.setString(3, cliente.getDireccion());
            stmt.setString(4, cliente.getComuna());
            stmt.setString(5, cliente.getCorreoElectronico());
            stmt.setString(6, cliente.getTelefono());
            stmt.setDate(7, Date.valueOf(cliente.getFechaNacimiento()));
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Es buena práctica usar un logger en una app real
            return false;
        }
    }

    /**
     * Busca un cliente por su RUT.
     * @param rut El RUT del cliente a buscar.
     * @return Un objeto Cliente si se encuentra, de lo contrario null.
     */
    public Cliente buscarClientePorRut(String rut) {
        String sql = "{CALL sp_buscarClientePorRut(?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, rut);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                        rs.getString("rut"),
                        rs.getString("nombre_completo"),
                        rs.getString("direccion"),
                        rs.getString("comuna"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getDate("fecha_nacimiento").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene una lista de todos los clientes registrados.
     * @return Una lista de objetos Cliente.
     */
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "{CALL sp_listarClientes()}";
        try (CallableStatement stmt = connection.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clientes.add(new Cliente(
                    rs.getString("rut"),
                    rs.getString("nombre_completo"),
                    rs.getString("direccion"),
                    rs.getString("comuna"),
                    rs.getString("correo"),
                    rs.getString("telefono"),
                    rs.getDate("fecha_nacimiento").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    /**
     * Actualiza los datos de un cliente existente.
     * @param cliente El objeto Cliente con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarCliente(Cliente cliente) {
        String sql = "{CALL sp_actualizarCliente(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, cliente.getRut());
            stmt.setString(2, cliente.getNombreCompleto());
            stmt.setString(3, cliente.getDireccion());
            stmt.setString(4, cliente.getComuna());
            stmt.setString(5, cliente.getCorreoElectronico());
            stmt.setString(6, cliente.getTelefono());
            stmt.setDate(7, Date.valueOf(cliente.getFechaNacimiento()));

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un cliente de la base de datos usando su RUT.
     * @param rut El RUT del cliente a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarCliente(String rut) {
        String sql = "{CALL sp_eliminarCliente(?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, rut);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}