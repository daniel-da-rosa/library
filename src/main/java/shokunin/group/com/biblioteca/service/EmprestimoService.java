package shokunin.group.com.biblioteca.service;

import shokunin.group.com.biblioteca.domain.Emprestimo;
import shokunin.group.com.biblioteca.repository.EmprestimoRepository;
import shokunin.group.com.biblioteca.strategy.contracts.EmprestimoStrategy;
import shokunin.group.com.biblioteca.domain.LibraryItem;
import shokunin.group.com.biblioteca.domain.Usuario;
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

        EmprestimoStrategy regra = estrategias.get(usuario.getTipo());
        if (regra == null) {
            throw LibraryExceptionFactory.regraNaoEncontrada(usuario.getTipo());
        }

        if (!item.isDisponivel()){
            throw LibraryExceptionFactory.itemIndisponivel(item.getTitulo());
        }

        Emprestimo novoEmprestimo = new Emprestimo.EmprestimoBuilder(usuario,item)
                .comPrazo(regra.getDiasEmprestimo())
                .build();

        item.setDisponivel(false);

        novoEmprestimo.getDetalhes().forEach((chave,valor) -> System.out.println(chave + ": " + valor));


        //TODO: Verificar se o usuario ja possui o maximo de itens emprestados
        emprestimoRepository.salvarEmprestimo(novoEmprestimo);

        return novoEmprestimo;

    }


    public double processarDevolucao(Emprestimo emprestimo, LocalDate dataRetorno){

        emprestimo.getItem().setDisponivel(true);// retorna o objeto ao status disponivel
        emprestimo.registraDevolucaoEfetiva(dataRetorno);
        EmprestimoStrategy regra = estrategias.get(emprestimo.getUsuario().getTipo());

        if (dataRetorno.isAfter(emprestimo.getDataPrevistaDevolucao())){
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(
                    emprestimo.getDataPrevistaDevolucao(),
                    dataRetorno
            );
            double multa = calcularMulta(regra, (int)diasAtraso);
            emprestimo.setValorMulta(multa);
            return multa;

        }

        return 0.0; //retorno sem multa

    }

    private double calcularMulta(EmprestimoStrategy regra, int dias){

        double multa = regra.getMultaDiaria() * dias;

        if (multa > regra.getMultaMaxima()){
            return regra.getMultaMaxima();
        } else if (multa < regra.getMultaMinima()){
            return regra.getMultaMinima();
        }
        return multa;
    }
}
