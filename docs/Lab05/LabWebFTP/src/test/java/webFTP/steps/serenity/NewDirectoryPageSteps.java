package webFTP.steps.serenity;

 
import net.thucydides.core.annotations.Step;
import webFTP.pages.NewDirectoryPage;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

public class NewDirectoryPageSteps {

    NewDirectoryPage newDirectoryPage;

    @Step
    public void enter_directory(String directory) {
        newDirectoryPage.enter_directory(directory);
    }

    @Step
    public void createDirectory() {
        newDirectoryPage.click_to_create_Directory();
    }

    @Step
    public void back() {
        newDirectoryPage.back();
    }

    @Step
    public void createDirectory(String directory) {
        enter_directory(directory);
        createDirectory();
        should_be_able_to_see_message("Directory "+directory+" was successfully created.");
        back();
    }

    @Step
    public void should_be_able_to_see_message(String message) {
        assertThat(newDirectoryPage.getContent(), hasItem(containsString(message)));
    }

}
