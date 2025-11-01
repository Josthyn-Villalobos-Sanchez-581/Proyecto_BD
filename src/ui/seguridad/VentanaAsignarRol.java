package ui.seguridad;

import modulos.Seguridad;
import ui.VentanaSeguridad;

import javax.swing.*;
import java.awt.*;

public class VentanaAsignarRol extends JFrame {

    private JTextField txtUsuario;
    private JTextField txtRol;
    private Seguridad seguridad = new Seguridad();

    public VentanaAsignarRol() {
        setTitle("🎯 Asignar Rol a Usuario - Oracle XE");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 🔹 Encabezado
        JLabel lblTitulo = new JLabel("Asignar Rol a Usuario", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(3, 73, 145));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 🔹 Panel central con campos
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 12, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        panelCampos.setBackground(Color.WHITE);

        panelCampos.add(new JLabel("👤 Usuario:", JLabel.RIGHT));
        txtUsuario = new JTextField();
        panelCampos.add(txtUsuario);

        panelCampos.add(new JLabel("🎭 Rol:", JLabel.RIGHT));
        txtRol = new JTextField();
        panelCampos.add(txtRol);

        add(panelCampos, BorderLayout.CENTER);

        // 🔹 Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnAsignar = new JButton("✅ Asignar Rol");
        btnAsignar.setBackground(new Color(3, 73, 145));
        btnAsignar.setForeground(Color.WHITE);
        btnAsignar.setFocusPainted(false);

        JButton btnRegresar = new JButton("⬅️ Regresar");
        btnRegresar.setBackground(new Color(200, 50, 50));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);

        // Acción de asignar
        btnAsignar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String rol = txtRol.getText().trim();

            if (usuario.isEmpty() || rol.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese el nombre del usuario y el rol.",
                        "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            seguridad.asignarRolAUsuario(usuario, rol);
            JOptionPane.showMessageDialog(this,
                    "✅ Rol '" + rol + "' asignado al usuario '" + usuario + "'.\n\nVerifique en Oracle SQL Developer.",
                    "Operación completada",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Acción de regresar
        btnRegresar.addActionListener(e -> {
            dispose();
            new VentanaSeguridad().setVisible(true);
        });

        panelBotones.add(btnAsignar);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }
}
