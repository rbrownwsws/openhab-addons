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

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.OverrideMode;
import org.openhab.binding.hive.internal.client.feature.HeatingThermostatFeature;
import org.openhab.binding.hive.internal.client.feature.TransientModeFeature;
import org.openhab.binding.hive.internal.client.feature.WaterHeaterFeature;

/**
 * A {@link ThingHandlerStrategy} that handles the "transient-remaining"
 * channel.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class BoostTimeRemainingHandlerStrategy extends ThingHandlerStrategyBase  {
    private static final BoostTimeRemainingHandlerStrategy INSTANCE = new BoostTimeRemainingHandlerStrategy();

    public static BoostTimeRemainingHandlerStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final Node hiveNode
    ) {
        useFeatureSafely(hiveNode, TransientModeFeature.class, transientModeFeature -> {
            long minutesRemaining = Instant.now().until(transientModeFeature.getEndDatetime(), ChronoUnit.MINUTES);
            minutesRemaining = Math.max(0, minutesRemaining);

            // If we know the transient mode has been cancelled set remaining
            // time to 0
            final @Nullable HeatingThermostatFeature heatingThermostatFeature = hiveNode.getFeature(HeatingThermostatFeature.class);
            final @Nullable WaterHeaterFeature waterHeaterFeature = hiveNode.getFeature(WaterHeaterFeature.class);
            if ((heatingThermostatFeature != null && heatingThermostatFeature.getTemporaryOperatingModeOverride() == OverrideMode.NONE)
                    || (waterHeaterFeature != null && waterHeaterFeature.getTemporaryOperatingModeOverride() == OverrideMode.NONE)
            ) {
                minutesRemaining = 0;
            }

            long finalMinutesRemaining = minutesRemaining;
            useChannelSafely(thing, HiveBindingConstants.CHANNEL_TRANSIENT_REMAINING, transientRemainingChannel -> {
                thingHandlerCallback.stateUpdated(transientRemainingChannel, new DecimalType(finalMinutesRemaining));
            });
        });
    }
}
