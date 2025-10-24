package com.Llanokick;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        // Conectarse a la base de datos
        Conexion conexion = new Conexion();
        Connection connection = conexion.conectar();

        if (connection != null) {
            System.out.println("🚀 Conexión exitosa!");
        } else {
            System.out.println("⚠️ Fallo en la conexión.");
            return; // si no hay conexión, salimos
        }

        //  Crear instancia del DAO
        ProductoDAO dao = new ProductoDAO();

        //  Crear un producto de prueba
        Producto nuevo = new Producto(
            0, // id_producto (0 porque es autoincremental)
            "Camiseta Llanokick",
            "Camiseta deportiva edición limitada",
            89999,
            15,
            "M",
            "Negro",
            1, // categoria_id
            2, // marca_id
            "https://img.com/camiseta.png"
        );

        // Insertar producto
        if (dao.agregarProducto(nuevo)) {
            System.out.println("✅ Producto agregado correctamente");
        }

        // Listar productos
        System.out.println("\n📦 Lista de productos:");
        for (Producto p : dao.listarProductos()) {
            System.out.println(p);
        }
    }
}
