/**
 * Classe que representa um Livro do catálogo da Biblioteca Municipal.
 *
 * Cada livro possui um identificador único (gerado automaticamente pelo
 * sistema), título, autor, ano de publicação, a quantidade total de
 * exemplares adquiridos pela biblioteca e a quantidade de exemplares
 * actualmente disponíveis para empréstimo.
 *
 * @author Joaquim Pascoal Mulima Júnior
 */
public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private int ano;
    private int quantidadeTotal;
    private int quantidadeDisponivel;

    public Livro(int id, String titulo, String autor, int ano, int quantidadeTotal) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeTotal; // ao registar, todos os exemplares estão disponíveis
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAno() {
        return ano;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    /**
     * Diminui em 1 a quantidade disponível (usado quando se efectua um
     * empréstimo). Retorna false se não houver exemplares disponíveis.
     */
    public boolean emprestar() {
        if (quantidadeDisponivel <= 0) {
            return false;
        }
        quantidadeDisponivel--;
        return true;
    }

    /**
     * Aumenta em 1 a quantidade disponível (usado quando se regista uma
     * devolução), sem nunca ultrapassar a quantidade total de exemplares.
     */
    public void devolver() {
        if (quantidadeDisponivel < quantidadeTotal) {
            quantidadeDisponivel++;
        }
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %-4d | Título: %-30s | Autor: %-20s | Ano: %-4d | Disponíveis: %d/%d",
                id, titulo, autor, ano, quantidadeDisponivel, quantidadeTotal);
    }
}
