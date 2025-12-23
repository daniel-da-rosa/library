package shokunin.group.com.biblioteca.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
    private static final String URL = "jdbc:sqlite:./biblioteca.db";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS books (\n" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  TITULO TEXT NOT NULL, \n" +
                "   AUTOR TEXT NOT NULL, \n" +
                "   ANO_PUBLICACAO INTEGER NOT NULL, \n" +
                "   GENERO TEXT NOT NULL, \n" +
                "   ISBN TEXT NOT NULL, \n" +
                "   DISPONIVEL BOOLEAN NOT NULL DEFAULT TRUE"+
                ")";

        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            throw new RuntimeException(e);

        }

    }
}
