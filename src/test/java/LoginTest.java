import client.ApiUser;
import client.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.junit.After;
import pageobject.LoginPage;
import pageobject.MainPage;
import pageobject.RegisterPage;
import pageobject.PasswordPage;


import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static pageobject.MainPage.URL;


    public class LoginTest {

        private WebDriver driver;
        private final String name = randomAlphanumeric(4, 8);
        private final String email = randomAlphanumeric(6, 10) + "@yandex.ru";
        private final String password = randomAlphanumeric(10, 20);
        private User user;
        private ApiUser apiUser;
        private static String accessToken;


        @Before
        public void setUp() {
            RestAssured.baseURI = URL;
            user = new User(email, password, name);
            apiUser = new ApiUser();
            driver = WebDriverFactory.createWebDriver();
        }



        @Test
        @DisplayName("Вход по кнопке 'Войти в аккаунт'.")
        public void enterByLoginButtonTest() {
            user = new User(email, password, name);
            ApiUser.postCreateNewUser(user);
            accessToken = ApiUser.checkRequestAuthLogin(user).then().extract().path("accessToken");
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
            accessToken = ApiUser.checkRequestAuthLogin(user).then().extract().path("accessToken");
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
            user = new User(email, password, name);
            accessToken = ApiUser.checkRequestAuthLogin(user).then().extract().path("accessToken");
        }

        @Test
        @DisplayName("Вход через кнопку в форме восстановления пароля.")
        public void enterByPasswordRecoveryFormatTest() {
            user = new User(email, password, name);
            ApiUser.postCreateNewUser(user);
            accessToken = ApiUser.checkRequestAuthLogin(user).then().extract().path("accessToken");
            driver.get(URL);
            MainPage mainPage = new MainPage(driver);
            mainPage.clickOnAccountButton();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.clickOnForgotPasswordLink();
            PasswordPage passwordPage = new PasswordPage(driver);
            passwordPage.waitForLoadedRecoverPassword();
            passwordPage.clickOnLoginLink();
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
