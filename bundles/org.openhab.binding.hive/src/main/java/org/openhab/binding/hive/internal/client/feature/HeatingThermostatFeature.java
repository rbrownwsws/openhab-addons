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
import org.openhab.binding.hive.internal.client.*;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HeatingThermostatFeature implements Feature {
    private final SettableFeatureAttribute<HeatingThermostatOperatingMode> operatingMode;
    private final FeatureAttribute<HeatingThermostatOperatingState> operatingState;
    private final SettableFeatureAttribute<Quantity<Temperature>> targetHeatTemperature;
    private final SettableFeatureAttribute<OverrideMode> temporaryOperatingModeOverride;

    private HeatingThermostatFeature(
            final SettableFeatureAttribute<HeatingThermostatOperatingMode> operatingMode,
            final FeatureAttribute<HeatingThermostatOperatingState> operatingState,
            final SettableFeatureAttribute<Quantity<Temperature>> targetHeatTemperature,
            final SettableFeatureAttribute<OverrideMode> temporaryOperatingModeOverride
    ) {
        Objects.requireNonNull(operatingMode);
        Objects.requireNonNull(operatingState);
        Objects.requireNonNull(targetHeatTemperature);
        Objects.requireNonNull(temporaryOperatingModeOverride);

        this.operatingMode = operatingMode;
        this.operatingState = operatingState;
        this.targetHeatTemperature = targetHeatTemperature;
        this.temporaryOperatingModeOverride = temporaryOperatingModeOverride;
    }

    public SettableFeatureAttribute<HeatingThermostatOperatingMode> getOperatingMode() {
        return this.operatingMode;
    }

    public HeatingThermostatFeature withTargetOperatingMode(final HeatingThermostatOperatingMode targetOperatingMode) {
        return HeatingThermostatFeature.builder()
                .from(this)
                .operatingMode(
                        DefaultFeatureAttribute.<HeatingThermostatOperatingMode>builder()
                                .from(this.operatingMode)
                                .targetValue(targetOperatingMode)
                                .build()
                )
                .build();
    }

    public FeatureAttribute<HeatingThermostatOperatingState> getOperatingState() {
        return this.operatingState;
    }

    public SettableFeatureAttribute<Quantity<Temperature>> getTargetHeatTemperature() {
        return this.targetHeatTemperature;
    }

    public HeatingThermostatFeature withTargetTargetHeatTemperature(final Quantity<Temperature> targetTargetHeatTemperature) {
        return HeatingThermostatFeature.builder()
                .from(this)
                .targetHeatTemperature(
                        DefaultFeatureAttribute.<Quantity<Temperature>>builder()
                                .from(this.targetHeatTemperature)
                                .targetValue(targetTargetHeatTemperature)
                                .build()
                )
                .build();
    }

    public SettableFeatureAttribute<OverrideMode> getTemporaryOperatingModeOverride() {
        return this.temporaryOperatingModeOverride;
    }

    public HeatingThermostatFeature withTargetTemporaryOperatingModeOverride(final OverrideMode targetOverrideMode) {
        return HeatingThermostatFeature.builder()
                .from(this)
                .temporaryOperatingModeOverride(
                        DefaultFeatureAttribute.<OverrideMode>builder()
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
        private @Nullable SettableFeatureAttribute<HeatingThermostatOperatingMode> operatingMode;
        private @Nullable FeatureAttribute<HeatingThermostatOperatingState> operatingState;
        private @Nullable SettableFeatureAttribute<Quantity<Temperature>> targetHeatTemperature;
        private @Nullable SettableFeatureAttribute<OverrideMode> temporaryOperatingModeOverride;

        public Builder from(final HeatingThermostatFeature heatingThermostatFeature) {
            Objects.requireNonNull(heatingThermostatFeature);

            return this.operatingMode(heatingThermostatFeature.getOperatingMode())
                    .operatingState(heatingThermostatFeature.getOperatingState())
                    .targetHeatTemperature(heatingThermostatFeature.getTargetHeatTemperature())
                    .temporaryOperatingModeOverride(heatingThermostatFeature.getTemporaryOperatingModeOverride());
        }

        public Builder operatingMode(final SettableFeatureAttribute<HeatingThermostatOperatingMode> operatingMode) {
            this.operatingMode = Objects.requireNonNull(operatingMode);

            return this;
        }

        public Builder operatingState(final FeatureAttribute<HeatingThermostatOperatingState> operatingState) {
            this.operatingState = Objects.requireNonNull(operatingState);

            return this;
        }

        public Builder targetHeatTemperature(final SettableFeatureAttribute<Quantity<Temperature>> targetHeatTemperature) {
            this.targetHeatTemperature = Objects.requireNonNull(targetHeatTemperature);

            return this;
        }

        public Builder temporaryOperatingModeOverride(final SettableFeatureAttribute<OverrideMode> temporaryOperatingModeOverride) {
            this.temporaryOperatingModeOverride = Objects.requireNonNull(temporaryOperatingModeOverride);

            return this;
        }

        public HeatingThermostatFeature build() {
            final @Nullable SettableFeatureAttribute<HeatingThermostatOperatingMode> operatingMode = this.operatingMode;
            final @Nullable FeatureAttribute<HeatingThermostatOperatingState> operatingState = this.operatingState;
            final @Nullable SettableFeatureAttribute<Quantity<Temperature>> targetHeatTemperature = this.targetHeatTemperature;
            final @Nullable SettableFeatureAttribute<OverrideMode> temporaryOperatingModeOverride = this.temporaryOperatingModeOverride;

            if (operatingMode == null
                    || operatingState == null
                    || targetHeatTemperature == null
                    || temporaryOperatingModeOverride == null
            ) {
                throw new IllegalStateException(BuilderUtil.REQUIRED_ATTRIBUTE_NOT_SET_MESSAGE);
            }

            return new HeatingThermostatFeature(
                    operatingMode,
                    operatingState,
                    targetHeatTemperature,
                    temporaryOperatingModeOverride
            );
        }
    }
}
