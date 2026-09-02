/**
 * Classe que representa um Utilizador (leitor) registado na Biblioteca
 * Municipal. Um utilizador precisa de estar registado no sistema antes de
 * poder requisitar qualquer livro.
 *
 * @author Joaquim Pascoal Mulima Júnior
 */
public class Utilizador {

    private int id;
    private String nome;
    private String email;

    public Utilizador(int id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return String.format("ID: %-4d | Nome: %-25s | Email: %s", id, nome, email);
    }
}
