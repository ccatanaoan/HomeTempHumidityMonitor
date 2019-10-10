package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xdialog extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.b4xdialog");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.b4xdialog.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public int _buttonsheight = 0;
public int _titlebarheight = 0;
public int _buttonwidth = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper _base = null;
public int _backgroundcolor = 0;
public int _overlaycolor = 0;
public int _bordercolor = 0;
public int _bordercornersradius = 0;
public int _borderwidth = 0;
public int _buttonscolor = 0;
public int _buttonstextcolor = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper _background = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _blurimageview = null;
public boolean _blurbackground = false;
public int _blurreducescale = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper _mparent = null;
public Object _title = null;
public int _titlebarcolor = 0;
public int _titlebartextcolor = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper _titlebar = null;
public boolean _putattop = false;
public anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont _titlebarfont = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont _buttonsfont = null;
public int _buttonstextcolordisabled = 0;
public int _visibleanimationduration = 0;
public int[] _buttonsorder = null;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public String  _background_click() throws Exception{
 //BA.debugLineNum = 249;BA.debugLine="Private Sub Background_Click";
 //BA.debugLineNum = 251;BA.debugLine="End Sub";
return "";
}
public String  _background_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 244;BA.debugLine="Private Sub Background_Touch (Action As Int, X As";
 //BA.debugLineNum = 246;BA.debugLine="End Sub";
return "";
}
public b4a.example.bitmapcreator  _blur(anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _bmp) throws Exception{
long _n = 0L;
b4a.example.bitmapcreator _bc = null;
int _reducescale = 0;
int _count = 0;
b4a.example.bitmapcreator._argbcolor[] _clrs = null;
b4a.example.bitmapcreator._argbcolor _temp = null;
int _m = 0;
int _steps = 0;
int _y = 0;
int _x = 0;
 //BA.debugLineNum = 278;BA.debugLine="Private Sub Blur (bmp As B4XBitmap) As BitmapCreat";
 //BA.debugLineNum = 279;BA.debugLine="Dim n As Long = DateTime.Now";
_n = __c.DateTime.getNow();
 //BA.debugLineNum = 280;BA.debugLine="Dim bc As BitmapCreator";
_bc = new b4a.example.bitmapcreator();
 //BA.debugLineNum = 281;BA.debugLine="Dim ReduceScale As Int = BlurReduceScale";
_reducescale = _blurreducescale;
 //BA.debugLineNum = 282;BA.debugLine="bc.Initialize(bmp.Width / ReduceScale / bmp.Scale";
_bc._initialize(ba,(int) (_bmp.getWidth()/(double)_reducescale/(double)_bmp.getScale()),(int) (_bmp.getHeight()/(double)_reducescale/(double)_bmp.getScale()));
 //BA.debugLineNum = 283;BA.debugLine="bc.CopyPixelsFromBitmap(bmp)";
_bc._copypixelsfrombitmap(_bmp);
 //BA.debugLineNum = 284;BA.debugLine="Dim count As Int = 2";
_count = (int) (2);
 //BA.debugLineNum = 285;BA.debugLine="Dim clrs(3) As ARGBColor";
_clrs = new b4a.example.bitmapcreator._argbcolor[(int) (3)];
{
int d0 = _clrs.length;
for (int i0 = 0;i0 < d0;i0++) {
_clrs[i0] = new b4a.example.bitmapcreator._argbcolor();
}
}
;
 //BA.debugLineNum = 286;BA.debugLine="Dim temp As ARGBColor";
_temp = new b4a.example.bitmapcreator._argbcolor();
 //BA.debugLineNum = 287;BA.debugLine="Dim m As Int";
_m = 0;
 //BA.debugLineNum = 288;BA.debugLine="For steps = 1 To count";
{
final int step10 = 1;
final int limit10 = _count;
_steps = (int) (1) ;
for (;_steps <= limit10 ;_steps = _steps + step10 ) {
 //BA.debugLineNum = 289;BA.debugLine="For y = 0 To bc.mHeight - 1";
{
final int step11 = 1;
final int limit11 = (int) (_bc._mheight-1);
_y = (int) (0) ;
for (;_y <= limit11 ;_y = _y + step11 ) {
 //BA.debugLineNum = 290;BA.debugLine="For x = 0 To 2";
{
final int step12 = 1;
final int limit12 = (int) (2);
_x = (int) (0) ;
for (;_x <= limit12 ;_x = _x + step12 ) {
 //BA.debugLineNum = 291;BA.debugLine="bc.GetARGB(x, y, clrs(x))";
_bc._getargb(_x,_y,_clrs[_x]);
 }
};
 //BA.debugLineNum = 293;BA.debugLine="SetAvg(bc, 1, y, clrs, temp)";
_setavg(_bc,(int) (1),_y,_clrs,_temp);
 //BA.debugLineNum = 294;BA.debugLine="m = 0";
_m = (int) (0);
 //BA.debugLineNum = 295;BA.debugLine="For x = 2 To bc.mWidth - 2";
{
final int step17 = 1;
final int limit17 = (int) (_bc._mwidth-2);
_x = (int) (2) ;
for (;_x <= limit17 ;_x = _x + step17 ) {
 //BA.debugLineNum = 296;BA.debugLine="bc.GetARGB(x + 1, y, clrs(m))";
_bc._getargb((int) (_x+1),_y,_clrs[_m]);
 //BA.debugLineNum = 297;BA.debugLine="m = (m + 1) Mod 3";
_m = (int) ((_m+1)%3);
 //BA.debugLineNum = 298;BA.debugLine="SetAvg(bc, x, y, clrs, temp)";
_setavg(_bc,_x,_y,_clrs,_temp);
 }
};
 }
};
 //BA.debugLineNum = 301;BA.debugLine="For x = 0 To bc.mWidth - 1";
{
final int step23 = 1;
final int limit23 = (int) (_bc._mwidth-1);
_x = (int) (0) ;
for (;_x <= limit23 ;_x = _x + step23 ) {
 //BA.debugLineNum = 302;BA.debugLine="For y = 0 To 2";
{
final int step24 = 1;
final int limit24 = (int) (2);
_y = (int) (0) ;
for (;_y <= limit24 ;_y = _y + step24 ) {
 //BA.debugLineNum = 303;BA.debugLine="bc.GetARGB(x, y, clrs(y))";
_bc._getargb(_x,_y,_clrs[_y]);
 }
};
 //BA.debugLineNum = 305;BA.debugLine="SetAvg(bc, x, 1, clrs, temp)";
_setavg(_bc,_x,(int) (1),_clrs,_temp);
 //BA.debugLineNum = 306;BA.debugLine="m = 0";
_m = (int) (0);
 //BA.debugLineNum = 307;BA.debugLine="For y = 2 To bc.mHeight - 2";
{
final int step29 = 1;
final int limit29 = (int) (_bc._mheight-2);
_y = (int) (2) ;
for (;_y <= limit29 ;_y = _y + step29 ) {
 //BA.debugLineNum = 308;BA.debugLine="bc.GetARGB(x, y + 1, clrs(m))";
_bc._getargb(_x,(int) (_y+1),_clrs[_m]);
 //BA.debugLineNum = 309;BA.debugLine="m = (m + 1) Mod 3";
_m = (int) ((_m+1)%3);
 //BA.debugLineNum = 310;BA.debugLine="SetAvg(bc, x, y, clrs, temp)";
_setavg(_bc,_x,_y,_clrs,_temp);
 }
};
 }
};
 }
};
 //BA.debugLineNum = 314;BA.debugLine="Log(\"Time: \" & (DateTime.Now - n))";
__c.LogImpl("920250660","Time: "+BA.NumberToString((__c.DateTime.getNow()-_n)),0);
 //BA.debugLineNum = 315;BA.debugLine="Return bc";
if (true) return _bc;
 //BA.debugLineNum = 316;BA.debugLine="End Sub";
return null;
}
public String  _button_click() throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _b = null;
 //BA.debugLineNum = 225;BA.debugLine="Private Sub Button_Click";
 //BA.debugLineNum = 226;BA.debugLine="Dim b As B4XView = Sender";
