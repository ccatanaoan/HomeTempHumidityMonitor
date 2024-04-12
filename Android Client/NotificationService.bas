B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=8
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: true
	
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
	Try
		' Log("NotificationPosted, package = " & SBN.PackageName & ", id = " & SBN.Id & ", text = " & SBN.TickerText)
		Dim p As Phone
		If p.SdkVersion >= 19 Then
			Dim jno As JavaObject = SBN.Notification
			Dim extras As JavaObject = jno.GetField("extras")
			extras.RunMethod("size", Null)
			If SBN.PackageName = "cloyd.smart.home.monitor" Then
				If SBN.Id = 726 Then
					SmartHomeMonitor.IsAirQualityNotificationOnGoing = True
				else If SBN.Id = 725 Then
					SmartHomeMonitor.IsTempHumidityNotificationOnGoing = True
				else If SBN.Id = 727 Then
					SmartHomeMonitor.IsAirQualityNotificationOnGoingBasement = True
				else If SBN.Id = 728 Then
					SmartHomeMonitor.IsTempHumidityNotificationOnGoingbasement = True
				else if SBN.Id = 730 Then
					SmartHomeMonitor.IsOldTempHumidityNotificationOnGoingBasement = True
				else if SBN.Id = 729 Then
					SmartHomeMonitor.IsOldTempHumidityNotificationOnGoing = True
				else if SBN.Id = 731 Then
					SmartHomeMonitor.IsOldAirQualityNotificationOnGoing = True
				else if SBN.Id = 732 Then
					SmartHomeMonitor.IsOldAirQualityNotificationOnGoingBasement = True
				End If
			End If
		End If
	Catch
		Log(LastException)
	End Try

End Sub

Sub Listener_NotificationRemoved (SBN As StatusBarNotification)
	Try
		'Log("NotificationRemoved, package = " & SBN.PackageName & ", id = " & SBN.Id & ", text = " & SBN.TickerText)
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
			else If SBN.Id = 730 Then
				SmartHomeMonitor.IsOldTempHumidityNotificationOnGoingBasement = False
			else If SBN.Id = 729 Then
				SmartHomeMonitor.IsOldTempHumidityNotificationOnGoing = False
			else If SBN.Id = 731 Then
				SmartHomeMonitor.IsOldAirQualityNotificationOnGoing = False
			else If SBN.Id = 732 Then
				SmartHomeMonitor.IsOldAirQualityNotificationOnGoingBasement = False
			End If
		End If
	Catch
		Log(LastException)
	End Try

End Sub

Sub Service_Destroy

End Sub