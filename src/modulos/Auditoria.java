package modulos;

import conexion.ConexionBD;
import modelos.RegistroAuditoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Auditoria {

    public List<RegistroAuditoria> obtenerRegistros() {
        List<RegistroAuditoria> registros = new ArrayList<>();

        // 🔹 Consulta modificada para manejar objetos nulos (GRANT / REVOKE)
        String sql = """
            SELECT 
                usuario,
                tipo_operacion,
                NVL(objeto, 
                    CASE 
                        WHEN tipo_operacion IN ('GRANT', 'REVOKE') THEN 'ROL O PRIVILEGIO'
                        ELSE 'SIN OBJETO'
                    END
                ) AS objeto,
                tipo_objeto,
                fecha,
                host,
                terminal
            FROM bitacora_auditoria
            ORDER BY fecha DESC
        """;

        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                registros.add(new RegistroAuditoria(
                        rs.getString("USUARIO"),
                        rs.getString("TIPO_OPERACION"),
                        rs.getString("OBJETO"),
                        rs.getString("TIPO_OBJETO"),
                        rs.getTimestamp("FECHA"),
                        rs.getString("HOST"),
                        rs.getString("TERMINAL")
                ));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al consultar auditoría: " + e.getMessage());
        }

        return registros;
    }
}
