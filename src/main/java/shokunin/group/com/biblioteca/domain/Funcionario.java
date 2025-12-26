package shokunin.group.com.biblioteca.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class Funcionario extends Usuario{
    private final String registro;
    private final String cargo;

    private Funcionario(FuncionarioBuilder builder){
        super(builder);
        this.registro = builder.registro;
        this.cargo = builder.cargo;
    }


    public String getCargo(){return cargo;}

    @Override
    public String getTipo(){return "FUNCIONARIO";}

    @Override
    public String toString(){
        return "Funcionario [registro=" + registro + ", cargo=" + cargo + "]";
    }

    @Override
    public String getMatriculaRegistro(){return registro;}

    @Override
    public String getNivelEnsino(){
        return "NSA";
    }

    @Override
    public Map<String,String> getDetalhes(){
        Map<String,String> detalhes = new LinkedHashMap<>();
        detalhes.put("Registro",getMatriculaRegistro());
        detalhes.put("Cargo",getCargo());
        return detalhes;
    }


    public static  class  FuncionarioBuilder extends UsuarioBuilder<FuncionarioBuilder>{
        private String registro;
        private String cargo;

        public FuncionarioBuilder(String nome, String documento, Unidade unidade){
            super(nome,documento,unidade);
            if(registro == null || registro.isBlank() || cargo == null || cargo.isBlank()){
                throw new IllegalArgumentException("Registro e cargo são obrigatórios");
            }

        }
        public FuncionarioBuilder comRegistro(String registro){
            this.registro = registro;
            return self();
        }

        public FuncionarioBuilder comCargo(String cargo){
            this.cargo = cargo;
            return self();
        }
        @Override
        public FuncionarioBuilder self(){
            return this;
        }

        @Override
        public Funcionario build(){
            return new Funcionario(this);
        }
    }

}
