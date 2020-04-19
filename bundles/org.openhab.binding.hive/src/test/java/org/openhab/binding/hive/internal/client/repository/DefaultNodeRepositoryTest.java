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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.net.URI;
import java.util.Collections;
import java.util.UUID;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openhab.binding.hive.internal.TestUtil;
import org.openhab.binding.hive.internal.client.*;
import org.openhab.binding.hive.internal.client.dto.*;
import org.openhab.binding.hive.internal.client.feature.LinksFeature;

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


    @Before
    public void setUp() {
        initMocks(this);
    }

    private void setUpSuccessResponse(final NodesDto content) {
        when(this.requestFactory.newRequest(any())).thenReturn(this.request);

        when(this.request.accept(any())).thenReturn(this.request);
        when(this.request.get()).thenReturn(this.response);

        when(this.response.getStatusCode()).thenReturn(200);

        when(this.response.getContent(eq(NodesDto.class))).thenReturn(content);
    }

    @Test
    public void testGetNode() {
        /* Given */
        final NodesDto nodesDto = new NodesDto();
        nodesDto.nodes = Collections.emptyList();

        this.setUpSuccessResponse(nodesDto);

        final NodeId nodeId = new NodeId(UUID.fromString(TestUtil.UUID_DEADBEEF));

        final DefaultNodeRepository nodeRepository = new DefaultNodeRepository(this.requestFactory);


        /* When */
        nodeRepository.getNode(nodeId);


        /* Then */
        verify(this.requestFactory, times(1)).newRequest(URI.create("nodes/" + TestUtil.UUID_DEADBEEF));
    }

    @Test
    public void testLinksFeatureNoLinks() {
        /* Given */
        final ReverseLinkDto reverseLinkDto = new ReverseLinkDto();
        reverseLinkDto.boundNode = TestUtil.NODE_ID_CAFEBABE;
        reverseLinkDto.bindingGroupIds = Collections.singleton(GroupId.TRVBM);

        final NodeDto nodeDto = TestUtil.createSimpleNodeDto(TestUtil.NODE_ID_DEADBEEF);
        nodeDto.features.links_v1 = new LinksV1FeatureDto();
        nodeDto.features.links_v1.reverseLinks = TestUtil.createSimpleFeatureAttributeDto(Collections.singleton(reverseLinkDto));

        final NodesDto nodesDto = new NodesDto();
        nodesDto.nodes = Collections.singletonList(nodeDto);

        this.setUpSuccessResponse(nodesDto);

        final DefaultNodeRepository nodeRepository = new DefaultNodeRepository(this.requestFactory);


        /* When */
        final @Nullable Node linksNode = nodeRepository.getNode(TestUtil.NODE_ID_DEADBEEF);


        /* Then */
        // No exceptions hopefully!
        assertThat(linksNode).isNotNull();

        final @Nullable LinksFeature linksFeature = linksNode.getFeature(LinksFeature.class);
        assertThat(linksFeature).isNotNull();

        assertThat(linksFeature.getLinksAttribute()).isNull();
        assertThat(linksFeature.getReverseLinksAttribute()).isNotNull();

        // TODO: verify more
    }

    @Test
    public void testLinksFeatureNoReverseLinks() {
        /* Given */
        final LinkDto linkDto = new LinkDto();
        linkDto.boundNode = TestUtil.NODE_ID_CAFEBABE;
        linkDto.bindingGroupId = GroupId.TRVBM;

        final NodeDto nodeDto = TestUtil.createSimpleNodeDto(TestUtil.NODE_ID_DEADBEEF);
        nodeDto.features.links_v1 = new LinksV1FeatureDto();
        nodeDto.features.links_v1.links = TestUtil.createSimpleFeatureAttributeDto(Collections.singleton(linkDto));

        final NodesDto nodesDto = new NodesDto();
        nodesDto.nodes = Collections.singletonList(nodeDto);

        this.setUpSuccessResponse(nodesDto);

        final DefaultNodeRepository nodeRepository = new DefaultNodeRepository(this.requestFactory);


        /* When */
        final @Nullable Node linksNode = nodeRepository.getNode(TestUtil.NODE_ID_DEADBEEF);


        /* Then */
        // No exceptions hopefully!
        assertThat(linksNode).isNotNull();

        final @Nullable LinksFeature linksFeature = linksNode.getFeature(LinksFeature.class);
        assertThat(linksFeature).isNotNull();

        assertThat(linksFeature.getLinksAttribute()).isNotNull();
        assertThat(linksFeature.getReverseLinksAttribute()).isNull();

        // TODO: verify more
    }
}
