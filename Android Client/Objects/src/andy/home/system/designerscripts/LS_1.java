package andy.home.system.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_1{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="AutoScaleAll"[1/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 3;BA.debugLine="If ActivitySize > 6.5 Then"[1/General script]
if ((anywheresoftware.b4a.keywords.LayoutBuilder.getScreenSize()>6.5d)) { 
;
//BA.debugLineNum = 4;BA.debugLine="ACToolBarLight1.Height = 64dip"[1/General script]
views.get("actoolbarlight1").vw.setHeight((int)((64d * scale)));
//BA.debugLineNum = 5;BA.debugLine="Else"[1/General script]
;}else{ 
;
//BA.debugLineNum = 6;BA.debugLine="If Portrait Then"[1/General script]
if ((BA.ObjectToBoolean( String.valueOf(anywheresoftware.b4a.keywords.LayoutBuilder.isPortrait())))) { 
;
//BA.debugLineNum = 7;BA.debugLine="ACToolBarLight1.Height = 56dip"[1/General script]
views.get("actoolbarlight1").vw.setHeight((int)((56d * scale)));
//BA.debugLineNum = 8;BA.debugLine="Else"[1/General script]
;}else{ 
;
//BA.debugLineNum = 9;BA.debugLine="ACToolBarLight1.Height = 48dip"[1/General script]
views.get("actoolbarlight1").vw.setHeight((int)((48d * scale)));
//BA.debugLineNum = 10;BA.debugLine="End If"[1/General script]
;};
//BA.debugLineNum = 11;BA.debugLine="End If"[1/General script]
;};

}
}