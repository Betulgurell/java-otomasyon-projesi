package hepsiburadaOtomasyon;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URL;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Locale;

public class AppTest {

    AndroidDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setUp() throws MalformedURLException {
        Locale.setDefault(Locale.ENGLISH); // Türkçe locale hatasını önle

        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("emulator-5554");
        options.setAppPackage("com.pozitron.hepsiburada");
        options.setAppActivity("com.hepsiburada.ui.home.SuperAppActivity");
        options.setNoReset(false);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void searchTest() throws InterruptedException {
        Thread.sleep(3000); // popup'ın tam açılmasını bekle

        // Popup'ı aşağı kaydırarak kapat
        swipeDown();
        Thread.sleep(2000);

        WebElement searchBox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@text='Ürün, kategori veya marka ara']")
                )
        );
        searchBox.click();

        WebElement searchInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//android.widget.EditText[@resource-id='searchTextField']")
                )
        );
        searchInput.sendKeys("laptop");
        //ARAMAYI TETİKLE
        driver.pressKey(new KeyEvent(AndroidKey.ENTER));

        Thread.sleep(5000);// sonuçları görebilmekn için bekle
    }

    private void swipeDown() {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        // Ekranın üst kısmından, aşağıya doğru kaydır
        swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), 500, 400));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), 500, 1800));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}