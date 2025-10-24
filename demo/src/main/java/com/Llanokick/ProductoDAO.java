package com.Llanokick;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private Conexion conexion;

    public ProductoDAO() {
        conexion = new Conexion();
    }

    //  Crear (INSERT)
    public boolean agregarProducto(Producto producto) {
        String sql = "INSERT INTO producto (nombre, descripcion, precio, stock, talla, color, categoria_id, marca_id, imagen_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setString(5, producto.getTalla());
            stmt.setString(6, producto.getColor());
            stmt.setInt(7, producto.getCategoriaId());
            stmt.setInt(8, producto.getMarcaId());
            stmt.setString(9, producto.getImagenURL());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error al agregar producto: " + e.getMessage());
            return false;
        }
    }

    //  Leer (SELECT todos)
    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto";

        try (Connection conn = conexion.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setTalla(rs.getString("talla"));
                p.setColor(rs.getString("color"));
                p.setCategoriaId(rs.getInt("categoria_id"));
                p.setMarcaId(rs.getInt("marca_id"));
                p.setImagenURL(rs.getString("imagen_url"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar productos: " + e.getMessage());
        }

        return lista;
    }

    //  Leer (SELECT por ID)
    public Producto obtenerProductoPorId(int id) {
        String sql = "SELECT * FROM producto WHERE id_producto = ?";
        Producto p = null;

        try (Connection conn = conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setTalla(rs.getString("talla"));
                p.setColor(rs.getString("color"));
                p.setCategoriaId(rs.getInt("categoria_id"));
                p.setMarcaId(rs.getInt("marca_id"));
                p.setImagenURL(rs.getString("imagen_url"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al obtener producto: " + e.getMessage());
        }

        return p;
    }

    //  Actualizar (UPDATE)
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE producto SET nombre=?, descripcion=?, precio=?, stock=?, talla=?, color=?, categoria_id=?, marca_id=?, imagen_url=? WHERE id_producto=?";

        try (Connection conn = conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setString(5, producto.getTalla());
            stmt.setString(6, producto.getColor());
            stmt.setInt(7, producto.getCategoriaId());
            stmt.setInt(8, producto.getMarcaId());
            stmt.setString(9, producto.getImagenURL());
            stmt.setInt(10, producto.getIdProducto());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    //  Eliminar (DELETE)
    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection conn = conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    //  Buscar producto por nombre (para la ventana de detalle)
public Producto buscarPorNombre(String nombre) {
    Producto p = null;
    String sql = "SELECT * FROM producto WHERE nombre = ?";

    try (Connection conn = conexion.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, nombre);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            p = new Producto();
            p.setIdProducto(rs.getInt("id_producto"));
            p.setNombre(rs.getString("nombre"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPrecio(rs.getDouble("precio"));
            p.setStock(rs.getInt("stock"));
            p.setTalla(rs.getString("talla"));
            p.setColor(rs.getString("color"));
            p.setCategoriaId(rs.getInt("categoria_id"));
            p.setMarcaId(rs.getInt("marca_id"));
            p.setImagenURL(rs.getString("imagen_url"));
        }

    } catch (SQLException e) {
        System.out.println("❌ Error al buscar producto por nombre: " + e.getMessage());
    }

    return p;
}



    
}
