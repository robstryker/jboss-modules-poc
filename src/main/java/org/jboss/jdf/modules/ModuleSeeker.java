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

import javax.xml.xpath.XPathExpressionException;

import org.jboss.jdf.modules.xml.ParsedXMLModule;
import org.jboss.jdf.modules.xml.XMLModuleParser;
import org.jboss.logging.Logger;
import org.jboss.modules.ModuleIdentifier;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ModuleSeeker {

    private static final Logger log = Logger.getLogger(ModuleSeeker.class.getName());

    private List<File> modulesDescriptors = new ArrayList<File>();

    public List<ModuleIdentifier> findModulesInPath(File rootPath) {
        if (!rootPath.isDirectory()) {
            throw new IllegalArgumentException(String.format("Path should be a directory: %s", rootPath));
        }
        scanDir(rootPath);
        log.debugf("Scan for modules finished. %s modules found.", modulesDescriptors.size());

        try {
            return createModuleIdentifiers();
        } catch (Exception e) {
            throw new RuntimeException("Problem creating modules from xml files. Cause: " + e.getCause(), e);
        }
    }

    /**
     * @return
     * @throws XPathExpressionException
     * @throws IOException
     * @throws SAXException
     * 
     */
    private List<ModuleIdentifier> createModuleIdentifiers() throws XPathExpressionException, SAXException, IOException {
        List<ModuleIdentifier> modules = new ArrayList<ModuleIdentifier>();
        XMLModuleParser xmlParser = new XMLModuleParser();
        for (File xml : modulesDescriptors) {
            log.tracef("Parsing xml file %s", xml);
            ParsedXMLModule parsedXmlModule = xmlParser.parse(xml);
            if (parsedXmlModule.isPrivateModule()) {
                String name = parsedXmlModule.getModuleName();
                String slot = parsedXmlModule.getSlot();
                log.tracef("Module %s:%s idenfied", name, slot);
                modules.add(ModuleIdentifier.create(name, slot));
            }
        }
        return modules;
    }

    /**
     * Recursivally seeks for all
     * 
     * @param rootPath
     */
    private void scanDir(File rootPath) {
        for (File file : rootPath.listFiles()) {
            if (file.isDirectory()) {
                scanDir(file);
            } else {
                scanFile(file);
            }
        }
    }

    /**
     * @param file
     */
    private void scanFile(File file) {
        int indexLastDot = file.getName().lastIndexOf('.');
        // If file has extension
        if (indexLastDot >= 0) {
            String fileName = file.getName();
            String fileExtension = fileName.substring(indexLastDot + 1);
            log.tracef("File: %s - extension [%s]", file, fileExtension);
            // If the extension is xml
            if (fileExtension.equalsIgnoreCase("xml")) {
                modulesDescriptors.add(file);
            }
        }
    }
}
