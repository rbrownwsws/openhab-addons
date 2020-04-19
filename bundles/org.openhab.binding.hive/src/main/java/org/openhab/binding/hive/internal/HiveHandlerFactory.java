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
package org.openhab.binding.hive.internal;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.eclipse.smarthome.io.net.http.HttpClientFactory;
import org.openhab.binding.hive.internal.client.DefaultHiveClientFactory;
import org.openhab.binding.hive.internal.client.HiveClientFactory;
import org.openhab.binding.hive.internal.discovery.DefaultHiveDiscoveryService;
import org.openhab.binding.hive.internal.handler.DefaultHiveAccountHandler;
import org.openhab.binding.hive.internal.handler.DefaultHiveThingHandler;
import org.openhab.binding.hive.internal.handler.strategy.*;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link HiveHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.hive", service = ThingHandlerFactory.class)
public final class HiveHandlerFactory extends BaseThingHandlerFactory {
    private static final Set<ThingHandlerStrategy> STRATEGIES_BOILER_MODULE = Collections.unmodifiableSet(Stream.of(
            PhysicalDeviceHandlerStrategy.getInstance(),
            ZigbeeDeviceHandlerStrategy.getInstance()
    ).collect(Collectors.toSet()));

    private static final Set<ThingHandlerStrategy> STRATEGIES_HEATING = Collections.unmodifiableSet(Stream.of(
            AutoBoostHandlerStrategy.getInstance(),
            BoostTimeRemainingHandlerStrategy.getInstance(),
            HeatingThermostatHandlerStrategy.getInstance(),
            OnOffDeviceHandlerStrategy.getInstance(),
            TemperatureSensorHandlerStrategy.getInstance(),
            TransientModeHandlerStrategy.getInstance(),
            HeatingTransientModeHandlerStrategy.getInstance(),
            HeatingThermostatEasyHandlerStrategy.getInstance()
    ).collect(Collectors.toSet()));

    private static final Set<ThingHandlerStrategy> STRATEGIES_HOT_WATER = Collections.unmodifiableSet(Stream.of(
            BoostTimeRemainingHandlerStrategy.getInstance(),
            OnOffDeviceHandlerStrategy.getInstance(),
            TransientModeHandlerStrategy.getInstance(),
            WaterHeaterHandlerStrategy.getInstance(),
            WaterHeaterEasyHandlerStrategy.getInstance()
    ).collect(Collectors.toSet()));

    private static final Set<ThingHandlerStrategy> STRATEGIES_HUB = Collections.unmodifiableSet(Stream.of(
            PhysicalDeviceHandlerStrategy.getInstance()
    ).collect(Collectors.toSet()));

    private static final Set<ThingHandlerStrategy> STRATEGIES_THERMOSTAT = Collections.unmodifiableSet(Stream.of(
            BatteryDeviceHandlerStrategy.getInstance(),
            PhysicalDeviceHandlerStrategy.getInstance(),
            ZigbeeDeviceHandlerStrategy.getInstance()
    ).collect(Collectors.toSet()));

    private static final Set<ThingHandlerStrategy> STRATEGIES_TRV_GROUP = Collections.unmodifiableSet(Stream.of(
            BoostTimeRemainingHandlerStrategy.getInstance(),
            HeatingThermostatHandlerStrategy.getInstance(),
            OnOffDeviceHandlerStrategy.getInstance(),
            TemperatureSensorHandlerStrategy.getInstance(),
            TransientModeHandlerStrategy.getInstance(),
            HeatingTransientModeHandlerStrategy.getInstance(),
            HeatingThermostatEasyHandlerStrategy.getInstance()
    ).collect(Collectors.toSet()));

    private static final Set<ThingHandlerStrategy> STRATEGIES_TRV = Collections.unmodifiableSet(Stream.of(
            BatteryDeviceHandlerStrategy.getInstance(),
            PhysicalDeviceHandlerStrategy.getInstance(),
            TemperatureSensorHandlerStrategy.getInstance(),
            ZigbeeDeviceHandlerStrategy.getInstance()
    ).collect(Collectors.toSet()));

    private final Logger logger = LoggerFactory.getLogger(HiveHandlerFactory.class);

