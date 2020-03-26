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

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public class FeatureAttribute<T> {
    private final T reportedValue;
    private final Instant reportReceivedTime;
    private final Instant reportChangedTime;

    private final T displayValue;

    public FeatureAttribute(
            final T reportedValue,
            final Instant reportChangedTime,
            final Instant reportReceivedTime,
            final T displayValue
    ) {
        Objects.requireNonNull(reportedValue);
        Objects.requireNonNull(reportChangedTime);
        Objects.requireNonNull(reportReceivedTime);
        Objects.requireNonNull(displayValue);

        this.reportedValue = reportedValue;
        this.reportChangedTime = reportChangedTime;
        this.reportReceivedTime = reportReceivedTime;
        this.displayValue = displayValue;
    }

    public final T getReportedValue() {
        return this.reportedValue;
    }

    public final Instant getReportReceivedTime() {
        return this.reportReceivedTime;
    }

    public final Instant getReportChangedTime() {
        return this.reportChangedTime;
    }

    public final T getDisplayValue() {
        return this.displayValue;
    }
}
