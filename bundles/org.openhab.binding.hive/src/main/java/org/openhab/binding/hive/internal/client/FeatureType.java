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

import java.net.URI;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class FeatureType extends SimpleValueTypeBase<URI> {
    public static final FeatureType BATTERY_DEVICE_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.battery_device.v1.json#"));
    public static final FeatureType CHILD_LOCK_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.child_lock.v1.json#"));
    public static final FeatureType DEVICE_MANAGEMENT_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.device_management.v1.json#"));
    public static final FeatureType DISPLAY_ORIENTATION_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.display_orientation.v1.json#"));
    public static final FeatureType ETHERNET_DEVICE_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.ethernet_device.v1.json#"));
    public static final FeatureType FROST_PROTECT_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.frost_protect.v1.json#"));
    public static final FeatureType GROUP_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.group.v1.json#"));
    public static final FeatureType HEATING_TEMPERATURE_CONTROL_DEVICE_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.heating_temperature_control_device.v1.json#"));
    public static final FeatureType HEATING_TEMPERATURE_CONTROL_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.heating_temperature_control.v1.json#"));
    public static final FeatureType HEATING_THERMOSTAT_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.heating_thermostat.v1.json#"));
    public static final FeatureType HIVE_HUB_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.hive_hub.v1.json#"));
    public static final FeatureType LIFECYCLE_STATE_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.lifecycle_state.v1.json#"));
    public static final FeatureType LINKS_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.links.v1.json#"));
    public static final FeatureType MOUNTING_MODE_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.mounting_mode.v1.json#"));
    public static final FeatureType ON_OFF_DEVICE_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.on_off_device.v1.json#"));
    public static final FeatureType PI_HEATING_DEMAND_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.pi_heating_demand.v1.json#"));
    public static final FeatureType PHYSICAL_DEVICE_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.physical_device.v1.json#"));
    public static final FeatureType RADIO_DEVICE_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.radio_device.v1.json#"));
    public static final FeatureType STANDBY_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.standby.v1.json#"));
    public static final FeatureType TEMPERATURE_SENSOR_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.temperature_sensor.v1.json#"));
    public static final FeatureType TRANSIENT_MODE_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.transient_mode.v1.json#"));
    public static final FeatureType TRV_CALIBRATION_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.trv_calibration.v1.json#"));
    public static final FeatureType TRV_ERROR_DIAGNOSTICS_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.trv_error_diagnostics.v1.json#"));
    public static final FeatureType WATER_HEATER_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.water_heater.v1.json#"));
    public static final FeatureType ZIGBEE_DEVICE_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.zigbee_device.v1.json#"));
    public static final FeatureType ZIGBEE_ROUTING_DEVICE_V1 = new FeatureType(URI.create("http://alertme.com/schema/json/feature/node.feature.zigbee_routing_device.v1.json#"));

    public FeatureType(final URI featureType) {
        super(featureType);
    }
}
