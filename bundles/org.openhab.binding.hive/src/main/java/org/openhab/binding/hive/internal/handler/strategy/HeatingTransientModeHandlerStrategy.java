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

import javax.measure.quantity.Temperature;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.feature.TransientModeHeatingActionsFeature;

import tec.uom.se.quantity.Quantities;

/**
 * A {@link ThingHandlerStrategy} for handling channels that interface with
 * the "set targetTemperature action" for transient override (boost) of
 * Hive Active Heating zones.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HeatingTransientModeHandlerStrategy extends ThingHandlerStrategyBase {
    private static final HeatingTransientModeHandlerStrategy INSTANCE = new HeatingTransientModeHandlerStrategy();

    public static HeatingTransientModeHandlerStrategy getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode
    ) {
        return useFeatureSafely(hiveNode, TransientModeHeatingActionsFeature.class, transientModeFeature -> {
            if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_TEMPERATURE_TARGET_BOOST)
                    && command instanceof QuantityType
            ) {
                // N.B. Suppress unchecked because openHAB should hopefully only be passing us QuantityType<Temperature>
                final QuantityType<Temperature> newTargetHeatTemperature = (QuantityType<Temperature>) command;
                transientModeFeature.setBoostTargetTemperature(
                        Quantities.getQuantity(
                                newTargetHeatTemperature.toBigDecimal(),
                                newTargetHeatTemperature.getUnit()
                        )
                );

                return true;
            }

            return false;
        });
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final Node hiveNode
    ) {
        useFeatureSafely(hiveNode, TransientModeHeatingActionsFeature.class, transientModeFeature -> {
            useChannelSafely(thing, HiveBindingConstants.CHANNEL_TEMPERATURE_TARGET_BOOST, boostTargetHeatTemperatureChannel -> {
                thingHandlerCallback.stateUpdated(
                        boostTargetHeatTemperatureChannel,
                        new QuantityType<>(
                                transientModeFeature.getBoostTargetTemperature().getValue(),
                                transientModeFeature.getBoostTargetTemperature().getUnit()
                        )
                );
            });
        });
    }
}
