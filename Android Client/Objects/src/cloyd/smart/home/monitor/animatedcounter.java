package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class animatedcounter extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.animatedcounter");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.animatedcounter.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _meventname = "";
public Object _mcallback = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mbase = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public anywheresoftware.b4a.objects.collections.List _imageviews = null;
public int _mdigits = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper _lbltemplate = null;
public anywheresoftware.b4a.objects.collections.List _mvalue = null;
public int _digitheight = 0;
public int _digitwidth = 0;
public int _mduration = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _fade = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _xfadeiv = null;
public Object _tag = null;
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
int _columns = 0;
anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _bmp = null;
int _left = 0;
int _i = 0;
anywheresoftware.b4a.objects.B4XViewWrapper _iv = null;
 //BA.debugLineNum = 70;BA.debugLine="Private Sub Base_Resize (Width As Double, Height A";
 //BA.debugLineNum = 71;BA.debugLine="mBase.GetView(0).SetLayoutAnimated(0, 0, 0, Width";
_mbase.GetView((int) (0)).SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),(int) (_height));
 //BA.debugLineNum = 72;BA.debugLine="xfadeIv.SetLayoutAnimated(0, 0, 0, Width, Height)";
_xfadeiv.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),(int) (_height));
 //BA.debugLineNum = 73;BA.debugLine="xfadeIv.SetBitmap(fade.Resize(Width, Height, Fals";
_xfadeiv.SetBitmap((android.graphics.Bitmap)(_fade.Resize((int) (_width),(int) (_height),__c.False).getObject()));
 //BA.debugLineNum = 74;BA.debugLine="DigitHeight = Height";
_digitheight = (int) (_height);
 //BA.debugLineNum = 75;BA.debugLine="Dim Columns As Int = mdigits";
_columns = _mdigits;
 //BA.debugLineNum = 76;BA.debugLine="DigitWidth = Width / Columns";
_digitwidth = (int) (_width/(double)_columns);
 //BA.debugLineNum = 77;BA.debugLine="Dim bmp As B4XBitmap = CreateBitmap(lblTemplate)";
_bmp = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
_bmp = _createbitmap(_lbltemplate);
 //BA.debugLineNum = 78;BA.debugLine="Dim left As Int = Width";
_left = (int) (_width);
 //BA.debugLineNum = 79;BA.debugLine="For i = 0 To ImageViews.Size - 1";
{
final int step9 = 1;
final int limit9 = (int) (_imageviews.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit9 ;_i = _i + step9 ) {
 //BA.debugLineNum = 80;BA.debugLine="Dim iv As B4XView = ImageViews.Get(i)";
_iv = new anywheresoftware.b4a.objects.B4XViewWrapper();
_iv.setObject((java.lang.Object)(_imageviews.Get(_i)));
 //BA.debugLineNum = 82;BA.debugLine="left = left - DigitWidth";
_left = (int) (_left-_digitwidth);
 //BA.debugLineNum = 83;BA.debugLine="iv.SetLayoutAnimated(0, left, TopFromValue(i), D";
_iv.SetLayoutAnimated((int) (0),_left,_topfromvalue(_i),_digitwidth,(int) (_digitheight*10));
 //BA.debugLineNum = 84;BA.debugLine="iv.SetBitmap(bmp)";
_iv.SetBitmap((android.graphics.Bitmap)(_bmp.getObject()));
 }
};
 //BA.debugLineNum = 86;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 5;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 6;BA.debugLine="Private mEventName As String 'ignore";
_meventname = "";
 //BA.debugLineNum = 7;BA.debugLine="Private mCallBack As Object 'ignore";
_mcallback = new Object();
 //BA.debugLineNum = 8;BA.debugLine="Public mBase As B4XView 'ignore";
_mbase = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 9;BA.debugLine="Private xui As XUI 'ignore";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 10;BA.debugLine="Private ImageViews As List";
_imageviews = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 11;BA.debugLine="Private mdigits As Int";
_mdigits = 0;
 //BA.debugLineNum = 12;BA.debugLine="Private lblTemplate As B4XView";
