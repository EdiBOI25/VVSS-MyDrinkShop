package carturesti.features.scenario;

import carturesti.steps.serenity.CarturestiUserSteps;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("src/test/resources/carturesti_scenario_data.csv")
public class LoginSearchCartLogoutScenarioTest {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    @Steps
    public CarturestiUserSteps carturestiUser;

    String email;
    String password;
    String bookTitle;
    String bookUrlPart;

    @Test
    public void valid_user_searches_book_adds_it_to_cart_removes_it_and_logs_out() {
        carturestiUser.opens_home_page();
        carturestiUser.logs_in_with_valid_credentials(email, password);
        carturestiUser.should_be_logged_in();

        carturestiUser.searches_for_book(bookTitle, bookUrlPart);
        carturestiUser.should_be_on_book_page(bookTitle, bookUrlPart);

        carturestiUser.adds_book_to_cart(bookTitle);
        carturestiUser.should_see_book_in_cart(bookTitle);

        carturestiUser.removes_book_from_cart();
        carturestiUser.should_see_empty_cart();

        carturestiUser.logs_out();
        carturestiUser.should_be_logged_out();
    }
}
