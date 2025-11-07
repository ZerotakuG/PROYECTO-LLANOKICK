package com.Llanokick.vistas;

import com.Llanokick.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VentanaAgregarProducto extends JFrame {
    private JTextField txtNombre, txtPrecio, txtStock, txtTalla, txtColor, txtImagenURL, txtCategoriaId, txtMarcaId;
    private JTextArea txtDescripcion;
    private ProductoDAO productoDAO = new ProductoDAO();
    private VentanaAdmin ventanaAdmin;

    public VentanaAgregarProducto(VentanaAdmin ventanaAdmin) {
        this.ventanaAdmin = ventanaAdmin;
        setTitle("➕ Agregar Producto");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridLayout(9, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextArea(2, 10);
        panelCampos.add(new JScrollPane(txtDescripcion));

        panelCampos.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelCampos.add(txtPrecio);

        panelCampos.add(new JLabel("Stock:"));
        txtStock = new JTextField();
        panelCampos.add(txtStock);

        panelCampos.add(new JLabel("Talla:"));
        txtTalla = new JTextField();
        panelCampos.add(txtTalla);

        panelCampos.add(new JLabel("Color:"));
        txtColor = new JTextField();
        panelCampos.add(txtColor);

        panelCampos.add(new JLabel("Categoría ID:"));
        txtCategoriaId = new JTextField();
        panelCampos.add(txtCategoriaId);

        panelCampos.add(new JLabel("Marca ID:"));
        txtMarcaId = new JTextField();
        panelCampos.add(txtMarcaId);

        panelCampos.add(new JLabel("URL Imagen:"));
        txtImagenURL = new JTextField();
        panelCampos.add(txtImagenURL);

        JButton btnGuardar = new JButton("💾 Guardar");
        btnGuardar.addActionListener(e -> guardarProducto());

        add(panelCampos, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void guardarProducto() {
        try {
            Producto p = new Producto();
            p.setNombre(txtNombre.getText());
            p.setDescripcion(txtDescripcion.getText());
            p.setPrecio(Double.parseDouble(txtPrecio.getText()));
            p.setStock(Integer.parseInt(txtStock.getText()));
            p.setTalla(txtTalla.getText());
            p.setColor(txtColor.getText());
            p.setCategoriaId(Integer.parseInt(txtCategoriaId.getText()));
            p.setMarcaId(Integer.parseInt(txtMarcaId.getText()));
            p.setImagenURL(txtImagenURL.getText());

            if (productoDAO.agregarProducto(p)) {
                registrarAccion("Agregó el producto: " + p.getNombre());
                JOptionPane.showMessageDialog(this, "✅ Producto agregado correctamente.");
                ventanaAdmin.recargarProductos();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo agregar el producto.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage());
        }
    }

    private void registrarAccion(String accion) {
        try (Connection conn = new Conexion().conectar();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO logacciones (usuario_id, accion) VALUES (?, ?)")) {
            stmt.setInt(1, 1); // puedes reemplazarlo con el ID del usuario logueado
            stmt.setString(2, accion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Error al registrar acción: " + e.getMessage());
        }
    }
}
