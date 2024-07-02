package ru.dkalchenko.task3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Application {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final int maxI = 10_000;
        final int maxJ = 40_000;
        runCodeInMainThreadOnly(maxI, maxJ);
        runCodeInWrittenThreadPool(maxI, maxJ);
    }

    /**
     * Придуманный из головы процесс для загрузки процессора, исполняемый одним потоком Main.
     */
    private static void runCodeInMainThreadOnly(int maxI, int maxJ) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < maxI; i++) {
            for (int j = 0; j < maxJ; j++) {
                double result = Math.random() * Math.tan(i) * Math.sin(j);
            }
        }
        long end = System.currentTimeMillis();
        final int delimiter = 1_000;
        System.out.println("Работа в одном потоке заняла: " + (end - start) / delimiter + " сек.");
    }

    /**
     * Придуманный из головы процесс для загрузки процессора, исполняемый написанным Пулом потоков.
     */
    private static void runCodeInWrittenThreadPool(int maxI, int maxJ) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        final int threads = 8;
        ThreadPool threadPool = new ThreadPool(threads);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < maxI; i++) {
            int a = i;
            CompletableFuture<Void> future =
                    CompletableFuture.runAsync(() -> {
                        double result;
                        for (int j = 0; j < maxJ; j++) {
                            result = Math.tan(a) * Math.cos(j);
                        }
                    }, threadPool);
            futures.add(future);
        }
        for (Future<?> future : futures) {
            future.get();
        }
        long end = System.currentTimeMillis();
        final int delimiter = 1_000;
        System.out.println("Работа в пуле потоков заняла: " + (end - start) / delimiter + " сек.");
        threadPool.shutdown();
    }

}
