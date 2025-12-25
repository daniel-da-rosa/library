package shokunin.group.com.biblioteca.exceptions;

public class LimiteEmprestimoExcedidoException extends LibraryException {
    public LimiteEmprestimoExcedidoException(String usuario){
        super("LIMITE_EMPRESTIMO_EXCEDIDO!!","O usuario: " + usuario + " ja atingiu o limite maximo de emprestimos");
    }
}
