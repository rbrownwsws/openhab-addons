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

    public PhysicalDeviceFeature(
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

    public FeatureAttribute<String> getHardwareIdentifierAttribute() {
        return this.hardwareIdentifier;
    }

    public String getHardwareIdentifier() {
        return this.hardwareIdentifier.getDisplayValue();
    }

    public FeatureAttribute<String> getModelAttribute() {
        return this.model;
    }

    public String getModel() {
        return this.model.getDisplayValue();
    }

    public FeatureAttribute<String> getManufacturerAttribute() {
        return this.manufacturer;
    }

    public String getManufacturer() {
        return this.manufacturer.getDisplayValue();
    }

    public FeatureAttribute<String> getSoftwareVersionAttribute() {
        return this.softwareVersion;
    }

    public String getSoftwareVersion() {
        return this.softwareVersion.getDisplayValue();
    }
}
