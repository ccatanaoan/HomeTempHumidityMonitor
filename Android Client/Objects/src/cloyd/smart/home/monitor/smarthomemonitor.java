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
        if (false) {
            BA.LogInfo("** Service (smarthomemonitor) Destroy (ignored)**");
        }
        else {
            BA.LogInfo("** Service (smarthomemonitor) Destroy **");
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
public static anywheresoftware.b4j.objects.MqttAsyncClientWrapper _mqtt = null;
public static String _mqttuser = "";
public static String _mqttpassword = "";
public static String _mqttserveruri = "";
public static anywheresoftware.b4a.objects.NotificationWrapper _notification1 = null;
public static boolean _isairqualitynotificationongoing = false;
public static boolean _istemphumiditynotificationongoing = false;
public static boolean _isairqualitynotificationongoingbasement = false;
public static boolean _istemphumiditynotificationongoingbasement = false;
public static boolean _isoldtemphumiditynotificationongoingbasement = false;
public static boolean _isoldtemphumiditynotificationongoing = false;
public static boolean _isoldairqualitynotificationongoing = false;
public static boolean _isoldairqualitynotificationongoingbasement = false;
public static long _lngticks = 0L;
public static long _lngtickstemphumid = 0L;
public static long _lngtickstemphumidbasement = 0L;
public static anywheresoftware.b4a.agraham.byteconverter.ByteConverter _bc = null;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;
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
 //BA.debugLineNum = 562;BA.debugLine="Private Sub CreateNotification(Title As String, Co";
 //BA.debugLineNum = 564;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 565;BA.debugLine="If p.SdkVersion >= 21 Then";
if (_p.getSdkVersion()>=21) { 
 //BA.debugLineNum = 566;BA.debugLine="Dim nb As NotificationBuilder";
_nb = new barxdroid.NotificationBuilder.NotificationBuilder();
 //BA.debugLineNum = 567;BA.debugLine="nb.Initialize";
_nb.Initialize(processBA);
 //BA.debugLineNum = 568;BA.debugLine="nb.DefaultSound = Sound";
_nb.setDefaultSound(_sound);
 //BA.debugLineNum = 569;BA.debugLine="nb.DefaultVibrate = Vibrate";
_nb.setDefaultVibrate(_vibrate);
 //BA.debugLineNum = 570;BA.debugLine="nb.ContentTitle = Title";
_nb.setContentTitle(_title);
 //BA.debugLineNum = 571;BA.debugLine="nb.ContentText = Content";
_nb.setContentText(_content);
 //BA.debugLineNum = 572;BA.debugLine="nb.setActivity(TargetActivity)";
_nb.setActivity(processBA,_targetactivity);
 //BA.debugLineNum = 573;BA.debugLine="nb.OnlyAlertOnce = True";
_nb.setOnlyAlertOnce(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 574;BA.debugLine="nb.SmallIcon = Icon";
_nb.setSmallIcon(_icon);
 //BA.debugLineNum = 575;BA.debugLine="nb.Tag = ChannelName";
_nb.setTag(_channelname);
 //BA.debugLineNum = 576;BA.debugLine="If p.SdkVersion >= 26 Then";
if (_p.getSdkVersion()>=26) { 
 //BA.debugLineNum = 577;BA.debugLine="Dim ctxt As JavaObject";
_ctxt = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 578;BA.debugLine="ctxt.InitializeContext";
_ctxt.InitializeContext(processBA);
 //BA.debugLineNum = 579;BA.debugLine="Dim manager As JavaObject";
_manager = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 580;BA.debugLine="manager.InitializeStatic(\"android.app.Notificat";
_manager.InitializeStatic("android.app.NotificationManager");
 //BA.debugLineNum = 581;BA.debugLine="Dim Channel As JavaObject";
_channel = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 582;BA.debugLine="Dim importance As String";
_importance = "";
 //BA.debugLineNum = 590;BA.debugLine="importance = \"IMPORTANCE_LOW\"";
_importance = "IMPORTANCE_LOW";
 //BA.debugLineNum = 591;BA.debugLine="Dim ChannelVisibleName As String = ChannelName";
_channelvisiblename = _channelname;
 //BA.debugLineNum = 592;BA.debugLine="Channel.InitializeNewInstance(\"android.app.Noti";
_channel.InitializeNewInstance("android.app.NotificationChannel",new Object[]{(Object)(_channelname),(Object)(_channelvisiblename),_manager.GetField(_importance)});
 //BA.debugLineNum = 596;BA.debugLine="Channel.RunMethod(\"setShowBadge\", Array(ShowBad";
_channel.RunMethod("setShowBadge",new Object[]{(Object)(_showbadge)});
 //BA.debugLineNum = 597;BA.debugLine="manager = ctxt.RunMethod(\"getSystemService\", Ar";
_manager.setObject((java.lang.Object)(_ctxt.RunMethod("getSystemService",new Object[]{(Object)("notification")})));
 //BA.debugLineNum = 598;BA.debugLine="manager.RunMethod(\"createNotificationChannel\",";
_manager.RunMethod("createNotificationChannel",new Object[]{(Object)(_channel.getObject())});
 //BA.debugLineNum = 599;BA.debugLine="Dim jo As JavaObject = nb";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_nb.getObject()));
 //BA.debugLineNum = 600;BA.debugLine="jo.RunMethod(\"setChannelId\", Array(ChannelName)";
_jo.RunMethod("setChannelId",new Object[]{(Object)(_channelname)});
 };
 //BA.debugLineNum = 602;BA.debugLine="Return  nb.GetNotification";
if (true) return (anywheresoftware.b4a.objects.NotificationWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.NotificationWrapper(), (java.lang.Object)(_nb.GetNotification(processBA)));
 }else {
 //BA.debugLineNum = 604;BA.debugLine="Dim n As Notification";
_n = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 605;BA.debugLine="n.Initialize";
_n.Initialize();
 //BA.debugLineNum = 606;BA.debugLine="n.Sound = Sound";
_n.setSound(_sound);
 //BA.debugLineNum = 607;BA.debugLine="n.Vibrate = Vibrate";
_n.setVibrate(_vibrate);
 //BA.debugLineNum = 608;BA.debugLine="n.Icon = Icon";
_n.setIcon(_icon);
 //BA.debugLineNum = 609;BA.debugLine="n.SetInfo(Title, Content, TargetActivity)";
_n.SetInfoNew(processBA,BA.ObjectToCharSequence(_title),BA.ObjectToCharSequence(_content),_targetactivity);
 //BA.debugLineNum = 610;BA.debugLine="Return n";
if (true) return _n;
 };
 //BA.debugLineNum = 612;BA.debugLine="End Sub";
return null;
}
public static String  _getairquality(int _number) throws Exception{
 //BA.debugLineNum = 674;BA.debugLine="Sub GetAirQuality(number As Int) As String";
 //BA.debugLineNum = 677;BA.debugLine="If number <= 100 Then";
if (_number<=100) { 
 //BA.debugLineNum = 678;BA.debugLine="Return(\"Carbon monoxide level is perfect\")";
if (true) return ("Carbon monoxide level is perfect");
 }else if(((_number>100) && (_number<400)) || _number==400) { 
 //BA.debugLineNum = 680;BA.debugLine="Return(\"Carbon monoxide level is normal\")";
if (true) return ("Carbon monoxide level is normal");
 }else if(((_number>400) && (_number<900)) || _number==900) { 
 //BA.debugLineNum = 682;BA.debugLine="Return(\"Carbon monoxide level is high\")";
if (true) return ("Carbon monoxide level is high");
 }else if(_number>900) { 
 //BA.debugLineNum = 684;BA.debugLine="Return(\"ALARM Carbon monoxide level is very high";
if (true) return ("ALARM Carbon monoxide level is very high");
 }else {
 //BA.debugLineNum = 686;BA.debugLine="Return(\"MQ-7 - cant read any value - check the s";
if (true) return ("MQ-7 - cant read any value - check the sensor!");
 };
 //BA.debugLineNum = 688;BA.debugLine="End Sub";
return "";
}
public static String  _getcomfort(String _dht11comfortstatus) throws Exception{
String _localcomfortstatus = "";
 //BA.debugLineNum = 647;BA.debugLine="Sub GetComfort(DHT11ComfortStatus As String) As St";
 //BA.debugLineNum = 648;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = "";
 //BA.debugLineNum = 649;BA.debugLine="Select Case DHT11ComfortStatus";
switch (BA.switchObjectToInt(_dht11comfortstatus,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(8),BA.NumberToString(9),BA.NumberToString(10))) {
case 0: {
 //BA.debugLineNum = 651;BA.debugLine="localcomfortstatus = \"Home is OK\"";
_localcomfortstatus = "Home is OK";
 break; }
case 1: {
 //BA.debugLineNum = 653;BA.debugLine="localcomfortstatus = \"Home is too hot\"";
_localcomfortstatus = "Home is too hot";
 break; }
case 2: {
 //BA.debugLineNum = 655;BA.debugLine="localcomfortstatus = \"Home is too cold\"";
_localcomfortstatus = "Home is too cold";
 break; }
case 3: {
 //BA.debugLineNum = 657;BA.debugLine="localcomfortstatus = \"Home is too dry\"";
_localcomfortstatus = "Home is too dry";
 break; }
case 4: {
 //BA.debugLineNum = 659;BA.debugLine="localcomfortstatus = \"Home is hot and dry\"";
_localcomfortstatus = "Home is hot and dry";
 break; }
case 5: {
 //BA.debugLineNum = 661;BA.debugLine="localcomfortstatus = \"Home is cold and dry\"";
_localcomfortstatus = "Home is cold and dry";
 break; }
case 6: {
 //BA.debugLineNum = 663;BA.debugLine="localcomfortstatus = \"Home is too humid\"";
_localcomfortstatus = "Home is too humid";
 break; }
case 7: {
 //BA.debugLineNum = 665;BA.debugLine="localcomfortstatus = \"Home is hot and humid\"";
_localcomfortstatus = "Home is hot and humid";
 break; }
case 8: {
 //BA.debugLineNum = 667;BA.debugLine="localcomfortstatus = \"Home is cold and humid\"";
_localcomfortstatus = "Home is cold and humid";
 break; }
default: {
 //BA.debugLineNum = 669;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = "Unknown";
 break; }
}
;
 //BA.debugLineNum = 671;BA.debugLine="Return localcomfortstatus";
if (true) return _localcomfortstatus;
 //BA.debugLineNum = 672;BA.debugLine="End Sub";
return "";
}
public static String  _getperception(String _dht11perception) throws Exception{
String _localperception = "";
 //BA.debugLineNum = 614;BA.debugLine="Sub GetPerception(DHT11Perception As String) As St";
 //BA.debugLineNum = 625;BA.debugLine="Dim localperception As String";
_localperception = "";
 //BA.debugLineNum = 626;BA.debugLine="Select Case DHT11Perception";
switch (BA.switchObjectToInt(_dht11perception,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(7))) {
case 0: {
 //BA.debugLineNum = 628;BA.debugLine="localperception = \"Feels like the western US, a";
_localperception = "Feels like the western US, a bit dry to some";
 break; }
case 1: {
 //BA.debugLineNum = 630;BA.debugLine="localperception = \"Very comfortable\"";
_localperception = "Very comfortable";
 break; }
case 2: {
 //BA.debugLineNum = 632;BA.debugLine="localperception = \"Comfortable\"";
_localperception = "Comfortable";
 break; }
case 3: {
 //BA.debugLineNum = 634;BA.debugLine="localperception = \"Okay but the humidity is at";
_localperception = "Okay but the humidity is at upper limit";
 break; }
case 4: {
 //BA.debugLineNum = 636;BA.debugLine="localperception = \"Uncomfortable, and the humid";
_localperception = "Uncomfortable, and the humidity is at upper limit";
 break; }
case 5: {
 //BA.debugLineNum = 638;BA.debugLine="localperception = \"Very humid, and quite uncomf";
_localperception = "Very humid, and quite uncomfortable";
 break; }
case 6: {
 //BA.debugLineNum = 640;BA.debugLine="localperception = \"Extremely uncomfortable, and";
_localperception = "Extremely uncomfortable, and oppressive";
 break; }
case 7: {
 //BA.debugLineNum = 642;BA.debugLine="localperception = \"Humidity is severely high, a";
_localperception = "Humidity is severely high, and even deadly for asthma related illnesses";
 break; }
}
;
 //BA.debugLineNum = 644;BA.debugLine="Return localperception";
if (true) return _localperception;
 //BA.debugLineNum = 645;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 530;BA.debugLine="Sub LogEvent(TextToLog As String)";
 //BA.debugLineNum = 531;BA.debugLine="Try";
try { //BA.debugLineNum = 532;BA.debugLine="Dim FW1 As TextWriter";
_fw1 = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 533;BA.debugLine="Dim FileName As String";
_filename = "";
 //BA.debugLineNum = 534;BA.debugLine="Dim Now As Long";
_now = 0L;
 //BA.debugLineNum = 535;BA.debugLine="Dim Month As Int";
_month = 0;
 //BA.debugLineNum = 536;BA.debugLine="Dim Day As Int";
_day = 0;
 //BA.debugLineNum = 537;BA.debugLine="Dim Year As Int";
_year = 0;
 //BA.debugLineNum = 538;BA.debugLine="Dim LogEntry As String";
_logentry = "";
 //BA.debugLineNum = 540;BA.debugLine="Now = DateTime.Now";
_now = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 541;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 542;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 543;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 545;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\" &";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 //BA.debugLineNum = 548;BA.debugLine="FW1.Initialize(File.OpenOutput (File.DirRootExte";
_fw1.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_filename,anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 549;BA.debugLine="LogEntry = NumberFormat(DateTime.GetHour(Now),2,";
_logentry = anywheresoftware.b4a.keywords.Common.NumberFormat(anywheresoftware.b4a.keywords.Common.DateTime.GetHour(_now),(int) (2),(int) (0))+":"+anywheresoftware.b4a.keywords.Common.NumberFormat(anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(_now),(int) (2),(int) (0))+":"+anywheresoftware.b4a.keywords.Common.NumberFormat(anywheresoftware.b4a.keywords.Common.DateTime.GetSecond(_now),(int) (2),(int) (0));
 //BA.debugLineNum = 550;BA.debugLine="LogEntry =LogEntry & \" \" & TextToLog";
_logentry = _logentry+" "+_texttolog;
 //BA.debugLineNum = 551;BA.debugLine="FW1.WriteLine(LogEntry)";
_fw1.WriteLine(_logentry);
 //BA.debugLineNum = 553;BA.debugLine="FW1.Close";
_fw1.Close();
 } 
       catch (Exception e20) {
			processBA.setLastException(e20); //BA.debugLineNum = 556;BA.debugLine="Log(\"Error in Sub LogEvent: \" & LastException.Me";
anywheresoftware.b4a.keywords.Common.LogImpl("15898266","Error in Sub LogEvent: "+anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage(),0);
 //BA.debugLineNum = 557;BA.debugLine="ToastMessageShow(LastException,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(processBA).getObject()),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 560;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connect() throws Exception{
String _clientid = "";
anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper _connopt = null;
 //BA.debugLineNum = 58;BA.debugLine="Sub MQTT_Connect";
 //BA.debugLineNum = 59;BA.debugLine="Try";
try { //BA.debugLineNum = 60;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'crea";
_clientid = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (999999999)));
 //BA.debugLineNum = 61;BA.debugLine="MQTT.Initialize(\"MQTT\", MQTTServerURI, ClientId)";
_mqtt.Initialize(processBA,"MQTT",_mqttserveruri,_clientid);
 //BA.debugLineNum = 63;BA.debugLine="Dim ConnOpt As MqttConnectOptions";
_connopt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper();
 //BA.debugLineNum = 64;BA.debugLine="ConnOpt.Initialize(MQTTUser, MQTTPassword)";
_connopt.Initialize(_mqttuser,_mqttpassword);
 //BA.debugLineNum = 65;BA.debugLine="MQTT.Connect2(ConnOpt)";
_mqtt.Connect2((org.eclipse.paho.client.mqttv3.MqttConnectOptions)(_connopt.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 67;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("15636105",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)),0);
 };
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 71;BA.debugLine="Sub MQTT_Connected (Success As Boolean)";
 //BA.debugLineNum = 72;BA.debugLine="Try";
try { //BA.debugLineNum = 73;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 74;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("15701635",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)),0);
 //BA.debugLineNum = 75;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 }else {
 //BA.debugLineNum = 77;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
_mqtt.Subscribe("TempHumid",(int) (0));
 //BA.debugLineNum = 78;BA.debugLine="MQTT.Subscribe(\"MQ7\", 0)";
_mqtt.Subscribe("MQ7",(int) (0));
 //BA.debugLineNum = 79;BA.debugLine="MQTT.Subscribe(\"MQ7Basement\", 0)";
_mqtt.Subscribe("MQ7Basement",(int) (0));
 //BA.debugLineNum = 80;BA.debugLine="MQTT.Subscribe(\"TempHumidBasement\", 0)";
_mqtt.Subscribe("TempHumidBasement",(int) (0));
 //BA.debugLineNum = 81;BA.debugLine="MQTT.Subscribe(\"HumidityAddValue\", 0)";
_mqtt.Subscribe("HumidityAddValue",(int) (0));
 };
 } 
       catch (Exception e13) {
			processBA.setLastException(e13); //BA.debugLineNum = 84;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("15701645",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)),0);
 //BA.debugLineNum = 85;BA.debugLine="ToastMessageShow(LastException,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(processBA).getObject()),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 88;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_disconnected() throws Exception{
 //BA.debugLineNum = 90;BA.debugLine="Private Sub MQTT_Disconnected";
 //BA.debugLineNum = 91;BA.debugLine="Try";
try { //BA.debugLineNum = 92;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e4) {
			processBA.setLastException(e4); //BA.debugLineNum = 94;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("15767172",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)),0);
 //BA.debugLineNum = 95;BA.debugLine="ToastMessageShow(LastException,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(processBA).getObject()),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
String _strhumidityaddvalue = "";
String _status = "";
String[] _a = null;
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _notificationtext = "";
b4a.example.dateutils._period _p = null;
String _managertemphumiditycooldowntime = "";
String[] _temphumidityprevious = null;
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
String[] _temphumiditybasementprevious = null;
String _managersensornotrespondingtime = "";
String _sensorintrouble = "";
anywheresoftware.b4a.objects.NotificationWrapper _n = null;
long _tomorrow = 0L;
long _ticks = 0L;
 //BA.debugLineNum = 99;BA.debugLine="Private Sub MQTT_MessageArrived (Topic As String,";
 //BA.debugLineNum = 100;BA.debugLine="Try";
try { //BA.debugLineNum = 101;BA.debugLine="Dim strHumidityAddValue As String = StateManager";
_strhumidityaddvalue = mostCurrent._statemanager._getsetting /*String*/ (processBA,"HumidityAddValue");
 //BA.debugLineNum = 102;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic).equals("TempHumid")) { 
 //BA.debugLineNum = 104;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 105;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 107;BA.debugLine="LogEvent(status)";
_logevent(_status);
 //BA.debugLineNum = 110;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 111;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 112;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 113;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 114;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 115;BA.debugLine="StateManager.SetSetting(\"TempHumidity\",status";
mostCurrent._statemanager._setsetting /*String*/ (processBA,"TempHumidity",_status);
 //BA.debugLineNum = 116;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (processBA);
 //BA.debugLineNum = 119;BA.debugLine="If (a(3) > 3) Or (a(4) <> 0)  Then";
if (((double)(Double.parseDouble(_a[(int) (3)]))>3) || ((_a[(int) (4)]).equals(BA.NumberToString(0)) == false)) { 
 //BA.debugLineNum = 120;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 121;BA.debugLine="NotificationText = GetPerception(a(3))";
_notificationtext = _getperception(_a[(int) (3)]);
 //BA.debugLineNum = 124;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(ln";
_p = mostCurrent._dateutils._periodbetween(processBA,_lngtickstemphumid,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 125;BA.debugLine="Dim managerTempHumidityCooldownTime As Strin";
_managertemphumiditycooldowntime = mostCurrent._statemanager._getsetting /*String*/ (processBA,"TempHumidityCooldownTime");
 //BA.debugLineNum = 126;BA.debugLine="If managerTempHumidityCooldownTime = \"\" Or I";
if ((_managertemphumiditycooldowntime).equals("") || anywheresoftware.b4a.keywords.Common.IsNumber(_managertemphumiditycooldowntime)==anywheresoftware.b4a.keywords.Common.False || (_managertemphumiditycooldowntime).equals("0")) { 
 //BA.debugLineNum = 127;BA.debugLine="managerTempHumidityCooldownTime = 1";
_managertemphumiditycooldowntime = BA.NumberToString(1);
 };
 //BA.debugLineNum = 129;BA.debugLine="If IsTempHumidityNotificationOnGoing = False";
if (_istemphumiditynotificationongoing==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 130;BA.debugLine="If p.Minutes > = managerTempHumidityCooldow";
if (_p.Minutes>=(double)(Double.parseDouble(_managertemphumiditycooldowntime))) { 
 //BA.debugLineNum = 131;BA.debugLine="If a(4) = 2 Or a(4) = 6 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6))) { 
 //BA.debugLineNum = 132;BA.debugLine="CreateNotification(GetComfort(a(4)),Notif";
_createnotification(_getcomfort(_a[(int) (4)]),_notificationtext,"tempcold",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area temperature").Notify((int) (725));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 134;BA.debugLine="CreateNotification(GetComfort(a(4)),Notif";
_createnotification(_getcomfort(_a[(int) (4)]),_notificationtext,"tempcoldhumid",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area temperature").Notify((int) (725));
 }else {
 //BA.debugLineNum = 136;BA.debugLine="CreateNotification(GetComfort(a(4)),Notif";
_createnotification(_getcomfort(_a[(int) (4)]),_notificationtext,"temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area temperature").Notify((int) (725));
 };
 //BA.debugLineNum = 138;BA.debugLine="lngTicksTempHumid = DateTime.now";
_lngtickstemphumid = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 };
 }else {
 //BA.debugLineNum = 141;BA.debugLine="Dim TempHumidityPrevious() As String = Rege";
_temphumidityprevious = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",mostCurrent._statemanager._getsetting /*String*/ (processBA,"TempHumidityPrevious"));
 //BA.debugLineNum = 142;BA.debugLine="If a(4) <> TempHumidityPrevious(4) Then";
if ((_a[(int) (4)]).equals(_temphumidityprevious[(int) (4)]) == false) { 
 //BA.debugLineNum = 143;BA.debugLine="If p.Minutes > = managerTempHumidityCooldo";
if (_p.Minutes>=(double)(Double.parseDouble(_managertemphumiditycooldowntime))) { 
 //BA.debugLineNum = 144;BA.debugLine="If a(4) = 2 Or a(4) = 6 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6))) { 
 //BA.debugLineNum = 145;BA.debugLine="CreateNotification(GetComfort(a(4)),Noti";
_createnotification(_getcomfort(_a[(int) (4)]),_notificationtext,"tempcold",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area temperature").Notify((int) (725));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 147;BA.debugLine="CreateNotification(GetComfort(a(4)),Noti";
_createnotification(_getcomfort(_a[(int) (4)]),_notificationtext,"tempcoldhumid",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area temperature").Notify((int) (725));
 }else {
 //BA.debugLineNum = 149;BA.debugLine="CreateNotification(GetComfort(a(4)),Noti";
_createnotification(_getcomfort(_a[(int) (4)]),_notificationtext,"temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area temperature").Notify((int) (725));
 };
 //BA.debugLineNum = 151;BA.debugLine="lngTicksTempHumid = DateTime.now";
_lngtickstemphumid = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 };
 }else if((_a[(int) (3)]).equals(_temphumidityprevious[(int) (3)]) == false) { 
 //BA.debugLineNum = 154;BA.debugLine="If p.Minutes > = managerTempHumidityCooldo";
if (_p.Minutes>=(double)(Double.parseDouble(_managertemphumiditycooldowntime))) { 
 //BA.debugLineNum = 155;BA.debugLine="If a(4) = 2 Or a(4) = 6 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6))) { 
 //BA.debugLineNum = 156;BA.debugLine="CreateNotification(\"* \" & GetComfort(a(4";
_createnotification("* "+_getcomfort(_a[(int) (4)]),_notificationtext,"tempcold",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area temperature").Notify((int) (725));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 158;BA.debugLine="CreateNotification(\"* \" & GetComfort(a(4";
_createnotification("* "+_getcomfort(_a[(int) (4)]),_notificationtext,"tempcoldhumid",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area temperature").Notify((int) (725));
 }else {
 //BA.debugLineNum = 160;BA.debugLine="CreateNotification(\"* \" & GetComfort(a(4";
_createnotification("* "+_getcomfort(_a[(int) (4)]),_notificationtext,"temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area temperature").Notify((int) (725));
 };
 //BA.debugLineNum = 162;BA.debugLine="lngTicksTempHumid = DateTime.now";
_lngtickstemphumid = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 };
 };
 };
 //BA.debugLineNum = 166;BA.debugLine="StateManager.SetSetting(\"TempHumidityPreviou";
mostCurrent._statemanager._setsetting /*String*/ (processBA,"TempHumidityPrevious",_status);
 //BA.debugLineNum = 167;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (processBA);
 }else {
 //BA.debugLineNum = 169;BA.debugLine="lngTicksTempHumid = DateTime.now";
_lngtickstemphumid = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 170;BA.debugLine="IsTempHumidityNotificationOnGoing = False";
_istemphumiditynotificationongoing = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 171;BA.debugLine="Notification1.Cancel(725)";
_notification1.Cancel((int) (725));
 };
 };
 };
 //BA.debugLineNum = 176;BA.debugLine="If strHumidityAddValue = \"\" Then";
if ((_strhumidityaddvalue).equals("")) { 
 //BA.debugLineNum = 177;BA.debugLine="strHumidityAddValue = \"0\"";
_strhumidityaddvalue = "0";
 };
 //BA.debugLineNum = 179;BA.debugLine="MQTT.Publish(\"HumidityAddValue\", bc.StringToByt";
_mqtt.Publish("HumidityAddValue",_bc.StringToBytes(_strhumidityaddvalue,"utf8"));
 }else if((_topic).equals("MQ7")) { 
 //BA.debugLineNum = 181;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 182;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 183;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 184;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 185;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 186;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 187;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 188;BA.debugLine="StateManager.SetSetting(\"AirQuality\",status)";
mostCurrent._statemanager._setsetting /*String*/ (processBA,"AirQuality",_status);
 //BA.debugLineNum = 189;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (processBA);
 //BA.debugLineNum = 191;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 192;BA.debugLine="NotificationText = GetAirQuality(a(0)) & \", a";
_notificationtext = _getairquality((int)(Double.parseDouble(_a[(int) (0)])))+", at "+_a[(int) (0)]+" ppm";
 //BA.debugLineNum = 193;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 194;BA.debugLine="If IsAirQualityNotificationOnGoing = False T";
if (_isairqualitynotificationongoing==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 195;BA.debugLine="CreateNotification(\"Living Area Air Quality";
_createnotification("Living Area Air Quality",_notificationtext,"co",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area carbon monoxide").Notify((int) (726));
 }else {
 //BA.debugLineNum = 197;BA.debugLine="CreateNotification(\"* Living Area Air Quali";
_createnotification("* Living Area Air Quality",_notificationtext,"co",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area carbon monoxide").Notify((int) (726));
 };
 }else {
 //BA.debugLineNum = 200;BA.debugLine="IsAirQualityNotificationOnGoing = False";
_isairqualitynotificationongoing = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 201;BA.debugLine="Notification1.Cancel(726)";
_notification1.Cancel((int) (726));
 };
 };
 };
 }else if((_topic).equals("MQ7Basement")) { 
 //BA.debugLineNum = 206;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 207;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 208;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 209;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 210;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 211;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 212;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 213;BA.debugLine="StateManager.SetSetting(\"AirQualityBasement\",";
mostCurrent._statemanager._setsetting /*String*/ (processBA,"AirQualityBasement",_status);
 //BA.debugLineNum = 214;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (processBA);
 //BA.debugLineNum = 216;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 217;BA.debugLine="NotificationText = GetAirQuality(a(0)) & \", a";
_notificationtext = _getairquality((int)(Double.parseDouble(_a[(int) (0)])))+", at "+_a[(int) (0)]+" ppm";
 //BA.debugLineNum = 218;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 219;BA.debugLine="If IsAirQualityNotificationOnGoingBasement =";
if (_isairqualitynotificationongoingbasement==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 220;BA.debugLine="CreateNotification(\"Basement Air Quality\",N";
_createnotification("Basement Air Quality",_notificationtext,"cobasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement carbon monoxide").Notify((int) (727));
 }else {
 //BA.debugLineNum = 222;BA.debugLine="CreateNotification(\"* Basement Air Quality\"";
_createnotification("* Basement Air Quality",_notificationtext,"cobasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement carbon monoxide").Notify((int) (727));
 };
 }else {
 //BA.debugLineNum = 225;BA.debugLine="IsAirQualityNotificationOnGoingBasement = Fa";
_isairqualitynotificationongoingbasement = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 226;BA.debugLine="Notification1.Cancel(727)";
_notification1.Cancel((int) (727));
 };
 };
 };
 //BA.debugLineNum = 232;BA.debugLine="Dim FileNameToday As String";
_filenametoday = "";
 //BA.debugLineNum = 233;BA.debugLine="Dim FileNameYesterday As String";
_filenameyesterday = "";
 //BA.debugLineNum = 234;BA.debugLine="Dim Now As Long";
_now = 0L;
 //BA.debugLineNum = 235;BA.debugLine="Dim Month As Int";
_month = 0;
 //BA.debugLineNum = 236;BA.debugLine="Dim Day As Int";
_day = 0;
 //BA.debugLineNum = 237;BA.debugLine="Dim Year As Int";
_year = 0;
 //BA.debugLineNum = 238;BA.debugLine="Dim Yesterday As Long";
_yesterday = 0L;
 //BA.debugLineNum = 239;BA.debugLine="Dim MonthYesterday As Int";
_monthyesterday = 0;
 //BA.debugLineNum = 240;BA.debugLine="Dim DayYesterday As Int";
_dayyesterday = 0;
 //BA.debugLineNum = 241;BA.debugLine="Dim YearYesterday As Int";
_yearyesterday = 0;
 //BA.debugLineNum = 243;BA.debugLine="Now = DateTime.Now";
_now = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 244;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 245;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 246;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 248;BA.debugLine="Yesterday = DateTime.add(DateTime.Now, 0, 0, -1";
_yesterday = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1));
 //BA.debugLineNum = 249;BA.debugLine="MonthYesterday = DateTime.GetMonth(Yesterday)";
_monthyesterday = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_yesterday);
 //BA.debugLineNum = 250;BA.debugLine="DayYesterday = DateTime.GetDayOfMonth (Yesterda";
_dayyesterday = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_yesterday);
 //BA.debugLineNum = 251;BA.debugLine="YearYesterday = DateTime.GetYear(Yesterday)";
