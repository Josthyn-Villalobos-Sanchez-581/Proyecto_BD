package modulos;

public class OperacionResultado {
    private boolean exito;
    private String mensaje;

    public OperacionResultado(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }
}
