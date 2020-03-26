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

import java.time.Instant;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class SettableFeatureAttribute<T> extends FeatureAttribute<T> {
    private @Nullable T targetValue = null;

    SettableFeatureAttribute(
            final T reportedValue,
            final Instant changedTime,
            final Instant receivedTime,
            final T displayValue
    ) {
        super(reportedValue,changedTime,receivedTime,displayValue);
    }

    public void setTargetValue(final T targetValue) {
        Objects.requireNonNull(targetValue);

        this.targetValue = targetValue;
    }

    public void clearTargetValue() {
        this.targetValue = null;
    }

    public @Nullable T getTargetValue() {
        return this.targetValue;
    }
}
