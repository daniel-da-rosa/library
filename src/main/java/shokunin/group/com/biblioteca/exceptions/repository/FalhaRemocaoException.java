package shokunin.group.com.biblioteca.exceptions.repository;

public class FalhaRemocaoException extends RepositoryException {
    public FalhaRemocaoException(String entidade, Object id, Throwable cause) {

        super("REPO-502",
                "REMOVER",
                String.format("Falha ao remover %s com ID %s",entidade,id),
                cause);
    }
}
