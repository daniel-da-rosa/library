package shokunin.group.com.biblioteca.domain;

import shokunin.group.com.biblioteca.domain.contracts.FuncionarioInterface;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Funcionario extends Usuario implements FuncionarioInterface {
    private  String registro;
    private  String cargo;

    private Funcionario(FuncionarioBuilder builder){
        super(builder);
        this.registro = builder.registro;
        this.cargo = builder.cargo;
    }


    public String getCargo(){return cargo;}

    @Override
    public String toString(){
        return "Funcionario [registro=" + registro + ", cargo=" + cargo + "]";
    }

    @Override
    public String getRegistro(){return registro;}

    @Override
    public Map<String,String> getDetalhes(){
        Map<String,String> detalhes = new LinkedHashMap<>();
        detalhes.put("Registro",getRegistro());
        detalhes.put("Cargo",getCargo());
        return Collections.unmodifiableMap(detalhes);
    }


    public static  class  FuncionarioBuilder extends UsuarioBuilder<FuncionarioBuilder>{
        private String registro;
        private String cargo;

        public FuncionarioBuilder(String nome, String documento, Unidade unidade){
            super(nome,documento,unidade);

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
