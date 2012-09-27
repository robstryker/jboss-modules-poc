/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
 */

package org.jboss.jdf.modules.model;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class BaseModule {

    private String name;

    private String slot = "main";

    private File sourceXML;

    /**
     * The name of the module. This name must match the name of the module being loaded
     * 
     * @return
     */
    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The version slot. If not specified, defaults to "main".
     */
    @XmlAttribute
    public String getSlot() {
        if (this.slot == null || this.slot.isEmpty()) {
            return "main";
        }
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    /**
     * @return the sourceXML
     */
    @XmlElement(name = "source-xml")
    public File getSourceXML() {
        return sourceXML;
    }

    /**
     * @param sourceXML the sourceXML to set
     */
    public void setSourceXML(File sourceXML) {
        this.sourceXML = sourceXML;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(this.getClass().getSimpleName() + " [name=%s, slot=%s] - XML: %s", getName(), getSlot(),
                getSourceXML());
    }

}
