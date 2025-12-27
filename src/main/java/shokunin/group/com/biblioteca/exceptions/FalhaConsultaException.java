package shokunin.group.com.biblioteca.exceptions;

public class FalhaConsultaException extends RepositoryException {
    public FalhaConsultaException(String entidade,Throwable cause) {

        super("REPO-503",
                "CONSULTAR",
                String.format("Falha ao consultar %s",entidade),
                cause
        );
    }

    public FalhaConsultaException(String descricaoConsulta,Throwable cause, boolean detalhe ){
        super(
                "REPO-503",
                "CONSULTAR",
                String.format("Falha a consultar: %s",descricaoConsulta),
                cause
        );
    }
}
