B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=8
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: true
	#StartCommandReturnValue: android.app.Service.START_STICKY
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Private MQTT As MqttClient
	'Private MQTTUser As String = "vynckfaq1"
	'Private MQTTPassword As String = "KHSV1Q1qSUUY"
	'Private MQTTServerURI As String = "tcp://mqtt.eclipseprojects.io:1883"
	'Private MQTTServerURI As String = "tcp://broker.hivemq.com:1883"
	'Private MQTTServerURI As String = "tcp://test.mosquitto.org:1883"
	Private MQTTServerURI As String = "tcp://192.168.1.125:1883"
	Private MQTTRetryTimer As Timer
	Private SensorFreshnessTimer As Timer
	Private MQTTConnecting As Boolean
	Private MQTTRetryDelay As Int = 5000
	Private Notification1 As Notification
	Private ForegroundNotification As Notification
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
	Private rp As RuntimePermissions
	Private shared As String
	Private LastChartLogPruneDay As String
End Sub

Sub Service_Create
	MQTTRetryTimer.Initialize("MQTTRetryTimer", MQTTRetryDelay)
	MQTTRetryTimer.Enabled = False
	SensorFreshnessTimer.Initialize("SensorFreshnessTimer", 30000)
	SensorFreshnessTimer.Enabled = False

	'Use an explicit foreground service notification instead of the automatic
	'foreground notification. This gives the service its own stable notification
	'ID and marks it as ongoing so its status-bar icon is not cleared.
	Service.AutomaticForegroundMode = Service.AUTOMATIC_FOREGROUND_NEVER
	ForegroundNotification = CreateServiceNotification

	'Notification1 remains the helper object used to cancel sensor notifications.
	Notification1.Initialize2(Notification1.IMPORTANCE_DEFAULT)

	'Phase 4.28.2 migration cleanup: remove obsolete LOW-importance sensor
	'channels and B4A automatic-foreground channels from earlier builds.
	'Does not delete the current *_statusbar_v1 alert channels or the
	'smart_home_monitor_service foreground-service channel.
	DeleteObsoleteNotificationChannels

	CreateNotification("Living area temperature","Living area temperature","temp",Main,False,False,False,"Living area temperature")
	CreateNotification("Living area carbon monoxide","Living area carbon monoxide","co",Main,False,False,False,"Living area carbon monoxide")
	CreateNotification("Basement temperature","Basement temperature","tempbasement",Main,False,False,False,"Basement temperature")
	CreateNotification("Basement carbon monoxide","Basement carbon monoxide","cobasement",Main,False,False,False,"Basement carbon monoxide")
	CreateNotification("Basement DHT22 sensor issue","Basement DHT22 sensor issue","sensor",Main,False,False,False,"Basement DHT22 sensor issue")
	CreateNotification("Living area DHT22 sensor issue","Living area DHT22 sensor issue","sensor",Main,False,False,False,"Living area DHT22 sensor issue")
	CreateNotification("Living area CO sensor issue","Living area CO sensor issue","sensor",Main,False,False,False,"Living area CO sensor issue")
	CreateNotification("Basement CO sensor issue","Basement CO sensor issue","sensor",Main,False,False,False,"Basement CO sensor issue")

	'Backward-compatible migration: existing installations already have the last
	'sensor payload saved but do not yet have phone receive timestamps. Give each
	'existing value a grace-period starting now. The next real reading replaces it.
	EnsureReceiveTimestamp("TempHumidity", "TempHumidityReceivedAt")
	EnsureReceiveTimestamp("TempHumidityBasement", "TempHumidityBasementReceivedAt")
	EnsureReceiveTimestamp("AirQuality", "AirQualityReceivedAt")
	EnsureReceiveTimestamp("AirQualityBasement", "AirQualityBasementReceivedAt")
	StateManager.SaveSettings
End Sub

