package patternmatching;

import java.util.List;

// 1. Hierarquia de classes para transporte
sealed interface Veiculo permits Caminhao, Van, Drone {}

record Caminhao(int capacidadeKg, String placa) implements Veiculo {}
record Van(int volumeM3) implements Veiculo {}
record Drone(double bateriaRestante) implements Veiculo {}

public class GestorLogistica {

    public static void main(String[] args) {
        List<Object> itensParaProcessar = List.of(
                0,                          // Um código de comando constante
                new Caminhao(15000, "ABC-1234"), // Caminhão pesado
                new Caminhao(500, "XYZ-9999"),   // Caminhão leve
                new Van(10),       // Van padrão
                "Carga Prioritária",        // Uma String avulsa
                new Drone(0.1) // Drone com bateria baixa
        );

        itensParaProcessar.forEach(item ->
                System.out.println("Processando: " + triagemCarga(item)));
    }

    public static String triagemCarga(Object obj) {
        return switch (obj) {
            // REGRA 1: Constantes SEMPRE antes de padrões de tipo
            // Se 'case Integer i' viesse antes, o 'case 0' seria inalcançável (ERRO).
            // Em vez de: case 0 -> ...
            case Integer i when i == 0 -> "COMANDO: Parada de emergência de frota.";

            // REGRA 2: Subclasses mais específicas ANTES das classes base
            // REGRA 3: Padrões Guardados (when) ANTES de padrões de tipo puros

            // Caso específico: Caminhão com carga pesada
            case Caminhao c when c.capacidadeKg() > 10000 ->
                    "LOGÍSTICA: Despachar para Rota de Rodovias (Peso: " + c.capacidadeKg() + "kg)";

            // Caso geral para Caminhão: Captura o que sobrou (capacidade <= 10000)
            case Caminhao c ->
                    "LOGÍSTICA: Despachar para Rota Urbana (Placa: " + c.placa() + ")";

            // Caso específico para Drone (Guarda)
            case Drone d when d.bateriaRestante() < 0.2 ->
                    "ALERTA: Drone com bateria baixa (" + (d.bateriaRestante()*100) + "%). Retornar à base!";

            // REGRA 4: O Padrão Total ou Interface Base por último
            // 'case Veiculo v' domina Caminhao, Van e Drone.
            case Veiculo v ->
                    "LOGÍSTICA: Veículo identificado: " + v.getClass().getSimpleName();

            // REGRA 5: O Padrão Object (Total) ou Default
            case Object o -> "ERRO: Carga ou objeto não identificado pelo sistema.";

            case null -> "ERRO: Entrada nula detectada.";
        };
    }
}