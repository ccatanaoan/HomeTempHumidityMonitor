B4J=true
Group=Default Group
ModulesStructureVersion=1
Type=Class
Version=6
@EndOfDesignText@
'version 2.00
#DesignerProperty: Key: MinValue, DisplayName: Min Value, FieldType: Float, DefaultValue: 0
#DesignerProperty: Key: MaxValue, DisplayName: Max Value, FieldType: Float, DefaultValue: 100
#DesignerProperty: Key: IndicatorLength, DisplayName: Indicator Length, FieldType: Int, DefaultValue: 20
#DesignerProperty: Key: CenterColor, DisplayName: Center Color, FieldType: Color, DefaultValue: 0xFF7C7C7C
#DesignerProperty: Key: BackgroundColor, DisplayName: Background Color, FieldType: Color, DefaultValue: 0xFFFFFFFF
#DesignerProperty: Key: IndicatorColor, DisplayName: Indicator Color, FieldType: Color, DefaultValue: 0xFF000000
#DesignerProperty: Key: PrefixText, DisplayName: Prefix Text, FieldType: String, DefaultValue: 
#DesignerProperty: Key: SuffixText, DisplayName: Suffix Text, FieldType: String, DefaultValue: °
#DesignerProperty: Key: Duration, DisplayName: Duration From 0 To 100, FieldType: Int, DefaultValue: 3000, Description: Milliseconds
#DesignerProperty: Key: GaugeType, DisplayName: Gauge Type, FieldType: String, DefaultValue: Half Circle, List:Half Circle|Full Circle
Sub Class_Globals
	Private mEventName As String 'ignore
	Private mCallBack As Object 'ignore
	Private mBase As B4XView
	Private xui As XUI
	Private cvsGauge, cvsIndicator As B4XCanvas
	Private mlbl As B4XView
	Type GaugeRange (LowValue As Float, HighValue As Float, Color As Int)
	Private mRanges As List
	Private GaugePanel, IndicatorPanel As B4XView
	Private Radius, x, y As Float
	Private IndicatorLength As Int
	Private CenterColor, IndicatorColor As Int
	Private MinValue, MaxValue As Float
	Private IndicatorBaseWidth As Float 
	Private mCurrentValue As Float = 50
	Private PrefixText, SuffixText As String
	Private DurationFromZeroTo100 As Int
	Private HALF_CIRCLE = 1, FULL_CIRCLE = 2 As Int
	Private GaugeType As Int
	Private AngleRange As Int
	Private AngleOffset As Int
	Private BackgroundColor As Int
End Sub

Public Sub Initialize (Callback As Object, EventName As String)
	mEventName = EventName
	mCallBack = Callback
	mRanges.Initialize
End Sub

'Base type must be Object
Public Sub DesignerCreateView (Base As Object, Lbl As Label, Props As Map)
	IndicatorLength = DipToCurrent(Props.Get("IndicatorLength"))
	CenterColor = xui.PaintOrColorToColor(Props.Get("CenterColor"))
	IndicatorColor = xui.PaintOrColorToColor(Props.Get("IndicatorColor"))
	BackgroundColor = xui.PaintOrColorToColor(Props.GetDefault("BackgroundColor", xui.Color_White))
	Dim m As Map = CreateMap("Half Circle": HALF_CIRCLE, "Full Circle": FULL_CIRCLE)
	GaugeType = m.Get(Props.GetDefault("GaugeType", "Half Circle"))
	MinValue = Props.Get("MinValue")
	MaxValue = Props.Get("MaxValue")
	PrefixText = Props.Get("PrefixText")
	SuffixText = Props.Get("SuffixText")
	DurationFromZeroTo100 = Props.Get("Duration")
	mBase = Base
	Dim NativeFont As Object
#if B4J
	Dim fx As JFX
	NativeFont = fx.LoadFont(File.DirAssets, "Crysta.ttf", 20)
	Lbl.Style = ""
#else if B4A
	NativeFont = Typeface.LoadFromAssets("Crysta.ttf")
#else if B4i
	NativeFont = Font.CreateNew2("Crystal", 20)
