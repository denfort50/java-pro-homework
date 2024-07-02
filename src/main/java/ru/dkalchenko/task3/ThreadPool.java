package ru.dkalchenko.task3;

import java.util.LinkedList;
import java.util.concurrent.Executor;

public class ThreadPool implements Executor {

    private final LinkedList<Runnable> taskQueue;
    private boolean isRunning;

    public ThreadPool(int threads) {
        this.taskQueue = new LinkedList<>();
        this.isRunning = true;
        for (int i = 0; i < threads; i++) {
            new Thread(new Worker()).start();
        }
    }

    @Override
    public void execute(Runnable command) {
        if (isRunning) {
            taskQueue.offer(command);
        } else {
            throw new IllegalStateException("Не могу взять задачу в работу, инициализировано закрытие всех потоков");
        }
    }

    public void shutdown() {
        isRunning = false;
    }

    public void awaitTermination() throws InterruptedException {
        while (!taskQueue.isEmpty()) {
            Thread.currentThread().wait();
        }
    }

    /**
     * Класс сделан внутренним, чтобы у Worker'ов был прямой доступ к полям "isRunning" и "taskQueue".
     */
    private final class Worker implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                synchronized (taskQueue) {
                    Runnable nextTask = taskQueue.poll();
                    if (nextTask != null) {
                        nextTask.run();
                    }
                }
            }
        }
    }

}