_b = new anywheresoftware.b4a.objects.B4XViewWrapper();
_b.setObject((java.lang.Object)(__c.Sender(ba)));
 //BA.debugLineNum = 227;BA.debugLine="Close(b.Tag)";
_close((int)(BA.ObjectToNumber(_b.getTag())));
 //BA.debugLineNum = 228;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 1;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 2;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 3;BA.debugLine="Public ButtonsHeight As Int = 40dip";
_buttonsheight = __c.DipToCurrent((int) (40));
 //BA.debugLineNum = 4;BA.debugLine="Public TitleBarHeight As Int = 30dip";
_titlebarheight = __c.DipToCurrent((int) (30));
 //BA.debugLineNum = 5;BA.debugLine="Private ButtonWidth As Int = 80dip";
_buttonwidth = __c.DipToCurrent((int) (80));
 //BA.debugLineNum = 6;BA.debugLine="Public Base As B4XView";
_base = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 7;BA.debugLine="Public BackgroundColor As Int = 0xFF555555 'base";
_backgroundcolor = (int) (0xff555555);
 //BA.debugLineNum = 8;BA.debugLine="Public OverlayColor As Int = 0xaa000000";
_overlaycolor = (int) (0xaa000000);
 //BA.debugLineNum = 9;BA.debugLine="Public BorderColor As Int = 0xff000000";
_bordercolor = (int) (0xff000000);
 //BA.debugLineNum = 10;BA.debugLine="Public BorderCornersRadius As Int = 2dip";
_bordercornersradius = __c.DipToCurrent((int) (2));
 //BA.debugLineNum = 11;BA.debugLine="Public BorderWidth As Int = 2dip";
_borderwidth = __c.DipToCurrent((int) (2));
 //BA.debugLineNum = 12;BA.debugLine="Public ButtonsColor As Int = 0xFF555555";
_buttonscolor = (int) (0xff555555);
 //BA.debugLineNum = 13;BA.debugLine="Public ButtonsTextColor As Int = 0xFF89D5FF";
_buttonstextcolor = (int) (0xff89d5ff);
 //BA.debugLineNum = 14;BA.debugLine="Private Background As B4XView";
_background = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Private BlurImageView As B4XView";
_blurimageview = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Public BlurBackground As Boolean";
_blurbackground = false;
 //BA.debugLineNum = 17;BA.debugLine="Private BlurReduceScale As Int";
_blurreducescale = 0;
 //BA.debugLineNum = 18;BA.debugLine="Public mParent As B4XView";
_mparent = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Public Title As Object = \"\"";
_title = (Object)("");
 //BA.debugLineNum = 20;BA.debugLine="Public TitleBarColor As Int = 0xFF0083B8";
_titlebarcolor = (int) (0xff0083b8);
 //BA.debugLineNum = 21;BA.debugLine="Public TitleBarTextColor As Int";
_titlebartextcolor = 0;
 //BA.debugLineNum = 22;BA.debugLine="Public TitleBar As B4XView";
_titlebar = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Public PutAtTop As Boolean";
_putattop = false;
 //BA.debugLineNum = 24;BA.debugLine="Public TitleBarFont As B4XFont";
_titlebarfont = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont();
 //BA.debugLineNum = 25;BA.debugLine="Public ButtonsFont As B4XFont";
_buttonsfont = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont();
 //BA.debugLineNum = 26;BA.debugLine="Public ButtonsTextColorDisabled As Int = 0xFF8080";
_buttonstextcolordisabled = (int) (0xff808080);
 //BA.debugLineNum = 27;BA.debugLine="Public VisibleAnimationDuration As Int = 100";
_visibleanimationduration = (int) (100);
 //BA.debugLineNum = 28;BA.debugLine="Public ButtonsOrder() As Int = Array As Int(xui.D";
_buttonsorder = new int[]{_xui.DialogResponse_Positive,_xui.DialogResponse_Negative,_xui.DialogResponse_Cancel};
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public boolean  _close(int _result) throws Exception{
 //BA.debugLineNum = 230;BA.debugLine="Public Sub Close (Result As Int) As Boolean";
 //BA.debugLineNum = 231;BA.debugLine="If getVisible Then";
if (_getvisible()) { 
 //BA.debugLineNum = 232;BA.debugLine="CallSubDelayed2(Me, \"CloseMessage\", Result)";
__c.CallSubDelayed2(ba,this,"CloseMessage",(Object)(_result));
 //BA.debugLineNum = 233;BA.debugLine="Return True";
if (true) return __c.True;
 };
 //BA.debugLineNum = 235;BA.debugLine="Return False";
if (true) return __c.False;
 //BA.debugLineNum = 236;BA.debugLine="End Sub";
return false;
}
public String  _createbutton(Object _text,int _code) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _btn = null;
anywheresoftware.b4a.objects.B4XViewWrapper _xbtn = null;
int _numberofbuttons = 0;
 //BA.debugLineNum = 186;BA.debugLine="Private Sub CreateButton (Text As Object, Code As";
 //BA.debugLineNum = 187;BA.debugLine="If Text = \"\" Then Return";
