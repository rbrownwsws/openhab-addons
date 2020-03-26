/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.hive.internal.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
abstract class URITypeBase extends FromStringTypeBase<URI> {
    public URITypeBase(final String stringValue) {
        super(stringValue);
    }

    protected URI transform(final String stringValue) {
        try {
            return new URI(stringValue);
        } catch (final URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
