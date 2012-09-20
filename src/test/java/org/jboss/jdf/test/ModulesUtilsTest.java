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
import java.util.Set;

import org.jboss.jdf.modules.ModulesUtils;
import org.jboss.jdf.modules.model.AbstractModule;
import org.jboss.jdf.modules.model.GAV;
import org.jboss.jdf.modules.model.Module;
import org.jboss.jdf.modules.model.ModuleAlias;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ModulesUtilsTest extends AbstractModulesTest {

    /**
     * Test method for
     * {@link org.jboss.jdf.modules.ModulesUtils#getModuleFromAlias(org.jboss.jdf.modules.model.ModuleAlias, java.util.List)}.
     */
    @Test
    public void testGetModuleFromAlias() {
        for (AbstractModule am : modules) {
            if (am instanceof ModuleAlias && am.getName().equals("org.apache.commons.logging")) {
                ModuleAlias ma = (ModuleAlias) am;
                Module module = ModulesUtils.getModuleFromAlias(ma, modules);
                Assert.assertEquals("Should Find module from module-alias", "org.slf4j.jcl-over-slf4j", module.getName());

            }
        }
    }

    /**
     * Test method for {@link org.jboss.jdf.modules.ModulesUtils#isPrivateModule(org.jboss.jdf.modules.model.Module)}.
     */
    @Test
    public void testIsPrivateModule() {
        for (AbstractModule am : modules) {
            if (am.getName().equals("org.codehaus.jackson.jackson-jaxrs")) {
                boolean isPrivate = ModulesUtils.isPrivateModule((Module) am);
                Assert.assertTrue("Module should be private", isPrivate);

            }
        }
    }

    /**
     * Test method for {@link org.jboss.jdf.modules.ModulesUtils#isPrivateModule(org.jboss.jdf.modules.model.Module)}.
     */
    @Test
    public void testNotPrivateModule() {
        for (AbstractModule am : modules) {
            if (am.getName().equals("org.picketlink")) {
                boolean isPrivate = ModulesUtils.isPrivateModule((Module) am);
                Assert.assertFalse("Module should NOT be private", isPrivate);

            }
        }
    }

    /**
     * Test method for {@link org.jboss.jdf.modules.ModulesUtils#getPackagesFromResource(java.io.File)}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetPackagesFromResource() throws IOException {
        for (AbstractModule am : modules) {
            if (am.getName().equals("org.picketlink")) {
                Module module = (Module) am;
                File jar = module.getResources().get(0);
                Set<String> packages = ModulesUtils.getPackagesFromResource(jar);
                Assert.assertEquals("Picketlink packages for " + jar + " should be 90 packages", 90, packages.size());

            }
        }
    }

    /**
     * Test method for {@link org.jboss.jdf.modules.ModulesUtils#getPackagesFromModule(org.jboss.jdf.modules.model.Module)}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetPackagesFromModule() throws IOException {
        for (AbstractModule am : modules) {
            if (am.getName().equals("org.picketlink")) {
                Module module = (Module) am;
                Set<String> packages = ModulesUtils.getPackagesFromModule(module);
                Assert.assertEquals("Picketlink packages for picketlink module should be 100 packages", 100, packages.size());

            }
        }
    }

    /**
     * Test method for {@link org.jboss.jdf.modules.ModulesUtils#getGavFromJar(java.io.File)}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetGavFromJar() throws IOException {
        for (AbstractModule am : modules) {
            if (am.getName().equals("org.picketlink")) {
                Module module = (Module) am;
                File jar = module.getResources().get(0);
                GAV gav = ModulesUtils.getGavFromJar(jar);
                Assert.assertEquals(gav.getGroupId(), "org.picketlink");
                Assert.assertEquals(gav.getArtifactId(), "picketlink-core");
                Assert.assertEquals(gav.getVersion(), "2.1.1.Final-redhat-1");
            }
        }
    }

    /**
     * Test method for {@link org.jboss.jdf.modules.ModulesUtils#getGavFromJar(java.io.File)}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetGavFromNoGavJar() throws IOException {
        for (AbstractModule am : modules) {
            if (am.getName().equals("org.dom4j")) {
                Module module = (Module) am;
                File jar = module.getResources().get(0);
                GAV gav = ModulesUtils.getGavFromJar(jar);
                Assert.assertNull("GAV not present. Should be null", gav);
            }
        }
    }
}
