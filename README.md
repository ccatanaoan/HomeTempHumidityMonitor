# Cloyd-Home-Temperature-Humidity-Monitor
I wrote and developed these ESP8266 program and Android mobile application client for our home automation. The mobile application can remotely control and monitor the sensor via cloud MQTT.

## Features that are sent to the Android mobile application client via cloyd MQTT
- Determine heat index
- Determine dew point
- Determine thermal comfort:
  - Empiric comfort function based on comfort profiles(parametric lines)
  - Multiple comfort profiles possible. Default based on [Cooling/Comfort] (https://c03.apogee.net/contentplayer/?coursetype=ces&utilityid=duquesnelight&id=1347)
  - Determine if it's too cold, hot, humid, dry, based on current comfort profile
  - More info at Determining Thermal Comfort Using a Humidity and Temperature Sensor
  - Determine human perception based on humidity, temperature and dew point according to Horstmeyer, Steve (2006-08-15). Relative Humidity....Relative to What? The Dew Point Temperature...a better approach

## Hardware used for Internet of Things (IoT):
* NodeMcu LUA WIFI Board Based on ESP8266 CP2102 Module.
* DHT11 Temperature & Humidity Sensor Module.

## Mobile application used for Internet of Things (IoT):
- Custom-made Android mobile application
- Cloud MQTT

This site was built using [GitHub Pages](https://pages.github.com/).