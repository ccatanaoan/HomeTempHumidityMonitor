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

public class main extends androidx.appcompat.app.AppCompatActivity implements B4AActivity{
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
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, true))
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
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
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
public static String _emailaddress = "";
public static String _password = "";
public static String _authtoken = "";
public static String _userregion = "";
public static String _accountid = "";
public static String _networkid = "";
public static String _commandid = "";
public static boolean _commandcomplete = false;
public static String _camerathumbnail = "";
public static String _response = "";
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
public anywheresoftware.b4a.objects.LabelWrapper _lblstatus = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ivdriveway = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ivfrontdoor = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ivsideyard = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scrollviewblink = null;
public anywheresoftware.b4a.objects.PanelWrapper _panelblink = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldriveway = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfrontdoor = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsideyard = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndriveway = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldrivewaybatt = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldrivewaytimestamp = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldrivewaywifi = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfrontdoorbatt = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfrontdoortimestamp = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfrontdoorwifi = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsideyardbatt = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsideyardtimestamp = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsideyardwifi = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsyncmodule = null;
public cloyd.smart.home.monitor.b4xpageindicator _b4xpageindicator1 = null;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;

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
String _strhumidityaddvalue = "";
 //BA.debugLineNum = 99;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 100;BA.debugLine="Try";
