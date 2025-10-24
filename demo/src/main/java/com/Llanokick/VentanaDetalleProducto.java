package com.Llanokick;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

public class VentanaDetalleProducto extends JFrame {

    private Producto producto;
    private List<Producto> carrito;

    public VentanaDetalleProducto(Producto producto, List<Producto> carrito) {
        this.producto = producto;
        this.carrito = carrito;

        setTitle("🩴 Detalle del Producto");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🖼️ Imagen del producto
        JLabel lblImagen = new JLabel(cargarImagen(producto.getImagenURL()));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblImagen, BorderLayout.NORTH);

        // 📋 Panel de detalles
        JPanel panelDetalles = new JPanel(new GridLayout(0, 1));
        panelDetalles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelDetalles.add(new JLabel("🧢 Nombre: " + producto.getNombre()));
        panelDetalles.add(new JLabel("📝 Descripción: " + producto.getDescripcion()));
        panelDetalles.add(new JLabel("💲 Precio: $" + producto.getPrecio()));
        panelDetalles.add(new JLabel("📦 Stock: " + producto.getStock()));
        panelDetalles.add(new JLabel("👟 Talla: " + producto.getTalla()));
        panelDetalles.add(new JLabel("🎨 Color: " + producto.getColor()));

        add(panelDetalles, BorderLayout.CENTER);

        // 🛒 Botón para agregar al carrito
        JButton btnAgregar = new JButton("🛒 Agregar al carrito");
        btnAgregar.setBackground(new Color(50, 205, 50));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.addActionListener(e -> {
            carrito.add(producto);
            JOptionPane.showMessageDialog(this,
                    producto.getNombre() + " fue agregado al carrito 🛍️",
                    "Producto agregado",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnAgregar);
        add(panelBoton, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 🖼️ Método para cargar imagen desde ruta local o URL
    private ImageIcon cargarImagen(String rutaImagen) {
        try {
            Image imagen;

            if (rutaImagen.startsWith("http")) {
                imagen = new ImageIcon(new URL(rutaImagen)).getImage();
            } else {
                imagen = new ImageIcon(rutaImagen).getImage();
            }

            Image redimensionada = imagen.getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            return new ImageIcon(redimensionada);

        } catch (Exception e) {
            System.out.println("⚠️ No se pudo cargar la imagen: " + rutaImagen);
            BufferedImage imgDefecto = new BufferedImage(350, 350, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = imgDefecto.createGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, 350, 350);
            g.setColor(Color.BLACK);
            g.drawString("Sin imagen", 130, 175);
            g.dispose();
            return new ImageIcon(imgDefecto);
        }
    }
}
