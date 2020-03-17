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
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.openhab.binding.hive.internal.client.repository.DefaultNodeRepository;
import org.openhab.binding.hive.internal.client.repository.DefaultSessionRepository;
import org.openhab.binding.hive.internal.client.repository.NodeRepository;
import org.openhab.binding.hive.internal.client.repository.SessionRepository;

import java.util.Objects;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HiveClientFactory {
    public static HiveClient newClient(
            final Username username,
            final Password password
    ) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        final HttpClient httpClient = new HttpClient(new SslContextFactory());
        try {
            httpClient.start();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        final JettyHiveApiRequestFactory requestFactory = new JettyHiveApiRequestFactory(
                httpClient,
                HiveApiConstants.DEFAULT_BASE_PATH,
                new GsonJsonService(),
                "openHAB"
        );

        final SessionRepository sessionRepository = new DefaultSessionRepository(
                requestFactory
        );

        final NodeRepository nodeRepository = new DefaultNodeRepository(
                requestFactory
        );

        return new DefaultHiveClient(
                requestFactory,
                username,
                password,
                sessionRepository,
                nodeRepository
        );
    }
}
