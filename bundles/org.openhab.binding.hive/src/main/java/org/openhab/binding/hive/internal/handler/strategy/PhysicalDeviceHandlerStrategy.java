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
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.openhab.binding.hive.internal.client.Node;
import org.openhab.binding.hive.internal.client.feature.PhysicalDeviceFeature;

/**
 * A {@link ThingHandlerStrategy} for handling
 * {@link PhysicalDeviceFeature}.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class PhysicalDeviceHandlerStrategy extends ThingHandlerStrategyBase {
    private static final PhysicalDeviceHandlerStrategy INSTANCE = new PhysicalDeviceHandlerStrategy();

    public static PhysicalDeviceHandlerStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public void handleUpdate(
            final Thing thing,
            final ThingHandlerCallback thingHandlerCallback,
            final Node hiveNode
    ) {
        useFeatureSafely(hiveNode, PhysicalDeviceFeature.class, physicalDeviceFeature -> {
            thing.setProperty(Thing.PROPERTY_VENDOR, physicalDeviceFeature.getManufacturer());
            thing.setProperty(Thing.PROPERTY_MODEL_ID, physicalDeviceFeature.getModel());
            thing.setProperty(Thing.PROPERTY_FIRMWARE_VERSION, physicalDeviceFeature.getSoftwareVersion());
            thing.setProperty(Thing.PROPERTY_SERIAL_NUMBER, physicalDeviceFeature.getHardwareIdentifier());
        });
    }
}
