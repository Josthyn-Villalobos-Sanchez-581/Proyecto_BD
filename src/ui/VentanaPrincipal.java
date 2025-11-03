package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 🔹 Importar vistas modernas
import ui.performance.TunningView;
import ui.performance.PerformanceBDView;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Administrador Oracle XE - Proyecto BD");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // === FONDO ANIMADO ===
        FondoAnimado fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // === ENCABEZADO ===
        JLabel lblTitulo = new JLabel("Administración y Auditoría de Base de Datos Oracle 21c XE", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));
        lblTitulo.setForeground(new Color(0, 220, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // === PANEL CENTRAL (2 FILAS x 3 COLUMNAS) ===
        JPanel panelOpciones = new JPanel(new GridLayout(2, 3, 40, 40));
        panelOpciones.setOpaque(false);
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(60, 180, 60, 180));

        // === TARJETAS DE MÓDULOS ===
        panelOpciones.add(crearTarjeta("🔐", "Seguridad", "Administrar roles, usuarios y privilegios.", () -> {
            dispose(); new VentanaSeguridad().setVisible(true);
        }));

        panelOpciones.add(crearTarjeta("🧩", "Auditoría", "Registrar y analizar eventos en la base de datos.", () -> {
            dispose(); new VentanaAuditoria().setVisible(true);
        }));

        panelOpciones.add(crearTarjeta("⚙️", "Tunning", "Optimizar el rendimiento de consultas SQL.", () -> {
            dispose(); new TunningView().setVisible(true);
        }));

        panelOpciones.add(crearTarjeta("🗂", "Tablespaces", "Administrar el almacenamiento y datafiles.", () -> {
            dispose(); new ui.tablespaces.VentanaTablespaces().setVisible(true);
        }));

        panelOpciones.add(crearTarjeta("💾", "Respaldos", "Gestionar respaldos y restauraciones.", () -> {
            dispose(); new VentanaBackupRestore().setVisible(true);
        }));

        panelOpciones.add(crearTarjeta("📈", "Performance", "Monitorear el rendimiento de la base de datos.", () -> {
            dispose(); new PerformanceBDView().setVisible(true);
        }));

        fondo.add(panelOpciones, BorderLayout.CENTER);

        // === PIE DE PÁGINA ===
        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        pie.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton btnAcerca = crearBotonInferior("🛈 Acerca del Sistema", new Color(0, 140, 255), e -> {
            JOptionPane.showMessageDialog(this, """
                    📊 Proyecto Final - Administración y Auditoría de BD Oracle 21c XE
                    Ingeniería en Sistemas - Universidad Nacional (UNA)
                    Desarrollado por: Jairo, Josthyn, Gerald y Froylan
                    Curso: Administración de Bases de Datos
                    Profesor: MAP. Rodolfo Sánchez Sánchez
                    """, "Información del Proyecto", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnSalir = crearBotonInferior("⏻ Salir del Sistema", new Color(190, 50, 50), e -> System.exit(0));

        JPanel botonesPie = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        botonesPie.setOpaque(false);
        botonesPie.add(btnAcerca);
        botonesPie.add(btnSalir);

        JLabel lblFooter = new JLabel("© 2025 - Proyecto Final Oracle XE - Universidad Nacional de Costa Rica", JLabel.CENTER);
        lblFooter.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
        lblFooter.setForeground(new Color(160, 170, 180));
        lblFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pie.add(botonesPie, BorderLayout.CENTER);
        pie.add(lblFooter, BorderLayout.SOUTH);
        fondo.add(pie, BorderLayout.SOUTH);

        setVisible(true);
    }

    // === TARJETA DE MÓDULO (ÍCONO + DESCRIPCIÓN + BOTÓN) ===
    private JPanel crearTarjeta(String icono, String titulo, String descripcion, Runnable accion) {
        JPanel card = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 30, 45, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblIcono = new JLabel(icono, JLabel.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        lblIcono.setForeground(new Color(0, 220, 255));

        JLabel lblTitulo = new JLabel(titulo, JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        lblTitulo.setForeground(new Color(0, 220, 255));

        JTextArea lblDesc = new JTextArea(descripcion);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(new Color(200, 210, 230));
        lblDesc.setOpaque(false);
        lblDesc.setEditable(false);
        lblDesc.setWrapStyleWord(true);
        lblDesc.setLineWrap(true);
        lblDesc.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JButton btnAbrir = crearBotonModulo("Abrir módulo", accion);

        JPanel contenedorBoton = new JPanel();
        contenedorBoton.setOpaque(false);
        contenedorBoton.add(btnAbrir);

        card.add(lblIcono, BorderLayout.NORTH);
        card.add(lblTitulo, BorderLayout.CENTER);
        card.add(lblDesc, BorderLayout.SOUTH);
        card.add(contenedorBoton, BorderLayout.PAGE_END);

        // Hover visual del panel
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                lblIcono.setForeground(new Color(0, 255, 255));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 240, 255), 2, true),
                        BorderFactory.createEmptyBorder(18, 18, 18, 18)
                ));
            }

            public void mouseExited(MouseEvent e) {
                lblIcono.setForeground(new Color(0, 220, 255));
                card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            }
        });

        return card;
    }

    // === BOTÓN DE CADA MÓDULO (ARMONIZADO CON INFERIORES) ===
    private JButton crearBotonModulo(String texto, Runnable accion) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 140, 255));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> accion.run());

        // Hover elegante
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0, 180, 255));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0, 140, 255));
            }
        });

        // Bordes redondeados
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getBackground());
                g2.fillRoundRect(0, 0, btn.getWidth(), btn.getHeight(), 20, 20);
                super.paint(g, c);
            }
        });

        return btn;
    }

    // === BOTONES INFERIORES (Acerca / Salir) UNIFICADOS ===
    private JButton crearBotonInferior(String texto, Color colorBase, ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorBase);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);

        // Hover luminoso
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorBase.brighter());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(colorBase);
            }
        });

        // Bordes redondeados uniformes
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getBackground());
                g2.fillRoundRect(0, 0, btn.getWidth(), btn.getHeight(), 25, 25);
                super.paint(g, c);
            }
        });

        return btn;
    }

    // === FONDO ANIMADO ===
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
            double x, y;
            double vx, vy;
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

    // === MAIN ===
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
