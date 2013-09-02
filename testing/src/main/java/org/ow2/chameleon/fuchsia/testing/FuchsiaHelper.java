/*
 * Copyright 2009 OW2 Chameleon Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.ow2.chameleon.fuchsia.testing;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.MissingHandlerException;
import org.apache.felix.ipojo.UnacceptableConfiguration;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.ow2.chameleon.testing.helpers.OSGiHelper;

import java.util.Dictionary;

public class FuchsiaHelper {
    private final BundleContext context;

    public FuchsiaHelper(BundleContext pContext) {
        context = pContext;
    }

    /**
     * Create an instance with the given factory and properties
     *
     * @param factory
     * @param properties
     * @return
     */
    public static ComponentInstance createInstance(final Factory factory, final Dictionary<String, Object> properties) {
        ComponentInstance instance = null;

        // Create an instance
        try {
            instance = factory.createComponentInstance(properties);
        } catch (UnacceptableConfiguration e) {
        } catch (MissingHandlerException e) {
        } catch (ConfigurationException e) {
        }
        return instance;
    }

    /**
     * Get the Factory linked to the given pid
     *
     * @param osgi
     * @param name
     * @return The factory
     */
    public static Factory getValidFactory(final OSGiHelper osgi, final String name) {
        // Get The Factory ServiceReference
        ServiceReference facref = osgi.getServiceReference(Factory.class.getName(), "(&(factory.state=1)(factory.name=" + name + "))");
        // Get the factory
        Factory factory = (Factory) osgi.getServiceObject(facref);

        return factory;
    }

    public static void waitForIt(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            assert false;
        }
    }

    public <T> ServiceRegistration registerService(T service, Class<T> klass) {
        return context.registerService(klass.getName(), service, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getServiceObject(Class<T> klass, String filter) {
        ServiceReference[] srefs = null;

        try {
            srefs = context.getAllServiceReferences(klass.getName(), filter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (srefs != null && srefs.length > 0) {
            return (T) context.getService(srefs[0]);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getServiceObject(Class<T> klass) {
        ServiceReference sref = null;

        sref = context.getServiceReference(klass.getName());

        if (sref != null) {
            T service = (T) context.getService(sref);
            context.ungetService(sref);
            return service;
        } else {
            return null;
        }
    }

    public void dispose() {

    }

}