try { //BA.debugLineNum = 101;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 102;BA.debugLine="CreatePreferenceScreen";
_createpreferencescreen();
 //BA.debugLineNum = 103;BA.debugLine="If manager.GetAll.Size = 0 Then SetDefaults";
if (_manager.GetAll().getSize()==0) { 
_setdefaults();};
 //BA.debugLineNum = 105;BA.debugLine="StartService(SmartHomeMonitor)";
anywheresoftware.b4a.keywords.Common.StartService(processBA,(Object)(mostCurrent._smarthomemonitor.getObject()));
 //BA.debugLineNum = 106;BA.debugLine="csu.Initialize";
_csu._initialize(processBA);
 //BA.debugLineNum = 107;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 109;BA.debugLine="Activity.LoadLayout(\"Main\")";
mostCurrent._activity.LoadLayout("Main",mostCurrent.activityBA);
 //BA.debugLineNum = 111;BA.debugLine="TabStrip1.LoadLayout(\"1ScrollView\", \"LIVING AREA";
mostCurrent._tabstrip1.LoadLayout("1ScrollView",BA.ObjectToCharSequence("LIVING AREA"+anywheresoftware.b4a.keywords.Common.CRLF+"Temp & Humidity  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf2c7)))));
 //BA.debugLineNum = 112;BA.debugLine="ScrollView1.Panel.LoadLayout(\"1\")";
mostCurrent._scrollview1.getPanel().LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 113;BA.debugLine="Panel1.Height = ScrollView1.Height 'Panel1.Heigh";
mostCurrent._panel1.setHeight(mostCurrent._scrollview1.getHeight());
 //BA.debugLineNum = 114;BA.debugLine="ScrollView1.Panel.Height = Panel1.Height";
mostCurrent._scrollview1.getPanel().setHeight(mostCurrent._panel1.getHeight());
 //BA.debugLineNum = 115;BA.debugLine="TabStrip1.LoadLayout(\"2\", \"LIVING AREA\" & CRLF &";
mostCurrent._tabstrip1.LoadLayout("2",BA.ObjectToCharSequence("LIVING AREA"+anywheresoftware.b4a.keywords.Common.CRLF+"Air Quality (CO)  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf289)))));
 //BA.debugLineNum = 116;BA.debugLine="TabStrip1.LoadLayout(\"ScrollViewBasement\", \"BASE";
mostCurrent._tabstrip1.LoadLayout("ScrollViewBasement",BA.ObjectToCharSequence("BASEMENT"+anywheresoftware.b4a.keywords.Common.CRLF+"Temp & Humidity  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf2c7)))));
 //BA.debugLineNum = 117;BA.debugLine="ScrollViewBasement.Panel.LoadLayout(\"TempHumidit";
mostCurrent._scrollviewbasement.getPanel().LoadLayout("TempHumidityBasement",mostCurrent.activityBA);
 //BA.debugLineNum = 118;BA.debugLine="PanelTempHumidityBasement.Height = ScrollViewBas";
mostCurrent._paneltemphumiditybasement.setHeight(mostCurrent._scrollviewbasement.getHeight());
 //BA.debugLineNum = 119;BA.debugLine="ScrollViewBasement.Panel.Height = PanelTempHumid";
mostCurrent._scrollviewbasement.getPanel().setHeight(mostCurrent._paneltemphumiditybasement.getHeight());
 //BA.debugLineNum = 120;BA.debugLine="TabStrip1.LoadLayout(\"AirQualityBasement\", \"BASE";
mostCurrent._tabstrip1.LoadLayout("AirQualityBasement",BA.ObjectToCharSequence("BASEMENT"+anywheresoftware.b4a.keywords.Common.CRLF+"Air Quality (CO)  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf289)))));
 //BA.debugLineNum = 121;BA.debugLine="TabStrip1.LoadLayout(\"blinkscrollview\", \"OUTSIDE";
mostCurrent._tabstrip1.LoadLayout("blinkscrollview",BA.ObjectToCharSequence("OUTSIDE"+anywheresoftware.b4a.keywords.Common.CRLF+"Security Camera  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf030)))));
 //BA.debugLineNum = 122;BA.debugLine="ScrollViewBlink.Panel.LoadLayout(\"blink\")";
mostCurrent._scrollviewblink.getPanel().LoadLayout("blink",mostCurrent.activityBA);
 //BA.debugLineNum = 124;BA.debugLine="ScrollViewBlink.panel.height = 910dip";
mostCurrent._scrollviewblink.getPanel().setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (910)));
 //BA.debugLineNum = 125;BA.debugLine="panelBlink.Height = 910dip";
mostCurrent._panelblink.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (910)));
 //BA.debugLineNum = 127;BA.debugLine="For Each lbl As Label In GetAllTabLabels(TabStri";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
{
final anywheresoftware.b4a.BA.IterableList group24 = _getalltablabels(mostCurrent._tabstrip1);
final int groupLen24 = group24.getSize()
;int index24 = 0;
;
for (; index24 < groupLen24;index24++){
_lbl.setObject((android.widget.TextView)(group24.Get(index24)));
 //BA.debugLineNum = 129;BA.debugLine="lbl.SingleLine = False";
_lbl.setSingleLine(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 131;BA.debugLine="lbl.Typeface = Typeface.FONTAWESOME";
_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME());
 //BA.debugLineNum = 133;BA.debugLine="lbl.Padding = Array As Int(0, 0, 0, 0)";
_lbl.setPadding(new int[]{(int) (0),(int) (0),(int) (0),(int) (0)});
 }
};
 //BA.debugLineNum = 137;BA.debugLine="For Each v As View In GetAllTabLabels(TabStrip1)";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group29 = _getalltablabels(mostCurrent._tabstrip1);
final int groupLen29 = group29.getSize()
;int index29 = 0;
;
for (; index29 < groupLen29;index29++){
_v.setObject((android.view.View)(group29.Get(index29)));
 //BA.debugLineNum = 139;BA.debugLine="v.Width = 33%x";
_v.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA));
 }
};
 //BA.debugLineNum = 142;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 143;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 144;BA.debugLine="ACToolBarLight1.NavigationIconDrawable = bd";
mostCurrent._actoolbarlight1.setNavigationIconDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 145;BA.debugLine="ToolbarHelper.Initialize";
mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 146;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 147;BA.debugLine="ToolbarHelper.Title= cs.Initialize.Size(22).Appe";
mostCurrent._toolbarhelper.setTitle(BA.ObjectToCharSequence(_cs.Initialize().Size((int) (22)).Append(BA.ObjectToCharSequence("Smart Home Monitor")).PopAll().getObject()));
 //BA.debugLineNum = 148;BA.debugLine="ToolbarHelper.subTitle = \"\"";
mostCurrent._toolbarhelper.setSubtitle(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 149;BA.debugLine="ToolbarHelper.ShowUpIndicator = False 'set to tr";
mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 150;BA.debugLine="ACToolBarLight1.InitMenuListener";
mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 151;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 152;BA.debugLine="Dim xl As XmlLayoutBuilder";
_xl = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 153;BA.debugLine="jo.RunMethod(\"setPopupTheme\", Array(xl.GetResour";
_jo.RunMethod("setPopupTheme",new Object[]{(Object)(_xl.GetResourceId("style","ToolbarMenu"))});
 //BA.debugLineNum = 155;BA.debugLine="GaugeHumidity.SetRanges(Array(GaugeHumidity.Crea";
mostCurrent._gaugehumidity._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (70),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (70),(float) (80),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (80),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 158;BA.debugLine="GaugeTemp.SetRanges(Array(GaugeTemp.CreateRange(";
mostCurrent._gaugetemp._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 161;BA.debugLine="GaugeHeatIndex.SetRanges(Array(GaugeHeatIndex.Cr";
mostCurrent._gaugeheatindex._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 166;BA.debugLine="GaugeDewPoint.SetRanges(Array(GaugeDewPoint.Crea";
mostCurrent._gaugedewpoint._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (60.8),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (60.8),(float) (64.4),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (64.4),(float) (78.8),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (78.8),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 172;BA.debugLine="GaugeAirQuality.SetRanges(Array(GaugeAirQuality.";
mostCurrent._gaugeairquality._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (100),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (100),(float) (400),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (400),(float) (900),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (900),(float) (1000),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 176;BA.debugLine="GaugeAirQuality.CurrentValue=0";
mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 178;BA.debugLine="GaugeHumidityBasement.SetRanges(Array(GaugeHumid";
mostCurrent._gaugehumiditybasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (70),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (70),(float) (80),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (80),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 181;BA.debugLine="GaugeTempBasement.SetRanges(Array(GaugeTempBasem";
mostCurrent._gaugetempbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 184;BA.debugLine="GaugeHeatIndexBasement.SetRanges(Array(GaugeHeat";
mostCurrent._gaugeheatindexbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 189;BA.debugLine="GaugeDewPointBasement.SetRanges(Array(GaugeDewPo";
mostCurrent._gaugedewpointbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (60.8),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (60.8),(float) (64.4),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (64.4),(float) (78.8),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (78.8),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 195;BA.debugLine="GaugeAirQualityBasement.SetRanges(Array(GaugeAir";
mostCurrent._gaugeairqualitybasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (100),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (100),(float) (400),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (400),(float) (900),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (900),(float) (1000),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 199;BA.debugLine="GaugeAirQualityBasement.CurrentValue=0";
mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 201;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 202;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 203;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 204;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"The";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 205;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 206;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 207;BA.debugLine="lblPing.Visible = False";
mostCurrent._lblping.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 208;BA.debugLine="GaugeAirQuality.CurrentValue = 0";
mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 209;BA.debugLine="GaugeAirQualityBasement.CurrentValue = 0";
mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 210;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 211;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.Bol";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).PopAll().getObject()));
 //BA.debugLineNum = 213;BA.debugLine="lblPerceptionBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblperceptionbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 214;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.App";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 215;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 216;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bold.";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 217;BA.debugLine="lblPingBasement.Visible = False";
mostCurrent._lblpingbasement.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 218;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 219;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Initia";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).PopAll().getObject()));
 //BA.debugLineNum = 221;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 222;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Rea";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Read weather","utf8"));
 //BA.debugLineNum = 224;BA.debugLine="Dim strHumidityAddValue As String = StateManage";
_strhumidityaddvalue = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue");
 //BA.debugLineNum = 225;BA.debugLine="If strHumidityAddValue = \"\" Then";
if ((_strhumidityaddvalue).equals("")) { 
 //BA.debugLineNum = 226;BA.debugLine="strHumidityAddValue = \"0\"";
_strhumidityaddvalue = "0";
 };
 //BA.debugLineNum = 228;BA.debugLine="MQTT.Publish(\"HumidityAddValue\", bc.StringToByt";
_mqtt.Publish("HumidityAddValue",_bc.StringToBytes(_strhumidityaddvalue,"utf8"));
 };
 //BA.debugLineNum = 231;BA.debugLine="SetTextShadow(lblDriveway)";
_settextshadow(mostCurrent._lbldriveway);
 //BA.debugLineNum = 232;BA.debugLine="SetTextShadow(lblDrivewayBatt)";
_settextshadow(mostCurrent._lbldrivewaybatt);
 //BA.debugLineNum = 233;BA.debugLine="SetTextShadow(lblDrivewayTimestamp)";
_settextshadow(mostCurrent._lbldrivewaytimestamp);
 //BA.debugLineNum = 234;BA.debugLine="SetTextShadow(lblDrivewayWifi)";
_settextshadow(mostCurrent._lbldrivewaywifi);
 //BA.debugLineNum = 235;BA.debugLine="SetTextShadow(lblFrontDoor)";
_settextshadow(mostCurrent._lblfrontdoor);
 //BA.debugLineNum = 236;BA.debugLine="SetTextShadow(lblFrontDoorBatt)";
_settextshadow(mostCurrent._lblfrontdoorbatt);
 //BA.debugLineNum = 237;BA.debugLine="SetTextShadow(lblFrontDoorTimestamp)";
_settextshadow(mostCurrent._lblfrontdoortimestamp);
 //BA.debugLineNum = 238;BA.debugLine="SetTextShadow(lblFrontDoorWiFi)";
_settextshadow(mostCurrent._lblfrontdoorwifi);
 //BA.debugLineNum = 239;BA.debugLine="SetTextShadow(lblSideYard)";
_settextshadow(mostCurrent._lblsideyard);
 //BA.debugLineNum = 240;BA.debugLine="SetTextShadow(lblSideYardBatt)";
_settextshadow(mostCurrent._lblsideyardbatt);
 //BA.debugLineNum = 241;BA.debugLine="SetTextShadow(lblSideYardTimestamp)";
_settextshadow(mostCurrent._lblsideyardtimestamp);
 //BA.debugLineNum = 242;BA.debugLine="SetTextShadow(lblSideYardWiFi)";
_settextshadow(mostCurrent._lblsideyardwifi);
 //BA.debugLineNum = 243;BA.debugLine="SetTextShadow(lblSyncModule)";
_settextshadow(mostCurrent._lblsyncmodule);
 } 
       catch (Exception e96) {
			processBA.setLastException(e96); //BA.debugLineNum = 249;BA.debugLine="ToastMessageShow(LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 251;BA.debugLine="End Sub";
return "";
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
 //BA.debugLineNum = 529;BA.debugLine="Sub Activity_Createmenu(Menu As ACMenu)";
 //BA.debugLineNum = 530;BA.debugLine="Try";
try { //BA.debugLineNum = 531;BA.debugLine="Menu.Clear";
_menu.Clear();
 //BA.debugLineNum = 532;BA.debugLine="gblACMenu = Menu";
mostCurrent._gblacmenu = _menu;
 //BA.debugLineNum = 533;BA.debugLine="Menu.Add(0, 0, \"Settings\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Settings"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 535;BA.debugLine="Menu.Add(0, 0, \"About\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 } 
       catch (Exception e7) {
			processBA.setLastException(e7); //BA.debugLineNum = 537;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("422675464",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 539;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 615;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 616;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 617;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (mostCurrent._tabstrip1.getCurrentPage()==2) { 
 //BA.debugLineNum = 618;BA.debugLine="TabStrip1.ScrollTo(1,True)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 619;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 //BA.debugLineNum = 620;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==1) { 
 //BA.debugLineNum = 622;BA.debugLine="TabStrip1.ScrollTo(0,True)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 623;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 //BA.debugLineNum = 624;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==3) { 
 //BA.debugLineNum = 626;BA.debugLine="TabStrip1.ScrollTo(2,True)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 627;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 //BA.debugLineNum = 628;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==4) { 
 //BA.debugLineNum = 630;BA.debugLine="TabStrip1.ScrollTo(3,True)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 631;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 //BA.debugLineNum = 632;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 636;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 306;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 308;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _in = null;
String _notificationclicked = "";
 //BA.debugLineNum = 253;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 254;BA.debugLine="Try";
try { //BA.debugLineNum = 255;BA.debugLine="HandleSettings";
_handlesettings();
 //BA.debugLineNum = 256;BA.debugLine="Dim in As Intent = Activity.GetStartingIntent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
_in = mostCurrent._activity.GetStartingIntent();
 //BA.debugLineNum = 257;BA.debugLine="Dim NotificationClicked As String";
_notificationclicked = "";
 //BA.debugLineNum = 258;BA.debugLine="If in.IsInitialized And in <> OldIntent Then";
if (_in.IsInitialized() && (_in).equals(_oldintent) == false) { 
 //BA.debugLineNum = 259;BA.debugLine="OldIntent = in";
_oldintent = _in;
 //BA.debugLineNum = 260;BA.debugLine="If in.HasExtra(\"Notification_Tag\") Then";
if (_in.HasExtra("Notification_Tag")) { 
 //BA.debugLineNum = 261;BA.debugLine="NotificationClicked = in.GetExtra(\"Notificatio";
_notificationclicked = BA.ObjectToString(_in.GetExtra("Notification_Tag"));
 };
 };
 //BA.debugLineNum = 264;BA.debugLine="If NotificationClicked = \"Living area temperatur";
if ((_notificationclicked).equals("Living area temperature")) { 
 //BA.debugLineNum = 265;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 266;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 }else if((_notificationclicked).equals("Living area carbon monoxide")) { 
 //BA.debugLineNum = 268;BA.debugLine="TabStrip1.ScrollTo(1,False)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 269;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 }else if((_notificationclicked).equals("Basement temperature")) { 
 //BA.debugLineNum = 271;BA.debugLine="TabStrip1.ScrollTo(2,False)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 272;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 }else if((_notificationclicked).equals("Basement carbon monoxide")) { 
 //BA.debugLineNum = 274;BA.debugLine="TabStrip1.ScrollTo(3,False)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 275;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 }else if((_notificationclicked).equals("Basement DHT22 sensor issue")) { 
 //BA.debugLineNum = 277;BA.debugLine="TabStrip1.ScrollTo(2,False)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 278;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 }else if((_notificationclicked).equals("Living area DHT22 sensor issue")) { 
 //BA.debugLineNum = 280;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 281;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 }else if((_notificationclicked).equals("Living area CO sensor issue")) { 
 //BA.debugLineNum = 283;BA.debugLine="TabStrip1.ScrollTo(1,False)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 284;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 }else if((_notificationclicked).equals("Basement CO sensor issue")) { 
 //BA.debugLineNum = 286;BA.debugLine="TabStrip1.ScrollTo(3,False)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 287;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 }else {
 //BA.debugLineNum = 289;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 290;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 };
 } 
       catch (Exception e40) {
			processBA.setLastException(e40); //BA.debugLineNum = 294;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("4196649",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 297;BA.debugLine="Try";
try { //BA.debugLineNum = 298;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connected";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 299;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 } 
       catch (Exception e47) {
			processBA.setLastException(e47); //BA.debugLineNum = 302;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("4196657",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 304;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
int _result = 0;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 453;BA.debugLine="Sub ACToolBarLight1_MenuItemClick (Item As ACMenuI";
 //BA.debugLineNum = 454;BA.debugLine="Try";
try { //BA.debugLineNum = 455;BA.debugLine="If Item.Title = \"About\" Then";
if ((_item.getTitle()).equals("About")) { 
 //BA.debugLineNum = 456;BA.debugLine="ShowAboutMenu";
_showaboutmenu();
 }else if((_item.getTitle()).equals("Settings")) { 
 //BA.debugLineNum = 458;BA.debugLine="StartActivity(screen.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_screen.CreateIntent()));
 }else if((_item.getTitle()).equals("Restart board")) { 
 //BA.debugLineNum = 460;BA.debugLine="Try";
try { //BA.debugLineNum = 461;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 462;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 463;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets,";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 464;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (mostCurrent._tabstrip1.getCurrentPage()==2) { 
 //BA.debugLineNum = 465;BA.debugLine="result = Msgbox2(\"Restart the BASEMENT contro";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 466;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 467;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 468;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 470;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringT";
_mqtt.Publish("TempHumidBasement",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else if(mostCurrent._tabstrip1.getCurrentPage()==1) { 
 //BA.debugLineNum = 473;BA.debugLine="result = Msgbox2(\"Restart the AIR QUALITY con";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 474;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 475;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 476;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 478;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Restar";
_mqtt.Publish("MQ7",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else if(mostCurrent._tabstrip1.getCurrentPage()==3) { 
 //BA.debugLineNum = 481;BA.debugLine="result = Msgbox2(\"Restart the BASEMENT AIR QU";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 482;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 483;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 484;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 486;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes";
_mqtt.Publish("MQ7Basement",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else {
 //BA.debugLineNum = 489;BA.debugLine="result = Msgbox2(\"Restart the WEATHER control";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the WEATHER controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 490;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 491;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 492;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 494;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Restart controller","utf8"));
 };
 };
 } 
       catch (Exception e45) {
			processBA.setLastException(e45); //BA.debugLineNum = 498;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("422478893",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 };
 } 
       catch (Exception e49) {
			processBA.setLastException(e49); //BA.debugLineNum = 502;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("422478897",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 504;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper  _blur(anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _bmp) throws Exception{
b4a.example.bitmapcreator _bcbitmap = null;
int _reducescale = 0;
int _count = 0;
b4a.example.bitmapcreator._argbcolor[] _clrs = null;
b4a.example.bitmapcreator._argbcolor _temp = null;
int _m = 0;
int _steps = 0;
int _y = 0;
int _x = 0;
 //BA.debugLineNum = 1567;BA.debugLine="Private Sub Blur (bmp As B4XBitmap) As B4XBitmap";
 //BA.debugLineNum = 1568;BA.debugLine="Dim bcBitmap As BitmapCreator";
_bcbitmap = new b4a.example.bitmapcreator();
 //BA.debugLineNum = 1569;BA.debugLine="Dim ReduceScale As Int = 2";
_reducescale = (int) (2);
 //BA.debugLineNum = 1570;BA.debugLine="bcBitmap.Initialize(bmp.Width / ReduceScale / bmp";
_bcbitmap._initialize(processBA,(int) (_bmp.getWidth()/(double)_reducescale/(double)_bmp.getScale()),(int) (_bmp.getHeight()/(double)_reducescale/(double)_bmp.getScale()));
 //BA.debugLineNum = 1571;BA.debugLine="bcBitmap.CopyPixelsFromBitmap(bmp)";
_bcbitmap._copypixelsfrombitmap(_bmp);
 //BA.debugLineNum = 1572;BA.debugLine="Dim count As Int = 3";
_count = (int) (3);
 //BA.debugLineNum = 1573;BA.debugLine="Dim clrs(3) As ARGBColor";
_clrs = new b4a.example.bitmapcreator._argbcolor[(int) (3)];
{
int d0 = _clrs.length;
for (int i0 = 0;i0 < d0;i0++) {
_clrs[i0] = new b4a.example.bitmapcreator._argbcolor();
}
}
;
 //BA.debugLineNum = 1574;BA.debugLine="Dim temp As ARGBColor";
_temp = new b4a.example.bitmapcreator._argbcolor();
 //BA.debugLineNum = 1575;BA.debugLine="Dim m As Int";
_m = 0;
 //BA.debugLineNum = 1576;BA.debugLine="For steps = 1 To count";
{
final int step9 = 1;
final int limit9 = _count;
_steps = (int) (1) ;
for (;_steps <= limit9 ;_steps = _steps + step9 ) {
 //BA.debugLineNum = 1577;BA.debugLine="For y = 0 To bcBitmap.mHeight - 1";
{
final int step10 = 1;
final int limit10 = (int) (_bcbitmap._mheight-1);
_y = (int) (0) ;
for (;_y <= limit10 ;_y = _y + step10 ) {
 //BA.debugLineNum = 1578;BA.debugLine="For x = 0 To 2";
{
final int step11 = 1;
final int limit11 = (int) (2);
_x = (int) (0) ;
for (;_x <= limit11 ;_x = _x + step11 ) {
 //BA.debugLineNum = 1579;BA.debugLine="bcBitmap.GetARGB(x, y, clrs(x))";
_bcbitmap._getargb(_x,_y,_clrs[_x]);
 }
};
 //BA.debugLineNum = 1581;BA.debugLine="SetAvg(bcBitmap, 1, y, clrs, temp)";
_setavg(_bcbitmap,(int) (1),_y,_clrs,_temp);
 //BA.debugLineNum = 1582;BA.debugLine="m = 0";
_m = (int) (0);
 //BA.debugLineNum = 1583;BA.debugLine="For x = 2 To bcBitmap.mWidth - 2";
{
final int step16 = 1;
final int limit16 = (int) (_bcbitmap._mwidth-2);
_x = (int) (2) ;
for (;_x <= limit16 ;_x = _x + step16 ) {
 //BA.debugLineNum = 1584;BA.debugLine="bcBitmap.GetARGB(x + 1, y, clrs(m))";
_bcbitmap._getargb((int) (_x+1),_y,_clrs[_m]);
 //BA.debugLineNum = 1585;BA.debugLine="m = (m + 1) Mod clrs.Length";
_m = (int) ((_m+1)%_clrs.length);
 //BA.debugLineNum = 1586;BA.debugLine="SetAvg(bcBitmap, x, y, clrs, temp)";
_setavg(_bcbitmap,_x,_y,_clrs,_temp);
 }
};
 }
};
 //BA.debugLineNum = 1589;BA.debugLine="For x = 0 To bcBitmap.mWidth - 1";
{
final int step22 = 1;
final int limit22 = (int) (_bcbitmap._mwidth-1);
_x = (int) (0) ;
for (;_x <= limit22 ;_x = _x + step22 ) {
 //BA.debugLineNum = 1590;BA.debugLine="For y = 0 To 2";
{
final int step23 = 1;
final int limit23 = (int) (2);
_y = (int) (0) ;
for (;_y <= limit23 ;_y = _y + step23 ) {
 //BA.debugLineNum = 1591;BA.debugLine="bcBitmap.GetARGB(x, y, clrs(y))";
_bcbitmap._getargb(_x,_y,_clrs[_y]);
 }
};
 //BA.debugLineNum = 1593;BA.debugLine="SetAvg(bcBitmap, x, 1, clrs, temp)";
_setavg(_bcbitmap,_x,(int) (1),_clrs,_temp);
 //BA.debugLineNum = 1594;BA.debugLine="m = 0";
_m = (int) (0);
 //BA.debugLineNum = 1595;BA.debugLine="For y = 2 To bcBitmap.mHeight - 2";
{
final int step28 = 1;
final int limit28 = (int) (_bcbitmap._mheight-2);
_y = (int) (2) ;
for (;_y <= limit28 ;_y = _y + step28 ) {
 //BA.debugLineNum = 1596;BA.debugLine="bcBitmap.GetARGB(x, y + 1, clrs(m))";
_bcbitmap._getargb(_x,(int) (_y+1),_clrs[_m]);
 //BA.debugLineNum = 1597;BA.debugLine="m = (m + 1) Mod clrs.Length";
_m = (int) ((_m+1)%_clrs.length);
 //BA.debugLineNum = 1598;BA.debugLine="SetAvg(bcBitmap, x, y, clrs, temp)";
_setavg(_bcbitmap,_x,_y,_clrs,_temp);
 }
};
 }
};
 }
};
 //BA.debugLineNum = 1602;BA.debugLine="Return bcBitmap.Bitmap";
if (true) return _bcbitmap._getbitmap();
 //BA.debugLineNum = 1603;BA.debugLine="End Sub";
return null;
}
public static String  _bluriv(String _image,anywheresoftware.b4a.objects.ImageViewWrapper _iv) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _bmp = null;
 //BA.debugLineNum = 1557;BA.debugLine="Sub BlurIV (image As String,iv As ImageView)";
 //BA.debugLineNum = 1558;BA.debugLine="Try";
try { //BA.debugLineNum = 1559;BA.debugLine="Dim bmp As B4XBitmap = xui.LoadBitmapResize(File";
_bmp = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
_bmp = mostCurrent._xui.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_image,_iv.getWidth(),_iv.getHeight(),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1560;BA.debugLine="iv.Bitmap = (Blur(bmp))";
_iv.setBitmap((android.graphics.Bitmap)((_blur(_bmp)).getObject()));
 } 
       catch (Exception e5) {
			processBA.setLastException(e5); //BA.debugLineNum = 1562;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("424707077",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1563;BA.debugLine="ToastMessageShow(\"BlurIV: \" & LastException,Fals";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("BlurIV: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1565;BA.debugLine="End Sub";
return "";
}
public static String  _btndriveway_click() throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 1544;BA.debugLine="Sub btnDriveway_Click";
 //BA.debugLineNum = 1545;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 1546;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"0";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 1547;BA.debugLine="If Msgbox2(\"Refresh all cameras?\", \"Smart Home Mo";
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Refresh all cameras?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 1548;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 1549;BA.debugLine="btnDriveway.Enabled = False";
mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1550;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",mostCurrent._ivdriveway);
 //BA.debugLineNum = 1551;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",mostCurrent._ivfrontdoor);
 //BA.debugLineNum = 1552;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",mostCurrent._ivsideyard);
 //BA.debugLineNum = 1553;BA.debugLine="RefreshCameras(False)";
_refreshcameras(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1555;BA.debugLine="End Sub";
return "";
}
public static String  _checkairqualitysetting() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _status = "";
String[] _a = null;
long _tomorrow = 0L;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 638;BA.debugLine="Sub CheckAirQualitySetting";
 //BA.debugLineNum = 639;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 640;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 641;BA.debugLine="Try";
try { //BA.debugLineNum = 642;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 643;BA.debugLine="status = StateManager.GetSetting(\"AirQuality\")";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"AirQuality");
 //BA.debugLineNum = 644;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 645;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 646;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 647;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 648;BA.debugLine="GaugeAirQuality.CurrentValue = a(0)";
mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (0)])));
 //BA.debugLineNum = 649;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 650;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 652;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 };
 //BA.debugLineNum = 654;BA.debugLine="If a(1) = \"\" Then";
if ((_a[(int) (1)]).equals("")) { 
 //BA.debugLineNum = 655;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 656;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 657;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 658;BA.debugLine="a(1) = DateTime.Date(Tomorrow)";
_a[(int) (1)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 660;BA.debugLine="If a(2).Contains(\"|24:\") Then";
if (_a[(int) (2)].contains("|24:")) { 
 //BA.debugLineNum = 661;BA.debugLine="a(2) = a(2).Replace(\"|24:\",\"|00:\")";
_a[(int) (2)] = _a[(int) (2)].replace("|24:","|00:");
 //BA.debugLineNum = 662;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 663;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 664;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 665;BA.debugLine="a(2) = DateTime.Date(Tomorrow)";
_a[(int) (2)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 667;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 668;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 669;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 670;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 671;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 673;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 674;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 676;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 679;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.B";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e44) {
			processBA.setLastException(e44); //BA.debugLineNum = 683;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("423068717",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 684;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.Bol";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 686;BA.debugLine="End Sub";
return "";
}
public static String  _checkairqualitysettingbasement() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _status = "";
String[] _a = null;
long _tomorrow = 0L;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 688;BA.debugLine="Sub CheckAirQualitySettingBasement";
 //BA.debugLineNum = 689;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 690;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 691;BA.debugLine="Try";
try { //BA.debugLineNum = 692;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 693;BA.debugLine="status = StateManager.GetSetting(\"AirQualityBase";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"AirQualityBasement");
 //BA.debugLineNum = 694;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 695;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 696;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 697;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 698;BA.debugLine="GaugeAirQualityBasement.CurrentValue = a(0)";
mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (0)])));
 //BA.debugLineNum = 699;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 700;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bo";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 702;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bo";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 };
 //BA.debugLineNum = 704;BA.debugLine="If a(1) = \"\" Then";
if ((_a[(int) (1)]).equals("")) { 
 //BA.debugLineNum = 705;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 706;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 707;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 708;BA.debugLine="a(1) = DateTime.Date(Tomorrow)";
_a[(int) (1)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 710;BA.debugLine="If a(2).Contains(\"|24:\") Then";
if (_a[(int) (2)].contains("|24:")) { 
 //BA.debugLineNum = 711;BA.debugLine="a(2) = a(2).Replace(\"|24:\",\"|00:\")";
_a[(int) (2)] = _a[(int) (2)].replace("|24:","|00:");
 //BA.debugLineNum = 712;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 713;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 714;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 715;BA.debugLine="a(2) = DateTime.Date(Tomorrow)";
_a[(int) (2)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 718;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 719;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 720;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 721;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 722;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 724;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 725;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Ini";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 727;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Ini";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 730;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Init";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdatebasement.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e44) {
			processBA.setLastException(e44); //BA.debugLineNum = 734;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("423134254",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 735;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Initia";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 737;BA.debugLine="End Sub";
return "";
}
public static String  _checkbattlife(int _battlevel) throws Exception{
 //BA.debugLineNum = 1474;BA.debugLine="Sub CheckBattLife(battlevel As Int) As String";
 //BA.debugLineNum = 1475;BA.debugLine="Try";
try { //BA.debugLineNum = 1477;BA.debugLine="If battlevel <= 136 Then";
if (_battlevel<=136) { 
 //BA.debugLineNum = 1478;BA.debugLine="Return \"Replace battery now!\"";
if (true) return "Replace battery now!";
 }else if(_battlevel>=160) { 
 //BA.debugLineNum = 1480;BA.debugLine="Return \"Very high\"";
if (true) return "Very high";
 }else if(_battlevel>136 && _battlevel<138) { 
 //BA.debugLineNum = 1482;BA.debugLine="Return \"Very low\"";
if (true) return "Very low";
 }else {
 //BA.debugLineNum = 1484;BA.debugLine="Return \"High\"";
if (true) return "High";
 };
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 1487;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("424379405",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1488;BA.debugLine="ToastMessageShow(\"CheckBattLife: \" & LastExcepti";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("CheckBattLife: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1489;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 1491;BA.debugLine="End Sub";
return "";
}
public static String  _checklfrlevel(int _lfrlevel) throws Exception{
 //BA.debugLineNum = 1493;BA.debugLine="Sub CheckLFRLevel(lfrlevel As Int) As String";
 //BA.debugLineNum = 1494;BA.debugLine="Try";
try { //BA.debugLineNum = 1496;BA.debugLine="If lfrlevel > -67 Then";
if (_lfrlevel>-67) { 
 //BA.debugLineNum = 1497;BA.debugLine="Return \"Amazing\"";
if (true) return "Amazing";
 }else if(_lfrlevel>-70 && _lfrlevel<=-67) { 
 //BA.debugLineNum = 1499;BA.debugLine="Return \"Very good\"";
if (true) return "Very good";
 }else if(_lfrlevel>-80 && _lfrlevel<=-70) { 
 //BA.debugLineNum = 1501;BA.debugLine="Return \"OK\"";
if (true) return "OK";
 }else if(_lfrlevel>-90 && _lfrlevel<=-80) { 
 //BA.debugLineNum = 1503;BA.debugLine="Return \"Not Good\"";
if (true) return "Not Good";
 }else {
 //BA.debugLineNum = 1505;BA.debugLine="Return \"Unusable\"";
if (true) return "Unusable";
 };
 } 
       catch (Exception e14) {
			processBA.setLastException(e14); //BA.debugLineNum = 1508;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("424444943",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1509;BA.debugLine="lblStatus.Text = \"CheckLFRLevel LastException: \"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("CheckLFRLevel LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))));
 //BA.debugLineNum = 1510;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 1512;BA.debugLine="End Sub";
return "";
}
public static String  _checktemphumiditysetting() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _status = "";
String[] _a = null;
long _tomorrow = 0L;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 739;BA.debugLine="Sub CheckTempHumiditySetting";
 //BA.debugLineNum = 740;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 741;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 742;BA.debugLine="Try";
try { //BA.debugLineNum = 743;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 744;BA.debugLine="status = StateManager.GetSetting(\"TempHumidity\")";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"TempHumidity");
 //BA.debugLineNum = 745;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 746;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 747;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 748;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 749;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 750;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 751;BA.debugLine="GaugeTemp.CurrentValue = a(1)";
mostCurrent._gaugetemp._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 752;BA.debugLine="GaugeHumidity.CurrentValue = a(2)";
mostCurrent._gaugehumidity._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 753;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 754;BA.debugLine="If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6)) || (_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 755;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 757;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 759;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 };
 //BA.debugLineNum = 762;BA.debugLine="GaugeHeatIndex.CurrentValue = a(5)";
mostCurrent._gaugeheatindex._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 763;BA.debugLine="GaugeDewPoint.CurrentValue = a(6)";
mostCurrent._gaugedewpoint._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 764;BA.debugLine="If a(7) = \"\" Then";
if ((_a[(int) (7)]).equals("")) { 
 //BA.debugLineNum = 765;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 766;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 767;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 768;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 770;BA.debugLine="If a(8).Contains(\"|24:\") Then";
if (_a[(int) (8)].contains("|24:")) { 
 //BA.debugLineNum = 771;BA.debugLine="a(8) = a(8).Replace(\"|24:\",\"|00:\")";
_a[(int) (8)] = _a[(int) (8)].replace("|24:","|00:");
 //BA.debugLineNum = 772;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 773;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 774;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 775;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 777;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 778;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 779;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 780;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 781;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 783;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 784;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appen";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 786;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appen";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 789;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e52) {
			processBA.setLastException(e52); //BA.debugLineNum = 793;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("423199798",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 794;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 796;BA.debugLine="End Sub";
return "";
}
public static String  _checktemphumiditysettingbasement() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _status = "";
String[] _a = null;
long _tomorrow = 0L;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 798;BA.debugLine="Sub CheckTempHumiditySettingBasement";
 //BA.debugLineNum = 799;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 800;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 801;BA.debugLine="Try";
try { //BA.debugLineNum = 802;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 803;BA.debugLine="status = StateManager.GetSetting(\"TempHumidityBa";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"TempHumidityBasement");
 //BA.debugLineNum = 804;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 805;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 806;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 807;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 808;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 809;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 810;BA.debugLine="GaugeTempBasement.CurrentValue = a(1)";
mostCurrent._gaugetempbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 811;BA.debugLine="GaugeHumidityBasement.CurrentValue = a(2)";
mostCurrent._gaugehumiditybasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 812;BA.debugLine="lblPerceptionBasement.Text = cs.Initialize.Bol";
mostCurrent._lblperceptionbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 813;BA.debugLine="If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6)) || (_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 814;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 816;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 818;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 };
 //BA.debugLineNum = 820;BA.debugLine="GaugeHeatIndexBasement.CurrentValue = a(5)";
mostCurrent._gaugeheatindexbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 821;BA.debugLine="GaugeDewPointBasement.CurrentValue = a(6)";
mostCurrent._gaugedewpointbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 822;BA.debugLine="If a(7) = \"\" Then";
if ((_a[(int) (7)]).equals("")) { 
 //BA.debugLineNum = 823;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 824;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 825;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 826;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 828;BA.debugLine="If a(8).Contains(\"|24:\") Then";
if (_a[(int) (8)].contains("|24:")) { 
 //BA.debugLineNum = 829;BA.debugLine="a(8) = a(8).Replace(\"|24:\",\"|00:\")";
_a[(int) (8)] = _a[(int) (8)].replace("|24:","|00:");
 //BA.debugLineNum = 830;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 831;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 832;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 833;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 835;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 836;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 837;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 838;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 839;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 841;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 842;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bo";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 844;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bo";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 847;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bol";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdatebasement.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e52) {
			processBA.setLastException(e52); //BA.debugLineNum = 851;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("423265333",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 852;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bold.";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 854;BA.debugLine="End Sub";
return "";
}
public static String  _convertdatetime(String _inputtime) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
 //BA.debugLineNum = 1514;BA.debugLine="Sub ConvertDateTime(inputTime As String) As String";
 //BA.debugLineNum = 1516;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 1517;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 1518;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 1521;BA.debugLine="Return DateTime.Date(lngTicks)";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 //BA.debugLineNum = 1522;BA.debugLine="End Sub";
return "";
}
public static String  _createpreferencescreen() throws Exception{
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat1 = null;
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat2 = null;
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat3 = null;
anywheresoftware.b4a.objects.IntentWrapper _in = null;
 //BA.debugLineNum = 856;BA.debugLine="Sub CreatePreferenceScreen";
 //BA.debugLineNum = 857;BA.debugLine="screen.Initialize(\"Settings\", \"\")";
_screen.Initialize("Settings","");
 //BA.debugLineNum = 859;BA.debugLine="Dim cat1,cat2,cat3 As AHPreferenceCategory";
_cat1 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat2 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat3 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
 //BA.debugLineNum = 861;BA.debugLine="cat1.Initialize(\"Temperature & Humidity\")";
_cat1.Initialize("Temperature & Humidity");
 //BA.debugLineNum = 862;BA.debugLine="cat1.AddEditText(\"TempHumidityCooldownTime\", \"Liv";
_cat1.AddEditText("TempHumidityCooldownTime","Living Area Cooldown Time","Minimum creation time interval between new notification","5","");
 //BA.debugLineNum = 863;BA.debugLine="cat1.AddEditText(\"TempHumidityCooldownTimeBasemen";
_cat1.AddEditText("TempHumidityCooldownTimeBasement","Basement Cooldown Time","Minimum creation time interval between new notification","5","");
 //BA.debugLineNum = 864;BA.debugLine="cat1.AddEditText(\"HumidityAddValue\", \"Humidity Ad";
_cat1.AddEditText("HumidityAddValue","Humidity Additional Value","Value to be added to humidity to improve accuracy","0","");
 //BA.debugLineNum = 866;BA.debugLine="cat2.Initialize(\"Special Settings\")";
_cat2.Initialize("Special Settings");
 //BA.debugLineNum = 867;BA.debugLine="Dim In As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 868;BA.debugLine="In.Initialize(\"android.settings.ACTION_NOTIFICATI";
_in.Initialize("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS","");
 //BA.debugLineNum = 869;BA.debugLine="cat2.AddIntent(\"Notification Access\", \"Enable or";
_cat2.AddIntent("Notification Access","Enable or disable listening to notifications",(android.content.Intent)(_in.getObject()),BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 871;BA.debugLine="cat3.Initialize(\"Sensors\")";
_cat3.Initialize("Sensors");
 //BA.debugLineNum = 872;BA.debugLine="cat3.AddEditText(\"SensorNotRespondingTime\", \"Sens";
_cat3.AddEditText("SensorNotRespondingTime","Sensor Not Responding","Data age when to restart sensor","5","");
 //BA.debugLineNum = 874;BA.debugLine="screen.AddPreferenceCategory(cat2)";
_screen.AddPreferenceCategory(_cat2);
 //BA.debugLineNum = 875;BA.debugLine="screen.AddPreferenceCategory(cat1)";
_screen.AddPreferenceCategory(_cat1);
 //BA.debugLineNum = 876;BA.debugLine="screen.AddPreferenceCategory(cat3)";
_screen.AddPreferenceCategory(_cat3);
 //BA.debugLineNum = 877;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTime","5");
 //BA.debugLineNum = 878;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 879;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",\"0\")";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue","0");
 //BA.debugLineNum = 880;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime","5");
 //BA.debugLineNum = 881;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 882;BA.debugLine="End Sub";
return "";
}
public static void  _downloadimage(String _link,anywheresoftware.b4a.objects.ImageViewWrapper _iv,String _camera) throws Exception{
ResumableSub_DownloadImage rsub = new ResumableSub_DownloadImage(null,_link,_iv,_camera);
rsub.resume(processBA, null);
}
public static class ResumableSub_DownloadImage extends BA.ResumableSub {
public ResumableSub_DownloadImage(cloyd.smart.home.monitor.main parent,String _link,anywheresoftware.b4a.objects.ImageViewWrapper _iv,String _camera) {
this.parent = parent;
this._link = _link;
this._iv = _iv;
this._camera = _camera;
}
cloyd.smart.home.monitor.main parent;
String _link;
anywheresoftware.b4a.objects.ImageViewWrapper _iv;
String _camera;
cloyd.smart.home.monitor.httpjob _j = null;

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
 //BA.debugLineNum = 1138;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 12;
this.catchState = 11;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 11;
 //BA.debugLineNum = 1139;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1140;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1141;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1142;BA.debugLine="j.Download(Link)";
_j._download /*String*/ (_link);
 //BA.debugLineNum = 1143;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1144;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 13;
return;
case 13:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1145;BA.debugLine="If j.Success Then";
if (true) break;

case 4:
//if
this.state = 9;
if (_j._success /*boolean*/ ) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 1159;BA.debugLine="iv.Bitmap = j.GetBitmap";
_iv.setBitmap((android.graphics.Bitmap)(_j._getbitmap /*anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper*/ ().getObject()));
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1161;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1162;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;

case 9:
//C
this.state = 12;
;
 //BA.debugLineNum = 1164;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 11:
//C
this.state = 12;
this.catchState = 0;
 //BA.debugLineNum = 1166;BA.debugLine="ToastMessageShow(\"DownloadImage: \" & LastExcepti";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("DownloadImage: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1167;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("423789598",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 12:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1169;BA.debugLine="End Sub";
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
public static void  _jobdone(cloyd.smart.home.monitor.httpjob _j) throws Exception{
}
public static String  _getairquality(int _number) throws Exception{
 //BA.debugLineNum = 377;BA.debugLine="Sub GetAirQuality(number As Int) As String";
 //BA.debugLineNum = 380;BA.debugLine="If number <= 100 Then";
if (_number<=100) { 
 //BA.debugLineNum = 381;BA.debugLine="Return(\"Carbon monoxide perfect\")";
if (true) return ("Carbon monoxide perfect");
 }else if(((_number>100) && (_number<400)) || _number==400) { 
 //BA.debugLineNum = 383;BA.debugLine="Return(\"Carbon monoxide normal\")";
if (true) return ("Carbon monoxide normal");
 }else if(((_number>400) && (_number<900)) || _number==900) { 
 //BA.debugLineNum = 385;BA.debugLine="Return(\"Carbon monoxide high\")";
if (true) return ("Carbon monoxide high");
 }else if(_number>900) { 
 //BA.debugLineNum = 387;BA.debugLine="Return(\"ALARM Carbon monoxide very high\")";
if (true) return ("ALARM Carbon monoxide very high");
 }else {
 //BA.debugLineNum = 389;BA.debugLine="Return(\"MQ-7 - cant read any value - check the s";
if (true) return ("MQ-7 - cant read any value - check the sensor!");
 };
 //BA.debugLineNum = 391;BA.debugLine="End Sub";
return "";
}
public static String  _getallcameras(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.List _networks = null;
anywheresoftware.b4a.objects.collections.Map _colnetworks = null;
 //BA.debugLineNum = 1230;BA.debugLine="Sub GetAllCameras(json As String)";
 //BA.debugLineNum = 1231;BA.debugLine="Try";
try { //BA.debugLineNum = 1232;BA.debugLine="lblStatus.Text = \"Getting all cameras informatio";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Getting all cameras information..."));
 //BA.debugLineNum = 1233;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1234;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1235;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1239;BA.debugLine="Dim networks As List = root.Get(\"networks\")";
_networks = new anywheresoftware.b4a.objects.collections.List();
_networks.setObject((java.util.List)(_root.Get((Object)("networks"))));
 //BA.debugLineNum = 1240;BA.debugLine="For Each colnetworks As Map In networks";
_colnetworks = new anywheresoftware.b4a.objects.collections.Map();
{
final anywheresoftware.b4a.BA.IterableList group7 = _networks;
final int groupLen7 = group7.getSize()
;int index7 = 0;
;
for (; index7 < groupLen7;index7++){
_colnetworks.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(group7.Get(index7)));
 //BA.debugLineNum = 1249;BA.debugLine="networkID = colnetworks.Get(\"network_id\")";
_networkid = BA.ObjectToString(_colnetworks.Get((Object)("network_id")));
 //BA.debugLineNum = 1250;BA.debugLine="Log(\"networkID: \" &  networkID)";
anywheresoftware.b4a.keywords.Common.LogImpl("423986196","networkID: "+_networkid,0);
 }
};
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 1254;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("423986200",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1255;BA.debugLine="ToastMessageShow(\"GetAllCameras: \" & LastExcepti";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("GetAllCameras: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1257;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _getalltablabels(anywheresoftware.b4a.objects.TabStripViewPager _tabstrip) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
anywheresoftware.b4a.objects.PanelWrapper _tc = null;
anywheresoftware.b4a.objects.collections.List _res = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
 //BA.debugLineNum = 602;BA.debugLine="Public Sub GetAllTabLabels (tabstrip As TabStrip)";
 //BA.debugLineNum = 603;BA.debugLine="Dim jo As JavaObject = tabstrip";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_tabstrip));
 //BA.debugLineNum = 604;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 605;BA.debugLine="r.Target = jo.GetField(\"tabStrip\")";
_r.Target = _jo.GetField("tabStrip");
 //BA.debugLineNum = 606;BA.debugLine="Dim tc As Panel = r.GetField(\"tabsContainer\")";
_tc = new anywheresoftware.b4a.objects.PanelWrapper();
_tc.setObject((android.view.ViewGroup)(_r.GetField("tabsContainer")));
 //BA.debugLineNum = 607;BA.debugLine="Dim res As List";
_res = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 608;BA.debugLine="res.Initialize";
_res.Initialize();
 //BA.debugLineNum = 609;BA.debugLine="For Each v As View In tc";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group7 = _tc;
final int groupLen7 = group7.getSize()
;int index7 = 0;
;
for (; index7 < groupLen7;index7++){
_v.setObject((android.view.View)(group7.Get(index7)));
 //BA.debugLineNum = 610;BA.debugLine="If v Is Label Then res.Add(v)";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
_res.Add((Object)(_v.getObject()));};
 }
};
 //BA.debugLineNum = 612;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 613;BA.debugLine="End Sub";
return null;
}
public static String  _getauthinfo(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.Map _authtokenmap = null;
anywheresoftware.b4a.objects.collections.Map _region = null;
 //BA.debugLineNum = 1172;BA.debugLine="Sub GetAuthInfo(json As String)";
 //BA.debugLineNum = 1173;BA.debugLine="Try";
try { //BA.debugLineNum = 1174;BA.debugLine="lblStatus.Text = \"Getting authtoken...\"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Getting authtoken..."));
 //BA.debugLineNum = 1175;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1176;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1177;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1178;BA.debugLine="Dim authtokenmap As Map = root.Get(\"authtoken\")";
_authtokenmap = new anywheresoftware.b4a.objects.collections.Map();
_authtokenmap.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("authtoken"))));
 //BA.debugLineNum = 1179;BA.debugLine="authToken = authtokenmap.Get(\"authtoken\")";
_authtoken = BA.ObjectToString(_authtokenmap.Get((Object)("authtoken")));
 //BA.debugLineNum = 1180;BA.debugLine="Log(\"authToken: \" &  authToken)";
anywheresoftware.b4a.keywords.Common.LogImpl("423855112","authToken: "+_authtoken,0);
 //BA.debugLineNum = 1188;BA.debugLine="Dim region As Map = root.Get(\"region\")";
_region = new anywheresoftware.b4a.objects.collections.Map();
_region.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("region"))));
 //BA.debugLineNum = 1189;BA.debugLine="userRegion = region.GetKeyAt(0)";
_userregion = BA.ObjectToString(_region.GetKeyAt((int) (0)));
 //BA.debugLineNum = 1190;BA.debugLine="Log(\"userRegion: \" &  userRegion)";
anywheresoftware.b4a.keywords.Common.LogImpl("423855122","userRegion: "+_userregion,0);
 } 
       catch (Exception e13) {
			processBA.setLastException(e13); //BA.debugLineNum = 1195;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("423855127",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1196;BA.debugLine="ToastMessageShow(\"GetAuthInfo: \" & LastException";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("GetAuthInfo: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1199;BA.debugLine="End Sub";
return "";
}
public static String  _getcamerainfo(String _json,b4a.example3.customlistview _clv) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.Map _camera_status = null;
int _total_108_wakeups = 0;
int _battery_voltage = 0;
String _light_sensor_data_valid = "";
int _lfr_tb_wakeups = 0;
String _fw_git_hash = "";
int _wifi_strength = 0;
int _lfr_strength = 0;
int _total_tb_wakeups = 0;
String _created_at = "";
int _light_sensor_ch0 = 0;
String _mac = "";
int _unit_number = 0;
String _updated_at = "";
int _light_sensor_ch1 = 0;
int _time_dhcp_lease = 0;
int _temperature = 0;
int _time_first_video = 0;
int _time_dns_resolve = 0;
int _id = 0;
String _temp_alert_status = "";
int _time_108_boot = 0;
int _lfr_108_wakeups = 0;
int _lifetime_duration = 0;
int _wifi_connect_failure_count = 0;
int _camera_id = 0;
String _battery_alert_status = "";
int _dhcp_failure_count = 0;
String _ip_address = "";
String _ipv = "";
String _light_sensor_data_new = "";
int _network_id = 0;
int _account_id = 0;
String _serial = "";
int _dev_1 = 0;
int _time_wlan_connect = 0;
int _dev_2 = 0;
int _socket_failure_count = 0;
int _dev_3 = 0;
int _pir_rejections = 0;
int _sync_module_id = 0;
int _lifetime_count = 0;
int _error_codes = 0;
String _fw_version = "";
String _ac_power = "";
 //BA.debugLineNum = 1347;BA.debugLine="Sub GetCameraInfo(json As String, clv As CustomLis";
 //BA.debugLineNum = 1348;BA.debugLine="Try";
try { //BA.debugLineNum = 1349;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1350;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1351;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1352;BA.debugLine="Dim camera_status As Map = root.Get(\"camera_stat";
_camera_status = new anywheresoftware.b4a.objects.collections.Map();
_camera_status.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("camera_status"))));
 //BA.debugLineNum = 1353;BA.debugLine="Dim total_108_wakeups As Int = camera_status.Get";
_total_108_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("total_108_wakeups"))));
 //BA.debugLineNum = 1354;BA.debugLine="Dim battery_voltage As Int = camera_status.Get(\"";
_battery_voltage = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("battery_voltage"))));
 //BA.debugLineNum = 1355;BA.debugLine="Dim light_sensor_data_valid As String = camera_s";
_light_sensor_data_valid = BA.ObjectToString(_camera_status.Get((Object)("light_sensor_data_valid")));
 //BA.debugLineNum = 1356;BA.debugLine="Dim lfr_tb_wakeups As Int = camera_status.Get(\"l";
_lfr_tb_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lfr_tb_wakeups"))));
 //BA.debugLineNum = 1357;BA.debugLine="Dim fw_git_hash As String = camera_status.Get(\"f";
_fw_git_hash = BA.ObjectToString(_camera_status.Get((Object)("fw_git_hash")));
 //BA.debugLineNum = 1358;BA.debugLine="Dim wifi_strength As Int = camera_status.Get(\"wi";
_wifi_strength = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("wifi_strength"))));
 //BA.debugLineNum = 1359;BA.debugLine="Dim lfr_strength As Int = camera_status.Get(\"lfr";
_lfr_strength = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lfr_strength"))));
 //BA.debugLineNum = 1360;BA.debugLine="Dim total_tb_wakeups As Int = camera_status.Get(";
_total_tb_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("total_tb_wakeups"))));
 //BA.debugLineNum = 1361;BA.debugLine="Dim created_at As String = camera_status.Get(\"cr";
_created_at = BA.ObjectToString(_camera_status.Get((Object)("created_at")));
 //BA.debugLineNum = 1362;BA.debugLine="Dim light_sensor_ch0 As Int = camera_status.Get(";
_light_sensor_ch0 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("light_sensor_ch0"))));
 //BA.debugLineNum = 1363;BA.debugLine="Dim mac As String = camera_status.Get(\"mac\")";
_mac = BA.ObjectToString(_camera_status.Get((Object)("mac")));
 //BA.debugLineNum = 1364;BA.debugLine="Dim unit_number As Int = camera_status.Get(\"unit";
_unit_number = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("unit_number"))));
 //BA.debugLineNum = 1365;BA.debugLine="Dim updated_at As String = camera_status.Get(\"up";
