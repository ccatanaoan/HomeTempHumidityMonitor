#include "B4RDefines.h"

B4R::Serial* b4r_main::_serial1;
B4R::D1Pins* b4r_main::_d1pins;
B4R::Pin* b4r_main::_pin16;
B4R::Pin* b4r_main::_d6;
B4R::B4RESPWiFi* b4r_main::_wifi;
B4R::WiFiSocket* b4r_main::_wifistr;
B4R::MqttClient* b4r_main::_mqtt;
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
B4R::ByteConverter* b4r_main::_bc;
Double b4r_main::_dht22humidityaddvalue;
B4R::B4RESP8266TimeTools* b4r_main::_timelib;
ULong b4r_main::_timestamp;
static B4R::Serial be_gann1_3;
static B4R::D1Pins be_gann2_3;
static B4R::Pin be_gann3_3;
static B4R::Pin be_gann4_3;
static B4R::B4RESPWiFi be_gann5_3;
static B4R::WiFiSocket be_gann6_3;
static B4R::MqttClient be_gann7_3;
static B4R::B4RString be_gann8_5;
static B4R::B4RESP8266 be_gann10_3;
static B4R::B4RString be_gann11_5;
static B4R::B4RString be_gann12_5;
static B4R::Pin be_gann13_3;
static B4R::D1Pins be_gann14_3;
static B4R::ByteConverter be_gann22_3;
static B4R::B4RESP8266TimeTools be_gann24_3;


 #include "DHTesp.h"

#define DHT22Pin 0 //change here the IO-Pin

ComfortState cf;
DHTesp dht22;

void setup(B4R::Object* o){
	dht22.setup(DHT22Pin, DHTesp::DHT22);
}

void SetHostName(B4R::Object* o) {
	B4R::Array* b = (B4R::Array*)B4R::Object::toPointer(o);
	char* c = (char*)b->data;
	WiFi.hostname(c);
}

void stopAP (B4R::Object* u) {
	WiFi.softAPdisconnect(true);
}

