package education.common;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Selenium
{
    public static void main(String[] args)
    {
        new Selenium().go();
    }

    private void go()
    {
        try (final BrowserWebDriverContainer<?> firefox = new BrowserWebDriverContainer<>()
                .withCapabilities(new FirefoxOptions())
                .withSharedMemorySize(2147483648L)
        )
        {
            List<String> portBindings = new ArrayList<>();
            portBindings.add("4444:4444"); // hostPort:containerPort
            portBindings.add("15900:5900"); // hostPort:containerPort
            portBindings.add("7900:7900"); // hostPort:containerPort
            firefox.setPortBindings(portBindings);
            firefox.start();

            WebDriver driver = firefox.getWebDriver();

            File file = capturePlate(driver, "PDT74");
            openFile(file);

            System.out.println("Enter license plate.");
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
            {
                openFile(capturePlate(driver, reader.readLine()));
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Finished");
    }

    private File capturePlate(WebDriver driver, String plate)
    {
        driver.get("https://www.carjam.co.nz/car/?plate=" + plate);

        final Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(d ->
        {
            final WebElement panelTitle = driver.findElement(By.className("panel-title"));
            final WebElement disclaimer = panelTitle.findElement(By.xpath("//*[contains(text(), 'Disclaimer')]"));
            return disclaimer.isDisplayed();
        });
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
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

}
