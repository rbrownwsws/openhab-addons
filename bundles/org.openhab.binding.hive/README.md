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

| Thing                                | Image                                                                                                                                                                                                                                                       | Supported | Tested |
|--------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|--------|
| Hive Thermostat Gen 1                | <img src="https://bucketeer-e9a3f077-0ec6-47cc-a7da-226f8fd8c41c.s3-eu-west-1.amazonaws.com/app/image_sources/user-guides/hive-thermostat-old-b11c8e8d7866f541a6ed5ccc06fd30e014e689fdbecc0dfaed60f649dc22a493.png" width="100" alt="Hive Thermostat V1" /> | ✓         | ✗     |
| Hive Thermostat Gen 2                | <img src="https://images.ctfassets.net/mijf9lz5yt3u/6kf14gk9NgTjDAq2IkNN2u/916eb54a1eeb821400d0aef3a1ab354a/2_hive-thermostat.jpg" width="100" alt="Hive Thermostat" />                                                                                     | ✓         | ✓     |
| Hive Radiator Valve                  | <img src="https://images.ctfassets.net/mijf9lz5yt3u/2wF4YxLe93jGDAqRvVFxWv/a0d64ffd918e7f0dabd4cc07257542af/gallery-image-1.jpg" width="100" alt="Hive Radiator Valve" />                                                                                   | ✓         | ✓     |
| Hive Boiler Module                   | <img src="https://images.ctfassets.net/mijf9lz5yt3u/3DfQ7wNAgd7JgMa2nYMc7W/8b639f5e277f6eed8e25b1c3a1d8ce6b/1_hive-active-hubless-heating.jpg" width="100" alt="hive active heating receiver" />                                                            | ✓         | ✓     |

_N.B. At the moment I have not kept track of the firmware / hardware revision of devices.  Hopefully the Hive API abstracts away from this so it should not be a problem._

## Discovery

After manually adding a "Hive Account" thing all the things associated with that account will be auto-discovered
and appear in the UI Inbox.


## Thing Configuration

### Hive Account

| Parameter       | Required | Description                                                                             |
|-----------------|----------|-----------------------------------------------------------------------------------------|
| username        | YES      | Hive account username (same as in mobile app)                                           |
| password        | YES      | Hive account password (same as in mobile app)                                           |
| pollingInterval | YES      | The time in seconds to wait between each poll of the Hive API for updates (default: 10) |

### Other Things

It is recommended that you use auto-discovery and the UI to add things other than `Hive Account`s.
You should not have to modify these parameters if you use auto-discovery.

`bridge` - The `Hive Account` to use to communicate with this thing.

| Parameter | Required | Description                                                 |
|-----------|----------|-------------------------------------------------------------|
| nodeId    | YES      | The Hive API `nodeId` that identifies the thing.            |

_N.B. You can only get the nodeId of thing by querying the Hive API.  This is why use of auto-discovery is recommended._

## Channels

_Here you should provide information about available channel types, what their meaning is and how they can be used._

_Note that it is planned to generate some part of this based on the XML files within ```src/main/resources/ESH-INF/thing``` of your binding._
### Hive Account

No channels (just acts as a bridge)

### Hive Boiler Module

| Channel                  | Type               | Read/Write   | Description                                                                    |
|--------------------------|--------------------|--------------|--------------------------------------------------------------------------------|
| radio-lqi-average        | Number             | Read Only    | The average zigbee radio **L**ink **Q**uality **I**ndicator                    |
| radio-lqi-last_known     | Number             | Read Only    | The last known zigbee radio **L**ink **Q**uality **I**ndicator                 |
| radio-rssi-average       | Number             | Read Only    | The average zigbee radio **R**eceived **S**ignal **S**trength **I**ndicator    |
| radio-rssi-last_known    | Number             | Read Only    | The last known zigbee radio **R**eceived **S**ignal **S**trength **I**ndicator |


### Hive Heating Zone

