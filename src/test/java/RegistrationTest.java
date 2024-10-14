import client.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;
import org.openqa.selenium.WebDriver;

import client.ApiUser;
import pageobject.RegisterPage;
import pageobject.LoginPage;
import pageobject.MainPage;


import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static pageobject.MainPage.URL;

public class RegistrationTest {
    private WebDriver driver;
    private User user;
    private ApiUser apiUser;
    public static String accessToken;

    String name = randomAlphanumeric(4, 8);
    String email = randomAlphanumeric(6, 10) + "@yandex.ru";
    String password = randomAlphanumeric(10, 20);
    String passwordFailed = randomAlphanumeric(0, 5);

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        user = new User(email, password, name);
        apiUser = new ApiUser();
        driver = WebDriverFactory.createWebDriver();
    }


    @Test
    @DisplayName("Успешная регистрация.")
    public void successfulRegistrationTest() {
        driver.get(URL);
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnLoginButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickOnRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.waitForLoadRegisterPage();
        registerPage.registration(name, email, password);
        loginPage.waitForLoadEntrance();
        user = new User(email, password, name);
        accessToken = ApiUser.checkRequestAuthLogin(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Неуспешная регистрация пользователя.")
    public void failedPasswordRegistrationTest() {
        driver.get(URL);
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnLoginButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickOnRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.waitForLoadRegisterPage();
        registerPage.registration(name, email, passwordFailed);
        Assert.assertTrue("Текст об ошибке отсутствует", driver.findElement(registerPage.errorPasswordText).isDisplayed());
    }

    @After
    public void tearDown() {
        driver.quit();
    }
    @After
    public void deleteUserTest() {
        if (accessToken != null) {
            ApiUser.deleteUser(accessToken);
        }
    }


}
