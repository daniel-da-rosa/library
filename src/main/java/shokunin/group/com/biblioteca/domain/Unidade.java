package shokunin.group.com.biblioteca.domain;

public class Unidade {
    private final String id;
    private final String nome;
    private final String endereco;
    private final String telefone;
    private final String email;

    public Unidade(String id,String nome,String endereco,String telefone,String email){
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;

    }
    public String getId(){return id;}
    public String getNome(){return nome;}
    public String getEndereco(){return endereco;}
    public String getTelefone(){return telefone;}
    public String getEmail(){return email;}
}
