B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=10.6
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: True
	#IncludeTitle: False
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	'Private xui As XUI
	Private Awake As PhoneWakeState
	Private TemperatureHourlyTimer As Timer
	Private HumidityHourlyTimer As Timer
	Private TemperatureDailyTimer As Timer
	Private HumidityDailyTimer As Timer
	Private rp As RuntimePermissions
	Private shared As String
	Private phone1 As Phone
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	
	Private LineChart As LineChart
	Private am12 As String
	Private am1 As String
	Private am2 As String
	Private am3 As String
	Private am4 As String
	Private am5 As String
	Private am6 As String
	Private am7 As String
	Private am8 As String
	Private am9 As String
	Private am10 As String
	Private am11 As String
	Private pm12 As String
	Private pm1 As String
	Private pm2 As String
	Private pm3 As String
	Private pm4 As String
	Private pm5 As String
	Private pm6 As String
	Private pm7 As String
	Private pm8 As String
	Private pm9 As String
	Private pm10 As String
	Private pm11 As String
	Private tempRightNow As String
	Private timeRightNow As Long
	Private timeArray(24) As String
	Private zeroRange As Float = 88.88
	Private tempZeroRange As Float
	Private tempMaxRange As Float
	Private tempMinRange As Float
	Private btnHumidityHourly As Button
	Private btnTempHourly As Button
	Private btnHumidityDaily As Button
	Private btnTempDaily As Button
	Private Panel1 As Panel
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("chart")
	
	phone1.SetScreenOrientation(0) 'landscape
	
	btnTempHourly_Click
End Sub

Sub Activity_Resume
	Awake.KeepAlive(True)
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	Awake.ReleaseKeepAlive
	If UserClosed Then
		phone1.SetScreenOrientation(1) 'portrait
	End If
End Sub

Sub btnTempHourly_Click
	TemperatureDailyTimer.Enabled = False
	HumidityHourlyTimer.Enabled = False
	HumidityDailyTimer.Enabled = False
	tempMaxRange=0
	tempMinRange=0
	TemperatureHourlyCreate
	TemperatureHourlyTimer.Initialize("TemperatureHourlyTimer",1000) 'check every second
	TemperatureHourlyTimer.Enabled = True 'start timer
End Sub

Sub btnHumidityHourly_Click
	TemperatureDailyTimer.Enabled = False
	TemperatureHourlyTimer.Enabled = False
	HumidityDailyTimer.Enabled = False
	tempMaxRange=0
	tempMinRange=0
	HumidityHourlyCreate
	HumidityHourlyTimer.Initialize("HumidityHourlyTimer",1000) 'check every second
	HumidityHourlyTimer.Enabled = True 'start timer
End Sub

Sub btnTempDaily_Click
	TemperatureHourlyTimer.Enabled = False
	HumidityHourlyTimer.Enabled = False
	HumidityDailyTimer.Enabled = False
	tempMaxRange=0
	tempMinRange=0
	TemperatureDailyCreate
	TemperatureDailyTimer.Initialize("TemperatureDailyTimer",1000) 'check every second
	TemperatureDailyTimer.Enabled = True 'start timer
End Sub

Sub btnHumidityDaily_Click
	TemperatureHourlyTimer.Enabled = False
	HumidityHourlyTimer.Enabled = False
	TemperatureDailyTimer.Enabled = False
	tempMaxRange=0
	tempMinRange=0
	HumidityDailyCreate
	HumidityDailyTimer.Initialize("HumidityDailyTimer",1000) 'check every second
	HumidityDailyTimer.Enabled = True 'start timer
End Sub

Private Sub TemperatureHourlyCreate()
	Try
		'  Immersive mode
		Dim lv As LayoutValues = GetRealSize
		Dim jo As JavaObject = Activity
		jo.RunMethod("setBottom", Array(lv.Height))
		jo.RunMethod("setRight", Array(lv.Width))
		Activity.Height = lv.Height
		Activity.Width = lv.Width
'		'  Immersive mode

		Activity.LoadLayout("chart")
		
		LineChart.GraphBackgroundColor = Colors.DarkGray ' Colors.Transparent
		LineChart.GraphFrameColor = Colors.Blue
		LineChart.GraphFrameWidth = 4.0
		LineChart.GraphPlotAreaBackgroundColor = Colors.ARGB(50, 0, 0, 255) ' Colors.DarkGray
		LineChart.GraphTitleTextSize = 15
		LineChart.GraphTitleColor = Colors.White
		LineChart.GraphTitleSkewX = -0.25
		LineChart.GraphTitleUnderline = True
		LineChart.GraphTitleBold = True
		LineChart.GraphTitle = "TEMPERATURE HOURLY  "	          'put this statement last
		
		LineChart.LegendBackgroundColor = Colors.White                          'it will be converted to an Alpha = 100
		LineChart.LegendTextColor = Colors.Black
		LineChart.LegendTextSize = 18.0

		DateTime.TimeFormat = "h:mm a"
		LineChart.DomianLabel = "The time now is: " & DateTime.Time(DateTime.Now) '"TIME OF THE DAY"
		LineChart.DomainLabelColor = Colors.Green
		LineChart.DomainLabelTextSize = 25.0

		LineChart.XaxisGridLineColor = Colors.DarkGray 'Colors.ARGB(100,255,255,255)
		LineChart.XaxisGridLineWidth = 2.0
		LineChart.XaxisLabelTicks = 1
		LineChart.XaxisLabelOrientation = 0
		LineChart.XaxisLabelTextColor = Colors.White
		LineChart.XaxisLabelTextSize = 32.0
		
		' ***************************** STARTED WORK ON HOURLY *****************************
		
		timeRightNow = DateTime.Now
		
		For i = 23 To 0 Step -1
			Dim p As Period
			p.Hours = 0
			p.Minutes = (i+1) * -5
			p.Seconds = 0
			Dim NextTime As Long
			NextTime = DateUtils.AddPeriod(timeRightNow, p)
			DateTime.TimeFormat = "HH:mm"
			'Log(DateTime.Time(NextTime)) 'DateUtils.TicksToString(NextTime))
			timeArray(23-i) = DateTime.Time(NextTime) 'DateUtils.TicksToString(NextTime)
		Next
		LineChart.XAxisLabels = timeArray 'Array As String("12 am","1 am", "2 am","3 am", "4 am","5 am","6 am", "7 am","8 am","9 am","10 am","11 am", "12 pm","1 pm", "2 pm","3 pm","4 pm", "5 pm","6 pm","7 pm","8 pm","9 pm", "10 pm","11 pm")
		
		' ***************************** STARTED WORK ON HOURLY *****************************
		LineChart.YaxisDivisions = 10
		'LineChart.YaxisRange(minimumRange, maximumRange)                                 'enable this line if you want to set the y-axis minimum and maximum values - else it will be scaled automatically
		LineChart.YaxisValueFormat = LineChart.ValueFormat_2                'could be ValueFormat_0, ValueFormat_1, ValueFormat_2, or ValueFormat_3
		LineChart.YaxisGridLineColor = Colors.DarkGray
		LineChart.YaxisGridLineWidth = 2
		LineChart.YaxisLabelTicks = 1
		LineChart.YaxisLabelColor = Colors.Yellow
		LineChart.YaxisLabelOrientation = -30
		LineChart.YaxisLabelTextSize = 25.0
		LineChart.YaxisTitleColor = Colors.Green
		LineChart.YaxisTitleFakeBold = False
		LineChart.YaxisTitleTextSize = 20.0
		LineChart.YaxisTitleUnderline = True
		LineChart.YaxisTitleTextSkewness = 0
		LineChart.YaxisLabelAndTitleDistance = 60.0
		LineChart.YaxisTitle = "Temperature (Fahrenheit)"                 'put this statement last
		
		LineChart.MaxNumberOfEntriesPerLineChart = 24                   'this value must be equal to the number of x-axis labels that you pass
		LineChart.GraphLegendVisibility = False
		
		' ********************* Today *********************
		
		ReadTemperatureHourly("Today")
			
		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_1_LegendText = "From " & timeArray(0) & " to " & timeArray(23) '"Today, " & DateTime.Date(DateTime.Now)

		CheckTempBoundaries
		
		If am12 <> tempZeroRange Then
			LineChart.Line_1_Data = Array As Float (am12)
		End If
		If am1 <> tempZeroRange Then
			LineChart.Line_1_Data = Array As Float (am12, am1)
		End If
		If am2 <> tempZeroRange Then
			If am1 = tempZeroRange Then
				am1 = (am12 + am2)/2
			End If
			If am12 = tempZeroRange Then
				am12 = am1
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2)
		End If
		If am3 <> tempZeroRange Then
			If am2 = tempZeroRange Then
				am2 = (am1 + am3)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3)
		End If
		If am4 <> tempZeroRange Then
			If am3 = tempZeroRange Then
				am3 = (am2 + am4)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4)
		End If
		If am5 <> tempZeroRange Then
			If am4 = tempZeroRange Then
				am4 = (am3 + am5)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5)
		End If
		If am6 <> tempZeroRange Then
			If am5 = tempZeroRange Then
				am5 = (am4 + am6)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6)
		End If
		If am7 <> tempZeroRange Then
			If am6 = tempZeroRange Then
				am6 = (am5 + am7)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7)
		End If
		If am8 <> tempZeroRange Then
			If am7 = tempZeroRange Then
				am7 = (am6 + am8)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8)
		End If
		If am9 <> tempZeroRange Then
			If am8 = tempZeroRange Then
				am8 = (am7 + am9)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9)
		End If
		If am10 <> tempZeroRange Then
			If am9 = tempZeroRange Then
				am9 = (am8 + am10)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9, am10)
		End If
		If am11 <> tempZeroRange Then
			If am10 = tempZeroRange Then
				am10 = (am9 + am11)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11)
		End If
		If pm12 <> tempZeroRange Then
			If am11 = tempZeroRange Then
				am11 = (am10 + pm12)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12)
		End If
		If pm1 <> tempZeroRange Then
			If pm12 = tempZeroRange Then
				pm12 = (am11 + pm1)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1)
		End If
		If pm2 <> tempZeroRange Then
			If pm1 = tempZeroRange Then
				pm1 = (pm12 + pm2)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2)
		End If
		If pm3 <> tempZeroRange Then
			If pm2 = tempZeroRange Then
				pm2 = (pm1 + pm3)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3)
		End If
		If pm4 <> tempZeroRange Then
			If pm3 = tempZeroRange Then
				pm3 = (pm2 + pm4)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4)
		End If
		If pm5 <> tempZeroRange Then
			If pm4 = tempZeroRange Then
				pm4 = (pm3 + pm5)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5)
		End If
		If pm6 <> tempZeroRange Then
			If pm5 = tempZeroRange Then
				pm5 = (pm4 + pm6)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6)
		End If
		If pm7 <> tempZeroRange Then
			If pm6 = tempZeroRange Then
				pm6 = (pm5 + pm7)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7)
		End If
		If pm8 <> tempZeroRange Then
			If pm7 = tempZeroRange Then
				pm7 = (pm6 + pm8)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8)
		End If
		If pm9 <> tempZeroRange Then
			If pm8 = tempZeroRange Then
				pm8 = (pm7 + pm9)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9)
		End If
		If pm10 <> tempZeroRange Then
			If pm9 = tempZeroRange Then
				pm9 = (pm8 + pm10)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10)
		End If
		If pm11 <> tempZeroRange Then
			If pm10 = tempZeroRange Then
				pm10 = (pm9 + pm11)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10, pm11)
		End If
		
		LineChart.Line_1_PointLabelTextColor = Colors.Yellow
		LineChart.Line_1_PointLabelTextSize = 35.0
		LineChart.Line_1_LineColor = Colors.Red
		LineChart.Line_1_LineWidth = 11.0
		LineChart.Line_1_PointColor = Colors.Yellow
		LineChart.Line_1_PointSize = 25.0
		LineChart.Line_1_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_1_DrawDash = False
		LineChart.Line_1_DrawCubic = False

		' ********************* Today *********************
			
		' ******************* Last 10 minutes *******************
		
		'LineChart.Line_2_LegendText = "Compiled: March 9, 2020 10:29 am" '& DateTime.Time(DateTime.Now)
		LineChart.Line_2_Data = Array As Float (tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow,tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow)
		LineChart.Line_2_PointLabelTextColor = Colors.Green
		LineChart.Line_2_PointLabelTextSize = 35.0
		LineChart.Line_2_LineColor = Colors.Green
		LineChart.Line_2_LineWidth = 5.0
		LineChart.Line_2_PointColor = Colors.Green
		LineChart.Line_2_PointSize = 1.0
		LineChart.Line_2_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_2_DrawDash = True
		LineChart.Line_2_DrawCubic = False
		
		' ******************* Last 10 minutes *******************
		
		LineChart.NumberOfLineCharts = 2                              'set the number of graphs to draw from the 1 to 5 graph that has been set up above
			 
		LineChart.DrawTheGraphs

	Catch
		Log(LastException)
		ToastMessageShow (LastException,True)
	End Try
	
