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
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public cloyd.smart.home.monitor.main _main = null;
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
 //BA.debugLineNum = 114;BA.debugLine="Private Sub CreateNotification(Title As String, Co";
 //BA.debugLineNum = 116;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 117;BA.debugLine="If p.SdkVersion >= 21 Then";
if (_p.getSdkVersion()>=21) { 
 //BA.debugLineNum = 118;BA.debugLine="Dim nb As NotificationBuilder";
_nb = new barxdroid.NotificationBuilder.NotificationBuilder();
 //BA.debugLineNum = 119;BA.debugLine="nb.Initialize";
_nb.Initialize(processBA);
 //BA.debugLineNum = 120;BA.debugLine="nb.DefaultSound = Sound";
_nb.setDefaultSound(_sound);
 //BA.debugLineNum = 121;BA.debugLine="nb.DefaultVibrate = Vibrate";
_nb.setDefaultVibrate(_vibrate);
 //BA.debugLineNum = 122;BA.debugLine="nb.ContentTitle = Title";
_nb.setContentTitle(_title);
 //BA.debugLineNum = 123;BA.debugLine="nb.ContentText = Content";
_nb.setContentText(_content);
 //BA.debugLineNum = 124;BA.debugLine="nb.setActivity(TargetActivity)";
_nb.setActivity(processBA,_targetactivity);
 //BA.debugLineNum = 125;BA.debugLine="nb.OnlyAlertOnce = True";
_nb.setOnlyAlertOnce(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 126;BA.debugLine="nb.SmallIcon = Icon";
_nb.setSmallIcon(_icon);
 //BA.debugLineNum = 127;BA.debugLine="If p.SdkVersion >= 26 Then";
if (_p.getSdkVersion()>=26) { 
 //BA.debugLineNum = 128;BA.debugLine="Dim ctxt As JavaObject";
_ctxt = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 129;BA.debugLine="ctxt.InitializeContext";
_ctxt.InitializeContext(processBA);
 //BA.debugLineNum = 130;BA.debugLine="Dim manager As JavaObject";
_manager = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 131;BA.debugLine="manager.InitializeStatic(\"android.app.Notificat";
_manager.InitializeStatic("android.app.NotificationManager");
 //BA.debugLineNum = 132;BA.debugLine="Dim Channel As JavaObject";
_channel = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 133;BA.debugLine="Dim importance As String";
_importance = "";
 //BA.debugLineNum = 134;BA.debugLine="If Sound Then importance = \"IMPORTANCE_HIGH\" El";
if (_sound) { 
_importance = "IMPORTANCE_HIGH";}
else {
_importance = "IMPORTANCE_LOW";};
 //BA.debugLineNum = 135;BA.debugLine="Dim ChannelVisibleName As String = ChannelName";
_channelvisiblename = _channelname;
 //BA.debugLineNum = 136;BA.debugLine="Channel.InitializeNewInstance(\"android.app.Noti";
_channel.InitializeNewInstance("android.app.NotificationChannel",new Object[]{(Object)(_channelname),(Object)(_channelvisiblename),_manager.GetField(_importance)});
 //BA.debugLineNum = 140;BA.debugLine="Channel.RunMethod(\"setShowBadge\", Array(ShowBad";
_channel.RunMethod("setShowBadge",new Object[]{(Object)(_showbadge)});
 //BA.debugLineNum = 141;BA.debugLine="manager = ctxt.RunMethod(\"getSystemService\", Ar";
_manager.setObject((java.lang.Object)(_ctxt.RunMethod("getSystemService",new Object[]{(Object)("notification")})));
 //BA.debugLineNum = 142;BA.debugLine="manager.RunMethod(\"createNotificationChannel\",";
_manager.RunMethod("createNotificationChannel",new Object[]{(Object)(_channel.getObject())});
 //BA.debugLineNum = 143;BA.debugLine="Dim jo As JavaObject = nb";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_nb.getObject()));
 //BA.debugLineNum = 144;BA.debugLine="jo.RunMethod(\"setChannelId\", Array(ChannelName)";
_jo.RunMethod("setChannelId",new Object[]{(Object)(_channelname)});
 };
 //BA.debugLineNum = 146;BA.debugLine="Return  nb.GetNotification";
if (true) return (anywheresoftware.b4a.objects.NotificationWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.NotificationWrapper(), (java.lang.Object)(_nb.GetNotification(processBA)));
 }else {
 //BA.debugLineNum = 148;BA.debugLine="Dim n As Notification";
_n = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 149;BA.debugLine="n.Initialize";
_n.Initialize();
 //BA.debugLineNum = 150;BA.debugLine="n.Sound = Sound";
_n.setSound(_sound);
 //BA.debugLineNum = 151;BA.debugLine="n.Vibrate = Vibrate";
_n.setVibrate(_vibrate);
 //BA.debugLineNum = 152;BA.debugLine="n.Icon = Icon";
_n.setIcon(_icon);
 //BA.debugLineNum = 153;BA.debugLine="n.SetInfo(Title, Content, TargetActivity)";
_n.SetInfoNew(processBA,BA.ObjectToCharSequence(_title),BA.ObjectToCharSequence(_content),_targetactivity);
 //BA.debugLineNum = 154;BA.debugLine="Return n";
if (true) return _n;
 };
 //BA.debugLineNum = 156;BA.debugLine="End Sub";
return null;
}
public static String  _getairquality(int _number) throws Exception{
 //BA.debugLineNum = 218;BA.debugLine="Sub GetAirQuality(number As Int) As String";
 //BA.debugLineNum = 221;BA.debugLine="If number <= 100 Then";
if (_number<=100) { 
 //BA.debugLineNum = 222;BA.debugLine="Return(\"Carbon monoxide perfect\")";
if (true) return ("Carbon monoxide perfect");
 }else if(((_number>100) && (_number<400)) || _number==400) { 
 //BA.debugLineNum = 224;BA.debugLine="Return(\"Carbon monoxide normal\")";
if (true) return ("Carbon monoxide normal");
 }else if(((_number>400) && (_number<900)) || _number==900) { 
 //BA.debugLineNum = 226;BA.debugLine="Return(\"Carbon monoxide high\")";
if (true) return ("Carbon monoxide high");
 }else if(_number>900) { 
 //BA.debugLineNum = 228;BA.debugLine="Return(\"ALARM Carbon monoxide very high\")";
if (true) return ("ALARM Carbon monoxide very high");
 }else {
 //BA.debugLineNum = 230;BA.debugLine="Return(\"MQ-7 - cant read any value - check the s";
if (true) return ("MQ-7 - cant read any value - check the sensor!");
 };
 //BA.debugLineNum = 232;BA.debugLine="End Sub";
return "";
}
public static String  _getcomfort(String _dht11comfortstatus) throws Exception{
String _localcomfortstatus = "";
 //BA.debugLineNum = 191;BA.debugLine="Sub GetComfort(DHT11ComfortStatus As String) As St";
 //BA.debugLineNum = 192;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = "";
 //BA.debugLineNum = 193;BA.debugLine="Select Case DHT11ComfortStatus";
switch (BA.switchObjectToInt(_dht11comfortstatus,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(8),BA.NumberToString(9),BA.NumberToString(10))) {
case 0: {
 //BA.debugLineNum = 195;BA.debugLine="localcomfortstatus = \"OK\"";
_localcomfortstatus = "OK";
 break; }
case 1: {
 //BA.debugLineNum = 197;BA.debugLine="localcomfortstatus = \"Too hot\"";
_localcomfortstatus = "Too hot";
 break; }
case 2: {
 //BA.debugLineNum = 199;BA.debugLine="localcomfortstatus = \"Too cold\"";
_localcomfortstatus = "Too cold";
 break; }
case 3: {
 //BA.debugLineNum = 201;BA.debugLine="localcomfortstatus = \"Too dry\"";
_localcomfortstatus = "Too dry";
 break; }
case 4: {
 //BA.debugLineNum = 203;BA.debugLine="localcomfortstatus = \"Hot and dry\"";
_localcomfortstatus = "Hot and dry";
 break; }
case 5: {
 //BA.debugLineNum = 205;BA.debugLine="localcomfortstatus = \"Cold and dry\"";
_localcomfortstatus = "Cold and dry";
 break; }
case 6: {
 //BA.debugLineNum = 207;BA.debugLine="localcomfortstatus = \"Too humid\"";
_localcomfortstatus = "Too humid";
 break; }
case 7: {
 //BA.debugLineNum = 209;BA.debugLine="localcomfortstatus = \"Hot and humid\"";
_localcomfortstatus = "Hot and humid";
 break; }
case 8: {
 //BA.debugLineNum = 211;BA.debugLine="localcomfortstatus = \"Cold and humid\"";
_localcomfortstatus = "Cold and humid";
 break; }
default: {
 //BA.debugLineNum = 213;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = "Unknown";
 break; }
}
;
 //BA.debugLineNum = 215;BA.debugLine="Return localcomfortstatus";
if (true) return _localcomfortstatus;
 //BA.debugLineNum = 216;BA.debugLine="End Sub";
return "";
}
public static String  _getperception(String _dht11perception) throws Exception{
String _localperception = "";
 //BA.debugLineNum = 158;BA.debugLine="Sub GetPerception(DHT11Perception As String) As St";
 //BA.debugLineNum = 169;BA.debugLine="Dim localperception As String";
_localperception = "";
 //BA.debugLineNum = 170;BA.debugLine="Select Case DHT11Perception";
switch (BA.switchObjectToInt(_dht11perception,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(7))) {
case 0: {
 //BA.debugLineNum = 172;BA.debugLine="localperception = \"Feels like the western US, a";
_localperception = "Feels like the western US, a bit dry to some";
 break; }
case 1: {
 //BA.debugLineNum = 174;BA.debugLine="localperception = \"Very comfortable\"";
_localperception = "Very comfortable";
 break; }
case 2: {
 //BA.debugLineNum = 176;BA.debugLine="localperception = \"Comfortable\"";
_localperception = "Comfortable";
 break; }
case 3: {
 //BA.debugLineNum = 178;BA.debugLine="localperception = \"OK but everyone perceives th";
_localperception = "OK but everyone perceives the humidity at upper limit";
 break; }
case 4: {
 //BA.debugLineNum = 180;BA.debugLine="localperception = \"Somewhat uncomfortable for m";
_localperception = "Somewhat uncomfortable for most people at upper limit";
 break; }
case 5: {
 //BA.debugLineNum = 182;BA.debugLine="localperception = \"Very humid, quite uncomforta";
_localperception = "Very humid, quite uncomfortable";
 break; }
case 6: {
 //BA.debugLineNum = 184;BA.debugLine="localperception = \"Extremely uncomfortable, opp";
_localperception = "Extremely uncomfortable, oppressive";
 break; }
case 7: {
 //BA.debugLineNum = 186;BA.debugLine="localperception = \"Severely high, even deadly f";
_localperception = "Severely high, even deadly for asthma related illnesses";
 break; }
}
;
 //BA.debugLineNum = 188;BA.debugLine="Return localperception";
if (true) return _localperception;
 //BA.debugLineNum = 189;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connect() throws Exception{
String _clientid = "";
anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper _connopt = null;
 //BA.debugLineNum = 33;BA.debugLine="Sub MQTT_Connect";
 //BA.debugLineNum = 34;BA.debugLine="Try";
try { //BA.debugLineNum = 35;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'crea";
_clientid = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (999999999)));
 //BA.debugLineNum = 36;BA.debugLine="MQTT.Initialize(\"MQTT\", MQTTServerURI, ClientId)";
