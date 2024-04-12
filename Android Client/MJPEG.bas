B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Class
Version=10
@EndOfDesignText@
Sub Class_Globals
	Private sock As Socket
	Private Astream As AsyncStreams
	Private mCallback As Object
	Private mEventName As String
	Private mHost As String
	Private mPath As String
	Private Data(1000000) As Byte
	Private index As Int
	Private bc As ByteConverter
	Private boundary As String
	Private isError As Boolean
	Private streamError As String
End Sub

Public Sub Initialize (Callback As Object, EventName As String)
	mCallback = Callback
	mEventName = EventName
End Sub

Public Sub Connect(url As String, Port As Int)
	Try
		If Astream.IsInitialized Then Astream.Close
		Dim i As Int = url.IndexOf("/")
		mPath = url.SubString(i)
		mHost = url.SubString2(0, i)
		sock.Initialize("sock")
		sock.Connect(mHost, Port, 10000)
	Catch
		Dim errorMessage As String = ""
		If LastException.IsInitialized Then
			errorMessage = "Connection error: " & LastException.Message
		End If
		If errorMessage.Trim.Length = 0 Then
			errorMessage = "Connection error"
		End If
		CallSub2(mCallback, mEventName & "_ConnectionError", errorMessage)
	End Try
End Sub

Public Sub Disconnect()
	If Astream.IsInitialized Then Astream.Close
	CallSub(mCallback, mEventName & "_Disconnected")
End Sub

Private Sub Sock_Connected (Successful As Boolean)
	If Successful Then
		boundary = ""
		Astream.Initialize(sock.InputStream, sock.OutputStream, "astream")
		Dim sTmp As String = $"GET ${mPath} HTTP/1.1
Host: ${mHost}
Connection: keep-alive

"$
		Astream.Write(sTmp.Replace(Chr(10), Chr(13) & Chr(10)).GetBytes("UTF8"))
	Else
		Dim errorMessage As String = ""
		If LastException.IsInitialized Then
			errorMessage = "Connection error: " & LastException.Message
		End If
		If errorMessage.Trim.Length = 0 Then
			errorMessage = "Connection error"
		End If
		CallSub2(mCallback, mEventName & "_ConnectionError", errorMessage)
	End If
End Sub

Private Sub AStream_NewData (Buffer() As Byte)
	Try
		bc.ArrayCopy(Buffer, 0, Data, index, Buffer.Length)
		index = index + Buffer.Length
		If boundary = "" Then
			Dim i1 As Int = IndexOfString("Content-Type", 0)
			If i1 > -1 Then
				Dim i2 As Int = IndexOfString(CRLF, i1 + 1)
				If i2 > -1 Then
					Dim ct As String = BytesToString(Data, i1, i2 - i1, "ASCII")
					Dim b As Int = ct.IndexOf("=")
					boundary = ct.SubString(b + 1)
				End If
			End If
			Dim i1 As Int = IndexOfString("HTTP/1.1", 0)
			If i1 > -1 Then
				Dim i2 As Int = IndexOfString(CRLF, i1 + 1)
				If i2 > -1 Then
					Dim ct As String = BytesToString(Data, i1, i2 - i1, "ASCII")
					Dim b As Int = ct.IndexOf("=")
					streamError = ct.SubString(b + 1)
					isError = streamError.StartsWith("HTTP/1.1 2") = False
				End If
			End If
		Else
			If isError Then
				streamError = BytesToString(Buffer, 0, Buffer.length, "utf8")
				CallSub2(mCallback, mEventName & "_StreamError",streamError)
				Astream.Close
				Return
			End If
			Dim b1 As Int = IndexOfString(boundary, 0)
			If b1 > -1 Then
				Dim b2 As Int = IndexOfString(boundary, b1 + 1)
				If b2 > -1 Then
					Dim startframe As Int = IndexOf(Array As Byte(0xff, 0xd8), b1)
					Dim endframe As Int = IndexOf(Array As Byte(0xff, 0xd9), b2 - 10)
					If startframe > -1 And endframe > -1 Then
						Dim In As InputStream
						In.InitializeFromBytesArray(Data, startframe, endframe - startframe + 2)
	#if B4J
					Dim bmp As Image
	#else
						Dim bmp As Bitmap
	#end if
						bmp.Initialize2(In)
						CallSub2(mCallback, mEventName & "_frame", bmp)
					End If
					TrimArray(b2)
				
				End If
			End If
		End If
	Catch
		Log(LastException)
	End Try
End Sub

Private Sub TrimArray (i As Int)
	bc.ArrayCopy(Data, i, Data, 0, index - i)
	index = index - i
End Sub

Private Sub IndexOfString(s As String, Start As Int) As Int
	Return IndexOf(s.GetBytes("ASCII"), Start)
End Sub

Private Sub IndexOf(bs() As Byte, Start As Int) As Int
	For i = Start To index - 1 - bs.Length
		For b = 0 To bs.Length - 1
			If bs(b) <> Data(i + b) Then Exit
		Next
		If b = bs.Length Then Return i
	Next
	Return -1
End Sub

Private Sub AStream_Error
	Try
		Dim errorMessage As String = ""
		If LastException.IsInitialized Then
			errorMessage = "Connection error: " & LastException.Message
		End If
		If errorMessage.Trim.Length = 0 Then
			errorMessage = "Connection error"
		End If
		CallSub2(mCallback, mEventName & "_ConnectionError", errorMessage)
	Catch
		Log(LastException)
	End Try
End Sub

Private Sub Astream_Terminated
	Try
		Dim errorMessage As String = ""
		If LastException.IsInitialized Then
			If LastException.Message.Contains("Object should first be initialized") = False Then
				errorMessage = "Terminated: " & LastException.Message
			End If
		End If
		If errorMessage.Trim.Length = 0 Or errorMessage.Contains("to read from field") Then
			errorMessage = "Terminated"
		End If
		CallSub2(mCallback, mEventName & "_Terminated", errorMessage)
	Catch
		Log(LastException)
	End Try
End Sub