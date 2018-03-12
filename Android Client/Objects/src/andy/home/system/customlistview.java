package andy.home.system;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class customlistview extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "andy.home.system.customlistview");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", andy.home.system.customlistview.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mbase = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _sv = null;
public anywheresoftware.b4a.objects.collections.List _items = null;
public float _dividersize = 0f;
public String _eventname = "";
public Object _callback = null;
public int _defaulttextcolor = 0;
public int _defaulttextbackgroundcolor = 0;
public int _animationduration = 0;
public long _lastreachendevent = 0L;
public int _pressedcolor = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public anywheresoftware.b4a.objects.LabelWrapper _designerlabel = null;
public boolean _horizontal = false;
public anywheresoftware.b4a.objects.StringUtils _su = null;
public int _mfirstvisibleindex = 0;
public int _mlastvisibleindex = 0;
public boolean _monitorvisiblerange = false;
public boolean _firescrollchanged = false;
public andy.home.system.main _main = null;
public andy.home.system.starter _starter = null;
public static class _clvitem{
public boolean IsInitialized;
public anywheresoftware.b4a.objects.B4XViewWrapper Panel;
public int Size;
public Object Value;
public int Color;
public boolean TextItem;
public int Offset;
public void Initialize() {
IsInitialized = true;
Panel = new anywheresoftware.b4a.objects.B4XViewWrapper();
Size = 0;
Value = new Object();
Color = 0;
TextItem = false;
Offset = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public String  _add(anywheresoftware.b4a.objects.B4XViewWrapper _pnl,Object _value) throws Exception{
 //BA.debugLineNum = 335;BA.debugLine="Public Sub Add(Pnl As B4XView, Value As Object)";
 //BA.debugLineNum = 336;BA.debugLine="InsertAt(items.Size, Pnl, Value)";
_insertat(_items.getSize(),_pnl,_value);
 //BA.debugLineNum = 337;BA.debugLine="End Sub";
return "";
}
public String  _addtextitem(Object _text,Object _value) throws Exception{
 //BA.debugLineNum = 146;BA.debugLine="Public Sub AddTextItem(Text As Object, Value As Ob";
 //BA.debugLineNum = 147;BA.debugLine="InsertAtTextItem(items.Size, Text, Value)";
_insertattextitem(_items.getSize(),_text,_value);
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _asview() throws Exception{
 //BA.debugLineNum = 90;BA.debugLine="Public Sub AsView As B4XView";
 //BA.debugLineNum = 91;BA.debugLine="Return mBase";
if (true) return _mbase;
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return null;
}
public String  _base_resize(double _width,double _height) throws Exception{
int _scrollbar = 0;
andy.home.system.customlistview._clvitem _it = null;
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
 //BA.debugLineNum = 66;BA.debugLine="Private Sub Base_Resize (Width As Double, Height A";
 //BA.debugLineNum = 67;BA.debugLine="sv.SetLayoutAnimated(0, 0, 0, Width, Height)";
_sv.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),(int) (_height));
 //BA.debugLineNum = 68;BA.debugLine="Dim scrollbar As Int";
_scrollbar = 0;
 //BA.debugLineNum = 69;BA.debugLine="If xui.IsB4J Then scrollbar = 20";
if (_xui.getIsB4J()) { 
_scrollbar = (int) (20);};
 //BA.debugLineNum = 70;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 71;BA.debugLine="sv.ScrollViewContentHeight = Height - scrollbar";
_sv.setScrollViewContentHeight((int) (_height-_scrollbar));
 //BA.debugLineNum = 72;BA.debugLine="For Each it As CLVItem In items";
{
final anywheresoftware.b4a.BA.IterableList group6 = _items;
final int groupLen6 = group6.getSize()
;int index6 = 0;
;
for (; index6 < groupLen6;index6++){
_it = (andy.home.system.customlistview._clvitem)(group6.Get(index6));
 //BA.debugLineNum = 73;BA.debugLine="it.Panel.Height = sv.ScrollViewContentHeight";
_it.Panel.setHeight(_sv.getScrollViewContentHeight());
 //BA.debugLineNum = 74;BA.debugLine="it.Panel.GetView(0).Height = it.Panel.Height";
_it.Panel.GetView((int) (0)).setHeight(_it.Panel.getHeight());
 }
};
 }else {
 //BA.debugLineNum = 77;BA.debugLine="sv.ScrollViewContentWidth = Width - scrollbar";
_sv.setScrollViewContentWidth((int) (_width-_scrollbar));
 //BA.debugLineNum = 78;BA.debugLine="For Each it As CLVItem In items";
{
final anywheresoftware.b4a.BA.IterableList group12 = _items;
final int groupLen12 = group12.getSize()
;int index12 = 0;
;
for (; index12 < groupLen12;index12++){
_it = (andy.home.system.customlistview._clvitem)(group12.Get(index12));
 //BA.debugLineNum = 79;BA.debugLine="it.Panel.Width = sv.ScrollViewContentWidth";
_it.Panel.setWidth(_sv.getScrollViewContentWidth());
 //BA.debugLineNum = 80;BA.debugLine="it.Panel.GetView(0).Width = it.Panel.Width";
_it.Panel.GetView((int) (0)).setWidth(_it.Panel.getWidth());
 //BA.debugLineNum = 81;BA.debugLine="If it.TextItem Then";
if (_it.TextItem) { 
 //BA.debugLineNum = 82;BA.debugLine="Dim lbl As B4XView = it.Panel.GetView(0).GetVi";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _it.Panel.GetView((int) (0)).GetView((int) (0));
 //BA.debugLineNum = 83;BA.debugLine="lbl.SetLayoutAnimated(0, lbl.Left, lbl.Top, it";
_lbl.SetLayoutAnimated((int) (0),_lbl.getLeft(),_lbl.getTop(),(int) (_it.Panel.getWidth()-_lbl.getLeft()),_lbl.getHeight());
 };
 }
};
 };
 //BA.debugLineNum = 87;BA.debugLine="If items.Size > 0 Then UpdateVisibleRange";
if (_items.getSize()>0) { 
_updatevisiblerange();};
 //BA.debugLineNum = 88;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 11;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 12;BA.debugLine="Private mBase As B4XView";
_mbase = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Public sv As B4XView";
_sv = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Type CLVItem(Panel As B4XView, Size As Int, Value";
;
 //BA.debugLineNum = 16;BA.debugLine="Private items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 17;BA.debugLine="Private DividerSize As Float";
_dividersize = 0f;
 //BA.debugLineNum = 18;BA.debugLine="Private EventName As String";
_eventname = "";
 //BA.debugLineNum = 19;BA.debugLine="Private CallBack As Object";
_callback = new Object();
 //BA.debugLineNum = 20;BA.debugLine="Public DefaultTextColor As Int";
_defaulttextcolor = 0;
 //BA.debugLineNum = 21;BA.debugLine="Public DefaultTextBackgroundColor As Int";
_defaulttextbackgroundcolor = 0;
 //BA.debugLineNum = 22;BA.debugLine="Public AnimationDuration As Int = 300";
_animationduration = (int) (300);
 //BA.debugLineNum = 23;BA.debugLine="Private LastReachEndEvent As Long";
_lastreachendevent = 0L;
 //BA.debugLineNum = 24;BA.debugLine="Private PressedColor As Int";
_pressedcolor = 0;
 //BA.debugLineNum = 25;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 26;BA.debugLine="Private DesignerLabel As Label";
_designerlabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private horizontal As Boolean";
_horizontal = false;
 //BA.debugLineNum = 31;BA.debugLine="Private su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 35;BA.debugLine="Private mFirstVisibleIndex = -1, mLastVisibleInde";
_mfirstvisibleindex = (int) (-1);
_mlastvisibleindex = (int) (0x7fffffff);
 //BA.debugLineNum = 36;BA.debugLine="Private MonitorVisibleRange As Boolean";
_monitorvisiblerange = false;
 //BA.debugLineNum = 37;BA.debugLine="Private FireScrollChanged As Boolean";
_firescrollchanged = false;
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public String  _clear() throws Exception{
 //BA.debugLineNum = 95;BA.debugLine="Public Sub Clear";
 //BA.debugLineNum = 96;BA.debugLine="items.Clear";
_items.Clear();
 //BA.debugLineNum = 97;BA.debugLine="sv.ScrollViewInnerPanel.RemoveAllViews";
_sv.getScrollViewInnerPanel().RemoveAllViews();
 //BA.debugLineNum = 98;BA.debugLine="SetScrollViewContentSize(0)";
_setscrollviewcontentsize((float) (0));
 //BA.debugLineNum = 99;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _createlabel(Object _txt) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
 //BA.debugLineNum = 472;BA.debugLine="Private Sub CreateLabel(txt As Object) As B4XView";
 //BA.debugLineNum = 473;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 474;BA.debugLine="lbl.Initialize(\"\")";
_lbl.Initialize(ba,"");
 //BA.debugLineNum = 475;BA.debugLine="lbl.Gravity = DesignerLabel.Gravity";
_lbl.setGravity(_designerlabel.getGravity());
 //BA.debugLineNum = 476;BA.debugLine="lbl.TextSize = DesignerLabel.TextSize";
_lbl.setTextSize(_designerlabel.getTextSize());
 //BA.debugLineNum = 477;BA.debugLine="lbl.SingleLine = False";
_lbl.setSingleLine(__c.False);
 //BA.debugLineNum = 478;BA.debugLine="lbl.Text = txt";
_lbl.setText(BA.ObjectToCharSequence(_txt));
 //BA.debugLineNum = 479;BA.debugLine="lbl.Width = sv.ScrollViewContentWidth - 10dip";
_lbl.setWidth((int) (_sv.getScrollViewContentWidth()-__c.DipToCurrent((int) (10))));
 //BA.debugLineNum = 480;BA.debugLine="lbl.Height = su.MeasureMultilineTextHeight(lbl, t";
_lbl.setHeight(_su.MeasureMultilineTextHeight((android.widget.TextView)(_lbl.getObject()),BA.ObjectToCharSequence(_txt)));
 //BA.debugLineNum = 481;BA.debugLine="Return lbl";
if (true) return (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 482;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _createpanel(String _paneleventname) throws Exception{
 //BA.debugLineNum = 443;BA.debugLine="Private Sub CreatePanel (PanelEventName As String)";
 //BA.debugLineNum = 444;BA.debugLine="Return xui.CreatePanel(PanelEventName)";
if (true) return _xui.CreatePanel(ba,_paneleventname);
 //BA.debugLineNum = 445;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _createscrollview() throws Exception{
anywheresoftware.b4a.objects.HorizontalScrollViewWrapper _hv = null;
anywheresoftware.b4a.objects.ScrollViewWrapper _nsv = null;
 //BA.debugLineNum = 452;BA.debugLine="Private Sub CreateScrollView As B4XView";
 //BA.debugLineNum = 453;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 454;BA.debugLine="Dim hv As HorizontalScrollView";
_hv = new anywheresoftware.b4a.objects.HorizontalScrollViewWrapper();
 //BA.debugLineNum = 455;BA.debugLine="hv.Initialize(0, \"sv\")";
_hv.Initialize(ba,(int) (0),"sv");
 //BA.debugLineNum = 456;BA.debugLine="Return hv";
if (true) return (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_hv.getObject()));
 }else {
 //BA.debugLineNum = 458;BA.debugLine="Dim nsv As ScrollView";
_nsv = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 459;BA.debugLine="nsv.Initialize2(0, \"sv\")";
_nsv.Initialize2(ba,(int) (0),"sv");
 //BA.debugLineNum = 460;BA.debugLine="Return nsv";
if (true) return (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_nsv.getObject()));
 };
 //BA.debugLineNum = 462;BA.debugLine="End Sub";
return null;
}
public String  _designercreateview(Object _base,anywheresoftware.b4a.objects.LabelWrapper _lbl,anywheresoftware.b4a.objects.collections.Map _props) throws Exception{
String _o = "";
 //BA.debugLineNum = 50;BA.debugLine="Public Sub DesignerCreateView (Base As Object, Lbl";
 //BA.debugLineNum = 51;BA.debugLine="mBase = Base";
_mbase.setObject((java.lang.Object)(_base));
 //BA.debugLineNum = 52;BA.debugLine="Dim o As String = Props.GetDefault(\"ListOrientati";
_o = BA.ObjectToString(_props.GetDefault((Object)("ListOrientation"),(Object)("Vertical")));
 //BA.debugLineNum = 53;BA.debugLine="horizontal = o = \"Horizontal\"";
_horizontal = (_o).equals("Horizontal");
 //BA.debugLineNum = 54;BA.debugLine="sv = CreateScrollView";
_sv = _createscrollview();
 //BA.debugLineNum = 55;BA.debugLine="mBase.AddView(sv, 0, 0, mBase.Width, mBase.Height";
_mbase.AddView((android.view.View)(_sv.getObject()),(int) (0),(int) (0),_mbase.getWidth(),_mbase.getHeight());
 //BA.debugLineNum = 56;BA.debugLine="sv.ScrollViewInnerPanel.Color = xui.PaintOrColorT";
_sv.getScrollViewInnerPanel().setColor(_xui.PaintOrColorToColor(_props.Get((Object)("DividerColor"))));
 //BA.debugLineNum = 57;BA.debugLine="DividerSize = DipToCurrent(Props.Get(\"DividerHeig";
_dividersize = (float) (__c.DipToCurrent((int)(BA.ObjectToNumber(_props.Get((Object)("DividerHeight"))))));
 //BA.debugLineNum = 58;BA.debugLine="PressedColor = xui.PaintOrColorToColor(Props.Get(";
_pressedcolor = _xui.PaintOrColorToColor(_props.Get((Object)("PressedColor")));
 //BA.debugLineNum = 59;BA.debugLine="AnimationDuration = Props.GetDefault(\"InsertAnima";
_animationduration = (int)(BA.ObjectToNumber(_props.GetDefault((Object)("InsertAnimationDuration"),(Object)(_animationduration))));
 //BA.debugLineNum = 60;BA.debugLine="DefaultTextColor = xui.PaintOrColorToColor(Lbl.Te";
_defaulttextcolor = _xui.PaintOrColorToColor((Object)(_lbl.getTextColor()));
 //BA.debugLineNum = 61;BA.debugLine="DefaultTextBackgroundColor = mBase.Color";
_defaulttextbackgroundcolor = _mbase.getColor();
 //BA.debugLineNum = 62;BA.debugLine="DesignerLabel = Lbl";
_designerlabel = _lbl;
 //BA.debugLineNum = 63;BA.debugLine="Base_Resize(mBase.Width, mBase.Height)";
_base_resize(_mbase.getWidth(),_mbase.getHeight());
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public int  _findindexfromoffset(int _offset) throws Exception{
int _position = 0;
int _stepsize = 0;
andy.home.system.customlistview._clvitem _currentitem = null;
int _nextoffset = 0;
int _prevoffset = 0;
 //BA.debugLineNum = 365;BA.debugLine="Public Sub FindIndexFromOffset(Offset As Int) As I";
 //BA.debugLineNum = 367;BA.debugLine="Dim Position As Int = items.Size / 2";
_position = (int) (_items.getSize()/(double)2);
 //BA.debugLineNum = 368;BA.debugLine="Dim StepSize As Int = Ceil(Position / 2)";
_stepsize = (int) (__c.Ceil(_position/(double)2));
 //BA.debugLineNum = 369;BA.debugLine="Do While True";
while (__c.True) {
 //BA.debugLineNum = 370;BA.debugLine="Dim CurrentItem As CLVItem = items.Get(Position)";
_currentitem = (andy.home.system.customlistview._clvitem)(_items.Get(_position));
 //BA.debugLineNum = 371;BA.debugLine="Dim NextOffset As Int";
_nextoffset = 0;
 //BA.debugLineNum = 372;BA.debugLine="If Position < items.Size - 1 Then";
if (_position<_items.getSize()-1) { 
 //BA.debugLineNum = 373;BA.debugLine="NextOffset = GetItem(Position + 1).Offset - 1";
_nextoffset = (int) (_getitem((int) (_position+1)).Offset-1);
 }else {
 //BA.debugLineNum = 375;BA.debugLine="NextOffset = 0x7FFFFFFF";
_nextoffset = (int) (0x7fffffff);
 };
 //BA.debugLineNum = 377;BA.debugLine="Dim PrevOffset As Int";
_prevoffset = 0;
 //BA.debugLineNum = 378;BA.debugLine="If Position = 0 Then";
if (_position==0) { 
 //BA.debugLineNum = 379;BA.debugLine="PrevOffset = 0x80000000";
_prevoffset = (int) (0x80000000);
 }else {
 //BA.debugLineNum = 381;BA.debugLine="PrevOffset = CurrentItem.Offset";
_prevoffset = _currentitem.Offset;
 };
 //BA.debugLineNum = 383;BA.debugLine="If Offset > NextOffset Then";
if (_offset>_nextoffset) { 
 //BA.debugLineNum = 384;BA.debugLine="Position = Min(Position + StepSize, items.Size";
_position = (int) (__c.Min(_position+_stepsize,_items.getSize()-1));
 }else if(_offset<_prevoffset) { 
 //BA.debugLineNum = 386;BA.debugLine="Position = Max(Position - StepSize, 0)";
_position = (int) (__c.Max(_position-_stepsize,0));
 }else {
 //BA.debugLineNum = 388;BA.debugLine="Return Position";
if (true) return _position;
 };
 //BA.debugLineNum = 390;BA.debugLine="StepSize = Ceil(StepSize / 2)";
_stepsize = (int) (__c.Ceil(_stepsize/(double)2));
 }
;
 //BA.debugLineNum = 392;BA.debugLine="Return -1";
if (true) return (int) (-1);
 //BA.debugLineNum = 393;BA.debugLine="End Sub";
return 0;
}
public int  _finditemoffset(int _index) throws Exception{
 //BA.debugLineNum = 360;BA.debugLine="Private Sub FindItemOffset(Index As Int) As Int";
 //BA.debugLineNum = 361;BA.debugLine="Return GetItem(Index).Offset";
if (true) return _getitem(_index).Offset;
 //BA.debugLineNum = 362;BA.debugLine="End Sub";
return 0;
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _getbase() throws Exception{
 //BA.debugLineNum = 101;BA.debugLine="Public Sub GetBase As B4XView";
 //BA.debugLineNum = 102;BA.debugLine="Return mBase";
if (true) return _mbase;
 //BA.debugLineNum = 103;BA.debugLine="End Sub";
return null;
}
public int  _getfirstvisibleindex() throws Exception{
 //BA.debugLineNum = 396;BA.debugLine="Public Sub getFirstVisibleIndex As Int";
 //BA.debugLineNum = 397;BA.debugLine="Return FindIndexFromOffset(GetScrollViewOffset +";
if (true) return _findindexfromoffset((int) (_getscrollviewoffset()+_dividersize));
 //BA.debugLineNum = 398;BA.debugLine="End Sub";
return 0;
}
public andy.home.system.customlistview._clvitem  _getitem(int _index) throws Exception{
 //BA.debugLineNum = 110;BA.debugLine="Private Sub GetItem(Index As Int) As CLVItem";
 //BA.debugLineNum = 111;BA.debugLine="Return items.Get(Index)";
if (true) return (andy.home.system.customlistview._clvitem)(_items.Get(_index));
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
return null;
}
public int  _getitemfromview(anywheresoftware.b4a.objects.B4XViewWrapper _v) throws Exception{
Object _parent = null;
anywheresoftware.b4a.objects.B4XViewWrapper _current = null;
 //BA.debugLineNum = 416;BA.debugLine="Public Sub GetItemFromView(v As B4XView) As Int";
 //BA.debugLineNum = 417;BA.debugLine="Dim parent = v As Object, current As B4XView";
_parent = (Object)(_v.getObject());
_current = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 418;BA.debugLine="Do While sv.ScrollViewInnerPanel <> parent";
while ((_sv.getScrollViewInnerPanel()).equals((java.lang.Object)(_parent)) == false) {
 //BA.debugLineNum = 419;BA.debugLine="current = parent";
_current.setObject((java.lang.Object)(_parent));
 //BA.debugLineNum = 420;BA.debugLine="parent = current.Parent";
_parent = (Object)(_current.getParent().getObject());
 }
;
 //BA.debugLineNum = 422;BA.debugLine="v = current";
_v = _current;
 //BA.debugLineNum = 423;BA.debugLine="Return v.Tag";
if (true) return (int)(BA.ObjectToNumber(_v.getTag()));
 //BA.debugLineNum = 424;BA.debugLine="End Sub";
return 0;
}
public int  _getlastvisibleindex() throws Exception{
 //BA.debugLineNum = 401;BA.debugLine="Public Sub getLastVisibleIndex As Int";
 //BA.debugLineNum = 402;BA.debugLine="Return FindIndexFromOffset(GetScrollViewOffset +";
if (true) return _findindexfromoffset((int) (_getscrollviewoffset()+_getscrollviewvisiblesize()));
 //BA.debugLineNum = 403;BA.debugLine="End Sub";
return 0;
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _getpanel(int _index) throws Exception{
 //BA.debugLineNum = 115;BA.debugLine="Public Sub GetPanel(Index As Int) As B4XView";
 //BA.debugLineNum = 116;BA.debugLine="Return GetItem(Index).Panel.GetView(0)";
if (true) return _getitem(_index).Panel.GetView((int) (0));
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return null;
}
public float  _getscrollviewcontentsize() throws Exception{
 //BA.debugLineNum = 288;BA.debugLine="Private Sub GetScrollViewContentSize As Float";
 //BA.debugLineNum = 289;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 290;BA.debugLine="Return sv.ScrollViewContentWidth";
if (true) return (float) (_sv.getScrollViewContentWidth());
 }else {
 //BA.debugLineNum = 292;BA.debugLine="Return sv.ScrollViewContentHeight";
if (true) return (float) (_sv.getScrollViewContentHeight());
 };
 //BA.debugLineNum = 294;BA.debugLine="End Sub";
return 0f;
}
public float  _getscrollviewoffset() throws Exception{
 //BA.debugLineNum = 272;BA.debugLine="Private Sub GetScrollViewOffset As Float";
 //BA.debugLineNum = 273;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 274;BA.debugLine="Return sv.ScrollViewOffsetX";
if (true) return (float) (_sv.getScrollViewOffsetX());
 }else {
 //BA.debugLineNum = 276;BA.debugLine="Return sv.ScrollViewOffsetY";
if (true) return (float) (_sv.getScrollViewOffsetY());
 };
 //BA.debugLineNum = 278;BA.debugLine="End Sub";
return 0f;
}
public float  _getscrollviewvisiblesize() throws Exception{
 //BA.debugLineNum = 264;BA.debugLine="Private Sub GetScrollViewVisibleSize As Float";
 //BA.debugLineNum = 265;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 266;BA.debugLine="Return sv.Width";
if (true) return (float) (_sv.getWidth());
 }else {
 //BA.debugLineNum = 268;BA.debugLine="Return sv.Height";
if (true) return (float) (_sv.getHeight());
 };
 //BA.debugLineNum = 270;BA.debugLine="End Sub";
return 0f;
}
public int  _getsize() throws Exception{
 //BA.debugLineNum = 106;BA.debugLine="Public Sub getSize As Int";
 //BA.debugLineNum = 107;BA.debugLine="Return items.Size";
if (true) return _items.getSize();
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return 0;
}
public Object  _getvalue(int _index) throws Exception{
 //BA.debugLineNum = 120;BA.debugLine="Public Sub GetValue(Index As Int) As Object";
 //BA.debugLineNum = 121;BA.debugLine="Return GetItem(Index).Value";
if (true) return _getitem(_index).Value;
 //BA.debugLineNum = 122;BA.debugLine="End Sub";
return null;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,Object _vcallback,String _veventname) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 40;BA.debugLine="Public Sub Initialize (vCallBack As Object, vEvent";
 //BA.debugLineNum = 41;BA.debugLine="EventName = vEventName";
_eventname = _veventname;
 //BA.debugLineNum = 42;BA.debugLine="CallBack = vCallBack";
_callback = _vcallback;
 //BA.debugLineNum = 43;BA.debugLine="items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 44;BA.debugLine="DefaultTextBackgroundColor = xui.Color_White";
_defaulttextbackgroundcolor = _xui.Color_White;
 //BA.debugLineNum = 45;BA.debugLine="DefaultTextColor = xui.Color_Black";
_defaulttextcolor = _xui.Color_Black;
 //BA.debugLineNum = 46;BA.debugLine="MonitorVisibleRange = xui.SubExists(CallBack, Eve";
_monitorvisiblerange = _xui.SubExists(ba,_callback,_eventname+"_VisibleRangeChanged",(int) (2));
 //BA.debugLineNum = 47;BA.debugLine="FireScrollChanged = xui.SubExists(CallBack, Event";
_firescrollchanged = _xui.SubExists(ba,_callback,_eventname+"_ScrollChanged",(int) (1));
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public String  _insertat(int _index,anywheresoftware.b4a.objects.B4XViewWrapper _pnl,Object _value) throws Exception{
float _size = 0f;
 //BA.debugLineNum = 169;BA.debugLine="Public Sub InsertAt(Index As Int, pnl As B4XView,";
 //BA.debugLineNum = 170;BA.debugLine="Dim size As Float";
_size = 0f;
 //BA.debugLineNum = 171;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 172;BA.debugLine="size = pnl.Width";
_size = (float) (_pnl.getWidth());
 }else {
 //BA.debugLineNum = 174;BA.debugLine="size = pnl.Height";
_size = (float) (_pnl.getHeight());
 };
 //BA.debugLineNum = 176;BA.debugLine="InsertAtImpl(Index, pnl, size, Value, 0)";
_insertatimpl(_index,_pnl,(int) (_size),_value,(int) (0));
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
return "";
}
public String  _insertatimpl(int _index,anywheresoftware.b4a.objects.B4XViewWrapper _pnl,int _itemsize,Object _value,int _initialsize) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
int _includedividierheight = 0;
andy.home.system.customlistview._clvitem _newitem = null;
int _offset = 0;
andy.home.system.customlistview._clvitem _previtem = null;
int _i = 0;
andy.home.system.customlistview._clvitem _it = null;
int _newoffset = 0;
 //BA.debugLineNum = 180;BA.debugLine="Private Sub InsertAtImpl(Index As Int, Pnl As B4XV";
 //BA.debugLineNum = 182;BA.debugLine="Dim p As B4XView = CreatePanel(\"Panel\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _createpanel("Panel");
 //BA.debugLineNum = 183;BA.debugLine="p.Color = Pnl.Color";
