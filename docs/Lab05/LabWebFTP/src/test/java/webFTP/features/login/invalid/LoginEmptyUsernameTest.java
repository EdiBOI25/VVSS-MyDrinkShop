package webFTP.features.login.invalid;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Issue;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import webFTP.steps.serenity.AccountPageSteps;
import webFTP.steps.serenity.LoginPageSteps;
import webFTP.steps.serenity.LogoutPageSteps;

import java.util.Iterator;
import java.util.Set;

@RunWith(SerenityRunner.class)
public class LoginEmptyUsernameTest {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    @Steps
    public LoginPageSteps loginPage;

    @Steps
    public AccountPageSteps accountPage;

    @Steps
    public LogoutPageSteps logoutPage;



    @Test
    public void login() {
        //loginPage.go_to_Login_page();
        webdriver.get("https://vvss:strugure@scs.ubbcluj.ro/vvta/net2ftp/index.php");
        loginPage.click_saveCookies();
        loginPage.login_steps("localhost","", "vvta1");

        //webdriver.switchTo().alert().accept();???
        loginPage.login_steps("localhost","vvta1", "vvta1");
        accountPage.should_be_in_user_directory("/home/"+"vvta");
        accountPage.logout();
        logoutPage.should_see_logout_message("You have logged out from the FTP server.");

    }


} 