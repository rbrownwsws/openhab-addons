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

import java.util.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.*;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.handler.strategy.ThingHandlerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A base {@link org.eclipse.smarthome.core.thing.binding.ThingHandler} for
 * Hive devices.
 *
 * <p>
 *     Delegates most functionality to a provided set of
 *     {@link ThingHandlerStrategy}s.
 * </p>
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
abstract class HiveHandlerBase extends BaseThingHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Set<ThingHandlerStrategy> handlerStrategies;

    private @Nullable Node hiveNode = null;

    public HiveHandlerBase(final Thing thing, final Set<ThingHandlerStrategy> handlerStrategies) {
        super(thing);

        this.handlerStrategies = Collections.unmodifiableSet(new HashSet<>(handlerStrategies));
    }

    @Override
    public void initialize() {
        final @Nullable HiveAccountHandler accountHandler = getAccountHandler();

        if (accountHandler != null) {
            accountHandler.addHiveHandler(this);
            updateStatus(ThingStatus.ONLINE);
        }
    }

    @Override
    public final void handleCommand(final ChannelUID channelUID, final Command command) {
        final HiveAccountHandler accountHandler = getAccountHandler();
        if (accountHandler == null) {
            return;
        }

        accountHandler.getAccountStateLock().lock();
        try {
            final Node hiveNode = this.hiveNode;
            if (hiveNode == null) {
                return;
            }

            boolean commandHandled = false;
            final Iterator<ThingHandlerStrategy> strategyIterator = handlerStrategies.iterator();
            while (strategyIterator.hasNext() && !commandHandled) {
                final @Nullable ThingHandlerStrategy strategy = strategyIterator.next();
                assert strategy != null;

                commandHandled = strategy.handleCommand(channelUID, command, hiveNode);
            }

            if (commandHandled) {
                accountHandler.updateNode(hiveNode);
            }
        } finally {
            accountHandler.getAccountStateLock().unlock();
        }
    }

    @Override
    public void dispose() {
        // Clean up the HiveAccountHandler reference to this handler.
        final @Nullable HiveAccountHandler accountHandler = getAccountHandler();
        if (accountHandler != null) {
            accountHandler.removeHiveHandler(this);
        }

        super.dispose();
    }

    public final void updateState(final Node hiveNode) {
        Objects.requireNonNull(hiveNode);

        // Save the node for use by handleCommand.
        this.hiveNode = hiveNode;

        // Update channel states using the handler strategies.
        final @Nullable ThingHandlerCallback thingHandlerCallback = this.getCallback();
        if (thingHandlerCallback != null) {
            for (final ThingHandlerStrategy strategy : this.handlerStrategies) {
                strategy.handleUpdate(this.getThing(), thingHandlerCallback, hiveNode);
            }
        }
    }

    /**
     * Get the HiveAccountHandler that is acting as a BridgeHandler for this thing.
     *
     * @return
     *      {@code null} If the bridge has not been set.
     */
    protected final @Nullable HiveAccountHandler getAccountHandler() {
        final @Nullable Bridge bridge = getBridge();
        if (bridge == null) {
            logger.debug("getAccountHandler() called but bridge is null.  Has something gone wrong?  Setting status to offline.");
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_UNINITIALIZED);
            return null;
        } else {
            return (HiveAccountHandler) bridge.getHandler();
        }
    }
}
