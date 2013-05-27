/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2012, Red Hat, Inc. and individual contributors by the
 * @authors tag. See the copyright.txt in the distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any
 * later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this software; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org. *****************************************************************************
 */
package org.richfaces.tests.showcase;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc and Juraj Huska</a>
 * @version $Revision$
 */
public abstract class AbstractShowcaseTest extends Arquillian {

    @ArquillianResource
    protected URL contextRoot;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {

        WebArchive war = ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/showcase.war"));
        return war;
    }

    protected URL getPortalRoot() {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/portal/classic/test");
        } catch (MalformedURLException ex) {
            // never happens
        }
        return url;
    }

    protected String getDemo() {
        // demo name - takes last part of package name
        String demoName = this.getClass().getPackage().getName();
        demoName = StringUtils.substringAfterLast(demoName, ".");
        return demoName;
    }

    protected String getSample() {
        // sample name - removes Test- prefix from class name and uncapitalize
        // first letter
        String sampleName = this.getClass().getSimpleName().substring(4);
        //sampleName = WordUtils.uncapitalize(sampleName);
        return sampleName;
    }

    protected String getAdditionToContextRoot() {

        // sample name - removes Test- prefix from class name and uncapitalize
        // first letter
        String sampleName = this.getClass().getSimpleName().substring(4);
        sampleName = WordUtils.uncapitalize(sampleName);

        // demo name - takes last part of package name
        String demoName = this.getClass().getPackage().getName();
        demoName = StringUtils.substringAfterLast(demoName, ".");

        String addition = SimplifiedFormat.format("richfaces/component-sample.jsf?skin=blueSky&demo={0}&sample={1}", demoName,
                sampleName);

        return addition;
    }

    protected URL getContextRoot() {

        String isHTTPTesting = System.getenv("HTTPStesting");

        if (isHTTPTesting != null && isHTTPTesting.equals("true")) {
            try {
                URL httpsUrl = new URL(contextRoot.toExternalForm().replace("http", "https").replace("8080", "8443"));
                return httpsUrl;
            } catch (MalformedURLException e) {
                // it is not malformed URL for sure
            }
        }
        System.out.println(contextRoot);
        return this.contextRoot;
    }
}
