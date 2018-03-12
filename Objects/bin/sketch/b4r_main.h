
#ifndef b4r_main_h
#define b4r_main_h

class b4r_main {
public:

static void initializeProcessGlobals();
static void _appstart();
static void _cleareeprom();
static void _connecttowifi();
static void _mqtt_connect(Byte _unused);
static void _mqtt_disconnected();
static void _mqtt_messagearrived(B4R::B4RString* _topic,B4R::Array* _payload);
static void _process_globals();
static B4R::Serial* _serial1;
static B4R::D1Pins* _d1pins;
static B4R::Pin* _pin16;
static B4R::Pin* _d6;
static B4R::B4RESPWiFi* _wifi;
static B4R::WiFiSocket* _wifistr;
static B4R::MqttClient* _mqtt;
static B4R::MqttConnectOptions* _mqttopt;
static B4R::B4RString* _mqttuser;
static B4R::B4RString* _mqttpassword;
static B4R::B4RString* _mqtthostname;
static Int _mqttport;
static bool _stoprelay;
static B4R::B4RESP8266* _esp;
static B4R::B4RString* _wifissid;
static B4R::B4RString* _wifipassword;
static ULong _relayopendelay;
static ULong _relaycloseddelay;
static B4R::B4RDht* _dht11sensor;
static B4R::Timer* _timer1;
static B4R::Pin* _dht11pin;
static Int _interval;
static B4R::D1Pins* _d3pins;
static Double _humidity;
static Double _temperature;
static void _readfromeeprom();
static void _relayclose(Byte _tag);
static void _relayopen(Byte _tag);
static void _timer1_tick();
static void _writetoeeprom(ULong _opendelay,ULong _closeddelay);
};

#endif