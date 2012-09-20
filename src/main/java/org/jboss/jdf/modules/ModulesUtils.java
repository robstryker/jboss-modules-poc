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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jboss.jdf.modules.model.AbstractModule;
import org.jboss.jdf.modules.model.GAV;
import org.jboss.jdf.modules.model.Module;
import org.jboss.jdf.modules.model.ModuleAlias;
import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ModulesUtils {

    private static final Logger log = Logger.getLogger(ModulesUtils.class.getName());

    /**
     * This method will find from all available modules the one that satisfies the module alias
     * 
     * @param alias
     * @param availableModules
     * @return
     */
    public static Module getModuleFromAlias(ModuleAlias alias, List<AbstractModule> availableModules) {
        for (AbstractModule module : availableModules) {
            if (alias.getTargetName().equals(module.getName()) && alias.getTargetSlot().equals(module.getSlot())) {
                log.debugf("Module %s found for alias %s", module.getName(), alias.getName());
                return (Module) module;
            }
        }
        return null;
    }

    /**
     * This method queries the jboss.api property to determine if it is a private module or not
     * 
     * @param module
     * @return
     */
    public static boolean isPrivateModule(Module module) {
        String propertyValue = module.getProperties().getProperty("jboss.api");
        boolean isPrivate = propertyValue != null && propertyValue.equals("private");
        log.debugf("Module %s identified as private? [%s]", module.getName(), isPrivate);
        return isPrivate;
    }

    /**
     * This methos get all package names from a individual jar
     * 
     * @param jar
     * @return
     * @throws IOException
     */
    public static Set<String> getPackagesFromResource(File jar) throws IOException {
        Set<String> packages = new TreeSet<String>();
        if (jar.isDirectory()) {
            throw new IOException(String.format("Parameter should be a file: %s", jar));
        }
        JarFile jarFile = new JarFile(jar);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) entries.nextElement();
            // get only package folders that has Classes
            if (jarEntry.getName().endsWith(".class")) {
                String className = jarEntry.getName().replaceAll("/", ".").replaceAll(".class", "");
                // Remove the class Name
                int i = className.lastIndexOf('.');
                if (i > 0) {
                    String packageName = className.substring(0, i);
                    packages.add(packageName);
                }
            }
        }
        log.debugf("Found %s package(s) for jar %s", packages, jar);
        return packages;
    }

    /**
     * This method gets all package names from all resources in module
     * 
     * @param module
     * @return
     * @throws IOException
     */
    public static Set<String> getPackagesFromModule(Module module) throws IOException {
        Set<String> packageNames = new TreeSet<String>();
        for (File jar : module.getResources()) {
            // Evicts native/resources folders
            if (!jar.isDirectory()) {
                packageNames.addAll(getPackagesFromResource(jar));
            }
        }
        log.debugf("Found %s package(s) for modules %s", packageNames, module);
        return packageNames;
    }

    public static GAV getGavFromJar(File jar) throws IOException {
        if (jar.isDirectory()) {
            throw new IOException(String.format("Parameter should be a file: %s", jar));
        }
        JarFile jarFile = new JarFile(jar);
        Enumeration<JarEntry> entries = jarFile.entries();
        // For all Entries
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) entries.nextElement();
            // look for pom.properties
            if (jarEntry.getName().endsWith("pom.properties")) {
                GAV gav = extractGavInformation(jarFile, jarEntry);
                log.debugf("This jar %s has GAV information: %s", jar, gav);
                return gav;

            }
        }
        return null;
    }

    /**
     * 
     * This methos extract GAV (GroupdId, ArtifactId, Version) from the JAR file (pom.properties)
     * 
     * @param jarFile the JAR File
     * @param jarEntry entry for pom.properties
     * @return null if no GAV information found
     * @throws IOException
     */
    private static GAV extractGavInformation(JarFile jarFile, JarEntry jarEntry) throws IOException {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        StringReader stringReader = null;
        try {
            is = jarFile.getInputStream(jarEntry);
            bos = new ByteArrayOutputStream();
            int i = 0;
            while ((i = is.read()) != -1) {
                bos.write(i);
            }
            String pomContent = new String(bos.toByteArray());
            stringReader = new StringReader(pomContent);
            Properties p = new Properties();
            p.load(stringReader);
            return new GAV(p.getProperty("groupId"), p.getProperty("artifactId"), p.getProperty("version"));
        } finally {
            safeClose(is);
            safeClose(bos);
            safeClose(stringReader);
        }
    }

    private static void safeClose(final Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
            } catch (Exception e) {
                // no-op
            }
    }
}
