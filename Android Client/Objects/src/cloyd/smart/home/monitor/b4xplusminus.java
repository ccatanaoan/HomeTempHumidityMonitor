package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xplusminus extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.b4xplusminus");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.b4xplusminus.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _meventname = "";
public Object _mcallback = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mbase = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public Object _tag = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _pnlplus = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _pnlminus = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _lblplus = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _lblminus = null;
public String _formation = "";
public boolean _mcyclic = false;
public boolean _mrapid = false;
public anywheresoftware.b4a.objects.B4XViewWrapper _mainlabel = null;
public int _arrowssize = 0;
public anywheresoftware.b4a.objects.collections.List _mstringitems = null;
public double _mstartrange = 0;
public double _minterval = 0;
public double _mendrange = 0;
public int _mselectedindex = 0;
public int _loopindex = 0;
public int _rapidperiod1 = 0;
public int _rapidperiod2 = 0;
public cloyd.smart.home.monitor.b4xformatter _formatter = null;
public boolean _stringmode = false;
public int _size = 0;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public String  _base_resize(double _width,double _height) throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Public Sub Base_Resize (Width As Double, Height As";
 //BA.debugLineNum = 81;BA.debugLine="If Formation = \"Horizontal\" Then";
if ((_formation).equals("Horizontal")) { 
 //BA.debugLineNum = 82;BA.debugLine="pnlMinus.SetLayoutAnimated(0, 0, 0, ArrowsSize,";
_pnlminus.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_arrowssize,(int) (_height));
 //BA.debugLineNum = 83;BA.debugLine="pnlPlus.SetLayoutAnimated(0, Width - ArrowsSize,";
_pnlplus.SetLayoutAnimated((int) (0),(int) (_width-_arrowssize),(int) (0),_arrowssize,(int) (_height));
 //BA.debugLineNum = 84;BA.debugLine="MainLabel.SetLayoutAnimated(0, 0, 0, Width, Heig";
_mainlabel.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),(int) (_height));
 }else if((_formation).equals("Vertical")) { 
 //BA.debugLineNum = 86;BA.debugLine="pnlPlus.SetLayoutAnimated(0, 0, 0, Width, Arrows";
_pnlplus.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),_arrowssize);
 //BA.debugLineNum = 87;BA.debugLine="pnlMinus.SetLayoutAnimated(0, 0, Height - Arrows";
_pnlminus.SetLayoutAnimated((int) (0),(int) (0),(int) (_height-_arrowssize),(int) (_width),_arrowssize);
 //BA.debugLineNum = 88;BA.debugLine="MainLabel.SetLayoutAnimated(0, 0, 0, Width, Heig";
_mainlabel.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),(int) (_height));
 }else {
 //BA.debugLineNum = 90;BA.debugLine="pnlMinus.SetLayoutAnimated(0, 0, Height - Arrows";
_pnlminus.SetLayoutAnimated((int) (0),(int) (0),(int) (_height-_arrowssize),(int) (_width/(double)2),_arrowssize);
 //BA.debugLineNum = 91;BA.debugLine="pnlPlus.SetLayoutAnimated(0, Width / 2, pnlMinus";
_pnlplus.SetLayoutAnimated((int) (0),(int) (_width/(double)2),_pnlminus.getTop(),_pnlminus.getWidth(),_pnlminus.getHeight());
 //BA.debugLineNum = 92;BA.debugLine="MainLabel.SetLayoutAnimated(0, 0, 0, Width, Heig";
_mainlabel.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),(int) (_height-_arrowssize+__c.DipToCurrent((int) (5))));
 };
 //BA.debugLineNum = 94;BA.debugLine="lblMinus.SetLayoutAnimated(0, 0, 0, pnlMinus.Widt";
_lblminus.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_pnlminus.getWidth(),_pnlminus.getHeight());
 //BA.debugLineNum = 95;BA.debugLine="lblPlus.SetLayoutAnimated(0, 0, 0, pnlPlus.Width,";
