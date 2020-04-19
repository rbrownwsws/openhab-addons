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

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Represents a Hive API node type.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class NodeType extends SimpleValueTypeBase<URI> {
    public static final NodeType HUB = new NodeType(URI.create("http://alertme.com/schema/json/node.class.hub.json#"));
    public static final NodeType THERMOSTAT = new NodeType(URI.create("http://alertme.com/schema/json/node.class.thermostat.json#"));
    public static final NodeType THERMOSTAT_UI = new NodeType(URI.create("http://alertme.com/schema/json/node.class.thermostatui.json#"));
    public static final NodeType RADIATOR_VALVE = new NodeType(URI.create("http://alertme.com/schema/json/node.class.trv.json#"));

    public NodeType(final URI nodeType) {
        super(nodeType);
    }
}