_p.setColor(_pnl.getColor());
 //BA.debugLineNum = 184;BA.debugLine="Pnl.Color = xui.Color_Transparent";
_pnl.setColor(_xui.Color_Transparent);
 //BA.debugLineNum = 185;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 186;BA.debugLine="p.AddView(Pnl, 0, 0, ItemSize, sv.ScrollViewCont";
_p.AddView((android.view.View)(_pnl.getObject()),(int) (0),(int) (0),_itemsize,_sv.getScrollViewContentHeight());
 }else {
 //BA.debugLineNum = 188;BA.debugLine="p.AddView(Pnl, 0, 0, sv.ScrollViewContentWidth,";
_p.AddView((android.view.View)(_pnl.getObject()),(int) (0),(int) (0),_sv.getScrollViewContentWidth(),_itemsize);
 };
 //BA.debugLineNum = 190;BA.debugLine="p.Tag = Index";
_p.setTag((Object)(_index));
 //BA.debugLineNum = 191;BA.debugLine="Dim IncludeDividierHeight As Int";
_includedividierheight = 0;
 //BA.debugLineNum = 192;BA.debugLine="If InitialSize = 0 Then IncludeDividierHeight = D";
if (_initialsize==0) { 
_includedividierheight = (int) (_dividersize);}
else {
_includedividierheight = (int) (0);};
 //BA.debugLineNum = 193;BA.debugLine="Dim NewItem As CLVItem";
