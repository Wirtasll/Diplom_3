import client.ApiUser;
import client.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import pageobject.LoginPage;
import pageobject.MainPage;
import pageobject.ProfilePage;


import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static pageobject.MainPage.URL;

public class AccountTest {

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
    @DisplayName("Переход в личный кабинет.")
    public void transitionToProfilePageTest() {
        user = new User(email, password, name);
        ApiUser.postCreateNewUser(user);
        accessToken = ApiUser.checkRequestAuthLogin(user).then().extract().path("accessToken");
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
        accessToken = ApiUser.checkRequestAuthLogin(user).then().extract().path("accessToken");
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
        accessToken = ApiUser.checkRequestAuthLogin(user).then().extract().path("accessToken");
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
        accessToken = ApiUser.checkRequestAuthLogin(user).then().extract().path("accessToken");
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
    @After
    public void deleteUserTest() {
        if (accessToken != null) {
            ApiUser.deleteUser(accessToken);
        }
    }
}
