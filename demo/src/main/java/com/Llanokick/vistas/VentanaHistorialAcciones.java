package com.Llanokick.vistas;

import com.Llanokick.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VentanaHistorialAcciones extends JFrame {
    private JTable tablaHistorial;
    private DefaultTableModel modelo;

    public VentanaHistorialAcciones() {
        setTitle("📜 Historial de Acciones");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new String[]{"ID", "Usuario ID", "Acción", "Fecha"}, 0);
        tablaHistorial = new JTable(modelo);
        tablaHistorial.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.addActionListener(e -> cargarHistorial());
        add(btnActualizar, BorderLayout.SOUTH);

        cargarHistorial();
    }

    private void cargarHistorial() {
        modelo.setRowCount(0);
        try (Connection conn = new Conexion().conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM logacciones ORDER BY fecha DESC")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id_log"),
                        rs.getInt("usuario_id"),
                        rs.getString("accion"),
                        rs.getTimestamp("fecha")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error al cargar historial: " + e.getMessage());
        }
    }
}