_newitem = new andy.home.system.customlistview._clvitem();
 //BA.debugLineNum = 194;BA.debugLine="NewItem.Panel = p";
_newitem.Panel = _p;
 //BA.debugLineNum = 195;BA.debugLine="NewItem.Size = ItemSize";
_newitem.Size = _itemsize;
 //BA.debugLineNum = 196;BA.debugLine="NewItem.Value = Value";
_newitem.Value = _value;
 //BA.debugLineNum = 197;BA.debugLine="NewItem.Color = p.Color";
_newitem.Color = _p.getColor();
 //BA.debugLineNum = 198;BA.debugLine="If Index = items.Size And InitialSize = 0 Then";
if (_index==_items.getSize() && _initialsize==0) { 
 //BA.debugLineNum = 199;BA.debugLine="items.Add(NewItem)";
_items.Add((Object)(_newitem));
 //BA.debugLineNum = 200;BA.debugLine="Dim offset As Int";
_offset = 0;
 //BA.debugLineNum = 201;BA.debugLine="If Index = 0 Then";
if (_index==0) { 
 //BA.debugLineNum = 202;BA.debugLine="offset = DividerSize";
_offset = (int) (_dividersize);
 }else {
 //BA.debugLineNum = 204;BA.debugLine="offset = GetScrollViewContentSize";
_offset = (int) (_getscrollviewcontentsize());
 };
 //BA.debugLineNum = 206;BA.debugLine="NewItem.Offset = offset";
_newitem.Offset = _offset;
 //BA.debugLineNum = 207;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 208;BA.debugLine="sv.ScrollViewInnerPanel.AddView(p, offset, 0, I";
_sv.getScrollViewInnerPanel().AddView((android.view.View)(_p.getObject()),_offset,(int) (0),_itemsize,_sv.getHeight());
 }else {
 //BA.debugLineNum = 210;BA.debugLine="sv.ScrollViewInnerPanel.AddView(p, 0, offset, s";
_sv.getScrollViewInnerPanel().AddView((android.view.View)(_p.getObject()),(int) (0),_offset,_sv.getWidth(),_itemsize);
 };
 }else {
 //BA.debugLineNum = 213;BA.debugLine="Dim offset As Int";
_offset = 0;
 //BA.debugLineNum = 214;BA.debugLine="If Index = 0 Then";
if (_index==0) { 
 //BA.debugLineNum = 215;BA.debugLine="offset = DividerSize";
_offset = (int) (_dividersize);
 }else {
 //BA.debugLineNum = 217;BA.debugLine="Dim PrevItem As CLVItem = items.Get(Index - 1)";
_previtem = (andy.home.system.customlistview._clvitem)(_items.Get((int) (_index-1)));
 //BA.debugLineNum = 218;BA.debugLine="offset = PrevItem.Offset + PrevItem.Size + Divi";
_offset = (int) (_previtem.Offset+_previtem.Size+_dividersize);
 };
 //BA.debugLineNum = 220;BA.debugLine="For i = Index To items.Size - 1";
{
final int step39 = 1;
final int limit39 = (int) (_items.getSize()-1);
_i = _index ;
for (;(step39 > 0 && _i <= limit39) || (step39 < 0 && _i >= limit39) ;_i = ((int)(0 + _i + step39))  ) {
 //BA.debugLineNum = 221;BA.debugLine="Dim It As CLVItem = items.Get(i)";
_it = (andy.home.system.customlistview._clvitem)(_items.Get(_i));
 //BA.debugLineNum = 222;BA.debugLine="Dim NewOffset As Int = It.Offset + ItemSize - I";
_newoffset = (int) (_it.Offset+_itemsize-_initialsize+_includedividierheight);
 //BA.debugLineNum = 223;BA.debugLine="If Min(NewOffset, It.Offset) - GetScrollViewOff";
if (__c.Min(_newoffset,_it.Offset)-_getscrollviewoffset()<_getscrollviewvisiblesize()) { 
 //BA.debugLineNum = 224;BA.debugLine="It.Offset = NewOffset";
_it.Offset = _newoffset;
 //BA.debugLineNum = 225;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 226;BA.debugLine="It.Panel.SetLayoutAnimated(AnimationDuration,";
_it.Panel.SetLayoutAnimated(_animationduration,_newoffset,(int) (0),_it.Size,_it.Panel.getHeight());
 }else {
 //BA.debugLineNum = 228;BA.debugLine="It.Panel.SetLayoutAnimated(AnimationDuration,";
_it.Panel.SetLayoutAnimated(_animationduration,(int) (0),_newoffset,_it.Panel.getWidth(),_it.Size);
 };
 }else {
 //BA.debugLineNum = 231;BA.debugLine="SetItemOffset(It, NewOffset)";
_setitemoffset(_it,(float) (_newoffset));
 };
 //BA.debugLineNum = 233;BA.debugLine="It.Panel.Tag = i + 1";
_it.Panel.setTag((Object)(_i+1));
 }
};
 //BA.debugLineNum = 235;BA.debugLine="items.InsertAt(Index, NewItem)";