End Sub

Private Sub HumidityHourlyCreate()
	Try
		'  Immersive mode
		Activity_WindowFocusChanged(True)
		Dim lv As LayoutValues = GetRealSize
		Dim jo As JavaObject = Activity
		jo.RunMethod("setBottom", Array(lv.Height))
		jo.RunMethod("setRight", Array(lv.Width))
		Activity.Height = lv.Height
		Activity.Width = lv.Width
		'  Immersive mode

		Activity.LoadLayout("chart")
			
		LineChart.GraphBackgroundColor = Colors.DarkGray ' Colors.Transparent
		LineChart.GraphFrameColor = Colors.Blue
		LineChart.GraphFrameWidth = 4.0
		LineChart.GraphPlotAreaBackgroundColor = Colors.ARGB(50, 0, 0, 255) ' Colors.DarkGray
		LineChart.GraphTitleTextSize = 15
		LineChart.GraphTitleColor = Colors.White
		LineChart.GraphTitleSkewX = -0.25
		LineChart.GraphTitleUnderline = True
		LineChart.GraphTitleBold = True
		LineChart.GraphTitle = " HUMIDITY HOURLY  "	          'put this statement last
		
		LineChart.LegendBackgroundColor = Colors.White                          'it will be converted to an Alpha = 100
		LineChart.LegendTextColor = Colors.Black
		LineChart.LegendTextSize = 18.0

		DateTime.TimeFormat = "h:mm a"
		LineChart.DomianLabel = "The time now is: " & DateTime.Time(DateTime.Now) '"TIME OF THE DAY"
		LineChart.DomainLabelColor = Colors.Green
		LineChart.DomainLabelTextSize = 25.0

		LineChart.XaxisGridLineColor = Colors.DarkGray
		LineChart.XaxisGridLineWidth = 2.0
		LineChart.XaxisLabelTicks = 1
		LineChart.XaxisLabelOrientation = 0
		LineChart.XaxisLabelTextColor = Colors.White
		LineChart.XaxisLabelTextSize = 32.0
		
		' ***************************** STARTED WORK ON HOURLY *****************************
		
		timeRightNow = DateTime.Now
		
		For i = 23 To 0 Step -1
			Dim p As Period
			p.Hours = 0
			p.Minutes = (i+1) * -5
			p.Seconds = 0
			Dim NextTime As Long
			NextTime = DateUtils.AddPeriod(timeRightNow, p)
			DateTime.TimeFormat = "HH:mm"
			'Log(DateTime.Time(NextTime)) 'DateUtils.TicksToString(NextTime))
			timeArray(23-i) = DateTime.Time(NextTime) 'DateUtils.TicksToString(NextTime)
		Next
		LineChart.XAxisLabels = timeArray 'Array As String("12 am","1 am", "2 am","3 am", "4 am","5 am","6 am", "7 am","8 am","9 am","10 am","11 am", "12 pm","1 pm", "2 pm","3 pm","4 pm", "5 pm","6 pm","7 pm","8 pm","9 pm", "10 pm","11 pm")
		
		' ***************************** STARTED WORK ON HOURLY *****************************
		LineChart.YaxisDivisions = 10
		'LineChart.YaxisRange(minimumRange, maximumRange)                                 'enable this line if you want to set the y-axis minimum and maximum values - else it will be scaled automatically
		LineChart.YaxisValueFormat = LineChart.ValueFormat_2                'could be ValueFormat_0, ValueFormat_1, ValueFormat_2, or ValueFormat_3
		LineChart.YaxisGridLineColor = Colors.DarkGray
		LineChart.YaxisGridLineWidth = 2
		LineChart.YaxisLabelTicks = 1
		LineChart.YaxisLabelColor = Colors.Yellow
		LineChart.YaxisLabelOrientation = -30
		LineChart.YaxisLabelTextSize = 25.0
		LineChart.YaxisTitleColor = Colors.Green
		LineChart.YaxisTitleFakeBold = False
		LineChart.YaxisTitleTextSize = 20.0
		LineChart.YaxisTitleUnderline = True
		LineChart.YaxisTitleTextSkewness = 0
		LineChart.YaxisLabelAndTitleDistance = 60.0
		LineChart.YaxisTitle = "Humidity (Percentage)"                 'put this statement last
		
		LineChart.MaxNumberOfEntriesPerLineChart = 24                   'this value must be equal to the number of x-axis labels that you pass
		LineChart.GraphLegendVisibility = False
		
		' ********************* Today *********************
		
		ReadHumidityHourly("Today")
			
		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_1_LegendText = "From " & timeArray(0) & " to " & timeArray(23) '"Today, " & DateTime.Date(DateTime.Now)

		CheckTempBoundaries
		
		If am12 <> tempZeroRange Then
			LineChart.Line_1_Data = Array As Float (am12)
		End If
		If am1 <> tempZeroRange Then
			LineChart.Line_1_Data = Array As Float (am12, am1)
		End If
		If am2 <> tempZeroRange Then
			If am1 = tempZeroRange Then
				am1 = (am12 + am2)/2
			End If
			If am12 = tempZeroRange Then
				am12 = am1
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2)
		End If
		If am3 <> tempZeroRange Then
			If am2 = tempZeroRange Then
				am2 = (am1 + am3)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3)
		End If
		If am4 <> tempZeroRange Then
			If am3 = tempZeroRange Then
				am3 = (am2 + am4)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4)
		End If
		If am5 <> tempZeroRange Then
			If am4 = tempZeroRange Then
				am4 = (am3 + am5)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5)
		End If
		If am6 <> tempZeroRange Then
			If am5 = tempZeroRange Then
				am5 = (am4 + am6)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6)
		End If
		If am7 <> tempZeroRange Then
			If am6 = tempZeroRange Then
				am6 = (am5 + am7)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7)
		End If
		If am8 <> tempZeroRange Then
			If am7 = tempZeroRange Then
				am7 = (am6 + am8)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8)
		End If
		If am9 <> tempZeroRange Then
			If am8 = tempZeroRange Then
				am8 = (am7 + am9)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9)
		End If
		If am10 <> tempZeroRange Then
			If am9 = tempZeroRange Then
				am9 = (am8 + am10)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9, am10)
		End If
		If am11 <> tempZeroRange Then
			If am10 = tempZeroRange Then
				am10 = (am9 + am11)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11)
		End If
		If pm12 <> tempZeroRange Then
			If am11 = tempZeroRange Then
				am11 = (am10 + pm12)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12)
		End If
		If pm1 <> tempZeroRange Then
			If pm12 = tempZeroRange Then
				pm12 = (am11 + pm1)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1)
		End If
		If pm2 <> tempZeroRange Then
			If pm1 = tempZeroRange Then
				pm1 = (pm12 + pm2)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2)
		End If
		If pm3 <> tempZeroRange Then
			If pm2 = tempZeroRange Then
				pm2 = (pm1 + pm3)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3)
		End If
		If pm4 <> tempZeroRange Then
			If pm3 = tempZeroRange Then
				pm3 = (pm2 + pm4)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4)
		End If
		If pm5 <> tempZeroRange Then
			If pm4 = tempZeroRange Then
				pm4 = (pm3 + pm5)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5)
		End If
		If pm6 <> tempZeroRange Then
			If pm5 = tempZeroRange Then
				pm5 = (pm4 + pm6)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6)
		End If
		If pm7 <> tempZeroRange Then
			If pm6 = tempZeroRange Then
				pm6 = (pm5 + pm7)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7)
		End If
		If pm8 <> tempZeroRange Then
			If pm7 = tempZeroRange Then
				pm7 = (pm6 + pm8)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8)
		End If
		If pm9 <> tempZeroRange Then
			If pm8 = tempZeroRange Then
				pm8 = (pm7 + pm9)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9)
		End If
		If pm10 <> tempZeroRange Then
			If pm9 = tempZeroRange Then
				pm9 = (pm8 + pm10)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10)
		End If
		If pm11 <> tempZeroRange Then
			If pm10 = tempZeroRange Then
				pm10 = (pm9 + pm11)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10, pm11)
		End If
		
		LineChart.Line_1_PointLabelTextColor = Colors.Yellow
		LineChart.Line_1_PointLabelTextSize = 35.0
		LineChart.Line_1_LineColor = Colors.Red
		LineChart.Line_1_LineWidth = 11.0
		LineChart.Line_1_PointColor = Colors.Yellow
		LineChart.Line_1_PointSize = 25.0
		LineChart.Line_1_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_1_DrawDash = False
		LineChart.Line_1_DrawCubic = False

		' ********************* Today *********************
			
		' ******************* Last 10 minutes *******************
		
		'LineChart.Line_2_LegendText = "Compiled: March 9, 2020 10:29 am" '& DateTime.Time(DateTime.Now)
		LineChart.Line_2_Data = Array As Float (tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow,tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow)
		LineChart.Line_2_PointLabelTextColor = Colors.Green
		LineChart.Line_2_PointLabelTextSize = 35.0
		LineChart.Line_2_LineColor = Colors.Green
		LineChart.Line_2_LineWidth = 5.0
		LineChart.Line_2_PointColor = Colors.Green
		LineChart.Line_2_PointSize = 1.0
		LineChart.Line_2_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_2_DrawDash = True
		LineChart.Line_2_DrawCubic = False
		
		' ******************* Last 10 minutes *******************
		
		LineChart.NumberOfLineCharts = 2                              'set the number of graphs to draw from the 1 to 5 graph that has been set up above
			 
		LineChart.DrawTheGraphs
		
	Catch
		Log(LastException)
		ToastMessageShow (LastException,True)
	End Try
