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
public final class Protocol extends SimpleValueTypeBase<String> {
    public static final Protocol SYNTHETIC = new Protocol("SYNTHETIC");
    public static final Protocol ZIGBEE = new Protocol("ZIGBEE");
    public static final Protocol PROXIED = new Protocol("PROXIED");
    public static final Protocol MQTT = new Protocol("MQTT");
    public static final Protocol XMPP = new Protocol("XMPP");
    public static final Protocol VIRTUAL = new Protocol("VIRTUAL");

    public static final Protocol NONE = new Protocol("NONE");

    public Protocol(final String protocol) {
        super(protocol);
    }
}
