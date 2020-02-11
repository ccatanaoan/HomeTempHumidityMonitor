package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class cmediadata extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "cloyd.smart.home.monitor.cmediadata");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", cloyd.smart.home.monitor.cmediadata.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public int _metadata_key_duration = 0;
public String _setnotcallederror = "";
public String _fd_path = "";
public String _fd_file = "";
public String _fd_combined = "";
public boolean _reflectorisvalid = false;
public anywheresoftware.b4a.agraham.reflection.Reflection _reflectedfile = null;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 13;BA.debugLine="Private Const METADATA_KEY_DURATION         As";
_metadata_key_duration = (int) (9);
 //BA.debugLineNum = 27;BA.debugLine="Private Const SetNotCalledError             As";
_setnotcallederror = "ProcessMediaFile has not been called";
 //BA.debugLineNum = 29;BA.debugLine="Private       FD_Path                       As";
_fd_path = "";
 //BA.debugLineNum = 30;BA.debugLine="Private       FD_File                       As";
_fd_file = "";
 //BA.debugLineNum = 32;BA.debugLine="Private       FD_Combined                   As";
_fd_combined = "";
 //BA.debugLineNum = 34;BA.debugLine="Private       ReflectorIsValid              As";
_reflectorisvalid = false;
 //BA.debugLineNum = 35;BA.debugLine="Private       ReflectedFile                 As";
_reflectedfile = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 36;BA.debugLine="End Sub";
return "";
}
public String  _getduration() throws Exception{
 //BA.debugLineNum = 87;BA.debugLine="Public  Sub GetDuration As String";
 //BA.debugLineNum = 88;BA.debugLine="Return GetMetaData(\"GetDuration\", METADATA_KEY_DU";
if (true) return _getmetadata("GetDuration",_metadata_key_duration);
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.drawable.BitmapDrawable  _getembeddedpicture() throws Exception{
byte[] _returndata = null;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _instream = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _coverart = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _coverartdrawable = null;
 //BA.debugLineNum = 121;BA.debugLine="Public  Sub GetEmbeddedPicture As BitmapDrawable";
 //BA.debugLineNum = 123;BA.debugLine="If ReflectorIsValid = False Then Return Null";
if (_reflectorisvalid==__c.False) { 
if (true) return (anywheresoftware.b4a.objects.drawable.BitmapDrawable) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.BitmapDrawable(), (android.graphics.drawable.BitmapDrawable)(__c.Null));};
 //BA.debugLineNum = 125;BA.debugLine="Try";
try { //BA.debugLineNum = 126;BA.debugLine="Dim ReturnData() As Byte = ReflectedFil";
_returndata = (byte[])(_reflectedfile.RunMethod("getEmbeddedPicture"));
 //BA.debugLineNum = 128;BA.debugLine="If ReturnData <> Null And ReturnData.Length <";
if (_returndata!= null && _returndata.length!=0) { 
 //BA.debugLineNum = 129;BA.debugLine="Dim InStream          As InputStream";
_instream = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 130;BA.debugLine="Dim CoverArt          As Bitmap";
_coverart = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 132;BA.debugLine="Dim CoverArtDrawable  As BitmapDrawable";
_coverartdrawable = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 134;BA.debugLine="InStream.InitializeFromBytesArray(Re";
_instream.InitializeFromBytesArray(_returndata,(int) (0),_returndata.length);
 //BA.debugLineNum = 135;BA.debugLine="CoverArt.Initialize2(InStream)";
_coverart.Initialize2((java.io.InputStream)(_instream.getObject()));
 //BA.debugLineNum = 136;BA.debugLine="CoverArtDrawable.Initialize(CoverArt)";
_coverartdrawable.Initialize((android.graphics.Bitmap)(_coverart.getObject()));
 //BA.debugLineNum = 138;BA.debugLine="Return CoverArtDrawable";
if (true) return _coverartdrawable;
 };
 //BA.debugLineNum = 141;BA.debugLine="Return Null";
if (true) return (anywheresoftware.b4a.objects.drawable.BitmapDrawable) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.BitmapDrawable(), (android.graphics.drawable.BitmapDrawable)(__c.Null));
 } 
       catch (Exception e15) {
			ba.setLastException(e15); //BA.debugLineNum = 143;BA.debugLine="Return Null";
if (true) return (anywheresoftware.b4a.objects.drawable.BitmapDrawable) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.BitmapDrawable(), (android.graphics.drawable.BitmapDrawable)(__c.Null));
 };
 //BA.debugLineNum = 145;BA.debugLine="End Sub";
