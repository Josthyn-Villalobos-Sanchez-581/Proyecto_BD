package ui.seguridad;

import modulos.Seguridad;
import ui.VentanaSeguridad;

import javax.swing.*;
import java.awt.*;

public class VentanaCrearRol extends JFrame {

    private JTextField txtRol;
    private Seguridad seguridad = new Seguridad();

    public VentanaCrearRol() {
        setTitle("🎭 Crear Rol - Oracle XE");
        setSize(420, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 🔹 Encabezado
        JLabel lblTitulo = new JLabel("Creación de Nuevo Rol en la Base de Datos", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(3, 73, 145));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 🔹 Panel central con campo
        JPanel panelCampos = new JPanel(new GridLayout(1, 2, 12, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        panelCampos.setBackground(Color.WHITE);

        panelCampos.add(new JLabel("🎭 Nombre del Rol:", JLabel.RIGHT));
        txtRol = new JTextField();
        panelCampos.add(txtRol);

        add(panelCampos, BorderLayout.CENTER);

        // 🔹 Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnCrear = new JButton("✅ Crear Rol");
        btnCrear.setBackground(new Color(3, 73, 145));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);

        JButton btnRegresar = new JButton("⬅️ Regresar");
        btnRegresar.setBackground(new Color(200, 50, 50));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);

        // Acción para crear rol
        btnCrear.addActionListener(e -> {
            String rol = txtRol.getText().trim();

            if (rol.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese un nombre para el rol.",
                        "Campo vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            seguridad.crearRol(rol);
            JOptionPane.showMessageDialog(this,
                    "✅ Rol '" + rol + "' creado correctamente (verifique en Oracle SQL Developer).",
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
