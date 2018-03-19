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
B4R::B4RDht* b4r_main::_dht11sensor;
B4R::Timer* b4r_main::_timer1;
B4R::Pin* b4r_main::_dht11pin;
Int b4r_main::_interval;
B4R::D1Pins* b4r_main::_d3pins;
Double b4r_main::_humidity;
Double b4r_main::_temperature;
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
static B4R::B4RDht be_gann16_3;
static B4R::Timer be_gann17_3;
static B4R::Pin be_gann18_3;
static B4R::D1Pins be_gann20_3;


 void SetSTA(B4R::Object* o) {
   WiFi.mode(WIFI_STA);
}
void b4r_main::_appstart(){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString* _clientid = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 34;BA.debugLine="Private Sub AppStart";
 //BA.debugLineNum = 35;BA.debugLine="Serial1.Initialize(115200)";
b4r_main::_serial1->Initialize((ULong) (115200));
 //BA.debugLineNum = 36;BA.debugLine="Delay(3000)";
Common_Delay((ULong) (3000));
 //BA.debugLineNum = 37;BA.debugLine="Log(\"AppStart\")";
B4R::Common::LogHelper(1,102,F("AppStart"));
 //BA.debugLineNum = 42;BA.debugLine="pin16.Initialize(16, pin16.MODE_OUTPUT)";
b4r_main::_pin16->Initialize((Byte) (16),Pin_MODE_OUTPUT);
 //BA.debugLineNum = 43;BA.debugLine="d6.Initialize(d1pins.D6, d6.MODE_OUTPUT)";
b4r_main::_d6->Initialize(b4r_main::_d1pins->D6,Pin_MODE_OUTPUT);
 //BA.debugLineNum = 45;BA.debugLine="Log(\"Stopping access point\")";
B4R::Common::LogHelper(1,102,F("Stopping access point"));
 //BA.debugLineNum = 46;BA.debugLine="RunNative(\"SetSTA\", Null)";
Common_RunNative(SetSTA,Common_Null);
 //BA.debugLineNum = 49;BA.debugLine="ConnectToWifi";
_connecttowifi();
 //BA.debugLineNum = 52;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'creat";
_clientid = B4R::B4RString::fromNumber((Long)(Common_Rnd((Long) (0),(Long) (999999999))));
 //BA.debugLineNum = 53;BA.debugLine="MQTT.Initialize2(WiFiStr.stream, MQTTHostName, MQ";
b4r_main::_mqtt->Initialize2(b4r_main::_wifistr->getStream(),b4r_main::_mqtthostname,(UInt) (b4r_main::_mqttport),_clientid,_mqtt_messagearrived,_mqtt_disconnected);
 //BA.debugLineNum = 54;BA.debugLine="MQTTOpt.Initialize(MQTTUser, MQTTPassword)";
b4r_main::_mqttopt->Initialize(b4r_main::_mqttuser,b4r_main::_mqttpassword);
 //BA.debugLineNum = 55;BA.debugLine="MQTT_Connect(0)";
_mqtt_connect((Byte) (0));
 //BA.debugLineNum = 58;BA.debugLine="Interval = 5";
b4r_main::_interval = 5;
 //BA.debugLineNum = 59;BA.debugLine="DHT11pin.Initialize(d3pins.D3, DHT11pin.MODE_INPU";
b4r_main::_dht11pin->Initialize(b4r_main::_d3pins->D3,Pin_MODE_INPUT);
 //BA.debugLineNum = 60;BA.debugLine="Timer1.Initialize(\"Timer1_Tick\", Interval * 1000)";
b4r_main::_timer1->Initialize(_timer1_tick,(ULong) (b4r_main::_interval*1000));
 //BA.debugLineNum = 61;BA.debugLine="Timer1.Enabled=True";
b4r_main::_timer1->setEnabled(Common_True);
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_connecttowifi(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 107;BA.debugLine="Sub ConnectToWifi";
 //BA.debugLineNum = 108;BA.debugLine="WiFi.Connect2(WiFiSSID, WiFiPassword)";
b4r_main::_wifi->Connect2(b4r_main::_wifissid,b4r_main::_wifipassword);
 //BA.debugLineNum = 110;BA.debugLine="If WiFi.IsConnected Then";
if (b4r_main::_wifi->getIsConnected()) { 
 //BA.debugLineNum = 111;BA.debugLine="Log(\"Connected to WiFi, Local IP \", WiFi.LocalIp";
B4R::Common::LogHelper(2,102,F("Connected to WiFi, Local IP "),101,b4r_main::_wifi->getLocalIp()->data);
 }else {
 //BA.debugLineNum = 113;BA.debugLine="Log(\"Not Connected to WiFi\")";
B4R::Common::LogHelper(1,102,F("Not Connected to WiFi"));
 };
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_connect(Byte _unused){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString be_ann62_4;
 //BA.debugLineNum = 76;BA.debugLine="Sub MQTT_Connect(Unused As Byte)";
 //BA.debugLineNum = 77;BA.debugLine="If WiFi.IsConnected = False Then";
if (b4r_main::_wifi->getIsConnected()==Common_False) { 
 //BA.debugLineNum = 78;BA.debugLine="ConnectToWifi";
_connecttowifi();
 };
 //BA.debugLineNum = 80;BA.debugLine="If MQTT.Connect = False Then";
if (b4r_main::_mqtt->Connect()==Common_False) { 
 //BA.debugLineNum = 81;BA.debugLine="Log(\"Connecting to broker\")";
B4R::Common::LogHelper(1,102,F("Connecting to broker"));
 //BA.debugLineNum = 82;BA.debugLine="MQTT.Connect2(MQTTOpt)";
b4r_main::_mqtt->Connect2(b4r_main::_mqttopt);
 //BA.debugLineNum = 83;BA.debugLine="CallSubPlus(\"MQTT_Connect\", 1000, 0)";
B4R::__c->CallSubPlus(_mqtt_connect,(ULong) (1000),(Byte) (0));
 }else {
 //BA.debugLineNum = 85;BA.debugLine="pin16.DigitalWrite(False)";
b4r_main::_pin16->DigitalWrite(Common_False);
 //BA.debugLineNum = 86;BA.debugLine="Log(\"Connected to broker\")";
B4R::Common::LogHelper(1,102,F("Connected to broker"));
 //BA.debugLineNum = 87;BA.debugLine="MQTT.Subscribe(\"Cloyd\", 0)";
b4r_main::_mqtt->Subscribe(be_ann62_4.wrap("Cloyd"),(Byte) (0));
 };
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_disconnected(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 100;BA.debugLine="Sub mqtt_Disconnected";
 //BA.debugLineNum = 101;BA.debugLine="pin16.DigitalWrite(True)";
b4r_main::_pin16->DigitalWrite(Common_True);
 //BA.debugLineNum = 102;BA.debugLine="Log(\"Disconnected from broker\")";
B4R::Common::LogHelper(1,102,F("Disconnected from broker"));
 //BA.debugLineNum = 103;BA.debugLine="MQTT.Close";
b4r_main::_mqtt->Close();
 //BA.debugLineNum = 104;BA.debugLine="MQTT_Connect(0)";
_mqtt_connect((Byte) (0));
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_messagearrived(B4R::B4RString* _topic,B4R::Array* _payload){
const UInt cp = B4R::StackMemory::cp;
B4R::Object be_ann67_8;
B4R::B4RString be_ann68_3;
B4R::B4RString be_ann69_4;
B4R::B4RString be_ann69_6;
 //BA.debugLineNum = 91;BA.debugLine="Sub mqtt_MessageArrived (Topic As String, Payload(";
 //BA.debugLineNum = 92;BA.debugLine="pin16.DigitalWrite(False)";
b4r_main::_pin16->DigitalWrite(Common_False);
 //BA.debugLineNum = 93;BA.debugLine="Log(\"Message arrived. Topic=\", Topic, \" Payload=\"";
B4R::Common::LogHelper(4,102,F("Message arrived. Topic="),101,_topic->data,102,F(" Payload="),100,be_ann67_8.wrapPointer(_payload));
 //BA.debugLineNum = 94;BA.debugLine="If Payload = \"Restart controller\" Then";
if ((_payload)->equals((be_ann68_3.wrap("Restart controller"))->GetBytes())) { 
 //BA.debugLineNum = 95;BA.debugLine="MQTT.Publish(\"Cloyd\",\"*Restarting relay by remot";
b4r_main::_mqtt->Publish(be_ann69_4.wrap("Cloyd"),(be_ann69_6.wrap("*Restarting relay by remote"))->GetBytes());
 //BA.debugLineNum = 96;BA.debugLine="ESP.Restart";
b4r_main::_esp->Restart();
 };
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}

void b4r_main::initializeProcessGlobals() {
     B4R::StackMemory::buffer = (byte*)malloc(STACK_BUFFER_SIZE);
     b4r_main::_process_globals();

   
}
void b4r_main::_process_globals(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 11;BA.debugLine="Public Serial1 As Serial";
b4r_main::_serial1 = &be_gann1_3;
 //BA.debugLineNum = 12;BA.debugLine="Private d1pins As D1Pins";
b4r_main::_d1pins = &be_gann2_3;
 //BA.debugLineNum = 13;BA.debugLine="Private pin16 As Pin";
b4r_main::_pin16 = &be_gann3_3;
 //BA.debugLineNum = 14;BA.debugLine="Private d6 As Pin";
b4r_main::_d6 = &be_gann4_3;
 //BA.debugLineNum = 15;BA.debugLine="Private WiFi As ESP8266WiFi";
b4r_main::_wifi = &be_gann5_3;
 //BA.debugLineNum = 16;BA.debugLine="Private WiFiStr As WiFiSocket";
b4r_main::_wifistr = &be_gann6_3;
 //BA.debugLineNum = 17;BA.debugLine="Private MQTT As MqttClient";
b4r_main::_mqtt = &be_gann7_3;
 //BA.debugLineNum = 18;BA.debugLine="Private MQTTOpt As MqttConnectOptions";
b4r_main::_mqttopt = &be_gann8_3;
 //BA.debugLineNum = 19;BA.debugLine="Private MQTTUser As String = \"vynckfaq\"";
b4r_main::_mqttuser = be_gann9_5.wrap("vynckfaq");
 //BA.debugLineNum = 20;BA.debugLine="Private MQTTPassword As String = \"KHSV1Q1qSUUY\"";
b4r_main::_mqttpassword = be_gann10_5.wrap("KHSV1Q1qSUUY");
 //BA.debugLineNum = 21;BA.debugLine="Private MQTTHostName As String = \"m14.cloudmqtt.c";
b4r_main::_mqtthostname = be_gann11_5.wrap("m14.cloudmqtt.com");
 //BA.debugLineNum = 22;BA.debugLine="Private MQTTPort As Int = 11816";
b4r_main::_mqttport = 11816;
 //BA.debugLineNum = 23;BA.debugLine="Private ESP As ESP8266";
b4r_main::_esp = &be_gann13_3;
 //BA.debugLineNum = 24;BA.debugLine="Private WiFiSSID As String = \"Rise Above This Hom";
b4r_main::_wifissid = be_gann14_5.wrap("Rise Above This Home");
 //BA.debugLineNum = 25;BA.debugLine="Private WiFiPassword As String = \"SteelReserve\"";
b4r_main::_wifipassword = be_gann15_5.wrap("SteelReserve");
 //BA.debugLineNum = 26;BA.debugLine="Public DHT11sensor As dht                'DHT11 s";
b4r_main::_dht11sensor = &be_gann16_3;
 //BA.debugLineNum = 27;BA.debugLine="Public Timer1 As Timer                    'Timer";
b4r_main::_timer1 = &be_gann17_3;
 //BA.debugLineNum = 28;BA.debugLine="Public DHT11pin As Pin                    ' ESP82";
b4r_main::_dht11pin = &be_gann18_3;
 //BA.debugLineNum = 29;BA.debugLine="Public Interval As Int                    'Interv";
b4r_main::_interval = 0;
 //BA.debugLineNum = 30;BA.debugLine="Private d3pins As D1Pins";
b4r_main::_d3pins = &be_gann20_3;
 //BA.debugLineNum = 31;BA.debugLine="Dim humidity,temperature As Double        'Humidi";
b4r_main::_humidity = 0;
b4r_main::_temperature = 0;
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
}
void b4r_main::_timer1_tick(){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString be_ann47_13;
B4R::B4RString be_ann47_17;
B4R::B4RString be_ann47_19;
B4R::B4RString be_ann47_23;
B4R::B4RString* be_ann47_24e1[6];
B4R::Array be_ann47_24e2;
B4R::B4RString* _s = B4R::B4RString::EMPTY;
B4R::B4RString be_ann48_4;
 //BA.debugLineNum = 64;BA.debugLine="Sub Timer1_Tick";
 //BA.debugLineNum = 65;BA.debugLine="DHT11sensor.Read11(DHT11pin.PinNumber)";
b4r_main::_dht11sensor->Read11(b4r_main::_dht11pin->PinNumber);
 //BA.debugLineNum = 66;BA.debugLine="humidity = DHT11sensor.GetHumidity + 12";
b4r_main::_humidity = b4r_main::_dht11sensor->GetHumidity()+12;
 //BA.debugLineNum = 67;BA.debugLine="temperature = DHT11sensor.GetTemperature * 9 / 5";
b4r_main::_temperature = b4r_main::_dht11sensor->GetTemperature()*9/(Double)5+32;
 //BA.debugLineNum = 68;BA.debugLine="Log(\"Humidity = \",humidity, \" %\", \"  Temperature";
B4R::Common::LogHelper(6,102,F("Humidity = "),7,(double)(b4r_main::_humidity),102,F(" %"),102,F("  Temperature = "),7,(double)(b4r_main::_temperature),102,F(" ºF"));
 //BA.debugLineNum = 70;BA.debugLine="If WiFi.IsConnected Then";
if (b4r_main::_wifi->getIsConnected()) { 
 //BA.debugLineNum = 71;BA.debugLine="Dim s As String  = JoinStrings(Array As String(\"";
_s = B4R::__c->JoinStrings(be_ann47_24e2.create(be_ann47_24e1,6,100,be_ann47_13.wrap("Humidity = "),B4R::B4RString::fromNumber((Double)(b4r_main::_humidity)),be_ann47_17.wrap(" %"),be_ann47_19.wrap("  Temperature = "),B4R::B4RString::fromNumber((Double)(b4r_main::_temperature)),be_ann47_23.wrap(" ºF")));
 //BA.debugLineNum = 72;BA.debugLine="MQTT.Publish(\"Cloyd\",s)";
b4r_main::_mqtt->Publish(be_ann48_4.wrap("Cloyd"),(_s)->GetBytes());
 };
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
