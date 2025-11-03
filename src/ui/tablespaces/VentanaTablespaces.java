package ui.tablespaces;

import javax.swing.*;
import java.awt.*;

import ui.VentanaPrincipal;

public class VentanaTablespaces extends JFrame {

    public VentanaTablespaces() {
        setTitle("Administración de Tablespaces - Oracle XE");
        setSize(760, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Administración de Tablespaces", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        lblTitulo.setForeground(new Color(3, 73, 145));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 18, 18));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        panelBotones.setBackground(Color.WHITE);

        JButton btnCrear = crearBoton("Crear Tablespace");
        btnCrear.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaCrearTablespace().setVisible(true));
        });

        JButton btnCrearTemp = crearBoton("Crear Temporary TS");
        btnCrearTemp.addActionListener(e -> {
            dispose();
            new VentanaCrearTemporaryTablespace().setVisible(true);
        });

        JButton btnAgregarDF = crearBoton("Agregar Datafile");
        btnAgregarDF.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaAgregarDatafile().setVisible(true));
        });

        JButton btnListar = crearBoton("Listar Tablespaces");
        btnListar.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaListarTablespaces().setVisible(true));
        });

        JButton btnListarDF = crearBoton("Listar Datafiles");
        btnListarDF.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaListarDatafiles().setVisible(true));
        });

        JButton btnRedimensionar = crearBoton("Redimensionar Datafile");
        btnRedimensionar.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaRedimensionarDatafile().setVisible(true));
        });

        JButton btnEliminar = crearBoton("Eliminar Tablespace");
        btnEliminar.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaEliminarTablespace().setVisible(true));
        });

        panelBotones.add(btnCrear);
        panelBotones.add(btnAgregarDF);
        panelBotones.add(btnListar);
        panelBotones.add(btnListarDF);
        panelBotones.add(btnRedimensionar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.CENTER);

        JButton btnRegresar = new JButton("Regresar al Menú Principal");
        btnRegresar.setBackground(new Color(200, 50, 50));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegresar.setFocusPainted(false);
        btnRegresar.addActionListener(e -> {
            dispose();
            new VentanaPrincipal().setVisible(true);
        });
        add(btnRegresar, BorderLayout.SOUTH);
    }

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
