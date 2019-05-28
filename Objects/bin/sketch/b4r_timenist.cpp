#include "B4RDefines.h"

B4R::Timer* b4r_timenist::_tmr;
B4R::WiFiSocket* b4r_timenist::_socket;
ULong b4r_timenist::_lastmillis;
B4R::AsyncStreams* b4r_timenist::_astream;
B4R::Array* b4r_timenist::_date;
B4R::ByteConverter* b4r_timenist::_bc;
ULong b4r_timenist::_seconds;
bool b4r_timenist::_firsttime;
b4r_main* b4r_timenist::_main;
static B4R::Timer be_gann1_3;
static B4R::WiFiSocket be_gann2_3;
static B4R::AsyncStreams be_gann4_3;
static Byte be_gann5_4e1[8];
static B4R::Array be_gann5_4e2;
static B4R::ByteConverter be_gann6_3;


 void b4r_timenist::_astream_error(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 81;BA.debugLine="Private Sub AStream_Error";
 //BA.debugLineNum = 83;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_timenist::_astream_newdata(B4R::Array* _buffer){
const UInt cp = B4R::StackMemory::cp;
Byte _i = 0;
B4R::Array* _s = NULL;
B4R::B4RString be_ann29_13;
UInt _multi = 0;
B4R::Array* _s2 = NULL;
B4R::B4RString be_ann36_13;
 //BA.debugLineNum = 35;BA.debugLine="Private Sub AStream_NewData (Buffer() As Byte)";
 //BA.debugLineNum = 37;BA.debugLine="Dim i As Byte = 0";
_i = (Byte) (0);
 //BA.debugLineNum = 38;BA.debugLine="For Each s() As Byte In bc.Split(Buffer, \" \")";
B4R::Iterator* group2 = b4r_timenist::_bc->Split(_buffer,(be_ann29_13.wrap(" "))->GetBytes());
while (group2->MoveNext()) {
_s = (B4R::Array*)B4R::Object::toPointer(group2->Get());
 //BA.debugLineNum = 39;BA.debugLine="Select i";
switch ((Int) (_i)) {
case 1: {
 //BA.debugLineNum = 41;BA.debugLine="bc.ArrayCopy(s, date)";
b4r_timenist::_bc->ArrayCopy(_s,b4r_timenist::_date);
 break; }
case 2: {
 //BA.debugLineNum = 43;BA.debugLine="Dim multi As UInt = 3600";
_multi = (UInt) (3600);
 //BA.debugLineNum = 44;BA.debugLine="seconds = 0";
b4r_timenist::_seconds = (ULong) (0);
 //BA.debugLineNum = 45;BA.debugLine="For Each s2() As Byte In bc.Split(s, \":\")";
B4R::Iterator* group9 = b4r_timenist::_bc->Split(_s,(be_ann36_13.wrap(":"))->GetBytes());
while (group9->MoveNext()) {
_s2 = (B4R::Array*)B4R::Object::toPointer(group9->Get());
 //BA.debugLineNum = 46;BA.debugLine="seconds = seconds + multi * bc.StringFromByte";
b4r_timenist::_seconds = (ULong) (b4r_timenist::_seconds+_multi*(Double)(atof(b4r_timenist::_bc->StringFromBytes(_s2)->data)));
 //BA.debugLineNum = 47;BA.debugLine="multi = multi / 60";
_multi = (UInt) (_multi/(Double)60);
 }
;
 break; }
}
;
 //BA.debugLineNum = 50;BA.debugLine="i = i + 1";
_i = (Byte) (_i+1);
 //BA.debugLineNum = 51;BA.debugLine="lastMillis = Millis";
b4r_timenist::_lastmillis = Common_Millis();
 }
;
 //BA.debugLineNum = 53;BA.debugLine="socket.Close";
b4r_timenist::_socket->Close();
 //BA.debugLineNum = 54;BA.debugLine="If firstTime Then";
if (b4r_timenist::_firsttime) { 
 //BA.debugLineNum = 55;BA.debugLine="Main.TimeIsAvailable";
b4r_timenist::_main->_timeisavailable /*void*/ ();
 //BA.debugLineNum = 56;BA.debugLine="firstTime = False";
b4r_timenist::_firsttime = Common_False;
 };
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_timenist::_connect(Byte _u){
const UInt cp = B4R::StackMemory::cp;
B4R::B4RString be_ann20_5;
 //BA.debugLineNum = 24;BA.debugLine="Private Sub Connect(u As Byte)";
 //BA.debugLineNum = 25;BA.debugLine="Log(\"Connecting to NIST server...\")";
B4R::Common::LogHelper(1,102,F("Connecting to NIST server..."));
 //BA.debugLineNum = 26;BA.debugLine="If socket.ConnectHost(\"time.nist.gov\", 13) Then";
if (b4r_timenist::_socket->ConnectHost(be_ann20_5.wrap("time.nist.gov"),(UInt) (13))) { 
 //BA.debugLineNum = 27;BA.debugLine="astream.Initialize(socket.Stream, \"astream_NewDa";
b4r_timenist::_astream->Initialize(b4r_timenist::_socket->getStream(),_astream_newdata,_astream_error);
 }else {
 //BA.debugLineNum = 29;BA.debugLine="Log(\"Failed to connect to NIST server\")";
B4R::Common::LogHelper(1,102,F("Failed to connect to NIST server"));
 //BA.debugLineNum = 30;BA.debugLine="CallSubPlus(\"Connect\", 1000, 0)";
B4R::__c->CallSubPlus(_connect,(ULong) (1000),(Byte) (0));
 };
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
B4R::Array* b4r_timenist::_getdate(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 60;BA.debugLine="Public Sub GetDate As Byte()";
 //BA.debugLineNum = 61;BA.debugLine="Return date";
B4R::StackMemory::cp = cp;
B4R::Array* res1 = B4R::StackMemory::ReturnArrayOnStack(b4r_timenist::_date, sizeof(Byte));
if (true) return res1;
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
return NULL;
}
UInt b4r_timenist::_gethours(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 68;BA.debugLine="Public Sub GetHours As UInt";
 //BA.debugLineNum = 69;BA.debugLine="Return Floor(GetUpdatedTotalSeconds/ 3600)";
B4R::StackMemory::cp = cp;
UInt res1 = (UInt) (Common_Floor(_getupdatedtotalseconds()/(Double)3600));
if (true) return res1;
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
return 0;
}
UInt b4r_timenist::_getminutes(){
const UInt cp = B4R::StackMemory::cp;
Int _minutes = 0;
 //BA.debugLineNum = 72;BA.debugLine="Public Sub GetMinutes As UInt";
 //BA.debugLineNum = 73;BA.debugLine="Dim minutes As Int = Floor(GetUpdatedTotalSeconds";
_minutes = (Int) (Common_Floor(_getupdatedtotalseconds()/(Double)60));
 //BA.debugLineNum = 74;BA.debugLine="Return minutes Mod 60";
B4R::StackMemory::cp = cp;
UInt res2 = (UInt) (_minutes%60);
if (true) return res2;
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
return 0;
}
UInt b4r_timenist::_getseconds(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 77;BA.debugLine="Public Sub GetSeconds As UInt";
 //BA.debugLineNum = 78;BA.debugLine="Return GetUpdatedTotalSeconds Mod 60";
B4R::StackMemory::cp = cp;
UInt res1 = (UInt) (_getupdatedtotalseconds()%60);
if (true) return res1;
 //BA.debugLineNum = 79;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
return 0;
}
ULong b4r_timenist::_getupdatedtotalseconds(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 64;BA.debugLine="Private Sub GetUpdatedTotalSeconds As ULong";
 //BA.debugLineNum = 65;BA.debugLine="Return seconds + (Millis - lastMillis) / 1000";
B4R::StackMemory::cp = cp;
ULong res1 = (ULong) (b4r_timenist::_seconds+(Common_Millis()-b4r_timenist::_lastmillis)/(Double)1000);
if (true) return res1;
 //BA.debugLineNum = 66;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
return 0L;
}
void b4r_timenist::_process_globals(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 3;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 4;BA.debugLine="Private tmr As Timer";
b4r_timenist::_tmr = &be_gann1_3;
 //BA.debugLineNum = 5;BA.debugLine="Private socket As WiFiSocket";
b4r_timenist::_socket = &be_gann2_3;
 //BA.debugLineNum = 6;BA.debugLine="Private lastMillis As ULong";
b4r_timenist::_lastmillis = 0L;
 //BA.debugLineNum = 7;BA.debugLine="Private astream As AsyncStreams";
b4r_timenist::_astream = &be_gann4_3;
 //BA.debugLineNum = 8;BA.debugLine="Private date(8) As Byte";
b4r_timenist::_date =be_gann5_4e2.wrap(be_gann5_4e1,8);
 //BA.debugLineNum = 9;BA.debugLine="Private bc As ByteConverter";
b4r_timenist::_bc = &be_gann6_3;
 //BA.debugLineNum = 10;BA.debugLine="Private seconds As ULong";
b4r_timenist::_seconds = 0L;
 //BA.debugLineNum = 11;BA.debugLine="Private firstTime As Boolean = True";
b4r_timenist::_firsttime = Common_True;
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
}
void b4r_timenist::_start(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 14;BA.debugLine="Public Sub Start";
 //BA.debugLineNum = 15;BA.debugLine="tmr.Initialize(\"tmr_Tick\", 60 * 10000) 'check wit";
b4r_timenist::_tmr->Initialize(_tmr_tick,(ULong) (60*10000));
 //BA.debugLineNum = 16;BA.debugLine="tmr.Enabled = True";
b4r_timenist::_tmr->setEnabled(Common_True);
 //BA.debugLineNum = 17;BA.debugLine="tmr_Tick";
_tmr_tick();
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
void b4r_timenist::_tmr_tick(){
const UInt cp = B4R::StackMemory::cp;
 //BA.debugLineNum = 20;BA.debugLine="Private Sub tmr_Tick";
 //BA.debugLineNum = 21;BA.debugLine="Connect(0)";
_connect((Byte) (0));
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
B4R::StackMemory::cp = cp;
}
