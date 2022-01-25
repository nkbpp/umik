package ru.pfr.controller;

import org.junit.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpfrControllerTest {

    //В следующей таблице приведен обзор имеющихся в аннотации JUnit 4.x.

    //Аннотация 	                        Описание
    /*@Test
    public void method() 	                Аннотация @Test определяет что метод method() является тестовым.*/
    /*@Before
    public void method() 	                Аннотация @Before указывает на то, что метод будет выполнятся
                                            перед каждым тестируемым методом @Test.*/
    /*@After
    public void method() 	                Аннотация @After указывает на то что метод будет выполнятся
                                            после каждого тестируемого метода @Test*/
    /*@BeforeClass
    public static void method() 	        Аннотация @BeforeClass указывает на то, что метод будет
                                            выполнятся в начале всех тестов, а точней в момент запуска
                                            тестов(перед всеми тестами @Test).*/
    /*@AfterClass
    public static void method() 	        Аннотация @AfterClass  указывает на то, что метод будет
                                            выполнятся после всех тестов.*/
    /*@Ignore 	                            Аннотация @Ignore говорит, что метод будет проигнорирован в
                                            момент проведения тестирования.*/
    /*@Test (expected = Exception.class)    (expected = Exception.class) – указывает на то, что в данном
                                            тестовом методе вы преднамеренно ожидается Exception.*/
    /*@Test (timeout=100) 	                (timeout=100) – указывает, что тестируемый метод не должен
                                            занимать больше чем 100 миллисекунд.*/

    //Проверяемые методы (основные)

    //Метод 	                                         Описание
    /*fail(String) 	                                     Указывает на то что бы тестовый метод завалился при
                                                         этом выводя текстовое сообщение.*/

    /*assertTrue([message], boolean condition) 	         Проверяет, что логическое условие истинно.*/

    /*assertsEquals([String message], expected, actual)  Проверяет, что два значения совпадают.
                                                         Примечание: для массивов проверяются ссылки, а не
                                                         содержание массивов.*/

    /*assertNull([message], object)                      Проверяет, что объект является пустым null.*/
    /*assertNotNull([message], object) 	                 Проверяет, что объект не является пустым null.*/
    /*assertSame([String], expected, actual) 	         Проверяет, что обе переменные относятся к одному
                                                         объекту.*/
    /*assertNotSame([String], expected, actual) 	     Проверяет, что обе переменные относятся к разным
                                                         объектам.*/
    /*assertArrayEquals                                  Функция для сравнения массивов, которая
                                                         сравнивает эквивалентность каждого элемента обоих
                                                         массивов друг с другом*/
    @BeforeClass
    public static void globalSetUp() {
        System.out.println("Initial setup...");
        System.out.println("Test OpfrControllerTest");

    }


    private OpfrController opfrController;

    @Before
    public void setUp() {
        System.out.println("Code executes before each test method");
        opfrController = new OpfrController();
    }

    @Test
    public void dateddMMyyyy() {

        //expected
        Date expected = null;
        String date2 = "13.08.2020";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            expected = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //actual
        String s = "13.08.2020";
        Date actual = opfrController.dateddMMyyyy(s);

        assertEquals(expected,actual);

    }

    @Test
    public void dateyyyyMMdd() {

        //expected
        Date expected = null;
        String date2 = "13.08.2020";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            expected = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //actual
        String s = "2020-08-13";
        Date actual = opfrController.dateyyyyMMdd(s);

        assertEquals(expected,actual);

    }

    @Test
    public void okrug() {

        double f = 15.33333f;
        String s = opfrController.okrug(f);

        assertEquals("15,33",s);
        assertEquals("1,00",opfrController.okrug(1d));

        //assertNull("Not NaN for null", opfrController.okrug());

    }

    @AfterClass
    public static void tearDown() {
        System.out.println("Tests finished");
    }

    @After
    public void afterMethod() {
        System.out.println("Code executes after each test method");
    }

}