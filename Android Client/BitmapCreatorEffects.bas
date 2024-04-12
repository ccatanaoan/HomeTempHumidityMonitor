B4J=true
Group=Default Group
ModulesStructureVersion=1
Type=Class
Version=6.3
@EndOfDesignText@
'Version 1.00
Sub Class_Globals
	Private xui As XUI 'ignore
	Type BCEPixelGroup (SrcX As Int, SrcY As Int, x As Float, y As Float, dx As Float, dy As Float)
End Sub

Public Sub Initialize
	
End Sub

Private Sub CreateBC(bmp As B4XBitmap) As BitmapCreator
	Dim bc As BitmapCreator
	bc.Initialize(bmp.Width / bmp.Scale, bmp.Height / bmp.Scale)
	bc.CopyPixelsFromBitmap(bmp)
	Return bc
End Sub

Public Sub GreyScale (bmp As B4XBitmap) As B4XBitmap
	Dim bc As BitmapCreator = CreateBC(bmp)
	Dim argb As ARGBColor
	For x = 0 To bc.mWidth - 1
		For y = 0 To bc.mHeight - 1
			bc.GetARGB(x, y, argb)
			Dim c As Int = argb.r * 0.21 + argb.g * 0.72 + 0.07 * argb.b
			argb.r = c
			argb.g = c
			argb.b = c
			bc.SetARGB(x, y, argb)
		Next
	Next
	Return bc.Bitmap
End Sub

Public Sub Negate (bmp As B4XBitmap) As B4XBitmap
	Dim bc As BitmapCreator = CreateBC(bmp)
	Dim argb As ARGBColor
	For x = 0 To bc.mWidth - 1
		For y = 0 To bc.mHeight - 1
			bc.GetARGB(x, y, argb)
			argb.r = Bit.Xor(argb.r, 0xff)
			argb.g = Bit.Xor(argb.g, 0xff)
			argb.b = Bit.Xor(argb.b, 0xff)
			bc.SetARGB(x, y, argb)
		Next
	Next
	Return bc.Bitmap
End Sub


Public Sub Blur (bmp As B4XBitmap) As B4XBitmap
	Dim bc As BitmapCreator
	Dim ReduceScale As Int = 2
	bc.Initialize(bmp.Width / ReduceScale / bmp.Scale, bmp.Height / ReduceScale / bmp.Scale)
	bc.CopyPixelsFromBitmap(bmp)
	Dim count As Int = 3
	Dim clrs(3) As ARGBColor
	Dim temp As ARGBColor
	Dim m As Int
	For steps = 1 To count
		For y = 0 To bc.mHeight - 1
			For x = 0 To 2
				bc.GetARGB(x, y, clrs(x))
			Next
			SetAvg(bc, 1, y, clrs, temp)
			m = 0
			For x = 2 To bc.mWidth - 2
				bc.GetARGB(x + 1, y, clrs(m))
				m = (m + 1) Mod clrs.Length
				SetAvg(bc, x, y, clrs, temp)
			Next
		Next
		For x = 0 To bc.mWidth - 1
			For y = 0 To 2
				bc.GetARGB(x, y, clrs(y))
			Next
			SetAvg(bc, x, 1, clrs, temp)
			m = 0
			For y = 2 To bc.mHeight - 2
				bc.GetARGB(x, y + 1, clrs(m))
				m = (m + 1) Mod clrs.Length
				SetAvg(bc, x, y, clrs, temp)
			Next
		Next
	Next
	Dim res As B4XBitmap = bc.Bitmap
	If ReduceScale > 1 Then res = res.Resize(ReduceScale * res.Width * xui.Scale, ReduceScale * res.Height * xui.Scale, False)
	Return res
End Sub

Private Sub SetAvg(bc As BitmapCreator, x As Int, y As Int, clrs() As ARGBColor, temp As ARGBColor)
	temp.Initialize
	For Each c As ARGBColor In clrs
		temp.r = temp.r + c.r
		temp.g = temp.g + c.g
		temp.b = temp.b + c.b
	Next
	temp.a = 255
	temp.r = temp.r / clrs.Length
	temp.g = temp.g / clrs.Length
	temp.b = temp.b / clrs.Length
	bc.SetARGB(x, y, temp)
End Sub

Public Sub Brightness(Bmp As B4XBitmap, Factor As Float) As B4XBitmap
	Dim bc As BitmapCreator = CreateBC(Bmp)
	Dim argb As ARGBColor
	For x = 0 To bc.mWidth - 1
		For y = 0 To bc.mHeight - 1
			bc.GetARGB(x, y, argb)
			argb.r = Min(255, argb.r * Factor)
			argb.g = Min(255, argb.g * Factor)
			argb.b = Min(255, argb.b * Factor)
			bc.SetARGB(x, y, argb)
		Next
	Next
	Return bc.Bitmap
End Sub

Public Sub Pixelate (Bmp As B4XBitmap, BoxSize As Int) As B4XBitmap
	Dim bc As BitmapCreator = CreateBC(Bmp)
	Dim rect As B4XRect
	rect.Initialize(0, 0, 0, 0)
	For x = 0 To bc.mWidth - 1 Step BoxSize
		rect.Left = x
		rect.Width = BoxSize
		For y = 0 To bc.mHeight - 1 Step BoxSize
			rect.Top = y
			rect.Height = BoxSize
			bc.FillRect(bc.GetColor(Min(bc.mWidth - 1, x + BoxSize / 2), _
				 Min(bc.mHeight - 1, y + BoxSize / 2)), rect)
		Next
	Next
	Return bc.Bitmap
