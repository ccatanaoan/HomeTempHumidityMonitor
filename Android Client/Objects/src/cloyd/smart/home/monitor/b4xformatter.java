package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xformatter extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.b4xformatter");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.b4xformatter.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.collections.List _formats = null;
public int _max_value = 0;
public int _min_value = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public static class _b4xformatdata{
public boolean IsInitialized;
public String Prefix;
public String Postfix;
public int MinimumIntegers;
public int MinimumFractions;
public int MaximumFractions;
public String GroupingCharacter;
public String DecimalPoint;
public int TextColor;
public anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont FormatFont;
public double RangeStart;
public double RangeEnd;
public boolean RemoveMinusSign;
public String IntegerPaddingChar;
public String FractionPaddingChar;
public void Initialize() {
IsInitialized = true;
Prefix = "";
Postfix = "";
MinimumIntegers = 0;
MinimumFractions = 0;
MaximumFractions = 0;
GroupingCharacter = "";
DecimalPoint = "";
TextColor = 0;
FormatFont = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont();
RangeStart = 0;
RangeEnd = 0;
RemoveMinusSign = false;
IntegerPaddingChar = "";
FractionPaddingChar = "";
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public String  _addformatdata(cloyd.smart.home.monitor.b4xformatter._b4xformatdata _data,double _rangestart,double _rangeend,boolean _includeedges) throws Exception{
double _factor = 0;
 //BA.debugLineNum = 63;BA.debugLine="Public Sub AddFormatData (Data As B4XFormatData, R";
 //BA.debugLineNum = 64;BA.debugLine="Dim factor As Double = Power(10, -Data.MaximumFra";
_factor = __c.Power(10,-_data.MaximumFractions /*int*/ );
 //BA.debugLineNum = 65;BA.debugLine="If IncludeEdges = False Then";
if (_includeedges==__c.False) { 
 //BA.debugLineNum = 66;BA.debugLine="RangeStart = RangeStart + factor";
_rangestart = _rangestart+_factor;
 //BA.debugLineNum = 67;BA.debugLine="RangeEnd = RangeEnd - factor";
_rangeend = _rangeend-_factor;
 };
 //BA.debugLineNum = 69;BA.debugLine="RangeStart = RangeStart - factor / 2";
_rangestart = _rangestart-_factor/(double)2;
 //BA.debugLineNum = 70;BA.debugLine="RangeEnd = RangeEnd + factor / 2";
_rangeend = _rangeend+_factor/(double)2;
 //BA.debugLineNum = 71;BA.debugLine="Data.RangeStart = RangeStart";
_data.RangeStart /*double*/  = _rangestart;
 //BA.debugLineNum = 72;BA.debugLine="Data.RangeEnd = RangeEnd";
_data.RangeEnd /*double*/  = _rangeend;
 //BA.debugLineNum = 73;BA.debugLine="formats.Add(Data)";
_formats.Add((Object)(_data));
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 1;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 2;BA.debugLine="Type B4XFormatData (Prefix As String, Postfix As";
;
 //BA.debugLineNum = 6;BA.debugLine="Private formats As List";
_formats = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 7;BA.debugLine="Public Const MAX_VALUE = 0x7fffffff, MIN_VALUE =";
_max_value = (int) (0x7fffffff);
_min_value = (int) (0x80000000);
 //BA.debugLineNum = 8;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 9;BA.debugLine="End Sub";
return "";
}
public cloyd.smart.home.monitor.b4xformatter._b4xformatdata  _copyformatdata(cloyd.smart.home.monitor.b4xformatter._b4xformatdata _data) throws Exception{
cloyd.smart.home.monitor.b4xformatter._b4xformatdata _d = null;
 //BA.debugLineNum = 36;BA.debugLine="Public Sub CopyFormatData (Data As B4XFormatData)";
 //BA.debugLineNum = 37;BA.debugLine="Dim d As B4XFormatData";
_d = new cloyd.smart.home.monitor.b4xformatter._b4xformatdata();
 //BA.debugLineNum = 38;BA.debugLine="d.Initialize";
_d.Initialize();
 //BA.debugLineNum = 39;BA.debugLine="d.DecimalPoint = Data.DecimalPoint";
_d.DecimalPoint /*String*/  = _data.DecimalPoint /*String*/ ;
 //BA.debugLineNum = 40;BA.debugLine="If Data.FormatFont.IsInitialized Then";
if (_data.FormatFont /*anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont*/ .getIsInitialized()) { 
 //BA.debugLineNum = 42;BA.debugLine="d.FormatFont = xui.CreateFont(Data.FormatFont.To";
_d.FormatFont /*anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont*/  = _xui.CreateFont((android.graphics.Typeface)(_data.FormatFont /*anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont*/ .ToNativeFont().getObject()),_data.FormatFont /*anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont*/ .getSize());
 };
 //BA.debugLineNum = 45;BA.debugLine="d.GroupingCharacter = Data.GroupingCharacter";
_d.GroupingCharacter /*String*/  = _data.GroupingCharacter /*String*/ ;
 //BA.debugLineNum = 46;BA.debugLine="d.MaximumFractions = Data.MaximumFractions";
_d.MaximumFractions /*int*/  = _data.MaximumFractions /*int*/ ;
 //BA.debugLineNum = 47;BA.debugLine="d.MinimumFractions = Data.MinimumFractions";
_d.MinimumFractions /*int*/  = _data.MinimumFractions /*int*/ ;
 //BA.debugLineNum = 48;BA.debugLine="d.MinimumIntegers = Data.MinimumIntegers";
_d.MinimumIntegers /*int*/  = _data.MinimumIntegers /*int*/ ;
 //BA.debugLineNum = 49;BA.debugLine="d.Postfix = Data.Postfix";
_d.Postfix /*String*/  = _data.Postfix /*String*/ ;
 //BA.debugLineNum = 50;BA.debugLine="d.Prefix = Data.Prefix";
_d.Prefix /*String*/  = _data.Prefix /*String*/ ;
 //BA.debugLineNum = 51;BA.debugLine="d.RangeEnd = Data.RangeEnd";
_d.RangeEnd /*double*/  = _data.RangeEnd /*double*/ ;
 //BA.debugLineNum = 52;BA.debugLine="d.RangeStart = Data.RangeStart";
_d.RangeStart /*double*/  = _data.RangeStart /*double*/ ;
 //BA.debugLineNum = 53;BA.debugLine="d.RemoveMinusSign = Data.RemoveMinusSign";
_d.RemoveMinusSign /*boolean*/  = _data.RemoveMinusSign /*boolean*/ ;
 //BA.debugLineNum = 54;BA.debugLine="d.TextColor = Data.TextColor";
_d.TextColor /*int*/  = _data.TextColor /*int*/ ;
 //BA.debugLineNum = 55;BA.debugLine="d.FractionPaddingChar = Data.FractionPaddingChar";
_d.FractionPaddingChar /*String*/  = _data.FractionPaddingChar /*String*/ ;
 //BA.debugLineNum = 56;BA.debugLine="d.IntegerPaddingChar = Data.IntegerPaddingChar";
_d.IntegerPaddingChar /*String*/  = _data.IntegerPaddingChar /*String*/ ;
 //BA.debugLineNum = 57;BA.debugLine="Return d";
if (true) return _d;
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return null;
}
public cloyd.smart.home.monitor.b4xformatter._b4xformatdata  _createdefaultformat() throws Exception{
cloyd.smart.home.monitor.b4xformatter._b4xformatdata _d = null;
 //BA.debugLineNum = 17;BA.debugLine="Private Sub CreateDefaultFormat As B4XFormatData";
 //BA.debugLineNum = 18;BA.debugLine="Dim d As B4XFormatData";
_d = new cloyd.smart.home.monitor.b4xformatter._b4xformatdata();
 //BA.debugLineNum = 19;BA.debugLine="d.Initialize";
_d.Initialize();
 //BA.debugLineNum = 20;BA.debugLine="d.GroupingCharacter = \",\"";
_d.GroupingCharacter /*String*/  = ",";
 //BA.debugLineNum = 21;BA.debugLine="d.DecimalPoint = \".\"";
_d.DecimalPoint /*String*/  = ".";
 //BA.debugLineNum = 22;BA.debugLine="d.MaximumFractions = 3";
_d.MaximumFractions /*int*/  = (int) (3);
 //BA.debugLineNum = 23;BA.debugLine="d.MinimumIntegers = 1";
_d.MinimumIntegers /*int*/  = (int) (1);
 //BA.debugLineNum = 24;BA.debugLine="d.IntegerPaddingChar = \"0\"";
_d.IntegerPaddingChar /*String*/  = "0";
 //BA.debugLineNum = 25;BA.debugLine="d.FractionPaddingChar = \"0\"";
_d.FractionPaddingChar /*String*/  = "0";
 //BA.debugLineNum = 26;BA.debugLine="Return d";
if (true) return _d;
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return null;
}
public String  _format(double _number) throws Exception{
cloyd.smart.home.monitor.b4xformatter._b4xformatdata _data = null;
anywheresoftware.b4a.keywords.StringBuilderWrapper _sb = null;
int _numberstartindex = 0;
double _factor = 0;
int _whole = 0;
double _frac = 0;
int _g = 0;
int _fracstartindex = 0;
int _lastzerocount = 0;
int _multipler = 0;
int _w = 0;
 //BA.debugLineNum = 90;BA.debugLine="Public Sub Format (Number As Double) As String";
 //BA.debugLineNum = 91;BA.debugLine="If Number < MIN_VALUE Or Number > MAX_VALUE Then";
if (_number<_min_value || _number>_max_value) { 
if (true) return "OVERFLOW";};
 //BA.debugLineNum = 92;BA.debugLine="Dim data As B4XFormatData = GetFormatData (Number";
_data = _getformatdata(_number);
 //BA.debugLineNum = 93;BA.debugLine="Dim sb As StringBuilder";
_sb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 94;BA.debugLine="sb.Initialize";
_sb.Initialize();
 //BA.debugLineNum = 95;BA.debugLine="sb.Append(data.Prefix)";
_sb.Append(_data.Prefix /*String*/ );
 //BA.debugLineNum = 96;BA.debugLine="Dim NumberStartIndex As Int = sb.Length";
_numberstartindex = _sb.getLength();
 //BA.debugLineNum = 97;BA.debugLine="Dim factor As Double = Power(10, -data.MaximumFra";
_factor = __c.Power(10,-_data.MaximumFractions /*int*/ -1)*5;
 //BA.debugLineNum = 98;BA.debugLine="If Number < -factor And data.RemoveMinusSign = Fa";
if (_number<-_factor && _data.RemoveMinusSign /*boolean*/ ==__c.False) { 
 //BA.debugLineNum = 99;BA.debugLine="sb.Append(\"-\")";
_sb.Append("-");
 //BA.debugLineNum = 100;BA.debugLine="NumberStartIndex = NumberStartIndex + 1";
_numberstartindex = (int) (_numberstartindex+1);
 };
 //BA.debugLineNum = 102;BA.debugLine="Number = Abs(Number) + factor";
_number = __c.Abs(_number)+_factor;
 //BA.debugLineNum = 103;BA.debugLine="Dim whole As Int = Number";
_whole = (int) (_number);
 //BA.debugLineNum = 104;BA.debugLine="Dim frac As Double = Number - whole";
_frac = _number-_whole;
 //BA.debugLineNum = 105;BA.debugLine="Dim g As Int";
_g = 0;
 //BA.debugLineNum = 106;BA.debugLine="Do While whole > 0";
while (_whole>0) {
 //BA.debugLineNum = 107;BA.debugLine="If g > 0 And g Mod 3 = 0 And data.GroupingCharac";
if (_g>0 && _g%3==0 && _data.GroupingCharacter /*String*/ .length()>0) { 
 //BA.debugLineNum = 108;BA.debugLine="sb.Insert(NumberStartIndex, data.GroupingCharac";
_sb.Insert(_numberstartindex,_data.GroupingCharacter /*String*/ );
 };
 //BA.debugLineNum = 110;BA.debugLine="g = g + 1";
_g = (int) (_g+1);
 //BA.debugLineNum = 111;BA.debugLine="sb.Insert(NumberStartIndex, whole Mod 10)";
_sb.Insert(_numberstartindex,BA.NumberToString(_whole%10));
 //BA.debugLineNum = 112;BA.debugLine="whole = whole / 10";
_whole = (int) (_whole/(double)10);
 }
;
 //BA.debugLineNum = 114;BA.debugLine="Do While sb.Length - NumberStartIndex < data.Mini";
while (_sb.getLength()-_numberstartindex<_data.MinimumIntegers /*int*/ ) {
 //BA.debugLineNum = 115;BA.debugLine="sb.Insert(NumberStartIndex, data.IntegerPaddingC";
_sb.Insert(_numberstartindex,_data.IntegerPaddingChar /*String*/ );
 }
;
 //BA.debugLineNum = 117;BA.debugLine="If data.MaximumFractions > 0 And (data.MinimumFra";
if (_data.MaximumFractions /*int*/ >0 && (_data.MinimumFractions /*int*/ >0 || _frac>0)) { 
 //BA.debugLineNum = 118;BA.debugLine="Dim FracStartIndex As Int = sb.Length";
_fracstartindex = _sb.getLength();
 //BA.debugLineNum = 119;BA.debugLine="Dim LastZeroCount As Int";
_lastzerocount = 0;
 //BA.debugLineNum = 120;BA.debugLine="Dim Multipler As Int = 10";
_multipler = (int) (10);
 //BA.debugLineNum = 121;BA.debugLine="Do While frac >= 2 * factor And sb.Length - Frac";
while (_frac>=2*_factor && _sb.getLength()-_fracstartindex<_data.MaximumFractions /*int*/ ) {
 //BA.debugLineNum = 122;BA.debugLine="Dim w As Int = (frac * Multipler)";
_w = (int) ((_frac*_multipler));
 //BA.debugLineNum = 123;BA.debugLine="w = w Mod 10";
_w = (int) (_w%10);
 //BA.debugLineNum = 124;BA.debugLine="If w = 0 Then LastZeroCount = LastZeroCount + 1";
if (_w==0) { 
_lastzerocount = (int) (_lastzerocount+1);}
else {
_lastzerocount = (int) (0);};
 //BA.debugLineNum = 125;BA.debugLine="sb.Append(w)";
_sb.Append(BA.NumberToString(_w));
 //BA.debugLineNum = 126;BA.debugLine="Multipler = Multipler * 10";
_multipler = (int) (_multipler*10);
 }
;
 //BA.debugLineNum = 128;BA.debugLine="If data.FractionPaddingChar <> \"0\" And LastZeroC";
if ((_data.FractionPaddingChar /*String*/ ).equals("0") == false && _lastzerocount>0) { 
 //BA.debugLineNum = 129;BA.debugLine="sb.Remove(sb.Length - LastZeroCount, sb.Length)";
_sb.Remove((int) (_sb.getLength()-_lastzerocount),_sb.getLength());
 //BA.debugLineNum = 130;BA.debugLine="LastZeroCount = 0";
_lastzerocount = (int) (0);
 };
 //BA.debugLineNum = 132;BA.debugLine="Do While sb.Length - FracStartIndex < data.Minim";
while (_sb.getLength()-_fracstartindex<_data.MinimumFractions /*int*/ ) {
 //BA.debugLineNum = 133;BA.debugLine="sb.Append(data.FractionPaddingChar)";
_sb.Append(_data.FractionPaddingChar /*String*/ );
 //BA.debugLineNum = 134;BA.debugLine="LastZeroCount = 0";
_lastzerocount = (int) (0);
 }
;
 //BA.debugLineNum = 136;BA.debugLine="LastZeroCount = Min(LastZeroCount, sb.Length - F";
_lastzerocount = (int) (__c.Min(_lastzerocount,_sb.getLength()-_fracstartindex-_data.MinimumFractions /*int*/ ));
 //BA.debugLineNum = 137;BA.debugLine="If LastZeroCount > 0 Then";
if (_lastzerocount>0) { 
 //BA.debugLineNum = 138;BA.debugLine="sb.Remove(sb.Length - LastZeroCount, sb.Length)";
_sb.Remove((int) (_sb.getLength()-_lastzerocount),_sb.getLength());
 };
 //BA.debugLineNum = 140;BA.debugLine="If sb.Length > FracStartIndex Then sb.Insert(Fra";
if (_sb.getLength()>_fracstartindex) { 
_sb.Insert(_fracstartindex,_data.DecimalPoint /*String*/ );};
 };
 //BA.debugLineNum = 142;BA.debugLine="sb.Append(data.Postfix)";
_sb.Append(_data.Postfix /*String*/ );
 //BA.debugLineNum = 143;BA.debugLine="Return sb.ToString";
if (true) return _sb.ToString();
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
return "";
}
public String  _formatlabel(double _number,anywheresoftware.b4a.objects.B4XViewWrapper _label) throws Exception{
cloyd.smart.home.monitor.b4xformatter._b4xformatdata _data = null;
 //BA.debugLineNum = 148;BA.debugLine="Public Sub FormatLabel (Number As Double, Label As";
 //BA.debugLineNum = 149;BA.debugLine="Label.Text = Format(Number)";
_label.setText(BA.ObjectToCharSequence(_format(_number)));
 //BA.debugLineNum = 150;BA.debugLine="Dim data As B4XFormatData = GetFormatData(Number)";
_data = _getformatdata(_number);
 //BA.debugLineNum = 151;BA.debugLine="If data.TextColor <> 0 Then Label.TextColor = dat";
if (_data.TextColor /*int*/ !=0) { 
_label.setTextColor(_data.TextColor /*int*/ );};
 //BA.debugLineNum = 152;BA.debugLine="If data.FormatFont.IsInitialized Then Label.Font";
if (_data.FormatFont /*anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont*/ .getIsInitialized()) { 
_label.setFont(_data.FormatFont /*anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont*/ );};
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return "";
}
public cloyd.smart.home.monitor.b4xformatter._b4xformatdata  _getdefaultformat() throws Exception{
 //BA.debugLineNum = 76;BA.debugLine="Public Sub GetDefaultFormat As B4XFormatData";
 //BA.debugLineNum = 77;BA.debugLine="Return formats.Get(0)";
if (true) return (cloyd.smart.home.monitor.b4xformatter._b4xformatdata)(_formats.Get((int) (0)));
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return null;
}
public cloyd.smart.home.monitor.b4xformatter._b4xformatdata  _getformatdata(double _number) throws Exception{
int _i = 0;
cloyd.smart.home.monitor.b4xformatter._b4xformatdata _d = null;
 //BA.debugLineNum = 81;BA.debugLine="Public Sub GetFormatData (Number As Double) As B4X";
 //BA.debugLineNum = 82;BA.debugLine="For i = formats.Size - 1 To 1 Step - 1";
{
final int step1 = -1;
final int limit1 = (int) (1);
_i = (int) (_formats.getSize()-1) ;
for (;_i >= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 83;BA.debugLine="Dim d As B4XFormatData = formats.Get(i)";
_d = (cloyd.smart.home.monitor.b4xformatter._b4xformatdata)(_formats.Get(_i));
 //BA.debugLineNum = 84;BA.debugLine="If Number <= d.RangeEnd And Number >= d.RangeSta";
if (_number<=_d.RangeEnd /*double*/  && _number>=_d.RangeStart /*double*/ ) { 
if (true) return _d;};
 }
};
 //BA.debugLineNum = 86;BA.debugLine="Return formats.Get(0)";
if (true) return (cloyd.smart.home.monitor.b4xformatter._b4xformatdata)(_formats.Get((int) (0)));
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return null;
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
cloyd.smart.home.monitor.b4xformatter._b4xformatdata _d = null;
 //BA.debugLineNum = 11;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 12;BA.debugLine="formats.Initialize";
_formats.Initialize();
 //BA.debugLineNum = 13;BA.debugLine="Dim d As B4XFormatData = CreateDefaultFormat";
_d = _createdefaultformat();
 //BA.debugLineNum = 14;BA.debugLine="AddFormatData(d, MIN_VALUE, MAX_VALUE, True)";
_addformatdata(_d,_min_value,_max_value,__c.True);
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public cloyd.smart.home.monitor.b4xformatter._b4xformatdata  _newformatdata() throws Exception{
 //BA.debugLineNum = 30;BA.debugLine="Public Sub NewFormatData As B4XFormatData";
 //BA.debugLineNum = 31;BA.debugLine="Return CopyFormatData(GetDefaultFormat)";
if (true) return _copyformatdata(_getdefaultformat());
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return null;
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
