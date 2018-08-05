package cloyd.smart.home.monitor;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class smarthomemonitor extends  android.app.Service{
	public static class smarthomemonitor_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (smarthomemonitor) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, smarthomemonitor.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, false, BA.class);
		}

	}
    static smarthomemonitor mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return smarthomemonitor.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "cloyd.smart.home.monitor", "cloyd.smart.home.monitor.smarthomemonitor");
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
			processBA.raiseEvent2(null, true, "CREATE", true, "cloyd.smart.home.monitor.smarthomemonitor", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!false && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, true) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (smarthomemonitor) Create ***");
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
                    BA.LogInfo("** Service (smarthomemonitor) Create **");
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
    	BA.LogInfo("** Service (smarthomemonitor) Start **");
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
        BA.LogInfo("** Service (smarthomemonitor) Destroy **");
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
public static anywheresoftware.b4j.objects.MqttAsyncClientWrapper _mqtt = null;
public static String _mqttuser = "";
public static String _mqttpassword = "";
public static String _mqttserveruri = "";
public static anywheresoftware.b4a.objects.NotificationWrapper _notification1 = null;
public static boolean _isairqualitynotificationongoing = false;
public static boolean _istemphumiditynotificationongoing = false;
public static boolean _isairqualitynotificationongoingbasement = false;
public static boolean _istemphumiditynotificationongoingbasement = false;
public static long _lngticks = 0L;
public static long _lngtickstemphumid = 0L;
public static long _lngtickstemphumidbasement = 0L;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public static anywheresoftware.b4a.objects.NotificationWrapper  _createnotification(String _title,String _content,String _icon,Object _targetactivity,boolean _sound,boolean _vibrate,boolean _showbadge,String _channelname) throws Exception{
anywheresoftware.b4a.phone.Phone _p = null;
barxdroid.NotificationBuilder.NotificationBuilder _nb = null;
anywheresoftware.b4j.object.JavaObject _ctxt = null;
anywheresoftware.b4j.object.JavaObject _manager = null;
anywheresoftware.b4j.object.JavaObject _channel = null;
String _importance = "";
String _channelvisiblename = "";
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.objects.NotificationWrapper _n = null;
 //BA.debugLineNum = 294;BA.debugLine="Private Sub CreateNotification(Title As String, Co";
 //BA.debugLineNum = 296;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 297;BA.debugLine="If p.SdkVersion >= 21 Then";
if (_p.getSdkVersion()>=21) { 
 //BA.debugLineNum = 298;BA.debugLine="Dim nb As NotificationBuilder";
_nb = new barxdroid.NotificationBuilder.NotificationBuilder();
 //BA.debugLineNum = 299;BA.debugLine="nb.Initialize";
_nb.Initialize(processBA);
 //BA.debugLineNum = 300;BA.debugLine="nb.DefaultSound = Sound";
_nb.setDefaultSound(_sound);
 //BA.debugLineNum = 301;BA.debugLine="nb.DefaultVibrate = Vibrate";
_nb.setDefaultVibrate(_vibrate);
 //BA.debugLineNum = 302;BA.debugLine="nb.ContentTitle = Title";
_nb.setContentTitle(_title);
 //BA.debugLineNum = 303;BA.debugLine="nb.ContentText = Content";
_nb.setContentText(_content);
 //BA.debugLineNum = 304;BA.debugLine="nb.setActivity(TargetActivity)";
_nb.setActivity(processBA,_targetactivity);
 //BA.debugLineNum = 305;BA.debugLine="nb.OnlyAlertOnce = True";
_nb.setOnlyAlertOnce(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 306;BA.debugLine="nb.SmallIcon = Icon";
_nb.setSmallIcon(_icon);
 //BA.debugLineNum = 307;BA.debugLine="nb.Tag = ChannelName";
_nb.setTag(_channelname);
 //BA.debugLineNum = 308;BA.debugLine="If p.SdkVersion >= 26 Then";
if (_p.getSdkVersion()>=26) { 
 //BA.debugLineNum = 309;BA.debugLine="Dim ctxt As JavaObject";
_ctxt = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 310;BA.debugLine="ctxt.InitializeContext";
_ctxt.InitializeContext(processBA);
 //BA.debugLineNum = 311;BA.debugLine="Dim manager As JavaObject";
_manager = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 312;BA.debugLine="manager.InitializeStatic(\"android.app.Notificat";
_manager.InitializeStatic("android.app.NotificationManager");
 //BA.debugLineNum = 313;BA.debugLine="Dim Channel As JavaObject";
_channel = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 314;BA.debugLine="Dim importance As String";
_importance = "";
 //BA.debugLineNum = 316;BA.debugLine="importance = \"IMPORTANCE_HIGH\"";
_importance = "IMPORTANCE_HIGH";
 //BA.debugLineNum = 317;BA.debugLine="Dim ChannelVisibleName As String = ChannelName";
_channelvisiblename = _channelname;
 //BA.debugLineNum = 318;BA.debugLine="Channel.InitializeNewInstance(\"android.app.Noti";
_channel.InitializeNewInstance("android.app.NotificationChannel",new Object[]{(Object)(_channelname),(Object)(_channelvisiblename),_manager.GetField(_importance)});
 //BA.debugLineNum = 322;BA.debugLine="Channel.RunMethod(\"setShowBadge\", Array(ShowBad";
_channel.RunMethod("setShowBadge",new Object[]{(Object)(_showbadge)});
 //BA.debugLineNum = 323;BA.debugLine="manager = ctxt.RunMethod(\"getSystemService\", Ar";
_manager.setObject((java.lang.Object)(_ctxt.RunMethod("getSystemService",new Object[]{(Object)("notification")})));
 //BA.debugLineNum = 324;BA.debugLine="manager.RunMethod(\"createNotificationChannel\",";
_manager.RunMethod("createNotificationChannel",new Object[]{(Object)(_channel.getObject())});
 //BA.debugLineNum = 325;BA.debugLine="Dim jo As JavaObject = nb";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_nb.getObject()));
 //BA.debugLineNum = 326;BA.debugLine="jo.RunMethod(\"setChannelId\", Array(ChannelName)";
_jo.RunMethod("setChannelId",new Object[]{(Object)(_channelname)});
 };
 //BA.debugLineNum = 328;BA.debugLine="Return  nb.GetNotification";
if (true) return (anywheresoftware.b4a.objects.NotificationWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.NotificationWrapper(), (java.lang.Object)(_nb.GetNotification(processBA)));
 }else {
 //BA.debugLineNum = 330;BA.debugLine="Dim n As Notification";
_n = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 331;BA.debugLine="n.Initialize";
_n.Initialize();
 //BA.debugLineNum = 332;BA.debugLine="n.Sound = Sound";
_n.setSound(_sound);
 //BA.debugLineNum = 333;BA.debugLine="n.Vibrate = Vibrate";
_n.setVibrate(_vibrate);
 //BA.debugLineNum = 334;BA.debugLine="n.Icon = Icon";
_n.setIcon(_icon);
 //BA.debugLineNum = 335;BA.debugLine="n.SetInfo(Title, Content, TargetActivity)";
_n.SetInfoNew(processBA,BA.ObjectToCharSequence(_title),BA.ObjectToCharSequence(_content),_targetactivity);
 //BA.debugLineNum = 336;BA.debugLine="Return n";
if (true) return _n;
 };
 //BA.debugLineNum = 338;BA.debugLine="End Sub";
return null;
}
public static String  _getairquality(int _number) throws Exception{
 //BA.debugLineNum = 400;BA.debugLine="Sub GetAirQuality(number As Int) As String";
 //BA.debugLineNum = 403;BA.debugLine="If number <= 100 Then";
if (_number<=100) { 
 //BA.debugLineNum = 404;BA.debugLine="Return(\"Carbon monoxide level is perfect\")";
if (true) return ("Carbon monoxide level is perfect");
 }else if(((_number>100) && (_number<400)) || _number==400) { 
 //BA.debugLineNum = 406;BA.debugLine="Return(\"Carbon monoxide level is normal\")";
if (true) return ("Carbon monoxide level is normal");
 }else if(((_number>400) && (_number<900)) || _number==900) { 
 //BA.debugLineNum = 408;BA.debugLine="Return(\"Carbon monoxide level is high\")";
if (true) return ("Carbon monoxide level is high");
 }else if(_number>900) { 
 //BA.debugLineNum = 410;BA.debugLine="Return(\"ALARM Carbon monoxide level is very high";
if (true) return ("ALARM Carbon monoxide level is very high");
 }else {
 //BA.debugLineNum = 412;BA.debugLine="Return(\"MQ-7 - cant read any value - check the s";
if (true) return ("MQ-7 - cant read any value - check the sensor!");
 };
 //BA.debugLineNum = 414;BA.debugLine="End Sub";
return "";
}
public static String  _getcomfort(String _dht11comfortstatus) throws Exception{
String _localcomfortstatus = "";
 //BA.debugLineNum = 373;BA.debugLine="Sub GetComfort(DHT11ComfortStatus As String) As St";
 //BA.debugLineNum = 374;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = "";
 //BA.debugLineNum = 375;BA.debugLine="Select Case DHT11ComfortStatus";
switch (BA.switchObjectToInt(_dht11comfortstatus,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(8),BA.NumberToString(9),BA.NumberToString(10))) {
case 0: {
 //BA.debugLineNum = 377;BA.debugLine="localcomfortstatus = \"Home is OK\"";
_localcomfortstatus = "Home is OK";
 break; }
case 1: {
 //BA.debugLineNum = 379;BA.debugLine="localcomfortstatus = \"Home is too hot\"";
_localcomfortstatus = "Home is too hot";
 break; }
case 2: {
 //BA.debugLineNum = 381;BA.debugLine="localcomfortstatus = \"Home is too cold\"";
_localcomfortstatus = "Home is too cold";
 break; }
case 3: {
 //BA.debugLineNum = 383;BA.debugLine="localcomfortstatus = \"Home is too dry\"";
_localcomfortstatus = "Home is too dry";
 break; }
case 4: {
 //BA.debugLineNum = 385;BA.debugLine="localcomfortstatus = \"Home is hot and dry\"";
_localcomfortstatus = "Home is hot and dry";
 break; }
case 5: {
 //BA.debugLineNum = 387;BA.debugLine="localcomfortstatus = \"Home is cold and dry\"";
_localcomfortstatus = "Home is cold and dry";
 break; }
case 6: {
 //BA.debugLineNum = 389;BA.debugLine="localcomfortstatus = \"Home is too humid\"";
_localcomfortstatus = "Home is too humid";
 break; }
case 7: {
 //BA.debugLineNum = 391;BA.debugLine="localcomfortstatus = \"Home is hot and humid\"";
_localcomfortstatus = "Home is hot and humid";
 break; }
case 8: {
 //BA.debugLineNum = 393;BA.debugLine="localcomfortstatus = \"Home is cold and humid\"";
_localcomfortstatus = "Home is cold and humid";
 break; }
default: {
 //BA.debugLineNum = 395;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = "Unknown";
 break; }
}
;
 //BA.debugLineNum = 397;BA.debugLine="Return localcomfortstatus";
if (true) return _localcomfortstatus;
 //BA.debugLineNum = 398;BA.debugLine="End Sub";
return "";
}
public static String  _getperception(String _dht11perception) throws Exception{
String _localperception = "";
 //BA.debugLineNum = 340;BA.debugLine="Sub GetPerception(DHT11Perception As String) As St";
 //BA.debugLineNum = 351;BA.debugLine="Dim localperception As String";
_localperception = "";
 //BA.debugLineNum = 352;BA.debugLine="Select Case DHT11Perception";
switch (BA.switchObjectToInt(_dht11perception,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(7))) {
case 0: {
 //BA.debugLineNum = 354;BA.debugLine="localperception = \"Home feels like the western";
_localperception = "Home feels like the western US, a bit dry to some";
 break; }
case 1: {
 //BA.debugLineNum = 356;BA.debugLine="localperception = \"Home is very comfortable\"";
_localperception = "Home is very comfortable";
 break; }
case 2: {
 //BA.debugLineNum = 358;BA.debugLine="localperception = \"Home is comfortable\"";
_localperception = "Home is comfortable";
 break; }
case 3: {
 //BA.debugLineNum = 360;BA.debugLine="localperception = \"Home is okay but the humidit";
_localperception = "Home is okay but the humidity is at upper limit";
 break; }
case 4: {
 //BA.debugLineNum = 362;BA.debugLine="localperception = \"Home is uncomfortable, and t";
_localperception = "Home is uncomfortable, and the humidity is at upper limit";
 break; }
case 5: {
 //BA.debugLineNum = 364;BA.debugLine="localperception = \"Home is very humid, and quit";
_localperception = "Home is very humid, and quite uncomfortable";
 break; }
case 6: {
 //BA.debugLineNum = 366;BA.debugLine="localperception = \"Home is extremely uncomforta";
_localperception = "Home is extremely uncomfortable, and oppressive";
 break; }
case 7: {
 //BA.debugLineNum = 368;BA.debugLine="localperception = \"Home humidity is severely hi";
_localperception = "Home humidity is severely high, and even deadly for asthma related illnesses";
 break; }
}
;
 //BA.debugLineNum = 370;BA.debugLine="Return localperception";
if (true) return _localperception;
 //BA.debugLineNum = 371;BA.debugLine="End Sub";
return "";
}
public static String  _logevent(String _texttolog) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _fw1 = null;
String _filename = "";
long _now = 0L;
int _month = 0;
int _day = 0;
int _year = 0;
String _logentry = "";
 //BA.debugLineNum = 263;BA.debugLine="Sub LogEvent(TextToLog As String)";
 //BA.debugLineNum = 264;BA.debugLine="Try";
try { //BA.debugLineNum = 265;BA.debugLine="Dim FW1 As TextWriter";
_fw1 = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 266;BA.debugLine="Dim FileName As String";
_filename = "";
 //BA.debugLineNum = 267;BA.debugLine="Dim Now As Long";
_now = 0L;
 //BA.debugLineNum = 268;BA.debugLine="Dim Month As Int";
_month = 0;
 //BA.debugLineNum = 269;BA.debugLine="Dim Day As Int";
_day = 0;
 //BA.debugLineNum = 270;BA.debugLine="Dim Year As Int";
_year = 0;
 //BA.debugLineNum = 271;BA.debugLine="Dim LogEntry As String";
_logentry = "";
 //BA.debugLineNum = 273;BA.debugLine="Now = DateTime.Now";
_now = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 274;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 275;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 276;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 278;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\" &";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 //BA.debugLineNum = 281;BA.debugLine="FW1.Initialize(File.OpenOutput (File.DirRootExte";
_fw1.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_filename,anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 282;BA.debugLine="LogEntry = NumberFormat(DateTime.GetHour(Now),2,";
_logentry = anywheresoftware.b4a.keywords.Common.NumberFormat(anywheresoftware.b4a.keywords.Common.DateTime.GetHour(_now),(int) (2),(int) (0))+":"+anywheresoftware.b4a.keywords.Common.NumberFormat(anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(_now),(int) (2),(int) (0))+":"+anywheresoftware.b4a.keywords.Common.NumberFormat(anywheresoftware.b4a.keywords.Common.DateTime.GetSecond(_now),(int) (2),(int) (0));
 //BA.debugLineNum = 283;BA.debugLine="LogEntry =LogEntry & \" \" & TextToLog";
_logentry = _logentry+" "+_texttolog;
 //BA.debugLineNum = 284;BA.debugLine="FW1.WriteLine(LogEntry)";
_fw1.WriteLine(_logentry);
 //BA.debugLineNum = 286;BA.debugLine="FW1.Close";
_fw1.Close();
 } 
       catch (Exception e20) {
			processBA.setLastException(e20); //BA.debugLineNum = 289;BA.debugLine="Log(\"Error in Sub LogEvent\")";
anywheresoftware.b4a.keywords.Common.Log("Error in Sub LogEvent");
 };
 //BA.debugLineNum = 292;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connect() throws Exception{
String _clientid = "";
anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper _connopt = null;
 //BA.debugLineNum = 49;BA.debugLine="Sub MQTT_Connect";
 //BA.debugLineNum = 50;BA.debugLine="Try";
try { //BA.debugLineNum = 51;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'crea";
_clientid = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (999999999)));
 //BA.debugLineNum = 52;BA.debugLine="MQTT.Initialize(\"MQTT\", MQTTServerURI, ClientId)";
