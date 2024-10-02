import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.After;
import pageObject.LoginPage;
import pageObject.MainPage;
import pageObject.RegisterPage;
import pageObject.PasswordPage;

import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;


@RunWith(Parameterized.class)
    public class LoginTest {
        private WebDriver driver;
        private String driverType;
        private final static String EMAIL = "Wirtasll@yandex.ru";
        private final static String PASSWORD = "qwertyqwerty";

        public LoginTest(String driverType) {
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
                driver.navigate().to("https://stellarburgers.nomoreparties.site/");
            } else if (driverType.equals("yandexdriver")) {
                System.setProperty("webdriver.chrome.driver", "/WebDriver/bin/chromedriver126.exe");
                // Установка пути к браузеру Yandex
                ChromeOptions options = new ChromeOptions();
                options.setBinary("/Users/Иван/AppData/Local/Yandex/YandexBrowser/Application/browser.exe");
                driver = new ChromeDriver(options);
                // Ожидание
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                // Переход на тестируемый сайт
                driver.navigate().to("https://stellarburgers.nomoreparties.site/");
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
        @DisplayName("Вход по кнопке 'Войти в аккаунт'.")
        public void enterByLoginButtonTest() {
            MainPage mainPage = new MainPage(driver);
            mainPage.clickOnLoginButton();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.authorization(EMAIL, PASSWORD);
            mainPage.waitForLoadMainPage();
        }

        @Test
        @DisplayName("Вход по кнопке 'Личный Кабинет'.")
        public void enterByPersonalAccountButtonTest() {
            MainPage mainPage = new MainPage(driver);
            mainPage.clickOnAccountButton();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.authorization(EMAIL, PASSWORD);
            mainPage.waitForLoadMainPage();
        }

        @Test
        @DisplayName("Вход через кнопку в форме регистрации.")
        public void enterByRegistrationFormTest() {
            MainPage mainPage = new MainPage(driver);
            mainPage.clickOnLoginButton();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.clickOnRegister();
            RegisterPage registerPage = new RegisterPage(driver);
            String name = randomAlphanumeric(4, 8);
            String email = randomAlphanumeric(6, 10) + "@yandex.ru";;
            String password = randomAlphanumeric(6, 10);
            registerPage.registration(name, email, password);
            loginPage.waitForLoadEntrance();
            loginPage.authorization(email, password);
            mainPage.waitForLoadMainPage();
        }

        @Test
        @DisplayName("Вход через кнопку в форме восстановления пароля.")
        public void enterByPasswordRecoveryFormatTest() {
            MainPage mainPage = new MainPage(driver);
            mainPage.clickOnAccountButton();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.clickOnForgotPasswordLink();
            PasswordPage PasswordPage = new PasswordPage(driver);
            PasswordPage.waitForLoadedRecoverPassword();
            PasswordPage.clickOnLoginLink();
            loginPage.authorization(EMAIL, PASSWORD);
            mainPage.waitForLoadMainPage();
        }

        @After
        public void tearDown() {
            driver.quit();
        }
}
