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
import org.openhab.binding.hive.internal.client.OverrideMode;
import org.openhab.binding.hive.internal.client.WaterHeaterOperatingMode;
import org.openhab.binding.hive.internal.client.feature.WaterHeaterFeature;

/**
 * A {@link ThingHandlerStrategy} for handling
 * {@link WaterHeaterFeature}.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class WaterHeaterHandlerStrategy extends ThingHandlerStrategyBase {
    private static final WaterHeaterHandlerStrategy INSTANCE = new WaterHeaterHandlerStrategy();

    public static WaterHeaterHandlerStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode
    ) {
        return useFeatureSafely(hiveNode, WaterHeaterFeature.class, waterHeaterFeature -> {
            boolean needUpdate = false;

            if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_MODE_OPERATING)
                    && command instanceof StringType
            ) {
                final StringType newOperatingMode = (StringType) command;

                waterHeaterFeature.setOperatingMode(WaterHeaterOperatingMode.valueOf(newOperatingMode.toString()));

                needUpdate = true;
            } else if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_MODE_OPERATING_OVERRIDE)
                    && command instanceof OnOffType
            ) {
                final OnOffType newOverrideMode = (OnOffType) command;
                waterHeaterFeature.setTemporaryOperatingModeOverride(newOverrideMode == OnOffType.ON ? OverrideMode.TRANSIENT : OverrideMode.NONE);

                needUpdate = true;
            }

            return needUpdate;
        });
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final Node hiveNode
    ) {
        useFeatureSafely(hiveNode, WaterHeaterFeature.class, waterHeaterFeature -> {
            useChannelSafely(thing, HiveBindingConstants.CHANNEL_MODE_OPERATING, operatingModeChannel -> {
                thingHandlerCallback.stateUpdated(operatingModeChannel, new StringType(waterHeaterFeature.getOperatingMode().toString()));
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_EASY_STATE_IS_ON, isOnChannel -> {
                thingHandlerCallback.stateUpdated(isOnChannel, OnOffType.from(waterHeaterFeature.isOn()));
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_MODE_OPERATING_OVERRIDE, operatingModeOverrideChannel -> {
                thingHandlerCallback.stateUpdated(operatingModeOverrideChannel, OnOffType.from(waterHeaterFeature.getTemporaryOperatingModeOverride() == OverrideMode.TRANSIENT));
            });
        });
    }
}
