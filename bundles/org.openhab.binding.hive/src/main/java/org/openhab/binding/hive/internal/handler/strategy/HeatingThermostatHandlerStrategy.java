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
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.HeatingThermostatOperatingMode;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.OverrideMode;
import org.openhab.binding.hive.internal.client.feature.HeatingThermostatFeature;

import tec.uom.se.quantity.Quantities;

/**
 * A {@link ThingHandlerStrategy} for handling
 * {@link HeatingThermostatFeature}.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HeatingThermostatHandlerStrategy extends ThingHandlerStrategyBase {
    @SuppressWarnings("unchecked")
    @Override
    public @Nullable Node handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode
    ) {
        return useFeatureSafely(hiveNode, HeatingThermostatFeature.class, heatingThermostatFeature -> {
            @Nullable HeatingThermostatFeature newHeatingThermostatFeature = null;

            if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_TEMPERATURE_TARGET)
                    && command instanceof QuantityType
            ) {
                // N.B. Suppress unchecked because openHAB should hopefully only be passing us QuantityType<Temperature>
                final QuantityType<Temperature> newTargetHeatTemperature = (QuantityType<Temperature>) command;

                newHeatingThermostatFeature = heatingThermostatFeature.withTargetTargetHeatTemperature(
                        Quantities.getQuantity(
                                newTargetHeatTemperature.toBigDecimal(),
                                newTargetHeatTemperature.getUnit()
                        )
                );
            } else if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_MODE_OPERATING)
                    && command instanceof StringType
            ) {
                final StringType newOperatingMode = (StringType) command;
                newHeatingThermostatFeature = heatingThermostatFeature.withTargetOperatingMode(
                        HeatingThermostatOperatingMode.valueOf(newOperatingMode.toString())
                );
            } else if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_MODE_OPERATING_OVERRIDE)
                    && command instanceof OnOffType
            ) {
                final OnOffType newOverrideMode = (OnOffType) command;
                heatingThermostatFeature.withTargetTemporaryOperatingModeOverride(
                        newOverrideMode == OnOffType.ON ? OverrideMode.TRANSIENT : OverrideMode.NONE
                );
            }

            if (newHeatingThermostatFeature != null) {
                final Node.Builder nodeBuilder = Node.builder();
                nodeBuilder.from(hiveNode);

                nodeBuilder.putFeature(HeatingThermostatFeature.class, newHeatingThermostatFeature);

                return nodeBuilder.build();
            } else {
                return null;
            }
        });
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final Node hiveNode
    ) {
        useFeatureSafely(hiveNode, HeatingThermostatFeature.class, heatingThermostatFeature -> {
            useChannelSafely(thing, HiveBindingConstants.CHANNEL_MODE_OPERATING, operatingModeChannel -> {
                thingHandlerCallback.stateUpdated(operatingModeChannel, new StringType(heatingThermostatFeature.getOperatingMode().getDisplayValue().toString()));
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_STATE_OPERATING, operatingStateChannel -> {
                thingHandlerCallback.stateUpdated(operatingStateChannel, new StringType(heatingThermostatFeature.getOperatingState().getDisplayValue().toString()));
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_TEMPERATURE_TARGET, targetHeatTemperatureChannel -> {
                thingHandlerCallback.stateUpdated(
                        targetHeatTemperatureChannel,
                        new QuantityType<>(
                                heatingThermostatFeature.getTargetHeatTemperature().getDisplayValue().getValue(),
                                heatingThermostatFeature.getTargetHeatTemperature().getDisplayValue().getUnit()
                        )
                );
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_MODE_OPERATING_OVERRIDE, operatingModeOverrideChannel -> {
                thingHandlerCallback.stateUpdated(operatingModeOverrideChannel, OnOffType.from(heatingThermostatFeature.getTemporaryOperatingModeOverride().getDisplayValue() == OverrideMode.TRANSIENT));
            });
        });
    }
}
