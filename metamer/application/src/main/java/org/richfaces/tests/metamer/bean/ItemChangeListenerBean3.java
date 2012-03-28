/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
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
 */
package org.richfaces.tests.metamer.bean;

import static org.richfaces.tests.metamer.bean.RichBean.logToPage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeListener;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean
@ViewScoped
public class ItemChangeListenerBean3 implements ItemChangeListener {

    @Override
    public void processItemChange(ItemChangeEvent event) throws AbortProcessingException {
        logToPage("* itemChangeListenerBean3 item changed: " + (event.getOldItem() == null ? null : event.getOldItem().getId()) + " -> "
                + (event.getNewItem() != null ? event.getNewItem().getId() : null));
    }
}