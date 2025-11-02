package ui.tablespaces;

import modulos.Tablespaces;
import javax.swing.*;
import java.awt.*;

public class VentanaAgregarDatafile extends JFrame {
    private JTextField txtTS, txtRuta, txtSize;
    private Tablespaces ts = new Tablespaces();

    public VentanaAgregarDatafile() {
        setTitle("Agregar Datafile");
        setSize(480, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Agregar Datafile a Tablespace", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setBorder(BorderFactory.createEmptyBorder(12,0,12,0));
        add(lbl, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3,2,12,12));
        panel.setBorder(BorderFactory.createEmptyBorder(18,36,18,36));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Nombre Tablespace:", JLabel.RIGHT));
        txtTS = new JTextField();
        panel.add(txtTS);

        panel.add(new JLabel("Ruta Datafile (abs):", JLabel.RIGHT));
        txtRuta = new JTextField();
        panel.add(txtRuta);

        panel.add(new JLabel("Tamaño (MB):", JLabel.RIGHT));
        txtSize = new JTextField("50");
        panel.add(txtSize);

        add(panel, BorderLayout.CENTER);

        JPanel pBot = new JPanel();
        pBot.setBackground(Color.WHITE);
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> {
            String tsName = txtTS.getText().trim();
            String ruta = txtRuta.getText().trim();
            int size;
            try {
                size = Integer.parseInt(txtSize.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Tamaño inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tsName.isEmpty() || ruta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = ts.agregarDatafile(tsName.toUpperCase(), ruta, size);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Datafile agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar datafile. Ver consola.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnReg = new JButton("Regresar");
        btnReg.addActionListener(e -> {
            dispose();
            new VentanaTablespaces().setVisible(true);
        });

        pBot.add(btnAgregar);
        pBot.add(btnReg);
        add(pBot, BorderLayout.SOUTH);
    }
}