_updated_at = BA.ObjectToString(_camera_status.Get((Object)("updated_at")));
 //BA.debugLineNum = 1366;BA.debugLine="Dim light_sensor_ch1 As Int = camera_status.Get(";
_light_sensor_ch1 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("light_sensor_ch1"))));
 //BA.debugLineNum = 1367;BA.debugLine="Dim time_dhcp_lease As Int = camera_status.Get(\"";
_time_dhcp_lease = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_dhcp_lease"))));
 //BA.debugLineNum = 1368;BA.debugLine="Dim temperature As Int = camera_status.Get(\"temp";
_temperature = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("temperature"))));
 //BA.debugLineNum = 1369;BA.debugLine="Dim time_first_video As Int = camera_status.Get(";
_time_first_video = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_first_video"))));
 //BA.debugLineNum = 1370;BA.debugLine="Dim time_dns_resolve As Int = camera_status.Get(";
_time_dns_resolve = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_dns_resolve"))));
 //BA.debugLineNum = 1371;BA.debugLine="Dim id As Int = camera_status.Get(\"id\")";
_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("id"))));
 //BA.debugLineNum = 1372;BA.debugLine="Dim temp_alert_status As String = camera_status.";
_temp_alert_status = BA.ObjectToString(_camera_status.Get((Object)("temp_alert_status")));
 //BA.debugLineNum = 1373;BA.debugLine="Dim time_108_boot As Int = camera_status.Get(\"ti";
