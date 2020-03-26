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
import org.openhab.binding.hive.internal.client.HeatingThermostatOperatingMode;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.OnOffMode;
import org.openhab.binding.hive.internal.client.OverrideMode;
import org.openhab.binding.hive.internal.client.feature.HeatingThermostatFeature;
import org.openhab.binding.hive.internal.client.feature.OnOffDeviceFeature;
import org.openhab.binding.hive.internal.client.feature.TransientModeFeature;

/**
 * A {@link ThingHandlerStrategy} that handles channels that provide a
 * simplified interface with Hive Active Heating zones.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HeatingThermostatEasyHandlerStrategy extends ThingHandlerStrategyBase {
    private static final HeatingThermostatEasyHandlerStrategy INSTANCE = new HeatingThermostatEasyHandlerStrategy();

    public static HeatingThermostatEasyHandlerStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean handleCommand(
            final ChannelUID channelUID,
            final Command command,
            final Node hiveNode
    ) {
        return useFeatureSafely(hiveNode, HeatingThermostatFeature.class, heatingThermostatFeature -> {
            return useFeatureSafely(hiveNode, OnOffDeviceFeature.class, onOffDeviceFeature -> {
                return useFeatureSafely(hiveNode, TransientModeFeature.class, transientModeFeature -> {
                    return handleCommand(
                            channelUID,
                            command,
                            hiveNode,
                            heatingThermostatFeature,
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
        useFeatureSafely(hiveNode, HeatingThermostatFeature.class, heatingThermostatFeature -> {
            useFeatureSafely(hiveNode, OnOffDeviceFeature.class, onOffDeviceFeature -> {
                useFeatureSafely(hiveNode, TransientModeFeature.class, transientModeFeature -> {
                    handleUpdate(
                            thing,
                            thingHandlerCallback,
                            hiveNode,
                            heatingThermostatFeature,
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
            final HeatingThermostatFeature heatingThermostatFeature,
            final OnOffDeviceFeature onOffDeviceFeature,
            final TransientModeFeature transientModeFeature
    ) {
        boolean needUpdate = false;
        if (channelUID.getId().equals(HiveBindingConstants.CHANNEL_EASY_MODE_OPERATING)
                && command instanceof StringType
        ) {
            final StringType newOperatingMode = (StringType) command;
            if (newOperatingMode.toString().equals(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_MANUAL)) {
                heatingThermostatFeature.setOperatingMode(HeatingThermostatOperatingMode.MANUAL);
                onOffDeviceFeature.setMode(OnOffMode.ON);
            } else if (newOperatingMode.toString().equals(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_SCHEDULE)) {
                heatingThermostatFeature.setOperatingMode(HeatingThermostatOperatingMode.SCHEDULE);
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
            heatingThermostatFeature.setTemporaryOperatingModeOverride(newOverrideMode == OnOffType.ON ? OverrideMode.TRANSIENT : OverrideMode.NONE);

            needUpdate = true;
        }

        return needUpdate;
    }

    private void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final Node hiveNode,
            final HeatingThermostatFeature heatingThermostatFeature,
            final OnOffDeviceFeature onOffDeviceFeature,
            final TransientModeFeature transientModeFeature
    ) {
        useChannelSafely(thing, HiveBindingConstants.CHANNEL_EASY_MODE_OPERATING, easyModeOperatingChannel -> {
            if (onOffDeviceFeature.getMode() == OnOffMode.OFF) {
                thingHandlerCallback.stateUpdated(easyModeOperatingChannel, new StringType(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_OFF));
            } else if (heatingThermostatFeature.getOperatingMode() == HeatingThermostatOperatingMode.SCHEDULE) {
                thingHandlerCallback.stateUpdated(easyModeOperatingChannel, new StringType(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_SCHEDULE));
            } else {
                thingHandlerCallback.stateUpdated(easyModeOperatingChannel, new StringType(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_MANUAL));
            }
        });

        useChannelSafely(thing, HiveBindingConstants.CHANNEL_EASY_MODE_BOOST, easyModeBoostChannel -> {
            final OnOffType boostActive = OnOffType.from(heatingThermostatFeature.getTemporaryOperatingModeOverride() == OverrideMode.TRANSIENT);
            thingHandlerCallback.stateUpdated(easyModeBoostChannel, boostActive);
        });

        useChannelSafely(thing, HiveBindingConstants.CHANNEL_EASY_STATE_IS_ON, easyStateIsOnChannel -> {
            final OnOffType isOn = OnOffType.from(heatingThermostatFeature.getOperatingState().equals("HEAT"));
            thingHandlerCallback.stateUpdated(easyStateIsOnChannel, isOn);
        });
    }
}
