package cloyd.home.weather.station.designerscripts;
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
//BA.debugLineNum = 13;BA.debugLine="ACToolBarLight1.SetLeftAndRight(0,100%x)"[1/General script]
views.get("actoolbarlight1").vw.setLeft((int)(0d));
views.get("actoolbarlight1").vw.setWidth((int)((100d / 100 * width) - (0d)));
//BA.debugLineNum = 15;BA.debugLine="lblPerception.Top = ACToolBarLight1.bottom + 10dip"[1/General script]
views.get("lblperception").vw.setTop((int)((views.get("actoolbarlight1").vw.getTop() + views.get("actoolbarlight1").vw.getHeight())+(10d * scale)));
//BA.debugLineNum = 16;BA.debugLine="lblComfort.Top = lblPerception.Bottom + 5dip"[1/General script]
views.get("lblcomfort").vw.setTop((int)((views.get("lblperception").vw.getTop() + views.get("lblperception").vw.getHeight())+(5d * scale)));
//BA.debugLineNum = 18;BA.debugLine="GaugeHeatIndex.Width = 200dip"[1/General script]
views.get("gaugeheatindex").vw.setWidth((int)((200d * scale)));
//BA.debugLineNum = 19;BA.debugLine="GaugeHeatIndex.top = lblComfort.Bottom + 10dip"[1/General script]
views.get("gaugeheatindex").vw.setTop((int)((views.get("lblcomfort").vw.getTop() + views.get("lblcomfort").vw.getHeight())+(10d * scale)));
//BA.debugLineNum = 20;BA.debugLine="GaugeHeatIndex.SetLeftAndRight(-10dip,50%x)"[1/General script]
views.get("gaugeheatindex").vw.setLeft((int)(0-(10d * scale)));
views.get("gaugeheatindex").vw.setWidth((int)((50d / 100 * width) - (0-(10d * scale))));
//BA.debugLineNum = 22;BA.debugLine="GaugeDewPoint.Width = 200dip"[1/General script]
views.get("gaugedewpoint").vw.setWidth((int)((200d * scale)));
//BA.debugLineNum = 23;BA.debugLine="GaugeDewPoint.top = lblComfort.Bottom + 10dip"[1/General script]
views.get("gaugedewpoint").vw.setTop((int)((views.get("lblcomfort").vw.getTop() + views.get("lblcomfort").vw.getHeight())+(10d * scale)));
//BA.debugLineNum = 24;BA.debugLine="GaugeDewPoint.SetLeftAndRight(50%x,100%x+10dip)"[1/General script]
views.get("gaugedewpoint").vw.setLeft((int)((50d / 100 * width)));
views.get("gaugedewpoint").vw.setWidth((int)((100d / 100 * width)+(10d * scale) - ((50d / 100 * width))));
//BA.debugLineNum = 26;BA.debugLine="GaugeTemp.Width = 200dip"[1/General script]
views.get("gaugetemp").vw.setWidth((int)((200d * scale)));
//BA.debugLineNum = 27;BA.debugLine="GaugeTemp.top = GaugeHeatIndex.Bottom + 40dip"[1/General script]
views.get("gaugetemp").vw.setTop((int)((views.get("gaugeheatindex").vw.getTop() + views.get("gaugeheatindex").vw.getHeight())+(40d * scale)));
//BA.debugLineNum = 28;BA.debugLine="GaugeTemp.SetLeftAndRight(-10dip,50%x)"[1/General script]
views.get("gaugetemp").vw.setLeft((int)(0-(10d * scale)));
views.get("gaugetemp").vw.setWidth((int)((50d / 100 * width) - (0-(10d * scale))));
//BA.debugLineNum = 30;BA.debugLine="GaugeHumidity.Width = 200dip"[1/General script]
views.get("gaugehumidity").vw.setWidth((int)((200d * scale)));
//BA.debugLineNum = 31;BA.debugLine="GaugeHumidity.top = GaugeHeatIndex.Bottom + 40dip"[1/General script]
views.get("gaugehumidity").vw.setTop((int)((views.get("gaugeheatindex").vw.getTop() + views.get("gaugeheatindex").vw.getHeight())+(40d * scale)));
//BA.debugLineNum = 32;BA.debugLine="GaugeHumidity.SetLeftAndRight(50%x,100%x+10dip)"[1/General script]
views.get("gaugehumidity").vw.setLeft((int)((50d / 100 * width)));
views.get("gaugehumidity").vw.setWidth((int)((100d / 100 * width)+(10d * scale) - ((50d / 100 * width))));
//BA.debugLineNum = 34;BA.debugLine="lblLastUpdate.Top = GaugeHumidity.Bottom + 60dip"[1/General script]
views.get("lbllastupdate").vw.setTop((int)((views.get("gaugehumidity").vw.getTop() + views.get("gaugehumidity").vw.getHeight())+(60d * scale)));
//BA.debugLineNum = 36;BA.debugLine="lblAuthor.Bottom = 100%y"[1/General script]
views.get("lblauthor").vw.setTop((int)((100d / 100 * height) - (views.get("lblauthor").vw.getHeight())));
//BA.debugLineNum = 37;BA.debugLine="lblAuthor.Width = 100%x"[1/General script]
views.get("lblauthor").vw.setWidth((int)((100d / 100 * width)));
//BA.debugLineNum = 39;BA.debugLine="If (100%y)<(lblLastUpdate.Bottom) Then"[1/General script]
if ((((100d / 100 * height))<((views.get("lbllastupdate").vw.getTop() + views.get("lbllastupdate").vw.getHeight())))) { 
;
//BA.debugLineNum = 40;BA.debugLine="lblLastUpdate.Top = GaugeHumidity.Bottom + 20dip"[1/General script]
views.get("lbllastupdate").vw.setTop((int)((views.get("gaugehumidity").vw.getTop() + views.get("gaugehumidity").vw.getHeight())+(20d * scale)));
//BA.debugLineNum = 41;BA.debugLine="End If"[1/General script]
;};
//BA.debugLineNum = 43;BA.debugLine="lblPing.Top = lblLastUpdate.Top"[1/General script]
views.get("lblping").vw.setTop((int)((views.get("lbllastupdate").vw.getTop())));
//BA.debugLineNum = 44;BA.debugLine="lblPing.Right = 100%x"[1/General script]
views.get("lblping").vw.setLeft((int)((100d / 100 * width) - (views.get("lblping").vw.getWidth())));

}
}