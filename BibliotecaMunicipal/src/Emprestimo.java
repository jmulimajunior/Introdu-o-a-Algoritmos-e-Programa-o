import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe que representa um registo de Empréstimo, associando um Livro a um
 * Utilizador, com as respectivas datas de empréstimo e de devolução.
 *
 * @author Joaquim Pascoal Mulima Júnior
 */
public class Emprestimo {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int id;
    private int livroId;
    private int utilizadorId;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao; // null enquanto o livro não for devolvido
    private boolean devolvido;

    public Emprestimo(int id, int livroId, int utilizadorId, LocalDate dataEmprestimo) {
        this.id = id;
        this.livroId = livroId;
        this.utilizadorId = utilizadorId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = null;
        this.devolvido = false;
    }

    public int getId() {
        return id;
    }

    public int getLivroId() {
        return livroId;
    }

    public int getUtilizadorId() {
        return utilizadorId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void registarDevolucao(LocalDate data) {
        this.dataDevolucao = data;
        this.devolvido = true;
    }

    @Override
    public String toString() {
        String estado = devolvido
                ? "Devolvido em " + dataDevolucao.format(FORMATO_DATA)
                : "Em curso";
        return String.format(
                "Empréstimo #%-4d | Livro ID: %-4d | Utilizador ID: %-4d | Data: %s | Estado: %s",
                id, livroId, utilizadorId, dataEmprestimo.format(FORMATO_DATA), estado);
    }
}