End Sub

Private Sub TemperatureDailyCreate()
	Try
		'  Immersive mode
		Activity_WindowFocusChanged(True)
		Dim lv As LayoutValues = GetRealSize
		Dim jo As JavaObject = Activity
		jo.RunMethod("setBottom", Array(lv.Height))
		jo.RunMethod("setRight", Array(lv.Width))
		Activity.Height = lv.Height
		Activity.Width = lv.Width
		'  Immersive mode
		
		Activity.LoadLayout("chart")
		
		LineChart.GraphBackgroundColor = Colors.DarkGray ' Colors.Transparent
		LineChart.GraphFrameColor = Colors.Blue
		LineChart.GraphFrameWidth = 4.0
		LineChart.GraphPlotAreaBackgroundColor = Colors.ARGB(50, 0, 0, 255) ' Colors.DarkGray
		LineChart.GraphTitleTextSize = 15
		LineChart.GraphTitleColor = Colors.White
		LineChart.GraphTitleSkewX = -0.25
		LineChart.GraphTitleUnderline = True
		LineChart.GraphTitleBold = True
		LineChart.GraphTitle = "TEMPERATURE DAILY  "	          'put this statement last
		
		LineChart.LegendBackgroundColor = Colors.White                          'it will be converted to an Alpha = 100
		LineChart.LegendTextColor = Colors.Black
		LineChart.LegendTextSize = 18.0

		DateTime.TimeFormat = "h:mm a"
		LineChart.DomianLabel = "The time now is: " & DateTime.Time(DateTime.Now) '"TIME OF THE DAY"
		LineChart.DomainLabelColor = Colors.Green
		LineChart.DomainLabelTextSize = 25.0

		LineChart.XaxisGridLineColor = Colors.DarkGray
		LineChart.XaxisGridLineWidth = 2.0
		LineChart.XaxisLabelTicks = 1
		LineChart.XaxisLabelOrientation = 0
		LineChart.XaxisLabelTextColor = Colors.White
		LineChart.XaxisLabelTextSize = 32.0
		LineChart.XAxisLabels = Array As String("12 am","1 am", "2 am","3 am", "4 am","5 am","6 am", "7 am","8 am","9 am","10 am","11 am", "12 pm","1 pm", "2 pm","3 pm","4 pm", "5 pm","6 pm","7 pm","8 pm","9 pm", "10 pm","11 pm")
		
		LineChart.YaxisDivisions = 10
		'LineChart.YaxisRange(minimumRange, maximumRange)                                'enable this line if you want to set the y-axis minimum and maximum values - else it will be scaled automatically
		LineChart.YaxisValueFormat = LineChart.ValueFormat_2                'could be ValueFormat_0, ValueFormat_1, ValueFormat_2, or ValueFormat_3
		LineChart.YaxisGridLineColor = Colors.DarkGray
		LineChart.YaxisGridLineWidth = 2
		LineChart.YaxisLabelTicks = 1
		LineChart.YaxisLabelColor = Colors.Yellow
		LineChart.YaxisLabelOrientation = -30
		LineChart.YaxisLabelTextSize = 25.0
		LineChart.YaxisTitleColor = Colors.Green
		LineChart.YaxisTitleFakeBold = False
		LineChart.YaxisTitleTextSize = 20.0
		LineChart.YaxisTitleUnderline = True
		LineChart.YaxisTitleTextSkewness = 0
		LineChart.YaxisLabelAndTitleDistance = 60.0
		LineChart.YaxisTitle = "Temperature (Fahrenheit)"                 'put this statement last
		
		LineChart.MaxNumberOfEntriesPerLineChart = 24                   'this value must be equal to the number of x-axis labels that you pass
		LineChart.GraphLegendVisibility = False
		
		' ********************* Today *********************
		
		ReadTemperatureDaily("Today")
			
		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_1_LegendText = "Today, " & DateTime.Date(DateTime.Now)
		
		CheckTempBoundariesDaily
		
		If am12 <> tempZeroRange Then
			LineChart.Line_1_Data = Array As Float (am12)
		End If
		If am1 <> tempZeroRange Then
			LineChart.Line_1_Data = Array As Float (am12, am1)
		End If
		If am2 <> tempZeroRange Then
			If am1 = tempZeroRange Then
				am1 = (am12 + am2)/2
			End If
			If am12 = tempZeroRange Then
				am12 = am1
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2)
		End If
		If am3 <> tempZeroRange Then
			If am2 = tempZeroRange Then
				am2 = (am1 + am3)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3)
		End If
		If am4 <> tempZeroRange Then
			If am3 = tempZeroRange Then
				am3 = (am2 + am4)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4)
		End If
		If am5 <> tempZeroRange Then
			If am4 = tempZeroRange Then
				am4 = (am3 + am5)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5)
		End If
		If am6 <> tempZeroRange Then
			If am5 = tempZeroRange Then
				am5 = (am4 + am6)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6)
		End If
		If am7 <> tempZeroRange Then
			If am6 = tempZeroRange Then
				am6 = (am5 + am7)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7)
		End If
		If am8 <> tempZeroRange Then
			If am7 = tempZeroRange Then
				am7 = (am6 + am8)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8)
		End If
		If am9 <> tempZeroRange Then
			If am8 = tempZeroRange Then
				am8 = (am7 + am9)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9)
		End If
		If am10 <> tempZeroRange Then
			If am9 = tempZeroRange Then
				am9 = (am8 + am10)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9, am10)
		End If
		If am11 <> tempZeroRange Then
			If am10 = tempZeroRange Then
				am10 = (am9 + am11)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11)
		End If
		If pm12 <> tempZeroRange Then
			If am11 = tempZeroRange Then
				am11 = (am10 + pm12)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12)
		End If
		If pm1 <> tempZeroRange Then
			If pm12 = tempZeroRange Then
				pm12 = (am11 + pm1)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1)
		End If
		If pm2 <> tempZeroRange Then
			If pm1 = tempZeroRange Then
				pm1 = (pm12 + pm2)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2)
		End If
		If pm3 <> tempZeroRange Then
			If pm2 = tempZeroRange Then
				pm2 = (pm1 + pm3)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3)
		End If
		If pm4 <> tempZeroRange Then
			If pm3 = tempZeroRange Then
				pm3 = (pm2 + pm4)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4)
		End If
		If pm5 <> tempZeroRange Then
			If pm4 = tempZeroRange Then
				pm4 = (pm3 + pm5)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5)
		End If
		If pm6 <> tempZeroRange Then
			If pm5 = tempZeroRange Then
				pm5 = (pm4 + pm6)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6)
		End If
		If pm7 <> tempZeroRange Then
			If pm6 = tempZeroRange Then
				pm6 = (pm5 + pm7)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7)
		End If
		If pm8 <> tempZeroRange Then
			If pm7 = tempZeroRange Then
				pm7 = (pm6 + pm8)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8)
		End If
		If pm9 <> tempZeroRange Then
			If pm8 = tempZeroRange Then
				pm8 = (pm7 + pm9)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9)
		End If
		If pm10 <> tempZeroRange Then
			If pm9 = tempZeroRange Then
				pm9 = (pm8 + pm10)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10)
		End If
		If pm11 <> tempZeroRange Then
			If pm10 = tempZeroRange Then
				pm10 = (pm9 + pm11)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10, pm11)
		End If
					
		LineChart.Line_1_PointLabelTextColor = Colors.Yellow
		LineChart.Line_1_PointLabelTextSize = 35.0
		LineChart.Line_1_LineColor = Colors.Red
		LineChart.Line_1_LineWidth = 11.0
		LineChart.Line_1_PointColor = Colors.Yellow
		LineChart.Line_1_PointSize = 25.0
		LineChart.Line_1_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_1_DrawDash = False
		LineChart.Line_1_DrawCubic = False

		' ********************* Today *********************
		
		' ******************* Yesterday *******************

		ReadTemperatureDaily("Yesterday")
			
		Dim Yesterday As Long
		Yesterday = DateTime.add(DateTime.Now, 0, 0, -1)

		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_2_LegendText = "Yesterday, " & DateTime.Date(Yesterday)
		
		CheckTempBoundariesDaily
		
		If am12 <> tempZeroRange Then
			LineChart.Line_2_Data = Array As Float (am12)
		End If
		If am1 <> tempZeroRange Then
			LineChart.Line_2_Data = Array As Float (am12, am1)
		End If
		If am2 <> tempZeroRange Then
			If am1 = tempZeroRange Then
				am1 = (am12 + am2)/2
			End If
			If am12 = tempZeroRange Then
				am12 = am1
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2)
		End If
		If am3 <> tempZeroRange Then
			If am2 = tempZeroRange Then
				am2 = (am1 + am3)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3)
		End If
		If am4 <> tempZeroRange Then
			If am3 = tempZeroRange Then
				am3 = (am2 + am4)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4)
		End If
		If am5 <> tempZeroRange Then
			If am4 = tempZeroRange Then
				am4 = (am3 + am5)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5)
		End If
		If am6 <> tempZeroRange Then
			If am5 = tempZeroRange Then
				am5 = (am4 + am6)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6)
		End If
		If am7 <> tempZeroRange Then
			If am6 = tempZeroRange Then
				am6 = (am5 + am7)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7)
		End If
		If am8 <> tempZeroRange Then
			If am7 = tempZeroRange Then
				am7 = (am6 + am8)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8)
		End If
		If am9 <> tempZeroRange Then
			If am8 = tempZeroRange Then
				am8 = (am7 + am9)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9)
		End If
		If am10 <> tempZeroRange Then
			If am9 = tempZeroRange Then
				am9 = (am8 + am10)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9, am10)
		End If
		If am11 <> tempZeroRange Then
			If am10 = tempZeroRange Then
				am10 = (am9 + am11)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11)
		End If
		If pm12 <> tempZeroRange Then
			If am11 = tempZeroRange Then
				am11 = (am10 + pm12)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12)
		End If
		If pm1 <> tempZeroRange Then
			If pm12 = tempZeroRange Then
				pm12 = (am11 + pm1)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1)
		End If
		If pm2 <> tempZeroRange Then
			If pm1 = tempZeroRange Then
				pm1 = (pm12 + pm2)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2)
		End If
		If pm3 <> tempZeroRange Then
			If pm2 = tempZeroRange Then
				pm2 = (pm1 + pm3)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3)
		End If
		If pm4 <> tempZeroRange Then
			If pm3 = tempZeroRange Then
				pm3 = (pm2 + pm4)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4)
		End If
		If pm5 <> tempZeroRange Then
			If pm4 = tempZeroRange Then
				pm4 = (pm3 + pm5)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5)
		End If
		If pm6 <> tempZeroRange Then
			If pm5 = tempZeroRange Then
				pm5 = (pm4 + pm6)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6)
		End If
		If pm7 <> tempZeroRange Then
			If pm6 = tempZeroRange Then
				pm6 = (pm5 + pm7)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7)
		End If
		If pm8 <> tempZeroRange Then
			If pm7 = tempZeroRange Then
				pm7 = (pm6 + pm8)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8)
		End If
		If pm9 <> tempZeroRange Then
			If pm8 = tempZeroRange Then
				pm8 = (pm7 + pm9)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9)
		End If
		If pm10 <> tempZeroRange Then
			If pm9 = tempZeroRange Then
				pm9 = (pm8 + pm10)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10)
		End If
		If pm11 <> tempZeroRange Then
			If pm10 = tempZeroRange Then
				pm10 = (pm9 + pm11)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10, pm11)
		End If
			
		LineChart.Line_2_PointLabelTextColor = Colors.Cyan
		LineChart.Line_2_PointLabelTextSize = 35.0
		LineChart.Line_2_LineColor = Colors.White
		LineChart.Line_2_LineWidth = 7.0
		LineChart.Line_2_PointColor = Colors.Cyan
		LineChart.Line_2_PointSize = 10.0
		LineChart.Line_2_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_2_DrawDash = False
		LineChart.Line_2_DrawCubic = False
		
		' ******************* Yesterday *******************
		
		' ******************* Last 10 minutes *******************
		
		LineChart.Line_3_LegendText = "Real time"
		LineChart.Line_3_Data = Array As Float (tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow,tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow)
		LineChart.Line_3_PointLabelTextColor = Colors.Green
		LineChart.Line_3_PointLabelTextSize = 35.0
		LineChart.Line_3_LineColor = Colors.Green
		LineChart.Line_3_LineWidth = 5.0
		LineChart.Line_3_PointColor = Colors.Green
		LineChart.Line_3_PointSize = 1.0
		LineChart.Line_3_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_3_DrawDash = True
		LineChart.Line_3_DrawCubic = False
		
		' ******************* Last 10 minutes *******************
		
		LineChart.NumberOfLineCharts = 3                              'set the number of graphs to draw from the 1 to 5 graph that has been set up above
			 
		LineChart.DrawTheGraphs

	Catch
		Log(LastException)
		ToastMessageShow (LastException,True)
	End Try
