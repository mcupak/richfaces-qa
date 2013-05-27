package org.richfaces.tests.metamer.ftest.a4jPush;

import static java.text.MessageFormat.format;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.pushAttributes;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.ajocado.waiting.conditions.TextEquals;
import org.jboss.test.selenium.support.ui.TextNotEquals;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestTwoPush extends AbstractWebDriverTest {

    private TwoPushPage page;

    TextNotEquals textNotEq = TextNotEquals.getInstance();
    TextEquals textEq = TextEquals.getInstance();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jPush/twoPush.xhtml");
    }

    @BeforeMethod
    public void loadPage() {
        page = new TwoPushPage();
        injectWebElementsToPage(page);
    }

    @Test
    public void testSimplePushEventReceive() {
        verifyPushUpdateReceive(3L);
    }

    @Test
    public void testPushEnable() {
        // disable push updates
        page.pushEnabledChckBox.click();

        (new WebDriverWait(driver, TIMEOUT)).until(
            new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return page.pushEnabledChckBox.isEnabled();
                }
            });

        // enable push updates
        page.pushEnabledChckBox.click();

        verifyPushUpdateReceive(10L);
    }

    @Test
    public void testOnSubscribed() {
        pushAttributes.set(PushAttributes.onsubscribed, "metamerEvents += \"onsubscribed \"");

        // first onsubscribed event receive immediatelly after form update
        String event = ((String) executeJS("return window.metamerEvents")).trim();
        // there are 2 push components on page (this example verify that one doesn't influence another one)
        assertEquals(event, "onsubscribed onsubscribed", "Attribute onsubscribed doesn't work");

        page.pushEnabledChckBox.click();
        page.pushEnabledChckBox.click();
        // second onsubscribed event receive after manual re-attach by checkbox
        event = ((String) executeJS("return window.metamerEvents")).trim();
        assertEquals(event, "onsubscribed onsubscribed", "Attribute onsubscribed doesn't work");
    }

    /**
     * @param timeout in second: Since re-attach to topic require more time, there should
     * be set a bit longer timeout to verify it correctly
     */
    private void verifyPushUpdateReceive(long timeout) {
        for (int i = 0; i < 5; ++i) {
            // date time before first push event received
            final String output1Text = page.output1.getText();
            // note: for fail in first iteration the 0 iterations done is correct information
            String msgFormat = "Push event was not received within {0} sec. Iteration(s) done: {1}";

            // since there is not re-attach to topic, 3 sec timeout should be enough
            (new WebDriverWait(driver, timeout))
                .withMessage(format(msgFormat, timeout, i))
                .until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        page.push1Btn.click();
                        return !page.output1.getText().equals(output1Text);
                    }
                });
        }
    }

/*    @Test3
    public void testPush1() {
        *//**
         * 1. open page (handle alert)
         * 2. click push button and verify push updates receiving
         * 3. then disable push (detach)
         * 4. try receive some updates (on both push buttons)
         * 5. then enable push for first push again
         * 6. start receive notifications (waint until first one received)
         *//*
        String startingDate = selenium.getText(date1output);
        waitFor(2000);
        selenium.click(push1Button);
        selenium.answerOnNextPrompt("OK");

        String currentDateTime = date.getValue();
        System.out.println(" # 1. " + startingDate + ", 2. " + currentDateTime);

        System.out.println(" ### pushEnabled = " + pushEnabled.getValue());

        selenium.check(push1checkbox);
        System.out.println(" ### pushEnabled = " + pushEnabled.getValue());

        currentDateTime = selenium.getText(date1output);

    }*/

}
