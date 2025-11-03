package modulos;

import conexion.ConexionBD;
import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Performance {

    private Connection conn;

    public Performance() throws SQLException {
        conn = ConexionBD.conectar();
    }

    // ============================================================
    // 🔹 1. TUNNING DE CONSULTAS
    // ============================================================

    /** Analiza el plan de ejecución de una sentencia SQL */
    public void analizarConsulta(String sql) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM plan_table");
            stmt.executeUpdate("EXPLAIN PLAN FOR " + sql);

            ResultSet rs = stmt.executeQuery(
                    "SELECT LPAD(' ', LEVEL-1) || OPERATION || ' (' || OPTIONS || ')' AS OPERACION, " +
                            "OBJECT_NAME AS OBJETO FROM plan_table " +
                            "START WITH ID = 0 CONNECT BY PRIOR ID = PARENT_ID"
            );

            System.out.println("\n🔍 PLAN DE EJECUCIÓN:");
            while (rs.next()) {
                System.out.println(rs.getString("OPERACION") + " → " + rs.getString("OBJETO"));
            }

            registrarLog("Analizado plan de ejecución para: " + sql);

        } catch (SQLException e) {
            System.err.println("⚠️ Error al analizar consulta: " + e.getMessage());
        }
    }

    /** Crea un índice sobre una tabla y columna */
    public void crearIndice(String tabla, String columna) {
        String sql = "CREATE INDEX IDX_" + columna + " ON " + tabla + "(" + columna + ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("✅ Índice creado: " + sql);
            registrarLog("Índice creado: " + sql);
        } catch (SQLException e) {
            System.err.println("⚠️ Error al crear índice: " + e.getMessage());
        }
    }

    /** Actualiza las estadísticas de una tabla */
    public void actualizarEstadisticas(String tabla) {
        String sql = "ANALYZE TABLE " + tabla + " COMPUTE STATISTICS";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("📊 Estadísticas actualizadas para: " + tabla);
            registrarLog("Estadísticas actualizadas: " + tabla);
        } catch (SQLException e) {
            System.err.println("⚠️ Error al actualizar estadísticas: " + e.getMessage());
        }
    }

    // ============================================================
    // 🔹 2. PERFORMANCE GENERAL DE LA BASE DE DATOS
    // ============================================================

    public String mostrarPerformanceBD() {
        StringBuilder sb = new StringBuilder();

        try (Statement stmt = conn.createStatement()) {
            sb.append("📊 ESTADÍSTICAS DE LA BASE DE DATOS\n");
            sb.append("------------------------------------\n");

            // 1️⃣ Sesiones activas
            ResultSet rs1 = stmt.executeQuery(
                    "SELECT COUNT(*) AS sesiones_activas FROM v$session WHERE status = 'ACTIVE'"
            );
            if (rs1.next()) {
                sb.append("Sesiones activas: ").append(rs1.getInt("sesiones_activas")).append("\n");
            }

            // 2️⃣ Buffer Cache
            ResultSet rs2 = stmt.executeQuery(
                    "SELECT ROUND(value/1024/1024,2) AS buffer_cache_MB FROM v$parameter WHERE name='db_cache_size'"
            );
            if (rs2.next()) {
                sb.append("Buffer Cache: ").append(rs2.getDouble("buffer_cache_MB")).append(" MB\n");
            }

            // 3️⃣ Actividad reciente
            sb.append("\n🔹 Actividad reciente:\n");
            ResultSet rs3 = stmt.executeQuery(
                    "SELECT name, value FROM v$sysstat WHERE name IN ('parse count (total)', 'execute count', 'user commits')"
            );
            while (rs3.next()) {
                sb.append(String.format("%-25s %10d%n", rs3.getString("name"), rs3.getInt("value")));
            }

            // 4️⃣ Espacio libre
            sb.append("\n💾 Espacio libre por Tablespace:\n");
            ResultSet rs4 = stmt.executeQuery(
                    "SELECT tablespace_name, ROUND(SUM(bytes)/1024/1024,2) AS MB_LIBRES " +
                            "FROM dba_free_space GROUP BY tablespace_name"
            );
            while (rs4.next()) {
                sb.append(String.format("%-20s %10.2f MB%n",
                        rs4.getString("tablespace_name"), rs4.getDouble("MB_LIBRES")));
            }

            registrarLog("Consulta de performance general ejecutada.");
        } catch (SQLException e) {
            sb.append("⚠️ Error al obtener métricas: ").append(e.getMessage()).append("\n");
        }

        return sb.toString();
    }


    // ============================================================
    // 🔹 UTILIDAD: Registrar log de acciones
    // ============================================================

    private void registrarLog(String mensaje) {
        try {
            java.nio.file.Path logDir = java.nio.file.Paths.get("resources/logs");
            if (!java.nio.file.Files.exists(logDir)) {
                java.nio.file.Files.createDirectories(logDir);
            }

            try (FileWriter fw = new FileWriter("resources/logs/performance.log", true)) {
                fw.write(LocalDateTime.now() + " - " + mensaje + "\n");
            }
        } catch (IOException e) {
            System.err.println("⚠️ No se pudo crear el log: " + e.getMessage());
        }
    }

}
