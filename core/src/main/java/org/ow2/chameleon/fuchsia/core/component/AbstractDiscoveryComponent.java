package org.ow2.chameleon.fuchsia.core.component;


import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.ow2.chameleon.fuchsia.core.declaration.ImportDeclaration;
import org.slf4j.Logger;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Abstract implementation of an discovery component which provides an {@link DiscoveryService}.
 * Start must be call before registering the service !
 * Stop must be called while the service is no more available !
 *
 * @author Morgan Martinet
 */
public abstract class AbstractDiscoveryComponent implements DiscoveryService, DiscoveryIntrospection {

    private final Map<ImportDeclaration, ServiceRegistration> importDeclarationsRegistered;
    private final BundleContext bundleContext;


    protected AbstractDiscoveryComponent(BundleContext bundleContext) {
        this.importDeclarationsRegistered = new HashMap<ImportDeclaration, ServiceRegistration>();
        this.bundleContext = bundleContext;
    }

    /**
     * Start the discovery component, iPOJO Validate instance callback.
     * Must be override !
     */
    protected void start() {
    }

    /**
     * Stop the discovery component, iPOJO Invalidate instance callback.
     * Must be override !
     */
    protected void stop() {
        synchronized (importDeclarationsRegistered) {
            for (ServiceRegistration registration : importDeclarationsRegistered.values()) {
                if (registration != null) {
                    registration.unregister();
                }
            }
            importDeclarationsRegistered.clear();
        }
    }

    /**
     * Utility method to register an ImportDeclaration has a Service in OSGi.
     * If you use it make sure to use unregisterImportDeclaration(...) to unregister the ImportDeclaration
     *
     * @param importDeclaration the ImportDeclaration to register
     */
    protected void registerImportDeclaration(ImportDeclaration importDeclaration) {
        synchronized (importDeclarationsRegistered) {
            if (importDeclarationsRegistered.containsKey(importDeclaration)) {
                throw new IllegalStateException("The given ImportDeclaration has already been registered.");
            }

            Dictionary<String, Object> props = new Hashtable<String, Object>();
            String clazzes[] = new String[]{ImportDeclaration.class.getName()};
            ServiceRegistration registration;
            registration = bundleContext.registerService(clazzes, importDeclaration, props);

            importDeclarationsRegistered.put(importDeclaration, registration);
        }
    }

    /**
     * Utility method to unregister an ImportDeclaration of OSGi.
     * Use it only if you have used registerImportDeclaration(...) to register the ImportDeclaration
     *
     * @param importDeclaration the ImportDeclaration to unregister
     */
    protected void unregisterImportDeclaration(ImportDeclaration importDeclaration) {
        ServiceRegistration registration;
        synchronized (importDeclarationsRegistered) {
            registration = importDeclarationsRegistered.remove(importDeclaration);
            if (registration == null) {
                throw new IllegalStateException("The given ImportDeclaration has never been registered"
                        + "or have already been unregistered.");
            }
        }
        registration.unregister();
    }

    @Override
    public String toString() {
        return getName();
    }

    public Set<ImportDeclaration> getImportDeclarations() {
        return new HashSet<ImportDeclaration>(importDeclarationsRegistered.keySet());
    }

    protected BundleContext getBundleContext() {
        return bundleContext;
    }

    public abstract Logger getLogger();
}
