package webFTP.steps.serenity;

import net.thucydides.core.annotations.Step;
import webFTP.pages.LogoutPage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;

public class LogoutPageSteps {

    LogoutPage logoutPage;

    @Step
    public void should_see_logout_message(String message) {
        assertThat(logoutPage.getDefinitions(), hasItem(containsString(message)));
    }

}
