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

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class NodeType extends URITypeBase {
    public static final NodeType HUB = new NodeType("http://alertme.com/schema/json/node.class.hub.json#");
    public static final NodeType THERMOSTAT = new NodeType("http://alertme.com/schema/json/node.class.thermostat.json#");
    public static final NodeType THERMOSTAT_UI = new NodeType("http://alertme.com/schema/json/node.class.thermostatui.json#");
    public static final NodeType RADIATOR_VALVE = new NodeType("http://alertme.com/schema/json/node.class.trv.json#");

    public NodeType(final String stringValue) {
        super(stringValue);
    }
}
