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
package org.openhab.binding.hive.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@link HiveBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HiveBindingConstants {
    public static final String BINDING_ID = "hive";

    /* ######## Type UIDs ######## */
    public static final ThingTypeUID THING_TYPE_ACCOUNT = new ThingTypeUID(BINDING_ID, "account");
    public static final ThingTypeUID THING_TYPE_BOILER_MODULE = new ThingTypeUID(BINDING_ID, "boiler_module");
    public static final ThingTypeUID THING_TYPE_HEATING =  new ThingTypeUID(BINDING_ID, "heating");
    public static final ThingTypeUID THING_TYPE_HOT_WATER =  new ThingTypeUID(BINDING_ID, "hot_water");
    public static final ThingTypeUID THING_TYPE_HUB = new ThingTypeUID(BINDING_ID, "hub");
    public static final ThingTypeUID THING_TYPE_THERMOSTAT = new ThingTypeUID(BINDING_ID, "thermostat");
    public static final ThingTypeUID THING_TYPE_TRV = new ThingTypeUID(BINDING_ID, "trv");
    public static final ThingTypeUID THING_TYPE_TRV_GROUP = new ThingTypeUID(BINDING_ID, "trv_group");

    /**
     * The set of {@link ThingTypeUID}s supported by this binding.
     */
    // @formatter:off
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.unmodifiableSet(Stream.of(
            THING_TYPE_ACCOUNT,
            THING_TYPE_BOILER_MODULE,
            THING_TYPE_HEATING,
            THING_TYPE_HOT_WATER,
            THING_TYPE_HUB,
            THING_TYPE_THERMOSTAT,
            THING_TYPE_TRV,
            THING_TYPE_TRV_GROUP
    ).collect(Collectors.toSet()));
    // @formatter:on

    /**
     * The set of {@link ThingTypeUID}s that can be discovered
     * (everything but accounts).
     */
    public static final Set<ThingTypeUID> DISCOVERABLE_THING_TYPES_UIDS = Collections.unmodifiableSet(
            SUPPORTED_THING_TYPES_UIDS.stream()
                    .filter(it -> it != THING_TYPE_ACCOUNT)
                    .collect(Collectors.toSet())
    );

    /* ######## Channel ids ######## */
    public static final String CHANNEL_BATTERY_LEVEL = "battery-level";
    public static final String CHANNEL_BATTERY_LOW = "battery-low";
    public static final String CHANNEL_BATTERY_STATE = "battery-state";
    public static final String CHANNEL_BATTERY_VOLTAGE = "battery-voltage";
    public static final String CHANNEL_BATTERY_NOTIFICATION_STATE = "battery-notification_state";

    public static final String CHANNEL_MODE_ON_OFF = "mode-on_off";
    public static final String CHANNEL_MODE_OPERATING = "mode-operating";
    public static final String CHANNEL_MODE_OPERATING_OVERRIDE = "mode-operating-override";
    public static final String CHANNEL_STATE_OPERATING = "state-operating";
    public static final String CHANNEL_TEMPERATURE_CURRENT = "temperature-current";
    public static final String CHANNEL_TEMPERATURE_TARGET = "temperature-target";
    public static final String CHANNEL_TEMPERATURE_TARGET_BOOST = "temperature-target-boost";
    public static final String CHANNEL_IS_ON = "is_on";
    public static final String CHANNEL_TRANSIENT_DURATION = "transient-duration";
    public static final String CHANNEL_TRANSIENT_REMAINING = "transient-remaining";
    public static final String CHANNEL_TRANSIENT_ENABLED = "transient-enabled";
    public static final String CHANNEL_TRANSIENT_START_TIME = "transient-start_time";
    public static final String CHANNEL_TRANSIENT_END_TIME = "transient-end_time";
    public static final String CHANNEL_RADIO_LQI_AVERAGE = "radio-lqi-average";
    public static final String CHANNEL_RADIO_LQI_LAST_KNOWN = "radio-lqi-last_known";
    public static final String CHANNEL_RADIO_RSSI_AVERAGE = "radio-rssi-average";
    public static final String CHANNEL_RADIO_RSSI_LAST_KNOWN = "radio-rssi-last_known";


    /* ######## Config params ######## */
    public static final String CONFIG_NODE_ID = "nodeId";

    public static final Duration SETTLE_TIME = Duration.ofSeconds(5);

    private HiveBindingConstants() {
        throw new AssertionError();
    }
}
