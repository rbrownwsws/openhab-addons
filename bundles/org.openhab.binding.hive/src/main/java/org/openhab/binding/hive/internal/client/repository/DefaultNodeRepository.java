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

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

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

/**
 * The default implementation of {@link NodeRepository}.
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

    private BatteryDeviceFeature getBatteryDeviceFeatureFromDto(
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

    private OnOffDeviceFeature getOnOffDeviceFeatureFromDto(
            final OnOffDeviceV1FeatureDto onOffDeviceV1FeatureDto
    ) {
        return new OnOffDeviceFeature(
                FeatureAttributeFactory.getSettableFromDto(onOffDeviceV1FeatureDto.mode)
        );
    }

    private PhysicalDeviceFeature getPhysicalDeviceFeatureFromDto(
            final PhysicalDeviceV1FeatureDto physicalDeviceV1FeatureDto
    ) {
        final FeatureAttribute<String> hardwareIdentifier;

        final @Nullable FeatureAttributeDto<String> nativeIdentifierAttributeDto = physicalDeviceV1FeatureDto.nativeIdentifier;
        final @Nullable FeatureAttributeDto<String> hardwareIdentifierAttributeDto = physicalDeviceV1FeatureDto.hardwareIdentifier;

        if (nativeIdentifierAttributeDto != null) {
            hardwareIdentifier = FeatureAttributeFactory.getReadOnlyFromDto(physicalDeviceV1FeatureDto.nativeIdentifier);
        } else if (hardwareIdentifierAttributeDto != null) {
            // Weirdly this specific attribute does not have time fields.
            // Make it manually with fake timestamps.

            final @Nullable String reportedValue = hardwareIdentifierAttributeDto.reportedValue;
            final @Nullable String displayValue = hardwareIdentifierAttributeDto.displayValue;

            if (reportedValue == null) {
                throw new HiveClientResponseException("Hardware Identifier reported value unexpectedly null");
            }
            if (displayValue == null) {
                throw new HiveClientResponseException("Hardware Identifier display value unexpectedly null");
            }

            hardwareIdentifier = new FeatureAttribute<>(
                    reportedValue,
                    Instant.now(),
                    Instant.now(),
                    displayValue
            );
        } else {
            hardwareIdentifier = new FeatureAttribute<>(
                    "UNKNOWN",
                    Instant.now(),
                    Instant.now(),
                    "UNKNOWN"
            );
        }

        return new PhysicalDeviceFeature(
                hardwareIdentifier,
                FeatureAttributeFactory.getReadOnlyFromDto(physicalDeviceV1FeatureDto.model),
                FeatureAttributeFactory.getReadOnlyFromDto(physicalDeviceV1FeatureDto.manufacturer),
                FeatureAttributeFactory.getReadOnlyFromDto(physicalDeviceV1FeatureDto.softwareVersion)
        );
    }

    private HeatingThermostatFeature getHeatingThermostatFeatureFromDto(
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

    private TemperatureSensorFeature getTemperatureSensorFeatureFromDto(
            final TemperatureSensorV1FeatureDto temperatureSensorV1FeatureDto
    ) {
        return new TemperatureSensorFeature(
                FeatureAttributeFactory.getReadOnlyFromDtoWithAdapter(
                        Temperature::new,
                        temperatureSensorV1FeatureDto.temperature
                )
        );
    }

    private TransientModeFeature getTransientModeFeatureFromDto(
            final TransientModeV1FeatureDto transientModeV1FeatureDto
    ) {
        return new TransientModeFeature(
                FeatureAttributeFactory.getSettableFromDtoWithAdapter(
                        Duration::ofSeconds,
                        transientModeV1FeatureDto.duration
                ),
                FeatureAttributeFactory.getSettableFromDto(transientModeV1FeatureDto.isEnabled),
                FeatureAttributeFactory.getReadOnlyFromDto(transientModeV1FeatureDto.startDatetime),
                FeatureAttributeFactory.getReadOnlyFromDto(transientModeV1FeatureDto.endDatetime)
        );
    }

    private TransientModeHeatingActionsFeature getTransientModeHeatingActionsFeatureFromDto(
            final TransientModeV1FeatureDto transientModeV1FeatureDto
    ) {
        final @Nullable FeatureAttributeDto<List<ActionDto>> actionsAttribute = transientModeV1FeatureDto.actions;
        if (actionsAttribute == null) {
            throw new HiveClientResponseException("Transient mode actions attribute is unexpectedly null.");
        }

        final @Nullable List<ActionDto> actions = actionsAttribute.reportedValue;
        if (actions == null) {
            throw new HiveClientResponseException("Transient mode action list is unexpectedly null.");
        }

        if (actions.isEmpty()) {
            throw new HiveClientResponseException("Transient mode action list is unexpectedly empty.");
        }
        final @Nullable ActionDto targetTempAction = actions.get(0);
        if (targetTempAction == null) {
            throw new HiveClientResponseException("Transient mode action is unexpectedly null.");
        }

        final @Nullable String targetTempString = targetTempAction.value;
        if (targetTempString == null) {
            throw new HiveClientResponseException("Transient Target Temperature is unexpectedly null.");
        }

        final Temperature targetHeatTemperature = new Temperature(new BigDecimal(targetTempString));

        return new TransientModeHeatingActionsFeature(
                FeatureAttributeFactory.getSettableFromDtoWithAdapter(
                        (val) -> targetHeatTemperature,
                        transientModeV1FeatureDto.actions
                )
        );
    }

    private WaterHeaterFeature getWaterHeaterFeatureFromDto(
            final WaterHeaterV1FeatureDto waterHeaterV1FeatureDto
    ) {
        return new WaterHeaterFeature(
                FeatureAttributeFactory.getSettableFromDto(waterHeaterV1FeatureDto.operatingMode),
                FeatureAttributeFactory.getReadOnlyFromDto(waterHeaterV1FeatureDto.isOn),
                FeatureAttributeFactory.getSettableFromDto(waterHeaterV1FeatureDto.temporaryOperatingModeOverride)
        );
    }

    private ZigbeeDeviceFeature getZigbeeDeviceFeatureFromDto(
            final ZigbeeDeviceV1FeatureDto zigbeeDeviceV1FeatureDto
    ) {
        return new ZigbeeDeviceFeature(
                FeatureAttributeFactory.getReadOnlyFromDto(zigbeeDeviceV1FeatureDto.averageLQI),
                FeatureAttributeFactory.getReadOnlyFromDto(zigbeeDeviceV1FeatureDto.lastKnownLQI),
                FeatureAttributeFactory.getReadOnlyFromDto(zigbeeDeviceV1FeatureDto.averageRSSI),
                FeatureAttributeFactory.getReadOnlyFromDto(zigbeeDeviceV1FeatureDto.lastKnownRSSI)
        );
    }

    private void updateFeaturesDtoWithOnOffDeviceFeature(
            final FeaturesDto featuresDto,
            final OnOffDeviceFeature onOffDeviceFeature
    ) {
        final @Nullable OnOffMode onOffModeTarget = onOffDeviceFeature.getModeAttribute().getTargetValue();
        if (onOffModeTarget != null) {
            final OnOffDeviceV1FeatureDto onOffDeviceV1FeatureDto = new OnOffDeviceV1FeatureDto();
            featuresDto.on_off_device_v1 = onOffDeviceV1FeatureDto;

            final FeatureAttributeDto<OnOffMode> modeAttribute = new FeatureAttributeDto<>();
            onOffDeviceV1FeatureDto.mode = modeAttribute;
            modeAttribute.targetValue = onOffModeTarget;
        }
    }

    private void updateFeaturesDtoWithHeatingThermostatFeature(
            final FeaturesDto featuresDto,
            final HeatingThermostatFeature heatingThermostatFeature
    ) {
        final @Nullable HeatingThermostatOperatingMode operatingModeTarget = heatingThermostatFeature.getOperatingModeAttribute().getTargetValue();
        final @Nullable Temperature targetHeatTemperatureTarget = heatingThermostatFeature.getTargetHeatTemperatureAttribute().getTargetValue();
        final @Nullable OverrideMode temporaryOperatingModeOverrideTarget = heatingThermostatFeature.getTemporaryOperatingModeOverrideAttribute().getTargetValue();

        // If one of the HeatingThermostatFeature attributes has been set...
        if (operatingModeTarget != null
                || targetHeatTemperatureTarget != null
                || temporaryOperatingModeOverrideTarget != null
        ) {
            final HeatingThermostatV1FeatureDto heatingThermostatV1FeatureDto = new HeatingThermostatV1FeatureDto();
            featuresDto.heating_thermostat_v1 = heatingThermostatV1FeatureDto;

            if (operatingModeTarget != null) {
                final FeatureAttributeDto<HeatingThermostatOperatingMode> operatingModeFeatureAttributeDto = new FeatureAttributeDto<>();
                heatingThermostatV1FeatureDto.operatingMode = operatingModeFeatureAttributeDto;
                operatingModeFeatureAttributeDto.targetValue = operatingModeTarget;
            }

            if (targetHeatTemperatureTarget != null) {
                // FIXME: Check temperature units.
                final FeatureAttributeDto<BigDecimal> targetHeatTemperatureAttribute = new FeatureAttributeDto<>();
                heatingThermostatV1FeatureDto.targetHeatTemperature = targetHeatTemperatureAttribute;
                targetHeatTemperatureAttribute.targetValue = targetHeatTemperatureTarget.getValue();
            }

            if (temporaryOperatingModeOverrideTarget != null) {
                final FeatureAttributeDto<OverrideMode> temporaryOperatingModeAttributeDto = new FeatureAttributeDto<>();
                heatingThermostatV1FeatureDto.temporaryOperatingModeOverride = temporaryOperatingModeAttributeDto;
                temporaryOperatingModeAttributeDto.targetValue = temporaryOperatingModeOverrideTarget;
            }
        }
    }

    private void updateFeaturesDtoWithTransientModeFeature(
            final FeaturesDto featuresDto,
            final TransientModeFeature transientModeFeature
    ) {
        final @Nullable Duration durationTarget = transientModeFeature.getDurationAttribute().getTargetValue();
        final @Nullable Boolean isEnabledTarget = transientModeFeature.getIsEnabledAttribute().getTargetValue();

        if (durationTarget != null || isEnabledTarget != null) {
            @Nullable TransientModeV1FeatureDto transientModeV1FeatureDto = featuresDto.transient_mode_v1;
            if (transientModeV1FeatureDto == null) {
                transientModeV1FeatureDto = new TransientModeV1FeatureDto();
                featuresDto.transient_mode_v1 = transientModeV1FeatureDto;
            }

            if (durationTarget != null) {
                final FeatureAttributeDto<Long> durationAttribute = new FeatureAttributeDto<>();
                transientModeV1FeatureDto.duration = durationAttribute;
                durationAttribute.targetValue = Math.max(1, durationTarget.getSeconds());
            }

            if (isEnabledTarget != null) {
                final FeatureAttributeDto<Boolean> isEnabledAttribute = new FeatureAttributeDto<>();
                transientModeV1FeatureDto.isEnabled = isEnabledAttribute;
                isEnabledAttribute.targetValue = isEnabledTarget;
            }
        }
    }

    private void updateFeaturesDtoWithTransientModeHeatingActionsFeature(
            final FeaturesDto featuresDto,
            final TransientModeHeatingActionsFeature transientModeHeatingActionsFeature
    ) {
        final @Nullable Temperature boostTargetTemperatureTarget = transientModeHeatingActionsFeature.getBoostTargetTemperatureAttribute().getTargetValue();
        if (boostTargetTemperatureTarget != null) {
            @Nullable TransientModeV1FeatureDto transientModeV1FeatureDto = featuresDto.transient_mode_v1;
            if (transientModeV1FeatureDto == null) {
                transientModeV1FeatureDto = new TransientModeV1FeatureDto();
                featuresDto.transient_mode_v1 = transientModeV1FeatureDto;
            }

            final ActionDto actionDto = new ActionDto();
            actionDto.actionType = ActionType.GENERIC;
            actionDto.featureType = FeatureType.HEATING_THERMOSTAT_V1;
            actionDto.attribute = AttributeName.ATTRIBUTE_NAME_TARGET_HEAT_TEMPERATURE;
            actionDto.value = boostTargetTemperatureTarget.getValue().toString();

            final FeatureAttributeDto<List<ActionDto>> actionsDto = new FeatureAttributeDto<>();
            transientModeV1FeatureDto.actions = actionsDto;
            actionsDto.targetValue = Collections.singletonList(actionDto);
        }
    }

    private void updateFeaturesDtoWithWaterHeaterFeature(
            final FeaturesDto featuresDto,
            final WaterHeaterFeature waterHeaterFeature
    ) {
        final @Nullable WaterHeaterOperatingMode operatingModeTarget = waterHeaterFeature.getOperatingModeAttribute().getTargetValue();
        final @Nullable OverrideMode temporaryOperatingModeOverrideTarget = waterHeaterFeature.getTemporaryOperatingModeOverrideAttribute().getTargetValue();

        if (operatingModeTarget != null || temporaryOperatingModeOverrideTarget != null) {
            final WaterHeaterV1FeatureDto waterHeaterV1FeatureDto = new WaterHeaterV1FeatureDto();
            featuresDto.water_heater_v1 = waterHeaterV1FeatureDto;

            if (operatingModeTarget != null) {
                final FeatureAttributeDto<WaterHeaterOperatingMode> operatingModeAttributeDto = new FeatureAttributeDto<>();
                waterHeaterV1FeatureDto.operatingMode = operatingModeAttributeDto;
                operatingModeAttributeDto.targetValue = operatingModeTarget;
            }

            if (temporaryOperatingModeOverrideTarget != null) {
                final FeatureAttributeDto<OverrideMode> temporaryOperatingModeOverrideAttribute = new FeatureAttributeDto<>();
                waterHeaterV1FeatureDto.temporaryOperatingModeOverride = temporaryOperatingModeOverrideAttribute;
                temporaryOperatingModeOverrideAttribute.targetValue = temporaryOperatingModeOverrideTarget;
            }
        }
    }

    private Set<Node> parseNodesDto(
            final NodesDto nodesDto
    ) {
        final @Nullable List<@Nullable NodeDto> nodeDtos = nodesDto.nodes;
        if (nodeDtos == null) {
            throw new HiveClientResponseException("Nodes list is unexpectedly null.");
        }

        final Set<Node> nodes = new HashSet<>();
        // For each node
        for (final @Nullable NodeDto nodeDto : nodeDtos) {
            if (nodeDto == null) {
                throw new HiveClientResponseException("Node DTO is unexpectedly null.");
            }

            final @Nullable NodeId nodeId = nodeDto.id;
            if (nodeId == null) {
                throw new HiveClientResponseException("NodeId is unexpectedly null.");
            }

            final @Nullable NodeId parentNodeId = nodeDto.parentNodeId;
            if (parentNodeId == null) {
                throw new HiveClientResponseException("ParentNodeId is unexpectedly null.");
            }

            final @Nullable NodeName nodeName = nodeDto.name;
            if (nodeName == null) {
                throw new HiveClientResponseException("NodeName is unexpectedly null.");
            }

            final @Nullable NodeType nodeType = nodeDto.nodeType;
            if (nodeType == null) {
                throw new HiveClientResponseException("NodeType is unexpectedly null.");
            }

            final @Nullable String protocolString = nodeDto.protocol;
            final Protocol protocol;
            if (protocolString != null) {
                protocol = new Protocol(protocolString);
            } else {
                protocol = Protocol.NONE;
            }

            final Map<Class<? extends Feature>, Feature> features = new HashMap<>();

            final @Nullable FeaturesDto featuresDto = nodeDto.features;
            if (featuresDto == null) {
                throw new HiveClientResponseException("Node features unexpectedly null.");
            }

            final @Nullable BatteryDeviceV1FeatureDto batteryDeviceV1FeatureDto = featuresDto.battery_device_v1;
            final @Nullable HeatingThermostatV1FeatureDto heatingThermostatV1FeatureDto = featuresDto.heating_thermostat_v1;
            final @Nullable OnOffDeviceV1FeatureDto onOffDeviceV1FeatureDto = featuresDto.on_off_device_v1;
            final @Nullable PhysicalDeviceV1FeatureDto physicalDeviceV1FeatureDto = featuresDto.physical_device_v1;
            final @Nullable TemperatureSensorV1FeatureDto temperatureSensorV1FeatureDto = featuresDto.temperature_sensor_v1;
            final @Nullable TransientModeV1FeatureDto transientModeV1FeatureDto = featuresDto.transient_mode_v1;
            final @Nullable WaterHeaterV1FeatureDto waterHeaterV1FeatureDto = featuresDto.water_heater_v1;
            final @Nullable ZigbeeDeviceV1FeatureDto zigbeeDeviceV1FeatureDto = featuresDto.zigbee_device_v1;

            final @Nullable DeviceManagementV1FeatureDto deviceManagementV1FeatureDto = featuresDto.device_management_v1;

            if (batteryDeviceV1FeatureDto != null) {
                features.put(BatteryDeviceFeature.class, getBatteryDeviceFeatureFromDto(batteryDeviceV1FeatureDto));
            }
            if (heatingThermostatV1FeatureDto != null) {
                features.put(HeatingThermostatFeature.class, getHeatingThermostatFeatureFromDto(heatingThermostatV1FeatureDto));
            }
            if (onOffDeviceV1FeatureDto != null) {
                features.put(OnOffDeviceFeature.class, getOnOffDeviceFeatureFromDto(onOffDeviceV1FeatureDto));
            }
            if (physicalDeviceV1FeatureDto != null) {
                features.put(PhysicalDeviceFeature.class, getPhysicalDeviceFeatureFromDto(physicalDeviceV1FeatureDto));
            }
            if (temperatureSensorV1FeatureDto != null) {
                features.put(TemperatureSensorFeature.class, getTemperatureSensorFeatureFromDto(temperatureSensorV1FeatureDto));
            }
            if (transientModeV1FeatureDto != null) {
                features.put(TransientModeFeature.class, getTransientModeFeatureFromDto(transientModeV1FeatureDto));

                if (heatingThermostatV1FeatureDto != null) {
                    features.put(TransientModeHeatingActionsFeature.class, getTransientModeHeatingActionsFeatureFromDto(transientModeV1FeatureDto));
                }
            }
            if (waterHeaterV1FeatureDto != null) {
                features.put(WaterHeaterFeature.class, getWaterHeaterFeatureFromDto(waterHeaterV1FeatureDto));
            }
            if (zigbeeDeviceV1FeatureDto != null) {
                features.put(ZigbeeDeviceFeature.class, getZigbeeDeviceFeatureFromDto(zigbeeDeviceV1FeatureDto));
            }

            if (deviceManagementV1FeatureDto == null) {
                throw new HiveClientResponseException("DeviceManagement feature is unexpectedly null.");
            }

            final @Nullable FeatureAttributeDto<ProductType> productTypeFeatureAttributeDto = deviceManagementV1FeatureDto.productType;
            if (productTypeFeatureAttributeDto == null) {
                throw new HiveClientResponseException("DeviceManagement>ProductType attribute is unexpectedly null.");
            }

            final @Nullable ProductType productType = productTypeFeatureAttributeDto.reportedValue;
            if (productType == null) {
                throw new HiveClientResponseException("ProductType is unexpectedly null.");
            }

            nodes.add(new DefaultNode(
                    nodeId,
                    nodeName,
                    nodeType,
                    productType,
                    protocol,
                    parentNodeId,
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

        final FeaturesDto featuresDto = new FeaturesDto();
        nodeDto.features = featuresDto;

        for (final Feature feature : node.getFeatures()) {
            final Class<? extends Feature> featureClass = feature.getClass();

            if (featureClass.equals(OnOffDeviceFeature.class)) {
                updateFeaturesDtoWithOnOffDeviceFeature(featuresDto, (OnOffDeviceFeature) feature);
            } else if (featureClass.equals(HeatingThermostatFeature.class)) {
                updateFeaturesDtoWithHeatingThermostatFeature(featuresDto, (HeatingThermostatFeature) feature);
            } else if (featureClass.equals(TransientModeFeature.class)) {
                updateFeaturesDtoWithTransientModeFeature(featuresDto, (TransientModeFeature) feature);
            } else if (featureClass.equals(TransientModeHeatingActionsFeature.class)) {
                updateFeaturesDtoWithTransientModeHeatingActionsFeature(featuresDto, (TransientModeHeatingActionsFeature) feature);
            } else if (featureClass.equals(WaterHeaterFeature.class)) {
                updateFeaturesDtoWithWaterHeaterFeature(featuresDto, (WaterHeaterFeature) feature);
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
