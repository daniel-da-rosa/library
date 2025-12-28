package shokunin.group.com.biblioteca.exceptions.repository;

public class EntidadeNaoEncontradaException extends RepositoryException {

    public EntidadeNaoEncontradaException(String entidade,Object id) {

        super(
                "REPO -404",
                "BUSCAR",
                String.format("%s com ID '%s' não encontrado(a)",entidade,id)
        );
    }
}
