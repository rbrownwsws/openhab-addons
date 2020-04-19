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

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.FeatureAttribute;
import org.openhab.binding.hive.internal.client.Link;
import org.openhab.binding.hive.internal.client.ReverseLink;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class LinksFeature implements Feature {
    private final @Nullable FeatureAttribute<Set<Link>> links;
    private final @Nullable FeatureAttribute<Set<ReverseLink>> reverseLinks;

    public LinksFeature(
            final @Nullable FeatureAttribute<Set<Link>> links,
            final @Nullable FeatureAttribute<Set<ReverseLink>> reverseLinks
    ) {
        this.links = links;
        this.reverseLinks = reverseLinks;
    }

    public final @Nullable FeatureAttribute<Set<Link>> getLinksAttribute() {
        return this.links;
    }

    public final Set<Link> getLinks() {
        final @Nullable FeatureAttribute<Set<Link>> links = this.links;
        if (links != null) {
            return links.getDisplayValue();
        } else {
            return Collections.emptySet();
        }
    }

    public final @Nullable FeatureAttribute<Set<ReverseLink>> getReverseLinksAttribute() {
        return this.reverseLinks;
    }

    public final Set<ReverseLink> getReverseLinks() {
        final @Nullable FeatureAttribute<Set<ReverseLink>> reverseLinks = this.reverseLinks;

        if (reverseLinks != null) {
            return reverseLinks.getDisplayValue();
        } else {
            return Collections.emptySet();
        }
    }
}
