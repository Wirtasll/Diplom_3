import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import page_object.ApiUser;
import page_object.RegisterPage;
import page_object.LoginPage;
import page_object.MainPage;


import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@RunWith(Parameterized.class)
public class RegistrationTest {
    final String URL = "https://stellarburgers.nomoreparties.site/";
    private WebDriver driver;
    private String driverType;
    public static String accessToken;

    String NAME = randomAlphanumeric(4, 8);
    String EMAIL = randomAlphanumeric(6, 10) + "@yandex.ru";
    String PASSWORD = randomAlphanumeric(10, 20);
    String PASSWORD_FAILED = randomAlphanumeric(0, 5);

    public RegistrationTest(String driverType) {
        this.driverType = driverType;
    }

    @Before
    public void startUp() {
        if (driverType.equals("chromedriver")) {
            System.setProperty("webdriver.chrome.driver", "/WebDriver/bin/chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
            // Ожидание
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            // Переход на тестируемый сайт
            driver.navigate().to(URL);
        } else if (driverType.equals("yandexdriver")) {
            System.setProperty("webdriver.chrome.driver", "/WebDriver/bin/chromedriver126.exe");
            // Установка пути к браузеру Yandex
            ChromeOptions options = new ChromeOptions();
            options.setBinary("/Users/Иван/AppData/Local/Yandex/YandexBrowser/Application/browser.exe");
            driver = new ChromeDriver(options);
            // Ожидание
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            // Переход на тестируемый сайт
            driver.navigate().to(URL);
        }
    }

    @Parameterized.Parameters(name = "Результаты проверок браузера: {0}")
    public static Object[][] getDataDriver() {
        return new Object[][]{
                {"chromedriver"},
                {"yandexdriver"},
        };
    }

    @Test
    @DisplayName("Успешная регистрация.")
    public void successfulRegistrationTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnLoginButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickOnRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.waitForLoadRegisterPage();
        registerPage.registration(NAME, EMAIL, PASSWORD);
        loginPage.waitForLoadEntrance();
    }

    @Test
    @DisplayName("Неуспешная регистрация пользователя.")
    public void failedPasswordRegistrationTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnLoginButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickOnRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.waitForLoadRegisterPage();
        registerPage.registration(NAME, EMAIL, PASSWORD_FAILED);
        Assert.assertTrue("Текст об ошибке отсутствует", driver.findElement(registerPage.errorPasswordText).isDisplayed());
    }

    @After
    public void tearDown() {
        driver.quit();
    }
    @AfterClass
    public static void afterClass() {
        ApiUser.deleteUser(accessToken);
    }


}