Private Sub DeleteObsoleteNotificationChannels
	Dim p As Phone
	If p.SdkVersion < 26 Then Return

	Try
		Dim ctxt As JavaObject
		ctxt.InitializeContext
		Dim manager As JavaObject = ctxt.RunMethod("getSystemService", Array("notification"))

		Dim OldChannelIds() As String = Array As String( _
			"Living area temperature", _
			"Living area carbon monoxide", _
			"Basement temperature", _
			"Basement carbon monoxide", _
			"Living area DHT22 sensor issue", _
			"Basement DHT22 sensor issue", _
			"Living area CO sensor issue", _
			"Basement CO sensor issue", _
			"channel_2", _
			"channel_3")

		For Each ChannelId As String In OldChannelIds
			manager.RunMethod("deleteNotificationChannel", Array(ChannelId))
		Next
	Catch
		Log("DeleteObsoleteNotificationChannels: " & LastException)
	End Try
End Sub

Sub Service_Start (StartingIntent As Intent)
	'724 is reserved for the persistent Smart Home Monitor service notification.
	'Existing sensor / warning notifications use 725 through 732.
	Service.StartForeground(724, ForegroundNotification)
	SensorFreshnessTimer.Enabled = True
	MQTT_Connect
End Sub

Sub Service_Destroy
	MQTTRetryTimer.Enabled = False
	SensorFreshnessTimer.Enabled = False
End Sub

'Connect to private GEEKOM Mosquitto broker
Sub MQTT_Connect
	Try
		If MQTTConnecting Then Return
		If MQTT.IsInitialized And MQTT.Connected Then Return

		MQTTRetryTimer.Enabled = False
		MQTTConnecting = True

		If MQTT.IsInitialized = False Then
			Dim ClientId As String = "SmartHomeMonitorService-" & Rnd(0, 999999999)
			MQTT.Initialize("MQTT", MQTTServerURI, ClientId)
		End If

		Log("Connecting to MQTT broker: " & MQTTServerURI)
		MQTT.Connect
	Catch
		MQTTConnecting = False
		Log("MQTT_Connect: " & LastException)
		ScheduleMQTTRetry
	End Try
End Sub

Sub MQTT_Connected (Success As Boolean)
	MQTTConnecting = False

	Try
		If Success = False Then
			Log("MQTT connection failed: " & LastException)
			ScheduleMQTTRetry
		Else
			MQTTRetryTimer.Enabled = False
			Log("Connected to MQTT broker: " & MQTTServerURI)
			MQTT.Subscribe("TempHumid", 0)
			MQTT.Subscribe("MQ7LivingRoomCloyd", 0)
			MQTT.Subscribe("MQ7Basement", 0)
			MQTT.Subscribe("TempHumidBasement", 0)
			MQTT.Subscribe("HumidityAddValue", 0)
		End If
	Catch
		Log("MQTT_Connected: " & LastException)
		ScheduleMQTTRetry
	End Try
End Sub

Private Sub MQTT_Disconnected
	MQTTConnecting = False
	Log("Disconnected from MQTT broker")
	ScheduleMQTTRetry
End Sub

Private Sub ScheduleMQTTRetry
	If MQTT.IsInitialized And MQTT.Connected Then Return
	MQTTRetryTimer.Enabled = False
	MQTTRetryTimer.Enabled = True
	Log("MQTT reconnect scheduled in " & MQTTRetryDelay & " ms")
End Sub

Private Sub MQTTRetryTimer_Tick
	MQTTRetryTimer.Enabled = False
	MQTT_Connect
End Sub

Private Sub SensorFreshnessTimer_Tick
	RunSensorFreshnessChecks
End Sub