_lblplus.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_pnlplus.getWidth(),_pnlplus.getHeight());
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 10;BA.debugLine="Public Tag As Object";
_tag = new Object();
 //BA.debugLineNum = 11;BA.debugLine="Public pnlPlus, pnlMinus As B4XView";
_pnlplus = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnlminus = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Public lblPlus, lblMinus As B4XView";
_lblplus = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lblminus = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Public Formation As String";
_formation = "";
 //BA.debugLineNum = 14;BA.debugLine="Public mCyclic As Boolean";
_mcyclic = false;
 //BA.debugLineNum = 15;BA.debugLine="Public mRapid As Boolean";
_mrapid = false;
 //BA.debugLineNum = 16;BA.debugLine="Public MainLabel As B4XView";
_mainlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Public ArrowsSize As Int = 30dip";
_arrowssize = __c.DipToCurrent((int) (30));
 //BA.debugLineNum = 18;BA.debugLine="Private mStringItems As List";
_mstringitems = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 19;BA.debugLine="Private mStartRange, mInterval, mEndRange As Doub";
_mstartrange = 0;
_minterval = 0;
_mendrange = 0;
 //BA.debugLineNum = 20;BA.debugLine="Private mSelectedIndex As Int = -1";
_mselectedindex = (int) (-1);
 //BA.debugLineNum = 21;BA.debugLine="Private LoopIndex As Int";
_loopindex = 0;
 //BA.debugLineNum = 22;BA.debugLine="Public RapidPeriod1 As Int = 1000";
_rapidperiod1 = (int) (1000);
 //BA.debugLineNum = 23;BA.debugLine="Public RapidPeriod2 As Int = 30";
_rapidperiod2 = (int) (30);
 //BA.debugLineNum = 24;BA.debugLine="Public Formatter As B4XFormatter";
_formatter = new cloyd.smart.home.monitor.b4xformatter();
 //BA.debugLineNum = 25;BA.debugLine="Private StringMode As Boolean";
_stringmode = false;
 //BA.debugLineNum = 26;BA.debugLine="Private Size As Int";
_size = 0;
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _createlabel(String _text) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _xlbl = null;
 //BA.debugLineNum = 243;BA.debugLine="Private Sub CreateLabel (text As String) As B4XVie";
 //BA.debugLineNum = 244;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 245;BA.debugLine="lbl.Initialize(\"\")";
_lbl.Initialize(ba,"");
 //BA.debugLineNum = 246;BA.debugLine="Dim xlbl As B4XView = lbl";
_xlbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_xlbl.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 247;BA.debugLine="xlbl.Font = xui.CreateMaterialIcons(30)";
_xlbl.setFont(_xui.CreateMaterialIcons((float) (30)));
 //BA.debugLineNum = 248;BA.debugLine="xlbl.Text = text";
_xlbl.setText(BA.ObjectToCharSequence(_text));
 //BA.debugLineNum = 249;BA.debugLine="xlbl.TextColor = MainLabel.TextColor";
_xlbl.setTextColor(_mainlabel.getTextColor());
 //BA.debugLineNum = 250;BA.debugLine="Return xlbl";
if (true) return _xlbl;
 //BA.debugLineNum = 251;BA.debugLine="End Sub";
return null;
}
public String  _decrement() throws Exception{
 //BA.debugLineNum = 179;BA.debugLine="Public Sub Decrement";
 //BA.debugLineNum = 180;BA.debugLine="If mSelectedIndex <= 0 Then";
if (_mselectedindex<=0) { 
 //BA.debugLineNum = 181;BA.debugLine="If mCyclic Then";
if (_mcyclic) { 
 //BA.debugLineNum = 182;BA.debugLine="mSelectedIndex = Size";
_mselectedindex = _size;
 }else {
 //BA.debugLineNum = 184;BA.debugLine="mSelectedIndex = 0";
_mselectedindex = (int) (0);
 //BA.debugLineNum = 185;BA.debugLine="LoopIndex = LoopIndex + 1";
_loopindex = (int) (_loopindex+1);
 //BA.debugLineNum = 186;BA.debugLine="Return";
if (true) return "";
 };
 };
 //BA.debugLineNum = 189;BA.debugLine="SetIndex(mSelectedIndex - 1, True)";
_setindex((int) (_mselectedindex-1),__c.True);
 //BA.debugLineNum = 190;BA.debugLine="End Sub";
return "";
}
public String  _designercreateview(Object _base,anywheresoftware.b4a.objects.LabelWrapper _lbl,anywheresoftware.b4a.objects.collections.Map _props) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _pnl = null;
 //BA.debugLineNum = 36;BA.debugLine="Public Sub DesignerCreateView (Base As Object, lbl";
 //BA.debugLineNum = 37;BA.debugLine="mBase = Base";
