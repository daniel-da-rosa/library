package shokunin.group.com.biblioteca.domain.itens;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Periodico extends LibraryItem{
    private final String editora;
    private final LocalDate dataPublicacao;
    private final String issn;
    private final Integer numero;

    private Periodico(PeriodicoBuilder builder){
        super(builder);
        this.editora = builder.editora;
        this.dataPublicacao = builder.dataPublicacao;
        this.issn = builder.issn;
        this.numero = builder.numero;
    }
    public String getEditora(){
        return editora;
    }
    public LocalDate getDataPublicacao(){
        return dataPublicacao;
    }
    public String getIssn(){
        return issn;
    }
    public Integer getNumero(){
        return numero;
    }
    @Override
    public String getTipo(){
        return "Periodico";
    }

    public static class PeriodicoBuilder extends Builder<PeriodicoBuilder>{
        private String editora;
        private LocalDate dataPublicacao;
        private String issn;
        private Integer numero;

        public PeriodicoBuilder(String titulo, String issn, StatusItemLibrary status){
            super(titulo,status);

            if(issn == null || issn.isBlank()){
                throw new IllegalArgumentException("ISSN é obrigatório");
            }
            this.issn = issn;

        }

        public PeriodicoBuilder comEditora(String editora){
            this.editora = editora;
            return self();
        }
        public PeriodicoBuilder comDataPublicacao(LocalDate dataPublicacao){
            this.dataPublicacao = dataPublicacao;
            return self();
        }

        public PeriodicoBuilder comNumero(Integer numero){
            this.numero = numero;
            return self();
        }

        public Periodico build(){
            return new Periodico(this);
        }

        protected PeriodicoBuilder self(){
            return this;
        }

    }
    @Override
    public String toString(){
        return "Titulo:"+getTitulo()+"\nTipo:"+getTipo()+"\nDisponivel:"+(status.permiteEmprestimo() ? "Sim" : "Nao")+"\nPeriodico [editora=" + editora + ", dataPublicacao=" + dataPublicacao + ", issn=" + issn + ", numero=" + numero + "]";
    }

    @Override
    public Map<String,String> getDetalhes(){
        Map<String,String> detalhes = new LinkedHashMap<>();
        detalhes.put("Titulo",getTitulo());
        detalhes.put("Tipo",getTipo());
        detalhes.put("Editora",getEditora());
        detalhes.put("Data de Publicacao",getDataPublicacao().toString());
        detalhes.put("ISSN",getIssn());
        detalhes.put("Numero",getNumero().toString());
        detalhes.put("Disponivel",status.permiteEmprestimo() ? "Sim" : "Nao");
        return Collections.unmodifiableMap(detalhes);
    }
}
