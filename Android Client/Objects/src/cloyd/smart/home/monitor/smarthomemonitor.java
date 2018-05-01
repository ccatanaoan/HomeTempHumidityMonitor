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
public static long _lngticks = 0L;
public static long _lngtickstemphumid = 0L;
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
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
 //BA.debugLineNum = 152;BA.debugLine="Private Sub CreateNotification(Title As String, Co";
 //BA.debugLineNum = 154;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 155;BA.debugLine="If p.SdkVersion >= 21 Then";
if (_p.getSdkVersion()>=21) { 
 //BA.debugLineNum = 156;BA.debugLine="Dim nb As NotificationBuilder";
_nb = new barxdroid.NotificationBuilder.NotificationBuilder();
 //BA.debugLineNum = 157;BA.debugLine="nb.Initialize";
_nb.Initialize(processBA);
 //BA.debugLineNum = 158;BA.debugLine="nb.DefaultSound = Sound";
_nb.setDefaultSound(_sound);
 //BA.debugLineNum = 159;BA.debugLine="nb.DefaultVibrate = Vibrate";
_nb.setDefaultVibrate(_vibrate);
 //BA.debugLineNum = 160;BA.debugLine="nb.ContentTitle = Title";
_nb.setContentTitle(_title);
 //BA.debugLineNum = 161;BA.debugLine="nb.ContentText = Content";
_nb.setContentText(_content);
 //BA.debugLineNum = 162;BA.debugLine="nb.setActivity(TargetActivity)";
_nb.setActivity(processBA,_targetactivity);
 //BA.debugLineNum = 163;BA.debugLine="nb.OnlyAlertOnce = True";
_nb.setOnlyAlertOnce(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 164;BA.debugLine="nb.SmallIcon = Icon";
_nb.setSmallIcon(_icon);
 //BA.debugLineNum = 165;BA.debugLine="nb.Tag = ChannelName";
_nb.setTag(_channelname);
 //BA.debugLineNum = 166;BA.debugLine="If p.SdkVersion >= 26 Then";
if (_p.getSdkVersion()>=26) { 
 //BA.debugLineNum = 167;BA.debugLine="Dim ctxt As JavaObject";
_ctxt = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 168;BA.debugLine="ctxt.InitializeContext";
_ctxt.InitializeContext(processBA);
 //BA.debugLineNum = 169;BA.debugLine="Dim manager As JavaObject";
_manager = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 170;BA.debugLine="manager.InitializeStatic(\"android.app.Notificat";
_manager.InitializeStatic("android.app.NotificationManager");
 //BA.debugLineNum = 171;BA.debugLine="Dim Channel As JavaObject";
_channel = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 172;BA.debugLine="Dim importance As String";
_importance = "";
 //BA.debugLineNum = 174;BA.debugLine="importance = \"IMPORTANCE_HIGH\"";
_importance = "IMPORTANCE_HIGH";
 //BA.debugLineNum = 175;BA.debugLine="Dim ChannelVisibleName As String = ChannelName";
_channelvisiblename = _channelname;
 //BA.debugLineNum = 176;BA.debugLine="Channel.InitializeNewInstance(\"android.app.Noti";
_channel.InitializeNewInstance("android.app.NotificationChannel",new Object[]{(Object)(_channelname),(Object)(_channelvisiblename),_manager.GetField(_importance)});
 //BA.debugLineNum = 180;BA.debugLine="Channel.RunMethod(\"setShowBadge\", Array(ShowBad";
_channel.RunMethod("setShowBadge",new Object[]{(Object)(_showbadge)});
 //BA.debugLineNum = 181;BA.debugLine="manager = ctxt.RunMethod(\"getSystemService\", Ar";
_manager.setObject((java.lang.Object)(_ctxt.RunMethod("getSystemService",new Object[]{(Object)("notification")})));
 //BA.debugLineNum = 182;BA.debugLine="manager.RunMethod(\"createNotificationChannel\",";
_manager.RunMethod("createNotificationChannel",new Object[]{(Object)(_channel.getObject())});
 //BA.debugLineNum = 183;BA.debugLine="Dim jo As JavaObject = nb";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_nb.getObject()));
 //BA.debugLineNum = 184;BA.debugLine="jo.RunMethod(\"setChannelId\", Array(ChannelName)";
_jo.RunMethod("setChannelId",new Object[]{(Object)(_channelname)});
 };
 //BA.debugLineNum = 186;BA.debugLine="Return  nb.GetNotification";
if (true) return (anywheresoftware.b4a.objects.NotificationWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.NotificationWrapper(), (java.lang.Object)(_nb.GetNotification(processBA)));
 }else {
 //BA.debugLineNum = 188;BA.debugLine="Dim n As Notification";
_n = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 189;BA.debugLine="n.Initialize";
_n.Initialize();
 //BA.debugLineNum = 190;BA.debugLine="n.Sound = Sound";
_n.setSound(_sound);
 //BA.debugLineNum = 191;BA.debugLine="n.Vibrate = Vibrate";
_n.setVibrate(_vibrate);
 //BA.debugLineNum = 192;BA.debugLine="n.Icon = Icon";
_n.setIcon(_icon);
 //BA.debugLineNum = 193;BA.debugLine="n.SetInfo(Title, Content, TargetActivity)";
_n.SetInfoNew(processBA,BA.ObjectToCharSequence(_title),BA.ObjectToCharSequence(_content),_targetactivity);
 //BA.debugLineNum = 194;BA.debugLine="Return n";
if (true) return _n;
 };
 //BA.debugLineNum = 196;BA.debugLine="End Sub";
return null;
}
public static String  _getairquality(int _number) throws Exception{
 //BA.debugLineNum = 258;BA.debugLine="Sub GetAirQuality(number As Int) As String";
 //BA.debugLineNum = 261;BA.debugLine="If number <= 100 Then";
if (_number<=100) { 
 //BA.debugLineNum = 262;BA.debugLine="Return(\"Carbon monoxide level is perfect\")";
if (true) return ("Carbon monoxide level is perfect");
 }else if(((_number>100) && (_number<400)) || _number==400) { 
 //BA.debugLineNum = 264;BA.debugLine="Return(\"Carbon monoxide level is normal\")";
if (true) return ("Carbon monoxide level is normal");
 }else if(((_number>400) && (_number<900)) || _number==900) { 
 //BA.debugLineNum = 266;BA.debugLine="Return(\"Carbon monoxide level is high\")";
if (true) return ("Carbon monoxide level is high");
 }else if(_number>900) { 
 //BA.debugLineNum = 268;BA.debugLine="Return(\"ALARM Carbon monoxide level is very high";
if (true) return ("ALARM Carbon monoxide level is very high");
 }else {
 //BA.debugLineNum = 270;BA.debugLine="Return(\"MQ-7 - cant read any value - check the s";
if (true) return ("MQ-7 - cant read any value - check the sensor!");
 };
 //BA.debugLineNum = 272;BA.debugLine="End Sub";
return "";
}
public static String  _getcomfort(String _dht11comfortstatus) throws Exception{
String _localcomfortstatus = "";
 //BA.debugLineNum = 231;BA.debugLine="Sub GetComfort(DHT11ComfortStatus As String) As St";
 //BA.debugLineNum = 232;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = "";
 //BA.debugLineNum = 233;BA.debugLine="Select Case DHT11ComfortStatus";
switch (BA.switchObjectToInt(_dht11comfortstatus,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(8),BA.NumberToString(9),BA.NumberToString(10))) {
case 0: {
 //BA.debugLineNum = 235;BA.debugLine="localcomfortstatus = \"OK\"";
_localcomfortstatus = "OK";
 break; }
case 1: {
 //BA.debugLineNum = 237;BA.debugLine="localcomfortstatus = \"Too hot\"";
_localcomfortstatus = "Too hot";
 break; }
case 2: {
 //BA.debugLineNum = 239;BA.debugLine="localcomfortstatus = \"Too cold\"";
_localcomfortstatus = "Too cold";
 break; }
case 3: {
 //BA.debugLineNum = 241;BA.debugLine="localcomfortstatus = \"Too dry\"";
_localcomfortstatus = "Too dry";
 break; }
case 4: {
 //BA.debugLineNum = 243;BA.debugLine="localcomfortstatus = \"Hot and dry\"";
_localcomfortstatus = "Hot and dry";
 break; }
case 5: {
 //BA.debugLineNum = 245;BA.debugLine="localcomfortstatus = \"Cold and dry\"";
_localcomfortstatus = "Cold and dry";
 break; }
case 6: {
 //BA.debugLineNum = 247;BA.debugLine="localcomfortstatus = \"Too humid\"";
_localcomfortstatus = "Too humid";
 break; }
case 7: {
 //BA.debugLineNum = 249;BA.debugLine="localcomfortstatus = \"Hot and humid\"";
_localcomfortstatus = "Hot and humid";
 break; }
case 8: {
 //BA.debugLineNum = 251;BA.debugLine="localcomfortstatus = \"Cold and humid\"";
_localcomfortstatus = "Cold and humid";
 break; }
default: {
 //BA.debugLineNum = 253;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = "Unknown";
 break; }
}
;
 //BA.debugLineNum = 255;BA.debugLine="Return localcomfortstatus";
if (true) return _localcomfortstatus;
 //BA.debugLineNum = 256;BA.debugLine="End Sub";
return "";
}
public static String  _getperception(String _dht11perception) throws Exception{
String _localperception = "";
 //BA.debugLineNum = 198;BA.debugLine="Sub GetPerception(DHT11Perception As String) As St";
 //BA.debugLineNum = 209;BA.debugLine="Dim localperception As String";
_localperception = "";
 //BA.debugLineNum = 210;BA.debugLine="Select Case DHT11Perception";
switch (BA.switchObjectToInt(_dht11perception,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(7))) {
case 0: {
 //BA.debugLineNum = 212;BA.debugLine="localperception = \"Home feels like the western";
_localperception = "Home feels like the western US, a bit dry to some";
 break; }
case 1: {
 //BA.debugLineNum = 214;BA.debugLine="localperception = \"Home is very comfortable\"";
_localperception = "Home is very comfortable";
 break; }
case 2: {
 //BA.debugLineNum = 216;BA.debugLine="localperception = \"Home is comfortable\"";
_localperception = "Home is comfortable";
 break; }
case 3: {
 //BA.debugLineNum = 218;BA.debugLine="localperception = \"Home is okay but the humidit";
_localperception = "Home is okay but the humidity is at upper limit";
 break; }
case 4: {
 //BA.debugLineNum = 220;BA.debugLine="localperception = \"Home is uncomfortable, and t";
_localperception = "Home is uncomfortable, and the humidity is at upper limit";
 break; }
case 5: {
 //BA.debugLineNum = 222;BA.debugLine="localperception = \"Home is very humid, and quit";
_localperception = "Home is very humid, and quite uncomfortable";
 break; }
case 6: {
 //BA.debugLineNum = 224;BA.debugLine="localperception = \"Home is extremely uncomforta";
_localperception = "Home is extremely uncomfortable, and oppressive";
 break; }
case 7: {
 //BA.debugLineNum = 226;BA.debugLine="localperception = \"Home humidity is severely hi";
_localperception = "Home humidity is severely high, and even deadly for asthma related illnesses";
 break; }
}
;
 //BA.debugLineNum = 228;BA.debugLine="Return localperception";
if (true) return _localperception;
 //BA.debugLineNum = 229;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connect() throws Exception{
String _clientid = "";
anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper _connopt = null;
 //BA.debugLineNum = 44;BA.debugLine="Sub MQTT_Connect";
 //BA.debugLineNum = 45;BA.debugLine="Try";
try { //BA.debugLineNum = 46;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'crea";
_clientid = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (999999999)));
 //BA.debugLineNum = 47;BA.debugLine="MQTT.Initialize(\"MQTT\", MQTTServerURI, ClientId)";
