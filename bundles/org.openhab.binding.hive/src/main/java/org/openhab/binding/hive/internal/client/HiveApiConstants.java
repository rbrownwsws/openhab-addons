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

import java.net.URI;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HiveApiConstants {
    private HiveApiConstants() {
        throw new AssertionError();
    }

    /**
     * The location of the real Hive API.
     */
    public static final URI DEFAULT_BASE_PATH = URI.create("https://api.prod.bgchprod.info/omnia/");


    public static final URI ENDPOINT_ACCESS_TOKENS = URI.create("accessTokens");

    public static final URI ENDPOINT_CALLBACK_TOKENS = URI.create("callbackTokens");

    public static final URI ENDPOINT_CHANNELS = URI.create("channels");

    public static final URI ENDPOINT_CONTACTS = URI.create("contacts");

    public static final URI ENDPOINT_DEVICE_TOKENS = URI.create("deviceTokens");

    public static final URI ENDPOINT_EVENTS = URI.create("events");

    public static final URI ENDPOINT_MEDIA = URI.create("media");

    public static final URI ENDPOINT_MIGRATION = URI.create("migrations");

    public static final URI ENDPOINT_NODES = URI.create("nodes");
    public static final URI ENDPOINT_NODE = URI.create("nodes/");

    public static final URI ENDPOINT_PASSWORDS = URI.create("auth/passwordReset");

    public static final URI ENDPOINT_RESERVATIONS = URI.create("nodes/reservation");

    public static final URI ENDPOINT_RULES = URI.create("rules");

    public static final URI ENDPOINT_SESSIONS = URI.create("auth/sessions");
    public static final URI ENDPOINT_SESSION = URI.create("auth/sessions/");

    public static final URI ENDPOINT_TOPOLOGY = URI.create("topology");

    public static final URI ENDPOINT_USERS = URI.create("users");
}
