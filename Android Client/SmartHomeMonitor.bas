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
	Public lngTicks As Long
	Public lngTicksTempHumid As Long
	Public lngTicksTempHumidBasement As Long
End Sub

Sub Service_Create
	Notification1.Initialize2(Notification1.IMPORTANCE_DEFAULT)
	Service.AutomaticForegroundMode = Service.AUTOMATIC_FOREGROUND_ALWAYS
	CreateNotification("Temperature","Temperature","temp",Main,False,False,True,"Temperature")
	CreateNotification("Carbon Monoxide","Carbon Monoxide","co",Main,False,False,True,"Carbon Monoxide")
	CreateNotification("Basement Temperature","Basement Temperature","temp",Main,False,False,True,"Basement Temperature")
	CreateNotification("Basement Carbon Monoxide","Basement Carbon Monoxide","co",Main,False,False,True,"Basement Carbon Monoxide")

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
					StateManager.SetSetting("TempHumidity",status)
					StateManager.SaveSettings
					
					If DateTime.GetMinute(DateTime.Now) Mod 10 = 0 Or DateTime.GetMinute(DateTime.Now) = 59 Then
						LogEvent(status)
					End If
					
					Dim NotificationText As String
					NotificationText = "Temperature: " & a(1) & "°F | Humidity: " & a(2) & "% | Comfort: " & GetComfort(a(4))
					' OK|81.46|58.50|4|1|83.43|65.54|18-07-21|22:22:48
					If (a(3) > 3) Or (a(4) <> 0)  Then
						If IsTempHumidityNotificationOnGoing = False Then
							Dim p As Period = DateUtils.PeriodBetween(lngTicksTempHumid,DateTime.now)
							Dim managerTempHumidityCooldownTime As String = StateManager.GetSetting("TempHumidityCooldownTime")
							If managerTempHumidityCooldownTime = "" Or IsNumber(managerTempHumidityCooldownTime) = False Or managerTempHumidityCooldownTime ="0" Then
								managerTempHumidityCooldownTime = 1
							End If
							If p.Minutes > = managerTempHumidityCooldownTime Then
								If a(4) <> 0 Then
									CreateNotification(GetComfort(a(4)),NotificationText,"temp",Main,False,False,True,"Temperature").Notify(725)
								Else
									CreateNotification(GetPerception(a(3)),NotificationText,"temp",Main,False,False,True,"Temperature").Notify(725)
								End If
								lngTicksTempHumid = DateTime.now
							End If
						End If
					Else
						lngTicksTempHumid = DateTime.now
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
					NotificationText = GetAirQuality(a(0)) & ", at " & a(0) & " ppm"
					If a(0) > 400 Then
						If IsAirQualityNotificationOnGoing = False Then
							CreateNotification("Living Area Air Quality",NotificationText,"co",Main,False,False,True,"Carbon Monoxide").Notify(726)
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
			Log("MQ7 basement status: " & status)
			Dim a() As String = Regex.Split("\|",status)
			If a.Length = 3 Then
				If IsNumber(a(0)) And a(0) > 0 Then
					StateManager.SetSetting("AirQualityBasement",status)
					StateManager.SaveSettings
					
					Dim NotificationText As String
					NotificationText = GetAirQuality(a(0)) & ", at " & a(0) & " ppm"
					If a(0) > 400 Then
						If IsAirQualityNotificationOnGoingBasement = False Then
							CreateNotification("Basement Air Quality",NotificationText,"co",Main,False,False,True,"Basement Carbon Monoxide").Notify(727)
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
				Log(FileName)
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
					
					Dim NotificationText As String
					NotificationText = "Temperature: " & a(1) & "°F | Humidity: " & a(2) & "% | Comfort: " & GetComfort(a(4))
					' OK|81.46|58.50|4|1|83.43|65.54|18-07-21|22:22:48
					If (a(3) > 3) Or (a(4) <> 0)  Then
						If IsTempHumidityNotificationOnGoingBasement = False Then
							Dim p As Period = DateUtils.PeriodBetween(lngTicksTempHumidBasement,DateTime.now)
							Dim managerTempHumidityCooldownTime As String = StateManager.GetSetting("TempHumidityCooldownTimeBasement")
							If managerTempHumidityCooldownTime = "" Or IsNumber(managerTempHumidityCooldownTime) = False Or managerTempHumidityCooldownTime ="0" Then
								managerTempHumidityCooldownTime = 1
							End If
							If p.Minutes > = managerTempHumidityCooldownTime Then
								If a(4) <> 0 Then
									CreateNotification(GetComfort(a(4)).Replace("Home","Basement"),NotificationText,"temp",Main,False,False,True,"Basement Temperature").Notify(728)
								Else
									CreateNotification(GetPerception(a(3)).Replace("Home","Basement"),NotificationText,"temp",Main,False,False,True,"Basement Temperature").Notify(728)
								End If
								lngTicksTempHumidBasement = DateTime.now
							End If
						End If
					Else
						lngTicksTempHumidBasement = DateTime.now
						IsTempHumidityNotificationOnGoingBasement = False
						Notification1.Cancel(728)
					End If
				End If
			End If
		End If
	Catch
		Log(LastException)
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
		Log("Error in Sub LogEvent")
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
			localperception = "Home is uncomfortable, and the humidity is at upper limit"
		Case 5
			localperception = "Home is very humid, and quite uncomfortable"
		Case 6
			localperception = "Home is extremely uncomfortable, and oppressive"
		Case 7
			localperception = "Home humidity is severely high, and even deadly for asthma related illnesses"
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