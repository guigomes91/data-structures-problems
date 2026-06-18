package datastructures;

import java.util.ArrayList;
import java.util.List;

public class RopeStructure {

    // Estrutura do Nó baseada na árvore binária da Rope
    public static class Node {
        private Node left;
        private Node right;
        private int weight; // Peso da subárvore à esquerda
        private String str; // Conteúdo do texto (apenas para nós folhas)

        // Construtor para nós folhas
        public Node(String str) {
            this(null, null, str.length(), str);
        }

        // Construtor para nós internos
        public Node(Node left, Node right, int weight, String str) {
            this.left = left;
            this.right = right;
            this.str = str;
            this.weight = weight;
        }

        @Override
        public String toString() {
            if (str != null) return str;
            return (left != null ? left.toString() : "") + (right != null ? right.toString() : "");
        }
    }

    // Busca o caractere em um índice específico (Recursivo)
    public static char indexAt(Node node, int index) {
        if (index > node.weight - 1) {
            // Se o índice for maior que o peso, move-se para a direita subtraindo o peso
            return indexAt(node.right, index - node.weight);
        } else if (node.left != null) {
            // Se for menor, move-se para a esquerda
            return indexAt(node.left, index);
        } else {
            // Retorna o caractere quando atinge o nó folha
            return node.str.charAt(index);
        }
    }

    // Concatena duas Ropes criando uma nova raiz
    public static Node concat(Node node1, Node node2) {
        return new Node(node1, node2, getLength(node1), null);
    }

    // Método auxiliar para calcular o comprimento total de uma subárvore
    private static int getLength(Node node) {
        if (node.str != null) {
            return node.weight;
        } else {
            return getLength(node.left) + (node.right == null ? 0 : getLength(node.right));
        }
    }

    // Implementação da inserção de texto
    public static Node insert(Node node, int index, String str) {
        // A inserção exige um split em s1 e s2, e duas concatenações
        List<Node> splitRopes = split(node, index);
        Node insertNode = new Node(str);

        if (splitRopes.size() == 1) {
            return (index == 0) ? concat(insertNode, splitRopes.get(0))
                    : concat(splitRopes.get(0), insertNode);
        } else {
            Node result = concat(splitRopes.get(0), insertNode);
            return concat(result, splitRopes.get(1));
        }
    }

    // Implementação da remoção de um trecho
    public static Node delete(Node node, int start, int end) {
        // Exige dois splits e uma concatenação
        List<Node> splitRopes1 = split(node, start);
        Node beforeNode = (splitRopes1.size() > 1) ? splitRopes1.get(0) : null;
        Node afterNode = (splitRopes1.size() > 1) ? splitRopes1.get(1) : splitRopes1.get(0);

        List<Node> splitRopes2 = split(afterNode, end - start);
        if (splitRopes2.size() == 1) return beforeNode;

        return (beforeNode == null) ? splitRopes2.get(1)
                : concat(beforeNode, splitRopes2.get(1));
    }

    // Simulação do método split
    // Esta versão simplificada atende ao teste básico de funcionamento no IntelliJ
    private static List<Node> split(Node node, int index) {
        List<Node> result = new ArrayList<>();
        String fullText = node.toString();
        if (index > 0) result.add(new Node(fullText.substring(0, index)));
        if (index < fullText.length()) result.add(new Node(fullText.substring(index)));
        return result;
    }

    public static void main(String[] args) {
        // Exemplo de uso: "I am a very cool rope"
        Node leftBranch = concat(new Node("I "), new Node("am "));
        Node rightBranch = concat(new Node("a "), new Node("very "));
        Node root = concat(leftBranch, rightBranch);
        root = concat(root, new Node("cool rope"));

        System.out.println("Texto completo: " + root.toString());
        System.out.println("Caractere no índice 5: " + indexAt(root, 5)); // Deve ser 'a'

        root = insert(root, 2, "really ");
        System.out.println("Após inserção: " + root.toString());

        root = delete(root, 2, 9);
        System.out.println("Após remoção: " + root.toString());
    }
}