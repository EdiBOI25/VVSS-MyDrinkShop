package webFTP.steps.serenity;


import net.thucydides.core.annotations.Step;
import webFTP.pages.DeleteDirectoryPage;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

public class DeleteDirectoryPageSteps {

    DeleteDirectoryPage deleteDirectoryPage;

    @Step
    public void delete_directory() {

        deleteDirectoryPage.click_delete_directory();
    }

    @Step
    public void back() {

        deleteDirectoryPage.back();
    }

    @Step
    public void delete_directory(String directory) {
        should_be_able_to_see_message("Directory "+directory);
        delete_directory();
        //should_be_able_to_see_message("Deleted subdirectory /home/scs/others/vvta/"+directory);
        should_be_able_to_see_message("Processing directory /home/vvta1/"+directory);
        back();
    }

    @Step
    public void should_be_able_to_see_message(String message) {
        assertThat(deleteDirectoryPage.getContent(), hasItem(containsString(message)));
    }

}
