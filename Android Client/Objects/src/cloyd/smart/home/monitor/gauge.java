package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class gauge extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.gauge");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.gauge.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _meventname = "";
public Object _mcallback = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mbase = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvsgauge = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvsindicator = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mlbl = null;
public anywheresoftware.b4a.objects.collections.List _mranges = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _gaugepanel = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _indicatorpanel = null;
public float _radius = 0f;
public float _x = 0f;
public float _y = 0f;
public int _indicatorlength = 0;
public int _centercolor = 0;
public int _indicatorcolor = 0;
public float _minvalue = 0f;
public float _maxvalue = 0f;
public float _indicatorbasewidth = 0f;
public float _mcurrentvalue = 0f;
public String _prefixtext = "";
public String _suffixtext = "";
public int _half_circle = 0;
public int _full_circle = 0;
public int _gaugetype = 0;
public int _anglerange = 0;
public int _angleoffset = 0;
public int _backgroundcolor = 0;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.chart _chart = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.b4xcollections _b4xcollections = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public static class _gaugerange{
public boolean IsInitialized;
public float LowValue;
public float HighValue;
public int Color;
public void Initialize() {
IsInitialized = true;
LowValue = 0f;
HighValue = 0f;
Color = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public String  _animatevalueto(float _newvalue) throws Exception{
 //BA.debugLineNum = 227;BA.debugLine="Private Sub AnimateValueTo(NewValue As Float)";
 //BA.debugLineNum = 228;BA.debugLine="mCurrentValue = NewValue";
_mcurrentvalue = _newvalue;
 //BA.debugLineNum = 229;BA.debugLine="DrawIndicator(mCurrentValue)";
_drawindicator(_mcurrentvalue);
 //BA.debugLineNum = 230;BA.debugLine="End Sub";
return "";
}
public String  _base_resize(double _width,double _height) throws Exception{
 //BA.debugLineNum = 82;BA.debugLine="Private Sub Base_Resize (Width As Double, Height A";
 //BA.debugLineNum = 83;BA.debugLine="mlbl.SetLayoutAnimated(0, 0, Height - mlbl.Heigh";
_mlbl.SetLayoutAnimated((int) (0),(int) (0),(int) (_height-_mlbl.getHeight()-__c.DipToCurrent((int) (5))),(int) (_width),_mlbl.getHeight());
 //BA.debugLineNum = 84;BA.debugLine="GaugePanel.SetLayoutAnimated(0, 0, 0, Width, Heig";
_gaugepanel.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),(int) (_height));
 //BA.debugLineNum = 85;BA.debugLine="cvsGauge.Resize(Width, Height)";
_cvsgauge.Resize((float) (_width),(float) (_height));
 //BA.debugLineNum = 86;BA.debugLine="IndicatorPanel.SetLayoutAnimated(0, 0, 0, Width,";
_indicatorpanel.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),(int) (_height));
 //BA.debugLineNum = 87;BA.debugLine="cvsIndicator.Resize(Width, Height)";
_cvsindicator.Resize((float) (_width),(float) (_height));
 //BA.debugLineNum = 89;BA.debugLine="DrawBackground";
_drawbackground();
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 13;BA.debugLine="Private mEventName As String 'ignore";
_meventname = "";
 //BA.debugLineNum = 14;BA.debugLine="Private mCallBack As Object 'ignore";
_mcallback = new Object();
 //BA.debugLineNum = 15;BA.debugLine="Private mBase As B4XView";
