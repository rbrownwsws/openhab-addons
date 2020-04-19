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

    public AutoBoostFeature(
            final SettableFeatureAttribute<Duration> autoBoostDuration,
            final SettableFeatureAttribute<Quantity<Temperature>> autoBoostTargetHeatTemperature
    ) {
        Objects.requireNonNull(autoBoostDuration);
        Objects.requireNonNull(autoBoostTargetHeatTemperature);

        this.autoBoostDuration = autoBoostDuration;
        this.autoBoostTargetHeatTemperature = autoBoostTargetHeatTemperature;
    }

    public final SettableFeatureAttribute<Duration> getAutoBoostDurationAttribute() {
        return this.autoBoostDuration;
    }

    public final Duration getAutoBoostDuration() {
        return this.autoBoostDuration.getDisplayValue();
    }

    public final void setAutoBoostDuration(final Duration duration) {
        Objects.requireNonNull(duration);

        this.autoBoostDuration.setTargetValue(duration);
    }

    public final SettableFeatureAttribute<Quantity<Temperature>> getAutoBoostTargetHeatTemperatureAttribute() {
        return this.autoBoostTargetHeatTemperature;
    }

    public final Quantity<Temperature> getAutoBoostTargetHeatTemperature() {
        return this.autoBoostTargetHeatTemperature.getDisplayValue();
    }

    public final void setAutoBoostTargetHeatTemperature(final Quantity<Temperature> targetHeatTemperature) {
        this.autoBoostTargetHeatTemperature.setTargetValue(targetHeatTemperature);
    }
}
