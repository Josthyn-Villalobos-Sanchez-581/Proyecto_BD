package ui.tablespaces;

import modulos.Tablespaces;
import javax.swing.*;
import java.awt.*;

public class VentanaCrearTablespace extends JFrame {
    private JTextField txtNombre, txtRuta, txtSize;
    private JCheckBox chkTemp;
    private Tablespaces ts = new Tablespaces();

    public VentanaCrearTablespace() {
        setTitle("Crear Tablespace");
        setSize(480, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Crear nuevo Tablespace", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        add(lbl, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(4, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 36, 18, 36));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Nombre Tablespace:", JLabel.RIGHT));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Ruta Datafile (abs):", JLabel.RIGHT));
        txtRuta = new JTextField();
        panel.add(txtRuta);

        panel.add(new JLabel("Tamaño (MB):", JLabel.RIGHT));
        txtSize = new JTextField("50");
        panel.add(txtSize);

        panel.add(new JLabel("Tipo:", JLabel.RIGHT));
        chkTemp = new JCheckBox("¿Temporal (TEMPORARY)?");
        chkTemp.setBackground(Color.WHITE);
        panel.add(chkTemp);

        add(panel, BorderLayout.CENTER);

        JPanel pBot = new JPanel();
        pBot.setBackground(Color.WHITE);

        JButton btnCrear = new JButton("Crear");
        btnCrear.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String ruta = txtRuta.getText().trim();
            int size;
            try {
                size = Integer.parseInt(txtSize.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Tamaño inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nombre.isEmpty() || ruta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (ts.existeTablespace(nombre)) {
                JOptionPane.showMessageDialog(this, "El tablespace ya existe.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = ts.crearTablespace(nombre.toUpperCase(), ruta, size, chkTemp.isSelected());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Tablespace creado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear tablespace. Ver consola.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnReg = new JButton("Regresar");
        btnReg.addActionListener(e -> {
            dispose();
            new VentanaTablespaces().setVisible(true);
        });

        pBot.add(btnCrear);
        pBot.add(btnReg);
        add(pBot, BorderLayout.SOUTH);
    }
}
