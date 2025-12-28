package shokunin.group.com.biblioteca.domain.itens;

import java.util.Map;

public abstract class LibraryItem {
    protected Integer id;
    protected final String titulo;
    protected StatusItemLibrary status;

    protected LibraryItem(Builder<?> builder) {
        this.id = builder.id;
        this.titulo = builder.titulo;
        this.status = builder.status;

    }
    public abstract String getTipo();
    public abstract Map<String, String> getDetalhes();

    public Integer getId() {
        return id;
    }
    public String getTitulo(){
        return titulo;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public StatusItemLibrary getStatus(){
        return status;
    }
    public void devolver(){
        this.status = this.status.devolver();
    }
    public void emprestar(){
        this.status = this.status.emprestar();
    }

    public abstract static class Builder<T extends Builder<T>> {

        protected Integer id;
        protected String titulo;
        protected StatusItemLibrary status;

        public Builder(String titulo, StatusItemLibrary status) {
            if (titulo == null || titulo.isBlank()) {
                throw new IllegalArgumentException("Título é obrigatório");
            }
            this.titulo = titulo;
            this.status = java.util.Objects.requireNonNull(status, "Status é obrigatório");

        }

        public T comId(Integer id) {
            this.id = id;
            return self();
        }

        protected abstract T self();

        public abstract LibraryItem build();
    }
}
