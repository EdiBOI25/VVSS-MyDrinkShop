package webFTP.steps.serenity;
 
import net.thucydides.core.annotations.StepGroup;
import webFTP.pages.LoginPage;
import net.thucydides.core.annotations.Step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;

public class LoginPageSteps {

    LoginPage loginPage;

    @Step
    public void select_server_name(String server) {

        loginPage.select_server(server);
    }

    @Step
    public void enter_username(String username) {

        loginPage.enter_username(username);
    }

    @Step
    public void enter_password(String password) {

        loginPage.enter_password(password);
    }

    @Step
    public void click_Login() {

        loginPage.click_Login();
    }

    @Step
    public void go_to_Login_page() {

        loginPage.open();
    }

    @Step
    public void click_saveCookies() {

        loginPage.saveCookies();
    }

    @Step
    public void login_steps(String server, String username, String password) {
        select_server_name(server);
        enter_username(username);
        enter_password(password);
        click_Login();
    }

    @Step
    public void should_to_see_alert(String alert, String message) {
        assert message.equals(alert);
    }
}
