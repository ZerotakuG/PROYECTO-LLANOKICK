package com.Llanokick.vistas;

import com.Llanokick.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaFactura extends JFrame {

    private JTable tablaFactura;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;

    public VentanaFactura(List<Producto> carrito, int[] cantidades) {
        setTitle("🧾 Factura de Compra");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Factura de Compra", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        // 🧮 Tabla
        String[] columnas = {"Imagen", "Nombre", "Color", "Talla", "Cantidad", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 0) ? ImageIcon.class : Object.class;
            }
        };

        tablaFactura = new JTable(modeloTabla);
        tablaFactura.setRowHeight(70);
        JScrollPane scroll = new JScrollPane(tablaFactura);
        add(scroll, BorderLayout.CENTER);

        // 🧾 Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        lblTotal = new JLabel("Total: $0", JLabel.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panelInferior.add(lblTotal, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelInferior.add(btnCerrar, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);

        // 🧠 Cargar productos
        cargarFactura(carrito, cantidades);
    }

    private void cargarFactura(List<Producto> carrito, int[] cantidades) {
        modeloTabla.setRowCount(0);
        double total = 0;

        for (int i = 0; i < carrito.size(); i++) {
            Producto p = carrito.get(i);
            int cant = cantidades[i];
            double subtotal = p.getPrecio() * cant;
            total += subtotal;

            ImageIcon icono = new ImageIcon(
                new ImageIcon(p.getImagenURL()).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)
            );

            modeloTabla.addRow(new Object[]{
                icono,
                p.getNombre(),
                p.getColor(),
                p.getTalla(),
                cant,
                "$" + subtotal
            });
        }

        lblTotal.setText("Total: $" + total);
    }
}
