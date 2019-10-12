package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class fileprovider extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.fileprovider");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.fileprovider.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _sharedfolder = "";
public boolean _usefileprovider = false;
public anywheresoftware.b4a.objects.RuntimePermissions _rp = null;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Public SharedFolder As String";
_sharedfolder = "";
 //BA.debugLineNum = 4;BA.debugLine="Public UseFileProvider As Boolean";
_usefileprovider = false;
 //BA.debugLineNum = 5;BA.debugLine="Private rp As RuntimePermissions";
_rp = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 6;BA.debugLine="End Sub";
return "";
}
public Object  _getfileuri(String _filename) throws Exception{
anywheresoftware.b4j.object.JavaObject _uri = null;
anywheresoftware.b4j.object.JavaObject _f = null;
anywheresoftware.b4j.object.JavaObject _fp = null;
anywheresoftware.b4j.object.JavaObject _context = null;
 //BA.debugLineNum = 22;BA.debugLine="Public Sub GetFileUri (FileName As String) As Obje";
 //BA.debugLineNum = 24;BA.debugLine="If UseFileProvider = False Then";
if (_usefileprovider==__c.False) { 
 //BA.debugLineNum = 25;BA.debugLine="Dim uri As JavaObject";
_uri = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 26;BA.debugLine="Return uri.InitializeStatic(\"android.net.Uri\").R";
if (true) return _uri.InitializeStatic("android.net.Uri").RunMethod("parse",new Object[]{(Object)("file://"+__c.File.Combine(_sharedfolder,_filename))});
 }else {
 //BA.debugLineNum = 28;BA.debugLine="Dim f As JavaObject";
_f = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 29;BA.debugLine="f.InitializeNewInstance(\"java.io.File\", Array(Sh";
_f.InitializeNewInstance("java.io.File",new Object[]{(Object)(_sharedfolder),(Object)(_filename)});
 //BA.debugLineNum = 30;BA.debugLine="Dim fp As JavaObject";
_fp = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 31;BA.debugLine="Dim context As JavaObject";
_context = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 32;BA.debugLine="context.InitializeContext";
_context.InitializeContext(ba);
 //BA.debugLineNum = 33;BA.debugLine="fp.InitializeStatic(\"android.support.v4.content.";
_fp.InitializeStatic("androidx.core.content.FileProvider");
 //BA.debugLineNum = 34;BA.debugLine="Return fp.RunMethod(\"getUriForFile\", Array(conte";
if (true) return _fp.RunMethod("getUriForFile",new Object[]{(Object)(_context.getObject()),(Object)(__c.Application.getPackageName()+".provider"),(Object)(_f.getObject())});
 };
 //BA.debugLineNum = 36;BA.debugLine="End Sub";
return null;
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
anywheresoftware.b4a.phone.Phone _p = null;
 //BA.debugLineNum = 8;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 9;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 10;BA.debugLine="If p.SdkVersion >= 24 Or File.ExternalWritable =";
if (_p.getSdkVersion()>=24 || __c.File.getExternalWritable()==__c.False) { 
 //BA.debugLineNum = 11;BA.debugLine="UseFileProvider = True";
_usefileprovider = __c.True;
 //BA.debugLineNum = 12;BA.debugLine="SharedFolder = File.Combine(File.DirInternal, \"s";
_sharedfolder = __c.File.Combine(__c.File.getDirInternal(),"shared");
 //BA.debugLineNum = 13;BA.debugLine="File.MakeDir(\"\", SharedFolder)";
__c.File.MakeDir("",_sharedfolder);
 }else {
 //BA.debugLineNum = 15;BA.debugLine="UseFileProvider = False";
_usefileprovider = __c.False;
 //BA.debugLineNum = 16;BA.debugLine="SharedFolder = rp.GetSafeDirDefaultExternal(\"sha";
_sharedfolder = _rp.GetSafeDirDefaultExternal("shared");
 };
 //BA.debugLineNum = 18;BA.debugLine="Log($\"Using FileProvider? ${UseFileProvider}\"$)";
__c.LogImpl("810354698",("Using FileProvider? "+__c.SmartStringFormatter("",(Object)(_usefileprovider))+""),0);
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public String  _setfileuriasintentdata(anywheresoftware.b4a.objects.IntentWrapper _intent,String _filename) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 40;BA.debugLine="Public Sub SetFileUriAsIntentData (Intent As Inten";
 //BA.debugLineNum = 41;BA.debugLine="Dim jo As JavaObject = Intent";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_intent.getObject()));
 //BA.debugLineNum = 42;BA.debugLine="jo.RunMethod(\"setData\", Array(GetFileUri(FileName";
_jo.RunMethod("setData",new Object[]{_getfileuri(_filename)});
 //BA.debugLineNum = 43;BA.debugLine="Intent.Flags = Bit.Or(Intent.Flags, 1) 'FLAG_GRAN";
_intent.setFlags(__c.Bit.Or(_intent.getFlags(),(int) (1)));
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
