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

package org.jboss.jdf.modules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jboss.jdf.modules.model.AbstractModule;
import org.jboss.jdf.modules.model.GAV;
import org.jboss.jdf.modules.model.Module;
import org.jboss.jdf.modules.model.ModuleAlias;
import org.jboss.jdf.modules.xml.XMLModuleParser;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class Main {

    /**
     * @param args
     * @throws IOException
     * @throws SAXException
     * @throws ModuleLoadException
     */
    public static void main(String[] args) throws SAXException, IOException {
        System.setProperty("module.path", "/java/tools/jboss-EAP-6.0.0.GA/jboss-eap-6.0/modules");
        File f = new File(System.getProperty("module.path"));
        ModulesFinder moduleSeeker = new ModulesFinder();

        // Find module.xml in path
        List<File> modulesXML = moduleSeeker.findModulesInPath(f);

        // Parse
        List<AbstractModule> modules = new ArrayList<AbstractModule>();
        XMLModuleParser parser = new XMLModuleParser();
        for (File xml : modulesXML) {
            AbstractModule module = parser.parse(xml);
            modules.add(module);
        }

        for (AbstractModule module : modules) {
            // System.out.println(module);
            if (module instanceof ModuleAlias) {
                // Test Find alias target
                ModuleAlias alias = (ModuleAlias) module;
                Module concreteModule = ModulesUtils.getModuleFromAlias(alias, modules);
                // System.out.println("Alias " + alias);
                // System.out.println("Points to " + concreteModule);

            }

            if (module instanceof Module) {
                Module m = (Module) module;
                // Test private Module
                if (ModulesUtils.isPrivateModule(m)) {
                    // System.out.println("Private Module: " + m);
                }

                // Test get Package names from module
                Set<String> packageNames = ModulesUtils.getPackagesFromModule(m);
                for (String packageName : packageNames) {
                    // System.out.println(packageName);
                }

                // Extract the GAV information
                for (File jar : ((Module) module).getResources()) {
                    if (jar.isFile()) {
                        GAV gav = ModulesUtils.getGavFromJar(jar);
                        // System.out.println(gav);
                    }
                }

            }
        }

    }

}
