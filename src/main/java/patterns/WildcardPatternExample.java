package patterns;

import java.util.*;

// Exemplo de uma classe genérica
class Ponto<E> {
    private final int x, y;
    public Ponto(int x, int y) { this.x = x; this.y = y; }

    @Override
    public boolean equals(Object obj) {
        // Uso de wildcard (?) para verificar o tipo genérico no pattern matching
        return obj instanceof Ponto<?> other
                && this.x == other.x && this.y == other.y;
    }
}

public class WildcardPatternExample {

    enum Status { DRAFT, READY }
    record Book(String title) { void review() { System.out.println("Revisando: " + title); } }

    // Exemplo de método genérico processando Mapas
    public static <K, V> void process(Map<K, ? extends V> map) {
        // Uso de wildcards no padrão para contornar o Type Erasure
        // O Java permite EnumMap<?, ? extends V>, mas não EnumMap<K, V> no instanceof
        if (map instanceof EnumMap<?, ? extends V> books
                && books.get(Status.DRAFT) instanceof Book book) {

            // A variável 'book' já está tipada e pronta para uso
            book.review();
        }
    }

    public static void main(String[] args) {
        EnumMap<Status, Book> myBooks = new EnumMap<>(Status.class);
        myBooks.put(Status.DRAFT, new Book("Java Moderno em Ação"));

        process(myBooks);
    }
}