_mqtt.Initialize(processBA,"MQTT",_mqttserveruri,_clientid);
 //BA.debugLineNum = 54;BA.debugLine="Dim ConnOpt As MqttConnectOptions";
_connopt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper();
 //BA.debugLineNum = 55;BA.debugLine="ConnOpt.Initialize(MQTTUser, MQTTPassword)";
_connopt.Initialize(_mqttuser,_mqttpassword);
 //BA.debugLineNum = 56;BA.debugLine="MQTT.Connect2(ConnOpt)";
_mqtt.Connect2((org.eclipse.paho.client.mqttv3.MqttConnectOptions)(_connopt.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 58;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 60;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 62;BA.debugLine="Sub MQTT_Connected (Success As Boolean)";
 //BA.debugLineNum = 63;BA.debugLine="Try";
try { //BA.debugLineNum = 64;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 65;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 //BA.debugLineNum = 66;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 }else {
 //BA.debugLineNum = 68;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
_mqtt.Subscribe("TempHumid",(int) (0));
 //BA.debugLineNum = 69;BA.debugLine="MQTT.Subscribe(\"MQ7\", 0)";
_mqtt.Subscribe("MQ7",(int) (0));
 //BA.debugLineNum = 70;BA.debugLine="MQTT.Subscribe(\"MQ7Basement\", 0)";
_mqtt.Subscribe("MQ7Basement",(int) (0));
 //BA.debugLineNum = 71;BA.debugLine="MQTT.Subscribe(\"TempHumidBasement\", 0)";
_mqtt.Subscribe("TempHumidBasement",(int) (0));
 };
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 74;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 77;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_disconnected() throws Exception{
 //BA.debugLineNum = 79;BA.debugLine="Private Sub MQTT_Disconnected";
 //BA.debugLineNum = 80;BA.debugLine="Try";
try { //BA.debugLineNum = 81;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e4) {
			processBA.setLastException(e4); //BA.debugLineNum = 83;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
String _status = "";
String[] _a = null;
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _notificationtext = "";
b4a.example.dateutils._period _p = null;
String _managertemphumiditycooldowntime = "";
String _filenametoday = "";
String _filenameyesterday = "";
long _now = 0L;
int _month = 0;
int _day = 0;
int _year = 0;
long _yesterday = 0L;
int _monthyesterday = 0;
int _dayyesterday = 0;
int _yearyesterday = 0;
anywheresoftware.b4a.objects.collections.List _flist = null;
int _i = 0;
String _filename = "";
 //BA.debugLineNum = 87;BA.debugLine="Private Sub MQTT_MessageArrived (Topic As String,";
 //BA.debugLineNum = 88;BA.debugLine="Try";
try { //BA.debugLineNum = 89;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic).equals("TempHumid")) { 
 //BA.debugLineNum = 91;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 92;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 94;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 95;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 96;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 97;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 98;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 99;BA.debugLine="StateManager.SetSetting(\"TempHumidity\",status";
mostCurrent._statemanager._setsetting(processBA,"TempHumidity",_status);
 //BA.debugLineNum = 100;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(processBA);
 //BA.debugLineNum = 102;BA.debugLine="If DateTime.GetMinute(DateTime.Now) Mod 10 =";
if (anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(anywheresoftware.b4a.keywords.Common.DateTime.getNow())%10==0 || anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(anywheresoftware.b4a.keywords.Common.DateTime.getNow())==59) { 
 //BA.debugLineNum = 103;BA.debugLine="LogEvent(status)";
_logevent(_status);
 };
 //BA.debugLineNum = 106;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 107;BA.debugLine="NotificationText = \"Temperature: \" & a(1) & \"";
_notificationtext = "Temperature: "+_a[(int) (1)]+"°F | Humidity: "+_a[(int) (2)]+"% | Comfort: "+_getcomfort(_a[(int) (4)]);
 //BA.debugLineNum = 109;BA.debugLine="If (a(3) > 3) Or (a(4) <> 0)  Then";
if (((double)(Double.parseDouble(_a[(int) (3)]))>3) || ((_a[(int) (4)]).equals(BA.NumberToString(0)) == false)) { 
 //BA.debugLineNum = 110;BA.debugLine="If IsTempHumidityNotificationOnGoing = False";
if (_istemphumiditynotificationongoing==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 111;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(l";
_p = mostCurrent._dateutils._periodbetween(processBA,_lngtickstemphumid,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 112;BA.debugLine="Dim managerTempHumidityCooldownTime As Stri";
_managertemphumiditycooldowntime = mostCurrent._statemanager._getsetting(processBA,"TempHumidityCooldownTime");
 //BA.debugLineNum = 113;BA.debugLine="If managerTempHumidityCooldownTime = \"\" Or";
if ((_managertemphumiditycooldowntime).equals("") || anywheresoftware.b4a.keywords.Common.IsNumber(_managertemphumiditycooldowntime)==anywheresoftware.b4a.keywords.Common.False || (_managertemphumiditycooldowntime).equals("0")) { 
 //BA.debugLineNum = 114;BA.debugLine="managerTempHumidityCooldownTime = 1";
_managertemphumiditycooldowntime = BA.NumberToString(1);
 };
 //BA.debugLineNum = 116;BA.debugLine="If p.Minutes > = managerTempHumidityCooldow";
if (_p.Minutes>=(double)(Double.parseDouble(_managertemphumiditycooldowntime))) { 
 //BA.debugLineNum = 117;BA.debugLine="If a(4) <> 0 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(0)) == false) { 
 //BA.debugLineNum = 118;BA.debugLine="CreateNotification(GetComfort(a(4)),Notif";
_createnotification(_getcomfort(_a[(int) (4)]),_notificationtext,"temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Temperature").Notify((int) (725));
 }else {
 //BA.debugLineNum = 120;BA.debugLine="CreateNotification(GetPerception(a(3)),No";
_createnotification(_getperception(_a[(int) (3)]),_notificationtext,"temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Temperature").Notify((int) (725));
 };
 //BA.debugLineNum = 122;BA.debugLine="lngTicksTempHumid = DateTime.now";
_lngtickstemphumid = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 };
 };
 }else {
 //BA.debugLineNum = 126;BA.debugLine="lngTicksTempHumid = DateTime.now";
_lngtickstemphumid = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 127;BA.debugLine="IsTempHumidityNotificationOnGoing = False";
_istemphumiditynotificationongoing = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 128;BA.debugLine="Notification1.Cancel(725)";
_notification1.Cancel((int) (725));
 };
 };
 };
 }else if((_topic).equals("MQ7")) { 
 //BA.debugLineNum = 133;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 134;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 135;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 136;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 137;BA.debugLine="Log(\"MQ7 status: \" & status)";
anywheresoftware.b4a.keywords.Common.Log("MQ7 status: "+_status);
 //BA.debugLineNum = 138;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 139;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 140;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 141;BA.debugLine="StateManager.SetSetting(\"AirQuality\",status)";
mostCurrent._statemanager._setsetting(processBA,"AirQuality",_status);
 //BA.debugLineNum = 142;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(processBA);
 //BA.debugLineNum = 144;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 145;BA.debugLine="NotificationText = GetAirQuality(a(0)) & \", a";
_notificationtext = _getairquality((int)(Double.parseDouble(_a[(int) (0)])))+", at "+_a[(int) (0)]+" ppm";
 //BA.debugLineNum = 146;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 147;BA.debugLine="If IsAirQualityNotificationOnGoing = False T";
if (_isairqualitynotificationongoing==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 148;BA.debugLine="CreateNotification(\"Living Area Air Quality";
_createnotification("Living Area Air Quality",_notificationtext,"co",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Carbon Monoxide").Notify((int) (726));
 };
 }else {
 //BA.debugLineNum = 151;BA.debugLine="IsAirQualityNotificationOnGoing = False";
_isairqualitynotificationongoing = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 152;BA.debugLine="Notification1.Cancel(726)";
_notification1.Cancel((int) (726));
 };
 };
 };
 }else if((_topic).equals("MQ7Basement")) { 
 //BA.debugLineNum = 157;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 158;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 159;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 160;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 161;BA.debugLine="Log(\"MQ7 basement status: \" & status)";
anywheresoftware.b4a.keywords.Common.Log("MQ7 basement status: "+_status);
 //BA.debugLineNum = 162;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 163;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 164;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 165;BA.debugLine="StateManager.SetSetting(\"AirQualityBasement\",";
mostCurrent._statemanager._setsetting(processBA,"AirQualityBasement",_status);
 //BA.debugLineNum = 166;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(processBA);
 //BA.debugLineNum = 168;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 169;BA.debugLine="NotificationText = GetAirQuality(a(0)) & \", a";
_notificationtext = _getairquality((int)(Double.parseDouble(_a[(int) (0)])))+", at "+_a[(int) (0)]+" ppm";
 //BA.debugLineNum = 170;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 171;BA.debugLine="If IsAirQualityNotificationOnGoingBasement =";
if (_isairqualitynotificationongoingbasement==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 172;BA.debugLine="CreateNotification(\"Basement Air Quality\",N";
_createnotification("Basement Air Quality",_notificationtext,"co",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement Carbon Monoxide").Notify((int) (727));
 };
 }else {
 //BA.debugLineNum = 175;BA.debugLine="IsAirQualityNotificationOnGoingBasement = Fa";
_isairqualitynotificationongoingbasement = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 176;BA.debugLine="Notification1.Cancel(727)";
_notification1.Cancel((int) (727));
 };
 };
 };
 //BA.debugLineNum = 182;BA.debugLine="Dim FileNameToday As String";
