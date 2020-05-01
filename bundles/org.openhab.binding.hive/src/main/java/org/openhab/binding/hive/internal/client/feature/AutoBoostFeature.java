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

import java.time.Duration;
import java.util.Objects;

import javax.measure.Quantity;
import javax.measure.quantity.Temperature;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.BuilderUtil;
import org.openhab.binding.hive.internal.client.SettableFeatureAttribute;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class AutoBoostFeature implements Feature {
    private final SettableFeatureAttribute<Duration> autoBoostDuration;
    private final SettableFeatureAttribute<Quantity<Temperature>> autoBoostTargetHeatTemperature;

    private AutoBoostFeature(
            final SettableFeatureAttribute<Duration> autoBoostDuration,
            final SettableFeatureAttribute<Quantity<Temperature>> autoBoostTargetHeatTemperature
    ) {
        Objects.requireNonNull(autoBoostDuration);
        Objects.requireNonNull(autoBoostTargetHeatTemperature);

        this.autoBoostDuration = autoBoostDuration;
        this.autoBoostTargetHeatTemperature = autoBoostTargetHeatTemperature;
    }

    public SettableFeatureAttribute<Duration> getAutoBoostDuration() {
        return this.autoBoostDuration;
    }

    public AutoBoostFeature withTargetAutoBoostDuration(final Duration targetAutoBoostDuration) {
        Objects.requireNonNull(targetAutoBoostDuration);

        return AutoBoostFeature.builder()
                .from(this)
                .autoBoostDuration(
                        this.autoBoostDuration.withTargetValue(targetAutoBoostDuration)
                )
                .build();
    }

    public SettableFeatureAttribute<Quantity<Temperature>> getAutoBoostTargetHeatTemperature() {
        return this.autoBoostTargetHeatTemperature;
    }

    public AutoBoostFeature withTargetAutoBoostTargetHeatTemperature(final Quantity<Temperature> targetAutoBoostTargetHeatTemperature) {
        Objects.requireNonNull(targetAutoBoostTargetHeatTemperature);

        return AutoBoostFeature.builder()
                .from(this)
                .autoBoostTargetHeatTemperature(
                        this.autoBoostTargetHeatTemperature.withTargetValue(targetAutoBoostTargetHeatTemperature)
                )
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable SettableFeatureAttribute<Duration> autoBoostDuration;
        private @Nullable SettableFeatureAttribute<Quantity<Temperature>> autoBoostTargetHeatTemperature;

        public Builder from(final AutoBoostFeature autoBoostFeature) {
            Objects.requireNonNull(autoBoostFeature);

            this.autoBoostDuration(autoBoostFeature.autoBoostDuration);
            this.autoBoostTargetHeatTemperature(autoBoostFeature.autoBoostTargetHeatTemperature);

            return this;
        }

        public Builder autoBoostDuration(final SettableFeatureAttribute<Duration> autoBoostDuration) {
            this.autoBoostDuration = Objects.requireNonNull(autoBoostDuration);

            return this;
        }

        public Builder autoBoostTargetHeatTemperature(final SettableFeatureAttribute<Quantity<Temperature>> autoBoostTargetHeatTemperature) {
            this.autoBoostTargetHeatTemperature = Objects.requireNonNull(autoBoostTargetHeatTemperature);

            return this;
        }

        public AutoBoostFeature build() {
            final @Nullable SettableFeatureAttribute<Duration> autoBoostDuration = this.autoBoostDuration;
            final @Nullable SettableFeatureAttribute<Quantity<Temperature>> autoBoostTargetHeatTemperature = this.autoBoostTargetHeatTemperature;

            if (autoBoostDuration == null
                    || autoBoostTargetHeatTemperature == null
            ) {
                throw new IllegalStateException(BuilderUtil.REQUIRED_ATTRIBUTE_NOT_SET_MESSAGE);
            }

            return new AutoBoostFeature(
                    autoBoostDuration,
                    autoBoostTargetHeatTemperature
            );
        }
    }
}
