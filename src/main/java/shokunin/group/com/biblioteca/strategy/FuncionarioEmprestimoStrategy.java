package shokunin.group.com.biblioteca.strategy;

import shokunin.group.com.biblioteca.strategy.contracts.EmprestimoStrategy;

public class FuncionarioEmprestimoStrategy implements EmprestimoStrategy {
    @Override
    public int getDiasEmprestimo(){
        return 7;
    }

    @Override
    public double getMultaDiaria(){
        return 0.5;
    }
    @Override
    public double getMultaMaxima(){
        return 10;
    }
    @Override
    public double getMultaMinima(){
        return 1.0;
    }
    @Override
    public int getMaximoItens(){
        return 5;
    }
    @Override
    public int getMinimoItens(){
        return 1;
    }

    @Override
    public String getTipoUsuario() {
        return "FUNCIONARIO";
    }
}
