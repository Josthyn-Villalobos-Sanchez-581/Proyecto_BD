package ui.tablespaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ui.VentanaPrincipal;

public class VentanaTablespaces extends JFrame {

    public VentanaTablespaces() {
        setTitle("🗂 Administración de Tablespaces - Oracle XE");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // === FONDO ANIMADO ===
        FondoAnimado fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // === ENCABEZADO ===
        JLabel lblTitulo = new JLabel("Administración de Tablespaces - Oracle XE", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));
        lblTitulo.setForeground(new Color(0, 220, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 35, 35));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(60, 200, 60, 200));

        // === BOTONES PRINCIPALES ===
        panelBotones.add(crearBoton("📁 Crear Tablespace", e -> abrir(VentanaCrearTablespace.class)));
        panelBotones.add(crearBoton("➕ Agregar Datafile", e -> abrir(VentanaAgregarDatafile.class)));
        panelBotones.add(crearBoton("📜 Listar Tablespaces", e -> abrir(VentanaListarTablespaces.class)));
        panelBotones.add(crearBoton("📄 Listar Datafiles", e -> abrir(VentanaListarDatafiles.class)));
        panelBotones.add(crearBoton("⚙️ Redimensionar Datafile", e -> abrir(VentanaRedimensionarDatafile.class)));
        panelBotones.add(crearBoton("🗑 Eliminar Tablespace", e -> abrir(VentanaEliminarTablespace.class)));

        fondo.add(panelBotones, BorderLayout.CENTER);

        // === BOTÓN VOLVER ===
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 25));
        pie.setOpaque(false);

        JButton btnRegresar = crearBotonInferior("⏪ Volver al Menú Principal", new Color(190, 50, 50));
        btnRegresar.addActionListener(e -> {
            dispose();
            new VentanaPrincipal().setVisible(true);
        });

        pie.add(btnRegresar);
        fondo.add(pie, BorderLayout.SOUTH);

        setVisible(true);
    }

    // === BOTÓN PRINCIPAL UNIFICADO ===
    private JButton crearBoton(String texto, ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(0, 140, 255));
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addActionListener(action);
        boton.setPreferredSize(new Dimension(250, 55));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Redondeado moderno
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

        // Hover efecto suave
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(0, 180, 255));
            }

            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(0, 140, 255));
            }
        });

        return boton;
    }

    // === BOTÓN INFERIOR ===
    private JButton crearBotonInferior(String texto, Color colorBase) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorBase);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(280, 50));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

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
                boton.setBackground(colorBase.brighter());
            }

            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorBase);
            }
        });

        return boton;
    }

    // === MÉTODO GENÉRICO PARA ABRIR SUBVENTANAS ===
    private void abrir(Class<? extends JFrame> claseVentana) {
        dispose();
        SwingUtilities.invokeLater(() -> {
            try {
                claseVentana.getDeclaredConstructor().newInstance().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir ventana: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // === FONDO ANIMADO COHERENTE CON TODO EL SISTEMA ===
    private static class FondoAnimado extends JPanel {
        private final java.util.List<Nodo> nodos = new ArrayList<>();
        private final java.util.Random rand = new Random();

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
        SwingUtilities.invokeLater(VentanaTablespaces::new);
    }
}
