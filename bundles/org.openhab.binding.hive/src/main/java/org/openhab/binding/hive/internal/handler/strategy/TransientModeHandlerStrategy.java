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
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.feature.TransientModeFeature;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class TransientModeHandlerStrategy extends ThingHandlerStrategyBase<TransientModeFeature> {
    private static final int SECONDS_PER_MINUTE = 60;

    private static final TransientModeHandlerStrategy INSTANCE = new TransientModeHandlerStrategy();

    public static TransientModeHandlerStrategy getInstance() {
        return INSTANCE;
    }

    private TransientModeHandlerStrategy() {
        super(TransientModeFeature.class);
    }

    @Override
    public boolean handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode,
            final TransientModeFeature transientModeFeature
    ) {
        if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_TRANSIENT_DURATION)
                && command instanceof DecimalType
        ) {
            final DecimalType newDurationMins = (DecimalType) command;

            transientModeFeature.setDuration(Duration.ofSeconds(newDurationMins.longValue() * SECONDS_PER_MINUTE));

            return true;
        }

        return false;
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final TransientModeFeature transientModeFeature
    ) {
        final long durationMins = transientModeFeature.getDuration().getSeconds() / SECONDS_PER_MINUTE;
        final ChannelUID transientDurationChannel = thing.getChannel(HiveBindingConstants.CHANNEL_TRANSIENT_DURATION).getUID();
        thingHandlerCallback.stateUpdated(transientDurationChannel, new DecimalType(durationMins));

        final ChannelUID transientEnabledChannel = thing.getChannel(HiveBindingConstants.CHANNEL_TRANSIENT_ENABLED).getUID();
        thingHandlerCallback.stateUpdated(transientEnabledChannel, OnOffType.from(transientModeFeature.getIsEnabled()));

        final ChannelUID transientStartTimeChannel = thing.getChannel(HiveBindingConstants.CHANNEL_TRANSIENT_START_TIME).getUID();
        thingHandlerCallback.stateUpdated(transientStartTimeChannel, new DateTimeType(transientModeFeature.getStartDatetime()));

        final ChannelUID transientEndTimeChannel = thing.getChannel(HiveBindingConstants.CHANNEL_TRANSIENT_END_TIME).getUID();
        thingHandlerCallback.stateUpdated(transientEndTimeChannel, new DateTimeType(transientModeFeature.getEndDatetime()));

        final ChannelUID transientRemainingChannel = thing.getChannel(HiveBindingConstants.CHANNEL_TRANSIENT_REMAINING).getUID();
        thingHandlerCallback.stateUpdated(transientRemainingChannel, new DecimalType(Math.max(0, Instant.now().until(transientModeFeature.getEndDatetime(), ChronoUnit.MINUTES))));
    }
}
