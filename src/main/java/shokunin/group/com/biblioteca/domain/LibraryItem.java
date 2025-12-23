package shokunin.group.com.biblioteca.domain;

import java.util.Map;

public abstract class LibraryItem {
    protected Integer id;
    protected final String titulo;
    protected boolean disponivel;

    protected LibraryItem(Builder<?> builder) {
        this.id = builder.id;
        this.titulo = builder.titulo;
        this.disponivel = builder.disponivel;

    }
    public abstract String getTipo();
    public abstract Map<String, String> getDetalhes();

    public Integer getId() {
        return id;
    }
    public String getTitulo(){
        return titulo;
    }
    public boolean isDisponivel(){
        return disponivel;
    }
    public void setDisponivel(boolean disponivel){
        this.disponivel = disponivel;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public abstract static class Builder<T extends Builder<T>> {

        protected Integer id;
        protected String titulo;
        protected boolean disponivel;

        public Builder(String titulo) {
            if (titulo == null || titulo.isBlank()) {
                throw new IllegalArgumentException("Título é obrigatório");
            }
            this.titulo = titulo;

        }

        public T comId(Integer id) {
            this.id = id;
            return self();
        }

        public T comDisponibilidade(boolean disponivel) {
            this.disponivel = disponivel;
            return self();
        }

        protected abstract T self();

        public abstract LibraryItem build();
    }
}
