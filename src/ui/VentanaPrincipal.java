package ui;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Panel Principal - Administración de Base de Datos Oracle XE");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 🔹 Encabezado
        JLabel lblTitulo = new JLabel("Administración y Auditoría de Base de Datos Oracle 21c XE", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(3, 73, 145));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(lblTitulo, BorderLayout.NORTH);

        // 🔹 Panel central con botones
        JPanel panelBotones = new JPanel(new GridLayout(3, 3, 25, 25));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        panelBotones.setBackground(Color.WHITE);

        // --- Botones principales ---
        JButton btnSeguridad = crearBoton("Módulo de Seguridad");
        btnSeguridad.addActionListener(e -> {
            dispose(); // 🔹 Cierra la ventana principal
            VentanaSeguridad ventana = new VentanaSeguridad();
            ventana.setVisible(true);
        });

        JButton btnAuditoria = crearBoton("Módulo de Auditoría");
        btnAuditoria.addActionListener(e -> {
            dispose(); // 🔹 Cierra la ventana principal
            VentanaAuditoria ventana = new VentanaAuditoria();
            ventana.setVisible(true);
        });

        JButton btnTuning = crearBoton("Tuning de Consultas");
        btnTuning.addActionListener(e -> {
            // TODO: Agregar funcionalidad de tuning aquí
            JOptionPane.showMessageDialog(this,
                    "Este módulo será desarrollado por el compañero encargado del Tuning de consultas.",
                    "Módulo pendiente", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnTablespaces = crearBoton("Administración de Tablespaces");
        btnTablespaces.addActionListener(e -> {
            // TODO: Agregar funcionalidad de administración de tablespaces aquí
            JOptionPane.showMessageDialog(this,
                    "Este módulo será desarrollado por el compañero encargado de Tablespaces.",
                    "Módulo pendiente", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnBackups = crearBoton("Gestión de Respaldos y Recuperación");
        btnBackups.addActionListener(e -> {
            dispose(); // cierra la ventana principal
            VentanaBackupRestore ventana = new VentanaBackupRestore();
            ventana.setVisible(true);
        });

        JButton btnPerformance = crearBoton("Performance de la Base de Datos");
        btnPerformance.addActionListener(e -> {
            // TODO: Agregar funcionalidad de performance aquí
            JOptionPane.showMessageDialog(this,
                    "Este módulo será desarrollado por el compañero encargado de Performance.",
                    "Módulo pendiente", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnAcerca = crearBoton("Acerca del Proyecto");
        btnAcerca.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    """
                    Proyecto Final - Administración y Auditoría de BD Oracle 21c XE
                    Ingeniería en Sistemas - Universidad Nacional (UNA)
                    Desarrollado por: Equipo GradEm-SIUA
                    Curso: Administración de Bases de Datos
                    Profesor: MAP. Rodolfo Sánchez Sánchez
                    """,
                    "Información del Proyecto",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnSalir = new JButton("Salir del Sistema");
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSalir.setBackground(new Color(200, 50, 50));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener(e -> System.exit(0));

        // --- Agregar botones al panel ---
        panelBotones.add(btnSeguridad);
        panelBotones.add(btnAuditoria);
        panelBotones.add(btnTuning);
        panelBotones.add(btnTablespaces);
        panelBotones.add(btnBackups);
        panelBotones.add(btnPerformance);
        panelBotones.add(btnAcerca);
        panelBotones.add(new JLabel()); // espacio vacío
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.CENTER);

        // 🔹 Pie de página
        JLabel lblFooter = new JLabel("© 2025 - Proyecto Final Oracle XE - Universidad Nacional de Costa Rica", JLabel.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblFooter.setForeground(Color.GRAY);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(lblFooter, BorderLayout.SOUTH);
    }

    // 🔧 Método reutilizable para crear botones estilizados
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setBackground(new Color(3, 73, 145));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