_yearyesterday = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_yesterday);
 //BA.debugLineNum = 253;BA.debugLine="FileNameToday = \"LivingRoomTempHumid_\" & Year &";
_filenametoday = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 //BA.debugLineNum = 254;BA.debugLine="FileNameYesterday = \"LivingRoomTempHumid_\" & Ye";
_filenameyesterday = "LivingRoomTempHumid_"+BA.NumberToString(_yearyesterday)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_monthyesterday,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_dayyesterday,(int) (2),(int) (0))+".log";
 //BA.debugLineNum = 256;BA.debugLine="Dim flist As List = WildCardFilesList2(File.Dir";
_flist = new anywheresoftware.b4a.objects.collections.List();
_flist = _wildcardfileslist2(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"LivingRoomTempHumid_*.log",anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 258;BA.debugLine="For i = 0 To flist.Size -1";
{
final int step141 = 1;
final int limit141 = (int) (_flist.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit141 ;_i = _i + step141 ) {
 //BA.debugLineNum = 259;BA.debugLine="Dim FileName As String = flist.Get(i)";
_filename = BA.ObjectToString(_flist.Get(_i));
 //BA.debugLineNum = 260;BA.debugLine="If FileName <> FileNameToday Then";
if ((_filename).equals(_filenametoday) == false) { 
 //BA.debugLineNum = 261;BA.debugLine="If FileName <> FileNameYesterday Then";
if ((_filename).equals(_filenameyesterday) == false) { 
 //BA.debugLineNum = 262;BA.debugLine="File.Delete(File.DirRootExternal,FileName)";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_filename);
 };
 };
 }
};
 }else if((_topic).equals("TempHumidBasement")) { 
 //BA.debugLineNum = 269;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 270;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 272;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 273;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 274;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 275;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 276;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 277;BA.debugLine="StateManager.SetSetting(\"TempHumidityBasement";
mostCurrent._statemanager._setsetting /*String*/ (processBA,"TempHumidityBasement",_status);
 //BA.debugLineNum = 278;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (processBA);
 //BA.debugLineNum = 283;BA.debugLine="If (a(3) > 3) Or ((a(4) <> 0) And (a(4) <> 2)";
if (((double)(Double.parseDouble(_a[(int) (3)]))>3) || (((_a[(int) (4)]).equals(BA.NumberToString(0)) == false) && ((_a[(int) (4)]).equals(BA.NumberToString(2)) == false))) { 
 //BA.debugLineNum = 284;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 285;BA.debugLine="NotificationText = GetPerception(a(3))";
_notificationtext = _getperception(_a[(int) (3)]);
 //BA.debugLineNum = 287;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(ln";
_p = mostCurrent._dateutils._periodbetween(processBA,_lngtickstemphumidbasement,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 288;BA.debugLine="Dim managerTempHumidityCooldownTime As Strin";
_managertemphumiditycooldowntime = mostCurrent._statemanager._getsetting /*String*/ (processBA,"TempHumidityCooldownTimeBasement");
 //BA.debugLineNum = 289;BA.debugLine="If managerTempHumidityCooldownTime = \"\" Or I";
if ((_managertemphumiditycooldowntime).equals("") || anywheresoftware.b4a.keywords.Common.IsNumber(_managertemphumiditycooldowntime)==anywheresoftware.b4a.keywords.Common.False || (_managertemphumiditycooldowntime).equals("0")) { 
 //BA.debugLineNum = 290;BA.debugLine="managerTempHumidityCooldownTime = 1";
_managertemphumiditycooldowntime = BA.NumberToString(1);
 };
 //BA.debugLineNum = 292;BA.debugLine="If IsTempHumidityNotificationOnGoingBasement";
if (_istemphumiditynotificationongoingbasement==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 293;BA.debugLine="If p.Minutes > = managerTempHumidityCooldow";
if (_p.Minutes>=(double)(Double.parseDouble(_managertemphumiditycooldowntime))) { 
 //BA.debugLineNum = 294;BA.debugLine="If a(4) = 2 Or a(4) = 6 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6))) { 
 //BA.debugLineNum = 295;BA.debugLine="CreateNotification(GetComfort(a(4)).Repla";
_createnotification(_getcomfort(_a[(int) (4)]).replace("Home","Basement"),_notificationtext,"tempcoldbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement temperature").Notify((int) (728));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 297;BA.debugLine="CreateNotification(GetComfort(a(4)).Repla";
_createnotification(_getcomfort(_a[(int) (4)]).replace("Home","Basement"),_notificationtext,"tempcoldhumidbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement temperature").Notify((int) (728));
 }else {
 //BA.debugLineNum = 299;BA.debugLine="CreateNotification(GetComfort(a(4)).Repla";
_createnotification(_getcomfort(_a[(int) (4)]).replace("Home","Basement"),_notificationtext,"tempbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement temperature").Notify((int) (728));
 };
 //BA.debugLineNum = 301;BA.debugLine="lngTicksTempHumidBasement = DateTime.now";
_lngtickstemphumidbasement = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 };
 }else {
 //BA.debugLineNum = 304;BA.debugLine="Dim TempHumidityBasementPrevious() As Strin";
_temphumiditybasementprevious = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",mostCurrent._statemanager._getsetting /*String*/ (processBA,"TempHumidityBasementPrevious"));
 //BA.debugLineNum = 305;BA.debugLine="If a(4) <> TempHumidityBasementPrevious(4)";
if ((_a[(int) (4)]).equals(_temphumiditybasementprevious[(int) (4)]) == false) { 
 //BA.debugLineNum = 306;BA.debugLine="If p.Minutes > = managerTempHumidityCooldo";
if (_p.Minutes>=(double)(Double.parseDouble(_managertemphumiditycooldowntime))) { 
 //BA.debugLineNum = 307;BA.debugLine="If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6)) || (_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 308;BA.debugLine="CreateNotification(GetComfort(a(4)).Repl";
_createnotification(_getcomfort(_a[(int) (4)]).replace("Home","Basement"),_notificationtext,"tempcoldbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement temperature").Notify((int) (728));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 310;BA.debugLine="CreateNotification(GetComfort(a(4)).Repl";
_createnotification(_getcomfort(_a[(int) (4)]).replace("Home","Basement"),_notificationtext,"tempcoldhumidbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement temperature").Notify((int) (728));
 }else {
 //BA.debugLineNum = 312;BA.debugLine="CreateNotification(GetComfort(a(4)).Repl";
_createnotification(_getcomfort(_a[(int) (4)]).replace("Home","Basement"),_notificationtext,"tempbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement temperature").Notify((int) (728));
 };
 //BA.debugLineNum = 314;BA.debugLine="lngTicksTempHumidBasement = DateTime.now";
_lngtickstemphumidbasement = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 };
 }else if((_a[(int) (3)]).equals(_temphumiditybasementprevious[(int) (3)]) == false) { 
 //BA.debugLineNum = 317;BA.debugLine="If p.Minutes > = managerTempHumidityCooldo";
if (_p.Minutes>=(double)(Double.parseDouble(_managertemphumiditycooldowntime))) { 
 //BA.debugLineNum = 318;BA.debugLine="If a(4) = 2 Or a(4) = 6 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6))) { 
 //BA.debugLineNum = 319;BA.debugLine="CreateNotification(\"* \" & GetComfort(a(4";
_createnotification("* "+_getcomfort(_a[(int) (4)]).replace("Home","Basement"),_notificationtext,"tempcoldbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement temperature").Notify((int) (728));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 321;BA.debugLine="CreateNotification(\"* \" & GetComfort(a(4";
_createnotification("* "+_getcomfort(_a[(int) (4)]).replace("Home","Basement"),_notificationtext,"tempcoldhumidbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement temperature").Notify((int) (728));
 }else {
 //BA.debugLineNum = 323;BA.debugLine="CreateNotification(\"* \" & GetComfort(a(4";
_createnotification("* "+_getcomfort(_a[(int) (4)]).replace("Home","Basement"),_notificationtext,"tempbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement temperature").Notify((int) (728));
 };
 //BA.debugLineNum = 325;BA.debugLine="lngTicksTempHumidBasement = DateTime.now";
_lngtickstemphumidbasement = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 };
 };
 };
 //BA.debugLineNum = 329;BA.debugLine="StateManager.SetSetting(\"TempHumidityBasemen";