End Sub

Private Sub HumidityDailyCreate()
	Try
		'  Immersive mode
		Activity_WindowFocusChanged(True)
		Dim lv As LayoutValues = GetRealSize
		Dim jo As JavaObject = Activity
		jo.RunMethod("setBottom", Array(lv.Height))
		jo.RunMethod("setRight", Array(lv.Width))
		Activity.Height = lv.Height
		Activity.Width = lv.Width
		'  Immersive mode
		
		Activity.LoadLayout("chart")
		
		LineChart.GraphBackgroundColor = Colors.DarkGray 'Colors.Transparent
		LineChart.GraphFrameColor = Colors.Blue
		LineChart.GraphFrameWidth = 4.0
		LineChart.GraphPlotAreaBackgroundColor = Colors.ARGB(50, 0, 0, 255) 'Colors.DarkGray
		LineChart.GraphTitleTextSize = 15
		LineChart.GraphTitleColor = Colors.White
		LineChart.GraphTitleSkewX = -0.25
		LineChart.GraphTitleUnderline = True
		LineChart.GraphTitleBold = True
		LineChart.GraphTitle = " HUMIDITY DAILY  "	          'put this statement last
		
		LineChart.LegendBackgroundColor = Colors.White                          'it will be converted to an Alpha = 100
		LineChart.LegendTextColor = Colors.Black
		LineChart.LegendTextSize = 18.0

		DateTime.TimeFormat = "h:mm a"
		LineChart.DomianLabel = "The time now is: " & DateTime.Time(DateTime.Now) '"TIME OF THE DAY"
		LineChart.DomainLabelColor = Colors.Green
		LineChart.DomainLabelTextSize = 25.0

		LineChart.XaxisGridLineColor = Colors.DarkGray
		LineChart.XaxisGridLineWidth = 2.0
		LineChart.XaxisLabelTicks = 1
		LineChart.XaxisLabelOrientation = 0
		LineChart.XaxisLabelTextColor = Colors.White
		LineChart.XaxisLabelTextSize = 32.0
		LineChart.XAxisLabels = Array As String("12 am","1 am", "2 am","3 am", "4 am","5 am","6 am", "7 am","8 am","9 am","10 am","11 am", "12 pm","1 pm", "2 pm","3 pm","4 pm", "5 pm","6 pm","7 pm","8 pm","9 pm", "10 pm","11 pm")
		
		LineChart.YaxisDivisions = 10
		'LineChart.YaxisRange(minimumRange, maximumRange)                                'enable this line if you want to set the y-axis minimum and maximum values - else it will be scaled automatically
		LineChart.YaxisValueFormat = LineChart.ValueFormat_2                'could be ValueFormat_0, ValueFormat_1, ValueFormat_2, or ValueFormat_3
		LineChart.YaxisGridLineColor = Colors.DarkGray
		LineChart.YaxisGridLineWidth = 2
		LineChart.YaxisLabelTicks = 1
		LineChart.YaxisLabelColor = Colors.Yellow
		LineChart.YaxisLabelOrientation = -30
		LineChart.YaxisLabelTextSize = 25.0
		LineChart.YaxisTitleColor = Colors.Green
		LineChart.YaxisTitleFakeBold = False
		LineChart.YaxisTitleTextSize = 20.0
		LineChart.YaxisTitleUnderline = True
		LineChart.YaxisTitleTextSkewness = 0
		LineChart.YaxisLabelAndTitleDistance = 60.0
		LineChart.YaxisTitle = "Humidity (Percentage)"                 'put this statement last
		
		LineChart.MaxNumberOfEntriesPerLineChart = 24                   'this value must be equal to the number of x-axis labels that you pass
		LineChart.GraphLegendVisibility = False
		
		' ********************* Today *********************
		
		ReadHumidityDaily("Today")
			
		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_1_LegendText = "Today, " & DateTime.Date(DateTime.Now)
		
		CheckTempBoundariesDaily
		
		If am12 <> tempZeroRange Then
			LineChart.Line_1_Data = Array As Float (am12)
		End If
		If am1 <> tempZeroRange Then
			LineChart.Line_1_Data = Array As Float (am12, am1)
		End If
		If am2 <> tempZeroRange Then
			If am1 = tempZeroRange Then
				am1 = (am12 + am2)/2
			End If
			If am12 = tempZeroRange Then
				am12 = am1
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2)
		End If
		If am3 <> tempZeroRange Then
			If am2 = tempZeroRange Then
				am2 = (am1 + am3)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3)
		End If
		If am4 <> tempZeroRange Then
			If am3 = tempZeroRange Then
				am3 = (am2 + am4)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4)
		End If
		If am5 <> tempZeroRange Then
			If am4 = tempZeroRange Then
				am4 = (am3 + am5)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5)
		End If
		If am6 <> tempZeroRange Then
			If am5 = tempZeroRange Then
				am5 = (am4 + am6)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6)
		End If
		If am7 <> tempZeroRange Then
			If am6 = tempZeroRange Then
				am6 = (am5 + am7)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7)
		End If
		If am8 <> tempZeroRange Then
			If am7 = tempZeroRange Then
				am7 = (am6 + am8)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8)
		End If
		If am9 <> tempZeroRange Then
			If am8 = tempZeroRange Then
				am8 = (am7 + am9)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9)
		End If
		If am10 <> tempZeroRange Then
			If am9 = tempZeroRange Then
				am9 = (am8 + am10)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9, am10)
		End If
		If am11 <> tempZeroRange Then
			If am10 = tempZeroRange Then
				am10 = (am9 + am11)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11)
		End If
		If pm12 <> tempZeroRange Then
			If am11 = tempZeroRange Then
				am11 = (am10 + pm12)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12)
		End If
		If pm1 <> tempZeroRange Then
			If pm12 = tempZeroRange Then
				pm12 = (am11 + pm1)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1)
		End If
		If pm2 <> tempZeroRange Then
			If pm1 = tempZeroRange Then
				pm1 = (pm12 + pm2)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2)
		End If
		If pm3 <> tempZeroRange Then
			If pm2 = tempZeroRange Then
				pm2 = (pm1 + pm3)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3)
		End If
		If pm4 <> tempZeroRange Then
			If pm3 = tempZeroRange Then
				pm3 = (pm2 + pm4)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4)
		End If
		If pm5 <> tempZeroRange Then
			If pm4 = tempZeroRange Then
				pm4 = (pm3 + pm5)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5)
		End If
		If pm6 <> tempZeroRange Then
			If pm5 = tempZeroRange Then
				pm5 = (pm4 + pm6)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6)
		End If
		If pm7 <> tempZeroRange Then
			If pm6 = tempZeroRange Then
				pm6 = (pm5 + pm7)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7)
		End If
		If pm8 <> tempZeroRange Then
			If pm7 = tempZeroRange Then
				pm7 = (pm6 + pm8)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8)
		End If
		If pm9 <> tempZeroRange Then
			If pm8 = tempZeroRange Then
				pm8 = (pm7 + pm9)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9)
		End If
		If pm10 <> tempZeroRange Then
			If pm9 = tempZeroRange Then
				pm9 = (pm8 + pm10)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10)
		End If
		If pm11 <> tempZeroRange Then
			If pm10 = tempZeroRange Then
				pm10 = (pm9 + pm11)/2
			End If
			LineChart.Line_1_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10, pm11)
		End If
					
		LineChart.Line_1_PointLabelTextColor = Colors.Yellow
		LineChart.Line_1_PointLabelTextSize = 35.0
		LineChart.Line_1_LineColor = Colors.Red
		LineChart.Line_1_LineWidth = 11.0
		LineChart.Line_1_PointColor = Colors.Yellow
		LineChart.Line_1_PointSize = 25.0
		LineChart.Line_1_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_1_DrawDash = False
		LineChart.Line_1_DrawCubic = False

		' ********************* Today *********************
		
		' ******************* Yesterday *******************

		ReadHumidityDaily("Yesterday")
			
		Dim Yesterday As Long
		Yesterday = DateTime.add(DateTime.Now, 0, 0, -1)

		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_2_LegendText = "Yesterday, " & DateTime.Date(Yesterday)
		
		CheckTempBoundariesDaily
		
		If am12 <> tempZeroRange Then
			LineChart.Line_2_Data = Array As Float (am12)
		End If
		If am1 <> tempZeroRange Then
			LineChart.Line_2_Data = Array As Float (am12, am1)
		End If
		If am2 <> tempZeroRange Then
			If am1 = tempZeroRange Then
				am1 = (am12 + am2)/2
			End If
			If am12 = tempZeroRange Then
				am12 = am1
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2)
		End If
		If am3 <> tempZeroRange Then
			If am2 = tempZeroRange Then
				am2 = (am1 + am3)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3)
		End If
		If am4 <> tempZeroRange Then
			If am3 = tempZeroRange Then
				am3 = (am2 + am4)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4)
		End If
		If am5 <> tempZeroRange Then
			If am4 = tempZeroRange Then
				am4 = (am3 + am5)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5)
		End If
		If am6 <> tempZeroRange Then
			If am5 = tempZeroRange Then
				am5 = (am4 + am6)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6)
		End If
		If am7 <> tempZeroRange Then
			If am6 = tempZeroRange Then
				am6 = (am5 + am7)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7)
		End If
		If am8 <> tempZeroRange Then
			If am7 = tempZeroRange Then
				am7 = (am6 + am8)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8)
		End If
		If am9 <> tempZeroRange Then
			If am8 = tempZeroRange Then
				am8 = (am7 + am9)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9)
		End If
		If am10 <> tempZeroRange Then
			If am9 = tempZeroRange Then
				am9 = (am8 + am10)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9, am10)
		End If
		If am11 <> tempZeroRange Then
			If am10 = tempZeroRange Then
				am10 = (am9 + am11)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11)
		End If
		If pm12 <> tempZeroRange Then
			If am11 = tempZeroRange Then
				am11 = (am10 + pm12)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12)
		End If
		If pm1 <> tempZeroRange Then
			If pm12 = tempZeroRange Then
				pm12 = (am11 + pm1)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1)
		End If
		If pm2 <> tempZeroRange Then
			If pm1 = tempZeroRange Then
				pm1 = (pm12 + pm2)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2)
		End If
		If pm3 <> tempZeroRange Then
			If pm2 = tempZeroRange Then
				pm2 = (pm1 + pm3)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3)
		End If
		If pm4 <> tempZeroRange Then
			If pm3 = tempZeroRange Then
				pm3 = (pm2 + pm4)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4)
		End If
		If pm5 <> tempZeroRange Then
			If pm4 = tempZeroRange Then
				pm4 = (pm3 + pm5)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5)
		End If
		If pm6 <> tempZeroRange Then
			If pm5 = tempZeroRange Then
				pm5 = (pm4 + pm6)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6)
		End If
		If pm7 <> tempZeroRange Then
			If pm6 = tempZeroRange Then
				pm6 = (pm5 + pm7)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7)
		End If
		If pm8 <> tempZeroRange Then
			If pm7 = tempZeroRange Then
				pm7 = (pm6 + pm8)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8)
		End If
		If pm9 <> tempZeroRange Then
			If pm8 = tempZeroRange Then
				pm8 = (pm7 + pm9)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9)
		End If
		If pm10 <> tempZeroRange Then
			If pm9 = tempZeroRange Then
				pm9 = (pm8 + pm10)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10)
		End If
		If pm11 <> tempZeroRange Then
			If pm10 = tempZeroRange Then
				pm10 = (pm9 + pm11)/2
			End If
			LineChart.Line_2_Data = Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10, pm11)
		End If
			
		LineChart.Line_2_PointLabelTextColor = Colors.Cyan
		LineChart.Line_2_PointLabelTextSize = 35.0
		LineChart.Line_2_LineColor = Colors.White
		LineChart.Line_2_LineWidth = 7.0
		LineChart.Line_2_PointColor = Colors.Cyan
		LineChart.Line_2_PointSize = 10.0
		LineChart.Line_2_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_2_DrawDash = False
		LineChart.Line_2_DrawCubic = False
		
		' ******************* Yesterday *******************
		
		' ******************* Last 10 minutes *******************
		
		LineChart.Line_3_LegendText = "Real time"
		LineChart.Line_3_Data = Array As Float (tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow,tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow, tempRightNow)
		LineChart.Line_3_PointLabelTextColor = Colors.Green
		LineChart.Line_3_PointLabelTextSize = 35.0
		LineChart.Line_3_LineColor = Colors.Green
		LineChart.Line_3_LineWidth = 5.0
		LineChart.Line_3_PointColor = Colors.Green
		LineChart.Line_3_PointSize = 1.0
		LineChart.Line_3_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_3_DrawDash = True
		LineChart.Line_3_DrawCubic = False
		
		' ******************* Last 10 minutes *******************
		
		LineChart.NumberOfLineCharts = 3                              'set the number of graphs to draw from the 1 to 5 graph that has been set up above
			 
		LineChart.DrawTheGraphs

	Catch
		Log(LastException)
		ToastMessageShow (LastException,True)
	End Try
