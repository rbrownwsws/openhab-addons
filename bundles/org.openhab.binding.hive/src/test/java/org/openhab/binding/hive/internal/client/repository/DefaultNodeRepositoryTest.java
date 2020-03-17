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
package org.openhab.binding.hive.internal.client.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.net.URI;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openhab.binding.hive.internal.client.*;
import org.openhab.binding.hive.internal.client.dto.NodesDto;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public class DefaultNodeRepositoryTest {
    @NonNullByDefault({})
    @Mock
    private HiveApiRequestFactory requestFactory;

    @NonNullByDefault({})
    @Mock
    private HiveApiRequest request;

    @NonNullByDefault({})
    @Mock
    private HiveApiResponse response;

    @NonNullByDefault({})
    private NodesDto nodesDto;

    @Before
    public void setUp() {
        initMocks(this);

        this.nodesDto = new NodesDto();
        this.nodesDto.nodes = Collections.emptyList();
    }

    @Test
    public void testGetNode() {
        /* Given */
        when(this.requestFactory.newRequest(any())).thenReturn(this.request);

        when(this.request.accept(any())).thenReturn(this.request);
        when(this.request.get()).thenReturn(this.response);

        when(this.response.getStatusCode()).thenReturn(200);
        when(this.response.getContent(any())).thenReturn(this.nodesDto);

        final NodeId nodeId = new NodeId("deadbeef-dead-beef-dead-beefdeadbeef");

        final DefaultNodeRepository nodeRepository = new DefaultNodeRepository(this.requestFactory);


        /* When */
        nodeRepository.getNode(nodeId);


        /* Then */
        verify(this.requestFactory, times(1)).newRequest(URI.create("nodes/deadbeef-dead-beef-dead-beefdeadbeef"));
    }
}
