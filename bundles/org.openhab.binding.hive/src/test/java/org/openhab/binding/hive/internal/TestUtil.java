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
package org.openhab.binding.hive.internal;

import java.io.*;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public class TestUtil {
    private TestUtil() {
        throw new AssertionError();
    }

    public static String getResourceAsString(final String resource) throws IOException {
        try (final InputStream inputStream = TestUtil.class.getResourceAsStream(resource); final Writer writer = new StringWriter()) {
            final Reader reader = new BufferedReader(new InputStreamReader(inputStream));

            char[] buffer = new char[1024];
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

            return writer.toString();
        }
    }
}