_time_108_boot = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_108_boot"))));
 //BA.debugLineNum = 1374;BA.debugLine="Dim lfr_108_wakeups As Int = camera_status.Get(\"";
_lfr_108_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lfr_108_wakeups"))));
 //BA.debugLineNum = 1375;BA.debugLine="cameraThumbnail = camera_status.Get(\"thumbnail\")";
_camerathumbnail = BA.ObjectToString(_camera_status.Get((Object)("thumbnail")));
 //BA.debugLineNum = 1376;BA.debugLine="Log(\"cameraThumbnail: \" & cameraThumbnail)";
anywheresoftware.b4a.keywords.Common.LogImpl("424248349","cameraThumbnail: "+_camerathumbnail,0);
 //BA.debugLineNum = 1377;BA.debugLine="Dim lifetime_duration As Int = camera_status.Get";
_lifetime_duration = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lifetime_duration"))));
 //BA.debugLineNum = 1378;BA.debugLine="Dim wifi_connect_failure_count As Int = camera_s";
_wifi_connect_failure_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("wifi_connect_failure_count"))));
 //BA.debugLineNum = 1379;BA.debugLine="Dim camera_id As Int = camera_status.Get(\"camera";
_camera_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("camera_id"))));
 //BA.debugLineNum = 1380;BA.debugLine="Dim battery_alert_status As String = camera_stat";
_battery_alert_status = BA.ObjectToString(_camera_status.Get((Object)("battery_alert_status")));
 //BA.debugLineNum = 1381;BA.debugLine="Dim dhcp_failure_count As Int = camera_status.Ge";
_dhcp_failure_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dhcp_failure_count"))));
 //BA.debugLineNum = 1382;BA.debugLine="Dim ip_address As String = camera_status.Get(\"ip";
_ip_address = BA.ObjectToString(_camera_status.Get((Object)("ip_address")));
 //BA.debugLineNum = 1383;BA.debugLine="Dim ipv As String = camera_status.Get(\"ipv\")";
_ipv = BA.ObjectToString(_camera_status.Get((Object)("ipv")));
 //BA.debugLineNum = 1384;BA.debugLine="Dim light_sensor_data_new As String = camera_sta";
_light_sensor_data_new = BA.ObjectToString(_camera_status.Get((Object)("light_sensor_data_new")));
 //BA.debugLineNum = 1385;BA.debugLine="Dim network_id As Int = camera_status.Get(\"netwo";
_network_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("network_id"))));
 //BA.debugLineNum = 1386;BA.debugLine="Dim account_id As Int = camera_status.Get(\"accou";
_account_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("account_id"))));
 //BA.debugLineNum = 1387;BA.debugLine="Dim serial As String = camera_status.Get(\"serial";
_serial = BA.ObjectToString(_camera_status.Get((Object)("serial")));
 //BA.debugLineNum = 1388;BA.debugLine="Dim dev_1 As Int = camera_status.Get(\"dev_1\")";
_dev_1 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dev_1"))));
 //BA.debugLineNum = 1389;BA.debugLine="Dim time_wlan_connect As Int = camera_status.Get";
_time_wlan_connect = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_wlan_connect"))));
 //BA.debugLineNum = 1390;BA.debugLine="Dim dev_2 As Int = camera_status.Get(\"dev_2\")";
_dev_2 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dev_2"))));
 //BA.debugLineNum = 1391;BA.debugLine="Dim socket_failure_count As Int = camera_status.";
_socket_failure_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("socket_failure_count"))));
 //BA.debugLineNum = 1392;BA.debugLine="Dim dev_3 As Int = camera_status.Get(\"dev_3\")";
_dev_3 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dev_3"))));
 //BA.debugLineNum = 1393;BA.debugLine="Dim pir_rejections As Int = camera_status.Get(\"p";
_pir_rejections = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("pir_rejections"))));
 //BA.debugLineNum = 1394;BA.debugLine="Dim sync_module_id As Int = camera_status.Get(\"s";
_sync_module_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("sync_module_id"))));
 //BA.debugLineNum = 1395;BA.debugLine="Dim lifetime_count As Int = camera_status.Get(\"l";
_lifetime_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lifetime_count"))));
 //BA.debugLineNum = 1396;BA.debugLine="Dim error_codes As Int = camera_status.Get(\"erro";
_error_codes = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("error_codes"))));
 //BA.debugLineNum = 1397;BA.debugLine="Dim fw_version As String = camera_status.Get(\"fw";
_fw_version = BA.ObjectToString(_camera_status.Get((Object)("fw_version")));
 //BA.debugLineNum = 1398;BA.debugLine="Dim ac_power As String = camera_status.Get(\"ac_p";
_ac_power = BA.ObjectToString(_camera_status.Get((Object)("ac_power")));
 //BA.debugLineNum = 1404;BA.debugLine="If camera_id = \"347574\" Then";
