package ui.seguridad;

import modulos.Seguridad;
import modulos.Tablespaces;
import modulos.OperacionResultado;
import ui.VentanaSeguridad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class VentanaAsignarTablespace extends JFrame {

    private JTextField txtUsuario;
    private Seguridad seguridad = new Seguridad();
    private Tablespaces tablespaces = new Tablespaces();
    private String selectedTablespace = null;
    private JPanel selectedCard = null;

    public VentanaAsignarTablespace() {
        setTitle("Asignar Tablespace a Usuario - Oracle XE");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        FondoAnimado fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // === ENCABEZADO ===
        JLabel lblTitulo = new JLabel("Asignar Tablespace a Usuario", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));
        lblTitulo.setForeground(new Color(0, 220, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 120, 20, 120));

        // === FORMULARIO DE USUARIO ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        fondo.add(panelCentral, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel cardForm = new JPanel(new GridBagLayout());
        cardForm.setOpaque(true);
        cardForm.setBackground(new Color(255, 255, 255, 30));
        cardForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 140, 255, 120), 1, true),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints inner = new GridBagConstraints();
        inner.insets = new Insets(12, 12, 12, 12);
        inner.fill = GridBagConstraints.HORIZONTAL;
        inner.gridx = 0;
        inner.gridy = 0;

        JLabel lblUsuario = crearLabel("Usuario:");
        cardForm.add(lblUsuario, inner);
        inner.gridx = 1;
        txtUsuario = crearCampoTexto();
        cardForm.add(txtUsuario, inner);

        formPanel.add(cardForm, gbc);
        panelCentral.add(formPanel, BorderLayout.NORTH);

        // === SECCIÓN DE TABLESPACES ===
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        cardsPanel.setOpaque(false);

        List<String> lista = tablespaces.listarTablespaces();
        if (lista.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay tablespaces disponibles.", JLabel.CENTER);
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            lblVacio.setForeground(Color.LIGHT_GRAY);
            cardsPanel.add(lblVacio);
        } else {
            for (String info : lista) {
                JPanel card = crearCard(info);
                cardsPanel.add(card);
            }
        }

        JScrollPane scroll = new JScrollPane(cardsPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        panelCentral.add(scroll, BorderLayout.CENTER);

        fondo.add(panelCentral, BorderLayout.CENTER);

        // === PIE DE BOTONES ===
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pie.setOpaque(false);

        JButton btnAsignar = crearBoton("Asignar Tablespace", e -> asignar());
        JButton btnRegresar = crearBotonInferior("Volver", new Color(190, 50, 50));

        btnRegresar.addActionListener(e -> {
            dispose();
            new VentanaSeguridad().setVisible(true);
        });

        pie.add(btnAsignar);
        pie.add(btnRegresar);
        fondo.add(pie, BorderLayout.SOUTH);

        setVisible(true);
    }

    // === CREAR UNA CARD VISUAL PARA UN TABLESPACE ===
    private JPanel crearCard(String info) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(300, 130));
        card.setBackground(new Color(15, 25, 45));
        card.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 255), 2, true));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblInfo = new JLabel("<html><center>" + info.replace(" | ", "<br>") + "</center></html>", JLabel.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.add(lblInfo, BorderLayout.CENTER);

        // Efectos visuales
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!card.equals(selectedCard)) {
                    card.setBackground(new Color(25, 35, 65));
                }
            }

            public void mouseExited(MouseEvent e) {
                if (!card.equals(selectedCard)) {
                    card.setBackground(new Color(15, 25, 45));
                }
            }

            public void mouseClicked(MouseEvent e) {
                seleccionarCard(card, info);
            }
        });

        return card;
    }

    private void seleccionarCard(JPanel card, String info) {
        if (selectedCard != null) {
            selectedCard.setBackground(new Color(15, 25, 45));
        }
        card.setBackground(new Color(0, 120, 255));
        selectedCard = card;

        // Extraer nombre y tipo del tablespace
        String[] partes = info.split("\\|");
        selectedTablespace = partes[0].trim();

        boolean esTemporal = info.toUpperCase().contains("TEMPORARY");
        card.putClientProperty("temporal", esTemporal);
    }

    // === ACCIÓN PRINCIPAL ===
    private void asignar() {
        String usuario = txtUsuario.getText().trim();

        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del usuario.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedCard == null || selectedTablespace == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un tablespace.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean temporal = (boolean) selectedCard.getClientProperty("temporal");

        try {
            OperacionResultado res = seguridad.asignarTablespaceAUsuario(usuario, selectedTablespace, temporal);

            if (res.isExito()) {
                JOptionPane.showMessageDialog(this, res.getMensaje(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, res.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado al asignar tablespace:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === COMPONENTES VISUALES REUTILIZABLES ===
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto, JLabel.RIGHT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setPreferredSize(new Dimension(240, 32));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 140, 255), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campo.setBackground(new Color(240, 245, 250));
        campo.setForeground(Color.BLACK);
        return campo;
    }

    private JButton crearBoton(String texto, ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(0, 140, 255));
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addActionListener(action);
        boton.setPreferredSize(new Dimension(230, 50));
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

    private JButton crearBotonInferior(String texto, Color colorBase) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorBase);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(230, 50));

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
        SwingUtilities.invokeLater(VentanaAsignarTablespace::new);
    }
}
