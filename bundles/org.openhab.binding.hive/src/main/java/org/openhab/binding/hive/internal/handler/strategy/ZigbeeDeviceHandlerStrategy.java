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
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.feature.ZigbeeDeviceFeature;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class ZigbeeDeviceHandlerStrategy extends ThingHandlerStrategyBase<ZigbeeDeviceFeature> {
    private static final ZigbeeDeviceHandlerStrategy INSTANCE = new ZigbeeDeviceHandlerStrategy();

    public static ZigbeeDeviceHandlerStrategy getInstance() {
        return INSTANCE;
    }

    private ZigbeeDeviceHandlerStrategy() {
        super(ZigbeeDeviceFeature.class);
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final ZigbeeDeviceFeature zigbeeDeviceFeature
    ) {
        final ChannelUID averageLqiChannel = thing.getChannel(HiveBindingConstants.CHANNEL_RADIO_LQI_AVERAGE).getUID();
        thingHandlerCallback.stateUpdated(averageLqiChannel, new DecimalType(zigbeeDeviceFeature.getAverageLQI()));

        final ChannelUID lastKnownLqiChannel = thing.getChannel(HiveBindingConstants.CHANNEL_RADIO_LQI_LAST_KNOWN).getUID();
        thingHandlerCallback.stateUpdated(lastKnownLqiChannel, new DecimalType(zigbeeDeviceFeature.getLastKnownLQI()));

        final ChannelUID averageRssiChannel = thing.getChannel(HiveBindingConstants.CHANNEL_RADIO_RSSI_AVERAGE).getUID();
        thingHandlerCallback.stateUpdated(averageRssiChannel, new DecimalType(zigbeeDeviceFeature.getAverageRSSI()));

        final ChannelUID lastKnownRssiChannel = thing.getChannel(HiveBindingConstants.CHANNEL_RADIO_RSSI_LAST_KNOWN).getUID();
        thingHandlerCallback.stateUpdated(lastKnownRssiChannel, new DecimalType(zigbeeDeviceFeature.getLastKnownRSSI()));
    }
}
