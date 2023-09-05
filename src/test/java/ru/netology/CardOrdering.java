package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardOrderingTest {
    private WebDriver driver;
    private static ChromeOptions options;

    @BeforeAll
    static void setUpAll() {
        options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void testPositiveAllInput() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Петров-Иванов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79512852525");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldCardFormInvalidName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivan Petrov"); // Имя набрано латиницей
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79512852525");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldCardFormInvalidPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+795128525252");
        driver.findElement(By.className("button")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldCardFormWithoutName() { // В заявке отсутствует имя
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79512852525");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldCardFormWithoutPhone() { // В заявке отсутствует номер телефона
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Смит Джон");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldCardFormWithoutCheckbox() { // В заявке не проставлен чек-бокс
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79512852525");
        driver.findElement(By.className("button")).click();

        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actual = driver.findElement(By.cssSelector("[data-test-id=`agreement`].input_invalid .checkbox__text")).getText().trim();

        assertEquals(expected, actual);
    }
}
