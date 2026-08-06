package hepsiburadaOtomasyon;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;
import java.util.Locale;

public class LoginTest {
    AndroidDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setUP() throws MalformedURLException {
        Locale.setDefault(Locale.ENGLISH);

        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("emulator-5554");
        options.setAppPackage("com.pozitron.hepsiburada");
        options.setAppActivity("com.hepsiburada.ui.home.SuperAppActivity");
        options.setNoReset(false);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void loginTest() throws InterruptedException {

        // 1. İLK POP-UP (Ana sayfa açılışındaki pop-up'ı aşağı kaydırıp kapat)
        Thread.sleep(4000);
        swipeDown();
        Thread.sleep(2000);

        // 2. HESABIM SEKMESİ (Alt menüden Hesabım'a tıkla)
        WebElement hesabim = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.view.View[@content-desc='Hesabım']")
                )
        );
        hesabim.click();

        // 3. İKİNCİ POP-UP (Hesabım ekranında çıkan 2. pop-up'ı da aşağı kaydırıp kapat)
        Thread.sleep(3000);
        swipeDown();
        Thread.sleep(2000);

        // 4. ÜYE OL (Pop-up kapandıktan sonra temiz ekranda Üye Ol butonuna tıkla)
        WebElement uyeOlBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.widget.TextView[@text='Üye ol']")
                )
        );
        uyeOlBtn.click();
        System.out.println("2. Pop-up da kapatıldı ve Üye Ol butonuna tıklandı!");


        Thread.sleep(5000);
    }

    // Ekranı güvenli şekilde aşağı kaydırma metodu
    private void swipeDown() {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.3);
        int endY = (int) (size.height * 0.7);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
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