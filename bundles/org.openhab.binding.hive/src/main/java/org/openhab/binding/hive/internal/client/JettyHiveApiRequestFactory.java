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
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.WWWAuthenticationProtocolHandler;
import org.eclipse.jetty.client.api.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Objects;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
final class JettyHiveApiRequestFactory implements HiveApiRequestFactory, SessionAuthenticationManager {
    private static final String ACCESS_TOKEN_HEADER = "X-Omnia-Access-Token";
    private static final String CLIENT_ID_HEADER = "X-Omnia-Client";

    private final Logger logger = LoggerFactory.getLogger(JettyHiveApiRequestFactory.class);

    private final HttpClient httpClient;
    private final URI apiBasePath;
    private final JsonService jsonService;
    private final String clientId;

    @Nullable
    private Session session = null;

    public JettyHiveApiRequestFactory(
            final HttpClient httpClient,
            final URI apiBasePath,
            final JsonService jsonService,
            final String clientId
    ) {
        Objects.requireNonNull(httpClient);
        Objects.requireNonNull(apiBasePath);
        Objects.requireNonNull(jsonService);
        Objects.requireNonNull(clientId);

        if (!apiBasePath.isAbsolute()) {
            throw new IllegalArgumentException("API base path must be absolute");
        }

        this.httpClient = httpClient;
        this.apiBasePath = apiBasePath;
        this.jsonService = jsonService;
        this.clientId = clientId;

        // Remove jetty's default authentication handler as it will be confused
        // by the Hive API's lack of "WWW-Authenticate" header and will mess
        // up manual authentication.
        this.httpClient.getProtocolHandlers().remove(WWWAuthenticationProtocolHandler.NAME);
    }

    @Override
    public void clearSession() {
        this.session = null;
    }

    @Override
    public @Nullable Session getSession() {
        return this.session;
    }

    @Override
    public void setSession(final Session session) {
        Objects.requireNonNull(session);

        this.session = session;
    }

    @Override
    public HiveApiRequest newRequest(final URI endpointPath) {
        final URI target = this.apiBasePath.resolve(endpointPath);

        final Request request = this.httpClient.newRequest(target);

        // Add X-Omnia-Client header
        request.header(CLIENT_ID_HEADER, this.clientId);

        // If we have a session ID add X-Omnia-Access-Token header.
        if (this.session != null) {
            request.header(ACCESS_TOKEN_HEADER, this.session.getSessionId().toString());
        }

        return new JettyHiveApiRequest(
                this.jsonService,
                request
        );
    }
}
