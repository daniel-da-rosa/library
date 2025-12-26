package shokunin.group.com.biblioteca.repository;

import shokunin.group.com.biblioteca.domain.Emprestimo;
import shokunin.group.com.biblioteca.util.DBConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmprestimoRepository {

    public void salvarEmprestimo(Emprestimo emprestimo){
        String sql = "INSERT INTO emprestimos (USUARIO_ID, ITEMLIBRARY_ID, DATA_EMPRESTIMO, DATA_DEVOLUCAO_PREVISTA,DATA_DEVOLUCAO_REAL,MULTA,STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";

        //Usando o Pool de conexão - evita de ficar abrindo e fechando conexão a cada operção.

        try(Connection conn = DBConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setInt(1,emprestimo.getUsuario().getId());
                pstmt.setInt(2,emprestimo.getItem().getId());
                pstmt.setString(3,emprestimo.getDataEmprestimo().toString());
                pstmt.setString(4,emprestimo.getDataPrevistaDevolucao().toString());
                pstmt.setString(5,null);
                pstmt.setDouble(6,emprestimo.getMulta());
                pstmt.setString(7,"ABERTO");
                pstmt.executeUpdate();
            System.out.println("Emprestimo salvo com sucesso");
    }catch (SQLException e){
        System.out.println("Erro ao salvar emprestimo: " + e.getMessage());
        }
    }
}
