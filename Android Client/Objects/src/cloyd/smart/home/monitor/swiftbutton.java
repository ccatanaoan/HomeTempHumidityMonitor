package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class swiftbutton extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.swiftbutton");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.swiftbutton.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _meventname = "";
public Object _mcallback = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mbase = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _xlbl = null;
public int _clr1 = 0;
public int _clr2 = 0;
public int _disabledcolor = 0;
public boolean _pressed = false;
public Object _tag = null;
public boolean _mdisabled = false;
public int _cornersradius = 0;
public int _sideheight = 0;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.chart _chart = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.b4xcollections _b4xcollections = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public String  _base_resize(double _width,double _height) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _v = null;
 //BA.debugLineNum = 46;BA.debugLine="Private Sub Base_Resize (Width As Double, Height A";
 //BA.debugLineNum = 47;BA.debugLine="cvs.Resize(Width, Height)";
_cvs.Resize((float) (_width),(float) (_height));
 //BA.debugLineNum = 48;BA.debugLine="For Each v As B4XView In mBase.GetAllViewsRecursi";
_v = new anywheresoftware.b4a.objects.B4XViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group2 = _mbase.GetAllViewsRecursive();
final int groupLen2 = group2.getSize()
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_v.setObject((java.lang.Object)(group2.Get(index2)));
 //BA.debugLineNum = 49;BA.debugLine="v.SetLayoutAnimated(0, 0, 0, Width, Height)";
_v.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),(int) (_height));
 }
};
 //BA.debugLineNum = 51;BA.debugLine="Draw";
_draw();
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 9;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Private mEventName As String 'ignore";
_meventname = "";
 //BA.debugLineNum = 11;BA.debugLine="Private mCallBack As Object 'ignore";
_mcallback = new Object();
 //BA.debugLineNum = 12;BA.debugLine="Public mBase As B4XView 'ignore";
_mbase = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Private xui As XUI 'ignore";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 14;BA.debugLine="Private cvs As B4XCanvas";
_cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 15;BA.debugLine="Public xLBL As B4XView";
_xlbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Public clr1, clr2, disabledColor As Int";
_clr1 = 0;
_clr2 = 0;
_disabledcolor = 0;
 //BA.debugLineNum = 17;BA.debugLine="Private pressed As Boolean";
_pressed = false;
 //BA.debugLineNum = 18;BA.debugLine="Public Tag As Object";
_tag = new Object();
 //BA.debugLineNum = 19;BA.debugLine="Private mDisabled As Boolean";
_mdisabled = false;
 //BA.debugLineNum = 20;BA.debugLine="Public CornersRadius, SideHeight As Int";
_cornersradius = 0;
_sideheight = 0;
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public String  _designercreateview(Object _base,anywheresoftware.b4a.objects.LabelWrapper _lbl,anywheresoftware.b4a.objects.collections.Map _props) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
 //BA.debugLineNum = 28;BA.debugLine="Public Sub DesignerCreateView (Base As Object, Lbl";
 //BA.debugLineNum = 29;BA.debugLine="mBase = Base";
_mbase.setObject((java.lang.Object)(_base));
 //BA.debugLineNum = 30;BA.debugLine="Tag = mBase.Tag : mBase.Tag = Me";
_tag = _mbase.getTag();
 //BA.debugLineNum = 30;BA.debugLine="Tag = mBase.Tag : mBase.Tag = Me";
_mbase.setTag(this);
 //BA.debugLineNum = 31;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"p\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _xui.CreatePanel(ba,"p");
 //BA.debugLineNum = 32;BA.debugLine="p.Color = xui.Color_Transparent";
_p.setColor(_xui.Color_Transparent);
 //BA.debugLineNum = 33;BA.debugLine="clr1 = xui.PaintOrColorToColor(Props.Get(\"Primary";
_clr1 = _xui.PaintOrColorToColor(_props.Get((Object)("PrimaryColor")));
 //BA.debugLineNum = 34;BA.debugLine="clr2 = xui.PaintOrColorToColor(Props.Get(\"Seconda";
_clr2 = _xui.PaintOrColorToColor(_props.Get((Object)("SecondaryColor")));
 //BA.debugLineNum = 35;BA.debugLine="disabledColor = xui.PaintOrColorToColor(Props.Get";
_disabledcolor = _xui.PaintOrColorToColor(_props.GetDefault((Object)("DisabledColor"),(Object)(0xff999999)));
 //BA.debugLineNum = 36;BA.debugLine="CornersRadius = DipToCurrent(Props.GetDefault(\"Co";
_cornersradius = __c.DipToCurrent((int)(BA.ObjectToNumber(_props.GetDefault((Object)("CornersRadius"),(Object)(15)))));
 //BA.debugLineNum = 37;BA.debugLine="SideHeight = DipToCurrent(Props.GetDefault(\"SideH";
