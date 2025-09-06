package education.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeleniumTest
{
    @Test
    void shouldOpenSelenium()
    {
        try (final BrowserWebDriverContainer<?> firefox = new BrowserWebDriverContainer<>()
                .withCapabilities(new FirefoxOptions())
                .withSharedMemorySize(2147483648L)
        )
        {
            List <String> portBindings = new ArrayList<>();
            portBindings.add("4444:4444"); // hostPort:containerPort
            portBindings.add("15900:5900"); // hostPort:containerPort
            portBindings.add("7900:7900"); // hostPort:containerPort
            firefox.setPortBindings(portBindings);

            firefox.start();

            WebDriver driver = firefox.getWebDriver();

            String[] plates = new String[] {
                "PDT74",
                "QHR279"
            };

            Arrays.stream(plates).forEach(plate ->
            {
                File file = capturePlate(driver, plate);
//                openFile(file);
            });
        }
        System.out.println("Finished");
    }

    private static void openFile(File file)
    {
        try
        {
            String[] params = {"xdg-open", file.getAbsolutePath()};
            Process p = null;
            p = Runtime.getRuntime().exec(params);
            System.out.println(p.pid());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private File capturePlate(WebDriver driver, String plate)
    {
        driver.get("https://www.carjam.co.nz/car/?plate=" + plate);

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(d -> {
            WebElement panelTitle = driver.findElement(By.className("panel-title"));
            WebElement disclaimer = panelTitle.findElement(By.xpath("//*[contains(text(), 'Disclaimer')]"));
            return disclaimer.isDisplayed();
        });

        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    }
}