_mqtt.Initialize(processBA,"MQTT",_mqttserveruri,_clientid);
 //BA.debugLineNum = 38;BA.debugLine="Dim ConnOpt As MqttConnectOptions";
_connopt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper();
 //BA.debugLineNum = 39;BA.debugLine="ConnOpt.Initialize(MQTTUser, MQTTPassword)";
_connopt.Initialize(_mqttuser,_mqttpassword);
 //BA.debugLineNum = 40;BA.debugLine="MQTT.Connect2(ConnOpt)";
_mqtt.Connect2((org.eclipse.paho.client.mqttv3.MqttConnectOptions)(_connopt.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 42;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 46;BA.debugLine="Sub MQTT_Connected (Success As Boolean)";
 //BA.debugLineNum = 47;BA.debugLine="Try";
try { //BA.debugLineNum = 48;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 49;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 //BA.debugLineNum = 50;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 }else {
 //BA.debugLineNum = 52;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
_mqtt.Subscribe("TempHumid",(int) (0));
 //BA.debugLineNum = 53;BA.debugLine="MQTT.Subscribe(\"MQ7\", 0)";
_mqtt.Subscribe("MQ7",(int) (0));
 };
 } 
       catch (Exception e10) {
			processBA.setLastException(e10); //BA.debugLineNum = 56;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_disconnected() throws Exception{
 //BA.debugLineNum = 61;BA.debugLine="Private Sub MQTT_Disconnected";
 //BA.debugLineNum = 62;BA.debugLine="Try";
try { //BA.debugLineNum = 63;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e4) {
			processBA.setLastException(e4); //BA.debugLineNum = 65;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 67;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
String _status = "";
String[] _a = null;
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _notificationtext = "";
 //BA.debugLineNum = 69;BA.debugLine="Private Sub MQTT_MessageArrived (Topic As String,";
 //BA.debugLineNum = 70;BA.debugLine="Try";
try { //BA.debugLineNum = 71;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic).equals("TempHumid")) { 
 //BA.debugLineNum = 73;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 74;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 76;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 77;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 78;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 79;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 80;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 81;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 82;BA.debugLine="NotificationText = \"Temperature: \" & a(1) & \"";
_notificationtext = "Temperature: "+_a[(int) (1)]+"°F | Humidity: "+_a[(int) (2)]+"% | Comfort: "+_getcomfort(_a[(int) (4)]);
 //BA.debugLineNum = 83;BA.debugLine="If (a(3) > 3) Or (a(4) <> 0 And a(4) <> 2)  T";
if (((double)(Double.parseDouble(_a[(int) (3)]))>3) || ((_a[(int) (4)]).equals(BA.NumberToString(0)) == false && (_a[(int) (4)]).equals(BA.NumberToString(2)) == false)) { 
 //BA.debugLineNum = 84;BA.debugLine="CreateNotification(GetPerception(a(3)),Notif";
_createnotification(_getperception(_a[(int) (3)]),_notificationtext,"temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,"Temperature").Notify((int) (725));
 }else {
 //BA.debugLineNum = 86;BA.debugLine="Notification1.Cancel(725)";
_notification1.Cancel((int) (725));
 };
 };
 };
 }else if((_topic).equals("MQ7")) { 
 //BA.debugLineNum = 91;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 92;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 93;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 94;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 95;BA.debugLine="Log(\"MQ7 status: \" & status)";
anywheresoftware.b4a.keywords.Common.Log("MQ7 status: "+_status);
 //BA.debugLineNum = 96;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 97;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 98;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 99;BA.debugLine="Dim NotificationText As String";
_notificationtext = "";
 //BA.debugLineNum = 100;BA.debugLine="NotificationText = GetAirQuality(a(0)) & \" |";
_notificationtext = _getairquality((int)(Double.parseDouble(_a[(int) (0)])))+" | "+_a[(int) (0)]+" ppm";
 //BA.debugLineNum = 101;BA.debugLine="If a(0) > 400   Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 102;BA.debugLine="CreateNotification(\"Air quality\",Notificatio";
_createnotification("Air quality",_notificationtext,"co",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,"Carbon Monoxide").Notify((int) (726));
 }else {
 //BA.debugLineNum = 104;BA.debugLine="Notification1.Cancel(726)";
_notification1.Cancel((int) (726));
 };
 };
 };
 };
 } 
       catch (Exception e39) {
			processBA.setLastException(e39); //BA.debugLineNum = 110;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 };
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 13;BA.debugLine="Dim Notification1 As Notification";
_notification1 = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 17;BA.debugLine="Notification1.Initialize";
_notification1.Initialize();
 //BA.debugLineNum = 18;BA.debugLine="Service.AutomaticForegroundMode = Service.AUTOMAT";
mostCurrent._service.AutomaticForegroundMode = mostCurrent._service.AUTOMATIC_FOREGROUND_ALWAYS;
 //BA.debugLineNum = 19;BA.debugLine="CreateNotification(\"Temperature\",\"Temperature\",\"t";
_createnotification("Temperature","Temperature","temp",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,"Temperature");
 //BA.debugLineNum = 20;BA.debugLine="CreateNotification(\"Carbon Monoxide\",\"Carbon Mono";
_createnotification("Carbon Monoxide","Carbon Monoxide","co",(Object)(mostCurrent._main.getObject()),anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,"Carbon Monoxide");
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 24;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
}
