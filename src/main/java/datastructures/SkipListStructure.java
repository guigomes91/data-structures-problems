package datastructures;

import java.util.Random;

public class SkipListStructure {

    private static final int MAX_LEVEL = 10; // Limite de camadas
    private final Node head = new Node(null, MAX_LEVEL); // Nó cabeça sentinela
    private int topLayer = 1; // Camada atual mais alta
    private int size = 0;
    private final Random random = new Random();

    // Estrutura do Nó da Skip List
    private static class Node {
        private final Integer data;
        private final Node[] next; // Array de ponteiros para as camadas

        public Node(Integer data, int level) {
            this.data = data;
            this.next = new Node[level];
        }

        public Integer getData() { return data; }
    }

    // Busca se um item existe (Começa no topo e desce as camadas)
    public boolean contains(Integer data) {
        Node cursorNode = head;

        for (int i = topLayer - 1; i >= 0; i--) {
            while (cursorNode.next[i] != null) {
                if (cursorNode.next[i].getData() > data) {
                    break; // Pula para a camada de baixo
                }
                if (cursorNode.next[i].getData().equals(data)) {
                    return true; // Encontrou
                }
                cursorNode = cursorNode.next[i];
            }
        }
        return false;
    }

    // Insere um novo item em uma camada decidida aleatoriamente
    public void insert(Integer data) {
        int layer = incrementLayerNo(); // Decide a camada aleatoriamente
        Node newNode = new Node(data, layer + 1);
        Node cursorNode = head;

        // Navega para encontrar a posição correta de inserção
        for (int i = topLayer - 1; i >= 0; i--) {
            while (cursorNode.next[i] != null) {
                if (cursorNode.next[i].getData() > data) {
                    break;
                }
                cursorNode = cursorNode.next[i];
            }

            // Faz a ligação dos nós nas camadas sorteadas
            if (i <= layer) {
                newNode.next[i] = cursorNode.next[i];
                cursorNode.next[i] = newNode;
            }
        }
        size++;
    }

    // Remove um item e ajusta os ponteiros para manter a integridade
    public boolean delete(Integer data) {
        Node cursorNode = head;
        boolean deleted = false;

        for (int i = topLayer - 1; i >= 0; i--) {
            while (cursorNode.next[i] != null) {
                if (cursorNode.next[i].getData() > data) {
                    break;
                }
                if (cursorNode.next[i].getData().equals(data)) {
                    // Remove o nó "pulando" ele na lista
                    cursorNode.next[i] = cursorNode.next[i].next[i];
                    deleted = true;
                    // Note: size-- deve ser tratado com cuidado aqui para não repetir
                    break;
                }
                cursorNode = cursorNode.next[i];
            }
        }
        if (deleted) size--;
        return deleted;
    }

    // Método probabilístico para decidir o nível do novo nó
    private int incrementLayerNo() {
        int level = 0;
        // Simulação simples de probabilidade de 50% para subir de nível
        while (random.nextDouble() < 0.5 && level < MAX_LEVEL - 1) {
            level++;
        }
        if (level + 1 > topLayer) {
            topLayer = level + 1;
        }
        return level;
    }

    public static void main(String[] args) {
        SkipListStructure skipList = new SkipListStructure();

        // Exemplo de inserção de itens
        int[] items = {1, 2, 3, 4, 5, 8, 9, 10, 11, 34};
        for (int item : items) skipList.insert(item);

        System.out.println("Busca pelo 11: " + skipList.contains(11)); // true
        System.out.println("Busca pelo 7: " + skipList.contains(7));   // false

        skipList.insert(7);
        System.out.println("Após inserir 7, busca pelo 7: " + skipList.contains(7)); // true

        skipList.delete(10);
        System.out.println("Após deletar 10, busca pelo 10: " + skipList.contains(10)); // false
    }
}