package ru.dkalchenko.task2;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskExecutionTest {

    /**
     * Реализуйте удаление из листа всех дубликатов.
     */
    @Test
    @Order(1)
    public void deleteDuplicates() {
        // given
        final List<Integer> integers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 6, 6, 7, 7, 8, 9));
        // decision
        List<Integer> uniqueIntegers = integers.stream()
                .distinct()
                .toList();
        // check
        final int expected = 9;
        assertThat(uniqueIntegers.size()).isEqualTo(expected);
    }

    /**
     * Найдите в списке целых чисел 3-е наибольшее число (пример: 5 2 10 9 4 3 10 1 13 => 10).
     */
    @Test
    @Order(2)
    public void findThirdBigNumber() {
        // given
        final List<Integer> integers = new ArrayList<>(List.of(5, 2, 10, 9, 4, 3, 10, 1, 13));
        // decision
        int result = integers.stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow();
        // check
        final int expected = 10;
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Найдите в списке целых чисел 3-е наибольшее «уникальное» число
     * (пример: 5 2 10 9 4 3 10 1 13 => 9, в отличие от прошлой задачи здесь разные 10 считает за одно число).
     */
    @Test
    @Order(3)
    public void findThirdUniqueBigNumber() {
        // given
        final List<Integer> integers = new ArrayList<>(List.of(5, 2, 6, 10, 9, 4, 3, 10, 1, 13));
        // decision
        int result = integers.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .limit(1)
                .findFirst()
                .orElseThrow();
        // check
        final int expected = 9;
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Имеется список объектов типа Сотрудник (имя, возраст, должность).
     * Необходимо получить список имен 3 самых старших сотрудников с должностью «Инженер», в порядке убывания возраста.
     */
    @Test
    @Order(4)
    public void employeesFilteringAndSorting() {
        // given
        final Employee alexandr = new Employee("Александр", 30, "Python-разработчик");
        final Employee vladislav = new Employee("Владислав", 28, "Golang-разработчик");
        final Employee denis = new Employee("Денис", 29, "Java-разработчик");
        final Employee elena = new Employee("Елена", 32, "Инженер");
        final Employee pavel = new Employee("Павел", 31, "Инженер");
        final Employee svetlana = new Employee("Светлана", 35, "Инженер");
        final Employee sergey = new Employee("Сергей", 37, "Инженер");
        List<Employee> employees = new ArrayList<>(List.of(denis, alexandr, vladislav, svetlana, elena, pavel, sergey));
        // decision
        final int numbersLimit = 3;
        List<Employee> result = employees.stream()
                .filter(employee -> "Инженер".equals(employee.position()))
                .sorted(Comparator.comparing(Employee::age).reversed())
                .limit(numbersLimit)
                .toList();
        // check
        assertThat(result).containsExactly(sergey, svetlana, elena);
    }

    /**
     * Имеется список объектов типа Сотрудник (имя, возраст, должность).
     * Посчитайте средний возраст сотрудников с должностью «Инженер».
     */
    @Test
    @Order(5)
    public void employeesFilteringAndAverageAgeCalculation() {
        // given
        final Employee denis = new Employee("Денис", 29, "Java-разработчик");
        final Employee alexandr = new Employee("Александр", 30, "Python-разработчик");
        final Employee vladislav = new Employee("Владислав", 28, "Golang-разработчик");
        final Employee svetlana = new Employee("Светлана", 35, "Инженер");
        final Employee elena = new Employee("Елена", 32, "Инженер");
        final Employee pavel = new Employee("Павел", 31, "Инженер");
        final Employee sergey = new Employee("Сергей", 37, "Инженер");
        List<Employee> employees = new ArrayList<>(List.of(denis, alexandr, vladislav, svetlana, elena, pavel, sergey));
        // decision
        double result = employees.stream()
                .filter(employee -> "Инженер".equals(employee.position()))
                .mapToInt(Employee::age)
                .average()
                .orElseThrow();
        // check
        final double expected = 33.75;
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Найдите в списке слов самое длинное.
     */
    @Test
    @Order(6)
    public void findLongestWordInStringList() {
        // given
        List<String> strings = new ArrayList<>(List.of("Каледарь", "Красота", "Реструктуризация", "Оброк"));
        // decision
        String result = strings.stream()
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
        // check
        assertThat(result).isEqualTo("Реструктуризация");
    }

    /**
     * Имеется строка с набором слов в нижнем регистре, разделенных пробелом.
     * Постройте хеш-мапы, в которой будут храниться пары: слово - сколько раз оно встречается во входной строке.
     */
    @Test
    @Order(7)
    public void hashmapWithStringsCounting() {
        // given
        String words = "спесивица, наркомат, мифология, закуска, патан, затушёвывание, ксенон, подготовление, "
                + "прокладыватель, апробирование, спесивица, наркомат, мифология, закуска, патан, затушёвывание, "
                + "ксенон, подготовление, прокладыватель, спесивица, наркомат, мифология, закуска, патан, "
                + "затушёвывание, ксенон, подготовление, спесивица, наркомат, мифология, закуска, патан, "
                + "затушёвывание, ксенон, подготовление, спесивица, наркомат, мифология, закуска, патан, "
                + "затушёвывание, ксенон, спесивица, наркомат, мифология, закуска, патан, затушёвывание, "
                + "спесивица, наркомат, мифология, закуска, патан, спесивица, наркомат, мифология, закуска, спесивица, "
                + "наркомат, мифология, спесивица, наркомат, спесивица";
        // decision
        Map<String, Long> result = Arrays.stream(words.replaceAll(" ", "").split(","))
                .collect(Collectors.groupingBy(string -> string, Collectors.counting()));
        // check
        final int expectedSize = 10;
        final Map.Entry<String, Long> entry1 = entry("апробирование", (long) 1);
        final Map.Entry<String, Long> entry2 = entry("прокладыватель", (long) 2);
        final Map.Entry<String, Long> entry3 = entry("подготовление", (long) 4);
        final Map.Entry<String, Long> entry4 = entry("ксенон", (long) 5);
        final Map.Entry<String, Long> entry5 = entry("затушёвывание", (long) 6);
        final Map.Entry<String, Long> entry6 = entry("патан", (long) 7);
        final Map.Entry<String, Long> entry7 = entry("закуска", (long) 8);
        final Map.Entry<String, Long> entry8 = entry("мифология", (long) 9);
        final Map.Entry<String, Long> entry9 = entry("наркомат", (long) 10);
        final Map.Entry<String, Long> entry10 = entry("спесивица", (long) 11);
        assertThat(result).hasSize(expectedSize)
                .containsOnly(entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9, entry10);
    }

    /**
     * Отпечатайте в консоль строки из списка в порядке увеличения длины слова.
     * Если слова имеют одинаковую длину, то должен быть сохранен алфавитный порядок.
     */
    @Test
    @Order(8)
    public void wordsLengthAndAlphabetSorting() {
        // given
        String words = "откорм, перещипывание, негритёнок, безумство, скорлупа, гарпунщик, взяткополучатель, "
                + "обливальщица, глазунья, внешность, сценичность, льносолома, ременщик, расточка, предисловие, "
                + "аспирантура, впайка, пришаркивание, урбанистка, одноженец, штука, кудель, самочинность, "
                + "подстрекатель, глушь, подпруга, юнак, каждодневность, фонетист, специалистка, буйство, "
                + "прокладывание, пахталка, грабельник, надменность, трирема, бессознательность, замусоривание, "
                + "кассия, шерстобойня, белизна, неизведанное, подготовитель, медовар, рафинад, терлик, баптистка, "
                + "новшество, забег, дезориентирование";
        // decision
        List<String> result = Arrays.stream(words.replaceAll(" ", "").split(","))
                .sorted(Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()))
                .toList();
        // check
        assertThat(result).containsExactly("юнак",
                "глушь", "забег", "штука",
                "впайка", "кассия", "кудель", "откорм", "терлик",
                "белизна", "буйство", "медовар", "рафинад", "трирема",
                "глазунья", "пахталка", "подпруга", "расточка", "ременщик", "скорлупа", "фонетист",
                "баптистка", "безумство", "внешность", "гарпунщик", "новшество",
                "одноженец", "грабельник", "льносолома", "негритёнок", "урбанистка",
                "аспирантура", "надменность", "предисловие", "сценичность", "шерстобойня",
                "неизведанное", "обливальщица", "самочинность", "специалистка",
                "замусоривание", "перещипывание", "подготовитель", "подстрекатель", "пришаркивание", "прокладывание",
                "каждодневность",
                "взяткополучатель",
                "бессознательность", "дезориентирование");
    }

    /**
     * Имеется массив строк, в каждой из которых лежит набор из 5 строк, разделенных пробелом.
     * Найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них.
     */
    @Test
    @Order(9)
    public void findLongestWordInArrayOfStrings() {
        // given
        String[] stringArray = new String[] {
                "Я пишу код на Java",
                "Периодически приходится писать на Kotlin",
                "Разработка программного обеспечения отличная профессия",
                "Высокие зарплаты вполне реальная история",
                "Так что приходите в IT"};
        // decision
        String result = Arrays.stream(stringArray)
                .map(words -> words.split(" "))
                .flatMap(Arrays::stream)
                .max(Comparator.comparing(String::length))
                .orElseThrow();
        // check
        assertThat(result).isIn("Периодически", "программного");
    }

}
