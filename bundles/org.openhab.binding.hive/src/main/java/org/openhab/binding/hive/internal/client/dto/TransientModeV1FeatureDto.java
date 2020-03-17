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
package org.openhab.binding.hive.internal.client.dto;

import org.eclipse.jdt.annotation.NonNullByDefault;

import java.time.ZonedDateTime;
import java.util.List;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault({})
public final class TransientModeV1FeatureDto extends FeatureDtoBase {
    public FeatureAttributeDto<List<ActionDto>> actions;
    public FeatureAttributeDto<Long> duration;
    public FeatureAttributeDto<Boolean> isEnabled;
    public FeatureAttributeDto<List<ActionDto>> previousConfiguration;
    public FeatureAttributeDto<ZonedDateTime> startDatetime;
    public FeatureAttributeDto<ZonedDateTime> endDatetime;
}
