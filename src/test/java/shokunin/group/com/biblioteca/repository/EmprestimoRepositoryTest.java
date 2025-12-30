package shokunin.group.com.biblioteca.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shokunin.group.com.biblioteca.domain.emprestimos.Emprestimo;
import shokunin.group.com.biblioteca.domain.itens.Book;
import shokunin.group.com.biblioteca.domain.itens.StatusItemLibrary;
import shokunin.group.com.biblioteca.domain.unidades.Unidade;
import shokunin.group.com.biblioteca.domain.users.Aluno;
import shokunin.group.com.biblioteca.domain.users.Email;
import shokunin.group.com.biblioteca.domain.users.enums.NivelEnsino;
import shokunin.group.com.biblioteca.util.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EmprestimoRepositoryTest {

    private EmprestimoRepositorySQLite repository;
    private UsuarioRepository usuarioRepository;
    private UnidadeRepository unidadeRepository;

    // Precisamos guardar esses objetos para usar seus IDs nos testes
    private Aluno alunoSalvo;
    private Book livroSalvo;

    @BeforeEach
    void setup() {
        // 1. Garante que as tabelas existem (Criação do Schema)
        DBConnector.createTables();

        // 2. Limpa as tabelas para garantir um teste limpo (Teardown no início)
        limparBancoDeDados();

        repository = new EmprestimoRepositorySQLite();
        usuarioRepository = new UsuarioRepository();
        unidadeRepository = new UnidadeRepository();

        // 3. PREPARA O TERRENO (Dependências de Foreign Key)
        // Precisamos de uma Unidade real no banco
        Unidade unidade = new Unidade.UnidadeBuilder()
                .comNome("Senai Teste DB")
                .comEndereco("Rua do SQL, 100")
                .comEmail("sql@senai.br")
                .comTelefone("48 9999-0000")
                .build();
        unidadeRepository.salvar(unidade); // Assume que este método salva no banco

        // Precisamos de um Aluno real no banco
        alunoSalvo = new Aluno.AlunoBuilder("Tester Raiz", "12345", unidade, Email.of("raiz@teste.com"))
                .comMatricula("2025-DB")
                .comNivelEnsino(NivelEnsino.TECNOLOGO)
                .build();
        usuarioRepository.salvar(alunoSalvo); // Assume que salva e gera ID

        // Precisamos de um Livro real (Simulando inserção se não tiver repo de itens ainda)
        livroSalvo = new Book.BookBuilder("SQL Performance", "978-000", "Oracle Expert", 2024, StatusItemLibrary.disponivel())
                .comGenero("Tecnologia")
                .build();
        salvarLivroNaMao(livroSalvo); // Método auxiliar logo abaixo
    }

    @Test
    @DisplayName("INTEGRAÇÃO: Deve inserir no SQLite, gerar ID e recuperar os dados com integridade")
    void deveSalvarERecuperarDoBanco() {
        // 1. CENÁRIO
        Emprestimo emprestimoOriginal = new Emprestimo.EmprestimoBuilder(alunoSalvo, livroSalvo)
                .comDataEmprestimo(LocalDate.now())
                .comPrazo(7) // Vai calcular a data prevista
                .build();

        // 2. AÇÃO (O Teste Real)
        // Chama o método que acabamos de refatorar. Ele DEVE retornar o ID.
        Integer idGerado = repository.criarEmprestimo(emprestimoOriginal);

        // 3. VALIDAÇÃO (Prova Real)

        // Verificação A: O Banco gerou uma chave primária?
        assertThat(idGerado).isNotNull().isGreaterThan(0);
        System.out.println("ID Gerado pelo SQLite: " + idGerado);

        // Verificação B: O dado está lá fisicamente? Vamos buscar.
        Optional<Emprestimo> recuperadoOpt = repository.buscarEmprestimoPorId(idGerado);
        assertThat(recuperadoOpt).isPresent();

        Emprestimo recuperado = recuperadoOpt.get();

        // Verificação C: Integridade dos Dados (O que entrou = O que saiu)
        assertThat(recuperado.getId()).isEqualTo(idGerado);
        assertThat(recuperado.getUsuario().getId()).isEqualTo(alunoSalvo.getId());
        assertThat(recuperado.getItem().getId()).isEqualTo(livroSalvo.getId());
        assertThat(recuperado.getDataEmprestimo()).isEqualTo(LocalDate.now());
    }

    // --- Métodos Auxiliares para o Teste ---

    private void limparBancoDeDados() {
        try (Connection conn = DBConnector.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = OFF");

            conn.createStatement().execute("DELETE FROM emprestimos");
            conn.createStatement().execute("DELETE FROM usuarios");
            conn.createStatement().execute("DELETE FROM unidades");
            conn.createStatement().execute("DELETE FROM itemlibrary");

            stmt.execute("PRAGMA foreign_keys = ON");
            // Adicionar delete de itens se tiver tabela separada
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao limpar banco de teste", e);
        }
    }

    // Como não vi seu ItemLibraryRepository, fiz esse helper pra garantir que o livro exista
    private void salvarLivroNaMao(Book livro) {
        String sql = "INSERT INTO itemlibrary (TITULO, ISBN_issn, AUTOR, DATA_ANO_PUBLICACAO, STATUS) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getIsbn());
            stmt.setString(3, livro.getAutor());
            stmt.setInt(4, livro.getAnoPublicacao());
            stmt.setString(5, "DISPONIVEL");
            stmt.executeUpdate();

            var rs = stmt.getGeneratedKeys();
            if (rs.next()) livro.setId(rs.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar livro de teste", e);
        }
    }
}