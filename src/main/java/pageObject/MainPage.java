package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

public class MainPage {

    private final WebDriver driver;

    //Кнопка "Войти в аккаунт"
    private final By loginButton = By.xpath(".//button[text()='Войти в аккаунт']");
    //Кнопка "Личный кабинет"
    private final By accountButton = By.xpath(".//a[@href='/account']");
    //Логотип "Stellar Burgers"
    private final By logo = By.xpath(".//div/a[@href='/']");
    //Кнопка "Конструктор"
    private final By constructorButton = By.xpath(".//p[text()='Конструктор']");
    //Кнопка перехода "Булки"
    private final By bunsButton = By.xpath("//span[@class='text text_type_main-default'][text()='Булки']");
    //Кнопка перехода "Соуса"
    private final By saucesButton = By.xpath("//span[@class='text text_type_main-default'][text()='Соусы']");
    //Кнопка перехода "Начинки"
    private final By fillingsButton = By.xpath("//span[@class='text text_type_main-default'][text()='Начинки']");
    private final By activityTopping = By.xpath("//div[starts-with(@class,'tab_tab__1SPyG tab_tab_type_current__2BEPc')]//span");

    //картинка с "Булкой" для проверки видимости раздела
    public By bunsImg = By.xpath(".//img[@alt='Краторная булка N-200i']");
    //текст заголовка "Булки" для проверки видимости раздела
    public By bunsText = By.xpath(".//h2[text()='Булки']");
    //картинка с "Соусом" для проверки видимости раздела
    public By saucesImg = By.xpath(".//p[text()='Соус с шипами Антарианского плоскоходца']");
    //картинка с "Начинкой" для проверки видимости раздела
    public By fillingsImg = By.xpath(".//img[@alt='Плоды Фалленианского дерева']");
    //текст для проверки видимости на главной странице
    public By textBurgerMainPage = By.xpath(".//section/h1[text()='Соберите бургер']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    //Клик по кнопке 'Войти в аккаунт'
    public void clickOnLoginButton() {
        driver.findElement(loginButton).click();
        waitForInvisibilityLoadingAnimation();
    }

    //Клик по кнопке 'Личный Кабинет'
    public void clickOnAccountButton() {
        driver.findElement(accountButton).click();
        waitForInvisibilityLoadingAnimation();
    }

    //Клик по логотипу 'Stellar Burgers'
    public void clickOnLogo() {
        driver.findElement(logo).click();
        waitForInvisibilityLoadingAnimation();
    }

    //Клик по кнопке 'Конструктор'
    public void clickOnConstructorButton() {
        driver.findElement(constructorButton).click();
        waitForInvisibilityLoadingAnimation();
    }


    //Клик по кнопке 'Булки'
    public void clickOnBunsButton() throws InterruptedException {
        Thread.sleep(500);
        driver.findElement(bunsButton).click();

    }

    //Клик по кнопке 'Соуса'
    public void clickOnSaucesButton() throws InterruptedException {
        Thread.sleep(500);
        driver.findElement(saucesButton).click();
    }

    //Клик по кнопке 'Начинки'
    public void clickOnFillingButton() throws InterruptedException {
        Thread.sleep(500);
        driver.findElement(fillingsButton).click();
    }

    public void checkToppingBun() throws InterruptedException {
        Thread.sleep(500);
        String countActivity = driver.findElement(activityTopping).getText();
        assertEquals("Булки", countActivity);
    }
    public void checkToppingSauce() throws InterruptedException {
        Thread.sleep(500);
        String countActivity = driver.findElement(activityTopping).getText();
        assertEquals(countActivity,"Соусы");
    }
    public void checkToppingFillings() throws InterruptedException {
        Thread.sleep(1000);
        String countActivity = driver.findElement(activityTopping).getText();
        assertEquals(countActivity,"Начинки");
    }
    //Выставлено ожидание главная страница, и загрузка текста 'Соберите бургер'
    public void waitForLoadMainPage() {
        new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOfElementLocated(textBurgerMainPage));
    }

    //Выставлено ожидание загрузки текст и картинка с булкой на главной странице
    public void waitForLoadBunsHeader() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(bunsImg));
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(bunsText));
    }

    //Выставлено ожидание загрузки картинка с соусом на главной странице.
    public void waitForLoadSaucesHeader() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(saucesImg));
        waitDocReady();

    }

    //Выставлено ожидание загрузки с начинкой на главной странице.
    public void waitForLoadFillingsHeader() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(fillingsImg));
        waitDocReady();
    }

    //Выставлено ожидание загрузки страницы полностью, анимация исчезнет.
    public void waitForInvisibilityLoadingAnimation() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfElementLocated
                        (By.xpath(".//img[@src='./static/media/loading.89540200.svg' and @alt='loading animation']")));
        waitDocReady();
    }

    //Выставлено ожидание загрузки страницы полностью, дополнительный метод ожидания.
    public void waitDocReady() {
        new WebDriverWait(driver, 20)
                .until((ExpectedCondition<Boolean>) wd ->
                        ((JavascriptExecutor) wd)
                                .executeScript("return document.readyState")
                                .equals("complete"));
    }
}
