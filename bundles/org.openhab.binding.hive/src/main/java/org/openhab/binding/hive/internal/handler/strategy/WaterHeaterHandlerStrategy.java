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
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.OperatingMode;
import org.openhab.binding.hive.internal.client.OverrideMode;
import org.openhab.binding.hive.internal.client.feature.WaterHeaterFeature;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class WaterHeaterHandlerStrategy extends ThingHandlerStrategyBase<WaterHeaterFeature> {
    private static final WaterHeaterHandlerStrategy INSTANCE = new WaterHeaterHandlerStrategy();

    public static WaterHeaterHandlerStrategy getInstance() {
        return INSTANCE;
    }

    public WaterHeaterHandlerStrategy() {
        super(WaterHeaterFeature.class);
    }

    @Override
    public boolean handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode,
            final WaterHeaterFeature waterHeaterFeature
    ) {
        boolean needUpdate = false;

        if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_MODE_OPERATING)
                && command instanceof StringType
        ) {
            final StringType newOperatingMode = (StringType) command;

            waterHeaterFeature.setOperatingMode(OperatingMode.valueOf(newOperatingMode.toString()));

            needUpdate = true;
        } else if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_MODE_OPERATING_OVERRIDE)
                && command instanceof OnOffType
        ) {
            final OnOffType newOverrideMode = (OnOffType) command;
            waterHeaterFeature.setTemporaryOperatingModeOverride(newOverrideMode == OnOffType.ON ? OverrideMode.TRANSIENT : OverrideMode.NONE);

            needUpdate = true;
        }

        return needUpdate;
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final WaterHeaterFeature waterHeaterFeature
    ) {
        final ChannelUID operatingModeChannel = thing.getChannel(HiveBindingConstants.CHANNEL_MODE_OPERATING).getUID();
        thingHandlerCallback.stateUpdated(operatingModeChannel, new StringType(waterHeaterFeature.getOperatingMode().toString()));

        final ChannelUID isOnChannel = thing.getChannel(HiveBindingConstants.CHANNEL_IS_ON).getUID();
        thingHandlerCallback.stateUpdated(isOnChannel, OnOffType.from(waterHeaterFeature.isOn()));

        final ChannelUID operatingModeOverrideChannel = thing.getChannel(HiveBindingConstants.CHANNEL_MODE_OPERATING_OVERRIDE).getUID();
        thingHandlerCallback.stateUpdated(operatingModeOverrideChannel, OnOffType.from(waterHeaterFeature.getTemporaryOperatingModeOverride() == OverrideMode.TRANSIENT));
    }
}
