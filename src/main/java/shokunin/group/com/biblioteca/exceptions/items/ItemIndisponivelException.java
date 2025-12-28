package shokunin.group.com.biblioteca.exceptions.items;

public class ItemIndisponivelException extends LibraryException {
    public ItemIndisponivelException(String titulo){
        super("ITEM_INDISPONIVEL!","O item:"+ titulo + " nao esta disponivel para emprestimo");
    }
}