End Sub

Sub GetRealSize As LayoutValues
	Dim lv As LayoutValues
	Dim p As Phone
	If p.SdkVersion >= 17 Then
		Dim ctxt As JavaObject
		ctxt.InitializeContext
		Dim display As JavaObject = ctxt.RunMethodJO("getSystemService", Array("window")).RunMethod("getDefaultDisplay", Null)
		Dim point As JavaObject
		point.InitializeNewInstance("android.graphics.Point", Null)
		display.RunMethod("getRealSize", Array(point))
		lv.Width = point.GetField("x")
		lv.Height = point.GetField("y")
	Else
		lv.Width = 100%x
		lv.Height = 100%y
	End If
	lv.Scale = 100dip / 100
	Return lv
End Sub

Sub ReadTemperatureDaily(fileDay As String)
	Try
		Dim TextReader1 As TextReader
		Dim Now As Long
		Dim Month As Int
		Dim Day As Int
		Dim Year As Int
		Dim FileName As String
	
		am12 = zeroRange
		am1 = zeroRange
		am2 = zeroRange
		am3 = zeroRange
		am4 = zeroRange
		am5 = zeroRange
		am6 = zeroRange
		am7 = zeroRange
		am8 = zeroRange
		am9 = zeroRange
		am10 = zeroRange
		am11 = zeroRange
		pm12 = zeroRange
		pm1 = zeroRange
		pm2 = zeroRange
		pm3 = zeroRange
		pm4 = zeroRange
		pm5 = zeroRange
		pm6 = zeroRange
		pm7 = zeroRange
		pm8 = zeroRange
		pm9 = zeroRange
		pm10 = zeroRange
		pm11 = zeroRange
		
		Now = DateTime.Now
		Month = DateTime.GetMonth(Now)
		Day = DateTime.GetDayOfMonth (Now)
		Year = DateTime.GetYear(Now)

		If fileDay = "Today" Then
			FileName = "LivingRoomTempHumid_" & Year & "-" & NumberFormat(Month,2,0) & "-" & NumberFormat(Day,2,0) & ".log"
		Else
			Now = DateTime.add(DateTime.Now, 0, 0, -1)
			Month = DateTime.GetMonth(Now)
			Day = DateTime.GetDayOfMonth (Now)
			Year = DateTime.GetYear(Now)
			FileName = "LivingRoomTempHumid_" & Year & "-" & NumberFormat(Month,2,0) & "-" & NumberFormat(Day,2,0) & ".log"
		End If
			
		shared = rp.GetSafeDirDefaultExternal("")
		TextReader1.Initialize(File.OpenInput(shared, FileName))
		Dim line As String
		line = TextReader1.ReadLine
		Do While line <> Null
			'Log(line) 'write the line to LogCat
			line = TextReader1.ReadLine
			If line = Null Then
				Exit
			End If
			Dim a() As String = Regex.Split("\|",line)
			If a.Length = 9 Then
				Dim timeStamp As String
				timeStamp = a(0).SubString2(0,2)
				
				If IsNumber(a(1)) = False Then Continue

				Select timeStamp
					Case "00"
						If am12 = zeroRange Or am12 = "" Then am12 = NumberFormat(a(1),0,2)
					Case "01"
						If am1 = zeroRange Or am1 = "" Then am1 = NumberFormat(a(1),0,2)
					Case "02"
						If am2 = zeroRange Or am2 = "" Then am2 = NumberFormat(a(1),0,2)
					Case "03"
						If am3 = zeroRange Or am3 = "" Then am3 = NumberFormat(a(1),0,2)
					Case "04"
						If am4 = zeroRange Or am4 = "" Then am4 = NumberFormat(a(1),0,2)
					Case "05"
						If am5 = zeroRange Or am5 = "" Then am5 = NumberFormat(a(1),0,2)
					Case "06"
						If am6 = zeroRange Or am6 = "" Then am6 = NumberFormat(a(1),0,2)
					Case "07"
						If am7 = zeroRange Or am7 = "" Then am7 = NumberFormat(a(1),0,2)
					Case "08"
						If am8 = zeroRange Or am8 = "" Then am8 = NumberFormat(a(1),0,2)
					Case "09"
						If am9 = zeroRange Or am9 = "" Then am9 = NumberFormat(a(1),0,2)
					Case "10"
						If am10 = zeroRange Or am10 = "" Then am10 = NumberFormat(a(1),0,2)
					Case "11"
						If am11 = zeroRange Or am11 = "" Then am11 = NumberFormat(a(1),0,2)
					Case "12"
						If pm12 = zeroRange Or pm12 = "" Then pm12 = NumberFormat(a(1),0,2)
					Case "13"
						If pm1 = zeroRange Or pm1 = "" Then pm1 = NumberFormat(a(1),0,2)
					Case "14"
						If pm2 = zeroRange Or pm2 = "" Then pm2 = NumberFormat(a(1),0,2)
					Case "15"
						If pm3 = zeroRange Or pm3 = "" Then pm3 = NumberFormat(a(1),0,2)
					Case "16"
						If pm4 = zeroRange Or pm4 = "" Then pm4 = NumberFormat(a(1),0,2)
					Case "17"
						If pm5 = zeroRange Or pm5 = "" Then pm5 = NumberFormat(a(1),0,2)
					Case "18"
						If pm6 = zeroRange Or pm6 = "" Then pm6 = NumberFormat(a(1),0,2)
					Case "19"
						If pm7 = zeroRange Or pm7 = "" Then pm7 = NumberFormat(a(1),0,2)
					Case "20"
						If pm8 = zeroRange Or pm8 = "" Then pm8 = NumberFormat(a(1),0,2)
					Case "21"
						If pm9 = zeroRange Or pm9 = "" Then pm9 = NumberFormat(a(1),0,2)
					Case "22"
						If pm10 = zeroRange Or pm10 = "" Then pm10 = NumberFormat(a(1),0,2)
					Case "23"
						If pm11 = zeroRange Or pm11 = "" Then pm11 = NumberFormat(a(1),0,2)
				End Select
				If fileDay = "Today" Then
					tempRightNow = NumberFormat(a(1),0,2)
				End If
			End If
		Loop

		TextReader1.Close
	Catch
		Log(LastException)
	End Try
