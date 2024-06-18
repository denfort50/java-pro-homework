package ru.dkalchenko.task1;

import ru.dkalchenko.task1.annotation.*;

public class TestRunnerTest {

    @BeforeSuite
    public static void beforeSuite() {
        System.out.println("Этот метод выполняется 1 раз перед всеми тестами.");
    }

    @AfterSuite
    public static void afterSuite() {
        System.out.println("Этот метод выполняется 1 раз после всех тестов.");
    }

    @BeforeTest
    public static void beforeTest() {
        System.out.println("Этот метод выполняется перед каждым тестом.");
    }

    @AfterTest
    public static void afterTest() {
        System.out.println("Этот метод выполняется после каждого теста.");
    }

    @Test(priority = 1)
    public void firstTest() {
        System.out.println("Первый тест выполнен.");
    }

    @Test(priority = 2)
    public void secondTest() {
        System.out.println("Второй тест выполнен.");
    }

    @Test(priority = 3)
    public void thirdTest() {
        System.out.println("Третий тест выполнен.");
    }

    @Test(priority = 4)
    public void fourthTest() {
        System.out.println("Четвертый тест выполнен.");
    }

    @CsvSource("10, Java, 20, true")
    public void csvSourceTest1(int a, String b, int c, boolean d) {
        System.out.println("===============ПАРСИНГ_ПАРАМЕТРОВ===============");
        System.out.println("int a = " + a);
        System.out.println("String b = " + b);
        System.out.println("int c = " + c);
        System.out.println("boolean d = " + d);
    }

    @CsvSource("Java, true, Kotlin, 30, C#, false")
    public void csvSourceTest2(String a, boolean b, String c, int d, String e, boolean f) {
        System.out.println("===============ПАРСИНГ_ПАРАМЕТРОВ===============");
        System.out.println("String a = " + a);
        System.out.println("boolean b = " + b);
        System.out.println("String c = " + c);
        System.out.println("int d = " + d);
        System.out.println("String e = " + e);
        System.out.println("boolean f = " + f);
    }

    // Закоментированная часть для проверки работы валидации
    /*
    @Test(priority = 0)
    public void zeroTest() {
        System.out.println("Нулевой тест выполнен.");
    }

    @Test(priority = 11)
    public void eleventhTest() {
        System.out.println("Одиннадцатый тест выполнен.");
    }

    @Test(priority = 6)
    public static void testIncorrect() {
        System.out.println("Применяем аннотацию @Test на статическом методе.");
    }

    @BeforeSuite
    public void beforeSuiteIncorrect() {
        System.out.println("Применяем аннотацию @BeforeSuite на нестатическом методе.");
    }

    @AfterSuite
    public void afterSuiteIncorrect() {
        System.out.println("Применяем аннотацию @AfterSuite на нестатическом методе.");
    }
    */

}
