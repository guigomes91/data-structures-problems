package datastructures;

import java.util.*;

public class SortMergeJoin {

    // Definição dos Records para representar as tabelas e o resultado
    public record Author(int authorId, String name) {}
    public record Book(int bookId, String title, int authorId) {}
    public record ResultRow(int authorId, String name, String title, int bookId) {}

    public static List<ResultRow> sortMergeJoin(
            List<Author> authorsTable, List<Book> booksTable) {

        // PASSO 1: Ordenação das tabelas pelo atributo de junção (authorId)
        // Ordena autores de forma crescente por ID
        authorsTable.sort(Comparator.comparing(Author::authorId));

        // Ordena livros por authorId e, em caso de empate, por bookId
        booksTable.sort((b1, b2) -> {
            int sortResult = Comparator.comparing(Book::authorId).compare(b1, b2);
            return sortResult != 0 ? sortResult :
                    Comparator.comparing(Book::bookId).compare(b1, b2);
        });

        List<ResultRow> resultSet = new LinkedList<>();
        int authorCount = authorsTable.size();
        int bookCount = booksTable.size();

        // PASSO 2: Intercalação (Merge) usando dois ponteiros
        int p = 0; // ponteiro para autores
        int q = 0; // ponteiro para livros

        while (p < authorCount && q < bookCount) {
            Author author = authorsTable.get(p);
            Book book = booksTable.get(q);

            if (author.authorId() == book.authorId()) {
                // Se os IDs coincidem, adiciona ao resultado e avança o ponteiro dos livros
                resultSet.add(new ResultRow(
                        author.authorId(),
                        author.name(),
                        book.title(),
                        book.bookId()
                ));
                q++;
            } else {
                // Se não coincidem, avança o ponteiro da tabela de autores
                p++;
            }
        }

        return resultSet;
    }

    public static void main(String[] args) {
        // Dados de exemplo baseados nas fontes
        List<Author> authors = new ArrayList<>(Arrays.asList(
                new Author(1, "Author_1"), new Author(5, "Author_5"),
                new Author(3, "Author_3"), new Author(2, "Author_2"),
                new Author(4, "Author_4")
        ));

        List<Book> books = new ArrayList<>(Arrays.asList(
                new Book(1, "Book_1", 1), new Book(2, "Book_2", 1),
                new Book(3, "Book_3", 2), new Book(4, "Book_4", 3),
                new Book(9, "Book_9", 5)
        ));

        List<ResultRow> result = sortMergeJoin(authors, books);

        System.out.println("Resultado da Junção (Sort Merge):");
        result.forEach(System.out::println);
    }
}