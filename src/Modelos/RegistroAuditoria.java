package modelos;

import java.sql.Timestamp;

public class RegistroAuditoria {
    private String usuario;
    private String tipoOperacion;
    private String objeto;
    private String tipoObjeto;
    private Timestamp fecha;
    private String host;
    private String terminal;

    public RegistroAuditoria(String usuario, String tipoOperacion, String objeto,
                             String tipoObjeto, Timestamp fecha, String host, String terminal) {
        this.usuario = usuario;
        this.tipoOperacion = tipoOperacion;
        this.objeto = objeto;
        this.tipoObjeto = tipoObjeto;
        this.fecha = fecha;
        this.host = host;
        this.terminal = terminal;
    }

    // Getters
    public String getUsuario() { return usuario; }
    public String getTipoOperacion() { return tipoOperacion; }
    public String getObjeto() { return objeto; }
    public String getTipoObjeto() { return tipoObjeto; }
    public Timestamp getFecha() { return fecha; }
    public String getHost() { return host; }
    public String getTerminal() { return terminal; }
}
