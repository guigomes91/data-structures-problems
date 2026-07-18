package ica.task;

import java.util.Comparator;
import java.util.List;

record Task(String id, String name, int priority, long createdAt) {}

public class TaskMain {
    static List<Task> tasks = List.of(
            new Task("t9",  "deploy",   2, 1700000500L),
            new Task("t10", "review",   1, 1700000100L),
            new Task("t2",  "build",    2, 1700000300L),
            new Task("t1",  "test",     3, 1700000200L),
            new Task("t11", "deploy",   1, 1700000400L),
            new Task("t3",  "cleanup",  3, 1700000600L)
    );

    public static List<Task> ordernaPorNomeCrescente() {
        var listOrderedByName = tasks
                .stream()
                .sorted(
                        Comparator.comparing(Task::name)
                )
                .toList();

        System.out.println(listOrderedByName);
        return listOrderedByName;
    }

    public static List<Task> ordenaPorPrioridadeCrescente() {
        var listOrderedByPriority = tasks
                .stream()
                .sorted(
                        Comparator.comparingInt(Task::priority)
                )
                .toList();
        System.out.println(listOrderedByPriority);
        return listOrderedByPriority;
    }

    public static List<Task> ordenaPorPrioridadeDecrescente() {
        var listOrderedByPriority = tasks
                .stream()
                .sorted(
                        Comparator.comparingInt(Task::priority).reversed()
                )
                .toList();
        System.out.println(listOrderedByPriority);
        return listOrderedByPriority;
    }

    public static List<Task> ordenaPorPrioridadeDecrescenteNomeAlfabetico() {
        // priority desc, depois name ASC
        Comparator<Task> comparator = Comparator
                .comparingInt(Task::priority).reversed()
                .thenComparing(Task::name);

        // comparing(priority).thenComparing(name).reversed() -- inverte a cadeia inteira: a desc, b DESC
        // Se precisar inverter só o desempate: .thenComparing(Task::createdAt, Comparator.reverseOrder())

        var listOrderedByPriorityAndName = tasks
                .stream()
                .sorted(comparator)
                .toList();
        System.out.println(listOrderedByPriorityAndName);

        return listOrderedByPriorityAndName;
    }

    public static List<Task> ordenaPorIdTarefa() {
        // CERTO: extrai o número e compara como int
        var listOrderedById = tasks
                .stream()
                .sorted(
                        Comparator.comparingInt(task -> Integer.parseInt(task.id().substring(1)))
                )
                .toList();
        System.out.println(listOrderedById);
        return listOrderedById;
    }

    public static void main(String[] args) {
        TaskMain.ordenaPorIdTarefa();
    }
}