_filenametoday = "";
 //BA.debugLineNum = 183;BA.debugLine="Dim FileNameYesterday As String";
_filenameyesterday = "";
 //BA.debugLineNum = 184;BA.debugLine="Dim Now As Long";
_now = 0L;
 //BA.debugLineNum = 185;BA.debugLine="Dim Month As Int";
_month = 0;
 //BA.debugLineNum = 186;BA.debugLine="Dim Day As Int";
_day = 0;
 //BA.debugLineNum = 187;BA.debugLine="Dim Year As Int";
_year = 0;
 //BA.debugLineNum = 188;BA.debugLine="Dim Yesterday As Long";
_yesterday = 0L;
 //BA.debugLineNum = 189;BA.debugLine="Dim MonthYesterday As Int";
_monthyesterday = 0;
 //BA.debugLineNum = 190;BA.debugLine="Dim DayYesterday As Int";
_dayyesterday = 0;
 //BA.debugLineNum = 191;BA.debugLine="Dim YearYesterday As Int";
_yearyesterday = 0;
 //BA.debugLineNum = 193;BA.debugLine="Now = DateTime.Now";
_now = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 194;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 195;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 196;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 198;BA.debugLine="Yesterday = DateTime.add(DateTime.Now, 0, 0, -1";
_yesterday = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1));
 //BA.debugLineNum = 199;BA.debugLine="MonthYesterday = DateTime.GetMonth(Yesterday)";
