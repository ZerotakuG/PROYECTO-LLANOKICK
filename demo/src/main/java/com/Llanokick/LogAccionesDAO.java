package com.Llanokick;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogAccionesDAO {
    private Conexion conexion = new Conexion();

    // 🔹 Registrar una acción
    public void registrarAccion(int usuarioId, String accion) {
        String sql = "INSERT INTO logacciones (usuario_id, accion) VALUES (?, ?)";
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setString(2, accion);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error al registrar acción: " + e.getMessage());
        }
    }

    // 🔹 Listar logs (para mostrar en la ventana de admin)
    public List<String[]> listarLogs() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT l.id_log, u.nombre AS usuario, l.accion, l.fecha " +
                     "FROM logacciones l " +
                     "INNER JOIN usuario u ON l.usuario_id = u.id_usuario " +
                     "ORDER BY l.fecha DESC";

        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new String[]{
                    String.valueOf(rs.getInt("id_log")),
                    rs.getString("usuario"),
                    rs.getString("accion"),
                    rs.getString("fecha")
                });
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar logs: " + e.getMessage());
        }
        return lista;
    }
}
