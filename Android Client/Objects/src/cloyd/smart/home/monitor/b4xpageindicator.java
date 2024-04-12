package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xpageindicator extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.b4xpageindicator");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.b4xpageindicator.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _meventname = "";
public Object _mcallback = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mbase = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public int _clr = 0;
public b4a.example.bitmapcreator _bc = null;
public int _mcurrentpage = 0;
public int _mcount = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper _iv = null;
public b4a.example.bcpath._bcbrush _transparent = null;
public b4a.example.bcpath._bcbrush _activebrush = null;
public b4a.example.bcpath._bcbrush _inactivebrush = null;
public int _r = 0;
public int _gap = 0;
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
 //BA.debugLineNum = 40;BA.debugLine="Private Sub Base_Resize (Width As Double, Height A";
 //BA.debugLineNum = 41;BA.debugLine="If bc.IsInitialized = False Or bc.mWidth <> Width";
if (_bc.IsInitialized()==__c.False || _bc._mwidth!=_width) { 
 //BA.debugLineNum = 42;BA.debugLine="bc.Initialize(Width, 20dip)";
_bc._initialize(ba,(int) (_width),__c.DipToCurrent((int) (20)));
 //BA.debugLineNum = 43;BA.debugLine="Transparent = bc.CreateBrushFromColor(xui.Color_";
_transparent = _bc._createbrushfromcolor(_xui.Color_Transparent);
 //BA.debugLineNum = 44;BA.debugLine="ActiveBrush = bc.CreateBrushFromColor(clr)";
_activebrush = _bc._createbrushfromcolor(_clr);
 //BA.debugLineNum = 45;BA.debugLine="InactiveBrush = bc.CreateBrushFromColor(Bit.Or(0";
_inactivebrush = _bc._createbrushfromcolor(__c.Bit.Or((int) (0x55000000),__c.Bit.And((int) (0x00ffffff),_clr)));
 };
 //BA.debugLineNum = 47;BA.debugLine="iv.SetLayoutAnimated(0, 0, 0, Width, bc.mHeight)";
_iv.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),_bc._mheight);
 //BA.debugLineNum = 48;BA.debugLine="Draw";
_draw();
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 4;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 5;BA.debugLine="Private mEventName As String 'ignore";
_meventname = "";
 //BA.debugLineNum = 6;BA.debugLine="Private mCallBack As Object 'ignore";
_mcallback = new Object();
 //BA.debugLineNum = 7;BA.debugLine="Public mBase As B4XView 'ignore";
_mbase = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 8;BA.debugLine="Private xui As XUI 'ignore";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 9;BA.debugLine="Private clr As Int";
_clr = 0;
 //BA.debugLineNum = 10;BA.debugLine="Private bc As BitmapCreator";
_bc = new b4a.example.bitmapcreator();
 //BA.debugLineNum = 11;BA.debugLine="Private mCurrentPage As Int";
_mcurrentpage = 0;
 //BA.debugLineNum = 12;BA.debugLine="Private mCount As Int";
_mcount = 0;
 //BA.debugLineNum = 13;BA.debugLine="Private iv As B4XView";
_iv = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Private Transparent As BCBrush";
_transparent = new b4a.example.bcpath._bcbrush();
 //BA.debugLineNum = 15;BA.debugLine="Private ActiveBrush, InactiveBrush As BCBrush";
_activebrush = new b4a.example.bcpath._bcbrush();
_inactivebrush = new b4a.example.bcpath._bcbrush();
 //BA.debugLineNum = 16;BA.debugLine="Private r As Int = 5dip";
_r = __c.DipToCurrent((int) (5));
 //BA.debugLineNum = 17;BA.debugLine="Private gap As Int = 15dip";
_gap = __c.DipToCurrent((int) (15));
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
public String  _designercreateview(Object _base,anywheresoftware.b4a.objects.LabelWrapper _lbl,anywheresoftware.b4a.objects.collections.Map _props) throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
 //BA.debugLineNum = 26;BA.debugLine="Public Sub DesignerCreateView (Base As Object, Lbl";
 //BA.debugLineNum = 27;BA.debugLine="mBase = Base";
_mbase.setObject((java.lang.Object)(_base));
 //BA.debugLineNum = 28;BA.debugLine="mBase.BringToFront";
_mbase.BringToFront();
 //BA.debugLineNum = 29;BA.debugLine="clr = xui.PaintOrColorToColor(Props.Get(\"Color\"))";
_clr = _xui.PaintOrColorToColor(_props.Get((Object)("Color")));
 //BA.debugLineNum = 30;BA.debugLine="mCount = Max(1, Props.Get(\"Page Count\"))";
