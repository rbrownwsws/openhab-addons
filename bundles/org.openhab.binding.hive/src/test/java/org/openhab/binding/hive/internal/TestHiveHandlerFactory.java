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

import static org.mockito.Mockito.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Before;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public class TestHiveHandlerFactory {
    private @Nullable HiveHandlerFactory handlerFactory;

    @Before
    public void setUp() {
        handlerFactory = new HiveHandlerFactory();
    }
}