| Channel                  | Type               | Read/Write   | Description                  |
|--------------------------|--------------------|--------------|------------------------------|
| temperature-current      | Number:Temperature | Read Only    | The current temperature of the zone.  Only in Celsius for now. |
| temperature-target       | Number:Temperature | Read/Write   | The temperature you want the zone to be heated to.  Only in Celsius for now. |
| mode-operating           | String             | Read/Write   | The operating mode of heating in the zone (Schedule vs. Manual). |
| mode-on_off              | Switch             | Read/Write   | Is heating for this zone turned on or off. |
| state-operating          | String             | Read Only    | Is heating for this zone active? (OFF/HEAT). |
| mode-operating-override  | Switch             | Read/Write   | Is the transient override (boost) active for this zone. |
| temperature-target-boost | Number:Temperature | Read/Write   | The temperature you want the zone to be heated to when the transient override (boost) is active.  Only in Celsius for now. |
| transient-duration       | Number             | Read/Write   | How long in minutes the transient override (boost) should be active for once started. |
| transient-start_time     | DateTime           | Read Only    | The last time the transient override (boost) was started. |
| transient-end_time       | DateTime           | Read Only    | The last time the transient override (boost) was scheduled to end (even if it was cancelled). |
| transient-remaining      | Number             | Read Only    | The time between now and `transient-end_time` in minutes. _N.B. This does not reset to 0 if you cancel the transient override (boost)._ |


### Hive Hot Water

| Channel                  | Type               | Read/Write   | Description                  |
|--------------------------|--------------------|--------------|------------------------------|
| mode-operating           | String             | Read/Write   | The operating mode of the hot water (Schedule vs. Manual). |
| mode-on_off              | Switch             | Read/Write   | Is the hot water turned on or off. |
| is_on                    | Switch             | Read Only    | Is the hot water currently being heated. |
| transient-duration       | Number             | Read/Write   | How long in minutes the transient override (boost) should be active for once started. |
| transient-start_time     | DateTime           | Read Only    | The last time the transient override (boost) was started. |
| transient-end_time       | DateTime           | Read Only    | The last time the transient override (boost) was scheduled to end (even if it was cancelled). |
| transient-remaining      | Number             | Read Only    | The time between now and `transient-end_time` in minutes. _N.B. This does not reset to 0 if you cancel the transient override (boost)._ |


### Hive Hub

No channels exposed in current version of binding.

### Hive Thermostat

| Channel                  | Type               | Read/Write   | Description                  |
|--------------------------|--------------------|--------------|------------------------------|
| battery-level            | Number             | Read Only    | The percentage of charge the device's battery has left. _N.B. This seems to be a very course measurement so don't expect to see it change very often._ |
| battery-low              | Switch             | Read Only    | Turns "On" when the battery is low. _N.B. I don't have any devices with low battery at the moment so my implementation may be wrong.  This may give a false alarm for now._ |
| radio-lqi-average        | Number             | Read Only    | The average zigbee radio **L**ink **Q**uality **I**ndicator                    |
| radio-lqi-last_known     | Number             | Read Only    | The last known zigbee radio **L**ink **Q**uality **I**ndicator                 |
| radio-rssi-average       | Number             | Read Only    | The average zigbee radio **R**eceived **S**ignal **S**trength **I**ndicator    |
| radio-rssi-last_known    | Number             | Read Only    | The last known zigbee radio **R**eceived **S**ignal **S**trength **I**ndicator |


### Hive Radiator Valve

| Channel                  | Type               | Read/Write   | Description                  |
|--------------------------|--------------------|--------------|------------------------------|
| temperature-current      | Number:Temperature | Read Only    |                              |
| battery-level            | Number             | Read Only    | The percentage of charge the device's battery has left. _N.B. This seems to be a very course measurement so don't expect to see it change very often._ |
| battery-low              | Switch             | Read Only    | Turns "On" when the battery is low. _N.B. I don't have any devices with low battery at the moment so my implementation may be wrong.  This may give a false alarm for now._ |
| radio-lqi-average        | Number             | Read Only    | The average zigbee radio **L**ink **Q**uality **I**ndicator                    |
| radio-lqi-last_known     | Number             | Read Only    | The last known zigbee radio **L**ink **Q**uality **I**ndicator                 |
| radio-rssi-average       | Number             | Read Only    | The average zigbee radio **R**eceived **S**ignal **S**trength **I**ndicator    |
| radio-rssi-last_known    | Number             | Read Only    | The last known zigbee radio **R**eceived **S**ignal **S**trength **I**ndicator |


### Hive Radiator Valve Heating Zone

