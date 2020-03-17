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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.hive.internal.client.FeatureAttribute;
import org.openhab.binding.hive.internal.client.SettableFeatureAttribute;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public class TransientModeFeature implements Feature {
    private final SettableFeatureAttribute<Duration> duration;
    private final FeatureAttribute<Boolean> isEnabled;
    private final FeatureAttribute<ZonedDateTime> startDatetime;
    private final FeatureAttribute<ZonedDateTime> endDatetime;

    public TransientModeFeature(
            final SettableFeatureAttribute<Duration> duration,
            final FeatureAttribute<Boolean> isEnabled,
            final FeatureAttribute<ZonedDateTime> startDatetime,
            final FeatureAttribute<ZonedDateTime> endDatetime
    ) {
        Objects.requireNonNull(duration);
        Objects.requireNonNull(isEnabled);
        Objects.requireNonNull(startDatetime);
        Objects.requireNonNull(endDatetime);

        this.duration = duration;
        this.isEnabled = isEnabled;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
    }

    public final SettableFeatureAttribute<Duration> getDurationAttribute() {
        return this.duration;
    }

    public final Duration getDuration() {
        return this.duration.getDisplayValue();
    }

    public final void setDuration(final Duration duration) {
        Objects.requireNonNull(duration);

        this.duration.setTargetValue(duration);
    }

    public final FeatureAttribute<Boolean> getIsEnabledAttribute() {
        return this.isEnabled;
    }

    public final boolean getIsEnabled() {
        return this.isEnabled.getDisplayValue();
    }

    public final FeatureAttribute<ZonedDateTime> getStartDatetimeAttribute() {
        return this.startDatetime;
    }

    public final ZonedDateTime getStartDatetime() {
        return this.startDatetime.getDisplayValue();
    }

    public final FeatureAttribute<ZonedDateTime> getEndDatetimeAttribute() {
        return this.endDatetime;
    }

    public final ZonedDateTime getEndDatetime() {
        return this.endDatetime.getDisplayValue();
    }
}
