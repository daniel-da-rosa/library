package shokunin.group.com.biblioteca.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {

    private static final HikariDataSource datasource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:./biblioteca.db");//caminho e nome do banco. se não existir ele cria.
        config.setDriverClassName("org.sqlite.JDBC");
        config.setConnectionInitSql("PRAGMA foreign_keys = ON;");
        //seta o tamanho do pool de conexão, numero de conexões mantidas abertas
        config.setMaximumPoolSize(10);
        datasource = new HikariDataSource(config);
        createTables();
    }

    public static Connection getConnection() throws  SQLException{
        return datasource.getConnection();
    }

    public static void createTables(){

            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

                // 1. Tabela Pai: UNIDADES
                stmt.execute("""
                CREATE TABLE IF NOT EXISTS unidades (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    endereco TEXT,
                    telefone TEXT
                );
            """);

                // 2. Tabela Filha: USUARIOS (Depende de Unidade)
                stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    NOME TEXT NOT NULL,
                    EMAIL TEXT,
                    SENHA TEXT,
                    FONE text,
                    DOCUMENTO TEXT,
                    MATRICULA_REGISTRO TEXT NOT NULL,
                    TIPO TEXT NOT NULL, -- 'ALUNO' ou 'FUNCIONARIO'
                    UNIDADE_ID INTEGER NOT NULL,
                    ATIVO BOOLEAN DEFAULT TRUE,
                    NIVEL_ENSINO TEXT,
                    FOREIGN KEY (UNIDADE_ID) REFERENCES UNIDADES(ID)
                );
            """);

                // 3. Tabela Filha: itemLibrary (Depende de Unidade)
                stmt.execute("""
                CREATE TABLE IF NOT EXISTS itemLibrary (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    EDITORA TEXT,
                    TITULO TEXT NOT NULL,
                    GENERO TEXT,
                    DATA_ANO_PUBLICACAO TEXT,
                    AUTOR TEXT,
                    ISBN_ISSN TEXT NOT NULL,
                    DISPONIVEL BOOLEAN DEFAULT TRUE,
                    NUMERO INTEGER,
                    UNIDADE_ID INTEGER,
                    FOREIGN KEY (unidade_id) REFERENCES unidades(id)
                );
            """);

                // 4. Tabela de Ligação: EMPRESTIMOS (Depende de Usuario e Book)
                stmt.execute("""
                CREATE TABLE IF NOT EXISTS emprestimos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    usuario_id INTEGER NOT NULL,
                    itemLibrary_id INTEGER NOT NULL,
                    data_emprestimo TEXT NOT NULL,
                    data_devolucao_prevista TEXT NOT NULL,
                    DATA_DEVOLUCAO_REAL TEXT,
                    MULTA FLOAT DEFAULT 0.0,
                    status TEXT DEFAULT 'ABERTO',
                    
                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                    FOREIGN KEY (itemLibrary_id) REFERENCES itemLibrary(id)
                );
            """);

                System.out.println("[DB] Tabelas relacionais sincronizadas com sucesso.");

            } catch (SQLException e) {
                System.err.println("Erro ao criar estrutura do banco: " + e.getMessage());
                throw new RuntimeException(e);
            }

    }
}
