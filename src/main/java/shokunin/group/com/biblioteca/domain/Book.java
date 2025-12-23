package shokunin.group.com.biblioteca.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class Book extends LibraryItem{
    private final String autor;
    private final Integer anoPublicacao;
    private final String genero;
    private final String isbn;

    private Book(BookBuilder builder){
        super(builder);
        this.autor = builder.autor;
        this.anoPublicacao = builder.anoPublicacao;
        this.genero = builder.genero;
        this.isbn = builder.isbn;
    }

    @Override
    public String getTipo(){
        return "Livro";
    }

    public String getAutor(){
        return autor;
    }
    public Integer getAnoPublicacao(){
        return anoPublicacao;
    }

    public String getGenero(){
        return genero;
    }

    public String getIsbn(){
        return isbn;
    }

    public static class BookBuilder extends Builder<BookBuilder>{
        private String autor;
        private Integer anoPublicacao;
        private String genero;
        private String isbn;

        public BookBuilder(String titulo,String isbn,String autor,Integer anoPublicacao){
            super(titulo);
            if(isbn == null || isbn.isBlank()){
                throw new IllegalArgumentException("ISBN é obrigatório");
            }
            this.isbn = isbn;
            this.autor = autor;
            this.anoPublicacao = anoPublicacao;
        }

        public BookBuilder comGenero(String genero){
            this.genero = genero;
            return self();
        }
        public BookBuilder comAnoPublicacao(Integer anoPublicacao){
            this.anoPublicacao = anoPublicacao;
            return self();
        }
        public BookBuilder comAutor(String autor){
            this.autor = autor;
            return self();
        }

        @Override
        protected BookBuilder self(){
            return this;
        }

        @Override
        public Book build(){
            return new Book(this);
        }

    }

    @Override
    public String toString(){
        return "Titulo:"+getTitulo()+"\nTipo:"+getTipo()+"\nDisponivel:"+(isDisponivel() ? "Sim" : "Nao")+"\nLivro [autor=" + autor + ", anoPublicacao=" + anoPublicacao + ", genero=" + genero + ", isbn=" + isbn + "]";
    }

    @Override
    public Map<String,String>getDetalhes(){
        Map<String, String> detalhes = new LinkedHashMap<>();
        detalhes.put("Titulo", getTitulo());
        detalhes.put("Autor", autor);
        detalhes.put("Ano de Publicacao", anoPublicacao.toString());
        detalhes.put("Genero", genero);
        detalhes.put("ISBN", isbn);
        detalhes.put("Disponivel", isDisponivel() ? "Sim" : "Nao");
        return detalhes;
    }

}