_lbltemplate = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Private mValue As List";
_mvalue = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 14;BA.debugLine="Private DigitHeight, DigitWidth As Int";
_digitheight = 0;
_digitwidth = 0;
 //BA.debugLineNum = 15;BA.debugLine="Private mDuration As Int";
_mduration = 0;
 //BA.debugLineNum = 16;BA.debugLine="Private fade As B4XBitmap";
_fade = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Private xfadeIv As B4XView";
_xfadeiv = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Public Tag As Object";
_tag = new Object();
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper  _createbitmap(anywheresoftware.b4a.objects.B4XViewWrapper _lbl) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
anywheresoftware.b4a.objects.B4XCanvas.B4XRect _r = null;
int _baseline = 0;
int _i = 0;
anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _res = null;
 //BA.debugLineNum = 93;BA.debugLine="Private Sub CreateBitmap (lbl As B4XView) As B4XBi";
 //BA.debugLineNum = 94;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _xui.CreatePanel(ba,"");
 //BA.debugLineNum = 95;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, DigitWidth, DigitHei";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_digitwidth,(int) (_digitheight*10));
 //BA.debugLineNum = 96;BA.debugLine="Dim cvs As B4XCanvas";
_cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 97;BA.debugLine="cvs.Initialize(p)";
_cvs.Initialize(_p);
 //BA.debugLineNum = 98;BA.debugLine="Dim r As B4XRect = cvs.MeasureText(\"5\", lbl.Font)";
_r = _cvs.MeasureText("5",_lbl.getFont());
 //BA.debugLineNum = 99;BA.debugLine="Dim BaseLine As Int = DigitHeight / 2 - r.Height";
_baseline = (int) (_digitheight/(double)2-_r.getHeight()/(double)2-_r.getTop());
 //BA.debugLineNum = 100;BA.debugLine="For i = 0 To 9";
{
final int step7 = 1;
final int limit7 = (int) (9);
_i = (int) (0) ;
for (;_i <= limit7 ;_i = _i + step7 ) {
 //BA.debugLineNum = 101;BA.debugLine="cvs.DrawText(i, DigitWidth / 2, i * DigitHeight";
_cvs.DrawText(ba,BA.NumberToString(_i),(float) (_digitwidth/(double)2),(float) (_i*_digitheight+_baseline),_lbl.getFont(),_lbl.getTextColor(),BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"));
 }
};
 //BA.debugLineNum = 103;BA.debugLine="cvs.Invalidate";
_cvs.Invalidate();
 //BA.debugLineNum = 104;BA.debugLine="Dim res As B4XBitmap = cvs.CreateBitmap";
_res = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
_res = _cvs.CreateBitmap();
 //BA.debugLineNum = 105;BA.debugLine="cvs.Release";
_cvs.Release();
 //BA.debugLineNum = 106;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper  _createfadebitmap(int _clr) throws Exception{
b4a.example.bitmapcreator _bc = null;
anywheresoftware.b4a.objects.B4XCanvas.B4XRect _r = null;
int _tclr = 0;
 //BA.debugLineNum = 56;BA.debugLine="Private Sub CreateFadeBitmap (clr As Int) As B4XBi";
 //BA.debugLineNum = 57;BA.debugLine="Dim bc As BitmapCreator";
_bc = new b4a.example.bitmapcreator();
 //BA.debugLineNum = 58;BA.debugLine="bc.Initialize(200, 50)";
_bc._initialize(ba,(int) (200),(int) (50));
 //BA.debugLineNum = 59;BA.debugLine="Dim r As B4XRect";
_r = new anywheresoftware.b4a.objects.B4XCanvas.B4XRect();
 //BA.debugLineNum = 60;BA.debugLine="r.Initialize(0, 0, bc.mWidth, bc.mHeight / 3)";
_r.Initialize((float) (0),(float) (0),(float) (_bc._mwidth),(float) (_bc._mheight/(double)3));
 //BA.debugLineNum = 61;BA.debugLine="Dim tclr As Int = Bit.And(0x00ffffff, clr)";
_tclr = __c.Bit.And((int) (0x00ffffff),_clr);
 //BA.debugLineNum = 62;BA.debugLine="bc.FillGradient(Array As Int(clr, tclr), r, \"TOP_";
_bc._fillgradient(new int[]{_clr,_tclr},_r,"TOP_BOTTOM");
 //BA.debugLineNum = 63;BA.debugLine="r.Top = bc.mHeight * 2 / 3";
_r.setTop((float) (_bc._mheight*2/(double)3));
 //BA.debugLineNum = 64;BA.debugLine="r.Bottom = bc.mHeight";
_r.setBottom((float) (_bc._mheight));
 //BA.debugLineNum = 65;BA.debugLine="bc.FillGradient(Array As Int(clr, tclr), r, \"BOTT";
_bc._fillgradient(new int[]{_clr,_tclr},_r,"BOTTOM_TOP");
 //BA.debugLineNum = 66;BA.debugLine="Return bc.Bitmap";
if (true) return _bc._getbitmap();
 //BA.debugLineNum = 67;BA.debugLine="End Sub";
return null;
}
public String  _designercreateview(Object _base,anywheresoftware.b4a.objects.LabelWrapper _lbl,anywheresoftware.b4a.objects.collections.Map _props) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _pnl = null;
int _i = 0;
anywheresoftware.b4a.objects.ImageViewWrapper _iv = null;
anywheresoftware.b4a.objects.ImageViewWrapper _fadeiv = null;
 //BA.debugLineNum = 30;BA.debugLine="Public Sub DesignerCreateView (Base As Object, lbl";
 //BA.debugLineNum = 31;BA.debugLine="mBase = Base";
