package shokunin.group.com.biblioteca.application;

import shokunin.group.com.biblioteca.domain.Book;
import shokunin.group.com.biblioteca.domain.LibraryItem;
import shokunin.group.com.biblioteca.domain.Periodico;
import shokunin.group.com.biblioteca.util.DBConnector;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

//        try (Connection conn = DBConnector.getConnection()) {
//            if(conn != null){
//                System.out.println("Connected to the database.");
//                DBConnector.createTables();
//
//            }
//        } catch (Exception e) {
//            System.err.println("Error connecting to the database: " + e.getMessage());
//            e.printStackTrace();
//        }

        try {
            System.out.println("Creating book...");
            Book book = new Book.BookBuilder("O Senhor dos Anéis", "978-3-16-148410-0", "J.R.R. Tolkien", 1954)
                    .comGenero("Fantasia")
                    .comDisponibilidade(true)
                    .build();
            Periodico revista = new Periodico.PeriodicoBuilder("VEJA", "123456789")
                    .comEditora("Abril")
                    .comDataPublicacao(LocalDate.now())
                    .comNumero(1)
                    .build();

            List<LibraryItem> items = new ArrayList<>();
            items.add(book);
            items.add(revista);

            items.forEach(item -> {
                System.out.println("------------------------------------------------");
                Map<String,String> detalhes = item.getDetalhes();
                detalhes.forEach((chave,valor) -> System.out.println(chave + ": " + valor));

            });

        } catch (Exception e) {
            System.err.println("Error creating book: " + e.getMessage());
            e.printStackTrace();

        }




    }
}