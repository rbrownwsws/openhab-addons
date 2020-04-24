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
import org.openhab.binding.hive.internal.client.BuilderUtil;
import org.openhab.binding.hive.internal.client.Eui64;
import org.openhab.binding.hive.internal.client.FeatureAttribute;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class ZigbeeDeviceFeature implements Feature {
    private final FeatureAttribute<Eui64> eui64;
    private final FeatureAttribute<Integer> averageLQI;
    private final FeatureAttribute<Integer> lastKnownLQI;
    private final FeatureAttribute<Integer> averageRSSI;
    private final FeatureAttribute<Integer> lastKnownRSSI;

    private ZigbeeDeviceFeature(
            final FeatureAttribute<Eui64> eui64,
            final FeatureAttribute<Integer> averageLQI,
            final FeatureAttribute<Integer> lastKnownLQI,
            final FeatureAttribute<Integer> averageRSSI,
            final FeatureAttribute<Integer> lastKnownRSSI
    ) {
        Objects.requireNonNull(eui64);
        Objects.requireNonNull(averageLQI);
        Objects.requireNonNull(lastKnownLQI);
        Objects.requireNonNull(averageRSSI);
        Objects.requireNonNull(lastKnownRSSI);

        this.eui64 = eui64;
        this.averageLQI = averageLQI;
        this.lastKnownLQI = lastKnownLQI;
        this.averageRSSI = averageRSSI;
        this.lastKnownRSSI = lastKnownRSSI;
    }

    public FeatureAttribute<Eui64> getEui64() {
        return this.eui64;
    }

    public FeatureAttribute<Integer> getAverageLQI() {
        return this.averageLQI;
    }

    public FeatureAttribute<Integer> getLastKnownLQI() {
        return this.lastKnownLQI;
    }

    public FeatureAttribute<Integer> getAverageRSSI() {
        return this.averageRSSI;
    }

    public FeatureAttribute<Integer> getLastKnownRSSI() {
        return this.lastKnownRSSI;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static final class Builder {
        private @Nullable FeatureAttribute<Eui64> eui64;
        private @Nullable FeatureAttribute<Integer> averageLQI;
        private @Nullable FeatureAttribute<Integer> lastKnownLQI;
        private @Nullable FeatureAttribute<Integer> averageRSSI;
        private @Nullable FeatureAttribute<Integer> lastKnownRSSI;

        public Builder from(final ZigbeeDeviceFeature zigbeeDeviceFeature) {
            Objects.requireNonNull(zigbeeDeviceFeature);

            return this.eui64(zigbeeDeviceFeature.getEui64())
                    .averageLQI(zigbeeDeviceFeature.getAverageLQI())
                    .lastKnownLQI(zigbeeDeviceFeature.getLastKnownLQI())
                    .averageRSSI(zigbeeDeviceFeature.getAverageRSSI())
                    .lastKnownRSSI(zigbeeDeviceFeature.getLastKnownRSSI());
        }

        public Builder eui64(final FeatureAttribute<Eui64> eui64) {
            this.eui64 = Objects.requireNonNull(eui64);

            return this;
        }

        public Builder averageLQI(final FeatureAttribute<Integer> averageLQI) {
            this.averageLQI = Objects.requireNonNull(averageLQI);

            return this;
        }

        public Builder lastKnownLQI(final FeatureAttribute<Integer> lastKnownLQI) {
            this.lastKnownLQI = Objects.requireNonNull(lastKnownLQI);

            return this;
        }

        public Builder averageRSSI(final FeatureAttribute<Integer> averageRSSI) {
            this.averageRSSI = Objects.requireNonNull(averageRSSI);

            return this;
        }

        public Builder lastKnownRSSI(final FeatureAttribute<Integer> lastKnownRSSI) {
            this.lastKnownRSSI = Objects.requireNonNull(lastKnownRSSI);

            return this;
        }
        
        public ZigbeeDeviceFeature build() {
            final @Nullable FeatureAttribute<Eui64> eui64 = this.eui64;
            final @Nullable FeatureAttribute<Integer> averageLQI = this.averageLQI;
            final @Nullable FeatureAttribute<Integer> lastKnownLQI = this.lastKnownLQI;
            final @Nullable FeatureAttribute<Integer> averageRSSI = this.averageRSSI;
            final @Nullable FeatureAttribute<Integer> lastKnownRSSI = this.lastKnownRSSI;

            if (eui64 == null
                    || averageLQI == null
                    || lastKnownLQI == null
                    || averageRSSI == null
                    || lastKnownRSSI == null
            ) {
                throw new IllegalStateException(BuilderUtil.REQUIRED_ATTRIBUTE_NOT_SET_MESSAGE);
            }

            return new ZigbeeDeviceFeature(
                    eui64,
                    averageLQI,
                    lastKnownLQI,
                    averageRSSI,
                    lastKnownRSSI
            );
        }
    }
}
