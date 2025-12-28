package shokunin.group.com.biblioteca.exceptions.items;

public class LibraryException extends RuntimeException{
    private final String codigoErro;

    public LibraryException(String codigoErro, String message){
        super(message);
        this.codigoErro = codigoErro;
    }

    public String getCodigoErro(){
        return codigoErro;
    }
}
