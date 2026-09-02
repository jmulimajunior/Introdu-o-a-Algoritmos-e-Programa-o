import java.time.LocalDate;

/**
 * Classe principal de domínio: simula, em memória, a base de dados da
 * Biblioteca Municipal usando exclusivamente vectores (arrays) e uma matriz.
 *
 * Estruturas de dados utilizadas:
 *  - livros[]            -> vector de objectos Livro (catálogo)
 *  - utilizadores[]      -> vector de objectos Utilizador
 *  - emprestimos[]       -> vector de objectos Emprestimo (histórico)
 *  - matrizEmprestimos[][] -> matriz utilizador x livro, em que cada célula
 *    guarda o número de vezes que esse utilizador requisitou esse livro.
 *    Esta matriz é a base usada para as estatísticas (livro mais emprestado).
 *
 * @author Joaquim Pascoal Mulima Júnior
 */
public class Biblioteca {

    private static final int MAX_LIVROS = 200;
    private static final int MAX_UTILIZADORES = 200;
    private static final int MAX_EMPRESTIMOS = 2000;

    private final Livro[] livros = new Livro[MAX_LIVROS];
    private int totalLivros = 0;

    private final Utilizador[] utilizadores = new Utilizador[MAX_UTILIZADORES];
    private int totalUtilizadores = 0;

    private final Emprestimo[] emprestimos = new Emprestimo[MAX_EMPRESTIMOS];
    private int totalEmprestimos = 0;

    // Matriz: linha = índice do utilizador no vector "utilizadores"
    //         coluna = índice do livro no vector "livros"
    private final int[][] matrizEmprestimos = new int[MAX_UTILIZADORES][MAX_LIVROS];

    private int proximoIdLivro = 1;
    private int proximoIdUtilizador = 1;
    private int proximoIdEmprestimo = 1;

    // ---------------------------------------------------------------
    // REGISTO E CONSULTA DE LIVROS
    // ---------------------------------------------------------------

    public Livro registarLivro(String titulo, String autor, int ano, int quantidade) {
        if (totalLivros >= MAX_LIVROS) {
            throw new IllegalStateException("Capacidade máxima do catálogo atingida (" + MAX_LIVROS + " livros).");
        }
        Livro novo = new Livro(proximoIdLivro, titulo, autor, ano, quantidade);
        livros[totalLivros] = novo;
        totalLivros++;
        proximoIdLivro++;
        return novo;
    }

    public int getTotalLivros() {
        return totalLivros;
    }

    public Livro[] listarLivros() {
        Livro[] copia = new Livro[totalLivros];
        System.arraycopy(livros, 0, copia, 0, totalLivros);
        return copia;
    }

    public Livro[] pesquisarPorAutor(String termo) {
        return pesquisar(termo, true);
    }

    public Livro[] pesquisarPorTitulo(String termo) {
        return pesquisar(termo, false);
    }

    private Livro[] pesquisar(String termo, boolean porAutor) {
        String termoLower = termo.toLowerCase().trim();
        Livro[] resultadoTemp = new Livro[totalLivros];
        int count = 0;
        for (int i = 0; i < totalLivros; i++) {
            String campo = porAutor ? livros[i].getAutor() : livros[i].getTitulo();
            if (campo.toLowerCase().contains(termoLower)) {
                resultadoTemp[count] = livros[i];
                count++;
            }
        }
        Livro[] resultado = new Livro[count];
        System.arraycopy(resultadoTemp, 0, resultado, 0, count);
        return resultado;
    }

