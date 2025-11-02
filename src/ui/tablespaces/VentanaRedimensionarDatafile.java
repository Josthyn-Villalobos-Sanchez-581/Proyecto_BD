package ui.tablespaces;

import modulos.Tablespaces;
import javax.swing.*;
import java.awt.*;

public class VentanaRedimensionarDatafile extends JFrame {
    private JTextField txtFile, txtSize;
    private Tablespaces ts = new Tablespaces();

    public VentanaRedimensionarDatafile() {
        setTitle("Redimensionar Datafile");
        setSize(560, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Redimensionar Datafile (ruta completa)", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setBorder(BorderFactory.createEmptyBorder(12,0,12,0));
        add(lbl, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(2,2,12,12));
        panel.setBorder(BorderFactory.createEmptyBorder(18,36,18,36));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Ruta Datafile (abs):", JLabel.RIGHT));
        txtFile = new JTextField();
        panel.add(txtFile);

        panel.add(new JLabel("Nuevo tamaño (MB):", JLabel.RIGHT));
        txtSize = new JTextField();
        panel.add(txtSize);

        add(panel, BorderLayout.CENTER);

        JPanel pBot = new JPanel();
        JButton btnOk = new JButton("Redimensionar");
        btnOk.addActionListener(e -> {
            String ruta = txtFile.getText().trim();
            int size;
            try {
                size = Integer.parseInt(txtSize.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Tamaño inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean ok = ts.redimensionarDatafile(ruta, size);
            if (ok) JOptionPane.showMessageDialog(this, "Redimensionado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(this, "Error al redimensionar. Ver consola.", "Error", JOptionPane.ERROR_MESSAGE);
        });

        JButton btnReg = new JButton("Regresar");
        btnReg.addActionListener(e -> {
            dispose();
            new VentanaTablespaces().setVisible(true);
        });

        pBot.add(btnOk);
        pBot.add(btnReg);
        pBot.setBackground(Color.WHITE);
        add(pBot, BorderLayout.SOUTH);
    }
}
