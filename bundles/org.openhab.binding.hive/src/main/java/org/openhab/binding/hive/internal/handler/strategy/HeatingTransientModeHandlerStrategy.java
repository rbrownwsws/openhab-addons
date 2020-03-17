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
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.Temperature;
import org.openhab.binding.hive.internal.client.feature.HeatingTransientModeFeature;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HeatingTransientModeHandlerStrategy extends ThingHandlerStrategyBase<HeatingTransientModeFeature>  {
    private static final HeatingTransientModeHandlerStrategy INSTANCE = new HeatingTransientModeHandlerStrategy();

    public static HeatingTransientModeHandlerStrategy getInstance() {
        return INSTANCE;
    }

    private HeatingTransientModeHandlerStrategy() {
        super(HeatingTransientModeFeature.class);
    }

    @Override
    public boolean handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode,
            final HeatingTransientModeFeature transientModeFeature
    ) {
        boolean needUpdate = TransientModeHandlerStrategy.getInstance().handleCommand(channelUID, command, hiveNode, transientModeFeature);

        if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_TEMPERATURE_TARGET_BOOST)
                && command instanceof QuantityType
        ) {
            final QuantityType<?> newTargetHeatTemperature = (QuantityType<?>) command;
            transientModeFeature.setBoostTargetTemperature(new Temperature(newTargetHeatTemperature.toBigDecimal()));

            needUpdate = true;
        }

        return needUpdate;
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final HeatingTransientModeFeature transientModeFeature
    ) {
        TransientModeHandlerStrategy.getInstance().handleUpdate(thing, thingHandlerCallback, transientModeFeature);

        // FIXME: Actually check temperature unit.
        final ChannelUID boostTargetHeatTemperatureChannel = thing.getChannel(HiveBindingConstants.CHANNEL_TEMPERATURE_TARGET_BOOST).getUID();
        thingHandlerCallback.stateUpdated(boostTargetHeatTemperatureChannel, new QuantityType<>(transientModeFeature.getBoostTargetTemperature().getValue(), SIUnits.CELSIUS));
    }
}
