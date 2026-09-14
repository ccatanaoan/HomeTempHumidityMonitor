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
	Private BucketHasData(24) As Boolean
	Private btnHumidityHourly As Button
	Private btnTempHourly As Button
	Private btnHumidityDaily As Button
	Private btnTempDaily As Button
	Private Panel1 As Panel
End Sub

Sub Activity_Create(FirstTime As Boolean)
	' Phase 4.6: start with one completely fresh chart instance.
	phone1.SetScreenOrientation(0) 'landscape
	TemperatureHourlyCreate
	TemperatureHourlyTimer.Initialize("TemperatureHourlyTimer",60000)
	TemperatureHourlyTimer.Enabled = True
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
	TemperatureHourlyCreate
	TemperatureHourlyTimer.Initialize("TemperatureHourlyTimer",60000) 'refresh every 60 seconds
	TemperatureHourlyTimer.Enabled = True 'start timer
End Sub

Sub btnHumidityHourly_Click
	TemperatureDailyTimer.Enabled = False
	TemperatureHourlyTimer.Enabled = False
	HumidityDailyTimer.Enabled = False
	HumidityHourlyCreate
	HumidityHourlyTimer.Initialize("HumidityHourlyTimer",60000) 'refresh every 60 seconds
	HumidityHourlyTimer.Enabled = True 'start timer
End Sub

Sub btnTempDaily_Click
	TemperatureHourlyTimer.Enabled = False
	HumidityHourlyTimer.Enabled = False
	HumidityDailyTimer.Enabled = False
	TemperatureDailyCreate
	TemperatureDailyTimer.Initialize("TemperatureDailyTimer",60000) 'refresh every 60 seconds
	TemperatureDailyTimer.Enabled = True 'start timer
End Sub

Sub btnHumidityDaily_Click
	TemperatureHourlyTimer.Enabled = False
	HumidityHourlyTimer.Enabled = False
	TemperatureDailyTimer.Enabled = False
	HumidityDailyCreate
	HumidityDailyTimer.Initialize("HumidityDailyTimer",60000) 'refresh every 60 seconds
	HumidityDailyTimer.Enabled = True 'start timer
End Sub

Private Sub PrepareFreshChartLayout
	' This old AndroidPlot wrapper appends series when DrawTheGraphs is called
	' repeatedly on the same custom view. Re-create the chart view each time
	' so old series can never accumulate.
	Dim lv As LayoutValues = GetRealSize
	Dim jo As JavaObject = Activity
	jo.RunMethod("setBottom", Array(lv.Height))
	jo.RunMethod("setRight", Array(lv.Width))
	Activity.Height = lv.Height
	Activity.Width = lv.Width
	Activity.RemoveAllViews
	Activity.LoadLayout("chart")
End Sub