_items.InsertAt(_index,(Object)(_newitem));
 //BA.debugLineNum = 236;BA.debugLine="NewItem.Offset = offset";
_newitem.Offset = _offset;
 //BA.debugLineNum = 237;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 238;BA.debugLine="sv.ScrollViewInnerPanel.AddView(p, offset, 0, I";
_sv.getScrollViewInnerPanel().AddView((android.view.View)(_p.getObject()),_offset,(int) (0),_initialsize,_sv.getHeight());
 //BA.debugLineNum = 239;BA.debugLine="p.SetLayoutAnimated(AnimationDuration, offset,";
_p.SetLayoutAnimated(_animationduration,_offset,(int) (0),_itemsize,_p.getHeight());
 }else {
 //BA.debugLineNum = 241;BA.debugLine="sv.ScrollViewInnerPanel.AddView(p, 0, offset, s";
_sv.getScrollViewInnerPanel().AddView((android.view.View)(_p.getObject()),(int) (0),_offset,_sv.getWidth(),_initialsize);
 //BA.debugLineNum = 242;BA.debugLine="p.SetLayoutAnimated(AnimationDuration, 0, offse";
_p.SetLayoutAnimated(_animationduration,(int) (0),_offset,_p.getWidth(),_itemsize);
 };
 };
 //BA.debugLineNum = 245;BA.debugLine="SetScrollViewContentSize(GetScrollViewContentSize";
