package shokunin.group.com.biblioteca.application;

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
//
//        try {
//            System.out.println("Creating book...");
//            Book book = new Book.BookBuilder("O Senhor dos Anéis", "978-3-16-148410-0", "J.R.R. Tolkien", 1954)
//                    .comGenero("Fantasia")
//                    .comDisponibilidade(true)
//                    .build();
//            Periodico revista = new Periodico.PeriodicoBuilder("VEJA", "123456789")
//                    .comEditora("Abril")
//                    .comDataPublicacao(LocalDate.now())
//                    .comNumero(1)
//                    .build();
//
//            List<LibraryItem> items = new ArrayList<>();
//            items.add(book);
//            items.add(revista);
//
//            items.forEach(item -> {
//                System.out.println("------------------------------------------------");
//                Map<String,String> detalhes = item.getDetalhes();
//                detalhes.forEach((chave,valor) -> System.out.println(chave + ": " + valor));
//
//            });
//
//        } catch (Exception e) {
//            System.err.println("Error creating book: " + e.getMessage());
//            e.printStackTrace();
//
//        }
//        EmprestimoService service = new EmprestimoService(List.of(
//                new ALunoEmprestimoStrategy(),
//                new FuncionarioEmprestimoStrategy()
//        ), new EmprestimoRepository());
//
//        Unidade unidade = new Unidade.UnidadeBuilder()
//                .comId(1)
//                .comNome("Senai Tubarão")
//                .comEndereco("Marcolino Martins Cabral,123 Centro - Tubarão - SC")
//                .comTelefone("47999999999")
//                .comEmail("senai@senai.com")
//                .build();
//
//        Usuario aluno = new Aluno.AlunoBuilder("Julia Mota", "123456789",unidade)
//                .comAtivo(true)
//                .comMatricula("123456789")
//                .comNivelEnsino(NivelEnsino.TECNOLOGO)
//                .comEmail("julia@julia.com")
//                .comSenha("123456")
//                .comTelefone("49 99854-1285")
//                .build();
//
//        Book book = new Book.BookBuilder("O Teatro dos Vampiros","978-3-16-148410-0", "J.R.R. Tolkien", 1954)
//                .comGenero("Fantasia")
//                .comDisponibilidade(true)
//                .build();
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        Periodico revista = new Periodico.PeriodicoBuilder("VEJA", "123-4-5678-9")
//                .comEditora("Abril")
//                .comDataPublicacao(LocalDate.parse("01/09/2024", formatter))
//                .comNumero(187)
//                .build();
//
//        try{
//            Emprestimo emprestimo = service.processarEmprestimo(aluno,book);
//
//
//            // Simulando  Devolução com Atraso
//            System.out.println("\n--- Processando Devolução (Simulando Atraso) ---");
//            LocalDate dataRealDevolucao = LocalDate.now().plusDays(10);
//
//            double multa = service.processarDevolucao(emprestimo, dataRealDevolucao);
//
//            //Resumo final
//            emprestimo.getDetalhes().entrySet().forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
//            System.out.println("Status final do livro: " + (book.isDisponivel() ? "Disponível" : "Emprestado"));
//
//
//        } catch (ItemIndisponivelException e){
//            System.err.println("Erro ao processar emprestimo: " + e.getMessage());
//            e.getCodigoErro();
//        }






    }
}