Private Sub TemperatureHourlyCreate()
	Try
		' Fresh chart instance: no old AndroidPlot series survive.
		PrepareFreshChartLayout
		
		LineChart.GraphBackgroundColor = Colors.DarkGray ' Colors.Transparent
		LineChart.GraphFrameColor = Colors.Blue
		LineChart.GraphFrameWidth = 4.0
		LineChart.GraphPlotAreaBackgroundColor = Colors.ARGB(50, 0, 0, 255) ' Colors.DarkGray
		LineChart.GraphTitleTextSize = 15
		LineChart.GraphTitleColor = Colors.White
		LineChart.GraphTitleSkewX = -0.25
		LineChart.GraphTitleUnderline = True
		LineChart.GraphTitleBold = True
		LineChart.GraphTitle = "TEMPERATURE - LAST 2 HOURS  "	          'put this statement last
		
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
		
		timeRightNow = GetFiveMinuteWindowEnd(DateTime.Now)
		
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
		LineChart.XAxisLabels = BuildHourlyAxisLabels 'Array As String("12 am","1 am", "2 am","3 am", "4 am","5 am","6 am", "7 am","8 am","9 am","10 am","11 am", "12 pm","1 pm", "2 pm","3 pm","4 pm", "5 pm","6 pm","7 pm","8 pm","9 pm", "10 pm","11 pm")
		
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
		
		LineChart.MaxNumberOfEntriesPerLineChart = 26                   'this value must be equal to the number of x-axis labels that you pass
		LineChart.GraphLegendVisibility = False
		
		' ********************* Today *********************
		
		ReadTemperatureHourly

		Dim CurrentValid As Boolean = HasCurrentChartValue
		Dim CurrentValue As Float = GetSafeCurrentValue(70)
		Dim HourlyData() As Float = BuildChartData(CurrentValue)
		Dim HourlyHasData() As Boolean = CopyBucketFlags

		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_1_LegendText = "From " & timeArray(0) & " to " & timeArray(23)
		SetYAxisRangeSingle(HourlyData, HourlyHasData, CurrentValue, CurrentValid, False)
		LineChart.Line_1_Data = HourlyData

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
			
		' ******************* Current reading reference *******************
		
		'LineChart.Line_2_LegendText = "Compiled: March 9, 2020 10:29 am" '& DateTime.Time(DateTime.Now)
		If CurrentValid Then LineChart.Line_2_Data = BuildFlatLine(CurrentValue)
		LineChart.Line_2_PointLabelTextColor = Colors.Transparent
		LineChart.Line_2_PointLabelTextSize = 35.0
		LineChart.Line_2_LineColor = Colors.Green
		LineChart.Line_2_LineWidth = 5.0
		LineChart.Line_2_PointColor = Colors.Green
		LineChart.Line_2_PointSize = 1.0
		LineChart.Line_2_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_2_DrawDash = True
		LineChart.Line_2_DrawCubic = False

		Dim GreenLabelHasData() As Boolean = BuildGreenReferenceLabelFlags
		
		' ******************* Current reading reference *******************
		
		If CurrentValid Then
			LineChart.NumberOfLineCharts = 2
		Else
			LineChart.NumberOfLineCharts = 1
		End If

		LineChart.DrawTheGraphs
		ApplyMissingGaps(1, HourlyHasData)
		' Keep the real chart edges blank first.
		LineChart.ApplyEdgeSpacers(1, 1)
		' Then add one native AndroidPlot Y-value marker label for the green
		' reference value in the blank right-side area.
		If CurrentValid Then ApplyMissingGaps(-2, GreenLabelHasData)
		LineChart.Invalidate

	Catch
		Log(LastException)
		ToastMessageShow (LastException,True)
	End Try
	
End Sub

Private Sub HumidityHourlyCreate()
	Try
		Activity_WindowFocusChanged(True)
		' Fresh chart instance: no old AndroidPlot series survive.
		PrepareFreshChartLayout
			
		LineChart.GraphBackgroundColor = Colors.DarkGray ' Colors.Transparent
		LineChart.GraphFrameColor = Colors.Blue
		LineChart.GraphFrameWidth = 4.0
		LineChart.GraphPlotAreaBackgroundColor = Colors.ARGB(50, 0, 0, 255) ' Colors.DarkGray
		LineChart.GraphTitleTextSize = 15
		LineChart.GraphTitleColor = Colors.White
		LineChart.GraphTitleSkewX = -0.25
		LineChart.GraphTitleUnderline = True
		LineChart.GraphTitleBold = True
		LineChart.GraphTitle = "HUMIDITY - LAST 2 HOURS  "	          'put this statement last
		
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
		
		timeRightNow = GetFiveMinuteWindowEnd(DateTime.Now)
		
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
		LineChart.XAxisLabels = BuildHourlyAxisLabels 'Array As String("12 am","1 am", "2 am","3 am", "4 am","5 am","6 am", "7 am","8 am","9 am","10 am","11 am", "12 pm","1 pm", "2 pm","3 pm","4 pm", "5 pm","6 pm","7 pm","8 pm","9 pm", "10 pm","11 pm")
		
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
		
		LineChart.MaxNumberOfEntriesPerLineChart = 26                   'this value must be equal to the number of x-axis labels that you pass
		LineChart.GraphLegendVisibility = False
		
		' ********************* Today *********************
		
		ReadHumidityHourly

		Dim CurrentValid As Boolean = HasCurrentChartValue
		Dim CurrentValue As Float = GetSafeCurrentValue(50)
		Dim HourlyData() As Float = BuildChartData(CurrentValue)
		Dim HourlyHasData() As Boolean = CopyBucketFlags

		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_1_LegendText = "From " & timeArray(0) & " to " & timeArray(23)
		SetYAxisRangeSingle(HourlyData, HourlyHasData, CurrentValue, CurrentValid, True)
		LineChart.Line_1_Data = HourlyData

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
			
		' ******************* Current reading reference *******************
		
		'LineChart.Line_2_LegendText = "Compiled: March 9, 2020 10:29 am" '& DateTime.Time(DateTime.Now)
		If CurrentValid Then LineChart.Line_2_Data = BuildFlatLine(CurrentValue)
		LineChart.Line_2_PointLabelTextColor = Colors.Transparent
		LineChart.Line_2_PointLabelTextSize = 35.0
		LineChart.Line_2_LineColor = Colors.Green
		LineChart.Line_2_LineWidth = 5.0
		LineChart.Line_2_PointColor = Colors.Green
		LineChart.Line_2_PointSize = 1.0
		LineChart.Line_2_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_2_DrawDash = True
		LineChart.Line_2_DrawCubic = False

		Dim GreenLabelHasData() As Boolean = BuildGreenReferenceLabelFlags
		
		' ******************* Current reading reference *******************
		
		If CurrentValid Then
			LineChart.NumberOfLineCharts = 2
		Else
			LineChart.NumberOfLineCharts = 1
		End If

		LineChart.DrawTheGraphs
		ApplyMissingGaps(1, HourlyHasData)
		' Keep the real chart edges blank first.
		LineChart.ApplyEdgeSpacers(1, 1)
		' Then add one native AndroidPlot Y-value marker label for the green
		' reference value in the blank right-side area.
		If CurrentValid Then ApplyMissingGaps(-2, GreenLabelHasData)
		LineChart.Invalidate
		
	Catch
		Log(LastException)
		ToastMessageShow (LastException,True)
	End Try
