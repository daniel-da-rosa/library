package shokunin.group.com.biblioteca.repository.contracts;

import shokunin.group.com.biblioteca.domain.emprestimos.Emprestimo;

import java.util.Optional;

public interface IEmprestimoRepository {
    Integer criarEmprestimo(Emprestimo emprestimo);
    void alterarEmprestimo(Emprestimo emprestimo);
    Optional<Emprestimo> buscarEmprestimoPorId(Integer id);
}