mostCurrent._statemanager._setsetting /*String*/ (processBA,"TempHumidityBasementPrevious",_status);
 //BA.debugLineNum = 330;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (processBA);
 }else {
 //BA.debugLineNum = 332;BA.debugLine="lngTicksTempHumidBasement = DateTime.now";
_lngtickstemphumidbasement = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 333;BA.debugLine="IsTempHumidityNotificationOnGoingBasement =";
_istemphumiditynotificationongoingbasement = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 334;BA.debugLine="Notification1.Cancel(728)";
_notification1.Cancel((int) (728));
 };
 };
 };
 //BA.debugLineNum = 338;BA.debugLine="If strHumidityAddValue = \"\" Then";
if ((_strhumidityaddvalue).equals("")) { 
 //BA.debugLineNum = 339;BA.debugLine="strHumidityAddValue = \"0\"";
_strhumidityaddvalue = "0";
 };
 //BA.debugLineNum = 341;BA.debugLine="MQTT.Publish(\"HumidityAddValue\", bc.StringToByt";
_mqtt.Publish("HumidityAddValue",_bc.StringToBytes(_strhumidityaddvalue,"utf8"));
 };
 //BA.debugLineNum = 344;BA.debugLine="Dim managerSensorNotRespondingTime As String = S";
_managersensornotrespondingtime = mostCurrent._statemanager._getsetting /*String*/ (processBA,"SensorNotRespondingTime");
 //BA.debugLineNum = 345;BA.debugLine="If managerSensorNotRespondingTime = \"\" Or IsNumb";
