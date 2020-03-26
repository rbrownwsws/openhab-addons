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
package org.openhab.binding.hive.internal.client.feature;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.hive.internal.client.FeatureAttribute;
import org.openhab.binding.hive.internal.client.Temperature;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class TemperatureSensorFeature implements Feature {
    private final FeatureAttribute<Temperature> temperature;

    public TemperatureSensorFeature(
            final FeatureAttribute<Temperature> temperature
    ) {
        Objects.requireNonNull(temperature);

        this.temperature = temperature;
    }

    public FeatureAttribute<Temperature> getTemperatureAttribute() {
        return this.temperature;
    }

    public Temperature getTemperature() {
        return this.temperature.getDisplayValue();
    }
}