package shokunin.group.com.biblioteca.domain.emprestimos;
import shokunin.group.com.biblioteca.domain.itens.LibraryItem;
import shokunin.group.com.biblioteca.domain.users.Usuario;
import shokunin.group.com.biblioteca.strategy.contracts.EmprestimoStrategy;

import java.math.BigDecimal;
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
    private BigDecimal multa = BigDecimal.ZERO;

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

    public BigDecimal getMulta(){
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

    public BigDecimal finalizarEmprestimo(LocalDate dataRetorno, EmprestimoStrategy regra){

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

    private BigDecimal calcularValorComLimite(EmprestimoStrategy regra,Long dias){

        BigDecimal valorCalculado = regra.getMultaDiaria()
                .multiply(new BigDecimal(dias));

        return valorCalculado
                .max(regra.getMultaMinima())
                .min(regra.getMultaMaxima());
    }

    public static class EmprestimoBuilder{
        private Integer id ;
        private LocalDate dataEmprestimo = LocalDate.now();
        private LocalDate dataPrevistaDevolucao;
        private Usuario usuario;
        private LibraryItem item;
        private LocalDate devolucaoEfetiva;
        private BigDecimal multa;

        public EmprestimoBuilder (Usuario usuario, LibraryItem item) {
            this.usuario = usuario;
            this.item = item;
        }

        public EmprestimoBuilder comPrazo(int dias){
            this.dataPrevistaDevolucao = this.dataEmprestimo.plusDays(dias);
            return this;
        }
        public EmprestimoBuilder comDataEmprestimo(LocalDate dataEmprestimo){
            this.dataEmprestimo = dataEmprestimo;
            return this;
        }
        public EmprestimoBuilder comDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao){
            this.dataPrevistaDevolucao = dataPrevistaDevolucao;
            return this;
        }
        public EmprestimoBuilder comDataEfetiva(LocalDate devolucaoEfetiva){
            this.devolucaoEfetiva = devolucaoEfetiva;
            return this;
        }

        public EmprestimoBuilder comMulta(BigDecimal multa){
            this.multa = multa;
            return this;
        }
        public EmprestimoBuilder comId(Integer id){
            this.id = id;
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