_mbase = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 17;BA.debugLine="Private cvsGauge, cvsIndicator As B4XCanvas";
_cvsgauge = new anywheresoftware.b4a.objects.B4XCanvas();
_cvsindicator = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 18;BA.debugLine="Private mlbl As B4XView";
_mlbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Type GaugeRange (LowValue As Float, HighValue As";
;
 //BA.debugLineNum = 20;BA.debugLine="Private mRanges As List";
_mranges = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 21;BA.debugLine="Private GaugePanel, IndicatorPanel As B4XView";
_gaugepanel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_indicatorpanel = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private Radius, x, y As Float";
_radius = 0f;
_x = 0f;
_y = 0f;
 //BA.debugLineNum = 23;BA.debugLine="Private IndicatorLength As Int";
_indicatorlength = 0;
 //BA.debugLineNum = 24;BA.debugLine="Private CenterColor, IndicatorColor As Int";
_centercolor = 0;
_indicatorcolor = 0;
 //BA.debugLineNum = 25;BA.debugLine="Private MinValue, MaxValue As Float";
_minvalue = 0f;
_maxvalue = 0f;
 //BA.debugLineNum = 26;BA.debugLine="Private IndicatorBaseWidth As Float";
_indicatorbasewidth = 0f;
 //BA.debugLineNum = 27;BA.debugLine="Private mCurrentValue As Float = 50";
_mcurrentvalue = (float) (50);
 //BA.debugLineNum = 28;BA.debugLine="Private PrefixText, SuffixText As String";
_prefixtext = "";
_suffixtext = "";
 //BA.debugLineNum = 29;BA.debugLine="Private HALF_CIRCLE = 1, FULL_CIRCLE = 2 As Int";
_half_circle = (int) (1);
_full_circle = (int) (2);
 //BA.debugLineNum = 30;BA.debugLine="Private GaugeType As Int";
_gaugetype = 0;
 //BA.debugLineNum = 31;BA.debugLine="Private AngleRange As Int";
_anglerange = 0;
 //BA.debugLineNum = 32;BA.debugLine="Private AngleOffset As Int";
_angleoffset = 0;
 //BA.debugLineNum = 33;BA.debugLine="Private BackgroundColor As Int";
_backgroundcolor = 0;
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}
public String  _configurefullcircle() throws Exception{
 //BA.debugLineNum = 135;BA.debugLine="Private Sub ConfigureFullCircle";
 //BA.debugLineNum = 136;BA.debugLine="Radius = Min(GaugePanel.Width, GaugePanel.Height)";
_radius = (float) (__c.Min(_gaugepanel.getWidth(),_gaugepanel.getHeight())/(double)2-__c.DipToCurrent((int) (3)));
 //BA.debugLineNum = 137;BA.debugLine="x = cvsGauge.TargetRect.CenterX";
_x = _cvsgauge.getTargetRect().getCenterX();
 //BA.debugLineNum = 138;BA.debugLine="y = cvsGauge.TargetRect.CenterY";
_y = _cvsgauge.getTargetRect().getCenterY();
 //BA.debugLineNum = 139;BA.debugLine="AngleOffset = 135";
_angleoffset = (int) (135);
 //BA.debugLineNum = 140;BA.debugLine="AngleRange = 270";
_anglerange = (int) (270);
 //BA.debugLineNum = 141;BA.debugLine="IndicatorBaseWidth = 6dip";
_indicatorbasewidth = (float) (__c.DipToCurrent((int) (6)));
 //BA.debugLineNum = 142;BA.debugLine="CenterColor = IndicatorColor";
_centercolor = _indicatorcolor;
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
return "";
}
public String  _configurehalfcircle() throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Private Sub ConfigureHalfCircle";
 //BA.debugLineNum = 127;BA.debugLine="Radius = Min(GaugePanel.Width / 2, GaugePanel.Hei";
_radius = (float) (__c.Min(_gaugepanel.getWidth()/(double)2,_gaugepanel.getHeight()));
 //BA.debugLineNum = 128;BA.debugLine="x = cvsGauge.TargetRect.CenterX";
_x = _cvsgauge.getTargetRect().getCenterX();
 //BA.debugLineNum = 129;BA.debugLine="y = cvsGauge.TargetRect.Height";
_y = _cvsgauge.getTargetRect().getHeight();
 //BA.debugLineNum = 130;BA.debugLine="AngleOffset = 180";
_angleoffset = (int) (180);
 //BA.debugLineNum = 131;BA.debugLine="AngleRange = 180";
_anglerange = (int) (180);
 //BA.debugLineNum = 132;BA.debugLine="IndicatorBaseWidth = 20dip";
