package shokunin.group.com.biblioteca.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Unidade {
    private Integer id;
    private final String nome;
    private final String endereco;
    private final String telefone;
    private final String email;



    private Unidade(UnidadeBuilder builder){
        this.id = builder.id;
        this.nome = builder.nome;
        this.endereco = builder.endereco;
        this.telefone = builder.telefone;
        this.email = builder.email;

    }

    public Integer getId(){return id;}
    public void setId(Integer id){
        this.id = id;
    }
    public String getNome(){return nome;}
    public String getEndereco(){return endereco;}
    public String getTelefone(){return telefone;}
    public String getEmail(){return email;}

    public Map<String,String>getDetalhes(){
        Map<String,String>detalhes = new LinkedHashMap<>();
        detalhes.put("ID",getId().toString());
        detalhes.put("Nome",getNome());
        detalhes.put("Endereco",getEndereco());
        detalhes.put("Telefone",getTelefone());
        detalhes.put("Email",getEmail());
        return detalhes;
    }

    public static class UnidadeBuilder {
        private Integer id;
        private String nome;
        private String endereco;
        private String telefone;
        private String email;


        public UnidadeBuilder() {
        }

        public UnidadeBuilder comId(Integer id) {
            this.id = id;
            return this;
        }

        public UnidadeBuilder comNome(String nome) {
            this.nome = nome;
            return this;
        }

        public UnidadeBuilder comEndereco(String endereco) {
            this.endereco = endereco;
            return this;
        }

        public UnidadeBuilder comTelefone(String telefone) {
            this.telefone = telefone;
            return this;
        }

        public UnidadeBuilder comEmail(String email) {
            this.email = email;
            return this;
        }

        public Unidade build() {
            return new Unidade(this);
        }

        public UnidadeBuilder self() {
            return this;
        }
    }
}
