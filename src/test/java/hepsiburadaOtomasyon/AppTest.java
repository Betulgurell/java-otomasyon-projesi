package hepsiburadaOtomasyon;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
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

public class AppTest {

    AndroidDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setUp() throws MalformedURLException {
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
    public void searchTest() throws InterruptedException {
        // 1. POPUP KAPATMA
        Thread.sleep(3000);
        swipeDown();
        Thread.sleep(2000);

        // ARAMA KUTUSUNA TIKLA
        WebElement searchBox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@text='Ürün, kategori veya marka ara']")
                )
        );
        searchBox.click();

        // 3. LAPTOP YAZ VE ENTER a bas
        WebElement searchInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//android.widget.EditText[@resource-id='searchTextField'] | //android.widget.EditText")
                )
        );
        searchInput.sendKeys("laptop");
        driver.pressKey(new KeyEvent(AndroidKey.ENTER));

        Thread.sleep(4000);

        // 4. ilk ürüne tıkla
        WebElement firstProductPrice = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("(//android.widget.TextView[contains(@text, 'TL')])[1]")
                )
        );
        firstProductPrice.click();
        System.out.println("İlk ürünün fiyatına basıldı ve ürün detay sayfasına geçildi!");

        // sepete ekle butonuna tıkla
        Thread.sleep(3000); // Ürün sayfasının oturmasını bekle
        try {
            WebElement addToCartBtn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[contains(@text, 'Sepete Ekle') or contains(@content-desc, 'Sepete Ekle')]")
                    )
            );
            addToCartBtn.click();
            System.out.println("Sepete Ekle butonuna tıklandı!");
        } catch (Exception e) {
            // Buton katmanı tıklamayı engellerse ekranın alt sağındaki butona doğrudan bas
            Dimension size = driver.manage().window().getSize();
            int tapX = (int) (size.width * 0.75);
            int tapY = (int) (size.height * 0.94);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), tapX, tapY));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(tap));
            System.out.println("Koordinat ile alt bar Sepete Ekle alanına tıklandı!");
        }

        Thread.sleep(2000);

        // sepete git
        try {
            WebElement goToCartPopup = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//*[@text='Sepete git' or contains(@text, 'Sepete Git')]")
                    )
            );
            goToCartPopup.click();
        } catch (Exception e) {
            WebElement sepetimTab = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//*[@content-desc='Sepetim' or @text='Sepetim']")
                    )
            );
            sepetimTab.click();
        }
        System.out.println("Sepetim sayfasına geçildi!");

        // sepetin dolu olduğunu doğrula
        WebElement checkoutBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@text='Alışverişi tamamla' or contains(@text, 'Tamamla') or contains(@text, 'Ödenecek Tutar')]")
                )
        );
        System.out.println("Tebrikler! Ürün başarıyla sepete eklendi ve sepet doğrulandı.");

        Thread.sleep(5000);
    }

    private void swipeDown() {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

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