_sideheight = __c.DipToCurrent((int)(BA.ObjectToNumber(_props.GetDefault((Object)("SideHeight"),(Object)(5)))));
 //BA.debugLineNum = 38;BA.debugLine="xLBL = Lbl";
_xlbl.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 39;BA.debugLine="mBase.AddView(xLBL, 0, 0, 0, 0)";
_mbase.AddView((android.view.View)(_xlbl.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 40;BA.debugLine="mBase.AddView(p, 0, 0, 0, 0)";
_mbase.AddView((android.view.View)(_p.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 41;BA.debugLine="xLBL.SetTextAlignment(\"CENTER\", \"CENTER\")";
_xlbl.SetTextAlignment("CENTER","CENTER");
 //BA.debugLineNum = 42;BA.debugLine="cvs.Initialize(mBase)";
_cvs.Initialize(_mbase);
 //BA.debugLineNum = 43;BA.debugLine="Base_Resize(mBase.Width, mBase.Height)";
_base_resize(_mbase.getWidth(),_mbase.getHeight());
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public String  _draw() throws Exception{
anywheresoftware.b4a.objects.B4XCanvas.B4XRect _r = null;
anywheresoftware.b4a.objects.B4XCanvas.B4XPath _p = null;
int _c = 0;
 //BA.debugLineNum = 112;BA.debugLine="Private Sub Draw";
 //BA.debugLineNum = 113;BA.debugLine="cvs.ClearRect(cvs.TargetRect)";
_cvs.ClearRect(_cvs.getTargetRect());
 //BA.debugLineNum = 114;BA.debugLine="Dim r As B4XRect";
_r = new anywheresoftware.b4a.objects.B4XCanvas.B4XRect();
 //BA.debugLineNum = 115;BA.debugLine="Dim p As B4XPath";
_p = new anywheresoftware.b4a.objects.B4XCanvas.B4XPath();
 //BA.debugLineNum = 116;BA.debugLine="r.Initialize(0, SideHeight, mBase.Width, mBase.He";
_r.Initialize((float) (0),(float) (_sideheight),(float) (_mbase.getWidth()),(float) (_mbase.getHeight()));
 //BA.debugLineNum = 117;BA.debugLine="If pressed = False Then";
if (_pressed==__c.False) { 
 //BA.debugLineNum = 118;BA.debugLine="xLBL.Top = 0";
_xlbl.setTop((int) (0));
 //BA.debugLineNum = 119;BA.debugLine="p.InitializeRoundedRect(r, CornersRadius)";
_p.InitializeRoundedRect(_r,(float) (_cornersradius));
 //BA.debugLineNum = 120;BA.debugLine="cvs.DrawPath(p, clr2, True, 0)";
_cvs.DrawPath(_p,_clr2,__c.True,(float) (0));
 //BA.debugLineNum = 121;BA.debugLine="r.Initialize(0, 0, mBase.Width, mBase.Height - S";
_r.Initialize((float) (0),(float) (0),(float) (_mbase.getWidth()),(float) (_mbase.getHeight()-_sideheight));
 //BA.debugLineNum = 122;BA.debugLine="p.InitializeRoundedRect(r, CornersRadius)";
_p.InitializeRoundedRect(_r,(float) (_cornersradius));
 //BA.debugLineNum = 123;BA.debugLine="cvs.DrawPath(p, clr1, True, 0)";
_cvs.DrawPath(_p,_clr1,__c.True,(float) (0));
 }else {
 //BA.debugLineNum = 125;BA.debugLine="xLBL.Top = SideHeight";
_xlbl.setTop(_sideheight);
 //BA.debugLineNum = 126;BA.debugLine="p.InitializeRoundedRect(r, CornersRadius)";
_p.InitializeRoundedRect(_r,(float) (_cornersradius));
 //BA.debugLineNum = 127;BA.debugLine="Dim c As Int";
_c = 0;
 //BA.debugLineNum = 128;BA.debugLine="If mDisabled Then c = disabledColor Else c = clr";
if (_mdisabled) { 
_c = _disabledcolor;}
else {
_c = _clr1;};
 //BA.debugLineNum = 129;BA.debugLine="cvs.DrawPath(p, c, True, 0)";
_cvs.DrawPath(_p,_c,__c.True,(float) (0));
 };
 //BA.debugLineNum = 132;BA.debugLine="cvs.Invalidate";
_cvs.Invalidate();
 //BA.debugLineNum = 133;BA.debugLine="End Sub";
return "";
}
public boolean  _getenabled() throws Exception{
 //BA.debugLineNum = 54;BA.debugLine="Public Sub getEnabled As Boolean";
 //BA.debugLineNum = 55;BA.debugLine="Return Not(mDisabled)";
if (true) return __c.Not(_mdisabled);
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return false;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,Object _callback,String _eventname) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 23;BA.debugLine="Public Sub Initialize (Callback As Object, EventNa";
 //BA.debugLineNum = 24;BA.debugLine="mEventName = EventName";
_meventname = _eventname;
 //BA.debugLineNum = 25;BA.debugLine="mCallBack = Callback";
_mcallback = _callback;
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public String  _p_touch(int _action,float _x,float _y) throws Exception{
boolean _inside = false;
 //BA.debugLineNum = 64;BA.debugLine="Private Sub p_Touch (Action As Int, X As Float, Y";
 //BA.debugLineNum = 65;BA.debugLine="If mDisabled Then Return";
if (_mdisabled) { 
if (true) return "";};
 //BA.debugLineNum = 66;BA.debugLine="Dim Inside As Boolean = x > 0 And x < mBase.Width";
_inside = _x>0 && _x<_mbase.getWidth() && _y>0 && _y<_mbase.getHeight();
 //BA.debugLineNum = 67;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,_mbase.TOUCH_ACTION_DOWN,_mbase.TOUCH_ACTION_MOVE,_mbase.TOUCH_ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 69;BA.debugLine="SetPressedState(True)";
_setpressedstate(__c.True);
 //BA.debugLineNum = 70;BA.debugLine="Draw";
_draw();
 break; }
case 1: {
 //BA.debugLineNum = 72;BA.debugLine="If pressed <> Inside Then";
if (_pressed!=_inside) { 
 //BA.debugLineNum = 73;BA.debugLine="SetPressedState(Inside)";
_setpressedstate(_inside);
 //BA.debugLineNum = 74;BA.debugLine="Draw";
_draw();
 };
 break; }
case 2: {
 //BA.debugLineNum = 77;BA.debugLine="SetPressedState(False)";
_setpressedstate(__c.False);
 //BA.debugLineNum = 78;BA.debugLine="Draw";
_draw();
 //BA.debugLineNum = 79;BA.debugLine="If Inside Then";
if (_inside) { 
 //BA.debugLineNum = 80;BA.debugLine="CallSubDelayed(mCallBack, mEventName & \"_Click";
__c.CallSubDelayed(ba,_mcallback,_meventname+"_Click");
 };
 break; }
}
;
 //BA.debugLineNum = 83;BA.debugLine="End Sub";
return "";
}
public String  _setcolors(int _primary,int _secondary) throws Exception{
 //BA.debugLineNum = 102;BA.debugLine="Public Sub SetColors(Primary As Int, Secondary As";
 //BA.debugLineNum = 103;BA.debugLine="clr1 = Primary";
_clr1 = _primary;
 //BA.debugLineNum = 104;BA.debugLine="clr2 = Secondary";
_clr2 = _secondary;
 //BA.debugLineNum = 105;BA.debugLine="Draw";
_draw();
 //BA.debugLineNum = 106;BA.debugLine="End Sub";
return "";
}
public String  _setenabled(boolean _b) throws Exception{
 //BA.debugLineNum = 58;BA.debugLine="Public Sub setEnabled(b As Boolean)";
 //BA.debugLineNum = 59;BA.debugLine="mDisabled = Not(b)";
_mdisabled = __c.Not(_b);
 //BA.debugLineNum = 60;BA.debugLine="pressed = mDisabled";
_pressed = _mdisabled;
 //BA.debugLineNum = 61;BA.debugLine="Draw";
_draw();
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return "";
}
public String  _setpressedstate(boolean _newstate) throws Exception{
 //BA.debugLineNum = 85;BA.debugLine="Private Sub SetPressedState(NewState As Boolean)";
 //BA.debugLineNum = 86;BA.debugLine="If pressed = NewState Then Return";
if (_pressed==_newstate) { 
if (true) return "";};
 //BA.debugLineNum = 87;BA.debugLine="If NewState And xui.SubExists(mCallBack, mEventNa";
if (_newstate && _xui.SubExists(ba,_mcallback,_meventname+"_ButtonDown",(int) (0))) { 
 //BA.debugLineNum = 88;BA.debugLine="CallSubDelayed(mCallBack, mEventName & \"_ButtonD";
__c.CallSubDelayed(ba,_mcallback,_meventname+"_ButtonDown");
 };
 //BA.debugLineNum = 90;BA.debugLine="If NewState = False And xui.SubExists(mCallBack,";
if (_newstate==__c.False && _xui.SubExists(ba,_mcallback,_meventname+"_ButtonUp",(int) (0))) { 
 //BA.debugLineNum = 91;BA.debugLine="CallSubDelayed(mCallBack, mEventName & \"_ButtonU";
__c.CallSubDelayed(ba,_mcallback,_meventname+"_ButtonUp");
 };
 //BA.debugLineNum = 93;BA.debugLine="pressed= NewState";
_pressed = _newstate;
 //BA.debugLineNum = 94;BA.debugLine="End Sub";
return "";
}
public String  _update() throws Exception{
 //BA.debugLineNum = 108;BA.debugLine="Public Sub Update";
 //BA.debugLineNum = 109;BA.debugLine="Draw";
_draw();
 //BA.debugLineNum = 110;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