End Sub

Private Sub TemperatureDailyCreate()
	Try
		Activity_WindowFocusChanged(True)
		' Fresh chart instance: no old AndroidPlot series survive.
		PrepareFreshChartLayout
		
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
		LineChart.XAxisLabels = BuildDailyAxisLabels
		
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
		
		LineChart.MaxNumberOfEntriesPerLineChart = 26                   'this value must be equal to the number of x-axis labels that you pass
		LineChart.GraphLegendVisibility = False
		
		' ********************* Today *********************
		
		ReadTemperatureDaily("Today")

		Dim CurrentValid As Boolean = HasCurrentChartValue
		Dim CurrentValue As Float = GetSafeCurrentValue(70)
		Dim TodayData() As Float = BuildChartData(CurrentValue)
		Dim TodayHasData() As Boolean = CopyBucketFlags

		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_1_LegendText = "Today, " & DateTime.Date(DateTime.Now)
		LineChart.Line_1_Data = TodayData

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
		Dim YesterdayData() As Float = BuildChartData(CurrentValue)
		Dim YesterdayHasData() As Boolean = CopyBucketFlags

		Dim Yesterday As Long
		Yesterday = DateTime.Add(DateTime.Now, 0, 0, -1)
		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_2_LegendText = "Yesterday, " & DateTime.Date(Yesterday)
		LineChart.Line_2_Data = YesterdayData

		SetYAxisRangeDouble(TodayData, TodayHasData, YesterdayData, YesterdayHasData, _
			CurrentValue, CurrentValid, False)

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
		
		' ******************* Current reading reference *******************
		
		LineChart.Line_3_LegendText = "Real time"
		If CurrentValid Then LineChart.Line_3_Data = BuildFlatLine(CurrentValue)
		LineChart.Line_3_PointLabelTextColor = Colors.Transparent
		LineChart.Line_3_PointLabelTextSize = 35.0
		LineChart.Line_3_LineColor = Colors.Green
		LineChart.Line_3_LineWidth = 5.0
		LineChart.Line_3_PointColor = Colors.Green
		LineChart.Line_3_PointSize = 1.0
		LineChart.Line_3_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_3_DrawDash = True
		LineChart.Line_3_DrawCubic = False

		Dim GreenLabelHasData() As Boolean = BuildGreenReferenceLabelFlags
		
		' ******************* Current reading reference *******************
		
		If CurrentValid Then
			LineChart.NumberOfLineCharts = 3
		Else
			LineChart.NumberOfLineCharts = 2
		End If

		LineChart.DrawTheGraphs
		ApplyMissingGaps(1, TodayHasData)
		ApplyMissingGaps(2, YesterdayHasData)
		' Keep the real chart edges blank first.
		LineChart.ApplyEdgeSpacers(1, 1)
		' Then add one native AndroidPlot Y-value marker label for the green
		' reference value in the blank right-side area.
		If CurrentValid Then ApplyMissingGaps(-3, GreenLabelHasData)
		LineChart.Invalidate

	Catch
		Log(LastException)
		ToastMessageShow (LastException,True)
	End Try
End Sub

