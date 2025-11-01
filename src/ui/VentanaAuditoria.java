package ui;

import modulos.Auditoria;
import Modelos.RegistroAuditoria;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaAuditoria extends JFrame {

    private JTable tablaAuditoria;
    private DefaultTableModel modeloTabla;

    public VentanaAuditoria() {
        setTitle("📋 Auditoría del Sistema - Oracle XE");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔹 Encabezado
        JLabel lblTitulo = new JLabel("Registros de Auditoría", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 🔹 Configurar tabla
        modeloTabla = new DefaultTableModel(
                new String[]{"Usuario", "Operación", "Objeto", "Tipo", "Fecha", "Host", "Terminal"}, 0
        );
        tablaAuditoria = new JTable(modeloTabla);
        tablaAuditoria.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tablaAuditoria);

        // 🔹 Botones
        JButton btnRefrescar = new JButton("🔄 Refrescar");
        JButton btnRegresar = new JButton("⬅️ Regresar");

        // Acción para refrescar los datos
        btnRefrescar.addActionListener(e -> cargarRegistros());

        // Acción para regresar al menú principal
        btnRegresar.addActionListener(e -> {
            dispose(); // cierra la ventana actual
            new VentanaPrincipal().setVisible(true); // abre la ventana principal
        });

        // 🔹 Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnRegresar);

        // 🔹 Añadir todo al frame
        add(lblTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // 🔹 Cargar datos al iniciar
        cargarRegistros();
    }

    private void cargarRegistros() {
        modeloTabla.setRowCount(0); // limpia la tabla
        Auditoria auditoria = new Auditoria();
        List<RegistroAuditoria> registros = auditoria.obtenerRegistros();

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
    }
}