_mbase.setObject((java.lang.Object)(_base));
 //BA.debugLineNum = 38;BA.debugLine="Tag = mBase.Tag : mBase.Tag = Me";
_tag = _mbase.getTag();
 //BA.debugLineNum = 38;BA.debugLine="Tag = mBase.Tag : mBase.Tag = Me";
_mbase.setTag(this);
 //BA.debugLineNum = 39;BA.debugLine="MainLabel = lbl";
_mainlabel.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 40;BA.debugLine="MainLabel.SetTextAlignment(\"CENTER\", \"CENTER\")";
_mainlabel.SetTextAlignment("CENTER","CENTER");
 //BA.debugLineNum = 41;BA.debugLine="Dim pnl As B4XView = Props.Get(\"page\")";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl.setObject((java.lang.Object)(_props.Get((Object)("page"))));
 //BA.debugLineNum = 51;BA.debugLine="pnlPlus = xui.CreatePanel(\"pnlArrow\")";
_pnlplus = _xui.CreatePanel(ba,"pnlArrow");
 //BA.debugLineNum = 52;BA.debugLine="pnlMinus = xui.CreatePanel(\"pnlArrow\")";
_pnlminus = _xui.CreatePanel(ba,"pnlArrow");
 //BA.debugLineNum = 54;BA.debugLine="pnlPlus.Tag = True";
_pnlplus.setTag((Object)(__c.True));
 //BA.debugLineNum = 55;BA.debugLine="pnlMinus.Tag = False";
_pnlminus.setTag((Object)(__c.False));
 //BA.debugLineNum = 56;BA.debugLine="Formation = Props.Get(\"Orientation\")";
_formation = BA.ObjectToString(_props.Get((Object)("Orientation")));
 //BA.debugLineNum = 57;BA.debugLine="mCyclic = Props.Get(\"Cyclic\")";
_mcyclic = BA.ObjectToBoolean(_props.Get((Object)("Cyclic")));
 //BA.debugLineNum = 58;BA.debugLine="mRapid = Props.Get(\"Rapid\")";
_mrapid = BA.ObjectToBoolean(_props.Get((Object)("Rapid")));
 //BA.debugLineNum = 59;BA.debugLine="Formatter.GetDefaultFormat.FormatFont = MainLabel";
_formatter._getdefaultformat /*cloyd.smart.home.monitor.b4xformatter._b4xformatdata*/ ().FormatFont /*anywheresoftware.b4a.objects.B4XViewWrapper.B4XFont*/  = _mainlabel.getFont();
 //BA.debugLineNum = 60;BA.debugLine="Formatter.GetDefaultFormat.TextColor = MainLabel.";
_formatter._getdefaultformat /*cloyd.smart.home.monitor.b4xformatter._b4xformatdata*/ ().TextColor /*int*/  = _mainlabel.getTextColor();
 //BA.debugLineNum = 61;BA.debugLine="If Formation = \"Horizontal\" Then";
if ((_formation).equals("Horizontal")) { 
 //BA.debugLineNum = 62;BA.debugLine="lblPlus = CreateLabel(Chr(0xE315))";
_lblplus = _createlabel(BA.ObjectToString(__c.Chr((int) (0xe315))));
 //BA.debugLineNum = 63;BA.debugLine="lblMinus = CreateLabel(Chr(0xE314))";
_lblminus = _createlabel(BA.ObjectToString(__c.Chr((int) (0xe314))));
 }else {
 //BA.debugLineNum = 65;BA.debugLine="lblPlus = CreateLabel(Chr(0xE316))";
_lblplus = _createlabel(BA.ObjectToString(__c.Chr((int) (0xe316))));
 //BA.debugLineNum = 66;BA.debugLine="lblMinus = CreateLabel(Chr(0xE313))";
_lblminus = _createlabel(BA.ObjectToString(__c.Chr((int) (0xe313))));
 };
 //BA.debugLineNum = 68;BA.debugLine="lblMinus.SetTextAlignment(\"CENTER\", \"CENTER\")";