Private Sub HumidityDailyCreate()
	Try
		Activity_WindowFocusChanged(True)
		' Fresh chart instance: no old AndroidPlot series survive.
		PrepareFreshChartLayout
		
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
		LineChart.XAxisLabels = BuildDailyAxisLabels
		
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
		
		LineChart.MaxNumberOfEntriesPerLineChart = 26                   'this value must be equal to the number of x-axis labels that you pass
		LineChart.GraphLegendVisibility = False
		
		' ********************* Today *********************
		
		ReadHumidityDaily("Today")

		Dim CurrentValid As Boolean = HasCurrentChartValue
		Dim CurrentValue As Float = GetSafeCurrentValue(50)
		Dim TodayData() As Float = BuildChartData(CurrentValue)
		Dim TodayHasData() As Boolean = CopyBucketFlags

		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_1_LegendText = "Today, " & DateTime.Date(DateTime.Now)
		LineChart.Line_1_Data = TodayData

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
		Dim YesterdayData() As Float = BuildChartData(CurrentValue)
		Dim YesterdayHasData() As Boolean = CopyBucketFlags

		Dim Yesterday As Long
		Yesterday = DateTime.Add(DateTime.Now, 0, 0, -1)
		DateTime.DateFormat = "MMM d, yyyy"
		LineChart.Line_2_LegendText = "Yesterday, " & DateTime.Date(Yesterday)
		LineChart.Line_2_Data = YesterdayData

		SetYAxisRangeDouble(TodayData, TodayHasData, YesterdayData, YesterdayHasData, _
			CurrentValue, CurrentValid, True)

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
		
		' ******************* Current reading reference *******************
		
		LineChart.Line_3_LegendText = "Real time"
		If CurrentValid Then LineChart.Line_3_Data = BuildFlatLine(CurrentValue)
		LineChart.Line_3_PointLabelTextColor = Colors.Transparent
		LineChart.Line_3_PointLabelTextSize = 35.0
		LineChart.Line_3_LineColor = Colors.Green
		LineChart.Line_3_LineWidth = 5.0
		LineChart.Line_3_PointColor = Colors.Green
		LineChart.Line_3_PointSize = 1.0
		LineChart.Line_3_PointShape = LineChart.SHAPE_ROUND
		LineChart.Line_3_DrawDash = True
		LineChart.Line_3_DrawCubic = False

		Dim GreenLabelHasData() As Boolean = BuildGreenReferenceLabelFlags
		
		' ******************* Current reading reference *******************
		
		If CurrentValid Then
			LineChart.NumberOfLineCharts = 3
		Else
			LineChart.NumberOfLineCharts = 2
		End If

		LineChart.DrawTheGraphs
		ApplyMissingGaps(1, TodayHasData)
		ApplyMissingGaps(2, YesterdayHasData)
		' Keep the real chart edges blank first.
		LineChart.ApplyEdgeSpacers(1, 1)
		' Then add one native AndroidPlot Y-value marker label for the green
		' reference value in the blank right-side area.
		If CurrentValid Then ApplyMissingGaps(-3, GreenLabelHasData)
		LineChart.Invalidate

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
	ReadDailyAverages(fileDay, 1)
End Sub

Sub ReadHumidityDaily(fileDay As String)
	ReadDailyAverages(fileDay, 2)
End Sub

Sub ReadTemperatureHourly
	ReadRollingTwoHourMedianSamples(1)
End Sub

Sub ReadHumidityHourly
	ReadRollingTwoHourMedianSamples(2)
End Sub

' ============================================================
' CHART LOG READER - PHASE 4.2
'
' The log still contains every valid Living Room DHT22 sample.
' The chart now summarizes those real samples instead of choosing
' an arbitrary first/exact-minute sample:
'
'   Hourly view: 24 x 5-minute buckets = rolling last 2 hours.
'                Each point is the MEDIAN of all valid REAL sensor readings
'                received in that 5-minute window (no interpolation).
'
'   Daily view:  24 x 1-hour buckets.
'                Each point is the average of all valid samples
'                received during that hour.
'
' Legacy "c" carry-forward lines are ignored. The writer no longer
' creates them. The rolling chart
' reads yesterday + today directly, so midnight no longer needs
' the old future-file trick.
' ============================================================

