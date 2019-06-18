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
	Public IsAirQualityNotificationOnGoingBasement As Boolean
	Public IsTempHumidityNotificationOnGoingBasement As Boolean
	Public IsOldTempHumidityNotificationOnGoingBasement As Boolean
	Public IsOldTempHumidityNotificationOnGoing As Boolean
	Public IsOldAirQualityNotificationOnGoing As Boolean
	Public IsOldAirQualityNotificationOnGoingBasement As Boolean
	Public lngTicks As Long
	Public lngTicksTempHumid As Long
	Public lngTicksTempHumidBasement As Long
	Private bc As ByteConverter
End Sub

Sub Service_Create
	Notification1.Initialize2(Notification1.IMPORTANCE_DEFAULT)
	Service.AutomaticForegroundMode = Service.AUTOMATIC_FOREGROUND_ALWAYS
	CreateNotification("Living area temperature","Living area temperature","temp",Main,False,False,True,"Living area temperature")
	CreateNotification("Living area carbon monoxide","Living area carbon monoxide","co",Main,False,False,True,"Living area carbon monoxide")
	CreateNotification("Basement temperature","Basement temperature","temp",Main,False,False,True,"Basement temperature")
	CreateNotification("Basement carbon monoxide","Basement carbon monoxide","co",Main,False,False,True,"Basement carbon monoxide")
	CreateNotification("Basement DHT22 sensor issue","Basement DHT22 sensor issue","sensor",Main,False,False,True,"Basement DHT22 sensor issue")
	CreateNotification("Living area DHT22 sensor issue","Living area DHT22 sensor issue","sensor",Main,False,False,True,"Living area DHT22 sensor issue")
	CreateNotification("Living area CO sensor issue","Living area CO sensor issue","sensor",Main,False,False,True,"Living area CO sensor issue")
	CreateNotification("Basement CO sensor issue","Basement CO sensor issue","sensor",Main,False,False,True,"Basement CO sensor issue")
	
	Notification1.Icon = "icon"
	Notification1.Vibrate = False
	Notification1.AutoCancel = False
	Notification1.Sound = False
	Notification1.SetInfo("Smart Home Monitor","Service is running. Tap to open.",Main)
	Service.AutomaticForegroundNotification = Notification1
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
			MQTT.Subscribe("MQ7Basement", 0)
			MQTT.Subscribe("TempHumidBasement", 0)
			MQTT.Subscribe("HumidityAddValue", 0)
		End If
	Catch
		Log(LastException)
		ToastMessageShow(LastException,False)
	End Try
    
End Sub

Private Sub MQTT_Disconnected
	Try
		MQTT_Connect
	Catch
		Log(LastException)
		ToastMessageShow(LastException,False)
	End Try
End Sub

