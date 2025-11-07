package com.Llanokick.vistas;

import com.Llanokick.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VentanaEditarProducto extends JFrame {
    private JTextField txtNombre, txtPrecio, txtStock, txtTalla, txtColor, txtImagenURL, txtCategoriaId, txtMarcaId;
    private JTextArea txtDescripcion;
    private Producto producto;
    private ProductoDAO productoDAO = new ProductoDAO();
    private VentanaAdmin ventanaAdmin;

    public VentanaEditarProducto(VentanaAdmin ventanaAdmin, Producto producto) {
        this.ventanaAdmin = ventanaAdmin;
        this.producto = producto;

        setTitle("✏️ Editar Producto");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridLayout(9, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNombre = new JTextField(producto.getNombre());
        txtDescripcion = new JTextArea(producto.getDescripcion(), 2, 10);
        txtPrecio = new JTextField(String.valueOf(producto.getPrecio()));
        txtStock = new JTextField(String.valueOf(producto.getStock()));
        txtTalla = new JTextField(producto.getTalla());
        txtColor = new JTextField(producto.getColor());
        txtCategoriaId = new JTextField(String.valueOf(producto.getCategoriaId()));
        txtMarcaId = new JTextField(String.valueOf(producto.getMarcaId()));
        txtImagenURL = new JTextField(producto.getImagenURL());

        panelCampos.add(new JLabel("Nombre:")); panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Descripción:")); panelCampos.add(new JScrollPane(txtDescripcion));
        panelCampos.add(new JLabel("Precio:")); panelCampos.add(txtPrecio);
        panelCampos.add(new JLabel("Stock:")); panelCampos.add(txtStock);
        panelCampos.add(new JLabel("Talla:")); panelCampos.add(txtTalla);
        panelCampos.add(new JLabel("Color:")); panelCampos.add(txtColor);
        panelCampos.add(new JLabel("Categoría ID:")); panelCampos.add(txtCategoriaId);
        panelCampos.add(new JLabel("Marca ID:")); panelCampos.add(txtMarcaId);
        panelCampos.add(new JLabel("URL Imagen:")); panelCampos.add(txtImagenURL);

        JButton btnGuardar = new JButton("💾 Guardar Cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        add(panelCampos, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void guardarCambios() {
        try {
            producto.setNombre(txtNombre.getText());
            producto.setDescripcion(txtDescripcion.getText());
            producto.setPrecio(Double.parseDouble(txtPrecio.getText()));
            producto.setStock(Integer.parseInt(txtStock.getText()));
            producto.setTalla(txtTalla.getText());
            producto.setColor(txtColor.getText());
            producto.setCategoriaId(Integer.parseInt(txtCategoriaId.getText()));
            producto.setMarcaId(Integer.parseInt(txtMarcaId.getText()));
            producto.setImagenURL(txtImagenURL.getText());

            if (productoDAO.actualizarProducto(producto)) {
                registrarAccion("Editó el producto: " + producto.getNombre());
                JOptionPane.showMessageDialog(this, "✅ Producto actualizado correctamente.");
                ventanaAdmin.recargarProductos();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo actualizar el producto.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage());
        }
    }

    private void registrarAccion(String accion) {
        try (Connection conn = new Conexion().conectar();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO logacciones (usuario_id, accion) VALUES (?, ?)")) {
            stmt.setInt(1, 1); // reemplazar por el ID real del usuario
            stmt.setString(2, accion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Error al registrar acción: " + e.getMessage());
        }
    }
}
