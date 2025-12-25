package shokunin.group.com.biblioteca.repository;

import shokunin.group.com.biblioteca.domain.Emprestimo;
import shokunin.group.com.biblioteca.util.DBConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmprestimoRepository {

    public void salvarEmprestimo(Emprestimo emprestimo){
        String sql = "INSERT INTO emprestimos (USUARIO_NOME, ITEM_TITULO, DATA_EMPRESTIMO, DATA_DEVOLUCAO) VALUES (?, ?, ?, ?)";

        //Usando o Pool de conexão - evita de ficar abrindo e fechando conexão a cada operção.

        try(Connection conn = DBConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1,emprestimo.getUsuario().getNome());
                pstmt.setString(2,emprestimo.getItem().getTitulo());
                pstmt.setString(3,emprestimo.getDataEmprestimo().toString());
                pstmt.setString(4,emprestimo.getDataPrevistaDevolucao().toString());
                pstmt.executeUpdate();
            System.out.println("Emprestimo salvo com sucesso");
    }catch (SQLException e){
        System.out.println("Erro ao salvar emprestimo: " + e.getMessage());
        }
    }
}
