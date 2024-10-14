import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pageobject.MainPage;

import static pageobject.MainPage.URL;

public class ConstructorSectionTest {
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createWebDriver();
    }


    @Test
    @DisplayName("Переход в раздел 'Булки'.")
    public void transitionToBunsInConstructorTest() throws InterruptedException {
        driver.get(URL);
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnSaucesButton();
        mainPage.clickOnBunsButton();
        mainPage.checkToppingBun();
    }

    @Test
    @DisplayName("Переход в раздел 'Соусы'.")
    public void transitionToSaucesInConstructorTest() throws InterruptedException {
        driver.get(URL);
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnSaucesButton();
        mainPage.checkToppingSauce();
    }

    @Test
    @DisplayName("Переход в раздел 'Начинки'.")
    public void transitionToFillingsInConstructorTest() throws InterruptedException {
        driver.get(URL);
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnFillingButton();
        mainPage.checkToppingFillings();
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
