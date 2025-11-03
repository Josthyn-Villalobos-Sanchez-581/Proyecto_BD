package ui.tablespaces;

import modulos.Tablespaces;
import ui.tablespaces.VentanaTablespaces;

import javax.swing.*;
import java.awt.*;

public class VentanaCrearTemporaryTablespace extends JFrame {

    private JTextField txtNombre;
    private JTextField txtRuta;
    private JTextField txtTamano;
    private Tablespaces ts = new Tablespaces();

    public VentanaCrearTemporaryTablespace() {
        setTitle("Crear Temporary Tablespace - Oracle XE");
        setSize(520, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Crear Temporary Tablespace en Oracle 21c XE", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(3, 73, 145));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 12, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        panelCampos.setBackground(Color.WHITE);

        panelCampos.add(new JLabel("Nombre TS Temp:", JLabel.RIGHT));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Ruta TEMPFILE (.dbf):", JLabel.RIGHT));
        txtRuta = new JTextField();
        panelCampos.add(txtRuta);

        panelCampos.add(new JLabel("Tamaño (MB):", JLabel.RIGHT));
        txtTamano = new JTextField();
        panelCampos.add(txtTamano);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnCrear = new JButton("✅ Crear Temp TS");
        btnCrear.setBackground(new Color(3, 73, 145));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);

        JButton btnRegresar = new JButton("⬅ Regresar");
        btnRegresar.setBackground(new Color(200, 50, 50));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);

        btnCrear.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String ruta = txtRuta.getText().trim();
            String tam = txtTamano.getText().trim();

            if (nombre.isEmpty() || ruta.isEmpty() || tam.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe llenar todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = ts.crearTemporaryTablespace(nombre, ruta, Integer.parseInt(tam));
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Temporary Tablespace creado correctamente.", "Operación completada", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnRegresar.addActionListener(e -> {
            dispose();
            new VentanaTablespaces().setVisible(true);
        });

        panelBotones.add(btnCrear);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }
}