if ((_text).equals((Object)(""))) { 
if (true) return "";};
 //BA.debugLineNum = 191;BA.debugLine="Dim btn As Label";
_btn = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 193;BA.debugLine="btn.Initialize(\"Button\")";
_btn.Initialize(ba,"Button");
 //BA.debugLineNum = 194;BA.debugLine="Dim xbtn As B4XView = btn";
_xbtn = new anywheresoftware.b4a.objects.B4XViewWrapper();
_xbtn.setObject((java.lang.Object)(_btn.getObject()));
 //BA.debugLineNum = 195;BA.debugLine="InternalSetTextOrCSBuilderToLabel(xbtn, Text)";
_internalsettextorcsbuildertolabel(_xbtn,_text);
 //BA.debugLineNum = 196;BA.debugLine="xbtn.Tag = Code";
_xbtn.setTag((Object)(_code));
 //BA.debugLineNum = 197;BA.debugLine="xbtn.SetColorAndBorder(ButtonsColor, 0dip, Border";
_xbtn.SetColorAndBorder(_buttonscolor,__c.DipToCurrent((int) (0)),_bordercolor,__c.DipToCurrent((int) (5)));
 //BA.debugLineNum = 198;BA.debugLine="xbtn.SetTextAlignment(\"CENTER\", \"CENTER\")";
_xbtn.SetTextAlignment("CENTER","CENTER");
 //BA.debugLineNum = 199;BA.debugLine="xbtn.TextColor = ButtonsTextColor";
_xbtn.setTextColor(_buttonstextcolor);
 //BA.debugLineNum = 200;BA.debugLine="xbtn.Font = ButtonsFont";
_xbtn.setFont(_buttonsfont);
 //BA.debugLineNum = 201;BA.debugLine="Dim numberOfButtons As Int = Base.NumberOfViews '";
_numberofbuttons = _base.getNumberOfViews();
 //BA.debugLineNum = 202;BA.debugLine="Base.AddView(xbtn, Base.Width - 4dip - numberOfBu";
_base.AddView((android.view.View)(_xbtn.getObject()),(int) (_base.getWidth()-__c.DipToCurrent((int) (4))-_numberofbuttons*(_buttonwidth+__c.DipToCurrent((int) (5)))-_buttonwidth),(int) (_base.getHeight()-_buttonsheight-__c.DipToCurrent((int) (4))),_buttonwidth,_buttonsheight);
 //BA.debugLineNum = 204;BA.debugLine="If Code = xui.DialogResponse_Cancel Then xbtn.Req";
if (_code==_xui.DialogResponse_Cancel) { 
_xbtn.RequestFocus();};
 //BA.debugLineNum = 205;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _getbutton(int _resultcode) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _b = null;
 //BA.debugLineNum = 208;BA.debugLine="Public Sub GetButton (ResultCode As Int) As B4XVie";
 //BA.debugLineNum = 209;BA.debugLine="For Each b As B4XView In Base.GetAllViewsRecursiv";
_b = new anywheresoftware.b4a.objects.B4XViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group1 = _base.GetAllViewsRecursive();
final int groupLen1 = group1.getSize()
;int index1 = 0;
;
for (; index1 < groupLen1;index1++){
_b.setObject((java.lang.Object)(group1.Get(index1)));
 //BA.debugLineNum = 210;BA.debugLine="If b.Tag = ResultCode Then Return b";
if ((_b.getTag()).equals((Object)(_resultcode))) { 
if (true) return _b;};
 }
};
 //BA.debugLineNum = 212;BA.debugLine="Return Null";
if (true) return (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(__c.Null));
 //BA.debugLineNum = 213;BA.debugLine="End Sub";
return null;
}
public boolean  _getvisible() throws Exception{
 //BA.debugLineNum = 182;BA.debugLine="Public Sub getVisible As Boolean";
 //BA.debugLineNum = 183;BA.debugLine="Return Background.IsInitialized And Background.Pa";
if (true) return _background.IsInitialized() && _background.getParent().IsInitialized();
 //BA.debugLineNum = 184;BA.debugLine="End Sub";
return false;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.B4XViewWrapper _parent) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 31;BA.debugLine="Public Sub Initialize (Parent As B4XView)";
 //BA.debugLineNum = 32;BA.debugLine="BlurBackground = False";
_blurbackground = __c.False;
 //BA.debugLineNum = 33;BA.debugLine="If xui.IsB4i Then";
if (_xui.getIsB4i()) { 
 //BA.debugLineNum = 34;BA.debugLine="BlurReduceScale = 3";
_blurreducescale = (int) (3);
 }else if(_xui.getIsB4J()) { 
 //BA.debugLineNum = 36;BA.debugLine="BlurReduceScale = 1";
_blurreducescale = (int) (1);
 }else if(_xui.getIsB4A()) { 
 //BA.debugLineNum = 38;BA.debugLine="BlurReduceScale = 3";
_blurreducescale = (int) (3);
 };
 //BA.debugLineNum = 40;BA.debugLine="mParent = Parent";
_mparent = _parent;
 //BA.debugLineNum = 46;BA.debugLine="TitleBarFont = xui.CreateDefaultBoldFont(16)";
_titlebarfont = _xui.CreateDefaultBoldFont((float) (16));
 //BA.debugLineNum = 47;BA.debugLine="ButtonsFont = xui.CreateDefaultBoldFont(15)";
_buttonsfont = _xui.CreateDefaultBoldFont((float) (15));
 //BA.debugLineNum = 48;BA.debugLine="TitleBarTextColor = xui.Color_White";
