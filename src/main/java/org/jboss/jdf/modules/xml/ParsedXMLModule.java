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

package org.jboss.jdf.modules.xml;

import java.io.IOException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ParsedXMLModule {

    private Document document;
    private XPathExpression privateApiXpathExpression;
    private XPathExpression moduleExpression;

    /**
     * @param document
     * @throws XPathExpressionException
     */
    public ParsedXMLModule(Document document) {
        this.document = document;
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        try {
            privateApiXpathExpression = xPath.compile("/module/properties/property[@name='jboss.api'][@value='private']");
            moduleExpression = xPath.compile("/module");
        } catch (XPathExpressionException e) {
            throw new RuntimeException("There's an erron on XPath Expression. Cause " + e.getMessage(), e);
        }

    }

    /**
     * Return if this module is private.
     * 
     * A module is considered private once there is a propety name=jboss.api with value=private
     * 
     * @return true if this is Module is considered private.
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws IOException
     */
    public boolean isPrivateModule() throws XPathExpressionException {
        return privateApiXpathExpression.evaluate(document, XPathConstants.NODE) != null;
    }

    /**
     * Get The module name
     * 
     * @return
     * @throws XPathExpressionException
     */
    public String getModuleName() throws XPathExpressionException {
        Node node = (Node) moduleExpression.evaluate(document, XPathConstants.NODE);
        return node.getAttributes().getNamedItem("name").getNodeValue();
    }

    /**
     * Get The module slot
     * 
     * @return slot value or main if no slot is defined.
     * @throws XPathExpressionException
     */
    public String getSlot() throws XPathExpressionException {
        Node node = (Node) moduleExpression.evaluate(document, XPathConstants.NODE);
        Node slotAttribute = node.getAttributes().getNamedItem("slot");
        if (slotAttribute != null) {
            return slotAttribute.getNodeValue();
        } else {
            return "main";
        }
    }
}
