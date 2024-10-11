package page.object;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfilePage {

    private final WebDriver driver;

    // Кнопка "Конструктор"
    private final By constructorButton = By.xpath(".//a[@href='/']/p[text()='Конструктор']");
    // Кнопка "Выход"
    private final By exitButton = By.xpath(".//li/button[text()='Выход']");
    // Надпись для перехода в Личный кабинет
    public final By textOnProfilePage = By.xpath(".//nav/p[text()='В этом разделе вы можете изменить свои персональные данные']");


    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }


    @Step("Клик по кнопке 'Конструктор'")
    public void clickOnConstructorButton() {
        driver.findElement(constructorButton).click();
        waitForInvisibilityLoadingAnimation();
    }

    @Step("Клик по кнопке 'Выйти'")
    public void clickOnExitButton() {
        driver.findElement(exitButton).click();
        waitForInvisibilityLoadingAnimation();
    }

    @Step("Выставлено ожидание загрузки страницы личного кабинета с текстом изменения персональных данных")
    public void waitForLoadProfilePage() {
        // подожди 3 секунды, чтобы элемент с нужным текстом стал видимым
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.visibilityOfElementLocated(textOnProfilePage));
    }

    @Step("Выставлено ожидание загрузки страницы полностью, анимация исчезнет")
    public void waitForInvisibilityLoadingAnimation() {
        new WebDriverWait(driver, 6)
                .until(ExpectedConditions.invisibilityOfElementLocated
                        (By.xpath(".//img[@src='./static/media/loading.89540200.svg' and @alt='loading animation']")));
    }
}