_mbase.setObject((java.lang.Object)(_base));
 //BA.debugLineNum = 32;BA.debugLine="Tag = mBase.Tag : mBase.Tag = Me";
_tag = _mbase.getTag();
 //BA.debugLineNum = 32;BA.debugLine="Tag = mBase.Tag : mBase.Tag = Me";
_mbase.setTag(this);
 //BA.debugLineNum = 33;BA.debugLine="Dim pnl As B4XView = xui.CreatePanel(\"\") 'needed";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = _xui.CreatePanel(ba,"");
 //BA.debugLineNum = 34;BA.debugLine="mBase.AddView(pnl, 0, 0, 0, 0)";
_mbase.AddView((android.view.View)(_pnl.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 35;BA.debugLine="mdigits = Props.Get(\"Digits\")";
_mdigits = (int)(BA.ObjectToNumber(_props.Get((Object)("Digits"))));
 //BA.debugLineNum = 36;BA.debugLine="mDuration = Props.Get(\"Duration\")";
_mduration = (int)(BA.ObjectToNumber(_props.Get((Object)("Duration"))));
 //BA.debugLineNum = 37;BA.debugLine="lblTemplate = lbl";
_lbltemplate.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 38;BA.debugLine="fade = CreateFadeBitmap(xui.PaintOrColorToColor(P";
_fade = _createfadebitmap(_xui.PaintOrColorToColor(_props.GetDefault((Object)("FadeColor"),(Object)(_xui.Color_White))));
 //BA.debugLineNum = 39;BA.debugLine="For i = 0 To mdigits - 1";
{
final int step10 = 1;
final int limit10 = (int) (_mdigits-1);
_i = (int) (0) ;
for (;_i <= limit10 ;_i = _i + step10 ) {
 //BA.debugLineNum = 40;BA.debugLine="Dim iv As ImageView";
_iv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 41;BA.debugLine="iv.Initialize(\"\")";
_iv.Initialize(ba,"");
 //BA.debugLineNum = 42;BA.debugLine="ImageViews.Add(iv)";
_imageviews.Add((Object)(_iv.getObject()));
 //BA.debugLineNum = 43;BA.debugLine="mBase.GetView(0).AddView(iv, 0, 0, 0, 0)";
_mbase.GetView((int) (0)).AddView((android.view.View)(_iv.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 }
};
 //BA.debugLineNum = 45;BA.debugLine="Dim fadeIv As ImageView";
_fadeiv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 46;BA.debugLine="fadeIv.Initialize(\"\")";
_fadeiv.Initialize(ba,"");
 //BA.debugLineNum = 47;BA.debugLine="xfadeIv = fadeIv";
_xfadeiv.setObject((java.lang.Object)(_fadeiv.getObject()));
 //BA.debugLineNum = 48;BA.debugLine="mBase.GetView(0).AddView(fadeIv, 0, 0, 0, 0)";
_mbase.GetView((int) (0)).AddView((android.view.View)(_fadeiv.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 49;BA.debugLine="setValue(0)";
_setvalue((int) (0));
 //BA.debugLineNum = 50;BA.debugLine="If xui.IsB4A Then";
if (_xui.getIsB4A()) { 
 //BA.debugLineNum = 51;BA.debugLine="Base_Resize(mBase.Width, mBase.Height)";
_base_resize(_mbase.getWidth(),_mbase.getHeight());
 //BA.debugLineNum = 52;BA.debugLine="setValue(getValue)";
_setvalue(_getvalue());
 };
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public int  _getvalue() throws Exception{
int _res = 0;
int _i = 0;
 //BA.debugLineNum = 120;BA.debugLine="Public Sub getValue As Int";
 //BA.debugLineNum = 121;BA.debugLine="Dim res As Int";
_res = 0;
 //BA.debugLineNum = 122;BA.debugLine="For i = 0 To mValue.Size - 1";
{
final int step2 = 1;
final int limit2 = (int) (_mvalue.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 123;BA.debugLine="res = res + mValue.Get(i) * Power(10, i)";
_res = (int) (_res+(double)(BA.ObjectToNumber(_mvalue.Get(_i)))*__c.Power(10,_i));
 }
};
 //BA.debugLineNum = 125;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 126;BA.debugLine="End Sub";
return 0;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,Object _callback,String _eventname) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 22;BA.debugLine="Public Sub Initialize (Callback As Object, EventNa";
 //BA.debugLineNum = 23;BA.debugLine="mEventName = EventName";
_meventname = _eventname;
 //BA.debugLineNum = 24;BA.debugLine="mCallBack = Callback";
_mcallback = _callback;
 //BA.debugLineNum = 25;BA.debugLine="ImageViews.Initialize";
_imageviews.Initialize();
 //BA.debugLineNum = 26;BA.debugLine="mValue.Initialize";
_mvalue.Initialize();
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public String  _setvalue(int _v) throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.B4XViewWrapper _iv = null;
 //BA.debugLineNum = 109;BA.debugLine="Public Sub setValue(v As Int)";
 //BA.debugLineNum = 110;BA.debugLine="mValue.Clear";
_mvalue.Clear();
 //BA.debugLineNum = 111;BA.debugLine="For i = 0 To mdigits - 1";
{
final int step2 = 1;
final int limit2 = (int) (_mdigits-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 112;BA.debugLine="mValue.Add(v Mod 10)";
_mvalue.Add((Object)(_v%10));
 //BA.debugLineNum = 113;BA.debugLine="v = v / 10";
_v = (int) (_v/(double)10);
 //BA.debugLineNum = 114;BA.debugLine="Dim iv As B4XView = ImageViews.Get(i)";
_iv = new anywheresoftware.b4a.objects.B4XViewWrapper();
_iv.setObject((java.lang.Object)(_imageviews.Get(_i)));
 //BA.debugLineNum = 115;BA.debugLine="iv.SetLayoutAnimated(mDuration, iv.Left, TopFrom";
_iv.SetLayoutAnimated(_mduration,_iv.getLeft(),_topfromvalue(_i),(int) (__c.Max(1,_iv.getWidth())),(int) (__c.Max(1,_iv.getHeight())));
 }
};
 //BA.debugLineNum = 118;BA.debugLine="End Sub";
return "";
}
public int  _topfromvalue(int _digit) throws Exception{
int _d = 0;
 //BA.debugLineNum = 88;BA.debugLine="Private Sub TopFromValue (Digit As Int) As Int";
 //BA.debugLineNum = 89;BA.debugLine="Dim d As Int = mValue.Get(Digit)";
_d = (int)(BA.ObjectToNumber(_mvalue.Get(_digit)));
 //BA.debugLineNum = 90;BA.debugLine="Return -d * DigitHeight";
if (true) return (int) (-_d*_digitheight);
 //BA.debugLineNum = 91;BA.debugLine="End Sub";
return 0;
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
