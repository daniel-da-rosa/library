package shokunin.group.com.biblioteca.repository;

import shokunin.group.com.biblioteca.domain.itens.Book;
import shokunin.group.com.biblioteca.domain.itens.LibraryItem;
import shokunin.group.com.biblioteca.domain.itens.StatusItemLibrary;
import shokunin.group.com.biblioteca.util.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ItemLibraryRepository {

    public Optional<LibraryItem> buscarPorId(Integer id) {
        String sql = "SELECT * FROM itemLibrary WHERE id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extrairItemDoResult(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar item: " + id, e);
        }
        return Optional.empty();
    }

    private LibraryItem extrairItemDoResult(ResultSet rs) throws SQLException {
        // 1. Recuperar o status do banco e converter para o Value Object do domínio
        String statusDb = rs.getString("status");
        StatusItemLibrary status = statusDb.equalsIgnoreCase("DISPONIVEL")
                ? StatusItemLibrary.disponivel()
                : StatusItemLibrary.emprestado();

        // 2. Usar o Builder do Livro (ou a lógica de tipo de item)
        return new Book.BookBuilder(
                rs.getString("titulo"),
                rs.getString("ISBN_ISSN"),
                rs.getString("autor"),
                rs.getInt("data_ano_publicacao"),
                status
        )
                .comId(rs.getInt("id"))
                .comGenero(rs.getString("genero"))
                .build();
    }
}
