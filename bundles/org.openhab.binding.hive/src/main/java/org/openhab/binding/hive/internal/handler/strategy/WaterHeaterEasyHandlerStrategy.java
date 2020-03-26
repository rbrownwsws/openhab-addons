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
import org.openhab.binding.hive.internal.client.OnOffMode;
import org.openhab.binding.hive.internal.client.OverrideMode;
import org.openhab.binding.hive.internal.client.WaterHeaterOperatingMode;
import org.openhab.binding.hive.internal.client.feature.OnOffDeviceFeature;
import org.openhab.binding.hive.internal.client.feature.TransientModeFeature;
import org.openhab.binding.hive.internal.client.feature.WaterHeaterFeature;

/**
 * A {@link ThingHandlerStrategy} that handles channels that provide a
 * simplified interface with Hive Hot Water.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class WaterHeaterEasyHandlerStrategy extends ThingHandlerStrategyBase {
    private static final WaterHeaterEasyHandlerStrategy INSTANCE = new WaterHeaterEasyHandlerStrategy();

    public static WaterHeaterEasyHandlerStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode
    ) {
        return useFeatureSafely(hiveNode, WaterHeaterFeature.class, waterHeaterFeature -> {
            return useFeatureSafely(hiveNode, OnOffDeviceFeature.class, onOffDeviceFeature -> {
                return useFeatureSafely(hiveNode, TransientModeFeature.class, transientModeFeature -> {
                    return handleCommand(
                            channelUID,
                            command,
                            hiveNode,
                            waterHeaterFeature,
                            onOffDeviceFeature,
                            transientModeFeature
                    );
                });
            });
        });
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final Node hiveNode
    ) {
        useFeatureSafely(hiveNode, WaterHeaterFeature.class, waterHeaterFeature -> {
            useFeatureSafely(hiveNode, OnOffDeviceFeature.class, onOffDeviceFeature -> {
                useFeatureSafely(hiveNode, TransientModeFeature.class, transientModeFeature -> {
                    handleUpdate(
                            thing,
                            thingHandlerCallback,
                            hiveNode,
                            waterHeaterFeature,
                            onOffDeviceFeature,
                            transientModeFeature
                    );
                });
            });
        });
    }

    private boolean handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode,
            final WaterHeaterFeature waterHeaterFeature,
            final OnOffDeviceFeature onOffDeviceFeature,
            final TransientModeFeature transientModeFeature
    ) {
        boolean needUpdate = false;
        if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_EASY_MODE_OPERATING)
                && command instanceof StringType
        ) {
            final StringType newOperatingMode = (StringType) command;
            if (newOperatingMode.toString().equals(HiveBindingConstants.HOT_WATER_EASY_MODE_OPERATING_SCHEDULE)) {
                waterHeaterFeature.setOperatingMode(WaterHeaterOperatingMode.SCHEDULE);
                onOffDeviceFeature.setMode(OnOffMode.ON);
            } else if (newOperatingMode.toString().equals(HiveBindingConstants.HOT_WATER_EASY_MODE_OPERATING_ON)) {
                waterHeaterFeature.setOperatingMode(WaterHeaterOperatingMode.ON);
                onOffDeviceFeature.setMode(OnOffMode.ON);
            } else {
                // easy-mode-operating: OFF
                onOffDeviceFeature.setMode(OnOffMode.OFF);
            }

            needUpdate = true;
        } else if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_EASY_MODE_BOOST)
                && command instanceof OnOffType
        ) {
            final OnOffType newOverrideMode = (OnOffType) command;

            if (newOverrideMode == OnOffType.ON) {
                transientModeFeature.setIsEnabled(true);
                waterHeaterFeature.setTemporaryOperatingModeOverride(OverrideMode.TRANSIENT);
            } else {
                transientModeFeature.setIsEnabled(false);
                waterHeaterFeature.setTemporaryOperatingModeOverride(OverrideMode.NONE);
            }


            needUpdate = true;
        }

        return needUpdate;
    }

    private void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final Node hiveNode,
            final WaterHeaterFeature waterHeaterFeature,
            final OnOffDeviceFeature onOffDeviceFeature,
            final TransientModeFeature transientModeFeature
    ) {
        useChannelSafely(thing, HiveBindingConstants.CHANNEL_EASY_MODE_OPERATING, easyModeOperatingChannel -> {
            if (waterHeaterFeature.getOperatingMode() == WaterHeaterOperatingMode.SCHEDULE) {
                thingHandlerCallback.stateUpdated(easyModeOperatingChannel, new StringType(HiveBindingConstants.HOT_WATER_EASY_MODE_OPERATING_SCHEDULE));
            } else if (onOffDeviceFeature.getMode() == OnOffMode.ON) {
                thingHandlerCallback.stateUpdated(easyModeOperatingChannel, new StringType(HiveBindingConstants.HOT_WATER_EASY_MODE_OPERATING_ON));
            } else {
                thingHandlerCallback.stateUpdated(easyModeOperatingChannel, new StringType(HiveBindingConstants.HOT_WATER_EASY_MODE_OPERATING_OFF));
            }
        });

        useChannelSafely(thing, HiveBindingConstants.CHANNEL_EASY_MODE_BOOST, easyModeBoostChannel -> {
            final OnOffType boostActive = OnOffType.from(waterHeaterFeature.getTemporaryOperatingModeOverride() == OverrideMode.TRANSIENT
                    && transientModeFeature.getIsEnabled());
            thingHandlerCallback.stateUpdated(easyModeBoostChannel, boostActive);
        });
    }
}
