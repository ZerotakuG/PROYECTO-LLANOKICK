package com.Llanokick;

import java.sql.*;

public class UsuarioDAO {
    private Conexion conexion = new Conexion();

    public Usuario login(String correo, String clave) {
        String sql = "SELECT u.id_usuario, u.nombre, u.correo, u.clave, r.nombre AS rol " +
                     "FROM usuario u " +
                     "INNER JOIN rol r ON u.rol_id = r.id_rol " +
                     "WHERE u.correo = ? AND u.clave = ?";

        try (Connection conn = conexion.conectar();
        
             PreparedStatement ps = conn.prepareStatement(sql)) {
                System.out.println("🟡 Intentando login con: " + correo + " / " + clave);

            ps.setString(1, correo);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();
                System.out.println("🔎 Ejecutando consulta...");

            if (rs.next()) {
                Usuario user = new Usuario();
                user.setIdUsuario(rs.getInt("id_usuario"));
                user.setNombre(rs.getString("nombre"));
                user.setCorreo(rs.getString("correo"));
                user.setRol(rs.getString("rol"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
