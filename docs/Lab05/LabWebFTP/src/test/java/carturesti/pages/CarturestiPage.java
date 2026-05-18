package carturesti.pages;

import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@DefaultUrl("https://carturesti.ro/")
public class CarturestiPage extends PageObject {

    private static final String BASE_URL = "https://carturesti.ro";

    private static final By LOGIN_BUTTONS = By.cssSelector("button[data-toggle='modal'][data-target='#modalLogin']");
    private static final By LOGIN_MODAL = By.cssSelector("#modalLogin");
    private static final By LOGIN_TRIGGER = By.cssSelector("#loginTrigger");
    private static final By LOGIN_FORM = By.cssSelector("#modalLoginForm.visible");
    private static final By LOGIN_EMAIL = By.cssSelector("#modalLoginForm.visible #loginform-email");
    private static final By LOGIN_PASSWORD = By.cssSelector("#modalLoginForm.visible #loginform-password");
    private static final By LOGIN_SUBMIT = By.cssSelector("#modalLoginForm.visible button[name='login-button']");
    private static final By SEARCH_INPUT = By.cssSelector("#search-input");
    private static final By PRODUCT_TITLE = By.cssSelector(".titluProdus");
    private static final By ADD_TO_CART = By.cssSelector("#cartu-add-to-cart-btn-x");
    private static final By CART_BUTTON = By.cssSelector(".checkout__button");
    private static final By CART_PRODUCT_COUNT = By.cssSelector(".product-count");
    private static final By CART_PRODUCT = By.cssSelector(".checkout__summary .cartProductName a");
    private static final By REMOVE_FROM_CART = By.cssSelector(".checkout__summary .removeFromCart");
    private static final By EMPTY_CART = By.cssSelector(".checkout__empty, .checkout__empty *");
    private static final By COOKIE_BUTTONS = By.cssSelector(".cc-window .cc-btn, .cc-window button, .cc-window a, .cc-btn.cc-allow, .cc-btn.cc-dismiss, .cc-allow, .cc-dismiss");
    private static final By LOGOUT_CONTROLS = By.cssSelector("a[href*='/site/logout'], button[href*='/site/logout'], [data-method='post'][href*='/site/logout']");

    public void openHomePage() {
        open();
        waitForPageReady();
        dismissCookieBannerIfPresent();
    }

    public void openLoginPopup() {
        clickVisible(LOGIN_BUTTONS);
        waitUntilVisible(LOGIN_MODAL);
        clickIfVisible(LOGIN_TRIGGER);
        waitUntilVisible(LOGIN_FORM);
        waitUntilVisible(LOGIN_EMAIL);
    }

    public void loginThroughPopup(String email, String password) {
        openLoginPopup();
        typeInto(LOGIN_EMAIL, email);
        typeInto(LOGIN_PASSWORD, password);
        clickVisible(LOGIN_SUBMIT);
        waitForPageReady();
    }

    public void shouldBeLoggedIn() {
        waitForCondition(15).until(driver -> isLoggedIn());
        assertThat("User should be logged in on carturesti.ro", isLoggedIn(), is(true));
    }

    public void shouldShowInvalidLoginMessage(String expectedMessage) {
        waitForCondition(15).until(driver -> normalizedPageSource().contains(normalizeText(expectedMessage)));
        assertThat(normalizedPageSource(), containsString(normalizeText(expectedMessage)));
        assertThat("Invalid login should not authenticate the user", isLoggedIn(), is(false));
    }

    public void searchBookFromHeader(String bookTitle, String expectedUrlPart) {
        By bookSearchResult = By.cssSelector("a[href*='" + expectedUrlPart + "']");
        WebElementFacade searchInput = waitUntilVisible(SEARCH_INPUT);
        searchInput.clear();
        searchInput.type(bookTitle);

        try {
            waitForCondition(10).until(ExpectedConditions.elementToBeClickable(bookSearchResult));
            clickVisible(bookSearchResult);
        } catch (TimeoutException ignored) {
            searchInput.sendKeys(Keys.ENTER);
            waitForCondition(20).until(ExpectedConditions.elementToBeClickable(bookSearchResult));
            clickVisible(bookSearchResult);
        }

        waitForPageReady();
    }

    public void shouldBeOnBookPage(String expectedTitle, String expectedUrlPart) {
        WebElementFacade title = waitUntilVisible(PRODUCT_TITLE);
        assertTextContains(title.getText(), expectedTitle);
        assertThat(getDriver().getCurrentUrl(), containsString(expectedUrlPart));
    }

    public void addBookToCart(String expectedTitle) {
        clickVisible(ADD_TO_CART);
        waitForCondition(20).until(driver -> hasVisibleText(CART_PRODUCT, expectedTitle) || hasNonZeroCartCount());
    }

    public void shouldSeeBookInCart(String expectedTitle) {
        openCart();
        WebElementFacade cartProduct = waitUntilVisible(CART_PRODUCT);
        assertTextContains(cartProduct.getText(), expectedTitle);
    }