Private Sub RunSensorFreshnessChecks
	Try
		'Freshness is based only on when this phone received a VALID sensor reading.
		'The ESP timestamp is intentionally not used here. This keeps monitoring
		'correct while an ESP is waiting for NTP and publishing the 1970 fallback.
		'DHT22 sensors publish about every 60 seconds.
		IsOldTempHumidityNotificationOnGoingBasement = CheckSensorFreshness( _
			"TempHumidityBasementReceivedAt", 730, "Basement DHT22", _
			"Temperature and humidity data is ", "sensorbasement", _
			"Basement DHT22 sensor issue", "TempHumidBasement", _
			"DHTSensorNotRespondingTime", 2.25, _
			IsOldTempHumidityNotificationOnGoingBasement)

		IsOldTempHumidityNotificationOnGoing = CheckSensorFreshness( _
			"TempHumidityReceivedAt", 729, "Living area DHT22", _
			"Temperature and humidity data is ", "sensor", _
			"Living area DHT22 sensor issue", "TempHumid", _
			"DHTSensorNotRespondingTime", 2.25, _
			IsOldTempHumidityNotificationOnGoing)

		'MQ-7 heater/read cycle is about 151 seconds.
		IsOldAirQualityNotificationOnGoing = CheckSensorFreshness( _
			"AirQualityReceivedAt", 731, "Living area MQ7", _
			"Air quality data is ", "sensor", _
			"Living area CO sensor issue", "MQ7LivingRoomCloyd", _
			"MQ7SensorNotRespondingTime", 6, _
			IsOldAirQualityNotificationOnGoing)

		IsOldAirQualityNotificationOnGoingBasement = CheckSensorFreshness( _
			"AirQualityBasementReceivedAt", 732, "Basement MQ7", _
			"Air quality data is ", "sensorbasement", _
			"Basement CO sensor issue", "MQ7Basement", _
			"MQ7SensorNotRespondingTime", 6, _
			IsOldAirQualityNotificationOnGoingBasement)
	Catch
		Log("RunSensorFreshnessChecks: " & LastException)
	End Try
End Sub

Private Sub RefreshActiveUI(Topic As String)
	'Only refresh the activity if it is already visible. Never launch it from
	'the background service. State has already been saved before this is called.
	If IsPaused(Main) = False Then
		CallSubDelayed2(Main, "SensorDataUpdated", Topic)
	End If
End Sub