_indicatorbasewidth = (float) (__c.DipToCurrent((int) (20)));
 //BA.debugLineNum = 133;BA.debugLine="End Sub";
return "";
}
public cloyd.smart.home.monitor.gauge._gaugerange  _createrange(float _lowvalue,float _highvalue,int _color) throws Exception{
cloyd.smart.home.monitor.gauge._gaugerange _r = null;
 //BA.debugLineNum = 209;BA.debugLine="Public Sub CreateRange(LowValue As Float, HighValu";
 //BA.debugLineNum = 210;BA.debugLine="Dim r As GaugeRange";
_r = new cloyd.smart.home.monitor.gauge._gaugerange();
 //BA.debugLineNum = 211;BA.debugLine="r.Initialize";
_r.Initialize();
 //BA.debugLineNum = 212;BA.debugLine="r.LowValue = LowValue";
_r.LowValue /*float*/  = _lowvalue;
 //BA.debugLineNum = 213;BA.debugLine="r.HighValue = HighValue";
_r.HighValue /*float*/  = _highvalue;
 //BA.debugLineNum = 214;BA.debugLine="r.Color = Color";
_r.Color /*int*/  = _color;
 //BA.debugLineNum = 215;BA.debugLine="Return r";
if (true) return _r;
 //BA.debugLineNum = 216;BA.debugLine="End Sub";
return null;
}
public String  _designercreateview(Object _base,anywheresoftware.b4a.objects.LabelWrapper _lbl,anywheresoftware.b4a.objects.collections.Map _props) throws Exception{
anywheresoftware.b4a.objects.collections.Map _m = null;
Object _nativefont = null;
float _lblheight = 0f;
 //BA.debugLineNum = 43;BA.debugLine="Public Sub DesignerCreateView (Base As Object, Lbl";
 //BA.debugLineNum = 44;BA.debugLine="IndicatorLength = DipToCurrent(Props.Get(\"Indicat";
_indicatorlength = __c.DipToCurrent((int)(BA.ObjectToNumber(_props.Get((Object)("IndicatorLength")))));
 //BA.debugLineNum = 45;BA.debugLine="CenterColor = xui.PaintOrColorToColor(Props.Get(\"";
_centercolor = _xui.PaintOrColorToColor(_props.Get((Object)("CenterColor")));
 //BA.debugLineNum = 46;BA.debugLine="IndicatorColor = xui.PaintOrColorToColor(Props.Ge";
_indicatorcolor = _xui.PaintOrColorToColor(_props.Get((Object)("IndicatorColor")));
 //BA.debugLineNum = 47;BA.debugLine="BackgroundColor = xui.PaintOrColorToColor(Props.G";
_backgroundcolor = _xui.PaintOrColorToColor(_props.GetDefault((Object)("BackgroundColor"),(Object)(_xui.Color_White)));
 //BA.debugLineNum = 48;BA.debugLine="Dim m As Map = CreateMap(\"Half Circle\": HALF_CIRC";
_m = new anywheresoftware.b4a.objects.collections.Map();
_m = __c.createMap(new Object[] {(Object)("Half Circle"),(Object)(_half_circle),(Object)("Full Circle"),(Object)(_full_circle)});
 //BA.debugLineNum = 49;BA.debugLine="GaugeType = m.Get(Props.GetDefault(\"GaugeType\", \"";
_gaugetype = (int)(BA.ObjectToNumber(_m.Get(_props.GetDefault((Object)("GaugeType"),(Object)("Half Circle")))));
 //BA.debugLineNum = 50;BA.debugLine="MinValue = Props.Get(\"MinValue\")";
_minvalue = (float)(BA.ObjectToNumber(_props.Get((Object)("MinValue"))));
 //BA.debugLineNum = 51;BA.debugLine="MaxValue = Props.Get(\"MaxValue\")";
_maxvalue = (float)(BA.ObjectToNumber(_props.Get((Object)("MaxValue"))));
 //BA.debugLineNum = 52;BA.debugLine="PrefixText = Props.Get(\"PrefixText\")";
_prefixtext = BA.ObjectToString(_props.Get((Object)("PrefixText")));
 //BA.debugLineNum = 53;BA.debugLine="SuffixText = Props.Get(\"SuffixText\")";
_suffixtext = BA.ObjectToString(_props.Get((Object)("SuffixText")));
 //BA.debugLineNum = 54;BA.debugLine="mBase = Base";
_mbase.setObject((java.lang.Object)(_base));
 //BA.debugLineNum = 55;BA.debugLine="Dim NativeFont As Object";
_nativefont = new Object();
 //BA.debugLineNum = 61;BA.debugLine="NativeFont = Typeface.LoadFromAssets(\"Crysta.ttf\"";
_nativefont = (Object)(__c.Typeface.LoadFromAssets("Crysta.ttf"));
 //BA.debugLineNum = 65;BA.debugLine="GaugePanel = xui.CreatePanel(\"\")";
_gaugepanel = _xui.CreatePanel(ba,"");
 //BA.debugLineNum = 67;BA.debugLine="mBase.AddView(GaugePanel, 0, 0, mBase.Width, mBas";
_mbase.AddView((android.view.View)(_gaugepanel.getObject()),(int) (0),(int) (0),_mbase.getWidth(),_mbase.getHeight());
 //BA.debugLineNum = 68;BA.debugLine="cvsGauge.Initialize(GaugePanel)";
_cvsgauge.Initialize(_gaugepanel);
 //BA.debugLineNum = 69;BA.debugLine="IndicatorPanel = xui.CreatePanel(\"\")";
_indicatorpanel = _xui.CreatePanel(ba,"");
 //BA.debugLineNum = 70;BA.debugLine="mBase.AddView(IndicatorPanel, 0, 0, mBase.Width,";
_mbase.AddView((android.view.View)(_indicatorpanel.getObject()),(int) (0),(int) (0),_mbase.getWidth(),_mbase.getHeight());
 //BA.debugLineNum = 71;BA.debugLine="cvsIndicator.Initialize(IndicatorPanel)";
_cvsindicator.Initialize(_indicatorpanel);
 //BA.debugLineNum = 72;BA.debugLine="mlbl = Lbl";
_mlbl.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 73;BA.debugLine="mlbl.Font = xui.CreateFont(NativeFont, 30)";
_mlbl.setFont(_xui.CreateFont((android.graphics.Typeface)(_nativefont),(float) (30)));
 //BA.debugLineNum = 74;BA.debugLine="mlbl.SetTextAlignment(\"CENTER\", \"CENTER\")";
_mlbl.SetTextAlignment("CENTER","CENTER");
 //BA.debugLineNum = 75;BA.debugLine="mlbl.TextColor = xui.Color_Black";
_mlbl.setTextColor(_xui.Color_Black);
 //BA.debugLineNum = 76;BA.debugLine="Dim lblheight As Float";
_lblheight = 0f;
 //BA.debugLineNum = 77;BA.debugLine="If PrefixText.Contains(CRLF) Then lblheight = 65d";
if (_prefixtext.contains(__c.CRLF)) { 
_lblheight = (float) (__c.DipToCurrent((int) (65)));}
else {
_lblheight = (float) (__c.DipToCurrent((int) (35)));};
 //BA.debugLineNum = 78;BA.debugLine="mBase.AddView(mlbl, 0, 0, 0, lblheight) 'label si";
_mbase.AddView((android.view.View)(_mlbl.getObject()),(int) (0),(int) (0),(int) (0),(int) (_lblheight));
 //BA.debugLineNum = 79;BA.debugLine="Base_Resize(mBase.Width, mBase.Height)";
_base_resize(_mbase.getWidth(),_mbase.getHeight());
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return "";
}
public String  _drawbackground() throws Exception{
cloyd.smart.home.monitor.gauge._gaugerange _gr = null;
anywheresoftware.b4a.objects.B4XCanvas.B4XPath _p = null;
float _startangle = 0f;
 //BA.debugLineNum = 98;BA.debugLine="Private Sub DrawBackground";
 //BA.debugLineNum = 99;BA.debugLine="cvsGauge.ClearRect(cvsGauge.TargetRect)";
_cvsgauge.ClearRect(_cvsgauge.getTargetRect());
 //BA.debugLineNum = 100;BA.debugLine="Select GaugeType";
switch (BA.switchObjectToInt(_gaugetype,_half_circle,_full_circle)) {
case 0: {
 //BA.debugLineNum = 102;BA.debugLine="ConfigureHalfCircle";
_configurehalfcircle();
 break; }
case 1: {
 //BA.debugLineNum = 104;BA.debugLine="ConfigureFullCircle";
_configurefullcircle();
 break; }
}
;
 //BA.debugLineNum = 106;BA.debugLine="cvsGauge.DrawCircle(x, y, Radius + 1dip, Backgrou";
_cvsgauge.DrawCircle(_x,_y,(float) (_radius+__c.DipToCurrent((int) (1))),_backgroundcolor,__c.True,(float) (0));
 //BA.debugLineNum = 107;BA.debugLine="For Each gr As GaugeRange In mRanges";
{
final anywheresoftware.b4a.BA.IterableList group9 = _mranges;
final int groupLen9 = group9.getSize()
;int index9 = 0;
;
for (; index9 < groupLen9;index9++){
_gr = (cloyd.smart.home.monitor.gauge._gaugerange)(group9.Get(index9));
 //BA.debugLineNum = 108;BA.debugLine="Dim p As B4XPath";
_p = new anywheresoftware.b4a.objects.B4XCanvas.B4XPath();
 //BA.debugLineNum = 109;BA.debugLine="Dim StartAngle As Float = ValueToAngle(gr.LowVal";
_startangle = _valuetoangle(_gr.LowValue /*float*/ );
 //BA.debugLineNum = 110;BA.debugLine="p.InitializeArc(x, y, Radius, StartAngle, ValueT";
_p.InitializeArc(_x,_y,_radius,_startangle,(float) (_valuetoangle(_gr.HighValue /*float*/ )-_startangle));
 //BA.debugLineNum = 111;BA.debugLine="cvsGauge.ClipPath(p)";
_cvsgauge.ClipPath(_p);
 //BA.debugLineNum = 112;BA.debugLine="cvsGauge.DrawCircle(x, y, Radius, gr.Color, True";
_cvsgauge.DrawCircle(_x,_y,_radius,_gr.Color /*int*/ ,__c.True,(float) (0));
 //BA.debugLineNum = 113;BA.debugLine="cvsGauge.RemoveClip";
_cvsgauge.RemoveClip();
 }
};
 //BA.debugLineNum = 115;BA.debugLine="cvsGauge.DrawCircle(x, y, Radius - IndicatorLengt";
_cvsgauge.DrawCircle(_x,_y,(float) (_radius-_indicatorlength),_backgroundcolor,__c.True,(float) (0));
 //BA.debugLineNum = 116;BA.debugLine="If GaugeType = FULL_CIRCLE And PrefixText <> \"\" T";
if (_gaugetype==_full_circle && (_prefixtext).equals("") == false) { 
 //BA.debugLineNum = 117;BA.debugLine="cvsGauge.DrawText(PrefixText, x, y - 30dip, xui.";
_cvsgauge.DrawText(ba,_prefixtext,_x,(float) (_y-__c.DipToCurrent((int) (30))),_xui.CreateDefaultFont((float) (16)),_xui.Color_Black,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"));
 //BA.debugLineNum = 119;BA.debugLine="cvsGauge.DrawCircle(x, y, Radius + 1dip, xui.Col";
_cvsgauge.DrawCircle(_x,_y,(float) (_radius+__c.DipToCurrent((int) (1))),_xui.Color_Gray,__c.False,(float) (__c.DipToCurrent((int) (1))));
 };
 //BA.debugLineNum = 121;BA.debugLine="DrawTicks";
_drawticks();
 //BA.debugLineNum = 122;BA.debugLine="cvsGauge.Invalidate";
_cvsgauge.Invalidate();
 //BA.debugLineNum = 123;BA.debugLine="DrawIndicator(mCurrentValue)";
_drawindicator(_mcurrentvalue);
 //BA.debugLineNum = 124;BA.debugLine="End Sub";
return "";
}
public String  _drawindicator(float _value) throws Exception{
float _angle = 0f;
anywheresoftware.b4a.objects.B4XCanvas.B4XPath _p = null;
float _length = 0f;
float _circleradius = 0f;
String _s = "";
 //BA.debugLineNum = 171;BA.debugLine="Private Sub DrawIndicator (Value As Float)";
 //BA.debugLineNum = 172;BA.debugLine="cvsIndicator.ClearRect(cvsIndicator.TargetRect)";
_cvsindicator.ClearRect(_cvsindicator.getTargetRect());
 //BA.debugLineNum = 173;BA.debugLine="Dim angle As Float = ValueToAngle(Value)";
_angle = _valuetoangle(_value);
 //BA.debugLineNum = 174;BA.debugLine="Dim p As B4XPath";
_p = new anywheresoftware.b4a.objects.B4XCanvas.B4XPath();
 //BA.debugLineNum = 175;BA.debugLine="p.Initialize(x + IndicatorBaseWidth * SinD(angle)";
_p.Initialize((float) (_x+_indicatorbasewidth*__c.SinD(_angle)),(float) (_y-_indicatorbasewidth*__c.CosD(_angle)));
 //BA.debugLineNum = 176;BA.debugLine="Dim length As Float = Radius - 0.3 * IndicatorLen";
_length = (float) (_radius-0.3*_indicatorlength);
 //BA.debugLineNum = 177;BA.debugLine="p.LineTo(x + length * CosD(angle), y + length * S";
_p.LineTo((float) (_x+_length*__c.CosD(_angle)),(float) (_y+_length*__c.SinD(_angle)));
 //BA.debugLineNum = 178;BA.debugLine="p.LineTo(x - IndicatorBaseWidth * SinD(angle), y";
_p.LineTo((float) (_x-_indicatorbasewidth*__c.SinD(_angle)),(float) (_y+_indicatorbasewidth*__c.CosD(_angle)));
 //BA.debugLineNum = 179;BA.debugLine="cvsIndicator.ClipPath(p)";
_cvsindicator.ClipPath(_p);
 //BA.debugLineNum = 180;BA.debugLine="cvsIndicator.DrawRect(cvsIndicator.TargetRect, In";
_cvsindicator.DrawRect(_cvsindicator.getTargetRect(),_indicatorcolor,__c.True,(float) (0));
 //BA.debugLineNum = 181;BA.debugLine="cvsIndicator.RemoveClip";
_cvsindicator.RemoveClip();
 //BA.debugLineNum = 182;BA.debugLine="Dim CircleRadius As Float";
_circleradius = 0f;
 //BA.debugLineNum = 183;BA.debugLine="Dim s As String";
_s = "";
 //BA.debugLineNum = 184;BA.debugLine="If GaugeType = HALF_CIRCLE Then";
if (_gaugetype==_half_circle) { 
 //BA.debugLineNum = 185;BA.debugLine="s = PrefixText.ToUpperCase";
_s = _prefixtext.toUpperCase();
 //BA.debugLineNum = 186;BA.debugLine="CircleRadius =  Radius - IndicatorLength";
_circleradius = (float) (_radius-_indicatorlength);
 }else {
 //BA.debugLineNum = 188;BA.debugLine="CircleRadius = IndicatorBaseWidth";
_circleradius = _indicatorbasewidth;
 };
 //BA.debugLineNum = 190;BA.debugLine="cvsIndicator.DrawCircle(x, y, CircleRadius, Cente";
_cvsindicator.DrawCircle(_x,_y,_circleradius,_centercolor,__c.True,(float) (0));
 //BA.debugLineNum = 191;BA.debugLine="cvsIndicator.Invalidate";
_cvsindicator.Invalidate();
 //BA.debugLineNum = 193;BA.debugLine="mlbl.Text = s & NumberFormat2(Value, 1, 2, 0, Tru";
_mlbl.setText(BA.ObjectToCharSequence(_s+__c.NumberFormat2(_value,(int) (1),(int) (2),(int) (0),__c.True)+_suffixtext));
 //BA.debugLineNum = 194;BA.debugLine="End Sub";
return "";
}
public String  _drawticks() throws Exception{
int _r1 = 0;
int _longtick = 0;
int _shorttick = 0;
int _numberofticks = 0;
int _i = 0;
int _thickness = 0;
int _r = 0;
float _angle = 0f;
float _tr = 0f;
 //BA.debugLineNum = 146;BA.debugLine="Private Sub DrawTicks";
 //BA.debugLineNum = 147;BA.debugLine="Dim r1 As Int = Radius - 2dip";
_r1 = (int) (_radius-__c.DipToCurrent((int) (2)));
 //BA.debugLineNum = 148;BA.debugLine="Dim LongTick As Int = r1 - 7dip";
_longtick = (int) (_r1-__c.DipToCurrent((int) (7)));
 //BA.debugLineNum = 149;BA.debugLine="Dim ShortTick As Int = r1 - 5dip";
_shorttick = (int) (_r1-__c.DipToCurrent((int) (5)));
 //BA.debugLineNum = 150;BA.debugLine="Dim NumberOfTicks As Int";
_numberofticks = 0;
 //BA.debugLineNum = 151;BA.debugLine="If GaugeType = HALF_CIRCLE Then NumberOfTicks = 1";
if (_gaugetype==_half_circle) { 
_numberofticks = (int) (10);}
else {
_numberofticks = (int) (20);};
 //BA.debugLineNum = 152;BA.debugLine="For i = 0 To NumberOfTicks";
{
final int step6 = 1;
final int limit6 = _numberofticks;
_i = (int) (0) ;
for (;_i <= limit6 ;_i = _i + step6 ) {
 //BA.debugLineNum = 153;BA.debugLine="Dim thickness, r As Int";
_thickness = 0;
_r = 0;
 //BA.debugLineNum = 154;BA.debugLine="Dim angle As Float = i * AngleRange / NumberOfTi";
_angle = (float) (_i*_anglerange/(double)_numberofticks+_angleoffset);
 //BA.debugLineNum = 155;BA.debugLine="If i Mod 5 = 0 Then";
if (_i%5==0) { 
 //BA.debugLineNum = 156;BA.debugLine="thickness = 3dip";
_thickness = __c.DipToCurrent((int) (3));
 //BA.debugLineNum = 157;BA.debugLine="r = LongTick";
_r = _longtick;
 //BA.debugLineNum = 158;BA.debugLine="Dim tr As Float = r - 12dip";
_tr = (float) (_r-__c.DipToCurrent((int) (12)));
 //BA.debugLineNum = 159;BA.debugLine="cvsGauge.DrawTextRotated(NumberFormat(MinValue";
_cvsgauge.DrawTextRotated(ba,__c.NumberFormat(_minvalue+_i/(double)_numberofticks*(_maxvalue-_minvalue),(int) (1),(int) (0)),(float) (_x+_tr*__c.CosD(_angle)),(float) (_y+_tr*__c.SinD(_angle)),_xui.CreateDefaultFont((float) (10)),_xui.Color_Black,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"),(float) (_angle+90));
 }else {
 //BA.debugLineNum = 162;BA.debugLine="thickness = 1dip";
_thickness = __c.DipToCurrent((int) (1));
 //BA.debugLineNum = 163;BA.debugLine="r = ShortTick";
_r = _shorttick;
 };
 //BA.debugLineNum = 165;BA.debugLine="cvsGauge.DrawLine(x + r * CosD(angle), y + r * S";
_cvsgauge.DrawLine((float) (_x+_r*__c.CosD(_angle)),(float) (_y+_r*__c.SinD(_angle)),(float) (_x+_r1*__c.CosD(_angle)),(float) (_y+_r1*__c.SinD(_angle)),_xui.Color_Black,(float) (_thickness));
 }
};
 //BA.debugLineNum = 167;BA.debugLine="End Sub";
return "";
}
public float  _getcurrentvalue() throws Exception{
 //BA.debugLineNum = 223;BA.debugLine="Public Sub getCurrentValue As Float";
 //BA.debugLineNum = 224;BA.debugLine="Return mCurrentValue";
if (true) return _mcurrentvalue;
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
return 0f;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,Object _callback,String _eventname) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 36;BA.debugLine="Public Sub Initialize (Callback As Object, EventNa";
 //BA.debugLineNum = 37;BA.debugLine="mEventName = EventName";
_meventname = _eventname;
 //BA.debugLineNum = 38;BA.debugLine="mCallBack = Callback";
_mcallback = _callback;
 //BA.debugLineNum = 39;BA.debugLine="mRanges.Initialize";
_mranges.Initialize();
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}
public String  _setcurrentvalue(float _v) throws Exception{
float _newvalue = 0f;
 //BA.debugLineNum = 218;BA.debugLine="Public Sub setCurrentValue (v As Float)";
 //BA.debugLineNum = 219;BA.debugLine="Dim NewValue As Float = Min(MaxValue, Max(MinValu";
_newvalue = (float) (__c.Min(_maxvalue,__c.Max(_minvalue,_v)));
 //BA.debugLineNum = 220;BA.debugLine="AnimateValueTo(NewValue)";
_animatevalueto(_newvalue);
 //BA.debugLineNum = 221;BA.debugLine="End Sub";
return "";
}
public String  _setminandmax(float _newminvalue,float _newmaxvalue) throws Exception{
 //BA.debugLineNum = 92;BA.debugLine="Public Sub SetMinAndMax(NewMinValue As Float, NewM";
 //BA.debugLineNum = 93;BA.debugLine="MinValue = NewMinValue";
_minvalue = _newminvalue;
 //BA.debugLineNum = 94;BA.debugLine="MaxValue = NewMaxValue";
_maxvalue = _newmaxvalue;
 //BA.debugLineNum = 95;BA.debugLine="DrawBackground";
_drawbackground();
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public String  _setranges(anywheresoftware.b4a.objects.collections.List _ranges) throws Exception{
cloyd.smart.home.monitor.gauge._gaugerange _r = null;
 //BA.debugLineNum = 200;BA.debugLine="Public Sub SetRanges(Ranges As List)";
 //BA.debugLineNum = 201;BA.debugLine="mRanges = Ranges";
_mranges = _ranges;
 //BA.debugLineNum = 202;BA.debugLine="For Each r As GaugeRange In Ranges";
{
final anywheresoftware.b4a.BA.IterableList group2 = _ranges;
final int groupLen2 = group2.getSize()
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_r = (cloyd.smart.home.monitor.gauge._gaugerange)(group2.Get(index2));
 //BA.debugLineNum = 203;BA.debugLine="r.Color = Bit.And(0x00ffffff, r.Color)";
_r.Color /*int*/  = __c.Bit.And((int) (0x00ffffff),_r.Color /*int*/ );
 //BA.debugLineNum = 204;BA.debugLine="r.Color = Bit.Or(0x88000000, r.Color)";
_r.Color /*int*/  = __c.Bit.Or((int) (0x88000000),_r.Color /*int*/ );
 }
};
 //BA.debugLineNum = 206;BA.debugLine="DrawBackground";
_drawbackground();
 //BA.debugLineNum = 207;BA.debugLine="End Sub";
return "";
}
public float  _valuetoangle(float _value) throws Exception{
 //BA.debugLineNum = 196;BA.debugLine="Private Sub ValueToAngle (Value As Float) As Float";
 //BA.debugLineNum = 197;BA.debugLine="Return (Value - MinValue) / (MaxValue - MinValue)";
if (true) return (float) ((_value-_minvalue)/(double)(_maxvalue-_minvalue)*_anglerange+_angleoffset);
 //BA.debugLineNum = 198;BA.debugLine="End Sub";
return 0f;
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
