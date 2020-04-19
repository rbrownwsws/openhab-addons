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
 * Represents a media type used by the Hive API.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class MediaType extends SimpleValueTypeBase<String> {
    public static final MediaType JSON = new MediaType("application/json");
    public static final MediaType API_V6_0_0_JSON = new MediaType("application/vnd.alertme.zoo-6.0.0+json");
    public static final MediaType API_V6_1_0_JSON = new MediaType("application/vnd.alertme.zoo-6.1.0+json");
    public static final MediaType API_V6_2_0_JSON = new MediaType("application/vnd.alertme.zoo-6.2.0+json");
    public static final MediaType API_V6_3_0_JSON = new MediaType("application/vnd.alertme.zoo-6.3.0+json");
    public static final MediaType API_V6_4_0_JSON = new MediaType("application/vnd.alertme.zoo-6.4.0+json");
    public static final MediaType API_V6_5_0_JSON = new MediaType("application/vnd.alertme.zoo-6.5.0+json");
    public static final MediaType API_V6_6_0_JSON = new MediaType("application/vnd.alertme.zoo-6.6.0+json");

    public MediaType(final String mediaType) {
        super(mediaType);
    }
}
