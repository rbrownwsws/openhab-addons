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
import org.openhab.binding.hive.internal.client.OverrideMode;
import org.openhab.binding.hive.internal.client.SettableFeatureAttribute;
import org.openhab.binding.hive.internal.client.WaterHeaterOperatingMode;

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

    public WaterHeaterFeature(
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

    public SettableFeatureAttribute<WaterHeaterOperatingMode> getOperatingModeAttribute() {
        return this.operatingMode;
    }

    public WaterHeaterOperatingMode getOperatingMode() {
        return this.operatingMode.getDisplayValue();
    }

    public void setOperatingMode(final WaterHeaterOperatingMode operatingMode) {
        Objects.requireNonNull(operatingMode);

        this.operatingMode.setTargetValue(operatingMode);
    }

    public FeatureAttribute<Boolean> getIsOnAttribute() {
        return this.isOn;
    }

    public boolean isOn() {
        return this.isOn.getDisplayValue();
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
