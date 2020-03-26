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
 * A facade for HTTP requests to the Hive API
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public interface HiveApiRequest {
    HiveApiRequest accept(MediaType mediaType);

    /**
     *
     * @throws org.openhab.binding.hive.internal.client.exception.HiveClientRequestException
     */
    HiveApiResponse get();

    /**
     *
     * @throws org.openhab.binding.hive.internal.client.exception.HiveClientRequestException
     */
    HiveApiResponse post(Object requestBody);

    /**
     *
     * @throws org.openhab.binding.hive.internal.client.exception.HiveClientRequestException
     */
    HiveApiResponse put(Object requestBody);

    /**
     *
     * @throws org.openhab.binding.hive.internal.client.exception.HiveClientRequestException
     */
    HiveApiResponse delete();
}
