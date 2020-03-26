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

import java.util.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.feature.Feature;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class DefaultNode implements Node {
    private final NodeId id;
    private final NodeName name;
    private final NodeType nodeType;
    private final ProductType productType;
    private final Protocol protocol;
    private final NodeId parentNodeId;
    private final Map<Class<? extends Feature>, Feature> features;

    public DefaultNode(
            final NodeId id,
            final NodeName name,
            final NodeType nodeType,
            final ProductType productType,
            final Protocol protocol,
            final NodeId parentNodeId,
            final Map<Class<? extends Feature>, Feature> features
    ) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(nodeType);
        Objects.requireNonNull(productType);
        Objects.requireNonNull(protocol);
        Objects.requireNonNull(parentNodeId);
        Objects.requireNonNull(features);

        this.id = id;
        this.name = name;
        this.nodeType = nodeType;
        this.productType = productType;
        this.protocol = protocol;
        this.parentNodeId = parentNodeId;
        this.features = features;
    }

    @Override
    public NodeId getId() {
        return this.id;
    }

    @Override
    public NodeName getName() {
        return this.name;
    }

    @Override
    public NodeType getNodeType() {
        return this.nodeType;
    }

    @Override
    public ProductType getProductType() {
        return this.productType;
    }

    @Override
    public Protocol getProtocol() {
        return this.protocol;
    }

    @Override
    public NodeId getParentNodeId() {
        return this.parentNodeId;
    }

    @Override
    public Set<Feature> getFeatures() {
        return Collections.unmodifiableSet(new HashSet<>(this.features.values()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Feature> @Nullable T getFeature(final Class<T> classOfFeature) {
        // N.B. "Unchecked cast" warning suppressed because we should be
        //      ensuring that the class of the value matches the key
        //      when they are put in the map.
        return (T) this.features.get(classOfFeature);
    }
}
