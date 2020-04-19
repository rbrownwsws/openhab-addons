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
public final class ScheduleType extends SimpleValueTypeBase<URI> {
    public static final ScheduleType WEEKLY = new ScheduleType(URI.create("http://alertme.com/schema/json/configuration/configuration.device.schedule.weekly.v1.json#"));

    public ScheduleType(final URI scheduleType) {
        super(scheduleType);
    }
}
