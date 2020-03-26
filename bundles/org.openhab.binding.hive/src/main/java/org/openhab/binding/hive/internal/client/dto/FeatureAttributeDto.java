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
import org.eclipse.jdt.annotation.Nullable;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class FeatureAttributeDto<T> {
    public @Nullable T reportedValue;
    public @Nullable T targetValue;
    public @Nullable T displayValue;

    public @Nullable HiveApiInstant reportReceivedTime;
    public @Nullable HiveApiInstant reportChangedTime;
    public @Nullable HiveApiInstant targetSetTime;
    public @Nullable HiveApiInstant targetExpiryTime;

    public @Nullable String targetSetTXId;
    public @Nullable String propertyStatus;
}