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

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.exception.HiveApiNotAuthorisedException;
import org.openhab.binding.hive.internal.client.repository.NodeRepository;
import org.openhab.binding.hive.internal.client.repository.SessionRepository;

/**
 * The default implementation of {@link HiveClient}
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
final class DefaultHiveClient implements HiveClient {
    private final SessionAuthenticationManager authenticationManager;

    private final Username username;
    private final Password password;

    private final SessionRepository sessionRepository;
    private final NodeRepository nodeRepository;

    private @Nullable Session session;

    /**
     *
     *
     * @throws org.openhab.binding.hive.internal.client.exception.HiveApiAuthenticationException
     *      If we failed to authenticate with the provided username and password.
     *
     * @throws org.openhab.binding.hive.internal.client.exception.HiveApiUnknownException
     *      If something unexpected happens while communicating with the Hive
     *      API.
     */
    public DefaultHiveClient(
            final SessionAuthenticationManager authenticationManager,
            final Username username,
            final Password password,
            final SessionRepository sessionRepository,
            final NodeRepository nodeRepository
    ) {
        Objects.requireNonNull(authenticationManager);
        Objects.requireNonNull(sessionRepository);
        Objects.requireNonNull(nodeRepository);

        this.authenticationManager = authenticationManager;

        this.username = username;
        this.password = password;

        this.sessionRepository = sessionRepository;
        this.nodeRepository = nodeRepository;

        authenticate();
    }

    @Override
    public void close() {
        // TODO: Clean up
    }

    /**
     * Try to authenticate with the Hive API.
     *
     * @throws org.openhab.binding.hive.internal.client.exception.HiveApiAuthenticationException
     *      If we failed to authenticate with the stored username and password.
     *
     * @throws org.openhab.binding.hive.internal.client.exception.HiveApiUnknownException
     *      If something unexpected happens while communicating with the Hive
     *      API.
     */
    private void authenticate() {
        this.authenticationManager.clearSession();

        final Session session = this.sessionRepository.createSession(
                this.username,
                this.password
        );

        this.session = session;
        this.authenticationManager.setSession(session);
    }

    private <T> T makeAuthenticatedApiCall(final Supplier<T> apiCall) {
        // If we get a valid result return it.
        // If we are not authorised check if session has expired, reauthenticate and try again.
        // Otherwise let other exceptions bubble up.
        boolean reauthenticated = false;
        while (true) {
            try {
                return apiCall.get();
            } catch (final HiveApiNotAuthorisedException ex) {
                if (reauthenticated || this.sessionRepository.isValidSession(this.authenticationManager.getSession())) {
                    // We are either not authorised for this resource or
                    // something has gone wrong with the client logic.
                    // Pass on the exception.
                    throw ex;
                } else {
                    // Session seems to no longer be valid.
                    // Reauthenticate and try again.
                    authenticate();
                    reauthenticated = true;
                }
            }
        }
    }

    @Override
    public UserId getUserId() {
        final @Nullable Session session = this.session;
        if (session == null) {
            throw new IllegalStateException("Session is unexpectedly null.");
        }

        return session.getUserId();
    }

    @Override
    public Set<Node> getAllNodes() {
        return makeAuthenticatedApiCall(this.nodeRepository::getAllNodes);
    }

    @Override
    public @Nullable Node getNode(final NodeId nodeId) {
        Objects.requireNonNull(nodeId);

        return makeAuthenticatedApiCall(() -> this.nodeRepository.getNode(nodeId));
    }

    @Override
    public @Nullable Node updateNode(final Node node) {
        Objects.requireNonNull(node);

        return makeAuthenticatedApiCall(() -> this.nodeRepository.updateNode(node));
    }
}
