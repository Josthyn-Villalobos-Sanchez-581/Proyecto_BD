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
        setTitle("💾 Respaldo y Recuperación - Oracle XE");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // === FONDO ANIMADO ===
        FondoAnimado fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // === ENCABEZADO ===
        JLabel lblTitulo = new JLabel("Módulo de Respaldo y Recuperación - Oracle XE", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));
        lblTitulo.setForeground(new Color(0, 220, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panel = new JPanel(new GridLayout(3, 2, 35, 35));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(60, 220, 60, 220));

        // === BOTONES DE FUNCIONALIDAD ===
        JButton btnFull = crearBoton("📀 Respaldo Completo (FULL)", new Color(0, 140, 255));
        btnFull.addActionListener(e -> ejecutarBackupFull());

        JButton btnSchema = crearBoton("🧩 Respaldo por Esquema", new Color(0, 140, 255));
        btnSchema.addActionListener(e -> ejecutarBackupSchema());

        JButton btnTabla = crearBoton("📋 Respaldo por Tabla", new Color(0, 140, 255));
        btnTabla.addActionListener(e -> ejecutarBackupTabla());

        JButton btnRestore = crearBoton("♻️ Restaurar Respaldo", new Color(0, 140, 255));
        btnRestore.addActionListener(e -> ejecutarRestore());

        JButton btnVerLogs = crearBoton("📂 Ver Carpeta de Backups / Logs", new Color(0, 140, 255));
        btnVerLogs.addActionListener(e -> abrirCarpeta());

        // === BOTÓN VOLVER ===
        JButton btnRegresar = crearBoton("⏪ Volver al Menú Principal", new Color(190, 50, 50));
        btnRegresar.addActionListener(e -> {
            dispose();
            new VentanaPrincipal().setVisible(true);
        });

        // === AGREGAR BOTONES AL PANEL ===
        panel.add(btnFull);
        panel.add(btnSchema);
        panel.add(btnTabla);
        panel.add(btnRestore);
        panel.add(btnVerLogs);
        panel.add(btnRegresar);

        fondo.add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // === BOTÓN MODERNO REDONDEADO ===
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Redondeado y suavizado
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

        // Hover suave
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

    // === ACCIONES DE LOS BOTONES ===
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
            SwingUtilities.invokeLater(() -> {
                String ruta = br.realizarBackupPorSchema(schema.trim().toUpperCase());
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
            SwingUtilities.invokeLater(() -> {
                String ruta = br.realizarBackupPorTabla(schema.trim().toUpperCase(), tabla.trim().toUpperCase());
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

        String dump = JOptionPane.showInputDialog(this, "Ingrese nombre del archivo .dmp (ej: backup_full_2025-11-01_15-30.dmp):", "RESTORE", JOptionPane.PLAIN_MESSAGE);
        if (dump == null || dump.trim().isEmpty()) return;

        String dumpTrim = dump.trim();
        String schemaTrim = (schemaOtabla != null) ? schemaOtabla.trim() : null;

        SwingUtilities.invokeLater(() -> {
            boolean ok = br.restaurarBackup(tipo, schemaTrim, dumpTrim);
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

    // === FONDO ANIMADO COHERENTE CON TODO EL SISTEMA ===
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

