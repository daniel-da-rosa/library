package shokunin.group.com.biblioteca.exceptions;

public class LibraryExceptionFactory {
    public static LibraryException itemIndisponivel(String titulo){
        return new ItemIndisponivelException(titulo);
    }

    public static LibraryException limiteEmprestimoExcedido(String usuario){
        return new LimiteEmprestimoExcedidoException(usuario);
    }

    public static LibraryException regraNaoEncontrada(String tipoUsuario){
        return new LibraryException("Sem regras para o tipo:" +tipoUsuario,"lIB-003"){};
    }

}
