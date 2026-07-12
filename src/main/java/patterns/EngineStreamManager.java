package patterns;

import java.util.ArrayList;
import java.util.List;

// 1. Hierarquia Selada para controle de tipos de motores
sealed interface Engine permits RegularEngine, HighSpeedEngine, HypersonicEngine {}

// 2. Records representando os diferentes motores
record RegularEngine(String model, boolean electric, int autonomy) implements Engine {}
record HighSpeedEngine(String model) implements Engine {}
record HypersonicEngine(String model) implements Engine {}

public class EngineStreamManager {

    public static void main(String[] args) {
        // Criando uma lista mutável de motores
        List<Engine> engines = new ArrayList<>();
        engines.add(new RegularEngine("E-City", true, 50));   // Será removido (elétrico e baixa autonomia)
        engines.add(new RegularEngine("E-Long", true, 300));  // Mantido (elétrico, mas alta autonomia)
        engines.add(new RegularEngine("Gas-V8", false, 100)); // Mantido (não é elétrico)
        engines.add(new HighSpeedEngine("V-Race")); // Mantido (não é RegularEngine)

        System.out.println("Antes da filtragem: " + engines.size() + " motores.");

        // 3. Aplicação do Pattern Matching com Streams/Collections
        filterRegularEngines(engines, 100);

        System.out.println("Depois da filtragem: " + engines.size() + " motores.");
        engines.forEach(System.out::println);
    }

    /**
     * Remove motores do tipo RegularEngine que sejam elétricos e não passem no teste de autonomia.
     * Utiliza Type Pattern Matching para evitar cast manual.
     */
    public static List<Engine> filterRegularEngines(List<Engine> engines, int testSpeed) {

        // O uso de 'e instanceof RegularEngine engine' faz o teste de tipo e o cast em um passo
        engines.removeIf(e -> e instanceof RegularEngine engine
                && engine.electric()
                && !hasEnoughAutonomy(engine, testSpeed));

        return engines;
    }

    // Método auxiliar para lógica de negócio (autonomia)
    private static boolean hasEnoughAutonomy(RegularEngine engine, int threshold) {
        return engine.autonomy() >= threshold;
    }
}