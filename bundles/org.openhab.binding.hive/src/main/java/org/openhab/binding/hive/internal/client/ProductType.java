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
public final class ProductType extends StringTypeBase {
    public static final ProductType BOILER_MODULE = new ProductType("BOILER_MODULE");
    public static final ProductType DAYLIGHT_SD = new ProductType("DAYLIGHT_SD");
    public static final ProductType HEATING = new ProductType("HEATING");
    public static final ProductType HOT_WATER = new ProductType("HOT_WATER");
    public static final ProductType HUB = new ProductType("HUB");
    public static final ProductType THERMOSTAT_UI = new ProductType("THERMOSTAT_UI");
    public static final ProductType TRV = new ProductType("TRV");
    public static final ProductType TRV_GROUP = new ProductType("TRV_GROUP");

    public ProductType(final String stringValue) {
        super(stringValue);
    }
}
