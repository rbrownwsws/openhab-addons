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
import java.time.ZonedDateTime;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.BuilderUtil;
import org.openhab.binding.hive.internal.client.DefaultFeatureAttribute;
import org.openhab.binding.hive.internal.client.FeatureAttribute;
import org.openhab.binding.hive.internal.client.SettableFeatureAttribute;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class TransientModeFeature implements Feature {
    private final SettableFeatureAttribute<Duration> duration;
    private final SettableFeatureAttribute<Boolean> isEnabled;
    private final FeatureAttribute<ZonedDateTime> startDatetime;
    private final FeatureAttribute<ZonedDateTime> endDatetime;

    private TransientModeFeature(
            final SettableFeatureAttribute<Duration> duration,
            final SettableFeatureAttribute<Boolean> isEnabled,
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

    public SettableFeatureAttribute<Duration> getDuration() {
        return this.duration;
    }

    public TransientModeFeature withTargetDuration(final Duration targetDuration) {
        Objects.requireNonNull(targetDuration);

        return TransientModeFeature.builder()
                .from(this)
                .duration(DefaultFeatureAttribute.<Duration>builder()
                        .from(this.duration)
                        .targetValue(targetDuration)
                        .build()
                )
                .build();
    }

    public SettableFeatureAttribute<Boolean> getIsEnabled() {
        return this.isEnabled;
    }

    public TransientModeFeature withTargetIsEnabled(final boolean targetIsEnabled) {
        return TransientModeFeature.builder()
                .from(this)
                .isEnabled(DefaultFeatureAttribute.<Boolean>builder()
                        .from(this.isEnabled)
                        .targetValue(targetIsEnabled)
                        .build()
                )
                .build();
    }

    public FeatureAttribute<ZonedDateTime> getStartDatetime() {
        return this.startDatetime;
    }

    public FeatureAttribute<ZonedDateTime> getEndDatetime() {
        return this.endDatetime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable SettableFeatureAttribute<Duration> duration;
        private @Nullable SettableFeatureAttribute<Boolean> isEnabled;
        private @Nullable FeatureAttribute<ZonedDateTime> startDatetime;
        private @Nullable FeatureAttribute<ZonedDateTime> endDatetime;

        public Builder from(final TransientModeFeature transientModeFeature) {
            Objects.requireNonNull(transientModeFeature);

            return this.duration(transientModeFeature.getDuration())
                    .isEnabled(transientModeFeature.getIsEnabled())
                    .startDatetime(transientModeFeature.getStartDatetime())
                    .endDatetime(transientModeFeature.getEndDatetime());
        }

        public Builder duration(final SettableFeatureAttribute<Duration> duration) {
            this.duration = Objects.requireNonNull(duration);

            return this;
        }

        public Builder isEnabled(final SettableFeatureAttribute<Boolean> isEnabled) {
            this.isEnabled = Objects.requireNonNull(isEnabled);

            return this;
        }

        public Builder startDatetime(final FeatureAttribute<ZonedDateTime> startDatetime) {
            this.startDatetime = Objects.requireNonNull(startDatetime);

            return this;
        }

        public Builder endDatetime(final FeatureAttribute<ZonedDateTime> endDatetime) {
            this.endDatetime = Objects.requireNonNull(endDatetime);

            return this;
        }

        public TransientModeFeature build() {
            final @Nullable SettableFeatureAttribute<Duration> duration = this.duration;
            final @Nullable SettableFeatureAttribute<Boolean> isEnabled = this.isEnabled;
            final @Nullable FeatureAttribute<ZonedDateTime> startDatetime = this.startDatetime;
            final @Nullable FeatureAttribute<ZonedDateTime> endDatetime = this.endDatetime;

            if (duration == null
                    || isEnabled == null
                    || startDatetime == null
                    || endDatetime == null
            ) {
                throw new IllegalStateException(BuilderUtil.REQUIRED_ATTRIBUTE_NOT_SET_MESSAGE);
            }

            return new TransientModeFeature(
                    duration,
                    isEnabled,
                    startDatetime,
                    endDatetime
            );
        }
    }
}
