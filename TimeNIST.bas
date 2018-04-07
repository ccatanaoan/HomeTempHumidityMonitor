Type=StaticCode
Version=2.2
ModulesStructureVersion=1
B4R=true
@EndOfDesignText@


Sub Process_Globals
	Private tmr As Timer
	Private socket As WiFiSocket
	Private lastMillis As ULong	
	Private astream As AsyncStreams
	Private date(8) As Byte
	Private bc As ByteConverter
	Private seconds As ULong
	Private firstTime As Boolean = True
End Sub

Public Sub Start
	tmr.Initialize("tmr_Tick", 60 * 10000) 'check with server every minute
	tmr.Enabled = True
	tmr_Tick
End Sub

Private Sub tmr_Tick
	Connect(0)
End Sub

Private Sub Connect(u As Byte)
	Log("Connecting to NIST server...")
	If socket.ConnectHost("time.nist.gov", 13) Then
		astream.Initialize(socket.Stream, "astream_NewData", "astream_Error")
	Else
		Log("Failed to connect to NIST server")
		CallSubPlus("Connect", 1000, 0)
	End If
End Sub


Private Sub AStream_NewData (Buffer() As Byte)
	'Log(Buffer)
	Dim i As Byte = 0
	For Each s() As Byte In bc.Split(Buffer, " ")
		Select i
			Case 1
				bc.ArrayCopy(s, date)
			Case 2
				Dim multi As UInt = 3600
				seconds = 0
				For Each s2() As Byte In bc.Split(s, ":")
					seconds = seconds + multi * bc.StringFromBytes(s2) 'need to convert the bytes into string before they can be parsed as a number
					multi = multi / 60
				Next
		End Select
		i = i + 1
		lastMillis = Millis
	Next
	socket.Close
	If firstTime Then
		Main.TimeIsAvailable
		firstTime = False
	End If
End Sub

Public Sub GetDate As Byte()
	Return date
End Sub

Private Sub GetUpdatedTotalSeconds As ULong
	Return seconds + (Millis - lastMillis) / 1000
End Sub

Public Sub GetHours As UInt
	Return Floor(GetUpdatedTotalSeconds/ 3600)
End Sub

Public Sub GetMinutes As UInt
	Dim minutes As Int = Floor(GetUpdatedTotalSeconds / 60)
	Return minutes Mod 60
End Sub

Public Sub GetSeconds As UInt
	Return GetUpdatedTotalSeconds Mod 60
End Sub

Private Sub AStream_Error
	'Log("Disconnected")
End Sub