_setscrollviewcontentsize((float) (_getscrollviewcontentsize()+_itemsize-_initialsize+_includedividierheight));
 //BA.debugLineNum = 246;BA.debugLine="If items.Size = 1 Then SetScrollViewContentSize(G";
if (_items.getSize()==1) { 
_setscrollviewcontentsize((float) (_getscrollviewcontentsize()+_includedividierheight));};
 //BA.debugLineNum = 247;BA.debugLine="If Index < mLastVisibleIndex Or GetItem(mLastVisi";
if (_index<_mlastvisibleindex || _getitem(_mlastvisibleindex).Offset+_getitem(_mlastvisibleindex).Size+_dividersize<_getscrollviewvisiblesize()) { 
 //BA.debugLineNum = 249;BA.debugLine="UpdateVisibleRange";
_updatevisiblerange();
 };
 //BA.debugLineNum = 251;BA.debugLine="End Sub";
return "";
}
public String  _insertattextitem(int _index,Object _text,Object _value) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _pnl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
andy.home.system.customlistview._clvitem _item = null;
 //BA.debugLineNum = 151;BA.debugLine="Public Sub InsertAtTextItem(Index As Int, Text As";
 //BA.debugLineNum = 152;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 153;BA.debugLine="Log(\"AddTextItem is only supported in vertical o";
