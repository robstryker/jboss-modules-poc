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
import java.io.IOException;
import java.util.ArrayList;

import org.jboss.jdf.modules.model.AbstractModule;
import org.jboss.jdf.modules.xml.XMLModuleParser;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ParserTest extends AbstractModulesTest {

    private XMLModuleParser parser = new XMLModuleParser();

    /**
     * Test method for {@link org.jboss.jdf.modules.xml.XMLModuleParser#parse(java.io.File)}.
     * 
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public void testParse() throws SAXException, IOException {
        ArrayList<AbstractModule> modules = new ArrayList<AbstractModule>();
        for (File xml : xmldescriptors) {
            AbstractModule module = parser.parse(xml);
            modules.add(module);
        }
        Assert.assertEquals("Should be 259 modules found on EAP modules folder", 259, modules.size());
    }

    @Test
    public void testParseXMLNotExists() {
        try {
            parser.parse(new File("/xpto"));
            Assert.fail("Should fail");
        } catch (SAXException e) {
            Assert.fail("Should fail");
        } catch (IOException e) {
            Assert.assertNotNull("Should five a IOException", e);
        }
    }

}