Private Sub ReadDailyAverages(fileDay As String, ValueIndex As Int)
	Try
		ResetChartBuckets

		Dim TargetDay As Long
		If fileDay = "Today" Then
			TargetDay = DateTime.Now
		Else
			TargetDay = DateTime.Add(DateTime.Now, 0, 0, -1)
		End If

		Dim BucketSum(24) As Double
		Dim BucketCount(24) As Int

		Dim LatestTicks As Long = 0
		Dim LatestValue As String = ""

		AccumulateDailyFile(TargetDay, ValueIndex, BucketSum, BucketCount, _
			fileDay = "Today", LatestTicks, LatestValue)

		ApplyBucketAverages(BucketSum, BucketCount)

		If fileDay = "Today" And LatestValue <> "" Then
			tempRightNow = LatestValue
		End If

	Catch
		Log("ReadDailyAverages: " & LastException)
	End Try
End Sub

Private Sub AccumulateDailyFile(FileDay As Long, ValueIndex As Int, _
	BucketSum() As Double, BucketCount() As Int, TrackLatest As Boolean, _
	LatestTicks As Long, LatestValue As String)
	Try
		shared = rp.GetSafeDirDefaultExternal("")
		Dim FileName As String = GetChartLogFileName(FileDay)
		If File.Exists(shared, FileName) = False Then Return

		Dim TextReader1 As TextReader
		TextReader1.Initialize(File.OpenInput(shared, FileName))

		Dim line As String
		Do While True
			line = TextReader1.ReadLine
			If line = Null Then Exit

			Dim EntryTicks As Long = ParseChartLogTicks(FileDay, line)
			If EntryTicks = 0 Then Continue

			' Today must stop at the latest/current time.  Ignore any
			' impossible future-dated rows left by an older log format.
			If TrackLatest And EntryTicks > DateTime.Now Then Continue

			Dim a() As String = Regex.Split("\|", line)
			If a.Length <> 9 Then Continue
			If a(0).Contains(" OK") = False Then Continue
			If IsNumber(a(ValueIndex)) = False Then Continue

			Dim Value As Double = a(ValueIndex)
			If ValueIndex = 1 And Value <= 0 Then Continue

			Dim Bucket As Int = DateTime.GetHour(EntryTicks)
			If Bucket < 0 Or Bucket > 23 Then Continue

			BucketSum(Bucket) = BucketSum(Bucket) + Value
			BucketCount(Bucket) = BucketCount(Bucket) + 1

			If TrackLatest Then
				'Log files are appended chronologically, but compare timestamps
				'anyway so a malformed/out-of-order line cannot become "real time".
				If EntryTicks >= LatestTicks Then
					LatestTicks = EntryTicks
					LatestValue = NumberFormat(Value, 0, 2)
					tempRightNow = LatestValue
				End If
			End If
		Loop

		TextReader1.Close
	Catch
		Log("AccumulateDailyFile: " & LastException)
	End Try
End Sub

Private Sub ReadRollingTwoHourMedianSamples(ValueIndex As Int)
	Try
		ResetChartBuckets

		' Keep the proven 24 x 5-minute rolling chart geometry.
		' Each bucket now uses the MEDIAN of all valid real sensor
		' readings received in that five-minute window.
		' This rejects an isolated spike/drop naturally without inventing
		' or interpolating any readings. A bucket with no readings stays a gap.
		Dim BucketValues(24) As List
		For i = 0 To 23
			BucketValues(i).Initialize
		Next

		Dim WindowEnd As Long = timeRightNow
		Dim WindowStart As Long = WindowEnd - (120 * 60 * 1000)

		' Arrays are used as mutable holders so the newest REAL reading can
		' still drive tempRightNow / the green real-time reference line.
		Dim LatestTicks(1) As Long
		Dim LatestValue(1) As String

		Dim Yesterday As Long = DateTime.Add(WindowEnd, 0, 0, -1)
		AccumulateRollingMedianFile(Yesterday, ValueIndex, WindowStart, WindowEnd, _
			BucketValues, LatestTicks, LatestValue)
		AccumulateRollingMedianFile(WindowEnd, ValueIndex, WindowStart, WindowEnd, _
			BucketValues, LatestTicks, LatestValue)

		For Bucket = 0 To 23
			If BucketValues(Bucket).Size > 0 Then
				Dim MedianValue As Double = MedianOfValues(BucketValues(Bucket))
				SetChartBucket(Bucket, NumberFormat(MedianValue, 0, 2))
			End If
		Next

		If LatestValue(0) <> "" Then
			tempRightNow = LatestValue(0)
		End If

	Catch
		Log("ReadRollingTwoHourMedianSamples: " & LastException)
	End Try
End Sub

