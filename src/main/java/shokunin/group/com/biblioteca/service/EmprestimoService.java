package shokunin.group.com.biblioteca.service;

import shokunin.group.com.biblioteca.domain.emprestimos.Emprestimo;
import shokunin.group.com.biblioteca.domain.itens.LibraryItem;
import shokunin.group.com.biblioteca.domain.users.Aluno;
import shokunin.group.com.biblioteca.domain.users.Funcionario;
import shokunin.group.com.biblioteca.domain.users.Usuario;
import shokunin.group.com.biblioteca.repository.EmprestimoRepository;
import shokunin.group.com.biblioteca.strategy.contracts.EmprestimoStrategy;
import shokunin.group.com.biblioteca.exceptions.LibraryExceptionFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmprestimoService {
    private final Map<String,EmprestimoStrategy> estrategias = new HashMap<>();
    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoService(List<EmprestimoStrategy> listaEstrategias,EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
        for (EmprestimoStrategy estrategia : listaEstrategias) {
            estrategias.put(estrategia.getTipoUsuario(), estrategia);
        }

    }

    public Emprestimo processarEmprestimo(Usuario usuario, LibraryItem item){

        EmprestimoStrategy regra = switch (usuario){
          case Aluno a -> estrategias.get("ALUNO");
          case Funcionario f -> estrategias.get("FUNCIONARIO");

        };

        if (regra == null) {
            throw LibraryExceptionFactory.regraNaoEncontrada(usuario.getClass().getSimpleName());
        }

        if (!item.getStatus().permiteEmprestimo()){
            throw LibraryExceptionFactory.itemIndisponivel(item.getTitulo());
        }

        Emprestimo novoEmprestimo = new Emprestimo.EmprestimoBuilder(usuario,item)
                .comPrazo(regra.getDiasEmprestimo())
                .build();

        item.emprestar();

        novoEmprestimo.getDetalhes().forEach((chave,valor) -> System.out.println(chave + ": " + valor));


        //TODO: Verificar se o usuario ja possui o maximo de itens emprestados
        emprestimoRepository.salvarEmprestimo(novoEmprestimo);

        return novoEmprestimo;

    }


    public double processarDevolucao(Emprestimo emprestimo, LocalDate dataRetorno){

        EmprestimoStrategy regra = switch (emprestimo.getUsuario()){
            case Aluno a -> estrategias.get("ALUNO");
            case Funcionario f -> estrategias.get("FUNCIONARIO");

        };

        return emprestimo.finalizarEmprestimo(dataRetorno,regra);

    }

}
