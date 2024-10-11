import client.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import client.ApiUser;
import page.object.RegisterPage;
import page.object.LoginPage;
import page.object.MainPage;


import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static page.object.MainPage.URL;

@RunWith(Parameterized.class)
public class RegistrationTest {
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
    public void setUp() {
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
    @DisplayName("Успешная регистрация.")
    public void successfulRegistrationTest() {
        driver.get(URL);
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
        driver.get(URL);
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
    public static void deleteUserTest() {
        if (accessToken != null) {
            ApiUser.deleteUser(accessToken);
        }
    }


}