_titlebartextcolor = _xui.Color_White;
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return "";
}
public String  _internaladdstubtoclvifneeded(b4a.example3.customlistview _customlistview1,int _color) throws Exception{
b4a.example3.customlistview._clvitem _lastitem = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
int _height = 0;
 //BA.debugLineNum = 332;BA.debugLine="Public Sub InternalAddStubToCLVIfNeeded(CustomList";
 //BA.debugLineNum = 333;BA.debugLine="If CustomListView1.Size = 0 Then Return";
if (_customlistview1._getsize()==0) { 
if (true) return "";};
 //BA.debugLineNum = 334;BA.debugLine="Dim LastItem As CLVItem = CustomListView1.GetRawL";
_lastitem = _customlistview1._getrawlistitem((int) (_customlistview1._getsize()-1));
 //BA.debugLineNum = 335;BA.debugLine="If LastItem.Offset + LastItem.Panel.Height < Cust";
if (_lastitem.Offset+_lastitem.Panel.getHeight()<_customlistview1._asview().getHeight()) { 
 //BA.debugLineNum = 337;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"stub\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _xui.CreatePanel(ba,"stub");
 //BA.debugLineNum = 338;BA.debugLine="p.Color = Color";
_p.setColor(_color);
 //BA.debugLineNum = 339;BA.debugLine="Dim Height As Int = CustomListView1.AsView.Heigh";
_height = (int) (_customlistview1._asview().getHeight()-_lastitem.Offset-_lastitem.Panel.getHeight()-__c.DipToCurrent((int) (3)));
 //BA.debugLineNum = 340;BA.debugLine="If xui.IsB4J Then Height = Height + 5";
if (_xui.getIsB4J()) { 
_height = (int) (_height+5);};
 //BA.debugLineNum = 341;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, CustomListView1.AsV";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_customlistview1._asview().getWidth(),_height);
 //BA.debugLineNum = 342;BA.debugLine="CustomListView1.Add(p, \"\")";
_customlistview1._add(_p,(Object)(""));
 //BA.debugLineNum = 343;BA.debugLine="CustomListView1.sv.ScrollViewContentHeight = Cus";
_customlistview1._sv.setScrollViewContentHeight((int) (_customlistview1._sv.getScrollViewContentHeight()-_customlistview1._getdividersize()));
 };
 //BA.debugLineNum = 345;BA.debugLine="End Sub";
return "";
}
public String  _internalsettextorcsbuildertolabel(anywheresoftware.b4a.objects.B4XViewWrapper _xlbl,Object _text) throws Exception{
 //BA.debugLineNum = 347;BA.debugLine="Public Sub InternalSetTextOrCSBuilderToLabel(xlbl";
 //BA.debugLineNum = 349;BA.debugLine="xlbl.Text = Text";
_xlbl.setText(BA.ObjectToCharSequence(_text));
 //BA.debugLineNum = 359;BA.debugLine="End Sub";
return "";
}
public String  _resize(int _width,int _height) throws Exception{
int _top = 0;
 //BA.debugLineNum = 254;BA.debugLine="Public Sub Resize (Width As Int, Height As Int)";
 //BA.debugLineNum = 256;BA.debugLine="Dim Top As Int = Round(Background.Height / 2 - Ba";
_top = (int) (__c.Round(_background.getHeight()/(double)2-_base.getHeight()/(double)2));
 //BA.debugLineNum = 257;BA.debugLine="If PutAtTop Then Top = 20dip";
if (_putattop) { 
_top = __c.DipToCurrent((int) (20));};
 //BA.debugLineNum = 258;BA.debugLine="Background.SetLayoutAnimated(0, 0, 0, Width, Heig";
_background.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 259;BA.debugLine="Base.SetLayoutAnimated(200, Round(Background.Widt";
_base.SetLayoutAnimated((int) (200),(int) (__c.Round(_background.getWidth()/(double)2-_base.getWidth()/(double)2)),_top,_base.getWidth(),_base.getHeight());
 //BA.debugLineNum = 260;BA.debugLine="If xui.IsB4J Then";
if (_xui.getIsB4J()) { 
 //BA.debugLineNum = 261;BA.debugLine="UpdateBlur";
_updateblur();
 };
 //BA.debugLineNum = 263;BA.debugLine="End Sub";
return "";
}
public String  _setavg(b4a.example.bitmapcreator _bc,int _x,int _y,b4a.example.bitmapcreator._argbcolor[] _clrs,b4a.example.bitmapcreator._argbcolor _temp) throws Exception{
b4a.example.bitmapcreator._argbcolor _c = null;
 //BA.debugLineNum = 318;BA.debugLine="Private Sub SetAvg(bc As BitmapCreator, x As Int,";
 //BA.debugLineNum = 319;BA.debugLine="temp.Initialize";
_temp.Initialize();
 //BA.debugLineNum = 320;BA.debugLine="For Each c As ARGBColor In clrs";
{
final b4a.example.bitmapcreator._argbcolor[] group2 = _clrs;
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_c = group2[index2];
 //BA.debugLineNum = 321;BA.debugLine="temp.r = temp.r + c.r";
_temp.r = (int) (_temp.r+_c.r);
 //BA.debugLineNum = 322;BA.debugLine="temp.g = temp.g + c.g";
_temp.g = (int) (_temp.g+_c.g);
 //BA.debugLineNum = 323;BA.debugLine="temp.b = temp.b + c.b";
_temp.b = (int) (_temp.b+_c.b);
 }
};
 //BA.debugLineNum = 325;BA.debugLine="temp.a = 255";
_temp.a = (int) (255);
 //BA.debugLineNum = 326;BA.debugLine="temp.r = temp.r / 3";
_temp.r = (int) (_temp.r/(double)3);
 //BA.debugLineNum = 327;BA.debugLine="temp.g = temp.g / 3";
_temp.g = (int) (_temp.g/(double)3);
 //BA.debugLineNum = 328;BA.debugLine="temp.b = temp.b / 3";
_temp.b = (int) (_temp.b/(double)3);
 //BA.debugLineNum = 329;BA.debugLine="bc.SetARGB(x, y, temp)";
_bc._setargb(_x,_y,_temp);
 //BA.debugLineNum = 330;BA.debugLine="End Sub";
return "";
}
public String  _setbuttonstate(int _resultcode,boolean _enabled) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _b = null;
 //BA.debugLineNum = 216;BA.debugLine="Public Sub SetButtonState (ResultCode As Int, Enab";
 //BA.debugLineNum = 217;BA.debugLine="Dim b As B4XView = GetButton(ResultCode)";
_b = new anywheresoftware.b4a.objects.B4XViewWrapper();
_b = _getbutton(_resultcode);
 //BA.debugLineNum = 218;BA.debugLine="If b.IsInitialized Then";
