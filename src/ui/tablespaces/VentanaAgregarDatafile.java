package ui.tablespaces;

import modulos.Tablespaces;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ui.VentanaPrincipal;

public class VentanaAgregarDatafile extends JFrame {
    private JTextField txtTS, txtRuta, txtSize;
    private final Tablespaces ts = new Tablespaces();

    public VentanaAgregarDatafile() {
        setTitle("📂 Agregar Datafile - Oracle XE");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // === FONDO ANIMADO ===
        FondoAnimado fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // === ENCABEZADO ===
        JLabel lblTitulo = new JLabel("Agregar Datafile a un Tablespace", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));
        lblTitulo.setForeground(new Color(0, 220, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // === PANEL CENTRAL (FORMULARIO) ===
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setOpaque(false);
        panelForm.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel lblTS = new JLabel("Nombre del Tablespace:");
        lblTS.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblTS.setForeground(new Color(200, 210, 230));
        panelForm.add(lblTS, gbc);

        gbc.gridx = 1;
        txtTS = crearCampoTexto();
        panelForm.add(txtTS, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblRuta = new JLabel("Ruta del Datafile (absoluta):");
        lblRuta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblRuta.setForeground(new Color(200, 210, 230));
        panelForm.add(lblRuta, gbc);

        gbc.gridx = 1;
        txtRuta = crearCampoTexto();
        panelForm.add(txtRuta, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblSize = new JLabel("Tamaño (MB):");
        lblSize.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSize.setForeground(new Color(200, 210, 230));
        panelForm.add(lblSize, gbc);

        gbc.gridx = 1;
        txtSize = crearCampoTexto();
        txtSize.setText("50");
        panelForm.add(txtSize, gbc);

        fondo.add(panelForm, BorderLayout.CENTER);

        // === PIE DE PÁGINA CON BOTONES ===
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 25));
        pie.setOpaque(false);

        JButton btnAgregar = crearBoton("💾 Agregar Datafile", new Color(0, 120, 255));
        btnAgregar.addActionListener(e -> ejecutarAgregarDatafile());

        JButton btnRegresar = crearBoton("⏪ Volver a Tablespaces", new Color(190, 50, 50));
        btnRegresar.addActionListener(e -> {
            dispose();
            new VentanaTablespaces().setVisible(true);
        });

        pie.add(btnAgregar);
        pie.add(btnRegresar);
        fondo.add(pie, BorderLayout.SOUTH);

        setVisible(true);
    }

    // === CREAR CAMPO DE TEXTO UNIFORME ===
    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setForeground(Color.WHITE);
        campo.setBackground(new Color(25, 30, 45, 200));
        campo.setCaretColor(new Color(0, 220, 255));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 160, 255), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return campo;
    }

    // === BOTÓN REDONDEADO ===
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        boton.setPreferredSize(new Dimension(260, 50));

        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(boton.getBackground());
                g2.fillRoundRect(0, 0, boton.getWidth(), boton.getHeight(), 25, 25);
                super.paint(g, c);
            }
        });

        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(color.brighter());
            }

            public void mouseExited(MouseEvent e) {
                boton.setBackground(color);
            }
        });

        return boton;
    }

    // === FUNCIÓN PRINCIPAL ===
    private void ejecutarAgregarDatafile() {
        String tsName = txtTS.getText().trim();
        String ruta = txtRuta.getText().trim();
        int size;

        try {
            size = Integer.parseInt(txtSize.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Tamaño inválido. Ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tsName.isEmpty() || ruta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Complete todos los campos antes de continuar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = ts.agregarDatafile(tsName.toUpperCase(), ruta, size);
        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Datafile agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al agregar Datafile. Ver consola para más detalles.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === FONDO ANIMADO COHERENTE ===
    private static class FondoAnimado extends JPanel {
        private final List<Nodo> nodos = new ArrayList<>();
        private final Random rand = new Random();

        public FondoAnimado() {
            setBackground(new Color(10, 12, 18));
            for (int i = 0; i < 40; i++)
                nodos.add(new Nodo(rand.nextInt(1920), rand.nextInt(1080), rand.nextInt(2) + 1));

            Timer timer = new Timer(40, e -> {
                for (Nodo n : nodos) {
                    n.x += n.vx;
                    n.y += n.vy;
                    if (n.x < 0 || n.x > 1920) n.vx *= -1;
                    if (n.y < 0 || n.y > 1080) n.vy *= -1;
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint grad = new GradientPaint(0, 0, new Color(5, 10, 25),
                    getWidth(), getHeight(), new Color(0, 40, 70));
            g2.setPaint(grad);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(0, 120, 255, 40));
            for (Nodo n1 : nodos)
                for (Nodo n2 : nodos)
                    if (n1.dist(n2) < 150)
                        g2.drawLine((int) n1.x, (int) n1.y, (int) n2.x, (int) n2.y);

            for (Nodo n : nodos) {
                g2.setColor(new Color(0, 200, 255, 150));
                g2.fillOval((int) n.x, (int) n.y, 6, 6);
            }
        }

        private static class Nodo {
            double x, y, vx, vy;
            Nodo(double x, double y, double vel) {
                this.x = x; this.y = y;
                this.vx = vel * (Math.random() > 0.5 ? 1 : -1);
                this.vy = vel * (Math.random() > 0.5 ? 1 : -1);
            }
            double dist(Nodo o) {
                double dx = x - o.x, dy = y - o.y;
                return Math.sqrt(dx * dx + dy * dy);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaAgregarDatafile::new);
    }
}
