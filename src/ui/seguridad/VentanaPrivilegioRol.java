package ui.seguridad;

import modulos.Seguridad;
import ui.VentanaSeguridad;

import javax.swing.*;
import java.awt.*;

public class VentanaPrivilegioRol extends JFrame {

    private JTextField txtRol;
    private JTextField txtPrivilegio;
    private JTextField txtTabla;
    private Seguridad seguridad = new Seguridad();

    public VentanaPrivilegioRol() {
        setTitle("📦 Asignar Privilegio a Rol - Oracle XE");
        setSize(480, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 🔹 Encabezado
        JLabel lblTitulo = new JLabel("Asignación de Privilegios a un Rol", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(3, 73, 145));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 🔹 Panel central (campos)
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 12, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        panelCampos.setBackground(Color.WHITE);

        panelCampos.add(new JLabel("🎭 Rol:", JLabel.RIGHT));
        txtRol = new JTextField();
        panelCampos.add(txtRol);

        panelCampos.add(new JLabel("🧩 Privilegio (SELECT, INSERT, UPDATE, DELETE):", JLabel.RIGHT));
        txtPrivilegio = new JTextField();
        panelCampos.add(txtPrivilegio);

        panelCampos.add(new JLabel("📄 Tabla destino:", JLabel.RIGHT));
        txtTabla = new JTextField();
        panelCampos.add(txtTabla);

        add(panelCampos, BorderLayout.CENTER);

        // 🔹 Panel inferior (botones)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnAsignar = new JButton("✅ Asignar Privilegio");
        btnAsignar.setBackground(new Color(3, 73, 145));
        btnAsignar.setForeground(Color.WHITE);
        btnAsignar.setFocusPainted(false);

        JButton btnRegresar = new JButton("⬅️ Regresar");
        btnRegresar.setBackground(new Color(200, 50, 50));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);

        // Acción para asignar privilegio
        btnAsignar.addActionListener(e -> {
            String rol = txtRol.getText().trim();
            String privilegio = txtPrivilegio.getText().trim();
            String tabla = txtTabla.getText().trim();

            if (rol.isEmpty() || privilegio.isEmpty() || tabla.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar el rol, el privilegio y la tabla destino.",
                        "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            seguridad.asignarPrivilegioARol(rol, privilegio, tabla);
            JOptionPane.showMessageDialog(this,
                    "✅ Privilegio '" + privilegio + "' asignado correctamente al rol '" + rol + "' sobre la tabla '" + tabla + "'.",
                    "Operación completada", JOptionPane.INFORMATION_MESSAGE);
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