_mcount = (int) (__c.Max(1,(double)(BA.ObjectToNumber(_props.Get((Object)("Page Count"))))));
 //BA.debugLineNum = 31;BA.debugLine="Dim ImageView1 As ImageView";
_imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 32;BA.debugLine="ImageView1.Initialize(\"\")";
_imageview1.Initialize(ba,"");
 //BA.debugLineNum = 33;BA.debugLine="iv = ImageView1";
_iv.setObject((java.lang.Object)(_imageview1.getObject()));
 //BA.debugLineNum = 34;BA.debugLine="mBase.AddView(iv, 0, 0, 0, 0)";
_mbase.AddView((android.view.View)(_iv.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 35;BA.debugLine="If mBase.Width > 0 Then";
if (_mbase.getWidth()>0) { 
 //BA.debugLineNum = 36;BA.debugLine="Base_Resize(mBase.Width, mBase.Height)";
_base_resize(_mbase.getWidth(),_mbase.getHeight());
 };
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public String  _draw() throws Exception{
int _totalwidth = 0;
int _startx = 0;
int _i = 0;
b4a.example.bcpath._bcbrush _brush = null;
 //BA.debugLineNum = 51;BA.debugLine="Private Sub Draw";
 //BA.debugLineNum = 52;BA.debugLine="bc.DrawRect2(bc.TargetRect, Transparent, True, 0)";
_bc._drawrect2(_bc._targetrect,_transparent,__c.True,(int) (0));
 //BA.debugLineNum = 53;BA.debugLine="Dim TotalWidth As Int = (mCount - 1) * gap";
_totalwidth = (int) ((_mcount-1)*_gap);
 //BA.debugLineNum = 54;BA.debugLine="Dim StartX As Int = bc.mWidth / 2 - TotalWidth /";
_startx = (int) (_bc._mwidth/(double)2-_totalwidth/(double)2);
 //BA.debugLineNum = 55;BA.debugLine="For i = 0 To mCount - 1";
{
final int step4 = 1;
final int limit4 = (int) (_mcount-1);
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 56;BA.debugLine="Dim brush As BCBrush";
_brush = new b4a.example.bcpath._bcbrush();
 //BA.debugLineNum = 57;BA.debugLine="If mCurrentPage = i Then brush = ActiveBrush Els";
if (_mcurrentpage==_i) { 
_brush = _activebrush;}
else {
_brush = _inactivebrush;};
 //BA.debugLineNum = 58;BA.debugLine="bc.DrawCircle2(StartX + gap * i, bc.mHeight / 2,";
_bc._drawcircle2((float) (_startx+_gap*_i),(float) (_bc._mheight/(double)2),(float) (_r),_brush,__c.True,(int) (0));
 }
};
 //BA.debugLineNum = 60;BA.debugLine="bc.SetBitmapToImageView(bc.Bitmap, iv)";
_bc._setbitmaptoimageview(_bc._getbitmap(),_iv);
 //BA.debugLineNum = 61;BA.debugLine="End Sub";
return "";
}
public int  _getcount() throws Exception{
 //BA.debugLineNum = 72;BA.debugLine="Public Sub getCount As Int";
 //BA.debugLineNum = 73;BA.debugLine="Return mCount";
if (true) return _mcount;
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return 0;
}
public int  _getcurrentpage() throws Exception{
 //BA.debugLineNum = 63;BA.debugLine="Public Sub getCurrentPage As Int";
 //BA.debugLineNum = 64;BA.debugLine="Return mCurrentPage";
if (true) return _mcurrentpage;
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return 0;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,Object _callback,String _eventname) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 20;BA.debugLine="Public Sub Initialize (Callback As Object, EventNa";
 //BA.debugLineNum = 21;BA.debugLine="mEventName = EventName";
_meventname = _eventname;
 //BA.debugLineNum = 22;BA.debugLine="mCallBack = Callback";
_mcallback = _callback;
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public String  _setcount(int _c) throws Exception{
 //BA.debugLineNum = 76;BA.debugLine="Public Sub setCount(c As Int)";
 //BA.debugLineNum = 77;BA.debugLine="mCount = c";
_mcount = _c;
 //BA.debugLineNum = 78;BA.debugLine="Draw";
_draw();
 //BA.debugLineNum = 79;BA.debugLine="End Sub";
return "";
}
public String  _setcurrentpage(int _i) throws Exception{
 //BA.debugLineNum = 67;BA.debugLine="Public Sub setCurrentPage(i As Int)";
 //BA.debugLineNum = 68;BA.debugLine="mCurrentPage = i";
_mcurrentpage = _i;
 //BA.debugLineNum = 69;BA.debugLine="Draw";
_draw();
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
