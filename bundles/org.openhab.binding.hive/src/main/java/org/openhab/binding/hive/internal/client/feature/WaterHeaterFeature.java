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
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.*;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class WaterHeaterFeature implements Feature {
    private final SettableFeatureAttribute<WaterHeaterOperatingMode> operatingMode;
    private final FeatureAttribute<Boolean> isOn;
    private final SettableFeatureAttribute<OverrideMode> temporaryOperatingModeOverride;

    private WaterHeaterFeature(
            final SettableFeatureAttribute<WaterHeaterOperatingMode> operatingMode,
            final FeatureAttribute<Boolean> isOn,
            final SettableFeatureAttribute<OverrideMode> temporaryOperatingModeOverride
    ) {
        Objects.requireNonNull(operatingMode);
        Objects.requireNonNull(isOn);
        Objects.requireNonNull(temporaryOperatingModeOverride);

        this.operatingMode = operatingMode;
        this.isOn = isOn;
        this.temporaryOperatingModeOverride = temporaryOperatingModeOverride;
    }

    public SettableFeatureAttribute<WaterHeaterOperatingMode> getOperatingMode() {
        return this.operatingMode;
    }

    public WaterHeaterFeature withTargetOperatingMode(final WaterHeaterOperatingMode targetOperatingMode) {
        Objects.requireNonNull(targetOperatingMode);

        return WaterHeaterFeature.builder()
                .from(this)
                .operatingMode(DefaultFeatureAttribute.<WaterHeaterOperatingMode>builder()
                        .from(this.operatingMode)
                        .targetValue(targetOperatingMode)
                        .build()
                )
                .build();
    }

    public FeatureAttribute<Boolean> getIsOn() {
        return this.isOn;
    }

    public SettableFeatureAttribute<OverrideMode> getTemporaryOperatingModeOverride() {
        return this.temporaryOperatingModeOverride;
    }

    public WaterHeaterFeature withTargetTemporaryOperatingModeOverride(final OverrideMode targetOverrideMode) {
        Objects.requireNonNull(targetOverrideMode);

        return WaterHeaterFeature.builder()
                .from(this)
                .temporaryOperatingModeOverride(DefaultFeatureAttribute.<OverrideMode>builder()
                        .from(this.temporaryOperatingModeOverride)
                        .targetValue(targetOverrideMode)
                        .build()
                )
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable SettableFeatureAttribute<WaterHeaterOperatingMode> operatingMode;
        private @Nullable FeatureAttribute<Boolean> isOn;
        private @Nullable SettableFeatureAttribute<OverrideMode> temporaryOperatingModeOverride;

        public Builder from(final WaterHeaterFeature waterHeaterFeature) {
            Objects.requireNonNull(waterHeaterFeature);

            return this.operatingMode(waterHeaterFeature.getOperatingMode())
                    .isOn(waterHeaterFeature.getIsOn())
                    .temporaryOperatingModeOverride(waterHeaterFeature.getTemporaryOperatingModeOverride());
        }

        public Builder operatingMode(final SettableFeatureAttribute<WaterHeaterOperatingMode> operatingMode) {
            this.operatingMode = Objects.requireNonNull(operatingMode);

            return this;
        }

        public Builder isOn(final FeatureAttribute<Boolean> isOn) {
            this.isOn = Objects.requireNonNull(isOn);

            return this;
        }

        public Builder temporaryOperatingModeOverride(final SettableFeatureAttribute<OverrideMode> temporaryOperatingModeOverride) {
            this.temporaryOperatingModeOverride = Objects.requireNonNull(temporaryOperatingModeOverride);

            return this;
        }

        public WaterHeaterFeature build() {
            final @Nullable SettableFeatureAttribute<WaterHeaterOperatingMode> operatingMode = this.operatingMode;
            final @Nullable FeatureAttribute<Boolean> isOn = this.isOn;
            final @Nullable SettableFeatureAttribute<OverrideMode> temporaryOperatingModeOverride = this.temporaryOperatingModeOverride;

            if (operatingMode == null
                    || isOn == null
                    || temporaryOperatingModeOverride == null
            ) {
                throw new IllegalStateException(BuilderUtil.REQUIRED_ATTRIBUTE_NOT_SET_MESSAGE);
            }

            return new WaterHeaterFeature(
                    operatingMode,
                    isOn,
                    temporaryOperatingModeOverride
            );
        }
    }
}
