package shokunin.group.com.biblioteca.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shokunin.group.com.biblioteca.domain.emprestimos.Emprestimo;
import shokunin.group.com.biblioteca.domain.itens.StatusItemLibrary;
import shokunin.group.com.biblioteca.domain.users.Email;
import shokunin.group.com.biblioteca.domain.users.enums.NivelEnsino;
import shokunin.group.com.biblioteca.domain.itens.Book;
import shokunin.group.com.biblioteca.domain.unidades.Unidade;
import shokunin.group.com.biblioteca.domain.users.Aluno;
import shokunin.group.com.biblioteca.domain.users.Usuario;
import shokunin.group.com.biblioteca.repository.EmprestimoRepositorySQLite;
import shokunin.group.com.biblioteca.repository.ItemLibraryRepository;
import shokunin.group.com.biblioteca.repository.UnidadeRepository;
import shokunin.group.com.biblioteca.repository.UsuarioRepository;
import shokunin.group.com.biblioteca.repository.contracts.IEmprestimoRepository;
import shokunin.group.com.biblioteca.repository.fakes.FakeEmprestimoRepository;
import shokunin.group.com.biblioteca.strategy.ALunoEmprestimoStrategy;
import shokunin.group.com.biblioteca.strategy.FuncionarioEmprestimoStrategy;
import shokunin.group.com.biblioteca.exceptions.items.ItemIndisponivelException;
import shokunin.group.com.biblioteca.util.DBConnector;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmprestimoServiceTest {
    private EmprestimoService service;
    private Usuario aluno;
    private Book livro;
    private UnidadeRepository unidadeRepository;
    private UsuarioRepository usuarioRepository;
    private EmprestimoRepositorySQLite emprestimoRepositorySQLite;
    private IEmprestimoRepository repository;

    @BeforeEach
    void setup(){

        // 1. Inicializa o FAKE
        this.repository = new FakeEmprestimoRepository();

        // 2. Passa "this.repository" (o Fake)
        service = new EmprestimoService(List.of(
                new ALunoEmprestimoStrategy(),
                new FuncionarioEmprestimoStrategy()
        ), this.repository); // <--- AQUI MUDOU

        // 3. Cria os objetos APENAS EM MEMÓRIA
        Unidade unidade = new Unidade.UnidadeBuilder()
                .comNome("Escola Senai")
                .comEndereco("Rua Senai, 123")
                .comTelefone("123456789")
                .comEmail("escola@senai.com")
                .build();
        //  ID fake para não ficar null
        unidade.setId(1);

        // 4. Não precisa salvar no banco

        aluno = new Aluno.AlunoBuilder("Moto Moto","1234",unidade, Email.of("mandagascar@julien.com"))
                .comMatricula("Mat -2025-001")
                .comNivelEnsino(NivelEnsino.TECNOLOGO)
                .comTelefone("31 9 9765-0983")
                .build();
        // Damos um ID fake
        aluno.setId(100);

        // 5. NÃO precisa salvar no banco

        livro = new Book.BookBuilder("O SENHOR DOS ANEIS","978-3-16-148410-0","J.R.R. Tolkien",1954, StatusItemLibrary.disponivel() )
                .comGenero("Fantasia")
                .build();
        livro.setId(1);
    }
    @Test
    @DisplayName("Deve calcular a multa corretamente quando houver atraso")
    void deveCalcularMultaComAtraso(){
        Emprestimo emprestimo = service.processarEmprestimo(aluno,livro);

        //simula a devolucao com atraso
        LocalDate dataDevolucao = LocalDate.now().plusDays(10);
        BigDecimal multa = service.processarDevolucao(emprestimo,dataDevolucao);

        //verifica se a multa foi calculada corretamente
        assertThat(multa).isGreaterThan(new BigDecimal(0.0)); // verifica se a multa foi calculada corretamente. É auto explicativo mas ajuda a entender o que está acontecendo.
        assertThat(emprestimo.getMulta()).isEqualTo(multa);
        assertThat(livro.getStatus().permiteEmprestimo()).isTrue(); //verifica se o livro foi devolvido corretamente

    }
    @Test
    @DisplayName("Deve impedir que um livro já empresatdo (disponivel = false) seja emprestado novamente")
    void deveValidarLivroIndisponivel(){
        livro.emprestar();

        //teste de emprestimo
        assertThatThrownBy(() -> service.processarEmprestimo(aluno,livro))
                .isInstanceOf(ItemIndisponivelException.class); //valida se a exceção foi lancada

    }

    @Test
    @DisplayName("Deve impedir a devolução de emprestimo já finalizado")
    void deveValidarEmprestimoFinalizado(){
        Emprestimo emprestimo = service.processarEmprestimo(aluno,livro);

        //primeira devolucao
        service.processarDevolucao(emprestimo,LocalDate.now().plusDays(5));

        //segunda devolucao
        assertThatThrownBy(() ->
                service.processarDevolucao(emprestimo,LocalDate.now().plusDays(6)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Emprestimo ja finalizado");

    }

    @Test
    @DisplayName("Deve calcular multa dentro do limites Min e Max")
    void deveCalcularMultaDentroDosLimites(){
        Emprestimo emprestimoMax = service.processarEmprestimo(aluno,livro);

        //simula a devolucao com atraso max
        LocalDate dataDevolucaoMax = LocalDate.now().plusDays(100);
        BigDecimal multaMax = service.processarDevolucao(emprestimoMax,dataDevolucaoMax);

        //verifica se a multa foi calculada corretamente - por hora o valor maximo  está hardcoded - fixo.
        assertThat(multaMax).isEqualByComparingTo(BigDecimal.valueOf(10));

        //simula a devolucao com atraso min
        Emprestimo emprestimoMin = service.processarEmprestimo(aluno,livro);
        LocalDate dataDevolucaoMin = LocalDate.now().plusDays(8);
        BigDecimal multaMin = service.processarDevolucao(emprestimoMin,dataDevolucaoMin);

        //verifica se a multa foi calculada corretamente - por hora o valor minimo está hardcoded - fixo.
        assertThat(multaMin).isEqualByComparingTo(BigDecimal.valueOf(1));

    }

    @Test
    @DisplayName("Não deve calcular multa quando a devolução é feita no prazo.")
    void naoDeveCalcularMulta(){

        Emprestimo emprestimo = service.processarEmprestimo(aluno,livro);
        LocalDate dataDevolucao = LocalDate.now().plusDays(5);
        BigDecimal multa = service.processarDevolucao(emprestimo,dataDevolucao);

        //verifica se a multa foi calculada corretamente - por hora o valor minimo está hardcoded - fixo.
        assertThat(multa).isZero();
        assertThat(emprestimo.getMulta()).isZero();

    }
    @Test
    @DisplayName("Deve restaurar um item para disponviel após a devolução")
    void deveRestaurarItemAposDevolucao(){

        Emprestimo emprestimo = service.processarEmprestimo(aluno,livro);

        //verifica se o item foi emprestado
        assertThat(livro.getStatus().permiteEmprestimo()).isFalse();

        service.processarDevolucao(emprestimo,LocalDate.now().plusDays(5));
        //verifica se o item foi restaurado para disponivel
        assertThat(livro.getStatus().permiteEmprestimo()).isTrue();
    }

    @Test
    @DisplayName("Deve impedir uma data de devolução anterior a data de emprestimo")
    void deveImpedirDataDevolucaoAnteriorDataEmprestimo(){

        Emprestimo emprestimo = service.processarEmprestimo(aluno,livro);

        LocalDate dataPassado = LocalDate.now().minusDays(5);

        assertThatThrownBy(() -> service.processarDevolucao(emprestimo,dataPassado))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de devolucao nao pode ser anterior a data de emprestimo");

    }
    @Test
    @DisplayName("Deve validar clico completo de dados")
    void deveValidarClicoCompletoDePersistenciaNoBancoDeDados(){

        Emprestimo emprestimoCriado = service.processarEmprestimo(aluno,livro);

        Emprestimo emprestimoRecuperado = repository.buscarEmprestimoPorId(emprestimoCriado.getId())
                .orElseThrow(() -> new RuntimeException("Emprestimo nao encontrado"));

        // 3. Asserções de Reidratação (Verificando se os objetos voltaram "vivos")
        assertThat(emprestimoRecuperado.getUsuario().getNome()).isEqualTo(aluno.getNome());
        assertThat(emprestimoRecuperado.getItem().getTitulo()).isEqualTo(livro.getTitulo());
        assertThat(emprestimoRecuperado.getDataEmprestimo()).isEqualTo(LocalDate.now());

        // Verifica se os Value Objects (Email e Status) voltaram corretos
        assertThat(emprestimoRecuperado.getUsuario().getEmail().toString()).isEqualTo("mandagascar@julien.com");

    }
}


