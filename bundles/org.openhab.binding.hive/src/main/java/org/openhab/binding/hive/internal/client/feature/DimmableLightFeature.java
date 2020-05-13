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
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.BrightnessLevel;
import org.openhab.binding.hive.internal.client.BuilderUtil;
import org.openhab.binding.hive.internal.client.SettableFeatureAttribute;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 * @author John McLaughlin - 
 * 
 */
@NonNullByDefault
public final class DimmableLightFeature implements Feature {
    private final SettableFeatureAttribute<BrightnessLevel> brightnessLevel;

    private DimmableLightFeature(
            final SettableFeatureAttribute<BrightnessLevel> brightnessLevel
    ) {
        Objects.requireNonNull(brightnessLevel);

        this.brightnessLevel = brightnessLevel;
    }

    public SettableFeatureAttribute<BrightnessLevel> getBrightnessLevel() {
        return this.brightnessLevel;
    }

    public DimmableLightFeature withTargetBrightness(final BrightnessLevel targetBrightnessLevel) {
        Objects.requireNonNull(targetBrightnessLevel);

        return DimmableLightFeature.builder().from(this).brightnessLevel(this.brightnessLevel.withTargetValue(targetBrightnessLevel)).build();
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable SettableFeatureAttribute<BrightnessLevel> brightnessLevel;

        public Builder from(final DimmableLightFeature dimmableLightFeature) {
            Objects.requireNonNull(dimmableLightFeature);

            return this.brightnessLevel(dimmableLightFeature.getBrightnessLevel());
        }

        public Builder brightnessLevel(final SettableFeatureAttribute<BrightnessLevel> settableFeatureAttribute) {
            this.brightnessLevel = Objects.requireNonNull(settableFeatureAttribute);

            return this;
        }

        public DimmableLightFeature build() {
            final @Nullable SettableFeatureAttribute<BrightnessLevel> brightnessLevel = this.brightnessLevel;

            if (brightnessLevel == null) {
                throw new IllegalStateException(BuilderUtil.REQUIRED_ATTRIBUTE_NOT_SET_MESSAGE);
            }

            return new DimmableLightFeature(brightnessLevel);
        }
    }
}
