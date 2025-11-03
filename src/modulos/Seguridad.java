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

    public OperacionResultado asignarPrivilegiosDeSchemaARol(String rol, String schema, List<String> privilegiosSeleccionados) {
        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement()) {

            // Validar existencia del rol
            if (!existeRol(conn, rol)) {
                return new OperacionResultado(false, "El rol '" + rol + "' no existe.");
            }

            // Validar que haya privilegios
            if (privilegiosSeleccionados == null || privilegiosSeleccionados.isEmpty()) {
                return new OperacionResultado(false, "No se seleccionaron privilegios para asignar.");
            }

            // Si hay un schema seleccionado, lo mostramos como referencia (no afecta el GRANT)
            String schemaMsg = (schema != null && !schema.isEmpty())
                    ? " sobre el schema '" + schema + "'"
                    : "";

            // Otorgar todos los privilegios seleccionados
            for (String privilegio : privilegiosSeleccionados) {
                try {
                    String sql = "GRANT " + privilegio + " TO " + rol;
                    st.execute(sql);
                    System.out.println("✅ Privilegio otorgado: " + privilegio + " al rol " + rol + schemaMsg);
                } catch (SQLException e) {
                    System.out.println("⚠ Error al otorgar privilegio " + privilegio + ": " + e.getMessage());
                }
            }

            return new OperacionResultado(true,
                    "Privilegios asignados correctamente al rol '" + rol + "'" + schemaMsg + ".");

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al asignar privilegios al rol: " + e.getMessage());
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



    public OperacionResultado revocarPrivilegiosDeSchema(String usuario, String schema, List<String> privilegiosSeleccionados) {
        List<String> revocados = new ArrayList<>();
        List<String> noAsignados = new ArrayList<>();

        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement()) {

            if (!existeUsuario(conn, usuario)) {
                return new OperacionResultado(false, "El usuario '" + usuario + "' no existe.");
            }

            if (privilegiosSeleccionados == null || privilegiosSeleccionados.isEmpty()) {
                return new OperacionResultado(false, "No se seleccionaron privilegios para revocar.");
            }

            String schemaMsg = (schema != null && !schema.isEmpty())
                    ? " sobre el schema '" + schema + "'"
                    : "";

            for (String privilegio : privilegiosSeleccionados) {
                try {
                    st.execute("REVOKE " + privilegio + " FROM " + usuario);
                    revocados.add(privilegio);
                    System.out.println("✅ Privilegio revocado: " + privilegio + " del usuario " + usuario + schemaMsg);
                } catch (SQLException e) {
                    // ORA-01927 → Privilege not granted
                    if (e.getMessage().contains("ORA-01927")) {
                        System.out.println("ℹ El privilegio " + privilegio + " no estaba asignado al usuario " + usuario);
                        noAsignados.add(privilegio);
                    } else {
                        System.out.println("⚠ Error al revocar " + privilegio + ": " + e.getMessage());
                    }
                }
            }

            // Construir mensaje final
            StringBuilder mensaje = new StringBuilder("Resultado de la revocación de privilegios para '")
                    .append(usuario).append("'").append(schemaMsg).append(":\n\n");

            if (!revocados.isEmpty()) {
                mensaje.append("✅ Revocados correctamente:\n  - ")
                        .append(String.join("\n  - ", revocados))
                        .append("\n\n");
            }

            if (!noAsignados.isEmpty()) {
                mensaje.append("ℹ No estaban asignados:\n  - ")
                        .append(String.join("\n  - ", noAsignados))
                        .append("\n");
            }

            if (revocados.isEmpty() && noAsignados.isEmpty()) {
                mensaje.append("⚠ No se pudo revocar ningún privilegio.");
            }

            return new OperacionResultado(true, mensaje.toString());

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al revocar privilegios: " + e.getMessage());
        }
    }



    public OperacionResultado revocarPrivilegiosDeSchemaARol(String rol, String schema, List<String> privilegiosSeleccionados) {
        List<String> revocados = new ArrayList<>();
        List<String> noAsignados = new ArrayList<>();

        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement()) {

            // Validar que el rol exista
            if (!existeRol(conn, rol)) {
                return new OperacionResultado(false, "El rol '" + rol + "' no existe.");
            }

            if (privilegiosSeleccionados == null || privilegiosSeleccionados.isEmpty()) {
                return new OperacionResultado(false, "No se seleccionaron privilegios para revocar.");
            }

            String schemaMsg = (schema != null && !schema.isEmpty())
                    ? " sobre el schema '" + schema + "'"
                    : "";

            for (String privilegio : privilegiosSeleccionados) {
                try {
                    // Ejecutar la revocación
                    String sql = "REVOKE " + privilegio + " FROM " + rol;
                    st.execute(sql);
                    revocados.add(privilegio);
                    System.out.println("✅ Privilegio revocado: " + privilegio + " del rol " + rol + schemaMsg);

                } catch (SQLException e) {
                    // Detectar si el privilegio no estaba asignado
                    if (e.getMessage().contains("ORA-01952") || e.getMessage().contains("ORA-01927")) {
                        System.out.println("ℹ El privilegio " + privilegio + " no estaba asignado al rol " + rol);
                        noAsignados.add(privilegio);
                    } else {
                        System.out.println("⚠ Error al revocar " + privilegio + ": " + e.getMessage());
                    }
                }
            }

            // Crear mensaje final
            StringBuilder mensaje = new StringBuilder("Resultado de la revocación de privilegios para el rol '")
                    .append(rol).append("'").append(schemaMsg).append(":\n\n");

            if (!revocados.isEmpty()) {
                mensaje.append("✅ Revocados correctamente:\n  - ")
                        .append(String.join("\n  - ", revocados))
                        .append("\n\n");
            }

            if (!noAsignados.isEmpty()) {
                mensaje.append("ℹ No estaban asignados:\n  - ")
                        .append(String.join("\n  - ", noAsignados))
                        .append("\n");
            }

            if (revocados.isEmpty() && noAsignados.isEmpty()) {
                mensaje.append("⚠ No se pudo revocar ningún privilegio.");
            }

            return new OperacionResultado(true, mensaje.toString());

        } catch (SQLException e) {
            return new OperacionResultado(false, "Error al revocar privilegios del rol: " + e.getMessage());
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