Private Sub AccumulateRollingMedianFile(FileDay As Long, ValueIndex As Int, _
	WindowStart As Long, WindowEnd As Long, BucketValues() As List, _
	LatestTicks() As Long, LatestValue() As String)
	Try
		shared = rp.GetSafeDirDefaultExternal("")
		Dim FileName As String = GetChartLogFileName(FileDay)
		If File.Exists(shared, FileName) = False Then Return

		Dim TextReader1 As TextReader
		TextReader1.Initialize(File.OpenInput(shared, FileName))

		Dim line As String
		Do While True
			line = TextReader1.ReadLine
			If line = Null Then Exit

			Dim EntryTicks As Long = ParseChartLogTicks(FileDay, line)
			If EntryTicks = 0 Then Continue
			If EntryTicks < WindowStart Or EntryTicks >= WindowEnd Then Continue

			Dim a() As String = Regex.Split("\|", line)
			If a.Length <> 9 Then Continue
			If a(0).Contains(" OK") = False Then Continue
			If IsNumber(a(ValueIndex)) = False Then Continue

			Dim Value As Double = a(ValueIndex)
			If ValueIndex = 1 And Value <= 0 Then Continue

			Dim Bucket As Int = Floor((EntryTicks - WindowStart) / 300000)
			If Bucket < 0 Or Bucket > 23 Then Continue

			' Keep every valid REAL reading in the bucket. The median is
			' calculated only after both possible date files are scanned.
			BucketValues(Bucket).Add(Value)

			If EntryTicks >= LatestTicks(0) Then
				LatestTicks(0) = EntryTicks
				LatestValue(0) = NumberFormat(Value, 0, 2)
			End If
		Loop

		TextReader1.Close
	Catch
		Log("AccumulateRollingMedianFile: " & LastException)
	End Try
End Sub

Private Sub MedianOfValues(Values As List) As Double
	' Work on a copy so the original bucket data remains untouched.
	Dim Sorted As List
	Sorted.Initialize
	For Each Item As Object In Values
		Sorted.Add(Item)
	Next
	Sorted.Sort(True)

	Dim Count As Int = Sorted.Size
	If Count = 0 Then Return 0

	Dim Middle As Int = Floor(Count / 2)
	If Count Mod 2 = 1 Then
		Dim OddValue As Double = Sorted.Get(Middle)
		Return OddValue
	Else
		Dim LowerValue As Double = Sorted.Get(Middle - 1)
		Dim UpperValue As Double = Sorted.Get(Middle)
		Return (LowerValue + UpperValue) / 2
	End If
End Sub

Private Sub ParseChartLogTicks(FileDay As Long, line As String) As Long
	Try
		If line.Length < 8 Then Return 0

		Dim TimePart As String = line.SubString2(0, 8)

		'Legacy lines copied into the following day's file used "c"
		'instead of the first colon.  They are duplicates, not real samples
		'for that date, so never include them in averages.
		If TimePart.Contains("c") Then Return 0

		Dim t() As String = Regex.Split(":", TimePart)
		If t.Length <> 3 Then Return 0
		If IsNumber(t(0)) = False Or IsNumber(t(1)) = False Or IsNumber(t(2)) = False Then Return 0

		Return DateUtils.SetDateAndTime( _
			DateTime.GetYear(FileDay), _
			DateTime.GetMonth(FileDay), _
			DateTime.GetDayOfMonth(FileDay), _
			t(0), t(1), t(2))
	Catch
		Return 0
	End Try
End Sub

Private Sub GetChartLogFileName(FileDay As Long) As String
	Return DateTime.GetYear(FileDay) & "-" & _
		NumberFormat(DateTime.GetMonth(FileDay), 2, 0) & "-" & _
		NumberFormat(DateTime.GetDayOfMonth(FileDay), 2, 0) & ".log"
End Sub

Private Sub ResetChartBuckets
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
	For i = 0 To 23
		BucketHasData(i) = False
	Next
End Sub

Private Sub ApplyBucketAverages(BucketSum() As Double, BucketCount() As Int)
	For i = 0 To 23
		If BucketCount(i) > 0 Then
			SetChartBucket(i, NumberFormat(BucketSum(i) / BucketCount(i), 0, 2))
		End If
	Next
End Sub

