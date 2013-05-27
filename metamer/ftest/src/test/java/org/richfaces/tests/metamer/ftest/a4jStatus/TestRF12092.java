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
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.arquillian.ajocado.Graphene.jq;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.cheiron.halt.XHRHalter;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test for RF-12092
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestRF12092 extends AbstractStatusTest {

    private JQueryLocator statusMessageStart = jq("span[id$=a4jstatus]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/RF-12092.xhtml");
    }

    @Test(groups="4.3")
    @IssueTracking("https://issues.jboss.org/browse/RFPL-12092")
    public void testStatusIsClearedWhenRequestCompleted() {

        XHRHalter.enable();
        XHRHalter halt = getCurrentXHRHalter();

        String status = selenium.getText(statusMessageStart).trim();
        assertEquals(status, "In progress", "The status was not rendered for the request!");
        halt.complete();

        assertFalse(selenium.isVisible(statusMessageStart));
    }

}
