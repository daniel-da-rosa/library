package shokunin.group.com.biblioteca.exceptions.repository;

public class ConexaoBancoDadosException extends RepositoryException {
    public ConexaoBancoDadosException(Throwable cause) {

        super(
                "REPO-DB-001",
                "CONEXAO",
                "Falha ao obter conexão com o banco de dados",
                cause
        );
    }
}