if (_camera_id==(double)(Double.parseDouble("347574"))) { 
 //BA.debugLineNum = 1405;BA.debugLine="lblDrivewayBatt.Text = \"Batt: \" & NumberFormat2";
mostCurrent._lbldrivewaybatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V - "+_checkbattlife(_battery_voltage)));
 //BA.debugLineNum = 1406;BA.debugLine="lblDrivewayTimestamp.Text = ConvertDateTime(upd";
mostCurrent._lbldrivewaytimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1407;BA.debugLine="lblDriveway.Text = \"Driveway v\" & fw_version";
mostCurrent._lbldriveway.setText(BA.ObjectToCharSequence("Driveway v"+_fw_version));
 //BA.debugLineNum = 1408;BA.debugLine="lblDrivewayWifi.Text = \"WiFi: \" & wifi_strength";
mostCurrent._lbldrivewaywifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 }else if(_camera_id==(double)(Double.parseDouble("236967"))) { 
 //BA.debugLineNum = 1410;BA.debugLine="lblFrontDoorBatt.Text = \"Batt: \" & NumberFormat";
mostCurrent._lblfrontdoorbatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V - "+_checkbattlife(_battery_voltage)));
 //BA.debugLineNum = 1411;BA.debugLine="lblFrontDoorTimestamp.Text = ConvertDateTime(up";
mostCurrent._lblfrontdoortimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1412;BA.debugLine="lblFrontDoor.Text = \"Front Door v\" & fw_version";
mostCurrent._lblfrontdoor.setText(BA.ObjectToCharSequence("Front Door v"+_fw_version));
 //BA.debugLineNum = 1413;BA.debugLine="lblFrontDoorWiFi.Text = \"WiFi: \" & wifi_strengt";
mostCurrent._lblfrontdoorwifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 }else if(_camera_id==(double)(Double.parseDouble("226821"))) { 
 //BA.debugLineNum = 1415;BA.debugLine="lblSideYardBatt.Text = \"Batt: \" & NumberFormat2";
mostCurrent._lblsideyardbatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V - "+_checkbattlife(_battery_voltage)));
 //BA.debugLineNum = 1416;BA.debugLine="lblSideYardTimestamp.Text = ConvertDateTime(upd";
mostCurrent._lblsideyardtimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1417;BA.debugLine="lblSideYard.Text = \"Side Yard v\" & fw_version";
mostCurrent._lblsideyard.setText(BA.ObjectToCharSequence("Side Yard v"+_fw_version));
 //BA.debugLineNum = 1418;BA.debugLine="lblSideYardWiFi.Text = \"WiFi: \" & wifi_strength";
mostCurrent._lblsideyardwifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 };
 } 
       catch (Exception e69) {
			processBA.setLastException(e69); //BA.debugLineNum = 1432;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("424248405",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1433;BA.debugLine="ToastMessageShow(\"GetCameraInfo: \" & LastExcepti";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("GetCameraInfo: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1435;BA.debugLine="End Sub";
return "";
}
public static String  _getcomfort(String _dht11comfortstatus) throws Exception{
String _localcomfortstatus = "";
 //BA.debugLineNum = 426;BA.debugLine="Sub GetComfort(DHT11ComfortStatus As String) As St";
 //BA.debugLineNum = 427;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = "";
 //BA.debugLineNum = 428;BA.debugLine="Select Case DHT11ComfortStatus";
switch (BA.switchObjectToInt(_dht11comfortstatus,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(8),BA.NumberToString(9),BA.NumberToString(10))) {
case 0: {
 //BA.debugLineNum = 430;BA.debugLine="localcomfortstatus = \"OK\"";
_localcomfortstatus = "OK";
 break; }
case 1: {
 //BA.debugLineNum = 432;BA.debugLine="localcomfortstatus = \"Too hot\"";
_localcomfortstatus = "Too hot";
 break; }
case 2: {
 //BA.debugLineNum = 434;BA.debugLine="localcomfortstatus = \"Too cold\"";
_localcomfortstatus = "Too cold";
 break; }
case 3: {
 //BA.debugLineNum = 436;BA.debugLine="localcomfortstatus = \"Too dry\"";
_localcomfortstatus = "Too dry";
 break; }
case 4: {
 //BA.debugLineNum = 438;BA.debugLine="localcomfortstatus = \"Hot and dry\"";
_localcomfortstatus = "Hot and dry";
 break; }
case 5: {
 //BA.debugLineNum = 440;BA.debugLine="localcomfortstatus = \"Cold and dry\"";
_localcomfortstatus = "Cold and dry";
 break; }
case 6: {
 //BA.debugLineNum = 442;BA.debugLine="localcomfortstatus = \"Too humid\"";
_localcomfortstatus = "Too humid";
 break; }
case 7: {
 //BA.debugLineNum = 444;BA.debugLine="localcomfortstatus = \"Hot and humid\"";
_localcomfortstatus = "Hot and humid";
 break; }
case 8: {
 //BA.debugLineNum = 446;BA.debugLine="localcomfortstatus = \"Cold and humid\"";
_localcomfortstatus = "Cold and humid";
 break; }
default: {
 //BA.debugLineNum = 448;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = "Unknown";
 break; }
}
;
 //BA.debugLineNum = 450;BA.debugLine="Return localcomfortstatus";
if (true) return _localcomfortstatus;
 //BA.debugLineNum = 451;BA.debugLine="End Sub";
return "";
}
public static String  _getcommandid(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
 //BA.debugLineNum = 1259;BA.debugLine="Sub GetCommandID(json As String)";
 //BA.debugLineNum = 1260;BA.debugLine="Try";
try { //BA.debugLineNum = 1261;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1262;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1263;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1278;BA.debugLine="commandID = root.Get(\"id\")";
_commandid = BA.ObjectToString(_root.Get((Object)("id")));
 //BA.debugLineNum = 1279;BA.debugLine="Log(\"commandID: \" & commandID)";
anywheresoftware.b4a.keywords.Common.LogImpl("424051732","commandID: "+_commandid,0);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 1301;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("424051754",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1302;BA.debugLine="ToastMessageShow(\"GetCommandID: \" & LastExceptio";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("GetCommandID: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1304;BA.debugLine="End Sub";
return "";
}
public static String  _getcommandstatus(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
 //BA.debugLineNum = 1306;BA.debugLine="Sub GetCommandStatus(json As String)";
 //BA.debugLineNum = 1307;BA.debugLine="Try";
try { //BA.debugLineNum = 1308;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1309;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1310;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1311;BA.debugLine="commandComplete = root.Get(\"complete\")";
_commandcomplete = BA.ObjectToBoolean(_root.Get((Object)("complete")));
 //BA.debugLineNum = 1312;BA.debugLine="Log(\"commandComplete: \" & commandComplete)";
anywheresoftware.b4a.keywords.Common.LogImpl("424117254","commandComplete: "+BA.ObjectToString(_commandcomplete),0);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 1314;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("424117256",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1315;BA.debugLine="ToastMessageShow(\"GetCommandStatus: \" & LastExce";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("GetCommandStatus: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1317;BA.debugLine="End Sub";
return "";
}
public static String  _getperception(String _dht11perception) throws Exception{
String _localperception = "";
 //BA.debugLineNum = 393;BA.debugLine="Sub GetPerception(DHT11Perception As String) As St";
 //BA.debugLineNum = 404;BA.debugLine="Dim localperception As String";
_localperception = "";
 //BA.debugLineNum = 405;BA.debugLine="Select Case DHT11Perception";
switch (BA.switchObjectToInt(_dht11perception,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(7))) {
case 0: {
 //BA.debugLineNum = 407;BA.debugLine="localperception = \"Feels like the western US, a";
_localperception = "Feels like the western US, a bit dry to some";
 break; }
case 1: {
 //BA.debugLineNum = 409;BA.debugLine="localperception = \"Very comfortable\"";
_localperception = "Very comfortable";
 break; }
case 2: {
 //BA.debugLineNum = 411;BA.debugLine="localperception = \"Comfortable\"";
_localperception = "Comfortable";
 break; }
case 3: {
 //BA.debugLineNum = 413;BA.debugLine="localperception = \"OK but humidity is at upper";
_localperception = "OK but humidity is at upper limit";
 break; }
case 4: {
 //BA.debugLineNum = 415;BA.debugLine="localperception = \"Uncomfortable and the humidi";
_localperception = "Uncomfortable and the humidity is at upper limit";
 break; }
case 5: {
 //BA.debugLineNum = 417;BA.debugLine="localperception = \"Very humid, quite uncomforta";
_localperception = "Very humid, quite uncomfortable";
 break; }
case 6: {
 //BA.debugLineNum = 419;BA.debugLine="localperception = \"Extremely uncomfortable, opp";
_localperception = "Extremely uncomfortable, oppressive";
 break; }
case 7: {
 //BA.debugLineNum = 421;BA.debugLine="localperception = \"Severely high, even deadly f";
_localperception = "Severely high, even deadly for asthma related illnesses";
 break; }
}
;
 //BA.debugLineNum = 423;BA.debugLine="Return localperception";
if (true) return _localperception;
 //BA.debugLineNum = 424;BA.debugLine="End Sub";
return "";
}
public static String  _getresterror(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
int _code = 0;
String _message = "";
 //BA.debugLineNum = 1319;BA.debugLine="Sub GetRESTError(json As String) As String";
 //BA.debugLineNum = 1320;BA.debugLine="Try";
try { //BA.debugLineNum = 1324;BA.debugLine="If json.Contains(\"<h1>Not Found</h1>\") Then";
if (_json.contains("<h1>Not Found</h1>")) { 
 //BA.debugLineNum = 1325;BA.debugLine="Return \"REST endpoint URL not found. Try again.";
if (true) return "REST endpoint URL not found. Try again.";
 }else {
 //BA.debugLineNum = 1327;BA.debugLine="If json.IndexOf(\"{\") <> -1 Then";
if (_json.indexOf("{")!=-1) { 
 //BA.debugLineNum = 1328;BA.debugLine="json = json.SubString(json.IndexOf(\"{\"))";
_json = _json.substring(_json.indexOf("{"));
 //BA.debugLineNum = 1329;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1330;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1331;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1332;BA.debugLine="Dim code As Int = root.Get(\"code\")";
_code = (int)(BA.ObjectToNumber(_root.Get((Object)("code"))));
 //BA.debugLineNum = 1333;BA.debugLine="Dim message As String = root.Get(\"message\")";
_message = BA.ObjectToString(_root.Get((Object)("message")));
 //BA.debugLineNum = 1334;BA.debugLine="Log(\"Code: \" & code & \" Message: \" & message)";
anywheresoftware.b4a.keywords.Common.LogImpl("424182799","Code: "+BA.NumberToString(_code)+" Message: "+_message,0);
 //BA.debugLineNum = 1335;BA.debugLine="Return \"Code: \" & code & \" Message: \" & messag";
if (true) return "Code: "+BA.NumberToString(_code)+" Message: "+_message;
 }else {
 //BA.debugLineNum = 1337;BA.debugLine="Return json";
if (true) return _json;
 };
 };
 } 
       catch (Exception e19) {
			processBA.setLastException(e19); //BA.debugLineNum = 1341;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("424182806",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1342;BA.debugLine="ToastMessageShow(\"GetRESTError: \" & LastExceptio";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("GetRESTError: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1343;BA.debugLine="Return json";
if (true) return _json;
 };
 //BA.debugLineNum = 1345;BA.debugLine="End Sub";
return "";
}
public static String  _getsyncmoduleinfo(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.Map _syncmodule = null;
int _wifi_strength = 0;
String _os_version = "";
String _fw_version = "";
String _status = "";
 //BA.debugLineNum = 1437;BA.debugLine="Sub GetSyncModuleInfo(json As String)";
 //BA.debugLineNum = 1438;BA.debugLine="Try";
try { //BA.debugLineNum = 1439;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1440;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1441;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1442;BA.debugLine="Dim syncmodule As Map = root.Get(\"syncmodule\")";
_syncmodule = new anywheresoftware.b4a.objects.collections.Map();
_syncmodule.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("syncmodule"))));
 //BA.debugLineNum = 1446;BA.debugLine="Dim wifi_strength As Int = syncmodule.Get(\"wifi_";
_wifi_strength = (int)(BA.ObjectToNumber(_syncmodule.Get((Object)("wifi_strength"))));
 //BA.debugLineNum = 1447;BA.debugLine="Dim os_version As String = syncmodule.Get(\"os_ve";
_os_version = BA.ObjectToString(_syncmodule.Get((Object)("os_version")));
 //BA.debugLineNum = 1464;BA.debugLine="Dim fw_version As String = syncmodule.Get(\"fw_ve";
_fw_version = BA.ObjectToString(_syncmodule.Get((Object)("fw_version")));
 //BA.debugLineNum = 1466;BA.debugLine="Dim status As String = syncmodule.Get(\"status\")";
_status = BA.ObjectToString(_syncmodule.Get((Object)("status")));
 //BA.debugLineNum = 1467;BA.debugLine="lblSyncModule.Text = \"Sync Module is \" & status";
mostCurrent._lblsyncmodule.setText(BA.ObjectToCharSequence("Sync Module is "+_status+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Wifi Strength: "+BA.NumberToString(_wifi_strength)+" bars"+anywheresoftware.b4a.keywords.Common.CRLF+"Firmware version: "+_fw_version+anywheresoftware.b4a.keywords.Common.CRLF+"OS version: "+_os_version));
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 1469;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("424313888",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1470;BA.debugLine="ToastMessageShow(\"GetSyncModuleInfo: \" & LastExc";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("GetSyncModuleInfo: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1472;BA.debugLine="End Sub";
return "";
}
public static String  _getuserinfo(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
 //BA.debugLineNum = 1201;BA.debugLine="Sub GetUserInfo(json As String)";
 //BA.debugLineNum = 1202;BA.debugLine="Try";
try { //BA.debugLineNum = 1203;BA.debugLine="lblStatus.Text = \"Getting user information...\"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Getting user information..."));
 //BA.debugLineNum = 1204;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1205;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1206;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1221;BA.debugLine="accountID = root.Get(\"account_id\") '88438";
_accountid = BA.ObjectToString(_root.Get((Object)("account_id")));
 //BA.debugLineNum = 1222;BA.debugLine="Log(\"accountID: \" &  accountID)";
anywheresoftware.b4a.keywords.Common.LogImpl("423920661","accountID: "+_accountid,0);
 } 
       catch (Exception e9) {
			processBA.setLastException(e9); //BA.debugLineNum = 1225;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("423920664",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1226;BA.debugLine="ToastMessageShow(\"GetUserInfo: \" & LastException";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("GetUserInfo: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1228;BA.debugLine="End Sub";
return "";
}
public static String  _getversioncode() throws Exception{
String _appversion = "";
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
String _packagename = "";
 //BA.debugLineNum = 516;BA.debugLine="Sub GetVersionCode() As String";
 //BA.debugLineNum = 517;BA.debugLine="Dim AppVersion As String";
_appversion = "";
 //BA.debugLineNum = 518;BA.debugLine="Try";
try { //BA.debugLineNum = 519;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 520;BA.debugLine="Dim packageName As String";
_packagename = "";
 //BA.debugLineNum = 521;BA.debugLine="packageName =  Application.PackageName";
_packagename = anywheresoftware.b4a.keywords.Common.Application.getPackageName();
 //BA.debugLineNum = 522;BA.debugLine="AppVersion = pm.GetVersionName(packageName)";
_appversion = _pm.GetVersionName(_packagename);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 524;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("422609928",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 526;BA.debugLine="Return AppVersion";
if (true) return _appversion;
 //BA.debugLineNum = 527;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 42;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 43;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 45;BA.debugLine="Private gblACMenu As ACMenu";
mostCurrent._gblacmenu = new de.amberhome.objects.appcompat.ACMenuWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 47;BA.debugLine="Private GaugeHumidity As Gauge";
mostCurrent._gaugehumidity = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 48;BA.debugLine="Private GaugeTemp As Gauge";
mostCurrent._gaugetemp = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 49;BA.debugLine="Private GaugeDewPoint As Gauge";
mostCurrent._gaugedewpoint = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 50;BA.debugLine="Private GaugeHeatIndex As Gauge";
mostCurrent._gaugeheatindex = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 51;BA.debugLine="Private lblComfort As Label";
mostCurrent._lblcomfort = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private lblPerception As Label";
mostCurrent._lblperception = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private lblLastUpdate As Label";
mostCurrent._lbllastupdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private lblPing As Label";
mostCurrent._lblping = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private TabStrip1 As TabStrip";
mostCurrent._tabstrip1 = new anywheresoftware.b4a.objects.TabStripViewPager();
 //BA.debugLineNum = 56;BA.debugLine="Private lblFontAwesome As Label";
mostCurrent._lblfontawesome = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private GaugeAirQuality As Gauge";
mostCurrent._gaugeairquality = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 58;BA.debugLine="Private lblAirQuality As Label";
mostCurrent._lblairquality = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private lblAirQualityLastUpdate As Label";
mostCurrent._lblairqualitylastupdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private ScrollView1 As ScrollView";
mostCurrent._scrollview1 = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private GaugeAirQualityBasement As Gauge";
mostCurrent._gaugeairqualitybasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 63;BA.debugLine="Private lblAirQualityBasement As Label";
mostCurrent._lblairqualitybasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private lblAirQualityLastUpdateBasement As Label";
mostCurrent._lblairqualitylastupdatebasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private PanelAirQualityBasement As Panel";
mostCurrent._panelairqualitybasement = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private GaugeDewPointBasement As Gauge";
mostCurrent._gaugedewpointbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 67;BA.debugLine="Private GaugeHeatIndexBasement As Gauge";
mostCurrent._gaugeheatindexbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 68;BA.debugLine="Private GaugeHumidityBasement As Gauge";
mostCurrent._gaugehumiditybasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 69;BA.debugLine="Private GaugeTempBasement As Gauge";
mostCurrent._gaugetempbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 70;BA.debugLine="Private lblComfortBasement As Label";
mostCurrent._lblcomfortbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Private lblLastUpdateBasement As Label";
mostCurrent._lbllastupdatebasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Private lblPerceptionBasement As Label";
mostCurrent._lblperceptionbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Private lblPingBasement As Label";
mostCurrent._lblpingbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Private ScrollViewBasement As ScrollView";
mostCurrent._scrollviewbasement = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Private PanelTempHumidityBasement As Panel";
mostCurrent._paneltemphumiditybasement = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Private lblStatus As Label";
mostCurrent._lblstatus = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 77;BA.debugLine="Private ivDriveway As ImageView";
mostCurrent._ivdriveway = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 78;BA.debugLine="Private ivFrontDoor As ImageView";
mostCurrent._ivfrontdoor = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Private ivSideYard As ImageView";
mostCurrent._ivsideyard = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private ScrollViewBlink As ScrollView";
mostCurrent._scrollviewblink = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private panelBlink As Panel";
mostCurrent._panelblink = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Private lblDriveway As Label";
mostCurrent._lbldriveway = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Private lblFrontDoor As Label";
mostCurrent._lblfrontdoor = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Private lblSideYard As Label";
mostCurrent._lblsideyard = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 85;BA.debugLine="Private btnDriveway As Button";
mostCurrent._btndriveway = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Private lblDrivewayBatt As Label";
mostCurrent._lbldrivewaybatt = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 87;BA.debugLine="Private lblDrivewayTimestamp As Label";
mostCurrent._lbldrivewaytimestamp = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 88;BA.debugLine="Private lblDrivewayWifi As Label";
mostCurrent._lbldrivewaywifi = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 89;BA.debugLine="Private lblFrontDoorBatt As Label";
mostCurrent._lblfrontdoorbatt = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 90;BA.debugLine="Private lblFrontDoorTimestamp As Label";
mostCurrent._lblfrontdoortimestamp = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 91;BA.debugLine="Private lblFrontDoorWiFi As Label";
mostCurrent._lblfrontdoorwifi = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Private lblSideYardBatt As Label";
mostCurrent._lblsideyardbatt = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Private lblSideYardTimestamp As Label";
mostCurrent._lblsideyardtimestamp = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Private lblSideYardWiFi As Label";
mostCurrent._lblsideyardwifi = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 95;BA.debugLine="Private lblSyncModule As Label";
mostCurrent._lblsyncmodule = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 96;BA.debugLine="Private B4XPageIndicator1 As B4XPageIndicator";
mostCurrent._b4xpageindicator1 = new cloyd.smart.home.monitor.b4xpageindicator();
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _handlesettings() throws Exception{
 //BA.debugLineNum = 898;BA.debugLine="Sub HandleSettings";
 //BA.debugLineNum = 899;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTime",_manager.GetString("TempHumidityCooldownTime"));
 //BA.debugLineNum = 900;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTimeBasement",_manager.GetString("TempHumidityCooldownTimeBasement"));
 //BA.debugLineNum = 901;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",manage";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue",_manager.GetString("HumidityAddValue"));
 //BA.debugLineNum = 902;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime",_manager.GetString("SensorNotRespondingTime"));
 //BA.debugLineNum = 903;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 904;BA.debugLine="End Sub";
return "";
}
public static String  _hideping() throws Exception{
 //BA.debugLineNum = 541;BA.debugLine="Private Sub HidePing";
 //BA.debugLineNum = 542;BA.debugLine="lblPing.SetVisibleAnimated(200, False)";
mostCurrent._lblping.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 543;BA.debugLine="End Sub";
return "";
}
public static String  _hidepingbasement() throws Exception{
 //BA.debugLineNum = 545;BA.debugLine="Private Sub HidePingBasement";
 //BA.debugLineNum = 546;BA.debugLine="lblPingBasement.SetVisibleAnimated(200, False)";
mostCurrent._lblpingbasement.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 547;BA.debugLine="End Sub";
return "";
}
public static String  _ivdriveway_click() throws Exception{
 //BA.debugLineNum = 1655;BA.debugLine="Sub ivDriveway_Click";
 //BA.debugLineNum = 1656;BA.debugLine="lblDrivewayBatt.Visible = Not(lblDrivewayBatt.Vis";
mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaybatt.getVisible()));
 //BA.debugLineNum = 1657;BA.debugLine="lblDrivewayTimestamp.Visible = Not(lblDrivewayTim";
mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaytimestamp.getVisible()));
 //BA.debugLineNum = 1658;BA.debugLine="lblDrivewayWifi.Visible = Not(lblDrivewayWifi.Vis";
mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaywifi.getVisible()));
 //BA.debugLineNum = 1659;BA.debugLine="lblDriveway.Visible = Not(lblDriveway.Visible)";
mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldriveway.getVisible()));
 //BA.debugLineNum = 1660;BA.debugLine="lblFrontDoorBatt.Visible = Not(lblFrontDoorBatt.V";
mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorbatt.getVisible()));
 //BA.debugLineNum = 1661;BA.debugLine="lblFrontDoorTimestamp.Visible = Not(lblFrontDoorT";
mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoortimestamp.getVisible()));
 //BA.debugLineNum = 1662;BA.debugLine="lblFrontDoorWiFi.Visible = Not(lblFrontDoorWiFi.V";
mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorwifi.getVisible()));
 //BA.debugLineNum = 1663;BA.debugLine="lblFrontDoor.Visible = Not(lblFrontDoor.Visible)";
mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoor.getVisible()));
 //BA.debugLineNum = 1664;BA.debugLine="lblSideYardBatt.Visible = Not(lblSideYardBatt.Vis";
mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardbatt.getVisible()));
 //BA.debugLineNum = 1665;BA.debugLine="lblSideYardTimestamp.Visible = Not(lblSideYardTim";
mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardtimestamp.getVisible()));
 //BA.debugLineNum = 1666;BA.debugLine="lblSideYardWiFi.Visible = Not(lblSideYardWiFi.Vis";
mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardwifi.getVisible()));
 //BA.debugLineNum = 1667;BA.debugLine="lblSideYard.Visible = Not(lblSideYard.Visible)";
mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyard.getVisible()));
 //BA.debugLineNum = 1668;BA.debugLine="End Sub";
return "";
}
public static String  _ivfrontdoor_click() throws Exception{
 //BA.debugLineNum = 1640;BA.debugLine="Sub ivFrontDoor_Click";
 //BA.debugLineNum = 1641;BA.debugLine="lblDrivewayBatt.Visible = Not(lblDrivewayBatt.Vis";
mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaybatt.getVisible()));
 //BA.debugLineNum = 1642;BA.debugLine="lblDrivewayTimestamp.Visible = Not(lblDrivewayTim";
mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaytimestamp.getVisible()));
 //BA.debugLineNum = 1643;BA.debugLine="lblDrivewayWifi.Visible = Not(lblDrivewayWifi.Vis";
mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaywifi.getVisible()));
 //BA.debugLineNum = 1644;BA.debugLine="lblDriveway.Visible = Not(lblDriveway.Visible)";
mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldriveway.getVisible()));
 //BA.debugLineNum = 1645;BA.debugLine="lblFrontDoorBatt.Visible = Not(lblFrontDoorBatt.V";
mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorbatt.getVisible()));
 //BA.debugLineNum = 1646;BA.debugLine="lblFrontDoorTimestamp.Visible = Not(lblFrontDoorT";
mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoortimestamp.getVisible()));
 //BA.debugLineNum = 1647;BA.debugLine="lblFrontDoorWiFi.Visible = Not(lblFrontDoorWiFi.V";
mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorwifi.getVisible()));
 //BA.debugLineNum = 1648;BA.debugLine="lblFrontDoor.Visible = Not(lblFrontDoor.Visible)";
mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoor.getVisible()));
 //BA.debugLineNum = 1649;BA.debugLine="lblSideYardBatt.Visible = Not(lblSideYardBatt.Vis";
mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardbatt.getVisible()));
 //BA.debugLineNum = 1650;BA.debugLine="lblSideYardTimestamp.Visible = Not(lblSideYardTim";
mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardtimestamp.getVisible()));
 //BA.debugLineNum = 1651;BA.debugLine="lblSideYardWiFi.Visible = Not(lblSideYardWiFi.Vis";
mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardwifi.getVisible()));
 //BA.debugLineNum = 1652;BA.debugLine="lblSideYard.Visible = Not(lblSideYard.Visible)";
mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyard.getVisible()));
 //BA.debugLineNum = 1653;BA.debugLine="End Sub";
return "";
}
public static String  _ivsideyard_click() throws Exception{
 //BA.debugLineNum = 1625;BA.debugLine="Sub ivSideYard_Click";
 //BA.debugLineNum = 1626;BA.debugLine="lblDrivewayBatt.Visible = Not(lblDrivewayBatt.Vis";
mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaybatt.getVisible()));
 //BA.debugLineNum = 1627;BA.debugLine="lblDrivewayTimestamp.Visible = Not(lblDrivewayTim";
mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaytimestamp.getVisible()));
 //BA.debugLineNum = 1628;BA.debugLine="lblDrivewayWifi.Visible = Not(lblDrivewayWifi.Vis";
mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaywifi.getVisible()));
 //BA.debugLineNum = 1629;BA.debugLine="lblDriveway.Visible = Not(lblDriveway.Visible)";
mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldriveway.getVisible()));
 //BA.debugLineNum = 1630;BA.debugLine="lblFrontDoorBatt.Visible = Not(lblFrontDoorBatt.V";
mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorbatt.getVisible()));
 //BA.debugLineNum = 1631;BA.debugLine="lblFrontDoorTimestamp.Visible = Not(lblFrontDoorT";
mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoortimestamp.getVisible()));
 //BA.debugLineNum = 1632;BA.debugLine="lblFrontDoorWiFi.Visible = Not(lblFrontDoorWiFi.V";
mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorwifi.getVisible()));
 //BA.debugLineNum = 1633;BA.debugLine="lblFrontDoor.Visible = Not(lblFrontDoor.Visible)";
mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoor.getVisible()));
 //BA.debugLineNum = 1634;BA.debugLine="lblSideYardBatt.Visible = Not(lblSideYardBatt.Vis";
mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardbatt.getVisible()));
 //BA.debugLineNum = 1635;BA.debugLine="lblSideYardTimestamp.Visible = Not(lblSideYardTim";
mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardtimestamp.getVisible()));
 //BA.debugLineNum = 1636;BA.debugLine="lblSideYardWiFi.Visible = Not(lblSideYardWiFi.Vis";
mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardwifi.getVisible()));
 //BA.debugLineNum = 1637;BA.debugLine="lblSideYard.Visible = Not(lblSideYard.Visible)";
mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyard.getVisible()));
 //BA.debugLineNum = 1638;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connect() throws Exception{
String _clientid = "";
anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper _connopt = null;
 //BA.debugLineNum = 311;BA.debugLine="Sub MQTT_Connect";
 //BA.debugLineNum = 312;BA.debugLine="Try";
try { //BA.debugLineNum = 313;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'crea";
_clientid = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (999999999)));
 //BA.debugLineNum = 314;BA.debugLine="MQTT.Initialize(\"MQTT\", MQTTServerURI, ClientId)";
_mqtt.Initialize(processBA,"MQTT",_mqttserveruri,_clientid);
 //BA.debugLineNum = 316;BA.debugLine="Dim ConnOpt As MqttConnectOptions";
_connopt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper();
 //BA.debugLineNum = 317;BA.debugLine="ConnOpt.Initialize(MQTTUser, MQTTPassword)";
_connopt.Initialize(_mqttuser,_mqttpassword);
 //BA.debugLineNum = 318;BA.debugLine="MQTT.Connect2(ConnOpt)";
