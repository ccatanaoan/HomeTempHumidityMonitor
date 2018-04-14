package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class notificationservice extends  android.app.Service{
	public static class notificationservice_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (notificationservice) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, notificationservice.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, false, BA.class);
		}

	}
    static notificationservice mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return notificationservice.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "cloyd.smart.home.monitor", "cloyd.smart.home.monitor.notificationservice");
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
			processBA.raiseEvent2(null, true, "CREATE", true, "cloyd.smart.home.monitor.notificationservice", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!false && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, true) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (notificationservice) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (false) {
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
                    BA.LogInfo("** Service (notificationservice) Create **");
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
        if (false)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (notificationservice) Start **");
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
        BA.LogInfo("** Service (notificationservice) Destroy **");
		processBA.raiseEvent(null, "service_destroy");
        processBA.service = null;
		mostCurrent = null;
		processBA.setActivityPaused(true);
        processBA.runHook("ondestroy", this, null);
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.NotificationListenerWrapper.NotificationListener _listener = null;
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public static String  _clearall() throws Exception{
 //BA.debugLineNum = 51;BA.debugLine="Sub ClearAll";
 //BA.debugLineNum = 52;BA.debugLine="listener.ClearAll";
_listener.ClearAll();
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public static String  _getactive() throws Exception{
 //BA.debugLineNum = 55;BA.debugLine="Sub GetActive";
 //BA.debugLineNum = 56;BA.debugLine="listener.GetActiveNotifications";
_listener.GetActiveNotifications();
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
public static String  _listener_notificationposted(anywheresoftware.b4a.objects.NotificationListenerWrapper.StatusBarNotificationWrapper _sbn) throws Exception{
anywheresoftware.b4a.phone.Phone _p = null;
anywheresoftware.b4j.object.JavaObject _jno = null;
anywheresoftware.b4j.object.JavaObject _extras = null;
 //BA.debugLineNum = 18;BA.debugLine="Sub Listener_NotificationPosted (SBN As StatusBarN";
 //BA.debugLineNum = 21;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 22;BA.debugLine="If p.SdkVersion >= 19 Then";
if (_p.getSdkVersion()>=19) { 
 //BA.debugLineNum = 23;BA.debugLine="Dim jno As JavaObject = SBN.Notification";
_jno = new anywheresoftware.b4j.object.JavaObject();
_jno.setObject((java.lang.Object)(_sbn.getNotification().getObject()));
 //BA.debugLineNum = 24;BA.debugLine="Dim extras As JavaObject = jno.GetField(\"extras\"";
_extras = new anywheresoftware.b4j.object.JavaObject();
_extras.setObject((java.lang.Object)(_jno.GetField("extras")));
 //BA.debugLineNum = 25;BA.debugLine="extras.RunMethod(\"size\", Null)";
_extras.RunMethod("size",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 29;BA.debugLine="If SBN.PackageName = \"cloyd.smart.home.monitor\"";
if ((_sbn.getPackageName()).equals("cloyd.smart.home.monitor")) { 
 //BA.debugLineNum = 30;BA.debugLine="If SBN.Id = 726 Then";
if (_sbn.getId()==726) { 
 //BA.debugLineNum = 31;BA.debugLine="SmartHomeMonitor.IsAirQualityNotificationOnGoi";
mostCurrent._smarthomemonitor._isairqualitynotificationongoing = anywheresoftware.b4a.keywords.Common.True;
 }else if(_sbn.getId()==725) { 
 //BA.debugLineNum = 33;BA.debugLine="SmartHomeMonitor.IsTempHumidityNotificationOnG";
mostCurrent._smarthomemonitor._istemphumiditynotificationongoing = anywheresoftware.b4a.keywords.Common.True;
 };
 };
 };
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _listener_notificationremoved(anywheresoftware.b4a.objects.NotificationListenerWrapper.StatusBarNotificationWrapper _sbn) throws Exception{
 //BA.debugLineNum = 39;BA.debugLine="Sub Listener_NotificationRemoved (SBN As StatusBar";
 //BA.debugLineNum = 42;BA.debugLine="If SBN.PackageName = \"cloyd.smart.home.monitor\" T";
if ((_sbn.getPackageName()).equals("cloyd.smart.home.monitor")) { 
 //BA.debugLineNum = 43;BA.debugLine="If SBN.Id = 726 Then";
if (_sbn.getId()==726) { 
 //BA.debugLineNum = 44;BA.debugLine="SmartHomeMonitor.IsAirQualityNotificationOnGoin";
mostCurrent._smarthomemonitor._isairqualitynotificationongoing = anywheresoftware.b4a.keywords.Common.False;
 }else if(_sbn.getId()==725) { 
 //BA.debugLineNum = 46;BA.debugLine="SmartHomeMonitor.IsTempHumidityNotificationOnGo";
mostCurrent._smarthomemonitor._istemphumiditynotificationongoing = anywheresoftware.b4a.keywords.Common.False;
 };
 };
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Private listener As NotificationListener";
_listener = new anywheresoftware.b4a.objects.NotificationListenerWrapper.NotificationListener();
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 11;BA.debugLine="listener.Initialize(\"listener\")";
_listener.Initialize(processBA,"listener");
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 59;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 61;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 15;BA.debugLine="If listener.HandleIntent(StartingIntent) Then Ret";
if (_listener.HandleIntent(_startingintent)) { 
if (true) return "";};
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
}
