package cloyd.smart.home.monitor;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "cloyd.smart.home.monitor", "cloyd.smart.home.monitor.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "cloyd.smart.home.monitor", "cloyd.smart.home.monitor.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "cloyd.smart.home.monitor.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4j.objects.MqttAsyncClientWrapper _mqtt = null;
public static String _mqttuser = "";
public static String _mqttpassword = "";
public static String _mqttserveruri = "";
public static anywheresoftware.b4a.agraham.byteconverter.ByteConverter _bc = null;
public static b4a.example.callsubutils _csu = null;
public static anywheresoftware.b4a.objects.IntentWrapper _oldintent = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACMenuWrapper _gblacmenu = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public cloyd.smart.home.monitor.gauge _gaugehumidity = null;
public cloyd.smart.home.monitor.gauge _gaugetemp = null;
public cloyd.smart.home.monitor.gauge _gaugedewpoint = null;
public cloyd.smart.home.monitor.gauge _gaugeheatindex = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcomfort = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblperception = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllastupdate = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblping = null;
public anywheresoftware.b4a.objects.TabStripViewPager _tabstrip1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfontawesome = null;
public cloyd.smart.home.monitor.gauge _gaugeairquality = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblairquality = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblairqualitylastupdate = null;
public anywheresoftware.b4a.objects.WebViewWrapper _webview1 = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scrollview1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public anywheresoftware.b4a.object.XmlLayoutBuilder _xml = null;
public de.amberhome.objects.BottomNavigationViewWrapper _dsbottomnavigationview1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panelwebview = null;
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.objects.CSBuilder _cs = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.object.XmlLayoutBuilder _xl = null;
 //BA.debugLineNum = 55;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 56;BA.debugLine="Try";
