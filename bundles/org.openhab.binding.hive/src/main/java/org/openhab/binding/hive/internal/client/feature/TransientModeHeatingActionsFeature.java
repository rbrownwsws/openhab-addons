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
import org.openhab.binding.hive.internal.client.SettableFeatureAttribute;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class TransientModeHeatingActionsFeature implements Feature {
    private final SettableFeatureAttribute<Quantity<Temperature>> boostTargetTemperature;

    public TransientModeHeatingActionsFeature(
            final SettableFeatureAttribute<Quantity<Temperature>> boostTargetTemperature
    ) {
        Objects.requireNonNull(boostTargetTemperature);

        this.boostTargetTemperature = boostTargetTemperature;
    }

    public final SettableFeatureAttribute<Quantity<Temperature>> getBoostTargetTemperatureAttribute() {
        return this.boostTargetTemperature;
    }

    public final Quantity<Temperature> getBoostTargetTemperature() {
        return this.boostTargetTemperature.getDisplayValue();
    }

    public final void setBoostTargetTemperature(final Quantity<Temperature> boostTargetTemperature) {
        Objects.requireNonNull(boostTargetTemperature);

        this.boostTargetTemperature.setTargetValue(boostTargetTemperature);
    }
}
