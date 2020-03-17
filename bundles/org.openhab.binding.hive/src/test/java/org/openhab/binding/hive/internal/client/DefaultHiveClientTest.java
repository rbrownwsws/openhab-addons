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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openhab.binding.hive.internal.client.exception.*;
import org.openhab.binding.hive.internal.client.repository.*;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public class DefaultHiveClientTest {
    @NonNullByDefault({})
    @Mock
    private SessionAuthenticationManager authenticationManager;

    @NonNullByDefault({})
    @Mock
    private SessionRepository sessionRepository;

    @NonNullByDefault({})
    @Mock
    private NodeRepository nodeRepository;

    @NonNullByDefault({})
    private Username username;

    @NonNullByDefault({})
    private Password password;

    @NonNullByDefault({})
    private Session session;

    @Before
    public void setUp() {
        initMocks(this);
        this.username = new Username("hiveuser@example.com");
        this.password = new Password("password123");
        this.session = new Session(
                new SessionId("deadbeef-dead-beef-dead-beefdeadbeef"),
                new UserId("deadbeef-dead-beef-dead-beefdeadbeef")
        );
    }

    private DefaultHiveClient createClient() {
        return new DefaultHiveClient(
                this.authenticationManager,
                this.username,
                this.password,
                this.sessionRepository,
                this.nodeRepository
        );
    }

    /**
     * Test that DefaultHiveClient tries to authenticate when it is created.
     */
    @Test
    public void testGoodLogin() {
        /* Given */
        when(this.sessionRepository.createSession(any(), any())).thenReturn(this.session);


        /* When */
        final DefaultHiveClient hiveClient = createClient();


        /* Then */
        verify(this.sessionRepository, times(1)).createSession(eq(this.username), eq(this.password));
        verify(this.authenticationManager, times(1)).setSession(eq(this.session));
    }

    /**
     * Test that DefaultHiveClient passes on authentication exceptions.
     */
    @Test(expected = HiveApiAuthenticationException.class)
    public void testBadCredentialsLogin() {
        /* Given */
        when(this.sessionRepository.createSession(any(), any())).thenThrow(new HiveApiAuthenticationException());


        /* When / Then */
        // This should throw HiveApiAuthenticationException.
        // No assertThrows in this version of jUnit :(
        final DefaultHiveClient hiveClient = createClient();
    }

    /**
     * Test that DefaultHiveClient passes on exception when API does something unexpected.
     */
    @Test(expected = HiveApiUnknownException.class)
    public void testApiErrorLogin() {
        /* Given */
        when(this.sessionRepository.createSession(any(), any())).thenThrow(new HiveApiUnknownException());


        /* When / Then */
        // This should throw HiveApiUnknownException.
        // No assertThrows in this version of jUnit :(
        final DefaultHiveClient hiveClient = createClient();
    }

    /**
     * Test the DefaultHiveClient passes on the nodes from the NodeRepository.
     */
    @Test
    public void testGetNodes() {
        /* Given */
        final Node expectedNode = mock(Node.class);
        final Set<Node> expectedResults = Collections.unmodifiableSet(Collections.singleton(expectedNode));
        when(this.nodeRepository.getAllNodes()).thenReturn(expectedResults);

        DefaultHiveClient hiveClient = createClient();


        /* When */
        Set<Node> returnedResults = hiveClient.getAllNodes();


        /* Then */
        assertThat(returnedResults).containsExactly(expectedNode);
    }
}
