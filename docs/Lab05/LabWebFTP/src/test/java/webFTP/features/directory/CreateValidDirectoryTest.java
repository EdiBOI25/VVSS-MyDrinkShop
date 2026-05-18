package webFTP.features.directory;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import webFTP.steps.serenity.*;

@RunWith(SerenityRunner.class)
public class CreateValidDirectoryTest {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    @Steps
    public LoginPageSteps user;

    @Steps
    public AccountPageSteps userLoggedIn;

    @Steps
    public NewDirectoryPageSteps userNewDirectory;

    @Steps
    public DeleteDirectoryPageSteps userDeleteDirectory;

    @Steps
    public LogoutPageSteps userLoggedOut;

    @Test
    public void createNewDirectory() {
        //user.go_to_Login_page();
        webdriver.get("https://vvss:strugure@scs.ubbcluj.ro/vvta/net2ftp/index.php");
        user.click_saveCookies();
        user.login_steps("localhost", "vvta1", "vvta1");//valid data
        userLoggedIn.should_be_in_user_directory("/home/"+"vvta1");
        userLoggedIn.newDirectory();
        userNewDirectory.createDirectory("abc");
        userLoggedIn.should_be_able_to_see_new_directory("abc");
        userLoggedIn.select_directory_to_delete("abc");
        userLoggedIn.delete_selected_directory();
        userDeleteDirectory.delete_directory("abc");
        userLoggedIn.should_not_be_able_to_see_new_directory("abc");
        userLoggedIn.logout();
        userLoggedOut.should_see_logout_message("You have logged out from the FTP server.");
    }


} 