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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.hive.internal.client.adapter.*;
import org.openhab.binding.hive.internal.client.dto.HiveApiInstant;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
final class GsonJsonService implements JsonService {
    private final Gson gson;

    public GsonJsonService() {
        this.gson = (new GsonBuilder())
                .registerTypeAdapter(HiveApiInstant.class, new HiveApiInstantGsonAdapter())
                .registerTypeAdapter(UserId.class, new UserIdGsonAdapter())
                .registerTypeAdapter(SessionId.class, new SessionIdGsonAdapter())
                .registerTypeAdapter(Username.class, new UsernameGsonAdapter())
                .registerTypeAdapter(Password.class, new PasswordGsonAdapter())
                .registerTypeAdapter(NodeId.class, new NodeIdGsonAdapter())
                .registerTypeAdapter(NodeName.class, new NodeNameGsonAdapter())
                .registerTypeAdapter(NodeType.class, new NodeTypeGsonAdapter())
                .registerTypeAdapter(FeatureType.class, new FeatureTypeGsonAdapter())
                .registerTypeAdapter(ProductType.class, new ProductTypeGsonAdapter())
                .registerTypeAdapter(ActionType.class, new ActionTypeGsonAdapter())
                .registerTypeAdapter(AttributeName.class, new AttributeNameGsonAdapter())
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeGsonAdapter())
                .create();
    }

    @Override
    public String toJson(final Object object) {
        Objects.requireNonNull(object);

        return this.gson.toJson(object);
    }

    @Override
    public <T> T fromJson(final String json, final Class<T> classOfT) {
        Objects.requireNonNull(json);
        Objects.requireNonNull(classOfT);

        return this.gson.fromJson(json, classOfT);
    }
}