Private Sub MQTT_MessageArrived (Topic As String, Payload() As Byte)
	Try
		Dim strHumidityAddValue As String = StateManager.GetSetting("HumidityAddValue")
		If Topic = "TempHumid" Then
		
			Dim status As String
			status = BytesToString(Payload, 0, Payload.Length, "UTF8")
			'If DateTime.GetSecond(DateTime.Now) Mod 5 = 0 Then
				LogEvent(status)
			'End If

			Dim a() As String = Regex.Split("\|",status)
			If a.Length = 9 Then
				Dim cs As CSBuilder
				cs.Initialize
				If a(0) = "OK" And a(1) > 0 Then
					StateManager.SetSetting("TempHumidity",status)
					StateManager.SetSetting("TempHumidityReceivedAt", DateTime.Now)
					StateManager.SaveSettings
					RefreshActiveUI("TempHumid")
									
					' OK|81.46|58.50|4|1|83.43|65.54|18-07-21|22:22:48
					If (a(3) > 3) Or (a(4) <> 0)  Then
						Dim NotificationText As String
						NotificationText = GetPerception(a(3))
						' OK|81.46|58.50|4|1|83.43|65.54|18-07-21|22:22:48
			
						If IsTempHumidityNotificationOnGoing = False Then
								Notification1.Cancel(725)
								If a(4) = 2 Or a(4) = 6 Then
									CreateNotification(GetComfort(a(4)),NotificationText,"tempcold",Main,False,False,False,"Living area temperature").Notify(725)
								else If a(4) = 10 Then
									CreateNotification(GetComfort(a(4)),NotificationText,"tempcoldhumid",Main,False,False,False,"Living area temperature").Notify(725)
								Else
									CreateNotification(GetComfort(a(4)),NotificationText,"temp",Main,False,False,False,"Living area temperature").Notify(725)
								End If
								lngTicksTempHumid = DateTime.now
						Else
							Dim TempHumidityPrevious() As String = Regex.Split("\|",StateManager.GetSetting("TempHumidityPrevious"))
							If a(4) <> TempHumidityPrevious(4) Then
									Notification1.Cancel(725)
									If a(4) = 2 Or a(4) = 6 Then
										CreateNotification(GetComfort(a(4)),NotificationText,"tempcold",Main,False,False,False,"Living area temperature").Notify(725)
									else If a(4) = 10 Then
										CreateNotification(GetComfort(a(4)),NotificationText,"tempcoldhumid",Main,False,False,False,"Living area temperature").Notify(725)
									Else
										CreateNotification(GetComfort(a(4)),NotificationText,"temp",Main,False,False,False,"Living area temperature").Notify(725)
									End If
									lngTicksTempHumid = DateTime.now
							else if a(3) <> TempHumidityPrevious(3) Then
									Notification1.Cancel(725)
									If a(4) = 2 Or a(4) = 6 Then
										CreateNotification("* " & GetComfort(a(4)),NotificationText,"tempcold",Main,False,False,False,"Living area temperature").Notify(725)
									else If a(4) = 10 Then
										CreateNotification("* " & GetComfort(a(4)),NotificationText,"tempcoldhumid",Main,False,False,False,"Living area temperature").Notify(725)
									Else
										CreateNotification("* " & GetComfort(a(4)),NotificationText,"temp",Main,False,False,False,"Living area temperature").Notify(725)
									End If
									lngTicksTempHumid = DateTime.now
							End If
						End If
					Else
						lngTicksTempHumid = DateTime.now
						IsTempHumidityNotificationOnGoing = False
						Notification1.Cancel(725)
					End If
					StateManager.SetSetting("TempHumidityPrevious",status)
					StateManager.SaveSettings
				End If
			End If
			
			If strHumidityAddValue = "" Then
				strHumidityAddValue = "0"
			End If
			MQTT.Publish("HumidityAddValue", bc.StringToBytes(strHumidityAddValue, "utf8"))
		Else If Topic = "MQ7LivingRoomCloyd" Then
			Dim status As String
			Dim cs As CSBuilder
			cs.Initialize
			status = BytesToString(Payload, 0, Payload.Length, "UTF8") ' MQ7 status: 334|18-04-14|00:20:54
			Dim a() As String = Regex.Split("\|",status)
			If a.Length = 3 Then
				If IsNumber(a(0)) And a(0) > 0 Then
					StateManager.SetSetting("AirQuality",status)
					StateManager.SetSetting("AirQualityReceivedAt", DateTime.Now)
					StateManager.SaveSettings
					RefreshActiveUI("MQ7LivingRoomCloyd")
					
					Dim NotificationText As String
					NotificationText = GetAirQuality((a(0)/10)) & ", at " & (a(0)/10) & " ppm"
					If (a(0)/10) > 40 Then
						If IsAirQualityNotificationOnGoing = False Then
							CreateNotification("Living Area Air Quality",NotificationText,"co",Main,False,False,False,"Living area carbon monoxide").Notify(726)
						Else
							CreateNotification("* Living Area Air Quality",NotificationText,"co",Main,False,False,False,"Living area carbon monoxide").Notify(726)
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
					StateManager.SetSetting("AirQualityBasementReceivedAt", DateTime.Now)
					StateManager.SaveSettings
					RefreshActiveUI("MQ7Basement")
					
					Dim NotificationText As String
					NotificationText = GetAirQuality((a(0)/10)) & ", at " & (a(0)/10) & " ppm"
					If (a(0)/10) > 40 Then
						If IsAirQualityNotificationOnGoingBasement = False Then
							CreateNotification("Basement Air Quality",NotificationText,"cobasement",Main,False,False,False,"Basement carbon monoxide").Notify(727)
						Else
							CreateNotification("* Basement Air Quality",NotificationText,"cobasement",Main,False,False,False,"Basement carbon monoxide").Notify(727)
						End If
					Else
						IsAirQualityNotificationOnGoingBasement = False
						Notification1.Cancel(727)
					End If
				End If
			End If
			

		else If Topic = "TempHumidBasement" Then
		
			Dim status As String
			status = BytesToString(Payload, 0, Payload.Length, "UTF8")

			Dim a() As String = Regex.Split("\|",status)
			If a.Length = 9 Then
				Dim cs As CSBuilder
				cs.Initialize
				If a(0) = "OK" And a(1) > 0 Then
					StateManager.SetSetting("TempHumidityBasement",status)
					StateManager.SetSetting("TempHumidityBasementReceivedAt", DateTime.Now)
					StateManager.SaveSettings
					RefreshActiveUI("TempHumidBasement")
					
					' OK|81.46|58.50|4|1|83.43|65.54|18-07-21|22:22:48
					' Added "(a(4) <> 2)" as Too Cold is normal in the basement.
					' <-- removed 5/28/2019 --> Added "(a(4) <> 10)" as Cold and humid is normal in the basement.
					If (a(3) > 3) Or ((a(4) <> 0) And (a(4) <> 2)) Then
						Dim NotificationText As String
						NotificationText = GetPerception(a(3))
					
						If IsTempHumidityNotificationOnGoingBasement = False Then
								Notification1.Cancel(728)
								If a(4) = 2 Or a(4) = 6 Then
									CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldbasement",Main,False,False,False,"Basement temperature").Notify(728)
								else If a(4) = 10 Then
									CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldhumidbasement",Main,False,False,False,"Basement temperature").Notify(728)
								Else
									CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempbasement",Main,False,False,False,"Basement temperature").Notify(728)
								End If
								lngTicksTempHumidBasement = DateTime.now
						Else
							Dim TempHumidityBasementPrevious() As String = Regex.Split("\|",StateManager.GetSetting("TempHumidityBasementPrevious"))
							If a(4) <> TempHumidityBasementPrevious(4) Then
									Notification1.Cancel(728)
									If a(4) = 2 Or a(4) = 6 Then
										CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldbasement",Main,False,False,False,"Basement temperature").Notify(728)
									else If a(4) = 10 Then
										CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldhumidbasement",Main,False,False,False,"Basement temperature").Notify(728)
									Else
										CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempbasement",Main,False,False,False,"Basement temperature").Notify(728)
									End If
									lngTicksTempHumidBasement = DateTime.now
							else if a(3) <> TempHumidityBasementPrevious(3) Then
									Notification1.Cancel(728)
									If a(4) = 2 Or a(4) = 6 Then
										CreateNotification("* " & GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldbasement",Main,False,False,False,"Basement temperature").Notify(728)
									else If a(4) = 10 Then
										CreateNotification("* " & GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempcoldhumidbasement",Main,False,False,False,"Basement temperature").Notify(728)
									Else
										CreateNotification("* " & GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"tempbasement",Main,False,False,False,"Basement temperature").Notify(728)
									End If
									lngTicksTempHumidBasement = DateTime.now
							End If
						End If
					Else
						lngTicksTempHumidBasement = DateTime.now
						IsTempHumidityNotificationOnGoingBasement = False
						Notification1.Cancel(728)
					End If
					StateManager.SetSetting("TempHumidityBasementPrevious",status)
					StateManager.SaveSettings
				End If
			End If
			If strHumidityAddValue = "" Then
				strHumidityAddValue = "0"
			End If
			MQTT.Publish("HumidityAddValue", bc.StringToBytes(strHumidityAddValue, "utf8"))
		End If
		
		RunSensorFreshnessChecks

	Catch
		Log(LastException)
	End Try
End Sub

Private Sub EnsureReceiveTimestamp(DataKey As String, ReceivedKey As String)
	Dim ExistingReceivedAt As String = StateManager.GetSetting(ReceivedKey)
	If ExistingReceivedAt = "" Or IsNumber(ExistingReceivedAt) = False Then
		If StateManager.GetSetting(DataKey) <> "" Then
			StateManager.SetSetting(ReceivedKey, DateTime.Now)
		End If
	End If
End Sub

Private Sub CheckSensorFreshness(ReceivedKey As String, NotificationId As Int, _
	NotificationTitle As String, MessagePrefix As String, Icon As String, _
	ChannelName As String, Topic As String, StaleSettingKey As String, _
	DefaultStaleMinutes As Double, NotificationOnGoing As Boolean) As Boolean

	Dim ReceivedText As String = StateManager.GetSetting(ReceivedKey)

	'No valid reading has been received yet. Do not manufacture a stale event.
	If ReceivedText = "" Or IsNumber(ReceivedText) = False Then
		Notification1.Cancel(NotificationId)
		Return False
	End If

	Dim ReceivedAt As Long = ReceivedText
	Dim AgeTicks As Long = DateTime.Now - ReceivedAt
	If AgeTicks < 0 Then AgeTicks = 0

	Dim StaleMinutesText As String = StateManager.GetSetting(StaleSettingKey)
	Dim StaleMinutes As Double = DefaultStaleMinutes

	If StaleMinutesText <> "" And IsNumber(StaleMinutesText) Then
		Dim ConfiguredMinutes As Double = StaleMinutesText
		If ConfiguredMinutes > 0 Then StaleMinutes = ConfiguredMinutes
	End If

	Dim StaleTicks As Long = Round(StaleMinutes * DateTime.TicksPerMinute)
	Dim AgeMinutes As Int = AgeTicks / DateTime.TicksPerMinute

	If AgeTicks >= StaleTicks Then
		If NotificationOnGoing = False Then
			CreateNotification(NotificationTitle, _
				MessagePrefix & AgeMinutes & " minutes old", _
				Icon, Main, False, False, False, ChannelName).Notify(NotificationId)

			'Latch the stale state here instead of depending on NotificationListener.
			'It is cleared only when fresh sensor data is received again.
			NotificationOnGoing = True

			If MQTT.IsInitialized And MQTT.Connected Then
				MQTT.Publish(Topic, bc.StringToBytes("Sensor is not working", "utf8"))
			End If
		End If

		Return True
	Else
		Notification1.Cancel(NotificationId)
		Return False
	End If
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

		FileName = Year & "-" & NumberFormat(Month,2,0) & "-" & NumberFormat(Day,2,0) & ".log"

		shared = rp.GetSafeDirDefaultExternal("")
		FW1.Initialize(File.OpenOutput (shared, FileName, True))
		LogEntry = NumberFormat(DateTime.GetHour(Now),2,0) & ":" & NumberFormat(DateTime.GetMinute(Now),2,0)& ":" & NumberFormat(DateTime.GetSecond (Now),2,0)
		LogEntry = LogEntry & " " & TextToLog
		FW1.WriteLine(LogEntry)

		FW1.Close
		
		'Phase 4.2: no more copied "c" lines in tomorrow's file.
		'The rolling chart reads yesterday + today directly.
		PruneOldChartLogsIfNeeded

	Catch
		Log("Error in Sub LogEvent: " & LastException.Message)
		ToastMessageShow(LastException,False)
	End Try

End Sub


' ============================================================
' CHART LOG RETENTION - PHASE 4.2
'
' Keep 30 calendar days of raw Living Room DHT22 samples.
' Pruning runs only once per day and only touches files whose names
' exactly match yyyy-MM-dd.log. Other files are never deleted here.
' ============================================================
Private Sub PruneOldChartLogsIfNeeded
	Try
		Dim Now As Long = DateTime.Now
		Dim TodayKey As String = DateTime.GetYear(Now) & "-" & _
			NumberFormat(DateTime.GetMonth(Now), 2, 0) & "-" & _
			NumberFormat(DateTime.GetDayOfMonth(Now), 2, 0)

		If LastChartLogPruneDay = TodayKey Then Return
		LastChartLogPruneDay = TodayKey

		shared = rp.GetSafeDirDefaultExternal("")
		Dim KeepFrom As Long = DateTime.Add(Now, 0, 0, -29)
		Dim CutoffDate As Long = DateUtils.SetDate( _
			DateTime.GetYear(KeepFrom), _
			DateTime.GetMonth(KeepFrom), _
			DateTime.GetDayOfMonth(KeepFrom))

		Dim FilesFound As List = WildCardFilesList2(shared, "*.log", True, True)
		For i = 0 To FilesFound.Size - 1
			Dim FileName As String = FilesFound.Get(i)
			Dim FileDate As Long = ChartLogDateFromFileName(FileName)
			If FileDate > 0 And FileDate < CutoffDate Then
				File.Delete(shared, FileName)
			End If
		Next
	Catch
		Log("PruneOldChartLogsIfNeeded: " & LastException)
	End Try
End Sub

Private Sub ChartLogDateFromFileName(FileName As String) As Long
	Try
		If Regex.IsMatch("^\d{4}-\d{2}-\d{2}\.log$", FileName) = False Then Return 0

		Dim YearText As String = FileName.SubString2(0, 4)
		Dim MonthText As String = FileName.SubString2(5, 7)
		Dim DayText As String = FileName.SubString2(8, 10)
		If IsNumber(YearText) = False Or IsNumber(MonthText) = False Or IsNumber(DayText) = False Then Return 0

		Dim YearValue As Int = YearText
		Dim MonthValue As Int = MonthText
		Dim DayValue As Int = DayText
		Return DateUtils.SetDate(YearValue, MonthValue, DayValue)
	Catch
		Return 0
	End Try
End Sub

Private Sub CreateServiceNotification As Notification
	Dim p As Phone

	If p.SdkVersion >= 21 Then
		Dim nb As NotificationBuilder
		nb.Initialize
		nb.DefaultSound = False
		nb.DefaultVibrate = False
		nb.ContentTitle = "Smart Home Monitor"
		nb.ContentText = "Service is running. Tap to open."
		nb.setActivity(Main)
		nb.OnlyAlertOnce = True
		nb.OnGoingEvent = True
		nb.SmallIcon = "icon"
		nb.Tag = "Smart Home Monitor service"

		If p.SdkVersion >= 26 Then
			Dim ctxt As JavaObject
			ctxt.InitializeContext

			Dim manager As JavaObject
			manager.InitializeStatic("android.app.NotificationManager")

			Dim Channel As JavaObject
			Dim ChannelId As String = "smart_home_monitor_service"
			Dim ChannelVisibleName As String = "Smart Home Monitor service"
			Dim importance As String = "IMPORTANCE_LOW"

			Channel.InitializeNewInstance("android.app.NotificationChannel", _
				Array(ChannelId, ChannelVisibleName, manager.GetField(importance)))
			Channel.RunMethod("setShowBadge", Array(False))

			manager = ctxt.RunMethod("getSystemService", Array("notification"))
			manager.RunMethod("createNotificationChannel", Array(Channel))

			Dim jo As JavaObject = nb
			jo.RunMethod("setChannelId", Array(ChannelId))
		End If

		Return nb.GetNotification
	Else
		Dim n As Notification
		n.Initialize
		n.Icon = "icon"
		n.Vibrate = False
		n.Sound = False
		n.AutoCancel = False
		n.OnGoingEvent = True
		n.SetInfo("Smart Home Monitor", "Service is running. Tap to open.", Main)
		Return n
	End If
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
			Dim importance As String = "IMPORTANCE_DEFAULT"
			Dim ChannelId As String
			Dim ChannelVisibleName As String = ChannelName

			'Phase 4.28:
			'All non-service alerts should remain eligible for a status-bar icon.
			'The old alert channels were created as LOW importance, and Android
			'will not let an app raise the importance of an existing channel.
			'Use fresh channel IDs at DEFAULT importance while keeping them silent.
			'
			'The foreground service notification does not use this routine.
			'CreateServiceNotification remains on smart_home_monitor_service at LOW.
			Select ChannelName
				Case "Living area temperature"
					ChannelId = "living_area_climate_alerts_statusbar_v1"
				Case "Living area carbon monoxide"
					ChannelId = "living_area_co_alerts_statusbar_v1"
				Case "Basement temperature"
					ChannelId = "basement_climate_alerts_statusbar_v1"
				Case "Basement carbon monoxide"
					ChannelId = "basement_co_alerts_statusbar_v1"
				Case "Basement DHT22 sensor issue"
					ChannelId = "basement_dht22_sensor_issue_statusbar_v1"
				Case "Living area DHT22 sensor issue"
					ChannelId = "living_area_dht22_sensor_issue_statusbar_v1"
				Case "Living area CO sensor issue"
					ChannelId = "living_area_co_sensor_issue_statusbar_v1"
				Case "Basement CO sensor issue"
					ChannelId = "basement_co_sensor_issue_statusbar_v1"
				Case Else
					'Future non-service notifications using this helper get a fresh
					'DEFAULT-importance channel instead of silently falling back to LOW.
					ChannelId = "alert_statusbar_v1_" & ChannelName
			End Select

'			IMPORTANCE_MAX: unused
'			IMPORTANCE_HIGH: shows everywhere, makes noise And peeks
'			IMPORTANCE_DEFAULT: status-bar eligible; kept silent below
'			IMPORTANCE_LOW: reserved for the foreground service notification
'			IMPORTANCE_MIN: only shows in the shade, below the fold
'			IMPORTANCE_NONE: a notification with no importance; does Not show in the shade
			Channel.InitializeNewInstance("android.app.NotificationChannel", _
                   Array(ChannelId, ChannelVisibleName, manager.GetField(importance)))

			'Keep alert channels silent even though their importance is DEFAULT.
			Channel.RunMethod("setSound", Array(Null, Null))
			Channel.RunMethod("enableVibration", Array(False))
			Channel.RunMethod("setShowBadge", Array(ShowBadge))
			manager = ctxt.RunMethod("getSystemService", Array("notification"))
			manager.RunMethod("createNotificationChannel", Array(Channel))
			Dim jo As JavaObject = nb
			jo.RunMethod("setChannelId", Array(ChannelId))
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
		' https://www.google.com/url?sa=i&source=imgres&cd=&cad=rja&uact=8&ved=2ahUKEwiQ2LSXpMznAhVkgnIEHQ67C9cQjRx6BAgBEAQ&url=https%3A%2F%2Ftwitter.com%2Fterpweather%2Fstatus%2F484003487127461889&psig=AOvVaw0h-Vtb_wN3Yy_gfmROPjFh&ust=1581606149864984
		Case 0
			localperception = "A bit dry"
		Case 1
			localperception = "Very comfortable"
		Case 2
			localperception = "Comfortable"
		Case 3
			localperception = "Okay but sticky"
		Case 4
			localperception = "Slightly uncomfortable and the humidity is at upper limit"
		Case 5
			localperception = "Very humid and uncomfortable"
		Case 6
			localperception = "Extremely uncomfortable and oppressive"
		Case 7
			localperception = "Humidity is severely high and intolerable"
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
	If number <= 10 Then
		Return("Carbon monoxide level is perfect")
	else if ((number > 10) And (number < 40)) Or number = 40 Then
		Return("Carbon monoxide level is normal")
	else if ((number > 40) And (number < 90)) Or number = 90 Then
		Return("Carbon monoxide level is high")
	else If number > 90 Then
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


