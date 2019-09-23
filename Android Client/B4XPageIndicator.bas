B4J=true
Group=Default Group
ModulesStructureVersion=1
Type=Class
Version=7.32
@EndOfDesignText@
#DesignerProperty: Key: Color, DisplayName: Color, FieldType: Color, DefaultValue: 0xFFFFFFFF
#DesignerProperty: Key: Page Count, DisplayName: Page Count, FieldType: Int, DefaultValue: 3

Sub Class_Globals
	Private mEventName As String 'ignore
	Private mCallBack As Object 'ignore
	Public mBase As B4XView 'ignore
	Private xui As XUI 'ignore
	Private clr As Int
	Private bc As BitmapCreator
	Private mCurrentPage As Int
	Private mCount As Int
	Private iv As B4XView
	Private Transparent As BCBrush
	Private ActiveBrush, InactiveBrush As BCBrush
	Private r As Int = 5dip
	Private gap As Int = 15dip
End Sub

Public Sub Initialize (Callback As Object, EventName As String)
	mEventName = EventName
	mCallBack = Callback
End Sub

'Base type must be Object
Public Sub DesignerCreateView (Base As Object, Lbl As Label, Props As Map)
	mBase = Base
	mBase.BringToFront
	clr = xui.PaintOrColorToColor(Props.Get("Color")) 'Example of getting a color value from Props
	mCount = Max(1, Props.Get("Page Count"))
	Dim ImageView1 As ImageView
	ImageView1.Initialize("")
	iv = ImageView1
	mBase.AddView(iv, 0, 0, 0, 0)
	If mBase.Width > 0 Then
		Base_Resize(mBase.Width, mBase.Height)
	End If
End Sub

Private Sub Base_Resize (Width As Double, Height As Double)
	If bc.IsInitialized = False Or bc.mWidth <> Width Then
		bc.Initialize(Width, 20dip)
		Transparent = bc.CreateBrushFromColor(xui.Color_Transparent)
		ActiveBrush = bc.CreateBrushFromColor(clr)
		InactiveBrush = bc.CreateBrushFromColor(Bit.Or(0x55000000, Bit.And(0x00FFFFFF, clr)))
	End If
	iv.SetLayoutAnimated(0, 0, 0, Width, bc.mHeight)
	Draw
End Sub

Private Sub Draw
	bc.DrawRect2(bc.TargetRect, Transparent, True, 0)
	Dim TotalWidth As Int = (mCount - 1) * gap
	Dim StartX As Int = bc.mWidth / 2 - TotalWidth / 2
	For i = 0 To mCount - 1
		Dim brush As BCBrush
		If mCurrentPage = i Then brush = ActiveBrush Else brush = InactiveBrush
		bc.DrawCircle2(StartX + gap * i, bc.mHeight / 2, r, brush, True, 0)
	Next
	bc.SetBitmapToImageView(bc.Bitmap, iv)
End Sub

Public Sub getCurrentPage As Int
	Return mCurrentPage
End Sub

Public Sub setCurrentPage(i As Int)
	mCurrentPage = i
	Draw
End Sub

Public Sub getCount As Int
	Return mCount
End Sub

Public Sub setCount(c As Int)
	mCount = c
	Draw
End Sub