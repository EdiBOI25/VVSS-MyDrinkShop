package webFTP.features.login.invalid;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Issue;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import webFTP.steps.serenity.AccountPageSteps;
import webFTP.steps.serenity.LoginPageSteps;
import webFTP.steps.serenity.LogoutPageSteps;

@RunWith(SerenityRunner.class)
public class LoginInvalidServerTest {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    @Steps
    public LoginPageSteps loginPage;

    @Steps
    public LogoutPageSteps logoutPage;


    @Test
    public void login() {
        //loginPage.go_to_Login_page();
        webdriver.get("https://vvss:strugure@scs.ubbcluj.ro/vvta/net2ftp/index.php");
        loginPage.click_saveCookies();
        loginPage.login_steps("nessie.cs.ubbcluj.ro","vvta1", "vvta1");
        logoutPage.should_see_logout_message("Unable to connect to FTP server "+"nessie.cs.ubbcluj.ro");
    }

} 