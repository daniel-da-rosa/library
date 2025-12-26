package shokunin.group.com.biblioteca.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shokunin.group.com.biblioteca.domain.*;
import shokunin.group.com.biblioteca.domain.enums.NivelEnsino;
import shokunin.group.com.biblioteca.repository.EmprestimoRepository;
import shokunin.group.com.biblioteca.repository.UnidadeRepository;
import shokunin.group.com.biblioteca.repository.UsuarioRepository;
import shokunin.group.com.biblioteca.strategy.ALunoEmprestimoStrategy;
import shokunin.group.com.biblioteca.strategy.FuncionarioEmprestimoStrategy;
import shokunin.group.com.biblioteca.exceptions.ItemIndisponivelException;

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

    @BeforeEach
    void setup(){

         unidadeRepository = new UnidadeRepository();
         usuarioRepository = new UsuarioRepository();
         EmprestimoRepository emprestimoRepository = new EmprestimoRepository();

         //carrega as regras para dentro do service e repositorio para persistir os dados
        service = new EmprestimoService(List.of(
                  new ALunoEmprestimoStrategy(),
                  new FuncionarioEmprestimoStrategy()
        ),emprestimoRepository);

        //cria os objetos necessarios para os testes
        Unidade unidade = new Unidade.UnidadeBuilder()
                .comNome("Escola Senai")
                .comEndereco("Rua Senai, 123")
                .comTelefone("123456789")
                .comEmail("escola@senai.com")
                .build();

        //Persiste no banco de dados
        unidadeRepository.salvar(unidade);


        aluno = new Aluno.AlunoBuilder("Moto Moto","1234",unidade)
                .comMatricula("Mat -2025-001")
                .comNivelEnsino(NivelEnsino.TECNOLOGO)
                .comEmail("mandagascar@julien.com")
                .comTelefone("31 9 9765-0983")
                .build();

        usuarioRepository.salvar(aluno);

        livro = new Book.BookBuilder("O Senhor dos Anéis","978-3-16-148410-0","J.R.R. Tolkien",1954)
                .comGenero("Fantasia")
                .comDisponibilidade(true)
                .build();
        //todo criar repositorio do itemLibrary itemLibrayRepository.salvar(livro);

        livro.setId(1);
    }
    @Test
    @DisplayName("Deve calcular a multa corretamente quando houver atraso")
    void deveCalcularMultaComAtraso(){
        Emprestimo emprestimo = service.processarEmprestimo(aluno,livro);

        //simula a devolucao com atraso
        LocalDate dataDevolucao = LocalDate.now().plusDays(10);
        double multa = service.processarDevolucao(emprestimo,dataDevolucao);

        //verifica se a multa foi calculada corretamente
        assertThat(multa).isGreaterThan(0); // verifica se a multa foi calculada corretamente. É auto explicativo mas ajuda a entender o que está acontecendo.
        assertThat(emprestimo.getMulta()).isEqualTo(multa);
        assertThat(livro.isDisponivel()).isTrue(); //verifica se o livro foi devolvido corretamente

    }
    @Test
    @DisplayName("Deve impedir que um livro já empresatdo (disponivel = false) seja emprestado novamente")
    void deveValidarLivroIndisponivel(){
        livro.setDisponivel(false);

        //teste de emprestimo
        assertThatThrownBy(() -> service.processarEmprestimo(aluno,livro))
                .isInstanceOf(ItemIndisponivelException.class)
                .isInstanceOf(ItemIndisponivelException.class); //valida se a exceção foi lancada

    }
}
