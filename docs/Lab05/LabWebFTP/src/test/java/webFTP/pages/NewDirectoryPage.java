package webFTP.pages;
 
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

public class NewDirectoryPage extends PageObject {

    //@FindBy(xpath="//*[@id=\"NewDirForm\"]/input[16]")
    @FindBy(name="newNames[1]")
    private WebElementFacade textDirectory;

    @FindBy(xpath="//*[@id=\"NewDirForm\"]/a[2]/img")
    private WebElementFacade createButton;

    @FindBy(xpath="//*[@id=\"NewDirForm\"]/a[1]/img")
    private WebElementFacade backButtonBtn;

    public void enter_directory(String directory) {
        textDirectory.type(directory);
    }

    public void click_to_create_Directory() {
        createButton.click();
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
