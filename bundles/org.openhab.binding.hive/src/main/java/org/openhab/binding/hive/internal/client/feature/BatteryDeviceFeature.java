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
import org.openhab.binding.hive.internal.client.BatteryLevel;
import org.openhab.binding.hive.internal.client.FeatureAttribute;

import javax.measure.Quantity;
import javax.measure.quantity.ElectricPotential;
import java.util.Objects;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class BatteryDeviceFeature implements Feature {
    private final FeatureAttribute<BatteryLevel> batteryLevel;
    private final FeatureAttribute<String> batteryState;
    private final FeatureAttribute<Quantity<ElectricPotential>> batteryVoltage;
    private final FeatureAttribute<String> batteryNotificationState;

    public BatteryDeviceFeature(
            final FeatureAttribute<BatteryLevel> batteryLevel,
            final FeatureAttribute<String> batteryState,
            final FeatureAttribute<Quantity<ElectricPotential>> batteryVoltage,
            final FeatureAttribute<String> batteryNotificationState
    ) {
        Objects.requireNonNull(batteryLevel);
        Objects.requireNonNull(batteryState);
        Objects.requireNonNull(batteryVoltage);
        Objects.requireNonNull(batteryNotificationState);

        this.batteryLevel = batteryLevel;
        this.batteryState = batteryState;
        this.batteryVoltage = batteryVoltage;
        this.batteryNotificationState = batteryNotificationState;
    }

    public FeatureAttribute<BatteryLevel> getBatteryLevelAttribute() {
        return this.batteryLevel;
    }

    public BatteryLevel getBatteryLevel() {
        return this.batteryLevel.getDisplayValue();
    }

    public FeatureAttribute<String> getBatteryStateAttribute() {
        return this.batteryState;
    }

    public String getBatteryState() {
        return this.batteryState.getDisplayValue();
    }

    public FeatureAttribute<Quantity<ElectricPotential>> getBatteryVoltageAttribute() {
        return this.batteryVoltage;
    }

    public Quantity<ElectricPotential> getBatteryVoltage() {
        return this.batteryVoltage.getDisplayValue();
    }

    public FeatureAttribute<String> getBatteryNotificationStateAttribute() {
        return this.batteryNotificationState;
    }

    public String getBatteryNotificationState() {
        return this.batteryNotificationState.getDisplayValue();
    }
}
