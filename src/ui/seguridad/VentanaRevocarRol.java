package ui.seguridad;

import modulos.Seguridad;
import ui.VentanaSeguridad;

import javax.swing.*;
import java.awt.*;

public class VentanaRevocarRol extends JFrame {

    private JTextField txtUsuario;
    private JTextField txtRol;
    private Seguridad seguridad = new Seguridad();

    public VentanaRevocarRol() {
        setTitle("🚫 Revocar Rol a Usuario - Oracle XE");
        setSize(450, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 🔹 Encabezado
        JLabel lblTitulo = new JLabel("Revocar Rol Asignado a un Usuario", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(3, 73, 145));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 🔹 Panel central (campos)
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 12, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));
        panelCampos.setBackground(Color.WHITE);

        panelCampos.add(new JLabel("👤 Usuario:", JLabel.RIGHT));
        txtUsuario = new JTextField();
        panelCampos.add(txtUsuario);

        panelCampos.add(new JLabel("🎭 Rol:", JLabel.RIGHT));
        txtRol = new JTextField();
        panelCampos.add(txtRol);

        add(panelCampos, BorderLayout.CENTER);

        // 🔹 Panel inferior (botones)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnRevocar = new JButton("🚫 Revocar Rol");
        btnRevocar.setBackground(new Color(3, 73, 145));
        btnRevocar.setForeground(Color.WHITE);
        btnRevocar.setFocusPainted(false);

        JButton btnRegresar = new JButton("⬅️ Regresar");
        btnRegresar.setBackground(new Color(200, 50, 50));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);

        // Acción para revocar rol
        btnRevocar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String rol = txtRol.getText().trim();

            if (usuario.isEmpty() || rol.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar el nombre del usuario y el rol a revocar.",
                        "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            seguridad.revocarRol(usuario, rol);
            JOptionPane.showMessageDialog(this,
                    "✅ Rol '" + rol + "' revocado correctamente del usuario '" + usuario + "'.",
                    "Operación completada", JOptionPane.INFORMATION_MESSAGE);
        });

        // Acción de regresar
        btnRegresar.addActionListener(e -> {
            dispose(); // 🔹 Cierra la ventana principal
            VentanaSeguridad ventana = new VentanaSeguridad();
            ventana.setVisible(true);
        });

        panelBotones.add(btnRevocar);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }
}