if ((_managersensornotrespondingtime).equals("") || anywheresoftware.b4a.keywords.Common.IsNumber(_managersensornotrespondingtime)==anywheresoftware.b4a.keywords.Common.False || (_managersensornotrespondingtime).equals("0")) { 
 //BA.debugLineNum = 346;BA.debugLine="managerSensorNotRespondingTime = 1";
_managersensornotrespondingtime = BA.NumberToString(1);
 };
 //BA.debugLineNum = 349;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 350;BA.debugLine="Dim sensorInTrouble As String";
_sensorintrouble = "";
 //BA.debugLineNum = 351;BA.debugLine="sensorInTrouble = \"TempHumidityBasement\"";
_sensorintrouble = "TempHumidityBasement";
 //BA.debugLineNum = 352;BA.debugLine="status = StateManager.GetSetting(\"TempHumidityBa";
_status = mostCurrent._statemanager._getsetting /*String*/ (processBA,"TempHumidityBasement");
 //BA.debugLineNum = 353;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 354;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 356;BA.debugLine="Dim n As Notification 'ignore";
_n = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 358;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 359;BA.debugLine="If a(7) = \"\" Then";
if ((_a[(int) (7)]).equals("")) { 
 //BA.debugLineNum = 360;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 361;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 362;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 363;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 365;BA.debugLine="If a(8).Contains(\"|24:\") Then";
if (_a[(int) (8)].contains("|24:")) { 
 //BA.debugLineNum = 366;BA.debugLine="a(8) = a(8).Replace(\"|24:\",\"|00:\")";
_a[(int) (8)] = _a[(int) (8)].replace("|24:","|00:");
 //BA.debugLineNum = 367;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 368;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 369;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 370;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 373;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 374;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) & \"";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 375;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 376;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 377;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngTi";
_p = mostCurrent._dateutils._periodbetween(processBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 378;BA.debugLine="If p.Minutes <> 59 And p.Minutes > = managerSen";
if (_p.Minutes!=59 && _p.Minutes>=(double)(Double.parseDouble(_managersensornotrespondingtime)) && _p.Days<=1) { 
 //BA.debugLineNum = 379;BA.debugLine="If IsOldTempHumidityNotificationOnGoingBasemen";
if (_isoldtemphumiditynotificationongoingbasement==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 380;BA.debugLine="CreateNotification(\"Basement DHT22 sensor is";
_createnotification("Basement DHT22 sensor is not responding","Temperature and humidity data is "+BA.NumberToString(_p.Minutes)+" minutes old","sensorbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement DHT22 sensor issue").Notify((int) (730));
 //BA.debugLineNum = 381;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringTo";
_mqtt.Publish("TempHumidBasement",_bc.StringToBytes("Sensor is not working","utf8"));
 };
 }else {
 //BA.debugLineNum = 384;BA.debugLine="IsOldTempHumidityNotificationOnGoingBasement =";
_isoldtemphumiditynotificationongoingbasement = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 385;BA.debugLine="n.Cancel(730)";
_n.Cancel((int) (730));
 };
 };
 //BA.debugLineNum = 389;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 390;BA.debugLine="sensorInTrouble = \"TempHumidity\"";
_sensorintrouble = "TempHumidity";
 //BA.debugLineNum = 391;BA.debugLine="status = StateManager.GetSetting(\"TempHumidity\")";
_status = mostCurrent._statemanager._getsetting /*String*/ (processBA,"TempHumidity");
 //BA.debugLineNum = 392;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 393;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 395;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 396;BA.debugLine="If a(7) = \"\" Then";
if ((_a[(int) (7)]).equals("")) { 
 //BA.debugLineNum = 397;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 398;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 399;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 400;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 402;BA.debugLine="If a(8).Contains(\"|24:\") Then";
if (_a[(int) (8)].contains("|24:")) { 
 //BA.debugLineNum = 403;BA.debugLine="a(8) = a(8).Replace(\"|24:\",\"|00:\")";
_a[(int) (8)] = _a[(int) (8)].replace("|24:","|00:");
 //BA.debugLineNum = 404;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 405;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 406;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 407;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 410;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 412;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) & \"";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 413;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 414;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 415;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngTi";
_p = mostCurrent._dateutils._periodbetween(processBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 416;BA.debugLine="If p.Minutes <> 59 And p.Minutes > = managerSen";
if (_p.Minutes!=59 && _p.Minutes>=(double)(Double.parseDouble(_managersensornotrespondingtime)) && _p.Days<=1) { 
 //BA.debugLineNum = 417;BA.debugLine="If IsOldTempHumidityNotificationOnGoing = Fals";
if (_isoldtemphumiditynotificationongoing==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 418;BA.debugLine="CreateNotification(\"Living area DHT22 sensor";
_createnotification("Living area DHT22 sensor is not responding","Temperature and humidity data is "+BA.NumberToString(_p.Minutes)+" minutes old","sensor",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area DHT22 sensor issue").Notify((int) (729));
 //BA.debugLineNum = 419;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"S";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Sensor is not working","utf8"));
 };
 }else {
 //BA.debugLineNum = 422;BA.debugLine="IsOldTempHumidityNotificationOnGoing = False";
_isoldtemphumiditynotificationongoing = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 423;BA.debugLine="n.Cancel(729)";
_n.Cancel((int) (729));
 };
 };
 //BA.debugLineNum = 427;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 428;BA.debugLine="sensorInTrouble = \"AirQuality\"";
_sensorintrouble = "AirQuality";
 //BA.debugLineNum = 429;BA.debugLine="status = StateManager.GetSetting(\"AirQuality\")";
_status = mostCurrent._statemanager._getsetting /*String*/ (processBA,"AirQuality");
 //BA.debugLineNum = 430;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 431;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 433;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 434;BA.debugLine="If a(1) = \"\" Then";
if ((_a[(int) (1)]).equals("")) { 
 //BA.debugLineNum = 435;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 436;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 437;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 438;BA.debugLine="a(1) = DateTime.Date(Tomorrow)";
_a[(int) (1)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 440;BA.debugLine="If a(2).Contains(\"|24:\") Then";
if (_a[(int) (2)].contains("|24:")) { 
 //BA.debugLineNum = 441;BA.debugLine="a(2) = a(2).Replace(\"|24:\",\"|00:\")";
_a[(int) (2)] = _a[(int) (2)].replace("|24:","|00:");
 //BA.debugLineNum = 442;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 443;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 444;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 445;BA.debugLine="a(2) = DateTime.Date(Tomorrow)";
_a[(int) (2)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 448;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 449;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) & \"";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 450;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 451;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 452;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngTi";
_p = mostCurrent._dateutils._periodbetween(processBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 453;BA.debugLine="If p.Minutes <> 59 And p.Minutes > = managerSen";
if (_p.Minutes!=59 && _p.Minutes>=(double)(Double.parseDouble(_managersensornotrespondingtime)) && _p.Days<=1) { 
 //BA.debugLineNum = 454;BA.debugLine="If IsOldAirQualityNotificationOnGoing = False";
if (_isoldairqualitynotificationongoing==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 455;BA.debugLine="CreateNotification(\"Living area carbon monoxi";
_createnotification("Living area carbon monoxide sensor is not responding","Air quality data is "+BA.NumberToString(_p.Minutes)+" minutes old","sensor",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area CO sensor issue").Notify((int) (731));
 //BA.debugLineNum = 456;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Sensor";
_mqtt.Publish("MQ7",_bc.StringToBytes("Sensor is not working","utf8"));
 };
 }else {
 //BA.debugLineNum = 459;BA.debugLine="IsOldAirQualityNotificationOnGoing = False";
_isoldairqualitynotificationongoing = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 460;BA.debugLine="n.Cancel(731)";
_n.Cancel((int) (731));
 };
 };
 //BA.debugLineNum = 464;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 465;BA.debugLine="sensorInTrouble = \"AirQualityBasement\"";
_sensorintrouble = "AirQualityBasement";
 //BA.debugLineNum = 466;BA.debugLine="status = StateManager.GetSetting(\"AirQualityBase";
_status = mostCurrent._statemanager._getsetting /*String*/ (processBA,"AirQualityBasement");
 //BA.debugLineNum = 467;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 468;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 470;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 471;BA.debugLine="If a(1) = \"\" Then";
if ((_a[(int) (1)]).equals("")) { 
 //BA.debugLineNum = 472;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 473;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 474;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 475;BA.debugLine="a(1) = DateTime.Date(Tomorrow)";
_a[(int) (1)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 477;BA.debugLine="If a(2).Contains(\"|24:\") Then";
if (_a[(int) (2)].contains("|24:")) { 
 //BA.debugLineNum = 478;BA.debugLine="a(2) = a(2).Replace(\"|24:\",\"|00:\")";
_a[(int) (2)] = _a[(int) (2)].replace("|24:","|00:");
 //BA.debugLineNum = 479;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 480;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 481;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 482;BA.debugLine="a(2) = DateTime.Date(Tomorrow)";
_a[(int) (2)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 485;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 486;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) & \"";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 487;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 488;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 489;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngTi";
_p = mostCurrent._dateutils._periodbetween(processBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 490;BA.debugLine="If p.Minutes <> 59 And p.Minutes > = managerSen";
if (_p.Minutes!=59 && _p.Minutes>=(double)(Double.parseDouble(_managersensornotrespondingtime)) && _p.Days<=1) { 
 //BA.debugLineNum = 491;BA.debugLine="If IsOldAirQualityNotificationOnGoingBasement";
if (_isoldairqualitynotificationongoingbasement==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 492;BA.debugLine="CreateNotification(\"Basement carbon monoxide";
_createnotification("Basement carbon monoxide sensor is not responding","Air quality data is "+BA.NumberToString(_p.Minutes)+" minutes old","sensorbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement CO sensor issue").Notify((int) (732));
 //BA.debugLineNum = 493;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes(";
_mqtt.Publish("MQ7Basement",_bc.StringToBytes("Sensor is not working","utf8"));
 };
 }else {
 //BA.debugLineNum = 496;BA.debugLine="IsOldAirQualityNotificationOnGoingBasement = F";
_isoldairqualitynotificationongoingbasement = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 497;BA.debugLine="n.Cancel(732)";
_n.Cancel((int) (732));
 };
 };
 } 
       catch (Exception e361) {
			processBA.setLastException(e361); //BA.debugLineNum = 502;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("15833107",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)),0);
 //BA.debugLineNum = 504;BA.debugLine="Select sensorInTrouble";
switch (BA.switchObjectToInt(_sensorintrouble,"TempHumidityBasement","TempHumidity","AirQuality","AirQualityBasement")) {
case 0: {
 //BA.debugLineNum = 506;BA.debugLine="If IsOldTempHumidityNotificationOnGoingBasemen";
if (_isoldtemphumiditynotificationongoingbasement==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 507;BA.debugLine="CreateNotification(\"Basement DHT22 sensor exc";
_createnotification("Basement DHT22 sensor exception",anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage(),"sensorbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement DHT22 sensor issue").Notify((int) (730));
 //BA.debugLineNum = 508;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringTo";
_mqtt.Publish("TempHumidBasement",_bc.StringToBytes("TempHumidityBasement Exception: "+anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage(),"utf8"));
 };
 break; }
case 1: {
 //BA.debugLineNum = 511;BA.debugLine="If IsOldTempHumidityNotificationOnGoing = Fals";
if (_isoldtemphumiditynotificationongoing==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 512;BA.debugLine="CreateNotification(\"Living area DHT22 sensor";
_createnotification("Living area DHT22 sensor exception",anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage(),"sensor",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area DHT22 sensor issue").Notify((int) (729));
 //BA.debugLineNum = 513;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"T";
_mqtt.Publish("TempHumid",_bc.StringToBytes("TempHumidity Exception: "+anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage(),"utf8"));
 };
 break; }
case 2: {
 //BA.debugLineNum = 516;BA.debugLine="If IsOldAirQualityNotificationOnGoing = False";
if (_isoldairqualitynotificationongoing==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 517;BA.debugLine="CreateNotification(\"Living area carbon monoxi";
_createnotification("Living area carbon monoxide sensor exception",anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage(),"sensor",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area CO sensor issue").Notify((int) (731));
 //BA.debugLineNum = 518;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"AirQual";
_mqtt.Publish("MQ7",_bc.StringToBytes("AirQuality Exception: "+anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage(),"utf8"));
 };
 break; }
case 3: {
 //BA.debugLineNum = 521;BA.debugLine="If IsOldAirQualityNotificationOnGoingBasement";
if (_isoldairqualitynotificationongoingbasement==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 522;BA.debugLine="CreateNotification(\"Basement carbon monoxide";
_createnotification("Basement carbon monoxide sensor exception",anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage(),"sensorbasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement CO sensor issue").Notify((int) (732));
 //BA.debugLineNum = 523;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes(";
_mqtt.Publish("MQ7Basement",_bc.StringToBytes("AirQualityBasement Exception: "+anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage(),"utf8"));
 };
 break; }
}
;
 };
 //BA.debugLineNum = 528;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 18;BA.debugLine="Public IsOldTempHumidityNotificationOnGoingBaseme";
_isoldtemphumiditynotificationongoingbasement = false;
 //BA.debugLineNum = 19;BA.debugLine="Public IsOldTempHumidityNotificationOnGoing As Bo";
_isoldtemphumiditynotificationongoing = false;
 //BA.debugLineNum = 20;BA.debugLine="Public IsOldAirQualityNotificationOnGoing As Bool";
_isoldairqualitynotificationongoing = false;
 //BA.debugLineNum = 21;BA.debugLine="Public IsOldAirQualityNotificationOnGoingBasement";
_isoldairqualitynotificationongoingbasement = false;
 //BA.debugLineNum = 22;BA.debugLine="Public lngTicks As Long";
_lngticks = 0L;
 //BA.debugLineNum = 23;BA.debugLine="Public lngTicksTempHumid As Long";
_lngtickstemphumid = 0L;
 //BA.debugLineNum = 24;BA.debugLine="Public lngTicksTempHumidBasement As Long";
_lngtickstemphumidbasement = 0L;
 //BA.debugLineNum = 25;BA.debugLine="Private bc As ByteConverter";
_bc = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 29;BA.debugLine="Notification1.Initialize2(Notification1.IMPORTANC";
_notification1.Initialize2(_notification1.IMPORTANCE_DEFAULT);
 //BA.debugLineNum = 30;BA.debugLine="Service.AutomaticForegroundMode = Service.AUTOMAT";
mostCurrent._service.AutomaticForegroundMode = mostCurrent._service.AUTOMATIC_FOREGROUND_ALWAYS;
 //BA.debugLineNum = 31;BA.debugLine="CreateNotification(\"Living area temperature\",\"Liv";
_createnotification("Living area temperature","Living area temperature","temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area temperature");
 //BA.debugLineNum = 32;BA.debugLine="CreateNotification(\"Living area carbon monoxide\",";
_createnotification("Living area carbon monoxide","Living area carbon monoxide","co",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area carbon monoxide");
 //BA.debugLineNum = 33;BA.debugLine="CreateNotification(\"Basement temperature\",\"Baseme";
_createnotification("Basement temperature","Basement temperature","temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement temperature");
 //BA.debugLineNum = 34;BA.debugLine="CreateNotification(\"Basement carbon monoxide\",\"Ba";
_createnotification("Basement carbon monoxide","Basement carbon monoxide","cobasement",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement carbon monoxide");
 //BA.debugLineNum = 35;BA.debugLine="CreateNotification(\"Basement DHT22 sensor issue\",";
_createnotification("Basement DHT22 sensor issue","Basement DHT22 sensor issue","sensor",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement DHT22 sensor issue");
 //BA.debugLineNum = 36;BA.debugLine="CreateNotification(\"Living area DHT22 sensor issu";
_createnotification("Living area DHT22 sensor issue","Living area DHT22 sensor issue","sensor",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area DHT22 sensor issue");
 //BA.debugLineNum = 37;BA.debugLine="CreateNotification(\"Living area CO sensor issue\",";
_createnotification("Living area CO sensor issue","Living area CO sensor issue","sensor",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Living area CO sensor issue");
 //BA.debugLineNum = 38;BA.debugLine="CreateNotification(\"Basement CO sensor issue\",\"Ba";
_createnotification("Basement CO sensor issue","Basement CO sensor issue","sensor",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Basement CO sensor issue");
 //BA.debugLineNum = 40;BA.debugLine="Notification1.Icon = \"icon\"";
_notification1.setIcon("icon");
 //BA.debugLineNum = 41;BA.debugLine="Notification1.Vibrate = False";
_notification1.setVibrate(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 42;BA.debugLine="Notification1.AutoCancel = False";
_notification1.setAutoCancel(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 43;BA.debugLine="Notification1.Sound = False";
_notification1.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 44;BA.debugLine="Notification1.SetInfo(\"Smart Home Monitor\",\"Servi";
_notification1.SetInfoNew(processBA,BA.ObjectToCharSequence("Smart Home Monitor"),BA.ObjectToCharSequence("Service is running. Tap to open."),(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 45;BA.debugLine="Service.AutomaticForegroundNotification = Notific";
mostCurrent._service.AutomaticForegroundNotification = (android.app.Notification)(_notification1.getObject());
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 53;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 55;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 48;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 49;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 //BA.debugLineNum = 51;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 690;BA.debugLine="Sub WildCardFilesList2(FilesPath As String, WildCa";
 //BA.debugLineNum = 691;BA.debugLine="If File.IsDirectory(\"\", FilesPath) Then";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory("",_filespath)) { 
 //BA.debugLineNum = 692;BA.debugLine="Dim FilesFound As List = File.ListFiles(FilesPat";
_filesfound = new anywheresoftware.b4a.objects.collections.List();
_filesfound = anywheresoftware.b4a.keywords.Common.File.ListFiles(_filespath);
 //BA.debugLineNum = 693;BA.debugLine="Dim GetCards() As String = Regex.Split(\",\", Wild";
_getcards = anywheresoftware.b4a.keywords.Common.Regex.Split(",",_wildcards);
 //BA.debugLineNum = 694;BA.debugLine="Dim FilteredFiles As List : FilteredFiles.Initia";
_filteredfiles = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 694;BA.debugLine="Dim FilteredFiles As List : FilteredFiles.Initia";
_filteredfiles.Initialize();
 //BA.debugLineNum = 695;BA.debugLine="For i = 0 To FilesFound.Size -1";
{
final int step6 = 1;
final int limit6 = (int) (_filesfound.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit6 ;_i = _i + step6 ) {
 //BA.debugLineNum = 696;BA.debugLine="For l = 0 To GetCards.Length -1";
{
final int step7 = 1;
final int limit7 = (int) (_getcards.length-1);
_l = (int) (0) ;
for (;_l <= limit7 ;_l = _l + step7 ) {
 //BA.debugLineNum = 697;BA.debugLine="Dim TestItem As String = FilesFound.Get(i)";
_testitem = BA.ObjectToString(_filesfound.Get(_i));
 //BA.debugLineNum = 698;BA.debugLine="Dim mask As String = GetCards(l).Trim";
_mask = _getcards[_l].trim();
 //BA.debugLineNum = 699;BA.debugLine="Dim pattern As String = \"^\"&mask.Replace(\".\",\"";
_pattern = "^"+_mask.replace(".","\\.").replace("*",".+").replace("?",".")+"$";
 //BA.debugLineNum = 700;BA.debugLine="If Regex.IsMatch(pattern,TestItem) = True Then";
if (anywheresoftware.b4a.keywords.Common.Regex.IsMatch(_pattern,_testitem)==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 701;BA.debugLine="FilteredFiles.Add(TestItem.Trim)";
_filteredfiles.Add((Object)(_testitem.trim()));
 };
 }
};
 }
};
 //BA.debugLineNum = 705;BA.debugLine="If Sorted Then";
if (_sorted) { 
 //BA.debugLineNum = 706;BA.debugLine="FilteredFiles.SortCaseInsensitive(Ascending)";
_filteredfiles.SortCaseInsensitive(_ascending);
 };
 //BA.debugLineNum = 708;BA.debugLine="Return FilteredFiles";
if (true) return _filteredfiles;
 }else {
 //BA.debugLineNum = 710;BA.debugLine="ToastMessageShow(\"You must pass a valid Director";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You must pass a valid Directory."),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 712;BA.debugLine="End Sub";
return null;
}
}
