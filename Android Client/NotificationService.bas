B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=8
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	
#End Region

Sub Process_Globals
	Private listener As NotificationListener
End Sub

Sub Service_Create
	listener.Initialize("listener")
End Sub

Sub Service_Start (StartingIntent As Intent)
	If listener.HandleIntent(StartingIntent) Then Return
End Sub

Sub Listener_NotificationPosted (SBN As StatusBarNotification)
	'Log("NotificationPosted, package = " & SBN.PackageName & ", id = " & SBN.Id & _
		'", text = " & SBN.TickerText)
	Dim p As Phone
	If p.SdkVersion >= 19 Then
		Dim jno As JavaObject = SBN.Notification
		Dim extras As JavaObject = jno.GetField("extras")
		extras.RunMethod("size", Null)
		'Log(extras)
		'Dim title As String = extras.RunMethod("getString", Array As Object("android.title"))
		'LogColor("Title = " & title, Colors.Blue)
		If SBN.PackageName = "cloyd.smart.home.monitor" Then
			If SBN.Id = 726 Then
				SmartHomeMonitor.IsAirQualityNotificationOnGoing = True
			else If SBN.Id = 725 Then
				SmartHomeMonitor.IsTempHumidityNotificationOnGoing = True
			else If SBN.Id = 727 Then
				SmartHomeMonitor.IsAirQualityNotificationOnGoingBasement = True
			else If SBN.Id = 728 Then
				SmartHomeMonitor.IsTempHumidityNotificationOnGoingbasement = True
			End If
		End If
	End If
End Sub

Sub Listener_NotificationRemoved (SBN As StatusBarNotification)
	'Log("NotificationRemoved, package = " & SBN.PackageName & ", id = " & SBN.Id & _
		'", text = " & SBN.TickerText)
	If SBN.PackageName = "cloyd.smart.home.monitor" Then
		If SBN.Id = 726 Then
			SmartHomeMonitor.IsAirQualityNotificationOnGoing = False
		else If SBN.Id = 725 Then
			SmartHomeMonitor.lngTicks = DateTime.now
			SmartHomeMonitor.lngTicksTempHumid = DateTime.now
			SmartHomeMonitor.IsTempHumidityNotificationOnGoing = False
		else If SBN.Id = 727 Then
			SmartHomeMonitor.IsAirQualityNotificationOnGoingBasement = False
		else If SBN.Id = 728 Then
			SmartHomeMonitor.lngTicksTempHumidBasement = DateTime.now
			SmartHomeMonitor.IsTempHumidityNotificationOnGoingBasement = False
		End If
	End If
End Sub

Sub Service_Destroy

End Sub
