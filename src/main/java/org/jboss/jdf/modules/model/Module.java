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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class Module extends AbstractModule {

    private String mainClass;

    private Properties properties = new Properties();

    private List<File> resources = new ArrayList<File>();

    private List<ModuleDependency> moduleDependencies = new ArrayList<ModuleDependency>();

    private List<SystemDependency> systemDependencies = new ArrayList<SystemDependency>();

    private Filter exports;

    /**
     * The main class of this module, if any.
     * 
     * @return
     */
    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    /**
     * The resources that make up this module.
     * 
     * @return
     */
    public List<File> getResources() {
        return resources;
    }

    /**
     * The modules API exposes a method which can read property (string key-value pair) values from a module. To specify values
     * for these properties you use the "properties" element which can contain zero or more "property" elements
     * 
     * @return
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * The Module dependencies for this module.
     * 
     * @return
     */
    public List<ModuleDependency> getModuleDependencies() {
        return moduleDependencies;
    }

    /**
     * The System dependencies for this module.
     * 
     * @return
     */
    public List<SystemDependency> getSystemDependencies() {
        return systemDependencies;
    }

    /**
     * The filter expressions to apply to the export filter of the local resources of this module.
     * 
     * @return the exports
     */
    public Filter getExports() {
        return exports;
    }

    public void setExports(Filter exports) {
        this.exports = exports;
    }
    
    
}
