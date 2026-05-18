package webFTP.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;


public class LogoutPage extends PageObject {

    @FindBy(xpath="//*[@id=\"posts\"]/div/p[1]/text()[1]")
    private WebElementFacade logoutMessage;

    @FindBy(xpath = "/html/body/div/div[2]/div/div[2]/div/h1/text()")
    private WebElementFacade errorMessage;

    public List<String> getDefinitions() {
        WebElementFacade definitionList = find(By.tagName("div"));
        return definitionList.findElements(By.tagName("p")).stream()
                .map( element -> element.getText() )
                .collect(Collectors.toList());
    }

}