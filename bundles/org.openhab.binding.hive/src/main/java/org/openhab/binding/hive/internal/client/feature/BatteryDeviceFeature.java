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
import javax.measure.quantity.ElectricPotential;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.BatteryLevel;
import org.openhab.binding.hive.internal.client.BuilderUtil;
import org.openhab.binding.hive.internal.client.FeatureAttribute;

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

    private BatteryDeviceFeature(
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

    public FeatureAttribute<BatteryLevel> getBatteryLevel() {
        return this.batteryLevel;
    }

    public FeatureAttribute<String> getBatteryState() {
        return this.batteryState;
    }

    public FeatureAttribute<Quantity<ElectricPotential>> getBatteryVoltage() {
        return this.batteryVoltage;
    }

    public FeatureAttribute<String> getBatteryNotificationState() {
        return this.batteryNotificationState;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable FeatureAttribute<BatteryLevel> batteryLevel;
        private @Nullable FeatureAttribute<String> batteryState;
        private @Nullable FeatureAttribute<Quantity<ElectricPotential>> batteryVoltage;
        private @Nullable FeatureAttribute<String> batteryNotificationState;

        public Builder from(final BatteryDeviceFeature batteryDeviceFeature) {
            Objects.requireNonNull(batteryDeviceFeature);

            return this.batteryLevel(batteryDeviceFeature.getBatteryLevel())
                    .batteryState(batteryDeviceFeature.getBatteryState())
                    .batteryVoltage(batteryDeviceFeature.getBatteryVoltage())
                    .batteryNotificationState(batteryDeviceFeature.getBatteryNotificationState());
        }

        public Builder batteryLevel(final FeatureAttribute<BatteryLevel> batteryLevel) {
            this.batteryLevel = Objects.requireNonNull(batteryLevel);

            return this;
        }

        public Builder batteryState(final FeatureAttribute<String> batteryState) {
            this.batteryState = Objects.requireNonNull(batteryState);

            return this;
        }

        public Builder batteryVoltage(final FeatureAttribute<Quantity<ElectricPotential>> batteryVoltage) {
            this.batteryVoltage = Objects.requireNonNull(batteryVoltage);

            return this;
        }

        public Builder batteryNotificationState(final FeatureAttribute<String> batteryNotificationState) {
            this.batteryNotificationState = Objects.requireNonNull(batteryNotificationState);

            return this;
        }

        public BatteryDeviceFeature build() {
            final @Nullable FeatureAttribute<BatteryLevel> batteryLevel = this.batteryLevel;
            final @Nullable FeatureAttribute<String> batteryState = this.batteryState;
            final @Nullable FeatureAttribute<Quantity<ElectricPotential>> batteryVoltage = this.batteryVoltage;
            final @Nullable FeatureAttribute<String> batteryNotificationState = this.batteryNotificationState;

            if (batteryLevel == null
                    || batteryState == null
                    || batteryVoltage == null
                    || batteryNotificationState == null
            ) {
                throw new IllegalStateException(BuilderUtil.REQUIRED_ATTRIBUTE_NOT_SET_MESSAGE);
            }

            return new BatteryDeviceFeature(
                    batteryLevel,
                    batteryState,
                    batteryVoltage,
                    batteryNotificationState
            );
        }
    }
}
