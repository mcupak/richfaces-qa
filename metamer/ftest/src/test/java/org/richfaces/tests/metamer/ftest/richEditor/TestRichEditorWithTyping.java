/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2012, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richEditor;

import static java.text.MessageFormat.format;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.editorAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.google.common.base.Predicate;
import java.net.URL;
import org.jboss.test.selenium.support.ui.TextContains;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichEditorWithTyping extends AbstractWebDriverTest {

    private EditorSimplePage page;
    private String phaseListenerLogFormat = "*3 value changed: <p> {0}</p> -> <p> {1}</p>";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richEditor/simple.xhtml");
    }

    @BeforeMethod
    public void loadPage() {
        page = new EditorSimplePage();
        injectWebElementsToPage(page);
    }

    @Test
    public void testImmediate() {
        editorAttributes.set(EditorAttributes.immediate, Boolean.TRUE);
        verifyValueChangeListener(page.hButton, page.valueChangeListenerAfterImmediate);
    }

    @Test
    public void testOnDirty() {
        editorAttributes.set(EditorAttributes.ondirty, "metamerEvents += \"dirty \"");
        executeJS("window.metamerEvents = \"\";");
        typeTextToEditor("x");
        String event = ((String) executeJS("return window.metamerEvents")).trim();
        assertEquals(event, "dirty", "Attribute ondirty doesn't work");
    }

    @Test
    public void testTypeAndSubmit() throws InterruptedException {
        typeTextToEditor("SOMETHING");
        page.hButton.submit();
        new WebDriverWait(driver, WAIT_TIME).until(TextContains.getInstance().
                element(page.output).text("SOMETHING"));
    }

    @Test
    public void testValue() {
        // write some value in editor and submit by normal way
        typeTextToEditor("text1");
        page.hButton.submit();

        // then set value from outside, and check this value in editor
        editorAttributes.set(EditorAttributes.value, "text2");
        String found = getTextFromEditor();
        assertTrue(found != null && found.contains("text2"));
    }

    @Test
    public void testValueChangeListenerWithHButton() {
        verifyValueChangeListener(page.hButton, page.valueChangeListener);
    }

    @Test
    public void testValueChangeListenerWithA4jButton() {
        verifyValueChangeListener(page.a4jButton, page.valueChangeListener);
    }

    /**
     * Method for retrieve text from editor. Editor lives within iFrame, so
     * there are need some additional steps to reach element containing editor
     * text
     *
     * @return
     */
    private String getTextFromEditor() {
        try {
//            driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(0);//must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            return activeArea.getText();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    /**
     * Since editor component lives within iFrame element, additional steps are
     * required
     *
     * This method selects appropriate iframe, do action, and return focus to
     * PARENT frame
     *
     * @param text
     */
    private void typeTextToEditor(String text) {
        try {
//            driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(0);//must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            activeArea.click();
            activeArea.sendKeys(text);
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    /**
     * Provide common steps needed to verify valueChangeListener. Accepts
     * JQueryLocator for submit button - provide ability to verify JSF submit as
     * well as Ajax submit.
     *
     * @param submitBtn
     */
    private void verifyValueChangeListener(WebElement submitBtn, final WebElement listener) {

        typeTextToEditor("text1");
        // and submit typed text
        submitBtn.submit();
        new WebDriverWait(driver, WAIT_TIME).until(TextContains.getInstance().
                element(page.output).text("text1"));
        typeTextToEditor("text2");
        // and submit typed text
        submitBtn.submit();
        new WebDriverWait(driver, WAIT_TIME).until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver webDriver) {
                return listener.getText().contains(format(phaseListenerLogFormat, "text1", "text1text2")) || listener.getText().contains(format(phaseListenerLogFormat, "text1", "text2text1"));
            }
        });
    }
}
