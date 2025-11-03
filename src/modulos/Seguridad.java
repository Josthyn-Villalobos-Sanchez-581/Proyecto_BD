package modulos;

import conexion.ConexionBD;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;


public class Seguridad {

    /* ===========================================================
       🔹 UTILIDADES DE VALIDACIÓN
       =========================================================== */

    private boolean existeUsuario(Connection conn, String usuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM DBA_USERS WHERE USERNAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private boolean existeRol(Connection conn, String rol) throws SQLException {
        String sql = "SELECT COUNT(*) FROM DBA_ROLES WHERE ROLE = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private boolean existeTablespace(Connection conn, String ts) throws SQLException {
        String sql = "SELECT COUNT(*) FROM DBA_TABLESPACES WHERE TABLESPACE_NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ts.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    /* ===========================================================
       🔹 CREAR / ELIMINAR USUARIO
       =========================================================== */

    public OperacionResultado crearUsuario(String usuario, String contrasena) {
        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement()) {

            if (existeUsuario(conn, usuario)) {
                return new OperacionResultado(false, "El usuario '" + usuario + "' ya existe.");
            }

            st.execute("CREATE USER " + usuario + " IDENTIFIED BY " + contrasena);

            if (existeTablespace(conn, "USERS")) {
                st.execute("ALTER USER " + usuario + " DEFAULT TABLESPACE USERS");
            }
            if (existeTablespace(conn, "TEMP")) {
                st.execute("ALTER USER " + usuario + " TEMPORARY TABLESPACE TEMP");
            }

            return new OperacionResultado(true, "Usuario '" + usuario + "' creado correctamente.");

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al crear usuario: " + e.getMessage());
        }
    }

    public OperacionResultado borrarUsuario(String usuario) {
        try (Connection conn = ConexionBD.conectar()) {
            if (!existeUsuario(conn, usuario)) {
                return new OperacionResultado(false, "No se puede eliminar: el usuario '" + usuario + "' no existe.");
            }

            String sql = "DROP USER " + usuario + " CASCADE";
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
                return new OperacionResultado(true, "Usuario '" + usuario + "' eliminado correctamente.");
            }

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al eliminar usuario: " + e.getMessage());
        }
    }

    /* ===========================================================
       🔹 CREAR / ELIMINAR ROL
       =========================================================== */

