# Hive Binding

<p>
<img src="https://www.hivehome.com/assets/hive-primary-logo-d6f32b5c6a0a3ecde57e3c6dafead4e6d8a4fb5230177bd85f3aedccf9d59382.svg" alt="Centrica Hive Limited logo" />
</p>

This binding integrates the [Hive smart home system](https://www.hivehome.com) using the Hive REST API.

_N.B. As this binding uses the Hive REST API it can only integrate Hive branded devices connected to a Hive Hub.
Setups that do not include a Hive Hub or connect to a different brand of Zigbee hub/bridge will not work with this binding._

| Hub Type     | Image |
|--------------|-------|
| Hive Hub     | <img src="https://bucketeer-e9a3f077-0ec6-47cc-a7da-226f8fd8c41c.s3-eu-west-1.amazonaws.com/app/image_sources/skus/HCD00010-ab3f78de42c32eef68fde6c90578eed4f6e88e5ac2a24df6f3a8aa0af5b38699.png" width="100" alt="Hive Hub" /> |
| Hive Hub 360 | <img src="https://images.ctfassets.net/mijf9lz5yt3u/6i0dFZIMOgV4uB065KVuRH/e35ed4b672d0a6d57a12dc754254befa/gallery-image-1.png" width="100" alt="Hive Hub 360" /> |

## Supported Things

_Please describe the different supported things / devices within this section._
_Which different types are supported, which models were tested etc.?_
_Note that it is planned to generate some part of this based on the XML files within ```src/main/resources/ESH-INF/thing``` of your binding._

<img src="https://images.ctfassets.net/mijf9lz5yt3u/3DfQ7wNAgd7JgMa2nYMc7W/8b639f5e277f6eed8e25b1c3a1d8ce6b/1_hive-active-hubless-heating.jpg" width="100" alt="hive active heating receiver" />

| Thing                                | Image                                                                                                                                                                                                                                                       | Supported | Tested |
|--------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|--------|
| Hive Thermostat V1                   | <img src="https://bucketeer-e9a3f077-0ec6-47cc-a7da-226f8fd8c41c.s3-eu-west-1.amazonaws.com/app/image_sources/user-guides/hive-thermostat-old-b11c8e8d7866f541a6ed5ccc06fd30e014e689fdbecc0dfaed60f649dc22a493.png" width="100" alt="Hive Thermostat V1" /> | ✓         | ✗     |
| Hive Thermostat V2                   | <img src="https://images.ctfassets.net/mijf9lz5yt3u/6kf14gk9NgTjDAq2IkNN2u/916eb54a1eeb821400d0aef3a1ab354a/2_hive-thermostat.jpg" width="100" alt="Hive Thermostat" />                                                                                     | ✓         | ✓     |
| Hive Radiator Valve                  | <img src="https://images.ctfassets.net/mijf9lz5yt3u/2wF4YxLe93jGDAqRvVFxWv/a0d64ffd918e7f0dabd4cc07257542af/gallery-image-1.jpg" width="100" alt="Hive Radiator Valve" />                                                                                   | ✓         | ✓     |
| Hive Plug                            | <img src="https://images.ctfassets.net/mijf9lz5yt3u/6Lig0F2WtiPbDHj8SmB3zL/ce80da3d13cdfd06f1ed5c830c542445/gallery-image-1.jpg" width="100" alt="Hive Plug" />                                                                                             | ✗         | ✗     |
| Hive Motion Sensor                   | <img src="https://images.ctfassets.net/mijf9lz5yt3u/5Fu5Cp6T8M85mWgOO8xXOr/e116fe5b2ae9cfa7a4a0e5fa194ccce5/gallery-image-1.jpg" width="100" alt="Hive Motion Sensor" />                                                                                    | ✗         | ✗     |
| Hive Window or Door Sensor           | <img src="https://images.ctfassets.net/mijf9lz5yt3u/1qLpKsGtOiD1UAk0l8MjTr/e4191b4d0bdc5cb30187c2d3219615e9/gallery-image-1.jpg" width="100" alt="Hive Window or Door Sensor" />                                                                            | ✗         | ✗     |
| Hive Dimmable Light Bulb\*           | <img src="https://images.ctfassets.net/mijf9lz5yt3u/2hWmSpizASY8vHfEMPYwpI/35d12e81d9a86a177e7879ac5b7cd61a/gallery-image-1.jpg" width="100" alt="Hive Dimmable Light Bulb" />                                                                              | ✗         | ✗     |
| Hive Cool to Warm White Light Bulb\* | <img src="https://images.ctfassets.net/mijf9lz5yt3u/11R0N4QsLzu6gxmkLIq8Rg/82f79c892a378cf3d650ec47ea467b42/gallery-image-1.jpg" width="100" alt="Hive Cool to Warm Whit Light Bulb" />                                                                     | ✗         | ✗     |
| Hive Colour Changing Light Bulb\*    | <img src="https://images.ctfassets.net/mijf9lz5yt3u/4nELGVWquXwItc0XqCaHmJ/8b7d4052b6010a291eb0112e662f34cd/gallery-image-1.jpg" width="100" alt="Hive Colour Changing Light Bulb" />                                                                       | ✗         | ✗     |
| Hive View                            | <img src="https://images.ctfassets.net/mijf9lz5yt3u/w24Irav7f1vHdd4UFUccM/ef4a3194d9b87e3e04fc269d77cbed86/gallery-image-1.jpg" width="100" alt="Hive View" />                                                                                              | ✗         | ✗     |
| Hive View Outdoor                    | <img src="https://images.ctfassets.net/mijf9lz5yt3u/3UTDzV9RyNeKrCBi3pDi8X/3648d3ae40cf353cd1d09bf24c13f7f9/gallery-image-2.jpg" width="100" alt="Hive View Outdoor" />                                                                                     | ✗         | ✗     |

\*Hive Light Bulbs come in B22, E27, E14 and GU10 variants.  Only one of these variants is pictured.

## Discovery

_Describe the available auto-discovery features here. Mention for what it works and what needs to be kept in mind when using it._

## Binding Configuration

_If your binding requires or supports general configuration settings, please create a folder ```cfg``` and place the configuration file ```<bindingId>.cfg``` inside it. In this section, you should link to this file and provide some information about the options. The file could e.g. look like:_

```
# Configuration for the Philips Hue Binding
#
# Default secret key for the pairing of the Philips Hue Bridge.
# It has to be between 10-40 (alphanumeric) characters
# This may be changed by the user for security reasons.
secret=openHABSecret
```

_Note that it is planned to generate some part of this based on the information that is available within ```src/main/resources/ESH-INF/binding``` of your binding._

_If your binding does not offer any generic configurations, you can remove this section completely._

## Thing Configuration

_Describe what is needed to manually configure a thing, either through the (Paper) UI or via a thing-file. This should be mainly about its mandatory and optional configuration parameters. A short example entry for a thing file can help!_

_Note that it is planned to generate some part of this based on the XML files within ```src/main/resources/ESH-INF/thing``` of your binding._

## Channels

_Here you should provide information about available channel types, what their meaning is and how they can be used._

_Note that it is planned to generate some part of this based on the XML files within ```src/main/resources/ESH-INF/thing``` of your binding._

| channel  | type   | description                  |
|----------|--------|------------------------------|
| control  | Switch | This is the control channel  |

## Full Example

_Provide a full usage example based on textual configuration files (*.things, *.items, *.sitemap)._

## Any custom content here!

_Feel free to add additional sections for whatever you think should also be mentioned about your binding!_

[hive-hub]: ""