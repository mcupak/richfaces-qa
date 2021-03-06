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
package org.richfaces.tests.showcase.param;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestParam extends AbstractGrapheneTest {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    protected JQueryLocator buttonAlex = jq("fieldset form input[type=submit]:first");
    protected JQueryLocator buttonJohn = jq("fieldset form input[type=submit]:last");
    protected JQueryLocator selectedName = jq("span[id$=rep]");

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testWhetherThereIsNothigSetAtFirst() {
        String actualString = selenium.getText(selectedName).trim();
        assertEquals(actualString, "Selected Name:",
            "There can not be anything selected at first after page first load!");
    }

    @Test
    public void testClickToButtonAndCheckTheSelectedName() {
        guardXhr(selenium).click(buttonAlex);

        String actualString = selenium.getText(selectedName).trim();
        assertEquals(actualString, "Selected Name:Alex", "Selected name shoud be Alex!");

        guardXhr(selenium).click(buttonJohn);

        actualString = selenium.getText(selectedName).trim();
        assertEquals(actualString, "Selected Name:John", "Selected name shoud be John!");

        guardXhr(selenium).click(buttonAlex);
        actualString = selenium.getText(selectedName).trim();
        assertEquals(actualString, "Selected Name:Alex", "Selected name shoud be again Alex!");
    }

}
