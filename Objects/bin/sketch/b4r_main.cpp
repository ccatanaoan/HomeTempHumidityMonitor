#include "B4RDefines.h"

B4R::Serial* b4r_main::_serial1;
B4R::D1Pins* b4r_main::_d1pins;
B4R::Pin* b4r_main::_pin16;
B4R::Pin* b4r_main::_d6;
B4R::B4RESPWiFi* b4r_main::_wifi;
B4R::WiFiSocket* b4r_main::_wifistr;
B4R::MqttClient* b4r_main::_mqtt;
B4R::MqttConnectOptions* b4r_main::_mqttopt;
B4R::B4RString* b4r_main::_mqttuser;
B4R::B4RString* b4r_main::_mqttpassword;
B4R::B4RString* b4r_main::_mqtthostname;
Int b4r_main::_mqttport;
B4R::B4RESP8266* b4r_main::_esp;
B4R::B4RString* b4r_main::_wifissid;
B4R::B4RString* b4r_main::_wifipassword;
B4R::Pin* b4r_main::_dht22pin;
B4R::D1Pins* b4r_main::_d3pins;
Double b4r_main::_dht22temp;
Double b4r_main::_dht22hum;
Int b4r_main::_dht22state;
Double b4r_main::_dht22heatindex;
Double b4r_main::_dht22dewpoint;
Int b4r_main::_dht22perception;
Int b4r_main::_dht22comfortstatus;
B4R::Pin* b4r_main::_mq7pin;
Byte b4r_main::_mq7pinnumber;
UInt b4r_main::_readvoltage;
static B4R::Serial be_gann1_3;
static B4R::D1Pins be_gann2_3;
static B4R::Pin be_gann3_3;
static B4R::Pin be_gann4_3;
static B4R::B4RESPWiFi be_gann5_3;
static B4R::WiFiSocket be_gann6_3;
static B4R::MqttClient be_gann7_3;
static B4R::MqttConnectOptions be_gann8_3;
static B4R::B4RString be_gann9_5;
static B4R::B4RString be_gann10_5;
static B4R::B4RString be_gann11_5;
static B4R::B4RESP8266 be_gann13_3;
static B4R::B4RString be_gann14_5;
static B4R::B4RString be_gann15_5;
static B4R::Pin be_gann16_3;
static B4R::D1Pins be_gann17_3;
static B4R::Pin be_gann25_3;


 #include "DHTesp.h"

#define DHT22Pin 0 //change here the IO-Pin

ComfortState cf;
DHTesp dht22;

void setup(B4R::Object* o){
	dht22.setup(DHT22Pin);
}

 void SetSTA(B4R::Object* o) {
   WiFi.mode(WIFI_STA);
}

void ReadDHT22(B4R::Object* o) {
	 b4r_main::_dht22state = dht22.getStatus();
	 // Add 12 percent based on home hygrometer.
 	 float humidity = dht22.getHumidity() + 13.5;
 	 float temperature = dht22.toFahrenheit(dht22.getTemperature()) + 0.5;
     b4r_main::_dht22hum  = humidity; 
     b4r_main::_dht22temp = temperature; 
	 b4r_main::_dht22heatindex = dht22.computeHeatIndex(temperature, humidity, true);
	 b4r_main::_dht22dewpoint = dht22.computeDewPoint(temperature, humidity, true);
	 b4r_main::_dht22perception = dht22.computePerception(temperature, humidity, true);
	 float cr = dht22.getComfortRatio(cf, temperature, humidity, true);

	  switch(cf) {
	    case Comfort_OK:
	      b4r_main::_dht22comfortstatus = 0;
	      break;
	    case Comfort_TooHot:
	      b4r_main::_dht22comfortstatus = 1;
	      break;
	    case Comfort_TooCold:
	      b4r_main::_dht22comfortstatus = 2;
	      break;
	    case Comfort_TooDry:
	      b4r_main::_dht22comfortstatus = 4;
	      break;
	    case Comfort_TooHumid:
	      b4r_main::_dht22comfortstatus = 8;
	      break;
	    case Comfort_HotAndHumid:
	      b4r_main::_dht22comfortstatus = 9;
	      break;
	    case Comfort_HotAndDry:
	      b4r_main::_dht22comfortstatus = 5;
	      break;
	    case Comfort_ColdAndHumid:
	      b4r_main::_dht22comfortstatus = 10;
	      break;
	    case Comfort_ColdAndDry:
	      b4r_main::_dht22comfortstatus = 6;
	      break;
	    default:
	      b4r_main::_dht22comfortstatus = -1;
	      break;
	  };
  }
