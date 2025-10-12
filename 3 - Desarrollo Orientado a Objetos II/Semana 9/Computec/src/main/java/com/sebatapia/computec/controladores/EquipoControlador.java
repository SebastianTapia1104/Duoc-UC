package com.sebatapia.computec.controladores;

import com.sebatapia.computec.modelos.Desktop;
import com.sebatapia.computec.modelos.Equipo;
import com.sebatapia.computec.modelos.Laptop;
import com.sebatapia.computec.singleton.DatabaseConnection;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipoControlador {

    private final Connection connection;

    public EquipoControlador() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean registrarEquipo(Equipo equipo) {
        String sql = "{CALL sp_registrarEquipo(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, equipo.getModelo());
            stmt.setString(2, equipo.getCpu());
            stmt.setInt(3, equipo.getDiscoDuroGB());
            stmt.setInt(4, equipo.getRamGB());
            stmt.setDouble(5, equipo.getPrecio());
            stmt.setString(6, equipo.getTipoEquipo());

            if (equipo instanceof Laptop) {
                Laptop laptop = (Laptop) equipo;
                stmt.setDouble(7, laptop.getTamanoPantalla());
                stmt.setBoolean(8, laptop.isEsTouch());
                stmt.setInt(9, laptop.getPuertosUSB());
                stmt.setNull(10, java.sql.Types.VARCHAR);
                stmt.setNull(11, java.sql.Types.VARCHAR);
            } else if (equipo instanceof Desktop) {
                Desktop desktop = (Desktop) equipo;
                stmt.setNull(7, java.sql.Types.DOUBLE);
                stmt.setNull(8, java.sql.Types.BOOLEAN);
                stmt.setNull(9, java.sql.Types.INTEGER);
                stmt.setString(10, desktop.getPotenciaFuente());
                stmt.setString(11, desktop.getFactorForma());
            }
            stmt.registerOutParameter(12, java.sql.Types.INTEGER);

            stmt.executeUpdate();
            int idGenerado = stmt.getInt(12);
            if (idGenerado > 0) {
                equipo.setIdEquipo(idGenerado);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Equipo> listarEquipos() {
        List<Equipo> equipos = new ArrayList<>();
        String sql = "{CALL sp_listarEquipos()}";
        try (CallableStatement stmt = connection.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String tipoEquipo = rs.getString("tipo_equipo");
                if ("Laptop".equalsIgnoreCase(tipoEquipo)) {
                    equipos.add(new Laptop(
                        rs.getInt("id_equipo"), rs.getString("modelo"), rs.getString("cpu"),
                        rs.getInt("disco_duro_gb"), rs.getInt("ram_gb"), rs.getDouble("precio"),
                        rs.getDouble("tamano_pantalla"), rs.getBoolean("es_touch"), rs.getInt("puertos_usb")
                    ));
                } else if ("Desktop".equalsIgnoreCase(tipoEquipo)) {
                    equipos.add(new Desktop(
                        rs.getInt("id_equipo"), rs.getString("modelo"), rs.getString("cpu"),
                        rs.getInt("disco_duro_gb"), rs.getInt("ram_gb"), rs.getDouble("precio"),
                        rs.getString("potencia_fuente"), rs.getString("factor_forma")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipos;
    }
    
    public Equipo buscarEquipoPorId(int id) {
        String sql = "{CALL sp_buscarEquipoPorId(?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipoEquipo = rs.getString("tipo_equipo");
                    if ("Laptop".equalsIgnoreCase(tipoEquipo)) {
                        return new Laptop(
                            rs.getInt("id_equipo"), rs.getString("modelo"), rs.getString("cpu"),
                            rs.getInt("disco_duro_gb"), rs.getInt("ram_gb"), rs.getDouble("precio"),
                            rs.getDouble("tamano_pantalla"), rs.getBoolean("es_touch"), rs.getInt("puertos_usb")
                        );
                    } else if ("Desktop".equalsIgnoreCase(tipoEquipo)) {
                        return new Desktop(
                            rs.getInt("id_equipo"), rs.getString("modelo"), rs.getString("cpu"),
                            rs.getInt("disco_duro_gb"), rs.getInt("ram_gb"), rs.getDouble("precio"),
                            rs.getString("potencia_fuente"), rs.getString("factor_forma")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizarEquipo(Equipo equipo) {
        String sql = "{CALL sp_actualizarEquipo(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, equipo.getIdEquipo());
            stmt.setString(2, equipo.getModelo());
            stmt.setString(3, equipo.getCpu());
            stmt.setInt(4, equipo.getDiscoDuroGB());
            stmt.setInt(5, equipo.getRamGB());
            stmt.setDouble(6, equipo.getPrecio());
            stmt.setString(7, equipo.getTipoEquipo());

            if (equipo instanceof Laptop) {
                Laptop laptop = (Laptop) equipo;
                stmt.setDouble(8, laptop.getTamanoPantalla());
                stmt.setBoolean(9, laptop.isEsTouch());
                stmt.setInt(10, laptop.getPuertosUSB());
                stmt.setNull(11, java.sql.Types.VARCHAR);
                stmt.setNull(12, java.sql.Types.VARCHAR);
            } else if (equipo instanceof Desktop) {
                Desktop desktop = (Desktop) equipo;
                stmt.setNull(8, java.sql.Types.DOUBLE);
                stmt.setNull(9, java.sql.Types.BOOLEAN);
                stmt.setNull(10, java.sql.Types.INTEGER);
                stmt.setString(11, desktop.getPotenciaFuente());
                stmt.setString(12, desktop.getFactorForma());
            }

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarEquipo(int id) {
        String sql = "{CALL sp_eliminarEquipo(?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}