Private Sub SetChartBucket(Index As Int, Value As String)
	Select Index
		Case 0
			am12 = Value
		Case 1
			am1 = Value
		Case 2
			am2 = Value
		Case 3
			am3 = Value
		Case 4
			am4 = Value
		Case 5
			am5 = Value
		Case 6
			am6 = Value
		Case 7
			am7 = Value
		Case 8
			am8 = Value
		Case 9
			am9 = Value
		Case 10
			am10 = Value
		Case 11
			am11 = Value
		Case 12
			pm12 = Value
		Case 13
			pm1 = Value
		Case 14
			pm2 = Value
		Case 15
			pm3 = Value
		Case 16
			pm4 = Value
		Case 17
			pm5 = Value
		Case 18
			pm6 = Value
		Case 19
			pm7 = Value
		Case 20
			pm8 = Value
		Case 21
			pm9 = Value
		Case 22
			pm10 = Value
		Case 23
			pm11 = Value
	End Select
	If Index >= 0 And Index <= 23 Then BucketHasData(Index) = True
End Sub



' ============================================================
' CHART DISPLAY HELPERS - PHASE 4.6
'
' Missing buckets remain missing. The legacy wrapper only accepts
' Float arrays, so it is first given an aligned placeholder array.
' After DrawTheGraphs creates its SimpleXYSeries objects, Reflection
' changes each missing Y value to Java null. AndroidPlot renders null
' Y values as breaks in the line instead of inventing a reading.
' ============================================================

Private Sub HasCurrentChartValue As Boolean
	Return tempRightNow <> "" And IsNumber(tempRightNow)
End Sub

Private Sub GetSafeCurrentValue(DefaultValue As Float) As Float
	If HasCurrentChartValue Then Return tempRightNow
	Return DefaultValue
End Sub

Private Sub CopyBucketFlags As Boolean()
	' Balanced geometry:
	' index 0 = one left spacer
	' indexes 1..24 = the 24 real buckets
	' index 25 = one right spacer
	Dim Result(26) As Boolean
	For i = 0 To 23
		Result(i + 1) = BucketHasData(i)
	Next
	Return Result
End Sub

Private Sub BuildChartData(DefaultValue As Float) As Float()
	Dim Result(26) As Float
	Dim Placeholder As Float = DefaultValue

	For i = 0 To 23
		If BucketHasData(i) Then
			Placeholder = GetChartBucketValue(i)
			Exit
		End If
	Next

	For i = 0 To 25
		Result(i) = Placeholder
	Next

	For i = 0 To 23
		If BucketHasData(i) Then
			Result(i + 1) = GetChartBucketValue(i)
		End If
	Next
	Return Result
End Sub

Private Sub BuildFlatLine(Value As Float) As Float()
	Dim Result(26) As Float
	For i = 0 To 25
		Result(i) = Value
	Next
	Return Result
End Sub

Private Sub BuildGreenReferenceLabelFlags As Boolean()
	' Show one green value at the RIGHT spacer (original series index 25).
	' The wrapper renderer preserves the original series index for labels.
	Dim Result(26) As Boolean
	Result(25) = True
	Return Result
End Sub

Private Sub GetChartBucketValue(Index As Int) As Float
	Select Index
		Case 0: Return am12
		Case 1: Return am1
		Case 2: Return am2
		Case 3: Return am3
		Case 4: Return am4
		Case 5: Return am5
		Case 6: Return am6
		Case 7: Return am7
		Case 8: Return am8
		Case 9: Return am9
		Case 10: Return am10
		Case 11: Return am11
		Case 12: Return pm12
		Case 13: Return pm1
		Case 14: Return pm2
		Case 15: Return pm3
		Case 16: Return pm4
		Case 17: Return pm5
		Case 18: Return pm6
		Case 19: Return pm7
		Case 20: Return pm8
		Case 21: Return pm9
		Case 22: Return pm10
		Case 23: Return pm11
	End Select
	Return 0
End Sub

Private Sub SetYAxisRangeSingle(Data() As Float, HasData() As Boolean, _
	CurrentValue As Float, CurrentValid As Boolean, IsHumidity As Boolean)
	Dim MinValue As Double = 1E+20
	Dim MaxValue As Double = -1E+20
	Dim Found As Boolean = False

	For i = 0 To Data.Length - 1
		If HasData(i) Then
			If Data(i) < MinValue Then MinValue = Data(i)
			If Data(i) > MaxValue Then MaxValue = Data(i)
			Found = True
		End If
	Next

	If CurrentValid Then
		If CurrentValue < MinValue Then MinValue = CurrentValue
		If CurrentValue > MaxValue Then MaxValue = CurrentValue
		Found = True
	End If

	ApplySafeYAxisRange(MinValue, MaxValue, Found, IsHumidity)
