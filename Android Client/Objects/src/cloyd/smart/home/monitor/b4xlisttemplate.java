package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xlisttemplate extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.b4xlisttemplate");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.b4xlisttemplate.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mbase = null;
public b4a.example3.customlistview _customlistview1 = null;
public anywheresoftware.b4a.objects.collections.List _options = null;
public String _selecteditem = "";
public cloyd.smart.home.monitor.b4xdialog _xdialog = null;
public boolean _allowmultiselection = false;
public int _selectioncolor = 0;
public anywheresoftware.b4a.objects.collections.List _selecteditems = null;
public int _multiselectionminimum = 0;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 1;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 2;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 3;BA.debugLine="Public mBase As B4XView";
_mbase = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 4;BA.debugLine="Public CustomListView1 As CustomListView";
_customlistview1 = new b4a.example3.customlistview();
 //BA.debugLineNum = 5;BA.debugLine="Public Options As List";
_options = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 6;BA.debugLine="Public SelectedItem As String";
_selecteditem = "";
 //BA.debugLineNum = 7;BA.debugLine="Private xDialog As B4XDialog";
_xdialog = new cloyd.smart.home.monitor.b4xdialog();
 //BA.debugLineNum = 8;BA.debugLine="Public AllowMultiSelection As Boolean";
_allowmultiselection = false;
 //BA.debugLineNum = 9;BA.debugLine="Public SelectionColor As Int = 0xAA0086FF";
_selectioncolor = (int) (0xaa0086ff);
 //BA.debugLineNum = 10;BA.debugLine="Public SelectedItems As List";