_lblminus.SetTextAlignment("CENTER","CENTER");
 //BA.debugLineNum = 69;BA.debugLine="lblPlus.SetTextAlignment(\"CENTER\", \"CENTER\")";
_lblplus.SetTextAlignment("CENTER","CENTER");
 //BA.debugLineNum = 70;BA.debugLine="MainLabel.SetTextAlignment(\"CENTER\", \"CENTER\")";
_mainlabel.SetTextAlignment("CENTER","CENTER");
 //BA.debugLineNum = 71;BA.debugLine="mBase.AddView(MainLabel, 0, 0, 0, 0)";
_mbase.AddView((android.view.View)(_mainlabel.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 72;BA.debugLine="mBase.AddView(pnlPlus, 0, 0, 0, 0)";
_mbase.AddView((android.view.View)(_pnlplus.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 73;BA.debugLine="mBase.AddView(pnlMinus, 0, 0, 0, 0)";
_mbase.AddView((android.view.View)(_pnlminus.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 74;BA.debugLine="pnlPlus.AddView(lblPlus, 0, 0, 0, 0)";
_pnlplus.AddView((android.view.View)(_lblplus.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 75;BA.debugLine="pnlMinus.AddView(lblMinus, 0, 0, 0, 0)";
_pnlminus.AddView((android.view.View)(_lblminus.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 76;BA.debugLine="SetIndex(-1, False)";
_setindex((int) (-1),__c.False);
 //BA.debugLineNum = 77;BA.debugLine="Base_Resize(mBase.Width, mBase.Height)";
_base_resize(_mbase.getWidth(),_mbase.getHeight());
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public Object  _getselectedvalue() throws Exception{
 //BA.debugLineNum = 114;BA.debugLine="Public Sub getSelectedValue As Object";
 //BA.debugLineNum = 115;BA.debugLine="Return GetValueImpl (mSelectedIndex)";
if (true) return _getvalueimpl(_mselectedindex);
 //BA.debugLineNum = 116;BA.debugLine="End Sub";
return null;
}
public Object  _getvalueimpl(int _index) throws Exception{
 //BA.debugLineNum = 127;BA.debugLine="Private Sub GetValueImpl (Index As Int) As Object";
 //BA.debugLineNum = 128;BA.debugLine="If StringMode Then";
if (_stringmode) { 
 //BA.debugLineNum = 129;BA.debugLine="Return mStringItems.Get(Index)";
if (true) return _mstringitems.Get(_index);
 }else {
 //BA.debugLineNum = 131;BA.debugLine="If Index = Size - 1 Then";
if (_index==_size-1) { 
 //BA.debugLineNum = 132;BA.debugLine="Return mEndRange";
if (true) return (Object)(_mendrange);
 }else {
 //BA.debugLineNum = 134;BA.debugLine="Return mStartRange + Index * mInterval";
if (true) return (Object)(_mstartrange+_index*_minterval);
 };
 };
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return null;
}
public String  _increment() throws Exception{
double _v1 = 0;
double _v2 = 0;
 //BA.debugLineNum = 159;BA.debugLine="Public Sub Increment";
 //BA.debugLineNum = 160;BA.debugLine="If mSelectedIndex = Size - 3 And StringMode = Fal";
if (_mselectedindex==_size-3 && _stringmode==__c.False) { 
 //BA.debugLineNum = 161;BA.debugLine="Dim v1 As Double = GetValueImpl(mSelectedIndex +";
_v1 = (double)(BA.ObjectToNumber(_getvalueimpl((int) (_mselectedindex+1))));
 //BA.debugLineNum = 162;BA.debugLine="Dim v2 As Double = GetValueImpl(mSelectedIndex +";
_v2 = (double)(BA.ObjectToNumber(_getvalueimpl((int) (_mselectedindex+2))));
 //BA.debugLineNum = 163;BA.debugLine="If Formatter.Format(v1) = Formatter.Format(v2) T";
if ((_formatter._format /*String*/ (_v1)).equals(_formatter._format /*String*/ (_v2))) { 
 //BA.debugLineNum = 164;BA.debugLine="mSelectedIndex = mSelectedIndex + 1";
_mselectedindex = (int) (_mselectedindex+1);
 };
 };
 //BA.debugLineNum = 167;BA.debugLine="If mSelectedIndex >= Size - 1 Then";
if (_mselectedindex>=_size-1) { 
 //BA.debugLineNum = 168;BA.debugLine="If mCyclic Then";
if (_mcyclic) { 
 //BA.debugLineNum = 169;BA.debugLine="mSelectedIndex = -1";
_mselectedindex = (int) (-1);
 }else {
 //BA.debugLineNum = 171;BA.debugLine="mSelectedIndex = Size - 1";
_mselectedindex = (int) (_size-1);
 //BA.debugLineNum = 172;BA.debugLine="LoopIndex = LoopIndex + 1";
_loopindex = (int) (_loopindex+1);
 //BA.debugLineNum = 173;BA.debugLine="Return";
if (true) return "";
 };
 };
 //BA.debugLineNum = 176;BA.debugLine="SetIndex(mSelectedIndex + 1, True)";
_setindex((int) (_mselectedindex+1),__c.True);
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba,Object _callback,String _eventname) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 29;BA.debugLine="Public Sub Initialize (Callback As Object, EventNa";
 //BA.debugLineNum = 30;BA.debugLine="mEventName = EventName";
_meventname = _eventname;
 //BA.debugLineNum = 31;BA.debugLine="mCallBack = Callback";
_mcallback = _callback;
 //BA.debugLineNum = 32;BA.debugLine="Formatter.Initialize";
_formatter._initialize /*String*/ (ba);
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
public String  _internalsettextorcsbuildertolabel(anywheresoftware.b4a.objects.B4XViewWrapper _xlbl,Object _text) throws Exception{
 //BA.debugLineNum = 253;BA.debugLine="Private Sub InternalSetTextOrCSBuilderToLabel(xlbl";
 //BA.debugLineNum = 255;BA.debugLine="xlbl.Text = Text";
_xlbl.setText(BA.ObjectToCharSequence(_text));
 //BA.debugLineNum = 265;BA.debugLine="End Sub";
return "";
}
public String  _pnlarrow_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 192;BA.debugLine="Private Sub pnlArrow_Touch (Action As Int, X As Fl";
 //BA.debugLineNum = 193;BA.debugLine="Touch(Action = lblPlus.TOUCH_ACTION_DOWN, Action";
_touch(_action==_lblplus.TOUCH_ACTION_DOWN,_action!=_lblplus.TOUCH_ACTION_MOVE,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(__c.Sender(ba))));
 //BA.debugLineNum = 194;BA.debugLine="End Sub";
return "";
}
public String  _setindex(int _i,boolean _raiseevent) throws Exception{
Object _value = null;
 //BA.debugLineNum = 139;BA.debugLine="Private Sub SetIndex(i As Int, RaiseEvent As Boole";
 //BA.debugLineNum = 140;BA.debugLine="If i >= Size Then i = -1";
if (_i>=_size) { 
_i = (int) (-1);};
 //BA.debugLineNum = 141;BA.debugLine="mSelectedIndex = i";
_mselectedindex = _i;
 //BA.debugLineNum = 142;BA.debugLine="If mSelectedIndex = -1 Then";
if (_mselectedindex==-1) { 
 //BA.debugLineNum = 143;BA.debugLine="MainLabel.Text = \"\"";
_mainlabel.setText(BA.ObjectToCharSequence(""));
 }else {
 //BA.debugLineNum = 145;BA.debugLine="Dim value As Object = GetValueImpl (mSelectedInd";
_value = _getvalueimpl(_mselectedindex);
 //BA.debugLineNum = 146;BA.debugLine="If StringMode Then";
if (_stringmode) { 
 //BA.debugLineNum = 147;BA.debugLine="InternalSetTextOrCSBuilderToLabel(MainLabel, va";
_internalsettextorcsbuildertolabel(_mainlabel,_value);
 }else {
 //BA.debugLineNum = 149;BA.debugLine="MainLabel.Text = Formatter.Format(value)";
_mainlabel.setText(BA.ObjectToCharSequence(_formatter._format /*String*/ ((double)(BA.ObjectToNumber(_value)))));
 };
 //BA.debugLineNum = 151;BA.debugLine="If RaiseEvent And xui.SubExists(mCallBack, mEven";
if (_raiseevent && _xui.SubExists(ba,_mcallback,_meventname+"_valuechanged",(int) (1))) { 
 //BA.debugLineNum = 152;BA.debugLine="CallSub2(mCallBack, mEventName & \"_valuechanged";
__c.CallSubNew2(ba,_mcallback,_meventname+"_valuechanged",_value);
 };
 };
 //BA.debugLineNum = 155;BA.debugLine="lblPlus.Enabled = Size > 0 And (mCyclic Or mSelec";
_lblplus.setEnabled(_size>0 && (_mcyclic || _mselectedindex<_size-1));
 //BA.debugLineNum = 156;BA.debugLine="lblMinus.Enabled = Size > 0 And (mCyclic Or mSele";
_lblminus.setEnabled(_size>0 && (_mcyclic || _mselectedindex>0));
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return "";
}
public String  _setnumericrange(double _startrange,double _endrange,double _interval) throws Exception{
 //BA.debugLineNum = 105;BA.debugLine="Public Sub SetNumericRange (StartRange As Double,";
 //BA.debugLineNum = 106;BA.debugLine="Size = Ceil((EndRange - StartRange) / Interval) +";
_size = (int) (__c.Ceil((_endrange-_startrange)/(double)_interval)+1);
 //BA.debugLineNum = 107;BA.debugLine="mStartRange = StartRange";
_mstartrange = _startrange;
 //BA.debugLineNum = 108;BA.debugLine="mEndRange = EndRange";
_mendrange = _endrange;
 //BA.debugLineNum = 109;BA.debugLine="mInterval = Interval";
_minterval = _interval;
 //BA.debugLineNum = 110;BA.debugLine="StringMode = False";
_stringmode = __c.False;
 //BA.debugLineNum = 111;BA.debugLine="SetIndex(0, False)";
_setindex((int) (0),__c.False);
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
return "";
}
public String  _setselectedvalue(Object _v) throws Exception{
double _n = 0;
 //BA.debugLineNum = 118;BA.debugLine="Public Sub setSelectedValue (v As Object)";
 //BA.debugLineNum = 119;BA.debugLine="If StringMode Then";
if (_stringmode) { 
 //BA.debugLineNum = 120;BA.debugLine="SetIndex(mStringItems.IndexOf(v), False)";
_setindex(_mstringitems.IndexOf(_v),__c.False);
 }else {
 //BA.debugLineNum = 122;BA.debugLine="Dim n As Double = v";
_n = (double)(BA.ObjectToNumber(_v));
 //BA.debugLineNum = 123;BA.debugLine="SetIndex(Min(Size - 1, Max(0, Round((n - mStartR";
_setindex((int) (__c.Min(_size-1,__c.Max(0,__c.Round((_n-_mstartrange)/(double)_minterval)))),__c.False);
 };
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
return "";
}
public String  _setstringitems(anywheresoftware.b4a.objects.collections.List _list) throws Exception{
 //BA.debugLineNum = 98;BA.debugLine="Public Sub SetStringItems (list As List)";
 //BA.debugLineNum = 99;BA.debugLine="mStringItems = list";
_mstringitems = _list;
 //BA.debugLineNum = 100;BA.debugLine="StringMode = True";
_stringmode = __c.True;
 //BA.debugLineNum = 101;BA.debugLine="Size = mStringItems.Size";
_size = _mstringitems.getSize();
 //BA.debugLineNum = 102;BA.debugLine="SetIndex(-1, False)";
_setindex((int) (-1),__c.False);
 //BA.debugLineNum = 103;BA.debugLine="End Sub";
return "";
}
public void  _startdownloop(boolean _up) throws Exception{
ResumableSub_StartDownLoop rsub = new ResumableSub_StartDownLoop(this,_up);
rsub.resume(ba, null);
}
public static class ResumableSub_StartDownLoop extends BA.ResumableSub {
public ResumableSub_StartDownLoop(cloyd.smart.home.monitor.b4xplusminus parent,boolean _up) {
this.parent = parent;
this._up = _up;
}
cloyd.smart.home.monitor.b4xplusminus parent;
boolean _up;
int _myindex = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 231;BA.debugLine="LoopIndex = LoopIndex + 1";
parent._loopindex = (int) (parent._loopindex+1);
 //BA.debugLineNum = 232;BA.debugLine="If mRapid = False Then Return";
if (true) break;

case 1:
//if
this.state = 6;
if (parent._mrapid==parent.__c.False) { 
this.state = 3;
;}if (true) break;

case 3:
//C
this.state = 6;
if (true) return ;
if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 233;BA.debugLine="Dim MyIndex As Int = LoopIndex";
_myindex = parent._loopindex;
 //BA.debugLineNum = 234;BA.debugLine="Sleep(RapidPeriod1)";
parent.__c.Sleep(ba,this,parent._rapidperiod1);
this.state = 19;
return;
case 19:
//C
this.state = 7;
;
 //BA.debugLineNum = 235;BA.debugLine="Do While MyIndex = LoopIndex";
if (true) break;

case 7:
//do while
this.state = 18;
while (_myindex==parent._loopindex) {
this.state = 9;
if (true) break;
}
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 236;BA.debugLine="If Up Then Increment Else Decrement";
if (true) break;

case 10:
//if
this.state = 17;
if (_up) { 
this.state = 12;
;}
else {
this.state = 14;
;}if (true) break;

case 12:
//C
this.state = 17;
parent._increment();
if (true) break;

case 14:
//C
this.state = 17;
parent._decrement();
if (true) break;

case 17:
//C
this.state = 7;
;
 //BA.debugLineNum = 237;BA.debugLine="Sleep(RapidPeriod2)";
parent.__c.Sleep(ba,this,parent._rapidperiod2);
this.state = 20;
return;
case 20:
//C
this.state = 7;
;
 if (true) break;

case 18:
//C
this.state = -1;
;
 //BA.debugLineNum = 239;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public String  _touch(boolean _start,boolean _stop,anywheresoftware.b4a.objects.B4XViewWrapper _pnl) throws Exception{
 //BA.debugLineNum = 196;BA.debugLine="Private Sub Touch (Start As Boolean, Stop As Boole";
 //BA.debugLineNum = 197;BA.debugLine="If pnl.GetView(0).Enabled = False Then Return";
if (_pnl.GetView((int) (0)).getEnabled()==__c.False) { 
if (true) return "";};
 //BA.debugLineNum = 198;BA.debugLine="If Start Then";
if (_start) { 
 //BA.debugLineNum = 199;BA.debugLine="If pnl.Tag = True Then";
if ((_pnl.getTag()).equals((Object)(__c.True))) { 
 //BA.debugLineNum = 200;BA.debugLine="Increment";
_increment();
 //BA.debugLineNum = 201;BA.debugLine="StartDownLoop(True)";
_startdownloop(__c.True);
 }else {
 //BA.debugLineNum = 203;BA.debugLine="Decrement";
_decrement();
 //BA.debugLineNum = 204;BA.debugLine="StartDownLoop(False)";
_startdownloop(__c.False);
 };
 }else if(_stop) { 
 //BA.debugLineNum = 207;BA.debugLine="LoopIndex = LoopIndex + 1";
_loopindex = (int) (_loopindex+1);
 };
 //BA.debugLineNum = 209;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
