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
package org.openhab.binding.hive.internal.client.repository;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.*;
import org.openhab.binding.hive.internal.client.dto.*;
import org.openhab.binding.hive.internal.client.exception.HiveApiNotAuthorisedException;
import org.openhab.binding.hive.internal.client.exception.HiveApiUnknownException;
import org.openhab.binding.hive.internal.client.exception.HiveClientResponseException;
import org.openhab.binding.hive.internal.client.feature.*;
import tec.uom.se.quantity.Quantities;
import tec.uom.se.unit.Units;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.util.*;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class DefaultNodeRepository implements NodeRepository {
    private final HiveApiRequestFactory requestFactory;

    public DefaultNodeRepository(
            final HiveApiRequestFactory requestFactory
    ) {
        Objects.requireNonNull(requestFactory);

        this.requestFactory = requestFactory;
    }

    private URI getEndpointPathForNode(final NodeId nodeId) {
        return HiveApiConstants.ENDPOINT_NODE.resolve(nodeId.toString());
    }

    private BatteryDeviceFeature getBatteryDeviceFeature(
            final BatteryDeviceV1FeatureDto batteryDeviceV1FeatureDto
    ) {
        return new BatteryDeviceFeature(
                FeatureAttributeFactory.getReadOnlyFromDtoWithAdapter(
                        BatteryLevel::new,
                        batteryDeviceV1FeatureDto.batteryLevel
                ),
                FeatureAttributeFactory.getReadOnlyFromDto(batteryDeviceV1FeatureDto.batteryState),
                FeatureAttributeFactory.getReadOnlyFromDtoWithAdapter(
                        (val) -> Quantities.getQuantity(val, Units.VOLT),
                        batteryDeviceV1FeatureDto.batteryVoltage
                ),
                FeatureAttributeFactory.getReadOnlyFromDto(batteryDeviceV1FeatureDto.notificationState)
        );
    }

    private OnOffDeviceFeature getOnOffDeviceFeature(
            final OnOffDeviceV1FeatureDto onOffDeviceV1FeatureDto
    ) {
        return new OnOffDeviceFeature(
                FeatureAttributeFactory.getSettableFromDto(onOffDeviceV1FeatureDto.mode)
        );
    }

    private HeatingThermostatFeature getHeatingThermostatFeature(
            final HeatingThermostatV1FeatureDto heatingThermostatV1FeatureDto
    ) {
        return new HeatingThermostatFeature(
                FeatureAttributeFactory.getSettableFromDto(heatingThermostatV1FeatureDto.operatingMode),
                FeatureAttributeFactory.getReadOnlyFromDto(heatingThermostatV1FeatureDto.operatingState),
                FeatureAttributeFactory.getSettableFromDtoWithAdapter(
                        Temperature::new,
                        heatingThermostatV1FeatureDto.targetHeatTemperature
                ),
                FeatureAttributeFactory.getSettableFromDto(heatingThermostatV1FeatureDto.temporaryOperatingModeOverride)
        );
    }

    private TemperatureSensorFeature getTemperatureSensorFeature(
            final TemperatureSensorV1FeatureDto temperatureSensorV1FeatureDto
    ) {
        return new TemperatureSensorFeature(
                FeatureAttributeFactory.getReadOnlyFromDtoWithAdapter(
                        Temperature::new,
                        temperatureSensorV1FeatureDto.temperature
                )
        );
    }

    private TransientModeFeature getTransientModeFeature(
            final TransientModeV1FeatureDto transientModeV1FeatureDto
    ) {
        return new TransientModeFeature(
                FeatureAttributeFactory.getSettableFromDtoWithAdapter(
                        Duration::ofSeconds,
                        transientModeV1FeatureDto.duration
                ),
                FeatureAttributeFactory.getReadOnlyFromDto(transientModeV1FeatureDto.isEnabled),
                FeatureAttributeFactory.getReadOnlyFromDto(transientModeV1FeatureDto.startDatetime),
                FeatureAttributeFactory.getReadOnlyFromDto(transientModeV1FeatureDto.endDatetime)
        );
    }

    private TransientModeHeatingActionsFeature getTransientModeHeatingActionsFeature(
            final TransientModeV1FeatureDto transientModeV1FeatureDto
    ) {
        // FIXME: Check the DTO is valid before blindly using it.
        final Temperature targetHeatTemperature = new Temperature(new BigDecimal(transientModeV1FeatureDto.actions.reportedValue.get(0).value));

        return new TransientModeHeatingActionsFeature(
                FeatureAttributeFactory.getSettableFromDtoWithAdapter(
                        (val) -> targetHeatTemperature,
                        transientModeV1FeatureDto.actions
                )
        );
    }

    private WaterHeaterFeature getWaterHeaterFeature(
            final WaterHeaterV1FeatureDto waterHeaterV1FeatureDto
    ) {
        return new WaterHeaterFeature(
                FeatureAttributeFactory.getSettableFromDto(waterHeaterV1FeatureDto.operatingMode),
                FeatureAttributeFactory.getReadOnlyFromDto(waterHeaterV1FeatureDto.isOn),
                FeatureAttributeFactory.getSettableFromDto(waterHeaterV1FeatureDto.temporaryOperatingModeOverride)
        );
    }

    private ZigbeeDeviceFeature getZigbeeDeviceFeature(
            final ZigbeeDeviceV1FeatureDto zigbeeDeviceV1FeatureDto
    ) {
        return new ZigbeeDeviceFeature(
                FeatureAttributeFactory.getReadOnlyFromDto(zigbeeDeviceV1FeatureDto.averageLQI),
                FeatureAttributeFactory.getReadOnlyFromDto(zigbeeDeviceV1FeatureDto.lastKnownLQI),
                FeatureAttributeFactory.getReadOnlyFromDto(zigbeeDeviceV1FeatureDto.averageRSSI),
                FeatureAttributeFactory.getReadOnlyFromDto(zigbeeDeviceV1FeatureDto.lastKnownRSSI)
        );
    }

    private Set<Node> parseNodesDto(
            final NodesDto nodesDto
    ) {
        final Set<Node> nodes = new HashSet<>();
        // For each node
        for (NodeDto nodeDto : nodesDto.nodes) {
            final Protocol protocol;
            if (nodeDto.protocol != null) {
                protocol = new Protocol(nodeDto.protocol);
            } else {
                protocol = Protocol.NONE;
            }

            final Map<Class<? extends Feature>, Feature> features = new HashMap<>();

            if (nodeDto.features.battery_device_v1 != null) {
                features.put(BatteryDeviceFeature.class, getBatteryDeviceFeature(nodeDto.features.battery_device_v1));
            }
            if (nodeDto.features.heating_thermostat_v1 != null) {
                features.put(HeatingThermostatFeature.class, getHeatingThermostatFeature(nodeDto.features.heating_thermostat_v1));
            }
            if (nodeDto.features.on_off_device_v1 != null) {
                features.put(OnOffDeviceFeature.class, getOnOffDeviceFeature(nodeDto.features.on_off_device_v1));
            }
            if (nodeDto.features.temperature_sensor_v1 != null) {
                features.put(TemperatureSensorFeature.class, getTemperatureSensorFeature(nodeDto.features.temperature_sensor_v1));
            }
            if (nodeDto.features.transient_mode_v1 != null) {
                features.put(TransientModeFeature.class, getTransientModeFeature(nodeDto.features.transient_mode_v1));

                if (nodeDto.features.heating_thermostat_v1 != null) {
                    features.put(TransientModeHeatingActionsFeature.class, getTransientModeHeatingActionsFeature(nodeDto.features.transient_mode_v1));
                }
            }
            if (nodeDto.features.water_heater_v1 != null) {
                features.put(WaterHeaterFeature.class, getWaterHeaterFeature(nodeDto.features.water_heater_v1));
            }
            if (nodeDto.features.zigbee_device_v1 != null) {
                features.put(ZigbeeDeviceFeature.class, getZigbeeDeviceFeature(nodeDto.features.zigbee_device_v1));
            }

            nodes.add(new DefaultNode(
                    nodeDto.id,
                    nodeDto.name,
                    nodeDto.nodeType,
                    nodeDto.features.device_management_v1.productType.reportedValue,
                    protocol,
                    nodeDto.parentNodeId,
                    features
            ));
        }

        return Collections.unmodifiableSet(nodes);
    }

    @Override
    public Set<Node> getAllNodes() {
        /* Send our get nodes request to the Hive API. */
        final HiveApiResponse response = this.requestFactory.newRequest(HiveApiConstants.ENDPOINT_NODES)
                .accept(MediaType.API_V6_5_0_JSON)
                .get();

        if (response.getStatusCode() == 401) {
            throw new HiveApiNotAuthorisedException();
        } else if (response.getStatusCode() != 200) {
            throw new HiveApiUnknownException("Getting nodes failed for an unknown reason");
        }

        final NodesDto nodesDto = response.getContent(NodesDto.class);

        return parseNodesDto(nodesDto);
    }

    @Override
    public @Nullable Node getNode(final NodeId nodeId) {
        /* Send our get node request to the Hive API. */
        final HiveApiResponse response = this.requestFactory.newRequest(getEndpointPathForNode(nodeId))
                .accept(MediaType.API_V6_5_0_JSON)
                .get();

        if (response.getStatusCode() == 401) {
            throw new HiveApiNotAuthorisedException();
        } else if (response.getStatusCode() != 200) {
            throw new HiveApiUnknownException("Getting nodes failed for an unknown reason");
        }

        final NodesDto nodesDto = response.getContent(NodesDto.class);
        final Set<Node> nodes = parseNodesDto(nodesDto);

        if (nodes.isEmpty()) {
            // There is no node with that ID
            return null;
        } else if (nodes.size() == 1) {
            return nodes.iterator().next();
        } else {
            // Something has gone wrong.
            throw new HiveClientResponseException("Got multiple nodes when requesting node by ID!");
        }
    }

    @Override
    public @Nullable Node updateNode(final Node node) {
        /* Prepare DTO to send Hive API */
        final NodeDto nodeDto = new NodeDto();
        nodeDto.features = new FeaturesDto();

        for (final Feature feature : node.getFeatures()) {
            final Class<? extends Feature> featureClass = feature.getClass();

            if (featureClass.equals(OnOffDeviceFeature.class)) {
                final OnOffDeviceFeature onOffDeviceFeature = (OnOffDeviceFeature) feature;

                if (onOffDeviceFeature.getModeAttribute().getTargetValue() != null) {
                    nodeDto.features.on_off_device_v1 = new OnOffDeviceV1FeatureDto();

                    nodeDto.features.on_off_device_v1.mode = new FeatureAttributeDto<>();
                    nodeDto.features.on_off_device_v1.mode.targetValue = onOffDeviceFeature.getModeAttribute().getTargetValue();
                }
            } else if (featureClass.equals(HeatingThermostatFeature.class)) {
                final HeatingThermostatFeature heatingThermostatFeature = (HeatingThermostatFeature) feature;

                if (heatingThermostatFeature.getOperatingModeAttribute().getTargetValue() != null
                        || heatingThermostatFeature.getTargetHeatTemperatureAttribute().getTargetValue() != null
                        || heatingThermostatFeature.getTemporaryOperatingModeOverrideAttribute().getTargetValue() != null
                ) {
                    nodeDto.features.heating_thermostat_v1 = new HeatingThermostatV1FeatureDto();

                    if (heatingThermostatFeature.getOperatingModeAttribute().getTargetValue() != null) {
                        nodeDto.features.heating_thermostat_v1.operatingMode = new FeatureAttributeDto<>();
                        nodeDto.features.heating_thermostat_v1.operatingMode.targetValue = heatingThermostatFeature.getOperatingModeAttribute().getTargetValue();
                    }

                    if (heatingThermostatFeature.getTargetHeatTemperatureAttribute().getTargetValue() != null) {
                        // FIXME: Check temperature units.
                        nodeDto.features.heating_thermostat_v1.targetHeatTemperature = new FeatureAttributeDto<>();
                        nodeDto.features.heating_thermostat_v1.targetHeatTemperature.targetValue = heatingThermostatFeature.getTargetHeatTemperatureAttribute().getTargetValue().getValue();
                    }

                    if (heatingThermostatFeature.getTemporaryOperatingModeOverrideAttribute().getTargetValue() != null) {
                        nodeDto.features.heating_thermostat_v1.temporaryOperatingModeOverride = new FeatureAttributeDto<>();
                        nodeDto.features.heating_thermostat_v1.temporaryOperatingModeOverride.targetValue = heatingThermostatFeature.getTemporaryOperatingModeOverrideAttribute().getTargetValue();
                    }
                }
            } else if (featureClass.equals(TransientModeFeature.class)) {
                final TransientModeFeature transientModeFeature = (TransientModeFeature) feature;

                if (transientModeFeature.getDurationAttribute().getTargetValue() != null) {
                    if (nodeDto.features.transient_mode_v1 == null) {
                        nodeDto.features.transient_mode_v1 = new TransientModeV1FeatureDto();
                    }

                    nodeDto.features.transient_mode_v1.duration = new FeatureAttributeDto<>();
                    nodeDto.features.transient_mode_v1.duration.targetValue = Math.max(1, transientModeFeature.getDurationAttribute().getTargetValue().getSeconds());
                }
            } else if (featureClass.equals(TransientModeHeatingActionsFeature.class)) {
                final TransientModeHeatingActionsFeature transientModeHeatingActionsFeature = (TransientModeHeatingActionsFeature) feature;

                if (transientModeHeatingActionsFeature.getBoostTargetTemperatureAttribute().getTargetValue() != null) {
                    if (nodeDto.features.transient_mode_v1 == null) {
                        nodeDto.features.transient_mode_v1 = new TransientModeV1FeatureDto();
                    }

                    final ActionDto actionDto = new ActionDto();
                    actionDto.actionType = ActionType.GENERIC;
                    actionDto.featureType = FeatureType.HEATING_THERMOSTAT_V1;
                    actionDto.attribute = AttributeName.ATTRIBUTE_NAME_TARGET_HEAT_TEMPERATURE;
                    actionDto.value = transientModeHeatingActionsFeature.getBoostTargetTemperatureAttribute().getTargetValue().getValue().toString();

                    nodeDto.features.transient_mode_v1.actions = new FeatureAttributeDto<>();
                    nodeDto.features.transient_mode_v1.actions.targetValue = Collections.singletonList(actionDto);
                }
            } else if (featureClass.equals(WaterHeaterFeature.class)) {
                final WaterHeaterFeature waterHeaterFeature = (WaterHeaterFeature) feature;

                if (waterHeaterFeature.getOperatingModeAttribute().getTargetValue() != null) {
                    nodeDto.features.water_heater_v1 = new WaterHeaterV1FeatureDto();

                    nodeDto.features.water_heater_v1.operatingMode.targetValue = waterHeaterFeature.getOperatingModeAttribute().getTargetValue();
                }

                if (waterHeaterFeature.getTemporaryOperatingModeOverrideAttribute().getTargetValue() != null) {
                    nodeDto.features.water_heater_v1.temporaryOperatingModeOverride = new FeatureAttributeDto<>();
                    nodeDto.features.water_heater_v1.temporaryOperatingModeOverride.targetValue = waterHeaterFeature.getTemporaryOperatingModeOverrideAttribute().getTargetValue();
                }
            }
        }

        final NodesDto nodesDto = new NodesDto();
        nodesDto.nodes = Collections.singletonList(nodeDto);

        final HiveApiResponse response = this.requestFactory.newRequest(getEndpointPathForNode(node.getId()))
                .accept(MediaType.API_V6_5_0_JSON)
                .put(nodesDto);

        if (response.getStatusCode() == 401) {
            throw new HiveApiNotAuthorisedException();
        } else if (response.getStatusCode() != 200) {
            throw new HiveApiUnknownException("Updating node failed for an unknown reason. Response code: " + response.getStatusCode());
        }

        final NodesDto responseNodesDto = response.getContent(NodesDto.class);
        final Set<Node> responseNodes = parseNodesDto(responseNodesDto);

        if (responseNodes.size() == 1) {
            return responseNodes.iterator().next();
        } else {
            // Something has gone wrong.
            throw new HiveClientResponseException("The parsed response is malformed.");
        }
    }
}