_selecteditems = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 11;BA.debugLine="Public MultiSelectionMinimum As Int = 0";
_multiselectionminimum = (int) (0);
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public String  _customlistview1_itemclick(int _index,Object _value) throws Exception{
b4a.example3.customlistview._clvitem _item = null;
 //BA.debugLineNum = 76;BA.debugLine="Private Sub CustomListView1_ItemClick (Index As In";
 //BA.debugLineNum = 77;BA.debugLine="If Value = \"\" Then Return";
if ((_value).equals((Object)(""))) { 
if (true) return "";};
 //BA.debugLineNum = 78;BA.debugLine="If AllowMultiSelection Then";
if (_allowmultiselection) { 
 //BA.debugLineNum = 79;BA.debugLine="Dim Item As CLVItem = CustomListView1.GetRawList";
_item = _customlistview1._getrawlistitem(_index);
 //BA.debugLineNum = 80;BA.debugLine="SelectItem (Item, True)";
_selectitem(_item,__c.True);
 }else {
 //BA.debugLineNum = 82;BA.debugLine="SelectedItem = Value";
_selecteditem = BA.ObjectToString(_value);
 //BA.debugLineNum = 83;BA.debugLine="SelectedItems.Clear";
_selecteditems.Clear();
 //BA.debugLineNum = 84;BA.debugLine="SelectedItems.Add(Value)";
_selecteditems.Add(_value);
 //BA.debugLineNum = 85;BA.debugLine="xDialog.Close(xui.DialogResponse_Positive)";
_xdialog._close /*boolean*/ (_xui.DialogResponse_Positive);
 };
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
public String  _dialogclosed(int _result) throws Exception{
 //BA.debugLineNum = 89;BA.debugLine="Private Sub DialogClosed(Result As Int) 'ignore";
 //BA.debugLineNum = 91;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _getpanel(cloyd.smart.home.monitor.b4xdialog _dialog) throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Public Sub GetPanel (Dialog As B4XDialog) As B4XVi";
 //BA.debugLineNum = 38;BA.debugLine="Return mBase";
if (true) return _mbase;
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return null;
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 14;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 15;BA.debugLine="mBase = xui.CreatePanel(\"mBase\")";
_mbase = _xui.CreatePanel(ba,"mBase");
 //BA.debugLineNum = 16;BA.debugLine="mBase.SetLayoutAnimated(0, 0, 0, 300dip, 300dip)";
_mbase.SetLayoutAnimated((int) (0),(int) (0),(int) (0),__c.DipToCurrent((int) (300)),__c.DipToCurrent((int) (300)));
 //BA.debugLineNum = 17;BA.debugLine="mBase.LoadLayout(\"ListTemplate\")";
_mbase.LoadLayout("ListTemplate",ba);
 //BA.debugLineNum = 18;BA.debugLine="mBase.SetColorAndBorder(xui.Color_Transparent, 0,";
_mbase.SetColorAndBorder(_xui.Color_Transparent,(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 19;BA.debugLine="CustomListView1.sv.SetColorAndBorder(xui.Color_Tr";
_customlistview1._sv.SetColorAndBorder(_xui.Color_Transparent,(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 20;BA.debugLine="CustomListView1.DefaultTextBackgroundColor = 0xFF";
_customlistview1._defaulttextbackgroundcolor = (int) (0xff555555);
 //BA.debugLineNum = 21;BA.debugLine="CustomListView1.DefaultTextColor = xui.Color_Whit";
_customlistview1._defaulttextcolor = _xui.Color_White;
 //BA.debugLineNum = 26;BA.debugLine="Options.Initialize";
_options.Initialize();
 //BA.debugLineNum = 27;BA.debugLine="SelectedItems.Initialize";
_selecteditems.Initialize();
 //BA.debugLineNum = 28;BA.debugLine="End Sub";
return "";
}
public String  _resize(int _width,int _height) throws Exception{
 //BA.debugLineNum = 31;BA.debugLine="Public Sub Resize(Width As Int, Height As Int)";
 //BA.debugLineNum = 32;BA.debugLine="mBase.SetLayoutAnimated(0, 0, 0, Width, Height)";
_mbase.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 33;BA.debugLine="CustomListView1.Base_Resize(Width, Height)";
_customlistview1._base_resize(_width,_height);
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}
public String  _selectitem(b4a.example3.customlistview._clvitem _item,boolean _toggle) throws Exception{
int _index = 0;
boolean _selected = false;
 //BA.debugLineNum = 59;BA.debugLine="Private Sub SelectItem (Item As CLVItem, Toggle As";
 //BA.debugLineNum = 60;BA.debugLine="Dim index As Int = SelectedItems.IndexOf(Item.Val";
_index = _selecteditems.IndexOf(_item.Value);
 //BA.debugLineNum = 61;BA.debugLine="Dim Selected As Boolean = index > -1";
_selected = _index>-1;
 //BA.debugLineNum = 62;BA.debugLine="If Toggle Then Selected = Not(Selected)";
if (_toggle) { 
_selected = __c.Not(_selected);};
 //BA.debugLineNum = 63;BA.debugLine="If Selected Then";
if (_selected) { 
 //BA.debugLineNum = 64;BA.debugLine="If Not(Toggle) Then Item.Panel.Color = Selection";
if (__c.Not(_toggle)) { 
_item.Panel.setColor(_selectioncolor);};
 //BA.debugLineNum = 65;BA.debugLine="Item.Color = CustomListView1.DefaultTextBackgrou";
_item.Color = _customlistview1._defaulttextbackgroundcolor;
 //BA.debugLineNum = 66;BA.debugLine="If index = -1 Then SelectedItems.Add(Item.Value)";
if (_index==-1) { 
_selecteditems.Add(_item.Value);};
 }else {
 //BA.debugLineNum = 68;BA.debugLine="Item.Color = SelectionColor";
_item.Color = _selectioncolor;
 //BA.debugLineNum = 69;BA.debugLine="If Not(Toggle) Then Item.Panel.Color = CustomLis";
if (__c.Not(_toggle)) { 
_item.Panel.setColor(_customlistview1._defaulttextbackgroundcolor);};
 //BA.debugLineNum = 70;BA.debugLine="If index > -1 Then SelectedItems.RemoveAt(index)";
if (_index>-1) { 
_selecteditems.RemoveAt(_index);};
 };
 //BA.debugLineNum = 72;BA.debugLine="xDialog.SetButtonState(xui.DialogResponse_Positiv";
_xdialog._setbuttonstate /*String*/ (_xui.DialogResponse_Positive,_selecteditems.getSize()>=_multiselectionminimum);
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return "";
}
public void  _show(cloyd.smart.home.monitor.b4xdialog _dialog) throws Exception{
ResumableSub_Show rsub = new ResumableSub_Show(this,_dialog);
rsub.resume(ba, null);
}
public static class ResumableSub_Show extends BA.ResumableSub {
public ResumableSub_Show(cloyd.smart.home.monitor.b4xlisttemplate parent,cloyd.smart.home.monitor.b4xdialog _dialog) {
this.parent = parent;
this._dialog = _dialog;
}
cloyd.smart.home.monitor.b4xlisttemplate parent;
cloyd.smart.home.monitor.b4xdialog _dialog;
Object _opt = null;
int _i = 0;
b4a.example3.customlistview._clvitem _item = null;
anywheresoftware.b4a.BA.IterableList group3;
int index3;
int groupLen3;
int step8;
int limit8;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 42;BA.debugLine="xDialog = Dialog";
parent._xdialog = _dialog;
 //BA.debugLineNum = 43;BA.debugLine="CustomListView1.Clear";
parent._customlistview1._clear();
 //BA.debugLineNum = 44;BA.debugLine="For Each opt As Object In Options";
if (true) break;

case 1:
//for
this.state = 4;
group3 = parent._options;
index3 = 0;
groupLen3 = group3.getSize();
this.state = 12;
if (true) break;

case 12:
//C
this.state = 4;
if (index3 < groupLen3) {
this.state = 3;
_opt = group3.Get(index3);}
if (true) break;

case 13:
//C
this.state = 12;
index3++;
if (true) break;

case 3:
//C
this.state = 13;
 //BA.debugLineNum = 45;BA.debugLine="CustomListView1.AddTextItem(opt, opt)";
parent._customlistview1._addtextitem(_opt,_opt);
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 47;BA.debugLine="If AllowMultiSelection Then";

case 4:
//if
this.state = 11;
if (parent._allowmultiselection) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 48;BA.debugLine="Sleep(20)";
parent.__c.Sleep(ba,this,(int) (20));
this.state = 14;
return;
case 14:
//C
this.state = 7;
;
 //BA.debugLineNum = 49;BA.debugLine="For i = 0 To CustomListView1.Size - 1";
if (true) break;

case 7:
//for
this.state = 10;
step8 = 1;
limit8 = (int) (parent._customlistview1._getsize()-1);
_i = (int) (0) ;
this.state = 15;
if (true) break;

case 15:
//C
this.state = 10;
if ((step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8)) this.state = 9;
if (true) break;

case 16:
//C
this.state = 15;
_i = ((int)(0 + _i + step8)) ;
if (true) break;

case 9:
//C
this.state = 16;
 //BA.debugLineNum = 50;BA.debugLine="Dim item As CLVItem = CustomListView1.GetRawLis";
_item = parent._customlistview1._getrawlistitem(_i);
 //BA.debugLineNum = 51;BA.debugLine="SelectItem (item, False)";
parent._selectitem(_item,parent.__c.False);
 if (true) break;
if (true) break;

case 10:
//C
this.state = 11;
;
 if (true) break;

case 11:
//C
this.state = -1;
;
 //BA.debugLineNum = 54;BA.debugLine="Dialog.InternalAddStubToCLVIfNeeded(CustomListVie";
_dialog._internaladdstubtoclvifneeded /*String*/ (parent._customlistview1,parent._customlistview1._defaulttextbackgroundcolor);
 //BA.debugLineNum = 55;BA.debugLine="SelectedItem = \"\"";
parent._selecteditem = "";
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
if (BA.fastSubCompare(sub, "DIALOGCLOSED"))
	return _dialogclosed(((Number)args[0]).intValue());
if (BA.fastSubCompare(sub, "GETPANEL"))
	return _getpanel((cloyd.smart.home.monitor.b4xdialog) args[0]);
return BA.SubDelegator.SubNotFound;
}
}
