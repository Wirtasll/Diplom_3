package pageobject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage {

    private final WebDriver driver;

    //Заголовок "Вход"
    public final By entrance = By.xpath(".//main/div/h2[text()='Вход']");
    //Поле "Email"
    private final By emailField = By.xpath(".//div[@class='input pr-6 pl-6 input_type_text input_size_default']/input[@name='name']");
    //Поле "Пароль"
    private final By passwordField = By.xpath(".//div[@class='input pr-6 pl-6 input_type_password input_size_default']/input[@name='Пароль']");
    //Логотип "Stellar Burgers"
    public final By enterButton = By.xpath(".//div/a[@href='/']");
    //Кнопка "Войти"
    private final By loginButton = By.xpath(".//button[text()='Войти']");
    //Кнопка "Конструктор"
    private final By constructorButton = By.xpath(".//a/p[text()='Конструктор']");
    //Ссылка "Зарегистрироваться"
    private final By registerLink = By.xpath(".//a[@href='/register' and text()='Зарегистрироваться']");
    //Ссылка "Восстановить пароль"
    private final By forgotPasswordLink = By.xpath(".//a[@href='/forgot-password' and text()='Восстановить пароль']");


    public LoginPage(WebDriver driver) {
            this.driver = driver;
    }


    @Step("Ввод Email")
    public void setEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    @Step("Ввод пароля")
    public void setPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    @Step("Клик по кнопке 'Войти'")
    public void clickOnLoginButton() {
        driver.findElement(loginButton).click();
        waitForInvisibilityLoadingAnimation();
    }

    @Step("Клик по ссылке 'Зарегистрироваться'")
    public void clickOnRegister() {
        driver.findElement(registerLink).click();
        waitForInvisibilityLoadingAnimation();
    }

    @Step("Клик по ссылке 'Восстановить пароль'")
    public void clickOnForgotPasswordLink() {
        driver.findElement(forgotPasswordLink).click();
        waitForInvisibilityLoadingAnimation();
    }

    @Step("Клик по кнопке 'Конструктор'")
    public void clickOnConstructorButton() {
        driver.findElement(constructorButton).click();
        waitForInvisibilityLoadingAnimation();
    }

    @Step("Клик по кнопке 'Stellar Burgers'")
    public void clickOnLogo() {
        driver.findElement(enterButton).click();
        waitForInvisibilityLoadingAnimation();
    }

    @Step("Авторизация пользователя")
    public void authorization(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickOnLoginButton();
    }

    @Step("Выставлено ожидание загрузки страницы с текстом 'Вход'")
    public void waitForLoadEntrance() {
           new WebDriverWait(driver, 5)
                   .until(ExpectedConditions.visibilityOfElementLocated(entrance));
    }

    @Step("Выставлено ожидание загрузки страницы полностью, анимация исчезнет.")
    public void waitForInvisibilityLoadingAnimation() {
        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.invisibilityOfElementLocated
                        (By.xpath(".//img[@src='./static/media/loading.89540200.svg' and @alt='loading animation']")));
    }
}

