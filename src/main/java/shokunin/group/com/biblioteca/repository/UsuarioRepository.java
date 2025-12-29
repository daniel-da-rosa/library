package shokunin.group.com.biblioteca.repository;

import shokunin.group.com.biblioteca.domain.users.Email;
import shokunin.group.com.biblioteca.domain.users.enums.NivelEnsino;
import shokunin.group.com.biblioteca.domain.unidades.Unidade;
import shokunin.group.com.biblioteca.domain.users.Aluno;
import shokunin.group.com.biblioteca.domain.users.Funcionario;
import shokunin.group.com.biblioteca.domain.users.Usuario;
import shokunin.group.com.biblioteca.exceptions.repository.RepositoryExceptionFactory;
import shokunin.group.com.biblioteca.util.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {

    // ===== SALVAR =====

    public void salvar(Usuario usuario) {
        validarUsuario(usuario);

        String sql = """
            INSERT INTO usuarios (NOME, EMAIL, SENHA, FONE, DOCUMENTO, 
                                  MATRICULA_REGISTRO, TIPO, UNIDADE_ID, ATIVO, NIVEL_ENSINO)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setCamposComuns(pstmt, usuario);
            setCamposEspecificos(pstmt, usuario);
            executarInsert(pstmt, usuario);

        } catch (SQLException e) {
            throw  RepositoryExceptionFactory.falhaPersistencia("Usuario", e);
        }
    }

    // ===== BUSCAR POR ID =====

    public Optional<Usuario> buscarUsuarioPorId(Integer id) {
        if (id == null) return Optional.empty();

        String sql = """
            SELECT u.*,
                   un.id AS unidadeId,
                   un.nome AS unidadeNome,
                   un.endereco AS unidadeEndereco,
                   un.telefone AS unidadeTelefone
              FROM usuarios u
              JOIN unidades un ON u.UNIDADE_ID = un.id
             WHERE u.id = ?
            """;

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? extrairUsuarioDoResult(rs) : Optional.empty();
            }

        } catch (SQLException e) {
            throw  RepositoryExceptionFactory.falhaConsultaDetalhada("Usuário: " + id, e);
        }
    }

    // ===== LISTAR TODOS =====

    public List<Usuario> retornarTodosUsuarios() {
        String sql = """
            SELECT u.*,
                   un.id AS unidadeId,
                   un.nome AS unidadeNome,
                   un.endereco AS unidadeEndereco,
                   un.telefone AS unidadeTelefone
              FROM usuarios u
              JOIN unidades un ON u.id_unidade = un.id
            """;

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                extrairUsuarioDoResult(rs).ifPresent(usuarios::add);
            }

        } catch (SQLException e) {
            throw RepositoryExceptionFactory.falhaConsulta ("Todos usuários", e);
        }

        return usuarios;
    }

    // ===== MÉTODOS PRIVADOS - VALIDAÇÃO E EXECUÇÃO =====

    private void validarUsuario(Usuario usuario) {
        if (usuario.getUnidade() == null || usuario.getUnidade().getId() == null) {
            throw new IllegalStateException("Não é possível salvar usuário sem ID da Unidade");
        }
    }

    private void executarInsert(PreparedStatement pstmt, Usuario usuario) throws SQLException {
        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        }
    }

    // ===== MÉTODOS PRIVADOS - MAPEAMENTO PARA INSERT =====

    private void setCamposComuns(PreparedStatement pstmt, Usuario usuario) throws SQLException {
        pstmt.setString(1, usuario.getNome());
        pstmt.setString(2, usuario.getEmail().toString());
        pstmt.setString(3, usuario.getSenha());
        pstmt.setString(4, usuario.getTelefone());
        pstmt.setString(5, usuario.getDocumento());
        pstmt.setInt(8, usuario.getUnidade().getId());
        pstmt.setBoolean(9, usuario.isAtivo());
    }

    private void setCamposEspecificos(PreparedStatement pstmt, Usuario usuario) throws SQLException {
        switch (usuario) {
            case Aluno a -> {
                pstmt.setString(6, a.getMatricula());
                pstmt.setString(7, "ALUNO");
                pstmt.setString(10, a.getNivelEnsino());
            }
            case Funcionario f -> {
                pstmt.setString(6, f.getRegistro());
                pstmt.setString(7, "FUNCIONARIO");
                pstmt.setNull(10, Types.VARCHAR);
            }
            default -> throw new IllegalStateException(
                    "Tipo de usuário não suportado: " + usuario.getClass().getSimpleName());
        }
    }

    // ===== MÉTODOS PRIVADOS - MAPEAMENTO DE RESULTSET =====

    private Optional<Usuario> extrairUsuarioDoResult(ResultSet rs) throws SQLException {
        Unidade unidade = construirUnidade(rs);
        String tipo = rs.getString("TIPO");
        return montarUsuario(tipo, rs, unidade);
    }

    private Unidade construirUnidade(ResultSet rs) throws SQLException {
        return Unidade.builder()
                .comId(rs.getInt("unidadeId"))
                .comNome(rs.getString("unidadeNome"))
                .comEndereco(rs.getString("unidadeEndereco"))
                .comTelefone(rs.getString("unidadeTelefone"))
                .build();
    }

    private Optional<Usuario> montarUsuario(String tipo, ResultSet rs, Unidade unidade) throws SQLException {
        return Optional.ofNullable(switch (tipo) {
            case "ALUNO" -> construirAluno(rs, unidade);
            case "FUNCIONARIO" -> construirFuncionario(rs, unidade);
            default -> throw new IllegalStateException("Tipo de usuário desconhecido no banco: " + tipo);
        });
    }

    private Aluno construirAluno(ResultSet rs, Unidade unidade) throws SQLException {
        String nivelStr = rs.getString("NIVEL_ENSINO");
        NivelEnsino nivelEnsino = (nivelStr != null) ? NivelEnsino.valueOf(nivelStr) : null;

        return new Aluno.AlunoBuilder(
                rs.getString("NOME"),
                rs.getString("DOCUMENTO"),
                unidade,
                Email.of(rs.getString("EMAIL")))
                .comId(rs.getInt("id"))
                .comMatricula(rs.getString("MATRICULA_REGISTRO"))
                .comNivelEnsino(nivelEnsino)
                .comAtivo(rs.getBoolean("ATIVO"))
                .comTelefone(rs.getString("FONE"))
                .comSenha(rs.getString("SENHA"))
                .build();
    }

    private Funcionario construirFuncionario(ResultSet rs, Unidade unidade) throws SQLException {
        return new Funcionario.FuncionarioBuilder(
                rs.getString("NOME"),
                rs.getString("DOCUMENTO"),
                unidade,
                Email.of(rs.getString("EMAIL")))
                .comId(rs.getInt("id"))
                .comRegistro(rs.getString("MATRICULA_REGISTRO"))
                .comAtivo(rs.getBoolean("ATIVO"))
                .comTelefone(rs.getString("FONE"))
                .comSenha(rs.getString("SENHA"))
                .build();
    }
}