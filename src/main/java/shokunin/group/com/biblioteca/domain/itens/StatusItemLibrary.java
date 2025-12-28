package shokunin.group.com.biblioteca.domain.itens;

import shokunin.group.com.biblioteca.domain.itens.enums.Status;

public final class StatusItemLibrary {
    private final Status tipo;

    private StatusItemLibrary(Status tipo) {
        this.tipo = tipo;
    }

    public static StatusItemLibrary disponivel() {
        return new StatusItemLibrary(Status.DISPONIVEL);
    }

    public StatusItemLibrary emprestado() {
        if(this.tipo != Status.DISPONIVEL){
            throw new IllegalArgumentException("Item não disponível para empréstimo");
        }
        return new StatusItemLibrary(Status.EMPRESTADO);
    }
    public StatusItemLibrary reservado(){
        if(this.tipo != Status.DISPONIVEL){
            throw new IllegalArgumentException("Item não disponível para reserva");
        }
        return new StatusItemLibrary(Status.RESERVADO);
    }
    public static StatusItemLibrary perda(){
        return new StatusItemLibrary(Status.PERDA);
    }
    public static StatusItemLibrary danificado(){
        return new StatusItemLibrary(Status.DANIFICADO);
    }

    public boolean permiteEmprestimo(){
        return this.tipo == Status.DISPONIVEL;
    }

    public StatusItemLibrary devolver(){
        if(this.tipo != Status.EMPRESTADO){
            throw new IllegalArgumentException("Item não disponível para devolução");
        }
        return new StatusItemLibrary(Status.DISPONIVEL);
    }

    public StatusItemLibrary emprestar(){
        if(this.tipo != Status.DISPONIVEL){
            throw new IllegalArgumentException("Item não disponível para empréstimo");
        }
        return new StatusItemLibrary(Status.EMPRESTADO);
    }


}