End Sub

Sub ReadHumidityDaily(fileDay As String)
	Try
		Dim TextReader1 As TextReader
		Dim Now As Long
		Dim Month As Int
		Dim Day As Int
		Dim Year As Int
		Dim FileName As String
	
		am12 = zeroRange
		am1 = zeroRange
		am2 = zeroRange
		am3 = zeroRange
		am4 = zeroRange
		am5 = zeroRange
		am6 = zeroRange
		am7 = zeroRange
		am8 = zeroRange
		am9 = zeroRange
		am10 = zeroRange
		am11 = zeroRange
		pm12 = zeroRange
		pm1 = zeroRange
		pm2 = zeroRange
		pm3 = zeroRange
		pm4 = zeroRange
		pm5 = zeroRange
		pm6 = zeroRange
		pm7 = zeroRange
		pm8 = zeroRange
		pm9 = zeroRange
		pm10 = zeroRange
		pm11 = zeroRange
		
		Now = DateTime.Now
		Month = DateTime.GetMonth(Now)
		Day = DateTime.GetDayOfMonth (Now)
		Year = DateTime.GetYear(Now)

		If fileDay = "Today" Then
			FileName = "LivingRoomTempHumid_" & Year & "-" & NumberFormat(Month,2,0) & "-" & NumberFormat(Day,2,0) & ".log"
		Else
			Now = DateTime.add(DateTime.Now, 0, 0, -1)
			Month = DateTime.GetMonth(Now)
			Day = DateTime.GetDayOfMonth (Now)
			Year = DateTime.GetYear(Now)
			FileName = "LivingRoomTempHumid_" & Year & "-" & NumberFormat(Month,2,0) & "-" & NumberFormat(Day,2,0) & ".log"
		End If
		
		shared = rp.GetSafeDirDefaultExternal("")
		TextReader1.Initialize(File.OpenInput(shared, FileName))
		Dim line As String
		line = TextReader1.ReadLine
		Do While line <> Null
			'Log(line) 'write the line to LogCat
			line = TextReader1.ReadLine
			If line = Null Then
				Exit
			End If
			Dim a() As String = Regex.Split("\|",line)
			If a.Length = 9 Then
				Dim timeStamp As String
				timeStamp = a(0).SubString2(0,2)
				
				If IsNumber(a(2)) = False Then Continue
				
				Select timeStamp
					Case "00"
						If am12 = zeroRange Or am12 = "" Then am12 = NumberFormat(a(2),0,2)
					Case "01"
						If am1 = zeroRange Or am1 = "" Then am1 = NumberFormat(a(2),0,2)
					Case "02"
						If am2 = zeroRange Or am2 = "" Then am2 = NumberFormat(a(2),0,2)
					Case "03"
						If am3 = zeroRange Or am3 = "" Then am3 = NumberFormat(a(2),0,2)
					Case "04"
						If am4 = zeroRange Or am4 = "" Then am4 = NumberFormat(a(2),0,2)
					Case "05"
						If am5 = zeroRange Or am5 = "" Then am5 = NumberFormat(a(2),0,2)
					Case "06"
						If am6 = zeroRange Or am6 = "" Then am6 = NumberFormat(a(2),0,2)
					Case "07"
						If am7 = zeroRange Or am7 = "" Then am7 = NumberFormat(a(2),0,2)
					Case "08"
						If am8 = zeroRange Or am8 = "" Then am8 = NumberFormat(a(2),0,2)
					Case "09"
						If am9 = zeroRange Or am9 = "" Then am9 = NumberFormat(a(2),0,2)
					Case "10"
						If am10 = zeroRange Or am10 = "" Then am10 = NumberFormat(a(2),0,2)
					Case "11"
						If am11 = zeroRange Or am11 = "" Then am11 = NumberFormat(a(2),0,2)
					Case "12"
						If pm12 = zeroRange Or pm12 = "" Then pm12 = NumberFormat(a(2),0,2)
					Case "13"
						If pm1 = zeroRange Or pm1 = "" Then pm1 = NumberFormat(a(2),0,2)
					Case "14"
						If pm2 = zeroRange Or pm2 = "" Then pm2 = NumberFormat(a(2),0,2)
					Case "15"
						If pm3 = zeroRange Or pm3 = "" Then pm3 = NumberFormat(a(2),0,2)
					Case "16"
						If pm4 = zeroRange Or pm4 = "" Then pm4 = NumberFormat(a(2),0,2)
					Case "17"
						If pm5 = zeroRange Or pm5 = "" Then pm5 = NumberFormat(a(2),0,2)
					Case "18"
						If pm6 = zeroRange Or pm6 = "" Then pm6 = NumberFormat(a(2),0,2)
					Case "19"
						If pm7 = zeroRange Or pm7 = "" Then pm7 = NumberFormat(a(2),0,2)
					Case "20"
						If pm8 = zeroRange Or pm8 = "" Then pm8 = NumberFormat(a(2),0,2)
					Case "21"
						If pm9 = zeroRange Or pm9 = "" Then pm9 = NumberFormat(a(2),0,2)
					Case "22"
						If pm10 = zeroRange Or pm10 = "" Then pm10 = NumberFormat(a(2),0,2)
					Case "23"
						If pm11 = zeroRange Or pm11 = "" Then pm11 = NumberFormat(a(2),0,2)
				End Select
				If fileDay = "Today" Then
					tempRightNow = NumberFormat(a(2),0,2)
				End If
			End If
		Loop

		TextReader1.Close
	Catch
		Log(LastException)
	End Try
End Sub

