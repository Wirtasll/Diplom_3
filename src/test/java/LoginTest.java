import client.ApiUser;
import client.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.junit.After;
import page.object.LoginPage;
import page.object.MainPage;
import page.object.RegisterPage;
import page.object.PasswordPage;


import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static page.object.MainPage.URL;


@RunWith(Parameterized.class)
    public class LoginTest {

        private WebDriver driver;
        private String driverType;
        private final String name = randomAlphanumeric(4, 8);
        private final String email = randomAlphanumeric(6, 10) + "@yandex.ru";
        private final String password = randomAlphanumeric(10, 20);
        private User user;
        private ApiUser apiUser;
        private static String accessToken;


        public LoginTest(String driverType) {
            this.driverType = driverType;
        }

        @Before
        public void setUp() {
            RestAssured.baseURI = URL;
            user = new User(email, password, name);
            apiUser = new ApiUser();
            accessToken = ApiUser.checkRequestAuthLogin(user).then().extract().path("accessToken");
            driver = WebDriverFactory.createWebDriver();
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
            user = new User(email, password, name);
            ApiUser.postCreateNewUser(user);
            driver.get(URL);
            MainPage mainPage = new MainPage(driver);
            mainPage.clickOnLoginButton();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.authorization(email, password);
            mainPage.waitForLoadMainPage();
        }

        @Test
        @DisplayName("Вход по кнопке 'Личный Кабинет'.")
        public void enterByPersonalAccountButtonTest() {
            user = new User(email, password, name);
            ApiUser.postCreateNewUser(user);
            driver.get(URL);
            MainPage mainPage = new MainPage(driver);
            mainPage.clickOnAccountButton();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.authorization(email, password);
            mainPage.waitForLoadMainPage();
        }

        @Test
        @DisplayName("Вход через кнопку в форме регистрации.")
        public void enterByRegistrationFormTest() {
            driver.get(URL);
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
            user = new User(email, password, name);
            ApiUser.postCreateNewUser(user);
            driver.get(URL);
            MainPage mainPage = new MainPage(driver);
            mainPage.clickOnAccountButton();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.clickOnForgotPasswordLink();
            PasswordPage PasswordPage = new PasswordPage(driver);
            PasswordPage.waitForLoadedRecoverPassword();
            PasswordPage.clickOnLoginLink();
            loginPage.authorization(email, password);
            mainPage.waitForLoadMainPage();
        }

        @After
        public void tearDown() {
            driver.quit();
        }
        @AfterClass
        public static void deleteUserTest() {
        if (accessToken != null) {
            ApiUser.deleteUser(accessToken);
        }
    }
}