| Channel                  | Type               | Read/Write   | Description                  |
|--------------------------|--------------------|--------------|------------------------------|
| temperature-current      | Number:Temperature | Read Only    | The current temperature of the zone.  Only in Celsius for now. |
| temperature-target       | Number:Temperature | Read/Write   | The temperature you want the zone to be heated to.  Only in Celsius for now. |
| mode-operating           | String             | Read/Write   | The operating mode of heating in the zone (Schedule vs. Manual). |
| mode-on_off              | Switch             | Read/Write   | Is heating for this zone turned on or off. |
| state-operating          | String             | Read Only    | Is heating for this zone active? (OFF/HEAT). |
| mode-operating-override  | Switch             | Read/Write   | Is the transient override (boost) active for this zone. |
| temperature-target-boost | Number:Temperature | Read/Write   | The temperature you want the zone to be heated to when the transient override (boost) is active.  Only in Celsius for now. |
| transient-duration       | Number             | Read/Write   | How long in minutes the transient override (boost) should be active for once started. |
| transient-start_time     | DateTime           | Read Only    | The last time the transient override (boost) was started. |
| transient-end_time       | DateTime           | Read Only    | The last time the transient override (boost) was scheduled to end (even if it was cancelled). |
| transient-remaining      | Number             | Read Only    | The time between now and `transient-end_time` in minutes. _N.B. This does not reset to 0 if you cancel the transient override (boost)._ |


## Full Example

### Things
Add using the UI and auto-discovery as explained above.

### example.items

```
// Thermostat / TRV Heating Zone
Number:Temperature LivingRoom_HeatingZone_Temperature            "Current Temperature"         <temperature> {channel="hive:node:deadbeef-dead-beef-dead-beefdeadbeef:temperature-current"}
Number:Temperature LivingRoom_HeatingZone_TargetTemperature      "Target Temperature"          <heating>     {channel="hive:node:deadbeef-dead-beef-dead-beefdeadbeef:temperature-target"}
String             LivingRoom_HeatingZone_OperatingMode          "Heating Mode"                <heating>     {channel="hive:node:deadbeef-dead-beef-dead-beefdeadbeef:mode-operating"}
String             LivingRoom_HeatingZone_OperatingState         "Heating State"               <heating>     {channel="hive:node:deadbeef-dead-beef-dead-beefdeadbeef:state-operating"}
Switch             LivingRoom_HeatingZone_OnOff                  "Heating Enabled"             <heating>     {channel="hive:node:deadbeef-dead-beef-dead-beefdeadbeef:mode-on_off"}
Switch             LivingRoom_HeatingZone_BoostActive            "Boost Active"                <heating>     {channel="hive:node:deadbeef-dead-beef-dead-beefdeadbeef:mode-operating-override"}
Number             LivingRoom_HeatingZone_BoostDuration          "Boost Duration [%d minutes]" <heating>     {channel="hive:node:deadbeef-dead-beef-dead-beefdeadbeef:transient-duration"}
Number:Temperature LivingRoom_HeatingZone_BoostTargetTemperature "Boost Target Temperature"    <heating>     {channel="hive:node:deadbeef-dead-beef-dead-beefdeadbeef:temperature-target-boost"}

// Hot Water
Switch            HotWater_On                                    "Hot Water On"                <water>       {channel="hive:node:cafebabe-cafe-babe-cafe-babecafebabe:is_on"}
String            HotWater_Operating_Mode                        "Hot Water Operating Mode"    <water>       {channel="hive:node:cafebabe-cafe-babe-cafe-babecafebabe:mode-operating"}
Switch            HotWater_OnOff                                 "Hot Water Enabled"           <water>       {channel="hive:node:cafebabe-cafe-babe-cafe-babecafebabe:mode-on_off"}
Switch            HotWater_BoostActive                           "Boost Active"                <water>       {channel="hive:node:cafebabe-cafe-babe-cafe-babecafebabe:mode-operating-override"}
Number            HotWater_BoostDuration                         "Boost Duration [%d minutes]" <water>       {channel="hive:node:cafebabe-cafe-babe-cafe-babecafebabe:transient-duration"}
```

### example.sitemap

```
sitemap home label="Home" {
    Frame label="Living Room" icon="sofa" {
        Default  item=LivingRoom_TRV_Temperature
        Setpoint item=LivingRoom_HeatingZone_TargetTemperature minValue=7 maxValue=25
        Default  item=LivingRoom_HeatingZone_OperatingMode
        Default  item=LivingRoom_HeatingZone_OperatingState
        Default  item=LivingRoom_HeatingZone_OnOff
        Default  item=LivingRoom_HeatingZone_BoostActive
        Default  item=LivingRoom_HeatingZone_BoostDuration
        Setpoint item=LivingRoom_HeatingZone_BoostTargetTemperature minValue=7 maxValue=25
    }

    Frame label="Hot Water" icon="water" {
        Text    item=HotWater_On label="Hot Water is currently [%s]"
        Default item=HotWater_Operating_Mode
        Default item=HotWater_OnOff
        Default item=HotWater_BoostActive
        Default item=HotWater_BoostDuration
    }
}
```