_mqtt.Connect2((org.eclipse.paho.client.mqttv3.MqttConnectOptions)(_connopt.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 320;BA.debugLine="Log(\"MQTT_Connect: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("422020105","MQTT_Connect: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 322;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 324;BA.debugLine="Sub MQTT_Connected (Success As Boolean)";
 //BA.debugLineNum = 325;BA.debugLine="Try";
try { //BA.debugLineNum = 326;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 327;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("422085635",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 328;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 }else {
 //BA.debugLineNum = 330;BA.debugLine="Log(\"Connected to MQTT broker\")";
anywheresoftware.b4a.keywords.Common.LogImpl("422085638","Connected to MQTT broker",0);
 //BA.debugLineNum = 331;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
_mqtt.Subscribe("TempHumid",(int) (0));
 //BA.debugLineNum = 332;BA.debugLine="MQTT.Subscribe(\"MQ7\", 0)";
_mqtt.Subscribe("MQ7",(int) (0));
 //BA.debugLineNum = 333;BA.debugLine="MQTT.Subscribe(\"MQ7Basement\", 0)";
_mqtt.Subscribe("MQ7Basement",(int) (0));
 //BA.debugLineNum = 334;BA.debugLine="MQTT.Subscribe(\"TempHumidBasement\", 0)";
_mqtt.Subscribe("TempHumidBasement",(int) (0));
 //BA.debugLineNum = 335;BA.debugLine="MQTT.Subscribe(\"HumidityAddValue\", 0)";
_mqtt.Subscribe("HumidityAddValue",(int) (0));
 };
 } 
       catch (Exception e14) {
			processBA.setLastException(e14); //BA.debugLineNum = 338;BA.debugLine="Log(\"MQTT_Connected: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("422085646","MQTT_Connected: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 340;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_disconnected() throws Exception{
 //BA.debugLineNum = 342;BA.debugLine="Private Sub MQTT_Disconnected";
 //BA.debugLineNum = 343;BA.debugLine="Try";
try { //BA.debugLineNum = 344;BA.debugLine="gblACMenu.Clear";
mostCurrent._gblacmenu.Clear();
 //BA.debugLineNum = 345;BA.debugLine="gblACMenu.Add(0, 0, \"Settings\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Settings"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 347;BA.debugLine="gblACMenu.Add(0, 0, \"About\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 348;BA.debugLine="Log(\"Disconnected from MQTT broker\")";
anywheresoftware.b4a.keywords.Common.LogImpl("422151174","Disconnected from MQTT broker",0);
 //BA.debugLineNum = 349;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 351;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("422151177",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 353;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
 //BA.debugLineNum = 355;BA.debugLine="Private Sub MQTT_MessageArrived (Topic As String,";
 //BA.debugLineNum = 356;BA.debugLine="Try";
try { //BA.debugLineNum = 357;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic).equals("TempHumid")) { 
 //BA.debugLineNum = 358;BA.debugLine="lblPing.SetVisibleAnimated(500, True)";
mostCurrent._lblping.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 359;BA.debugLine="csu.CallSubPlus(Me, \"HidePing\", 700)";
_csu._v7(main.getObject(),"HidePing",(int) (700));
 //BA.debugLineNum = 361;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 }else if((_topic).equals("MQ7")) { 
 //BA.debugLineNum = 363;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 }else if((_topic).equals("MQ7Basement")) { 
 //BA.debugLineNum = 365;BA.debugLine="CheckAirQualitySettingBasement";
_checkairqualitysettingbasement();
 }else if((_topic).equals("TempHumidBasement")) { 
 //BA.debugLineNum = 367;BA.debugLine="lblPingBasement.SetVisibleAnimated(500, True)";
mostCurrent._lblpingbasement.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 368;BA.debugLine="csu.CallSubPlus(Me, \"HidePingBasement\", 700)";
_csu._v7(main.getObject(),"HidePingBasement",(int) (700));
 //BA.debugLineNum = 370;BA.debugLine="CheckTempHumiditySettingBasement";
_checktemphumiditysettingbasement();
 };
 } 
       catch (Exception e16) {
			processBA.setLastException(e16); //BA.debugLineNum = 373;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("422216722",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 375;BA.debugLine="End Sub";
return "";
}
public static long  _parseutcstring(String _utc) throws Exception{
String _df = "";
long _res = 0L;
 //BA.debugLineNum = 1524;BA.debugLine="Sub ParseUTCstring(utc As String) As Long";
 //BA.debugLineNum = 1525;BA.debugLine="Dim df As String = DateTime.DateFormat";
_df = anywheresoftware.b4a.keywords.Common.DateTime.getDateFormat();
 //BA.debugLineNum = 1526;BA.debugLine="Dim res As Long";
_res = 0L;
 //BA.debugLineNum = 1527;BA.debugLine="If utc.CharAt(10) = \"T\" Then";
if (_utc.charAt((int) (10))==BA.ObjectToChar("T")) { 
 //BA.debugLineNum = 1529;BA.debugLine="If utc.CharAt(19) = \".\" Then utc = utc.SubString";
if (_utc.charAt((int) (19))==BA.ObjectToChar(".")) { 
_utc = _utc.substring((int) (0),(int) (19))+"+0000";};
 //BA.debugLineNum = 1530;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd'T'HH:mm:ssZ\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
 }else {
 //BA.debugLineNum = 1533;BA.debugLine="DateTime.DateFormat = \"EEE MMM dd HH:mm:ss Z yyy";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
 };
 //BA.debugLineNum = 1535;BA.debugLine="Try";
try { //BA.debugLineNum = 1536;BA.debugLine="res = DateTime.DateParse(utc)";
_res = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_utc);
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 1538;BA.debugLine="res = 0";
_res = (long) (0);
 };
 //BA.debugLineNum = 1540;BA.debugLine="DateTime.DateFormat = df";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat(_df);
 //BA.debugLineNum = 1541;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 1542;BA.debugLine="End Sub";
return 0L;
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
httputils2service._process_globals();
		
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
 //BA.debugLineNum = 30;BA.debugLine="Private emailAddress As String = \"antimone2001@ho";
_emailaddress = "[redacted]";
 //BA.debugLineNum = 31;BA.debugLine="Private password As String = \"[redacted]\"";
_password = "[redacted]";
 //BA.debugLineNum = 32;BA.debugLine="Private authToken As String";
_authtoken = "";
 //BA.debugLineNum = 33;BA.debugLine="Private userRegion As String";
_userregion = "";
 //BA.debugLineNum = 34;BA.debugLine="Private accountID As String";
_accountid = "";
 //BA.debugLineNum = 35;BA.debugLine="Private networkID As String";
_networkid = "";
 //BA.debugLineNum = 36;BA.debugLine="Private commandID As String";
_commandid = "";
 //BA.debugLineNum = 37;BA.debugLine="Private commandComplete As Boolean";
_commandcomplete = false;
 //BA.debugLineNum = 38;BA.debugLine="Private cameraThumbnail As String";
_camerathumbnail = "";
 //BA.debugLineNum = 39;BA.debugLine="Private response As String";
_response = "";
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}
public static void  _refreshcameras(boolean _firstrun) throws Exception{
ResumableSub_RefreshCameras rsub = new ResumableSub_RefreshCameras(null,_firstrun);
rsub.resume(processBA, null);
}
public static class ResumableSub_RefreshCameras extends BA.ResumableSub {
public ResumableSub_RefreshCameras(cloyd.smart.home.monitor.main parent,boolean _firstrun) {
this.parent = parent;
this._firstrun = _firstrun;
}
cloyd.smart.home.monitor.main parent;
boolean _firstrun;
String _camera = "";
anywheresoftware.b4a.objects.ImageViewWrapper _iv = null;
b4a.example3.customlistview _clv = null;
anywheresoftware.b4a.objects.collections.List _links = null;
String _link = "";
int _i = 0;
anywheresoftware.b4a.BA.IterableList group22;
int index22;
int groupLen22;
int step66;
int limit66;

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
 //BA.debugLineNum = 960;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 80;
this.catchState = 79;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 79;
 //BA.debugLineNum = 961;BA.debugLine="Dim camera As String";
_camera = "";
 //BA.debugLineNum = 962;BA.debugLine="Dim iv As ImageView";
_iv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 963;BA.debugLine="Dim clv As CustomListView";
_clv = new b4a.example3.customlistview();
 //BA.debugLineNum = 964;BA.debugLine="Dim links As List";
_links = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 965;BA.debugLine="links = Array(\"347574\", \"236967\", \"226821\")";
_links = anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)("347574"),(Object)("236967"),(Object)("226821")});
 //BA.debugLineNum = 967;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia-";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/syncmodules");
 //BA.debugLineNum = 968;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 81;
return;
case 81:
//C
this.state = 4;
;
 //BA.debugLineNum = 969;BA.debugLine="GetSyncModuleInfo(response)";
_getsyncmoduleinfo(parent._response);
 //BA.debugLineNum = 971;BA.debugLine="lblDrivewayBatt.Visible = True";
parent.mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 972;BA.debugLine="lblDrivewayTimestamp.Visible = True";
parent.mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 973;BA.debugLine="lblDrivewayWifi.Visible = True";
parent.mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 974;BA.debugLine="lblDriveway.Visible = True";
parent.mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 975;BA.debugLine="lblFrontDoorBatt.Visible = True";
parent.mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 976;BA.debugLine="lblFrontDoorTimestamp.Visible = True";
parent.mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 977;BA.debugLine="lblFrontDoorWiFi.Visible = True";
parent.mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 978;BA.debugLine="lblFrontDoor.Visible = True";
parent.mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 979;BA.debugLine="lblSideYardBatt.Visible = True";
parent.mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 980;BA.debugLine="lblSideYardTimestamp.Visible = True";
parent.mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 981;BA.debugLine="lblSideYardWiFi.Visible = True";
parent.mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 982;BA.debugLine="lblSideYard.Visible = True";
parent.mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 984;BA.debugLine="For Each link As String In links";
if (true) break;

case 4:
//for
this.state = 77;
group22 = _links;
index22 = 0;
groupLen22 = group22.getSize();
this.state = 82;
if (true) break;

case 82:
//C
this.state = 77;
if (index22 < groupLen22) {
this.state = 6;
_link = BA.ObjectToString(group22.Get(index22));}
if (true) break;

case 83:
//C
this.state = 82;
index22++;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 985;BA.debugLine="camera = link";
_camera = _link;
 //BA.debugLineNum = 986;BA.debugLine="If FirstRun Then";
if (true) break;

