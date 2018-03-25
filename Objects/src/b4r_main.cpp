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
B4R::Timer* b4r_main::_timer1;
B4R::Pin* b4r_main::_dht22pin;
Int b4r_main::_interval;
B4R::D1Pins* b4r_main::_d3pins;
Double b4r_main::_dht22temp;
Double b4r_main::_dht22hum;
Int b4r_main::_dht22state;
Double b4r_main::_dht22heatindex;
Double b4r_main::_dht22dewpoint;
Int b4r_main::_dht22perception;
Int b4r_main::_dht22comfortstatus;
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
static B4R::Timer be_gann16_3;
static B4R::Pin be_gann17_3;
static B4R::D1Pins be_gann19_3;


 #include "DHTesp.h"

#define DHT22Pin 0 //change here the IO-Pin

ComfortState cf;
DHTesp dht22;

void setup(B4R::Object* o){
	dht22.setup(DHT22Pin);
}

void ReadDHT1(B4R::Object* o) {
	 b4r_main::_dht22state = dht22.getStatus();
	 // Add 12 percent based on home hygrometer.
 	 float humidity = dht22.getHumidity() + 13;
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
void SetSTA(B4R::Object* o) {
   WiFi.mode(WIFI_STA);
}
void b4r_main::_appstart(){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString* _clientid = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 55;BA.debugLine="Private Sub AppStart";
 //BA.debugLineNum = 56;BA.debugLine="Serial1.Initialize(115200)";
b4r_main::_serial1->Initialize((ULong) (115200));
 //BA.debugLineNum = 57;BA.debugLine="Delay(3000)";
Common_Delay((ULong) (3000));
 //BA.debugLineNum = 58;BA.debugLine="Log(\"AppStart\")";
B4R::Common::LogHelper(1,102,F("AppStart"));
 //BA.debugLineNum = 63;BA.debugLine="pin16.Initialize(16, pin16.MODE_OUTPUT)";
b4r_main::_pin16->Initialize((Byte) (16),Pin_MODE_OUTPUT);
 //BA.debugLineNum = 64;BA.debugLine="d6.Initialize(d1pins.D6, d6.MODE_OUTPUT)";
b4r_main::_d6->Initialize(b4r_main::_d1pins->D6,Pin_MODE_OUTPUT);
 //BA.debugLineNum = 66;BA.debugLine="Log(\"Stopping access point\")";
B4R::Common::LogHelper(1,102,F("Stopping access point"));
 //BA.debugLineNum = 67;BA.debugLine="RunNative(\"SetSTA\", Null)";
Common_RunNative(SetSTA,Common_Null);
 //BA.debugLineNum = 70;BA.debugLine="ConnectToWifi";
_connecttowifi();
 //BA.debugLineNum = 73;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'creat";
_clientid = B4R::B4RString::fromNumber((Long)(Common_Rnd((Long) (0),(Long) (999999999))));
 //BA.debugLineNum = 74;BA.debugLine="MQTT.Initialize2(WiFiStr.stream, MQTTHostName, MQ";
b4r_main::_mqtt->Initialize2(b4r_main::_wifistr->getStream(),b4r_main::_mqtthostname,(UInt) (b4r_main::_mqttport),_clientid,_mqtt_messagearrived,_mqtt_disconnected);
 //BA.debugLineNum = 75;BA.debugLine="MQTTOpt.Initialize(MQTTUser, MQTTPassword)";
b4r_main::_mqttopt->Initialize(b4r_main::_mqttuser,b4r_main::_mqttpassword);
 //BA.debugLineNum = 76;BA.debugLine="MQTT_Connect(0)";
_mqtt_connect((Byte) (0));
 //BA.debugLineNum = 79;BA.debugLine="Interval = 5";
b4r_main::_interval = 5;
 //BA.debugLineNum = 80;BA.debugLine="DHT22pin.Initialize(d3pins.D3, DHT22pin.MODE_INPU";
b4r_main::_dht22pin->Initialize(b4r_main::_d3pins->D3,Pin_MODE_INPUT);
 //BA.debugLineNum = 81;BA.debugLine="Timer1.Initialize(\"Timer1_Tick\", Interval * 1000)";
b4r_main::_timer1->Initialize(_timer1_tick,(ULong) (b4r_main::_interval*1000));
 //BA.debugLineNum = 82;BA.debugLine="RunNative(\"setup\",Null)";
Common_RunNative(setup,Common_Null);
 //BA.debugLineNum = 83;BA.debugLine="Timer1.Enabled=True";
b4r_main::_timer1->setEnabled(Common_True);
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_connecttowifi(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 117;BA.debugLine="Sub ConnectToWifi";
 //BA.debugLineNum = 118;BA.debugLine="WiFi.Connect2(WiFiSSID, WiFiPassword)";
b4r_main::_wifi->Connect2(b4r_main::_wifissid,b4r_main::_wifipassword);
 //BA.debugLineNum = 120;BA.debugLine="If WiFi.IsConnected Then";
if (b4r_main::_wifi->getIsConnected()) { 
 //BA.debugLineNum = 121;BA.debugLine="Log(\"Connected to WiFi, Local IP \", WiFi.LocalIp";
B4R::Common::LogHelper(2,102,F("Connected to WiFi, Local IP "),101,b4r_main::_wifi->getLocalIp()->data);
 }else {
 //BA.debugLineNum = 123;BA.debugLine="Log(\"Not Connected to WiFi\")";
B4R::Common::LogHelper(1,102,F("Not Connected to WiFi"));
 };
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_connect(Byte _unused){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString be_ann58_4;
 //BA.debugLineNum = 86;BA.debugLine="Sub MQTT_Connect(Unused As Byte)";
 //BA.debugLineNum = 87;BA.debugLine="If WiFi.IsConnected = False Then";
if (b4r_main::_wifi->getIsConnected()==Common_False) { 
 //BA.debugLineNum = 88;BA.debugLine="ConnectToWifi";
_connecttowifi();
 };
 //BA.debugLineNum = 90;BA.debugLine="If MQTT.Connect = False Then";
if (b4r_main::_mqtt->Connect()==Common_False) { 
 //BA.debugLineNum = 91;BA.debugLine="Log(\"Connecting to broker\")";
B4R::Common::LogHelper(1,102,F("Connecting to broker"));
 //BA.debugLineNum = 92;BA.debugLine="MQTT.Connect2(MQTTOpt)";
b4r_main::_mqtt->Connect2(b4r_main::_mqttopt);
 //BA.debugLineNum = 93;BA.debugLine="CallSubPlus(\"MQTT_Connect\", 1000, 0)";
B4R::__c->CallSubPlus(_mqtt_connect,(ULong) (1000),(Byte) (0));
 }else {
 //BA.debugLineNum = 95;BA.debugLine="pin16.DigitalWrite(False)";
b4r_main::_pin16->DigitalWrite(Common_False);
 //BA.debugLineNum = 96;BA.debugLine="Log(\"Connected to broker\")";
B4R::Common::LogHelper(1,102,F("Connected to broker"));
 //BA.debugLineNum = 97;BA.debugLine="MQTT.Subscribe(\"Cloyd\", 0)";
b4r_main::_mqtt->Subscribe(be_ann58_4.wrap("Cloyd"),(Byte) (0));
 };
 //BA.debugLineNum = 99;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_disconnected(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 110;BA.debugLine="Sub mqtt_Disconnected";
 //BA.debugLineNum = 111;BA.debugLine="pin16.DigitalWrite(True)";
b4r_main::_pin16->DigitalWrite(Common_True);
 //BA.debugLineNum = 112;BA.debugLine="Log(\"Disconnected from broker\")";
B4R::Common::LogHelper(1,102,F("Disconnected from broker"));
 //BA.debugLineNum = 113;BA.debugLine="MQTT.Close";
b4r_main::_mqtt->Close();
 //BA.debugLineNum = 114;BA.debugLine="MQTT_Connect(0)";
_mqtt_connect((Byte) (0));
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_messagearrived(B4R::B4RString* _topic,B4R::Array* _payload){
const UInt cp = B4R::StackMemory::cp;
B4R::Object be_ann63_8;
B4R::B4RString be_ann64_3;
B4R::B4RString be_ann65_4;
B4R::B4RString be_ann65_6;
 //BA.debugLineNum = 101;BA.debugLine="Sub mqtt_MessageArrived (Topic As String, Payload(";
 //BA.debugLineNum = 102;BA.debugLine="pin16.DigitalWrite(False)";
b4r_main::_pin16->DigitalWrite(Common_False);
 //BA.debugLineNum = 103;BA.debugLine="Log(\"Message arrived. Topic=\", Topic, \" Payload=\"";
B4R::Common::LogHelper(4,102,F("Message arrived. Topic="),101,_topic->data,102,F(" Payload="),100,be_ann63_8.wrapPointer(_payload));
 //BA.debugLineNum = 104;BA.debugLine="If Payload = \"Restart controller\" Then";
if ((_payload)->equals((be_ann64_3.wrap("Restart controller"))->GetBytes())) { 
 //BA.debugLineNum = 105;BA.debugLine="MQTT.Publish(\"Cloyd\",\"*Restarting relay by remot";
b4r_main::_mqtt->Publish(be_ann65_4.wrap("Cloyd"),(be_ann65_6.wrap("*Restarting relay by remote"))->GetBytes());
 //BA.debugLineNum = 106;BA.debugLine="ESP.Restart";
b4r_main::_esp->Restart();
 };
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}

void b4r_main::initializeProcessGlobals() {
     B4R::StackMemory::buffer = (byte*)malloc(STACK_BUFFER_SIZE);
     b4r_main::_process_globals();

   
}
void b4r_main::_process_globals(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 24;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 27;BA.debugLine="Public Serial1 As Serial";
b4r_main::_serial1 = &be_gann1_3;
 //BA.debugLineNum = 28;BA.debugLine="Private d1pins As D1Pins";
b4r_main::_d1pins = &be_gann2_3;
 //BA.debugLineNum = 29;BA.debugLine="Private pin16 As Pin";
b4r_main::_pin16 = &be_gann3_3;
 //BA.debugLineNum = 30;BA.debugLine="Private d6 As Pin";
b4r_main::_d6 = &be_gann4_3;
 //BA.debugLineNum = 31;BA.debugLine="Private WiFi As ESP8266WiFi";
b4r_main::_wifi = &be_gann5_3;
 //BA.debugLineNum = 32;BA.debugLine="Private WiFiStr As WiFiSocket";
b4r_main::_wifistr = &be_gann6_3;
 //BA.debugLineNum = 33;BA.debugLine="Private MQTT As MqttClient";
b4r_main::_mqtt = &be_gann7_3;
 //BA.debugLineNum = 34;BA.debugLine="Private MQTTOpt As MqttConnectOptions";
b4r_main::_mqttopt = &be_gann8_3;
 //BA.debugLineNum = 35;BA.debugLine="Private MQTTUser As String = \"vynckfaq\"";
b4r_main::_mqttuser = be_gann9_5.wrap("vynckfaq");
 //BA.debugLineNum = 36;BA.debugLine="Private MQTTPassword As String = \"KHSV1Q1qSUUY\"";
b4r_main::_mqttpassword = be_gann10_5.wrap("KHSV1Q1qSUUY");
 //BA.debugLineNum = 37;BA.debugLine="Private MQTTHostName As String = \"m14.cloudmqtt.c";
b4r_main::_mqtthostname = be_gann11_5.wrap("m14.cloudmqtt.com");
 //BA.debugLineNum = 38;BA.debugLine="Private MQTTPort As Int = 11816";
b4r_main::_mqttport = 11816;
 //BA.debugLineNum = 39;BA.debugLine="Private ESP As ESP8266";
b4r_main::_esp = &be_gann13_3;
 //BA.debugLineNum = 40;BA.debugLine="Private WiFiSSID As String = \"Rise Above This Hom";
b4r_main::_wifissid = be_gann14_5.wrap("Rise Above This Home");
 //BA.debugLineNum = 41;BA.debugLine="Private WiFiPassword As String = \"SteelReserve\"";
b4r_main::_wifipassword = be_gann15_5.wrap("SteelReserve");
 //BA.debugLineNum = 42;BA.debugLine="Public Timer1 As Timer                    'Timer";
b4r_main::_timer1 = &be_gann16_3;
 //BA.debugLineNum = 43;BA.debugLine="Public DHT22pin As Pin                    ' ESP82";
b4r_main::_dht22pin = &be_gann17_3;
 //BA.debugLineNum = 44;BA.debugLine="Public Interval As Int                    'Interv";
b4r_main::_interval = 0;
 //BA.debugLineNum = 45;BA.debugLine="Private d3pins As D1Pins";
b4r_main::_d3pins = &be_gann19_3;
 //BA.debugLineNum = 46;BA.debugLine="Public DHT22Temp As Double";
b4r_main::_dht22temp = 0;
 //BA.debugLineNum = 47;BA.debugLine="Public DHT22Hum As Double";
b4r_main::_dht22hum = 0;
 //BA.debugLineNum = 48;BA.debugLine="Public DHT22State As Int";
b4r_main::_dht22state = 0;
 //BA.debugLineNum = 49;BA.debugLine="Public DHT22HeatIndex As Double";
b4r_main::_dht22heatindex = 0;
 //BA.debugLineNum = 50;BA.debugLine="Public DHT22DewPoint As Double";
b4r_main::_dht22dewpoint = 0;
 //BA.debugLineNum = 51;BA.debugLine="Public DHT22Perception As Int";
b4r_main::_dht22perception = 0;
 //BA.debugLineNum = 52;BA.debugLine="Public DHT22ComfortStatus As Int";
b4r_main::_dht22comfortstatus = 0;
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
}
void b4r_main::_timer1_tick(){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString* _localstate = B4R::B4RString::EMPTY;
B4R::B4RString be_ann88_2;
B4R::B4RString be_ann90_2;
B4R::B4RString be_ann92_2;
B4R::B4RString* _localperception = B4R::B4RString::EMPTY;
B4R::B4RString be_ann97_2;
B4R::B4RString be_ann99_2;
B4R::B4RString be_ann101_2;
B4R::B4RString be_ann103_2;
B4R::B4RString be_ann105_2;
B4R::B4RString be_ann107_2;
B4R::B4RString be_ann109_2;
B4R::B4RString be_ann111_2;
B4R::B4RString* _localcomfortstatus = B4R::B4RString::EMPTY;
B4R::B4RString be_ann116_2;
B4R::B4RString be_ann118_2;
B4R::B4RString be_ann120_2;
B4R::B4RString be_ann122_2;
B4R::B4RString be_ann124_2;
B4R::B4RString be_ann126_2;
B4R::B4RString be_ann128_2;
B4R::B4RString be_ann130_2;
B4R::B4RString be_ann132_2;
B4R::B4RString be_ann134_2;
B4R::B4RString* _s = B4R::B4RString::EMPTY;
B4R::B4RString be_ann139_12;
B4R::B4RString be_ann139_16;
B4R::B4RString be_ann139_20;
B4R::B4RString be_ann139_24;
B4R::B4RString be_ann139_28;
B4R::B4RString be_ann139_32;
B4R::B4RString* be_ann139_35e1[13];
B4R::Array be_ann139_35e2;
B4R::B4RString be_ann141_4;
 //BA.debugLineNum = 127;BA.debugLine="Sub Timer1_Tick";
 //BA.debugLineNum = 128;BA.debugLine="RunNative(\"ReadDHT1\",Null)";
Common_RunNative(ReadDHT1,Common_Null);
 //BA.debugLineNum = 130;BA.debugLine="Dim localstate As String";
_localstate = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 131;BA.debugLine="Select Case DHT22State";
switch (b4r_main::_dht22state) {
case 0: {
 //BA.debugLineNum = 134;BA.debugLine="localstate = \"OK\"";
_localstate = be_ann88_2.wrap("OK");
 break; }
case 1: {
 //BA.debugLineNum = 136;BA.debugLine="localstate = \"TIMEOUT\"";
_localstate = be_ann90_2.wrap("TIMEOUT");
 break; }
case 2: {
 //BA.debugLineNum = 140;BA.debugLine="localstate = \"CHECKSUM\"";
_localstate = be_ann92_2.wrap("CHECKSUM");
 break; }
}
;
 //BA.debugLineNum = 153;BA.debugLine="Dim localperception As String";
_localperception = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 154;BA.debugLine="Select Case DHT22Perception";
switch (b4r_main::_dht22perception) {
case 0: {
 //BA.debugLineNum = 156;BA.debugLine="localperception = \"Feels like the western US, a";
_localperception = be_ann97_2.wrap("Feels like the western US, a bit dry to some");
 break; }
case 1: {
 //BA.debugLineNum = 158;BA.debugLine="localperception = \"Very comfortable\"";
_localperception = be_ann99_2.wrap("Very comfortable");
 break; }
case 2: {
 //BA.debugLineNum = 160;BA.debugLine="localperception = \"Comfortable\"";
_localperception = be_ann101_2.wrap("Comfortable");
 break; }
case 3: {
 //BA.debugLineNum = 162;BA.debugLine="localperception = \"OK but everyone perceives th";
_localperception = be_ann103_2.wrap("OK but everyone perceives the humidity at upper limit");
 break; }
case 4: {
 //BA.debugLineNum = 164;BA.debugLine="localperception = \"Somewhat uncomfortable for m";
_localperception = be_ann105_2.wrap("Somewhat uncomfortable for most people at upper limit");
 break; }
case 5: {
 //BA.debugLineNum = 166;BA.debugLine="localperception = \"Very humid, quite uncomforta";
_localperception = be_ann107_2.wrap("Very humid, quite uncomfortable");
 break; }
case 6: {
 //BA.debugLineNum = 168;BA.debugLine="localperception = \"Extremely uncomfortable, opp";
_localperception = be_ann109_2.wrap("Extremely uncomfortable, oppressive");
 break; }
case 7: {
 //BA.debugLineNum = 170;BA.debugLine="localperception = \"Severely high, even deadly f";
_localperception = be_ann111_2.wrap("Severely high, even deadly for asthma related illnesses");
 break; }
}
;
 //BA.debugLineNum = 173;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 174;BA.debugLine="Select Case DHT22ComfortStatus";
switch (b4r_main::_dht22comfortstatus) {
case 0: {
 //BA.debugLineNum = 176;BA.debugLine="localcomfortstatus = \"OK\"";
_localcomfortstatus = be_ann116_2.wrap("OK");
 break; }
case 1: {
 //BA.debugLineNum = 178;BA.debugLine="localcomfortstatus = \"Too hot\"";
_localcomfortstatus = be_ann118_2.wrap("Too hot");
 break; }
case 2: {
 //BA.debugLineNum = 180;BA.debugLine="localcomfortstatus = \"Too cold\"";
_localcomfortstatus = be_ann120_2.wrap("Too cold");
 break; }
case 4: {
 //BA.debugLineNum = 182;BA.debugLine="localcomfortstatus = \"Too dry\"";
_localcomfortstatus = be_ann122_2.wrap("Too dry");
 break; }
case 5: {
 //BA.debugLineNum = 184;BA.debugLine="localcomfortstatus = \"Hot and dry\"";
_localcomfortstatus = be_ann124_2.wrap("Hot and dry");
 break; }
case 6: {
 //BA.debugLineNum = 186;BA.debugLine="localcomfortstatus = \"Cold and dry\"";
_localcomfortstatus = be_ann126_2.wrap("Cold and dry");
 break; }
case 8: {
 //BA.debugLineNum = 188;BA.debugLine="localcomfortstatus = \"Too humid\"";
_localcomfortstatus = be_ann128_2.wrap("Too humid");
 break; }
case 9: {
 //BA.debugLineNum = 190;BA.debugLine="localcomfortstatus = \"Hot and humid\"";
_localcomfortstatus = be_ann130_2.wrap("Hot and humid");
 break; }
case 10: {
 //BA.debugLineNum = 192;BA.debugLine="localcomfortstatus = \"Cold and humid\"";
_localcomfortstatus = be_ann132_2.wrap("Cold and humid");
 break; }
default: {
 //BA.debugLineNum = 194;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = be_ann134_2.wrap("Unknown");
 break; }
}
;
 //BA.debugLineNum = 197;BA.debugLine="Log(\"State: \", localstate, \" Temp: \", DHT22Temp,";
B4R::Common::LogHelper(15,102,F("State: "),101,_localstate->data,102,F(" Temp: "),7,(double)(b4r_main::_dht22temp),102,F(" ºF Humidity: "),7,(double)(b4r_main::_dht22hum),102,F(" % Perception: "),101,_localperception->data,102,F(" Comfort Status: "),101,_localcomfortstatus->data,102,F(" HeatIndex: "),7,(double)(b4r_main::_dht22heatindex),102,F(" ºF Dew Point: "),7,(double)(b4r_main::_dht22dewpoint),102,F(" ºF"));
 //BA.debugLineNum = 198;BA.debugLine="If WiFi.IsConnected Then";
if (b4r_main::_wifi->getIsConnected()) { 
 //BA.debugLineNum = 199;BA.debugLine="Dim s As String";
_s = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 200;BA.debugLine="s = JoinStrings(Array As String(localstate,\"|\",D";
_s = B4R::__c->JoinStrings(be_ann139_35e2.create(be_ann139_35e1,13,100,_localstate,be_ann139_12.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22temp)),be_ann139_16.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22hum)),be_ann139_20.wrap("|"),B4R::B4RString::fromNumber((Long)(b4r_main::_dht22perception)),be_ann139_24.wrap("|"),B4R::B4RString::fromNumber((Long)(b4r_main::_dht22comfortstatus)),be_ann139_28.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22heatindex)),be_ann139_32.wrap("|"),B4R::B4RString::fromNumber((Double)(b4r_main::_dht22dewpoint))));
 //BA.debugLineNum = 201;BA.debugLine="Log(\"Sending to MQTT: \",s)";
B4R::Common::LogHelper(2,102,F("Sending to MQTT: "),101,_s->data);
 //BA.debugLineNum = 202;BA.debugLine="MQTT.Publish(\"Cloyd\",s)";
b4r_main::_mqtt->Publish(be_ann141_4.wrap("Cloyd"),(_s)->GetBytes());
 };
 //BA.debugLineNum = 204;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