void b4r_main::_appstart(){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString* _clientid = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 75;BA.debugLine="Private Sub AppStart";
 //BA.debugLineNum = 76;BA.debugLine="Serial1.Initialize(115200)";
b4r_main::_serial1->Initialize((ULong) (115200));
 //BA.debugLineNum = 77;BA.debugLine="Delay(3000)";
Common_Delay((ULong) (3000));
 //BA.debugLineNum = 78;BA.debugLine="Log(\"AppStart\")";
B4R::Common::LogHelper(1,102,F("AppStart"));
 //BA.debugLineNum = 83;BA.debugLine="pin16.Initialize(16, pin16.MODE_OUTPUT)";
b4r_main::_pin16->Initialize((Byte) (16),Pin_MODE_OUTPUT);
 //BA.debugLineNum = 84;BA.debugLine="d6.Initialize(d1pins.D6, d6.MODE_OUTPUT)";
b4r_main::_d6->Initialize(b4r_main::_d1pins->D6,Pin_MODE_OUTPUT);
 //BA.debugLineNum = 90;BA.debugLine="ConnectToWifi";
_connecttowifi();
 //BA.debugLineNum = 93;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'creat";
_clientid = B4R::B4RString::fromNumber((Long)(Common_Rnd((Long) (0),(Long) (999999999))));
 //BA.debugLineNum = 94;BA.debugLine="MQTT.Initialize2(WiFiStr.stream, MQTTHostName, MQ";
b4r_main::_mqtt->Initialize2(b4r_main::_wifistr->getStream(),b4r_main::_mqtthostname,(UInt) (b4r_main::_mqttport),_clientid,_mqtt_messagearrived,_mqtt_disconnected);
 //BA.debugLineNum = 95;BA.debugLine="MQTTOpt.Initialize(MQTTUser, MQTTPassword)";
b4r_main::_mqttopt->Initialize(b4r_main::_mqttuser,b4r_main::_mqttpassword);
 //BA.debugLineNum = 96;BA.debugLine="MQTT_Connect(0)";
_mqtt_connect((Byte) (0));
 //BA.debugLineNum = 99;BA.debugLine="DHT22pin.Initialize(d3pins.D3, DHT22pin.MODE_INPU";
b4r_main::_dht22pin->Initialize(b4r_main::_d3pins->D3,Pin_MODE_INPUT);
 //BA.debugLineNum = 100;BA.debugLine="RunNative(\"setup\",Null)";
Common_RunNative(setup,Common_Null);
 //BA.debugLineNum = 101;BA.debugLine="ReadWeather(0)";
_readweather((Byte) (0));
 //BA.debugLineNum = 104;BA.debugLine="MQ7Pin.Initialize(MQ7PinNumber, MQ7Pin.MODE_INPUT";
b4r_main::_mq7pin->Initialize(b4r_main::_mq7pinnumber,Pin_MODE_INPUT);
 //BA.debugLineNum = 105;BA.debugLine="Preparation1(0)";
_preparation1((Byte) (0));
 //BA.debugLineNum = 106;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_connecttowifi(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 152;BA.debugLine="Sub ConnectToWifi";
 //BA.debugLineNum = 153;BA.debugLine="Log(\"Connecting to WiFi\")";
B4R::Common::LogHelper(1,102,F("Connecting to WiFi"));
 //BA.debugLineNum = 154;BA.debugLine="WiFi.Connect2(WiFiSSID, WiFiPassword)";
b4r_main::_wifi->Connect2(b4r_main::_wifissid,b4r_main::_wifipassword);
 //BA.debugLineNum = 156;BA.debugLine="If WiFi.IsConnected Then";
if (b4r_main::_wifi->getIsConnected()) { 
 //BA.debugLineNum = 157;BA.debugLine="Log(\"Connected to \",WiFiSSID,\" network, Local IP";
B4R::Common::LogHelper(4,102,F("Connected to "),101,b4r_main::_wifissid->data,102,F(" network, Local IP "),101,b4r_main::_wifi->getLocalIp()->data);
 }else {
 //BA.debugLineNum = 159;BA.debugLine="Log(\"Not Connected to WiFi\")";
B4R::Common::LogHelper(1,102,F("Not Connected to WiFi"));
 };
 //BA.debugLineNum = 161;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_connect(Byte _unused){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString be_ann57_4;
B4R::B4RString be_ann58_4;
 //BA.debugLineNum = 108;BA.debugLine="Sub MQTT_Connect(Unused As Byte)";
 //BA.debugLineNum = 109;BA.debugLine="If WiFi.IsConnected = False Then";
if (b4r_main::_wifi->getIsConnected()==Common_False) { 
 //BA.debugLineNum = 110;BA.debugLine="ConnectToWifi";
_connecttowifi();
 };
 //BA.debugLineNum = 113;BA.debugLine="If MQTT.Connect = False Then";
if (b4r_main::_mqtt->Connect()==Common_False) { 
 //BA.debugLineNum = 114;BA.debugLine="Log(\"Connecting to broker\")";
B4R::Common::LogHelper(1,102,F("Connecting to broker"));
 //BA.debugLineNum = 115;BA.debugLine="MQTT.Connect2(MQTTOpt)";
b4r_main::_mqtt->Connect2(b4r_main::_mqttopt);
 //BA.debugLineNum = 116;BA.debugLine="CallSubPlus(\"MQTT_Connect\", 1000, 0)";
B4R::__c->CallSubPlus(_mqtt_connect,(ULong) (1000),(Byte) (0));
 }else {
 //BA.debugLineNum = 118;BA.debugLine="pin16.DigitalWrite(False)";
b4r_main::_pin16->DigitalWrite(Common_False);
 //BA.debugLineNum = 119;BA.debugLine="Log(\"Connected to broker\")";
B4R::Common::LogHelper(1,102,F("Connected to broker"));
 //BA.debugLineNum = 120;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
b4r_main::_mqtt->Subscribe(be_ann57_4.wrap("TempHumid"),(Byte) (0));
 //BA.debugLineNum = 121;BA.debugLine="MQTT.Subscribe(\"MQ7\", 0)";
b4r_main::_mqtt->Subscribe(be_ann58_4.wrap("MQ7"),(Byte) (0));
 };
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_disconnected(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 145;BA.debugLine="Sub mqtt_Disconnected";
 //BA.debugLineNum = 146;BA.debugLine="pin16.DigitalWrite(True)";
b4r_main::_pin16->DigitalWrite(Common_True);
 //BA.debugLineNum = 147;BA.debugLine="Log(\"Disconnected from broker\")";
B4R::Common::LogHelper(1,102,F("Disconnected from broker"));
 //BA.debugLineNum = 148;BA.debugLine="MQTT.Close";
b4r_main::_mqtt->Close();
 //BA.debugLineNum = 149;BA.debugLine="MQTT_Connect(0)";
_mqtt_connect((Byte) (0));
 //BA.debugLineNum = 150;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_messagearrived(B4R::B4RString* _topic,B4R::Array* _payload){
const UInt cp = B4R::StackMemory::cp;
B4R::Object be_ann63_8;
B4R::B4RString be_ann64_3;
B4R::B4RString be_ann65_3;
B4R::B4RString be_ann66_4;
B4R::B4RString be_ann66_6;
B4R::B4RString be_ann68_3;
B4R::B4RString be_ann71_3;
B4R::B4RString be_ann72_3;
B4R::B4RString* _s = B4R::B4RString::EMPTY;
B4R::B4RString be_ann74_10;
B4R::B4RString* be_ann74_13e1[2];
B4R::Array be_ann74_13e2;
B4R::B4RString be_ann75_4;
 //BA.debugLineNum = 125;BA.debugLine="Sub mqtt_MessageArrived (Topic As String, Payload(";
 //BA.debugLineNum = 126;BA.debugLine="pin16.DigitalWrite(False)";
b4r_main::_pin16->DigitalWrite(Common_False);
 //BA.debugLineNum = 127;BA.debugLine="Log(\"Message arrived. Topic=\", Topic, \" Payload=\"";
B4R::Common::LogHelper(4,102,F("Message arrived. Topic="),101,_topic->data,102,F(" Payload="),100,be_ann63_8.wrapPointer(_payload));
 //BA.debugLineNum = 129;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic)->equals(be_ann64_3.wrap("TempHumid"))) { 
 //BA.debugLineNum = 130;BA.debugLine="If Payload = \"Restart controller\" Then";
if ((_payload)->equals((be_ann65_3.wrap("Restart controller"))->GetBytes())) { 
 //BA.debugLineNum = 131;BA.debugLine="MQTT.Publish(\"TempHumid\",\"*Restarting relay by";
b4r_main::_mqtt->Publish(be_ann66_4.wrap("TempHumid"),(be_ann66_6.wrap("*Restarting relay by remote"))->GetBytes());
 //BA.debugLineNum = 132;BA.debugLine="ESP.Restart";
b4r_main::_esp->Restart();
 }else if((_payload)->equals((be_ann68_3.wrap("Read weather"))->GetBytes())) { 
 //BA.debugLineNum = 134;BA.debugLine="ReadWeather(0)";
_readweather((Byte) (0));
 };
 }else if((_topic)->equals(be_ann71_3.wrap("MQ7"))) { 
 //BA.debugLineNum = 137;BA.debugLine="If Payload = \"Read voltage\" Then";
if ((_payload)->equals((be_ann72_3.wrap("Read voltage"))->GetBytes())) { 
 //BA.debugLineNum = 138;BA.debugLine="Dim s As String";
_s = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 139;BA.debugLine="s = JoinStrings(Array As String(\"readVoltage|\",";
_s = B4R::__c->JoinStrings(be_ann74_13e2.create(be_ann74_13e1,2,100,be_ann74_10.wrap("readVoltage|"),B4R::B4RString::fromNumber((Long)(b4r_main::_readvoltage))));
 //BA.debugLineNum = 140;BA.debugLine="MQTT.Publish(\"MQ7\",s)";
b4r_main::_mqtt->Publish(be_ann75_4.wrap("MQ7"),(_s)->GetBytes());
 };
 };
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_preparation1(Byte _tag){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 236;BA.debugLine="Sub Preparation1(tag As Byte)";
 //BA.debugLineNum = 237;BA.debugLine="Log(\"Turn the heater fully on\")";
B4R::Common::LogHelper(1,102,F("Turn the heater fully on"));
 //BA.debugLineNum = 238;BA.debugLine="MQ7Pin.AnalogWrite(1024) ' HIGH = 1024";
b4r_main::_mq7pin->AnalogWrite((UInt) (1024));
 //BA.debugLineNum = 239;BA.debugLine="Log(\"Heat for 1 min\")";
B4R::Common::LogHelper(1,102,F("Heat for 1 min"));
 //BA.debugLineNum = 240;BA.debugLine="CallSubPlus(\"Preparation2\",60000,0)";
B4R::__c->CallSubPlus(_preparation2,(ULong) (60000),(Byte) (0));
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_preparation2(Byte _tag){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 243;BA.debugLine="Sub Preparation2(tag As Byte)";
 //BA.debugLineNum = 244;BA.debugLine="Log(\"Now reducing the heating power: turn the hea";
B4R::Common::LogHelper(1,102,F("Now reducing the heating power: turn the heater to approx 1.5V"));
 //BA.debugLineNum = 245;BA.debugLine="MQ7Pin.AnalogWrite(307.2) ' 1024x1500/5000; 1024";
b4r_main::_mq7pin->AnalogWrite((UInt) (307.2));
 //BA.debugLineNum = 246;BA.debugLine="Log(\"Heat for 90 sec\")";
B4R::Common::LogHelper(1,102,F("Heat for 90 sec"));
 //BA.debugLineNum = 247;BA.debugLine="CallSubPlus(\"ReadSensor1\",90000,0)";
B4R::__c->CallSubPlus(_readsensor1,(ULong) (90000),(Byte) (0));
 //BA.debugLineNum = 248;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}

void b4r_main::initializeProcessGlobals() {
     B4R::StackMemory::buffer = (byte*)malloc(STACK_BUFFER_SIZE);
     b4r_main::_process_globals();

   
}
void b4r_main::_process_globals(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 43;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 46;BA.debugLine="Public Serial1 As Serial";
b4r_main::_serial1 = &be_gann1_3;
 //BA.debugLineNum = 47;BA.debugLine="Private d1pins As D1Pins";
b4r_main::_d1pins = &be_gann2_3;
 //BA.debugLineNum = 48;BA.debugLine="Private pin16 As Pin";
b4r_main::_pin16 = &be_gann3_3;
 //BA.debugLineNum = 49;BA.debugLine="Private d6 As Pin";
b4r_main::_d6 = &be_gann4_3;
 //BA.debugLineNum = 50;BA.debugLine="Private WiFi As ESP8266WiFi";
b4r_main::_wifi = &be_gann5_3;
 //BA.debugLineNum = 51;BA.debugLine="Private WiFiStr As WiFiSocket";
b4r_main::_wifistr = &be_gann6_3;
 //BA.debugLineNum = 52;BA.debugLine="Private MQTT As MqttClient";
b4r_main::_mqtt = &be_gann7_3;
 //BA.debugLineNum = 53;BA.debugLine="Private MQTTOpt As MqttConnectOptions";
b4r_main::_mqttopt = &be_gann8_3;
 //BA.debugLineNum = 54;BA.debugLine="Private MQTTUser As String = \"vynckfaq\"";
b4r_main::_mqttuser = be_gann9_5.wrap("vynckfaq");
 //BA.debugLineNum = 55;BA.debugLine="Private MQTTPassword As String = \"KHSV1Q1qSUUY\"";
b4r_main::_mqttpassword = be_gann10_5.wrap("KHSV1Q1qSUUY");
 //BA.debugLineNum = 56;BA.debugLine="Private MQTTHostName As String = \"m14.cloudmqtt.c";
b4r_main::_mqtthostname = be_gann11_5.wrap("m14.cloudmqtt.com");
 //BA.debugLineNum = 57;BA.debugLine="Private MQTTPort As Int = 11816";
b4r_main::_mqttport = 11816;
 //BA.debugLineNum = 58;BA.debugLine="Private ESP As ESP8266";
b4r_main::_esp = &be_gann13_3;
 //BA.debugLineNum = 59;BA.debugLine="Private WiFiSSID As String = \"RiseAboveThisHome\"";
b4r_main::_wifissid = be_gann14_5.wrap("RiseAboveThisHome");
 //BA.debugLineNum = 60;BA.debugLine="Private WiFiPassword As String = \"SteelReserve\"";
b4r_main::_wifipassword = be_gann15_5.wrap("SteelReserve");
 //BA.debugLineNum = 61;BA.debugLine="Public DHT22pin As Pin                    ' ESP82";
b4r_main::_dht22pin = &be_gann16_3;
 //BA.debugLineNum = 62;BA.debugLine="Private d3pins As D1Pins";
b4r_main::_d3pins = &be_gann17_3;
 //BA.debugLineNum = 63;BA.debugLine="Public DHT22Temp As Double";
b4r_main::_dht22temp = 0;
 //BA.debugLineNum = 64;BA.debugLine="Public DHT22Hum As Double";
b4r_main::_dht22hum = 0;
 //BA.debugLineNum = 65;BA.debugLine="Public DHT22State As Int";
b4r_main::_dht22state = 0;
 //BA.debugLineNum = 66;BA.debugLine="Public DHT22HeatIndex As Double";
b4r_main::_dht22heatindex = 0;
 //BA.debugLineNum = 67;BA.debugLine="Public DHT22DewPoint As Double";
b4r_main::_dht22dewpoint = 0;
 //BA.debugLineNum = 68;BA.debugLine="Public DHT22Perception As Int";
b4r_main::_dht22perception = 0;
 //BA.debugLineNum = 69;BA.debugLine="Public DHT22ComfortStatus As Int";
b4r_main::_dht22comfortstatus = 0;
 //BA.debugLineNum = 70;BA.debugLine="Private MQ7Pin As Pin                 ' Output pi";
b4r_main::_mq7pin = &be_gann25_3;
 //BA.debugLineNum = 71;BA.debugLine="Private MQ7PinNumber As Byte = 0x00   ' Pin numbe";
b4r_main::_mq7pinnumber = (Byte) (0x00);
 //BA.debugLineNum = 72;BA.debugLine="Private readVoltage As UInt";
b4r_main::_readvoltage = 0;
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
}
void b4r_main::_readsensor1(Byte _tag){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 250;BA.debugLine="Sub ReadSensor1(tag As Byte)";
 //BA.debugLineNum = 251;BA.debugLine="Log(\"We need to read the sensor at 5V, but must n";
B4R::Common::LogHelper(1,102,F("We need to read the sensor at 5V, but must not let it heat up. So hurry!"));
 //BA.debugLineNum = 252;BA.debugLine="MQ7Pin.AnalogWrite(1024)";
b4r_main::_mq7pin->AnalogWrite((UInt) (1024));
 //BA.debugLineNum = 253;BA.debugLine="Log(\"Delay for 50 milli\")";
B4R::Common::LogHelper(1,102,F("Delay for 50 milli"));
 //BA.debugLineNum = 254;BA.debugLine="CallSubPlus(\"ReadSensor2\",50,0) ' Getting an anal";
B4R::__c->CallSubPlus(_readsensor2,(ULong) (50),(Byte) (0));
 //BA.debugLineNum = 255;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_readsensor2(Byte _tag){
const UInt cp = B4R::StackMemory::cp;
UInt _rawvoltage = 0;
B4R::B4RString* _s = B4R::B4RString::EMPTY;
B4R::B4RString* be_ann182_11e1[1];
B4R::Array be_ann182_11e2;
B4R::B4RString be_ann185_4;
 //BA.debugLineNum = 257;BA.debugLine="Sub ReadSensor2(tag As Byte)";
 //BA.debugLineNum = 258;BA.debugLine="Dim rawvoltage As UInt = MQ7Pin.AnalogRead / 2";
_rawvoltage = (UInt) (b4r_main::_mq7pin->AnalogRead()/(Double)2);
 //BA.debugLineNum = 260;BA.debugLine="Log(\"*************************\")";
B4R::Common::LogHelper(1,102,F("*************************"));
 //BA.debugLineNum = 261;BA.debugLine="Log(\"MQ-7 PPM: \",rawvoltage)";
B4R::Common::LogHelper(2,102,F("MQ-7 PPM: "),4,_rawvoltage);
 //BA.debugLineNum = 262;BA.debugLine="readVoltage = rawvoltage";
b4r_main::_readvoltage = _rawvoltage;
 //BA.debugLineNum = 264;BA.debugLine="Dim s As String";
_s = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 265;BA.debugLine="s = JoinStrings(Array As String(rawvoltage))";
_s = B4R::__c->JoinStrings(be_ann182_11e2.create(be_ann182_11e1,1,100,B4R::B4RString::fromNumber((Long)(_rawvoltage))));
 //BA.debugLineNum = 266;BA.debugLine="Log(\"Sending ppm via MQTT: \",s)";
B4R::Common::LogHelper(2,102,F("Sending ppm via MQTT: "),101,_s->data);
 //BA.debugLineNum = 267;BA.debugLine="If WiFi.IsConnected Then";
if (b4r_main::_wifi->getIsConnected()) { 
 //BA.debugLineNum = 268;BA.debugLine="MQTT.Publish(\"MQ7\",s)";
b4r_main::_mqtt->Publish(be_ann185_4.wrap("MQ7"),(_s)->GetBytes());
 };
 //BA.debugLineNum = 271;BA.debugLine="If rawvoltage <= 100 Then";
if (_rawvoltage<=100) { 
 //BA.debugLineNum = 272;BA.debugLine="Log(\"Air-Quality: CO perfect\")";
B4R::Common::LogHelper(1,102,F("Air-Quality: CO perfect"));
 }else if(((_rawvoltage>100) && (_rawvoltage<400)) || _rawvoltage==400) { 
 //BA.debugLineNum = 274;BA.debugLine="Log(\"Air-Quality: CO normal\")";
B4R::Common::LogHelper(1,102,F("Air-Quality: CO normal"));
 }else if(((_rawvoltage>400) && (_rawvoltage<900)) || _rawvoltage==900) { 
 //BA.debugLineNum = 276;BA.debugLine="Log(\"Air-Quality: CO high\")";
B4R::Common::LogHelper(1,102,F("Air-Quality: CO high"));
 }else if(_rawvoltage>900) { 
 //BA.debugLineNum = 278;BA.debugLine="Log(\"Air-Quality: ALARM CO very high\")";
B4R::Common::LogHelper(1,102,F("Air-Quality: ALARM CO very high"));
 }else {
 //BA.debugLineNum = 280;BA.debugLine="Log(\"MQ-7 - cant read any value - check the sens";
B4R::Common::LogHelper(1,102,F("MQ-7 - cant read any value - check the sensor!"));
 };
 //BA.debugLineNum = 282;BA.debugLine="Log(\"*************************\")";
B4R::Common::LogHelper(1,102,F("*************************"));
 //BA.debugLineNum = 284;BA.debugLine="CallSubPlus(\"Preparation1\",1000,0)";
B4R::__c->CallSubPlus(_preparation1,(ULong) (1000),(Byte) (0));
 //BA.debugLineNum = 285;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_readweather(Byte _tag){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString* _localstate = B4R::B4RString::EMPTY;
B4R::B4RString be_ann99_2;
B4R::B4RString be_ann101_2;
B4R::B4RString be_ann103_2;
B4R::B4RString* _localperception = B4R::B4RString::EMPTY;
B4R::B4RString be_ann108_2;
B4R::B4RString be_ann110_2;
B4R::B4RString be_ann112_2;
B4R::B4RString be_ann114_2;
B4R::B4RString be_ann116_2;
B4R::B4RString be_ann118_2;
B4R::B4RString be_ann120_2;
B4R::B4RString be_ann122_2;
B4R::B4RString* _localcomfortstatus = B4R::B4RString::EMPTY;
B4R::B4RString be_ann127_2;
B4R::B4RString be_ann129_2;
B4R::B4RString be_ann131_2;
B4R::B4RString be_ann133_2;
B4R::B4RString be_ann135_2;
B4R::B4RString be_ann137_2;
B4R::B4RString be_ann139_2;
B4R::B4RString be_ann141_2;
B4R::B4RString be_ann143_2;
B4R::B4RString be_ann145_2;
B4R::B4RString be_ann149_3;
B4R::B4RString* _s = B4R::B4RString::EMPTY;
B4R::B4RString be_ann151_12;
B4R::B4RString be_ann151_16;
B4R::B4RString be_ann151_20;
B4R::B4RString be_ann151_24;
B4R::B4RString be_ann151_28;
B4R::B4RString be_ann151_32;
B4R::B4RString* be_ann151_35e1[13];
B4R::Array be_ann151_35e2;
B4R::B4RString be_ann153_4;
 //BA.debugLineNum = 163;BA.debugLine="Sub ReadWeather(tag As Byte)";
 //BA.debugLineNum = 164;BA.debugLine="RunNative(\"ReadDHT22\",Null)";
Common_RunNative(ReadDHT22,Common_Null);
 //BA.debugLineNum = 166;BA.debugLine="Dim localstate As String";
_localstate = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 167;BA.debugLine="Select Case DHT22State";
switch (b4r_main::_dht22state) {
case 0: {
 //BA.debugLineNum = 170;BA.debugLine="localstate = \"OK\"";
_localstate = be_ann99_2.wrap("OK");
 break; }
case 1: {
 //BA.debugLineNum = 173;BA.debugLine="localstate = \"TIMEOUT\"";
_localstate = be_ann101_2.wrap("TIMEOUT");
 break; }
case 2: {
 //BA.debugLineNum = 176;BA.debugLine="localstate = \"CHECKSUM\"";
_localstate = be_ann103_2.wrap("CHECKSUM");
 break; }
}
;
 //BA.debugLineNum = 179;BA.debugLine="Dim localperception As String";
_localperception = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 180;BA.debugLine="Select Case DHT22Perception";
switch (b4r_main::_dht22perception) {
case 0: {
 //BA.debugLineNum = 182;BA.debugLine="localperception = \"Feels like the western US, a";
_localperception = be_ann108_2.wrap("Feels like the western US, a bit dry to some");
 break; }
case 1: {
 //BA.debugLineNum = 184;BA.debugLine="localperception = \"Very comfortable\"";
_localperception = be_ann110_2.wrap("Very comfortable");
 break; }
case 2: {
 //BA.debugLineNum = 186;BA.debugLine="localperception = \"Comfortable\"";
_localperception = be_ann112_2.wrap("Comfortable");
 break; }
case 3: {
 //BA.debugLineNum = 188;BA.debugLine="localperception = \"OK but everyone perceives th";
_localperception = be_ann114_2.wrap("OK but everyone perceives the humidity at upper limit");
 break; }
case 4: {
 //BA.debugLineNum = 190;BA.debugLine="localperception = \"Somewhat uncomfortable for m";
_localperception = be_ann116_2.wrap("Somewhat uncomfortable for most people at upper limit");
 break; }
case 5: {
 //BA.debugLineNum = 192;BA.debugLine="localperception = \"Very humid, quite uncomforta";
_localperception = be_ann118_2.wrap("Very humid, quite uncomfortable");
 break; }
case 6: {
 //BA.debugLineNum = 194;BA.debugLine="localperception = \"Extremely uncomfortable, opp";
_localperception = be_ann120_2.wrap("Extremely uncomfortable, oppressive");
 break; }
case 7: {
 //BA.debugLineNum = 196;BA.debugLine="localperception = \"Severely high, even deadly f";
_localperception = be_ann122_2.wrap("Severely high, even deadly for asthma related illnesses");
 break; }
}
;
 //BA.debugLineNum = 199;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 200;BA.debugLine="Select Case DHT22ComfortStatus";
switch (b4r_main::_dht22comfortstatus) {
case 0: {
 //BA.debugLineNum = 202;BA.debugLine="localcomfortstatus = \"OK\"";
_localcomfortstatus = be_ann127_2.wrap("OK");
 break; }
case 1: {
 //BA.debugLineNum = 204;BA.debugLine="localcomfortstatus = \"Too hot\"";
_localcomfortstatus = be_ann129_2.wrap("Too hot");
 break; }
case 2: {
 //BA.debugLineNum = 206;BA.debugLine="localcomfortstatus = \"Too cold\"";
_localcomfortstatus = be_ann131_2.wrap("Too cold");
 break; }
case 4: {
 //BA.debugLineNum = 208;BA.debugLine="localcomfortstatus = \"Too dry\"";
_localcomfortstatus = be_ann133_2.wrap("Too dry");
 break; }
case 5: {
 //BA.debugLineNum = 210;BA.debugLine="localcomfortstatus = \"Hot and dry\"";
_localcomfortstatus = be_ann135_2.wrap("Hot and dry");
 break; }
case 6: {
 //BA.debugLineNum = 212;BA.debugLine="localcomfortstatus = \"Cold and dry\"";
_localcomfortstatus = be_ann137_2.wrap("Cold and dry");
 break; }
case 8: {
 //BA.debugLineNum = 214;BA.debugLine="localcomfortstatus = \"Too humid\"";
_localcomfortstatus = be_ann139_2.wrap("Too humid");
 break; }
case 9: {
 //BA.debugLineNum = 216;BA.debugLine="localcomfortstatus = \"Hot and humid\"";
_localcomfortstatus = be_ann141_2.wrap("Hot and humid");
 break; }
case 10: {
 //BA.debugLineNum = 218;BA.debugLine="localcomfortstatus = \"Cold and humid\"";
_localcomfortstatus = be_ann143_2.wrap("Cold and humid");
 break; }
default: {
 //BA.debugLineNum = 220;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = be_ann145_2.wrap("Unknown");
 break; }
}
;
 //BA.debugLineNum = 223;BA.debugLine="Log(\"State: \", localstate, \" Temp: \", DHT22Temp,";
B4R::Common::LogHelper(15,102,F("State: "),101,_localstate->data,102,F(" Temp: "),7,(double)(b4r_main::_dht22temp),102,F(" ºF Humidity: "),7,(double)(b4r_main::_dht22hum),102,F(" % Perception: "),101,_localperception->data,102,F(" Comfort Status: "),101,_localcomfortstatus->data,102,F(" HeatIndex: "),7,(double)(b4r_main::_dht22heatindex),102,F(" ºF Dew Point: "),7,(double)(b4r_main::_dht22dewpoint),102,F(" ºF"));
 //BA.debugLineNum = 224;BA.debugLine="If WiFi.IsConnected Then";
if (b4r_main::_wifi->getIsConnected()) { 
 //BA.debugLineNum = 225;BA.debugLine="If localstate = \"OK\" And IsNumber(DHT22Temp) The";
if ((_localstate)->equals(be_ann149_3.wrap("OK")) && B4R::__c->IsNumber(B4R::B4RString::fromNumber((Double)(b4r_main::_dht22temp)))) { 
 //BA.debugLineNum = 226;BA.debugLine="Dim s As String";
_s = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 227;BA.debugLine="s = JoinStrings(Array As String(localstate,\"|\",";
_s = B4R::__c->JoinStrings(be_ann151_35e2.create(be_ann151_35e1,13,100,_localstate,be_ann151_12.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22temp)),be_ann151_16.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22hum)),be_ann151_20.wrap("|"),B4R::B4RString::fromNumber((Long)(b4r_main::_dht22perception)),be_ann151_24.wrap("|"),B4R::B4RString::fromNumber((Long)(b4r_main::_dht22comfortstatus)),be_ann151_28.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22heatindex)),be_ann151_32.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22dewpoint))));
 //BA.debugLineNum = 228;BA.debugLine="Log(\"Sending TempHumid to MQTT: \",s)";
B4R::Common::LogHelper(2,102,F("Sending TempHumid to MQTT: "),101,_s->data);
 //BA.debugLineNum = 229;BA.debugLine="MQTT.Publish(\"TempHumid\",s)";
b4r_main::_mqtt->Publish(be_ann153_4.wrap("TempHumid"),(_s)->GetBytes());
 };
 };
 //BA.debugLineNum = 233;BA.debugLine="CallSubPlus(\"ReadWeather\",6000,0)";
B4R::__c->CallSubPlus(_readweather,(ULong) (6000),(Byte) (0));
 //BA.debugLineNum = 234;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
