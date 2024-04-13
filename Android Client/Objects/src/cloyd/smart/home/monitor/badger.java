package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class badger extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.badger");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.badger.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.collections.Map _views = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _stub = null;
public int _radius = 0;
public int _animationduration = 0;
public float _cx = 0f;
public float _cy = 0f;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public int _color = 0;
public int _textcolor = 0;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.chart _chart = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.b4xcollections _b4xcollections = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Private views As Map";
_views = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 4;BA.debugLine="Private stub As B4XView 'ignore this is required";
_stub = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 5;BA.debugLine="Private radius As Int = 10dip";
_radius = __c.DipToCurrent((int) (10));
 //BA.debugLineNum = 6;BA.debugLine="Private animationDuration As Int = 500";
_animationduration = (int) (500);
 //BA.debugLineNum = 7;BA.debugLine="Private cx, cy As Float";
_cx = 0f;
_cy = 0f;
 //BA.debugLineNum = 8;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 9;BA.debugLine="Private color As Int = xui.Color_Red";
_color = _xui.Color_Red;
 //BA.debugLineNum = 10;BA.debugLine="Private textColor As Int = xui.Color_White";
_textcolor = _xui.Color_White;
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public String  _createlabel(anywheresoftware.b4a.objects.B4XViewWrapper _p,int _count) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _xlbl = null;
int _duration = 0;
 //BA.debugLineNum = 87;BA.debugLine="Private Sub CreateLabel(p As B4XView, count As Int";
 //BA.debugLineNum = 88;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 89;BA.debugLine="lbl.Initialize(\"\")";
_lbl.Initialize(ba,"");
 //BA.debugLineNum = 90;BA.debugLine="Dim xlbl As B4XView = lbl";
_xlbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_xlbl.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 91;BA.debugLine="xlbl.Font = xui.CreateDefaultBoldFont(14)";
_xlbl.setFont(_xui.CreateDefaultBoldFont((float) (14)));
 //BA.debugLineNum = 92;BA.debugLine="xlbl.TextColor = textColor";
_xlbl.setTextColor(_textcolor);
 //BA.debugLineNum = 93;BA.debugLine="xlbl.Text = count";
_xlbl.setText(BA.ObjectToCharSequence(_count));
 //BA.debugLineNum = 94;BA.debugLine="p.AddView(xlbl, radius, radius, 0, 0)";
_p.AddView((android.view.View)(_xlbl.getObject()),_radius,_radius,(int) (0),(int) (0));
 //BA.debugLineNum = 95;BA.debugLine="xlbl.SetTextAlignment(\"CENTER\", \"CENTER\")";
_xlbl.SetTextAlignment("CENTER","CENTER");
 //BA.debugLineNum = 96;BA.debugLine="Dim duration As Int = animationDuration";
_duration = _animationduration;
 //BA.debugLineNum = 97;BA.debugLine="xlbl.SetLayoutAnimated(duration, 0, 0, radius * 2";
_xlbl.SetLayoutAnimated(_duration,(int) (0),(int) (0),(int) (_radius*2),(int) (_radius*2));
 //BA.debugLineNum = 98;BA.debugLine="xlbl.Visible = False";
_xlbl.setVisible(__c.False);
 //BA.debugLineNum = 99;BA.debugLine="xlbl.SetVisibleAnimated(animationDuration, True)";
_xlbl.SetVisibleAnimated(_animationduration,__c.True);
 //BA.debugLineNum = 100;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _createnewpanel(anywheresoftware.b4a.objects.B4XViewWrapper _view) throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _p = null;
anywheresoftware.b4a.objects.B4XViewWrapper _xp = null;
anywheresoftware.b4a.objects.B4XViewWrapper _parent = null;
 //BA.debugLineNum = 68;BA.debugLine="Private Sub CreateNewPanel(view As B4XView) As B4X";
 //BA.debugLineNum = 72;BA.debugLine="Dim p As Panel";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 74;BA.debugLine="p.Initialize(\"\")";
_p.Initialize(ba,"");
 //BA.debugLineNum = 76;BA.debugLine="p.SetElevationAnimated(animationDuration, 8dip)";
