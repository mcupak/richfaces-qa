/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.showcase.ftest.webdriver.page.a4jJsFunction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class JsFunctionPage implements ShowcasePage {

    @FindBy(xpath = "//*[@class='example-cnt']//span[text()='Alex']")
    private WebElement nameAlex;
    @FindBy(xpath = "//*[@class='example-cnt']//span[text()='John']")
    private WebElement nameJohn;
    @FindBy(xpath = "//*[@class='example-cnt']//span[text()='Kate']")
    private WebElement nameKate;
    private Map<String, WebElement> names;
    @FindBy(xpath = "//*[@class='example-cnt']//b/span")
    private WebElement output;


    @Override
    public String getDemoName() {
        return "jsFunction";
    }

    @Override
    public String getSampleName() {
        return "jsFunction";
    }

    public WebElement getOutput() {
        return output;
    }

    public Map<String, WebElement> getNames() {
        if (names == null) {
            Map<String, WebElement> newNames = new HashMap<String, WebElement>();
            newNames.put("Alex", nameAlex);
            newNames.put("John", nameJohn);
            newNames.put("Kate", nameKate);
            names = Collections.unmodifiableMap(newNames);
        }
        return names;
    }

}
