package ui.performance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import modulos.Performance;
import ui.VentanaPrincipal;
import javax.swing.text.StyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class PerformanceBDView extends JFrame {
    private final Performance performance;
    private final JTextPane txtResultados; // 🔹 Cambiado a JTextPane para permitir centrado y formato

    public PerformanceBDView() {
        setTitle("📈 Performance de la Base de Datos - Oracle XE");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // === FONDO ANIMADO ===
        FondoAnimado fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // === CABECERA ===
        JLabel titulo = new JLabel("Monitoreo del Rendimiento de la Base de Datos Oracle", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));
        titulo.setForeground(new Color(0, 220, 255));
        titulo.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        fondo.add(titulo, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel centro = new JPanel(new BorderLayout(20, 20));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(30, 150, 40, 150));

        // 🔹 JTextPane (permite centrado y estilos)
        txtResultados = new JTextPane();
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Consolas", Font.BOLD, 18));
        txtResultados.setForeground(new Color(230, 240, 255));
        txtResultados.setBackground(new Color(15, 20, 28));
        txtResultados.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 150, 255), 1, true),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // 🔹 Centrar texto visualmente
        StyledDocument doc = txtResultados.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontSize(center, 18);
        StyleConstants.setForeground(center, new Color(230, 240, 255));
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        JScrollPane scrollResultados = new JScrollPane(txtResultados);
        scrollResultados.setOpaque(false);
        scrollResultados.getViewport().setOpaque(false);
        scrollResultados.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 255), 1, true));
        centro.add(scrollResultados, BorderLayout.CENTER);
        fondo.add(centro, BorderLayout.CENTER);

        // === BOTONES INFERIORES ===
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 25));
        botones.setOpaque(false);

        JButton btnCargar = crearBoton("Ver Métricas", new Color(0, 140, 255));
        JButton btnVolver = crearBoton("Volver al Menú", new Color(190, 50, 50));

        botones.add(btnCargar);
        botones.add(btnVolver);
        fondo.add(botones, BorderLayout.SOUTH);

        // === BACKEND ===
        Performance temp;
        try {
            temp = new Performance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la BD: " + e.getMessage());
            temp = null;
        }
        performance = temp;

        // === EVENTOS ===
        btnCargar.addActionListener(e -> {
            if (performance == null) return;
            mostrarMensaje("⏳ Consultando métricas globales...\n\n");
            String resultado = performance.mostrarPerformanceBD();
            mostrarMensaje(resultado + "\n\n✅ Métricas consultadas correctamente.\n");
        });

        btnVolver.addActionListener(e -> {
            dispose();
            new VentanaPrincipal().setVisible(true);
        });

        setVisible(true);
    }

    // === MÉTODO PARA ACTUALIZAR TEXTO CENTRADO ===
    private void mostrarMensaje(String mensaje) {
        StyledDocument doc = txtResultados.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            StyleConstants.setFontSize(center, 18);
            StyleConstants.setForeground(center, new Color(230, 240, 255));
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            doc.insertString(doc.getLength(), mensaje, center);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === BOTÓN REDONDEADO ===
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(230, 45));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

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

    // === FONDO ANIMADO ===
    private static class FondoAnimado extends JPanel {
        private final List<Nodo> nodos = new ArrayList<>();
        private final Random rand = new Random();

        public FondoAnimado() {
            setBackground(new Color(10, 12, 18));
            for (int i = 0; i < 35; i++)
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
        SwingUtilities.invokeLater(PerformanceBDView::new);
    }
}
