package switchexpressions;

enum PlayerType { TENNIS, FOOTBALL, SNOOKER, UNKNOWN }

interface Player {}
record TennisPlayer() implements Player {}
record FootballPlayer() implements Player {}
record SnookerPlayer() implements Player {}

public class PlayerFactory {

    public static Player createPlayer(PlayerType playerType) {
        // O switch é uma expressão que retorna um valor diretamente
        return switch (playerType) {
            // Seta simples para retornos diretos
            case TENNIS -> new TennisPlayer();

            // Bloco de código utilizando 'yield' para produzir o valor resultante
            case FOOTBALL -> {
                System.out.println("Log: Criando uma instância de FootballPlayer...");
                yield new FootballPlayer();
            }

            case SNOOKER -> new SnookerPlayer();

            // Tratamento de nulo integrado ao switch (JDK 17+)
            case null -> throw new NullPointerException("O tipo do jogador não pode ser nulo");

            // Separe o UNKNOWN do default
            case UNKNOWN -> throw new IllegalArgumentException("Tipo UNKNOWN não suportado");
            default -> throw new IllegalArgumentException("Tipo de jogador inválido ou desconhecido: " + playerType);
        }; // Ponto e vírgula obrigatório em switch expressions
    }

    public static void main(String[] args) {
        try {
            Player p1 = createPlayer(PlayerType.TENNIS);
            System.out.println("Criado: " + p1.getClass().getSimpleName());

            Player p2 = createPlayer(PlayerType.FOOTBALL);
            System.out.println("Criado: " + p2.getClass().getSimpleName());

            // Testando o gatilho de erro para nulo
            createPlayer(null);

        } catch (Exception e) {
            System.err.println("Erro detectado: " + e.getMessage());
        }
    }
}