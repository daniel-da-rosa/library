package shokunin.group.com.biblioteca.exceptions.repository;

public class FalhaPersistenciaException extends RepositoryException {
    public FalhaPersistenciaException(String entidade,Throwable cause) {

        super(
                "REPO-500",
                "SALVAR",
                String.format("Falha a persistir  %s no banco de dados", entidade),
                cause
                );
    }
}
