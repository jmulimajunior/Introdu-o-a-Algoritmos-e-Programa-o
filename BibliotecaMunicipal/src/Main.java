import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Ponto de entrada da aplicação. Apresenta um menu interactivo, em consola,
 * para gerir o inventário de livros e o histórico de empréstimos da
 * Biblioteca Municipal.
 *
 * @author Joaquim Pascoal Mulima Júnior
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Biblioteca biblioteca = new Biblioteca();

    public static void main(String[] args) {
        carregarDadosDemonstracao();

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcao = lerInteiro("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> registarLivro();
                case 2 -> listarCatalogo();
                case 3 -> pesquisarLivros();
                case 4 -> registarUtilizador();
                case 5 -> listarUtilizadores();
                case 6 -> efectuarEmprestimo();
                case 7 -> registarDevolucao();
                case 8 -> listarEmprestimos();
                case 9 -> mostrarEstatisticas();
                case 0 -> {
                    continuar = false;
                    System.out.println("\nA encerrar o sistema. Até breve!");
                }
                default -> System.out.println("\n[Erro] Opção inválida. Tente novamente.");
            }
        }
        scanner.close();
    }

    // ---------------------------------------------------------------
    // MENU
    // ---------------------------------------------------------------

    private static void mostrarMenu() {
        System.out.println("\n===================================================");
        System.out.println("   SISTEMA DE GESTÃO - BIBLIOTECA MUNICIPAL");
        System.out.println("===================================================");
        System.out.println(" 1. Registar novo livro");
        System.out.println(" 2. Listar catálogo completo");
        System.out.println(" 3. Pesquisar livro (por autor ou título)");
        System.out.println(" 4. Registar novo utilizador");
        System.out.println(" 5. Listar utilizadores");
        System.out.println(" 6. Efectuar empréstimo");
        System.out.println(" 7. Registar devolução");
        System.out.println(" 8. Listar histórico de empréstimos");
        System.out.println(" 9. Estatísticas");
        System.out.println(" 0. Sair");
        System.out.println("===================================================");
    }

    // ---------------------------------------------------------------
    // OPÇÃO 1 e 2 e 3 — LIVROS
    // ---------------------------------------------------------------

    private static void registarLivro() {
        System.out.println("\n--- Registo de novo livro ---");
        try {
            String titulo = lerTexto("Título: ");
            String autor = lerTexto("Autor: ");
            int ano = lerInteiro("Ano de publicação: ");
            int quantidade = lerInteiroPositivo("Quantidade de exemplares: ");

            Livro livro = biblioteca.registarLivro(titulo, autor, ano, quantidade);
            System.out.println("[OK] Livro registado com sucesso! " + livro);
        } catch (IllegalStateException e) {
            System.out.println("[Erro] " + e.getMessage());
        }
    }

    private static void listarCatalogo() {
        System.out.println("\n--- Catálogo de livros (" + biblioteca.getTotalLivros() + ") ---");
        Livro[] livros = biblioteca.listarLivros();
        if (livros.length == 0) {
            System.out.println("Ainda não há livros registados.");
            return;
        }
        for (Livro l : livros) {
            System.out.println(l);
        }
    }

    private static void pesquisarLivros() {
        System.out.println("\n--- Pesquisa de livros ---");
        System.out.println("1. Pesquisar por autor");
        System.out.println("2. Pesquisar por título");
        int opcao = lerInteiro("Escolha uma opção: ");

        if (opcao != 1 && opcao != 2) {
            System.out.println("[Erro] Opção inválida.");
            return;
        }
        String termo = lerTexto("Termo de pesquisa: ");
        Livro[] resultado = (opcao == 1)
                ? biblioteca.pesquisarPorAutor(termo)
                : biblioteca.pesquisarPorTitulo(termo);

        if (resultado.length == 0) {
            System.out.println("Nenhum livro encontrado para \"" + termo + "\".");
            return;
        }
        System.out.println("Foram encontrados " + resultado.length + " livro(s):");
        for (Livro l : resultado) {
            System.out.println(l);
        }
    }

    // ---------------------------------------------------------------
    // OPÇÃO 4 e 5 — UTILIZADORES
    // ---------------------------------------------------------------

    private static void registarUtilizador() {
        System.out.println("\n--- Registo de novo utilizador ---");
        try {
            String nome = lerTexto("Nome completo: ");
            String email = lerTexto("Email: ");
            Utilizador utilizador = biblioteca.registarUtilizador(nome, email);
            System.out.println("[OK] Utilizador registado com sucesso! " + utilizador);
        } catch (IllegalStateException e) {
            System.out.println("[Erro] " + e.getMessage());
        }
    }

    private static void listarUtilizadores() {
        System.out.println("\n--- Utilizadores registados ---");
        Utilizador[] utilizadores = biblioteca.listarUtilizadores();
        if (utilizadores.length == 0) {
            System.out.println("Ainda não há utilizadores registados.");
            return;
        }
        for (Utilizador u : utilizadores) {
            System.out.println(u);
        }
    }

    // ---------------------------------------------------------------
    // OPÇÃO 6 e 7 — EMPRÉSTIMOS
    // ---------------------------------------------------------------

    private static void efectuarEmprestimo() {
        System.out.println("\n--- Efectuar empréstimo ---");
        Integer livroId = lerIdValido("ID do livro (0 para cancelar): ", biblioteca::existeLivro, "livro");
        if (livroId == null) {
            System.out.println("Operação cancelada.");
            return;
        }
        Integer utilizadorId = lerIdValido("ID do utilizador (0 para cancelar): ", biblioteca::existeUtilizador, "utilizador");
        if (utilizadorId == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        Livro livro = biblioteca.obterLivroPorId(livroId);
        Utilizador utilizador = biblioteca.obterUtilizadorPorId(utilizadorId);
        System.out.println("\nLivro: " + livro.getTitulo() + " (" + livro.getAutor() + ")");
        System.out.println("Utilizador: " + utilizador.getNome());
        String confirmacao = lerConfirmacao("Confirmar este empréstimo? (S = sim / N = cancelar): ");
        if (!confirmacao.equals("S")) {
            System.out.println("Operação cancelada.");
            return;
        }

        try {
            Emprestimo e = biblioteca.emprestarLivro(livroId, utilizadorId);
            System.out.println("[OK] Empréstimo registado com sucesso! " + e);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[Erro] " + e.getMessage());
        }
    }

    private static void registarDevolucao() {
        System.out.println("\n--- Registar devolução ---");
        Integer livroId = lerIdValido("ID do livro (0 para cancelar): ", biblioteca::existeLivro, "livro");
        if (livroId == null) {
            System.out.println("Operação cancelada.");
            return;
        }
        Integer utilizadorId = lerIdValido("ID do utilizador (0 para cancelar): ", biblioteca::existeUtilizador, "utilizador");
        if (utilizadorId == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        Livro livro = biblioteca.obterLivroPorId(livroId);
        Utilizador utilizador = biblioteca.obterUtilizadorPorId(utilizadorId);
        System.out.println("\nLivro: " + livro.getTitulo() + " (" + livro.getAutor() + ")");
        System.out.println("Utilizador: " + utilizador.getNome());
        String confirmacao = lerConfirmacao("Confirmar esta devolução? (S = sim / N = cancelar): ");
        if (!confirmacao.equals("S")) {
            System.out.println("Operação cancelada.");
            return;
        }

        try {
            Emprestimo e = biblioteca.devolverLivro(livroId, utilizadorId);
            System.out.println("[OK] Devolução registada com sucesso! " + e);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[Erro] " + e.getMessage());
        }
    }

    private static void listarEmprestimos() {
        System.out.println("\n--- Histórico de empréstimos ---");
        Emprestimo[] emprestimos = biblioteca.listarEmprestimos();
        if (emprestimos.length == 0) {
            System.out.println("Ainda não foi efectuado nenhum empréstimo.");
            return;
        }
        for (Emprestimo e : emprestimos) {
            System.out.println(e);
        }
    }

    // ---------------------------------------------------------------
    // OPÇÃO 9 — ESTATÍSTICAS
    // ---------------------------------------------------------------

    private static void mostrarEstatisticas() {
        System.out.println("\n--- Estatísticas ---");
        Livro maisEmprestado = biblioteca.livroMaisEmprestado();
        if (maisEmprestado == null) {
            System.out.println("Ainda não foi efectuado nenhum empréstimo.");
        } else {
            int total = biblioteca.totalRequisicoesDoLivro(maisEmprestado.getId());
            System.out.println("Livro mais emprestado: " + maisEmprestado.getTitulo()
                    + " (" + total + " requisição(ões))");
        }
        System.out.println("Número total de livros requisitados: " + biblioteca.totalLivrosRequisitados());
    }

    // ---------------------------------------------------------------
    // UTILITÁRIOS DE LEITURA / VALIDAÇÃO DE ENTRADA
    // ---------------------------------------------------------------

    private static String lerTexto(String mensagem) {
        String valor;
        do {
            System.out.print(mensagem);
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("[Erro] Este campo não pode ficar vazio.");
            }
        } while (valor.isEmpty());
        return valor;
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String linha = scanner.nextLine().trim();
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("[Erro] Introduza um número inteiro válido.");
            }
        }
    }

    private static int lerInteiroPositivo(String mensagem) {
        while (true) {
            int valor = lerInteiro(mensagem);
            if (valor > 0) {
                return valor;
            }
            System.out.println("[Erro] O valor deve ser maior que zero.");
        }
    }

    /**
     * Pede um ID ao utilizador e valida imediatamente se existe (usando o
     * predicado fornecido), antes de avançar para o campo seguinte.
     * Se o ID não existir, mostra o erro e pede logo outro ID, sem
     * perguntas extra. Digitar 0 cancela a operação a qualquer momento.
     * Retorna o ID válido, ou null se o utilizador cancelar.
     */
    private static Integer lerIdValido(String mensagem, java.util.function.IntPredicate existe, String tipoEntidade) {
        while (true) {
            int id = lerInteiro(mensagem);
            if (id == 0) {
                return null;
            }
            if (existe.test(id)) {
                return id;
            }
            System.out.println("[Erro] Não existe nenhum " + tipoEntidade + " com o ID " + id + ". Tente outro ID.");
        }
    }

    /**
     * Pede uma confirmação simples S/N ao utilizador, repetindo a pergunta
     * até obter uma resposta válida. Retorna "S" ou "N".
     */
    private static String lerConfirmacao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = scanner.nextLine().trim().toUpperCase();
            if (resposta.equals("S") || resposta.equals("N")) {
                return resposta;
            }
            System.out.println("[Erro] Responda apenas com S ou N.");
        }
    }

    // ---------------------------------------------------------------
    // DADOS DE DEMONSTRAÇÃO (facilita testes ao arrancar a aplicação)
    // ---------------------------------------------------------------

    private static void carregarDadosDemonstracao() {
        biblioteca.registarLivro("Dom Casmurro", "Machado de Assis", 1899, 3);
        biblioteca.registarLivro("A Varanda do Frangipani", "Mia Couto", 1996, 2);
        biblioteca.registarLivro("Nós Matámos o Cão Tinhoso", "Luís Bernardo Honwana", 1964, 4);
        biblioteca.registarLivro("O Livro do Perdão", "Desmond Tutu e Mpho Tutu", 2014, 2);
        biblioteca.registarLivro("A Cegueira do Rio", "Mia Couto", 2024, 3);
        biblioteca.registarUtilizador("Ana Chissano", "ana.chissano@email.com");
        biblioteca.registarUtilizador("Bruno Macuácua", "bruno.macuacua@email.com");
    }
}
