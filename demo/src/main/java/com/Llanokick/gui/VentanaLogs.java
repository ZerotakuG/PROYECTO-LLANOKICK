package com.Llanokick.gui;

import com.Llanokick.LogAccionesDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaLogs extends JFrame {
    private LogAccionesDAO logDAO = new LogAccionesDAO();
    private DefaultTableModel modelo;

    public VentanaLogs() {
        setTitle("📜 Historial de Acciones - Admin");
        setSize(800, 400);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(new String[]{"ID", "Usuario", "Acción", "Fecha"}, 0);
        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        cargarLogs();
    }

    private void cargarLogs() {
        logDAO.listarLogs().forEach(fila -> modelo.addRow(fila));
    }
}
