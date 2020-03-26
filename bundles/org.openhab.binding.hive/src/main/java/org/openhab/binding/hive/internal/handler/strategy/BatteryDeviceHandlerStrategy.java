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
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.feature.BatteryDeviceFeature;

import tec.uom.se.unit.Units;

/**
 * A {@link ThingHandlerStrategy} for handling
 * {@link BatteryDeviceFeature}.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class BatteryDeviceHandlerStrategy extends ThingHandlerStrategyBase {
    private static final BatteryDeviceHandlerStrategy INSTANCE = new BatteryDeviceHandlerStrategy();

    public static BatteryDeviceHandlerStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final Node hiveNode
    ) {
        useFeatureSafely(hiveNode, BatteryDeviceFeature.class,  batteryDeviceFeature -> {
            useChannelSafely(thing, HiveBindingConstants.CHANNEL_BATTERY_LEVEL, batteryLevelChannel -> {
                thingHandlerCallback.stateUpdated(batteryLevelChannel, new DecimalType(batteryDeviceFeature.getBatteryLevel().intValue()));
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_BATTERY_LOW, batteryLowChannel -> {
                final boolean batteryLow = batteryDeviceFeature.getBatteryState().equals("LOW");
                thingHandlerCallback.stateUpdated(batteryLowChannel, OnOffType.from(batteryLow));
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_BATTERY_STATE, batteryStateChannel -> {
                thingHandlerCallback.stateUpdated(batteryStateChannel, new StringType(batteryDeviceFeature.getBatteryState()));
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_BATTERY_VOLTAGE, batteryVoltageChannel -> {
                thingHandlerCallback.stateUpdated(batteryVoltageChannel, new QuantityType<>(batteryDeviceFeature.getBatteryVoltage().getValue(), Units.VOLT));
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_BATTERY_NOTIFICATION_STATE, batteryNotificationStateChannel -> {
                thingHandlerCallback.stateUpdated(batteryNotificationStateChannel, new StringType(batteryDeviceFeature.getBatteryNotificationState()));
            });
        });
    }
}
