package switchexpressions;

// Demo de exaustividade do switch com sealed interface (Java 21+)
// Como testar no IntelliJ:
// 1. Rode a classe: compila e imprime as descrições normalmente.
// 2. Descomente o record Golf e adicione "Golf" na cláusula permits.
// 3. O switch em describe() fica sublinhado em vermelho na hora:
//    "'switch' expression does not cover all possible input values"
// 4. Alt+Enter em cima do switch -> "Add remaining branches"
//    e o IntelliJ gera o "case Golf golf ->" sozinho.

public class ExhaustivenessDemo {

    sealed interface Player permits Tennis, Football /*, Golf */ {}

    record Tennis(String name, int ranking) implements Player {}

    record Football(String name, String position) implements Player {}

    // record Golf(String name, int handicap) implements Player {}

    static String describe(Player player) {
        // Sem default e sem case Object: o compilador prova que está completo
        return switch (player) {
            case Tennis t   -> t.name() + " joga tênis (ranking " + t.ranking() + ")";
            case Football f -> f.name() + " joga futebol como " + f.position();
        };
    }

    public static void main(String[] args) {
        System.out.println(describe(new Tennis("Guga", 1)));
        System.out.println(describe(new Football("Marta", "atacante")));
    }
}