package objects;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Interface que define o contrato da Pilha Imutável.
 */
interface Stack<T> extends Iterable<T> {
    boolean isEmpty();
    Stack<T> push(T value);
    Stack<T> pop();
    T peek();
}

/**
 * Implementação principal da Pilha Imutável.
 */
public class ImmutableStack<E> implements Stack<E> {

    private final E head;
    private final Stack<E> tail;

    private ImmutableStack(final E head, final Stack<E> tail) {
        this.head = head;
        this.tail = tail;
    }

    // Método de fábrica para criar uma pilha vazia
    public static <U> Stack<U> empty(final Class<U> type) {
        return new EmptyStack<>();
    }

    @Override
    public Stack<E> push(E e) {
        return new ImmutableStack<>(e, this); // Retorna nova instância
    }

    @Override
    public Stack<E> pop() {
        return this.tail; // Retorna a pilha anterior
    }

    @Override
    public E peek() {
        return this.head; // Retorna o topo
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new StackIterator<>(this);
    }

    // --- Singleton para a Pilha Vazia ---
    private static class EmptyStack<U> implements Stack<U> {
        @Override
        public Stack<U> push(U u) {
            return new ImmutableStack<>(u, this);
        }

        @Override
        public Stack<U> pop() {
            throw new UnsupportedOperationException("Pilha vazia!");
        }

        @Override
        public U peek() {
            throw new UnsupportedOperationException("Pilha vazia!");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public Iterator<U> iterator() {
            return new StackIterator<>(this);
        }
    }

    // --- Implementação do Iterador ---
    private static class StackIterator<U> implements Iterator<U> {
        private Stack<U> stack;

        public StackIterator(final Stack<U> stack) {
            this.stack = stack;
        }

        @Override
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        @Override
        public U next() {
            if (!hasNext()) throw new NoSuchElementException();
            U e = this.stack.peek();
            this.stack = this.stack.pop();
            return e;
        }
    }

    // --- Método Main para teste ---
    public static void main(String[] args) {
        // Criando pilha inicial imutável
        Stack<String> s1 = ImmutableStack.empty(String.class);

        // Operações geram novas pilhas, as originais não mudam
        Stack<String> s2 = s1.push("Primeiro");
        Stack<String> s3 = s2.push("Segundo");
        Stack<String> s4 = s3.push("Terceiro");

        System.out.println("Pilha s4 (topo): " + s4.peek()); // Saída: Terceiro

        System.out.println("\nIterando sobre s4:");
        for (String item : s4) {
            System.out.println("- " + item);
        }

        // Demonstrando imutabilidade
        Stack<String> s5 = s4.pop();
        System.out.println("\nApós pop em s4, novo topo em s5: " + s5.peek()); // Segundo
        System.out.println("Topo original de s4 continua sendo: " + s4.peek()); // Terceiro
    }
}