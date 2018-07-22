package cloyd.smart.home.monitor.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_temphumiditybasement{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("lblperceptionbasement").vw.setTop((int)((10d * scale)));
views.get("lblcomfortbasement").vw.setTop((int)((views.get("lblperceptionbasement").vw.getTop() + views.get("lblperceptionbasement").vw.getHeight())+(5d * scale)));
views.get("gaugeheatindexbasement").vw.setWidth((int)((200d * scale)));
views.get("gaugeheatindexbasement").vw.setTop((int)((views.get("lblcomfortbasement").vw.getTop() + views.get("lblcomfortbasement").vw.getHeight())+(20d * scale)));
views.get("gaugeheatindexbasement").vw.setLeft((int)((0d * scale)));
views.get("gaugeheatindexbasement").vw.setWidth((int)((50d / 100 * width) - ((0d * scale))));
views.get("gaugedewpointbasement").vw.setWidth((int)((200d * scale)));
views.get("gaugedewpointbasement").vw.setTop((int)((views.get("lblcomfortbasement").vw.getTop() + views.get("lblcomfortbasement").vw.getHeight())+(20d * scale)));
views.get("gaugedewpointbasement").vw.setLeft((int)((50d / 100 * width)));
views.get("gaugedewpointbasement").vw.setWidth((int)((100d / 100 * width) - ((50d / 100 * width))));
views.get("gaugetempbasement").vw.setWidth((int)((200d * scale)));
views.get("gaugetempbasement").vw.setTop((int)((views.get("gaugeheatindexbasement").vw.getTop() + views.get("gaugeheatindexbasement").vw.getHeight())+(35d * scale)));
views.get("gaugetempbasement").vw.setLeft((int)((0d * scale)));
views.get("gaugetempbasement").vw.setWidth((int)((50d / 100 * width) - ((0d * scale))));
views.get("gaugehumiditybasement").vw.setWidth((int)((200d * scale)));
views.get("gaugehumiditybasement").vw.setTop((int)((views.get("gaugeheatindexbasement").vw.getTop() + views.get("gaugeheatindexbasement").vw.getHeight())+(35d * scale)));
views.get("gaugehumiditybasement").vw.setLeft((int)((50d / 100 * width)));
views.get("gaugehumiditybasement").vw.setWidth((int)((100d / 100 * width) - ((50d / 100 * width))));
views.get("lbllastupdatebasement").vw.setTop((int)((views.get("gaugehumiditybasement").vw.getTop() + views.get("gaugehumiditybasement").vw.getHeight())+(50d * scale)));
if ((((100d / 100 * height))<((views.get("lbllastupdatebasement").vw.getTop() + views.get("lbllastupdatebasement").vw.getHeight())))) { 
;
views.get("lbllastupdatebasement").vw.setTop((int)((views.get("gaugehumiditybasement").vw.getTop() + views.get("gaugehumiditybasement").vw.getHeight())+(20d * scale)));
;};
views.get("lblpingbasement").vw.setTop((int)((views.get("lbllastupdatebasement").vw.getTop())));
//BA.debugLineNum = 30;BA.debugLine="lblPingBasement.Right = 100%x"[TempHumidityBasement/General script]
views.get("lblpingbasement").vw.setLeft((int)((100d / 100 * width) - (views.get("lblpingbasement").vw.getWidth())));

}
}