#End If
	GaugePanel = xui.CreatePanel("")
	
	mBase.AddView(GaugePanel, 0, 0, mBase.Width, mBase.Height)
	cvsGauge.Initialize(GaugePanel)
	IndicatorPanel = xui.CreatePanel("")
	mBase.AddView(IndicatorPanel, 0, 0, mBase.Width, mBase.Height)
	cvsIndicator.Initialize(IndicatorPanel)
	mlbl = Lbl
	mlbl.Font = xui.CreateFont(NativeFont, 30)
	mlbl.SetTextAlignment("CENTER", "CENTER")
	mlbl.TextColor = xui.Color_Black
	Dim lblheight As Float
	If PrefixText.Contains(CRLF) Then lblheight = 65dip Else lblheight = 35dip
	mBase.AddView(mlbl, 0, 0, 0, lblheight) 'label size
	Base_Resize(mBase.Width, mBase.Height)
End Sub

Private Sub Base_Resize (Width As Double, Height As Double)
 	mlbl.SetLayoutAnimated(0, 0, Height - mlbl.Height - 5dip, Width, mlbl.Height)
	GaugePanel.SetLayoutAnimated(0, 0, 0, Width, Height)
	cvsGauge.Resize(Width, Height)
	IndicatorPanel.SetLayoutAnimated(0, 0, 0, Width, Height)
	cvsIndicator.Resize(Width, Height)
	
	DrawBackground
End Sub

Public Sub SetMinAndMax(NewMinValue As Float, NewMaxValue As Float)
	MinValue = NewMinValue
	MaxValue = NewMaxValue
	DrawBackground
End Sub

Private Sub DrawBackground
	cvsGauge.ClearRect(cvsGauge.TargetRect)
	Select GaugeType
		Case HALF_CIRCLE
			ConfigureHalfCircle
		Case FULL_CIRCLE 
			ConfigureFullCircle
	End Select
	cvsGauge.DrawCircle(x, y, Radius + 1dip, BackgroundColor, True, 0)
	For Each gr As GaugeRange In mRanges
		Dim p As B4XPath
		Dim StartAngle As Float = ValueToAngle(gr.LowValue)
		p.InitializeArc(x, y, Radius, StartAngle, ValueToAngle(gr.HighValue) - StartAngle)
		cvsGauge.ClipPath(p)
		cvsGauge.DrawCircle(x, y, Radius, gr.Color, True, 0)
		cvsGauge.RemoveClip
	Next
	cvsGauge.DrawCircle(x, y, Radius - IndicatorLength, BackgroundColor, True, 0)
	If GaugeType = FULL_CIRCLE And PrefixText <> "" Then
		cvsGauge.DrawText(PrefixText, x, y - 30dip, xui.CreateDefaultFont(16), xui.Color_Black, "CENTER")
		'outer border
		cvsGauge.DrawCircle(x, y, Radius + 1dip, xui.Color_Gray, False, 1dip)
	End If
	DrawTicks
	cvsGauge.Invalidate
	DrawIndicator(mCurrentValue)
End Sub

Private Sub ConfigureHalfCircle
	Radius = Min(GaugePanel.Width / 2, GaugePanel.Height)
	x = cvsGauge.TargetRect.CenterX
	y = cvsGauge.TargetRect.Height
	AngleOffset = 180
	AngleRange = 180
	IndicatorBaseWidth = 20dip
End Sub

Private Sub ConfigureFullCircle
	Radius = Min(GaugePanel.Width, GaugePanel.Height) / 2 - 3dip
	x = cvsGauge.TargetRect.CenterX
	y = cvsGauge.TargetRect.CenterY
	AngleOffset = 135
	AngleRange = 270
	IndicatorBaseWidth = 6dip
	CenterColor = IndicatorColor
	
End Sub