void ReadDHT22(B4R::Object* o) {
	 b4r_main::_dht22state = dht22.getStatus();
	 // Add 8 percent to relative humidity based on home hygrometer.
 	 float humidity = dht22.getHumidity() + b4r_main::_dht22humidityaddvalue;
 	 float temperature = dht22.toFahrenheit(dht22.getTemperature());
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
B4R::B4RString be_ann31_8;
B4R::Array* _bb = NULL;
B4R::Object be_ann32_6;
B4R::B4RString* _clientid = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 82;BA.debugLine="Private Sub AppStart";
 //BA.debugLineNum = 83;BA.debugLine="Serial1.Initialize(115200)";
b4r_main::_serial1->Initialize((ULong) (115200));
 //BA.debugLineNum = 84;BA.debugLine="Delay(3000)";
Common_Delay((ULong) (3000));
 //BA.debugLineNum = 85;BA.debugLine="Log(\"AppStart\")";
B4R::Common::LogHelper(1,102,F("AppStart"));
 //BA.debugLineNum = 87;BA.debugLine="Dim bb() As Byte = \"LivingRoomTempHumid\"";
_bb = (be_ann31_8.wrap("LivingRoomTempHumid"))->GetBytes();
 //BA.debugLineNum = 88;BA.debugLine="RunNative(\"SetHostName\", bb)";
Common_RunNative(SetHostName,be_ann32_6.wrapPointer(_bb));
 //BA.debugLineNum = 89;BA.debugLine="RunNative(\"stopAP\", Null)";
Common_RunNative(stopAP,Common_Null);
 //BA.debugLineNum = 94;BA.debugLine="pin16.Initialize(16, pin16.MODE_OUTPUT)";
b4r_main::_pin16->Initialize((Byte) (16),Pin_MODE_OUTPUT);
 //BA.debugLineNum = 95;BA.debugLine="d6.Initialize(d1pins.D6, d6.MODE_OUTPUT)";
b4r_main::_d6->Initialize(b4r_main::_d1pins->D6,Pin_MODE_OUTPUT);
 //BA.debugLineNum = 98;BA.debugLine="ConnectToWifi";
_connecttowifi();
 //BA.debugLineNum = 101;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'creat";
_clientid = B4R::B4RString::fromNumber((Long)(Common_Rnd((Long) (0),(Long) (999999999))));
 //BA.debugLineNum = 102;BA.debugLine="MQTT.Initialize2(WiFiStr.stream, MQTTHostName, MQ";
b4r_main::_mqtt->Initialize2(b4r_main::_wifistr->getStream(),b4r_main::_mqtthostname,(UInt) (b4r_main::_mqttport),_clientid,_mqtt_messagearrived,_mqtt_disconnected);
 //BA.debugLineNum = 103;BA.debugLine="MQTT_Connect(0)";
_mqtt_connect((Byte) (0));
 //BA.debugLineNum = 106;BA.debugLine="DHT22pin.Initialize(d3pins.D3, DHT22pin.MODE_INPU";
b4r_main::_dht22pin->Initialize(b4r_main::_d3pins->D3,Pin_MODE_INPUT);
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_connecttowifi(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 153;BA.debugLine="Sub ConnectToWifi";
 //BA.debugLineNum = 154;BA.debugLine="If WiFi.IsConnected Then";
if (b4r_main::_wifi->getIsConnected()) { 
 //BA.debugLineNum = 155;BA.debugLine="Log(\"Connected to \",WiFiSSID,\" network, Local IP";
B4R::Common::LogHelper(4,102,F("Connected to "),101,b4r_main::_wifissid->data,102,F(" network, Local IP "),101,b4r_main::_wifi->getLocalIp()->data);
 }else {
 //BA.debugLineNum = 157;BA.debugLine="pin16.DigitalWrite(True)";
b4r_main::_pin16->DigitalWrite(Common_True);
 //BA.debugLineNum = 158;BA.debugLine="Log(\"Connecting to WiFi\")";
B4R::Common::LogHelper(1,102,F("Connecting to WiFi"));
 //BA.debugLineNum = 159;BA.debugLine="If WiFi.Connect2(WiFiSSID, WiFiPassword) = False";
if (b4r_main::_wifi->Connect2(b4r_main::_wifissid,b4r_main::_wifipassword)==Common_False) { 
 //BA.debugLineNum = 160;BA.debugLine="ConnectToWifi";
_connecttowifi();
 };
 };
 //BA.debugLineNum = 163;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_connect(Byte _unused){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString be_ann54_4;
B4R::B4RString be_ann55_4;
 //BA.debugLineNum = 109;BA.debugLine="Sub MQTT_Connect(Unused As Byte)";
 //BA.debugLineNum = 110;BA.debugLine="If WiFi.IsConnected = False Then";
if (b4r_main::_wifi->getIsConnected()==Common_False) { 
 //BA.debugLineNum = 111;BA.debugLine="ConnectToWifi";
_connecttowifi();
 };
 //BA.debugLineNum = 114;BA.debugLine="If MQTT.Connect = False Then";
if (b4r_main::_mqtt->Connect()==Common_False) { 
 //BA.debugLineNum = 115;BA.debugLine="pin16.DigitalWrite(True)";
b4r_main::_pin16->DigitalWrite(Common_True);
 //BA.debugLineNum = 116;BA.debugLine="Log(\"Not connected to broker\")";
B4R::Common::LogHelper(1,102,F("Not connected to broker"));
 //BA.debugLineNum = 117;BA.debugLine="MQTT.Connect";
b4r_main::_mqtt->Connect();
 //BA.debugLineNum = 118;BA.debugLine="CallSubPlus(\"MQTT_Connect\", 1000, 0)";
B4R::__c->CallSubPlus(_mqtt_connect,(ULong) (1000),(Byte) (0));
 }else {
 //BA.debugLineNum = 120;BA.debugLine="pin16.DigitalWrite(False)";
b4r_main::_pin16->DigitalWrite(Common_False);
 //BA.debugLineNum = 121;BA.debugLine="Log(\"Connected to broker\")";
B4R::Common::LogHelper(1,102,F("Connected to broker"));
 //BA.debugLineNum = 122;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
b4r_main::_mqtt->Subscribe(be_ann54_4.wrap("TempHumid"),(Byte) (0));
 //BA.debugLineNum = 123;BA.debugLine="MQTT.Subscribe(\"HumidityAddValue\", 0)";
b4r_main::_mqtt->Subscribe(be_ann55_4.wrap("HumidityAddValue"),(Byte) (0));
 //BA.debugLineNum = 124;BA.debugLine="ReadSensor";
_readsensor();
 };
 //BA.debugLineNum = 126;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_disconnected(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 146;BA.debugLine="Sub mqtt_Disconnected";
 //BA.debugLineNum = 147;BA.debugLine="pin16.DigitalWrite(True)";
b4r_main::_pin16->DigitalWrite(Common_True);
 //BA.debugLineNum = 148;BA.debugLine="Log(\"Disconnected from broker\")";
B4R::Common::LogHelper(1,102,F("Disconnected from broker"));
 //BA.debugLineNum = 149;BA.debugLine="MQTT.Close";
b4r_main::_mqtt->Close();
 //BA.debugLineNum = 150;BA.debugLine="MQTT_Connect(0)";
_mqtt_connect((Byte) (0));
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_messagearrived(B4R::B4RString* _topic,B4R::Array* _payload){
const UInt cp = B4R::StackMemory::cp;
B4R::Object be_ann60_8;
B4R::B4RString be_ann61_3;
B4R::B4RString be_ann62_3;
B4R::B4RString be_ann63_4;
B4R::B4RString be_ann63_6;
B4R::B4RString be_ann65_3;
B4R::B4RString be_ann68_3;
 //BA.debugLineNum = 128;BA.debugLine="Sub mqtt_MessageArrived (Topic As String, Payload(";
 //BA.debugLineNum = 129;BA.debugLine="Log(\"Message arrived. Topic=\", Topic, \" Payload=\"";
B4R::Common::LogHelper(4,102,F("Message arrived. Topic="),101,_topic->data,102,F(" Payload="),100,be_ann60_8.wrapPointer(_payload));
 //BA.debugLineNum = 131;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic)->equals(be_ann61_3.wrap("TempHumid"))) { 
 //BA.debugLineNum = 132;BA.debugLine="If Payload = \"Restart controller\" Then";
if ((_payload)->equals((be_ann62_3.wrap("Restart controller"))->GetBytes())) { 
 //BA.debugLineNum = 133;BA.debugLine="MQTT.Publish(\"TempHumid\",\"*Restarting relay by";
b4r_main::_mqtt->Publish(be_ann63_4.wrap("TempHumid"),(be_ann63_6.wrap("*Restarting relay by remote"))->GetBytes());
 //BA.debugLineNum = 134;BA.debugLine="ESP.Restart";
b4r_main::_esp->Restart();
 }else if((_payload)->equals((be_ann65_3.wrap("Read weather"))->GetBytes())) { 
 //BA.debugLineNum = 136;BA.debugLine="ReadWeather(1)";
_readweather((Byte) (1));
 };
 }else if((_topic)->equals(be_ann68_3.wrap("HumidityAddValue"))) { 
 //BA.debugLineNum = 139;BA.debugLine="DHT22HumidityAddValue = bc.StringFromBytes(Paylo";
b4r_main::_dht22humidityaddvalue = (Double)(atof(b4r_main::_bc->StringFromBytes(_payload)->data));
 //BA.debugLineNum = 140;BA.debugLine="If IsNumber(DHT22HumidityAddValue) = False Then";
if (B4R::__c->IsNumber(B4R::B4RString::fromNumber((Double)(b4r_main::_dht22humidityaddvalue)))==Common_False) { 
 //BA.debugLineNum = 141;BA.debugLine="DHT22HumidityAddValue = 0";
b4r_main::_dht22humidityaddvalue = 0;
 };
 };
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}

void b4r_main::initializeProcessGlobals() {
     B4R::StackMemory::buffer = (byte*)malloc(STACK_BUFFER_SIZE);
     b4r_main::_process_globals();

   
}
void b4r_main::_process_globals(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 51;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 54;BA.debugLine="Public Serial1 As Serial";
b4r_main::_serial1 = &be_gann1_3;
 //BA.debugLineNum = 55;BA.debugLine="Private d1pins As D1Pins";
b4r_main::_d1pins = &be_gann2_3;
 //BA.debugLineNum = 56;BA.debugLine="Private pin16 As Pin";
b4r_main::_pin16 = &be_gann3_3;
 //BA.debugLineNum = 57;BA.debugLine="Private d6 As Pin";
b4r_main::_d6 = &be_gann4_3;
 //BA.debugLineNum = 58;BA.debugLine="Private WiFi As ESP8266WiFi";
b4r_main::_wifi = &be_gann5_3;
 //BA.debugLineNum = 59;BA.debugLine="Private WiFiStr As WiFiSocket";
b4r_main::_wifistr = &be_gann6_3;
 //BA.debugLineNum = 60;BA.debugLine="Private MQTT As MqttClient";
b4r_main::_mqtt = &be_gann7_3;
 //BA.debugLineNum = 62;BA.debugLine="Private MQTTHostName As String = \"test.mosquitto.";
b4r_main::_mqtthostname = be_gann8_5.wrap("test.mosquitto.org");
 //BA.debugLineNum = 63;BA.debugLine="Private MQTTPort As Int = 1883";
b4r_main::_mqttport = 1883;
 //BA.debugLineNum = 64;BA.debugLine="Private ESP As ESP8266";
b4r_main::_esp = &be_gann10_3;
 //BA.debugLineNum = 65;BA.debugLine="Private WiFiSSID As String = \"InternetOfThings\"";
b4r_main::_wifissid = be_gann11_5.wrap("InternetOfThings");
 //BA.debugLineNum = 66;BA.debugLine="Private WiFiPassword As String = \"SteelReserve\"";
b4r_main::_wifipassword = be_gann12_5.wrap("SteelReserve");
 //BA.debugLineNum = 67;BA.debugLine="Public DHT22pin As Pin                    ' ESP82";
b4r_main::_dht22pin = &be_gann13_3;
 //BA.debugLineNum = 68;BA.debugLine="Private d3pins As D1Pins";
b4r_main::_d3pins = &be_gann14_3;
 //BA.debugLineNum = 69;BA.debugLine="Public DHT22Temp As Double";
b4r_main::_dht22temp = 0;
 //BA.debugLineNum = 70;BA.debugLine="Public DHT22Hum As Double";
b4r_main::_dht22hum = 0;
 //BA.debugLineNum = 71;BA.debugLine="Public DHT22State As Int";
b4r_main::_dht22state = 0;
 //BA.debugLineNum = 72;BA.debugLine="Public DHT22HeatIndex As Double";
b4r_main::_dht22heatindex = 0;
 //BA.debugLineNum = 73;BA.debugLine="Public DHT22DewPoint As Double";
b4r_main::_dht22dewpoint = 0;
 //BA.debugLineNum = 74;BA.debugLine="Public DHT22Perception As Int";
b4r_main::_dht22perception = 0;
 //BA.debugLineNum = 75;BA.debugLine="Public DHT22ComfortStatus As Int";
b4r_main::_dht22comfortstatus = 0;
 //BA.debugLineNum = 76;BA.debugLine="Private bc As ByteConverter";
b4r_main::_bc = &be_gann22_3;
 //BA.debugLineNum = 77;BA.debugLine="Public DHT22HumidityAddValue As Double";
b4r_main::_dht22humidityaddvalue = 0;
 //BA.debugLineNum = 78;BA.debugLine="Public timelib As ESPTimetools";
b4r_main::_timelib = &be_gann24_3;
 //BA.debugLineNum = 79;BA.debugLine="Public timestamp As ULong";
b4r_main::_timestamp = 0L;
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
}
void b4r_main::_readsensor(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 204;BA.debugLine="Public Sub ReadSensor";
 //BA.debugLineNum = 206;BA.debugLine="RunNative(\"setup\",Null)";
Common_RunNative(setup,Common_Null);
 //BA.debugLineNum = 207;BA.debugLine="ReadWeather(0)";
_readweather((Byte) (0));
 //BA.debugLineNum = 208;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_readweather(Byte _tag){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString* _localstate = B4R::B4RString::EMPTY;
B4R::B4RString be_ann97_2;
B4R::B4RString be_ann99_2;
B4R::B4RString be_ann101_2;
B4R::B4RString be_ann104_3;
B4R::B4RString be_ann105_4;
B4R::B4RString* _s = B4R::B4RString::EMPTY;
B4R::B4RString be_ann109_12;
B4R::B4RString be_ann109_16;
B4R::B4RString be_ann109_20;
B4R::B4RString be_ann109_24;
B4R::B4RString be_ann109_28;
B4R::B4RString be_ann109_32;
B4R::B4RString be_ann109_36;
B4R::B4RString be_ann109_54;
B4R::B4RString be_ann109_72;
B4R::B4RString be_ann109_90;
B4R::B4RString be_ann109_108;
B4R::B4RString be_ann109_126;
B4R::B4RString* be_ann109_143e1[25];
B4R::Array be_ann109_143e2;
B4R::B4RString be_ann111_4;
 //BA.debugLineNum = 165;BA.debugLine="Sub ReadWeather(tag As Byte)";
 //BA.debugLineNum = 166;BA.debugLine="RunNative(\"ReadDHT22\",Null)";
Common_RunNative(ReadDHT22,Common_Null);
 //BA.debugLineNum = 168;BA.debugLine="Dim localstate As String";
_localstate = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 169;BA.debugLine="Select Case DHT22State";
switch (b4r_main::_dht22state) {
case 0: {
 //BA.debugLineNum = 172;BA.debugLine="localstate = \"OK\"";
_localstate = be_ann97_2.wrap("OK");
 break; }
case 1: {
 //BA.debugLineNum = 175;BA.debugLine="localstate = \"TIMEOUT\"";
_localstate = be_ann99_2.wrap("TIMEOUT");
 break; }
case 2: {
 //BA.debugLineNum = 178;BA.debugLine="localstate = \"CHECKSUM\"";
_localstate = be_ann101_2.wrap("CHECKSUM");
 break; }
}
;
 //BA.debugLineNum = 181;BA.debugLine="If WiFi.IsConnected Then";
if (b4r_main::_wifi->getIsConnected()) { 
 //BA.debugLineNum = 182;BA.debugLine="If localstate = \"OK\" And IsNumber(DHT22Temp) The";
if ((_localstate)->equals(be_ann104_3.wrap("OK")) && B4R::__c->IsNumber(B4R::B4RString::fromNumber((Double)(b4r_main::_dht22temp)))) { 
 //BA.debugLineNum = 184;BA.debugLine="timelib.initialize(\"pool.ntp.org\",0)";
b4r_main::_timelib->initialize(be_ann105_4.wrap("pool.ntp.org"),0);
 //BA.debugLineNum = 185;BA.debugLine="timestamp = timelib.timestamp";
b4r_main::_timestamp = b4r_main::_timelib->timestamp();
 //BA.debugLineNum = 186;BA.debugLine="If timestamp <> 0 Then";
if (b4r_main::_timestamp!=0) { 
 //BA.debugLineNum = 187;BA.debugLine="Dim s As String";
_s = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 188;BA.debugLine="s = JoinStrings(Array As String(localstate,\"|\"";
_s = B4R::__c->JoinStrings(be_ann109_143e2.create(be_ann109_143e1,25,100,_localstate,be_ann109_12.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22temp)),be_ann109_16.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22hum)),be_ann109_20.wrap("|"),B4R::B4RString::fromNumber((Long)(b4r_main::_dht22perception)),be_ann109_24.wrap("|"),B4R::B4RString::fromNumber((Long)(b4r_main::_dht22comfortstatus)),be_ann109_28.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22heatindex)),be_ann109_32.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22dewpoint)),be_ann109_36.wrap("|"),B4R::__c->NumberFormat(b4r_main::_timelib->getyear(b4r_main::_timestamp),(Byte) (2),(Byte) (0)),be_ann109_54.wrap("-"),B4R::__c->NumberFormat(b4r_main::_timelib->getmonth(b4r_main::_timestamp),(Byte) (2),(Byte) (0)),be_ann109_72.wrap("-"),B4R::__c->NumberFormat(b4r_main::_timelib->getday(b4r_main::_timestamp),(Byte) (2),(Byte) (0)),be_ann109_90.wrap("|"),B4R::__c->NumberFormat(b4r_main::_timelib->gethour(b4r_main::_timestamp),(Byte) (2),(Byte) (0)),be_ann109_108.wrap(":"),B4R::__c->NumberFormat(b4r_main::_timelib->getminute(b4r_main::_timestamp),(Byte) (2),(Byte) (0)),be_ann109_126.wrap(":"),B4R::__c->NumberFormat(b4r_main::_timelib->getseconds(b4r_main::_timestamp),(Byte) (2),(Byte) (0))));
 //BA.debugLineNum = 192;BA.debugLine="Log(\"TempHumid to MQTT: \",s)";
B4R::Common::LogHelper(2,102,F("TempHumid to MQTT: "),101,_s->data);
 //BA.debugLineNum = 193;BA.debugLine="MQTT.Publish(\"TempHumid\",s)";
b4r_main::_mqtt->Publish(be_ann111_4.wrap("TempHumid"),(_s)->GetBytes());
 };
 };
 };
 //BA.debugLineNum = 199;BA.debugLine="If tag = 0 Then";
if (_tag==0) { 
 //BA.debugLineNum = 200;BA.debugLine="CallSubPlus(\"ReadWeather\",60000,0)";
B4R::__c->CallSubPlus(_readweather,(ULong) (60000),(Byte) (0));
 };
 //BA.debugLineNum = 202;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