Sub ReadTemperatureHourly(fileDay As String)
	Try
		Dim TextReader1 As TextReader
		Dim Now As Long
		Dim Month As Int
		Dim Day As Int
		Dim Year As Int
		Dim FileName As String
	
		am12 = zeroRange
		am1 = zeroRange
		am2 = zeroRange
		am3 = zeroRange
		am4 = zeroRange
		am5 = zeroRange
		am6 = zeroRange
		am7 = zeroRange
		am8 = zeroRange
		am9 = zeroRange
		am10 = zeroRange
		am11 = zeroRange
		pm12 = zeroRange
		pm1 = zeroRange
		pm2 = zeroRange
		pm3 = zeroRange
		pm4 = zeroRange
		pm5 = zeroRange
		pm6 = zeroRange
		pm7 = zeroRange
		pm8 = zeroRange
		pm9 = zeroRange
		pm10 = zeroRange
		pm11 = zeroRange
		
		Now = DateTime.Now
		Month = DateTime.GetMonth(Now)
		Day = DateTime.GetDayOfMonth (Now)
		Year = DateTime.GetYear(Now)
			
		If fileDay = "Today" Then
			FileName = "LivingRoomTempHumid_" & Year & "-" & NumberFormat(Month,2,0) & "-" & NumberFormat(Day,2,0) & ".log"
		Else
			Now = DateTime.add(DateTime.Now, 0, 0, -1)
			Month = DateTime.GetMonth(Now)
			Day = DateTime.GetDayOfMonth (Now)
			Year = DateTime.GetYear(Now)
			FileName = "LivingRoomTempHumid_" & Year & "-" & NumberFormat(Month,2,0) & "-" & NumberFormat(Day,2,0) & ".log"
		End If
		
		shared = rp.GetSafeDirDefaultExternal("")
		TextReader1.Initialize(File.OpenInput(shared, FileName))
		Dim line As String
		line = TextReader1.ReadLine
		Do While line <> Null
			'Log(line) 'write the line to LogCat
			line = TextReader1.ReadLine
			If line = Null Then
				Exit
			End If
			Dim a() As String = Regex.Split("\|",line)
			If a.Length = 9 Then
				Dim timeStamp As String
				timeStamp = a(0).SubString2(0,5)
					
				If IsNumber(a(1)) = False Then Continue
					
				Select timeStamp
					Case timeArray(0)
						If am12 = zeroRange Or am12 = "" Then am12 = NumberFormat(a(1),0,2)
					Case timeArray(1)
						If am1 = zeroRange Or am1 = "" Then am1 = NumberFormat(a(1),0,2)
					Case timeArray(2)
						If am2 = zeroRange Or am2 = "" Then am2 = NumberFormat(a(1),0,2)
					Case timeArray(3)
						If am3 = zeroRange Or am3 = "" Then am3 = NumberFormat(a(1),0,2)
					Case timeArray(4)
						If am4 = zeroRange Or am4 = "" Then am4 = NumberFormat(a(1),0,2)
					Case timeArray(5)
						If am5 = zeroRange Or am5 = "" Then am5 = NumberFormat(a(1),0,2)
					Case timeArray(6)
						If am6 = zeroRange Or am6 = "" Then am6 = NumberFormat(a(1),0,2)
					Case timeArray(7)
						If am7 = zeroRange Or am7 = "" Then am7 = NumberFormat(a(1),0,2)
					Case timeArray(8)
						If am8 = zeroRange Or am8 = "" Then am8 = NumberFormat(a(1),0,2)
					Case timeArray(9)
						If am9 = zeroRange Or am9 = "" Then am9 = NumberFormat(a(1),0,2)
					Case timeArray(10)
						If am10 = zeroRange Or am10 = "" Then am10 = NumberFormat(a(1),0,2)
					Case timeArray(11)
						If am11 = zeroRange Or am11 = "" Then am11 = NumberFormat(a(1),0,2)
					Case timeArray(12)
						If pm12 = zeroRange Or pm12 = "" Then pm12 = NumberFormat(a(1),0,2)
					Case timeArray(13)
						If pm1 = zeroRange Or pm1 = "" Then pm1 = NumberFormat(a(1),0,2)
					Case timeArray(14)
						If pm2 = zeroRange Or pm2 = "" Then pm2 = NumberFormat(a(1),0,2)
					Case timeArray(15)
						If pm3 = zeroRange Or pm3 = "" Then pm3 = NumberFormat(a(1),0,2)
					Case timeArray(16)
						If pm4 = zeroRange Or pm4 = "" Then pm4 = NumberFormat(a(1),0,2)
					Case timeArray(17)
						If pm5 = zeroRange Or pm5 = "" Then pm5 = NumberFormat(a(1),0,2)
					Case timeArray(18)
						If pm6 = zeroRange Or pm6 = "" Then pm6 = NumberFormat(a(1),0,2)
					Case timeArray(19)
						If pm7 = zeroRange Or pm7 = "" Then pm7 = NumberFormat(a(1),0,2)
					Case timeArray(20)
						If pm8 = zeroRange Or pm8 = "" Then pm8 = NumberFormat(a(1),0,2)
					Case timeArray(21)
						If pm9 = zeroRange Or pm9 = "" Then pm9 = NumberFormat(a(1),0,2)
					Case timeArray(22)
						If pm10 = zeroRange Or pm10 = "" Then pm10 = NumberFormat(a(1),0,2)
					Case timeArray(23)
						If pm11 = zeroRange Or pm11 = "" Then pm11 = NumberFormat(a(1),0,2)
				End Select
				If fileDay = "Today" Then
					tempRightNow = NumberFormat(a(1),0,2)
					DateTime.TimeFormat = "h:mm a"
				End If
			End If
		Loop
			
		TextReader1.Close
		'Next

	Catch
		Log(LastException)
	End Try
End Sub

Sub ReadHumidityHourly(fileDay As String)
	Try
		Dim TextReader1 As TextReader
		Dim Now As Long
		Dim Month As Int
		Dim Day As Int
		Dim Year As Int
		Dim FileName As String
	
		am12 = zeroRange
		am1 = zeroRange
		am2 = zeroRange
		am3 = zeroRange
		am4 = zeroRange
		am5 = zeroRange
		am6 = zeroRange
		am7 = zeroRange
		am8 = zeroRange
		am9 = zeroRange
		am10 = zeroRange
		am11 = zeroRange
		pm12 = zeroRange
		pm1 = zeroRange
		pm2 = zeroRange
		pm3 = zeroRange
		pm4 = zeroRange
		pm5 = zeroRange
		pm6 = zeroRange
		pm7 = zeroRange
		pm8 = zeroRange
		pm9 = zeroRange
		pm10 = zeroRange
		pm11 = zeroRange
		
		Now = DateTime.Now
		Month = DateTime.GetMonth(Now)
		Day = DateTime.GetDayOfMonth (Now)
		Year = DateTime.GetYear(Now)
			
		If fileDay = "Today" Then
			FileName = "LivingRoomTempHumid_" & Year & "-" & NumberFormat(Month,2,0) & "-" & NumberFormat(Day,2,0) & ".log"
		Else
			Now = DateTime.add(DateTime.Now, 0, 0, -1)
			Month = DateTime.GetMonth(Now)
			Day = DateTime.GetDayOfMonth (Now)
			Year = DateTime.GetYear(Now)
			FileName = "LivingRoomTempHumid_" & Year & "-" & NumberFormat(Month,2,0) & "-" & NumberFormat(Day,2,0) & ".log"
		End If
		
		shared = rp.GetSafeDirDefaultExternal("")
		TextReader1.Initialize(File.OpenInput(shared, FileName))
		Dim line As String
		line = TextReader1.ReadLine
		Do While line <> Null
			'Log(line) 'write the line to LogCat
			line = TextReader1.ReadLine
			If line = Null Then
				Exit
			End If
			Dim a() As String = Regex.Split("\|",line)
			If a.Length = 9 Then
				Dim timeStamp As String
				timeStamp = a(0).SubString2(0,5)
					
				If IsNumber(a(2)) = False Then Continue
						
				Select timeStamp
					Case timeArray(0)
						If am12 = zeroRange Or am12 = "" Then am12 = NumberFormat(a(2),0,2)
					Case timeArray(1)
						If am1 = zeroRange Or am1 = "" Then am1 = NumberFormat(a(2),0,2)
					Case timeArray(2)
						If am2 = zeroRange Or am2 = "" Then am2 = NumberFormat(a(2),0,2)
					Case timeArray(3)
						If am3 = zeroRange Or am3 = "" Then am3 = NumberFormat(a(2),0,2)
					Case timeArray(4)
						If am4 = zeroRange Or am4 = "" Then am4 = NumberFormat(a(2),0,2)
					Case timeArray(5)
						If am5 = zeroRange Or am5 = "" Then am5 = NumberFormat(a(2),0,2)
					Case timeArray(6)
						If am6 = zeroRange Or am6 = "" Then am6 = NumberFormat(a(2),0,2)
					Case timeArray(7)
						If am7 = zeroRange Or am7 = "" Then am7 = NumberFormat(a(2),0,2)
					Case timeArray(8)
						If am8 = zeroRange Or am8 = "" Then am8 = NumberFormat(a(2),0,2)
					Case timeArray(9)
						If am9 = zeroRange Or am9 = "" Then am9 = NumberFormat(a(2),0,2)
					Case timeArray(10)
						If am10 = zeroRange Or am10 = "" Then am10 = NumberFormat(a(2),0,2)
					Case timeArray(11)
						If am11 = zeroRange Or am11 = "" Then am11 = NumberFormat(a(2),0,2)
					Case timeArray(12)
						If pm12 = zeroRange Or pm12 = "" Then pm12 = NumberFormat(a(2),0,2)
					Case timeArray(13)
						If pm1 = zeroRange Or pm1 = "" Then pm1 = NumberFormat(a(2),0,2)
					Case timeArray(14)
						If pm2 = zeroRange Or pm2 = "" Then pm2 = NumberFormat(a(2),0,2)
					Case timeArray(15)
						If pm3 = zeroRange Or pm3 = "" Then pm3 = NumberFormat(a(2),0,2)
					Case timeArray(16)
						If pm4 = zeroRange Or pm4 = "" Then pm4 = NumberFormat(a(2),0,2)
					Case timeArray(17)
						If pm5 = zeroRange Or pm5 = "" Then pm5 = NumberFormat(a(2),0,2)
					Case timeArray(18)
						If pm6 = zeroRange Or pm6 = "" Then pm6 = NumberFormat(a(2),0,2)
					Case timeArray(19)
						If pm7 = zeroRange Or pm7 = "" Then pm7 = NumberFormat(a(2),0,2)
					Case timeArray(20)
						If pm8 = zeroRange Or pm8 = "" Then pm8 = NumberFormat(a(2),0,2)
					Case timeArray(21)
						If pm9 = zeroRange Or pm9 = "" Then pm9 = NumberFormat(a(2),0,2)
					Case timeArray(22)
						If pm10 = zeroRange Or pm10 = "" Then pm10 = NumberFormat(a(2),0,2)
					Case timeArray(23)
						If pm11 = zeroRange Or pm11 = "" Then pm11 = NumberFormat(a(2),0,2)
				End Select
				If fileDay = "Today" Then
					tempRightNow = NumberFormat(a(2),0,2)
					DateTime.TimeFormat = "h:mm a"
				End If
			End If
		Loop
			
		TextReader1.Close
		'Next

	Catch
		Log(LastException)
	End Try
End Sub

