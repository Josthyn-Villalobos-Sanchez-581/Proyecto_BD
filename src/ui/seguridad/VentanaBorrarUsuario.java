package ui.seguridad;

import modulos.Seguridad;
import ui.VentanaSeguridad;

import javax.swing.*;
import java.awt.*;

public class VentanaBorrarUsuario extends JFrame {

    private JTextField txtUsuario;
    private Seguridad seguridad = new Seguridad();

    public VentanaBorrarUsuario() {
        setTitle("🗑️ Eliminar Usuario - Oracle XE");
        setSize(420, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 🔹 Encabezado
        JLabel lblTitulo = new JLabel("Eliminar Usuario del Sistema", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(3, 73, 145));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 🔹 Panel central con campo de texto
        JPanel panelCampos = new JPanel(new GridLayout(1, 2, 12, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        panelCampos.setBackground(Color.WHITE);

        panelCampos.add(new JLabel("👤 Usuario:", JLabel.RIGHT));
        txtUsuario = new JTextField();
        panelCampos.add(txtUsuario);

        add(panelCampos, BorderLayout.CENTER);

        // 🔹 Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnBorrar = new JButton("🗑️ Eliminar Usuario");
        btnBorrar.setBackground(new Color(3, 73, 145));
        btnBorrar.setForeground(Color.WHITE);
        btnBorrar.setFocusPainted(false);

        JButton btnRegresar = new JButton("⬅️ Regresar");
        btnRegresar.setBackground(new Color(200, 50, 50));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);

        // Acción para borrar usuario
        btnBorrar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();

            if (usuario.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese el nombre del usuario que desea eliminar.",
                        "Campo vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que desea eliminar el usuario '" + usuario + "'?\nEsta acción no se puede deshacer.",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                seguridad.borrarUsuario(usuario);
                JOptionPane.showMessageDialog(this,
                        "✅ Usuario '" + usuario + "' eliminado correctamente (si existía en Oracle).",
                        "Operación completada",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Acción de regresar
        btnRegresar.addActionListener(e -> {
            dispose();
            new VentanaSeguridad().setVisible(true);
        });

        panelBotones.add(btnBorrar);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }
}
