package datastructures;

import java.util.Arrays;

public class KDTreeStructure {

    private Node root;
    private Node found;
    private double foundDistance;
    private int visited;

    // Estrutura do Nó da K-D Tree para 2 dimensões
    private final class Node {
        private final double[] coords; // Armazena [x, y]
        private Node left;
        private Node right;

        public Node(double[] coords) {
            this.coords = coords;
        }

        // Método auxiliar para obter a coordenada baseada no eixo (0 para X, 1 para Y)
        public double get(int index) {
            return coords[index];
        }

        // Calcula a distância euclidiana quadrada entre dois pontos
        public double theDistance(Node target) {
            double dx = this.coords[0] - target.coords[0]; // eixo X
            double dy = this.coords[1] - target.coords[1]; // eixo Y
            return dx * dx + dy * dy;
        }
    }

    // Inserção na K-D Tree
    public void insert(double[] coords) {
        root = insert(root, coords, 0);
    }

    private Node insert(Node root, double[] coords, int depth) {
        if (root == null) {
            return new Node(coords);
        }

        // Alterna o eixo de comparação: depth % 2 (0=X, 1=Y)
        int cd = depth % 2;

        if (coords[cd] < root.coords[cd]) {
            root.left = insert(root.left, coords, depth + 1);
        } else {
            root.right = insert(root.right, coords, depth + 1);
        }

        return root;
    }

    // Encontra o vizinho mais próximo de uma coordenada alvo
    public double[] findNearest(double[] coords) {
        Node targetNode = new Node(coords);
        visited = 0;
        foundDistance = Double.MAX_VALUE;
        found = null;

        nearest(root, targetNode, 0);

        return found != null ? found.coords.clone() : null;
    }

    private void nearest(Node root, Node targetNode, int index) {
        if (root == null) {
            return;
        }

        visited++;
        double theDistance = root.theDistance(targetNode);

        // Se o nó atual for o mais próximo até agora, atualiza
        if (found == null || theDistance < foundDistance) {
            foundDistance = theDistance;
            found = root;
        }

        if (foundDistance == 0) return; // Encontrou o ponto exato

        double rootTargetDistance = root.get(index) - targetNode.get(index);
        int nextIndex = (index + 1) % 2;

        // Pesquisa primeiro a subárvore onde o alvo estaria "naturalmente"
        nearest(rootTargetDistance > 0 ? root.left : root.right, targetNode, nextIndex);

        // Verifica se é necessário verificar a outra subárvore (poda)
        if (rootTargetDistance * rootTargetDistance >= foundDistance) {
            return;
        }

        nearest(rootTargetDistance > 0 ? root.right : root.left, targetNode, nextIndex);
    }

    public static void main(String[] args) {
        KDTreeStructure tree = new KDTreeStructure();

        // Dados de exemplo
        double[][] coords = {
                {3, 5}, {1, 4}, {5, 4}, {2, 3}, {4, 2}, {3, 2},
                {5, 2}, {2, 1}, {2, 4}, {2, 5}
        };

        for (double[] coord : coords) {
            tree.insert(coord);
        }

        // Busca pelo vizinho mais próximo de (4, 4)
        double[] target = {4, 4};
        double[] nearest = tree.findNearest(target);

        System.out.println("Alvo: " + Arrays.toString(target));
        System.out.println("Vizinho mais próximo encontrado: " + Arrays.toString(nearest));
        System.out.println("O resultado esperado é (5.0, 4.0)");
    }
}