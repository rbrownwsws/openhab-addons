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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public abstract class GsonAdapterTestBase<T extends TypeAdapter<?>> {
    @NonNullByDefault({})
    @Mock
    protected JsonWriter jsonWriter;

    @Before
    public void setUp() {
        initMocks(this);
    }

    protected abstract T getAdapter();

    /**
     * Makes sure we actually output null instead of throwing a NPE.
     */
    @Test
    public void testNullValue() throws IOException {
        /* Given */
        final T adapter = getAdapter();


        /* When */
        adapter.write(this.jsonWriter, null);


        /* Then */
        verify(this.jsonWriter, times(1)).nullValue();
    }
}
