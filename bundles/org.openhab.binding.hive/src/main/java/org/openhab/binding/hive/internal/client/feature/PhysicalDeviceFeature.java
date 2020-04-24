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
import org.openhab.binding.hive.internal.client.BuilderUtil;
import org.openhab.binding.hive.internal.client.FeatureAttribute;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class PhysicalDeviceFeature implements Feature {
    private final FeatureAttribute<String> hardwareIdentifier;
    private final FeatureAttribute<String> model;
    private final FeatureAttribute<String> manufacturer;
    private final FeatureAttribute<String> softwareVersion;

    private PhysicalDeviceFeature(
            final FeatureAttribute<String> hardwareIdentifier,
            final FeatureAttribute<String> model,
            final FeatureAttribute<String> manufacturer,
            final FeatureAttribute<String> softwareVersion
    ) {
        Objects.requireNonNull(hardwareIdentifier);
        Objects.requireNonNull(model);
        Objects.requireNonNull(manufacturer);
        Objects.requireNonNull(softwareVersion);

        this.hardwareIdentifier = hardwareIdentifier;
        this.model = model;
        this.manufacturer = manufacturer;
        this.softwareVersion = softwareVersion;
    }

    public FeatureAttribute<String> getHardwareIdentifier() {
        return this.hardwareIdentifier;
    }

    public FeatureAttribute<String> getModel() {
        return this.model;
    }

    public FeatureAttribute<String> getManufacturer() {
        return this.manufacturer;
    }

    public FeatureAttribute<String> getSoftwareVersion() {
        return this.softwareVersion;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static final class Builder {
        private @Nullable FeatureAttribute<String> hardwareIdentifier;
        private @Nullable FeatureAttribute<String> model;
        private @Nullable FeatureAttribute<String> manufacturer;
        private @Nullable FeatureAttribute<String> softwareVersion;

        public Builder from(final PhysicalDeviceFeature physicalDeviceFeature) {
            Objects.requireNonNull(physicalDeviceFeature);

            return this.hardwareIdentifier(physicalDeviceFeature.getHardwareIdentifier())
                    .model(physicalDeviceFeature.getModel())
                    .manufacturer(physicalDeviceFeature.getManufacturer())
                    .softwareVersion(physicalDeviceFeature.getSoftwareVersion());
        }

        public Builder hardwareIdentifier(final FeatureAttribute<String> hardwareIdentifier) {
            this.hardwareIdentifier = Objects.requireNonNull(hardwareIdentifier);

            return this;
        }

        public Builder model(final FeatureAttribute<String> model) {
            this.model = Objects.requireNonNull(model);

            return this;
        }

        public Builder manufacturer(final FeatureAttribute<String> manufacturer) {
            this.manufacturer = Objects.requireNonNull(manufacturer);

            return this;
        }

        public Builder softwareVersion(final FeatureAttribute<String> softwareVersion) {
            this.softwareVersion = Objects.requireNonNull(softwareVersion);

            return this;
        }
        
        public PhysicalDeviceFeature build() {
            final @Nullable FeatureAttribute<String> hardwareIdentifier = this.hardwareIdentifier;
            final @Nullable FeatureAttribute<String> model = this.model;
            final @Nullable FeatureAttribute<String> manufacturer = this.manufacturer;
            final @Nullable FeatureAttribute<String> softwareVersion = this.softwareVersion;

            if (hardwareIdentifier == null
                    || model == null
                    || manufacturer == null
                    || softwareVersion == null
            ) {
                throw new IllegalStateException(BuilderUtil.REQUIRED_ATTRIBUTE_NOT_SET_MESSAGE);
            }

            return new PhysicalDeviceFeature(
                    hardwareIdentifier,
                    model,
                    manufacturer,
                    softwareVersion
            );
        }
    }
}
