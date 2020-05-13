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
 * Represents a Hive API node type.
 *
 * @author Ross Brown - Initial contribution
 * @author John McLaughlin - Added support for Hive Light Bulbs and Light Groups
 * 
 */
@NonNullByDefault
public enum NodeType {
    HUB,
    LIGHT,
    THERMOSTAT,
    THERMOSTAT_UI,
    RADIATOR_VALVE,
    SYNTHETIC_DAYLIGHT,
    SYNTHETIC_GROUP,
    SYNTHETIC_HOME_STATE,
    SYNTHETIC_RULE,

    UNEXPECTED
}
