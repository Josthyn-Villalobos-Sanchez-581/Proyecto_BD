package ui;

import modulos.BackupRestore;

import javax.swing.*;
import java.awt.*;

public class VentanaBackupRestore extends JFrame {

    private BackupRestore br = new BackupRestore();

    public VentanaBackupRestore() {
        setTitle("Backup / Restore - Oracle XE");
        setSize(720, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Módulo de Respaldo y Recuperación", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(lbl, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3, 2, 18, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        panel.setBackground(Color.WHITE);

        JButton btnFull = crearBoton("Respaldo Completo (FULL)");
        btnFull.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                String ruta = br.realizarBackupFull();
                if (ruta != null) {
                    JOptionPane.showMessageDialog(this, "✅ Backup FULL creado:\n" + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Falló backup FULL. Revisa la consola y logs.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        });

        JButton btnSchema = crearBoton("Respaldo por Esquema");
        btnSchema.addActionListener(e -> {
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
        });

        JButton btnTabla = crearBoton("Respaldo por Tabla");
        btnTabla.addActionListener(e -> {
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
        });

        JButton btnRestore = crearBoton("Restaurar Respaldo");
        btnRestore.addActionListener(e -> {
            // Primero, elegir el tipo de backup
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

            if (tipo == null) return; // Canceló

            // Para SCHEMA o TABLE pedimos schema o schema.tabla
            String schemaOtabla = null;
            if (tipo.equals("SCHEMA")) {
                schemaOtabla = JOptionPane.showInputDialog(this, "Ingrese el nombre del schema a restaurar:", "SCHEMA", JOptionPane.PLAIN_MESSAGE);
                if (schemaOtabla == null || schemaOtabla.trim().isEmpty()) return;
            } else if (tipo.equals("TABLE")) {
                schemaOtabla = JOptionPane.showInputDialog(this, "Ingrese schema.tabla a restaurar (ej: PRUEBA.MI_TABLA):", "TABLE", JOptionPane.PLAIN_MESSAGE);
                if (schemaOtabla == null || schemaOtabla.trim().isEmpty()) return;
            }

            // Pedimos el nombre del archivo .dmp
            String dump = JOptionPane.showInputDialog(this, "Ingrese nombre del archivo .dmp (ej: backup_full_2025-11-01_15-30.dmp):", "RESTORE", JOptionPane.PLAIN_MESSAGE);
            if (dump == null || dump.trim().isEmpty()) return;

            String dumpTrim = dump.trim();
            String schemaTrim = (schemaOtabla != null) ? schemaOtabla.trim() : null;

            SwingUtilities.invokeLater(() -> {
                boolean ok = br.restaurarBackup(tipo, schemaTrim, dumpTrim);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "✅ Restauración ejecutada (revisar logs).", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Falló restauración. Revisa consola y logs.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        });


        JButton btnVerLogs = crearBoton("Abrir carpeta de backups / logs");
        btnVerLogs.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new java.io.File(System.getProperty("user.dir") + "\\resources\\backups"));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo abrir la carpeta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(btnFull);
        panel.add(btnSchema);
        panel.add(btnTabla);
        panel.add(btnRestore);
        panel.add(btnVerLogs);

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(new Color(200, 50, 50));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegresar.addActionListener(e -> {
            dispose();
            new ui.VentanaPrincipal().setVisible(true);
        });

        add(panel, BorderLayout.CENTER);
        add(btnRegresar, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(new Color(3, 73, 145));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }
}
