package ui;

import modulos.Auditoria;
import Modelos.RegistroAuditoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VentanaAuditoria extends JFrame {

    private JTable tablaAuditoria;
    private DefaultTableModel modeloTabla;

    public VentanaAuditoria() {
        setTitle("Auditoría del Sistema - Oracle XE");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        FondoAnimado fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // === Encabezado ===
        JLabel lblTitulo = new JLabel("📋 Registros de Auditoría del Sistema", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));
        lblTitulo.setForeground(new Color(0, 220, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // === Tabla de auditoría ===
        String[] columnas = {"Usuario", "Operación", "Objeto", "Tipo", "Fecha", "Host", "Terminal"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaAuditoria = new JTable(modeloTabla);
        tablaAuditoria.setRowHeight(26);
        tablaAuditoria.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tablaAuditoria.setForeground(Color.WHITE);
        tablaAuditoria.setBackground(new Color(20, 25, 35));
        tablaAuditoria.setGridColor(new Color(50, 60, 80));
        tablaAuditoria.setSelectionBackground(new Color(0, 100, 180));
        tablaAuditoria.setSelectionForeground(Color.WHITE);

        JTableHeader header = tablaAuditoria.getTableHeader();
        header.setBackground(new Color(0, 60, 110));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));

        JScrollPane scrollPane = new JScrollPane(tablaAuditoria);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        fondo.add(scrollPane, BorderLayout.CENTER);

        // === Panel de botones ===
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pie.setOpaque(false);

        JButton btnRefrescar = crearBoton("🔄 Refrescar", e -> cargarRegistros());
        JButton btnRegresar = crearBotonInferior("⬅️ Volver al Menú", new Color(190, 50, 50));

        btnRegresar.addActionListener(e -> {
            dispose();
            new VentanaPrincipal().setVisible(true);
        });

        pie.add(btnRefrescar);
        pie.add(btnRegresar);
        fondo.add(pie, BorderLayout.SOUTH);

        // === Cargar registros ===
        cargarRegistros();

        setVisible(true);
    }

    private void cargarRegistros() {
        modeloTabla.setRowCount(0);
        Auditoria auditoria = new Auditoria();

        try {
            List<RegistroAuditoria> registros = auditoria.obtenerRegistros();

            if (registros.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron registros de auditoría.",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (RegistroAuditoria r : registros) {
                modeloTabla.addRow(new Object[]{
                        r.getUsuario(),
                        r.getTipoOperacion(),
                        r.getObjeto(),
                        r.getTipoObjeto(),
                        r.getFecha(),
                        r.getHost(),
                        r.getTerminal()
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al cargar los registros de auditoría:\n" + ex.getMessage(),
                    "Error de carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === Botones personalizados ===
    private JButton crearBoton(String texto, ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(0, 140, 255));
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addActionListener(action);
        boton.setPreferredSize(new Dimension(250, 55));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // Bordes redondeados
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

    private JButton crearBotonInferior(String texto, Color colorBase) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorBase);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(250, 50));

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

    // === Fondo animado ===
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
}
