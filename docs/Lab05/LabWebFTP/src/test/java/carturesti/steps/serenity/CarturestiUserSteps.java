package carturesti.steps.serenity;

import carturesti.pages.CarturestiPage;
import net.thucydides.core.annotations.Step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CarturestiUserSteps {

    CarturestiPage carturestiPage;

    @Step
    public void opens_home_page() {
        carturestiPage.openHomePage();
    }

    @Step
    public void logs_in_with_valid_credentials(String email, String password) {
        carturestiPage.loginThroughPopup(resolveValue(email), resolveValue(password));
    }

    @Step
    public void logs_in_with_invalid_credentials(String email, String password) {
        carturestiPage.loginThroughPopup(email, password);
    }

    @Step
    public void should_be_logged_in() {
        carturestiPage.shouldBeLoggedIn();
    }

    @Step
    public void should_see_invalid_login_message(String expectedMessage) {
        carturestiPage.shouldShowInvalidLoginMessage(expectedMessage);
    }

    @Step
    public void searches_for_book(String bookTitle, String bookUrlPart) {
        carturestiPage.searchBookFromHeader(bookTitle, bookUrlPart);
    }

    @Step
    public void should_be_on_book_page(String bookTitle, String bookUrlPart) {
        carturestiPage.shouldBeOnBookPage(bookTitle, bookUrlPart);
    }

    @Step
    public void adds_book_to_cart(String bookTitle) {
        carturestiPage.addBookToCart(bookTitle);
    }

    @Step
    public void should_see_book_in_cart(String bookTitle) {
        carturestiPage.shouldSeeBookInCart(bookTitle);
    }

    @Step
    public void removes_book_from_cart() {
        carturestiPage.removeBookFromCart();
    }

    @Step
    public void should_see_empty_cart() {
        carturestiPage.shouldSeeEmptyCart();
    }

    @Step
    public void logs_out() {
        carturestiPage.logout();
    }

    @Step
    public void should_be_logged_out() {
        carturestiPage.shouldBeLoggedOut();
    }

    private String resolveValue(String rawValue) {
        if (rawValue == null) {
            return "";
        }
        if (rawValue.startsWith("env:")) {
            String environmentVariable = rawValue.substring("env:".length());
            String resolvedValue = System.getenv(environmentVariable);
            assertThat("Set environment variable " + environmentVariable + " before running valid login tests",
                    resolvedValue != null && !resolvedValue.trim().isEmpty(), is(true));
            return resolvedValue;
        }
        if (rawValue.startsWith("property:")) {
            String propertyName = rawValue.substring("property:".length());
            String resolvedValue = System.getProperty(propertyName);
            assertThat("Set system property " + propertyName + " before running valid login tests",
                    resolvedValue != null && !resolvedValue.trim().isEmpty(), is(true));
            return resolvedValue;
        }
        return rawValue;
    }
}
