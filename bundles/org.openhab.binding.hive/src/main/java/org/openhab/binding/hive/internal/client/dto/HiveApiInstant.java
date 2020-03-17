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
package org.openhab.binding.hive.internal.client.dto;

import org.eclipse.jdt.annotation.NonNullByDefault;

import java.time.Instant;
import java.util.Objects;

/**
 * A class representing absolute times returned by the Hive API.
 *
 * <p>
 *     N.B. This should only be used by DTOs.
 * </p>
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HiveApiInstant {
    private Instant instant;

    public HiveApiInstant(final Instant instant) {
        Objects.requireNonNull(instant);

        this.instant = instant;
    }

    public Instant asInstant() {
        return this.instant;
    }

    @NonNullByDefault({})
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final HiveApiInstant that = (HiveApiInstant) o;
        return instant.equals(that.instant);
    }

    @NonNullByDefault({})
    @Override
    public int hashCode() {
        return Objects.hash(instant);
    }
}
