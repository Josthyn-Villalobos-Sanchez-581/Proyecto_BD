package ui;

import ui.seguridad.*;
import javax.swing.*;
import java.awt.*;

public class VentanaSeguridad extends JFrame {

    public VentanaSeguridad() {
        setTitle("Módulo de Seguridad - Oracle XE");
        setSize(700, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 🔹 Encabezado
        JLabel lblTitulo = new JLabel("Administración de Seguridad - Oracle XE", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        lblTitulo.setForeground(new Color(3, 73, 145));
        add(lblTitulo, BorderLayout.NORTH);

        // 🔹 Panel principal con botones
        JPanel panelBotones = new JPanel(new GridLayout(4, 2, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        panelBotones.setBackground(Color.WHITE);

        // --- Botones de funcionalidades ---
        JButton btnCrearUsuario = crearBoton("Crear Usuario");
        btnCrearUsuario.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaCrearUsuario().setVisible(true));
        });

        JButton btnBorrarUsuario = crearBoton("Borrar Usuario");
        btnBorrarUsuario.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaBorrarUsuario().setVisible(true));
        });

        JButton btnCrearRol = crearBoton("Crear Rol");
        btnCrearRol.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaCrearRol().setVisible(true));
        });

        JButton btnAsignarRol = crearBoton("Asignar Rol");
        btnAsignarRol.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaAsignarRol().setVisible(true));
        });

        JButton btnRevocarRol = crearBoton("Revocar Rol");
        btnRevocarRol.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaRevocarRol().setVisible(true));
        });

        JButton btnPrivilegioUsuario = crearBoton("Privilegio a Usuario");
        btnPrivilegioUsuario.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaPrivilegioUsuario().setVisible(true));
        });

        JButton btnPrivilegioRol = crearBoton("Privilegio a Rol");
        btnPrivilegioRol.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaPrivilegioRol().setVisible(true));
        });

        JButton btnRevocarPrivilegio = crearBoton("Revocar Privilegio");
        btnRevocarPrivilegio.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaRevocarPrivilegio().setVisible(true));
        });

        // --- Botón regresar ---
        JButton btnRegresar = new JButton("Regresar al Menú Principal");
        btnRegresar.setBackground(new Color(200, 50, 50));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegresar.setFocusPainted(false);
        btnRegresar.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
        });

        // --- Agregar botones al panel ---
        panelBotones.add(btnCrearUsuario);
        panelBotones.add(btnBorrarUsuario);
        panelBotones.add(btnCrearRol);
        panelBotones.add(btnAsignarRol);
        panelBotones.add(btnRevocarRol);
        panelBotones.add(btnPrivilegioUsuario);
        panelBotones.add(btnPrivilegioRol);
        panelBotones.add(btnRevocarPrivilegio);

        // --- Agregar paneles al layout ---
        add(panelBotones, BorderLayout.CENTER);
        add(btnRegresar, BorderLayout.SOUTH);
    }

    // 🔧 Método auxiliar para crear botones uniformes
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(new Color(3, 73, 145));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return boton;
    }
}