Private Sub DrawTicks
	Dim r1 As Int = Radius - 2dip
	Dim LongTick As Int = r1 - 7dip
	Dim ShortTick As Int = r1 - 5dip
	Dim NumberOfTicks As Int
	If GaugeType = HALF_CIRCLE Then NumberOfTicks = 10 Else NumberOfTicks = 20
	For i = 0 To NumberOfTicks
		Dim thickness, r As Int
		Dim angle As Float = i * AngleRange / NumberOfTicks + AngleOffset
		If i Mod 5 = 0 Then
			thickness = 3dip
			r = LongTick
			Dim tr As Float = r - 12dip
			cvsGauge.DrawTextRotated(NumberFormat(MinValue + i / NumberOfTicks * (MaxValue - MinValue), 1, 0), _
               x + tr * CosD(angle), y + tr * SinD(angle), xui.CreateDefaultFont(10), xui.Color_Black, "CENTER", angle + 90)
		Else
			thickness = 1dip
			r = ShortTick
		End If
		cvsGauge.DrawLine(x + r * CosD(angle), y + r * SinD(angle), x + r1 * CosD(angle), y + r1 * SinD(angle), xui.Color_Black, thickness)
	Next
End Sub



Private Sub DrawIndicator (Value As Float)
	cvsIndicator.ClearRect(cvsIndicator.TargetRect)
	Dim angle As Float = ValueToAngle(Value)
	Dim p As B4XPath
	p.Initialize(x + IndicatorBaseWidth * SinD(angle), y - IndicatorBaseWidth * CosD(angle))
	Dim length As Float = Radius - 0.3 * IndicatorLength
	p.LineTo(x + length * CosD(angle), y + length * SinD(angle))
	p.LineTo(x - IndicatorBaseWidth * SinD(angle), y + IndicatorBaseWidth * CosD(angle))
	cvsIndicator.ClipPath(p)
	cvsIndicator.DrawRect(cvsIndicator.TargetRect, IndicatorColor, True, 0)
	cvsIndicator.RemoveClip
	Dim CircleRadius As Float
	Dim s As String
	If GaugeType = HALF_CIRCLE Then
		s = PrefixText.ToUpperCase
		CircleRadius =  Radius - IndicatorLength
	Else
		CircleRadius = IndicatorBaseWidth
	End If
	cvsIndicator.DrawCircle(x, y, CircleRadius, CenterColor, True, 0)
	cvsIndicator.Invalidate
	mlbl.Text = s & NumberFormat2(Value, 2, 1, 1, True) & SuffixText
End Sub

Private Sub ValueToAngle (Value As Float) As Float
	Return (Value - MinValue) / (MaxValue - MinValue) * AngleRange + AngleOffset
End Sub

Public Sub SetRanges(Ranges As List)
	mRanges = Ranges
	For Each r As GaugeRange In Ranges
		r.Color = Bit.And(0x00ffffff, r.Color)
		r.Color = Bit.Or(0x88000000, r.Color)
	Next
	DrawBackground
End Sub

Public Sub CreateRange(LowValue As Float, HighValue As Float, Color As Int) As GaugeRange
	Dim r As GaugeRange
	r.Initialize
	r.LowValue = LowValue
	r.HighValue = HighValue
	r.Color = Color
	Return r
End Sub

Public Sub setCurrentValue (v As Float)
	Dim NewValue As Float = Min(MaxValue, Max(MinValue, v))
	AnimateValueTo(NewValue)
End Sub

Public Sub getCurrentValue As Float
	Return mCurrentValue
End Sub

Private Sub AnimateValueTo(NewValue As Float)
	Dim n As Long = DateTime.Now
	Dim duration As Int = Abs(mCurrentValue - NewValue) / 100 * DurationFromZeroTo100 + 1000
	Dim start As Float = mCurrentValue
	mCurrentValue = NewValue
	Dim tempValue As Float
	Do While DateTime.Now < n + duration
		tempValue = ValueFromTimeEaseInOut(DateTime.Now - n, start, NewValue - start, duration)
		DrawIndicator(tempValue)
		Sleep(10)
		If NewValue <> mCurrentValue Then Return 'will happen if another update has started
	Loop
	DrawIndicator(mCurrentValue)
End Sub

'quartic easing in/out from http://gizma.com/easing/
Private Sub ValueFromTimeEaseInOut(Time As Float, Start As Float, ChangeInValue As Float, Duration As Int) As Float
	Time = Time / (Duration / 2)
	If Time < 1 Then
		Return ChangeInValue / 2 * Time * Time * Time * Time + Start
	Else
		Time = Time - 2
		Return -ChangeInValue / 2 * (Time * Time * Time * Time - 2) + Start
	End If
End Sub