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

import java.time.Instant;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.dto.FeatureAttributeDto;
import org.openhab.binding.hive.internal.client.dto.HiveApiInstant;
import org.openhab.binding.hive.internal.client.exception.HiveClientResponseException;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class FeatureAttributeFactory {
    private static final String FEATURE_ATTRIBUTE_NULL_MESSAGE = "FeatureAttribute is unexpectedly null";

    @FunctionalInterface
    public interface Adapter<F, T> {
        T adapt(F from);
    }

    @FunctionalInterface
    private interface Factory<A extends FeatureAttribute<T>, T> {
        A create(
                T reportedValue,
                Instant reportChangedTime,
                Instant reportReceivedTime,
                T displayValue
        );
    }

    private FeatureAttributeFactory() {
        throw new AssertionError();
    }

    private static <F, A extends FeatureAttribute<T>, T> A createFeatureAttribute(
            final Adapter<F, T> adapter,
            final @Nullable FeatureAttributeDto<F> dto,
            final Factory<A, T> factory
    ) {
        Objects.requireNonNull(adapter);
        Objects.requireNonNull(factory);

        if (dto == null) {
            throw new HiveClientResponseException(FEATURE_ATTRIBUTE_NULL_MESSAGE);
        }

        final @Nullable F reportedValue = dto.reportedValue;
        final @Nullable HiveApiInstant reportChangedTime = dto.reportChangedTime;
        final @Nullable HiveApiInstant reportReceivedTime = dto.reportReceivedTime;
        final @Nullable F displayValue = dto.displayValue;

        if (reportedValue == null) {
            throw new HiveClientResponseException("Reported value is unexpectedly null.");
        }
        if (reportChangedTime == null) {
            throw new HiveClientResponseException("Report Changed Time is unexpectedly null.");
        }
        if (reportReceivedTime == null) {
            throw new HiveClientResponseException("Reported Received Time is unexpectedly null.");
        }
        if (displayValue == null) {
            throw new HiveClientResponseException("Display value is unexpectedly null.");
        }

        return factory.create(
                adapter.adapt(reportedValue),
                reportChangedTime.asInstant(),
                reportReceivedTime.asInstant(),
                adapter.adapt(displayValue)
        );
    }

    public static <T> FeatureAttribute<T> getReadOnlyFromDto(final @Nullable FeatureAttributeDto<T> dto) {
        return getReadOnlyFromDtoWithAdapter((val) -> val, dto);
    }

    public static <F, T> FeatureAttribute<T> getReadOnlyFromDtoWithAdapter(
            final Adapter<F, T> adapter,
            final @Nullable FeatureAttributeDto<F> dto
    ) {
        // N.B. Type parameter because Checker Framework needs a little help.
        return FeatureAttributeFactory.<F, FeatureAttribute<T>, T>createFeatureAttribute(
                adapter,
                dto,
                FeatureAttribute::new
        );
    }

    public static <T> SettableFeatureAttribute<T> getSettableFromDto(final @Nullable FeatureAttributeDto<T> dto) {
        return getSettableFromDtoWithAdapter((val) -> val, dto);
    }

    public static <F, T> SettableFeatureAttribute<T> getSettableFromDtoWithAdapter(
            final Adapter<F, T> adapter,
            final @Nullable FeatureAttributeDto<F> dto
    ) {
        // N.B. Type parameter because Checker Framework needs a little help.
        return FeatureAttributeFactory.<F, SettableFeatureAttribute<T>, T>createFeatureAttribute(
                adapter,
                dto,
                SettableFeatureAttribute::new
        );
    }
}
