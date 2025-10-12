package com.sebatapia.computec.controladores;

import com.sebatapia.computec.modelos.Venta;
import com.sebatapia.computec.singleton.DatabaseConnection;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VentaControlador {

    private final Connection connection;

    public VentaControlador() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean registrarVenta(Venta venta) {
        String sql = "{CALL sp_registrarVenta(?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(venta.getFechaHora()));
            stmt.setString(2, venta.getCliente().getRut());
            stmt.setInt(3, venta.getEquipo().getIdEquipo());
            stmt.setDouble(4, venta.getPrecioFinal()); // Usamos el precio final guardado en la venta

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> generarReporteVentas(String tipoEquipoFiltro) {
        List<Object[]> reporte = new ArrayList<>();
        String sql = "{CALL sp_reporteVentas(?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, tipoEquipoFiltro);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = {
                        rs.getString("modelo_equipo"),
                        rs.getString("nombre_cliente"),
                        rs.getString("telefono_cliente"),
                        rs.getString("correo_cliente"),
                        rs.getDouble("precio_venta")
                    };
                    reporte.add(fila);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reporte;
    }

    public Map<String, Object> obtenerEstadisticasVentas() {
        Map<String, Object> estadisticas = new HashMap<>();
        String sql = "{CALL sp_estadisticasVentas()}";
        try (CallableStatement stmt = connection.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                estadisticas.put("total_ventas", rs.getInt("total_ventas"));
                estadisticas.put("monto_total", rs.getDouble("monto_total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estadisticas;
    }
}