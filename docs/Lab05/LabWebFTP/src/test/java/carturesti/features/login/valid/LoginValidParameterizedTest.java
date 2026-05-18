package carturesti.features.login.valid;

import carturesti.steps.serenity.CarturestiUserSteps;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("src/test/resources/carturesti_valid_login.csv")
public class LoginValidParameterizedTest {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    @Steps
    public CarturestiUserSteps carturestiUser;

    String email;
    String password;

    @Test
    public void login_valid_with_csv_data() {
        carturestiUser.opens_home_page();
        carturestiUser.logs_in_with_valid_credentials(email, password);
        carturestiUser.should_be_logged_in();
        carturestiUser.logs_out();
        carturestiUser.should_be_logged_out();
    }
}
