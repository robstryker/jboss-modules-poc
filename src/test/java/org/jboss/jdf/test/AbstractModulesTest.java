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

package org.jboss.jdf.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jboss.jdf.modules.io.ModulesFinder;
import org.jboss.jdf.modules.model.AbstractModule;
import org.jboss.jdf.modules.xml.XMLModuleParser;
import org.junit.BeforeClass;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public abstract class AbstractModulesTest {

    protected static String modulesRoot;

    protected static List<File> xmldescriptors;

    protected static List<AbstractModule> modules = new ArrayList<AbstractModule>();

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        modulesRoot = "/java/tools/jboss-EAP-6.0.0.GA/jboss-eap-6.0/modules";
        xmldescriptors = new ModulesFinder().findModulesInPath(new File(modulesRoot));
        XMLModuleParser parser = new XMLModuleParser();
        for (File file : xmldescriptors) {
            AbstractModule module = parser.parse(file);
            modules.add(module);
        }
    }

}
