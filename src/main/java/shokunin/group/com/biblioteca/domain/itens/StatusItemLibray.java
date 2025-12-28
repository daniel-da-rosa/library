package shokunin.group.com.biblioteca.domain.itens;

import shokunin.group.com.biblioteca.domain.itens.enums.Status;

public final class StatusItemLibray {
    private final Status tipo;

    private StatusItemLibray(Status tipo) {
        this.tipo = tipo;
    }

    public static StatusItemLibray disponivel() {
        return new StatusItemLibray(Status.DISPONIVEL);
    }

    public StatusItemLibray emprestado() {
        if(this.tipo != Status.DISPONIVEL){
            throw new IllegalArgumentException("Item não disponível para empréstimo");
        }
        return new StatusItemLibray(Status.EMPRESTADO);
    }
    public  StatusItemLibray reservado(){
        if(this.tipo != Status.DISPONIVEL){
            throw new IllegalArgumentException("Item não disponível para reserva");
        }
        return new StatusItemLibray(Status.RESERVADO);
    }
    public static StatusItemLibray perda(){
        return new StatusItemLibray(Status.PERDA);
    }
    public static StatusItemLibray danificado(){
        return new StatusItemLibray(Status.DANIFICADO);
    }

    public boolean permiteEmprestimo(){
        return this.tipo == Status.DISPONIVEL;
    }

    public StatusItemLibray devolver(){
        if(this.tipo != Status.EMPRESTADO){
            throw new IllegalArgumentException("Item não disponível para devolução");
        }
        return new StatusItemLibray(Status.DISPONIVEL);
    }

    public StatusItemLibray emprestar(){
        if(this.tipo != Status.DISPONIVEL){
            throw new IllegalArgumentException("Item não disponível para empréstimo");
        }
        return new StatusItemLibray(Status.EMPRESTADO);
    }


}
