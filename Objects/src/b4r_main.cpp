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
bool b4r_main::_stoprelay;
B4R::B4RESP8266* b4r_main::_esp;
B4R::B4RString* b4r_main::_wifissid;
B4R::B4RString* b4r_main::_wifipassword;
ULong b4r_main::_relayopendelay;
ULong b4r_main::_relaycloseddelay;
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
static B4R::B4RESP8266 be_gann14_3;
static B4R::B4RString be_gann15_5;
static B4R::B4RString be_gann16_5;
static B4R::B4RDht be_gann19_3;
static B4R::Timer be_gann20_3;
static B4R::Pin be_gann21_3;
static B4R::D1Pins be_gann23_3;


 void b4r_main::_appstart(){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString* _clientid = B4R::B4RString::EMPTY;
 //BA.debugLineNum = 38;BA.debugLine="Private Sub AppStart";
 //BA.debugLineNum = 39;BA.debugLine="Serial1.Initialize(115200)";
b4r_main::_serial1->Initialize((ULong) (115200));
 //BA.debugLineNum = 40;BA.debugLine="Delay(3000)";
Common_Delay((ULong) (3000));
 //BA.debugLineNum = 41;BA.debugLine="Log(\"AppStart\")";
B4R::Common::LogHelper(1,102,F("AppStart"));
 //BA.debugLineNum = 46;BA.debugLine="pin16.Initialize(16, pin16.MODE_OUTPUT)";
b4r_main::_pin16->Initialize((Byte) (16),Pin_MODE_OUTPUT);
 //BA.debugLineNum = 47;BA.debugLine="d6.Initialize(d1pins.D6, d6.MODE_OUTPUT)";
b4r_main::_d6->Initialize(b4r_main::_d1pins->D6,Pin_MODE_OUTPUT);
 //BA.debugLineNum = 50;BA.debugLine="ConnectToWifi";
_connecttowifi();
 //BA.debugLineNum = 53;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'creat";
_clientid = B4R::B4RString::fromNumber((Long)(Common_Rnd((Long) (0),(Long) (999999999))));
 //BA.debugLineNum = 54;BA.debugLine="MQTT.Initialize2(WiFiStr.stream, MQTTHostName, MQ";
b4r_main::_mqtt->Initialize2(b4r_main::_wifistr->getStream(),b4r_main::_mqtthostname,(UInt) (b4r_main::_mqttport),_clientid,_mqtt_messagearrived,_mqtt_disconnected);
 //BA.debugLineNum = 55;BA.debugLine="MQTTOpt.Initialize(MQTTUser, MQTTPassword)";
b4r_main::_mqttopt->Initialize(b4r_main::_mqttuser,b4r_main::_mqttpassword);
 //BA.debugLineNum = 56;BA.debugLine="MQTT_Connect(0)";
_mqtt_connect((Byte) (0));
 //BA.debugLineNum = 65;BA.debugLine="Interval=5";
b4r_main::_interval = 5;
 //BA.debugLineNum = 66;BA.debugLine="DHT11pin.Initialize(d3pins.D3, DHT11pin.MODE_INPU";
b4r_main::_dht11pin->Initialize(b4r_main::_d3pins->D3,Pin_MODE_INPUT);
 //BA.debugLineNum = 67;BA.debugLine="Timer1.Initialize(\"Timer1_Tick\", Interval * 1000)";
b4r_main::_timer1->Initialize(_timer1_tick,(ULong) (b4r_main::_interval*1000));
 //BA.debugLineNum = 68;BA.debugLine="Timer1.Enabled=True";
b4r_main::_timer1->setEnabled(Common_True);
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_cleareeprom(){
const UInt cp = B4R::StackMemory::cp;
B4R::B4REEPROM be_ann170_3;
B4R::B4REEPROM* _eeprom = NULL;
UInt _i = 0;
Byte be_ann174_9e1[1];
B4R::Array be_ann174_9e2;
 //BA.debugLineNum = 234;BA.debugLine="Sub ClearEEPROM";
 //BA.debugLineNum = 235;BA.debugLine="Dim eeprom As EEPROM";
_eeprom = &be_ann170_3;
 //BA.debugLineNum = 236;BA.debugLine="Dim i As UInt";
_i = 0;
 //BA.debugLineNum = 238;BA.debugLine="Log(\"Size: \", eeprom.Size, \" bytes\")";
B4R::Common::LogHelper(3,102,F("Size: "),4,_eeprom->getSize(),102,F(" bytes"));
 //BA.debugLineNum = 239;BA.debugLine="For i = 0 To eeprom.Size";
{
const int step4 = 1;
const int limit4 = _eeprom->getSize();
_i = (UInt) (0) ;
for (;(step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4) ;_i = ((UInt)(0 + _i + step4))  ) {
 //BA.debugLineNum = 240;BA.debugLine="eeprom.WriteBytes(Array As Byte(0), i)";
_eeprom->WriteBytes(be_ann174_9e2.create(be_ann174_9e1,1,1,(Byte) (0)),_i);
 //BA.debugLineNum = 241;BA.debugLine="Log(i)";
B4R::Common::LogHelper(1,4,_i);
 }
};
 //BA.debugLineNum = 243;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_connecttowifi(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 156;BA.debugLine="Sub ConnectToWifi";
 //BA.debugLineNum = 157;BA.debugLine="WiFi.Connect2(WiFiSSID, WiFiPassword)";
b4r_main::_wifi->Connect2(b4r_main::_wifissid,b4r_main::_wifipassword);
 //BA.debugLineNum = 159;BA.debugLine="If WiFi.IsConnected Then";
if (b4r_main::_wifi->getIsConnected()) { 
 //BA.debugLineNum = 160;BA.debugLine="Log(\"Connected to WiFi, Local IP \", WiFi.LocalIp";
B4R::Common::LogHelper(2,102,F("Connected to WiFi, Local IP "),101,b4r_main::_wifi->getLocalIp()->data);
 }else {
 //BA.debugLineNum = 162;BA.debugLine="Log(\"Not Connected to WiFi\")";
B4R::Common::LogHelper(1,102,F("Not Connected to WiFi"));
 };
 //BA.debugLineNum = 164;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_connect(Byte _unused){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString be_ann61_4;
 //BA.debugLineNum = 80;BA.debugLine="Sub MQTT_Connect(Unused As Byte)";
 //BA.debugLineNum = 81;BA.debugLine="If WiFi.IsConnected = False Then";
if (b4r_main::_wifi->getIsConnected()==Common_False) { 
 //BA.debugLineNum = 82;BA.debugLine="ConnectToWifi";
_connecttowifi();
 };
 //BA.debugLineNum = 84;BA.debugLine="If MQTT.Connect = False Then";
if (b4r_main::_mqtt->Connect()==Common_False) { 
 //BA.debugLineNum = 85;BA.debugLine="Log(\"Connecting to broker\")";
B4R::Common::LogHelper(1,102,F("Connecting to broker"));
 //BA.debugLineNum = 86;BA.debugLine="MQTT.Connect2(MQTTOpt)";
b4r_main::_mqtt->Connect2(b4r_main::_mqttopt);
 //BA.debugLineNum = 87;BA.debugLine="CallSubPlus(\"MQTT_Connect\", 1000, 0)";
B4R::__c->CallSubPlus(_mqtt_connect,(ULong) (1000),(Byte) (0));
 }else {
 //BA.debugLineNum = 89;BA.debugLine="pin16.DigitalWrite(False)";
b4r_main::_pin16->DigitalWrite(Common_False);
 //BA.debugLineNum = 90;BA.debugLine="Log(\"Connected to broker\")";
B4R::Common::LogHelper(1,102,F("Connected to broker"));
 //BA.debugLineNum = 91;BA.debugLine="MQTT.Subscribe(\"Cloyd\", 0)";
b4r_main::_mqtt->Subscribe(be_ann61_4.wrap("Cloyd"),(Byte) (0));
 };
 //BA.debugLineNum = 93;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 150;BA.debugLine="If WiFi.IsConnected = False Then";
if (b4r_main::_wifi->getIsConnected()==Common_False) { 
 //BA.debugLineNum = 151;BA.debugLine="ConnectToWifi";
_connecttowifi();
 };
 //BA.debugLineNum = 153;BA.debugLine="MQTT_Connect(0)";
_mqtt_connect((Byte) (0));
 //BA.debugLineNum = 154;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_mqtt_messagearrived(B4R::B4RString* _topic,B4R::Array* _payload){
const UInt cp = B4R::StackMemory::cp;
B4R::Object be_ann90_8;
B4R::B4RString be_ann91_3;
B4R::B4RString be_ann92_4;
B4R::B4RString be_ann92_6;
B4R::B4RString be_ann95_3;
B4R::B4RString be_ann96_4;
B4R::B4RString be_ann96_6;
B4R::B4RString be_ann99_3;
B4R::B4RString be_ann100_4;
B4R::B4RString be_ann100_6;
B4R::B4RString be_ann102_3;
B4R::B4RString be_ann104_4;
B4R::B4RString be_ann104_6;
B4R::B4RString be_ann106_4;
B4R::B4RString be_ann106_6;
B4R::B4RString be_ann108_4;
B4R::B4RString be_ann108_6;
 //BA.debugLineNum = 121;BA.debugLine="Sub mqtt_MessageArrived (Topic As String, Payload(";
 //BA.debugLineNum = 122;BA.debugLine="pin16.DigitalWrite(False)";
b4r_main::_pin16->DigitalWrite(Common_False);
 //BA.debugLineNum = 123;BA.debugLine="Log(\"Message arrived. Topic=\", Topic, \" Payload=\"";
B4R::Common::LogHelper(4,102,F("Message arrived. Topic="),101,_topic->data,102,F(" Payload="),100,be_ann90_8.wrapPointer(_payload));
 //BA.debugLineNum = 124;BA.debugLine="If Payload=\"Remotely close relay\" Then";
if ((_payload)->equals((be_ann91_3.wrap("Remotely close relay"))->GetBytes())) { 
 //BA.debugLineNum = 125;BA.debugLine="MQTT.Publish(\"Cloyd\",\"*Closing relay by remote\")";
b4r_main::_mqtt->Publish(be_ann92_4.wrap("Cloyd"),(be_ann92_6.wrap("*Closing relay by remote"))->GetBytes());
 //BA.debugLineNum = 126;BA.debugLine="d6.DigitalWrite(True)";
b4r_main::_d6->DigitalWrite(Common_True);
 //BA.debugLineNum = 127;BA.debugLine="StopRelay = True";
b4r_main::_stoprelay = Common_True;
 }else if((_payload)->equals((be_ann95_3.wrap("Remotely open relay"))->GetBytes())) { 
 //BA.debugLineNum = 129;BA.debugLine="MQTT.Publish(\"Cloyd\",\"*Opening relay by remote\")";
b4r_main::_mqtt->Publish(be_ann96_4.wrap("Cloyd"),(be_ann96_6.wrap("*Opening relay by remote"))->GetBytes());
 //BA.debugLineNum = 130;BA.debugLine="StopRelay = False";
b4r_main::_stoprelay = Common_False;
 //BA.debugLineNum = 131;BA.debugLine="RelayOpen(0)";
_relayopen((Byte) (0));
 }else if((_payload)->equals((be_ann99_3.wrap("Restart controller"))->GetBytes())) { 
 //BA.debugLineNum = 133;BA.debugLine="MQTT.Publish(\"Cloyd\",\"*Restarting relay by remot";
b4r_main::_mqtt->Publish(be_ann100_4.wrap("Cloyd"),(be_ann100_6.wrap("*Restarting relay by remote"))->GetBytes());
 //BA.debugLineNum = 134;BA.debugLine="ESP.Restart";
b4r_main::_esp->Restart();
 }else if((_payload)->equals((be_ann102_3.wrap("Check relay status"))->GetBytes())) { 
 //BA.debugLineNum = 136;BA.debugLine="If StopRelay Then";
if (b4r_main::_stoprelay) { 
 //BA.debugLineNum = 137;BA.debugLine="MQTT.Publish(\"Cloyd\",\"*Relay is permanently clo";
b4r_main::_mqtt->Publish(be_ann104_4.wrap("Cloyd"),(be_ann104_6.wrap("*Relay is permanently closed"))->GetBytes());
 }else if(b4r_main::_d6->DigitalRead()) { 
 //BA.debugLineNum = 139;BA.debugLine="MQTT.Publish(\"Cloyd\",\"*Relay is closed\")";
b4r_main::_mqtt->Publish(be_ann106_4.wrap("Cloyd"),(be_ann106_6.wrap("*Relay is closed"))->GetBytes());
 }else {
 //BA.debugLineNum = 141;BA.debugLine="MQTT.Publish(\"Cloyd\",\"*Relay is open\")";
b4r_main::_mqtt->Publish(be_ann108_4.wrap("Cloyd"),(be_ann108_6.wrap("*Relay is open"))->GetBytes());
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
 //BA.debugLineNum = 23;BA.debugLine="Private StopRelay As Boolean = False";
b4r_main::_stoprelay = Common_False;
 //BA.debugLineNum = 24;BA.debugLine="Private ESP As ESP8266";
b4r_main::_esp = &be_gann14_3;
 //BA.debugLineNum = 25;BA.debugLine="Private WiFiSSID As String = \"CloydNote8\"";
b4r_main::_wifissid = be_gann15_5.wrap("CloydNote8");
 //BA.debugLineNum = 26;BA.debugLine="Private WiFiPassword As String = \"adjj6347\"";
b4r_main::_wifipassword = be_gann16_5.wrap("adjj6347");
 //BA.debugLineNum = 27;BA.debugLine="Private RelayOpenDelay As ULong = 5000";
b4r_main::_relayopendelay = (ULong) (5000);
 //BA.debugLineNum = 28;BA.debugLine="Private RelayClosedDelay As ULong = 15000";
b4r_main::_relaycloseddelay = (ULong) (15000);
 //BA.debugLineNum = 30;BA.debugLine="Public DHT11sensor As dht                'DHT11 s";
b4r_main::_dht11sensor = &be_gann19_3;
 //BA.debugLineNum = 31;BA.debugLine="Public Timer1 As Timer                    'Timer";
b4r_main::_timer1 = &be_gann20_3;
 //BA.debugLineNum = 32;BA.debugLine="Public DHT11pin As Pin                    'Arduin";
b4r_main::_dht11pin = &be_gann21_3;
 //BA.debugLineNum = 33;BA.debugLine="Public Interval As Int                    'Interv";
b4r_main::_interval = 0;
 //BA.debugLineNum = 34;BA.debugLine="Private d3pins As D1Pins";
b4r_main::_d3pins = &be_gann23_3;
 //BA.debugLineNum = 35;BA.debugLine="Dim humidity,temperature As Double        'Humidi";
b4r_main::_humidity = 0;
b4r_main::_temperature = 0;
 //BA.debugLineNum = 36;BA.debugLine="End Sub";
}
void b4r_main::_readfromeeprom(){
const UInt cp = B4R::StackMemory::cp;
B4R::Object be_ann141_4e1[10];
B4R::Array be_ann141_4e2;
B4R::Object* be_ann141_4e3[10];
B4R::Array* _be = NULL;
B4R::ByteConverter be_ann142_3;
B4R::ByteConverter* _bc = NULL;
B4R::B4RSerializator be_ann143_3;
B4R::B4RSerializator* _serializator = NULL;
B4R::B4REEPROM be_ann144_3;
B4R::B4REEPROM* _eeprom = NULL;
Byte be_ann145_4e1[0];
B4R::Array be_ann145_4e2;
B4R::Array* _bytelengthofdataineeprom = NULL;
Int _actuallengthofdataineeprom = 0;
Byte be_ann147_4e1[0];
B4R::Array be_ann147_4e2;
B4R::Array* _bytedataineeprom = NULL;
B4R::Object be_ann148_4e1[0];
B4R::Array be_ann148_4e2;
B4R::Object* be_ann148_4e3[0];
B4R::Array* _objectsineeprom = NULL;
 //BA.debugLineNum = 185;BA.debugLine="Sub ReadFromEEPROM";
 //BA.debugLineNum = 186;BA.debugLine="Dim be(10) As Object 'used as a storage buffer fo";
_be =be_ann141_4e2.wrapObjects((void**)be_ann141_4e3,be_ann141_4e1,10, sizeof(B4R::Object));
 //BA.debugLineNum = 187;BA.debugLine="Dim bc As ByteConverter";
_bc = &be_ann142_3;
 //BA.debugLineNum = 188;BA.debugLine="Dim serializator As B4RSerializator";
_serializator = &be_ann143_3;
 //BA.debugLineNum = 189;BA.debugLine="Dim eeprom As EEPROM";
_eeprom = &be_ann144_3;
 //BA.debugLineNum = 191;BA.debugLine="Dim ByteLengthOfDataInEEPROM() As Byte";
_bytelengthofdataineeprom =be_ann145_4e2.wrap(be_ann145_4e1,0);
 //BA.debugLineNum = 192;BA.debugLine="Dim ActualLengthOfDataInEEPROM As Int";
_actuallengthofdataineeprom = 0;
 //BA.debugLineNum = 193;BA.debugLine="Dim ByteDataInEEPROM() As Byte";
_bytedataineeprom =be_ann147_4e2.wrap(be_ann147_4e1,0);
 //BA.debugLineNum = 194;BA.debugLine="Dim ObjectsInEEPROM() As Object";
_objectsineeprom =be_ann148_4e2.wrapObjects((void**)be_ann148_4e3,be_ann148_4e1,0, sizeof(B4R::Object));
 //BA.debugLineNum = 196;BA.debugLine="Log(\"Reading delay settings from the EEPROM\")";
B4R::Common::LogHelper(1,102,F("Reading delay settings from the EEPROM"));
 //BA.debugLineNum = 199;BA.debugLine="ByteLengthOfDataInEEPROM = eeprom.ReadBytes(700,";
_bytelengthofdataineeprom = _eeprom->ReadBytes((UInt) (700),(UInt) (2));
 //BA.debugLineNum = 202;BA.debugLine="ActualLengthOfDataInEEPROM =  bc.IntsFromBytes(By";
_actuallengthofdataineeprom = ((Int*)ByteConverter_IntsFromBytes(_bytelengthofdataineeprom)->getData((UInt) (0)))[B4R::Array::staticIndex];
 //BA.debugLineNum = 205;BA.debugLine="ByteDataInEEPROM = eeprom.ReadBytes(702, ActualLe";
_bytedataineeprom = _eeprom->ReadBytes((UInt) (702),(UInt) (_actuallengthofdataineeprom));
 //BA.debugLineNum = 208;BA.debugLine="ObjectsInEEPROM = serializator.ConvertBytesToArra";
_objectsineeprom = _serializator->ConvertBytesToArray(_bytedataineeprom,_be);
 //BA.debugLineNum = 215;BA.debugLine="RelayOpenDelay = ObjectsInEEPROM(0)";
b4r_main::_relayopendelay = (ULong)(((B4R::Object**)_objectsineeprom->getData((UInt) (0)))[B4R::Array::staticIndex]->toLong());
 //BA.debugLineNum = 216;BA.debugLine="RelayClosedDelay = ObjectsInEEPROM(1)";
b4r_main::_relaycloseddelay = (ULong)(((B4R::Object**)_objectsineeprom->getData((UInt) (1)))[B4R::Array::staticIndex]->toLong());
 //BA.debugLineNum = 218;BA.debugLine="If IsNumber(RelayOpenDelay) = False Then";
if (B4R::__c->IsNumber(B4R::B4RString::fromNumber((Double)(b4r_main::_relayopendelay)))==Common_False) { 
 //BA.debugLineNum = 219;BA.debugLine="Log(\"EEPROM's relay open delay is non-numeric. S";
B4R::Common::LogHelper(1,102,F("EEPROM's relay open delay is non-numeric. Setting to 20 minutes."));
 //BA.debugLineNum = 220;BA.debugLine="RelayOpenDelay = 1200000";
b4r_main::_relayopendelay = (ULong) (1200000);
 //BA.debugLineNum = 221;BA.debugLine="WriteToEEPROM(1200000, 7200000)";
_writetoeeprom((ULong) (1200000),(ULong) (7200000));
 };
 //BA.debugLineNum = 224;BA.debugLine="If IsNumber(RelayClosedDelay) = False Then";
if (B4R::__c->IsNumber(B4R::B4RString::fromNumber((Double)(b4r_main::_relaycloseddelay)))==Common_False) { 
 //BA.debugLineNum = 225;BA.debugLine="Log(\"EEPROM's relay closed delay is non-numeric.";
B4R::Common::LogHelper(1,102,F("EEPROM's relay closed delay is non-numeric. Setting to 2 hours."));
 //BA.debugLineNum = 226;BA.debugLine="RelayClosedDelay = 7200000";
b4r_main::_relaycloseddelay = (ULong) (7200000);
 //BA.debugLineNum = 227;BA.debugLine="WriteToEEPROM(1200000, 7200000)";
_writetoeeprom((ULong) (1200000),(ULong) (7200000));
 };
 //BA.debugLineNum = 230;BA.debugLine="Log(\"RelayOpenDelay: \", RelayOpenDelay)";
B4R::Common::LogHelper(2,102,F("RelayOpenDelay: "),6,b4r_main::_relayopendelay);
 //BA.debugLineNum = 231;BA.debugLine="Log(\"RelayClosedDelay: \", RelayClosedDelay)";
B4R::Common::LogHelper(2,102,F("RelayClosedDelay: "),6,b4r_main::_relaycloseddelay);
 //BA.debugLineNum = 232;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_relayclose(Byte _tag){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString be_ann72_4;
B4R::B4RString be_ann72_6;
 //BA.debugLineNum = 95;BA.debugLine="Private Sub RelayClose (tag As Byte)";
 //BA.debugLineNum = 96;BA.debugLine="If WiFi.IsConnected = False Then";
if (b4r_main::_wifi->getIsConnected()==Common_False) { 
 //BA.debugLineNum = 97;BA.debugLine="ConnectToWifi";
_connecttowifi();
 };
 //BA.debugLineNum = 99;BA.debugLine="If StopRelay Then";
if (b4r_main::_stoprelay) { 
 //BA.debugLineNum = 100;BA.debugLine="Return";
B4R::StackMemory::cp = cp;
if (true) return ;
 };
 //BA.debugLineNum = 102;BA.debugLine="Log(\"*Relay has been closed\")";
B4R::Common::LogHelper(1,102,F("*Relay has been closed"));
 //BA.debugLineNum = 103;BA.debugLine="MQTT.Publish(\"Cloyd\",\"*Relay has been closed\")";
b4r_main::_mqtt->Publish(be_ann72_4.wrap("Cloyd"),(be_ann72_6.wrap("*Relay has been closed"))->GetBytes());
 //BA.debugLineNum = 104;BA.debugLine="d6.DigitalWrite(True)";
b4r_main::_d6->DigitalWrite(Common_True);
 //BA.debugLineNum = 105;BA.debugLine="CallSubPlus(\"RelayOpen\", RelayClosedDelay, 0)";
B4R::__c->CallSubPlus(_relayopen,b4r_main::_relaycloseddelay,(Byte) (0));
 //BA.debugLineNum = 106;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_relayopen(Byte _tag){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString be_ann84_4;
B4R::B4RString be_ann84_6;
 //BA.debugLineNum = 108;BA.debugLine="Private Sub RelayOpen (tag As Byte)";
 //BA.debugLineNum = 109;BA.debugLine="If WiFi.IsConnected = False Then";
if (b4r_main::_wifi->getIsConnected()==Common_False) { 
 //BA.debugLineNum = 110;BA.debugLine="ConnectToWifi";
_connecttowifi();
 };
 //BA.debugLineNum = 112;BA.debugLine="If StopRelay Then";
if (b4r_main::_stoprelay) { 
 //BA.debugLineNum = 113;BA.debugLine="Return";
B4R::StackMemory::cp = cp;
if (true) return ;
 };
 //BA.debugLineNum = 115;BA.debugLine="Log(\"*Relay has been opened\")";
B4R::Common::LogHelper(1,102,F("*Relay has been opened"));
 //BA.debugLineNum = 116;BA.debugLine="MQTT.Publish(\"Cloyd\",\"*Relay has been opened\")";
b4r_main::_mqtt->Publish(be_ann84_4.wrap("Cloyd"),(be_ann84_6.wrap("*Relay has been opened"))->GetBytes());
 //BA.debugLineNum = 117;BA.debugLine="d6.DigitalWrite(False)";
b4r_main::_d6->DigitalWrite(Common_False);
 //BA.debugLineNum = 118;BA.debugLine="CallSubPlus(\"RelayClose\", RelayOpenDelay, 0)";
B4R::__c->CallSubPlus(_relayclose,b4r_main::_relayopendelay,(Byte) (0));
 //BA.debugLineNum = 119;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_timer1_tick(){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString be_ann47_13;
B4R::B4RString be_ann47_17;
B4R::B4RString be_ann47_19;
B4R::B4RString be_ann47_29;
B4R::B4RString* be_ann47_30e1[6];
B4R::Array be_ann47_30e2;
B4R::B4RString* _s = B4R::B4RString::EMPTY;
B4R::B4RString be_ann48_4;
 //BA.debugLineNum = 71;BA.debugLine="Sub Timer1_Tick";
 //BA.debugLineNum = 72;BA.debugLine="DHT11sensor.Read11(DHT11pin.PinNumber)";
b4r_main::_dht11sensor->Read11(b4r_main::_dht11pin->PinNumber);
 //BA.debugLineNum = 73;BA.debugLine="humidity=DHT11sensor.GetHumidity + 15";
b4r_main::_humidity = b4r_main::_dht11sensor->GetHumidity()+15;
 //BA.debugLineNum = 74;BA.debugLine="temperature=DHT11sensor.GetTemperature";
b4r_main::_temperature = b4r_main::_dht11sensor->GetTemperature();
 //BA.debugLineNum = 75;BA.debugLine="Log(\"Humidity = \",humidity, \" %\", \"  Temperature";
B4R::Common::LogHelper(6,102,F("Humidity = "),7,(double)(b4r_main::_humidity),102,F(" %"),102,F("  Temperature = "),7,(double)(b4r_main::_temperature*9/(Double)5+32),102,F(" ºF"));
 //BA.debugLineNum = 76;BA.debugLine="Dim s As String  = JoinStrings(Array As String(\"H";
_s = B4R::__c->JoinStrings(be_ann47_30e2.create(be_ann47_30e1,6,100,be_ann47_13.wrap("Humidity = "),B4R::B4RString::fromNumber((Double)(b4r_main::_humidity)),be_ann47_17.wrap(" %"),be_ann47_19.wrap("  Temperature = "),B4R::B4RString::fromNumber((Double)(b4r_main::_temperature*9/(Double)5+32)),be_ann47_29.wrap(" ºF")));
 //BA.debugLineNum = 77;BA.debugLine="MQTT.Publish(\"Cloyd\",s)";
b4r_main::_mqtt->Publish(be_ann48_4.wrap("Cloyd"),(_s)->GetBytes());
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_main::_writetoeeprom(ULong _opendelay,ULong _closeddelay){
const UInt cp = B4R::StackMemory::cp;
B4R::Object be_ann130_10;
B4R::Object be_ann130_12;
B4R::Object* be_ann130_13e1[2];
B4R::Array be_ann130_13e2;
B4R::Array* _listofobjectstostore = NULL;
Byte be_ann131_4e1[0];
B4R::Array be_ann131_4e2;
B4R::Array* _bytedatatostore = NULL;
B4R::ByteConverter be_ann132_3;
B4R::ByteConverter* _bc = NULL;
B4R::B4RSerializator be_ann133_3;
B4R::B4RSerializator* _serializator = NULL;
B4R::B4REEPROM be_ann134_3;
B4R::B4REEPROM* _eeprom = NULL;
Int be_ann137_15e1[1];
B4R::Array be_ann137_15e2;
 //BA.debugLineNum = 166;BA.debugLine="Sub WriteToEEPROM(OpenDelay As ULong, ClosedDelay";
 //BA.debugLineNum = 167;BA.debugLine="Dim ListOfObjectsToStore() As Object = Array(Open";
_listofobjectstostore = be_ann130_13e2.create(be_ann130_13e1,2,100,be_ann130_10.wrapNumber(_opendelay),be_ann130_12.wrapNumber(_closeddelay));
 //BA.debugLineNum = 168;BA.debugLine="Dim ByteDataToStore() As Byte";
_bytedatatostore =be_ann131_4e2.wrap(be_ann131_4e1,0);
 //BA.debugLineNum = 169;BA.debugLine="Dim bc As ByteConverter";
_bc = &be_ann132_3;
 //BA.debugLineNum = 170;BA.debugLine="Dim serializator As B4RSerializator";
_serializator = &be_ann133_3;
 //BA.debugLineNum = 171;BA.debugLine="Dim eeprom As EEPROM";
_eeprom = &be_ann134_3;
 //BA.debugLineNum = 174;BA.debugLine="ByteDataToStore = serializator.ConvertArrayToByte";
_bytedatatostore = _serializator->ConvertArrayToBytes(_listofobjectstostore);
 //BA.debugLineNum = 176;BA.debugLine="Log(\"Writing delay settings into the EEPROM\")";
B4R::Common::LogHelper(1,102,F("Writing delay settings into the EEPROM"));
 //BA.debugLineNum = 179;BA.debugLine="eeprom.WriteBytes(bc.IntsToBytes(Array As Int(Byt";
_eeprom->WriteBytes(ByteConverter_IntsToBytes(be_ann137_15e2.create(be_ann137_15e1,1,3,_bytedatatostore->length)),(UInt) (700));
 //BA.debugLineNum = 182;BA.debugLine="eeprom.WriteBytes(ByteDataToStore, 702)";
_eeprom->WriteBytes(_bytedatatostore,(UInt) (702));
 //BA.debugLineNum = 183;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