try { //BA.debugLineNum = 57;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 58;BA.debugLine="StartService(SmartHomeMonitor)";
anywheresoftware.b4a.keywords.Common.StartService(processBA,(Object)(mostCurrent._smarthomemonitor.getObject()));
 //BA.debugLineNum = 59;BA.debugLine="csu.Initialize";
_csu._initialize(processBA);
 //BA.debugLineNum = 60;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 62;BA.debugLine="Activity.LoadLayout(\"Main\")";
mostCurrent._activity.LoadLayout("Main",mostCurrent.activityBA);
 //BA.debugLineNum = 64;BA.debugLine="TabStrip1.LoadLayout(\"1ScrollView\", \"Temp & Humi";
mostCurrent._tabstrip1.LoadLayout("1ScrollView",BA.ObjectToCharSequence("Temp & Humidity  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf2c7)))));
 //BA.debugLineNum = 65;BA.debugLine="ScrollView1.Panel.LoadLayout(\"1\")";
mostCurrent._scrollview1.getPanel().LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 66;BA.debugLine="Panel1.Height = Panel1.Height + 100dip";
mostCurrent._panel1.setHeight((int) (mostCurrent._panel1.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100))));
 //BA.debugLineNum = 67;BA.debugLine="ScrollView1.Panel.Height = Panel1.Height";
mostCurrent._scrollview1.getPanel().setHeight(mostCurrent._panel1.getHeight());
 //BA.debugLineNum = 68;BA.debugLine="TabStrip1.LoadLayout(\"2\", \"Air Quality (CO)  \" &";
mostCurrent._tabstrip1.LoadLayout("2",BA.ObjectToCharSequence("Air Quality (CO)  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf289)))));
 //BA.debugLineNum = 69;BA.debugLine="TabStrip1.LoadLayout(\"3\", \"Security Camera  \" &";
mostCurrent._tabstrip1.LoadLayout("3",BA.ObjectToCharSequence("Security Camera  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf030)))));
 //BA.debugLineNum = 71;BA.debugLine="For Each lbl As Label In GetAllTabLabels(TabStri";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
{
final anywheresoftware.b4a.BA.IterableList group14 = _getalltablabels(mostCurrent._tabstrip1);
final int groupLen14 = group14.getSize()
;int index14 = 0;
;
for (; index14 < groupLen14;index14++){
_lbl.setObject((android.widget.TextView)(group14.Get(index14)));
 //BA.debugLineNum = 73;BA.debugLine="lbl.Typeface = Typeface.FONTAWESOME";
_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME());
 //BA.debugLineNum = 75;BA.debugLine="lbl.Padding = Array As Int(0, 0, 0, 0)";
_lbl.setPadding(new int[]{(int) (0),(int) (0),(int) (0),(int) (0)});
 }
};
 //BA.debugLineNum = 78;BA.debugLine="For Each v As View In GetAllTabLabels(TabStrip1)";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group18 = _getalltablabels(mostCurrent._tabstrip1);
final int groupLen18 = group18.getSize()
;int index18 = 0;
;
for (; index18 < groupLen18;index18++){
_v.setObject((android.view.View)(group18.Get(index18)));
 //BA.debugLineNum = 80;BA.debugLine="v.Width = 33%x";
_v.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA));
 }
};
 //BA.debugLineNum = 83;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 84;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 85;BA.debugLine="ACToolBarLight1.NavigationIconDrawable = bd";
mostCurrent._actoolbarlight1.setNavigationIconDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 86;BA.debugLine="ToolbarHelper.Initialize";
mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 87;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 88;BA.debugLine="ToolbarHelper.Title= cs.Initialize.Size(22).Appe";
mostCurrent._toolbarhelper.setTitle(BA.ObjectToCharSequence(_cs.Initialize().Size((int) (22)).Append(BA.ObjectToCharSequence("Smart Home Monitor")).PopAll().getObject()));
 //BA.debugLineNum = 89;BA.debugLine="ToolbarHelper.subTitle = \"\"";
mostCurrent._toolbarhelper.setSubtitle(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 90;BA.debugLine="ToolbarHelper.ShowUpIndicator = False 'set to tr";
mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 91;BA.debugLine="ACToolBarLight1.InitMenuListener";
mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 92;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 93;BA.debugLine="Dim xl As XmlLayoutBuilder";
_xl = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 94;BA.debugLine="jo.RunMethod(\"setPopupTheme\", Array(xl.GetResour";
_jo.RunMethod("setPopupTheme",new Object[]{(Object)(_xl.GetResourceId("style","ToolbarMenu"))});
 //BA.debugLineNum = 96;BA.debugLine="GaugeHumidity.SetRanges(Array(GaugeHumidity.Crea";
mostCurrent._gaugehumidity._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugehumidity._createrange((float) (0),(float) (70),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugehumidity._createrange((float) (70),(float) (80),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugehumidity._createrange((float) (80),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 99;BA.debugLine="GaugeTemp.SetRanges(Array(GaugeTemp.CreateRange(";
mostCurrent._gaugetemp._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugetemp._createrange((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugetemp._createrange((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugetemp._createrange((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 102;BA.debugLine="GaugeHeatIndex.SetRanges(Array(GaugeHeatIndex.Cr";
mostCurrent._gaugeheatindex._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeheatindex._createrange((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeheatindex._createrange((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeheatindex._createrange((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 107;BA.debugLine="GaugeDewPoint.SetRanges(Array(GaugeDewPoint.Crea";
mostCurrent._gaugedewpoint._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugedewpoint._createrange((float) (0),(float) (60.8),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugedewpoint._createrange((float) (60.8),(float) (64.4),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugedewpoint._createrange((float) (64.4),(float) (78.8),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugedewpoint._createrange((float) (78.8),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 113;BA.debugLine="GaugeAirQuality.SetRanges(Array(GaugeTemp.Create";
mostCurrent._gaugeairquality._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugetemp._createrange((float) (0),(float) (100),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeairquality._createrange((float) (100),(float) (400),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugeairquality._createrange((float) (400),(float) (900),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeairquality._createrange((float) (900),(float) (1000),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 117;BA.debugLine="GaugeAirQuality.CurrentValue=0";
mostCurrent._gaugeairquality._setcurrentvalue((float) (0));
 //BA.debugLineNum = 119;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 120;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 121;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 122;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"The";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 123;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 124;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 125;BA.debugLine="lblPing.Visible = False";
mostCurrent._lblping.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 126;BA.debugLine="GaugeAirQuality.CurrentValue = 0";
mostCurrent._gaugeairquality._setcurrentvalue((float) (0));
 //BA.debugLineNum = 127;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 128;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.Bol";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).PopAll().getObject()));
 //BA.debugLineNum = 131;BA.debugLine="DSBottomNavigationView1.Menu.Add2(1, 1, \"Main Do";
mostCurrent._dsbottomnavigationview1.getMenu().Add2((int) (1),(int) (1),BA.ObjectToCharSequence("Main Door"),mostCurrent._xml.GetDrawable("ic_home_black_24dp")).setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 132;BA.debugLine="DSBottomNavigationView1.Menu.Add2(2, 2, \"Kitchen";
mostCurrent._dsbottomnavigationview1.getMenu().Add2((int) (2),(int) (2),BA.ObjectToCharSequence("Kitchen"),mostCurrent._xml.GetDrawable("ic_android_black_24dp"));
 //BA.debugLineNum = 133;BA.debugLine="DSBottomNavigationView1.SetItemIconColors(Colors";
mostCurrent._dsbottomnavigationview1.SetItemIconColors(anywheresoftware.b4a.keywords.Common.Colors.LightGray,anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (152),(int) (253)),anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 135;BA.debugLine="WebView1.Initialize(\"WebView1\")";
mostCurrent._webview1.Initialize(mostCurrent.activityBA,"WebView1");
 //BA.debugLineNum = 137;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 138;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Rea";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Read weather","utf8"));
 };
 } 
       catch (Exception e57) {
			processBA.setLastException(e57); //BA.debugLineNum = 141;BA.debugLine="ToastMessageShow(LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
 //BA.debugLineNum = 472;BA.debugLine="Sub Activity_Createmenu(Menu As ACMenu)";
 //BA.debugLineNum = 473;BA.debugLine="Try";
try { //BA.debugLineNum = 474;BA.debugLine="Menu.Clear";
_menu.Clear();
 //BA.debugLineNum = 475;BA.debugLine="gblACMenu = Menu";
mostCurrent._gblacmenu = _menu;
 //BA.debugLineNum = 476;BA.debugLine="Menu.Add(0, 0, \"Enable Notification Listener\",Nu";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Enable Notification Listener"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 477;BA.debugLine="Menu.Add(0, 0, \"Restart board\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Restart board"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 478;BA.debugLine="Menu.Add(0, 0, \"About\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 480;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 482;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 538;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 539;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 540;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (mostCurrent._tabstrip1.getCurrentPage()==2) { 
 //BA.debugLineNum = 541;BA.debugLine="If WebView1.Url.Contains(\"cloyd.mynetgear.com:8";
if (mostCurrent._webview1.getUrl().contains("cloyd.mynetgear.com:81")) { 
 //BA.debugLineNum = 542;BA.debugLine="TabStrip1.ScrollTo(1,True)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 543;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 //BA.debugLineNum = 544;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 546;BA.debugLine="If WebView1.IsInitialized = False Then";
if (mostCurrent._webview1.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 547;BA.debugLine="WebView1.Initialize(\"WebView1\")";
mostCurrent._webview1.Initialize(mostCurrent.activityBA,"WebView1");
 };
 //BA.debugLineNum = 549;BA.debugLine="DSBottomNavigationView1.CheckedItem=1";
mostCurrent._dsbottomnavigationview1.setCheckedItem((int) (1));
 //BA.debugLineNum = 550;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 }else if(mostCurrent._tabstrip1.getCurrentPage()==1) { 
 //BA.debugLineNum = 554;BA.debugLine="TabStrip1.ScrollTo(0,True)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 555;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 //BA.debugLineNum = 556;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 560;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 179;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 180;BA.debugLine="If UserClosed = False Then";
if (_userclosed==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 181;BA.debugLine="WebView1.StopLoading";
mostCurrent._webview1.StopLoading();
 //BA.debugLineNum = 182;BA.debugLine="WebView1.RemoveView";
mostCurrent._webview1.RemoveView();
 };
 //BA.debugLineNum = 184;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _in = null;
String _notificationclicked = "";
 //BA.debugLineNum = 145;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 146;BA.debugLine="Try";
try { //BA.debugLineNum = 147;BA.debugLine="Dim in As Intent = Activity.GetStartingIntent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
_in = mostCurrent._activity.GetStartingIntent();
 //BA.debugLineNum = 148;BA.debugLine="Dim NotificationClicked As String";
_notificationclicked = "";
 //BA.debugLineNum = 149;BA.debugLine="If in.IsInitialized And in <> OldIntent Then";
if (_in.IsInitialized() && (_in).equals(_oldintent) == false) { 
 //BA.debugLineNum = 150;BA.debugLine="OldIntent = in";
_oldintent = _in;
 //BA.debugLineNum = 151;BA.debugLine="If in.HasExtra(\"Notification_Tag\") Then";
if (_in.HasExtra("Notification_Tag")) { 
 //BA.debugLineNum = 152;BA.debugLine="NotificationClicked = in.GetExtra(\"Notificatio";
_notificationclicked = BA.ObjectToString(_in.GetExtra("Notification_Tag"));
 };
 };
 //BA.debugLineNum = 155;BA.debugLine="If NotificationClicked = \"Temperature\" Then";
if ((_notificationclicked).equals("Temperature")) { 
 //BA.debugLineNum = 156;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 157;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 }else if((_notificationclicked).equals("Carbon Monoxide")) { 
 //BA.debugLineNum = 159;BA.debugLine="TabStrip1.ScrollTo(1,False)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 160;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 }else {
 //BA.debugLineNum = 162;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 163;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 };
 } 
       catch (Exception e21) {
			processBA.setLastException(e21); //BA.debugLineNum = 167;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 170;BA.debugLine="Try";
try { //BA.debugLineNum = 171;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connected";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 172;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 } 
       catch (Exception e28) {
			processBA.setLastException(e28); //BA.debugLineNum = 175;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
return "";
}
public static void  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
ResumableSub_ACToolBarLight1_MenuItemClick rsub = new ResumableSub_ACToolBarLight1_MenuItemClick(null,_item);
rsub.resume(processBA, null);
}
public static class ResumableSub_ACToolBarLight1_MenuItemClick extends BA.ResumableSub {
public ResumableSub_ACToolBarLight1_MenuItemClick(cloyd.smart.home.monitor.main parent,de.amberhome.objects.appcompat.ACMenuItemWrapper _item) {
this.parent = parent;
this._item = _item;
}
cloyd.smart.home.monitor.main parent;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item;
anywheresoftware.b4a.objects.IntentWrapper _in = null;
int _result = 0;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.samples.httputils2.httpjob _j = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
try {

        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 375;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 66;
this.catchState = 65;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 65;
 //BA.debugLineNum = 376;BA.debugLine="If Item.Title = \"About\" Then";
if (true) break;

case 4:
//if
this.state = 63;
if ((_item.getTitle()).equals("About")) { 
this.state = 6;
}else if((_item.getTitle()).equals("Enable Notification Listener")) { 
this.state = 8;
}else if((_item.getTitle()).equals("Restart board")) { 
this.state = 10;
}if (true) break;

case 6:
//C
this.state = 63;
 //BA.debugLineNum = 377;BA.debugLine="ShowAboutMenu";
_showaboutmenu();
 if (true) break;

case 8:
//C
this.state = 63;
 //BA.debugLineNum = 379;BA.debugLine="Dim In As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 380;BA.debugLine="In.Initialize(\"android.settings.ACTION_NOTIFICA";
_in.Initialize("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS","");
 //BA.debugLineNum = 381;BA.debugLine="StartActivity(In)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_in.getObject()));
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 383;BA.debugLine="Try";
if (true) break;

case 11:
//try
this.state = 62;
this.catchState = 61;
this.state = 13;
if (true) break;

case 13:
//C
this.state = 14;
this.catchState = 61;
 //BA.debugLineNum = 384;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 385;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 386;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets,";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 387;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (true) break;

case 14:
//if
this.state = 59;
if (parent.mostCurrent._tabstrip1.getCurrentPage()==2) { 
this.state = 16;
}else if(parent.mostCurrent._tabstrip1.getCurrentPage()==1) { 
this.state = 40;
}else {
this.state = 50;
}if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 388;BA.debugLine="If WebView1.Url.Contains(\"cloyd.mynetgear.com";
if (true) break;

case 17:
//if
this.state = 38;
if (parent.mostCurrent._webview1.getUrl().contains("cloyd.mynetgear.com:81")) { 
this.state = 19;
}else {
this.state = 29;
}if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 389;BA.debugLine="result = Msgbox2(\"Restart the main door CAME";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the main door CAMERA controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 390;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (true) break;

case 20:
//if
this.state = 27;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 22;
}if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 391;BA.debugLine="WebView1.StopLoading";
parent.mostCurrent._webview1.StopLoading();
 //BA.debugLineNum = 392;BA.debugLine="WebView1.RemoveView";
parent.mostCurrent._webview1.RemoveView();
 //BA.debugLineNum = 393;BA.debugLine="Dim j As HttpJob";
_j = new anywheresoftware.b4a.samples.httputils2.httpjob();
 //BA.debugLineNum = 394;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize(processBA,"",main.getObject());
 //BA.debugLineNum = 395;BA.debugLine="j.PostString(\"http://cloyd.mynetgear.com:81";
_j._poststring("http://cloyd.mynetgear.com:81/restart","");
 //BA.debugLineNum = 396;BA.debugLine="j.GetRequest.Timeout = 1500";
_j._getrequest().setTimeout((int) (1500));
 //BA.debugLineNum = 397;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 67;
return;
case 67:
//C
this.state = 23;
_j = (anywheresoftware.b4a.samples.httputils2.httpjob) result[0];
;
 //BA.debugLineNum = 398;BA.debugLine="If j.Success Then";
if (true) break;

case 23:
//if
this.state = 26;
if (_j._success) { 
this.state = 25;
}if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 399;BA.debugLine="Log(j.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_j._getstring());
 if (true) break;

case 26:
//C
this.state = 27;
;
 //BA.debugLineNum = 401;BA.debugLine="j.Release";
_j._release();
 //BA.debugLineNum = 402;BA.debugLine="WebView1.Initialize(\"WebView1\")";
parent.mostCurrent._webview1.Initialize(mostCurrent.activityBA,"WebView1");
 //BA.debugLineNum = 403;BA.debugLine="DSBottomNavigationView1.CheckedItem=2";
parent.mostCurrent._dsbottomnavigationview1.setCheckedItem((int) (2));
 if (true) break;

case 27:
//C
this.state = 38;
;
 if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 406;BA.debugLine="result = Msgbox2(\"Restart the kitchen CAMERA";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the kitchen CAMERA controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 407;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (true) break;

case 30:
//if
this.state = 37;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 32;
}if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 408;BA.debugLine="WebView1.StopLoading";
parent.mostCurrent._webview1.StopLoading();
 //BA.debugLineNum = 409;BA.debugLine="WebView1.RemoveView";
parent.mostCurrent._webview1.RemoveView();
 //BA.debugLineNum = 410;BA.debugLine="Dim j As HttpJob";
_j = new anywheresoftware.b4a.samples.httputils2.httpjob();
 //BA.debugLineNum = 411;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize(processBA,"",main.getObject());
 //BA.debugLineNum = 412;BA.debugLine="j.PostString(\"http://cloyd.mynetgear.com/re";
_j._poststring("http://cloyd.mynetgear.com/restart","");
 //BA.debugLineNum = 413;BA.debugLine="j.GetRequest.Timeout = 1500";
_j._getrequest().setTimeout((int) (1500));
 //BA.debugLineNum = 414;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 68;
return;
case 68:
//C
this.state = 33;
_j = (anywheresoftware.b4a.samples.httputils2.httpjob) result[0];
;
 //BA.debugLineNum = 415;BA.debugLine="If j.Success Then";
if (true) break;

case 33:
//if
this.state = 36;
if (_j._success) { 
this.state = 35;
}if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 416;BA.debugLine="Log(j.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_j._getstring());
 if (true) break;

case 36:
//C
this.state = 37;
;
 //BA.debugLineNum = 418;BA.debugLine="j.Release";
_j._release();
 //BA.debugLineNum = 419;BA.debugLine="WebView1.Initialize(\"WebView1\")";
parent.mostCurrent._webview1.Initialize(mostCurrent.activityBA,"WebView1");
 //BA.debugLineNum = 420;BA.debugLine="DSBottomNavigationView1.CheckedItem=1";
parent.mostCurrent._dsbottomnavigationview1.setCheckedItem((int) (1));
 if (true) break;

case 37:
//C
this.state = 38;
;
 if (true) break;

case 38:
//C
this.state = 59;
;
 if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 424;BA.debugLine="result = Msgbox2(\"Restart the AIR QUALITY con";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 425;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (true) break;

case 41:
//if
this.state = 48;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 43;
}if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 426;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (true) break;

case 44:
//if
this.state = 47;
if (parent._mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || parent._mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 46;
}if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 427;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 47:
//C
this.state = 48;
;
 //BA.debugLineNum = 429;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Restar";
parent._mqtt.Publish("MQ7",parent._bc.StringToBytes("Restart controller","utf8"));
 if (true) break;

case 48:
//C
this.state = 59;
;
 if (true) break;

case 50:
//C
this.state = 51;
 //BA.debugLineNum = 432;BA.debugLine="result = Msgbox2(\"Restart the WEATHER control";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the WEATHER controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 433;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (true) break;

case 51:
//if
this.state = 58;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 53;
}if (true) break;

case 53:
//C
this.state = 54;
 //BA.debugLineNum = 434;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (true) break;

case 54:
//if
this.state = 57;
if (parent._mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || parent._mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 56;
}if (true) break;

case 56:
//C
this.state = 57;
 //BA.debugLineNum = 435;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 57:
//C
this.state = 58;
;
 //BA.debugLineNum = 437;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"";
parent._mqtt.Publish("TempHumid",parent._bc.StringToBytes("Restart controller","utf8"));
 if (true) break;

case 58:
//C
this.state = 59;
;
 if (true) break;

case 59:
//C
this.state = 62;
;
 if (true) break;

case 61:
//C
this.state = 62;
this.catchState = 65;
 //BA.debugLineNum = 441;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 if (true) break;
if (true) break;

case 62:
//C
this.state = 63;
this.catchState = 65;
;
 if (true) break;

case 63:
//C
this.state = 66;
;
 if (true) break;

case 65:
//C
this.state = 66;
this.catchState = 0;
 //BA.debugLineNum = 445;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 if (true) break;
if (true) break;

case 66:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 447;BA.debugLine="End Sub";
if (true) break;
}} 
       catch (Exception e0) {
			
if (catchState == 0)
    throw e0;
else {
    state = catchState;
processBA.setLastException(e0);}
            }
        }
    }
}
public static void  _jobdone(anywheresoftware.b4a.samples.httputils2.httpjob _j) throws Exception{
}
public static String  _checkairqualitysetting() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String[] _a = null;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 579;BA.debugLine="Sub CheckAirQualitySetting";
 //BA.debugLineNum = 580;BA.debugLine="Try";
try { //BA.debugLineNum = 581;BA.debugLine="If lblAirQuality.Text.Contains(\"Waiting for data";
if (mostCurrent._lblairquality.getText().contains("Waiting for data...")) { 
 //BA.debugLineNum = 582;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 583;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 584;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",StateManag";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",mostCurrent._statemanager._getsetting(mostCurrent.activityBA,"AirQuality"));
 //BA.debugLineNum = 585;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 586;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 587;BA.debugLine="GaugeAirQuality.CurrentValue = a(0)";
mostCurrent._gaugeairquality._setcurrentvalue((float)(Double.parseDouble(_a[(int) (0)])));
 //BA.debugLineNum = 588;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 //BA.debugLineNum = 589;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 590;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 591;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 592;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 593;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lng";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 594;BA.debugLine="Log(\"AirQuality Time difference: \" & p.Minute";
anywheresoftware.b4a.keywords.Common.Log("AirQuality Time difference: "+BA.NumberToString(_p.Minutes)+" minutes"+BA.NumberToString(_p.Seconds)+" seconds");
 //BA.debugLineNum = 595;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 596;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 598;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 601;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 };
 } 
       catch (Exception e27) {
			processBA.setLastException(e27); //BA.debugLineNum = 606;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 608;BA.debugLine="End Sub";
return "";
}
public static String  _checktemphumiditysetting() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String[] _a = null;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 610;BA.debugLine="Sub CheckTempHumiditySetting";
 //BA.debugLineNum = 611;BA.debugLine="Try";
try { //BA.debugLineNum = 612;BA.debugLine="If lblPerception.Text.Contains(\"Waiting for data";
if (mostCurrent._lblperception.getText().contains("Waiting for data...")) { 
 //BA.debugLineNum = 613;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 614;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 615;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",StateManag";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",mostCurrent._statemanager._getsetting(mostCurrent.activityBA,"TempHumidity"));
 //BA.debugLineNum = 616;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 617;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 618;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 619;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 620;BA.debugLine="GaugeTemp.CurrentValue = a(1)";
mostCurrent._gaugetemp._setcurrentvalue((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 621;BA.debugLine="GaugeHumidity.CurrentValue = a(2)";
mostCurrent._gaugehumidity._setcurrentvalue((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 622;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 623;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 //BA.debugLineNum = 624;BA.debugLine="GaugeHeatIndex.CurrentValue = a(5)";
mostCurrent._gaugeheatindex._setcurrentvalue((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 625;BA.debugLine="GaugeDewPoint.CurrentValue = a(6)";
mostCurrent._gaugedewpoint._setcurrentvalue((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 626;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 627;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 628;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 629;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 630;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lng";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 631;BA.debugLine="Log(\"TempHumidity Time difference: \" & p.Minu";
anywheresoftware.b4a.keywords.Common.Log("TempHumidity Time difference: "+BA.NumberToString(_p.Minutes)+" minutes");
 //BA.debugLineNum = 632;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 633;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appe";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 635;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appe";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 638;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appen";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 };
 } 
       catch (Exception e33) {
			processBA.setLastException(e33); //BA.debugLineNum = 643;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 645;BA.debugLine="End Sub";
return "";
}
public static String  _dsbottomnavigationview1_navigationitemselected(de.amberhome.objects.appcompat.ACMenuItemWrapper _menuitem) throws Exception{
 //BA.debugLineNum = 647;BA.debugLine="Sub DSBottomNavigationView1_NavigationItemSelected";
 //BA.debugLineNum = 648;BA.debugLine="WebView1.StopLoading";
mostCurrent._webview1.StopLoading();
 //BA.debugLineNum = 649;BA.debugLine="WebView1.RemoveView";
mostCurrent._webview1.RemoveView();
 //BA.debugLineNum = 650;BA.debugLine="WebView1.JavaScriptEnabled = True";
mostCurrent._webview1.setJavaScriptEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 651;BA.debugLine="Activity.addview(WebView1,0dip,108dip,100%x,Panel";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._webview1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (108)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),mostCurrent._panelwebview.getHeight());
 //BA.debugLineNum = 652;BA.debugLine="Select MenuItem.Id";
switch (BA.switchObjectToInt(_menuitem.getId(),(int) (1),(int) (2))) {
case 0: {
 //BA.debugLineNum = 654;BA.debugLine="WebView1.LoadHtml($\"<div> 			<table width=\"100%";
mostCurrent._webview1.LoadHtml(("<div>\n"+"			<table width=\"100%\" height=\"100%\" align=\"center\" valign=\"center\">\n"+"			<tr><td align=\"middle\">\n"+"					Waiting for the main door camera feed...\n"+"			</td></tr>\n"+"			</table>\n"+"			</div>"));
 //BA.debugLineNum = 661;BA.debugLine="WebView1.LoadURL(\"http://cloyd.mynetgear.com:81";
mostCurrent._webview1.LoadUrl("http://cloyd.mynetgear.com:81/stream");
 break; }
case 1: {
 //BA.debugLineNum = 663;BA.debugLine="WebView1.LoadHtml($\"<div> 			<table width=\"100%";
mostCurrent._webview1.LoadHtml(("<div>\n"+"			<table width=\"100%\" height=\"100%\" align=\"center\" valign=\"center\">\n"+"			<tr><td align=\"middle\">\n"+"					Waiting for the kitchen camera feed...\n"+"			</td></tr>\n"+"			</table>\n"+"			</div>"));
 //BA.debugLineNum = 670;BA.debugLine="WebView1.LoadURL(\"http://cloyd.mynetgear.com/st";
mostCurrent._webview1.LoadUrl("http://cloyd.mynetgear.com/stream");
 break; }
}
;
 //BA.debugLineNum = 672;BA.debugLine="End Sub";
return "";
}
public static String  _getairquality(int _number) throws Exception{
 //BA.debugLineNum = 298;BA.debugLine="Sub GetAirQuality(number As Int) As String";
 //BA.debugLineNum = 301;BA.debugLine="If number <= 100 Then";
if (_number<=100) { 
 //BA.debugLineNum = 302;BA.debugLine="Return(\"Carbon monoxide perfect\")";
if (true) return ("Carbon monoxide perfect");
 }else if(((_number>100) && (_number<400)) || _number==400) { 
 //BA.debugLineNum = 304;BA.debugLine="Return(\"Carbon monoxide normal\")";
if (true) return ("Carbon monoxide normal");
 }else if(((_number>400) && (_number<900)) || _number==900) { 
 //BA.debugLineNum = 306;BA.debugLine="Return(\"Carbon monoxide high\")";
if (true) return ("Carbon monoxide high");
 }else if(_number>900) { 
 //BA.debugLineNum = 308;BA.debugLine="Return(\"ALARM Carbon monoxide very high\")";
if (true) return ("ALARM Carbon monoxide very high");
 }else {
 //BA.debugLineNum = 310;BA.debugLine="Return(\"MQ-7 - cant read any value - check the s";
if (true) return ("MQ-7 - cant read any value - check the sensor!");
 };
 //BA.debugLineNum = 312;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _getalltablabels(anywheresoftware.b4a.objects.TabStripViewPager _tabstrip) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
anywheresoftware.b4a.objects.PanelWrapper _tc = null;
anywheresoftware.b4a.objects.collections.List _res = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
 //BA.debugLineNum = 525;BA.debugLine="Public Sub GetAllTabLabels (tabstrip As TabStrip)";
 //BA.debugLineNum = 526;BA.debugLine="Dim jo As JavaObject = tabstrip";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_tabstrip));
 //BA.debugLineNum = 527;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 528;BA.debugLine="r.Target = jo.GetField(\"tabStrip\")";
_r.Target = _jo.GetField("tabStrip");
 //BA.debugLineNum = 529;BA.debugLine="Dim tc As Panel = r.GetField(\"tabsContainer\")";
_tc = new anywheresoftware.b4a.objects.PanelWrapper();
_tc.setObject((android.view.ViewGroup)(_r.GetField("tabsContainer")));
 //BA.debugLineNum = 530;BA.debugLine="Dim res As List";
_res = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 531;BA.debugLine="res.Initialize";
_res.Initialize();
 //BA.debugLineNum = 532;BA.debugLine="For Each v As View In tc";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group7 = _tc;
final int groupLen7 = group7.getSize()
;int index7 = 0;
;
for (; index7 < groupLen7;index7++){
_v.setObject((android.view.View)(group7.Get(index7)));
 //BA.debugLineNum = 533;BA.debugLine="If v Is Label Then res.Add(v)";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
_res.Add((Object)(_v.getObject()));};
 }
};
 //BA.debugLineNum = 535;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 536;BA.debugLine="End Sub";
return null;
}
public static String  _getcomfort(String _dht11comfortstatus) throws Exception{
String _localcomfortstatus = "";
 //BA.debugLineNum = 347;BA.debugLine="Sub GetComfort(DHT11ComfortStatus As String) As St";
 //BA.debugLineNum = 348;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = "";
 //BA.debugLineNum = 349;BA.debugLine="Select Case DHT11ComfortStatus";
switch (BA.switchObjectToInt(_dht11comfortstatus,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(8),BA.NumberToString(9),BA.NumberToString(10))) {
case 0: {
 //BA.debugLineNum = 351;BA.debugLine="localcomfortstatus = \"OK\"";
_localcomfortstatus = "OK";
 break; }
case 1: {
 //BA.debugLineNum = 353;BA.debugLine="localcomfortstatus = \"Too hot\"";
_localcomfortstatus = "Too hot";
 break; }
case 2: {
 //BA.debugLineNum = 355;BA.debugLine="localcomfortstatus = \"Too cold\"";
_localcomfortstatus = "Too cold";
 break; }
case 3: {
 //BA.debugLineNum = 357;BA.debugLine="localcomfortstatus = \"Too dry\"";
_localcomfortstatus = "Too dry";
 break; }
case 4: {
 //BA.debugLineNum = 359;BA.debugLine="localcomfortstatus = \"Hot and dry\"";
_localcomfortstatus = "Hot and dry";
 break; }
case 5: {
 //BA.debugLineNum = 361;BA.debugLine="localcomfortstatus = \"Cold and dry\"";
_localcomfortstatus = "Cold and dry";
 break; }
case 6: {
 //BA.debugLineNum = 363;BA.debugLine="localcomfortstatus = \"Too humid\"";
_localcomfortstatus = "Too humid";
 break; }
case 7: {
 //BA.debugLineNum = 365;BA.debugLine="localcomfortstatus = \"Hot and humid\"";
_localcomfortstatus = "Hot and humid";
 break; }
case 8: {
 //BA.debugLineNum = 367;BA.debugLine="localcomfortstatus = \"Cold and humid\"";
_localcomfortstatus = "Cold and humid";
 break; }
default: {
 //BA.debugLineNum = 369;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = "Unknown";
 break; }
}
;
 //BA.debugLineNum = 371;BA.debugLine="Return localcomfortstatus";
if (true) return _localcomfortstatus;
 //BA.debugLineNum = 372;BA.debugLine="End Sub";
return "";
}
public static String  _getperception(String _dht11perception) throws Exception{
String _localperception = "";
 //BA.debugLineNum = 314;BA.debugLine="Sub GetPerception(DHT11Perception As String) As St";
 //BA.debugLineNum = 325;BA.debugLine="Dim localperception As String";
_localperception = "";
 //BA.debugLineNum = 326;BA.debugLine="Select Case DHT11Perception";
switch (BA.switchObjectToInt(_dht11perception,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(7))) {
case 0: {
 //BA.debugLineNum = 328;BA.debugLine="localperception = \"Feels like the western US, a";
_localperception = "Feels like the western US, a bit dry to some";
 break; }
case 1: {
 //BA.debugLineNum = 330;BA.debugLine="localperception = \"Very comfortable\"";
_localperception = "Very comfortable";
 break; }
case 2: {
 //BA.debugLineNum = 332;BA.debugLine="localperception = \"Comfortable\"";
_localperception = "Comfortable";
 break; }
case 3: {
 //BA.debugLineNum = 334;BA.debugLine="localperception = \"OK but humidity is at upper";
_localperception = "OK but humidity is at upper limit";
 break; }
case 4: {
 //BA.debugLineNum = 336;BA.debugLine="localperception = \"Uncomfortable and the humidi";
_localperception = "Uncomfortable and the humidity is at upper limit";
 break; }
case 5: {
 //BA.debugLineNum = 338;BA.debugLine="localperception = \"Very humid, quite uncomforta";
_localperception = "Very humid, quite uncomfortable";
 break; }
case 6: {
 //BA.debugLineNum = 340;BA.debugLine="localperception = \"Extremely uncomfortable, opp";
_localperception = "Extremely uncomfortable, oppressive";
 break; }
case 7: {
 //BA.debugLineNum = 342;BA.debugLine="localperception = \"Severely high, even deadly f";
_localperception = "Severely high, even deadly for asthma related illnesses";
 break; }
}
;
 //BA.debugLineNum = 344;BA.debugLine="Return localperception";
if (true) return _localperception;
 //BA.debugLineNum = 345;BA.debugLine="End Sub";
return "";
}
public static String  _getversioncode() throws Exception{
String _appversion = "";
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
String _packagename = "";
 //BA.debugLineNum = 459;BA.debugLine="Sub GetVersionCode() As String";
 //BA.debugLineNum = 460;BA.debugLine="Dim AppVersion As String";
_appversion = "";
 //BA.debugLineNum = 461;BA.debugLine="Try";
try { //BA.debugLineNum = 462;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 463;BA.debugLine="Dim packageName As String";
_packagename = "";
 //BA.debugLineNum = 464;BA.debugLine="packageName =  Application.PackageName";
_packagename = anywheresoftware.b4a.keywords.Common.Application.getPackageName();
 //BA.debugLineNum = 465;BA.debugLine="AppVersion = pm.GetVersionName(packageName)";
_appversion = _pm.GetVersionName(_packagename);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 467;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 469;BA.debugLine="Return AppVersion";
if (true) return _appversion;
 //BA.debugLineNum = 470;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 29;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 30;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 32;BA.debugLine="Private gblACMenu As ACMenu";
mostCurrent._gblacmenu = new de.amberhome.objects.appcompat.ACMenuWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 34;BA.debugLine="Private GaugeHumidity As Gauge";
mostCurrent._gaugehumidity = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 35;BA.debugLine="Private GaugeTemp As Gauge";
mostCurrent._gaugetemp = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 36;BA.debugLine="Private GaugeDewPoint As Gauge";
mostCurrent._gaugedewpoint = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 37;BA.debugLine="Private GaugeHeatIndex As Gauge";
mostCurrent._gaugeheatindex = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 38;BA.debugLine="Private lblComfort As Label";
mostCurrent._lblcomfort = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private lblPerception As Label";
mostCurrent._lblperception = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private lblLastUpdate As Label";
mostCurrent._lbllastupdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private lblPing As Label";
mostCurrent._lblping = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private TabStrip1 As TabStrip";
mostCurrent._tabstrip1 = new anywheresoftware.b4a.objects.TabStripViewPager();
 //BA.debugLineNum = 43;BA.debugLine="Private lblFontAwesome As Label";
mostCurrent._lblfontawesome = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private GaugeAirQuality As Gauge";
mostCurrent._gaugeairquality = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 45;BA.debugLine="Private lblAirQuality As Label";
mostCurrent._lblairquality = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private lblAirQualityLastUpdate As Label";
mostCurrent._lblairqualitylastupdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private WebView1 As WebView 'ignore";
mostCurrent._webview1 = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private ScrollView1 As ScrollView";
mostCurrent._scrollview1 = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private xml As XmlLayoutBuilder";
mostCurrent._xml = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 51;BA.debugLine="Private DSBottomNavigationView1 As DSBottomNaviga";
mostCurrent._dsbottomnavigationview1 = new de.amberhome.objects.BottomNavigationViewWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private PanelWebView As Panel";
mostCurrent._panelwebview = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public static String  _hideping() throws Exception{
 //BA.debugLineNum = 484;BA.debugLine="Private Sub HidePing";
 //BA.debugLineNum = 485;BA.debugLine="lblPing.SetVisibleAnimated(200, False)";
mostCurrent._lblping.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 486;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connect() throws Exception{
String _clientid = "";
anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper _connopt = null;
 //BA.debugLineNum = 187;BA.debugLine="Sub MQTT_Connect";
 //BA.debugLineNum = 188;BA.debugLine="Try";
try { //BA.debugLineNum = 189;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'crea";
_clientid = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (999999999)));
 //BA.debugLineNum = 190;BA.debugLine="MQTT.Initialize(\"MQTT\", MQTTServerURI, Cli";
_mqtt.Initialize(processBA,"MQTT",_mqttserveruri,_clientid);
 //BA.debugLineNum = 192;BA.debugLine="Dim ConnOpt As MqttConnectOptions";
_connopt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper();
 //BA.debugLineNum = 193;BA.debugLine="ConnOpt.Initialize(MQTTUser, MQTTPassword)";
_connopt.Initialize(_mqttuser,_mqttpassword);
 //BA.debugLineNum = 194;BA.debugLine="MQTT.Connect2(ConnOpt)";
_mqtt.Connect2((org.eclipse.paho.client.mqttv3.MqttConnectOptions)(_connopt.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 196;BA.debugLine="Log(\"MQTT_Connect: \" & LastException)";
anywheresoftware.b4a.keywords.Common.Log("MQTT_Connect: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 198;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 200;BA.debugLine="Sub MQTT_Connected (Success As Boolean)";
 //BA.debugLineNum = 201;BA.debugLine="Try";
try { //BA.debugLineNum = 202;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 203;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 //BA.debugLineNum = 204;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 }else {
 //BA.debugLineNum = 206;BA.debugLine="Log(\"Connected to MQTT broker\")";
anywheresoftware.b4a.keywords.Common.Log("Connected to MQTT broker");
 //BA.debugLineNum = 207;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
_mqtt.Subscribe("TempHumid",(int) (0));
 //BA.debugLineNum = 208;BA.debugLine="MQTT.Subscribe(\"MQ7\", 0)";
_mqtt.Subscribe("MQ7",(int) (0));
 };
 } 
       catch (Exception e11) {
			processBA.setLastException(e11); //BA.debugLineNum = 211;BA.debugLine="Log(\"MQTT_Connected: \" & LastException)";
anywheresoftware.b4a.keywords.Common.Log("MQTT_Connected: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 213;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_disconnected() throws Exception{
 //BA.debugLineNum = 215;BA.debugLine="Private Sub MQTT_Disconnected";
 //BA.debugLineNum = 216;BA.debugLine="Try";
try { //BA.debugLineNum = 217;BA.debugLine="gblACMenu.Clear";
mostCurrent._gblacmenu.Clear();
 //BA.debugLineNum = 218;BA.debugLine="gblACMenu.Add(0, 0, \"Enable Notification Listene";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Enable Notification Listener"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 219;BA.debugLine="gblACMenu.Add(0, 0, \"Restart board\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Restart board"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 220;BA.debugLine="gblACMenu.Add(0, 0, \"About\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 221;BA.debugLine="Log(\"Disconnected from MQTT broker\")";
anywheresoftware.b4a.keywords.Common.Log("Disconnected from MQTT broker");
 //BA.debugLineNum = 222;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e9) {
			processBA.setLastException(e9); //BA.debugLineNum = 224;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 226;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
String _status = "";
String[] _a = null;
anywheresoftware.b4a.objects.CSBuilder _cs = null;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 228;BA.debugLine="Private Sub MQTT_MessageArrived (Topic As String,";
 //BA.debugLineNum = 229;BA.debugLine="Try";
try { //BA.debugLineNum = 230;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic).equals("TempHumid")) { 
 //BA.debugLineNum = 231;BA.debugLine="lblPing.SetVisibleAnimated(500, True)";
mostCurrent._lblping.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 232;BA.debugLine="csu.CallSubPlus(Me, \"HidePing\", 700)";
_csu._v7(main.getObject(),"HidePing",(int) (700));
 //BA.debugLineNum = 234;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 235;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 237;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 238;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 239;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 240;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 241;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 242;BA.debugLine="GaugeTemp.CurrentValue = a(1)";
mostCurrent._gaugetemp._setcurrentvalue((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 243;BA.debugLine="GaugeHumidity.CurrentValue = a(2)";
mostCurrent._gaugehumidity._setcurrentvalue((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 244;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 245;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 //BA.debugLineNum = 246;BA.debugLine="GaugeHeatIndex.CurrentValue = a(5)";
mostCurrent._gaugeheatindex._setcurrentvalue((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 247;BA.debugLine="GaugeDewPoint.CurrentValue = a(6)";
mostCurrent._gaugedewpoint._setcurrentvalue((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 248;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 250;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 251;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 252;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 253;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lng";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 254;BA.debugLine="Log(\"TempHumidity Time difference: \" & p.Minu";
anywheresoftware.b4a.keywords.Common.Log("TempHumidity Time difference: "+BA.NumberToString(_p.Minutes)+" minutes");
 //BA.debugLineNum = 255;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 256;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appe";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 258;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appe";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 261;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appen";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 //BA.debugLineNum = 264;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 }else if((_topic).equals("MQ7")) { 
 //BA.debugLineNum = 266;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 267;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 268;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 269;BA.debugLine="status = BytesToString(Payload, 0, Payload.Leng";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 270;BA.debugLine="Log(\"MQ7 status: \" & status)";
anywheresoftware.b4a.keywords.Common.Log("MQ7 status: "+_status);
 //BA.debugLineNum = 271;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 272;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 273;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 274;BA.debugLine="GaugeAirQuality.CurrentValue = a(0)";
mostCurrent._gaugeairquality._setcurrentvalue((float)(Double.parseDouble(_a[(int) (0)])));
 //BA.debugLineNum = 275;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 //BA.debugLineNum = 276;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 277;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 278;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 279;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 280;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lng";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 281;BA.debugLine="Log(\"AirQuality Time difference: \" & p.Minute";
anywheresoftware.b4a.keywords.Common.Log("AirQuality Time difference: "+BA.NumberToString(_p.Minutes)+" minutes"+BA.NumberToString(_p.Seconds)+" seconds");
 //BA.debugLineNum = 282;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 283;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 285;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 288;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 //BA.debugLineNum = 291;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 };
 } 
       catch (Exception e63) {
			processBA.setLastException(e63); //BA.debugLineNum = 294;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 296;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        anywheresoftware.b4a.samples.httputils2.httputils2service._process_globals();
b4a.example.dateutils._process_globals();
main._process_globals();
smarthomemonitor._process_globals();
notificationservice._process_globals();
statemanager._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 19;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 20;BA.debugLine="Private MQTT As MqttClient";
_mqtt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private MQTTUser As String = \"vynckfaq\"";
_mqttuser = "vynckfaq";
 //BA.debugLineNum = 22;BA.debugLine="Private MQTTPassword As String = \"KHSV1Q1qSUUY\"";
_mqttpassword = "KHSV1Q1qSUUY";
 //BA.debugLineNum = 23;BA.debugLine="Private MQTTServerURI As String = \"tcp://m14.clou";
_mqttserveruri = "tcp://m14.cloudmqtt.com:11816";
 //BA.debugLineNum = 24;BA.debugLine="Private bc As ByteConverter";
_bc = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 25;BA.debugLine="Private csu As CallSubUtils";
_csu = new b4a.example.callsubutils();
 //BA.debugLineNum = 26;BA.debugLine="Private OldIntent As Intent";
_oldintent = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static String  _showaboutmenu() throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 449;BA.debugLine="Sub ShowAboutMenu";
 //BA.debugLineNum = 450;BA.debugLine="Try";
try { //BA.debugLineNum = 451;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 452;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cloyd.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 453;BA.debugLine="Msgbox2(\"Smart Home Monitor v\" & GetVersionCode";
anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Smart Home Monitor v"+_getversioncode()+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Developed by Cloyd Nino Catanaoan"+anywheresoftware.b4a.keywords.Common.CRLF+"April 21, 2018"),BA.ObjectToCharSequence("About"),"OK","","",_bd.getBitmap(),mostCurrent.activityBA);
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 455;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 457;BA.debugLine="End Sub";
return "";
}
public static String  _tabstrip1_pageselected(int _position) throws Exception{
 //BA.debugLineNum = 500;BA.debugLine="Sub TabStrip1_PageSelected (Position As Int)";
 //BA.debugLineNum = 501;BA.debugLine="Try";
try { //BA.debugLineNum = 502;BA.debugLine="If Position = 2 Then";
if (_position==2) { 
 //BA.debugLineNum = 503;BA.debugLine="DSBottomNavigationView1.CheckedItem=1";
mostCurrent._dsbottomnavigationview1.setCheckedItem((int) (1));
 }else {
 //BA.debugLineNum = 505;BA.debugLine="WebView1.StopLoading";
mostCurrent._webview1.StopLoading();
 //BA.debugLineNum = 506;BA.debugLine="WebView1.RemoveView";
mostCurrent._webview1.RemoveView();
 };
 //BA.debugLineNum = 508;BA.debugLine="If Position = 0 Then";
if (_position==0) { 
 //BA.debugLineNum = 509;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 510;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Re";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Read weather","utf8"));
 };
 //BA.debugLineNum = 512;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 };
 //BA.debugLineNum = 514;BA.debugLine="If Position = 1 Then";
if (_position==1) { 
 //BA.debugLineNum = 515;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 516;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Read vol";
_mqtt.Publish("MQ7",_bc.StringToBytes("Read voltage","utf8"));
 };
 //BA.debugLineNum = 518;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 };
 } 
       catch (Exception e21) {
			processBA.setLastException(e21); //BA.debugLineNum = 521;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 523;BA.debugLine="End Sub";
return "";
}
public static String  _webview1_pagefinished(String _url) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _obj1 = null;
String _s = "";
float _f = 0f;
 //BA.debugLineNum = 562;BA.debugLine="Sub WebView1_PageFinished (Url As String)";
 //BA.debugLineNum = 563;BA.debugLine="Try";
try { //BA.debugLineNum = 564;BA.debugLine="Dim Obj1 As Reflector";
_obj1 = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 565;BA.debugLine="Dim s As String 'ignore";
_s = "";
 //BA.debugLineNum = 566;BA.debugLine="Dim f As Float 'ignore";
_f = 0f;
 //BA.debugLineNum = 568;BA.debugLine="Obj1.Target = WebView1";
_obj1.Target = (Object)(mostCurrent._webview1.getObject());
 //BA.debugLineNum = 569;BA.debugLine="s = Obj1.TypeName";
_s = _obj1.getTypeName();
 //BA.debugLineNum = 570;BA.debugLine="f = Obj1.RunMethod(\"getScale\")";
_f = (float)(BA.ObjectToNumber(_obj1.RunMethod("getScale")));
 //BA.debugLineNum = 572;BA.debugLine="Obj1.Target = WebView1";
_obj1.Target = (Object)(mostCurrent._webview1.getObject());
 //BA.debugLineNum = 573;BA.debugLine="Obj1.RunMethod2(\"setInitialScale\", \"230\", \"java.";
_obj1.RunMethod2("setInitialScale","230","java.lang.int");
 } 
       catch (Exception e11) {
			processBA.setLastException(e11); //BA.debugLineNum = 575;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 577;BA.debugLine="End Sub";
return "";
}

public boolean _onCreateOptionsMenu(android.view.Menu menu) {
    if (processBA.subExists("activity_createmenu")) {
        processBA.raiseEvent2(null, true, "activity_createmenu", false, new de.amberhome.objects.appcompat.ACMenuWrapper(menu));
        return true;
    }
    else
        return false;
}
}
