package modulos;

import conexion.ConexionBD;
import java.sql.*;

public class Seguridad {

    /**
     * 🔹 Crea un nuevo usuario en la base de datos Oracle.
     */
    public void crearUsuario(String usuario, String contrasena) {
        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement()) {

            String sql = "CREATE USER " + usuario + " IDENTIFIED BY " + contrasena;
            st.execute(sql);

            System.out.println("✅ Usuario '" + usuario + "' creado correctamente en la PDB.");

        } catch (SQLException e) {
            if (e.getErrorCode() == 65096) {
                System.out.println("⚠️ Error ORA-65096: El usuario debe crearse dentro de la PDB (XEPDB1).");
                System.out.println("👉 Verifica que la URL de conexión en ConexionBD.java use '/XEPDB1' en lugar de ':XE'.");
            } else {
                System.out.println("❌ Error SQL al crear usuario: " + e.getMessage());
            }
        }
    }


    /**
     * 🔹 Elimina un usuario existente.
     */
    public void borrarUsuario(String usuario) {
        String sql = "DROP USER " + usuario + " CASCADE";
        ejecutarComando(sql, "Usuario '" + usuario + "' eliminado correctamente.");
    }

    //crear un rol nuevo
    public void crearRol(String nombreRol) {
        String sql = "CREATE ROLE " + nombreRol;
        ejecutarComando(sql, "Rol '" + nombreRol + "' creado correctamente.");
    }

    //metodo para asignar permisos a un rol
    public void asignarPrivilegioARol(String rol, String privilegio, String tabla) {
        String sql = "GRANT " + privilegio + " ON " + tabla + " TO " + rol;
        ejecutarComando(sql, "Privilegio '" + privilegio + "' asignado al rol '" + rol + "' sobre tabla '" + tabla + "'.");
    }
    /**
     * 🔹 Asigna un rol o privilegio a un usuario.
     */
    public void asignarRolAUsuario(String usuario, String rol) {
        String sql = "GRANT " + rol + " TO " + usuario;
        ejecutarComando(sql, "Rol '" + rol + "' asignado al usuario '" + usuario + "'.");
    }

    /**
     * 🔹 Revoca un rol o privilegio de un usuario.
     */
    public void revocarRol(String usuario, String rol) {
        String sql = "REVOKE " + rol + " FROM " + usuario;
        ejecutarComando(sql, "Rol '" + rol + "' revocado del usuario '" + usuario + "'.");
    }

    //asignar privilegio a un usuario sobre tabla especifica
    public void asignarPrivilegioTabla(String usuario, String tabla, String privilegio) {
        String sql = "GRANT " + privilegio + " ON " + tabla + " TO " + usuario;
        ejecutarComando(sql, "Privilegio '" + privilegio + "' sobre tabla '" + tabla + "' asignado a '" + usuario + "'.");
    }

    //quitar privilegio a un usuario sobre tabla especifica
    public void revocarPrivilegioTabla(String usuario, String tabla, String privilegio) {
        String sql = "REVOKE " + privilegio + " ON " + tabla + " FROM " + usuario;
        ejecutarComando(sql, "Privilegio '" + privilegio + "' sobre tabla '" + tabla + "' revocado de '" + usuario + "'.");
    }

    // 🔹 Revocar privilegio de un rol sobre una tabla
    public void revocarPrivilegioDeRol(String rol, String tabla, String privilegio) {
        String sql = "REVOKE " + privilegio + " ON " + tabla + " FROM " + rol;
        ejecutarComando(sql, "Privilegio '" + privilegio + "' sobre tabla '" + tabla + "' revocado del rol '" + rol + "'.");
    }
    /**
     * 🔹 Lista todos los usuarios existentes en la base de datos.
     */
    public void listarUsuarios() {
        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT username, account_status FROM dba_users ORDER BY username")) {

            System.out.println("👥 Lista de usuarios:");
            System.out.println("----------------------");

            while (rs.next()) {
                System.out.println(" - " + rs.getString("USERNAME") + " (" + rs.getString("ACCOUNT_STATUS") + ")");
            }

        } catch (SQLSyntaxErrorException e) {
            System.out.println("❌ Error de sintaxis SQL en listarUsuarios(): " + e.getMessage());
        } catch (SQLRecoverableException e) {
            System.out.println("⚠️ Error recuperable (posible desconexión): " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ Error general al listar usuarios: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("⚠️ Error inesperado: " + e.getMessage());
        }
    }

    /**
     * 🔹 Ejecuta comandos SQL (CREATE, DROP, GRANT, REVOKE) con manejo detallado de errores.
     */
    private boolean ejecutarComando(String sql, String mensajeExito) {
        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement()) {

            st.execute(sql);
            System.out.println("✅ " + mensajeExito);
            return true;

        } catch (SQLSyntaxErrorException e) {
            System.out.println("❌ Error de sintaxis SQL: " + e.getMessage());
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠️ Violación de integridad (posible duplicado o dependencia): " + e.getMessage());
        } catch (SQLInvalidAuthorizationSpecException e) {
            System.out.println("🚫 Error de autorización o privilegios insuficientes: " + e.getMessage());
        } catch (SQLRecoverableException e) {
            System.out.println("⚠️ Error de conexión o sesión caducada: " + e.getMessage());
        } catch (SQLTimeoutException e) {
            System.out.println("⏳ Tiempo de espera excedido al ejecutar comando: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ Error SQL general: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("⚠️ Error inesperado en ejecutarComando(): " + e.getMessage());
        }
        return false;
    }
}
