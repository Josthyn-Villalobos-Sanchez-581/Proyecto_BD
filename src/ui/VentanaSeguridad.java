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
        setTitle("🔐 Módulo de Seguridad - Oracle XE");
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

        // === PANEL CENTRAL CON TARJETAS ===
        JPanel panelTarjetas = new JPanel(new GridLayout(2, 5, 30, 30));
        panelTarjetas.setOpaque(false);
        panelTarjetas.setBorder(BorderFactory.createEmptyBorder(50, 120, 60, 120));

        // === TARJETAS DE FUNCIONALIDADES ===
        panelTarjetas.add(crearTarjeta("👤", "Crear Usuario", "Registrar un nuevo usuario en la base de datos.", e -> abrir(VentanaCrearUsuario.class)));
        panelTarjetas.add(crearTarjeta("🗑", "Borrar Usuario", "Eliminar un usuario existente del sistema.", e -> abrir(VentanaBorrarUsuario.class)));
        panelTarjetas.add(crearTarjeta("🧩", "Crear Rol", "Definir un nuevo rol para control de acceso.", e -> abrir(VentanaCrearRol.class)));
        panelTarjetas.add(crearTarjeta("🚫", "Eliminar Rol", "Eliminar un rol existente del sistema.", e -> abrir(VentanaEliminarRol.class)));
        panelTarjetas.add(crearTarjeta("📎", "Asignar Rol", "Asignar roles a usuarios específicos.", e -> abrir(VentanaAsignarRol.class)));
        panelTarjetas.add(crearTarjeta("🔓", "Revocar Rol", "Quitar roles asignados a un usuario.", e -> abrir(VentanaRevocarRol.class)));
        panelTarjetas.add(crearTarjeta("🔑", "Privilegio a Usuario", "Otorgar privilegios individuales a usuarios.", e -> abrir(VentanaPrivilegioUsuario.class)));
        panelTarjetas.add(crearTarjeta("🛡", "Privilegio a Rol", "Asignar privilegios a roles específicos.", e -> abrir(VentanaPrivilegioRol.class)));
        panelTarjetas.add(crearTarjeta("❌", "Revocar Privilegio", "Quitar privilegios a usuarios o roles.", e -> abrir(VentanaRevocarPrivilegio.class)));
        panelTarjetas.add(crearTarjeta("💾", "Asignar Tablespace", "Asociar tablespaces a usuarios.", e -> abrir(VentanaAsignarTablespace.class)));

        fondo.add(panelTarjetas, BorderLayout.CENTER);

        // === PIE DE PÁGINA (BOTÓN VOLVER) ===
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

    // === TARJETA MODERNA (como la pantalla principal) ===
    private JPanel crearTarjeta(String icono, String titulo, String descripcion, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 30, 45, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblIcono = new JLabel(icono, JLabel.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        lblIcono.setForeground(new Color(0, 220, 255));

        JLabel lblTitulo = new JLabel(titulo, JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        lblTitulo.setForeground(new Color(0, 220, 255));

        JTextArea lblDesc = new JTextArea(descripcion);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(new Color(200, 210, 230));
        lblDesc.setOpaque(false);
        lblDesc.setEditable(false);
        lblDesc.setWrapStyleWord(true);
        lblDesc.setLineWrap(true);
        lblDesc.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JButton btnAbrir = new JButton("Abrir");
        btnAbrir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAbrir.setForeground(Color.WHITE);
        btnAbrir.setBackground(new Color(0, 120, 255));
        btnAbrir.setFocusPainted(false);
        btnAbrir.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnAbrir.addActionListener(action);

        // Efecto hover
        btnAbrir.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnAbrir.setBackground(new Color(0, 180, 255));
            }
            public void mouseExited(MouseEvent e) {
                btnAbrir.setBackground(new Color(0, 120, 255));
            }
        });

        card.add(lblIcono, BorderLayout.NORTH);
        card.add(lblTitulo, BorderLayout.CENTER);
        card.add(lblDesc, BorderLayout.SOUTH);

        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.add(btnAbrir);
        card.add(panelBoton, BorderLayout.PAGE_END);

        // Hover de tarjeta
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 240, 255), 2, true),
                        BorderFactory.createEmptyBorder(18, 18, 18, 18)
                ));
                lblIcono.setForeground(new Color(0, 255, 255));
            }

            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                lblIcono.setForeground(new Color(0, 220, 255));
            }
        });

        return card;
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
        SwingUtilities.invokeLater(VentanaSeguridad::new);
    }
}
