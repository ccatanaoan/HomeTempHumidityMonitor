package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class mjpeg extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.mjpeg");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.mjpeg.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.SocketWrapper _sock = null;
public anywheresoftware.b4a.randomaccessfile.AsyncStreams _astream = null;
public Object _mcallback = null;
public String _meventname = "";
public String _mhost = "";
public String _mpath = "";
public byte[] _data = null;
public int _index = 0;
public anywheresoftware.b4a.agraham.byteconverter.ByteConverter _bc = null;
public String _boundary = "";
public boolean _iserror = false;
public String _streamerror = "";
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.chart _chart = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.b4xcollections _b4xcollections = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public String  _astream_error() throws Exception{
String _errormessage = "";
 //BA.debugLineNum = 145;BA.debugLine="Private Sub AStream_Error";
 //BA.debugLineNum = 146;BA.debugLine="Try";
try { //BA.debugLineNum = 147;BA.debugLine="Dim errorMessage As String = \"\"";
_errormessage = "";
 //BA.debugLineNum = 148;BA.debugLine="If LastException.IsInitialized Then";
if (__c.LastException(getActivityBA()).IsInitialized()) { 
 //BA.debugLineNum = 149;BA.debugLine="errorMessage = \"Connection error: \" & LastExcep";
_errormessage = "Connection error: "+__c.LastException(getActivityBA()).getMessage();
 };
 //BA.debugLineNum = 151;BA.debugLine="If errorMessage.Trim.Length = 0 Then";
if (_errormessage.trim().length()==0) { 
 //BA.debugLineNum = 152;BA.debugLine="errorMessage = \"Connection error\"";
_errormessage = "Connection error";
 };
 //BA.debugLineNum = 154;BA.debugLine="CallSub2(mCallback, mEventName & \"_ConnectionErr";
__c.CallSubNew2(ba,_mcallback,_meventname+"_ConnectionError",(Object)(_errormessage));
 } 
       catch (Exception e11) {
			ba.setLastException(e11); //BA.debugLineNum = 156;BA.debugLine="Log(LastException)";
__c.LogImpl("031719435",BA.ObjectToString(__c.LastException(getActivityBA())),0);
 };
 //BA.debugLineNum = 158;BA.debugLine="End Sub";
return "";
}
public String  _astream_newdata(byte[] _buffer) throws Exception{
int _i1 = 0;
int _i2 = 0;
String _ct = "";
int _b = 0;
int _b1 = 0;
int _b2 = 0;
int _startframe = 0;
int _endframe = 0;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _in = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
 //BA.debugLineNum = 68;BA.debugLine="Private Sub AStream_NewData (Buffer() As Byte)";
 //BA.debugLineNum = 69;BA.debugLine="Try";
try { //BA.debugLineNum = 70;BA.debugLine="bc.ArrayCopy(Buffer, 0, Data, index, Buffer.Leng";
_bc.ArrayCopy((Object)(_buffer),(int) (0),(Object)(_data),_index,_buffer.length);
 //BA.debugLineNum = 71;BA.debugLine="index = index + Buffer.Length";
_index = (int) (_index+_buffer.length);
 //BA.debugLineNum = 72;BA.debugLine="If boundary = \"\" Then";
if ((_boundary).equals("")) { 
 //BA.debugLineNum = 73;BA.debugLine="Dim i1 As Int = IndexOfString(\"Content-Type\", 0";
_i1 = _indexofstring("Content-Type",(int) (0));
 //BA.debugLineNum = 74;BA.debugLine="If i1 > -1 Then";
if (_i1>-1) { 
 //BA.debugLineNum = 75;BA.debugLine="Dim i2 As Int = IndexOfString(CRLF, i1 + 1)";
_i2 = _indexofstring(__c.CRLF,(int) (_i1+1));
 //BA.debugLineNum = 76;BA.debugLine="If i2 > -1 Then";
if (_i2>-1) { 
 //BA.debugLineNum = 77;BA.debugLine="Dim ct As String = BytesToString(Data, i1, i2";
_ct = __c.BytesToString(_data,_i1,(int) (_i2-_i1),"ASCII");
 //BA.debugLineNum = 78;BA.debugLine="Dim b As Int = ct.IndexOf(\"=\")";
_b = _ct.indexOf("=");
 //BA.debugLineNum = 79;BA.debugLine="boundary = ct.SubString(b + 1)";
_boundary = _ct.substring((int) (_b+1));
 };
 };
 //BA.debugLineNum = 82;BA.debugLine="Dim i1 As Int = IndexOfString(\"HTTP/1.1\", 0)";
_i1 = _indexofstring("HTTP/1.1",(int) (0));
 //BA.debugLineNum = 83;BA.debugLine="If i1 > -1 Then";
if (_i1>-1) { 
 //BA.debugLineNum = 84;BA.debugLine="Dim i2 As Int = IndexOfString(CRLF, i1 + 1)";
_i2 = _indexofstring(__c.CRLF,(int) (_i1+1));
 //BA.debugLineNum = 85;BA.debugLine="If i2 > -1 Then";
if (_i2>-1) { 
 //BA.debugLineNum = 86;BA.debugLine="Dim ct As String = BytesToString(Data, i1, i2";
_ct = __c.BytesToString(_data,_i1,(int) (_i2-_i1),"ASCII");
 //BA.debugLineNum = 87;BA.debugLine="Dim b As Int = ct.IndexOf(\"=\")";
_b = _ct.indexOf("=");
 //BA.debugLineNum = 88;BA.debugLine="streamError = ct.SubString(b + 1)";
_streamerror = _ct.substring((int) (_b+1));
 //BA.debugLineNum = 89;BA.debugLine="isError = streamError.StartsWith(\"HTTP/1.1 2\"";
_iserror = _streamerror.startsWith("HTTP/1.1 2")==__c.False;
 };
 };
 }else {
 //BA.debugLineNum = 93;BA.debugLine="If isError Then";
if (_iserror) { 
 //BA.debugLineNum = 94;BA.debugLine="streamError = BytesToString(Buffer, 0, Buffer.";
_streamerror = __c.BytesToString(_buffer,(int) (0),_buffer.length,"utf8");
 //BA.debugLineNum = 95;BA.debugLine="CallSub2(mCallback, mEventName & \"_StreamError";
__c.CallSubNew2(ba,_mcallback,_meventname+"_StreamError",(Object)(_streamerror));
 //BA.debugLineNum = 96;BA.debugLine="Astream.Close";
_astream.Close();
 //BA.debugLineNum = 97;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 99;BA.debugLine="Dim b1 As Int = IndexOfString(boundary, 0)";
_b1 = _indexofstring(_boundary,(int) (0));
 //BA.debugLineNum = 100;BA.debugLine="If b1 > -1 Then";
if (_b1>-1) { 
 //BA.debugLineNum = 101;BA.debugLine="Dim b2 As Int = IndexOfString(boundary, b1 + 1";
_b2 = _indexofstring(_boundary,(int) (_b1+1));
 //BA.debugLineNum = 102;BA.debugLine="If b2 > -1 Then";
if (_b2>-1) { 
 //BA.debugLineNum = 103;BA.debugLine="Dim startframe As Int = IndexOf(Array As Byte";
_startframe = _indexof(new byte[]{(byte) (0xff),(byte) (0xd8)},_b1);
 //BA.debugLineNum = 104;BA.debugLine="Dim endframe As Int = IndexOf(Array As Byte(0";
_endframe = _indexof(new byte[]{(byte) (0xff),(byte) (0xd9)},(int) (_b2-10));
 //BA.debugLineNum = 105;BA.debugLine="If startframe > -1 And endframe > -1 Then";
if (_startframe>-1 && _endframe>-1) { 
 //BA.debugLineNum = 106;BA.debugLine="Dim In As InputStream";
_in = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 107;BA.debugLine="In.InitializeFromBytesArray(Data, startframe";
_in.InitializeFromBytesArray(_data,_startframe,(int) (_endframe-_startframe+2));
 //BA.debugLineNum = 111;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 113;BA.debugLine="bmp.Initialize2(In)";
_bmp.Initialize2((java.io.InputStream)(_in.getObject()));
 //BA.debugLineNum = 114;BA.debugLine="CallSub2(mCallback, mEventName & \"_frame\", b";
__c.CallSubNew2(ba,_mcallback,_meventname+"_frame",(Object)(_bmp));
 };
 //BA.debugLineNum = 116;BA.debugLine="TrimArray(b2)";
_trimarray(_b2);
 };
 };
 };
 } 
       catch (Exception e49) {
			ba.setLastException(e49); //BA.debugLineNum = 122;BA.debugLine="Log(LastException)";
__c.LogImpl("031457334",BA.ObjectToString(__c.LastException(getActivityBA())),0);
 };
 //BA.debugLineNum = 124;BA.debugLine="End Sub";
return "";
}
public String  _astream_terminated() throws Exception{
String _errormessage = "";
 //BA.debugLineNum = 160;BA.debugLine="Private Sub Astream_Terminated";
 //BA.debugLineNum = 161;BA.debugLine="Try";
try { //BA.debugLineNum = 162;BA.debugLine="Dim errorMessage As String = \"\"";
_errormessage = "";
 //BA.debugLineNum = 163;BA.debugLine="If LastException.IsInitialized Then";
if (__c.LastException(getActivityBA()).IsInitialized()) { 
 //BA.debugLineNum = 164;BA.debugLine="If LastException.Message.Contains(\"Object shoul";
if (__c.LastException(getActivityBA()).getMessage().contains("Object should first be initialized")==__c.False) { 
 //BA.debugLineNum = 165;BA.debugLine="errorMessage = \"Terminated: \" & LastException.";
_errormessage = "Terminated: "+__c.LastException(getActivityBA()).getMessage();
 };
 };
 //BA.debugLineNum = 168;BA.debugLine="If errorMessage.Trim.Length = 0 Or errorMessage.";
if (_errormessage.trim().length()==0 || _errormessage.contains("to read from field")) { 
 //BA.debugLineNum = 169;BA.debugLine="errorMessage = \"Terminated\"";
_errormessage = "Terminated";
 };
 //BA.debugLineNum = 171;BA.debugLine="CallSub2(mCallback, mEventName & \"_Terminated\",";
__c.CallSubNew2(ba,_mcallback,_meventname+"_Terminated",(Object)(_errormessage));
 } 
       catch (Exception e13) {
			ba.setLastException(e13); //BA.debugLineNum = 173;BA.debugLine="Log(LastException)";
__c.LogImpl("031784973",BA.ObjectToString(__c.LastException(getActivityBA())),0);
 };
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 1;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 2;BA.debugLine="Private sock As Socket";
_sock = new anywheresoftware.b4a.objects.SocketWrapper();
 //BA.debugLineNum = 3;BA.debugLine="Private Astream As AsyncStreams";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 4;BA.debugLine="Private mCallback As Object";
_mcallback = new Object();
 //BA.debugLineNum = 5;BA.debugLine="Private mEventName As String";
_meventname = "";
 //BA.debugLineNum = 6;BA.debugLine="Private mHost As String";
_mhost = "";
 //BA.debugLineNum = 7;BA.debugLine="Private mPath As String";
_mpath = "";
 //BA.debugLineNum = 8;BA.debugLine="Private Data(1000000) As Byte";
_data = new byte[(int) (1000000)];
;
 //BA.debugLineNum = 9;BA.debugLine="Private index As Int";
_index = 0;
 //BA.debugLineNum = 10;BA.debugLine="Private bc As ByteConverter";
_bc = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 11;BA.debugLine="Private boundary As String";
_boundary = "";
 //BA.debugLineNum = 12;BA.debugLine="Private isError As Boolean";
_iserror = false;
 //BA.debugLineNum = 13;BA.debugLine="Private streamError As String";
_streamerror = "";
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public String  _connect(String _url,int _port) throws Exception{
int _i = 0;
String _errormessage = "";
 //BA.debugLineNum = 21;BA.debugLine="Public Sub Connect(url As String, Port As Int)";
 //BA.debugLineNum = 22;BA.debugLine="Try";
try { //BA.debugLineNum = 23;BA.debugLine="If Astream.IsInitialized Then Astream.Close";
if (_astream.IsInitialized()) { 
_astream.Close();};
 //BA.debugLineNum = 24;BA.debugLine="Dim i As Int = url.IndexOf(\"/\")";
_i = _url.indexOf("/");
 //BA.debugLineNum = 25;BA.debugLine="mPath = url.SubString(i)";
_mpath = _url.substring(_i);
 //BA.debugLineNum = 26;BA.debugLine="mHost = url.SubString2(0, i)";
_mhost = _url.substring((int) (0),_i);
 //BA.debugLineNum = 27;BA.debugLine="sock.Initialize(\"sock\")";
_sock.Initialize("sock");
 //BA.debugLineNum = 28;BA.debugLine="sock.Connect(mHost, Port, 10000)";
_sock.Connect(ba,_mhost,_port,(int) (10000));
 } 
       catch (Exception e9) {
			ba.setLastException(e9); //BA.debugLineNum = 30;BA.debugLine="Dim errorMessage As String = \"\"";
_errormessage = "";
 //BA.debugLineNum = 31;BA.debugLine="If LastException.IsInitialized Then";
if (__c.LastException(getActivityBA()).IsInitialized()) { 
 //BA.debugLineNum = 32;BA.debugLine="errorMessage = \"Connection error: \" & LastExcep";
_errormessage = "Connection error: "+__c.LastException(getActivityBA()).getMessage();
 };
 //BA.debugLineNum = 34;BA.debugLine="If errorMessage.Trim.Length = 0 Then";
if (_errormessage.trim().length()==0) { 
 //BA.debugLineNum = 35;BA.debugLine="errorMessage = \"Connection error\"";
_errormessage = "Connection error";
 };
 //BA.debugLineNum = 37;BA.debugLine="CallSub2(mCallback, mEventName & \"_ConnectionErr";
__c.CallSubNew2(ba,_mcallback,_meventname+"_ConnectionError",(Object)(_errormessage));
 };
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public String  _disconnect() throws Exception{
 //BA.debugLineNum = 41;BA.debugLine="Public Sub Disconnect()";
 //BA.debugLineNum = 42;BA.debugLine="If Astream.IsInitialized Then Astream.Close";
if (_astream.IsInitialized()) { 
_astream.Close();};
 //BA.debugLineNum = 43;BA.debugLine="CallSub(mCallback, mEventName & \"_Disconnected\")";
__c.CallSubNew(ba,_mcallback,_meventname+"_Disconnected");
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public int  _indexof(byte[] _bs,int _start) throws Exception{
int _i = 0;
int _b = 0;
 //BA.debugLineNum = 135;BA.debugLine="Private Sub IndexOf(bs() As Byte, Start As Int) As";
 //BA.debugLineNum = 136;BA.debugLine="For i = Start To index - 1 - bs.Length";
{
final int step1 = 1;
final int limit1 = (int) (_index-1-_bs.length);
_i = _start ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 137;BA.debugLine="For b = 0 To bs.Length - 1";
{
final int step2 = 1;
final int limit2 = (int) (_bs.length-1);
_b = (int) (0) ;
for (;_b <= limit2 ;_b = _b + step2 ) {
 //BA.debugLineNum = 138;BA.debugLine="If bs(b) <> Data(i + b) Then Exit";
if (_bs[_b]!=_data[(int) (_i+_b)]) { 
if (true) break;};
 }
};
 //BA.debugLineNum = 140;BA.debugLine="If b = bs.Length Then Return i";
if (_b==_bs.length) { 
if (true) return _i;};
 }
};
 //BA.debugLineNum = 142;BA.debugLine="Return -1";
if (true) return (int) (-1);
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return 0;
}
public int  _indexofstring(String _s,int _start) throws Exception{
 //BA.debugLineNum = 131;BA.debugLine="Private Sub IndexOfString(s As String, Start As In";
 //BA.debugLineNum = 132;BA.debugLine="Return IndexOf(s.GetBytes(\"ASCII\"), Start)";
if (true) return _indexof(_s.getBytes("ASCII"),_start);
 //BA.debugLineNum = 133;BA.debugLine="End Sub";
return 0;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,Object _callback,String _eventname) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 16;BA.debugLine="Public Sub Initialize (Callback As Object, EventNa";
 //BA.debugLineNum = 17;BA.debugLine="mCallback = Callback";
_mcallback = _callback;
 //BA.debugLineNum = 18;BA.debugLine="mEventName = EventName";
_meventname = _eventname;
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public String  _sock_connected(boolean _successful) throws Exception{
String _stmp = "";
String _errormessage = "";
 //BA.debugLineNum = 46;BA.debugLine="Private Sub Sock_Connected (Successful As Boolean)";
 //BA.debugLineNum = 47;BA.debugLine="If Successful Then";
if (_successful) { 
 //BA.debugLineNum = 48;BA.debugLine="boundary = \"\"";
_boundary = "";
 //BA.debugLineNum = 49;BA.debugLine="Astream.Initialize(sock.InputStream, sock.Output";
_astream.Initialize(ba,_sock.getInputStream(),_sock.getOutputStream(),"astream");
 //BA.debugLineNum = 50;BA.debugLine="Dim sTmp As String = $\"GET ${mPath} HTTP/1.1 Hos";
_stmp = ("GET "+__c.SmartStringFormatter("",(Object)(_mpath))+" HTTP/1.1\n"+"Host: "+__c.SmartStringFormatter("",(Object)(_mhost))+"\n"+"Connection: keep-alive\n"+"\n"+"");
 //BA.debugLineNum = 55;BA.debugLine="Astream.Write(sTmp.Replace(Chr(10), Chr(13) & Ch";
_astream.Write(_stmp.replace(BA.ObjectToString(__c.Chr((int) (10))),BA.ObjectToString(__c.Chr((int) (13)))+BA.ObjectToString(__c.Chr((int) (10)))).getBytes("UTF8"));
 }else {
 //BA.debugLineNum = 57;BA.debugLine="Dim errorMessage As String = \"\"";
_errormessage = "";
 //BA.debugLineNum = 58;BA.debugLine="If LastException.IsInitialized Then";
if (__c.LastException(getActivityBA()).IsInitialized()) { 
 //BA.debugLineNum = 59;BA.debugLine="errorMessage = \"Connection error: \" & LastExcep";
_errormessage = "Connection error: "+__c.LastException(getActivityBA()).getMessage();
 };
 //BA.debugLineNum = 61;BA.debugLine="If errorMessage.Trim.Length = 0 Then";
if (_errormessage.trim().length()==0) { 
 //BA.debugLineNum = 62;BA.debugLine="errorMessage = \"Connection error\"";
_errormessage = "Connection error";
 };
 //BA.debugLineNum = 64;BA.debugLine="CallSub2(mCallback, mEventName & \"_ConnectionErr";
__c.CallSubNew2(ba,_mcallback,_meventname+"_ConnectionError",(Object)(_errormessage));
 };
 //BA.debugLineNum = 66;BA.debugLine="End Sub";
return "";
}
public String  _trimarray(int _i) throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Private Sub TrimArray (i As Int)";
 //BA.debugLineNum = 127;BA.debugLine="bc.ArrayCopy(Data, i, Data, 0, index - i)";
_bc.ArrayCopy((Object)(_data),_i,(Object)(_data),(int) (0),(int) (_index-_i));
 //BA.debugLineNum = 128;BA.debugLine="index = index - i";
_index = (int) (_index-_i);
 //BA.debugLineNum = 129;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
