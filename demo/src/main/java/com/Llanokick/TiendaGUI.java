package com.Llanokick;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.Llanokick.vistas.VentanaCarrito;


public class TiendaGUI extends JFrame {
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private ProductoDAO productoDAO;

    // 🛒 Lista global del carrito
    private List<Producto> carrito = new ArrayList<>();

    public TiendaGUI() {
        productoDAO = new ProductoDAO();
        setTitle("🛍️ Llanokicks Store");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🟢 Crear modelo de tabla
        modeloTabla = new DefaultTableModel(
                new String[]{"Imagen", "Nombre", "Descripción", "Precio", "Stock", "Talla", "Color"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return ImageIcon.class;
                return Object.class;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(120);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        // 🟣 Botón para cargar productos
        JButton btnCargar = new JButton("🔄 Cargar Productos");
        btnCargar.addActionListener(e -> cargarProductos());

        // 🛒 Botón para ver carrito
        JButton btnCarrito = new JButton("🛒 Mirar Carrito");
        btnCarrito.addActionListener(e -> {
            if (carrito.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tu carrito está vacío 😅");
            } else {
                VentanaCarrito ventanaCarrito = new VentanaCarrito(carrito);
                ventanaCarrito.setVisible(true);
            }
        });

        // Panel inferior con ambos botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCargar);
        panelBotones.add(btnCarrito);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // 🟡 Detectar doble clic sobre un producto
        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Doble clic
                    int fila = tablaProductos.getSelectedRow();
                    if (fila != -1) {
                        String nombre = (String) tablaProductos.getValueAt(fila, 1);
                        Producto producto = productoDAO.buscarPorNombre(nombre);
                        if (producto != null) {
                            // 🔹 Pasamos el carrito a la ventana de detalle
                            new VentanaDetalleProducto(producto, carrito);
                        }
                    }
                }
            }
        });
    }

    // 🔹 Cargar productos desde la base de datos
    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoDAO.listarProductos();

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

    // 🔹 Cargar la imagen (desde URL o ruta local)
    private ImageIcon cargarImagen(String rutaImagen) {
        try {
            Image imagen;
            if (rutaImagen.startsWith("http")) {
                imagen = new ImageIcon(new URL(rutaImagen)).getImage();
            } else {
                imagen = new ImageIcon(rutaImagen).getImage();
            }
            Image miniatura = imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(miniatura);
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo cargar la imagen: " + rutaImagen);
            BufferedImage imagenDefecto = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = imagenDefecto.createGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, 100, 100);
            g.setColor(Color.BLACK);
            g.drawString("No Img", 25, 55);
            g.dispose();
            return new ImageIcon(imagenDefecto);
        }
    }

    // 🔹 Ejecutar la app
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TiendaGUI tienda = new TiendaGUI();
            tienda.setVisible(true);
        });
    }
}
