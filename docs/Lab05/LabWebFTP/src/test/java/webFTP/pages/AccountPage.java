package webFTP.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;


public class AccountPage extends PageObject {

    //*[@id="list_17_dirfilename"]

    //@FindBy(name="directory")
    //@FindBy(xpath="//*[@id=\"toptable\"]/tbody/tr/td[2]/input")
    //@FindBy(xpath="/html/body/div/div[2]/div/form/input[23]")
    @FindBy(css="#BrowseForm > input[type=text]:nth-child(24)")
    private WebElementFacade currentDirectory;


    @FindBy(xpath="//*[@id=\"StatusbarForm\"]/a[4]/img")
    private WebElementFacade logoutButton;
    //#StatusbarForm > a:nth-child(29) > img
    //*[@id="StatusbarForm"]/a[4]/img

    //@FindBy(xpath = "//*[@id=\"smallbutton\"]")
    //@FindBy(id = "smallbutton")
    //@FindBy(xpath="//*[@id=\"maintable\"]/tbody/tr[1]/td/table/tbody/tr/td[1]")
    @FindBy(css="#buttontable > tbody")
    private WebElementFacade buttonList;

    @FindBy(id = "maintable")
    private WebElementFacade listOfDirectories;

//    @FindBy(xpath="//*[@id=\"maintable\"]/tbody/tr[1]/td/table/tbody/tr/td[2]")
//    private WebElementFacade rightButtonList;

    public String getCurrentDirectoryName(){
        System.out.println(currentDirectory.getValue());
        return currentDirectory.getText();
        //click_element_by_attribute_value(currentDirectory, "accesskey", "g");
        //List<WebElement> list = currentDirectory.findElements(By.tagName("input"));
        //return list.toString();//currentDirectory.getValue();
    }

    public void click_Logout(){
        logoutButton.click();
    }

    public void click_new_directory() {
        System.out.println("aici");
        click_element_by_attribute_value(buttonList, "value", "New dir");
        //click_element_by_attribute_value(leftButtonList, "accesskey", "w");

    }

    public void check_directory_to_delete(String directory) {
        click_element_by_attribute_value(listOfDirectories, "value", directory);
    }

    public List<String> getContent() {
        WebElementFacade definitionList = find(By.tagName("body"));
        return definitionList.findElements(By.tagName("a")).stream()
                .map( element -> element.getText() )
                .collect(Collectors.toList());
    }


    public List<String> getContent2() {
        WebElementFacade definitionList = find(By.tagName("td"));
        return definitionList.findElements(By.tagName("a")).stream()
                .map( element -> element.getText() )
                .collect(Collectors.toList());
    }

    private void click_element_by_attribute_value(WebElementFacade el, String attribute, String value) {
        //System.out.println(el);
        List<WebElement> list = el.findElements(By.tagName("input"));
        //list.forEach(System.out::println);
        for (int i = 0; i<list.size(); i++){
            String dir = list.get(i).getAttribute(attribute);
            if (dir.equalsIgnoreCase(value)) {
                list.get(i).click();
                break;
            }
        }
    }

    public void deleteDirectory() {

        click_element_by_attribute_value(buttonList, "value", "Delete");
    }

}