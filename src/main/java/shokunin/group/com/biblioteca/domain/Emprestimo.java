package shokunin.group.com.biblioteca.domain;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Emprestimo {
    private final UUID id;
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
    public UUID getId(){
        return id;
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
    public void registraDevolucaoEfetiva(LocalDate dataDevolucaoEfetiva){
        this.devolucaoEfetiva = dataDevolucaoEfetiva;
    }
    public double getMulta(){
        return multa;
    }
    public void setValorMulta(double multa){
        this.multa = multa;
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

        return detalhes;
    }

    public static class EmprestimoBuilder{
        private UUID id = UUID.randomUUID();
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
            return new Emprestimo(this);
        }

    }
}
