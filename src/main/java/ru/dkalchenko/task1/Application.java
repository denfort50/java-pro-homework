package ru.dkalchenko.task1;

import ru.dkalchenko.task1.processor.TestRunner;

public class Application {

    public static void main(String[] args) {
        System.out.println("Домашнее задание №1 выполнено.");
        System.out.println("====================РЕЗУЛЬТАТ====================");
        TestRunner.runTests(TestRunnerTest.class);
    }

}
