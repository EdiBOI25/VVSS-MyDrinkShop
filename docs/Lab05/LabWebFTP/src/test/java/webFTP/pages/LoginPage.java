package webFTP.pages;

import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import net.serenitybdd.core.pages.WebElementFacade;
import java.util.stream.Collectors;

import net.serenitybdd.core.annotations.findby.FindBy;

import net.thucydides.core.pages.PageObject;

import java.util.List;

@DefaultUrl("https://www.scs.ubbcluj.ro/vvta/net2ftp/index.php")
public class LoginPage extends PageObject {

    @FindBy(name="ftpserver")
    private WebElementFacade ftpServer;

    @FindBy(name="username")
    private WebElementFacade username;

    @FindBy(name="password")
    private WebElementFacade password;

    @FindBy(name="Login")
    private WebElementFacade loginButton;

    @FindBy(id="LoginButton1")
    private WebElementFacade saveCookies;


    public void select_server(String serverName) {
        ftpServer.clear();
        ftpServer.type(serverName);
        //ftpServer.waitUntilClickable();
        //ftpServer.selectByVisibleText(serverName); //diferenta dintre metode???
    }

    public void enter_username(String userName) {

        username.type(userName);
    }

    public void enter_password(String password) {

        this.password.type(password);
    }

    public void click_Login() {

        loginButton.click();
    }

    public void saveCookies() {
        //saveCookies.waitUntilVisible();
        saveCookies.click();
    }

}
