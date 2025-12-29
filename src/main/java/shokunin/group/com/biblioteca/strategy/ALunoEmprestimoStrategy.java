package shokunin.group.com.biblioteca.strategy;

import shokunin.group.com.biblioteca.strategy.contracts.EmprestimoStrategy;

import java.math.BigDecimal;

public class ALunoEmprestimoStrategy implements EmprestimoStrategy {
    @Override
    public int getDiasEmprestimo(){
        return 7;
    }

    @Override
    public BigDecimal getMultaDiaria(){
        return new BigDecimal(0.5);
    }
    @Override
    public BigDecimal getMultaMaxima(){
        return new BigDecimal(10);
    }
    @Override
    public BigDecimal getMultaMinima(){
        return new BigDecimal(1.0);
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
        return "ALUNO";
    }


}
