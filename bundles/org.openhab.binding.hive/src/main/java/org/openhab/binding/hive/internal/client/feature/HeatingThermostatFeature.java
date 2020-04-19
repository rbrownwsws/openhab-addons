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

    public HeatingThermostatFeature(
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

    public SettableFeatureAttribute<HeatingThermostatOperatingMode> getOperatingModeAttribute() {
        return this.operatingMode;
    }

    public HeatingThermostatOperatingMode getOperatingMode() {
        return this.operatingMode.getDisplayValue();
    }

    public void setOperatingMode(final HeatingThermostatOperatingMode operatingMode) {
        Objects.requireNonNull(operatingMode);

        this.operatingMode.setTargetValue(operatingMode);
    }

    public FeatureAttribute<HeatingThermostatOperatingState> getOperatingStateAttribute() {
        return this.operatingState;
    }

    public HeatingThermostatOperatingState getOperatingState() {
        return this.operatingState.getDisplayValue();
    }

    public SettableFeatureAttribute<Quantity<Temperature>> getTargetHeatTemperatureAttribute() {
        return this.targetHeatTemperature;
    }

    public Quantity<Temperature> getTargetHeatTemperature() {
        return this.targetHeatTemperature.getDisplayValue();
    }

    public void setTargetHeatTemperature(final Quantity<Temperature> targetHeatTemperature) {
        Objects.requireNonNull(targetHeatTemperature);

        this.targetHeatTemperature.setTargetValue(targetHeatTemperature);
    }

    public SettableFeatureAttribute<OverrideMode> getTemporaryOperatingModeOverrideAttribute() {
        return this.temporaryOperatingModeOverride;
    }

    public OverrideMode getTemporaryOperatingModeOverride() {
        return this.temporaryOperatingModeOverride.getDisplayValue();
    }

    public void setTemporaryOperatingModeOverride(final OverrideMode overrideMode) {
        Objects.requireNonNull(overrideMode);

        this.temporaryOperatingModeOverride.setTargetValue(overrideMode);
    }
}
