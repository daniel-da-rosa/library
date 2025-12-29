package shokunin.group.com.biblioteca.strategy.contracts;

import java.math.BigDecimal;

public interface EmprestimoStrategy {
    int getDiasEmprestimo();
    BigDecimal getMultaDiaria();
    BigDecimal getMultaMaxima();
    BigDecimal getMultaMinima();
    int getMaximoItens();
    int getMinimoItens();
    String getTipoUsuario();
}
