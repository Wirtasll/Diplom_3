import org.aeonbits.owner.Config;

public interface WebDriverConfig extends Config {
    @Key("chromeDriverPath")
    String chromeDriverPath();

    @Key("yandexDriverPath")
    String yandexDriverPath();
}