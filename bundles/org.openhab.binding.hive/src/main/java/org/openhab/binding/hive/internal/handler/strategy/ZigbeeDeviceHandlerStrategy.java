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
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.feature.ZigbeeDeviceFeature;

/**
 * A {@link ThingHandlerStrategy} for handling
 * {@link ZigbeeDeviceFeature}.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class ZigbeeDeviceHandlerStrategy extends ThingHandlerStrategyBase {
    private static final ZigbeeDeviceHandlerStrategy INSTANCE = new ZigbeeDeviceHandlerStrategy();

    public static ZigbeeDeviceHandlerStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final Node hiveNode
    ) {
        useFeatureSafely(hiveNode, ZigbeeDeviceFeature.class, zigbeeDeviceFeature -> {
            thing.setProperty(HiveBindingConstants.PROPERTY_EUI64, zigbeeDeviceFeature.getEui64().toString());

            // TODO: Extract MAC address from EUI64?
            //thing.setProperty(Thing.PROPERTY_MAC_ADDRESS, xxx);

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_RADIO_LQI_AVERAGE, averageLqiChannel -> {
                thingHandlerCallback.stateUpdated(averageLqiChannel, new DecimalType(zigbeeDeviceFeature.getAverageLQI()));
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_RADIO_LQI_LAST_KNOWN, lastKnownLqiChannel -> {
                thingHandlerCallback.stateUpdated(lastKnownLqiChannel, new DecimalType(zigbeeDeviceFeature.getLastKnownLQI()));
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_RADIO_RSSI_AVERAGE, averageRssiChannel -> {
                thingHandlerCallback.stateUpdated(averageRssiChannel, new DecimalType(zigbeeDeviceFeature.getAverageRSSI()));
            });

            useChannelSafely(thing, HiveBindingConstants.CHANNEL_RADIO_RSSI_LAST_KNOWN, lastKnownRssiChannel -> {
                thingHandlerCallback.stateUpdated(lastKnownRssiChannel, new DecimalType(zigbeeDeviceFeature.getLastKnownRSSI()));
            });
        });
    }
}