Private Sub MQTT_MessageArrived (Topic As String, Payload() As Byte)
	Try
		Dim strHumidityAddValue As String = StateManager.GetSetting("HumidityAddValue")
		If Topic = "TempHumid" Then
		
			Dim status As String
			status = BytesToString(Payload, 0, Payload.Length, "UTF8")

			Dim a() As String = Regex.Split("\|",status)
			If a.Length = 9 Then
				Dim cs As CSBuilder
				cs.Initialize
				If a(0) = "OK" And a(1) > 0 Then
					StateManager.SetSetting("TempHumidity",status)
					StateManager.SaveSettings
					
					'If DateTime.GetSecond(DateTime.Now) Mod 20 = 0 Then
					LogEvent(status)
					'End If
					
					' OK|81.46|58.50|4|1|83.43|65.54|18-07-21|22:22:48
					If (a(3) > 3) Or (a(4) <> 0)  Then
						Dim NotificationText As String
						NotificationText = GetPerception(a(3))
						' OK|81.46|58.50|4|1|83.43|65.54|18-07-21|22:22:48
			
						Dim p As Period = DateUtils.PeriodBetween(lngTicksTempHumid,DateTime.now)
						Dim managerTempHumidityCooldownTime As String = StateManager.GetSetting("TempHumidityCooldownTime")
						If managerTempHumidityCooldownTime = "" Or IsNumber(managerTempHumidityCooldownTime) = False Or managerTempHumidityCooldownTime ="0" Then
							managerTempHumidityCooldownTime = 1
						End If
						If IsTempHumidityNotificationOnGoing = False Then
							If p.Minutes > = managerTempHumidityCooldownTime Then
								If a(4) = 2 Or a(4) = 6 Then
									CreateNotification(GetComfort(a(4)),NotificationText,"tempcold",Main,False,False,True,"Living area temperature").Notify(725)
								else If a(4) = 10 Then
									CreateNotification(GetComfort(a(4)),NotificationText,"tempcoldhumid",Main,False,False,True,"Living area temperature").Notify(725)
								Else
									CreateNotification(GetComfort(a(4)),NotificationText,"temp",Main,False,False,True,"Living area temperature").Notify(725)
								End If
								lngTicksTempHumid = DateTime.now
							End If
						Else
							Dim TempHumidityPrevious() As String = Regex.Split("\|",StateManager.GetSetting("TempHumidityPrevious"))
							If a(4) <> TempHumidityPrevious(4) Then
								If p.Minutes > = managerTempHumidityCooldownTime Then
									If a(4) = 2 Or a(4) = 6 Then
										CreateNotification(GetComfort(a(4)),NotificationText,"tempcold",Main,False,False,True,"Living area temperature").Notify(725)
									else If a(4) = 10 Then
										CreateNotification(GetComfort(a(4)),NotificationText,"tempcoldhumid",Main,False,False,True,"Living area temperature").Notify(725)
									Else
										CreateNotification(GetComfort(a(4)),NotificationText,"temp",Main,False,False,True,"Living area temperature").Notify(725)
									End If
									lngTicksTempHumid = DateTime.now
								End If
							else if a(3) <> TempHumidityPrevious(3) Then
								If p.Minutes > = managerTempHumidityCooldownTime Then
									If a(4) = 2 Or a(4) = 6 Then
										CreateNotification("* " & GetComfort(a(4)),NotificationText,"tempcold",Main,False,False,True,"Living area temperature").Notify(725)
									else If a(4) = 10 Then
										CreateNotification("* " & GetComfort(a(4)),NotificationText,"tempcoldhumid",Main,False,False,True,"Living area temperature").Notify(725)
									Else
										CreateNotification("* " & GetComfort(a(4)),NotificationText,"temp",Main,False,False,True,"Living area temperature").Notify(725)
									End If
									lngTicksTempHumid = DateTime.now
								End If
							End If
						End If
						StateManager.SetSetting("TempHumidityPrevious",status)
						StateManager.SaveSettings
					Else
						lngTicksTempHumid = DateTime.now
						IsTempHumidityNotificationOnGoing = False
						Notification1.Cancel(725)
					End If
				End If
			End If	
			
			If strHumidityAddValue = "" Then
				strHumidityAddValue = "0"
			End If
			MQTT.Publish("HumidityAddValue", bc.StringToBytes(strHumidityAddValue, "utf8"))
		Else If Topic = "MQ7" Then
			Dim status As String
			Dim cs As CSBuilder
			cs.Initialize
			status = BytesToString(Payload, 0, Payload.Length, "UTF8") ' MQ7 status: 334|18-04-14|00:20:54
			Dim a() As String = Regex.Split("\|",status)
			If a.Length = 3 Then
				If IsNumber(a(0)) And a(0) > 0 Then
					StateManager.SetSetting("AirQuality",status)
					StateManager.SaveSettings
					
					Dim NotificationText As String
					NotificationText = GetAirQuality(a(0)) & ", at " & a(0) & " ppm"
					If a(0) > 400 Then
						If IsAirQualityNotificationOnGoing = False Then
							CreateNotification("Living Area Air Quality",NotificationText,"co",Main,False,False,True,"Living area carbon monoxide").Notify(726)
						Else
							CreateNotification("* Living Area Air Quality",NotificationText,"co",Main,False,False,True,"Living area carbon monoxide").Notify(726)
						End If
					Else
						IsAirQualityNotificationOnGoing = False
						Notification1.Cancel(726)
					End If
				End If
			End If
		Else If Topic = "MQ7Basement" Then
			Dim status As String
			Dim cs As CSBuilder
			cs.Initialize
			status = BytesToString(Payload, 0, Payload.Length, "UTF8") ' MQ7 status: 334|18-04-14|00:20:54
			Dim a() As String = Regex.Split("\|",status)
			If a.Length = 3 Then
				If IsNumber(a(0)) And a(0) > 0 Then
					StateManager.SetSetting("AirQualityBasement",status)
					StateManager.SaveSettings
					
					Dim NotificationText As String
					NotificationText = GetAirQuality(a(0)) & ", at " & a(0) & " ppm"
					If a(0) > 400 Then
						If IsAirQualityNotificationOnGoingBasement = False Then
							CreateNotification("Basement Air Quality",NotificationText,"co",Main,False,False,True,"Basement carbon monoxide").Notify(727)
						Else
							CreateNotification("* Basement Air Quality",NotificationText,"co",Main,False,False,True,"Basement carbon monoxide").Notify(727)
						End If
					Else
						IsAirQualityNotificationOnGoingBasement = False
						Notification1.Cancel(727)
					End If
				End If
			End If
			
			' Delete log files older than 2 days
			Dim FileNameToday As String
			Dim FileNameYesterday As String
			Dim Now As Long
			Dim Month As Int
			Dim Day As Int
			Dim Year As Int
			Dim Yesterday As Long
			Dim MonthYesterday As Int
			Dim DayYesterday As Int
			Dim YearYesterday As Int

			Now = DateTime.Now
			Month = DateTime.GetMonth(Now)
			Day = DateTime.GetDayOfMonth (Now)
			Year = DateTime.GetYear(Now)
			
			Yesterday = DateTime.add(DateTime.Now, 0, 0, -1)
			MonthYesterday = DateTime.GetMonth(Yesterday)
			DayYesterday = DateTime.GetDayOfMonth (Yesterday)
			YearYesterday = DateTime.GetYear(Yesterday)

			FileNameToday = "LivingRoomTempHumid_" & Year & "-" & NumberFormat(Month,2,0) & "-" & NumberFormat(Day,2,0) & ".log"
			FileNameYesterday = "LivingRoomTempHumid_" & YearYesterday & "-" & NumberFormat(MonthYesterday,2,0) & "-" & NumberFormat(DayYesterday,2,0) & ".log"
			
			Dim flist As List = WildCardFilesList2(File.DirRootExternal,"LivingRoomTempHumid_*.log",True, True)
			
			For i = 0 To flist.Size -1
				Dim FileName As String = flist.Get(i)
				If FileName <> FileNameToday Then
					If FileName <> FileNameYesterday Then
						File.Delete(File.DirRootExternal,FileName)
					End If
				End If
			Next
			
		else If Topic = "TempHumidBasement" Then
		
			Dim status As String
			status = BytesToString(Payload, 0, Payload.Length, "UTF8")

			Dim a() As String = Regex.Split("\|",status)
			If a.Length = 9 Then
				Dim cs As CSBuilder
				cs.Initialize
				If a(0) = "OK" And a(1) > 0 Then
					StateManager.SetSetting("TempHumidityBasement",status)
					StateManager.SaveSettings
					
					' OK|81.46|58.50|4|1|83.43|65.54|18-07-21|22:22:48
					' Added "(a(4) <> 2)" as Too Cold is normal in the basement.
					' <-- removed 5/28/2019 --> Added "(a(4) <> 10)" as Cold and humid is normal in the basement. 
					If (a(3) > 3) Or ((a(4) <> 0) And (a(4) <> 2)) Then
						Dim NotificationText As String
						NotificationText = GetPerception(a(3))
					
						Dim p As Period = DateUtils.PeriodBetween(lngTicksTempHumidBasement,DateTime.now)
						Dim managerTempHumidityCooldownTime As String = StateManager.GetSetting("TempHumidityCooldownTimeBasement")
						If managerTempHumidityCooldownTime = "" Or IsNumber(managerTempHumidityCooldownTime) = False Or managerTempHumidityCooldownTime ="0" Then
							managerTempHumidityCooldownTime = 1
						End If
						If IsTempHumidityNotificationOnGoingBasement = False Then
							If p.Minutes > = managerTempHumidityCooldownTime Then
								If a(4) = 2 Or a(4) = 6 Then
									CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldbasement",Main,False,False,True,"Basement temperature").Notify(728)
								else If a(4) = 10 Then
									CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldhumidbasement",Main,False,False,True,"Basement temperature").Notify(728)
								Else
									CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempbasement",Main,False,False,True,"Basement temperature").Notify(728)
								End If
								lngTicksTempHumidBasement = DateTime.now
							End If
						Else
							Dim TempHumidityBasementPrevious() As String = Regex.Split("\|",StateManager.GetSetting("TempHumidityBasementPrevious"))
							If a(4) <> TempHumidityBasementPrevious(4) Then
								If p.Minutes > = managerTempHumidityCooldownTime Then
									If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then
										CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldbasement",Main,False,False,True,"Basement temperature").Notify(728)
									else If a(4) = 10 Then
										CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldhumidbasement",Main,False,False,True,"Basement temperature").Notify(728)
									Else
										CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempbasement",Main,False,False,True,"Basement temperature").Notify(728)
									End If
									lngTicksTempHumidBasement = DateTime.now
								End If
							else if a(3) <> TempHumidityBasementPrevious(3) Then
								If p.Minutes > = managerTempHumidityCooldownTime Then
									If a(4) = 2 Or a(4) = 6 Then
										CreateNotification("* " & GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldbasement",Main,False,False,True,"Basement temperature").Notify(728)
									else If a(4) = 10 Then
										CreateNotification("* " & GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldhumidbasement",Main,False,False,True,"Basement temperature").Notify(728)
									Else
										CreateNotification("* " & GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempbasement",Main,False,False,True,"Basement temperature").Notify(728)
									End If
									lngTicksTempHumidBasement = DateTime.now
								End If
							End If
						End If
						StateManager.SetSetting("TempHumidityBasementPrevious",status)
						StateManager.SaveSettings
					Else
						lngTicksTempHumidBasement = DateTime.now
						IsTempHumidityNotificationOnGoingBasement = False
						Notification1.Cancel(728)
					End If
				End If
			End If
			If strHumidityAddValue = "" Then
				strHumidityAddValue = "0"
			End If
			MQTT.Publish("HumidityAddValue", bc.StringToBytes(strHumidityAddValue, "utf8"))
		End If
		
		Dim managerSensorNotRespondingTime As String = StateManager.GetSetting("SensorNotRespondingTime")
		If managerSensorNotRespondingTime = "" Or IsNumber(managerSensorNotRespondingTime) = False Or managerSensorNotRespondingTime ="0" Then
			managerSensorNotRespondingTime = 1
		End If
		
		Dim status As String
		status = StateManager.GetSetting("TempHumidityBasement")
		status = status.Replace("|24:","|00:")
		Dim a() As String = Regex.Split("\|",status)
		
		Dim n As Notification 'ignore
						
		If a.Length = 9 Then
			If a(7) = "" Then
				Dim Tomorrow As Long
				Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)
				DateTime.DateFormat = "yy-MM-dd"
				a(7) = DateTime.Date(Tomorrow)
			End If
			If a(8).Contains("|24:") Then
				a(8) = a(8).Replace("|24:","|00:")
				Dim Tomorrow As Long
				Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)
				DateTime.DateFormat = "yy-MM-dd"
				a(7) = DateTime.Date(Tomorrow)
			End If
			
			DateTime.DateFormat = "yy-MM-dd HH:mm:ss z"
			Dim ticks As Long = DateTime.DateParse(a(7) & " " & a(8) & " GMT")
			DateTime.DateFormat = "MMM d, yyyy h:mm:ss a z"
			Dim lngTicks As Long = ticks
			Dim p As Period = DateUtils.PeriodBetween(lngTicks,DateTime.now)
			If p.Minutes <> 59 And p.Minutes > = managerSensorNotRespondingTime Then
				If IsOldTempHumidityNotificationOnGoingBasement = False Then
					CreateNotification("Basement DHT22 sensor is not responding", "Temperature and humidity data is " & p.Minutes & " minutes old","sensorbasement",Main,False,False,True,"Basement DHT22 sensor issue").Notify(730)
					MQTT.Publish("TempHumidBasement", bc.StringToBytes("Sensor is not working", "utf8"))
				End If
			Else
				IsOldTempHumidityNotificationOnGoingBasement = False
				n.Cancel(730)
			End If
		End If
		
		Dim status As String
		status = StateManager.GetSetting("TempHumidity")
		status = status.Replace("|24:","|00:")
		Dim a() As String = Regex.Split("\|",status)
						
		If a.Length = 9 Then
			If a(7) = "" Then
				Dim Tomorrow As Long
				Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)
				DateTime.DateFormat = "yy-MM-dd"
				a(7) = DateTime.Date(Tomorrow)
			End If
			If a(8).Contains("|24:") Then
				a(8) = a(8).Replace("|24:","|00:")
				Dim Tomorrow As Long
				Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)
				DateTime.DateFormat = "yy-MM-dd"
				a(7) = DateTime.Date(Tomorrow)
			End If
			
			DateTime.DateFormat = "yy-MM-dd HH:mm:ss z"

			Dim ticks As Long = DateTime.DateParse(a(7) & " " & a(8) & " GMT")
			DateTime.DateFormat = "MMM d, yyyy h:mm:ss a z"
			Dim lngTicks As Long = ticks
			Dim p As Period = DateUtils.PeriodBetween(lngTicks,DateTime.now)
			If p.Minutes <> 59 And p.Minutes > = managerSensorNotRespondingTime Then
				If IsOldTempHumidityNotificationOnGoing = False Then
					CreateNotification("Living area DHT22 sensor is not responding", "Temperature and humidity data is " & p.Minutes & " minutes old","sensor",Main,False,False,True,"Living area DHT22 sensor issue").Notify(729)
					MQTT.Publish("TempHumid", bc.StringToBytes("Sensor is not working", "utf8"))
				End If
			Else
				IsOldTempHumidityNotificationOnGoing = False
				n.Cancel(729)
			End If
		End If
		
		Dim status As String
		status = StateManager.GetSetting("AirQuality")
		status = status.Replace("|24:","|00:")
		Dim a() As String = Regex.Split("\|",status)
						
		If a.Length = 3 Then
			If a(1) = "" Then
				Dim Tomorrow As Long
				Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)
				DateTime.DateFormat = "yy-MM-dd"
				a(1) = DateTime.Date(Tomorrow)
			End If
			If a(2).Contains("|24:") Then
				a(2) = a(2).Replace("|24:","|00:")
				Dim Tomorrow As Long
				Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)
				DateTime.DateFormat = "yy-MM-dd"
				a(2) = DateTime.Date(Tomorrow)
			End If
			
			DateTime.DateFormat = "yy-MM-dd HH:mm:ss z"
			Dim ticks As Long = DateTime.DateParse(a(1) & " " & a(2) & " GMT")
			DateTime.DateFormat = "MMM d, yyyy h:mm:ss a z"
			Dim lngTicks As Long = ticks
			Dim p As Period = DateUtils.PeriodBetween(lngTicks,DateTime.now)
			If p.Minutes <> 59 And p.Minutes > = managerSensorNotRespondingTime Then
				If IsOldAirQualityNotificationOnGoing = False Then
					CreateNotification("Living area carbon monoxide sensor is not responding", "Air quality data is " & p.Minutes & " minutes old","sensor",Main,False,False,True,"Living area CO sensor issue").Notify(731)
					MQTT.Publish("MQ7", bc.StringToBytes("Sensor is not working", "utf8"))
				End If
			Else
				IsOldAirQualityNotificationOnGoing = False
				n.Cancel(731)
			End If
		End If
		
		Dim status As String
		status = StateManager.GetSetting("AirQualityBasement")
		status = status.Replace("|24:","|00:")
		Dim a() As String = Regex.Split("\|",status)
						
		If a.Length = 3 Then
			If a(1) = "" Then
				Dim Tomorrow As Long
				Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)
				DateTime.DateFormat = "yy-MM-dd"
				a(1) = DateTime.Date(Tomorrow)
			End If
			If a(2).Contains("|24:") Then
				a(2) = a(2).Replace("|24:","|00:")
				Dim Tomorrow As Long
				Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1)
				DateTime.DateFormat = "yy-MM-dd"
				a(2) = DateTime.Date(Tomorrow)
			End If
			
			DateTime.DateFormat = "yy-MM-dd HH:mm:ss z"
			Dim ticks As Long = DateTime.DateParse(a(1) & " " & a(2) & " GMT")
			DateTime.DateFormat = "MMM d, yyyy h:mm:ss a z"
			Dim lngTicks As Long = ticks
			Dim p As Period = DateUtils.PeriodBetween(lngTicks,DateTime.now)
			If p.Minutes <> 59 And p.Minutes > = managerSensorNotRespondingTime Then
				If IsOldAirQualityNotificationOnGoingBasement = False Then
					CreateNotification("Basement carbon monoxide sensor is not responding", "Air quality data is " & p.Minutes & " minutes old","sensorbasement",Main,False,False,True,"Basement CO sensor issue").Notify(732)
					MQTT.Publish("MQ7Basement", bc.StringToBytes("Sensor is not working", "utf8"))
				End If
			Else
				IsOldAirQualityNotificationOnGoingBasement = False
				n.Cancel(732)
			End If
		End If
		
	Catch
		Log(LastException)
		ToastMessageShow(LastException,False)
	End Try
