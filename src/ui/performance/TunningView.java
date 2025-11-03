package ui.performance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import modulos.Performance;
import ui.VentanaPrincipal;

public class TunningView extends JFrame {
    private final Performance performance;
    private final JTextArea txtConsulta;
    private final JTextArea txtResultados;

    public TunningView() {
        setTitle("Tunning de Consultas SQL - Proyecto BD Oracle XE");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // === FONDO ANIMADO ===
        FondoAnimado fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // === CABECERA ===
        JLabel titulo = new JLabel("⚙️ Tunning de Consultas SQL", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 28));
        titulo.setForeground(new Color(0, 220, 255));
        titulo.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        fondo.add(titulo, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel centro = new JPanel(new BorderLayout(20, 20));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(30, 120, 40, 120));

        // Campo de consulta SQL
        txtConsulta = new JTextArea(6, 60);
        txtConsulta.setFont(new Font("Consolas", Font.PLAIN, 15));
        txtConsulta.setBackground(new Color(20, 25, 35));
        txtConsulta.setForeground(new Color(220, 235, 250));
        txtConsulta.setCaretColor(new Color(0, 220, 255));
        txtConsulta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 150, 255), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        txtConsulta.setText("-- Escriba aquí su consulta SQL...");
        centro.add(new JScrollPane(txtConsulta), BorderLayout.NORTH);

        // Resultados
        txtResultados = new JTextArea(15, 60);
        txtResultados.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtResultados.setForeground(new Color(210, 220, 230));
        txtResultados.setBackground(new Color(15, 20, 28));
        txtResultados.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 150, 255), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        txtResultados.setEditable(false);
        centro.add(new JScrollPane(txtResultados), BorderLayout.CENTER);
        fondo.add(centro, BorderLayout.CENTER);

        // === PANEL DE BOTONES ===
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 25));
        botones.setOpaque(false);
        botones.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        JButton btnAnalizar = crearBoton("🔍 Analizar Consulta", new Color(0, 140, 255));
        JButton btnIndice = crearBoton("⚙️ Crear Índice", new Color(0, 140, 255));
        JButton btnStats = crearBoton("📊 Actualizar Estadísticas", new Color(0, 140, 255));
        JButton btnVolver = crearBoton("⏪ Volver al Menú", new Color(190, 50, 50));

        botones.add(btnAnalizar);
        botones.add(btnIndice);
        botones.add(btnStats);
        botones.add(btnVolver);
        fondo.add(botones, BorderLayout.SOUTH);

        // === BACKEND ===
        Performance temp;
        try {
            temp = new Performance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de conexión con la BD: " + e.getMessage());
            temp = null;
        }
        performance = temp;

        // === EVENTOS ===
        btnAnalizar.addActionListener(e -> {
            if (performance == null) return;
            String sql = txtConsulta.getText().trim();
            if (sql.isEmpty() || sql.startsWith("--")) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese una consulta SQL válida.");
                return;
            }
            txtResultados.setText("⏳ Analizando consulta...\n");
            String resultado = performance.analizarConsulta(sql);
            txtResultados.setText(resultado + "\n✅ Consulta analizada correctamente.\n");
        });

        btnIndice.addActionListener(e -> {
            if (performance == null) return;
            String tabla = JOptionPane.showInputDialog(this, "Nombre de la tabla:");
            String columna = JOptionPane.showInputDialog(this, "Columna para el índice:");
            if (tabla != null && columna != null) {
                performance.crearIndice(tabla, columna);
                txtResultados.append("\n✅ Índice creado: " + tabla + "(" + columna + ")\n");
            }
        });

        btnStats.addActionListener(e -> {
            if (performance == null) return;
            String tabla = JOptionPane.showInputDialog(this, "Tabla para actualizar estadísticas:");
            if (tabla != null) {
                performance.actualizarEstadisticas(tabla);
                txtResultados.append("\n📊 Estadísticas actualizadas para " + tabla + "\n");
            }
        });

        btnVolver.addActionListener(e -> {
            dispose();
            new VentanaPrincipal().setVisible(true);
        });

        setVisible(true);
    }

    // === BOTÓN REDONDEADO Y COHERENTE CON EL DASHBOARD ===
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(230, 45));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Bordes redondeados y hover suave
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

    // === FONDO ANIMADO — MISMO QUE VENTANA PRINCIPAL ===
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
        SwingUtilities.invokeLater(TunningView::new);
    }
}
