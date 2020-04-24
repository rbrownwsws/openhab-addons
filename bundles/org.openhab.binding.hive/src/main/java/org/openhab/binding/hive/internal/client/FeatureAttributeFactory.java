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

import java.util.Objects;
import java.util.function.Function;

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

    private FeatureAttributeFactory() {
        throw new AssertionError();
    }

    private static <F, T> void buildFeatureAttribute(
            final DefaultFeatureAttribute.Builder<T> featureAttributeBuilder,
            final Function<F, T> adapter,
            final FeatureAttributeDto<F> dto
    ) {
        Objects.requireNonNull(featureAttributeBuilder);
        Objects.requireNonNull(adapter);
        Objects.requireNonNull(dto);

        final @Nullable F displayValue = dto.displayValue;
        final @Nullable F reportedValue = dto.reportedValue;
        final @Nullable HiveApiInstant reportChangedTime = dto.reportChangedTime;
        final @Nullable HiveApiInstant reportReceivedTime = dto.reportReceivedTime;

        if (displayValue == null) {
            throw new HiveClientResponseException("Display value is unexpectedly null.");
        }
        featureAttributeBuilder.displayValue(adapter.apply(displayValue));

        if (reportedValue == null) {
            throw new HiveClientResponseException("Reported value is unexpectedly null.");
        }
        featureAttributeBuilder.reportedValue(adapter.apply(reportedValue));

        if (reportChangedTime == null) {
            throw new HiveClientResponseException("Report Changed Time is unexpectedly null.");
        }
        featureAttributeBuilder.reportChangedTime(reportChangedTime.asInstant());

        if (reportReceivedTime == null) {
            throw new HiveClientResponseException("Reported Received Time is unexpectedly null.");
        }
        featureAttributeBuilder.reportReceivedTime(reportReceivedTime.asInstant());
    }

    public static <T> FeatureAttribute<T> getReadOnlyFromDto(final @Nullable FeatureAttributeDto<T> dto) {
        return getReadOnlyFromDtoWithAdapter(Function.identity(), dto);
    }

    public static <F, T> FeatureAttribute<T> getReadOnlyFromDtoWithAdapter(
            final Function<F, T> adapter,
            final @Nullable FeatureAttributeDto<F> dto
    ) {
        if (dto == null) {
            throw new HiveClientResponseException(FEATURE_ATTRIBUTE_NULL_MESSAGE);
        }

        final DefaultFeatureAttribute.Builder<T> featureAttributeBuilder = DefaultFeatureAttribute.builder();

        buildFeatureAttribute(
                featureAttributeBuilder,
                adapter,
                dto
        );

        return featureAttributeBuilder.build();
    }

    public static <T> SettableFeatureAttribute<T> getSettableFromDto(final @Nullable FeatureAttributeDto<T> dto) {
        return getSettableFromDtoWithAdapter(Function.identity(), dto);
    }

    public static <F, T> SettableFeatureAttribute<T> getSettableFromDtoWithAdapter(
            final Function<F, T> adapter,
            final @Nullable FeatureAttributeDto<F> dto
    ) {
        if (dto == null) {
            throw new HiveClientResponseException(FEATURE_ATTRIBUTE_NULL_MESSAGE);
        }

        final DefaultFeatureAttribute.Builder<T> featureAttributeBuilder = DefaultFeatureAttribute.builder();

        buildFeatureAttribute(
                featureAttributeBuilder,
                adapter,
                dto
        );

        final @Nullable F targetValue = dto.targetValue;
        if (targetValue != null) {
            featureAttributeBuilder.targetValue(adapter.apply(targetValue));
        }

        return featureAttributeBuilder.build();
    }
}
