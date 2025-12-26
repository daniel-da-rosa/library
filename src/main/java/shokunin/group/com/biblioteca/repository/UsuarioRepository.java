package shokunin.group.com.biblioteca.repository;

import shokunin.group.com.biblioteca.domain.Aluno;
import shokunin.group.com.biblioteca.domain.Funcionario;
import shokunin.group.com.biblioteca.domain.Usuario;
import shokunin.group.com.biblioteca.domain.Unidade;
import shokunin.group.com.biblioteca.domain.enums.NivelEnsino;
import shokunin.group.com.biblioteca.util.DBConnector;

import java.sql.*;
import java.util.Optional;

public class UsuarioRepository {

    private final UnidadeRepository unidadeRepository = new UnidadeRepository();
    /*
    * Salva um Aluno funcionario usando o polimorfismo do java.
    * */

    public void salvar(Usuario usuario){
        String sql = """
                INSERT INTO usuarios (NOME, EMAIL, SENHA, FONE, DOCUMENTO, MATRICULA_REGISTRO, TIPO, UNIDADE_ID, ATIVO, NIVEL_ENSINO)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try( Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1,usuario.getNome());
            pstmt.setString(2,usuario.getEmail());
            pstmt.setString(3,usuario.getSenha());
            pstmt.setString(4,usuario.getTelefone());
            pstmt.setString(5,usuario.getDocumento());
            //Aqui o polimorfismo - classe não sabe se é um aluno ou funcionario, ou professor.. tipo e matriculaRegistro são definidos nas classes filhas.
            pstmt.setString(6,usuario.getMatriculaRegistro());
            pstmt.setString(7,usuario.getTipo());

            if (usuario.getUnidade().getId() == null){
                throw new IllegalStateException(" Não é possivel salvar um usuário sem a id da Unidade");
            }

            pstmt.setInt(8,usuario.getUnidade().getId());
            pstmt.setBoolean(9, usuario.isAtivo());
            pstmt.setString(10,usuario.getNivelEnsino());//todo implementar o nivel de ensino no funcionario - hoje devolve null

            int affectedRow = pstmt.executeUpdate();

            if (affectedRow > 0){
                try(ResultSet rs = pstmt.getGeneratedKeys()){
                    if (rs.next()){
                        usuario.setId(rs.getInt(1));
                        System.out.println("[Repo] " + usuario.getTipo() + " '" + usuario.getNome() + "' salvo com ID: " + usuario.getId());
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o usuario :"+e.getMessage());
        }

    }

    /*
    * busca e reconstroi o objeto no caso Usuario de acordo com o tipo ;)
    */

    public Optional<Usuario>buscarPorId(Integer id) throws SQLException {
        if (id == null) return Optional.empty();


        String sql = "select * from usuarios where id = ?";

        try (Connection conn  = DBConnector.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    String tipo = rs.getString("TIPO");
                    Integer unidadeId = rs.getInt("UNIDADE_ID");

                    Unidade unidade = unidadeRepository.buscaPorId(unidadeId)
                            .orElseThrow(()-> new RuntimeException("Unidade ID :"+ unidadeId +" não encontrada!"));
                    if ("ALUNO".equals(tipo)) {
                        String nivelStr = rs.getString("NIVEL_ENSINO");
                        NivelEnsino nivelEnum = (nivelStr !=null)?NivelEnsino.valueOf(nivelStr):null;

                        return Optional.of(new Aluno.AlunoBuilder(rs.getString("NOME"), rs.getString("DOCUMENTO"), unidade)
                                .comId(rs.getInt("id"))
                                .comEmail(rs.getString("EMAIL"))
                                .comMatricula(rs.getString("MATRICULA_REGISTRO"))
                                .comNivelEnsino(nivelEnum)
                                .comAtivo(rs.getBoolean("ATIVO"))
                                .comTelefone(rs.getString("FONE"))
                                .build());
                    } else {
                        return Optional.of(new Funcionario.FuncionarioBuilder(rs.getString("NOME"), rs.getString("DOCUMENTO"), unidade)
                                .comId(rs.getInt("id"))
                                .comEmail(rs.getString("EMAIL"))
                                .comRegistro(rs.getString("MATRICULA_REGISTRO"))
                                .comAtivo(rs.getBoolean("ATIVO"))
                                .comTelefone(rs.getString("FONE"))
                                .build());
                    }
                }


        }catch (SQLException e){
            throw  new RuntimeException("Erro "+e.getMessage());


        }


        }
        return Optional.empty();
    }
}
