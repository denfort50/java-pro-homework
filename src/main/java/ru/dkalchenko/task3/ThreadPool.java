package ru.dkalchenko.task3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

public class ThreadPool implements Executor {

    private final LinkedList<Runnable> taskQueue;
    private final List<Worker> workers;
    private volatile boolean isRunning;

    public ThreadPool(int workersAmount) {
        taskQueue = new LinkedList<>();
        workers = new ArrayList<>(workersAmount);
        isRunning = true;
        for (int i = 0; i < workersAmount; i++) {
            Worker worker = new Worker();
            worker.setName("№" + i);
            this.workers.add(worker);
            worker.start();
        }
    }

    @Override
    public void execute(Runnable command) {
        synchronized (taskQueue) {
            if (isRunning) {
                taskQueue.offer(command);
                taskQueue.notifyAll();
            } else {
                throw new IllegalStateException("Не могу взять задачу в работу, потоки уже закрываются");
            }
        }
    }

    public void shutdown() {
        isRunning = false;
        workers.forEach(Thread::interrupt);
    }

    /**
     * Класс сделан внутренним, чтобы у Worker'ов был прямой доступ к полям "isRunning" и "taskQueue".
     */
    private final class Worker extends Thread {

        @Override
        public void run() {
            while (isRunning) {
                Runnable nextTask;
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty() && isRunning) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Работа потока " + Thread.currentThread().getName() + " прервана");
                        }
                    }
                    nextTask = taskQueue.poll();
                }
                if (nextTask != null) {
                    nextTask.run();
                }
            }
        }
    }

}