End Sub

Sub LogEvent(TextToLog As String)
	Try
		Dim FW1 As TextWriter
		Dim FileName As String
		Dim Now As Long
		Dim Month As Int
		Dim Day As Int
		Dim Year As Int
		Dim LogEntry As String

		Now = DateTime.Now
		Month = DateTime.GetMonth(Now)
		Day = DateTime.GetDayOfMonth (Now)
		Year = DateTime.GetYear(Now)

		FileName = "LivingRoomTempHumid_" & Year & "-" & NumberFormat(Month,2,0) & "-" & NumberFormat(Day,2,0) & ".log"

'		, DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now)
		FW1.Initialize(File.OpenOutput (File.DirRootExternal, FileName, True))
		LogEntry = NumberFormat(DateTime.GetHour(Now),2,0) & ":" & NumberFormat(DateTime.GetMinute(Now),2,0)& ":" & NumberFormat(DateTime.GetSecond (Now),2,0)
		LogEntry =LogEntry & " " & TextToLog
		FW1.WriteLine(LogEntry)

		FW1.Close

	Catch
		Log("Error in Sub LogEvent: " & LastException.Message)
		ToastMessageShow(LastException,False)
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
		nb.Tag = ChannelName
		If p.SdkVersion >= 26 Then
			Dim ctxt As JavaObject
			ctxt.InitializeContext
			Dim manager As JavaObject
			manager.InitializeStatic("android.app.NotificationManager")
			Dim Channel As JavaObject
			Dim importance As String
			'If Sound Then importance = "IMPORTANCE_HIGH" Else importance = "IMPORTANCE_LOW"