_monthyesterday = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_yesterday);
 //BA.debugLineNum = 200;BA.debugLine="DayYesterday = DateTime.GetDayOfMonth (Yesterda";
_dayyesterday = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_yesterday);
 //BA.debugLineNum = 201;BA.debugLine="YearYesterday = DateTime.GetYear(Yesterday)";
_yearyesterday = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_yesterday);
 //BA.debugLineNum = 203;BA.debugLine="FileNameToday = \"LivingRoomTempHumid_\" & Year &";
_filenametoday = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 //BA.debugLineNum = 204;BA.debugLine="FileNameYesterday = \"LivingRoomTempHumid_\" & Ye";
_filenameyesterday = "LivingRoomTempHumid_"+BA.NumberToString(_yearyesterday)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_monthyesterday,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_dayyesterday,(int) (2),(int) (0))+".log";
 //BA.debugLineNum = 206;BA.debugLine="Dim flist As List = WildCardFilesList2(File.Dir";
_flist = new anywheresoftware.b4a.objects.collections.List();
_flist = _wildcardfileslist2(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"LivingRoomTempHumid_*.log",anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 208;BA.debugLine="For i = 0 To flist.Size -1";
{
final int step107 = 1;
final int limit107 = (int) (_flist.getSize()-1);
_i = (int) (0) ;
for (;(step107 > 0 && _i <= limit107) || (step107 < 0 && _i >= limit107) ;_i = ((int)(0 + _i + step107))  ) {
 //BA.debugLineNum = 209;BA.debugLine="Dim FileName As String = flist.Get(i)";
_filename = BA.ObjectToString(_flist.Get(_i));
 //BA.debugLineNum = 210;BA.debugLine="Log(FileName)";
anywheresoftware.b4a.keywords.Common.Log(_filename);
 //BA.debugLineNum = 211;BA.debugLine="If FileName <> FileNameToday Then";
if ((_filename).equals(_filenametoday) == false) { 
 //BA.debugLineNum = 212;BA.debugLine="If FileName <> FileNameYesterday Then";
if ((_filename).equals(_filenameyesterday) == false) { 
 //BA.debugLineNum = 213;BA.debugLine="File.Delete(File.DirRootExternal,FileName)";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_filename);
 };
 };
 }
};
 }else if((_topic).equals("TempHumidBasement")) { 
 //BA.debugLineNum = 220;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 221;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 223;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 224;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 225;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 226;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 227;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 228;BA.debugLine="StateManager.SetSetting(\"TempHumidityBasement";
mostCurrent._statemanager._setsetting(processBA,"TempHumidityBasement",_status);
 //BA.debugLineNum = 229;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(processBA);
 //BA.debugLineNum = 231;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 232;BA.debugLine="NotificationText = \"Temperature: \" & a(1) & \"";
_notificationtext = "Temperature: "+_a[(int) (1)]+"°F | Humidity: "+_a[(int) (2)]+"% | Comfort: "+_getcomfort(_a[(int) (4)]);
 //BA.debugLineNum = 234;BA.debugLine="If (a(3) > 3) Or (a(4) <> 0)  Then";
if (((double)(Double.parseDouble(_a[(int) (3)]))>3) || ((_a[(int) (4)]).equals(BA.NumberToString(0)) == false)) { 
 //BA.debugLineNum = 235;BA.debugLine="If IsTempHumidityNotificationOnGoingBasement";
if (_istemphumiditynotificationongoingbasement==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 236;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(l";
_p = mostCurrent._dateutils._periodbetween(processBA,_lngtickstemphumidbasement,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 237;BA.debugLine="Dim managerTempHumidityCooldownTime As Stri";
_managertemphumiditycooldowntime = mostCurrent._statemanager._getsetting(processBA,"TempHumidityCooldownTimeBasement");
 //BA.debugLineNum = 238;BA.debugLine="If managerTempHumidityCooldownTime = \"\" Or";
if ((_managertemphumiditycooldowntime).equals("") || anywheresoftware.b4a.keywords.Common.IsNumber(_managertemphumiditycooldowntime)==anywheresoftware.b4a.keywords.Common.False || (_managertemphumiditycooldowntime).equals("0")) { 
 //BA.debugLineNum = 239;BA.debugLine="managerTempHumidityCooldownTime = 1";
_managertemphumiditycooldowntime = BA.NumberToString(1);
 };
 //BA.debugLineNum = 241;BA.debugLine="If p.Minutes > = managerTempHumidityCooldow";
if (_p.Minutes>=(double)(Double.parseDouble(_managertemphumiditycooldowntime))) { 
 //BA.debugLineNum = 242;BA.debugLine="If a(4) <> 0 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(0)) == false) { 
 //BA.debugLineNum = 243;BA.debugLine="CreateNotification(GetComfort(a(4)).Repla";
_createnotification(_getcomfort(_a[(int) (4)]).replace("Home","Basement"),_notificationtext,"temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement Temperature").Notify((int) (728));
 }else {
 //BA.debugLineNum = 245;BA.debugLine="CreateNotification(GetPerception(a(3)).Re";
_createnotification(_getperception(_a[(int) (3)]).replace("Home","Basement"),_notificationtext,"temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement Temperature").Notify((int) (728));
 };
 //BA.debugLineNum = 247;BA.debugLine="lngTicksTempHumidBasement = DateTime.now";
_lngtickstemphumidbasement = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 };
 };
 }else {
 //BA.debugLineNum = 251;BA.debugLine="lngTicksTempHumidBasement = DateTime.now";
_lngtickstemphumidbasement = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 252;BA.debugLine="IsTempHumidityNotificationOnGoingBasement =";
_istemphumiditynotificationongoingbasement = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 253;BA.debugLine="Notification1.Cancel(728)";
_notification1.Cancel((int) (728));
 };
 };
 };
 };
 } 
       catch (Exception e153) {
			processBA.setLastException(e153); //BA.debugLineNum = 259;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 261;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Private MQTT As MqttClient";
_mqtt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper();
 //BA.debugLineNum = 10;BA.debugLine="Private MQTTUser As String = \"vynckfaq\"";
_mqttuser = "vynckfaq";
 //BA.debugLineNum = 11;BA.debugLine="Private MQTTPassword As String = \"KHSV1Q1qSUUY\"";
_mqttpassword = "KHSV1Q1qSUUY";
 //BA.debugLineNum = 12;BA.debugLine="Private MQTTServerURI As String = \"tcp://m14.clou";
_mqttserveruri = "tcp://m14.cloudmqtt.com:11816";
 //BA.debugLineNum = 13;BA.debugLine="Private Notification1 As Notification";
_notification1 = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Public IsAirQualityNotificationOnGoing As Boolean";
_isairqualitynotificationongoing = false;
 //BA.debugLineNum = 15;BA.debugLine="Public IsTempHumidityNotificationOnGoing As Boole";
_istemphumiditynotificationongoing = false;
 //BA.debugLineNum = 16;BA.debugLine="Public IsAirQualityNotificationOnGoingBasement As";
_isairqualitynotificationongoingbasement = false;
 //BA.debugLineNum = 17;BA.debugLine="Public IsTempHumidityNotificationOnGoingBasement";
_istemphumiditynotificationongoingbasement = false;
 //BA.debugLineNum = 18;BA.debugLine="Public lngTicks As Long";
_lngticks = 0L;
 //BA.debugLineNum = 19;BA.debugLine="Public lngTicksTempHumid As Long";
_lngtickstemphumid = 0L;
 //BA.debugLineNum = 20;BA.debugLine="Public lngTicksTempHumidBasement As Long";
_lngtickstemphumidbasement = 0L;
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 24;BA.debugLine="Notification1.Initialize2(Notification1.IMPORTANC";
_notification1.Initialize2(_notification1.IMPORTANCE_DEFAULT);
 //BA.debugLineNum = 25;BA.debugLine="Service.AutomaticForegroundMode = Service.AUTOMAT";
mostCurrent._service.AutomaticForegroundMode = mostCurrent._service.AUTOMATIC_FOREGROUND_ALWAYS;
 //BA.debugLineNum = 26;BA.debugLine="CreateNotification(\"Temperature\",\"Temperature\",\"t";
_createnotification("Temperature","Temperature","temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Temperature");
 //BA.debugLineNum = 27;BA.debugLine="CreateNotification(\"Carbon Monoxide\",\"Carbon Mono";
_createnotification("Carbon Monoxide","Carbon Monoxide","co",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Carbon Monoxide");
 //BA.debugLineNum = 28;BA.debugLine="CreateNotification(\"Basement Temperature\",\"Baseme";
_createnotification("Basement Temperature","Basement Temperature","temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement Temperature");
 //BA.debugLineNum = 29;BA.debugLine="CreateNotification(\"Basement Carbon Monoxide\",\"Ba";
_createnotification("Basement Carbon Monoxide","Basement Carbon Monoxide","co",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement Carbon Monoxide");
 //BA.debugLineNum = 31;BA.debugLine="Notification1.Icon = \"icon\"";
_notification1.setIcon("icon");
 //BA.debugLineNum = 32;BA.debugLine="Notification1.Vibrate = False";
_notification1.setVibrate(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 33;BA.debugLine="Notification1.AutoCancel = False";
_notification1.setAutoCancel(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 34;BA.debugLine="Notification1.Sound = False";
_notification1.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 35;BA.debugLine="Notification1.SetInfo(\"Smart Home Monitor\",\"Servi";
_notification1.SetInfoNew(processBA,BA.ObjectToCharSequence("Smart Home Monitor"),BA.ObjectToCharSequence("Service is running. Tap to open."),(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 36;BA.debugLine="Service.AutomaticForegroundNotification = Notific";
mostCurrent._service.AutomaticForegroundNotification = (android.app.Notification)(_notification1.getObject());
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 44;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 39;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 40;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 //BA.debugLineNum = 42;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _wildcardfileslist2(String _filespath,String _wildcards,boolean _sorted,boolean _ascending) throws Exception{
anywheresoftware.b4a.objects.collections.List _filesfound = null;
String[] _getcards = null;
anywheresoftware.b4a.objects.collections.List _filteredfiles = null;
int _i = 0;
int _l = 0;
String _testitem = "";
String _mask = "";
String _pattern = "";
 //BA.debugLineNum = 416;BA.debugLine="Sub WildCardFilesList2(FilesPath As String, WildCa";
 //BA.debugLineNum = 417;BA.debugLine="If File.IsDirectory(\"\", FilesPath) Then";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory("",_filespath)) { 
 //BA.debugLineNum = 418;BA.debugLine="Dim FilesFound As List = File.ListFiles(FilesPat";
_filesfound = new anywheresoftware.b4a.objects.collections.List();
_filesfound = anywheresoftware.b4a.keywords.Common.File.ListFiles(_filespath);
 //BA.debugLineNum = 419;BA.debugLine="Dim GetCards() As String = Regex.Split(\",\", Wild";
_getcards = anywheresoftware.b4a.keywords.Common.Regex.Split(",",_wildcards);
 //BA.debugLineNum = 420;BA.debugLine="Dim FilteredFiles As List : FilteredFiles.Initia";
_filteredfiles = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 420;BA.debugLine="Dim FilteredFiles As List : FilteredFiles.Initia";
_filteredfiles.Initialize();
 //BA.debugLineNum = 421;BA.debugLine="For i = 0 To FilesFound.Size -1";
{
final int step6 = 1;
final int limit6 = (int) (_filesfound.getSize()-1);
_i = (int) (0) ;
for (;(step6 > 0 && _i <= limit6) || (step6 < 0 && _i >= limit6) ;_i = ((int)(0 + _i + step6))  ) {
 //BA.debugLineNum = 422;BA.debugLine="For l = 0 To GetCards.Length -1";
{
final int step7 = 1;
final int limit7 = (int) (_getcards.length-1);
_l = (int) (0) ;
for (;(step7 > 0 && _l <= limit7) || (step7 < 0 && _l >= limit7) ;_l = ((int)(0 + _l + step7))  ) {
 //BA.debugLineNum = 423;BA.debugLine="Dim TestItem As String = FilesFound.Get(i)";
_testitem = BA.ObjectToString(_filesfound.Get(_i));
 //BA.debugLineNum = 424;BA.debugLine="Dim mask As String = GetCards(l).Trim";
_mask = _getcards[_l].trim();
 //BA.debugLineNum = 425;BA.debugLine="Dim pattern As String = \"^\"&mask.Replace(\".\",\"";
_pattern = "^"+_mask.replace(".","\\.").replace("*",".+").replace("?",".")+"$";
 //BA.debugLineNum = 426;BA.debugLine="If Regex.IsMatch(pattern,TestItem) = True Then";
if (anywheresoftware.b4a.keywords.Common.Regex.IsMatch(_pattern,_testitem)==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 427;BA.debugLine="FilteredFiles.Add(TestItem.Trim)";
_filteredfiles.Add((Object)(_testitem.trim()));
 };
 }
};
 }
};
 //BA.debugLineNum = 431;BA.debugLine="If Sorted Then";
if (_sorted) { 
 //BA.debugLineNum = 432;BA.debugLine="FilteredFiles.SortCaseInsensitive(Ascending)";
_filteredfiles.SortCaseInsensitive(_ascending);
 };
 //BA.debugLineNum = 434;BA.debugLine="Return FilteredFiles";
if (true) return _filteredfiles;
 }else {
 //BA.debugLineNum = 436;BA.debugLine="ToastMessageShow(\"You must pass a valid Director";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You must pass a valid Directory."),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 438;BA.debugLine="End Sub";
return null;
}
}
