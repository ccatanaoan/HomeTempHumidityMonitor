B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=8
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: true
	'#StartCommandReturnValue: android.app.Service.START_STICKY
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Private MQTT As MqttClient
	Private MQTTUser As String = "vynckfaq"
	Private MQTTPassword As String = "KHSV1Q1qSUUY"
	Private MQTTServerURI As String = "tcp://m14.cloudmqtt.com:11816"
	Private Notification1 As Notification
	Public IsAirQualityNotificationOnGoing As Boolean 
	Public IsTempHumidityNotificationOnGoing As Boolean
End Sub

Sub Service_Create
	Notification1.Initialize
	Service.AutomaticForegroundMode = Service.AUTOMATIC_FOREGROUND_ALWAYS
	CreateNotification("Temperature","Temperature","temp",Main,False,False,True,"Temperature")
	CreateNotification("Carbon Monoxide","Carbon Monoxide","co",Main,False,False,True,"Carbon Monoxide")
End Sub

Sub Service_Start (StartingIntent As Intent)
	MQTT_Connect
	'Service.StopAutomaticForeground 'Call this when the background task completes (if there is one)
End Sub

Sub Service_Destroy
	
End Sub

'Connect to CloudMQTT broker
Sub MQTT_Connect
	Try
		Dim ClientId As String = Rnd(0, 999999999) 'create a unique id
		MQTT.Initialize("MQTT", MQTTServerURI, ClientId)

		Dim ConnOpt As MqttConnectOptions
		ConnOpt.Initialize(MQTTUser, MQTTPassword)
		MQTT.Connect2(ConnOpt)
	Catch
		Log(LastException)
	End Try
End Sub

Sub MQTT_Connected (Success As Boolean)
	Try
		If Success = False Then
			Log(LastException)
			MQTT_Connect
		Else
			MQTT.Subscribe("TempHumid", 0)
			MQTT.Subscribe("MQ7", 0)
		End If
	Catch
		Log(LastException)
	End Try
    
End Sub

Private Sub MQTT_Disconnected
	Try
		MQTT_Connect
	Catch
		Log(LastException)
	End Try
End Sub

Private Sub MQTT_MessageArrived (Topic As String, Payload() As Byte)
	Try
		If Topic = "TempHumid" Then
		
			Dim status As String
			status = BytesToString(Payload, 0, Payload.Length, "UTF8") 

			Dim a() As String = Regex.Split("\|",status)
			If a.Length = 9 Then
				Dim cs As CSBuilder
				cs.Initialize
				If a(0) = "OK" And a(1) > 0 Then
					Dim NotificationText As String
					NotificationText = "Temperature: " & a(1) & "°F | Humidity: " & a(2) & "% | Comfort: " & GetComfort(a(4))
					If (a(3) > 3) Or (a(4) <> 0 And a(4) <> 2)  Then
						If IsTempHumidityNotificationOnGoing = False Then
							CreateNotification(GetPerception(a(3)),NotificationText,"temp",Main,False,False,True,"Temperature").Notify(725)
						End If
					Else
						IsTempHumidityNotificationOnGoing = False
						Notification1.Cancel(725)
					End If
				End If
			End If
		Else If Topic = "MQ7" Then 
			Dim status As String
			Dim cs As CSBuilder
			cs.Initialize
			status = BytesToString(Payload, 0, Payload.Length, "UTF8") ' MQ7 status: 334|18-04-14|00:20:54
			Log("MQ7 status: " & status)
			Dim a() As String = Regex.Split("\|",status)
			If a.Length = 3 Then
				If IsNumber(a(0)) And a(0) > 0 Then
					StateManager.SetSetting("AirQuality",status)
					StateManager.SaveSettings
					
					Dim NotificationText As String
					NotificationText = GetAirQuality(a(0)) & " at " & a(0) & " ppm"
					If a(0) > 400 Then
						If IsAirQualityNotificationOnGoing = False Then
							CreateNotification("Air quality",NotificationText,"co",Main,False,False,True,"Carbon Monoxide").Notify(726)
						End If
					Else
						IsAirQualityNotificationOnGoing = False
						Notification1.Cancel(726)
					End If
				End If
			End If
		End If
	Catch
		Log(LastException)
	End Try
End Sub

