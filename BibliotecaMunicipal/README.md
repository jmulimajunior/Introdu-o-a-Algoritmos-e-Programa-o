# Sistema de Gestão da Biblioteca Municipal

Aplicação de consola, desenvolvida em **Java**, para apoiar os bibliotecários
da Biblioteca Municipal na gestão do inventário de livros, no registo de
utilizadores e no controlo de empréstimos e devoluções.

Trabalho de Campo da disciplina de **Introdução a Algoritmos e Programação**,
Curso de Licenciatura em Engenharia Informática, UnISCED.

## Funcionalidades

- **Registo de Livros**: inserir novos títulos no catálogo (identificador
  único gerado automaticamente, título, autor, ano de publicação e
  quantidade de exemplares disponíveis).
- **Consulta de Catálogo**: listar todos os livros ou pesquisar por autor
  ou por título.
- **Registo de Utilizadores**: inserir novos leitores no sistema.
- **Gestão de Empréstimos**: emprestar um livro a um utilizador registado
  (com verificação de stock) e registar a respectiva devolução.
- **Estatísticas**: livro mais emprestado e número total de livros
  requisitados.

## Estruturas de dados

O sistema simula uma base de dados em memória usando **exclusivamente
vectores (arrays) e uma matriz**, sem recorrer a colecções dinâmicas (`List`,
`Map`, etc.):

| Estrutura | Tipo | Finalidade |
|---|---|---|
| `livros[]` | vector de `Livro` | catálogo de livros |
| `utilizadores[]` | vector de `Utilizador` | leitores registados |
| `emprestimos[]` | vector de `Emprestimo` | histórico de empréstimos/devoluções |
| `matrizEmprestimos[][]` | matriz `int[utilizador][livro]` | conta quantas vezes cada utilizador requisitou cada livro; é a base do cálculo das estatísticas |

## Requisitos

- **JDK 17 ou superior** (a aplicação usa apenas API padrão do Java, sem
  dependências externas).

Para verificar a versão instalada:

```bash
java -version
javac -version
```

## Estrutura do projecto

```
BibliotecaMunicipal/
├── README.md
└── src/
    ├── Main.java         # menu interactivo (ponto de entrada)
    ├── Biblioteca.java   # lógica de negócio e estruturas de dados (arrays/matriz)
    ├── Livro.java        # entidade Livro
    ├── Utilizador.java   # entidade Utilizador
    └── Emprestimo.java   # entidade Emprestimo
```

## Como compilar e executar

1. Clonar o repositório:

   ```bash
   https://github.com/jmulimajunior/Introdu-o-a-Algoritmos-e-Programa-o.git
   ```

2. Compilar os ficheiros-fonte:

   ```bash
   javac -d bin src/*.java
   ```

3. Executar a aplicação (recomenda-se forçar a codificação UTF-8, para que
   os acentos sejam apresentados correctamente na consola):

   ```bash
   java -Dfile.encoding=UTF-8 -cp bin Main
   ```

Ao arrancar, o sistema carrega automaticamente alguns livros e utilizadores
de demonstração, para facilitar os testes.

## Exemplo de utilização

```
===================================================
   SISTEMA DE GESTÃO - BIBLIOTECA MUNICIPAL
===================================================
 1. Registar novo livro
 2. Listar catálogo completo
 3. Pesquisar livro (por autor ou título)
 4. Registar novo utilizador
 5. Listar utilizadores
 6. Efectuar empréstimo
 7. Registar devolução
 8. Listar histórico de empréstimos
 9. Estatísticas
 0. Sair
===================================================
Escolha uma opção: 6
--- Efectuar empréstimo ---
ID do livro (0 para cancelar): 1
ID do utilizador (0 para cancelar): 1

Livro: Dom Casmurro (Machado de Assis)
Utilizador: Ana Chissano
Confirmar este empréstimo? (S = sim / N = cancelar): S
[OK] Empréstimo registado com sucesso! Empréstimo #1 | Livro ID: 1 | Utilizador ID: 1 | Data: 31/08/2026 | Estado: Em curso
```

Se o ID do livro ou do utilizador não existir, o sistema apresenta o erro e
pede logo um novo ID (sem voltar ao menu principal). Para desistir da
operação a qualquer momento, basta digitar `0`.

## Tratamento de erros

A aplicação valida as entradas do utilizador e apresenta mensagens de erro
claras nos seguintes casos, entre outros:

- Entrada não numérica onde é esperado um número inteiro.
- IDs de livro ou de utilizador inexistentes: o sistema informa o erro e
  pede imediatamente um novo ID, sem avançar para o campo seguinte nem
  voltar ao menu principal (digitar `0` cancela a operação a qualquer
  momento).
- Antes de finalizar um empréstimo ou uma devolução, o sistema mostra o
  título/autor do livro e o nome do utilizador para confirmação (S/N).
- Empréstimo de um livro sem exemplares disponíveis.
- Devolução de um empréstimo que não existe ou já foi devolvido.
- Capacidade máxima do catálogo, dos utilizadores ou do histórico de
  empréstimos atingida.

## Autor

Joaquim Pascoal Mulima Júnior, Trabalho de Campo, Introdução a Algoritmos e Programação, UnISCED.

## Licença

Projecto académico, desenvolvido para fins educativos.