__c.Log("AddTextItem is only supported in vertical orientation.");
 //BA.debugLineNum = 154;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 156;BA.debugLine="Dim pnl As B4XView = CreatePanel(\"\")";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = _createpanel("");
 //BA.debugLineNum = 157;BA.debugLine="Dim lbl As B4XView = CreateLabel(Text)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _createlabel(_text);
 //BA.debugLineNum = 158;BA.debugLine="lbl.Height = Max(50dip, lbl.Height)";
_lbl.setHeight((int) (__c.Max(__c.DipToCurrent((int) (50)),_lbl.getHeight())));
 //BA.debugLineNum = 159;BA.debugLine="pnl.AddView(lbl, 5dip, 2dip, sv.ScrollViewContent";
_pnl.AddView((android.view.View)(_lbl.getObject()),__c.DipToCurrent((int) (5)),__c.DipToCurrent((int) (2)),(int) (_sv.getScrollViewContentWidth()-__c.DipToCurrent((int) (10))),_lbl.getHeight());
 //BA.debugLineNum = 160;BA.debugLine="lbl.TextColor = DefaultTextColor";
_lbl.setTextColor(_defaulttextcolor);
 //BA.debugLineNum = 161;BA.debugLine="pnl.Color = DefaultTextBackgroundColor";
_pnl.setColor(_defaulttextbackgroundcolor);
 //BA.debugLineNum = 162;BA.debugLine="pnl.Height = lbl.Height + 2dip";
_pnl.setHeight((int) (_lbl.getHeight()+__c.DipToCurrent((int) (2))));
 //BA.debugLineNum = 163;BA.debugLine="InsertAt(Index, pnl, Value)";
_insertat(_index,_pnl,_value);
 //BA.debugLineNum = 164;BA.debugLine="Dim item As CLVItem = GetItem(Index)";
_item = _getitem(_index);
 //BA.debugLineNum = 165;BA.debugLine="item.TextItem = True";
_item.TextItem = __c.True;
 //BA.debugLineNum = 166;BA.debugLine="End Sub";