End Sub

Public Sub PixelateAnimated (Duration As Int, Bmp As B4XBitmap, FromBoxSize As Int, ToBoxSize As Int, ImageView As B4XView) As ResumableSub
	Dim steps As Int = Min(10, Abs(ToBoxSize - FromBoxSize))
	Dim SleepLength As Int = Duration / steps
	Dim delta As Int = Round((ToBoxSize - FromBoxSize) / steps)
	For i = FromBoxSize To ToBoxSize Step delta
		ImageView.SetBitmap(Pixelate(Bmp, i))
		Sleep(SleepLength)
	Next
	ImageView.SetBitmap(Pixelate(Bmp, ToBoxSize))
	Return True
End Sub

'assuming that mask is the same size or larger than Bmp.
Public Sub DrawThroughMask (Bmp As B4XBitmap, Mask As B4XBitmap) As B4XBitmap
	Dim source As BitmapCreator = CreateBC(Bmp)
	Dim maskbc As BitmapCreator = CreateBC(Mask)
	Dim transparent As ARGBColor
	transparent.a = 0
	Dim argb1, argb2 As ARGBColor
	For x = 0 To source.mWidth - 1
		For y = 0 To source.mHeight - 1
			If maskbc.IsTransparent(x, y) Then
				source.SetARGB(x, y, transparent)
			Else
				maskbc.GetARGB(x, y, argb1)
				If argb1.a < 255 Then
					source.GetARGB(x, y, argb2)
					argb2.a = (argb2.a * argb1.a) / 255
					source.SetARGB(x, y, argb2)
				End If
			End If
		Next
	Next
	Return source.Bitmap
End Sub

'assuming that mask is the same size or larger than Bmp.
Public Sub DrawOutsideMask (Bmp As B4XBitmap, Mask As B4XBitmap) As B4XBitmap
	Dim source As BitmapCreator = CreateBC(Bmp)
	Dim maskbc As BitmapCreator = CreateBC(Mask)
	Dim transparent As ARGBColor
	transparent.a = 0
	Dim argb1, argb2 As ARGBColor
	For x = 0 To source.mWidth - 1
		For y = 0 To source.mHeight - 1
			If maskbc.IsTransparent(x, y) Then
				
			Else
				maskbc.GetARGB(x, y, argb1)
				If argb1.a = 255 Then
					source.SetARGB(x, y, transparent)
				Else
					source.GetARGB(x, y, argb2)
					argb2.a = (argb2.a * (255 - argb1.a)) / 255
					source.SetARGB(x, y, argb2)
				End If
			End If
		Next
	Next
	Return source.Bitmap
End Sub

Public Sub ImplodeAnimated (Duration As Int, Bmp As B4XBitmap, ImageView As B4XView) As ResumableSub
	Dim source As BitmapCreator = CreateBC(Bmp)
	
	Dim NumberOfSteps As Int = Duration / 20
	Dim steps As Int = NumberOfSteps
	Dim GroupSize As Int = 6
	Dim w As Int = Bmp.Width / Bmp.Scale / GroupSize
	Dim h As Int = Bmp.Height / Bmp.Scale / GroupSize
	Dim pgs(w, h) As BCEPixelGroup
	Dim target As BitmapCreator
	target.Initialize(w * GroupSize, h * GroupSize)
	For x = 0 To w - 1
		For y = 0 To h - 1
			Dim pg As BCEPixelGroup = pgs(x, y)
			pg.SrcX = x * GroupSize
			pg.Srcy = y * GroupSize
			Select Rnd(0, 4)
				Case 0
					pg.x = 0
					pg.y = Rnd(0, target.mHeight)
				Case 1
					pg.x = target.mWidth - 1
					pg.y = Rnd(0, target.mHeight)
				Case 2
					pg.x = Rnd(0, target.mWidth)
					pg.y = 0
				Case 3
					pg.x = Rnd(0, target.mWidth)
					pg.y = target.mHeight - 1
			End Select
			
			pg.dx = (pg.SrcX - pg.x) / steps
			pg.dy = (pg.SrcY - pg.y) / steps
		Next
	Next
	Dim r As B4XRect
	r.Initialize(0, 0, 0, 0)
	For i = 0 To steps - 1
		target.FillRect(xui.Color_Transparent, target.TargetRect)
		For x = 0 To w - 1
			For y = 0 To h - 1
				Dim pg As BCEPixelGroup = pgs(x, y)
				pg.x = pg.x + pg.dx
				pg.y = pg.y + pg.dy
				r.Left = pg.SrcX
				r.Right = pg.SrcX + GroupSize
				r.Top = pg.SrcY
				r.Bottom = pg.SrcY + GroupSize
				target.DrawBitmapCreator(source, r, pg.x, pg.y, True)
			Next
		Next
		ImageView.SetBitmap(target.Bitmap)
		If xui.IsB4i Then
			Sleep(5)
		Else
			Sleep(16)
		End If
	Next
	ImageView.SetBitmap(Bmp)
	Return True
End Sub









