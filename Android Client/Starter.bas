B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=6.31
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	#ExcludeFromLibrary: True
#End Region

Sub Process_Globals
	Public Provider As FileProvider	
	Public kvs As KeyValueStore
	Private const FLAG_ACTIVITY_CLEAR_TOP As Int = 0x04000000
	Private const FLAG_ACTIVITY_CLEAR_TASK As Int = 0x00008000
	Private const FLAG_ACTIVITY_NEW_TASK As Int = 0x10000000
	Private const FLAG_ONE_SHOT As Int = 0x40000000
   
	Private const ALM_RTC As Int = 0x00000001
	Private const ALM_RTC_WAKEUP As Int = 0x00000000 'ignore
End Sub

Sub Service_Create
	Provider.Initialize
	kvs.Initialize(File.DirInternal, "datastore")
End Sub

Sub Service_Start (StartingIntent As Intent)

End Sub

Sub Service_TaskRemoved
	'This event will be raised when the user removes the app from the recent apps list.
End Sub

'Return true to allow the OS default exceptions handler to handle the uncaught exception.
Sub Application_Error (Error As Exception, StackTrace As String) As Boolean
	ScheduleRestartCrashedActivity(DateTime.Now + 300, "Main", Error)
	Return True
End Sub

Sub Service_Destroy

End Sub

'Based on: https://www.b4x.com/android/forum/threads/now-android-6-servicestartatexact-not-accurate.65117/#post-412286
'Time: The time that the Activity should start - in ticks
'ActivityName: The name of the Activity that should be started
'Message: A message that will be passed along in the intent to start the Activity (via Extras)
Sub ScheduleRestartCrashedActivity (Time As Long, ActivityName As String, message As String)

	Dim in As Intent
	in.Initialize("", "")
	in.SetComponent(Application.PackageName & "/." &  ActivityName.ToLowerCase)
	in.PutExtra("Crash", message)
	in.Flags = Bit.Or(FLAG_ACTIVITY_CLEAR_TASK, Bit.Or(FLAG_ACTIVITY_CLEAR_TOP, FLAG_ACTIVITY_NEW_TASK))
   
	Dim jin As JavaObject = in
	jin.RunMethod("setAction", Array(Null))
   
	Dim ctxt As JavaObject
	ctxt.InitializeContext
	Dim am As JavaObject = ctxt.RunMethod("getSystemService", Array("alarm"))
	Dim pi As JavaObject
	pi = pi.InitializeStatic("android.app.PendingIntent").RunMethod("getActivity", Array(ctxt, 0, in, FLAG_ONE_SHOT))
   
	Dim p As Phone
	If p.SdkVersion < 20 Then
		am.RunMethod("set", Array(ALM_RTC, Time, pi))
	Else If p.SdkVersion < 23 Then
		am.RunMethod("setExact", Array(ALM_RTC, Time, pi))
	Else
		am.RunMethod("setExactAndAllowWhileIdle", Array(ALM_RTC, Time, pi))
	End If
   
End Sub