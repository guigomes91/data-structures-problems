package arrays;

import java.util.HashMap;
import java.util.Map;

public class TwoSumArray {

    // target = 0 - numbers = {2, 7, 11, 15}
    public int[] calculate(int[] numbers, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < numbers.length; i++) {
            // Iteracao 1: 9 - 2 = 7
            // Iteracao 2: 9 - 7 = 2
            int complemento = target - numbers[i];

            // Iteracao 1: aqui mapa esta vazio
            // Iteracao 2: aqui mapa contem o value 0 - retorna [0, 1]
            if (map.containsKey(complemento)) {
                return new int[] { map.get(complemento), i };
            }

            // Iteracao 1: preenche o mapa com [key 2; value 0]
            map.put(numbers[i], i);
        }

        return new int[0];
    }
}