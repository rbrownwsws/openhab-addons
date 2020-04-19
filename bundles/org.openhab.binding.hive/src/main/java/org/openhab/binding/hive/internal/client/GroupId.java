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

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class GroupId extends SimpleValueTypeBase<String> {
    /**
     * Group that links
     * Synthetic TRV Heating Zone --> Physical TRV Device
     */
    public static final GroupId TRVS = new GroupId("trvs");

    /**
     * Group that links
     * Synthetic TRV Heating Zone --> Parent Synthetic Thermostat Heating Zone
     */
    public static final GroupId TRVBM = new GroupId("trvbm");

    public GroupId(final String groupId) {
        super(groupId);
    }
}
