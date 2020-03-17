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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.hive.internal.handler.*;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * The {@link HiveHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.hive", service = ThingHandlerFactory.class)
public final class HiveHandlerFactory extends BaseThingHandlerFactory {
    private final Logger logger = LoggerFactory.getLogger(HiveHandlerFactory.class);

    private final Map<ThingUID, @Nullable ServiceRegistration<?>> discoveryServices = new HashMap<>();

    public HiveHandlerFactory() {
        super();
    }

    @Override
    protected void activate(final ComponentContext componentContext) {
        super.activate(componentContext);

        // Help keep track of when openHAB reloads binding while debugging.
        logger.debug("Handler has been activated");
    }

    @Override
    protected void deactivate(final ComponentContext componentContext) {
        super.deactivate(componentContext);

        // Help keep track of when openHAB reloads binding while debugging.
        logger.debug("Handler has been deactivated");
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
            final HiveAccountHandler handler = new HiveAccountHandler((Bridge) thing);

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
            return new HiveBoilerModuleHandler(thing);
        } else if (HiveBindingConstants.THING_TYPE_HEATING.equals(thingTypeUID)) {
            return new HiveHeatingHandler(thing);
        } else if (HiveBindingConstants.THING_TYPE_HOT_WATER.equals(thingTypeUID)) {
            return new HiveHotWaterHandler(thing);
        } else if (HiveBindingConstants.THING_TYPE_HUB.equals(thingTypeUID)) {
            return new HiveHubHandler(thing);
        } else if (HiveBindingConstants.THING_TYPE_THERMOSTAT.equals(thingTypeUID)) {
            return new HiveThermostatHandler(thing);
        } else if (HiveBindingConstants.THING_TYPE_TRV.equals(thingTypeUID)) {
            return new HiveTrvHandler(thing);
        } else if (HiveBindingConstants.THING_TYPE_TRV_GROUP.equals(thingTypeUID)) {
            return new HiveTrvGroupHandler(thing);
        }

        return null;
    }

    @Override
    protected void removeHandler(final ThingHandler thingHandler) {
        logger.debug("Removing handler");
        if (thingHandler instanceof HiveAccountHandler) {
            logger.debug("Handler is bridge");
            // Clean up associated discovery service.
            final @Nullable ServiceRegistration<?> serviceReg = this.discoveryServices.remove(thingHandler.getThing().getUID());

            if (serviceReg != null) {
                logger.debug("Unregistered service");
                serviceReg.unregister();
            }
        }

        super.removeHandler(thingHandler);
    }
}
