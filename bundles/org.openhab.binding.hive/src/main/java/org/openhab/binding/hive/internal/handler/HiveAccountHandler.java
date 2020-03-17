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
package org.openhab.binding.hive.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.hive.internal.HiveAccountConfig;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.*;
import org.openhab.binding.hive.internal.client.exception.HiveApiAuthenticationException;
import org.openhab.binding.hive.internal.client.exception.HiveApiException;
import org.openhab.binding.hive.internal.discovery.HiveDiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The {@link HiveAccountHandler} is responsible for handling interaction with
 * the Hive API.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HiveAccountHandler extends BaseBridgeHandler {
    private final Logger logger = LoggerFactory.getLogger(HiveAccountHandler.class);

    private final Map<NodeId, HiveHandlerBase> hiveHandlers = new HashMap<>();
    private final ReentrantLock accountStateLock = new ReentrantLock();

    private final HiveDiscoveryService discoveryService;

    private @Nullable ScheduledFuture<?> pollingJob = null;

    private @Nullable HiveClient hiveClient = null;

    /**
     * @param bridge
     * @see BaseThingHandler
     */
    public HiveAccountHandler(final Bridge bridge) {
        super(bridge);

        this.discoveryService = new HiveDiscoveryService(this.getThing().getUID());
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.UNKNOWN, ThingStatusDetail.CONFIGURATION_PENDING);

        scheduler.execute(() -> {
            // The the openHAB runtime should ensure none of this are null.
            // Do a little dance to make the null checker happy.
            final HiveAccountConfig config = getConfigAs(HiveAccountConfig.class);
            assert config != null;
            final String username = config.username;
            final String password = config.password;
            final int pollingInterval = config.pollingInterval;
            assert username != null;
            assert password != null;

            try {
                this.hiveClient = HiveClientFactory.newClient(
                        new Username(username),
                        new Password(password)
                );

                this.pollingJob = this.scheduler.scheduleWithFixedDelay(
                        this::poll,
                        0,
                        Math.max(pollingInterval, 1),
                        TimeUnit.SECONDS
                );

                updateStatus(ThingStatus.ONLINE);
            } catch (final HiveApiAuthenticationException ex) {
                updateStatus(
                        ThingStatus.OFFLINE,
                        ThingStatusDetail.CONFIGURATION_ERROR,
                        "Username and Password are not correct."
                );
            } catch (final HiveApiException ex) {
                updateStatus(
                        ThingStatus.OFFLINE,
                        ThingStatusDetail.COMMUNICATION_ERROR,
                        "Something went wrong communicating with the Hive Api. Details: " + ex.getMessage()
                );
            } catch (final RuntimeException ex) {
                updateStatus(
                        ThingStatus.OFFLINE,
                        ThingStatusDetail.NONE,
                        "Something went very wrong. Details: " + ex.getMessage()
                );
            }
        });
    }

    @Override
    public void handleCommand(final ChannelUID channelUID, final Command command) {
        // Do nothing.
    }

    @Override
    public void dispose() {
        final ScheduledFuture<?> pollingJob = this.pollingJob;
        if (pollingJob != null) {
            this.pollingJob.cancel(true);
        }

        super.dispose();
    }

    public DiscoveryService getDiscoveryService() {
        return this.discoveryService;
    }

    public void addHiveHandler(final HiveHandlerBase hiveHandler) {
        Objects.requireNonNull(hiveHandler);

        final NodeId nodeId = getNodeIdFromHiveHandler(hiveHandler);
        this.hiveHandlers.put(nodeId, hiveHandler);
    }

    public void removeHiveHandler(final HiveHandlerBase nodeHandler) {
        Objects.requireNonNull(nodeHandler);

        final NodeId nodeId = getNodeIdFromHiveHandler(nodeHandler);
        this.hiveHandlers.remove(nodeId);
    }

    public void updateNode(final Node node) {
        Objects.requireNonNull(node);

        final HiveClient hiveClient = this.hiveClient;

        if (hiveClient == null) {
            throw new IllegalStateException("UpdateNode called before HiveClient has been initialised.");
        }

        final @Nullable Node updatedNode = hiveClient.updateNode(node);

        /* Send updated node state to handlers */
        final @Nullable HiveHandlerBase hiveHandler = hiveHandlers.get(node.getId());

        // Give the handler an updated version of the node.
        if (hiveHandler != null && updatedNode != null) {
            hiveHandler.updateState(updatedNode);
        }
    }

    public ReentrantLock getAccountStateLock() {
        return accountStateLock;
    }

    private void poll() {
        this.accountStateLock.lock();
        try {
            final HiveClient hiveClient = this.hiveClient;
            if (hiveClient == null) {
                throw new IllegalStateException("Poll called before HiveClient has been initialised.");
            }

            // Get all nodes from the Hive API.
            final Set<Node> nodes = hiveClient.getAllNodes();

            // Tell the discovery service about the nodes we found.
            this.discoveryService.updateKnownNodes(nodes);

            /* Send updated node state to handlers */
            for (final Node node : nodes) {
                // Try to find a handler responsible for this node.
                final @Nullable HiveHandlerBase hiveHandler = hiveHandlers.get(node.getId());

                // Give the handler an updated version of the node.
                if (hiveHandler != null) {
                    hiveHandler.updateState(node);
                }
            }
        } catch (final Exception ex) {
            logger.debug("Polling failed with exception", ex);
        } finally {
            this.accountStateLock.unlock();
        }
    }

    private NodeId getNodeIdFromHiveHandler(final ThingHandler handler) {
        Objects.requireNonNull(handler);

        return new NodeId((String) handler.getThing().getConfiguration().get(HiveBindingConstants.CONFIG_NODE_ID));
    }
}
