package webFTP.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;
 
public class DeleteDirectoryPage extends PageObject {

    @FindBy(id="CopyDeleteMoveForm")
    private WebElementFacade content;

    @FindBy(xpath="//*[@id=\"CopyMoveDeleteForm\"]/a[2]/img")
    private WebElementFacade deleteButton;

    @FindBy(xpath="//*[@id=\"CopyMoveDeleteForm\"]/a[1]/img")
    private WebElementFacade backButtonBtn;

    public void click_delete_directory() {
        deleteButton.click();
    }

    public void back() {
        backButtonBtn.click();
    }

    public List<String> getContent() {
        WebElementFacade definitionList = find(By.tagName("div"));
        return definitionList.findElements(By.tagName("form")).stream()
                .map( element -> element.getText() )
                .collect(Collectors.toList());
    }

}