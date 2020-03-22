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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.Thing;
import org.openhab.binding.hive.internal.handler.strategy.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HiveTrvGroupHandler extends HiveHandlerBase {
    public HiveTrvGroupHandler(final Thing thing) {
        super(
                thing,
                Stream.of(
                        HeatingThermostatHandlerStrategy.getInstance(),
                        OnOffDeviceHandlerStrategy.getInstance(),
                        TemperatureSensorHandlerStrategy.getInstance(),
                        TransientModeHandlerStrategy.getInstance(),
                        HeatingTransientModeHandlerStrategy.getInstance()
                ).collect(Collectors.toSet())
        );
    }
}
