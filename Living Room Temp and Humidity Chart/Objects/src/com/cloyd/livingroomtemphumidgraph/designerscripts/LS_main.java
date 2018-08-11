package com.cloyd.livingroomtemphumidgraph.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_main{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="AutoScaleAll"[main/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 5;BA.debugLine="lc1.Left = 2%x"[main/General script]
views.get("lc1").vw.setLeft((int)((2d / 100 * width)));
//BA.debugLineNum = 6;BA.debugLine="lc1.Top = 0%y"[main/General script]
views.get("lc1").vw.setTop((int)((0d / 100 * height)));
//BA.debugLineNum = 7;BA.debugLine="lc1.Width = 96%x"[main/General script]
views.get("lc1").vw.setWidth((int)((96d / 100 * width)));
//BA.debugLineNum = 8;BA.debugLine="lc1.Height = 100%y"[main/General script]
views.get("lc1").vw.setHeight((int)((100d / 100 * height)));

}
}