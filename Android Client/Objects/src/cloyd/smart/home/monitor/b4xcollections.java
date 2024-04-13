package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xcollections {
private static b4xcollections mostCurrent = new b4xcollections();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.chart _chart = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public static cloyd.smart.home.monitor.b4xbitset  _createbitset(anywheresoftware.b4a.BA _ba,int _size) throws Exception{
cloyd.smart.home.monitor.b4xbitset _s = null;
 //BA.debugLineNum = 52;BA.debugLine="Public Sub CreateBitSet (Size As Int) As B4XBitSet";
 //BA.debugLineNum = 53;BA.debugLine="Dim s As B4XBitSet";
_s = new cloyd.smart.home.monitor.b4xbitset();
 //BA.debugLineNum = 54;BA.debugLine="s.Initialize(Size)";
_s._initialize /*String*/ ((_ba.processBA == null ? _ba : _ba.processBA),_size);
 //BA.debugLineNum = 55;BA.debugLine="Return s";
if (true) return _s;
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return null;
}
public static cloyd.smart.home.monitor.b4xorderedmap  _createorderedmap(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 30;BA.debugLine="Public Sub CreateOrderedMap As B4XOrderedMap";
 //BA.debugLineNum = 31;BA.debugLine="Return CreateOrderedMap2(Null, Null)";
if (true) return _createorderedmap2(_ba,(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return null;
}
public static cloyd.smart.home.monitor.b4xorderedmap  _createorderedmap2(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.collections.List _keys,anywheresoftware.b4a.objects.collections.List _values) throws Exception{
cloyd.smart.home.monitor.b4xorderedmap _m = null;
int _i = 0;
 //BA.debugLineNum = 38;BA.debugLine="Public Sub CreateOrderedMap2 (Keys As List, Values";
 //BA.debugLineNum = 39;BA.debugLine="Dim m As B4XOrderedMap";
_m = new cloyd.smart.home.monitor.b4xorderedmap();
 //BA.debugLineNum = 40;BA.debugLine="m.Initialize";
_m._initialize /*String*/ ((_ba.processBA == null ? _ba : _ba.processBA));
 //BA.debugLineNum = 41;BA.debugLine="If Keys <> Null And Values <> Null And Keys.IsIni";
if (_keys!= null && _values!= null && _keys.IsInitialized() && _values.IsInitialized()) { 
 //BA.debugLineNum = 42;BA.debugLine="For i = 0 To Keys.Size - 1";
{
final int step4 = 1;
final int limit4 = (int) (_keys.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 43;BA.debugLine="m.Put(Keys.Get(i), Values.Get(i))";
_m._put /*String*/ (_keys.Get(_i),_values.Get(_i));
 }
};
 };
 //BA.debugLineNum = 46;BA.debugLine="Return m";
if (true) return _m;
 //BA.debugLineNum = 47;BA.debugLine="End Sub";
return null;
}
public static cloyd.smart.home.monitor.b4xset  _createset(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 9;BA.debugLine="Public Sub CreateSet As B4XSet";
 //BA.debugLineNum = 10;BA.debugLine="Return CreateSet2(Null)";
if (true) return _createset2(_ba,(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return null;
}
public static cloyd.smart.home.monitor.b4xset  _createset2(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.collections.List _values) throws Exception{
cloyd.smart.home.monitor.b4xset _s = null;
Object _v = null;
 //BA.debugLineNum = 16;BA.debugLine="Public Sub CreateSet2 (Values As List) As B4XSet";
 //BA.debugLineNum = 17;BA.debugLine="Dim s As B4XSet";
_s = new cloyd.smart.home.monitor.b4xset();
 //BA.debugLineNum = 18;BA.debugLine="s.Initialize";
_s._initialize /*String*/ ((_ba.processBA == null ? _ba : _ba.processBA));
 //BA.debugLineNum = 19;BA.debugLine="If Values <> Null And Values.IsInitialized Then";
if (_values!= null && _values.IsInitialized()) { 
 //BA.debugLineNum = 20;BA.debugLine="For Each v As Object In Values";
{
final anywheresoftware.b4a.BA.IterableList group4 = _values;
final int groupLen4 = group4.getSize()
;int index4 = 0;
;
for (; index4 < groupLen4;index4++){
_v = group4.Get(index4);
 //BA.debugLineNum = 21;BA.debugLine="s.Add(v)";
_s._add /*String*/ (_v);
 }
};
 };
 //BA.debugLineNum = 24;BA.debugLine="Return s";
if (true) return _s;
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return null;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 4;BA.debugLine="End Sub";
return "";
}
}
