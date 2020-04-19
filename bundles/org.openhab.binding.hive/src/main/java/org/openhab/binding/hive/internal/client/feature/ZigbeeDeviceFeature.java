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

    public ZigbeeDeviceFeature(
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

    public FeatureAttribute<Eui64> getEui64Attribute() {
        return this.eui64;
    }

    public Eui64 getEui64() {
        return this.eui64.getDisplayValue();
    }

    public FeatureAttribute<Integer> getAverageLQIAttribute() {
        return this.averageLQI;
    }

    public int getAverageLQI() {
        return this.averageLQI.getDisplayValue();
    }

    public FeatureAttribute<Integer> getLastKnownLQIAttribute() {
        return this.lastKnownLQI;
    }

    public int getLastKnownLQI() {
        return this.lastKnownLQI.getDisplayValue();
    }

    public FeatureAttribute<Integer> getAverageRSSIAttribute() {
        return this.averageRSSI;
    }

    public int getAverageRSSI() {
        return this.averageRSSI.getDisplayValue();
    }

    public FeatureAttribute<Integer> getLastKnownRSSIAttribute() {
        return this.lastKnownRSSI;
    }

    public int getLastKnownRSSI() {
        return this.lastKnownRSSI.getDisplayValue();
    }
}
