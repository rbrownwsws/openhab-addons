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

import javax.measure.Quantity;
import javax.measure.quantity.Temperature;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.BuilderUtil;
import org.openhab.binding.hive.internal.client.FeatureAttribute;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class TemperatureSensorFeature implements Feature {
    private final FeatureAttribute<Quantity<Temperature>> temperature;

    private TemperatureSensorFeature(
            final FeatureAttribute<Quantity<Temperature>> temperature
    ) {
        Objects.requireNonNull(temperature);

        this.temperature = temperature;
    }

    public FeatureAttribute<Quantity<Temperature>> getTemperature() {
        return this.temperature;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable FeatureAttribute<Quantity<Temperature>> temperature;

        public Builder from(final TemperatureSensorFeature temperatureSensorFeature) {
            Objects.requireNonNull(temperatureSensorFeature);

            return this.temperature(temperatureSensorFeature.getTemperature());
        }

        public Builder temperature(final FeatureAttribute<Quantity<Temperature>> temperature) {
            this.temperature = Objects.requireNonNull(temperature);

            return this;
        }

        public TemperatureSensorFeature build() {
            final @Nullable FeatureAttribute<Quantity<Temperature>> temperature = this.temperature;

            if (temperature == null) {
                throw new IllegalStateException(BuilderUtil.REQUIRED_ATTRIBUTE_NOT_SET_MESSAGE);
            }

            return new TemperatureSensorFeature(temperature);
        }
    }
}
