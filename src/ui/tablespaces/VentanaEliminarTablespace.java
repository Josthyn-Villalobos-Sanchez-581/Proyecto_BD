package ui.tablespaces;

import modulos.Tablespaces;

import javax.swing.*;
import java.awt.*;

public class VentanaEliminarTablespace extends JFrame {
    private JTextField txtNombre;
    private Tablespaces ts = new Tablespaces();

    public VentanaEliminarTablespace() {
        setTitle("Eliminar Tablespace");
        setSize(420, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Eliminar Tablespace (INCLUIDOS DATAFILES)", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setBorder(BorderFactory.createEmptyBorder(12,0,12,0));
        add(lbl, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(1,2,12,12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(18,36,18,36));

        panel.add(new JLabel("Nombre Tablespace:", JLabel.RIGHT));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        add(panel, BorderLayout.CENTER);

        JPanel pBot = new JPanel();
        pBot.setBackground(Color.WHITE);
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el nombre del tablespace.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar tablespace " + nombre + " incluyendo datafiles?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            boolean ok = ts.eliminarTablespace(nombre.toUpperCase());
            if (ok) JOptionPane.showMessageDialog(this, "Tablespace eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(this, "Error al eliminar tablespace. Revise consola.", "Error", JOptionPane.ERROR_MESSAGE);
        });

        JButton btnReg = new JButton("Regresar");
        btnReg.addActionListener(e -> {
            dispose();
            new VentanaTablespaces().setVisible(true);
        });

        pBot.add(btnEliminar);
        pBot.add(btnReg);
        add(pBot, BorderLayout.SOUTH);
    }
}