return "";
}
public String  _jumptoitem(int _index) throws Exception{
 //BA.debugLineNum = 340;BA.debugLine="Public Sub JumpToItem(Index As Int)";
 //BA.debugLineNum = 341;BA.debugLine="SetScrollViewOffset(FindItemOffset(Index))";
_setscrollviewoffset((float) (_finditemoffset(_index)));
 //BA.debugLineNum = 342;BA.debugLine="End Sub";
return "";
}
public String  _panel_click() throws Exception{
 //BA.debugLineNum = 464;BA.debugLine="Private Sub Panel_Click";
 //BA.debugLineNum = 465;BA.debugLine="PanelClickHandler(Sender)";
_panelclickhandler((anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(__c.Sender(ba))));
 //BA.debugLineNum = 466;BA.debugLine="End Sub";
return "";
}
public void  _panelclickhandler(anywheresoftware.b4a.objects.B4XViewWrapper _senderpanel) throws Exception{
ResumableSub_PanelClickHandler rsub = new ResumableSub_PanelClickHandler(this,_senderpanel);
rsub.resume(ba, null);
}
public static class ResumableSub_PanelClickHandler extends BA.ResumableSub {
public ResumableSub_PanelClickHandler(andy.home.system.customlistview parent,anywheresoftware.b4a.objects.B4XViewWrapper _senderpanel) {
this.parent = parent;
this._senderpanel = _senderpanel;
}
andy.home.system.customlistview parent;
anywheresoftware.b4a.objects.B4XViewWrapper _senderpanel;
int _clr = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 406;BA.debugLine="Dim clr As Int = GetItem(SenderPanel.Tag).Color";
_clr = parent._getitem((int)(BA.ObjectToNumber(_senderpanel.getTag()))).Color;
 //BA.debugLineNum = 407;BA.debugLine="SenderPanel.SetColorAnimated(50, clr, PressedColo";
_senderpanel.SetColorAnimated((int) (50),_clr,parent._pressedcolor);
 //BA.debugLineNum = 408;BA.debugLine="If xui.SubExists(CallBack, EventName & \"_ItemClic";
if (true) break;

case 1:
//if
this.state = 4;
if (parent._xui.SubExists(ba,parent._callback,parent._eventname+"_ItemClick",(int) (2))) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 409;BA.debugLine="CallSub3(CallBack, EventName & \"_ItemClick\", Sen";
parent.__c.CallSubNew3(ba,parent._callback,parent._eventname+"_ItemClick",_senderpanel.getTag(),parent._getitem((int)(BA.ObjectToNumber(_senderpanel.getTag()))).Value);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 411;BA.debugLine="Sleep(200)";
parent.__c.Sleep(ba,this,(int) (200));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 412;BA.debugLine="SenderPanel.SetColorAnimated(200, PressedColor, c";
_senderpanel.SetColorAnimated((int) (200),parent._pressedcolor,_clr);
 //BA.debugLineNum = 413;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public String  _removeat(int _index) throws Exception{
andy.home.system.customlistview._clvitem _removeitem = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
int _i = 0;
andy.home.system.customlistview._clvitem _item = null;
int _newoffset = 0;
 //BA.debugLineNum = 125;BA.debugLine="Public Sub RemoveAt(Index As Int)";
 //BA.debugLineNum = 126;BA.debugLine="If getSize <= 1 Then";
if (_getsize()<=1) { 
 //BA.debugLineNum = 127;BA.debugLine="Clear";
_clear();
 //BA.debugLineNum = 128;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 130;BA.debugLine="Dim RemoveItem As CLVItem = items.Get(Index)";
_removeitem = (andy.home.system.customlistview._clvitem)(_items.Get(_index));
 //BA.debugLineNum = 131;BA.debugLine="Dim p As B4XView";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 132;BA.debugLine="For i = Index + 1 To items.Size - 1";
{
final int step7 = 1;
final int limit7 = (int) (_items.getSize()-1);
_i = (int) (_index+1) ;
for (;(step7 > 0 && _i <= limit7) || (step7 < 0 && _i >= limit7) ;_i = ((int)(0 + _i + step7))  ) {
 //BA.debugLineNum = 133;BA.debugLine="Dim item As CLVItem = items.Get(i)";
_item = (andy.home.system.customlistview._clvitem)(_items.Get(_i));
 //BA.debugLineNum = 134;BA.debugLine="p = item.Panel";
_p = _item.Panel;
 //BA.debugLineNum = 135;BA.debugLine="p.Tag = i - 1";
_p.setTag((Object)(_i-1));
 //BA.debugLineNum = 136;BA.debugLine="Dim NewOffset As Int = item.Offset - RemoveItem.";
_newoffset = (int) (_item.Offset-_removeitem.Size-_dividersize);
 //BA.debugLineNum = 137;BA.debugLine="SetItemOffset(item, NewOffset)";
_setitemoffset(_item,(float) (_newoffset));
 }
};
 //BA.debugLineNum = 139;BA.debugLine="SetScrollViewContentSize(GetScrollViewContentSize";
_setscrollviewcontentsize((float) (_getscrollviewcontentsize()-_removeitem.Size-_dividersize));
 //BA.debugLineNum = 140;BA.debugLine="items.RemoveAt(Index)";
_items.RemoveAt(_index);
 //BA.debugLineNum = 141;BA.debugLine="RemoveItem.Panel.RemoveViewFromParent";
_removeitem.Panel.RemoveViewFromParent();
 //BA.debugLineNum = 142;BA.debugLine="End Sub";
return "";
}
public String  _replaceat(int _index,anywheresoftware.b4a.objects.B4XViewWrapper _pnl,int _itemheight,Object _value) throws Exception{
andy.home.system.customlistview._clvitem _removeitem = null;
 //BA.debugLineNum = 325;BA.debugLine="Public Sub ReplaceAt(Index As Int, pnl As B4XView,";
 //BA.debugLineNum = 326;BA.debugLine="Dim RemoveItem As CLVItem = items.Get(Index)";
_removeitem = (andy.home.system.customlistview._clvitem)(_items.Get(_index));
 //BA.debugLineNum = 327;BA.debugLine="items.RemoveAt(Index)";
_items.RemoveAt(_index);
 //BA.debugLineNum = 328;BA.debugLine="RemoveItem.Panel.RemoveViewFromParent";
_removeitem.Panel.RemoveViewFromParent();
 //BA.debugLineNum = 329;BA.debugLine="InsertAtImpl(Index, pnl, ItemHeight, Value, Remov";
_insertatimpl(_index,_pnl,_itemheight,_value,_removeitem.Size);
 //BA.debugLineNum = 330;BA.debugLine="End Sub";
return "";
}
public String  _resizeitem(int _index,int _itemheight) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
Object _value = null;
anywheresoftware.b4a.objects.B4XViewWrapper _parent = null;
 //BA.debugLineNum = 314;BA.debugLine="Public Sub ResizeItem(Index As Int, ItemHeight As";
 //BA.debugLineNum = 315;BA.debugLine="Dim p As B4XView = GetPanel(Index)";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _getpanel(_index);
 //BA.debugLineNum = 316;BA.debugLine="Dim value As Object = items.Get(Index)";
_value = _items.Get(_index);
 //BA.debugLineNum = 317;BA.debugLine="Dim parent As B4XView = p.Parent";
_parent = new anywheresoftware.b4a.objects.B4XViewWrapper();
_parent = _p.getParent();
 //BA.debugLineNum = 318;BA.debugLine="p.Color = parent.Color";
_p.setColor(_parent.getColor());
 //BA.debugLineNum = 319;BA.debugLine="p.RemoveViewFromParent";
_p.RemoveViewFromParent();
 //BA.debugLineNum = 320;BA.debugLine="ReplaceAt(Index, p, ItemHeight, value)";
_replaceat(_index,_p,_itemheight,_value);
 //BA.debugLineNum = 321;BA.debugLine="End Sub";
return "";
}
public String  _scrollhandler() throws Exception{
int _position = 0;
 //BA.debugLineNum = 426;BA.debugLine="Private Sub ScrollHandler";
 //BA.debugLineNum = 427;BA.debugLine="If items.Size = 0 Then Return";
if (_items.getSize()==0) { 
if (true) return "";};
 //BA.debugLineNum = 428;BA.debugLine="Dim position As Int = GetScrollViewOffset";
_position = (int) (_getscrollviewoffset());
 //BA.debugLineNum = 429;BA.debugLine="If position + GetScrollViewVisibleSize >= GetScro";
if (_position+_getscrollviewvisiblesize()>=_getscrollviewcontentsize()-__c.DipToCurrent((int) (5)) && __c.DateTime.getNow()>_lastreachendevent+100) { 
 //BA.debugLineNum = 430;BA.debugLine="If xui.SubExists(CallBack, EventName & \"_ReachEn";
if (_xui.SubExists(ba,_callback,_eventname+"_ReachEnd",(int) (0))) { 
 //BA.debugLineNum = 431;BA.debugLine="LastReachEndEvent = DateTime.Now";
_lastreachendevent = __c.DateTime.getNow();
 //BA.debugLineNum = 432;BA.debugLine="CallSubDelayed(CallBack, EventName & \"_ReachEnd";
__c.CallSubDelayed(ba,_callback,_eventname+"_ReachEnd");
 }else {
 //BA.debugLineNum = 434;BA.debugLine="LastReachEndEvent = DateTime.Now + 1000 * DateT";
_lastreachendevent = (long) (__c.DateTime.getNow()+1000*__c.DateTime.TicksPerDay);
 };
 };
 //BA.debugLineNum = 437;BA.debugLine="UpdateVisibleRange";
_updatevisiblerange();
 //BA.debugLineNum = 438;BA.debugLine="If FireScrollChanged Then";
if (_firescrollchanged) { 
 //BA.debugLineNum = 439;BA.debugLine="CallSub2(CallBack, EventName & \"_ScrollChanged\",";
__c.CallSubNew2(ba,_callback,_eventname+"_ScrollChanged",(Object)(_position));
 };
 //BA.debugLineNum = 441;BA.debugLine="End Sub";
return "";
}
public String  _scrolltoitem(int _index) throws Exception{
float _offset = 0f;
anywheresoftware.b4a.objects.HorizontalScrollViewWrapper _hv = null;
anywheresoftware.b4a.objects.ScrollViewWrapper _nsv = null;
 //BA.debugLineNum = 345;BA.debugLine="Public Sub ScrollToItem(Index As Int)";
 //BA.debugLineNum = 349;BA.debugLine="Dim offset As Float = FindItemOffset(Index)";
_offset = (float) (_finditemoffset(_index));
 //BA.debugLineNum = 350;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 351;BA.debugLine="Dim hv As HorizontalScrollView = sv";
_hv = new anywheresoftware.b4a.objects.HorizontalScrollViewWrapper();
_hv.setObject((android.widget.HorizontalScrollView)(_sv.getObject()));
 //BA.debugLineNum = 352;BA.debugLine="hv.ScrollPosition = offset 'smooth scroll";
_hv.setScrollPosition((int) (_offset));
 }else {
 //BA.debugLineNum = 354;BA.debugLine="Dim nsv As ScrollView = sv";
_nsv = new anywheresoftware.b4a.objects.ScrollViewWrapper();
_nsv.setObject((android.widget.ScrollView)(_sv.getObject()));
 //BA.debugLineNum = 355;BA.debugLine="nsv.ScrollPosition = offset";
_nsv.setScrollPosition((int) (_offset));
 };
 //BA.debugLineNum = 358;BA.debugLine="End Sub";
