package shokunin.group.com.biblioteca.strategy.contracts;

public interface EmprestimoStrategy {
    int getDiasEmprestimo();
    double getMultaDiaria();
    double getMultaMaxima();
    double getMultaMinima();
    int getMaximoItens();
    int getMinimoItens();
    String getTipoUsuario();
}
