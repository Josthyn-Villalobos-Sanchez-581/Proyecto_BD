package ui;

import ui.seguridad.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VentanaSeguridad extends JFrame {

    public VentanaSeguridad() {
        setTitle("Módulo de Seguridad - Oracle XE");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // === FONDO ANIMADO ===
        FondoAnimado fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // === ENCABEZADO ===
        JLabel lblTitulo = new JLabel("Administración de Seguridad - Oracle XE", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));
        lblTitulo.setForeground(new Color(0, 220, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelBotones = new JPanel(new GridLayout(5, 2, 25, 25));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(60, 220, 60, 220));

        // === BOTONES DE FUNCIONALIDAD ===
        panelBotones.add(crearBoton("Crear Usuario", e -> abrir(VentanaCrearUsuario.class)));
        panelBotones.add(crearBoton("Borrar Usuario", e -> abrir(VentanaBorrarUsuario.class)));
        panelBotones.add(crearBoton("Crear Rol", e -> abrir(VentanaCrearRol.class)));
        panelBotones.add(crearBoton("Eliminar Rol", e -> abrir(VentanaEliminarRol.class)));
        panelBotones.add(crearBoton("Asignar Rol", e -> abrir(VentanaAsignarRol.class)));
        panelBotones.add(crearBoton("Revocar Rol", e -> abrir(VentanaRevocarRol.class)));
        panelBotones.add(crearBoton("Privilegio a Usuario", e -> abrir(VentanaPrivilegioUsuario.class)));
        panelBotones.add(crearBoton("Privilegio a Rol", e -> abrir(VentanaPrivilegioRol.class)));
        panelBotones.add(crearBoton("Revocar Privilegio", e -> abrir(VentanaRevocarPrivilegio.class)));
        panelBotones.add(crearBoton("Asignar Tablespace", e -> abrir(VentanaAsignarTablespace.class)));

        fondo.add(panelBotones, BorderLayout.CENTER);

        // === BOTÓN VOLVER ===
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 25));
        pie.setOpaque(false);

        JButton btnRegresar = crearBotonInferior("Volver al Menú Principal", new Color(190, 50, 50));
        btnRegresar.addActionListener(e -> {
            dispose();
            new VentanaPrincipal().setVisible(true);
        });

        pie.add(btnRegresar);
        fondo.add(pie, BorderLayout.SOUTH);

        setVisible(true);
    }

    // === BOTÓN MODERNO ===
    private JButton crearBoton(String texto, ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(0, 140, 255));
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addActionListener(action);
        boton.setPreferredSize(new Dimension(220, 55));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

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
                boton.setBackground(new Color(0, 180, 255));
            }

            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(0, 140, 255));
            }
        });

        return boton;
    }

    // === BOTÓN INFERIOR UNIFICADO ===
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

    // === MÉTODO PARA ABRIR SUBVENTANAS ===
    private void abrir(Class<? extends JFrame> claseVentana) {
        dispose();
        SwingUtilities.invokeLater(() -> {
            try {
                claseVentana.getDeclaredConstructor().newInstance().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir la ventana: " + ex.getMessage());
            }
        });
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
            double x, y, vx, vy;

            Nodo(double x, double y, double vel) {
                this.x = x;
                this.y = y;
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
        SwingUtilities.invokeLater(VentanaSeguridad::new);
    }
}
