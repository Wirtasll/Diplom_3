import client.ApiUser;
import client.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import page.object.LoginPage;
import page.object.MainPage;
import page.object.ProfilePage;


import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static page.object.MainPage.URL;

@RunWith(Parameterized.class)
public class AccountTest {

    private WebDriver driver;
    private String driverType;
    private final String name = randomAlphanumeric(4, 8);
    private final String email = randomAlphanumeric(6, 10) + "@yandex.ru";
    private final String password = randomAlphanumeric(10, 20);
    private User user;
    private ApiUser apiUser;
    private static String accessToken;

    public AccountTest(String driverType) {
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
    @DisplayName("Переход в личный кабинет.")
    public void transitionToProfilePageTest() {
        user = new User(email, password, name);
        ApiUser.postCreateNewUser(user);
        driver.get(URL);
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        Assert.assertTrue("Страница авторизации не отобразилась", driver.findElement(loginPage.entrance).isDisplayed());
    }

    @Test
    @DisplayName("Переход в конструктор из личного кабинета.")
    public void transitionToConstructorFromProfilePageTest() {
        user = new User(email, password, name);
        ApiUser.postCreateNewUser(user);
        driver.get(URL);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForInvisibilityLoadingAnimation();
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        loginPage.clickOnConstructorButton();
        mainPage.waitForLoadMainPage();
        Assert.assertTrue("Переход  в конструктор из личного кабинете не прошел", driver.findElement(mainPage.textBurgerMainPage).isDisplayed());
    }

    @Test
    @DisplayName("Клик по логотипу 'Stellar Burgers'.")
    public void transitionToStellarBurgersFromProfilePageTest() {
        user = new User(email, password, name);
        ApiUser.postCreateNewUser(user);
        driver.get(URL);
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        loginPage.clickOnLogo();
        mainPage.waitForLoadMainPage();
        Assert.assertTrue("Конструктор при клике на логотип не загрузился", driver.findElement(mainPage.textBurgerMainPage).isDisplayed());
    }

    @Test
    @DisplayName("Выход из аккаунта")
    public void exitFromProfileTest() {
        user = new User(email, password, name);
        ApiUser.postCreateNewUser(user);
        driver.get(URL);
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        loginPage.authorization(email, password);
        mainPage.waitForLoadMainPage();
        mainPage.clickOnAccountButton();
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForLoadProfilePage();
        profilePage.clickOnExitButton();
        mainPage.waitForInvisibilityLoadingAnimation();
        Assert.assertTrue("Не удалось выйти из аккаунта", driver.findElement(loginPage.entrance).isDisplayed());
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