_p.SetElevationAnimated(_animationduration,(float) (__c.DipToCurrent((int) (8))));
 //BA.debugLineNum = 78;BA.debugLine="Dim xp As B4XView = p";
_xp = new anywheresoftware.b4a.objects.B4XViewWrapper();
_xp.setObject((java.lang.Object)(_p.getObject()));
 //BA.debugLineNum = 79;BA.debugLine="xp.SetColorAndBorder(color, 0, color, radius)";
_xp.SetColorAndBorder(_color,(int) (0),_color,_radius);
 //BA.debugLineNum = 80;BA.debugLine="cx = view.Left + view.Width - radius";
_cx = (float) (_view.getLeft()+_view.getWidth()-_radius);
 //BA.debugLineNum = 81;BA.debugLine="cy = view.Top + radius";
_cy = (float) (_view.getTop()+_radius);
 //BA.debugLineNum = 82;BA.debugLine="Dim parent As B4XView = view.Parent";
_parent = new anywheresoftware.b4a.objects.B4XViewWrapper();
_parent = _view.getParent();
 //BA.debugLineNum = 83;BA.debugLine="parent.AddView(xp, cx, cy, 0, 0)";
_parent.AddView((android.view.View)(_xp.getObject()),(int) (_cx),(int) (_cy),(int) (0),(int) (0));
 //BA.debugLineNum = 84;BA.debugLine="Return p";
if (true) return (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_p.getObject()));
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return null;
}
public int  _getbadge(anywheresoftware.b4a.objects.B4XViewWrapper _view) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
 //BA.debugLineNum = 51;BA.debugLine="Public Sub GetBadge(view As B4XView) As Int";
 //BA.debugLineNum = 52;BA.debugLine="If views.ContainsKey(view) Then";
