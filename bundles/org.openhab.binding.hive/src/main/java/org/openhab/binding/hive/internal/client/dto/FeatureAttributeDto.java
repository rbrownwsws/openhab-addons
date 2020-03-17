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

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault({})
public final class FeatureAttributeDto<T> {
    public T reportedValue;
    public T targetValue;
    public T displayValue;

    public HiveApiInstant reportReceivedTime;
    public HiveApiInstant reportChangedTime;
    public HiveApiInstant targetSetTime;
    public HiveApiInstant targetExpiryTime;

    public String targetSetTXId;
    public String propertyStatus;
}
