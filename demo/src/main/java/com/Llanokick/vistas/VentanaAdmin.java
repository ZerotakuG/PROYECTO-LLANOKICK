package com.Llanokick.vistas;

import com.Llanokick.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.Comparator;

public class VentanaAdmin extends JFrame {

    private Usuario usuario; // ✅ Usuario logueado

    // ✅ Botones visibles o no según el rol
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnHistorial;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private ProductoDAO productoDAO;
    private List<Producto> productos;

    // ✅ CONSTRUCTOR RECIBE USUARIO
    public VentanaAdmin(Usuario usuario) {
        this.usuario = usuario;
        inicializarVentana();
        aplicarPermisos(); // ✅ Se ajustan permisos según rol
    }

    // ✅ Construye toda la interfaz
    private void inicializarVentana() {

        productoDAO = new ProductoDAO();
        setTitle("🛒 Panel Administración - " + usuario.getNombre());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ✅ Modelo tabla
        modeloTabla = new DefaultTableModel(
                new String[]{"Imagen", "Nombre", "Descripción", "Precio", "Stock", "Talla", "Color"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int col) { return col == 0 ? ImageIcon.class : Object.class; }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(120);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        // 🔺 Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCargar = new JButton("🔄 Cargar Productos");
        btnCargar.addActionListener(e -> cargarProductos());
        panelSuperior.add(btnCargar);

        // ✅ Ordenamiento
        String[] opcionesOrden = {"Ordenar por...", "Precio Ascendente", "Precio Descendente", "Talla", "Stock"};
        JComboBox<String> comboOrden = new JComboBox<>(opcionesOrden);
        comboOrden.addActionListener(e -> ordenarProductos((String) comboOrden.getSelectedItem()));
        panelSuperior.add(comboOrden);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // ✅ Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));

        btnAgregar = new JButton("➕ Agregar");
        btnEditar = new JButton("✏️ Editar");
        btnEliminar = new JButton("🗑️ Eliminar");
        btnHistorial = new JButton("📜 Historial");

        // ➕ Agregar
        btnAgregar.addActionListener(e -> {
            new VentanaAgregarProducto(this).setVisible(true);
        });

        // ✏️ Editar
        btnEditar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para editar.");
                return;
            }
            Producto p = productos.get(fila);
            new VentanaEditarProducto(this, p).setVisible(true);
        });

        // 🗑️ Eliminar
        btnEliminar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
                return;
            }

            Producto p = productos.get(fila);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Eliminar \"" + p.getNombre() + "\"?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (productoDAO.eliminarProducto(p.getIdProducto())) {
                    JOptionPane.showMessageDialog(this, "✅ Eliminado.");
                    cargarProductos();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al eliminar.");
                }
            }
        });

        // 📜 Historial
        btnHistorial.addActionListener(e -> new VentanaHistorialAcciones().setVisible(true));

        // ✅ Agregar botones al panel
        panelInferior.add(btnAgregar);
        panelInferior.add(btnEditar);
        panelInferior.add(btnEliminar);
        panelInferior.add(btnHistorial);

        add(panelInferior, BorderLayout.SOUTH);

        cargarProductos();
    }

    // ✅ APLICAR PERMISOS SEGÚN ROL
    private void aplicarPermisos() {

        String rol = usuario.getRol().toLowerCase();

        if (rol.equals("admin")) {
            // ✅ Admin puede todo
            btnAgregar.setVisible(true);
            btnEditar.setVisible(true);
            btnEliminar.setVisible(true);
            btnHistorial.setVisible(true);
        }

        if (rol.equals("superuser")) {
            // ✅ SuperUser: solo agregar y editar
            btnAgregar.setVisible(true);
            btnEditar.setVisible(true);
            btnEliminar.setVisible(false);   // ❌ NO PUEDE
            btnHistorial.setVisible(false);  // ❌ NO PUEDE
        }

        if (rol.equals("user")) {
            // ✅ Cliente: no puede nada
            btnAgregar.setVisible(false);
            btnEditar.setVisible(false);
            btnEliminar.setVisible(false);
            btnHistorial.setVisible(false);
        }
    }

    // ✅ Cargar productos
    private void cargarProductos() {
        productos = productoDAO.listarProductos();
        actualizarTabla();
    }

    // ✅ Actualizar tabla
    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                    cargarImagen(p.getImagenURL()),
                    p.getNombre(),
                    p.getDescripcion(),
                    "$" + p.getPrecio(),
                    p.getStock(),
                    p.getTalla(),
                    p.getColor()
            });
        }
    }

    // ✅ Ordenar
    private void ordenarProductos(String criterio) {
        if (productos == null || productos.isEmpty()) return;

        switch (criterio) {
            case "Precio Ascendente" -> productos.sort(Comparator.comparingDouble(Producto::getPrecio));
            case "Precio Descendente" -> productos.sort(Comparator.comparingDouble(Producto::getPrecio).reversed());
            case "Talla" -> productos.sort(Comparator.comparing(Producto::getTalla));
            case "Stock" -> productos.sort(Comparator.comparingInt(Producto::getStock).reversed());
        }
        actualizarTabla();
    }

    // ✅ Imagen
    private ImageIcon cargarImagen(String ruta) {
        try {
            Image img = ruta.startsWith("http")
                    ? new ImageIcon(new URL(ruta)).getImage()
                    : new ImageIcon(ruta).getImage();

            img = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(img);

        } catch (Exception e) {
            BufferedImage b = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = b.createGraphics();
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, 100, 100);
            g.setColor(Color.BLACK);
            g.drawString("No Img", 25, 55);
            g.dispose();
            return new ImageIcon(b);
        }
    }

    public void recargarProductos() {
        cargarProductos();
    }
}