if (_views.ContainsKey((Object)(_view.getObject()))) { 
 //BA.debugLineNum = 53;BA.debugLine="Dim lbl As B4XView = GetLabel(GetPanel(view))";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _getlabel(_getpanel((Object)(_view.getObject())));
 //BA.debugLineNum = 54;BA.debugLine="Return lbl.Text";
if (true) return (int)(Double.parseDouble(_lbl.getText()));
 }else {
 //BA.debugLineNum = 56;BA.debugLine="Return 0";
if (true) return (int) (0);
 };
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return 0;
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _getlabel(anywheresoftware.b4a.objects.B4XViewWrapper _panel) throws Exception{
 //BA.debugLineNum = 64;BA.debugLine="Private Sub GetLabel(panel As B4XView) As B4XView";
 //BA.debugLineNum = 65;BA.debugLine="Return panel.GetView(panel.NumberOfViews - 1)";
if (true) return _panel.GetView((int) (_panel.getNumberOfViews()-1));
 //BA.debugLineNum = 66;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _getpanel(Object _view) throws Exception{
 //BA.debugLineNum = 60;BA.debugLine="Private Sub GetPanel (view As Object) As B4XView";
 //BA.debugLineNum = 61;BA.debugLine="Return views.Get(view)";
if (true) return (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_views.Get(_view)));
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return null;
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 13;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 14;BA.debugLine="views.Initialize";
_views.Initialize();
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public void  _removebadge(anywheresoftware.b4a.objects.B4XViewWrapper _view) throws Exception{
ResumableSub_RemoveBadge rsub = new ResumableSub_RemoveBadge(this,_view);
rsub.resume(ba, null);
}
public static class ResumableSub_RemoveBadge extends BA.ResumableSub {
public ResumableSub_RemoveBadge(cloyd.smart.home.monitor.badger parent,anywheresoftware.b4a.objects.B4XViewWrapper _view) {
this.parent = parent;
this._view = _view;
}
cloyd.smart.home.monitor.badger parent;
anywheresoftware.b4a.objects.B4XViewWrapper _view;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 35;BA.debugLine="Dim p As B4XView = GetPanel(view)";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent._getpanel((Object)(_view.getObject()));
 //BA.debugLineNum = 36;BA.debugLine="GetLabel(p).RemoveViewFromParent";
parent._getlabel(_p).RemoveViewFromParent();
 //BA.debugLineNum = 37;BA.debugLine="views.Remove(view)";
parent._views.Remove((Object)(_view.getObject()));
 //BA.debugLineNum = 38;BA.debugLine="p.SetLayoutAnimated(animationDuration, cx, cy, 2d";
_p.SetLayoutAnimated(parent._animationduration,(int) (parent._cx),(int) (parent._cy),parent.__c.DipToCurrent((int) (2)),parent.__c.DipToCurrent((int) (2)));
 //BA.debugLineNum = 39;BA.debugLine="Sleep(animationDuration)";
parent.__c.Sleep(ba,this,parent._animationduration);
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 40;BA.debugLine="p.RemoveViewFromParent";
_p.RemoveViewFromParent();
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public void  _replacelabel(anywheresoftware.b4a.objects.B4XViewWrapper _view,int _badge) throws Exception{
ResumableSub_ReplaceLabel rsub = new ResumableSub_ReplaceLabel(this,_view,_badge);
rsub.resume(ba, null);
}
public static class ResumableSub_ReplaceLabel extends BA.ResumableSub {
public ResumableSub_ReplaceLabel(cloyd.smart.home.monitor.badger parent,anywheresoftware.b4a.objects.B4XViewWrapper _view,int _badge) {
this.parent = parent;
this._view = _view;
this._badge = _badge;
}
cloyd.smart.home.monitor.badger parent;
anywheresoftware.b4a.objects.B4XViewWrapper _view;
int _badge;
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 44;BA.debugLine="Dim lbl As B4XView = GetLabel(GetPanel(view))";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = parent._getlabel(parent._getpanel((Object)(_view.getObject())));
 //BA.debugLineNum = 45;BA.debugLine="lbl.SetLayoutAnimated(animationDuration / 2, radi";
_lbl.SetLayoutAnimated((int) (parent._animationduration/(double)2),(int) (parent._radius+parent.__c.DipToCurrent((int) (8))),(int) (0),(int) (parent._radius*2),(int) (parent._radius*2));
 //BA.debugLineNum = 46;BA.debugLine="CreateLabel(GetPanel(view), Badge)";
parent._createlabel(parent._getpanel((Object)(_view.getObject())),_badge);
 //BA.debugLineNum = 47;BA.debugLine="Sleep(animationDuration / 2)";
parent.__c.Sleep(ba,this,(int) (parent._animationduration/(double)2));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 48;BA.debugLine="lbl.RemoveViewFromParent";
_lbl.RemoveViewFromParent();
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public String  _setbadge(anywheresoftware.b4a.objects.B4XViewWrapper _view,int _badge) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
 //BA.debugLineNum = 17;BA.debugLine="Public Sub SetBadge(view As B4XView, Badge As Int)";
 //BA.debugLineNum = 18;BA.debugLine="If views.ContainsKey(view) Then";
if (_views.ContainsKey((Object)(_view.getObject()))) { 
 //BA.debugLineNum = 19;BA.debugLine="If Badge = 0 Then";
if (_badge==0) { 
 //BA.debugLineNum = 20;BA.debugLine="RemoveBadge(view)";
_removebadge(_view);
 }else {
 //BA.debugLineNum = 22;BA.debugLine="ReplaceLabel(view, Badge)";
_replacelabel(_view,_badge);
 };
 }else {
 //BA.debugLineNum = 25;BA.debugLine="If Badge > 0 Then";
if (_badge>0) { 
 //BA.debugLineNum = 26;BA.debugLine="Dim p As B4XView = CreateNewPanel(view)";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _createnewpanel(_view);
 //BA.debugLineNum = 27;BA.debugLine="CreateLabel(p, Badge)";
_createlabel(_p,_badge);
 //BA.debugLineNum = 28;BA.debugLine="p.SetLayoutAnimated(animationDuration, cx - rad";
_p.SetLayoutAnimated(_animationduration,(int) (_cx-_radius),(int) (_cy-_radius),(int) (_radius*2),(int) (_radius*2));
 //BA.debugLineNum = 29;BA.debugLine="views.Put(view, p)";
_views.Put((Object)(_view.getObject()),(Object)(_p.getObject()));
 };
 };
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
if (BA.fastSubCompare(sub, "GETPANEL"))
	return _getpanel((Object) args[0]);
return BA.SubDelegator.SubNotFound;
}
}
