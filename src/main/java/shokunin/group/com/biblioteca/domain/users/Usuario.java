package shokunin.group.com.biblioteca.domain.users;

import shokunin.group.com.biblioteca.domain.unidades.Unidade;

import java.util.Map;

public sealed abstract class Usuario permits Aluno,Funcionario{
    private  Integer id;
    private final String nome;
    private final String email;
    private final String senha;
    private final String telefone;
    private final String documento;
    private final boolean ativo;
    private final Unidade unidade;

    protected Usuario(UsuarioBuilder<?>  builder){
        this.id = builder.id;
        this.nome = builder.nome;
        this.documento = builder.documento;
        this.ativo = builder.ativo;
        this.unidade = builder.unidade;
        this.email = builder.email;
        this.senha = builder.senha;
        this.telefone = builder.telefone;

    }
    public Integer getId(){return id;}
    public void setId(Integer id){ this.id = id;}

    public String getNome(){return nome;}
    public String getDocumento(){return documento;}
    public boolean isAtivo(){return ativo;}
    public Unidade getUnidade(){return unidade;}
    public String getEmail(){return email;}
    public String getSenha(){return senha;}
    public String getTelefone(){return telefone;}

    //metodos abstratos
    public abstract Map<String,String> getDetalhes();



    //Builder
    public static abstract class UsuarioBuilder<T extends UsuarioBuilder<T>>{
        private Integer id;
        private String nome;
        private String documento;
        private boolean ativo = true;
        private Unidade unidade;
        private String email;
        private String senha;
        private String telefone;

        public UsuarioBuilder(String nome, String documento, Unidade unidade){
            if(nome==null || nome.isBlank() ||documento == null || documento.isBlank()){
                throw new IllegalArgumentException("Nome e documento são obrigatórios");
            }

            this.nome=nome;
            this.documento= documento;
            this.unidade = unidade;
        }

        public T comEmail(String email){
            this.email = email;
            return self();
        }

        public T comId(Integer id){
            this.id = id;
            return self();
        }

        public T comAtivo(boolean ativo){
            this.ativo = ativo;
            return self();
        }

        public T comSenha(String senha){
            this.senha = senha;
            return self();
        }

        public T comTelefone(String telefone){
            this.telefone = telefone;
            return self();
        }

        protected abstract T self();
        public abstract Usuario build();

    }
}
