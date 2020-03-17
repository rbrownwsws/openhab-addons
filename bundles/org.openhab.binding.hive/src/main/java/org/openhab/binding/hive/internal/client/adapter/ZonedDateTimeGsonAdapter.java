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

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *2020-03-04T19:13:47.462+0000
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class ZonedDateTimeGsonAdapter extends TypeAdapter<ZonedDateTime> {
    private static final DateTimeFormatter HIVE_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @NonNullByDefault({})
    @Override
    public void write(final JsonWriter out, final @Nullable ZonedDateTime zonedDateTime) throws IOException {
        if (zonedDateTime != null) {
            out.value(zonedDateTime.format(HIVE_DATETIME_FORMAT));
        } else {
            out.nullValue();
        }
    }

    @NonNullByDefault({})
    @Override
    public ZonedDateTime read(final JsonReader in) throws IOException {
        return ZonedDateTime.parse(in.nextString(), HIVE_DATETIME_FORMAT);
    }
}
