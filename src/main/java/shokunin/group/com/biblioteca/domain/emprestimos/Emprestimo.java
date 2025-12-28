package shokunin.group.com.biblioteca.domain.emprestimos;
import shokunin.group.com.biblioteca.domain.itens.LibraryItem;
import shokunin.group.com.biblioteca.domain.users.Usuario;
import shokunin.group.com.biblioteca.strategy.contracts.EmprestimoStrategy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Emprestimo {
    private  Integer id;
    private final LocalDate dataEmprestimo;
    private final LocalDate dataPrevistaDevolucao;
    private final Usuario usuario;
    private final LibraryItem item;
    private LocalDate devolucaoEfetiva;
    private double multa;

    public Emprestimo(EmprestimoBuilder builder) {
        this.id = builder.id;
        this.dataEmprestimo = builder.dataEmprestimo;
        this.dataPrevistaDevolucao = builder.dataPrevistaDevolucao;
        this.usuario = builder.usuario;
        this.item = builder.item;

    }
    public Integer getId(){
        return id;
    }
    public void setId(Integer id ){
        this.id =id;
    }

    public LocalDate getDataEmprestimo(){
        return dataEmprestimo;
    }
    public LocalDate getDataPrevistaDevolucao(){
        return dataPrevistaDevolucao;
    }
    public Usuario getUsuario(){
        return usuario;
    }
    public LibraryItem getItem(){
        return item;
    }
    public LocalDate getDevolucaoEfetiva(){
        return devolucaoEfetiva;
    }

    public double getMulta(){
        return multa;
    }

    public Map<String,String> getDetalhes(){
        Map<String, String> detalhes = new LinkedHashMap<>();
        detalhes.put("Usuario",getUsuario().getNome());
        detalhes.put("Item",getItem().getTitulo());
        detalhes.put("Tipo",getItem().getTipo());
        detalhes.put("Data de emprestimo",getDataEmprestimo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        detalhes.put("Data prevista de devolucao",getDataPrevistaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        if(getDevolucaoEfetiva() != null){
            detalhes.put("Multa",String.valueOf(getMulta()));
            detalhes.put("Data de devolucao",getDevolucaoEfetiva().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        return Collections.unmodifiableMap(detalhes);
    }

    public double finalizarEmprestimo(LocalDate dataRetorno, EmprestimoStrategy regra){

        java.util.Objects.requireNonNull(dataRetorno, "Data de devolucao é obrigatória");
        java.util.Objects.requireNonNull(regra, "Regra de emprestimo é obrigatória");

        if(this.devolucaoEfetiva!=null){
            throw new IllegalArgumentException("Emprestimo ja finalizado");
        }

        if(dataRetorno.isBefore(this.dataEmprestimo)){
            throw new IllegalArgumentException("Data de devolucao nao pode ser anterior a data de emprestimo");
        }

        this.devolucaoEfetiva = dataRetorno;
        this.getItem().devolver();// retorna o objeto ao status disponivel
        Long diasAtraso = ChronoUnit.DAYS.between(this.dataPrevistaDevolucao,dataRetorno);

        if(diasAtraso > 0){
            this.multa = calcularValorComLimite(regra,diasAtraso);
        }

        return this.multa;
    }

    private double calcularValorComLimite(EmprestimoStrategy regra,Long dias){
        double valor = regra.getMultaDiaria() * dias;
        return Math.min(Math.max(valor,regra.getMultaMinima()),regra.getMultaMaxima());
    }

    public static class EmprestimoBuilder{
        private Integer id ;
        private LocalDate dataEmprestimo = LocalDate.now();
        private LocalDate dataPrevistaDevolucao;
        private Usuario usuario;
        private LibraryItem item;

        public EmprestimoBuilder (Usuario usuario, LibraryItem item) {
            this.usuario = usuario;
            this.item = item;
        }

        public EmprestimoBuilder comPrazo(int dias){
            this.dataPrevistaDevolucao = this.dataEmprestimo.plusDays(dias);
            return this;
        }

        public Emprestimo build(){
            if (dataPrevistaDevolucao.isBefore(dataEmprestimo)) {
                throw new IllegalArgumentException("Data de devolucao nao pode ser anterior a data de emprestimo");
            }
            java.util.Objects.requireNonNull(usuario, "Usuário é obrigatório");
            java.util.Objects.requireNonNull(item, "Item é obrigatório");
            java.util.Objects.requireNonNull(dataPrevistaDevolucao, "Data de devolucao é obrigatória");
            return new Emprestimo(this);
        }

    }
}