End Sub

Private Sub SetYAxisRangeDouble(Data1() As Float, HasData1() As Boolean, _
	Data2() As Float, HasData2() As Boolean, CurrentValue As Float, _
	CurrentValid As Boolean, IsHumidity As Boolean)
	Dim MinValue As Double = 1E+20
	Dim MaxValue As Double = -1E+20
	Dim Found As Boolean = False

	For i = 0 To Data1.Length - 1
		If HasData1(i) Then
			If Data1(i) < MinValue Then MinValue = Data1(i)
			If Data1(i) > MaxValue Then MaxValue = Data1(i)
			Found = True
		End If
		If HasData2(i) Then
			If Data2(i) < MinValue Then MinValue = Data2(i)
			If Data2(i) > MaxValue Then MaxValue = Data2(i)
			Found = True
		End If
	Next

	If CurrentValid Then
		If CurrentValue < MinValue Then MinValue = CurrentValue
		If CurrentValue > MaxValue Then MaxValue = CurrentValue
		Found = True
	End If

	ApplySafeYAxisRange(MinValue, MaxValue, Found, IsHumidity)
End Sub

Private Sub ApplySafeYAxisRange(MinValue As Double, MaxValue As Double, _
	Found As Boolean, IsHumidity As Boolean)
	If Found = False Then
		If IsHumidity Then
			LineChart.YaxisRange(0, 100)
		Else
			LineChart.YaxisRange(60, 80)
		End If
		Return
	End If

	Dim Span As Double = MaxValue - MinValue
	Dim Margin As Double
	If Span <= 0.01 Then
		Margin = 1
	Else If Span >= 20 Then
		Margin = 2
	Else
		Margin = 0.5
	End If

	Dim Lower As Double = MinValue - Margin
	Dim Upper As Double = MaxValue + Margin
	If IsHumidity And Lower < 0 Then Lower = 0
	If Upper <= Lower Then Upper = Lower + 2
	LineChart.YaxisRange(Lower, Upper)
End Sub

Private Sub ApplyMissingGaps(LineNumber As Int, HasData() As Boolean)
	Try
		' AndroidPlot patched V4 works directly on the wrapper-created
		' SimpleXYSeries, so placeholder values at missing positions are
		' converted to real Java nulls and are not drawn.
		LineChart.ApplyMissingGaps(LineNumber, HasData)
	Catch
		Log("ApplyMissingGaps: " & LastException)
	End Try
End Sub

Private Sub BuildHourlyAxisLabels As String()
	Dim Labels(26) As String
	Labels(0) = ""
	For i = 0 To 23
		Labels(i + 1) = timeArray(i)
	Next
	Labels(25) = ""
	Return Labels
End Sub

Private Sub BuildDailyAxisLabels As String()
	Dim Hours() As String = Array As String( _
		"12 am","1 am","2 am","3 am","4 am","5 am","6 am","7 am", _
		"8 am","9 am","10 am","11 am","12 pm","1 pm","2 pm","3 pm", _
		"4 pm","5 pm","6 pm","7 pm","8 pm","9 pm","10 pm","11 pm")
	Dim Labels(26) As String
	Labels(0) = ""
	For i = 0 To 23
		Labels(i + 1) = Hours(i)
	Next
	Labels(25) = ""
	Return Labels
End Sub

Private Sub GetFiveMinuteWindowEnd(Ticks As Long) As Long
	Dim MinuteValue As Int = DateTime.GetMinute(Ticks)
	Dim MinutesToAdd As Int = 5 - (MinuteValue Mod 5)
	Dim MinuteStart As Long = DateUtils.SetDateAndTime( _
		DateTime.GetYear(Ticks), DateTime.GetMonth(Ticks), DateTime.GetDayOfMonth(Ticks), _
		DateTime.GetHour(Ticks), MinuteValue, 0)
	Return MinuteStart + (MinutesToAdd * DateTime.TicksPerMinute)
End Sub

Sub TemperatureHourlyTimer_Tick
	Activity.RequestFocus
	TemperatureHourlyCreate
End Sub

Sub HumidityHourlyTimer_Tick
	Activity.RequestFocus
	HumidityHourlyCreate
End Sub

Sub TemperatureDailyTimer_Tick
	Activity.RequestFocus
	TemperatureDailyCreate
End Sub

Sub HumidityDailyTimer_Tick
	Activity.RequestFocus
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
