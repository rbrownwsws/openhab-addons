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
package org.openhab.binding.hive.internal.client.adapter;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.hive.internal.client.ActionType;

import com.google.gson.stream.JsonReader;

/**
 * A gson {@link com.google.gson.TypeAdapter} for {@link ActionType}.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class ActionTypeGsonAdapter extends SimpleGsonTypeAdapterBase<ActionType> {
    @NonNullByDefault({})
    @Override
    public ActionType read(final JsonReader in) throws IOException {
        return new ActionType(in.nextString());
    }
}