    private int indiceLivroPorId(int id) {
        for (int i = 0; i < totalLivros; i++) {
            if (livros[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /** Indica se existe um livro registado com o ID indicado. */
    public boolean existeLivro(int id) {
        return indiceLivroPorId(id) != -1;
    }

    /** Devolve o livro com o ID indicado, ou null se não existir. */
    public Livro obterLivroPorId(int id) {
        int i = indiceLivroPorId(id);
        return (i == -1) ? null : livros[i];
    }

    // ---------------------------------------------------------------
    // REGISTO E CONSULTA DE UTILIZADORES
    // ---------------------------------------------------------------

    public Utilizador registarUtilizador(String nome, String email) {
        if (totalUtilizadores >= MAX_UTILIZADORES) {
            throw new IllegalStateException("Capacidade máxima de utilizadores atingida (" + MAX_UTILIZADORES + ").");
        }
        Utilizador novo = new Utilizador(proximoIdUtilizador, nome, email);
        utilizadores[totalUtilizadores] = novo;
        totalUtilizadores++;
        proximoIdUtilizador++;
        return novo;
    }

    public Utilizador[] listarUtilizadores() {
        Utilizador[] copia = new Utilizador[totalUtilizadores];
        System.arraycopy(utilizadores, 0, copia, 0, totalUtilizadores);
        return copia;
    }

    private int indiceUtilizadorPorId(int id) {
        for (int i = 0; i < totalUtilizadores; i++) {
            if (utilizadores[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /** Indica se existe um utilizador registado com o ID indicado. */
    public boolean existeUtilizador(int id) {
        return indiceUtilizadorPorId(id) != -1;
    }

    /** Devolve o utilizador com o ID indicado, ou null se não existir. */
    public Utilizador obterUtilizadorPorId(int id) {
        int i = indiceUtilizadorPorId(id);
        return (i == -1) ? null : utilizadores[i];
    }

    // ---------------------------------------------------------------
    // GESTÃO DE EMPRÉSTIMOS
    // ---------------------------------------------------------------

    /**
     * Efectua o empréstimo de um livro a um utilizador registado.
     * Lança IllegalArgumentException / IllegalStateException com mensagens
     * claras em caso de erro (utilizador ou livro inexistente, sem stock,
     * capacidade do histórico esgotada), para serem tratadas no menu.
     */
    public Emprestimo emprestarLivro(int livroId, int utilizadorId) {
        int iLivro = indiceLivroPorId(livroId);
        if (iLivro == -1) {
            throw new IllegalArgumentException("Não existe nenhum livro com o ID " + livroId + ".");
        }
        int iUtilizador = indiceUtilizadorPorId(utilizadorId);
        if (iUtilizador == -1) {
            throw new IllegalArgumentException("Não existe nenhum utilizador com o ID " + utilizadorId + ".");
        }
        if (totalEmprestimos >= MAX_EMPRESTIMOS) {
            throw new IllegalStateException("Capacidade máxima do histórico de empréstimos atingida.");
        }

        Livro livro = livros[iLivro];
        if (!livro.emprestar()) {
            throw new IllegalStateException("Não há exemplares disponíveis do livro \"" + livro.getTitulo() + "\".");
        }

        Emprestimo novo = new Emprestimo(proximoIdEmprestimo, livroId, utilizadorId, LocalDate.now());
        emprestimos[totalEmprestimos] = novo;
        totalEmprestimos++;
        proximoIdEmprestimo++;

        // Actualiza a matriz utilizador x livro (estrutura usada nas estatísticas)
        matrizEmprestimos[iUtilizador][iLivro]++;

        return novo;
    }

    /**
     * Regista a devolução do empréstimo activo mais antigo, para o par
     * (livroId, utilizadorId) indicado.
     */
    public Emprestimo devolverLivro(int livroId, int utilizadorId) {
        int iLivro = indiceLivroPorId(livroId);
        if (iLivro == -1) {
            throw new IllegalArgumentException("Não existe nenhum livro com o ID " + livroId + ".");
        }
        int iUtilizador = indiceUtilizadorPorId(utilizadorId);
        if (iUtilizador == -1) {
            throw new IllegalArgumentException("Não existe nenhum utilizador com o ID " + utilizadorId + ".");
        }

        for (int i = 0; i < totalEmprestimos; i++) {
            Emprestimo e = emprestimos[i];
            if (e.getLivroId() == livroId && e.getUtilizadorId() == utilizadorId && !e.isDevolvido()) {
                e.registarDevolucao(LocalDate.now());
                livros[iLivro].devolver();
                return e;
            }
        }
        throw new IllegalStateException("Não foi encontrado nenhum empréstimo em curso para este livro/utilizador.");
    }

    public Emprestimo[] listarEmprestimos() {
        Emprestimo[] copia = new Emprestimo[totalEmprestimos];
        System.arraycopy(emprestimos, 0, copia, 0, totalEmprestimos);
        return copia;
    }

    // ---------------------------------------------------------------
    // ESTATÍSTICAS (baseadas na matriz utilizador x livro)
    // ---------------------------------------------------------------

    /**
     * Percorre a matriz de empréstimos coluna a coluna (por livro) para
     * calcular quantas vezes cada livro foi requisitado, devolvendo o livro
     * com maior número de requisições.
     */
    public Livro livroMaisEmprestado() {
        if (totalLivros == 0) {
            return null;
        }
        int melhorIndice = -1;
        int melhorTotal = -1;
        for (int coluna = 0; coluna < totalLivros; coluna++) {
            int total = 0;
            for (int linha = 0; linha < totalUtilizadores; linha++) {
                total += matrizEmprestimos[linha][coluna];
            }
            if (total > melhorTotal) {
                melhorTotal = total;
                melhorIndice = coluna;
            }
        }
        if (melhorTotal <= 0) {
            return null; // nenhum empréstimo foi ainda efectuado
        }
        return livros[melhorIndice];
    }

    /** Número total de livros requisitados (soma de todos os empréstimos). */
    public int totalLivrosRequisitados() {
        return totalEmprestimos;
    }

    /** Número de vezes que um livro específico foi requisitado (soma da coluna). */
    public int totalRequisicoesDoLivro(int livroId) {
        int iLivro = indiceLivroPorId(livroId);
        if (iLivro == -1) {
            return 0;
        }
        int total = 0;
        for (int linha = 0; linha < totalUtilizadores; linha++) {
            total += matrizEmprestimos[linha][iLivro];
        }
        return total;
    }
}
