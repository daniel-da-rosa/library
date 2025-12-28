package shokunin.group.com.biblioteca.repository;
import shokunin.group.com.biblioteca.domain.unidades.Unidade;
import shokunin.group.com.biblioteca.util.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UnidadeRepository {
    /*
    * Salva uma nova Unidade no banco e atualiza o objeto com o id gerado.
    * Lembrando que isso está sendo feito como os antigo incas, com o springboot ou hibernate é beeem mais simples.
    * */

    public void salvar(Unidade unidade){
        String sql = "Insert into unidades (nome,endereco,telefone)values (?,?,?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS))
        {
            pstmt.setString(1, unidade.getNome());
            pstmt.setString(2, unidade.getEndereco());
            pstmt.setString(3, unidade.getTelefone());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0){
                try(ResultSet rs = pstmt.getGeneratedKeys()){
                    if(rs.next()){
                        //atualiza o objeto com a id do banco
                        unidade.setId(rs.getInt(1));
                        System.out.println("Unidade '"+ unidade.getNome()+" Salvo com a ID:"+unidade.getId());

                    }

                }

            }


        }catch (SQLException e)
        {
            throw  new RuntimeException("Erro ao salvar a unidade:"+e.getMessage());
        }


    }

    public Optional<Unidade>buscaPorId(Integer id){
        if(id == null) return Optional.empty();

        String sql = "select * from unidades where id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1,id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    Unidade unidade = new Unidade.UnidadeBuilder()
                            .comId(rs.getInt("id"))
                            .comNome(rs.getString("nome"))
                            .comEndereco(rs.getString("endereco"))
                            .comTelefone(rs.getString("telefone"))
                            .build();
                    return Optional.of(unidade);
                }
            }

        }catch (SQLException e){
            throw new RuntimeException("Erro ao buscar com id: "+ e.getMessage());
        }
        return Optional.empty();
    }

    public List<Unidade> listarTodos() {
        List<Unidade> lista = new ArrayList<>();
        String sql = " select * from unidades";

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Unidade.UnidadeBuilder()
                        .comId(rs.getInt("id"))
                        .comNome(rs.getString("nome"))
                        .comEndereco(rs.getString("endereco"))
                        .comTelefone(rs.getString("telefone"))
                        .build());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar  unidades:" + e.getMessage());
        }
        return  lista;
    }
}
