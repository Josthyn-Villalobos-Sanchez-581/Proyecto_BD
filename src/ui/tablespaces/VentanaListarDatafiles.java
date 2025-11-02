package ui.tablespaces;

import modulos.Tablespaces;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaListarDatafiles extends JFrame {
    private Tablespaces ts = new Tablespaces();

    public VentanaListarDatafiles() {
        setTitle("Listar Datafiles");
        setSize(800, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Datafiles (DBA_DATA_FILES)", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setBorder(BorderFactory.createEmptyBorder(12,0,12,0));
        add(lbl, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(sp, BorderLayout.CENTER);

        List<String> lista = ts.listarDataFiles();
        if (lista.isEmpty()) {
            area.setText("No se encontraron datafiles o error al consultar. Revise la consola.");
        } else {
            StringBuilder sb = new StringBuilder();
            lista.forEach(l -> sb.append(l).append("\n"));
            area.setText(sb.toString());
        }

        JPanel pBot = new JPanel();
        pBot.setBackground(Color.WHITE);
        JButton btnReg = new JButton("Regresar");
        btnReg.addActionListener(e -> {
            dispose();
            new VentanaTablespaces().setVisible(true);
        });
        pBot.add(btnReg);
        add(pBot, BorderLayout.SOUTH);
    }
}
