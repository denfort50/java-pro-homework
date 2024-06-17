package ru.dkalchenko.task1.processor;

import ru.dkalchenko.task1.annotation.*;
import ru.dkalchenko.task1.exception.IllegalAnnotationAmountException;
import ru.dkalchenko.task1.exception.IllegalAnnotationUsageException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.lang.reflect.Modifier.isStatic;

public class TestRunner {

    public static void runTests(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        validateAnnotationUsage(declaredMethods);

        Optional<Method> beforeSuiteMethod = Optional.empty();
        Optional<Method> beforeTestMethod = Optional.empty();
        List<Method> testMethods = new ArrayList<>();
        Optional<Method> afterTestMethod = Optional.empty();
        Optional<Method> afterSuiteMethod = Optional.empty();
        List<Method> csvSourceMethods = new ArrayList<>();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                beforeSuiteMethod = Optional.of(method);
            }
            if (method.isAnnotationPresent(BeforeTest.class)) {
                beforeTestMethod = Optional.of(method);
            }
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                afterSuiteMethod = Optional.of(method);
            }
            if (method.isAnnotationPresent(AfterTest.class)) {
                afterTestMethod = Optional.of(method);
            }
            if (method.isAnnotationPresent(CsvSource.class)) {
                csvSourceMethods.add(method);
            }
        }
        testMethods.sort(Comparator.comparingInt(method -> method.getDeclaredAnnotation(Test.class).priority()));

        beforeSuiteMethod.ifPresent(method -> invokeMethodOnNewInstance(clazz, method));
        if (beforeTestMethod.isEmpty() && afterTestMethod.isEmpty()) {
            testMethods.forEach(method -> invokeMethodOnNewInstance(clazz, method));
        } else if (beforeTestMethod.isPresent() && afterTestMethod.isPresent()) {
            for (Method method : testMethods) {
                invokeMethodOnNewInstance(clazz, beforeTestMethod.get());
                invokeMethodOnNewInstance(clazz, method);
                invokeMethodOnNewInstance(clazz, afterTestMethod.get());
            }
        } else if (beforeTestMethod.isPresent()) {
            for (Method method : testMethods) {
                invokeMethodOnNewInstance(clazz, beforeTestMethod.get());
                invokeMethodOnNewInstance(clazz, method);
            }
        } else {
            for (Method method : testMethods) {
                invokeMethodOnNewInstance(clazz, method);
                invokeMethodOnNewInstance(clazz, afterTestMethod.get());
            }
        }
        afterSuiteMethod.ifPresent(method -> invokeMethodOnNewInstance(clazz, method));

        for (Method method : csvSourceMethods) {
            String value = method.getDeclaredAnnotation(CsvSource.class).value();
            String[] parts = value.replaceAll(" ", "").split(",");
            Parameter[] parameters = method.getParameters();
            Object[] values = new Object[parameters.length];
            int counter = 0;
            for (Parameter parameter : parameters) {
                if ("int".equals(parameter.getType().getName())) {
                    values[counter] = Integer.valueOf(parts[counter++]);
                } else if ("boolean".equals(parameter.getType().getName())) {
                    values[counter] = Boolean.valueOf(parts[counter++]);
                } else if ("java.lang.String".equals(parameter.getType().getName())) {
                    values[counter] = parts[counter++];
                }
            }

            try {
                method.invoke(clazz.getDeclaredConstructor().newInstance(), values);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException
                     | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void validateAnnotationUsage(Method[] declaredMethods) {
        int beforeSuiteMethods = 0;
        int afterSuiteMethods = 0;
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                String annotationName = "BeforeSuite";
                validateIfAnnotationUsedOnStaticMethod(method, annotationName);
                beforeSuiteMethods++;
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                String annotationName = "AfterSuite";
                validateIfAnnotationUsedOnStaticMethod(method, annotationName);
                afterSuiteMethods++;
            }
            if (method.isAnnotationPresent(Test.class)) {
                String annotationName = "Test";
                validateIfAnnotationUsedOnNotStaticMethod(method, annotationName);
                validateRangeOfPriorityParameter(method);
            }
        }
        if (beforeSuiteMethods > 1 && afterSuiteMethods > 1) {
            throw new IllegalAnnotationAmountException(
                    "Аннотация @BeforeSuite и аннотация @AfterSuite могут быть применены только к одному методу");
        } else if (beforeSuiteMethods > 1) {
            throw new IllegalAnnotationAmountException(
                    "Аннотация @BeforeSuite может быть применена только к одному методу");
        } else if (afterSuiteMethods > 1) {
            throw new IllegalAnnotationAmountException(
                    "Аннотация @AfterSuite может быть применена только к одному методу");
        }
    }

    private static void validateIfAnnotationUsedOnStaticMethod(Method declaredMethod, String annotationName) {
        if (!isStatic(declaredMethod.getModifiers())) {
            throw new IllegalAnnotationUsageException(
                    "Аннотация @" + annotationName + " может быть применена только к статическому методу");
        }
    }

    private static void validateIfAnnotationUsedOnNotStaticMethod(Method declaredMethod, String annotationName) {
        if (isStatic(declaredMethod.getModifiers())) {
            throw new IllegalAnnotationUsageException(
                    "Аннотация @" + annotationName + " может быть применена только к нестатическому методу");
        }
    }

    private static void validateRangeOfPriorityParameter(Method method) {
        int priority = method.getDeclaredAnnotation(Test.class).priority();
        final int start = 1;
        final int finish = 10;
        if (priority < start || priority > finish) {
            throw new IllegalAnnotationUsageException(
                    "Параметр priority аннотации @Test может быть только в диапазоне от 1 до 10");
        }
    }

    private static void invokeMethodOnNewInstance(Class<?> clazz, Method method) {
        try {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException
                 | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}
