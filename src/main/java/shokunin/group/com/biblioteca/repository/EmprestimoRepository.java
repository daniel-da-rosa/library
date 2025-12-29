package shokunin.group.com.biblioteca.repository;

import shokunin.group.com.biblioteca.domain.emprestimos.Emprestimo;
import shokunin.group.com.biblioteca.domain.itens.LibraryItem;
import shokunin.group.com.biblioteca.domain.users.Usuario;
import shokunin.group.com.biblioteca.exceptions.repository.RepositoryExceptionFactory;
import shokunin.group.com.biblioteca.util.DBConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmprestimoRepository {

    private UsuarioRepository usuarioRepository = new UsuarioRepository();
    private ItemLibraryRepository itemLibraryRepository = new ItemLibraryRepository();

    public void criarEmprestimo(Emprestimo emprestimo){
        String sql = "INSERT INTO emprestimos (USUARIO_ID, ITEMLIBRARY_ID, DATA_EMPRESTIMO, DATA_DEVOLUCAO_PREVISTA,DATA_DEVOLUCAO_REAL,MULTA,STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";

        //Usando o Pool de conexão - evita de ficar abrindo e fechando conexão a cada operção.

        try(Connection conn = DBConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setInt(1,emprestimo.getUsuario().getId());
                pstmt.setInt(2,emprestimo.getItem().getId());
                pstmt.setString(3,emprestimo.getDataEmprestimo().toString());
                pstmt.setString(4,emprestimo.getDataPrevistaDevolucao().toString());
                pstmt.setString(5,null);
                pstmt.setBigDecimal(6,emprestimo.getMulta());
                pstmt.setString(7,"ABERTO");
                pstmt.executeUpdate();
            System.out.println("Emprestimo salvo com sucesso");
    }catch (SQLException e){
        System.out.println("Erro ao salvar emprestimo: " + e.getMessage());
        }
    }

    public void alterarEmprestimo(Emprestimo emprestimo) {

        String sql = "UPDATE emprestimos SET DATA_DEVOLUCAO_REAL = ?, MULTA = ?, STATUS = ? WHERE ID = ?";

        try(Connection conn = DBConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            if (emprestimo.getDevolucaoEfetiva() !=null){
                pstmt.setDate(1,java.sql.Date.valueOf(emprestimo.getDevolucaoEfetiva().toString()));
            } else{
                pstmt.setNull(1,java.sql.Types.DATE);
            }
            pstmt.setBigDecimal(2,emprestimo.getMulta());
            pstmt.setString(3,"FINALIZADO");
            pstmt.setInt(4,emprestimo.getId());
            pstmt.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException("Erro ao alterar emprestimo: " + e.getMessage());
        }
    }

    public Optional<Emprestimo> buscarEmprestimoPorId(Integer id) {

        if (id == null) return Optional.empty();

        String sqlEmprestimo = """
            select id, 
                   usuario_id,
                   itemLibrary_id,
                   data_emprestimo,
                   data_devolucao_prevista,
                   DATA_DEVOLUCAO_REAL,
                   MULTA,status 
              from emprestimos
             WHERE id = ?
            """;

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlEmprestimo)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    // 1. Busca as dependências (Usuario e Item)
                    Usuario usuario = usuarioRepository.buscarUsuarioPorId(rs.getInt("usuario_id"))
                            .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

                    LibraryItem item = itemLibraryRepository.buscarPorId(rs.getInt("itemLibrary_id"))
                            .orElseThrow(() -> new RuntimeException("Item nao encontrado"));

                    // 2. Inicia o Builder com as datas convertidas via String (Evita o ParseException do SQLite)
                    Emprestimo.EmprestimoBuilder builder = new Emprestimo.EmprestimoBuilder(usuario, item)
                            .comDataEmprestimo(LocalDate.parse(rs.getString("data_emprestimo")))
                            .comDataPrevistaDevolucao(LocalDate.parse(rs.getString("data_devolucao_prevista")))
                            .comMulta(rs.getBigDecimal("MULTA"))
                            .comId(rs.getInt("id"));

                    // 3. Busca a data de devolução real (pode ser nula se o empréstimo estiver aberto)
                    String dataRealStr = rs.getString("DATA_DEVOLUCAO_REAL");
                    if (dataRealStr != null && !dataRealStr.isEmpty()) {
                        builder.comDataEfetiva(LocalDate.parse(dataRealStr));
                    }

                    // 4. Retorna o objeto construído
                    return Optional.of(builder.build());
                }
            }
        } catch (SQLException e) {
            throw RepositoryExceptionFactory.falhaConsultaDetalhada("Emprestimo: " + id, e);
        }
        return Optional.empty();
    }





}
