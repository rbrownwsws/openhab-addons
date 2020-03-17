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
import org.openhab.binding.hive.internal.client.Password;
import org.openhab.binding.hive.internal.client.SessionId;
import org.openhab.binding.hive.internal.client.UserId;
import org.openhab.binding.hive.internal.client.Username;

/**
 * A model of a "Session"
 *
 * Based on the Hive API Swagger model.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault({})
public final class SessionDto {
    /**
     * extCustomerLevel (integer, optional): The user's account level in a third party's database (default value 1)
     */
    public Integer extCustomerLevel;

    /**
     * href (string, optional, read only): URL of the API call for retrieving this object
     */
    public String href;

    /**
     * id (string, optional)
     */
    public SessionId id;

    /**
     * latestSupportedApiVersion (string, optional): The API version which the user's account and data are compatible with
     */
    public String latestSupportedApiVersion;

    /*
     * links (inline_model_33, optional): URL Templates for links to other entities e.g.: "users.nodes": "https://api.example.com/nodes/{users.nodes}" ,
     */

    /**
     * password (string, optional): Password
     */
    public Password password;

    /**
     * sessionDuration (integer, optional): Session duration in minutes
     */
    public Integer sessionDuration;

    /**
     * sessionId (string, optional): UUID of this session
     */
    public SessionId sessionId;

    /**
     * userId (string, optional): User UUID
     */
    public UserId userId;

    /**
     * username (string, optional): Username
     */
    public Username username;

    /**
     * uuid (string, optional): User UUID
     */
    public UserId uuid;
}