if (_b.IsInitialized()) { 
 //BA.debugLineNum = 219;BA.debugLine="b.Enabled = Enabled";
_b.setEnabled(_enabled);
 //BA.debugLineNum = 220;BA.debugLine="If Enabled Then b.TextColor = ButtonsTextColor E";
if (_enabled) { 
_b.setTextColor(_buttonstextcolor);}
else {
_b.setTextColor(_buttonstextcolordisabled);};
 };
 //BA.debugLineNum = 222;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _show(Object _text,Object _yes,Object _no,Object _cancel) throws Exception{
ResumableSub_Show rsub = new ResumableSub_Show(this,_text,_yes,_no,_cancel);
rsub.resume(ba, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_Show extends BA.ResumableSub {
public ResumableSub_Show(cloyd.smart.home.monitor.b4xdialog parent,Object _text,Object _yes,Object _no,Object _cancel) {
this.parent = parent;
this._text = _text;
this._yes = _yes;
this._no = _no;
this._cancel = _cancel;
}
cloyd.smart.home.monitor.b4xdialog parent;
Object _text;
Object _yes;
Object _no;
Object _cancel;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _xlbl = null;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
{
parent.__c.ReturnFromResumableSub(this,null);return;}
case 0:
//C
this.state = -1;
 //BA.debugLineNum = 69;BA.debugLine="Dim p As B4XView";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 70;BA.debugLine="p = xui.CreatePanel(\"\")";
_p = parent._xui.CreatePanel(ba,"");
 //BA.debugLineNum = 71;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, 300dip, 100dip)";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),parent.__c.DipToCurrent((int) (300)),parent.__c.DipToCurrent((int) (100)));
 //BA.debugLineNum = 72;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="lbl.Initialize(\"\")";
_lbl.Initialize(ba,"");
 //BA.debugLineNum = 77;BA.debugLine="lbl.SingleLine = False";
_lbl.setSingleLine(parent.__c.False);
 //BA.debugLineNum = 81;BA.debugLine="Dim xlbl As B4XView = lbl";
_xlbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_xlbl.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 82;BA.debugLine="p.AddView(xlbl, 5dip, 0, p.Width - 10dip, p.Heigh";
_p.AddView((android.view.View)(_xlbl.getObject()),parent.__c.DipToCurrent((int) (5)),(int) (0),(int) (_p.getWidth()-parent.__c.DipToCurrent((int) (10))),_p.getHeight());
 //BA.debugLineNum = 83;BA.debugLine="xlbl.TextColor = xui.Color_White";
_xlbl.setTextColor(parent._xui.Color_White);
 //BA.debugLineNum = 84;BA.debugLine="xlbl.Font = TitleBarFont";
_xlbl.setFont(parent._titlebarfont);
 //BA.debugLineNum = 85;BA.debugLine="InternalSetTextOrCSBuilderToLabel(xlbl, Text)";
parent._internalsettextorcsbuildertolabel(_xlbl,_text);
 //BA.debugLineNum = 86;BA.debugLine="xlbl.SetTextAlignment(\"CENTER\", \"LEFT\")";
_xlbl.SetTextAlignment("CENTER","LEFT");
 //BA.debugLineNum = 87;BA.debugLine="Wait For (ShowCustom(p, Yes, No, Cancel)) Complet";
parent.__c.WaitFor("complete", ba, this, parent._showcustom(_p,_yes,_no,_cancel));
this.state = 1;
return;
case 1:
//C
this.state = -1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 88;BA.debugLine="Return Result";
if (true) {
parent.__c.ReturnFromResumableSub(this,(Object)(_result));return;};
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public void  _complete(int _result) throws Exception{
}
public anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _showcustom(anywheresoftware.b4a.objects.B4XViewWrapper _content,Object _yes,Object _no,Object _cancel) throws Exception{
ResumableSub_ShowCustom rsub = new ResumableSub_ShowCustom(this,_content,_yes,_no,_cancel);
rsub.resume(ba, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_ShowCustom extends BA.ResumableSub {
public ResumableSub_ShowCustom(cloyd.smart.home.monitor.b4xdialog parent,anywheresoftware.b4a.objects.B4XViewWrapper _content,Object _yes,Object _no,Object _cancel) {
this.parent = parent;
this._content = _content;
this._yes = _yes;
this._no = _no;
this._cancel = _cancel;
}
cloyd.smart.home.monitor.b4xdialog parent;
anywheresoftware.b4a.objects.B4XViewWrapper _content;
Object _yes;
Object _no;
Object _cancel;
anywheresoftware.b4a.objects.B4XViewWrapper _v = null;
anywheresoftware.b4a.objects.ImageViewWrapper _iv = null;
anywheresoftware.b4a.objects.PanelWrapper _p = null;
boolean _includetitle = false;
int _temptitlebarheight = 0;
int _height = 0;
int _width = 0;
int _top = 0;
boolean _removetitle = false;
int _i = 0;
int _btype = 0;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _xlbl = null;
int _result = 0;
anywheresoftware.b4a.BA.IterableList group1;
int index1;
int groupLen1;
int step45;
int limit45;
anywheresoftware.b4a.BA.IterableList group73;
int index73;
int groupLen73;
anywheresoftware.b4a.BA.IterableList group77;
int index77;
int groupLen77;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
{
parent.__c.ReturnFromResumableSub(this,null);return;}
case 0:
//C
this.state = 1;
 //BA.debugLineNum = 92;BA.debugLine="For Each v As B4XView In mParent.GetAllViewsRecur";
if (true) break;

case 1:
//for
this.state = 8;
_v = new anywheresoftware.b4a.objects.B4XViewWrapper();
group1 = parent._mparent.GetAllViewsRecursive();
index1 = 0;
groupLen1 = group1.getSize();
this.state = 60;
if (true) break;

case 60:
//C
this.state = 8;
if (index1 < groupLen1) {
this.state = 3;
_v.setObject((java.lang.Object)(group1.Get(index1)));}
if (true) break;

case 61:
//C
this.state = 60;
index1++;
if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 93;BA.debugLine="If v.Tag <> Null And v.Tag = \"b4xdialog_backgrou";
if (true) break;

case 4:
//if
this.state = 7;
if (_v.getTag()!= null && (_v.getTag()).equals((Object)("b4xdialog_background"))) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 94;BA.debugLine="v.RemoveViewFromParent";
_v.RemoveViewFromParent();
 //BA.debugLineNum = 95;BA.debugLine="Exit";
this.state = 8;
if (true) break;
 if (true) break;

case 7:
//C
this.state = 61;
;
 if (true) break;
if (true) break;

case 8:
//C
this.state = 9;
;
 //BA.debugLineNum = 98;BA.debugLine="Dim Background As B4XView = xui.CreatePanel(\"back";
parent._background = new anywheresoftware.b4a.objects.B4XViewWrapper();
parent._background = parent._xui.CreatePanel(ba,"background");
 //BA.debugLineNum = 99;BA.debugLine="Background.Tag = \"b4xdialog_background\"";
parent._background.setTag((Object)("b4xdialog_background"));
 //BA.debugLineNum = 100;BA.debugLine="If BlurBackground Then";
if (true) break;

case 9:
//if
this.state = 14;
if (parent._blurbackground) { 
this.state = 11;
}else {
this.state = 13;
}if (true) break;

case 11:
//C
this.state = 14;
 //BA.debugLineNum = 101;BA.debugLine="Dim iv As ImageView";
_iv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 102;BA.debugLine="iv.Initialize(\"\")";
_iv.Initialize(ba,"");
 //BA.debugLineNum = 103;BA.debugLine="BlurImageView = iv";
parent._blurimageview.setObject((java.lang.Object)(_iv.getObject()));
 //BA.debugLineNum = 104;BA.debugLine="Background.AddView(BlurImageView, 0, 0, Backgrou";
parent._background.AddView((android.view.View)(parent._blurimageview.getObject()),(int) (0),(int) (0),parent._background.getWidth(),parent._background.getHeight());
 //BA.debugLineNum = 105;BA.debugLine="Background.Color = xui.Color_Transparent";
parent._background.setColor(parent._xui.Color_Transparent);
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 107;BA.debugLine="Background.Color = OverlayColor";
parent._background.setColor(parent._overlaycolor);
 if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 110;BA.debugLine="Dim p As Panel = Background";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
_p.setObject((android.view.ViewGroup)(parent._background.getObject()));
 //BA.debugLineNum = 111;BA.debugLine="p.Elevation = 4dip";
_p.setElevation((float) (parent.__c.DipToCurrent((int) (4))));
 //BA.debugLineNum = 113;BA.debugLine="mParent.AddView(Background, 0, 0, mParent.Width,";
parent._mparent.AddView((android.view.View)(parent._background.getObject()),(int) (0),(int) (0),parent._mparent.getWidth(),parent._mparent.getHeight());
 //BA.debugLineNum = 114;BA.debugLine="UpdateBlur";
parent._updateblur();
 //BA.debugLineNum = 115;BA.debugLine="Base = xui.CreatePanel(\"\")";
parent._base = parent._xui.CreatePanel(ba,"");
 //BA.debugLineNum = 116;BA.debugLine="Base.SetColorAndBorder(BackgroundColor, BorderWid";
parent._base.SetColorAndBorder(parent._backgroundcolor,parent._borderwidth,parent._bordercolor,parent._bordercornersradius);
 //BA.debugLineNum = 117;BA.debugLine="Base.RequestFocus";
parent._base.RequestFocus();
 //BA.debugLineNum = 118;BA.debugLine="Dim IncludeTitle As Boolean = Title <> \"\"";
_includetitle = (parent._title).equals((Object)("")) == false;
 //BA.debugLineNum = 119;BA.debugLine="Dim TempTitleBarHeight As Int = 0";
_temptitlebarheight = (int) (0);
 //BA.debugLineNum = 120;BA.debugLine="If IncludeTitle Then";
if (true) break;

case 15:
//if
this.state = 18;
if (_includetitle) { 
this.state = 17;
}if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 121;BA.debugLine="TempTitleBarHeight = TitleBarHeight";
_temptitlebarheight = parent._titlebarheight;
 if (true) break;

case 18:
//C
this.state = 19;
;
 //BA.debugLineNum = 123;BA.debugLine="Dim height As Int = Content.Height + ButtonsHeigh";
_height = (int) (_content.getHeight()+parent._buttonsheight+parent.__c.DipToCurrent((int) (3))+parent.__c.DipToCurrent((int) (3))+_temptitlebarheight);
 //BA.debugLineNum = 124;BA.debugLine="Dim width As Int = Content.Width + 4dip";
_width = (int) (_content.getWidth()+parent.__c.DipToCurrent((int) (4)));
 //BA.debugLineNum = 125;BA.debugLine="Dim Top As Int = Round(Background.Height / 2 - he";
_top = (int) (parent.__c.Round(parent._background.getHeight()/(double)2-_height/(double)2));
 //BA.debugLineNum = 126;BA.debugLine="Dim RemoveTitle As Boolean";
_removetitle = false;
 //BA.debugLineNum = 127;BA.debugLine="If PutAtTop Then";
if (true) break;

case 19:
//if
this.state = 28;
if (parent._putattop) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 128;BA.debugLine="Top = 20dip";
_top = parent.__c.DipToCurrent((int) (20));
 //BA.debugLineNum = 129;BA.debugLine="If Background.Height - Top - height < 200dip The";
if (true) break;

case 22:
//if
this.state = 27;
if (parent._background.getHeight()-_top-_height<parent.__c.DipToCurrent((int) (200))) { 
this.state = 24;
;}if (true) break;

case 24:
//C
this.state = 27;
_removetitle = parent.__c.True;
if (true) break;

case 27:
//C
this.state = 28;
;
 if (true) break;
;
 //BA.debugLineNum = 131;BA.debugLine="If RemoveTitle Or Background.Height < height Then";

case 28:
//if
this.state = 31;
if (_removetitle || parent._background.getHeight()<_height) { 
this.state = 30;
}if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 132;BA.debugLine="Top = 1dip";
_top = parent.__c.DipToCurrent((int) (1));
 //BA.debugLineNum = 133;BA.debugLine="IncludeTitle = False";
_includetitle = parent.__c.False;
 //BA.debugLineNum = 134;BA.debugLine="height = height - TempTitleBarHeight - 3dip";
_height = (int) (_height-_temptitlebarheight-parent.__c.DipToCurrent((int) (3)));
 //BA.debugLineNum = 135;BA.debugLine="TempTitleBarHeight = 0";
_temptitlebarheight = (int) (0);
 if (true) break;

case 31:
//C
this.state = 32;
;
 //BA.debugLineNum = 137;BA.debugLine="Background.AddView(Base, Round(Background.Width /";
parent._background.AddView((android.view.View)(parent._base.getObject()),(int) (parent.__c.Round(parent._background.getWidth()/(double)2-_width/(double)2)),_top,(int) (parent.__c.Round(_width)),(int) (parent.__c.Round(_height)));
 //BA.debugLineNum = 139;BA.debugLine="For i = ButtonsOrder.Length - 1 To 0 Step - 1";
if (true) break;

case 32:
//for
this.state = 43;
step45 = -1;
limit45 = (int) (0);
_i = (int) (parent._buttonsorder.length-1) ;
this.state = 62;
if (true) break;

case 62:
//C
this.state = 43;
if ((step45 > 0 && _i <= limit45) || (step45 < 0 && _i >= limit45)) this.state = 34;
if (true) break;

case 63:
//C
this.state = 62;
_i = ((int)(0 + _i + step45)) ;
if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 140;BA.debugLine="Dim btype As Int = ButtonsOrder(i)";
_btype = parent._buttonsorder[_i];
 //BA.debugLineNum = 141;BA.debugLine="Select btype";
if (true) break;

case 35:
//select
this.state = 42;
switch (BA.switchObjectToInt(_btype,parent._xui.DialogResponse_Cancel,parent._xui.DialogResponse_Negative,parent._xui.DialogResponse_Positive)) {
case 0: {
this.state = 37;
if (true) break;
}
case 1: {
this.state = 39;
if (true) break;
}
case 2: {
this.state = 41;
if (true) break;
}
}
if (true) break;

case 37:
//C
this.state = 42;
 //BA.debugLineNum = 143;BA.debugLine="CreateButton(Cancel, btype)";
parent._createbutton(_cancel,_btype);
 if (true) break;

case 39:
//C
this.state = 42;
 //BA.debugLineNum = 145;BA.debugLine="CreateButton(No, btype)";
parent._createbutton(_no,_btype);
 if (true) break;

case 41:
//C
this.state = 42;
 //BA.debugLineNum = 147;BA.debugLine="CreateButton(Yes, btype)";
parent._createbutton(_yes,_btype);
 if (true) break;

case 42:
//C
this.state = 63;
;
 if (true) break;
if (true) break;

case 43:
//C
this.state = 44;
;
 //BA.debugLineNum = 150;BA.debugLine="Base.Visible = False";
parent._base.setVisible(parent.__c.False);
 //BA.debugLineNum = 151;BA.debugLine="If IncludeTitle Then";
if (true) break;

case 44:
//if
this.state = 47;
if (_includetitle) { 
this.state = 46;
}if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 152;BA.debugLine="TitleBar = xui.CreatePanel(\"TitleBar\")";
parent._titlebar = parent._xui.CreatePanel(ba,"TitleBar");
 //BA.debugLineNum = 153;BA.debugLine="TitleBar.Color = TitleBarColor";
parent._titlebar.setColor(parent._titlebarcolor);
 //BA.debugLineNum = 154;BA.debugLine="Base.AddView(TitleBar, 2dip, 2dip, Content.Width";
parent._base.AddView((android.view.View)(parent._titlebar.getObject()),parent.__c.DipToCurrent((int) (2)),parent.__c.DipToCurrent((int) (2)),_content.getWidth(),_temptitlebarheight);
 //BA.debugLineNum = 155;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 156;BA.debugLine="lbl.Initialize(\"\")";
_lbl.Initialize(ba,"");
 //BA.debugLineNum = 157;BA.debugLine="Dim xlbl As B4XView = lbl";
_xlbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_xlbl.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 158;BA.debugLine="InternalSetTextOrCSBuilderToLabel(xlbl, Title)";
parent._internalsettextorcsbuildertolabel(_xlbl,parent._title);
 //BA.debugLineNum = 159;BA.debugLine="xlbl.TextColor = TitleBarTextColor";
_xlbl.setTextColor(parent._titlebartextcolor);
 //BA.debugLineNum = 160;BA.debugLine="xlbl.Font = TitleBarFont";
_xlbl.setFont(parent._titlebarfont);
 //BA.debugLineNum = 161;BA.debugLine="xlbl.SetTextAlignment(\"CENTER\", \"CENTER\")";
_xlbl.SetTextAlignment("CENTER","CENTER");
 //BA.debugLineNum = 162;BA.debugLine="TitleBar.AddView(xlbl, 0, 0, TitleBar.Width, Tit";
parent._titlebar.AddView((android.view.View)(_xlbl.getObject()),(int) (0),(int) (0),parent._titlebar.getWidth(),parent._titlebar.getHeight());
 if (true) break;

case 47:
//C
this.state = 48;
;
 //BA.debugLineNum = 164;BA.debugLine="Content.RemoveViewFromParent";
_content.RemoveViewFromParent();
 //BA.debugLineNum = 165;BA.debugLine="Base.AddView(Content, 2dip, 2dip + TempTitleBarHe";
parent._base.AddView((android.view.View)(_content.getObject()),parent.__c.DipToCurrent((int) (2)),(int) (parent.__c.DipToCurrent((int) (2))+_temptitlebarheight),_content.getWidth(),_content.getHeight());
 //BA.debugLineNum = 166;BA.debugLine="Base.SetVisibleAnimated(VisibleAnimationDuration,";
parent._base.SetVisibleAnimated(parent._visibleanimationduration,parent.__c.True);
 //BA.debugLineNum = 167;BA.debugLine="For Each v As B4XView In Background.GetAllViewsRe";
if (true) break;

case 48:
//for
this.state = 51;
_v = new anywheresoftware.b4a.objects.B4XViewWrapper();
group73 = parent._background.GetAllViewsRecursive();
index73 = 0;
groupLen73 = group73.getSize();
this.state = 64;
if (true) break;

case 64:
//C
this.state = 51;
if (index73 < groupLen73) {
this.state = 50;
_v.setObject((java.lang.Object)(group73.Get(index73)));}
if (true) break;

case 65:
//C
this.state = 64;
index73++;
if (true) break;

case 50:
//C
this.state = 65;
 //BA.debugLineNum = 168;BA.debugLine="v.Enabled = True";
_v.setEnabled(parent.__c.True);
 if (true) break;
if (true) break;

case 51:
//C
this.state = 52;
;
 //BA.debugLineNum = 170;BA.debugLine="Wait For CloseMessage (Result As Int)";
parent.__c.WaitFor("closemessage", ba, this, null);
this.state = 66;
return;
case 66:
//C
this.state = 52;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 171;BA.debugLine="For Each v As B4XView In Background.GetAllViewsRe";
if (true) break;

case 52:
//for
this.state = 55;
_v = new anywheresoftware.b4a.objects.B4XViewWrapper();
group77 = parent._background.GetAllViewsRecursive();
index77 = 0;
groupLen77 = group77.getSize();
this.state = 67;
if (true) break;

case 67:
//C
this.state = 55;
if (index77 < groupLen77) {
this.state = 54;
_v.setObject((java.lang.Object)(group77.Get(index77)));}
if (true) break;

case 68:
//C
this.state = 67;
index77++;
if (true) break;

case 54:
//C
this.state = 68;
 //BA.debugLineNum = 172;BA.debugLine="v.Enabled = False";
_v.setEnabled(parent.__c.False);
 if (true) break;
if (true) break;

case 55:
//C
this.state = 56;
;
 //BA.debugLineNum = 174;BA.debugLine="Base.SetVisibleAnimated(VisibleAnimationDuration,";
parent._base.SetVisibleAnimated(parent._visibleanimationduration,parent.__c.False);
 //BA.debugLineNum = 175;BA.debugLine="If VisibleAnimationDuration > 0 Then";
if (true) break;

case 56:
//if
this.state = 59;
if (parent._visibleanimationduration>0) { 
this.state = 58;
}if (true) break;

case 58:
//C
this.state = 59;
 //BA.debugLineNum = 176;BA.debugLine="Sleep(VisibleAnimationDuration)";
parent.__c.Sleep(ba,this,parent._visibleanimationduration);
this.state = 69;
return;
case 69:
//C
this.state = 59;
;
 if (true) break;

case 59:
//C
this.state = -1;
;
 //BA.debugLineNum = 178;BA.debugLine="Background.RemoveViewFromParent";
parent._background.RemoveViewFromParent();
 //BA.debugLineNum = 179;BA.debugLine="Return Result";
if (true) {
parent.__c.ReturnFromResumableSub(this,(Object)(_result));return;};
 //BA.debugLineNum = 180;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public void  _closemessage(int _result) throws Exception{
}
public anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _showtemplate(Object _dialogtemplate,Object _yes,Object _no,Object _cancel) throws Exception{
ResumableSub_ShowTemplate rsub = new ResumableSub_ShowTemplate(this,_dialogtemplate,_yes,_no,_cancel);
rsub.resume(ba, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_ShowTemplate extends BA.ResumableSub {
public ResumableSub_ShowTemplate(cloyd.smart.home.monitor.b4xdialog parent,Object _dialogtemplate,Object _yes,Object _no,Object _cancel) {
this.parent = parent;
this._dialogtemplate = _dialogtemplate;
this._yes = _yes;
this._no = _no;
this._cancel = _cancel;
}
cloyd.smart.home.monitor.b4xdialog parent;
Object _dialogtemplate;
Object _yes;
Object _no;
Object _cancel;
anywheresoftware.b4a.objects.B4XViewWrapper _content = null;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
{
parent.__c.ReturnFromResumableSub(this,null);return;}
case 0:
//C
this.state = -1;
 //BA.debugLineNum = 60;BA.debugLine="Dim content As B4XView = CallSub2(DialogTemplate,";
_content = new anywheresoftware.b4a.objects.B4XViewWrapper();
_content.setObject((java.lang.Object)(parent.__c.CallSubNew2(ba,_dialogtemplate,"GetPanel",parent)));
 //BA.debugLineNum = 61;BA.debugLine="CallSub2(DialogTemplate, \"Show\", Me)";
parent.__c.CallSubNew2(ba,_dialogtemplate,"Show",parent);
 //BA.debugLineNum = 62;BA.debugLine="Wait For (ShowCustom(content , Yes, No, Cancel))";
parent.__c.WaitFor("complete", ba, this, parent._showcustom(_content,_yes,_no,_cancel));
this.state = 1;
return;
case 1:
//C
this.state = -1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 63;BA.debugLine="CallSub2(DialogTemplate, \"DialogClosed\", Result)";
parent.__c.CallSubNew2(ba,_dialogtemplate,"DialogClosed",(Object)(_result));
 //BA.debugLineNum = 64;BA.debugLine="PutAtTop = False";
parent._putattop = parent.__c.False;
 //BA.debugLineNum = 65;BA.debugLine="Return Result";
if (true) {
parent.__c.ReturnFromResumableSub(this,(Object)(_result));return;};
 //BA.debugLineNum = 66;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public String  _stub_click() throws Exception{
 //BA.debugLineNum = 361;BA.debugLine="Private Sub Stub_Click";
 //BA.debugLineNum = 363;BA.debugLine="End Sub";
return "";
}
public String  _updateblur() throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _source = null;
b4a.example.bitmapcreator _blurred = null;
 //BA.debugLineNum = 265;BA.debugLine="Private Sub UpdateBlur";
 //BA.debugLineNum = 266;BA.debugLine="If BlurBackground = False Then Return";
if (_blurbackground==__c.False) { 
if (true) return "";};
 //BA.debugLineNum = 267;BA.debugLine="Background.Visible = False";
_background.setVisible(__c.False);
 //BA.debugLineNum = 268;BA.debugLine="Dim source As B4XBitmap = Background.Parent.Snaps";
_source = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
_source = _background.getParent().Snapshot();
 //BA.debugLineNum = 269;BA.debugLine="If source.Width > Background.Width Or source.Heig";
if (_source.getWidth()>_background.getWidth() || _source.getHeight()>_background.getHeight()) { 
 //BA.debugLineNum = 270;BA.debugLine="source = source.Crop(0, 0, Background.Width, Bac";
_source = _source.Crop((int) (0),(int) (0),_background.getWidth(),_background.getHeight());
 };
 //BA.debugLineNum = 272;BA.debugLine="Background.Visible = True";
_background.setVisible(__c.True);
 //BA.debugLineNum = 273;BA.debugLine="BlurImageView.SetLayoutAnimated(0, 0, 0, Backgrou";
_blurimageview.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_background.getWidth(),_background.getHeight());
 //BA.debugLineNum = 274;BA.debugLine="Dim blurred As BitmapCreator = Blur(source)";
_blurred = _blur(_source);
 //BA.debugLineNum = 275;BA.debugLine="blurred.SetBitmapToImageView(blurred.Bitmap, Blur";
_blurred._setbitmaptoimageview(_blurred._getbitmap(),_blurimageview);
 //BA.debugLineNum = 276;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
