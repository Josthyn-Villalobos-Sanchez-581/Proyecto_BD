package ui.seguridad;

import modulos.Seguridad;
import ui.VentanaSeguridad;

import javax.swing.*;
import java.awt.*;

public class VentanaCrearUsuario extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private Seguridad seguridad = new Seguridad();

    public VentanaCrearUsuario() {
        setTitle("👤 Crear Usuario - Oracle XE");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 🔹 Encabezado
        JLabel lblTitulo = new JLabel("Creación de Nuevo Usuario en Oracle", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(3, 73, 145));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 🔹 Panel central con campos
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 12, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        panelCampos.setBackground(Color.WHITE);

        panelCampos.add(new JLabel("👤 Nombre de Usuario:", JLabel.RIGHT));
        txtUsuario = new JTextField();
        panelCampos.add(txtUsuario);

        panelCampos.add(new JLabel("🔑 Contraseña:", JLabel.RIGHT));
        txtContrasena = new JPasswordField();
        panelCampos.add(txtContrasena);

        add(panelCampos, BorderLayout.CENTER);

        // 🔹 Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnCrear = new JButton("✅ Crear Usuario");
        btnCrear.setBackground(new Color(3, 73, 145));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);

        JButton btnRegresar = new JButton("⬅️ Regresar");
        btnRegresar.setBackground(new Color(200, 50, 50));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);

        // Acción para crear usuario
        btnCrear.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String contrasena = new String(txtContrasena.getPassword()).trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar tanto el nombre de usuario como la contraseña.",
                        "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            seguridad.crearUsuario(usuario, contrasena);
            JOptionPane.showMessageDialog(this,
                    "✅ Usuario '" + usuario + "' creado correctamente (verifique en Oracle SQL Developer).",
                    "Operación completada",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Acción de regresar
        btnRegresar.addActionListener(e -> {
            dispose();
            new VentanaSeguridad().setVisible(true);
        });

        panelBotones.add(btnCrear);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }
}
