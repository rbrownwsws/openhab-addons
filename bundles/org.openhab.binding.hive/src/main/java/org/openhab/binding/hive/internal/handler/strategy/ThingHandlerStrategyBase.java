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
package org.openhab.binding.hive.internal.handler.strategy;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.feature.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public abstract class ThingHandlerStrategyBase<F extends Feature> implements ThingHandlerStrategy {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Class<F> featureClass;

    public ThingHandlerStrategyBase(final Class<F> featureClass) {
        Objects.requireNonNull(featureClass);

        this.featureClass = featureClass;
    }

    @Override
    public final boolean handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode
    ) {
        final @Nullable F feature = hiveNode.getFeature(featureClass);

        if (feature == null) {
            logger.warn("Could not get {} for {} ({}). Has it been given the wrong kind of handler?", featureClass.getName(), hiveNode.getName(), hiveNode.getId());
            return false;
        }

        return handleCommand(
                channelUID,
                command,
                hiveNode,
                feature
        );
    }

    @Override
    public final void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final Node hiveNode
    ) {
        final @Nullable F feature = hiveNode.getFeature(featureClass);

        if (feature == null) {
            logger.warn("Could not get {} for {} ({}). Has it been given the wrong kind of handler?", featureClass.getName(), hiveNode.getName(), hiveNode.getId());
            return;
        }

        handleUpdate(
                thing,
                thingHandlerCallback,
                feature
        );
    }

    public boolean handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode,
            final F feature
    ) {
        return false;
    }

    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final F feature
    ) {
        // Do nothing.
    }
}