'			IMPORTANCE_MAX: unused
'			IMPORTANCE_HIGH: shows everywhere, makes noise And peeks
'			IMPORTANCE_DEFAULT: shows everywhere, makes noise, but does Not visually intrude
'			IMPORTANCE_LOW: shows everywhere, but Is Not intrusive
'			IMPORTANCE_MIN: only shows in the shade, below the fold
'			IMPORTANCE_NONE: a notification with no importance; does Not show in the shade
			importance = "IMPORTANCE_LOW"
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
			localperception = "Feels like the western US, a bit dry to some"
		Case 1
			localperception = "Very comfortable"
		Case 2
			localperception = "Comfortable"
		Case 3
			localperception = "Okay but the humidity is at upper limit"
		Case 4
			localperception = "Uncomfortable, and the humidity is at upper limit"
		Case 5
			localperception = "Very humid, and quite uncomfortable"
		Case 6
			localperception = "Extremely uncomfortable, and oppressive"
		Case 7
			localperception = "Humidity is severely high, and even deadly for asthma related illnesses"
	End Select
	Return localperception
End Sub

Sub GetComfort(DHT11ComfortStatus As String) As String
	Dim localcomfortstatus As String
	Select Case DHT11ComfortStatus
		Case 0
			localcomfortstatus = "Home is OK"
		Case 1
			localcomfortstatus = "Home is too hot"
		Case 2
			localcomfortstatus = "Home is too cold"
		Case 4
			localcomfortstatus = "Home is too dry"
		Case 5
			localcomfortstatus = "Home is hot and dry"
		Case 6
			localcomfortstatus = "Home is cold and dry"
		Case 8
			localcomfortstatus = "Home is too humid"
		Case 9
			localcomfortstatus = "Home is hot and humid"
		Case 10
			localcomfortstatus = "Home is cold and humid"
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

Sub WildCardFilesList2(FilesPath As String, WildCards As String, Sorted As Boolean, Ascending As Boolean) As List 'ignore
	If File.IsDirectory("", FilesPath) Then
		Dim FilesFound As List = File.ListFiles(FilesPath)
		Dim GetCards() As String = Regex.Split(",", WildCards)
		Dim FilteredFiles As List : FilteredFiles.Initialize
		For i = 0 To FilesFound.Size -1
			For l = 0 To GetCards.Length -1
				Dim TestItem As String = FilesFound.Get(i)
				Dim mask As String = GetCards(l).Trim
				Dim pattern As String = "^"&mask.Replace(".","\.").Replace("*",".+").Replace("?",".")&"$"
				If Regex.IsMatch(pattern,TestItem) = True Then
					FilteredFiles.Add(TestItem.Trim)
				End If
			Next
		Next
		If Sorted Then
			FilteredFiles.SortCaseInsensitive(Ascending)
		End If
		Return FilteredFiles
	Else
		ToastMessageShow("You must pass a valid Directory.",False)
	End If
End Sub

