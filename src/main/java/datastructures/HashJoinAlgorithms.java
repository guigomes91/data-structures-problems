package datastructures;

import java.util.*;

public class HashJoinAlgorithms {

    record Author(int authorId, String name) {}
    record Book(int bookId, String title, int authorId) {}
    record ResultRow(int authorId, String name, String title, int bookId) {}

    public static List<ResultRow> hashJoin(List<Author> authorsTable, List<Book> booksTable) {

        // Passo 1: Criar o hash table da tabela menor (autores)
        Map<Integer, Author> authorMap = new HashMap<>();
        for (Author author : authorsTable) {
            authorMap.put(author.authorId(), author);
        }

        List<ResultRow> resultSet = new LinkedList<>();

        // Passo 2: Percorrer a tabela de livros e sondar o mapa
        for (Book book : booksTable) {
            Integer authorId = book.authorId();
            Author author = authorMap.get(authorId);

            if (author != null) {
                resultSet.add(new ResultRow(
                        author.authorId(),
                        author.name(),
                        book.title(),
                        book.bookId()
                ));
            }
        }

        return resultSet;
    }

    public static void main(String[] args) {
        // Dados de exemplo
        List<Author> authorsTable = Arrays.asList(
                new Author(1, "Author_1"), new Author(2, "Author_2"),
                new Author(3, "Author_3"), new Author(4, "Author_4"),
                new Author(5, "Author_5")
        );

        List<Book> booksTable = Arrays.asList(
                new Book(1, "Book_1", 1), new Book(2, "Book_2", 1),
                new Book(3, "Book_3", 2), new Book(4, "Book_4", 3),
                new Book(5, "Book_5", 3), new Book(6, "Book_6", 3),
                new Book(7, "Book_7", 4), new Book(8, "Book_8", 5),
                new Book(9, "Book_9", 5)
        );

        List<ResultRow> result = hashJoin(authorsTable, booksTable);
        result.forEach(System.out::println);
    }
}