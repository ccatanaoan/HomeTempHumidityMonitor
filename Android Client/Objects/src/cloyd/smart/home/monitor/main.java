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
public static de.amberhome.objects.preferenceactivity.PreferenceManager _manager = null;
public static de.amberhome.objects.preferenceactivity.PreferenceScreenWrapper _screen = null;
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
public anywheresoftware.b4a.objects.ScrollViewWrapper _scrollview1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public cloyd.smart.home.monitor.gauge _gaugeairqualitybasement = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblairqualitybasement = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblairqualitylastupdatebasement = null;
public anywheresoftware.b4a.objects.PanelWrapper _panelairqualitybasement = null;
public cloyd.smart.home.monitor.gauge _gaugedewpointbasement = null;
public cloyd.smart.home.monitor.gauge _gaugeheatindexbasement = null;
public cloyd.smart.home.monitor.gauge _gaugehumiditybasement = null;
public cloyd.smart.home.monitor.gauge _gaugetempbasement = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcomfortbasement = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllastupdatebasement = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblperceptionbasement = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpingbasement = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scrollviewbasement = null;
public anywheresoftware.b4a.objects.PanelWrapper _paneltemphumiditybasement = null;
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
 //BA.debugLineNum = 67;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 68;BA.debugLine="Try";
try { //BA.debugLineNum = 69;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 70;BA.debugLine="CreatePreferenceScreen";
_createpreferencescreen();
 //BA.debugLineNum = 71;BA.debugLine="If manager.GetAll.Size = 0 Then SetDefaults";
if (_manager.GetAll().getSize()==0) { 
_setdefaults();};
 //BA.debugLineNum = 73;BA.debugLine="StartService(SmartHomeMonitor)";
anywheresoftware.b4a.keywords.Common.StartService(processBA,(Object)(mostCurrent._smarthomemonitor.getObject()));
 //BA.debugLineNum = 74;BA.debugLine="csu.Initialize";
_csu._initialize(processBA);
 //BA.debugLineNum = 75;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 77;BA.debugLine="Activity.LoadLayout(\"Main\")";
mostCurrent._activity.LoadLayout("Main",mostCurrent.activityBA);
 //BA.debugLineNum = 79;BA.debugLine="TabStrip1.LoadLayout(\"1ScrollView\", \"LIVING AREA";
mostCurrent._tabstrip1.LoadLayout("1ScrollView",BA.ObjectToCharSequence("LIVING AREA"+anywheresoftware.b4a.keywords.Common.CRLF+"Temp & Humidity  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf2c7)))));
 //BA.debugLineNum = 80;BA.debugLine="ScrollView1.Panel.LoadLayout(\"1\")";
mostCurrent._scrollview1.getPanel().LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 81;BA.debugLine="Panel1.Height = ScrollView1.Height 'Panel1.Heigh";
mostCurrent._panel1.setHeight(mostCurrent._scrollview1.getHeight());
 //BA.debugLineNum = 82;BA.debugLine="ScrollView1.Panel.Height = Panel1.Height";
mostCurrent._scrollview1.getPanel().setHeight(mostCurrent._panel1.getHeight());
 //BA.debugLineNum = 83;BA.debugLine="TabStrip1.LoadLayout(\"2\", \"LIVING AREA\" & CRLF &";
mostCurrent._tabstrip1.LoadLayout("2",BA.ObjectToCharSequence("LIVING AREA"+anywheresoftware.b4a.keywords.Common.CRLF+"Air Quality (CO)  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf289)))));
 //BA.debugLineNum = 84;BA.debugLine="TabStrip1.LoadLayout(\"ScrollViewBasement\", \"BASE";
mostCurrent._tabstrip1.LoadLayout("ScrollViewBasement",BA.ObjectToCharSequence("BASEMENT"+anywheresoftware.b4a.keywords.Common.CRLF+"Temp & Humidity  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf2c7)))));
 //BA.debugLineNum = 85;BA.debugLine="ScrollViewBasement.Panel.LoadLayout(\"TempHumidit";
mostCurrent._scrollviewbasement.getPanel().LoadLayout("TempHumidityBasement",mostCurrent.activityBA);
 //BA.debugLineNum = 86;BA.debugLine="PanelTempHumidityBasement.Height = ScrollViewBas";
mostCurrent._paneltemphumiditybasement.setHeight(mostCurrent._scrollviewbasement.getHeight());
 //BA.debugLineNum = 87;BA.debugLine="ScrollViewBasement.Panel.Height = PanelTempHumid";
mostCurrent._scrollviewbasement.getPanel().setHeight(mostCurrent._paneltemphumiditybasement.getHeight());
 //BA.debugLineNum = 88;BA.debugLine="TabStrip1.LoadLayout(\"AirQualityBasement\", \"BASE";
mostCurrent._tabstrip1.LoadLayout("AirQualityBasement",BA.ObjectToCharSequence("BASEMENT"+anywheresoftware.b4a.keywords.Common.CRLF+"Air Quality (CO)  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf289)))));
 //BA.debugLineNum = 90;BA.debugLine="For Each lbl As Label In GetAllTabLabels(TabStri";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
{
final anywheresoftware.b4a.BA.IterableList group20 = _getalltablabels(mostCurrent._tabstrip1);
final int groupLen20 = group20.getSize()
;int index20 = 0;
;
for (; index20 < groupLen20;index20++){
_lbl.setObject((android.widget.TextView)(group20.Get(index20)));
 //BA.debugLineNum = 92;BA.debugLine="lbl.SingleLine = False";
_lbl.setSingleLine(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 94;BA.debugLine="lbl.Typeface = Typeface.FONTAWESOME";
_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME());
 //BA.debugLineNum = 96;BA.debugLine="lbl.Padding = Array As Int(0, 0, 0, 0)";
_lbl.setPadding(new int[]{(int) (0),(int) (0),(int) (0),(int) (0)});
 }
};
 //BA.debugLineNum = 100;BA.debugLine="For Each v As View In GetAllTabLabels(TabStrip1)";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group25 = _getalltablabels(mostCurrent._tabstrip1);
final int groupLen25 = group25.getSize()
;int index25 = 0;
;
for (; index25 < groupLen25;index25++){
_v.setObject((android.view.View)(group25.Get(index25)));
 //BA.debugLineNum = 102;BA.debugLine="v.Width = 33%x";
_v.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA));
 }
};
 //BA.debugLineNum = 105;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 106;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 107;BA.debugLine="ACToolBarLight1.NavigationIconDrawable = bd";
mostCurrent._actoolbarlight1.setNavigationIconDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 108;BA.debugLine="ToolbarHelper.Initialize";
mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 109;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 110;BA.debugLine="ToolbarHelper.Title= cs.Initialize.Size(22).Appe";
mostCurrent._toolbarhelper.setTitle(BA.ObjectToCharSequence(_cs.Initialize().Size((int) (22)).Append(BA.ObjectToCharSequence("Smart Home Monitor")).PopAll().getObject()));
 //BA.debugLineNum = 111;BA.debugLine="ToolbarHelper.subTitle = \"\"";
mostCurrent._toolbarhelper.setSubtitle(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 112;BA.debugLine="ToolbarHelper.ShowUpIndicator = False 'set to tr";
mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 113;BA.debugLine="ACToolBarLight1.InitMenuListener";
mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 114;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 115;BA.debugLine="Dim xl As XmlLayoutBuilder";
_xl = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 116;BA.debugLine="jo.RunMethod(\"setPopupTheme\", Array(xl.GetResour";
_jo.RunMethod("setPopupTheme",new Object[]{(Object)(_xl.GetResourceId("style","ToolbarMenu"))});
 //BA.debugLineNum = 118;BA.debugLine="GaugeHumidity.SetRanges(Array(GaugeHumidity.Crea";
mostCurrent._gaugehumidity._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugehumidity._createrange((float) (0),(float) (70),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugehumidity._createrange((float) (70),(float) (80),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugehumidity._createrange((float) (80),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 121;BA.debugLine="GaugeTemp.SetRanges(Array(GaugeTemp.CreateRange(";
mostCurrent._gaugetemp._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugetemp._createrange((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugetemp._createrange((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugetemp._createrange((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 124;BA.debugLine="GaugeHeatIndex.SetRanges(Array(GaugeHeatIndex.Cr";
mostCurrent._gaugeheatindex._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeheatindex._createrange((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeheatindex._createrange((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeheatindex._createrange((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 129;BA.debugLine="GaugeDewPoint.SetRanges(Array(GaugeDewPoint.Crea";
mostCurrent._gaugedewpoint._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugedewpoint._createrange((float) (0),(float) (60.8),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugedewpoint._createrange((float) (60.8),(float) (64.4),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugedewpoint._createrange((float) (64.4),(float) (78.8),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugedewpoint._createrange((float) (78.8),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 135;BA.debugLine="GaugeAirQuality.SetRanges(Array(GaugeAirQuality.";
mostCurrent._gaugeairquality._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeairquality._createrange((float) (0),(float) (100),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeairquality._createrange((float) (100),(float) (400),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugeairquality._createrange((float) (400),(float) (900),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeairquality._createrange((float) (900),(float) (1000),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 139;BA.debugLine="GaugeAirQuality.CurrentValue=0";
mostCurrent._gaugeairquality._setcurrentvalue((float) (0));
 //BA.debugLineNum = 141;BA.debugLine="GaugeHumidityBasement.SetRanges(Array(GaugeHumid";
mostCurrent._gaugehumiditybasement._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugehumiditybasement._createrange((float) (0),(float) (70),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugehumiditybasement._createrange((float) (70),(float) (80),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugehumiditybasement._createrange((float) (80),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 144;BA.debugLine="GaugeTempBasement.SetRanges(Array(GaugeTempBasem";
mostCurrent._gaugetempbasement._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugetempbasement._createrange((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugetempbasement._createrange((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugetempbasement._createrange((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 147;BA.debugLine="GaugeHeatIndexBasement.SetRanges(Array(GaugeHeat";
mostCurrent._gaugeheatindexbasement._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeheatindexbasement._createrange((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeheatindexbasement._createrange((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeheatindexbasement._createrange((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 152;BA.debugLine="GaugeDewPointBasement.SetRanges(Array(GaugeDewPo";
mostCurrent._gaugedewpointbasement._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugedewpointbasement._createrange((float) (0),(float) (60.8),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugedewpointbasement._createrange((float) (60.8),(float) (64.4),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugedewpointbasement._createrange((float) (64.4),(float) (78.8),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugedewpointbasement._createrange((float) (78.8),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 158;BA.debugLine="GaugeAirQualityBasement.SetRanges(Array(GaugeAir";
mostCurrent._gaugeairqualitybasement._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeairqualitybasement._createrange((float) (0),(float) (100),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeairqualitybasement._createrange((float) (100),(float) (400),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugeairqualitybasement._createrange((float) (400),(float) (900),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeairqualitybasement._createrange((float) (900),(float) (1000),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 162;BA.debugLine="GaugeAirQualityBasement.CurrentValue=0";
mostCurrent._gaugeairqualitybasement._setcurrentvalue((float) (0));
 //BA.debugLineNum = 164;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 165;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 166;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 167;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"The";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 168;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 169;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 170;BA.debugLine="lblPing.Visible = False";
mostCurrent._lblping.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 171;BA.debugLine="GaugeAirQuality.CurrentValue = 0";
mostCurrent._gaugeairquality._setcurrentvalue((float) (0));
 //BA.debugLineNum = 172;BA.debugLine="GaugeAirQualityBasement.CurrentValue = 0";
mostCurrent._gaugeairqualitybasement._setcurrentvalue((float) (0));
 //BA.debugLineNum = 173;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 174;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.Bol";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).PopAll().getObject()));
 //BA.debugLineNum = 176;BA.debugLine="lblPerceptionBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblperceptionbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 177;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.App";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 178;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 179;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bold.";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 180;BA.debugLine="lblPingBasement.Visible = False";
mostCurrent._lblpingbasement.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 181;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 182;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Initia";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).PopAll().getObject()));
 //BA.debugLineNum = 184;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 185;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Rea";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Read weather","utf8"));
 };
 } 
       catch (Exception e74) {
			processBA.setLastException(e74); //BA.debugLineNum = 188;BA.debugLine="ToastMessageShow(LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 190;BA.debugLine="End Sub";
return "";
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
 //BA.debugLineNum = 467;BA.debugLine="Sub Activity_Createmenu(Menu As ACMenu)";
 //BA.debugLineNum = 468;BA.debugLine="Try";
try { //BA.debugLineNum = 469;BA.debugLine="Menu.Clear";
_menu.Clear();
 //BA.debugLineNum = 470;BA.debugLine="gblACMenu = Menu";
mostCurrent._gblacmenu = _menu;
 //BA.debugLineNum = 471;BA.debugLine="Menu.Add(0, 0, \"Settings\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Settings"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 473;BA.debugLine="Menu.Add(0, 0, \"About\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 } 
       catch (Exception e7) {
			processBA.setLastException(e7); //BA.debugLineNum = 475;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 477;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 544;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 545;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 546;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (mostCurrent._tabstrip1.getCurrentPage()==2) { 
 //BA.debugLineNum = 547;BA.debugLine="TabStrip1.ScrollTo(1,True)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 548;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 //BA.debugLineNum = 549;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==1) { 
 //BA.debugLineNum = 551;BA.debugLine="TabStrip1.ScrollTo(0,True)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 552;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 //BA.debugLineNum = 553;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==3) { 
 //BA.debugLineNum = 555;BA.debugLine="TabStrip1.ScrollTo(2,True)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 556;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 //BA.debugLineNum = 557;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 561;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 245;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 247;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _in = null;
String _notificationclicked = "";
 //BA.debugLineNum = 192;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 193;BA.debugLine="Try";
try { //BA.debugLineNum = 194;BA.debugLine="HandleSettings";
_handlesettings();
 //BA.debugLineNum = 195;BA.debugLine="Dim in As Intent = Activity.GetStartingIntent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
_in = mostCurrent._activity.GetStartingIntent();
 //BA.debugLineNum = 196;BA.debugLine="Dim NotificationClicked As String";
_notificationclicked = "";
 //BA.debugLineNum = 197;BA.debugLine="If in.IsInitialized And in <> OldIntent Then";
if (_in.IsInitialized() && (_in).equals(_oldintent) == false) { 
 //BA.debugLineNum = 198;BA.debugLine="OldIntent = in";
_oldintent = _in;
 //BA.debugLineNum = 199;BA.debugLine="If in.HasExtra(\"Notification_Tag\") Then";
if (_in.HasExtra("Notification_Tag")) { 
 //BA.debugLineNum = 200;BA.debugLine="NotificationClicked = in.GetExtra(\"Notificatio";
_notificationclicked = BA.ObjectToString(_in.GetExtra("Notification_Tag"));
 };
 };
 //BA.debugLineNum = 203;BA.debugLine="If NotificationClicked = \"Living area temperatur";
if ((_notificationclicked).equals("Living area temperature")) { 
 //BA.debugLineNum = 204;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 205;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 }else if((_notificationclicked).equals("Living area carbon monoxide")) { 
 //BA.debugLineNum = 207;BA.debugLine="TabStrip1.ScrollTo(1,False)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 208;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 }else if((_notificationclicked).equals("Basement temperature")) { 
 //BA.debugLineNum = 210;BA.debugLine="TabStrip1.ScrollTo(2,False)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 211;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 }else if((_notificationclicked).equals("Basement carbon monoxide")) { 
 //BA.debugLineNum = 213;BA.debugLine="TabStrip1.ScrollTo(3,False)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 214;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 }else if((_notificationclicked).equals("Basement DHT22 sensor issue")) { 
 //BA.debugLineNum = 216;BA.debugLine="TabStrip1.ScrollTo(2,False)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 217;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 }else if((_notificationclicked).equals("Living area DHT22 sensor issue")) { 
 //BA.debugLineNum = 219;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 220;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 }else if((_notificationclicked).equals("Living area CO sensor issue")) { 
 //BA.debugLineNum = 222;BA.debugLine="TabStrip1.ScrollTo(1,False)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 223;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 }else if((_notificationclicked).equals("Basement CO sensor issue")) { 
 //BA.debugLineNum = 225;BA.debugLine="TabStrip1.ScrollTo(3,False)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 226;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 }else {
 //BA.debugLineNum = 228;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 229;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 };
 } 
       catch (Exception e40) {
			processBA.setLastException(e40); //BA.debugLineNum = 233;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 236;BA.debugLine="Try";
try { //BA.debugLineNum = 237;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connected";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 238;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 } 
       catch (Exception e47) {
			processBA.setLastException(e47); //BA.debugLineNum = 241;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 243;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
int _result = 0;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 391;BA.debugLine="Sub ACToolBarLight1_MenuItemClick (Item As ACMenuI";
 //BA.debugLineNum = 392;BA.debugLine="Try";
try { //BA.debugLineNum = 393;BA.debugLine="If Item.Title = \"About\" Then";
if ((_item.getTitle()).equals("About")) { 
 //BA.debugLineNum = 394;BA.debugLine="ShowAboutMenu";
_showaboutmenu();
 }else if((_item.getTitle()).equals("Settings")) { 
 //BA.debugLineNum = 396;BA.debugLine="StartActivity(screen.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_screen.CreateIntent()));
 }else if((_item.getTitle()).equals("Restart board")) { 
 //BA.debugLineNum = 398;BA.debugLine="Try";
try { //BA.debugLineNum = 399;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 400;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 401;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets,";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 402;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (mostCurrent._tabstrip1.getCurrentPage()==2) { 
 //BA.debugLineNum = 403;BA.debugLine="result = Msgbox2(\"Restart the BASEMENT contro";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 404;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 405;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 406;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 408;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringT";
_mqtt.Publish("TempHumidBasement",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else if(mostCurrent._tabstrip1.getCurrentPage()==1) { 
 //BA.debugLineNum = 411;BA.debugLine="result = Msgbox2(\"Restart the AIR QUALITY con";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 412;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 413;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 414;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 416;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Restar";
_mqtt.Publish("MQ7",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else if(mostCurrent._tabstrip1.getCurrentPage()==3) { 
 //BA.debugLineNum = 419;BA.debugLine="result = Msgbox2(\"Restart the BASEMENT AIR QU";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 420;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 421;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 422;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 424;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes";
_mqtt.Publish("MQ7Basement",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else {
 //BA.debugLineNum = 427;BA.debugLine="result = Msgbox2(\"Restart the WEATHER control";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the WEATHER controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 428;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 429;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 430;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 432;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Restart controller","utf8"));
 };
 };
 } 
       catch (Exception e45) {
			processBA.setLastException(e45); //BA.debugLineNum = 436;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 };
 } 
       catch (Exception e49) {
			processBA.setLastException(e49); //BA.debugLineNum = 440;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 442;BA.debugLine="End Sub";
return "";
}
public static String  _checkairqualitysetting() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _status = "";
String[] _a = null;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 563;BA.debugLine="Sub CheckAirQualitySetting";
 //BA.debugLineNum = 564;BA.debugLine="Try";
try { //BA.debugLineNum = 565;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 566;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 567;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 568;BA.debugLine="status = StateManager.GetSetting(\"AirQuality\")";
_status = mostCurrent._statemanager._getsetting(mostCurrent.activityBA,"AirQuality");
 //BA.debugLineNum = 569;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 570;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 571;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 572;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 573;BA.debugLine="GaugeAirQuality.CurrentValue = a(0)";
mostCurrent._gaugeairquality._setcurrentvalue((float)(Double.parseDouble(_a[(int) (0)])));
 //BA.debugLineNum = 574;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 575;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 577;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 };
 //BA.debugLineNum = 579;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 580;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 581;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 582;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 583;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 585;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 586;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 588;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 591;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.B";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e31) {
			processBA.setLastException(e31); //BA.debugLineNum = 595;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 597;BA.debugLine="End Sub";
return "";
}
public static String  _checkairqualitysettingbasement() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _status = "";
String[] _a = null;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 599;BA.debugLine="Sub CheckAirQualitySettingBasement";
 //BA.debugLineNum = 600;BA.debugLine="Try";
try { //BA.debugLineNum = 601;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 602;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 603;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 604;BA.debugLine="status = StateManager.GetSetting(\"AirQualityBase";
_status = mostCurrent._statemanager._getsetting(mostCurrent.activityBA,"AirQualityBasement");
 //BA.debugLineNum = 605;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 606;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 607;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 608;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 609;BA.debugLine="GaugeAirQualityBasement.CurrentValue = a(0)";
mostCurrent._gaugeairqualitybasement._setcurrentvalue((float)(Double.parseDouble(_a[(int) (0)])));
 //BA.debugLineNum = 610;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 611;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bo";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 613;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bo";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 };
 //BA.debugLineNum = 615;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 616;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 617;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 618;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 619;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 621;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 622;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Ini";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 624;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Ini";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 627;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Init";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdatebasement.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e31) {
			processBA.setLastException(e31); //BA.debugLineNum = 631;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 633;BA.debugLine="End Sub";
return "";
}
public static String  _checktemphumiditysetting() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _status = "";
String[] _a = null;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 635;BA.debugLine="Sub CheckTempHumiditySetting";
 //BA.debugLineNum = 636;BA.debugLine="Try";
try { //BA.debugLineNum = 637;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 638;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 639;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 640;BA.debugLine="status = StateManager.GetSetting(\"TempHumidity\")";
_status = mostCurrent._statemanager._getsetting(mostCurrent.activityBA,"TempHumidity");
 //BA.debugLineNum = 641;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 642;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 643;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 644;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 645;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 646;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 647;BA.debugLine="GaugeTemp.CurrentValue = a(1)";
mostCurrent._gaugetemp._setcurrentvalue((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 648;BA.debugLine="GaugeHumidity.CurrentValue = a(2)";
mostCurrent._gaugehumidity._setcurrentvalue((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 649;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 650;BA.debugLine="If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6)) || (_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 651;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 653;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 655;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 };
 //BA.debugLineNum = 658;BA.debugLine="GaugeHeatIndex.CurrentValue = a(5)";
mostCurrent._gaugeheatindex._setcurrentvalue((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 659;BA.debugLine="GaugeDewPoint.CurrentValue = a(6)";
mostCurrent._gaugedewpoint._setcurrentvalue((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 660;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 661;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 662;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 663;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 664;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 666;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 667;BA.debugLine="If a(8).IndexOf(\"|00:\") > -1 And p.Minutes=59";
if (_a[(int) (8)].indexOf("|00:")>-1 && _p.Minutes==59) { 
 //BA.debugLineNum = 668;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appe";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else if(_a[(int) (8)].indexOf("|00:0")>-1) { 
 //BA.debugLineNum = 670;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appe";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 672;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appe";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else {
 //BA.debugLineNum = 675;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appen";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 678;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e45) {
			processBA.setLastException(e45); //BA.debugLineNum = 682;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 684;BA.debugLine="End Sub";
return "";
}
public static String  _checktemphumiditysettingbasement() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _status = "";
String[] _a = null;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 686;BA.debugLine="Sub CheckTempHumiditySettingBasement";
 //BA.debugLineNum = 687;BA.debugLine="Try";
try { //BA.debugLineNum = 688;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 689;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 690;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 691;BA.debugLine="status = StateManager.GetSetting(\"TempHumidityBa";
_status = mostCurrent._statemanager._getsetting(mostCurrent.activityBA,"TempHumidityBasement");
 //BA.debugLineNum = 692;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 693;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 694;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 695;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 696;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 697;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 698;BA.debugLine="GaugeTempBasement.CurrentValue = a(1)";
mostCurrent._gaugetempbasement._setcurrentvalue((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 699;BA.debugLine="GaugeHumidityBasement.CurrentValue = a(2)";
mostCurrent._gaugehumiditybasement._setcurrentvalue((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 700;BA.debugLine="lblPerceptionBasement.Text = cs.Initialize.Bol";
mostCurrent._lblperceptionbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 701;BA.debugLine="If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6)) || (_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 702;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 704;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 706;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 };
 //BA.debugLineNum = 708;BA.debugLine="GaugeHeatIndexBasement.CurrentValue = a(5)";
mostCurrent._gaugeheatindexbasement._setcurrentvalue((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 709;BA.debugLine="GaugeDewPointBasement.CurrentValue = a(6)";
mostCurrent._gaugedewpointbasement._setcurrentvalue((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 710;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 711;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 712;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 713;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 714;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 716;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 717;BA.debugLine="If a(8).IndexOf(\"|00:\") > -1 And p.Minutes=59";
if (_a[(int) (8)].indexOf("|00:")>-1 && _p.Minutes==59) { 
 //BA.debugLineNum = 718;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.B";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else if(_a[(int) (8)].indexOf("|00:0")>-1) { 
 //BA.debugLineNum = 720;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.B";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 722;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.B";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else {
 //BA.debugLineNum = 725;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bo";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 728;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bol";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdatebasement.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e45) {
			processBA.setLastException(e45); //BA.debugLineNum = 732;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 734;BA.debugLine="End Sub";
return "";
}
public static String  _createpreferencescreen() throws Exception{
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat1 = null;
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat2 = null;
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat3 = null;
anywheresoftware.b4a.objects.IntentWrapper _in = null;
 //BA.debugLineNum = 736;BA.debugLine="Sub CreatePreferenceScreen";
 //BA.debugLineNum = 737;BA.debugLine="screen.Initialize(\"Settings\", \"\")";
_screen.Initialize("Settings","");
 //BA.debugLineNum = 739;BA.debugLine="Dim cat1,cat2,cat3 As AHPreferenceCategory";
_cat1 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat2 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat3 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
 //BA.debugLineNum = 741;BA.debugLine="cat1.Initialize(\"Temperature & Humidity\")";
_cat1.Initialize("Temperature & Humidity");
 //BA.debugLineNum = 742;BA.debugLine="cat1.AddEditText(\"TempHumidityCooldownTime\", \"Liv";
_cat1.AddEditText("TempHumidityCooldownTime","Living Area Cooldown Time","Minimum creation time interval between new notification","5","");
 //BA.debugLineNum = 743;BA.debugLine="cat1.AddEditText(\"TempHumidityCooldownTimeBasemen";
_cat1.AddEditText("TempHumidityCooldownTimeBasement","Basement Cooldown Time","Minimum creation time interval between new notification","5","");
 //BA.debugLineNum = 745;BA.debugLine="cat2.Initialize(\"Special Settings\")";
_cat2.Initialize("Special Settings");
 //BA.debugLineNum = 746;BA.debugLine="Dim In As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 747;BA.debugLine="In.Initialize(\"android.settings.ACTION_NOTIFICATI";
_in.Initialize("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS","");
 //BA.debugLineNum = 748;BA.debugLine="cat2.AddIntent(\"Notification Access\", \"Enable or";
_cat2.AddIntent("Notification Access","Enable or disable listening to notifications",(android.content.Intent)(_in.getObject()),BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 750;BA.debugLine="cat3.Initialize(\"Sensors\")";
_cat3.Initialize("Sensors");
 //BA.debugLineNum = 751;BA.debugLine="cat3.AddEditText(\"SensorNotRespondingTime\", \"Sens";
_cat3.AddEditText("SensorNotRespondingTime","Sensor Not Responding","Minimum data age to notify","5","");
 //BA.debugLineNum = 753;BA.debugLine="screen.AddPreferenceCategory(cat2)";
_screen.AddPreferenceCategory(_cat2);
 //BA.debugLineNum = 754;BA.debugLine="screen.AddPreferenceCategory(cat1)";
_screen.AddPreferenceCategory(_cat1);
 //BA.debugLineNum = 755;BA.debugLine="screen.AddPreferenceCategory(cat3)";
_screen.AddPreferenceCategory(_cat3);
 //BA.debugLineNum = 756;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting(mostCurrent.activityBA,"TempHumidityCooldownTime","5");
 //BA.debugLineNum = 757;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting(mostCurrent.activityBA,"TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 758;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting(mostCurrent.activityBA,"SensorNotRespondingTime","5");
 //BA.debugLineNum = 759;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(mostCurrent.activityBA);
 //BA.debugLineNum = 760;BA.debugLine="End Sub";
return "";
}
public static String  _getairquality(int _number) throws Exception{
 //BA.debugLineNum = 315;BA.debugLine="Sub GetAirQuality(number As Int) As String";
 //BA.debugLineNum = 318;BA.debugLine="If number <= 100 Then";
if (_number<=100) { 
 //BA.debugLineNum = 319;BA.debugLine="Return(\"Carbon monoxide perfect\")";
if (true) return ("Carbon monoxide perfect");
 }else if(((_number>100) && (_number<400)) || _number==400) { 
 //BA.debugLineNum = 321;BA.debugLine="Return(\"Carbon monoxide normal\")";
if (true) return ("Carbon monoxide normal");
 }else if(((_number>400) && (_number<900)) || _number==900) { 
 //BA.debugLineNum = 323;BA.debugLine="Return(\"Carbon monoxide high\")";
if (true) return ("Carbon monoxide high");
 }else if(_number>900) { 
 //BA.debugLineNum = 325;BA.debugLine="Return(\"ALARM Carbon monoxide very high\")";
if (true) return ("ALARM Carbon monoxide very high");
 }else {
 //BA.debugLineNum = 327;BA.debugLine="Return(\"MQ-7 - cant read any value - check the s";
if (true) return ("MQ-7 - cant read any value - check the sensor!");
 };
 //BA.debugLineNum = 329;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _getalltablabels(anywheresoftware.b4a.objects.TabStripViewPager _tabstrip) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
anywheresoftware.b4a.objects.PanelWrapper _tc = null;
anywheresoftware.b4a.objects.collections.List _res = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
 //BA.debugLineNum = 531;BA.debugLine="Public Sub GetAllTabLabels (tabstrip As TabStrip)";
 //BA.debugLineNum = 532;BA.debugLine="Dim jo As JavaObject = tabstrip";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_tabstrip));
 //BA.debugLineNum = 533;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 534;BA.debugLine="r.Target = jo.GetField(\"tabStrip\")";
_r.Target = _jo.GetField("tabStrip");
 //BA.debugLineNum = 535;BA.debugLine="Dim tc As Panel = r.GetField(\"tabsContainer\")";
_tc = new anywheresoftware.b4a.objects.PanelWrapper();
_tc.setObject((android.view.ViewGroup)(_r.GetField("tabsContainer")));
 //BA.debugLineNum = 536;BA.debugLine="Dim res As List";
_res = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 537;BA.debugLine="res.Initialize";
_res.Initialize();
 //BA.debugLineNum = 538;BA.debugLine="For Each v As View In tc";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group7 = _tc;
final int groupLen7 = group7.getSize()
;int index7 = 0;
;
for (; index7 < groupLen7;index7++){
_v.setObject((android.view.View)(group7.Get(index7)));
 //BA.debugLineNum = 539;BA.debugLine="If v Is Label Then res.Add(v)";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
_res.Add((Object)(_v.getObject()));};
 }
};
 //BA.debugLineNum = 541;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 542;BA.debugLine="End Sub";
return null;
}
public static String  _getcomfort(String _dht11comfortstatus) throws Exception{
String _localcomfortstatus = "";
 //BA.debugLineNum = 364;BA.debugLine="Sub GetComfort(DHT11ComfortStatus As String) As St";
 //BA.debugLineNum = 365;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = "";
 //BA.debugLineNum = 366;BA.debugLine="Select Case DHT11ComfortStatus";
switch (BA.switchObjectToInt(_dht11comfortstatus,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(8),BA.NumberToString(9),BA.NumberToString(10))) {
case 0: {
 //BA.debugLineNum = 368;BA.debugLine="localcomfortstatus = \"OK\"";
_localcomfortstatus = "OK";
 break; }
case 1: {
 //BA.debugLineNum = 370;BA.debugLine="localcomfortstatus = \"Too hot\"";
_localcomfortstatus = "Too hot";
 break; }
case 2: {
 //BA.debugLineNum = 372;BA.debugLine="localcomfortstatus = \"Too cold\"";
_localcomfortstatus = "Too cold";
 break; }
case 3: {
 //BA.debugLineNum = 374;BA.debugLine="localcomfortstatus = \"Too dry\"";
_localcomfortstatus = "Too dry";
 break; }
case 4: {
 //BA.debugLineNum = 376;BA.debugLine="localcomfortstatus = \"Hot and dry\"";
_localcomfortstatus = "Hot and dry";
 break; }
case 5: {
 //BA.debugLineNum = 378;BA.debugLine="localcomfortstatus = \"Cold and dry\"";
_localcomfortstatus = "Cold and dry";
 break; }
case 6: {
 //BA.debugLineNum = 380;BA.debugLine="localcomfortstatus = \"Too humid\"";
_localcomfortstatus = "Too humid";
 break; }
case 7: {
 //BA.debugLineNum = 382;BA.debugLine="localcomfortstatus = \"Hot and humid\"";
_localcomfortstatus = "Hot and humid";
 break; }
case 8: {
 //BA.debugLineNum = 384;BA.debugLine="localcomfortstatus = \"Cold and humid\"";
_localcomfortstatus = "Cold and humid";
 break; }
default: {
 //BA.debugLineNum = 386;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = "Unknown";
 break; }
}
;
 //BA.debugLineNum = 388;BA.debugLine="Return localcomfortstatus";
if (true) return _localcomfortstatus;
 //BA.debugLineNum = 389;BA.debugLine="End Sub";
return "";
}
public static String  _getperception(String _dht11perception) throws Exception{
String _localperception = "";
 //BA.debugLineNum = 331;BA.debugLine="Sub GetPerception(DHT11Perception As String) As St";
 //BA.debugLineNum = 342;BA.debugLine="Dim localperception As String";
_localperception = "";
 //BA.debugLineNum = 343;BA.debugLine="Select Case DHT11Perception";
switch (BA.switchObjectToInt(_dht11perception,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(7))) {
case 0: {
 //BA.debugLineNum = 345;BA.debugLine="localperception = \"Feels like the western US, a";
_localperception = "Feels like the western US, a bit dry to some";
 break; }
case 1: {
 //BA.debugLineNum = 347;BA.debugLine="localperception = \"Very comfortable\"";
_localperception = "Very comfortable";
 break; }
case 2: {
 //BA.debugLineNum = 349;BA.debugLine="localperception = \"Comfortable\"";
_localperception = "Comfortable";
 break; }
case 3: {
 //BA.debugLineNum = 351;BA.debugLine="localperception = \"OK but humidity is at upper";
_localperception = "OK but humidity is at upper limit";
 break; }
case 4: {
 //BA.debugLineNum = 353;BA.debugLine="localperception = \"Uncomfortable and the humidi";
_localperception = "Uncomfortable and the humidity is at upper limit";
 break; }
case 5: {
 //BA.debugLineNum = 355;BA.debugLine="localperception = \"Very humid, quite uncomforta";
_localperception = "Very humid, quite uncomfortable";
 break; }
case 6: {
 //BA.debugLineNum = 357;BA.debugLine="localperception = \"Extremely uncomfortable, opp";
_localperception = "Extremely uncomfortable, oppressive";
 break; }
case 7: {
 //BA.debugLineNum = 359;BA.debugLine="localperception = \"Severely high, even deadly f";
_localperception = "Severely high, even deadly for asthma related illnesses";
 break; }
}
;
 //BA.debugLineNum = 361;BA.debugLine="Return localperception";
if (true) return _localperception;
 //BA.debugLineNum = 362;BA.debugLine="End Sub";
return "";
}
public static String  _getversioncode() throws Exception{
String _appversion = "";
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
String _packagename = "";
 //BA.debugLineNum = 454;BA.debugLine="Sub GetVersionCode() As String";
 //BA.debugLineNum = 455;BA.debugLine="Dim AppVersion As String";
_appversion = "";
 //BA.debugLineNum = 456;BA.debugLine="Try";
try { //BA.debugLineNum = 457;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 458;BA.debugLine="Dim packageName As String";
_packagename = "";
 //BA.debugLineNum = 459;BA.debugLine="packageName =  Application.PackageName";
_packagename = anywheresoftware.b4a.keywords.Common.Application.getPackageName();
 //BA.debugLineNum = 460;BA.debugLine="AppVersion = pm.GetVersionName(packageName)";
_appversion = _pm.GetVersionName(_packagename);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 462;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 464;BA.debugLine="Return AppVersion";
if (true) return _appversion;
 //BA.debugLineNum = 465;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 31;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 32;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 34;BA.debugLine="Private gblACMenu As ACMenu";
mostCurrent._gblacmenu = new de.amberhome.objects.appcompat.ACMenuWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 36;BA.debugLine="Private GaugeHumidity As Gauge";
mostCurrent._gaugehumidity = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 37;BA.debugLine="Private GaugeTemp As Gauge";
mostCurrent._gaugetemp = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 38;BA.debugLine="Private GaugeDewPoint As Gauge";
mostCurrent._gaugedewpoint = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 39;BA.debugLine="Private GaugeHeatIndex As Gauge";
mostCurrent._gaugeheatindex = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 40;BA.debugLine="Private lblComfort As Label";
mostCurrent._lblcomfort = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private lblPerception As Label";
mostCurrent._lblperception = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private lblLastUpdate As Label";
mostCurrent._lbllastupdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private lblPing As Label";
mostCurrent._lblping = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private TabStrip1 As TabStrip";
mostCurrent._tabstrip1 = new anywheresoftware.b4a.objects.TabStripViewPager();
 //BA.debugLineNum = 45;BA.debugLine="Private lblFontAwesome As Label";
mostCurrent._lblfontawesome = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private GaugeAirQuality As Gauge";
mostCurrent._gaugeairquality = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 47;BA.debugLine="Private lblAirQuality As Label";
mostCurrent._lblairquality = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private lblAirQualityLastUpdate As Label";
mostCurrent._lblairqualitylastupdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private ScrollView1 As ScrollView";
mostCurrent._scrollview1 = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private GaugeAirQualityBasement As Gauge";
mostCurrent._gaugeairqualitybasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 52;BA.debugLine="Private lblAirQualityBasement As Label";
mostCurrent._lblairqualitybasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private lblAirQualityLastUpdateBasement As Label";
mostCurrent._lblairqualitylastupdatebasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private PanelAirQualityBasement As Panel";
mostCurrent._panelairqualitybasement = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private GaugeDewPointBasement As Gauge";
mostCurrent._gaugedewpointbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 56;BA.debugLine="Private GaugeHeatIndexBasement As Gauge";
mostCurrent._gaugeheatindexbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 57;BA.debugLine="Private GaugeHumidityBasement As Gauge";
mostCurrent._gaugehumiditybasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 58;BA.debugLine="Private GaugeTempBasement As Gauge";
mostCurrent._gaugetempbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 59;BA.debugLine="Private lblComfortBasement As Label";
mostCurrent._lblcomfortbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private lblLastUpdateBasement As Label";
mostCurrent._lbllastupdatebasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private lblPerceptionBasement As Label";
mostCurrent._lblperceptionbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private lblPingBasement As Label";
mostCurrent._lblpingbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private ScrollViewBasement As ScrollView";
mostCurrent._scrollviewbasement = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private PanelTempHumidityBasement As Panel";
mostCurrent._paneltemphumiditybasement = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static String  _handlesettings() throws Exception{
 //BA.debugLineNum = 774;BA.debugLine="Sub HandleSettings";
 //BA.debugLineNum = 775;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting(mostCurrent.activityBA,"TempHumidityCooldownTime",_manager.GetString("TempHumidityCooldownTime"));
 //BA.debugLineNum = 776;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting(mostCurrent.activityBA,"TempHumidityCooldownTimeBasement",_manager.GetString("TempHumidityCooldownTimeBasement"));
 //BA.debugLineNum = 777;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting(mostCurrent.activityBA,"SensorNotRespondingTime",_manager.GetString("SensorNotRespondingTime"));
 //BA.debugLineNum = 778;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(mostCurrent.activityBA);
 //BA.debugLineNum = 779;BA.debugLine="End Sub";
return "";
}
public static String  _hideping() throws Exception{
 //BA.debugLineNum = 479;BA.debugLine="Private Sub HidePing";
 //BA.debugLineNum = 480;BA.debugLine="lblPing.SetVisibleAnimated(200, False)";
mostCurrent._lblping.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 481;BA.debugLine="End Sub";
return "";
}
public static String  _hidepingbasement() throws Exception{
 //BA.debugLineNum = 483;BA.debugLine="Private Sub HidePingBasement";
 //BA.debugLineNum = 484;BA.debugLine="lblPingBasement.SetVisibleAnimated(200, False)";
mostCurrent._lblpingbasement.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 485;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connect() throws Exception{
String _clientid = "";
anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper _connopt = null;
 //BA.debugLineNum = 250;BA.debugLine="Sub MQTT_Connect";
 //BA.debugLineNum = 251;BA.debugLine="Try";
try { //BA.debugLineNum = 252;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'crea";
_clientid = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (999999999)));
 //BA.debugLineNum = 253;BA.debugLine="MQTT.Initialize(\"MQTT\", MQTTServerURI, Cli";
_mqtt.Initialize(processBA,"MQTT",_mqttserveruri,_clientid);
 //BA.debugLineNum = 255;BA.debugLine="Dim ConnOpt As MqttConnectOptions";
_connopt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper();
 //BA.debugLineNum = 256;BA.debugLine="ConnOpt.Initialize(MQTTUser, MQTTPassword)";
_connopt.Initialize(_mqttuser,_mqttpassword);
 //BA.debugLineNum = 257;BA.debugLine="MQTT.Connect2(ConnOpt)";
_mqtt.Connect2((org.eclipse.paho.client.mqttv3.MqttConnectOptions)(_connopt.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 259;BA.debugLine="Log(\"MQTT_Connect: \" & LastException)";
anywheresoftware.b4a.keywords.Common.Log("MQTT_Connect: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 261;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 263;BA.debugLine="Sub MQTT_Connected (Success As Boolean)";
 //BA.debugLineNum = 264;BA.debugLine="Try";
try { //BA.debugLineNum = 265;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 266;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 //BA.debugLineNum = 267;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 }else {
 //BA.debugLineNum = 269;BA.debugLine="Log(\"Connected to MQTT broker\")";
anywheresoftware.b4a.keywords.Common.Log("Connected to MQTT broker");
 //BA.debugLineNum = 270;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
_mqtt.Subscribe("TempHumid",(int) (0));
 //BA.debugLineNum = 271;BA.debugLine="MQTT.Subscribe(\"MQ7\", 0)";
_mqtt.Subscribe("MQ7",(int) (0));
 //BA.debugLineNum = 272;BA.debugLine="MQTT.Subscribe(\"MQ7Basement\", 0)";
_mqtt.Subscribe("MQ7Basement",(int) (0));
 //BA.debugLineNum = 273;BA.debugLine="MQTT.Subscribe(\"TempHumidBasement\", 0)";
_mqtt.Subscribe("TempHumidBasement",(int) (0));
 };
 } 
       catch (Exception e13) {
			processBA.setLastException(e13); //BA.debugLineNum = 276;BA.debugLine="Log(\"MQTT_Connected: \" & LastException)";
anywheresoftware.b4a.keywords.Common.Log("MQTT_Connected: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 278;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_disconnected() throws Exception{
 //BA.debugLineNum = 280;BA.debugLine="Private Sub MQTT_Disconnected";
 //BA.debugLineNum = 281;BA.debugLine="Try";
try { //BA.debugLineNum = 282;BA.debugLine="gblACMenu.Clear";
mostCurrent._gblacmenu.Clear();
 //BA.debugLineNum = 283;BA.debugLine="gblACMenu.Add(0, 0, \"Settings\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Settings"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 285;BA.debugLine="gblACMenu.Add(0, 0, \"About\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 286;BA.debugLine="Log(\"Disconnected from MQTT broker\")";
anywheresoftware.b4a.keywords.Common.Log("Disconnected from MQTT broker");
 //BA.debugLineNum = 287;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 289;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 291;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
 //BA.debugLineNum = 293;BA.debugLine="Private Sub MQTT_MessageArrived (Topic As String,";
 //BA.debugLineNum = 294;BA.debugLine="Try";
try { //BA.debugLineNum = 295;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic).equals("TempHumid")) { 
 //BA.debugLineNum = 296;BA.debugLine="lblPing.SetVisibleAnimated(500, True)";
mostCurrent._lblping.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 297;BA.debugLine="csu.CallSubPlus(Me, \"HidePing\", 700)";
_csu._v7(main.getObject(),"HidePing",(int) (700));
 //BA.debugLineNum = 299;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 }else if((_topic).equals("MQ7")) { 
 //BA.debugLineNum = 301;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 }else if((_topic).equals("MQ7Basement")) { 
 //BA.debugLineNum = 303;BA.debugLine="CheckAirQualitySettingBasement";
_checkairqualitysettingbasement();
 }else if((_topic).equals("TempHumidBasement")) { 
 //BA.debugLineNum = 305;BA.debugLine="lblPingBasement.SetVisibleAnimated(500, True)";
mostCurrent._lblpingbasement.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 306;BA.debugLine="csu.CallSubPlus(Me, \"HidePingBasement\", 700)";
_csu._v7(main.getObject(),"HidePingBasement",(int) (700));
 //BA.debugLineNum = 308;BA.debugLine="CheckTempHumiditySettingBasement";
_checktemphumiditysettingbasement();
 };
 } 
       catch (Exception e16) {
			processBA.setLastException(e16); //BA.debugLineNum = 311;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 313;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
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
 //BA.debugLineNum = 27;BA.debugLine="Dim manager As AHPreferenceManager";
_manager = new de.amberhome.objects.preferenceactivity.PreferenceManager();
 //BA.debugLineNum = 28;BA.debugLine="Dim screen As AHPreferenceScreen";
_screen = new de.amberhome.objects.preferenceactivity.PreferenceScreenWrapper();
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public static String  _setdefaults() throws Exception{
 //BA.debugLineNum = 763;BA.debugLine="Sub SetDefaults";
 //BA.debugLineNum = 765;BA.debugLine="manager.SetString(\"TempHumidityCooldownTime\", \"5\"";
_manager.SetString("TempHumidityCooldownTime","5");
 //BA.debugLineNum = 766;BA.debugLine="manager.SetString(\"TempHumidityCooldownTimeBaseme";
_manager.SetString("TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 767;BA.debugLine="manager.SetString(\"SensorNotRespondingTime\", \"5\")";
_manager.SetString("SensorNotRespondingTime","5");
 //BA.debugLineNum = 768;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting(mostCurrent.activityBA,"TempHumidityCooldownTime","5");
 //BA.debugLineNum = 769;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting(mostCurrent.activityBA,"TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 770;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting(mostCurrent.activityBA,"SensorNotRespondingTime","5");
 //BA.debugLineNum = 771;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(mostCurrent.activityBA);
 //BA.debugLineNum = 772;BA.debugLine="End Sub";
return "";
}
public static String  _showaboutmenu() throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 444;BA.debugLine="Sub ShowAboutMenu";
 //BA.debugLineNum = 445;BA.debugLine="Try";
try { //BA.debugLineNum = 446;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 447;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cloyd.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 448;BA.debugLine="Msgbox2(\"Smart Home Monitor v\" & GetVersionCode";
anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Smart Home Monitor v"+_getversioncode()+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Developed by Cloyd Nino Catanaoan"+anywheresoftware.b4a.keywords.Common.CRLF+"September 10, 2018"),BA.ObjectToCharSequence("About"),"OK","","",_bd.getBitmap(),mostCurrent.activityBA);
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 450;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 452;BA.debugLine="End Sub";
return "";
}
public static String  _tabstrip1_pageselected(int _position) throws Exception{
 //BA.debugLineNum = 500;BA.debugLine="Sub TabStrip1_PageSelected (Position As Int)";
 //BA.debugLineNum = 501;BA.debugLine="Try";
try { //BA.debugLineNum = 502;BA.debugLine="If Position = 0 Then";
if (_position==0) { 
 //BA.debugLineNum = 503;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 504;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Re";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Read weather","utf8"));
 };
 //BA.debugLineNum = 506;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 };
 //BA.debugLineNum = 508;BA.debugLine="If Position = 1 Then";
if (_position==1) { 
 //BA.debugLineNum = 509;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 510;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Read vol";
_mqtt.Publish("MQ7",_bc.StringToBytes("Read voltage","utf8"));
 };
 //BA.debugLineNum = 512;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 };
 //BA.debugLineNum = 514;BA.debugLine="If Position = 2 Then";
if (_position==2) { 
 //BA.debugLineNum = 515;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 516;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringToB";
_mqtt.Publish("TempHumidBasement",_bc.StringToBytes("Read weather","utf8"));
 };
 //BA.debugLineNum = 518;BA.debugLine="CheckTempHumiditySettingBasement";
_checktemphumiditysettingbasement();
 };
 //BA.debugLineNum = 520;BA.debugLine="If Position = 3 Then";
if (_position==3) { 
 //BA.debugLineNum = 521;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 522;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes(\"";
_mqtt.Publish("MQ7Basement",_bc.StringToBytes("Read voltage","utf8"));
 };
 //BA.debugLineNum = 524;BA.debugLine="CheckAirQualitySettingBasement";
_checkairqualitysettingbasement();
 };
 } 
       catch (Exception e27) {
			processBA.setLastException(e27); //BA.debugLineNum = 527;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 529;BA.debugLine="End Sub";
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
