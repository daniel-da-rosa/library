package shokunin.group.com.biblioteca.exceptions.repository;

public class FalhaAtualizacaoException extends RepositoryException {
    public FalhaAtualizacaoException(String entidade,Object id, Throwable cause) {

      super(
              "REPO-501",
              "ATUALIZAR",
              String.format("Falha ao atualizar %s com ID '%s'",entidade,id),
              cause
      );
    }
}
