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

public class ClienteControlador {

    private final Connection connection;

    public ClienteControlador() {
        // Obtiene la instancia única de la conexión a la base de datos
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

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
            e.printStackTrace();
            return false;
        }
    }

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