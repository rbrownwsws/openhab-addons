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
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.OperatingMode;
import org.openhab.binding.hive.internal.client.OverrideMode;
import org.openhab.binding.hive.internal.client.Temperature;
import org.openhab.binding.hive.internal.client.feature.HeatingThermostatFeature;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HeatingThermostatHandlerStrategy extends ThingHandlerStrategyBase<HeatingThermostatFeature> {
    private static final HeatingThermostatHandlerStrategy INSTANCE = new HeatingThermostatHandlerStrategy();

    public static HeatingThermostatHandlerStrategy getInstance() {
        return INSTANCE;
    }

    public HeatingThermostatHandlerStrategy() {
        super(HeatingThermostatFeature.class);
    }

    @Override
    public boolean handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode,
            final HeatingThermostatFeature heatingThermostatFeature
    ) {
        boolean needUpdate = false;

        if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_TEMPERATURE_TARGET)
                && command instanceof QuantityType
        ) {
            final QuantityType<?> newTargetHeatTemperature = (QuantityType<?>) command;
            heatingThermostatFeature.setTargetHeatTemperature(new Temperature(newTargetHeatTemperature.toBigDecimal()));

            needUpdate = true;
        } else if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_MODE_OPERATING)
                && command instanceof StringType
        ) {
            final StringType newOperatingMode = (StringType) command;
            heatingThermostatFeature.setOperatingMode(OperatingMode.valueOf(newOperatingMode.toString()));

            needUpdate = true;
        } else if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_MODE_OPERATING_OVERRIDE)
                && command instanceof OnOffType
        ) {
            final OnOffType newOverrideMode = (OnOffType) command;
            heatingThermostatFeature.setTemporaryOperatingModeOverride(newOverrideMode == OnOffType.ON ? OverrideMode.TRANSIENT : OverrideMode.NONE);

            needUpdate = true;
        }

        return needUpdate;
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final HeatingThermostatFeature heatingThermostatFeature
    ) {
        final ChannelUID operatingModeChannel = thing.getChannel(HiveBindingConstants.CHANNEL_MODE_OPERATING).getUID();
        thingHandlerCallback.stateUpdated(operatingModeChannel, new StringType(heatingThermostatFeature.getOperatingMode().toString()));

        final ChannelUID operatingStateChannel = thing.getChannel(HiveBindingConstants.CHANNEL_STATE_OPERATING).getUID();
        thingHandlerCallback.stateUpdated(operatingStateChannel, new StringType(heatingThermostatFeature.getOperatingState()));

        // FIXME: Actually check temperature unit.
        final ChannelUID targetHeatTemperatureChannel = thing.getChannel(HiveBindingConstants.CHANNEL_TEMPERATURE_TARGET).getUID();
        thingHandlerCallback.stateUpdated(targetHeatTemperatureChannel, new QuantityType<>(heatingThermostatFeature.getTargetHeatTemperature().getValue(), SIUnits.CELSIUS));

        final ChannelUID operatingModeOverrideChannel = thing.getChannel(HiveBindingConstants.CHANNEL_MODE_OPERATING_OVERRIDE).getUID();
        thingHandlerCallback.stateUpdated(operatingModeOverrideChannel, OnOffType.from(heatingThermostatFeature.getTemporaryOperatingModeOverride() == OverrideMode.TRANSIENT));
    }
}
