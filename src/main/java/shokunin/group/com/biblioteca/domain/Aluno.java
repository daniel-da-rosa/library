package shokunin.group.com.biblioteca.domain;

import shokunin.group.com.biblioteca.domain.contracts.AlunoInterface;
import shokunin.group.com.biblioteca.domain.enums.NivelEnsino;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Aluno extends Usuario implements AlunoInterface {
    private final String matricula;
    private final NivelEnsino nivelEnsino;

    private Aluno(AlunoBuilder builder){
        super(builder);
        this.matricula = builder.matricula;
        this.nivelEnsino = builder.nivelEnsino;
    }
    @Override
    public String getMatricula(){return matricula;}

    @Override
    public String getNivelEnsino(){return nivelEnsino.toString();}

    @Override
    public Map<String,String> getDetalhes(){
        Map<String,String> detalhes = new LinkedHashMap<>();
        detalhes.put("Matricula",getMatricula());
        detalhes.put("Nivel Ensino",getNivelEnsino().toString());
        return detalhes;
    }

    @Override
    public String toString(){
        return "Aluno [matricula=" + matricula + ", nivelEnsino=" + nivelEnsino + "]";
    }

    public static class AlunoBuilder extends UsuarioBuilder<AlunoBuilder>{
        private String matricula;
        private NivelEnsino nivelEnsino;

        public AlunoBuilder(String nome, String documento, Unidade unidade){
            super(nome,documento,unidade); //chama o builder do usuario

        }
        public AlunoBuilder comMatricula(String matricula){
            this.matricula = matricula;
            return self();
        }

        public AlunoBuilder comNivelEnsino(NivelEnsino nivelEnsino){
            this.nivelEnsino = nivelEnsino;
            return self();
        }
        @Override
        protected AlunoBuilder self(){
            return this;
        }
        @Override
        public Aluno build(){
            return new Aluno(this);
        }
    }
}
