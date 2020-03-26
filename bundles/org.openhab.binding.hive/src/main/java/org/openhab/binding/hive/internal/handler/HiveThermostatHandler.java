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
package org.openhab.binding.hive.internal.handler;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.Thing;
import org.openhab.binding.hive.internal.handler.strategy.BatteryDeviceHandlerStrategy;
import org.openhab.binding.hive.internal.handler.strategy.PhysicalDeviceHandlerStrategy;
import org.openhab.binding.hive.internal.handler.strategy.ZigbeeDeviceHandlerStrategy;

/**
 * A {@link org.eclipse.smarthome.core.thing.binding.ThingHandler} for
 * Hive Thermostats
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HiveThermostatHandler extends HiveHandlerBase {
    public HiveThermostatHandler(final Thing thing) {
        super(
                thing,
                Stream.of(
                        BatteryDeviceHandlerStrategy.getInstance(),
                        PhysicalDeviceHandlerStrategy.getInstance(),
                        ZigbeeDeviceHandlerStrategy.getInstance()
                ).collect(Collectors.toSet())
        );
    }
}
