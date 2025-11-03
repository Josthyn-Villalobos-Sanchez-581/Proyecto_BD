package ui;

import modulos.BackupRestore;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VentanaBackupRestore extends JFrame {

    private final BackupRestore br = new BackupRestore();

    public VentanaBackupRestore() {
        setTitle("💾 Módulo de Respaldo y Recuperación - Oracle XE");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // === FONDO ANIMADO ===
        FondoAnimado fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // === ENCABEZADO ===
        JLabel lblTitulo = new JLabel("Respaldo y Recuperación de la Base de Datos", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));
        lblTitulo.setForeground(new Color(0, 220, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // === PANEL CENTRAL CON TARJETAS ===
        JPanel panelTarjetas = new JPanel(new GridLayout(2, 3, 35, 35));
        panelTarjetas.setOpaque(false);
        panelTarjetas.setBorder(BorderFactory.createEmptyBorder(60, 150, 60, 150));

        // === TARJETAS DE FUNCIONALIDAD ===
        panelTarjetas.add(crearTarjeta("📀", "Respaldo Completo (FULL)", "Genera un respaldo completo de toda la base de datos.", e -> ejecutarBackupFull()));
        panelTarjetas.add(crearTarjeta("🧩", "Respaldo por Esquema", "Crea un respaldo de un esquema específico.", e -> ejecutarBackupSchema()));
        panelTarjetas.add(crearTarjeta("📋", "Respaldo por Tabla", "Genera un respaldo de una tabla específica.", e -> ejecutarBackupTabla()));
        panelTarjetas.add(crearTarjeta("♻️", "Restaurar Respaldo", "Permite restaurar respaldos previos (FULL, SCHEMA o TABLE).", e -> ejecutarRestore()));
        panelTarjetas.add(crearTarjeta("📂", "Ver Carpeta de Backups", "Abre la carpeta donde se guardan los respaldos y logs.", e -> abrirCarpeta()));

        fondo.add(panelTarjetas, BorderLayout.CENTER);

        // === PIE DE PÁGINA (BOTÓN VOLVER) ===
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 25));
        pie.setOpaque(false);

        JButton btnVolver = crearBotonInferior("Volver al Menú Principal", new Color(190, 50, 50));
        btnVolver.addActionListener(e -> {
            dispose();
            new VentanaPrincipal().setVisible(true);
        });

        pie.add(btnVolver);
        fondo.add(pie, BorderLayout.SOUTH);

        setVisible(true);
    }

    // === TARJETA MODERNA ===
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

        JButton btnAccion = new JButton("Ejecutar");
        btnAccion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAccion.setForeground(Color.WHITE);
        btnAccion.setBackground(new Color(0, 120, 255));
        btnAccion.setFocusPainted(false);
        btnAccion.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnAccion.addActionListener(action);

        // Hover suave
        btnAccion.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnAccion.setBackground(new Color(0, 180, 255));
            }

            public void mouseExited(MouseEvent e) {
                btnAccion.setBackground(new Color(0, 120, 255));
            }
        });

        card.add(lblIcono, BorderLayout.NORTH);
        card.add(lblTitulo, BorderLayout.CENTER);
        card.add(lblDesc, BorderLayout.SOUTH);

        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.add(btnAccion);
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

    // === BOTÓN INFERIOR ESTILIZADO ===
    private JButton crearBotonInferior(String texto, Color colorBase) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorBase);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(280, 50));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

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

    // === ACCIONES ===
    private void ejecutarBackupFull() {
        SwingUtilities.invokeLater(() -> {
            String ruta = br.realizarBackupFull();
            if (ruta != null) {
                JOptionPane.showMessageDialog(this, "✅ Backup FULL creado:\n" + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Falló backup FULL. Revisa la consola y logs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void ejecutarBackupSchema() {
        String schema = JOptionPane.showInputDialog(this, "Ingrese nombre de esquema (schema):", "SCHEMA", JOptionPane.PLAIN_MESSAGE);
        if (schema != null && !schema.trim().isEmpty()) {
            final String schemaFinal = schema.trim().toUpperCase();
            SwingUtilities.invokeLater(() -> {
                String ruta = br.realizarBackupPorSchema(schemaFinal);
                if (ruta != null) {
                    JOptionPane.showMessageDialog(this, "✅ Backup de esquema creado:\n" + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Falló backup por esquema.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    private void ejecutarBackupTabla() {
        String schema = JOptionPane.showInputDialog(this, "Ingrese nombre de esquema (schema):", "SCHEMA", JOptionPane.PLAIN_MESSAGE);
        String tabla = JOptionPane.showInputDialog(this, "Ingrese nombre de tabla:", "TABLE", JOptionPane.PLAIN_MESSAGE);
        if (schema != null && tabla != null && !schema.trim().isEmpty() && !tabla.trim().isEmpty()) {
            final String schemaFinal = schema.trim().toUpperCase();
            final String tablaFinal = tabla.trim().toUpperCase();
            SwingUtilities.invokeLater(() -> {
                String ruta = br.realizarBackupPorTabla(schemaFinal, tablaFinal);
                if (ruta != null) {
                    JOptionPane.showMessageDialog(this, "✅ Backup de tabla creado:\n" + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Falló backup por tabla.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    private void ejecutarRestore() {
        String[] opciones = {"FULL", "SCHEMA", "TABLE"};
        String tipo = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el tipo de backup:",
                "Tipo de Restauración",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (tipo == null) return;

        String schemaOtabla = null;
        if (tipo.equals("SCHEMA")) {
            schemaOtabla = JOptionPane.showInputDialog(this, "Ingrese el nombre del schema a restaurar:", "SCHEMA", JOptionPane.PLAIN_MESSAGE);
            if (schemaOtabla == null || schemaOtabla.trim().isEmpty()) return;
        } else if (tipo.equals("TABLE")) {
            schemaOtabla = JOptionPane.showInputDialog(this, "Ingrese schema.tabla a restaurar (ej: PRUEBA.MI_TABLA):", "TABLE", JOptionPane.PLAIN_MESSAGE);
            if (schemaOtabla == null || schemaOtabla.trim().isEmpty()) return;
        }

        String dump = JOptionPane.showInputDialog(this, "Ingrese nombre del archivo .dmp:", "RESTORE", JOptionPane.PLAIN_MESSAGE);
        if (dump == null || dump.trim().isEmpty()) return;

        final String tipoFinal = tipo;
        final String schemaFinal = (schemaOtabla != null) ? schemaOtabla.trim() : null;
        final String dumpFinal = dump.trim();

        SwingUtilities.invokeLater(() -> {
            boolean ok = br.restaurarBackup(tipoFinal, schemaFinal, dumpFinal);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Restauración ejecutada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Falló la restauración. Revisa consola y logs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void abrirCarpeta() {
        try {
            Desktop.getDesktop().open(new java.io.File(System.getProperty("user.dir") + "\\resources\\backups"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir la carpeta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        SwingUtilities.invokeLater(VentanaBackupRestore::new);
    }
}
