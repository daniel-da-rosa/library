package shokunin.group.com.biblioteca.exceptions;

public class RepositoryException extends RuntimeException {
    private final String codigoErro;
    private final String operacao;

    public RepositoryException(String message,String codigoErro,String operacao) {
        super(message);
        this.codigoErro = codigoErro;
        this.operacao = operacao;
    }

    public RepositoryException(String message,String codigoErro,String operacao,Throwable cause){
         super(message,cause);
         this.codigoErro = codigoErro;
         this.operacao = operacao;
    }

    public String getCodigoErro(){
        return codigoErro;
    }

    public String getOperacao() {
        return operacao;
    }

    @Override
    public String  toString(){
        return String.format("[%s] %s: %s",codigoErro,operacao,getMessage());
    }
}
