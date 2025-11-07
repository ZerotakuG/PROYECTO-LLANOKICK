package com.Llanokick.gui;

import com.Llanokick.*;
import com.Llanokick.vistas.VentanaAdmin;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private JTextField txtCorreo;
    private JPasswordField txtClave;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginWindow() {
        setTitle("🔐 Iniciar Sesión - Llanokick");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Bienvenido a Llanokick Store", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        panelCampos.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelCampos.add(txtCorreo);

        panelCampos.add(new JLabel("Clave:"));
        txtClave = new JPasswordField();
        panelCampos.add(txtClave);

        add(panelCampos, BorderLayout.CENTER);

        JButton btnLogin = new JButton("Ingresar");
        btnLogin.addActionListener(e -> autenticar());
        add(btnLogin, BorderLayout.SOUTH);
    }

    private void autenticar() {
    String correo = txtCorreo.getText().trim();
    String clave = new String(txtClave.getPassword()).trim();

    Usuario user = usuarioDAO.login(correo, clave);

    if (user != null) {
        JOptionPane.showMessageDialog(this,
            "✅ Bienvenido " + user.getNombre() + " (" + user.getRol() + ")");

        dispose(); // Cierra la ventana de login

        // 🔹 Dependiendo del rol, abrimos la tienda con distintos permisos
        switch (user.getRol().toLowerCase()) {
            case "admin":
                new VentanaAdmin(user).setVisible(true);
                break;
            case "superuser":
                new VentanaAdmin(user).setVisible(true);
                break;

            case "user":
            default:
                new TiendaGUI().setVisible(true);
        break;
}

    } else {
        JOptionPane.showMessageDialog(this,
            "❌ Correo o clave incorrectos.",
            "Error de autenticación",
            JOptionPane.ERROR_MESSAGE);
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }
}
