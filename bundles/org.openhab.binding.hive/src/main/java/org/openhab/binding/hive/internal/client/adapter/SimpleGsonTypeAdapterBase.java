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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import java.io.IOException;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
abstract class SimpleGsonTypeAdapterBase<T> extends TypeAdapter<T> {
    @NonNullByDefault({})
    @Override
    public void write(final JsonWriter out, final @Nullable T value) throws IOException {
        if (value != null) {
            out.value(value.toString());
        } else {
            out.nullValue();
        }
    }
}
