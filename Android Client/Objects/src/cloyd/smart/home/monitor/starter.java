package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class starter extends  android.app.Service{
	public static class starter_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (starter) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, starter.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, true, BA.class);
		}

	}
    static starter mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return starter.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "cloyd.smart.home.monitor", "cloyd.smart.home.monitor.starter");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false);
		    }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "cloyd.smart.home.monitor.starter", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!true && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (starter) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (true) {
			ServiceHelper.StarterHelper.runWaitForLayouts();
		}
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		onStartCommand(intent, 0, 0);
    }
    @Override
    public int onStartCommand(final android.content.Intent intent, int flags, int startId) {
    	if (ServiceHelper.StarterHelper.onStartCommand(processBA, new Runnable() {
            public void run() {
                handleStart(intent);
            }}))
			;
		else {
			ServiceHelper.StarterHelper.addWaitForLayout (new Runnable() {
				public void run() {
                    processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (starter) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
                    ServiceHelper.StarterHelper.removeWaitForLayout();
				}
			});
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_NOT_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (true)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (starter) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = ServiceHelper.StarterHelper.handleStartIntent(intent, _service, processBA);
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        if (true) {
            BA.LogInfo("** Service (starter) Destroy (ignored)**");
        }
        else {
            BA.LogInfo("** Service (starter) Destroy **");
		    processBA.raiseEvent(null, "service_destroy");
            processBA.service = null;
		    mostCurrent = null;
		    processBA.setActivityPaused(true);
            processBA.runHook("ondestroy", this, null);
        }
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public static cloyd.smart.home.monitor.fileprovider _provider = null;
public static cloyd.smart.home.monitor.keyvaluestore _kvs = null;
public static int _flag_activity_clear_top = 0;
public static int _flag_activity_clear_task = 0;
public static int _flag_activity_new_task = 0;
public static int _flag_one_shot = 0;
public static int _alm_rtc = 0;
public static int _alm_rtc_wakeup = 0;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.chart _chart = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.b4xcollections _b4xcollections = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
public static boolean  _application_error(anywheresoftware.b4a.objects.B4AException _error,String _stacktrace) throws Exception{
 //BA.debugLineNum = 32;BA.debugLine="Sub Application_Error (Error As Exception, StackTr";
 //BA.debugLineNum = 33;BA.debugLine="ScheduleRestartCrashedActivity(DateTime.Now + 300";
_schedulerestartcrashedactivity((long) (anywheresoftware.b4a.keywords.Common.DateTime.getNow()+300),"Main",BA.ObjectToString(_error));
 //BA.debugLineNum = 34;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return false;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Public Provider As FileProvider";
_provider = new cloyd.smart.home.monitor.fileprovider();
 //BA.debugLineNum = 8;BA.debugLine="Public kvs As KeyValueStore";
_kvs = new cloyd.smart.home.monitor.keyvaluestore();
 //BA.debugLineNum = 9;BA.debugLine="Private const FLAG_ACTIVITY_CLEAR_TOP As Int = 0x";
_flag_activity_clear_top = (int) (0x04000000);
 //BA.debugLineNum = 10;BA.debugLine="Private const FLAG_ACTIVITY_CLEAR_TASK As Int = 0";
_flag_activity_clear_task = (int) (0x00008000);
 //BA.debugLineNum = 11;BA.debugLine="Private const FLAG_ACTIVITY_NEW_TASK As Int = 0x1";
_flag_activity_new_task = (int) (0x10000000);
 //BA.debugLineNum = 12;BA.debugLine="Private const FLAG_ONE_SHOT As Int = 0x40000000";
_flag_one_shot = (int) (0x40000000);
 //BA.debugLineNum = 14;BA.debugLine="Private const ALM_RTC As Int = 0x00000001";
_alm_rtc = (int) (0x00000001);
 //BA.debugLineNum = 15;BA.debugLine="Private const ALM_RTC_WAKEUP As Int = 0x00000000";
_alm_rtc_wakeup = (int) (0x00000000);
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
public static String  _schedulerestartcrashedactivity(long _time,String _activityname,String _message) throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _in = null;
anywheresoftware.b4j.object.JavaObject _jin = null;
anywheresoftware.b4j.object.JavaObject _ctxt = null;
anywheresoftware.b4j.object.JavaObject _am = null;
anywheresoftware.b4j.object.JavaObject _pi = null;
anywheresoftware.b4a.phone.Phone _p = null;
 //BA.debugLineNum = 45;BA.debugLine="Sub ScheduleRestartCrashedActivity (Time As Long,";
 //BA.debugLineNum = 47;BA.debugLine="Dim in As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 48;BA.debugLine="in.Initialize(\"\", \"\")";
_in.Initialize("","");
 //BA.debugLineNum = 49;BA.debugLine="in.SetComponent(Application.PackageName & \"/.\" &";
_in.SetComponent(anywheresoftware.b4a.keywords.Common.Application.getPackageName()+"/."+_activityname.toLowerCase());
 //BA.debugLineNum = 50;BA.debugLine="in.PutExtra(\"Crash\", message)";
_in.PutExtra("Crash",(Object)(_message));
 //BA.debugLineNum = 51;BA.debugLine="in.Flags = Bit.Or(FLAG_ACTIVITY_CLEAR_TASK, Bit.O";
_in.setFlags(anywheresoftware.b4a.keywords.Common.Bit.Or(_flag_activity_clear_task,anywheresoftware.b4a.keywords.Common.Bit.Or(_flag_activity_clear_top,_flag_activity_new_task)));
 //BA.debugLineNum = 53;BA.debugLine="Dim jin As JavaObject = in";
_jin = new anywheresoftware.b4j.object.JavaObject();
_jin.setObject((java.lang.Object)(_in.getObject()));
 //BA.debugLineNum = 54;BA.debugLine="jin.RunMethod(\"setAction\", Array(Null))";
_jin.RunMethod("setAction",new Object[]{anywheresoftware.b4a.keywords.Common.Null});
 //BA.debugLineNum = 56;BA.debugLine="Dim ctxt As JavaObject";
_ctxt = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 57;BA.debugLine="ctxt.InitializeContext";
_ctxt.InitializeContext(processBA);
 //BA.debugLineNum = 58;BA.debugLine="Dim am As JavaObject = ctxt.RunMethod(\"getSystemS";
_am = new anywheresoftware.b4j.object.JavaObject();
_am.setObject((java.lang.Object)(_ctxt.RunMethod("getSystemService",new Object[]{(Object)("alarm")})));
 //BA.debugLineNum = 59;BA.debugLine="Dim pi As JavaObject";
_pi = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 60;BA.debugLine="pi = pi.InitializeStatic(\"android.app.PendingInte";
_pi.setObject((java.lang.Object)(_pi.InitializeStatic("android.app.PendingIntent").RunMethod("getActivity",new Object[]{(Object)(_ctxt.getObject()),(Object)(0),(Object)(_in.getObject()),(Object)(_flag_one_shot)})));
 //BA.debugLineNum = 62;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 63;BA.debugLine="If p.SdkVersion < 20 Then";
if (_p.getSdkVersion()<20) { 
 //BA.debugLineNum = 64;BA.debugLine="am.RunMethod(\"set\", Array(ALM_RTC, Time, pi))";
_am.RunMethod("set",new Object[]{(Object)(_alm_rtc),(Object)(_time),(Object)(_pi.getObject())});
 }else if(_p.getSdkVersion()<23) { 
 //BA.debugLineNum = 66;BA.debugLine="am.RunMethod(\"setExact\", Array(ALM_RTC, Time, pi";
_am.RunMethod("setExact",new Object[]{(Object)(_alm_rtc),(Object)(_time),(Object)(_pi.getObject())});
 }else {
 //BA.debugLineNum = 68;BA.debugLine="am.RunMethod(\"setExactAndAllowWhileIdle\", Array(";
_am.RunMethod("setExactAndAllowWhileIdle",new Object[]{(Object)(_alm_rtc),(Object)(_time),(Object)(_pi.getObject())});
 };
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 19;BA.debugLine="Provider.Initialize";
_provider._initialize /*String*/ (processBA);
 //BA.debugLineNum = 20;BA.debugLine="kvs.Initialize(File.DirInternal, \"datastore\")";
_kvs._initialize /*String*/ (processBA,anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"datastore");
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _service_taskremoved() throws Exception{
 //BA.debugLineNum = 27;BA.debugLine="Sub Service_TaskRemoved";
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
}
