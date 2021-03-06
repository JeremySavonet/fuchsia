package org.ow2.chameleon.fuchsia.core.declaration;

import org.osgi.framework.ServiceReference;
import org.ow2.chameleon.fuchsia.core.component.DiscoveryService;

import java.util.Map;

/**
 * {@link Declaration} is a data transfer object (DTO pattern) that transit between layers
 * in Fuchsia.
 * They are created by the {@link DiscoveryService}
 *
 * @author Morgan Martinet
 */
interface Declaration {

    /**
     * @return the metadata of the Declaration
     */
    Map<String, Object> getMetadata();

    /**
     * @return the extra-metadata of the Declaration
     */
    Map<String, Object> getExtraMetadata();

    /**
     * Return the Status of the Declaration at the moment t of the method call.
     * The status contains the bindings which exist between the Declaration and
     * the ImporterServices.
     *
     * @return the actual (at this moment t) Declaration status.
     */
    Status getStatus();

    /**
     * Bind the given ImporterService to the Declaration.
     * This method stock the ImporterService to remember the binding.
     * <p/>
     * This method should only be called by a ImportationLinker.
     * The linker must call this method when it give the Declaration to the ImporterService.
     *
     * @param serviceReference the ServiceReference the Declaration is bind to.
     */
    void bind(ServiceReference serviceReference);

    /**
     * Unbind the given ImporterService of the Declaration.
     * This method remove the ImporterService to forget the binding.
     * <p/>
     * This method should only be called by a ImportationLinker.
     * The linker must call this method when it remove the Declaration of the ImporterService.
     *
     * @param serviceReference the ServiceReference the Declaration is unbind to.
     */
    void unbind(ServiceReference serviceReference);


}
