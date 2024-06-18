package ru.dkalchenko;

import ru.dkalchenko.task1.TestRunnerTest;
import ru.dkalchenko.task1.processor.TestRunner;

public class App {

    public static void main(String[] args) {
        System.out.println("Домашнее задание №1 выполнено.");
        System.out.println("====================РЕЗУЛЬТАТ====================");
        TestRunner.runTests(TestRunnerTest.class);
    }

}