Sub CheckTempBoundaries
	Try
		Dim tempList As List
		tempList.Initialize
		tempList.AddAll(Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10, pm11))
		tempList.Sort(True)
			
		'Dim tempZeroRange As Float
		tempZeroRange = tempList.Get(0)-0.3
		
		If am12 = zeroRange Then am12 = tempZeroRange
		If am1 = zeroRange Then am1 = tempZeroRange
		If am2 = zeroRange Then am2 = tempZeroRange
		If am3 = zeroRange Then am3 = tempZeroRange
		If am4 = zeroRange Then am4 = tempZeroRange
		If am5 = zeroRange Then am5 = tempZeroRange
		If am6 = zeroRange Then am6 = tempZeroRange
		If am7 = zeroRange Then am7 = tempZeroRange
		If am8 = zeroRange Then am8 = tempZeroRange
		If am9 = zeroRange Then am9 = tempZeroRange
		If am10 = zeroRange Then am10 = tempZeroRange
		If am11 = zeroRange Then am11 = tempZeroRange
		If pm12 = zeroRange Then pm12 = tempZeroRange
		If pm1 = zeroRange Then pm1 = tempZeroRange
		If pm2 = zeroRange Then pm2 = tempZeroRange
		If pm3 = zeroRange Then pm3 = tempZeroRange
		If pm4 = zeroRange Then pm4 = tempZeroRange
		If pm5 = zeroRange Then pm5 = tempZeroRange
		If pm6 = zeroRange Then pm6 = tempZeroRange
		If pm7 = zeroRange Then pm7 = tempZeroRange
		If pm8 = zeroRange Then pm8 = tempZeroRange
		If pm9 = zeroRange Then pm9 = tempZeroRange
		If pm10 = zeroRange Then pm10 = tempZeroRange
		If pm11 = zeroRange Then pm11 = tempZeroRange
		
		am12 = NumberFormat(am12,0,2)
		am1 = NumberFormat(am1,0,2)
		am2 = NumberFormat(am2,0,2)
		am3 = NumberFormat(am3,0,2)
		am4 = NumberFormat(am4,0,2)
		am5 = NumberFormat(am5,0,2)
		am6 = NumberFormat(am6,0,2)
		am7 = NumberFormat(am7,0,2)
		am8 = NumberFormat(am8,0,2)
		am9 = NumberFormat(am9,0,2)
		am10 = NumberFormat(am10,0,2)
		am11 = NumberFormat(am11,0,2)
		pm12 = NumberFormat(pm12,0,2)
		pm1 = NumberFormat(pm1,0,2)
		pm2 = NumberFormat(pm2,0,2)
		pm3 = NumberFormat(pm3,0,2)
		pm4 = NumberFormat(pm4,0,2)
		pm5 = NumberFormat(pm5,0,2)
		pm6 = NumberFormat(pm6,0,2)
		pm7 = NumberFormat(pm7,0,2)
		pm8 = NumberFormat(pm8,0,2)
		pm9 = NumberFormat(pm9,0,2)
		pm10 = NumberFormat(pm10,0,2)
		pm11 = NumberFormat(pm11,0,2)
		
		tempList.Initialize
		tempList.AddAll(Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10, pm11))
		tempList.Sort(True)
		
		Dim minValue=0, maxValue=0 As Float
		If tempRightNow <= tempList.Get(0) Then
			minValue = tempRightNow-0.1
		Else
			minValue = tempList.Get(0)-0.1
		End If
	
		If tempList.Get(tempList.Size-1) >= 88.88 Then
			If tempRightNow >= (tempList.Get(tempList.Size-2)) Then
				maxValue = tempRightNow+0.6
			Else
				maxValue = (tempList.Get(tempList.Size-2))+0.6
			End If
		Else
			If tempRightNow >= (tempList.Get(tempList.Size-1)) Then
				maxValue = tempRightNow+0.6
			Else
				maxValue = (tempList.Get(tempList.Size-1))+0.6
			End If
		End If
	
		If minValue < 40 Then
			minValue = tempList.Get(tempList.Size-1)
		End If
	
		LineChart.YaxisRange(minValue-2, maxValue+2)
	Catch
		Log(LastException)
	End Try

End Sub

Sub CheckTempBoundariesDaily
	Try
		Dim tempList As List
		tempList.Initialize
		tempList.AddAll(Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10, pm11))
		tempList.Sort(True)
			
		tempZeroRange = tempList.Get(0)-0.3
		
		If am12 = zeroRange Then am12 = tempZeroRange
		If am1 = zeroRange Then am1 = tempZeroRange
		If am2 = zeroRange Then am2 = tempZeroRange
		If am3 = zeroRange Then am3 = tempZeroRange
		If am4 = zeroRange Then am4 = tempZeroRange
		If am5 = zeroRange Then am5 = tempZeroRange
		If am6 = zeroRange Then am6 = tempZeroRange
		If am7 = zeroRange Then am7 = tempZeroRange
		If am8 = zeroRange Then am8 = tempZeroRange
		If am9 = zeroRange Then am9 = tempZeroRange
		If am10 = zeroRange Then am10 = tempZeroRange
		If am11 = zeroRange Then am11 = tempZeroRange
		If pm12 = zeroRange Then pm12 = tempZeroRange
		If pm1 = zeroRange Then pm1 = tempZeroRange
		If pm2 = zeroRange Then pm2 = tempZeroRange
		If pm3 = zeroRange Then pm3 = tempZeroRange
		If pm4 = zeroRange Then pm4 = tempZeroRange
		If pm5 = zeroRange Then pm5 = tempZeroRange
		If pm6 = zeroRange Then pm6 = tempZeroRange
		If pm7 = zeroRange Then pm7 = tempZeroRange
		If pm8 = zeroRange Then pm8 = tempZeroRange
		If pm9 = zeroRange Then pm9 = tempZeroRange
		If pm10 = zeroRange Then pm10 = tempZeroRange
		If pm11 = zeroRange Then pm11 = tempZeroRange
		
		am12 = NumberFormat(am12,0,2)
		am1 = NumberFormat(am1,0,2)
		am2 = NumberFormat(am2,0,2)
		am3 = NumberFormat(am3,0,2)
		am4 = NumberFormat(am4,0,2)
		am5 = NumberFormat(am5,0,2)
		am6 = NumberFormat(am6,0,2)
		am7 = NumberFormat(am7,0,2)
		am8 = NumberFormat(am8,0,2)
		am9 = NumberFormat(am9,0,2)
		am10 = NumberFormat(am10,0,2)
		am11 = NumberFormat(am11,0,2)
		pm12 = NumberFormat(pm12,0,2)
		pm1 = NumberFormat(pm1,0,2)
		pm2 = NumberFormat(pm2,0,2)
		pm3 = NumberFormat(pm3,0,2)
		pm4 = NumberFormat(pm4,0,2)
		pm5 = NumberFormat(pm5,0,2)
		pm6 = NumberFormat(pm6,0,2)
		pm7 = NumberFormat(pm7,0,2)
		pm8 = NumberFormat(pm8,0,2)
		pm9 = NumberFormat(pm9,0,2)
		pm10 = NumberFormat(pm10,0,2)
		pm11 = NumberFormat(pm11,0,2)
		
		
		tempList.Initialize
		tempList.AddAll(Array As Float (am12, am1, am2, am3, am4, am5, am6, am7, am8, am9,am10, am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10, pm11))
		tempList.Sort(True)
	
		Dim minValue=0, maxValue=0 As Float
	
		If tempRightNow <= tempList.Get(0) Then
			If tempMinRange <= tempRightNow Then
				minValue = tempMinRange-0.3
			Else
				minValue = tempRightNow-0.3
			End If
		Else
			If tempMinRange > 0 And tempMinRange <= tempList.Get(0) Then
				minValue = tempMinRange-0.3
			Else
				minValue = tempList.Get(0)-0.3
			End If
		End If
	
		If tempList.Get(tempList.Size-1) >= 88.88 Then
			If tempRightNow >= (tempList.Get(tempList.Size-2)) Then
				If tempMaxRange >= tempRightNow Then
					maxValue =  tempMaxRange+0.6
				Else
					maxValue = tempRightNow+0.6
				End If
			Else
				If tempMaxRange >= (tempList.Get(tempList.Size-2)) Then
					maxValue =  tempMaxRange+0.6
				Else
					maxValue = (tempList.Get(tempList.Size-2))+0.6
				End If
			End If
		Else
			If tempRightNow >= (tempList.Get(tempList.Size-1)) Then
				If tempMaxRange >= tempRightNow Then
					maxValue =  tempMaxRange+0.6
				Else
					maxValue = tempRightNow+0.6
				End If
			Else
				If tempMaxRange >= (tempList.Get(tempList.Size-1)) Then
					maxValue =  tempMaxRange+0.6
				Else
					maxValue = (tempList.Get(tempList.Size-1))+0.6
				End If
			End If
		End If
	
		If (maxValue-0.3) >= tempMaxRange Then
			tempMaxRange = maxValue-0.3
		End If
	
		If minValue < 40 Then
			minValue = tempList.Get(tempList.Size-1)
		End If
	
		tempMinRange = minValue+0.5
	
		LineChart.YaxisRange(minValue-2, maxValue+2)
	Catch
		Log(LastException)
	End Try

End Sub

Sub TemperatureHourlyTimer_Tick
	Activity.RequestFocus
	btnHumidityHourly.RemoveView
	btnTempHourly.RemoveView
	btnHumidityDaily.RemoveView
	btnTempDaily.RemoveView
	LineChart.RemoveView
	tempMaxRange=0
	tempMinRange=0
	TemperatureHourlyCreate
End Sub

Sub HumidityHourlyTimer_Tick
	Activity.RequestFocus
	btnHumidityHourly.RemoveView
	btnTempHourly.RemoveView
	btnHumidityDaily.RemoveView
	btnTempDaily.RemoveView
	LineChart.RemoveView
	tempMaxRange=0
	tempMinRange=0
	HumidityHourlyCreate
End Sub

Sub TemperatureDailyTimer_Tick
	Activity.RequestFocus
	btnHumidityHourly.RemoveView
	btnTempHourly.RemoveView
	btnHumidityDaily.RemoveView
	btnTempDaily.RemoveView
	LineChart.RemoveView
	tempMaxRange=0
	tempMinRange=0
	TemperatureDailyCreate
End Sub

Sub HumidityDailyTimer_Tick
	Activity.RequestFocus
	btnHumidityHourly.RemoveView
	btnTempHourly.RemoveView
	btnHumidityDaily.RemoveView
	btnTempDaily.RemoveView
	LineChart.RemoveView
	tempMaxRange=0
	tempMinRange=0
	HumidityDailyCreate
End Sub

Sub Activity_WindowFocusChanged(HasFocus As Boolean)
	If HasFocus Then
		Try
			Dim jo As JavaObject = Activity
			'Sleep(300)
			jo.RunMethod("setSystemUiVisibility", Array As Object(5894)) '3846 - non-sticky
		Catch
			'Log(LastException) 'This can cause another error
		End Try 'ignore
		
	End If
End Sub

