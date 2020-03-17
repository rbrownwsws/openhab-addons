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
package org.openhab.binding.hive.internal.client;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.hive.internal.client.dto.FeatureAttributeDto;

import java.util.Objects;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class FeatureAttributeFactory {
    private FeatureAttributeFactory() {
        throw new AssertionError();
    }

    @FunctionalInterface
    public interface Adapter<F, T> {
        T adapt(F from);
    }

    public static <T> FeatureAttribute<T> getReadOnlyFromDto(final FeatureAttributeDto<T> dto) {
        Objects.requireNonNull(dto);

        return new FeatureAttribute<>(
                dto.reportedValue,
                dto.reportChangedTime.asInstant(),
                dto.reportReceivedTime.asInstant(),
                dto.displayValue
        );
    }

    public static <F, T> FeatureAttribute<T> getReadOnlyFromDtoWithAdapter(
            final Adapter<F, T> adapter,
            final FeatureAttributeDto<F> dto
    ) {
        Objects.requireNonNull(adapter);
        Objects.requireNonNull(dto);

        return new FeatureAttribute<>(
                adapter.adapt(dto.reportedValue),
                dto.reportChangedTime.asInstant(),
                dto.reportReceivedTime.asInstant(),
                adapter.adapt(dto.displayValue)
        );
    }

    public static <T> SettableFeatureAttribute<T> getSettableFromDto(final FeatureAttributeDto<T> dto) {
        Objects.requireNonNull(dto);

        return new SettableFeatureAttribute<>(
                dto.reportedValue,
                dto.reportChangedTime.asInstant(),
                dto.reportReceivedTime.asInstant(),
                dto.displayValue
        );
    }

    public static <F, T> SettableFeatureAttribute<T> getSettableFromDtoWithAdapter(
            final Adapter<F, T> adapter,
            final FeatureAttributeDto<F> dto
    ) {
        Objects.requireNonNull(adapter);
        Objects.requireNonNull(dto);

        return new SettableFeatureAttribute<>(
                adapter.adapt(dto.reportedValue),
                dto.reportChangedTime.asInstant(),
                dto.reportReceivedTime.asInstant(),
                adapter.adapt(dto.displayValue)
        );
    }
}