    public void removeBookFromCart() {
        openCart();
        WebElement removeControl = waitForCondition(10).until(driver -> {
            List<WebElement> controls = driver.findElements(REMOVE_FROM_CART);
            return controls.isEmpty() ? null : controls.get(0);
        });
        try {
            removeControl.click();
        } catch (RuntimeException clickFailed) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", removeControl);
        }
    }

    public void shouldSeeEmptyCart() {
        openCart();
        waitForCondition(15).until(driver -> {
            return !hasVisibleElement(CART_PRODUCT) && hasVisibleText(EMPTY_CART, "cosul tau este gol");
        });
        assertThat(hasVisibleText(EMPTY_CART, "cosul tau este gol"), is(true));
    }

    public void logout() {
        if (!clickFirstDisplayed(LOGOUT_CONTROLS)) {
            getDriver().get(BASE_URL + "/site/logout");
        }
        waitForPageReady();
    }

    public void shouldBeLoggedOut() {
        waitForCondition(15).until(driver -> isLoggedOut());
        assertThat("User should be logged out on carturesti.ro",
                isLoggedOut(), is(true));
    }

    private void openCart() {
        if (!isCartOpen()) {
            clickVisible(CART_BUTTON);
            try {
                waitForCondition(3).until(driver -> isCartOpen());
            } catch (TimeoutException cartDidNotOpenAfterFirstClick) {
                clickFirstAvailableWithJavaScript(CART_BUTTON);
                waitForCondition(10).until(driver -> isCartOpen());
            }
        }
    }

    private WebElementFacade waitUntilVisible(By locator) {
        WebElementFacade element = find(locator);
        element.waitUntilVisible();
        return element;
    }

    private void typeInto(By locator, String value) {
        WebElementFacade element = waitUntilVisible(locator);
        element.clear();
        element.type(value);
    }

    private void clickVisible(By locator) {
        if (!clickFirstDisplayed(locator)) {
            WebElement element = waitForCondition(15).until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
        }
    }

    private boolean clickFirstDisplayed(By locator) {
        List<WebElement> elements = getDriver().findElements(locator);
        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                try {
                    element.click();
                } catch (RuntimeException clickFailed) {
                    ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
                }
                return true;
            }
        }
        return false;
    }

    private void clickIfVisible(By locator) {
        clickFirstDisplayed(locator);
    }

    private void clickFirstAvailableWithJavaScript(By locator) {
        List<WebElement> elements = getDriver().findElements(locator);
        if (!elements.isEmpty()) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", elements.get(0));
        }
    }

    private boolean hasVisibleElement(By locator) {
        return getDriver().findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }

    private boolean hasVisibleText(By locator, String text) {
        return getDriver().findElements(locator).stream()
                .anyMatch(element -> element.isDisplayed() && normalizeText(element.getText()).contains(normalizeText(text)));
    }

    private boolean hasNonZeroCartCount() {
        return getDriver().findElements(CART_PRODUCT_COUNT).stream()
                .anyMatch(element -> element.isDisplayed() && element.getText().matches(".*[1-9].*"));
    }

    private boolean isLoggedIn() {
        return hasElement(LOGOUT_CONTROLS) && !hasVisibleElement(LOGIN_BUTTONS);
    }

    private boolean isLoggedOut() {
        return !hasElement(LOGOUT_CONTROLS) && hasVisibleElement(LOGIN_BUTTONS);
    }

    private boolean isCartOpen() {
        List<WebElement> checkouts = getDriver().findElements(By.cssSelector(".checkout"));
        boolean activeCartPanel = checkouts.stream()
                .anyMatch(element -> element.getAttribute("class") != null && element.getAttribute("class").contains("checkout--active"));
        return activeCartPanel || hasVisibleElement(CART_PRODUCT) || hasVisibleText(EMPTY_CART, "cosul tau este gol");
    }

    private void dismissCookieBannerIfPresent() {
        List<WebElement> cookieButtons = getDriver().findElements(COOKIE_BUTTONS);
        for (WebElement button : cookieButtons) {
            String text = normalizeText(button.getText());
            String className = button.getAttribute("class") == null ? "" : button.getAttribute("class");
            if (button.isDisplayed()
                    && (text.contains("permite toate") || text.contains("permite doar esentiale")
                    || className.contains("cc-allow") || className.contains("cc-dismiss"))) {
                try {
                    button.click();
                } catch (RuntimeException clickFailed) {
                    ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", button);
                }
                return;
            }
        }
    }

    private boolean hasElement(By locator) {
        return !getDriver().findElements(locator).isEmpty();
    }

    private void assertTextContains(String actualText, String expectedText) {
        assertThat(normalizeText(actualText), containsString(normalizeText(expectedText)));
    }

    private String normalizedPageSource() {
        return normalizeText(getDriver().getPageSource());
    }

    private String normalizeText(String text) {
        if (text == null) {
            return "";
        }
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ")
                .trim();
    }

    private WebDriverWait waitForCondition(int seconds) {
        return new WebDriverWait(getDriver(), seconds);
    }

    private void waitForPageReady() {
        WebDriver driver = getDriver();
        waitForCondition(20).until(currentDriver ->
                "complete".equals(((JavascriptExecutor) driver).executeScript("return document.readyState")));
    }
}
