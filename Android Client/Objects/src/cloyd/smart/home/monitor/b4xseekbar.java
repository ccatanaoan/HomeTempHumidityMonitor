package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xseekbar extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.b4xseekbar");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.b4xseekbar.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _meventname = "";
public Object _mcallback = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mbase = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public int _color1 = 0;
public int _color2 = 0;
public int _thumbcolor = 0;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public Object _tag = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _touchpanel = null;
public int _mvalue = 0;
public int _minvalue = 0;
public int _maxvalue = 0;
public int _interval = 0;
public boolean _vertical = false;
public int _size1 = 0;
public int _size2 = 0;
public int _radius1 = 0;
public int _radius2 = 0;
public boolean _pressed = false;
public int _size = 0;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.chart _chart = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.b4xcollections _b4xcollections = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public String  _base_resize(double _width,double _height) throws Exception{
 //BA.debugLineNum = 52;BA.debugLine="Private Sub Base_Resize (Width As Double, Height A";
 //BA.debugLineNum = 53;BA.debugLine="cvs.Resize(Width, Height)";
_cvs.Resize((float) (_width),(float) (_height));
 //BA.debugLineNum = 54;BA.debugLine="TouchPanel.SetLayoutAnimated(0, 0, 0, Width, Heig";
_touchpanel.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),(int) (_height));
 //BA.debugLineNum = 55;BA.debugLine="Vertical = mBase.Height > mBase.Width";
_vertical = _mbase.getHeight()>_mbase.getWidth();
 //BA.debugLineNum = 56;BA.debugLine="size = Max(mBase.Height, mBase.Width) - 2 * Radiu";
_size = (int) (__c.Max(_mbase.getHeight(),_mbase.getWidth())-2*_radius2);
 //BA.debugLineNum = 57;BA.debugLine="Update";
_update();
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 11;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 12;BA.debugLine="Private mEventName As String 'ignore";
_meventname = "";
 //BA.debugLineNum = 13;BA.debugLine="Private mCallBack As Object 'ignore";
_mcallback = new Object();
 //BA.debugLineNum = 14;BA.debugLine="Public mBase As B4XView 'ignore";
_mbase = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Private xui As XUI 'ignore";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 16;BA.debugLine="Public Color1, Color2, ThumbColor As Int";
_color1 = 0;
_color2 = 0;
_thumbcolor = 0;
 //BA.debugLineNum = 17;BA.debugLine="Private cvs As B4XCanvas";
_cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 18;BA.debugLine="Public Tag As Object";
_tag = new Object();
 //BA.debugLineNum = 19;BA.debugLine="Private TouchPanel As B4XView";
_touchpanel = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private mValue As Int";
_mvalue = 0;
 //BA.debugLineNum = 21;BA.debugLine="Public MinValue, MaxValue As Int";
_minvalue = 0;
_maxvalue = 0;
 //BA.debugLineNum = 22;BA.debugLine="Public Interval As Int = 1";
_interval = (int) (1);
 //BA.debugLineNum = 23;BA.debugLine="Private Vertical As Boolean";
_vertical = false;
 //BA.debugLineNum = 24;BA.debugLine="Public Size1 = 4dip, Size2 = 2dip, Radius1 = 6dip";
_size1 = __c.DipToCurrent((int) (4));
_size2 = __c.DipToCurrent((int) (2));
_radius1 = __c.DipToCurrent((int) (6));
_radius2 = __c.DipToCurrent((int) (12));
 //BA.debugLineNum = 25;BA.debugLine="Private Pressed As Boolean";
_pressed = false;
 //BA.debugLineNum = 26;BA.debugLine="Private size As Int";
