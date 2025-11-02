package modulos;

import conexion.ConexionBD;

import java.io.*;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

public class BackupRestore {

    private static final String BACKUP_DIR_NAME = "resources/backups";
    private static final String ORACLE_DIRECTORY_NAME = "BACKUP_DIR";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    /**
     * Asegura que exista la carpeta local para backups y devuelve la ruta absoluta.
     */
    public String ensureLocalBackupDir() {
        String projectDir = System.getProperty("user.dir");
        Path backupPath = Paths.get(projectDir, BACKUP_DIR_NAME);
        try {
            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
                System.out.println("✅ Carpeta de backups creada en: " + backupPath.toAbsolutePath());
            } else {
                System.out.println("📁 Carpeta de backups (ya existe): " + backupPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("❌ No se pudo crear la carpeta de backups: " + e.getMessage());
        }
        return backupPath.toAbsolutePath().toString();
    }

    /**
     * Crea o reemplaza el objeto DIRECTORY en Oracle apuntando a la carpeta de backups.
     * Debe ejecutarse con un usuario con privilegios (SYS en tu caso).
     */
    public boolean createOracleDirectoryObject() {
        String absPath = ensureLocalBackupDir();
        String sqlCreate = "CREATE OR REPLACE DIRECTORY " + ORACLE_DIRECTORY_NAME + " AS '" + absPath.replace("\\", "/") + "'";
        String sqlGrant = "GRANT READ, WRITE ON DIRECTORY " + ORACLE_DIRECTORY_NAME + " TO PUBLIC"; // puedes cambiar PUBLIC por usuario específico

        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement()) {

            st.execute(sqlCreate);
            st.execute(sqlGrant);
            System.out.println("✅ Objeto DIRECTORY '" + ORACLE_DIRECTORY_NAME + "' creado y otorgados permisos.");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error al crear DIRECTORY en Oracle: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ejecuta un comando del sistema y captura la salida. Retorna true si exitCode == 0.
     */
    private boolean ejecutarComando(List<String> comando, File workingDir) {
        try {
            ProcessBuilder pb = new ProcessBuilder(comando);
            if (workingDir != null) pb.directory(workingDir);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            // Capturar salida y mostrarla
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exit = p.waitFor();
            System.out.println("Proceso finalizado con código: " + exit);
            return exit == 0;
        } catch (Exception e) {
            System.out.println("❌ Error al ejecutar comando: " + e.getMessage());
            return false;
        }
    }

    /**
     * Realiza un backup FULL usando expdp.
     * Devuelve la ruta del archivo dmp generado o null si falló.
     */
    public String realizarBackupFull() {
        if (!createOracleDirectoryObject()) return null;

        String timestamp = SDF.format(new Date());
        String dumpFile = "backup_full_" + timestamp + ".dmp";
        String logFile = "backup_full_" + timestamp + ".log";

        // Nota: ajustar la cadena de conexión si tu expdp requiere forma especial.
        // Aquí se asume que expdp está en el PATH del sistema y que Oracle está en la misma máquina.
        String connect = "miadmin/rootroot@localhost:1521/XEPDB1";


        List<String> comando = Arrays.asList(
                "expdp",
                connect,
                "full=Y",
                "directory=" + ORACLE_DIRECTORY_NAME,
                "dumpfile=" + dumpFile,
                "logfile=" + logFile
        );

        System.out.println("▶ Ejecutando expdp (FULL): " + String.join(" ", comando));
        boolean ok = ejecutarComando(comando, null);

        String fullPath = Paths.get(System.getProperty("user.dir"), BACKUP_DIR_NAME, dumpFile).toString();
        if (ok && Files.exists(Paths.get(fullPath))) {
            System.out.println("✅ Backup FULL creado: " + fullPath);
            return fullPath;
        } else {
            System.out.println("❌ Falló la creación del backup FULL. Revisa la salida del expdp.");
            return null;
        }
    }

    /**
     * Realiza backup por esquema usando expdp (schemas=).
     */
    public String realizarBackupPorSchema(String schema) {
        if (schema == null || schema.trim().isEmpty()) {
            System.out.println("❌ El nombre de schema no puede estar vacío.");
            return null;
        }
        if (!createOracleDirectoryObject()) return null;

        String timestamp = SDF.format(new Date());
        String dumpFile = "backup_schema_" + schema + "_" + timestamp + ".dmp";
        String logFile = "backup_schema_" + schema + "_" + timestamp + ".log";
        String connect = "miadmin/rootroot@localhost:1521/XEPDB1";


        List<String> comando = Arrays.asList(
                "expdp",
                connect,
                "schemas=" + schema,
                "directory=" + ORACLE_DIRECTORY_NAME,
                "dumpfile=" + dumpFile,
                "logfile=" + logFile
        );

        System.out.println("▶ Ejecutando expdp (SCHEMA): " + String.join(" ", comando));
        boolean ok = ejecutarComando(comando, null);

        String fullPath = Paths.get(System.getProperty("user.dir"), BACKUP_DIR_NAME, dumpFile).toString();
        if (ok && Files.exists(Paths.get(fullPath))) {
            System.out.println("✅ Backup de schema creado: " + fullPath);
            return fullPath;
        } else {
            System.out.println("❌ Falló la creación del backup por schema. Revisa la salida del expdp.");
            return null;
        }
    }

    /**
     * Realiza backup por tabla usando expdp (tables=).
     * table must be like SCHEMA.TABLE or pass schema separately.
     */
    public String realizarBackupPorTabla(String schema, String tabla) {
        if (tabla == null || tabla.trim().isEmpty()) {
            System.out.println("❌ El nombre de tabla no puede estar vacío.");
            return null;
        }
        if (!createOracleDirectoryObject()) return null;

        String timestamp = SDF.format(new Date());
        String dumpFile = "backup_tabla_" + schema + "_" + tabla + "_" + timestamp + ".dmp";
        String logFile = "backup_tabla_" + schema + "_" + tabla + "_" + timestamp + ".log";
        String connect = "miadmin/rootroot@localhost:1521/XEPDB1";


        String tablesParam = schema + "." + tabla;
        List<String> comando = Arrays.asList(
                "expdp",
                connect,
                "tables=" + tablesParam,
                "directory=" + ORACLE_DIRECTORY_NAME,
                "dumpfile=" + dumpFile,
                "logfile=" + logFile
        );

        System.out.println("▶ Ejecutando expdp (TABLE): " + String.join(" ", comando));
        boolean ok = ejecutarComando(comando, null);

        String fullPath = Paths.get(System.getProperty("user.dir"), BACKUP_DIR_NAME, dumpFile).toString();
        if (ok && Files.exists(Paths.get(fullPath))) {
            System.out.println("✅ Backup de tabla creado: " + fullPath);
            return fullPath;
        } else {
            System.out.println("❌ Falló la creación del backup por tabla. Revisa la salida del expdp.");
            return null;
        }
    }

    /**
     * Restaura un dump usando impdp. 'dumpFullPath' debe ser un archivo dentro del DIRECTORY Oracle.
     */
    public boolean restaurarBackup(String tipoBackup, String schemaOtabla, String dumpFileName) {
        if (tipoBackup == null || dumpFileName == null || dumpFileName.trim().isEmpty()) {
            System.out.println("❌ Tipo de backup o archivo inválido.");
            return false;
        }

        Path localPath = Paths.get(System.getProperty("user.dir"), BACKUP_DIR_NAME, dumpFileName);
        if (!Files.exists(localPath)) {
            System.out.println("❌ El archivo " + localPath + " no existe.");
            return false;
        }

        String connect = "miadmin/rootroot@localhost:1521/XEPDB1"; // sin AS SYSDBA
        String logFile = "restore_" + dumpFileName.replace(".dmp", "") + ".log";

        List<String> comando = new ArrayList<>();
        comando.add("impdp");
        comando.add(connect);
        comando.add("directory=" + ORACLE_DIRECTORY_NAME);
        comando.add("dumpfile=" + dumpFileName);
        comando.add("logfile=" + logFile);

        switch (tipoBackup.toUpperCase()) {
            case "FULL":
                comando.add("full=Y");
                break;
            case "SCHEMA":
                if (schemaOtabla == null || schemaOtabla.trim().isEmpty()) {
                    System.out.println("❌ Para SCHEMA debes especificar el nombre del schema.");
                    return false;
                }
                comando.add("schemas=" + schemaOtabla);
                break;
            case "TABLE":
                if (schemaOtabla == null || schemaOtabla.trim().isEmpty() || !schemaOtabla.contains(".")) {
                    System.out.println("❌ Para TABLE debes especificar schema.tabla.");
                    return false;
                }
                comando.add("tables=" + schemaOtabla);
                break;
            default:
                System.out.println("❌ Tipo de backup desconocido: " + tipoBackup);
                return false;
        }

        System.out.println("▶ Ejecutando impdp: " + String.join(" ", comando));
        boolean ok = ejecutarComando(comando, null);

        if (ok) {
            System.out.println("✅ Restauración finalizada (revisa el log para detalles).");
        } else {
            System.out.println("❌ Falló la restauración. Revisa la salida del impdp.");
        }
        return ok;
    }

}
