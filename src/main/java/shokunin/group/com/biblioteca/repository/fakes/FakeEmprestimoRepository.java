package shokunin.group.com.biblioteca.repository.fakes;

import shokunin.group.com.biblioteca.domain.emprestimos.Emprestimo;
import shokunin.group.com.biblioteca.repository.contracts.IEmprestimoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//assina o mesmo contrato que o Repositório Real
public class FakeEmprestimoRepository implements IEmprestimoRepository {

    // "Banco de Dados" é apenas uma lista na memória
    private final List<Emprestimo> tabelaEmprestimos = new ArrayList<>();
    private int proximoId = 1; // Simulando o Auto-Increment

    @Override
    public Integer criarEmprestimo(Emprestimo emprestimo) {
        // Simula o comportamento do banco: gerar ID e salvar
        emprestimo.setId(proximoId++);

        tabelaEmprestimos.add(emprestimo);
        System.out.println("FAKE DB: Empréstimo salvo na memória: " + emprestimo.getDetalhes());
        return emprestimo.getId();
    }

    @Override
    public void alterarEmprestimo(Emprestimo emprestimo) {

        System.out.println("FAKE DB: Empréstimo atualizado: " + emprestimo.getId());
    }

    @Override
    public Optional<Emprestimo> buscarEmprestimoPorId(Integer id) {
        return tabelaEmprestimos.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }
}