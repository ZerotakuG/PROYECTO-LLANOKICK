package com.Llanokick.gui;

import com.Llanokick.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaSuperUser extends JFrame {
    private ProductoDAO productoDAO = new ProductoDAO();
    private LogAccionesDAO logDAO = new LogAccionesDAO();
    private Usuario usuario;

    public VentanaSuperUser(Usuario user) {
        this.usuario = user;

        setTitle("👨‍🔧 Panel SuperUser - " + user.getNombre());
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1, 10, 10));

        JButton btnEditar = new JButton("✏️ Editar Producto");
        JButton btnEliminar = new JButton("🗑️ Eliminar Producto");

        add(btnEditar);
        add(btnEliminar);

        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
    }

    private void editarProducto() {
        List<Producto> productos = productoDAO.listarProductos();
        String[] nombres = productos.stream().map(Producto::getNombre).toArray(String[]::new);

        String seleccionado = (String) JOptionPane.showInputDialog(this, "Selecciona producto:",
                "Editar Producto", JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);

        if (seleccionado != null) {
            Producto p = productoDAO.buscarPorNombre(seleccionado);
            String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre:", p.getNombre());
            if (nuevoNombre != null) {
                p.setNombre(nuevoNombre);
                productoDAO.actualizarProducto(p);
                logDAO.registrarAccion(usuario.getIdUsuario(), "Editó producto: " + nuevoNombre);
                JOptionPane.showMessageDialog(this, "✅ Producto actualizado");
            }
        }
    }

    private void eliminarProducto() {
        List<Producto> productos = productoDAO.listarProductos();
        String[] nombres = productos.stream().map(Producto::getNombre).toArray(String[]::new);

        String seleccionado = (String) JOptionPane.showInputDialog(this, "Selecciona producto a eliminar:",
                "Eliminar Producto", JOptionPane.WARNING_MESSAGE, null, nombres, nombres[0]);

        if (seleccionado != null) {
            Producto p = productoDAO.buscarPorNombre(seleccionado);
            productoDAO.eliminarProducto(p.getIdProducto());
            logDAO.registrarAccion(usuario.getIdUsuario(), "Eliminó producto: " + seleccionado);
            JOptionPane.showMessageDialog(this, "🗑️ Producto eliminado");
        }
    }
}
