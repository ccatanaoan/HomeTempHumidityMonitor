package cloyd.home.smart.monitor.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_2{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("lblairquality").vw.setTop((int)((10d * scale)));
views.get("lblairqualitylastupdate").vw.setTop((int)((views.get("gaugeairquality").vw.getTop() + views.get("gaugeairquality").vw.getHeight())+(40d * scale)));

}
}