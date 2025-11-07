package com.Llanokick;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.Llanokick.vistas.VentanaCarrito;

public class TiendaGUI extends JFrame {
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private ProductoDAO productoDAO;
    private List<Producto> productos; // productos cargados
    private List<Producto> carrito = new ArrayList<>();
    

    // 🔹 Datos del usuario y permisos
    private Usuario usuario;
    private boolean puedeAgregar;
    private boolean puedeEditar;
    private boolean puedeEliminar;
    private boolean puedeVerCambios;

    // 🔸 Constructor con roles
    public TiendaGUI(Usuario usuario, boolean puedeAgregar, boolean puedeEditar, boolean puedeEliminar, boolean puedeVerCambios) {
        this.usuario = usuario;
        this.puedeAgregar = puedeAgregar;
        this.puedeEditar = puedeEditar;
        this.puedeEliminar = puedeEliminar;
        this.puedeVerCambios = puedeVerCambios;
        inicializarVentana();
    }

    // 🔸 Constructor sin rol (modo libre o pruebas)
    public TiendaGUI() {
        this(null, false, false, false, false);
    }

    private void inicializarVentana() {
        productoDAO = new ProductoDAO();
        setTitle("🛍️ Llanokicks Store " + 
            (usuario != null ? "- " + usuario.getRol().toUpperCase() : ""));
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🟢 Modelo de tabla
        modeloTabla = new DefaultTableModel(
                new String[]{"Imagen", "Nombre", "Descripción", "Precio", "Stock", "Talla", "Color"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : Object.class;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(120);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        // 🟣 Botones base
        JButton btnCargar = new JButton("🔄 Cargar Productos");
        btnCargar.addActionListener(e -> cargarProductos());

        JButton btnCarrito = new JButton("🛒 Carrito");
        btnCarrito.addActionListener(e -> {
            if (carrito.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tu carrito está vacío 😅");
            } else {
                new VentanaCarrito(carrito).setVisible(true);
            }
        });

        // 🟡 ComboBox para ordenar
        String[] opcionesOrden = {
            "Ordenar por...",
            "Precio Ascendente",
            "Precio Descendente",
            "Talla",
            "Categoría"
        };
        JComboBox<String> comboOrden = new JComboBox<>(opcionesOrden);
        comboOrden.setFont(new Font("Arial", Font.PLAIN, 14));

        comboOrden.addActionListener(e -> {
            if (productos == null || productos.isEmpty()) return;
            String seleccion = (String) comboOrden.getSelectedItem();

            switch (seleccion) {
                case "Precio Ascendente" ->
                    productos.sort(Comparator.comparingDouble(Producto::getPrecio));
                case "Precio Descendente" ->
                    productos.sort(Comparator.comparingDouble(Producto::getPrecio).reversed());
                case "Talla" ->
                    productos.sort(Comparator.comparing(Producto::getTalla));
                case "Categoría" ->
                    productos.sort(Comparator.comparing(Producto::getCategoria));
            }
            actualizarTabla();
        });

        // 🔵 Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.add(btnCargar);
        panelSuperior.add(btnCarrito);
        panelSuperior.add(comboOrden);

        // 🔸 Panel inferior (según rol)
        JPanel panelInferior = new JPanel();

        if (puedeAgregar) {
            JButton btnAgregar = new JButton("➕ Agregar Producto");
            btnAgregar.addActionListener(e -> agregarProducto());
            panelInferior.add(btnAgregar);
        }

        if (puedeEditar) {
            JButton btnEditar = new JButton("✏️ Editar Producto");
            btnEditar.addActionListener(e -> editarProducto());
            panelInferior.add(btnEditar);
        }

        if (puedeEliminar) {
            JButton btnEliminar = new JButton("❌ Eliminar Producto");
            btnEliminar.addActionListener(e -> eliminarProducto());
            panelInferior.add(btnEliminar);
        }

        if (puedeVerCambios) {
            JButton btnCambios = new JButton("📜 Ver Cambios");
            btnCambios.addActionListener(e -> verHistorialCambios());
            panelInferior.add(btnCambios);
        }

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // 🟣 Doble clic para detalle
        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablaProductos.getSelectedRow();
                    if (fila != -1) {
                        String nombre = (String) tablaProductos.getValueAt(fila, 1);
                        Producto producto = productoDAO.buscarPorNombre(nombre);
                        if (producto != null) {
                            new VentanaDetalleProducto(producto, carrito);
                        }
                    }
                }
            }
        });
    }

    // 🔹 Cargar productos
    private void cargarProductos() {
        productos = productoDAO.listarProductos();
        actualizarTabla();
    }

    // 🔹 Actualizar tabla
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            ImageIcon icono = cargarImagen(p.getImagenURL());
            modeloTabla.addRow(new Object[]{
                    icono,
                    p.getNombre(),
                    p.getDescripcion(),
                    "$" + p.getPrecio(),
                    p.getStock(),
                    p.getTalla(),
                    p.getColor()
            });
        }
    }

    // 🔹 Cargar imagen
    private ImageIcon cargarImagen(String rutaImagen) {
        try {
            Image imagen = rutaImagen.startsWith("http")
                    ? new ImageIcon(new URL(rutaImagen)).getImage()
                    : new ImageIcon(rutaImagen).getImage();

            Image miniatura = imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(miniatura);
        } catch (Exception e) {
            BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, 100, 100);
            g.setColor(Color.BLACK);
            g.drawString("No Img", 25, 55);
            g.dispose();
            return new ImageIcon(img);
        }
    }

    // 🔹 Métodos de acciones (se llenarán después)
    private void agregarProducto() {
        JOptionPane.showMessageDialog(this, "🧩 Función agregar producto (pendiente)");
    }

    private void editarProducto() {
        JOptionPane.showMessageDialog(this, "🧩 Función editar producto (pendiente)");
    }

    private void eliminarProducto() {
        JOptionPane.showMessageDialog(this, "🧩 Función eliminar producto (pendiente)");
    }

    private void verHistorialCambios() {
        JOptionPane.showMessageDialog(this, "🧩 Función ver historial de cambios (pendiente)");
    }

    // 🔹 Ejecutar app en modo libre
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TiendaGUI().setVisible(true));
    }
}
