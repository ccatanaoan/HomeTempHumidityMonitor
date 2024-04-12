package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xbytesbuilder extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.b4xbytesbuilder");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.b4xbytesbuilder.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public byte[] _mbuffer = null;
public int _mlength = 0;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.chart _chart = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.b4xcollections _b4xcollections = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public cloyd.smart.home.monitor.b4xbytesbuilder  _append(byte[] _data) throws Exception{
int _oldlength = 0;
 //BA.debugLineNum = 24;BA.debugLine="Public Sub Append(Data() As Byte) As B4XBytesBuild";
 //BA.debugLineNum = 25;BA.debugLine="Dim OldLength As Int = ChangeLength(mLength + Dat";
_oldlength = _changelength((int) (_mlength+_data.length));
 //BA.debugLineNum = 26;BA.debugLine="Bit.ArrayCopy(Data, 0, mBuffer, OldLength, Data.L";
__c.Bit.ArrayCopy((Object)(_data),(int) (0),(Object)(_mbuffer),_oldlength,_data.length);
 //BA.debugLineNum = 27;BA.debugLine="Return Me";
if (true) return (cloyd.smart.home.monitor.b4xbytesbuilder)(this);
 //BA.debugLineNum = 28;BA.debugLine="End Sub";
return null;
}
public int  _changelength(int _newlength) throws Exception{
int _oldlength = 0;
byte[] _b = null;
 //BA.debugLineNum = 12;BA.debugLine="Private Sub ChangeLength (NewLength As Int) As Int";
 //BA.debugLineNum = 13;BA.debugLine="Dim OldLength As Int = mLength";
_oldlength = _mlength;
 //BA.debugLineNum = 14;BA.debugLine="mLength = NewLength";
_mlength = _newlength;
 //BA.debugLineNum = 15;BA.debugLine="If mBuffer.Length < mLength Then";
if (_mbuffer.length<_mlength) { 
 //BA.debugLineNum = 16;BA.debugLine="Dim b(Max(mBuffer.Length * 2, NewLength)) As Byt";
_b = new byte[(int) (__c.Max(_mbuffer.length*2,_newlength))];
;
 //BA.debugLineNum = 17;BA.debugLine="Bit.ArrayCopy(mBuffer, 0, b, 0, mBuffer.Length)";
__c.Bit.ArrayCopy((Object)(_mbuffer),(int) (0),(Object)(_b),(int) (0),_mbuffer.length);
 //BA.debugLineNum = 18;BA.debugLine="mBuffer = b";
_mbuffer = _b;
 };
 //BA.debugLineNum = 20;BA.debugLine="Return OldLength";
if (true) return _oldlength;
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return 0;
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 1;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 2;BA.debugLine="Private mBuffer() As Byte";
_mbuffer = new byte[(int) (0)];
;
 //BA.debugLineNum = 3;BA.debugLine="Private mLength As Int";
_mlength = 0;
 //BA.debugLineNum = 4;BA.debugLine="End Sub";
return "";
}
public String  _clear() throws Exception{
 //BA.debugLineNum = 30;BA.debugLine="Public Sub Clear";
 //BA.debugLineNum = 31;BA.debugLine="ChangeLength(0)";
_changelength((int) (0));
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return "";
}
public byte[]  _getbuffer() throws Exception{
 //BA.debugLineNum = 96;BA.debugLine="Private Sub getBuffer As Byte() 'ignore";
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return null;
}
public int  _getlength() throws Exception{
 //BA.debugLineNum = 85;BA.debugLine="Public Sub getLength As Int";
 //BA.debugLineNum = 86;BA.debugLine="Return mLength";
if (true) return _mlength;
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return 0;
}
public int  _indexof(byte[] _searchfor) throws Exception{
 //BA.debugLineNum = 100;BA.debugLine="Public Sub IndexOf(SearchFor() As Byte) As Int";
 //BA.debugLineNum = 101;BA.debugLine="Return IndexOf2(SearchFor, 0)";
if (true) return _indexof2(_searchfor,(int) (0));
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
return 0;
}
public int  _indexof2(byte[] _searchfor,int _index) throws Exception{
int _i1 = 0;
int _i2 = 0;
 //BA.debugLineNum = 105;BA.debugLine="Public Sub IndexOf2(SearchFor() As Byte, Index As";
 //BA.debugLineNum = 106;BA.debugLine="For i1 = Index To mLength - SearchFor.Length";
{
final int step1 = 1;
final int limit1 = (int) (_mlength-_searchfor.length);
_i1 = _index ;
for (;_i1 <= limit1 ;_i1 = _i1 + step1 ) {
 //BA.debugLineNum = 107;BA.debugLine="For i2 = 0 To SearchFor.Length - 1";
{
final int step2 = 1;
final int limit2 = (int) (_searchfor.length-1);
_i2 = (int) (0) ;
for (;_i2 <= limit2 ;_i2 = _i2 + step2 ) {
 //BA.debugLineNum = 108;BA.debugLine="If SearchFor(i2) <> mBuffer(i1 + i2) Then";
if (_searchfor[_i2]!=_mbuffer[(int) (_i1+_i2)]) { 
 //BA.debugLineNum = 109;BA.debugLine="Exit";
if (true) break;
 };
 }
};
 //BA.debugLineNum = 112;BA.debugLine="If i2 = SearchFor.Length Then";
if (_i2==_searchfor.length) { 
 //BA.debugLineNum = 113;BA.debugLine="Return i1";
if (true) return _i1;
 };
 }
};
 //BA.debugLineNum = 116;BA.debugLine="Return -1";
if (true) return (int) (-1);
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return 0;
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 6;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 7;BA.debugLine="Dim mBuffer(100) As Byte";
_mbuffer = new byte[(int) (100)];
;
 //BA.debugLineNum = 8;BA.debugLine="mLength = 0";
_mlength = (int) (0);
 //BA.debugLineNum = 9;BA.debugLine="End Sub";
return "";
}
public String  _insert(int _index,byte[] _data) throws Exception{
byte[] _afterindex = null;
 //BA.debugLineNum = 36;BA.debugLine="Public Sub Insert(Index As Int, Data() As Byte)";
 //BA.debugLineNum = 37;BA.debugLine="If Index >= mLength Then";
if (_index>=_mlength) { 
 //BA.debugLineNum = 38;BA.debugLine="If Index > mLength Then Log(\"Index too large\")";
if (_index>_mlength) { 
__c.LogImpl("911796482","Index too large",0);};
 //BA.debugLineNum = 39;BA.debugLine="Append(Data)";
_append(_data);
 }else {
 //BA.debugLineNum = 41;BA.debugLine="Dim AfterIndex() As Byte = SubArray(Index)";
_afterindex = _subarray(_index);
 //BA.debugLineNum = 42;BA.debugLine="ChangeLength(mLength + Data.Length)";
_changelength((int) (_mlength+_data.length));
 //BA.debugLineNum = 43;BA.debugLine="Bit.ArrayCopy(Data, 0, mBuffer, Index, Data.Leng";
__c.Bit.ArrayCopy((Object)(_data),(int) (0),(Object)(_mbuffer),_index,_data.length);
 //BA.debugLineNum = 44;BA.debugLine="Bit.ArrayCopy(AfterIndex, 0, mBuffer, Index + Da";
__c.Bit.ArrayCopy((Object)(_afterindex),(int) (0),(Object)(_mbuffer),(int) (_index+_data.length),_afterindex.length);
 };
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public byte[]  _remove(int _beginindex,int _endindex) throws Exception{
byte[] _res = null;
byte[] _afterendindex = null;
 //BA.debugLineNum = 62;BA.debugLine="Public Sub Remove(BeginIndex As Int, EndIndex As I";
 //BA.debugLineNum = 63;BA.debugLine="Dim res() As Byte = SubArray2(BeginIndex, EndInde";
_res = _subarray2(_beginindex,_endindex);
 //BA.debugLineNum = 64;BA.debugLine="If EndIndex <= mLength Then";
if (_endindex<=_mlength) { 
 //BA.debugLineNum = 65;BA.debugLine="Dim AfterEndIndex() As Byte = SubArray(EndIndex)";
_afterendindex = _subarray(_endindex);
 //BA.debugLineNum = 66;BA.debugLine="Bit.ArrayCopy(AfterEndIndex, 0, mBuffer, BeginIn";
__c.Bit.ArrayCopy((Object)(_afterendindex),(int) (0),(Object)(_mbuffer),_beginindex,_afterendindex.length);
 };
 //BA.debugLineNum = 68;BA.debugLine="ChangeLength(mLength - (EndIndex - BeginIndex))";
_changelength((int) (_mlength-(_endindex-_beginindex)));
 //BA.debugLineNum = 69;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return null;
}
public String  _set(int _index,byte[] _data) throws Exception{
 //BA.debugLineNum = 49;BA.debugLine="Public Sub Set(Index As Int, Data() As Byte)";
 //BA.debugLineNum = 50;BA.debugLine="If Index >= mLength Then";
if (_index>=_mlength) { 
 //BA.debugLineNum = 51;BA.debugLine="If Index > mLength Then Log(\"Index too large\")";
if (_index>_mlength) { 
__c.LogImpl("911862018","Index too large",0);};
 //BA.debugLineNum = 52;BA.debugLine="Append(Data)";
_append(_data);
 }else {
 //BA.debugLineNum = 54;BA.debugLine="If Data.Length + Index > mLength Then";
if (_data.length+_index>_mlength) { 
 //BA.debugLineNum = 55;BA.debugLine="ChangeLength(Data.Length + Index)";
_changelength((int) (_data.length+_index));
 };
 //BA.debugLineNum = 57;BA.debugLine="Bit.ArrayCopy(Data, 0, mBuffer, Index, Data.Leng";
__c.Bit.ArrayCopy((Object)(_data),(int) (0),(Object)(_mbuffer),_index,_data.length);
 };
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public byte[]  _subarray(int _beginindex) throws Exception{
 //BA.debugLineNum = 73;BA.debugLine="Public Sub SubArray(BeginIndex As Int) As Byte()";
 //BA.debugLineNum = 74;BA.debugLine="Return SubArray2(BeginIndex, mLength)";
if (true) return _subarray2(_beginindex,_mlength);
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return null;
}
public byte[]  _subarray2(int _beginindex,int _endindex) throws Exception{
byte[] _b = null;
 //BA.debugLineNum = 79;BA.debugLine="Public Sub SubArray2(BeginIndex As Int, EndIndex A";
 //BA.debugLineNum = 80;BA.debugLine="Dim b(EndIndex - BeginIndex) As Byte";
_b = new byte[(int) (_endindex-_beginindex)];
;
 //BA.debugLineNum = 81;BA.debugLine="Bit.ArrayCopy(mBuffer, BeginIndex, b, 0, b.Length";
__c.Bit.ArrayCopy((Object)(_mbuffer),_beginindex,(Object)(_b),(int) (0),_b.length);
 //BA.debugLineNum = 82;BA.debugLine="Return b";
if (true) return _b;
 //BA.debugLineNum = 83;BA.debugLine="End Sub";
return null;
}
public byte[]  _toarray() throws Exception{
 //BA.debugLineNum = 90;BA.debugLine="Public Sub ToArray() As Byte()";
 //BA.debugLineNum = 91;BA.debugLine="Return SubArray(0)";
if (true) return _subarray((int) (0));
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return null;
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