Private Sub CreateNotification(Title As String, Content As String, Icon As String, TargetActivity As Object, _
    Sound As Boolean, Vibrate As Boolean, ShowBadge As Boolean, ChannelName As String) As Notification
	Dim p As Phone
	If p.SdkVersion >= 21 Then
		Dim nb As NotificationBuilder
		nb.Initialize
		nb.DefaultSound = Sound
		nb.DefaultVibrate = Vibrate
		nb.ContentTitle = Title
		nb.ContentText = Content
		nb.setActivity(TargetActivity)
		nb.OnlyAlertOnce = True
		nb.SmallIcon = Icon
		If p.SdkVersion >= 26 Then
			Dim ctxt As JavaObject
			ctxt.InitializeContext
			Dim manager As JavaObject
			manager.InitializeStatic("android.app.NotificationManager")
			Dim Channel As JavaObject
			Dim importance As String
			'If Sound Then importance = "IMPORTANCE_HIGH" Else importance = "IMPORTANCE_LOW"
			importance = "IMPORTANCE_HIGH"
			Dim ChannelVisibleName As String = ChannelName 'Application.LabelName
			Channel.InitializeNewInstance("android.app.NotificationChannel", _
                   Array(ChannelName, ChannelVisibleName, manager.GetField(importance)))
			'modify the channel
			'For example: disable the badge feature
			Channel.RunMethod("setShowBadge", Array(ShowBadge))
			manager = ctxt.RunMethod("getSystemService", Array("notification"))
			manager.RunMethod("createNotificationChannel", Array(Channel))
			Dim jo As JavaObject = nb
			jo.RunMethod("setChannelId", Array(ChannelName))
		End If
		Return  nb.GetNotification
	Else
		Dim n As Notification
		n.Initialize
		n.Sound = Sound
		n.Vibrate = Vibrate
		n.Icon = Icon
		n.SetInfo(Title, Content, TargetActivity)
		Return n
	End If
End Sub

Sub GetPerception(DHT11Perception As String) As String
	' Return value       Dew point                        Human perception[6]
	'    7         Over 26 °C (>78.8°F)     Severely high, even deadly For asthma related illnesses
	'    6         24–26 °C (75.2-78.8°F)   Extremely uncomfortable, oppressive
	'    5         21–24 °C (69.8-75.2°F)   Very humid, quite uncomfortable
	'    4         18–21 °C (64.4-69.8°F)   Somewhat uncomfortable For most people at upper limit
	'    3         16–18 °C (60.8-64.4°F)   OK For most, but everyone perceives the humidity at upper limit
	'    2         13–16 °C (55.4-60.8°F)   Comfortable
	'    1         10–12 °C (50-53.6°F)     Very comfortable
	'    0         Under 10 °C (<50°F)      Feels like the western US, a Bit dry To some
	
	Dim localperception As String
	Select Case DHT11Perception
		Case 0
			localperception = "Home feels like the western US, a bit dry to some"
		Case 1
			localperception = "Home is very comfortable"
		Case 2
			localperception = "Home is comfortable"
		Case 3
			localperception = "Home is okay but the humidity is at upper limit"
		Case 4
			localperception = "Home is uncomfortable and the humidity is at upper limit"
		Case 5
			localperception = "Home is very humid, quite uncomfortable"
		Case 6
			localperception = "Home is extremely uncomfortable, oppressive"
		Case 7
			localperception = "Home humidity is severely high, even deadly for asthma related illnesses"
	End Select
	Return localperception
End Sub

Sub GetComfort(DHT11ComfortStatus As String) As String
	Dim localcomfortstatus As String
	Select Case DHT11ComfortStatus
		Case 0
			localcomfortstatus = "OK"
		Case 1
			localcomfortstatus = "Too hot"
		Case 2
			localcomfortstatus = "Too cold"
		Case 4
			localcomfortstatus = "Too dry"
		Case 5
			localcomfortstatus = "Hot and dry"
		Case 6
			localcomfortstatus = "Cold and dry"
		Case 8
			localcomfortstatus = "Too humid"
		Case 9
			localcomfortstatus = "Hot and humid"
		Case 10
			localcomfortstatus = "Cold and humid"
		Case Else
			localcomfortstatus = "Unknown"
	End Select
	Return localcomfortstatus
End Sub

Sub GetAirQuality(number As Int) As String
	' Detecting range: 10ppm-1000ppm carbon monoxide
	' Air quality-cases: < 100 perfect | 100 - 400 normal | > 400 - 900 high | > 900 abnormal
	If number <= 100 Then
		Return("Carbon monoxide level is perfect")
	else if ((number > 100) And (number < 400)) Or number = 400 Then
		Return("Carbon monoxide level is normal")
	else if ((number > 400) And (number < 900)) Or number = 900 Then
		Return("Carbon monoxide level is high")
	else If number > 900 Then
		Return("ALARM Carbon monoxide level is very high")
	Else
		Return("MQ-7 - cant read any value - check the sensor!")
	End If
End Sub