    private final Map<ThingUID, ServiceRegistration<?>> discoveryServices = new HashMap<>();

    private final HttpClient httpClient;
    private final HiveClientFactory hiveClientFactory;

    @Activate
    public HiveHandlerFactory(@Reference final HttpClientFactory httpClientFactory) {
        final HttpClient httpClient = httpClientFactory.createHttpClient("HiveBinding");
        try {
            httpClient.start();
        } catch (Exception ex) {
            // Wrap up all exceptions and throw a runtime exception as there
            // is nothing we can do to recover.
            throw new IllegalStateException("Could not start HttpClient.", ex);
        }

        // Keep a copy of HttpClient so we can clean up later
        this.httpClient = httpClient;
        this.hiveClientFactory = new DefaultHiveClientFactory(httpClient);
    }

    @Override
    protected void activate(final ComponentContext componentContext) {
        super.activate(componentContext);

        // Help keep track of when openHAB reloads binding while debugging.
        this.logger.trace("HandlerFactory has been activated");
    }

    @Override
    protected void deactivate(final ComponentContext componentContext) {
        super.deactivate(componentContext);

        // Help keep track of when openHAB reloads binding while debugging.
        this.logger.trace("HandlerFactory has been deactivated");

        // Clean up HttpClient
        try {
            httpClient.stop();
        } catch (Exception ignored) {
            // Don't care, just trying to clean up.
        }
    }

    @Override
    public boolean supportsThingType(final ThingTypeUID thingTypeUID) {
        return HiveBindingConstants.SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(final Thing thing) {
        final ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (HiveBindingConstants.THING_TYPE_ACCOUNT.equals(thingTypeUID)) {
            // Create the handler
            final DefaultHiveAccountHandler handler = new DefaultHiveAccountHandler(
                    this.hiveClientFactory,
                    DefaultHiveDiscoveryService::new,
                    (Bridge) thing
            );

            // Register the discovery service and keep track of it so we can
            // unregister it later.
            final ServiceRegistration<?> serviceReg = bundleContext.registerService(
                    DiscoveryService.class.getName(),
                    handler.getDiscoveryService(),
                    new Hashtable<>()
            );
            final ThingUID uid = thing.getUID();
            this.discoveryServices.put(uid, serviceReg);

            return handler;
        } else if (HiveBindingConstants.THING_TYPE_BOILER_MODULE.equals(thingTypeUID)) {
            return new DefaultHiveThingHandler(thing, STRATEGIES_BOILER_MODULE);
        } else if (HiveBindingConstants.THING_TYPE_HEATING.equals(thingTypeUID)) {
            return new DefaultHiveThingHandler(thing, STRATEGIES_HEATING);
        } else if (HiveBindingConstants.THING_TYPE_HOT_WATER.equals(thingTypeUID)) {
            return new DefaultHiveThingHandler(thing, STRATEGIES_HOT_WATER);
        } else if (HiveBindingConstants.THING_TYPE_HUB.equals(thingTypeUID)) {
            return new DefaultHiveThingHandler(thing, STRATEGIES_HUB);
        } else if (HiveBindingConstants.THING_TYPE_THERMOSTAT.equals(thingTypeUID)) {
            return new DefaultHiveThingHandler(thing, STRATEGIES_THERMOSTAT);
        } else if (HiveBindingConstants.THING_TYPE_TRV.equals(thingTypeUID)) {
            return new DefaultHiveThingHandler(thing, STRATEGIES_TRV);
        } else if (HiveBindingConstants.THING_TYPE_TRV_GROUP.equals(thingTypeUID)) {
            return new DefaultHiveThingHandler(thing, STRATEGIES_TRV_GROUP);
        }

        return null;
    }

    @Override
    protected void removeHandler(final ThingHandler thingHandler) {
        if (thingHandler instanceof DefaultHiveAccountHandler) {
            this.logger.trace("Removing bridge");
            // Clean up associated discovery service.
            final @Nullable ServiceRegistration<?> serviceReg = this.discoveryServices.remove(thingHandler.getThing().getUID());

            if (serviceReg != null) {
                this.logger.trace("Unregistered discovery service");
                serviceReg.unregister();
            }
        }

        super.removeHandler(thingHandler);
    }
}
