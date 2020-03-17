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
package org.openhab.binding.hive.internal.client;

import org.eclipse.jdt.annotation.NonNullByDefault;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class Temperature {
    public enum Unit {
        CELSIUS
    }

    private final BigDecimal value;
    private final Unit unit;

    /**
     * Create a new temperature using the default unit of Celsius.
     *
     * @param value
     *      The value of the temperature.
     */
    public Temperature(final BigDecimal value) {
        this.value = value;
        this.unit = Unit.CELSIUS;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Unit getUnit() {
        return this.unit;
    }

    @Override
    public String toString() {
        String out = this.value.toString();

        switch (this.unit) {
            case CELSIUS:
                out += "\u00B0C";
                break;
        }

        return out;
    }

    @NonNullByDefault({})
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Temperature that = (Temperature) o;
        return value.equals(that.value) &&
                unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }
}
