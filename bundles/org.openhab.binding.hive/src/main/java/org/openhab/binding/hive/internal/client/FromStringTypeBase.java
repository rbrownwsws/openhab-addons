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

import java.util.Objects;

/**
 * A base class for simple value types constructed from strings.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
abstract class FromStringTypeBase<T> {
    private final T value;

    public FromStringTypeBase(final String stringValue) {
        Objects.requireNonNull(stringValue);

        this.value = transform(stringValue);

        validate(this.value);
    }

    protected abstract T transform(final String stringValue);

    protected void validate(final T value) {}

    @Override
    public String toString() {
        return value.toString();
    }

    @NonNullByDefault({})
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final FromStringTypeBase<?> other = (FromStringTypeBase<?>) o;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
