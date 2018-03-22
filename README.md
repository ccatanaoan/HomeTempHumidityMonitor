# Cloyd-Home-Weather-Station
I wrote and developed these ESP8266 program and Android mobile application client for our home automation. The mobile application can remotely control and monitor the sensor via cloud MQTT.

The ESP8266 WiFi Module is a self contained system on chip (SoC) with integrated TCP/IP protocol stack that can give any microcontroller access to the WiFi network.

## Information sent from the sensor to the Android mobile app via cloud MQTT
- Temperature
- Relative humidity
- Heat index
- Dew point
- Thermal comfort:
  - Empiric comfort function based on comfort profiles(parametric lines)
  - Multiple comfort profiles possible. Default based on [Cooling/Comfort](https://c03.apogee.net/contentplayer/?coursetype=ces&utilityid=duquesnelight&id=1347)
  - Determine if it's too cold, hot, humid, dry, based on current comfort profile
  - More info at [Determining Thermal Comfort Using a Humidity and Temperature Sensor](https://www.azosensors.com/article.aspx?ArticleID=487)
- Human perception based on humidity, temperature and dew point according to Horstmeyer, Steve (2006-08-15). [Relative Humidity....Relative to What? The Dew Point Temperature...a better approach](http://www.shorstmeyer.com/wxfaqs/humidity/humidity.html)

## Hardware used for Internet of Things (IoT):
* NodeMcu LUA WIFI Board Based on ESP8266 CP2102 Module.
* DHT11 Temperature & Humidity Sensor Module.

## Mobile application used for Internet of Things (IoT):
- Custom-made Android mobile application
- Cloud MQTT