_size = 0;
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public String  _designercreateview(Object _base,anywheresoftware.b4a.objects.LabelWrapper _lbl,anywheresoftware.b4a.objects.collections.Map _props) throws Exception{
 //BA.debugLineNum = 35;BA.debugLine="Public Sub DesignerCreateView (Base As Object, Lbl";
 //BA.debugLineNum = 36;BA.debugLine="mBase = Base";
_mbase.setObject((java.lang.Object)(_base));
 //BA.debugLineNum = 37;BA.debugLine="Tag = mBase.Tag : mBase.Tag = Me";
_tag = _mbase.getTag();
 //BA.debugLineNum = 37;BA.debugLine="Tag = mBase.Tag : mBase.Tag = Me";
_mbase.setTag(this);
 //BA.debugLineNum = 38;BA.debugLine="Color1 = xui.PaintOrColorToColor(Props.Get(\"Color";
_color1 = _xui.PaintOrColorToColor(_props.Get((Object)("Color1")));
 //BA.debugLineNum = 39;BA.debugLine="Color2 = xui.PaintOrColorToColor(Props.Get(\"Color";
_color2 = _xui.PaintOrColorToColor(_props.Get((Object)("Color2")));
 //BA.debugLineNum = 40;BA.debugLine="ThumbColor = xui.PaintOrColorToColor(Props.Get(\"T";
_thumbcolor = _xui.PaintOrColorToColor(_props.Get((Object)("ThumbColor")));
 //BA.debugLineNum = 41;BA.debugLine="Interval = Max(1, Props.GetDefault(\"Interval\", 1)";
_interval = (int) (__c.Max(1,(double)(BA.ObjectToNumber(_props.GetDefault((Object)("Interval"),(Object)(1))))));
 //BA.debugLineNum = 42;BA.debugLine="MinValue = Props.Get(\"Min\")";
_minvalue = (int)(BA.ObjectToNumber(_props.Get((Object)("Min"))));
 //BA.debugLineNum = 43;BA.debugLine="MaxValue = Props.Get(\"Max\")";
_maxvalue = (int)(BA.ObjectToNumber(_props.Get((Object)("Max"))));
 //BA.debugLineNum = 44;BA.debugLine="mValue = Max(MinValue, Min(MaxValue, Props.Get(\"V";
_mvalue = (int) (__c.Max(_minvalue,__c.Min(_maxvalue,(double)(BA.ObjectToNumber(_props.Get((Object)("Value")))))));
 //BA.debugLineNum = 45;BA.debugLine="cvs.Initialize(mBase)";
_cvs.Initialize(_mbase);
 //BA.debugLineNum = 46;BA.debugLine="TouchPanel = xui.CreatePanel(\"TouchPanel\")";
_touchpanel = _xui.CreatePanel(ba,"TouchPanel");
 //BA.debugLineNum = 47;BA.debugLine="mBase.AddView(TouchPanel, 0, 0, mBase.Width, mBas";
_mbase.AddView((android.view.View)(_touchpanel.getObject()),(int) (0),(int) (0),_mbase.getWidth(),_mbase.getHeight());
 //BA.debugLineNum = 48;BA.debugLine="If xui.IsB4A Or xui.IsB4i Then Radius2 = 20dip";
if (_xui.getIsB4A() || _xui.getIsB4i()) { 
_radius2 = __c.DipToCurrent((int) (20));};
 //BA.debugLineNum = 49;BA.debugLine="If xui.IsB4A Then Base_Resize(mBase.Width, mBase.";
if (_xui.getIsB4A()) { 
_base_resize(_mbase.getWidth(),_mbase.getHeight());};
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public int  _getvalue() throws Exception{
 //BA.debugLineNum = 130;BA.debugLine="Public Sub getValue As Int";
 //BA.debugLineNum = 131;BA.debugLine="Return mValue";
if (true) return _mvalue;
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return 0;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,Object _callback,String _eventname) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 29;BA.debugLine="Public Sub Initialize (Callback As Object, EventNa";
 //BA.debugLineNum = 30;BA.debugLine="mEventName = EventName";
_meventname = _eventname;
 //BA.debugLineNum = 31;BA.debugLine="mCallBack = Callback";
_mcallback = _callback;
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return "";
}
public String  _raisetouchstateevent() throws Exception{
 //BA.debugLineNum = 102;BA.debugLine="Private Sub RaiseTouchStateEvent";
 //BA.debugLineNum = 103;BA.debugLine="If xui.SubExists(mCallBack, mEventName & \"_TouchS";
if (_xui.SubExists(ba,_mcallback,_meventname+"_TouchStateChanged",(int) (1))) { 
 //BA.debugLineNum = 104;BA.debugLine="CallSubDelayed2(mCallBack, mEventName & \"_TouchS";
__c.CallSubDelayed2(ba,_mcallback,_meventname+"_TouchStateChanged",(Object)(_pressed));
 };
 //BA.debugLineNum = 106;BA.debugLine="End Sub";
return "";
}
public String  _setvalue(int _v) throws Exception{
 //BA.debugLineNum = 125;BA.debugLine="Public Sub setValue(v As Int)";
 //BA.debugLineNum = 126;BA.debugLine="mValue = Max(MinValue, Min(MaxValue, v))";
_mvalue = (int) (__c.Max(_minvalue,__c.Min(_maxvalue,_v)));
 //BA.debugLineNum = 127;BA.debugLine="Update";
_update();
 //BA.debugLineNum = 128;BA.debugLine="End Sub";
return "";
}
public String  _setvaluebasedontouch(int _x,int _y) throws Exception{
int _v = 0;
int _newvalue = 0;
 //BA.debugLineNum = 108;BA.debugLine="Private Sub SetValueBasedOnTouch(x As Int, y As In";
 //BA.debugLineNum = 109;BA.debugLine="Dim v As Int";
_v = 0;
 //BA.debugLineNum = 110;BA.debugLine="If Vertical Then";
if (_vertical) { 
 //BA.debugLineNum = 111;BA.debugLine="v = (mBase.Height - Radius2 - y) / size * (MaxVa";
_v = (int) ((_mbase.getHeight()-_radius2-_y)/(double)_size*(_maxvalue-_minvalue)+_minvalue);
 }else {
 //BA.debugLineNum = 113;BA.debugLine="v = (x - Radius2) / size * (MaxValue - MinValue)";
_v = (int) ((_x-_radius2)/(double)_size*(_maxvalue-_minvalue)+_minvalue);
 };
 //BA.debugLineNum = 115;BA.debugLine="v = Round (v / Interval) * Interval";
_v = (int) (__c.Round(_v/(double)_interval)*_interval);
 //BA.debugLineNum = 116;BA.debugLine="Dim NewValue As Int = Max(MinValue, Min(MaxValue,";
_newvalue = (int) (__c.Max(_minvalue,__c.Min(_maxvalue,_v)));
 //BA.debugLineNum = 117;BA.debugLine="If NewValue <> mValue Then";
if (_newvalue!=_mvalue) { 
 //BA.debugLineNum = 118;BA.debugLine="mValue = NewValue";
_mvalue = _newvalue;
 //BA.debugLineNum = 119;BA.debugLine="If xui.SubExists(mCallBack, mEventName & \"_Value";
if (_xui.SubExists(ba,_mcallback,_meventname+"_ValueChanged",(int) (1))) { 
 //BA.debugLineNum = 120;BA.debugLine="CallSubDelayed2(mCallBack, mEventName & \"_Value";
__c.CallSubDelayed2(ba,_mcallback,_meventname+"_ValueChanged",(Object)(_mvalue));
 };
 };
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public String  _touchpanel_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 88;BA.debugLine="Private Sub TouchPanel_Touch (Action As Int, X As";
 //BA.debugLineNum = 89;BA.debugLine="If Action = TouchPanel.TOUCH_ACTION_DOWN Then";
if (_action==_touchpanel.TOUCH_ACTION_DOWN) { 
 //BA.debugLineNum = 90;BA.debugLine="Pressed = True";
_pressed = __c.True;
 //BA.debugLineNum = 91;BA.debugLine="RaiseTouchStateEvent";
_raisetouchstateevent();
 //BA.debugLineNum = 92;BA.debugLine="SetValueBasedOnTouch(X, Y)";
_setvaluebasedontouch((int) (_x),(int) (_y));
 }else if(_action==_touchpanel.TOUCH_ACTION_MOVE) { 
 //BA.debugLineNum = 94;BA.debugLine="SetValueBasedOnTouch(X, Y)";
_setvaluebasedontouch((int) (_x),(int) (_y));
 }else if(_action==_touchpanel.TOUCH_ACTION_UP) { 
 //BA.debugLineNum = 96;BA.debugLine="Pressed = False";
_pressed = __c.False;
 //BA.debugLineNum = 97;BA.debugLine="RaiseTouchStateEvent";
_raisetouchstateevent();
 };
 //BA.debugLineNum = 99;BA.debugLine="Update";
_update();
 //BA.debugLineNum = 100;BA.debugLine="End Sub";
return "";
}
public String  _update() throws Exception{
int _s1 = 0;
int _y = 0;
int _x = 0;
 //BA.debugLineNum = 61;BA.debugLine="Public Sub Update";
 //BA.debugLineNum = 63;BA.debugLine="cvs.ClearRect(cvs.TargetRect)";
_cvs.ClearRect(_cvs.getTargetRect());
 //BA.debugLineNum = 64;BA.debugLine="If size > 0 Then";
if (_size>0) { 
 //BA.debugLineNum = 65;BA.debugLine="If Vertical = False Then";
if (_vertical==__c.False) { 
 //BA.debugLineNum = 66;BA.debugLine="Dim s1 As Int = Radius2 + (mValue - MinValue) /";
_s1 = (int) (_radius2+(_mvalue-_minvalue)/(double)(_maxvalue-_minvalue)*_size);
 //BA.debugLineNum = 67;BA.debugLine="Dim y As Int = mBase.Height / 2";
_y = (int) (_mbase.getHeight()/(double)2);
 //BA.debugLineNum = 68;BA.debugLine="cvs.DrawLine(Radius2, y, s1, y, Color1, Size1)";
_cvs.DrawLine((float) (_radius2),(float) (_y),(float) (_s1),(float) (_y),_color1,(float) (_size1));
 //BA.debugLineNum = 69;BA.debugLine="cvs.DrawLine(s1, y, mBase.Width - Radius2, y, C";
_cvs.DrawLine((float) (_s1),(float) (_y),(float) (_mbase.getWidth()-_radius2),(float) (_y),_color2,(float) (_size2));
 //BA.debugLineNum = 70;BA.debugLine="cvs.DrawCircle(s1, y, Radius1, Color1, True, 0)";
_cvs.DrawCircle((float) (_s1),(float) (_y),(float) (_radius1),_color1,__c.True,(float) (0));
 //BA.debugLineNum = 71;BA.debugLine="If Pressed Then";
if (_pressed) { 
 //BA.debugLineNum = 72;BA.debugLine="cvs.DrawCircle(s1, y, Radius2, ThumbColor, Tru";
_cvs.DrawCircle((float) (_s1),(float) (_y),(float) (_radius2),_thumbcolor,__c.True,(float) (0));
 };
 }else {
 //BA.debugLineNum = 75;BA.debugLine="Dim s1 As Int = Radius2 + (MaxValue - mValue -";
_s1 = (int) (_radius2+(_maxvalue-_mvalue-_minvalue)/(double)(_maxvalue-_minvalue)*_size);
 //BA.debugLineNum = 76;BA.debugLine="Dim x As Int = mBase.Width / 2";
_x = (int) (_mbase.getWidth()/(double)2);
 //BA.debugLineNum = 77;BA.debugLine="cvs.DrawLine(x, Radius2, x, s1, Color2, Size2)";
_cvs.DrawLine((float) (_x),(float) (_radius2),(float) (_x),(float) (_s1),_color2,(float) (_size2));
 //BA.debugLineNum = 78;BA.debugLine="cvs.DrawLine(x, s1, x, mBase.Height - Radius2,";
_cvs.DrawLine((float) (_x),(float) (_s1),(float) (_x),(float) (_mbase.getHeight()-_radius2),_color1,(float) (_size1));
 //BA.debugLineNum = 79;BA.debugLine="cvs.DrawCircle(x, s1, Radius1, Color1, True, 0)";
_cvs.DrawCircle((float) (_x),(float) (_s1),(float) (_radius1),_color1,__c.True,(float) (0));
 //BA.debugLineNum = 80;BA.debugLine="If Pressed Then";
if (_pressed) { 
 //BA.debugLineNum = 81;BA.debugLine="cvs.DrawCircle(x, s1, Radius2, ThumbColor, Tru";
_cvs.DrawCircle((float) (_x),(float) (_s1),(float) (_radius2),_thumbcolor,__c.True,(float) (0));
 };
 };
 };
 //BA.debugLineNum = 85;BA.debugLine="cvs.Invalidate";
_cvs.Invalidate();
 //BA.debugLineNum = 86;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
