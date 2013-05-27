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
package org.richfaces.tests.metamer.ftest;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.event.PhaseId;

import org.apache.commons.lang.ArrayUtils;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;

/**
 * Retrieves and asserts the info about life-cycle.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23059 $
 */
public class PhaseInfo {

    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();
    private TextRetriever retrieveRequestTime = TextRetriever.getInstance().locator(jq("span[id$=requestTime]"));
    private JQueryLocator phasesItems = jq("div#phasesPanel li");
    private Map<PhaseId, Set<String>> map = new LinkedHashMap<PhaseId, Set<String>>();

    /**
     * Asserts that the phases has occurred in last request by the specified list.
     */
    public void assertPhases(PhaseId... expectedPhases) {
        initialize();
        if (ArrayUtils.contains(expectedPhases, PhaseId.ANY_PHASE)) {
            expectedPhases = new LinkedList<PhaseId>(PhaseId.VALUES).subList(1, 7).toArray(new PhaseId[6]);
        }
        PhaseId[] actualPhases = map.keySet().toArray(new PhaseId[map.size()]);
        assertEquals(actualPhases, expectedPhases);
    }

    /**
     * Asserts that in the given phase has occurred the listener or order producer writing the log message to phases
     * list.
     *
     * @param phaseId
     *            the phase where the listener occurred
     * @param message
     *            the part of the message which it should be looked up
     */
    public void assertListener(PhaseId phaseId, String message) {
        initialize();
        Set<String> set = map.get(phaseId);
        if (set != null && set.size() > 0) {
            for (String description : set) {
                if (description.contains(message)) {
                    return;
                }
            }
        }
        throw new AssertionError("The '" + message + "' wasn't found across messages in phase " + phaseId);
    }

    /**
     * Asserts that there is no specified message in phases list.
     *
     * @param message
     *            the part of the message which it should be looked up
     */
    public void assertNoListener(String message) {
        initialize();
        for (Entry<PhaseId, Set<String>> entry : map.entrySet()) {
            PhaseId phaseId = entry.getKey();
            Set<String> descriptions = entry.getValue();

            for (String description : descriptions) {
                if (description.contains(message)) {
                    throw new AssertionError("The '" + message + "' was found across messages in phase " + phaseId);
                }
            }
        }
    }

    public SeleniumCondition getListenerCondition(final PhaseId phaseId, final String message) {
        return new SeleniumCondition() {
            @Override
            public boolean isTrue() {
                initialize();
                Set<String> set = map.get(phaseId);
                if (set != null && set.size() > 0) {
                    for (String description : set) {
                        if (description.contains(message)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };

    }

    private void initialize() {
        if (retrieveRequestTime.isValueChanged()) {
            map.clear();
            retrieveRequestTime.initializeValue();

            int count = selenium.getCount(phasesItems);

            Set<String> set = null;
            for (int i = 1; i <= count; i++) {
                String description = selenium.getText(jq(SimplifiedFormat.format("{0}:nth-child({1})", phasesItems.getRawLocator(), i)));

                if (!description.startsWith("*")) {
                    set = new LinkedHashSet<String>();
                    map.put(getPhaseId(description), set);
                } else {
                    set.add(description.substring(2));
                }
            }
        }
    }

    private PhaseId getPhaseId(String phaseIdentifier) {
        for (PhaseId phaseId : PhaseId.VALUES) {
            if (phaseIdentifier.startsWith(phaseId.toString())) {
                return phaseId;
            }
        }
        throw new IllegalStateException("no such phase '" + phaseIdentifier + "'");
    }
}