return "";
}
public String  _setitemoffset(andy.home.system.customlistview._clvitem _item,float _offset) throws Exception{
 //BA.debugLineNum = 304;BA.debugLine="Private Sub SetItemOffset (item As CLVItem, offset";
 //BA.debugLineNum = 305;BA.debugLine="item.Offset = offset";
_item.Offset = (int) (_offset);
 //BA.debugLineNum = 306;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 307;BA.debugLine="item.Panel.Left = offset";
_item.Panel.setLeft((int) (_offset));
 }else {
 //BA.debugLineNum = 309;BA.debugLine="item.Panel.Top = offset";
_item.Panel.setTop((int) (_offset));
 };
 //BA.debugLineNum = 311;BA.debugLine="End Sub";
return "";
}
public String  _setscrollviewcontentsize(float _f) throws Exception{
 //BA.debugLineNum = 296;BA.debugLine="Private Sub SetScrollViewContentSize(f As Float)";
 //BA.debugLineNum = 297;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 298;BA.debugLine="sv.ScrollViewContentWidth = f";
_sv.setScrollViewContentWidth((int) (_f));
 }else {
 //BA.debugLineNum = 300;BA.debugLine="sv.ScrollViewContentHeight = f";
_sv.setScrollViewContentHeight((int) (_f));
 };
 //BA.debugLineNum = 302;BA.debugLine="End Sub";
return "";
}
public String  _setscrollviewoffset(float _offset) throws Exception{
 //BA.debugLineNum = 280;BA.debugLine="Private Sub SetScrollViewOffset(offset As Float)";
 //BA.debugLineNum = 281;BA.debugLine="If horizontal Then";
if (_horizontal) { 
 //BA.debugLineNum = 282;BA.debugLine="sv.ScrollViewOffsetX = offset";
_sv.setScrollViewOffsetX((int) (_offset));
 }else {
 //BA.debugLineNum = 284;BA.debugLine="sv.ScrollViewOffsetY = offset";
_sv.setScrollViewOffsetY((int) (_offset));
 };
 //BA.debugLineNum = 286;BA.debugLine="End Sub";
return "";
}
public String  _sv_scrollchanged(int _position) throws Exception{
 //BA.debugLineNum = 468;BA.debugLine="Private Sub sv_ScrollChanged(Position As Int)";
 //BA.debugLineNum = 469;BA.debugLine="ScrollHandler";
_scrollhandler();
 //BA.debugLineNum = 470;BA.debugLine="End Sub";
return "";
}
public String  _updatevisiblerange() throws Exception{
int _first = 0;
int _last = 0;
 //BA.debugLineNum = 253;BA.debugLine="Private Sub UpdateVisibleRange";
 //BA.debugLineNum = 254;BA.debugLine="If MonitorVisibleRange = False Then Return";
if (_monitorvisiblerange==__c.False) { 
if (true) return "";};
 //BA.debugLineNum = 255;BA.debugLine="Dim first As Int = getFirstVisibleIndex";
_first = _getfirstvisibleindex();
 //BA.debugLineNum = 256;BA.debugLine="Dim last As Int = getLastVisibleIndex";
_last = _getlastvisibleindex();
 //BA.debugLineNum = 257;BA.debugLine="If first <> mFirstVisibleIndex Or last <> mLastVi";
if (_first!=_mfirstvisibleindex || _last!=_mlastvisibleindex) { 
 //BA.debugLineNum = 258;BA.debugLine="mFirstVisibleIndex = first";
_mfirstvisibleindex = _first;
 //BA.debugLineNum = 259;BA.debugLine="mLastVisibleIndex = last";
_mlastvisibleindex = _last;
 //BA.debugLineNum = 260;BA.debugLine="CallSubDelayed3(CallBack, EventName & \"_VisibleR";
__c.CallSubDelayed3(ba,_callback,_eventname+"_VisibleRangeChanged",(Object)(_mfirstvisibleindex),(Object)(_mlastvisibleindex));
 };
 //BA.debugLineNum = 262;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
