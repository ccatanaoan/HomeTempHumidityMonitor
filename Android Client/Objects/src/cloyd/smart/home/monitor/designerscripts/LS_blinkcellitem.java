package cloyd.smart.home.monitor.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_blinkcellitem{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
views.get("ivwatched").vw.setTop((int)(((100d * scale)-(views.get("ivwatched").vw.getHeight()))/2d));
views.get("ivplay").vw.setTop((int)((((100d * scale)-(views.get("ivplay").vw.getHeight()))/2d)-(4d * scale)));

}
}