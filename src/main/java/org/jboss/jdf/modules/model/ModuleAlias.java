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

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ModuleAlias extends AbstractModule {

    private String targetName;

    private String targetSlot = "main";

    /**
     * The name of the module to which this alias refers.
     * 
     * @return the targetName
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * @param targetName the targetName to set
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * The version slot of the module to which this alias refers. If not specified, defaults to "main".
     * 
     * @return the targetSlot
     */
    public String getTargetSlot() {
        if (this.targetSlot == null || this.targetSlot.isEmpty()) {
            return "main";
        }
        return targetSlot;
    }

    /**
     * @param targetSlot the targetSlot to set
     */
    public void setTargetSlot(String targetSlot) {
        this.targetSlot = targetSlot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("ModuleAlias [name=%s, slot=%s, target-name=%s, target-slot=%s]", getName(), getSlot(),
                getTargetName(), getTargetSlot());
    }

}
