package shokunin.group.com.biblioteca.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnector {
    private static final HikariDataSource dataSource;

    static {
        try {
            // Isso força a JVM do Linux a carregar o driver do SQLite
            Class.forName("org.xerial.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC não encontrado no Classpath do WSL!");
            throw new RuntimeException(e);
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:./biblioteca.db");
        config.setDriverClassName("org.xerial.sqlite.JDBC");
        //configurar setMaximumPoolSize para 10
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
        createTables();
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public static void createTables(){
        String sqlBooks = """
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                TITULO TEXT NOT NULL,
                AUTOR TEXT NOT NULL,
                ANO_PUBLICACAO INTEGER NOT NULL,
                GENERO TEXT NOT NULL,
                DISPONIVEL BOOLEAN NOT NULL DEFAULT TRUE, -- Adicionada vírgula e default
                ISBN TEXT NOT NULL
            );
            """;

        String sqlEmprestimos = """
            CREATE TABLE IF NOT EXISTS emprestimos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                USUARIO_NOME TEXT NOT NULL,
                ITEM_TITULO TEXT NOT NULL, -- Importante para sabermos o que foi emprestado
                DATA_EMPRESTIMO TEXT NOT NULL, -- SQLite usa TEXT para datas
                DATA_DEVOLUCAO TEXT NOT NULL
            );
            """;

        try(Connection conn = getConnection(); Statement stmt = conn.createStatement()){
            stmt.execute(sqlBooks);
            stmt.execute(sqlEmprestimos);
            System.out.println("Tables created successfully");

        }catch (SQLException e){
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
}
