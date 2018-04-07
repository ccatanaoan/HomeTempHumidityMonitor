package cloyd.smart.home.monitor.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_1{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="AutoScaleAll"[1/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 4;BA.debugLine="lblPerception.Top = 10dip"[1/General script]
views.get("lblperception").vw.setTop((int)((10d * scale)));
//BA.debugLineNum = 5;BA.debugLine="lblComfort.Top = lblPerception.Bottom + 5dip"[1/General script]
views.get("lblcomfort").vw.setTop((int)((views.get("lblperception").vw.getTop() + views.get("lblperception").vw.getHeight())+(5d * scale)));
//BA.debugLineNum = 7;BA.debugLine="GaugeHeatIndex.Width = 200dip"[1/General script]
views.get("gaugeheatindex").vw.setWidth((int)((200d * scale)));
//BA.debugLineNum = 8;BA.debugLine="GaugeHeatIndex.top = lblComfort.Bottom + 10dip"[1/General script]
views.get("gaugeheatindex").vw.setTop((int)((views.get("lblcomfort").vw.getTop() + views.get("lblcomfort").vw.getHeight())+(10d * scale)));
//BA.debugLineNum = 9;BA.debugLine="GaugeHeatIndex.SetLeftAndRight(-10dip,50%x)"[1/General script]
views.get("gaugeheatindex").vw.setLeft((int)(0-(10d * scale)));
views.get("gaugeheatindex").vw.setWidth((int)((50d / 100 * width) - (0-(10d * scale))));
//BA.debugLineNum = 11;BA.debugLine="GaugeDewPoint.Width = 200dip"[1/General script]
views.get("gaugedewpoint").vw.setWidth((int)((200d * scale)));
//BA.debugLineNum = 12;BA.debugLine="GaugeDewPoint.top = lblComfort.Bottom + 10dip"[1/General script]
views.get("gaugedewpoint").vw.setTop((int)((views.get("lblcomfort").vw.getTop() + views.get("lblcomfort").vw.getHeight())+(10d * scale)));
//BA.debugLineNum = 13;BA.debugLine="GaugeDewPoint.SetLeftAndRight(50%x,100%x+10dip)"[1/General script]
views.get("gaugedewpoint").vw.setLeft((int)((50d / 100 * width)));
views.get("gaugedewpoint").vw.setWidth((int)((100d / 100 * width)+(10d * scale) - ((50d / 100 * width))));
//BA.debugLineNum = 15;BA.debugLine="GaugeTemp.Width = 200dip"[1/General script]
views.get("gaugetemp").vw.setWidth((int)((200d * scale)));
//BA.debugLineNum = 16;BA.debugLine="GaugeTemp.top = GaugeHeatIndex.Bottom + 40dip"[1/General script]
views.get("gaugetemp").vw.setTop((int)((views.get("gaugeheatindex").vw.getTop() + views.get("gaugeheatindex").vw.getHeight())+(40d * scale)));
//BA.debugLineNum = 17;BA.debugLine="GaugeTemp.SetLeftAndRight(-10dip,50%x)"[1/General script]
views.get("gaugetemp").vw.setLeft((int)(0-(10d * scale)));
views.get("gaugetemp").vw.setWidth((int)((50d / 100 * width) - (0-(10d * scale))));
//BA.debugLineNum = 19;BA.debugLine="GaugeHumidity.Width = 200dip"[1/General script]
views.get("gaugehumidity").vw.setWidth((int)((200d * scale)));
//BA.debugLineNum = 20;BA.debugLine="GaugeHumidity.top = GaugeHeatIndex.Bottom + 40dip"[1/General script]
views.get("gaugehumidity").vw.setTop((int)((views.get("gaugeheatindex").vw.getTop() + views.get("gaugeheatindex").vw.getHeight())+(40d * scale)));
//BA.debugLineNum = 21;BA.debugLine="GaugeHumidity.SetLeftAndRight(50%x,100%x+10dip)"[1/General script]
views.get("gaugehumidity").vw.setLeft((int)((50d / 100 * width)));
views.get("gaugehumidity").vw.setWidth((int)((100d / 100 * width)+(10d * scale) - ((50d / 100 * width))));
//BA.debugLineNum = 23;BA.debugLine="lblLastUpdate.Top = GaugeHumidity.Bottom + 50dip"[1/General script]
views.get("lbllastupdate").vw.setTop((int)((views.get("gaugehumidity").vw.getTop() + views.get("gaugehumidity").vw.getHeight())+(50d * scale)));
//BA.debugLineNum = 25;BA.debugLine="If (100%y)<(lblLastUpdate.Bottom) Then"[1/General script]
if ((((100d / 100 * height))<((views.get("lbllastupdate").vw.getTop() + views.get("lbllastupdate").vw.getHeight())))) { 
;
//BA.debugLineNum = 26;BA.debugLine="lblLastUpdate.Top = GaugeHumidity.Bottom + 20dip"[1/General script]
views.get("lbllastupdate").vw.setTop((int)((views.get("gaugehumidity").vw.getTop() + views.get("gaugehumidity").vw.getHeight())+(20d * scale)));
//BA.debugLineNum = 27;BA.debugLine="End If"[1/General script]
;};
//BA.debugLineNum = 29;BA.debugLine="lblPing.Top = lblLastUpdate.Top"[1/General script]
views.get("lblping").vw.setTop((int)((views.get("lbllastupdate").vw.getTop())));
//BA.debugLineNum = 30;BA.debugLine="lblPing.Right = 100%x"[1/General script]
views.get("lblping").vw.setLeft((int)((100d / 100 * width) - (views.get("lblping").vw.getWidth())));

}
}