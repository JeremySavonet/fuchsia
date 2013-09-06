package org.ow2.chameleon.fuchsia.core.component;

import org.ow2.chameleon.fuchsia.core.declaration.ImportDeclaration;
import org.ow2.chameleon.fuchsia.core.exceptions.ImporterException;

import java.util.List;

/**
 * The components providing this service are capable of creating a proxy thanks to an {@link ImportDeclaration}.
 *
 * @author barjo
 * @author Morgan Martinet
 */
public interface ImporterService {

    String TARGET_FILTER_PROPERTY = "target";

    /**
     * Reify the importDeclaration of given description as a local service.
     *
     * @param importDeclaration The {@link ImportDeclaration} of the service to be imported.
     */
    void addImportDeclaration(final ImportDeclaration importDeclaration) throws ImporterException;

    /**
     * Stop the reification of the given importDeclaration
     *
     * @param importDeclaration The {@link ImportDeclaration} of the service to stop to be imported.
     */
    void removeImportDeclaration(final ImportDeclaration importDeclaration) throws ImporterException;

    /**
     * @return The configuration prefix used or defined by this {@link ImporterService}.
     *         (i.e <code>json-rpc,org.jabsorb,jax-rs,bluetooth,upnp</code>.
     */
    List<String> getConfigPrefix();

    String getName();

}
