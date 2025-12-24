package shokunin.group.com.biblioteca.service;

import shokunin.group.com.biblioteca.domain.Emprestimo;
import shokunin.group.com.biblioteca.strategy.contracts.EmprestimoStrategy;
import shokunin.group.com.biblioteca.domain.LibraryItem;
import shokunin.group.com.biblioteca.domain.Usuario;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmprestimoService {
    private final Map<String,EmprestimoStrategy> estrategias = new HashMap<>();

    public EmprestimoService(List<EmprestimoStrategy> listaEstrategias) {
        for (EmprestimoStrategy estrategia : listaEstrategias) {
            estrategias.put(estrategia.getTipoUsuario(), estrategia);
        }

    }

    public void processarEmprestimo(Usuario usuario, LibraryItem item){
        EmprestimoStrategy regra = estrategias.get(usuario.getTipo());
        if (regra == null) {
            throw new IllegalArgumentException("Nenhuma regra de emprestimo econtrada para esse tipo de usuario");
        }

        int dias = regra.getDiasEmprestimo();
        int maximoItens = regra.getMaximoItens();

        if (!item.isDisponivel()){
            throw new IllegalArgumentException("Item nao esta disponivel para emprestimo");
        }
        //TODO: Verificar se o usuario ja possui o maximo de itens emprestados

        System.out.println("Emprestimo de " + item.getTitulo() + " para " + usuario.getNome() + " por " + dias + " dias");
        System.out.println("Maximo de itens: " + maximoItens);
        //TODO : Criar objeto emprestimo e persisitir no banco de dados

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