    public OperacionResultado crearRol(String nombreRol) {
        try (Connection conn = ConexionBD.conectar()) {
            if (existeRol(conn, nombreRol)) {
                return new OperacionResultado(false, "El rol '" + nombreRol + "' ya existe.");
            }

            String sql = "CREATE ROLE " + nombreRol;
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
                return new OperacionResultado(true, "Rol '" + nombreRol + "' creado correctamente.");
            }

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al crear rol: " + e.getMessage());
        }
    }

    public OperacionResultado eliminarRol(String nombreRol) {
        try (Connection conn = ConexionBD.conectar()) {
            if (!existeRol(conn, nombreRol)) {
                return new OperacionResultado(false, "No se puede eliminar: el rol '" + nombreRol + "' no existe.");
            }

            String sql = "DROP ROLE " + nombreRol;
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
                return new OperacionResultado(true, "Rol '" + nombreRol + "' eliminado correctamente.");
            }

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al eliminar rol: " + e.getMessage());
        }
    }

    /* ===========================================================
       🔹 ASIGNAR / REVOCAR ROLES Y PRIVILEGIOS
       =========================================================== */

    public OperacionResultado asignarRolAUsuario(String usuario, String rol) {
        try (Connection conn = ConexionBD.conectar()) {
            if (!existeUsuario(conn, usuario)) {
                return new OperacionResultado(false, "El usuario '" + usuario + "' no existe.");
            }
            if (!existeRol(conn, rol)) {
                return new OperacionResultado(false, "El rol '" + rol + "' no existe.");
            }

            String sql = "GRANT " + rol + " TO " + usuario;
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
                return new OperacionResultado(true, "Rol '" + rol + "' asignado al usuario '" + usuario + "'.");
            }

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al asignar rol: " + e.getMessage());
        }
    }

    public OperacionResultado revocarRol(String usuario, String rol) {
        try (Connection conn = ConexionBD.conectar()) {
            if (!existeUsuario(conn, usuario)) {
                return new OperacionResultado(false, "El usuario '" + usuario + "' no existe.");
            }
            if (!existeRol(conn, rol)) {
                return new OperacionResultado(false, "El rol '" + rol + "' no existe.");
            }

            String sql = "REVOKE " + rol + " FROM " + usuario;
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
                return new OperacionResultado(true, "Rol '" + rol + "' revocado del usuario '" + usuario + "'.");
            }

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al revocar rol: " + e.getMessage());
        }
    }

    public OperacionResultado asignarPrivilegioARol(String rol, String privilegio, String tabla) {
        try (Connection conn = ConexionBD.conectar()) {
            if (!existeRol(conn, rol)) {
                return new OperacionResultado(false, "El rol '" + rol + "' no existe.");
            }

            String sql = "GRANT " + privilegio + " ON " + tabla + " TO " + rol;
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
                return new OperacionResultado(true, "Privilegio '" + privilegio + "' asignado al rol '" + rol + "'.");
            }

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al asignar privilegio a rol: " + e.getMessage());
        }
    }

    public OperacionResultado asignarPrivilegiosDeSchema(String usuario, List<String> privilegiosSeleccionados) {
        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement()) {

            if (!existeUsuario(conn, usuario)) {
                return new OperacionResultado(false, "El usuario '" + usuario + "' no existe.");
            }

            if (privilegiosSeleccionados == null || privilegiosSeleccionados.isEmpty()) {
                return new OperacionResultado(false, "No se seleccionaron privilegios para asignar.");
            }

            for (String privilegio : privilegiosSeleccionados) {
                try {
                    st.execute("GRANT " + privilegio + " TO " + usuario);
                    System.out.println("✅ Privilegio otorgado: " + privilegio + " a " + usuario);
                } catch (SQLException e) {
                    System.out.println("⚠ Error al otorgar privilegio " + privilegio + ": " + e.getMessage());
                }
            }

            return new OperacionResultado(true,
                    "Privilegios asignados correctamente al usuario '" + usuario + "'.");

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al asignar privilegios: " + e.getMessage());
        }
    }



    public OperacionResultado revocarPrivilegioTabla(String usuario, String tabla, String privilegio) {
        try (Connection conn = ConexionBD.conectar()) {
            if (!existeUsuario(conn, usuario)) {
                return new OperacionResultado(false, "El usuario '" + usuario + "' no existe.");
            }

            String sql = "REVOKE " + privilegio + " ON " + tabla + " FROM " + usuario;
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
                return new OperacionResultado(true, "Privilegio '" + privilegio + "' revocado de '" + usuario + "'.");
            }

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al revocar privilegio: " + e.getMessage());
        }
    }

    public OperacionResultado revocarPrivilegioDeRol(String rol, String tabla, String privilegio) {
        try (Connection conn = ConexionBD.conectar()) {
            if (!existeRol(conn, rol)) {
                return new OperacionResultado(false, "El rol '" + rol + "' no existe.");
            }

            String sql = "REVOKE " + privilegio + " ON " + tabla + " FROM " + rol;
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
                return new OperacionResultado(true, "Privilegio '" + privilegio + "' revocado del rol '" + rol + "'.");
            }

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al revocar privilegio de rol: " + e.getMessage());
        }
    }

    /* ===========================================================
       🔹 ASIGNAR TABLESPACE A USUARIO (con validaciones)
       =========================================================== */

    public OperacionResultado asignarTablespaceAUsuario(String usuario, String tablespace, boolean temporal) {
        String tipo = temporal ? "TEMPORARY" : "DEFAULT";
        try (Connection conn = ConexionBD.conectar()) {
            if (!existeUsuario(conn, usuario)) {
                return new OperacionResultado(false, "El usuario '" + usuario + "' no existe.");
            }
            if (!existeTablespace(conn, tablespace)) {
                return new OperacionResultado(false, "El tablespace '" + tablespace + "' no existe.");
            }

            String sql = "ALTER USER " + usuario + " " + tipo + " TABLESPACE " + tablespace;
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
                return new OperacionResultado(true, "Tablespace '" + tablespace + "' asignado al usuario '" + usuario + "'.");
            }

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al asignar tablespace: " + e.getMessage());
        }
    }
}
