package com.Llanokick.vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import com.Llanokick.Producto;

public class VentanaCarrito extends JFrame {

    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    private JButton btnFinalizar, btnVaciar;

    private List<Producto> carrito;
    private int[] cantidades; // guardar cantidades por fila

    public VentanaCarrito(List<Producto> carrito) {
        this.carrito = carrito;
        cantidades = new int[carrito.size()];

        setTitle("🛒 Carrito de Compras");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Tu Carrito", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        String[] columnas = {"Imagen", "Nombre", "Talla", "Color", "Precio", "Cantidad", "+", "-"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6 || column == 7;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 0) ? ImageIcon.class : Object.class;
            }
        };

        tablaCarrito = new JTable(modeloTabla);
        tablaCarrito.setRowHeight(60);

        JScrollPane scroll = new JScrollPane(tablaCarrito);
        add(scroll, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));

        lblTotal = new JLabel("Total: $0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setHorizontalAlignment(JLabel.RIGHT);
        panelInferior.add(lblTotal, BorderLayout.NORTH);

        JPanel botones = new JPanel();
        btnFinalizar = new JButton("Finalizar Compra");
        btnVaciar = new JButton("Vaciar Carrito");
        botones.add(btnFinalizar);
        botones.add(btnVaciar);
        panelInferior.add(botones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        cargarCarrito();
        calcularTotal();

        tablaCarrito.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaCarrito.getSelectedRow();
                int col = tablaCarrito.getSelectedColumn();

                if (col == 6) { aumentarCantidad(fila); }
                if (col == 7) { disminuirCantidad(fila); }
            }
        });

        btnVaciar.addActionListener(e -> vaciarCarrito());
        btnFinalizar.addActionListener(e -> finalizarCompra());
    }

    private void cargarCarrito() {
        modeloTabla.setRowCount(0);
        int index = 0;

        for (Producto p : carrito) {
            cantidades[index] = 1;

            ImageIcon icono = new ImageIcon(
                new ImageIcon(p.getImagenURL()).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)
            );

            modeloTabla.addRow(new Object[]{
                icono,
                p.getNombre(),
                p.getTalla(),
                p.getColor(),
                "$" + p.getPrecio(),
                cantidades[index],
                "➕",
                "➖"
            });

            index++;
        }
    }

    private void aumentarCantidad(int fila) {
        cantidades[fila]++;
        modeloTabla.setValueAt(cantidades[fila], fila, 5);
        calcularTotal();
    }

    private void disminuirCantidad(int fila) {
        if (cantidades[fila] > 1) {
            cantidades[fila]--;
            modeloTabla.setValueAt(cantidades[fila], fila, 5);
        } else {
            eliminarProducto(fila);
        }
        calcularTotal();
    }

    private void eliminarProducto(int fila) {
        carrito.remove(fila);
        modeloTabla.removeRow(fila);

        int[] nuevo = new int[cantidades.length - 1];
        int j = 0;
        for (int i = 0; i < cantidades.length; i++) {
            if (i != fila) nuevo[j++] = cantidades[i];
        }
        cantidades = nuevo;
    }

    private void calcularTotal() {
        double total = 0;
        for (int i = 0; i < carrito.size(); i++) {
            total += carrito.get(i).getPrecio() * cantidades[i];
        }
        lblTotal.setText("Total: $" + total);
    }

    private void vaciarCarrito() {
        carrito.clear();
        modeloTabla.setRowCount(0);
        lblTotal.setText("Total: $0");
        JOptionPane.showMessageDialog(this, "🧹 Carrito vaciado.");
    }

    private void finalizarCompra() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tu carrito está vacío 😅");
            return;
        }
        JOptionPane.showMessageDialog(this, "✅ ¡Compra finalizada!");
        vaciarCarrito();
    }
}
