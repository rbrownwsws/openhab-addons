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
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.feature.BatteryDeviceFeature;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class BatteryDeviceHandlerStrategy extends ThingHandlerStrategyBase<BatteryDeviceFeature> {
    private static final BatteryDeviceHandlerStrategy INSTANCE = new BatteryDeviceHandlerStrategy();

    public static BatteryDeviceHandlerStrategy getInstance() {
        return INSTANCE;
    }

    private BatteryDeviceHandlerStrategy() {
        super(BatteryDeviceFeature.class);
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final BatteryDeviceFeature batteryDeviceFeature
    ) {
        final ChannelUID batteryLevelChannel = thing.getChannel(HiveBindingConstants.CHANNEL_BATTERY_LEVEL).getUID();
        thingHandlerCallback.stateUpdated(batteryLevelChannel, new DecimalType(batteryDeviceFeature.getBatteryLevel().intValue()));


        final boolean batteryLow = !(batteryDeviceFeature.getBatteryState().equals("FULL") || batteryDeviceFeature.getBatteryState().equals("NORMAL"));
        final ChannelUID batteryLowChannel = thing.getChannel(HiveBindingConstants.CHANNEL_BATTERY_LOW).getUID();
        thingHandlerCallback.stateUpdated(batteryLowChannel, OnOffType.from(batteryLow));
    }
}
