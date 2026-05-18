package webFTP.steps.serenity;

import net.thucydides.core.annotations.Step;
import org.junit.Assert;
import webFTP.pages.AccountPage;
 
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class AccountPageSteps {

    AccountPage accountPage;

    @Step
    public void should_be_in_user_directory(String userDirectory) {

        //assertThat("current directory", userDirectory.equals(accountPage.getCurrentDirectoryName()));
        //Assert.assertTrue(accountPage.getCurrentDirectoryName(), userDirectory.equals(accountPage.getCurrentDirectoryName()));
        Assert.assertTrue(userDirectory.contains(accountPage.getCurrentDirectoryName()));
    }

    @Step
    public void logout() {

        accountPage.click_Logout();
    }

    @Step
    public void newDirectory() {
        accountPage.click_new_directory();
    }


    @Step
    public void should_be_able_to_see_new_directory(String createdDirectory) {
        assertThat(accountPage.getContent(), hasItem(containsString(createdDirectory)));
    }

    @Step
    public void should_not_be_able_to_see_new_directory(String createdDirectory) {
        assertThat(accountPage.getContent(), not(hasItem(containsString(createdDirectory))));
    }

    @Step
    public void select_directory_to_delete(String directory) {

        accountPage.check_directory_to_delete(directory);
    }

    @Step
    public void delete_selected_directory() {
        accountPage.deleteDirectory();
    }

}