_mqtt.Initialize(processBA,"MQTT",_mqttserveruri,_clientid);
 //BA.debugLineNum = 49;BA.debugLine="Dim ConnOpt As MqttConnectOptions";
_connopt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper();
 //BA.debugLineNum = 50;BA.debugLine="ConnOpt.Initialize(MQTTUser, MQTTPassword)";
_connopt.Initialize(_mqttuser,_mqttpassword);
 //BA.debugLineNum = 51;BA.debugLine="MQTT.Connect2(ConnOpt)";
_mqtt.Connect2((org.eclipse.paho.client.mqttv3.MqttConnectOptions)(_connopt.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 53;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 55;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 57;BA.debugLine="Sub MQTT_Connected (Success As Boolean)";
 //BA.debugLineNum = 58;BA.debugLine="Try";
try { //BA.debugLineNum = 59;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 60;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 //BA.debugLineNum = 61;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 }else {
 //BA.debugLineNum = 63;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
_mqtt.Subscribe("TempHumid",(int) (0));
 //BA.debugLineNum = 64;BA.debugLine="MQTT.Subscribe(\"MQ7\", 0)";
_mqtt.Subscribe("MQ7",(int) (0));
 };
 } 
       catch (Exception e10) {
			processBA.setLastException(e10); //BA.debugLineNum = 67;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_disconnected() throws Exception{
 //BA.debugLineNum = 72;BA.debugLine="Private Sub MQTT_Disconnected";
 //BA.debugLineNum = 73;BA.debugLine="Try";
try { //BA.debugLineNum = 74;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e4) {
			processBA.setLastException(e4); //BA.debugLineNum = 76;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
String _status = "";
String[] _a = null;
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _notificationtext = "";
b4a.example.dateutils._period _p = null;
String _managertemphumiditycooldowntime = "";
 //BA.debugLineNum = 80;BA.debugLine="Private Sub MQTT_MessageArrived (Topic As String,";
 //BA.debugLineNum = 81;BA.debugLine="Try";
try { //BA.debugLineNum = 82;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic).equals("TempHumid")) { 
 //BA.debugLineNum = 84;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 85;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 87;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 88;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 89;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 90;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 91;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 92;BA.debugLine="StateManager.SetSetting(\"TempHumidity\",status";
mostCurrent._statemanager._setsetting(processBA,"TempHumidity",_status);
 //BA.debugLineNum = 93;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(processBA);
 //BA.debugLineNum = 95;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 96;BA.debugLine="NotificationText = \"Temperature: \" & a(1) & \"";
_notificationtext = "Temperature: "+_a[(int) (1)]+"°F | Humidity: "+_a[(int) (2)]+"% | Comfort: "+_getcomfort(_a[(int) (4)]);
 //BA.debugLineNum = 97;BA.debugLine="If (a(3) > 3) Or (a(4) <> 0 And a(4) <> 2)  T";
if (((double)(Double.parseDouble(_a[(int) (3)]))>3) || ((_a[(int) (4)]).equals(BA.NumberToString(0)) == false && (_a[(int) (4)]).equals(BA.NumberToString(2)) == false)) { 
 //BA.debugLineNum = 98;BA.debugLine="If IsTempHumidityNotificationOnGoing = False";
if (_istemphumiditynotificationongoing==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 99;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(l";
_p = mostCurrent._dateutils._periodbetween(processBA,_lngtickstemphumid,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 100;BA.debugLine="Dim managerTempHumidityCooldownTime As Stri";
_managertemphumiditycooldowntime = mostCurrent._statemanager._getsetting(processBA,"TempHumidityCooldownTime");
 //BA.debugLineNum = 101;BA.debugLine="If managerTempHumidityCooldownTime = \"\" Or";
if ((_managertemphumiditycooldowntime).equals("") || anywheresoftware.b4a.keywords.Common.IsNumber(_managertemphumiditycooldowntime)==anywheresoftware.b4a.keywords.Common.False || (_managertemphumiditycooldowntime).equals("0")) { 
 //BA.debugLineNum = 102;BA.debugLine="managerTempHumidityCooldownTime = 1";
_managertemphumiditycooldowntime = BA.NumberToString(1);
 };
 //BA.debugLineNum = 104;BA.debugLine="If lngTicksTempHumid = 0 Or p.Minutes > = m";
if (_lngtickstemphumid==0 || _p.Minutes>=(double)(Double.parseDouble(_managertemphumiditycooldowntime))) { 
 //BA.debugLineNum = 105;BA.debugLine="CreateNotification(GetPerception(a(3)),Not";
_createnotification(_getperception(_a[(int) (3)]),_notificationtext,"temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Temperature").Notify((int) (725));
 //BA.debugLineNum = 106;BA.debugLine="lngTicksTempHumid = DateTime.now";
_lngtickstemphumid = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 };
 };
 }else if((double)(Double.parseDouble(_a[(int) (1)]))>=80) { 
 //BA.debugLineNum = 110;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(ln";
_p = mostCurrent._dateutils._periodbetween(processBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 111;BA.debugLine="If lngTicks = 0 Or p.Minutes > = 2 Then";
if (_lngticks==0 || _p.Minutes>=2) { 
 //BA.debugLineNum = 112;BA.debugLine="CreateNotification(\"Warning! Home temperatu";
_createnotification("Warning! Home temperature is very high at "+_a[(int) (1)]+" degrees fahrenheit.",_notificationtext,"temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Temperature").Notify((int) (725));
 //BA.debugLineNum = 113;BA.debugLine="lngTicks = DateTime.now";
_lngticks = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 };
 }else {
 //BA.debugLineNum = 116;BA.debugLine="lngTicks = 0";
_lngticks = (long) (0);
 //BA.debugLineNum = 117;BA.debugLine="IsTempHumidityNotificationOnGoing = False";
_istemphumiditynotificationongoing = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 118;BA.debugLine="Notification1.Cancel(725)";
_notification1.Cancel((int) (725));
 };
 };
 };
 }else if((_topic).equals("MQ7")) { 
 //BA.debugLineNum = 123;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 124;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 125;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 126;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 127;BA.debugLine="Log(\"MQ7 status: \" & status)";
anywheresoftware.b4a.keywords.Common.Log("MQ7 status: "+_status);
 //BA.debugLineNum = 128;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 129;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 130;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 131;BA.debugLine="StateManager.SetSetting(\"AirQuality\",status)";
mostCurrent._statemanager._setsetting(processBA,"AirQuality",_status);
 //BA.debugLineNum = 132;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(processBA);
 //BA.debugLineNum = 134;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 135;BA.debugLine="NotificationText = GetAirQuality(a(0)) & \", a";
_notificationtext = _getairquality((int)(Double.parseDouble(_a[(int) (0)])))+", at "+_a[(int) (0)]+" ppm";
 //BA.debugLineNum = 136;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 137;BA.debugLine="If IsAirQualityNotificationOnGoing = False T";
if (_isairqualitynotificationongoing==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 138;BA.debugLine="CreateNotification(\"Air Quality\",Notificati";
_createnotification("Air Quality",_notificationtext,"co",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Carbon Monoxide").Notify((int) (726));
 };
 }else {
 //BA.debugLineNum = 141;BA.debugLine="IsAirQualityNotificationOnGoing = False";
_isairqualitynotificationongoing = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 142;BA.debugLine="Notification1.Cancel(726)";
_notification1.Cancel((int) (726));
 };
 };
 };
 };
 } 
       catch (Exception e64) {
			processBA.setLastException(e64); //BA.debugLineNum = 148;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 150;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 16;BA.debugLine="Public lngTicks As Long";
_lngticks = 0L;
 //BA.debugLineNum = 17;BA.debugLine="Public lngTicksTempHumid As Long";
_lngtickstemphumid = 0L;
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 20;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 21;BA.debugLine="Notification1.Initialize2(Notification1.IMPORTANC";
_notification1.Initialize2(_notification1.IMPORTANCE_DEFAULT);
 //BA.debugLineNum = 22;BA.debugLine="Service.AutomaticForegroundMode = Service.AUTOMAT";
mostCurrent._service.AutomaticForegroundMode = mostCurrent._service.AUTOMATIC_FOREGROUND_ALWAYS;
 //BA.debugLineNum = 23;BA.debugLine="CreateNotification(\"Temperature\",\"Temperature\",\"t";
_createnotification("Temperature","Temperature","temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Temperature");
 //BA.debugLineNum = 24;BA.debugLine="CreateNotification(\"Carbon Monoxide\",\"Carbon Mono";
_createnotification("Carbon Monoxide","Carbon Monoxide","co",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,"Carbon Monoxide");
 //BA.debugLineNum = 26;BA.debugLine="Notification1.Icon = \"icon\"";
_notification1.setIcon("icon");
 //BA.debugLineNum = 27;BA.debugLine="Notification1.Vibrate = False";
_notification1.setVibrate(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 28;BA.debugLine="Notification1.AutoCancel = False";
_notification1.setAutoCancel(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 29;BA.debugLine="Notification1.Sound = False";
_notification1.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 30;BA.debugLine="Notification1.SetInfo(\"Smart Home Monitor\",\"Servi";
_notification1.SetInfoNew(processBA,BA.ObjectToCharSequence("Smart Home Monitor"),BA.ObjectToCharSequence("Service is running. Tap to open."),(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 31;BA.debugLine="Service.AutomaticForegroundNotification = Notific";
mostCurrent._service.AutomaticForegroundNotification = (android.app.Notification)(_notification1.getObject());
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 39;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 34;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 35;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
}
