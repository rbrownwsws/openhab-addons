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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.dto.HiveApiInstant;

import java.io.IOException;
import java.time.Instant;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class HiveApiInstantGsonAdapter extends TypeAdapter<HiveApiInstant> {
    @NonNullByDefault({})
    @Override
    public void write(final JsonWriter out, final @Nullable HiveApiInstant hiveApiInstant) throws IOException {
        if (hiveApiInstant != null) {
            out.value(hiveApiInstant.asInstant().toEpochMilli());
        } else {
            out.nullValue();
        }
    }

    @NonNullByDefault({})
    @Override
    public HiveApiInstant read(final JsonReader in) throws IOException {
        return new HiveApiInstant(Instant.ofEpochMilli(in.nextLong()));
    }
}
