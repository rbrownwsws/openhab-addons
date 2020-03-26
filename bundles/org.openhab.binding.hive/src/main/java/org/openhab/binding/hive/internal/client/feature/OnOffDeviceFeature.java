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
package org.openhab.binding.hive.internal.client.feature;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.hive.internal.client.OnOffMode;
import org.openhab.binding.hive.internal.client.SettableFeatureAttribute;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class OnOffDeviceFeature implements Feature {
    private final SettableFeatureAttribute<OnOffMode> mode;

    public OnOffDeviceFeature(
            final SettableFeatureAttribute<OnOffMode> mode
    ) {
        Objects.requireNonNull(mode);

        this.mode = mode;
    }

    public SettableFeatureAttribute<OnOffMode> getModeAttribute() {
        return this.mode;
    }

    public OnOffMode getMode() {
        return this.mode.getDisplayValue();
    }

    public void setMode(final OnOffMode mode) {
        Objects.requireNonNull(mode);

        this.mode.setTargetValue(mode);
    }
}
