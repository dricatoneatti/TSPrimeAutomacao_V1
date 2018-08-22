package Bases;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverPage {

    public WebDriver driver;
    public WebDriverWait wait;

    public void getChrome(String url){
        System.setProperty("webdriver.chrome.driver", "src\\chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver,20);
        getUrl(url); // Navegação para a url solicitada
    }
    // Método para inserir url
    public void getUrl(String url){
        driver.get(url);
    }
}

