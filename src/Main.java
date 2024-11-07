import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        Scanner scanner = new Scanner(System.in);
        double minValue, maxValue;

        // Введення діапазону від користувача
        while (true) {
            System.out.print("Введіть мінімальне значення діапазону (від 0.5): ");
            minValue = scanner.nextDouble();
            System.out.print("Введіть максимальне значення діапазону (до 99.5): ");
            maxValue = scanner.nextDouble();

            // Перевірка коректного вводу
            if (minValue >= 0.5 && maxValue <= 99.5 && minValue < maxValue) {
                break;
            } else {
                System.out.println("Помилка: переконайтеся, що мінімальне значення не менше 0.5, максимальне значення не більше 99.5, і мінімальне значення менше максимального.");
            }
        }

        CopyOnWriteArraySet<Double> values = new CopyOnWriteArraySet<>();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Random random = new Random();
        int arraySize = random.nextInt(40, 60);

        // Генерація випадкових значень і додавання їх в набір
        for (int i = 0; i < arraySize; i++) {
            double randomValue = minValue + (maxValue - minValue) * random.nextDouble();
            values.add(randomValue);
        }
        System.out.print("Згенеровані значення: [");
        values.forEach(value -> System.out.printf("%.2f; ", value));
        System.out.println("]");

        // Розділення значень на три частини
        List<Double> part1 = new ArrayList<>();
        List<Double> part2 = new ArrayList<>();
        List<Double> part3 = new ArrayList<>();

        // Ділення діапазону на три частини [0.5 - 33.5], [33.5 - 66.5], [66.5 - 99.5]
        double range1 = minValue + (maxValue - minValue) / 3;
        double range2 = minValue + 2 * (maxValue - minValue) / 3;

        for (Double value : values) {
            if (value <= range1) {
                part1.add(value);
            } else if (value <= range2) {
                part2.add(value);
            } else {
                part3.add(value);
            }
        }

        System.out.print("Частина 1: [ ");
        part1.forEach(value -> System.out.printf("%.2f; ", value));
        System.out.println("]");

        System.out.print("Частина 2: [");
        part2.forEach(value -> System.out.printf("%.2f; ", value));
        System.out.println("]");

        System.out.print("Частина 3: [");
        part3.forEach(value -> System.out.printf("%.2f; ", value));
        System.out.println("]");


        // Створення завдань
        SquareTask task1 = new SquareTask(part1);
        SquareTask task2 = new SquareTask(part2);
        SquareTask task3 = new SquareTask(part3);

        Map<Double, Double> results = new HashMap<>();

        try {

            Future<Map<Double, Double>> future1 = executorService.submit(task1);
            Future<Map<Double, Double>> future2 = executorService.submit(task2);
            Future<Map<Double, Double>> future3 = executorService.submit(task3);

            while (true) {
                if (future1.isDone() && !future1.isCancelled()) {
                    results.putAll(future1.get());
                }
                if (future2.isDone() && !future2.isCancelled()) {
                    results.putAll(future2.get());
                }
                if (future3.isDone() && !future3.isCancelled()) {
                    results.putAll(future3.get());
                }

                if (future1.isDone() && future2.isDone() && future3.isDone()) {
                    break;
                }
            }


        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());;
        } finally {
            executorService.shutdown();
        }

        System.out.println("Результати (число -> квадрат):");
        for (Map.Entry<Double, Double> entry : results.entrySet()) {
            System.out.printf("%.2f -> %.2f%n", entry.getKey(), entry.getValue());
        }


        long endTime = System.currentTimeMillis();
        System.out.println("Програма завершилася. Час виконання: " + (endTime - startTime) + " мс");
    }
}