return null;
}
public String  _getmetadata(String _functioncalled,int _fieldnumber) throws Exception{
String _returndata = "";
 //BA.debugLineNum = 103;BA.debugLine="Private Sub GetMetaData(FunctionCalled As String,";
 //BA.debugLineNum = 105;BA.debugLine="If ReflectorIsValid = False Then Return Se";
if (_reflectorisvalid==__c.False) { 
if (true) return _setnotcallederror;};
 //BA.debugLineNum = 107;BA.debugLine="Try";
try { //BA.debugLineNum = 108;BA.debugLine="Dim ReturnData As String = ReflectedFil";
_returndata = BA.ObjectToString(_reflectedfile.RunMethod2("extractMetadata",BA.NumberToString(_fieldnumber),"java.lang.int"));
 //BA.debugLineNum = 113;BA.debugLine="If ReturnData = \"null\"  Then Return \"\"";
if ((_returndata).equals("null")) { 
if (true) return "";};
 //BA.debugLineNum = 115;BA.debugLine="Return ReturnData";
if (true) return _returndata;
 } 
       catch (Exception e7) {
			ba.setLastException(e7); //BA.debugLineNum = 117;BA.debugLine="Return \"Error on \"&FunctionCalled";
if (true) return "Error on "+_functioncalled;
 };
 //BA.debugLineNum = 119;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 39;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 41;BA.debugLine="FD_Path          = \"\"";
_fd_path = "";
 //BA.debugLineNum = 42;BA.debugLine="FD_File          = \"\"";
_fd_file = "";
 //BA.debugLineNum = 43;BA.debugLine="FD_Combined      = \"\"";
_fd_combined = "";
 //BA.debugLineNum = 45;BA.debugLine="ReflectorIsValid = False";
_reflectorisvalid = __c.False;
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public boolean  _processmediafile(String _filepath,String _filename) throws Exception{
Object _obj = null;
 //BA.debugLineNum = 49;BA.debugLine="Public Sub ProcessMediaFile(FilePath As String, Fi";
 //BA.debugLineNum = 51;BA.debugLine="If File.Exists(FilePath, FileName) = False The";
if (__c.File.Exists(_filepath,_filename)==__c.False) { 
if (true) return __c.False;};
 //BA.debugLineNum = 53;BA.debugLine="FD_Path     = FilePath";
_fd_path = _filepath;
 //BA.debugLineNum = 54;BA.debugLine="FD_File     = FileName";
_fd_file = _filename;
 //BA.debugLineNum = 55;BA.debugLine="FD_Combined = File.Combine(FD_Path, FD_File)";
_fd_combined = __c.File.Combine(_fd_path,_fd_file);
 //BA.debugLineNum = 57;BA.debugLine="Try";
try { //BA.debugLineNum = 58;BA.debugLine="Dim Obj As Object = ReflectedFile.CreateOb";
_obj = _reflectedfile.CreateObject("android.media.MediaMetadataRetriever");
 //BA.debugLineNum = 60;BA.debugLine="ReflectedFile.Target = Obj";
_reflectedfile.Target = _obj;
 //BA.debugLineNum = 62;BA.debugLine="Try";
try { //BA.debugLineNum = 63;BA.debugLine="ReflectedFile.RunMethod2(\"setDataSourc";
_reflectedfile.RunMethod2("setDataSource",_fd_combined,"java.lang.String");
 //BA.debugLineNum = 64;BA.debugLine="ReflectorIsValid = True";
_reflectorisvalid = __c.True;
 //BA.debugLineNum = 66;BA.debugLine="Return True";
if (true) return __c.True;
 } 
       catch (Exception e13) {
			ba.setLastException(e13); //BA.debugLineNum = 68;BA.debugLine="Msgbox(\"File cannot be read. Is it a valid Medi";
__c.Msgbox(BA.ObjectToCharSequence("File cannot be read. Is it a valid Media File?"),BA.ObjectToCharSequence("Error"),getActivityBA());
 //BA.debugLineNum = 69;BA.debugLine="Return False";
if (true) return __c.False;
 };
 } 
       catch (Exception e17) {
			ba.setLastException(e17); //BA.debugLineNum = 72;BA.debugLine="Msgbox(\"Unknown Error on Meddia File?\", \"Error\")";
__c.Msgbox(BA.ObjectToCharSequence("Unknown Error on Meddia File?"),BA.ObjectToCharSequence("Error"),getActivityBA());
 //BA.debugLineNum = 73;BA.debugLine="Return False";
if (true) return __c.False;
 };
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return false;
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