case 7:
//if
this.state = 28;
if (_firstrun) { 
this.state = 9;
}else {
this.state = 19;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 987;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 10:
//if
this.state = 17;
if ((_camera).equals("347574")) { 
this.state = 12;
}else if((_camera).equals("236967")) { 
this.state = 14;
}else if((_camera).equals("226821")) { 
this.state = 16;
}if (true) break;

case 12:
//C
this.state = 17;
 //BA.debugLineNum = 988;BA.debugLine="lblStatus.Text = \"Retrieving Driveway thumbna";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Driveway thumbnail..."));
 //BA.debugLineNum = 989;BA.debugLine="iv = ivDriveway";
_iv = parent.mostCurrent._ivdriveway;
 if (true) break;

case 14:
//C
this.state = 17;
 //BA.debugLineNum = 991;BA.debugLine="lblStatus.Text = \"Retrieving Front Door thumb";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Front Door thumbnail..."));
 //BA.debugLineNum = 992;BA.debugLine="iv = ivFrontDoor";
_iv = parent.mostCurrent._ivfrontdoor;
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 994;BA.debugLine="lblStatus.Text = \"Retrieving Side Yard thumbn";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Side Yard thumbnail..."));
 //BA.debugLineNum = 995;BA.debugLine="iv = ivSideYard";
_iv = parent.mostCurrent._ivsideyard;
 if (true) break;

case 17:
//C
this.state = 28;
;
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 998;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 20:
//if
this.state = 27;
if ((_camera).equals("347574")) { 
this.state = 22;
}else if((_camera).equals("236967")) { 
this.state = 24;
}else if((_camera).equals("226821")) { 
this.state = 26;
}if (true) break;

case 22:
//C
this.state = 27;
 //BA.debugLineNum = 999;BA.debugLine="lblStatus.Text = \"Capturing a new Driveway th";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Driveway thumbnail..."));
 //BA.debugLineNum = 1000;BA.debugLine="iv = ivDriveway";
_iv = parent.mostCurrent._ivdriveway;
 if (true) break;

case 24:
//C
this.state = 27;
 //BA.debugLineNum = 1002;BA.debugLine="lblStatus.Text = \"Capturing a new Front Door";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Front Door thumbnail..."));
 //BA.debugLineNum = 1003;BA.debugLine="iv = ivFrontDoor";
_iv = parent.mostCurrent._ivfrontdoor;
 if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 1005;BA.debugLine="lblStatus.Text = \"Capturing a new Side Yard t";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Side Yard thumbnail..."));
 //BA.debugLineNum = 1006;BA.debugLine="iv = ivSideYard";
_iv = parent.mostCurrent._ivsideyard;
 if (true) break;

case 27:
//C
this.state = 28;
;
 if (true) break;
;
 //BA.debugLineNum = 1010;BA.debugLine="If FirstRun Then";

case 28:
//if
this.state = 76;
if (_firstrun) { 
this.state = 30;
}else {
this.state = 32;
}if (true) break;

case 30:
//C
this.state = 76;
 //BA.debugLineNum = 1012;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedi";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera);
 //BA.debugLineNum = 1013;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 84;
return;
case 84:
//C
this.state = 76;
;
 //BA.debugLineNum = 1014;BA.debugLine="GetCameraInfo(response,clv)";
_getcamerainfo(parent._response,_clv);
 //BA.debugLineNum = 1015;BA.debugLine="DownloadImage(\"https://rest-\" & userRegion &\".";
_downloadimage("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg",_iv,_camera);
 if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 1017;BA.debugLine="RESTPost(\"https://rest-\" & userRegion &\".immed";
_restpost("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera+"/thumbnail");
 //BA.debugLineNum = 1018;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 85;
return;
case 85:
//C
this.state = 33;
;
 //BA.debugLineNum = 1019;BA.debugLine="If response.StartsWith(\"ERROR: \") Then";
if (true) break;

case 33:
//if
this.state = 75;
if (parent._response.startsWith("ERROR: ")) { 
this.state = 35;
}else {
this.state = 37;
}if (true) break;

case 35:
//C
this.state = 75;
 //BA.debugLineNum = 1020;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1021;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 1023;BA.debugLine="GetCommandID(response)";
_getcommandid(parent._response);
 //BA.debugLineNum = 1024;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immed";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 1025;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 86;
return;
case 86:
//C
this.state = 38;
;
 //BA.debugLineNum = 1026;BA.debugLine="If response.StartsWith(\"ERROR: \") Then";
if (true) break;

case 38:
//if
this.state = 74;
if (parent._response.startsWith("ERROR: ")) { 
this.state = 40;
}else {
this.state = 42;
}if (true) break;

case 40:
//C
this.state = 74;
 //BA.debugLineNum = 1027;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1028;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 42:
//C
this.state = 43;
 //BA.debugLineNum = 1030;BA.debugLine="For i = 1 To 30";
if (true) break;

case 43:
//for
this.state = 60;
step66 = 1;
limit66 = (int) (30);
_i = (int) (1) ;
this.state = 87;
if (true) break;

case 87:
//C
this.state = 60;
if ((step66 > 0 && _i <= limit66) || (step66 < 0 && _i >= limit66)) this.state = 45;
if (true) break;

case 88:
//C
this.state = 87;
_i = ((int)(0 + _i + step66)) ;
if (true) break;

case 45:
//C
this.state = 46;
 //BA.debugLineNum = 1031;BA.debugLine="GetCommandStatus(response)";
_getcommandstatus(parent._response);
 //BA.debugLineNum = 1032;BA.debugLine="If commandComplete Then";
if (true) break;

case 46:
//if
this.state = 59;
if (parent._commandcomplete) { 
this.state = 48;
}else {
this.state = 50;
}if (true) break;

case 48:
//C
this.state = 59;
 //BA.debugLineNum = 1033;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".im";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera);
 //BA.debugLineNum = 1034;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 89;
return;
case 89:
//C
this.state = 59;
;
 //BA.debugLineNum = 1035;BA.debugLine="GetCameraInfo(response,clv)";
_getcamerainfo(parent._response,_clv);
 //BA.debugLineNum = 1036;BA.debugLine="DownloadImage(\"https://rest-\" & userRegion";
_downloadimage("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg",_iv,_camera);
 //BA.debugLineNum = 1037;BA.debugLine="Exit";
this.state = 60;
if (true) break;
 if (true) break;

case 50:
//C
this.state = 51;
 //BA.debugLineNum = 1039;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 51:
//if
this.state = 58;
if ((_camera).equals("347574")) { 
this.state = 53;
}else if((_camera).equals("236967")) { 
this.state = 55;
}else if((_camera).equals("226821")) { 
this.state = 57;
}if (true) break;

case 53:
//C
this.state = 58;
 //BA.debugLineNum = 1040;BA.debugLine="lblStatus.Text = \"Awaiting for the Drivew";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Driveway thumbnail... "+BA.NumberToString(_i)+"/30"));
 if (true) break;

case 55:
//C
this.state = 58;
 //BA.debugLineNum = 1042;BA.debugLine="lblStatus.Text = \"Awaiting for the Front";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Front Door thumbnail...  "+BA.NumberToString(_i)+"/30"));
 if (true) break;

case 57:
//C
this.state = 58;
 //BA.debugLineNum = 1044;BA.debugLine="lblStatus.Text = \"Awaiting for the Side Y";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Side Yard thumbnail... "+BA.NumberToString(_i)+"/30"));
 if (true) break;

case 58:
//C
this.state = 59;
;
 if (true) break;

case 59:
//C
this.state = 88;
;
 //BA.debugLineNum = 1047;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".imm";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 1048;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 90;
return;
case 90:
//C
this.state = 88;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1050;BA.debugLine="If commandComplete = False Then";

case 60:
//if
this.state = 73;
if (parent._commandcomplete==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 62;
}else {
this.state = 72;
}if (true) break;

case 62:
//C
this.state = 63;
 //BA.debugLineNum = 1051;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 63:
//if
this.state = 70;
if ((_camera).equals("347574")) { 
this.state = 65;
}else if((_camera).equals("236967")) { 
this.state = 67;
}else if((_camera).equals("226821")) { 
this.state = 69;
}if (true) break;

case 65:
//C
this.state = 70;
 //BA.debugLineNum = 1052;BA.debugLine="lblStatus.Text = \"Failed to retrieve Drive";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Driveway thumbnail..."));
 if (true) break;

case 67:
//C
this.state = 70;
 //BA.debugLineNum = 1055;BA.debugLine="lblStatus.Text = \"Failed to retrieve Front";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Front Door thumbnail..."));
 if (true) break;

case 69:
//C
this.state = 70;
 //BA.debugLineNum = 1058;BA.debugLine="lblStatus.Text = \"Failed to retrieve Side";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Side Yard thumbnail..."));
 if (true) break;

case 70:
//C
this.state = 73;
;
 if (true) break;

case 72:
//C
this.state = 73;
 if (true) break;

case 73:
//C
this.state = 74;
;
 if (true) break;

case 74:
//C
this.state = 75;
;
 if (true) break;

case 75:
//C
this.state = 76;
;
 if (true) break;

case 76:
//C
this.state = 83;
;
 if (true) break;
if (true) break;

case 77:
//C
this.state = 80;
;
 //BA.debugLineNum = 1078;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 79:
//C
this.state = 80;
this.catchState = 0;
 //BA.debugLineNum = 1080;BA.debugLine="Log(\"RefreshCamera LastException: \" & LastExcept";
anywheresoftware.b4a.keywords.Common.LogImpl("423593086","RefreshCamera LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1081;BA.debugLine="ToastMessageShow(\"RefreshCamera: \" & LastExcepti";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("RefreshCamera: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;
if (true) break;

case 80:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1083;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1084;BA.debugLine="End Sub";
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
public static void  _requestauthtoken() throws Exception{
ResumableSub_RequestAuthToken rsub = new ResumableSub_RequestAuthToken(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_RequestAuthToken extends BA.ResumableSub {
public ResumableSub_RequestAuthToken(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
cloyd.smart.home.monitor.httpjob _joblogin = null;

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
 //BA.debugLineNum = 913;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 24;
this.catchState = 23;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 23;
 //BA.debugLineNum = 914;BA.debugLine="lblStatus.Text = \"Authenticating...\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Authenticating..."));
 //BA.debugLineNum = 915;BA.debugLine="Dim jobLogin As HttpJob";
_joblogin = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 916;BA.debugLine="jobLogin.Initialize(\"\", Me)";
_joblogin._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 917;BA.debugLine="jobLogin.PostString(\"https://rest.prod.immedia-s";
_joblogin._poststring /*String*/ ("https://rest.prod.immedia-semi.com/login","email="+parent._emailaddress+"&password="+parent._password);
 //BA.debugLineNum = 918;BA.debugLine="jobLogin.GetRequest.SetContentType(\"application/";
_joblogin._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetContentType("application/x-www-form-urlencoded");
 //BA.debugLineNum = 919;BA.debugLine="Wait For (jobLogin) JobDone(jobLogin As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_joblogin));
this.state = 25;
return;
case 25:
//C
this.state = 4;
_joblogin = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 920;BA.debugLine="If jobLogin.Success Then";
if (true) break;

case 4:
//if
this.state = 21;
if (_joblogin._success /*boolean*/ ) { 
this.state = 6;
}else {
this.state = 20;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 921;BA.debugLine="lblStatus.Text = \"Successfully logged in to the";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Successfully logged in to the Blink server..."));
 //BA.debugLineNum = 923;BA.debugLine="GetAuthInfo(jobLogin.GetString)";
_getauthinfo(_joblogin._getstring /*String*/ ());
 //BA.debugLineNum = 925;BA.debugLine="RESTGet(\"https://rest-\" & userRegion & \".immedi";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/user");
 //BA.debugLineNum = 926;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 26;
return;
case 26:
//C
this.state = 7;
;
 //BA.debugLineNum = 927;BA.debugLine="If response.StartsWith(\"ERROR: \") Then";
if (true) break;

case 7:
//if
this.state = 12;
if (parent._response.startsWith("ERROR: ")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 928;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 930;BA.debugLine="lblStatus.Text = \"AuthToken acquired...\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("AuthToken acquired..."));
 //BA.debugLineNum = 931;BA.debugLine="GetUserInfo(response)";
_getuserinfo(parent._response);
 if (true) break;

case 12:
//C
this.state = 13;
;
 //BA.debugLineNum = 934;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/api/v1/camera/usage");
 //BA.debugLineNum = 935;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 27;
return;
case 27:
//C
this.state = 13;
;
 //BA.debugLineNum = 936;BA.debugLine="If response.StartsWith(\"ERROR: \") Then";
if (true) break;

case 13:
//if
this.state = 18;
if (parent._response.startsWith("ERROR: ")) { 
this.state = 15;
}else {
this.state = 17;
}if (true) break;

case 15:
//C
this.state = 18;
 //BA.debugLineNum = 937;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 939;BA.debugLine="GetAllCameras(response)";
_getallcameras(parent._response);
 if (true) break;

case 18:
//C
this.state = 21;
;
 if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 942;BA.debugLine="lblStatus.Text = GetRESTError(jobLogin.ErrorMes";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_joblogin._errormessage /*String*/ )));
 //BA.debugLineNum = 943;BA.debugLine="Log(\"RequestAuthToken error: \" & jobLogin.Error";
anywheresoftware.b4a.keywords.Common.LogImpl("423527460","RequestAuthToken error: "+_joblogin._errormessage /*String*/ ,0);
 //BA.debugLineNum = 944;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 21:
//C
this.state = 24;
;
 //BA.debugLineNum = 946;BA.debugLine="jobLogin.Release";
_joblogin._release /*String*/ ();
 //BA.debugLineNum = 947;BA.debugLine="RefreshCameras(True)";
_refreshcameras(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 23:
//C
this.state = 24;
this.catchState = 0;
 //BA.debugLineNum = 949;BA.debugLine="ToastMessageShow(\"RequestAuthToken: \" & LastExce";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("RequestAuthToken: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 950;BA.debugLine="Log(\"RequestAuthToken LastException: \" & LastExc";
anywheresoftware.b4a.keywords.Common.LogImpl("423527467","RequestAuthToken LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 24:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 952;BA.debugLine="End Sub";
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
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _restget(String _url) throws Exception{
ResumableSub_RESTGet rsub = new ResumableSub_RESTGet(null,_url);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_RESTGet extends BA.ResumableSub {
public ResumableSub_RESTGet(cloyd.smart.home.monitor.main parent,String _url) {
this.parent = parent;
this._url = _url;
}
cloyd.smart.home.monitor.main parent;
String _url;
cloyd.smart.home.monitor.httpjob _j = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
try {

        switch (state) {
            case -1:
{
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,null);return;}
case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1087;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 12;
this.catchState = 11;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 11;
 //BA.debugLineNum = 1088;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1089;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1090;BA.debugLine="j.Initialize(\"\", Me) 'name is empty as it is no";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1091;BA.debugLine="j.Download(url)";
_j._download /*String*/ (_url);
 //BA.debugLineNum = 1092;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1093;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 13;
return;
case 13:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1094;BA.debugLine="If j.Success Then";
if (true) break;

case 4:
//if
this.state = 9;
if (_j._success /*boolean*/ ) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 1095;BA.debugLine="response = j.GetString";
parent._response = _j._getstring /*String*/ ();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1097;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1098;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;

case 9:
//C
this.state = 12;
;
 //BA.debugLineNum = 1100;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 11:
//C
this.state = 12;
this.catchState = 0;
 //BA.debugLineNum = 1102;BA.debugLine="response = \"ERROR: \" & LastException";
parent._response = "ERROR: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA));
 //BA.debugLineNum = 1103;BA.debugLine="Log(\"RESTDownload LastException: \" & LastExcepti";
anywheresoftware.b4a.keywords.Common.LogImpl("423658513","RESTDownload LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1104;BA.debugLine="ToastMessageShow(\"RESTGet: \" & LastException,Fal";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("RESTGet: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;
if (true) break;

case 12:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1106;BA.debugLine="Log(\"URL: \" & url)";
anywheresoftware.b4a.keywords.Common.LogImpl("423658516","URL: "+_url,0);
 //BA.debugLineNum = 1107;BA.debugLine="Log(\"Response: \" & response)";
anywheresoftware.b4a.keywords.Common.LogImpl("423658517","Response: "+parent._response,0);
 //BA.debugLineNum = 1108;BA.debugLine="Return(response)";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)((parent._response)));return;};
 //BA.debugLineNum = 1109;BA.debugLine="End Sub";
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
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _restpost(String _url) throws Exception{
ResumableSub_RESTPost rsub = new ResumableSub_RESTPost(null,_url);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_RESTPost extends BA.ResumableSub {
public ResumableSub_RESTPost(cloyd.smart.home.monitor.main parent,String _url) {
this.parent = parent;
this._url = _url;
}
cloyd.smart.home.monitor.main parent;
String _url;
cloyd.smart.home.monitor.httpjob _j = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
try {

        switch (state) {
            case -1:
{
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,null);return;}
case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1112;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 12;
this.catchState = 11;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 11;
 //BA.debugLineNum = 1113;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1114;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1115;BA.debugLine="j.Initialize(\"\", Me) 'name is empty as it is no";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1116;BA.debugLine="j.PostString(url,\"\")";
_j._poststring /*String*/ (_url,"");
 //BA.debugLineNum = 1117;BA.debugLine="j.GetRequest.SetContentType(\"application/x-www-f";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetContentType("application/x-www-form-urlencoded");
 //BA.debugLineNum = 1118;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1119;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 13;
return;
case 13:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1120;BA.debugLine="If j.Success Then";
if (true) break;

case 4:
//if
this.state = 9;
if (_j._success /*boolean*/ ) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 1121;BA.debugLine="response = j.GetString";
parent._response = _j._getstring /*String*/ ();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1123;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1124;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;

case 9:
//C
this.state = 12;
;
 //BA.debugLineNum = 1126;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 11:
//C
this.state = 12;
this.catchState = 0;
 //BA.debugLineNum = 1128;BA.debugLine="response = \"ERROR: \" & LastException";
parent._response = "ERROR: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA));
 //BA.debugLineNum = 1129;BA.debugLine="Log(\"RESTPost LastException: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("423724050","RESTPost LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1130;BA.debugLine="ToastMessageShow(\"RESTPost: \" & LastException,Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("RESTPost: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;
if (true) break;

case 12:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1132;BA.debugLine="Log(\"URL: \" & url)";
anywheresoftware.b4a.keywords.Common.LogImpl("423724053","URL: "+_url,0);
 //BA.debugLineNum = 1133;BA.debugLine="Log(\"Response: \" & response)";
anywheresoftware.b4a.keywords.Common.LogImpl("423724054","Response: "+parent._response,0);
 //BA.debugLineNum = 1134;BA.debugLine="Return(response)";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)((parent._response)));return;};
 //BA.debugLineNum = 1135;BA.debugLine="End Sub";
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
public static String  _setavg(b4a.example.bitmapcreator _bcbitmap,int _x,int _y,b4a.example.bitmapcreator._argbcolor[] _clrs,b4a.example.bitmapcreator._argbcolor _temp) throws Exception{
b4a.example.bitmapcreator._argbcolor _c = null;
 //BA.debugLineNum = 1605;BA.debugLine="Private Sub SetAvg(bcBitmap As BitmapCreator, x As";
 //BA.debugLineNum = 1606;BA.debugLine="temp.Initialize";
_temp.Initialize();
 //BA.debugLineNum = 1607;BA.debugLine="For Each c As ARGBColor In clrs";
{
final b4a.example.bitmapcreator._argbcolor[] group2 = _clrs;
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_c = group2[index2];
 //BA.debugLineNum = 1608;BA.debugLine="temp.r = temp.r + c.r";
_temp.r = (int) (_temp.r+_c.r);
 //BA.debugLineNum = 1609;BA.debugLine="temp.g = temp.g + c.g";
_temp.g = (int) (_temp.g+_c.g);
 //BA.debugLineNum = 1610;BA.debugLine="temp.b = temp.b + c.b";
_temp.b = (int) (_temp.b+_c.b);
 }
};
 //BA.debugLineNum = 1612;BA.debugLine="temp.a = 255";
_temp.a = (int) (255);
 //BA.debugLineNum = 1613;BA.debugLine="temp.r = temp.r / clrs.Length";
_temp.r = (int) (_temp.r/(double)_clrs.length);
 //BA.debugLineNum = 1614;BA.debugLine="temp.g = temp.g / clrs.Length";
_temp.g = (int) (_temp.g/(double)_clrs.length);
 //BA.debugLineNum = 1615;BA.debugLine="temp.b = temp.b / clrs.Length";
_temp.b = (int) (_temp.b/(double)_clrs.length);
 //BA.debugLineNum = 1616;BA.debugLine="bcBitmap.SetARGB(x, y, temp)";
_bcbitmap._setargb(_x,_y,_temp);
 //BA.debugLineNum = 1617;BA.debugLine="End Sub";
return "";
}
public static String  _setdefaults() throws Exception{
 //BA.debugLineNum = 885;BA.debugLine="Sub SetDefaults";
 //BA.debugLineNum = 887;BA.debugLine="manager.SetString(\"TempHumidityCooldownTime\", \"5\"";
_manager.SetString("TempHumidityCooldownTime","5");
 //BA.debugLineNum = 888;BA.debugLine="manager.SetString(\"TempHumidityCooldownTimeBaseme";
_manager.SetString("TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 889;BA.debugLine="manager.SetString(\"HumidityAddValue\", \"0\")";
_manager.SetString("HumidityAddValue","0");
 //BA.debugLineNum = 890;BA.debugLine="manager.SetString(\"SensorNotRespondingTime\", \"5\")";
_manager.SetString("SensorNotRespondingTime","5");
 //BA.debugLineNum = 891;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTime","5");
 //BA.debugLineNum = 892;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 893;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",\"0\")";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue","0");
 //BA.debugLineNum = 894;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime","5");
 //BA.debugLineNum = 895;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 896;BA.debugLine="End Sub";
return "";
}
public static String  _settextshadow(anywheresoftware.b4a.objects.LabelWrapper _lbl) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
float _radius = 0f;
float _dx = 0f;
float _dy = 0f;
 //BA.debugLineNum = 1619;BA.debugLine="Sub SetTextShadow(lbl As Label)";
 //BA.debugLineNum = 1620;BA.debugLine="Dim jo As JavaObject = lbl";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 1621;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Float";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1622;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Black)});
 //BA.debugLineNum = 1623;BA.debugLine="End Sub";
return "";
}
public static String  _showaboutmenu() throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 506;BA.debugLine="Sub ShowAboutMenu";
 //BA.debugLineNum = 507;BA.debugLine="Try";
try { //BA.debugLineNum = 508;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 509;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cloyd.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 510;BA.debugLine="Msgbox2(\"Smart Home Monitor v\" & GetVersionCode";
anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Smart Home Monitor v"+_getversioncode()+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Developed by Cloyd Nino Catanaoan"+anywheresoftware.b4a.keywords.Common.CRLF+"September 23, 2019"),BA.ObjectToCharSequence("About"),"OK","","",_bd.getBitmap(),mostCurrent.activityBA);
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 512;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("422544390",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 514;BA.debugLine="End Sub";
return "";
}
public static String  _tabstrip1_pageselected(int _position) throws Exception{
 //BA.debugLineNum = 562;BA.debugLine="Sub TabStrip1_PageSelected (Position As Int)";
 //BA.debugLineNum = 563;BA.debugLine="Try";
try { //BA.debugLineNum = 564;BA.debugLine="B4XPageIndicator1.CurrentPage = Position";
mostCurrent._b4xpageindicator1._setcurrentpage /*int*/ (_position);
 //BA.debugLineNum = 565;BA.debugLine="If Position = 0 Then";
if (_position==0) { 
 //BA.debugLineNum = 566;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 567;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Re";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Read weather","utf8"));
 };
 //BA.debugLineNum = 569;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 };
 //BA.debugLineNum = 571;BA.debugLine="If Position = 1 Then";
if (_position==1) { 
 //BA.debugLineNum = 572;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 573;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Read vol";
_mqtt.Publish("MQ7",_bc.StringToBytes("Read voltage","utf8"));
 };
 //BA.debugLineNum = 575;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 };
 //BA.debugLineNum = 577;BA.debugLine="If Position = 2 Then";
if (_position==2) { 
 //BA.debugLineNum = 578;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 579;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringToB";
_mqtt.Publish("TempHumidBasement",_bc.StringToBytes("Read weather","utf8"));
 };
 //BA.debugLineNum = 581;BA.debugLine="CheckTempHumiditySettingBasement";
_checktemphumiditysettingbasement();
 };
 //BA.debugLineNum = 583;BA.debugLine="If Position = 3 Then";
if (_position==3) { 
 //BA.debugLineNum = 584;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 585;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes(\"";
_mqtt.Publish("MQ7Basement",_bc.StringToBytes("Read voltage","utf8"));
 };
 //BA.debugLineNum = 587;BA.debugLine="CheckAirQualitySettingBasement";
_checkairqualitysettingbasement();
 };
 //BA.debugLineNum = 589;BA.debugLine="If Position = 4 Then";
if (_position==4) { 
 //BA.debugLineNum = 590;BA.debugLine="btnDriveway.Enabled = False";
mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 591;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 592;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",mostCurrent._ivdriveway);
 //BA.debugLineNum = 593;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",mostCurrent._ivfrontdoor);
 //BA.debugLineNum = 594;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",mostCurrent._ivsideyard);
 //BA.debugLineNum = 595;BA.debugLine="RequestAuthToken";
_requestauthtoken();
 };
 } 
       catch (Exception e36) {
			processBA.setLastException(e36); //BA.debugLineNum = 598;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("422872100",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 600;BA.debugLine="End Sub";
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
