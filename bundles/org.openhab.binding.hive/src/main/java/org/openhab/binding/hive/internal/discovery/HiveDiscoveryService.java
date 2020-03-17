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
package org.openhab.binding.hive.internal.discovery;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.NodeId;
import org.openhab.binding.hive.internal.client.ProductType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public class HiveDiscoveryService extends AbstractDiscoveryService {
    private static final String NODE_ID = "nodeId";

    /**
     * Number of seconds before discovery should timeout.
     *
     * <p>
     *     N.B. Set to 0 because I want to manually mark when discovery is
     *     finished.
     * </p>
     */
    private static final int DISCOVERY_TIMEOUT_SECONDS = 0;

    private final Logger logger = LoggerFactory.getLogger(HiveDiscoveryService.class);

    private final ThingUID bridgeUid;
    private final AtomicReference<Map<NodeId, Node>> lastKnownNodes = new AtomicReference<>(Collections.emptyMap());

    public HiveDiscoveryService(final ThingUID bridgeUid) {
        super(
                HiveBindingConstants.DISCOVERABLE_THING_TYPES_UIDS,
                DISCOVERY_TIMEOUT_SECONDS
        );

        Objects.requireNonNull(bridgeUid);

        this.bridgeUid = bridgeUid;
    }

    public void updateKnownNodes(final Set<Node> knownNodes) {
        Objects.requireNonNull(knownNodes);

        this.scheduler.execute(() -> {
            // Create a map from NodeId -> Node to help with following links later.
            final Map<NodeId, Node> nodeMap = new HashMap<>();
            for (final Node node : knownNodes) {
                nodeMap.put(node.getId(), node);
            }
            this.lastKnownNodes.set(nodeMap);

            // Trigger a new scan with the updated information.
            this.scheduler.execute(() -> this.startScan(null));
        });
    }

    @Override
    protected void startScan() {
        // Get a local copy of nodes to prevent concurrency problems
        final Map<NodeId, Node> nodes = this.lastKnownNodes.get();

        // Go through the set of nodes and report their discovery.
        for (final Node node : nodes.values()) {
            final ThingUID thingUID = new ThingUID("hive:node:" + node.getId().toString());

            // Convert Hive API ProductTypes into ThingTypes.
            final ThingTypeUID thingTypeUID;
            if (node.getProductType().equals(ProductType.BOILER_MODULE)) {
                thingTypeUID = HiveBindingConstants.THING_TYPE_BOILER_MODULE;
            } else if (node.getProductType().equals(ProductType.HEATING)) {
                thingTypeUID = HiveBindingConstants.THING_TYPE_HEATING;
            } else if (node.getProductType().equals(ProductType.HOT_WATER)) {
                thingTypeUID = HiveBindingConstants.THING_TYPE_HOT_WATER;
            } else if (node.getProductType().equals(ProductType.HUB)) {
                thingTypeUID = HiveBindingConstants.THING_TYPE_HUB;
            } else if (node.getProductType().equals(ProductType.THERMOSTAT_UI)) {
                thingTypeUID = HiveBindingConstants.THING_TYPE_THERMOSTAT;
            } else if (node.getProductType().equals(ProductType.TRV)) {
                thingTypeUID = HiveBindingConstants.THING_TYPE_TRV;
            } else if (node.getProductType().equals(ProductType.TRV_GROUP)) {
                thingTypeUID = HiveBindingConstants.THING_TYPE_TRV_GROUP;
            } else {
                logger.trace("Found a node that I cannot handle: {} ({})", node.getName(), node.getId());

                // We do not have a thing type for this yet so skip.
                continue;
            }

            // Do some fiddling with node names to get more descriptive
            // thing labels.
            final String label;
            if (node.getProductType().equals(ProductType.BOILER_MODULE)) {
                label = node.getName().toString() + " (Boiler Module)";
            } else if (node.getProductType().equals(ProductType.HEATING)) {
                // If node is heating node use the parent name because
                // the real name is just a generic Thermostat X.
                final Node parentNode = nodes.get(node.getParentNodeId());

                label = parentNode.getName().toString() + " (Thermostat Heating Zone)";
            } else if (node.getProductType().equals(ProductType.HOT_WATER)) {
                label = "Hot Water";
            } else if (node.getProductType().equals(ProductType.THERMOSTAT_UI)) {
                label = node.getName().toString() + " (Thermostat)";
            } else if (node.getProductType().equals(ProductType.TRV)) {
                label = node.getName().toString() + " (Radiator Valve)";
            } else if (node.getProductType().equals(ProductType.TRV_GROUP)) {
                label = node.getName().toString() + " (Radiator Valve Heating Zone)";
            } else {
                label = node.getName().toString();
            }

            final DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID)
                    .withThingType(thingTypeUID)
                    .withBridge(this.bridgeUid)
                    .withLabel(label)
                    .withProperty(NODE_ID, node.getId().toString())
                    .withRepresentationProperty(NODE_ID)
                    .build();

            thingDiscovered(discoveryResult);
        }

        stopScan();
    }

    @Override
    protected synchronized void stopScan() {
        super.stopScan();
        removeOlderResults(getTimestampOfLastScan());
    }

    @Override
    public void deactivate() {
        super.deactivate();

        // Remove Things from the inbox were discovered by this service.
        removeOlderResults(Instant.now().toEpochMilli(), this.bridgeUid);
    }
}
