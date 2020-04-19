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
package org.openhab.binding.hive.internal.handler.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.measure.Quantity;
import javax.measure.quantity.Temperature;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingProvider;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.eclipse.smarthome.core.thing.binding.ThingTypeProvider;
import org.eclipse.smarthome.core.thing.binding.builder.ChannelBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openhab.binding.hive.internal.HiveBindingConstants;
import org.openhab.binding.hive.internal.TestUtil;
import org.openhab.binding.hive.internal.client.*;
import org.openhab.binding.hive.internal.client.feature.Feature;
import org.openhab.binding.hive.internal.client.feature.HeatingThermostatFeature;

import org.openhab.binding.hive.internal.client.feature.OnOffDeviceFeature;
import tec.uom.se.quantity.Quantities;
import tec.uom.se.unit.Units;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public class HeatingThermostatEasyHandlerStrategyTest {
    @NonNullByDefault({})
    @Mock
    private Channel easyOperatingModeChannel;

    @NonNullByDefault({})
    @Mock
    private ChannelUID easyOperatingModeChannelUid;

    @NonNullByDefault({})
    @Mock
    private Channel easyBoostModeChannel;

    @NonNullByDefault({})
    @Mock
    private ChannelUID easyBoostModeChannelUid;

    @NonNullByDefault({})
    @Mock
    private Channel easyIsOnStateChannel;

    @NonNullByDefault({})
    @Mock
    private ChannelUID easyIsOnStateChannelUid;

    @NonNullByDefault({})
    @Mock
    private Thing thing;

    @NonNullByDefault({})
    @Mock
    private ThingHandlerCallback thingHandlerCallback;

    @Before
    public void setUp() {
        initMocks(this);

        when(this.thing.getChannel(HiveBindingConstants.CHANNEL_EASY_MODE_OPERATING)).thenReturn(this.easyOperatingModeChannel);
        when(this.easyOperatingModeChannel.getUID()).thenReturn(this.easyOperatingModeChannelUid);
        when(this.easyOperatingModeChannelUid.getId()).thenReturn(HiveBindingConstants.CHANNEL_EASY_MODE_OPERATING);

        when(this.thing.getChannel(HiveBindingConstants.CHANNEL_EASY_MODE_BOOST)).thenReturn(this.easyBoostModeChannel);
        when(this.easyBoostModeChannel.getUID()).thenReturn(this.easyBoostModeChannelUid);
        when(this.easyBoostModeChannelUid.getId()).thenReturn(HiveBindingConstants.CHANNEL_EASY_MODE_BOOST);

        when(this.thing.getChannel(HiveBindingConstants.CHANNEL_EASY_STATE_IS_ON)).thenReturn(this.easyIsOnStateChannel);
        when(this.easyIsOnStateChannel.getUID()).thenReturn(this.easyIsOnStateChannelUid);
        when(this.easyIsOnStateChannelUid.getId()).thenReturn(HiveBindingConstants.CHANNEL_EASY_STATE_IS_ON);
    }

    @Test
    public void testNormalUpdateScheduleHeatNoneOn() {
        /* Given */
        final HeatingThermostatEasyHandlerStrategy strategy = HeatingThermostatEasyHandlerStrategy.getInstance();

        final HeatingThermostatOperatingMode operatingMode = HeatingThermostatOperatingMode.SCHEDULE;
        final HeatingThermostatOperatingState operatingState = HeatingThermostatOperatingState.HEAT;
        final OverrideMode overrideMode = OverrideMode.NONE;
        final OnOffMode onOffMode = OnOffMode.ON;

        // Create the test node
        final Node node = getGoodNode(
                operatingMode,
                operatingState,
                overrideMode,
                onOffMode
        );


        /* When */
        strategy.handleUpdate(
                this.thing,
                this.thingHandlerCallback,
                node
        );


        /* Then */
        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyOperatingModeChannelUid),
                eq(new StringType(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_SCHEDULE))
        );

        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyBoostModeChannelUid),
                eq(OnOffType.OFF)
        );

        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyIsOnStateChannelUid),
                eq(OnOffType.ON)
        );
    }

    @Test
    public void testNormalUpdateManualHeatNoneOn() {
        /* Given */
        final HeatingThermostatEasyHandlerStrategy strategy = HeatingThermostatEasyHandlerStrategy.getInstance();

        final HeatingThermostatOperatingMode operatingMode = HeatingThermostatOperatingMode.MANUAL;
        final HeatingThermostatOperatingState operatingState = HeatingThermostatOperatingState.HEAT;
        final OverrideMode overrideMode = OverrideMode.NONE;
        final OnOffMode onOffMode = OnOffMode.ON;

        // Create the test node
        final Node node = getGoodNode(
                operatingMode,
                operatingState,
                overrideMode,
                onOffMode
        );


        /* When */
        strategy.handleUpdate(
                this.thing,
                this.thingHandlerCallback,
                node
        );


        /* Then */
        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyOperatingModeChannelUid),
                eq(new StringType(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_MANUAL))
        );

        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyBoostModeChannelUid),
                eq(OnOffType.OFF)
        );

        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyIsOnStateChannelUid),
                eq(OnOffType.ON)
        );
    }

    @Test
    public void testNormalUpdateScheduleOffNoneOff() {
        /* Given */
        final HeatingThermostatEasyHandlerStrategy strategy = HeatingThermostatEasyHandlerStrategy.getInstance();

        final HeatingThermostatOperatingMode operatingMode = HeatingThermostatOperatingMode.SCHEDULE;
        final HeatingThermostatOperatingState operatingState = HeatingThermostatOperatingState.OFF;
        final OverrideMode overrideMode = OverrideMode.NONE;
        final OnOffMode onOffMode = OnOffMode.OFF;

        // Create the test node
        final Node node = getGoodNode(
                operatingMode,
                operatingState,
                overrideMode,
                onOffMode
        );


        /* When */
        strategy.handleUpdate(
                this.thing,
                this.thingHandlerCallback,
                node
        );


        /* Then */
        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyOperatingModeChannelUid),
                eq(new StringType(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_OFF))
        );

        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyBoostModeChannelUid),
                eq(OnOffType.OFF)
        );

        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyIsOnStateChannelUid),
                eq(OnOffType.OFF)
        );
    }

    @Test
    public void testNormalUpdateScheduleHeatTransientOn() {
        /* Given */
        final HeatingThermostatEasyHandlerStrategy strategy = HeatingThermostatEasyHandlerStrategy.getInstance();

        final HeatingThermostatOperatingMode operatingMode = HeatingThermostatOperatingMode.SCHEDULE;
        final HeatingThermostatOperatingState operatingState = HeatingThermostatOperatingState.HEAT;
        final OverrideMode overrideMode = OverrideMode.TRANSIENT;
        final OnOffMode onOffMode = OnOffMode.ON;

        // Create the test node
        final Node node = getGoodNode(
                operatingMode,
                operatingState,
                overrideMode,
                onOffMode
        );


        /* When */
        strategy.handleUpdate(
                this.thing,
                this.thingHandlerCallback,
                node
        );


        /* Then */
        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyOperatingModeChannelUid),
                eq(new StringType(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_SCHEDULE))
        );

        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyBoostModeChannelUid),
                eq(OnOffType.ON)
        );

        verify(this.thingHandlerCallback).stateUpdated(
                eq(this.easyIsOnStateChannelUid),
                eq(OnOffType.ON)
        );
    }

    @Test
    public void testNormalCommandOperatingModeManual() {
        /* Given */
        final HeatingThermostatEasyHandlerStrategy strategy = HeatingThermostatEasyHandlerStrategy.getInstance();

        final HeatingThermostatOperatingMode operatingMode = HeatingThermostatOperatingMode.SCHEDULE;
        final OnOffMode onOffMode = OnOffMode.OFF;

        // Create the test node
        final Node node = getGoodNode(
                operatingMode,
                onOffMode
        );


        /* When */
        final boolean handledCommand = strategy.handleCommand(
                this.easyOperatingModeChannelUid,
                new StringType(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_MANUAL),
                node
        );


        /* Then */
        assertThat(handledCommand).isTrue();

        final @Nullable HeatingThermostatFeature heatingThermostatFeature = node.getFeature(HeatingThermostatFeature.class);
        assertThat(heatingThermostatFeature).isNotNull();

        final @Nullable OnOffDeviceFeature onOffDeviceFeature = node.getFeature(OnOffDeviceFeature.class);
        assertThat(onOffDeviceFeature).isNotNull();

        assertThat(heatingThermostatFeature.getOperatingModeAttribute().getTargetValue()).isEqualTo(HeatingThermostatOperatingMode.MANUAL);
        assertThat(onOffDeviceFeature.getModeAttribute().getTargetValue()).isEqualTo(OnOffMode.ON);
    }

    @Test
    public void testNormalCommandOperatingModeSchedule() {
        /* Given */
        final HeatingThermostatEasyHandlerStrategy strategy = HeatingThermostatEasyHandlerStrategy.getInstance();

        final HeatingThermostatOperatingMode operatingMode = HeatingThermostatOperatingMode.MANUAL;
        final OnOffMode onOffMode = OnOffMode.OFF;

        // Create the test node
        final Node node = getGoodNode(
                operatingMode,
                onOffMode
        );


        /* When */
        final boolean handledCommand = strategy.handleCommand(
                this.easyOperatingModeChannelUid,
                new StringType(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_SCHEDULE),
                node
        );


        /* Then */
        assertThat(handledCommand).isTrue();

        final @Nullable HeatingThermostatFeature heatingThermostatFeature = node.getFeature(HeatingThermostatFeature.class);
        assertThat(heatingThermostatFeature).isNotNull();

        final @Nullable OnOffDeviceFeature onOffDeviceFeature = node.getFeature(OnOffDeviceFeature.class);
        assertThat(onOffDeviceFeature).isNotNull();

        assertThat(heatingThermostatFeature.getOperatingModeAttribute().getTargetValue()).isEqualTo(HeatingThermostatOperatingMode.SCHEDULE);
        assertThat(onOffDeviceFeature.getModeAttribute().getTargetValue()).isEqualTo(OnOffMode.ON);
    }

    @Test
    public void testNormalCommandOperatingModeOff() {
        /* Given */
        final HeatingThermostatEasyHandlerStrategy strategy = HeatingThermostatEasyHandlerStrategy.getInstance();

        final HeatingThermostatOperatingMode operatingMode = HeatingThermostatOperatingMode.MANUAL;
        final OnOffMode onOffMode = OnOffMode.ON;

        // Create the test node
        final Node node = getGoodNode(
                operatingMode,
                onOffMode
        );


        /* When */
        final boolean handledCommand = strategy.handleCommand(
                this.easyOperatingModeChannelUid,
                new StringType(HiveBindingConstants.HEATING_EASY_MODE_OPERATING_OFF),
                node
        );


        /* Then */
        assertThat(handledCommand).isTrue();

        final @Nullable OnOffDeviceFeature onOffDeviceFeature = node.getFeature(OnOffDeviceFeature.class);
        assertThat(onOffDeviceFeature).isNotNull();

        assertThat(onOffDeviceFeature.getModeAttribute().getTargetValue()).isEqualTo(OnOffMode.OFF);
    }

    private static Node getGoodNode(
            final HeatingThermostatOperatingMode operatingMode,
            final OnOffMode onOffMode
    ) {
        return getGoodNode(
                operatingMode,
                HeatingThermostatOperatingState.OFF,
                OverrideMode.NONE,
                onOffMode
        );
    }

    private static Node getGoodNode(
            final HeatingThermostatOperatingMode operatingMode,
            final HeatingThermostatOperatingState operatingState,
            final OverrideMode overrideMode,
            final OnOffMode onOffMode
    ) {
        final Map<Class<? extends Feature>, Feature> features = new HashMap<>();

        // Create HeatingThermostatFeature
        final Quantity<Temperature> targetHeatTemperature = Quantities.getQuantity(20, Units.CELSIUS);
        final HeatingThermostatFeature heatingThermostatFeature = new HeatingThermostatFeature(
                TestUtil.createSimpleFeatureAttribute(operatingMode),
                TestUtil.createSimpleFeatureAttribute(operatingState),
                TestUtil.createSimpleFeatureAttribute(targetHeatTemperature),
                TestUtil.createSimpleFeatureAttribute(overrideMode)
        );

        features.put(HeatingThermostatFeature.class, heatingThermostatFeature);

        // Create OnOffDeviceFeature
        final OnOffDeviceFeature onOffDeviceFeature = new OnOffDeviceFeature(
                TestUtil.createSimpleFeatureAttribute(onOffMode)
        );

        features.put(OnOffDeviceFeature.class, onOffDeviceFeature);

        return TestUtil.getTestNodeWithFeatures(features);
    }
}
