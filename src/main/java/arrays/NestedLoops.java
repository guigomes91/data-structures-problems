package arrays;

import java.util.List;

public class NestedLoops {

    public static void main(String[] args) {
        List<Integer> listOne = List.of(1, 3, 10, 5, 9);
        List<Integer> listTwo = List.of(5, 7, 8, 2, 9);

        printPairInteger(listOne, listTwo);
    }

    static void printPairInteger(List<Integer> list1, List<Integer> list2) {
        for (Integer i : list1) {
            for (Integer j : list2) {
                if (i < j) {
                    System.out.println(i + " " + j);
                }
            }
        }
    }
}
