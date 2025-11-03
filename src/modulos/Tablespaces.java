package modulos;

import conexion.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Tablespaces {

    /* ===========================
       Métodos públicos (API)
       =========================== */

    // Crear un tablespace nuevo con un datafile
    public boolean crearTablespace(String nombre, String datafilePath, int sizeMB) {
        String sql = "CREATE TABLESPACE " + nombre +
                " DATAFILE '" + datafilePath + "' SIZE " + sizeMB + "M";
        return ejecutarComando(sql, "Tablespace '" + nombre + "' creado correctamente.");
    }

    public boolean crearTemporaryTablespace(String nombre, String rutaDatafile, int tamanoMB) {
        String sql = "CREATE TEMPORARY TABLESPACE " + nombre +
                " TEMPFILE '" + rutaDatafile + "' SIZE " + tamanoMB + "M "
                + "AUTOEXTEND ON NEXT 5M MAXSIZE UNLIMITED";

        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement()) {

            st.execute(sql);
            System.out.println("✅ Temporary Tablespace '" + nombre + "' creado correctamente.");
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error al crear temp tablespace: " + e.getMessage());
            return false;
        }
    }

    // Agregar un datafile a un tablespace existente
    public boolean agregarDatafile(String tablespace, String datafilePath, int sizeMB) {
        String sql = "ALTER TABLESPACE " + tablespace +
                " ADD DATAFILE '" + datafilePath + "' SIZE " + sizeMB + "M";
        return ejecutarComando(sql, "Datafile agregado al tablespace '" + tablespace + "'.");
    }

    // Redimensionar un datafile existente (nuevaTamanio en MB)
    public boolean redimensionarDatafile(String datafilePath, int nuevoSizeMB) {
        String sql = "ALTER DATABASE DATAFILE '" + datafilePath + "' RESIZE " + nuevoSizeMB + "M";
        return ejecutarComando(sql, "Datafile redimensionado: " + datafilePath);
    }

    // Eliminar tablespace (incluye contenidos y borra datafiles)
    public boolean eliminarTablespace(String nombre) {
        String sql = "DROP TABLESPACE " + nombre + " INCLUDING CONTENTS AND DATAFILES";
        return ejecutarComando(sql, "Tablespace '" + nombre + "' eliminado correctamente.");
    }

    // Listar tablespaces (info básica)
    public List<String> listarTablespaces() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT TABLESPACE_NAME, STATUS, CONTENTS FROM DBA_TABLESPACES ORDER BY TABLESPACE_NAME";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String row = String.format("%s | Estado: %s | Tipo: %s",
                        rs.getString("TABLESPACE_NAME"),
                        rs.getString("STATUS"),
                        rs.getString("CONTENTS"));
                lista.add(row);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar tablespaces: " + e.getMessage());
        }
        return lista;
    }

    // Listar datafiles con tamaños por tablespace
    public List<String> listarDataFiles() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT FILE_NAME, TABLESPACE_NAME, BYTES, AUTOEXTENSIBLE FROM DBA_DATA_FILES ORDER BY TABLESPACE_NAME";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long bytes = rs.getLong("BYTES");
                // Convertir bytes a MB (aprox)
                long mb = bytes / (1024 * 1024);
                String row = String.format("TS: %s | File: %s | Size(MB): %d | Autoextensible: %s",
                        rs.getString("TABLESPACE_NAME"),
                        rs.getString("FILE_NAME"),
                        mb,
                        rs.getString("AUTOEXTENSIBLE"));
                lista.add(row);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar datafiles: " + e.getMessage());
        }
        return lista;
    }

    // Verifica si un tablespace existe (útil antes de crear/eliminar)
    public boolean existeTablespace(String nombre) {
        String sql = "SELECT COUNT(*) AS CNT FROM DBA_TABLESPACES WHERE TABLESPACE_NAME = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CNT") > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error en existeTablespace(): " + e.getMessage());
        }
        return false;
    }

    /* ===========================
       Helper central: ejecutarComando
       Misma estructura de manejo de excepciones que Seguridad.java
       =========================== */

    private boolean ejecutarComando(String sql, String mensajeExito) {
        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement()) {

            st.execute(sql);
            System.out.println("✅ " + mensajeExito);
            return true;

        } catch (SQLSyntaxErrorException e) {
            System.out.println("❌ Error de sintaxis SQL: " + e.getMessage());
        } catch (SQLInvalidAuthorizationSpecException e) {
            System.out.println("🚫 Error de autorización o privilegios insuficientes: " + e.getMessage());
            System.out.println("👉 Recuerda que para operaciones sobre tablespaces necesitas conectar como SYS AS SYSDBA o un usuario con privilegios DBA.");
        } catch (SQLRecoverableException e) {
            System.out.println("⚠ Error recuperable (posible desconexión): " + e.getMessage());
        } catch (SQLTimeoutException e) {
            System.out.println("⏳ Tiempo de espera excedido al ejecutar comando: " + e.getMessage());
        } catch (SQLException e) {
            // Errores Oracle típicos (por ejemplo ORA-xxx)
            System.out.println("❌ Error SQL general: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("⚠ Error inesperado en ejecutarComando(): " + e.getMessage());
        }
        return false;
    }
}
