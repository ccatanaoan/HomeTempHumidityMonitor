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
public anywheresoftware.b4a.objects.ImageViewWrapper _ivscreenshot = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _lbldate = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _lbldeviceinfo = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _lblfileinfo = null;
public b4a.example3.customlistview _clvactivity = null;
public anywheresoftware.b4a.objects.WebViewWrapper _wvmedia = null;
public uk.co.martinpearman.b4a.webviewsettings.WebViewSettings _webviewsettings1 = null;
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
 //BA.debugLineNum = 106;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 107;BA.debugLine="Try";
try { //BA.debugLineNum = 108;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 109;BA.debugLine="CreatePreferenceScreen";
_createpreferencescreen();
 //BA.debugLineNum = 110;BA.debugLine="If manager.GetAll.Size = 0 Then SetDefaults";
if (_manager.GetAll().getSize()==0) { 
_setdefaults();};
 //BA.debugLineNum = 112;BA.debugLine="StartService(SmartHomeMonitor)";
anywheresoftware.b4a.keywords.Common.StartService(processBA,(Object)(mostCurrent._smarthomemonitor.getObject()));
 //BA.debugLineNum = 113;BA.debugLine="csu.Initialize";
_csu._initialize(processBA);
 //BA.debugLineNum = 114;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 116;BA.debugLine="Activity.LoadLayout(\"Main\")";
mostCurrent._activity.LoadLayout("Main",mostCurrent.activityBA);
 //BA.debugLineNum = 118;BA.debugLine="TabStrip1.LoadLayout(\"1ScrollView\", \"LIVING AREA";
mostCurrent._tabstrip1.LoadLayout("1ScrollView",BA.ObjectToCharSequence("LIVING AREA"+anywheresoftware.b4a.keywords.Common.CRLF+"Temp & Humidity  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf2c7)))));
 //BA.debugLineNum = 119;BA.debugLine="ScrollView1.Panel.LoadLayout(\"1\")";
mostCurrent._scrollview1.getPanel().LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 120;BA.debugLine="Panel1.Height = ScrollView1.Height 'Panel1.Heigh";
mostCurrent._panel1.setHeight(mostCurrent._scrollview1.getHeight());
 //BA.debugLineNum = 121;BA.debugLine="ScrollView1.Panel.Height = Panel1.Height";
mostCurrent._scrollview1.getPanel().setHeight(mostCurrent._panel1.getHeight());
 //BA.debugLineNum = 122;BA.debugLine="TabStrip1.LoadLayout(\"2\", \"LIVING AREA\" & CRLF &";
mostCurrent._tabstrip1.LoadLayout("2",BA.ObjectToCharSequence("LIVING AREA"+anywheresoftware.b4a.keywords.Common.CRLF+"Air Quality (CO)  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf289)))));
 //BA.debugLineNum = 123;BA.debugLine="TabStrip1.LoadLayout(\"ScrollViewBasement\", \"BASE";
mostCurrent._tabstrip1.LoadLayout("ScrollViewBasement",BA.ObjectToCharSequence("BASEMENT"+anywheresoftware.b4a.keywords.Common.CRLF+"Temp & Humidity  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf2c7)))));
 //BA.debugLineNum = 124;BA.debugLine="ScrollViewBasement.Panel.LoadLayout(\"TempHumidit";
mostCurrent._scrollviewbasement.getPanel().LoadLayout("TempHumidityBasement",mostCurrent.activityBA);
 //BA.debugLineNum = 125;BA.debugLine="PanelTempHumidityBasement.Height = ScrollViewBas";
mostCurrent._paneltemphumiditybasement.setHeight(mostCurrent._scrollviewbasement.getHeight());
 //BA.debugLineNum = 126;BA.debugLine="ScrollViewBasement.Panel.Height = PanelTempHumid";
mostCurrent._scrollviewbasement.getPanel().setHeight(mostCurrent._paneltemphumiditybasement.getHeight());
 //BA.debugLineNum = 127;BA.debugLine="TabStrip1.LoadLayout(\"AirQualityBasement\", \"BASE";
mostCurrent._tabstrip1.LoadLayout("AirQualityBasement",BA.ObjectToCharSequence("BASEMENT"+anywheresoftware.b4a.keywords.Common.CRLF+"Air Quality (CO)  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf289)))));
 //BA.debugLineNum = 128;BA.debugLine="TabStrip1.LoadLayout(\"blinkscrollview\", \"OUTSIDE";
mostCurrent._tabstrip1.LoadLayout("blinkscrollview",BA.ObjectToCharSequence("OUTSIDE"+anywheresoftware.b4a.keywords.Common.CRLF+"Security Camera  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf030)))));
 //BA.debugLineNum = 129;BA.debugLine="ScrollViewBlink.Panel.LoadLayout(\"blink\")";
mostCurrent._scrollviewblink.getPanel().LoadLayout("blink",mostCurrent.activityBA);
 //BA.debugLineNum = 131;BA.debugLine="ScrollViewBlink.panel.height = 910dip";
mostCurrent._scrollviewblink.getPanel().setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (910)));
 //BA.debugLineNum = 132;BA.debugLine="panelBlink.Height = 910dip";
mostCurrent._panelblink.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (910)));
 //BA.debugLineNum = 133;BA.debugLine="TabStrip1.LoadLayout(\"blinkactivity\", \"ACTIVITY\"";
mostCurrent._tabstrip1.LoadLayout("blinkactivity",BA.ObjectToCharSequence("ACTIVITY"+anywheresoftware.b4a.keywords.Common.CRLF+"Security Camera  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf030)))));
 //BA.debugLineNum = 135;BA.debugLine="For Each lbl As Label In GetAllTabLabels(TabStri";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
{
final anywheresoftware.b4a.BA.IterableList group25 = _getalltablabels(mostCurrent._tabstrip1);
final int groupLen25 = group25.getSize()
;int index25 = 0;
;
for (; index25 < groupLen25;index25++){
_lbl.setObject((android.widget.TextView)(group25.Get(index25)));
 //BA.debugLineNum = 137;BA.debugLine="lbl.SingleLine = False";
_lbl.setSingleLine(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 139;BA.debugLine="lbl.Typeface = Typeface.FONTAWESOME";
_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME());
 //BA.debugLineNum = 141;BA.debugLine="lbl.Padding = Array As Int(0, 0, 0, 0)";
_lbl.setPadding(new int[]{(int) (0),(int) (0),(int) (0),(int) (0)});
 }
};
 //BA.debugLineNum = 145;BA.debugLine="For Each v As View In GetAllTabLabels(TabStrip1)";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group30 = _getalltablabels(mostCurrent._tabstrip1);
final int groupLen30 = group30.getSize()
;int index30 = 0;
;
for (; index30 < groupLen30;index30++){
_v.setObject((android.view.View)(group30.Get(index30)));
 //BA.debugLineNum = 147;BA.debugLine="v.Width = 33%x";
_v.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA));
 }
};
 //BA.debugLineNum = 150;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 151;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 152;BA.debugLine="ACToolBarLight1.NavigationIconDrawable = bd";
mostCurrent._actoolbarlight1.setNavigationIconDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 153;BA.debugLine="ToolbarHelper.Initialize";
mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 154;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 155;BA.debugLine="ToolbarHelper.Title= cs.Initialize.Size(22).Appe";
mostCurrent._toolbarhelper.setTitle(BA.ObjectToCharSequence(_cs.Initialize().Size((int) (22)).Append(BA.ObjectToCharSequence("Smart Home Monitor")).PopAll().getObject()));
 //BA.debugLineNum = 156;BA.debugLine="ToolbarHelper.subTitle = \"\"";
mostCurrent._toolbarhelper.setSubtitle(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 157;BA.debugLine="ToolbarHelper.ShowUpIndicator = False 'set to tr";
mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 158;BA.debugLine="ACToolBarLight1.InitMenuListener";
mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 159;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 160;BA.debugLine="Dim xl As XmlLayoutBuilder";
_xl = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 161;BA.debugLine="jo.RunMethod(\"setPopupTheme\", Array(xl.GetResour";
_jo.RunMethod("setPopupTheme",new Object[]{(Object)(_xl.GetResourceId("style","ToolbarMenu"))});
 //BA.debugLineNum = 163;BA.debugLine="GaugeHumidity.SetRanges(Array(GaugeHumidity.Crea";
mostCurrent._gaugehumidity._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (70),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (70),(float) (80),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (80),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 166;BA.debugLine="GaugeTemp.SetRanges(Array(GaugeTemp.CreateRange(";
mostCurrent._gaugetemp._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 169;BA.debugLine="GaugeHeatIndex.SetRanges(Array(GaugeHeatIndex.Cr";
mostCurrent._gaugeheatindex._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 174;BA.debugLine="GaugeDewPoint.SetRanges(Array(GaugeDewPoint.Crea";
mostCurrent._gaugedewpoint._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (60.8),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (60.8),(float) (64.4),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (64.4),(float) (78.8),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (78.8),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 180;BA.debugLine="GaugeAirQuality.SetRanges(Array(GaugeAirQuality.";
mostCurrent._gaugeairquality._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (100),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (100),(float) (400),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (400),(float) (900),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (900),(float) (1000),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 184;BA.debugLine="GaugeAirQuality.CurrentValue=0";
mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 186;BA.debugLine="GaugeHumidityBasement.SetRanges(Array(GaugeHumid";
mostCurrent._gaugehumiditybasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (70),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (70),(float) (80),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (80),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 189;BA.debugLine="GaugeTempBasement.SetRanges(Array(GaugeTempBasem";
mostCurrent._gaugetempbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 192;BA.debugLine="GaugeHeatIndexBasement.SetRanges(Array(GaugeHeat";
mostCurrent._gaugeheatindexbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 197;BA.debugLine="GaugeDewPointBasement.SetRanges(Array(GaugeDewPo";
mostCurrent._gaugedewpointbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (60.8),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (60.8),(float) (64.4),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (64.4),(float) (78.8),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (78.8),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 203;BA.debugLine="GaugeAirQualityBasement.SetRanges(Array(GaugeAir";
mostCurrent._gaugeairqualitybasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (100),mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (100),(float) (400),mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (400),(float) (900),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (900),(float) (1000),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 207;BA.debugLine="GaugeAirQualityBasement.CurrentValue=0";
mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 209;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 210;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 211;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 212;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"The";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 213;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 214;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 215;BA.debugLine="lblPing.Visible = False";
mostCurrent._lblping.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 216;BA.debugLine="GaugeAirQuality.CurrentValue = 0";
mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 217;BA.debugLine="GaugeAirQualityBasement.CurrentValue = 0";
mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 218;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 219;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.Bol";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).PopAll().getObject()));
 //BA.debugLineNum = 221;BA.debugLine="lblPerceptionBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblperceptionbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 222;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.App";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 223;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 224;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bold.";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 225;BA.debugLine="lblPingBasement.Visible = False";
mostCurrent._lblpingbasement.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 226;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 227;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Initia";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).PopAll().getObject()));
 //BA.debugLineNum = 229;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 230;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Rea";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Read weather","utf8"));
 //BA.debugLineNum = 232;BA.debugLine="Dim strHumidityAddValue As String = StateManage";
_strhumidityaddvalue = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue");
 //BA.debugLineNum = 233;BA.debugLine="If strHumidityAddValue = \"\" Then";
if ((_strhumidityaddvalue).equals("")) { 
 //BA.debugLineNum = 234;BA.debugLine="strHumidityAddValue = \"0\"";
_strhumidityaddvalue = "0";
 };
 //BA.debugLineNum = 236;BA.debugLine="MQTT.Publish(\"HumidityAddValue\", bc.StringToByt";
_mqtt.Publish("HumidityAddValue",_bc.StringToBytes(_strhumidityaddvalue,"utf8"));
 };
 //BA.debugLineNum = 240;BA.debugLine="If FirstTime Or ivDriveway.Bitmap = Null Then";
if (_firsttime || mostCurrent._ivdriveway.getBitmap()== null) { 
 //BA.debugLineNum = 241;BA.debugLine="btnDriveway.Enabled = False";
mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 242;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 243;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",mostCurrent._ivdriveway);
 //BA.debugLineNum = 244;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",mostCurrent._ivfrontdoor);
 //BA.debugLineNum = 245;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",mostCurrent._ivsideyard);
 //BA.debugLineNum = 246;BA.debugLine="RequestAuthToken";
_requestauthtoken();
 };
 //BA.debugLineNum = 249;BA.debugLine="SetTextShadow(lblDriveway)";
_settextshadow(mostCurrent._lbldriveway);
 //BA.debugLineNum = 250;BA.debugLine="SetTextShadow(lblDrivewayBatt)";
_settextshadow(mostCurrent._lbldrivewaybatt);
 //BA.debugLineNum = 251;BA.debugLine="SetTextShadow(lblDrivewayTimestamp)";
_settextshadow(mostCurrent._lbldrivewaytimestamp);
 //BA.debugLineNum = 252;BA.debugLine="SetTextShadow(lblDrivewayWifi)";
_settextshadow(mostCurrent._lbldrivewaywifi);
 //BA.debugLineNum = 253;BA.debugLine="SetTextShadow(lblFrontDoor)";
_settextshadow(mostCurrent._lblfrontdoor);
 //BA.debugLineNum = 254;BA.debugLine="SetTextShadow(lblFrontDoorBatt)";
_settextshadow(mostCurrent._lblfrontdoorbatt);
 //BA.debugLineNum = 255;BA.debugLine="SetTextShadow(lblFrontDoorTimestamp)";
_settextshadow(mostCurrent._lblfrontdoortimestamp);
 //BA.debugLineNum = 256;BA.debugLine="SetTextShadow(lblFrontDoorWiFi)";
_settextshadow(mostCurrent._lblfrontdoorwifi);
 //BA.debugLineNum = 257;BA.debugLine="SetTextShadow(lblSideYard)";
_settextshadow(mostCurrent._lblsideyard);
 //BA.debugLineNum = 258;BA.debugLine="SetTextShadow(lblSideYardBatt)";
_settextshadow(mostCurrent._lblsideyardbatt);
 //BA.debugLineNum = 259;BA.debugLine="SetTextShadow(lblSideYardTimestamp)";
_settextshadow(mostCurrent._lblsideyardtimestamp);
 //BA.debugLineNum = 260;BA.debugLine="SetTextShadow(lblSideYardWiFi)";
_settextshadow(mostCurrent._lblsideyardwifi);
 //BA.debugLineNum = 261;BA.debugLine="SetTextShadow(lblSyncModule)";
_settextshadow(mostCurrent._lblsyncmodule);
 } 
       catch (Exception e105) {
			processBA.setLastException(e105); //BA.debugLineNum = 263;BA.debugLine="ToastMessageShow(LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 265;BA.debugLine="End Sub";
return "";
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
 //BA.debugLineNum = 542;BA.debugLine="Sub Activity_Createmenu(Menu As ACMenu)";
 //BA.debugLineNum = 543;BA.debugLine="Try";
try { //BA.debugLineNum = 544;BA.debugLine="Menu.Clear";
_menu.Clear();
 //BA.debugLineNum = 545;BA.debugLine="gblACMenu = Menu";
mostCurrent._gblacmenu = _menu;
 //BA.debugLineNum = 546;BA.debugLine="Menu.Add(0, 0, \"Settings\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Settings"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 548;BA.debugLine="Menu.Add(0, 0, \"About\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 } 
       catch (Exception e7) {
			processBA.setLastException(e7); //BA.debugLineNum = 550;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51376264",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 552;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 634;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 635;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 636;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (mostCurrent._tabstrip1.getCurrentPage()==2) { 
 //BA.debugLineNum = 637;BA.debugLine="TabStrip1.ScrollTo(1,True)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 638;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 //BA.debugLineNum = 639;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==1) { 
 //BA.debugLineNum = 641;BA.debugLine="TabStrip1.ScrollTo(0,True)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 642;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 //BA.debugLineNum = 643;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==3) { 
 //BA.debugLineNum = 645;BA.debugLine="TabStrip1.ScrollTo(2,True)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 646;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 //BA.debugLineNum = 647;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==4) { 
 //BA.debugLineNum = 649;BA.debugLine="TabStrip1.ScrollTo(3,True)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 650;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 //BA.debugLineNum = 651;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==5) { 
 //BA.debugLineNum = 653;BA.debugLine="TabStrip1.ScrollTo(4,True)";
mostCurrent._tabstrip1.ScrollTo((int) (4),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 654;BA.debugLine="TabStrip1_PageSelected(4)";
_tabstrip1_pageselected((int) (4));
 //BA.debugLineNum = 655;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 659;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 320;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 321;BA.debugLine="wvMedia.LoadUrl(\"\")";
mostCurrent._wvmedia.LoadUrl("");
 //BA.debugLineNum = 322;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _in = null;
String _notificationclicked = "";
 //BA.debugLineNum = 267;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 268;BA.debugLine="Try";
try { //BA.debugLineNum = 269;BA.debugLine="HandleSettings";
_handlesettings();
 //BA.debugLineNum = 270;BA.debugLine="Dim in As Intent = Activity.GetStartingIntent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
_in = mostCurrent._activity.GetStartingIntent();
 //BA.debugLineNum = 271;BA.debugLine="Dim NotificationClicked As String";
_notificationclicked = "";
 //BA.debugLineNum = 272;BA.debugLine="If in.IsInitialized And in <> OldIntent Then";
if (_in.IsInitialized() && (_in).equals(_oldintent) == false) { 
 //BA.debugLineNum = 273;BA.debugLine="OldIntent = in";
_oldintent = _in;
 //BA.debugLineNum = 274;BA.debugLine="If in.HasExtra(\"Notification_Tag\") Then";
if (_in.HasExtra("Notification_Tag")) { 
 //BA.debugLineNum = 275;BA.debugLine="NotificationClicked = in.GetExtra(\"Notificatio";
_notificationclicked = BA.ObjectToString(_in.GetExtra("Notification_Tag"));
 };
 };
 //BA.debugLineNum = 278;BA.debugLine="If NotificationClicked = \"Living area temperatur";
if ((_notificationclicked).equals("Living area temperature")) { 
 //BA.debugLineNum = 279;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 280;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 }else if((_notificationclicked).equals("Living area carbon monoxide")) { 
 //BA.debugLineNum = 282;BA.debugLine="TabStrip1.ScrollTo(1,False)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 283;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 }else if((_notificationclicked).equals("Basement temperature")) { 
 //BA.debugLineNum = 285;BA.debugLine="TabStrip1.ScrollTo(2,False)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 286;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 }else if((_notificationclicked).equals("Basement carbon monoxide")) { 
 //BA.debugLineNum = 288;BA.debugLine="TabStrip1.ScrollTo(3,False)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 289;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 }else if((_notificationclicked).equals("Basement DHT22 sensor issue")) { 
 //BA.debugLineNum = 291;BA.debugLine="TabStrip1.ScrollTo(2,False)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 292;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 }else if((_notificationclicked).equals("Living area DHT22 sensor issue")) { 
 //BA.debugLineNum = 294;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 295;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 }else if((_notificationclicked).equals("Living area CO sensor issue")) { 
 //BA.debugLineNum = 297;BA.debugLine="TabStrip1.ScrollTo(1,False)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 298;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 }else if((_notificationclicked).equals("Basement CO sensor issue")) { 
 //BA.debugLineNum = 300;BA.debugLine="TabStrip1.ScrollTo(3,False)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 301;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 }else {
 //BA.debugLineNum = 303;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 304;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 };
 } 
       catch (Exception e40) {
			processBA.setLastException(e40); //BA.debugLineNum = 308;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("5196649",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 311;BA.debugLine="Try";
try { //BA.debugLineNum = 312;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connected";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 313;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 } 
       catch (Exception e47) {
			processBA.setLastException(e47); //BA.debugLineNum = 316;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("5196657",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 318;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
int _result = 0;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 466;BA.debugLine="Sub ACToolBarLight1_MenuItemClick (Item As ACMenuI";
 //BA.debugLineNum = 467;BA.debugLine="Try";
try { //BA.debugLineNum = 468;BA.debugLine="If Item.Title = \"About\" Then";
if ((_item.getTitle()).equals("About")) { 
 //BA.debugLineNum = 469;BA.debugLine="ShowAboutMenu";
_showaboutmenu();
 }else if((_item.getTitle()).equals("Settings")) { 
 //BA.debugLineNum = 471;BA.debugLine="StartActivity(screen.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_screen.CreateIntent()));
 }else if((_item.getTitle()).equals("Restart board")) { 
 //BA.debugLineNum = 473;BA.debugLine="Try";
try { //BA.debugLineNum = 474;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 475;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 476;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets,";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 477;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (mostCurrent._tabstrip1.getCurrentPage()==2) { 
 //BA.debugLineNum = 478;BA.debugLine="result = Msgbox2(\"Restart the BASEMENT contro";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 479;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 480;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 481;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 483;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringT";
_mqtt.Publish("TempHumidBasement",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else if(mostCurrent._tabstrip1.getCurrentPage()==1) { 
 //BA.debugLineNum = 486;BA.debugLine="result = Msgbox2(\"Restart the AIR QUALITY con";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 487;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 488;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 489;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 491;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Restar";
_mqtt.Publish("MQ7",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else if(mostCurrent._tabstrip1.getCurrentPage()==3) { 
 //BA.debugLineNum = 494;BA.debugLine="result = Msgbox2(\"Restart the BASEMENT AIR QU";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 495;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 496;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 497;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 499;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes";
_mqtt.Publish("MQ7Basement",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else {
 //BA.debugLineNum = 502;BA.debugLine="result = Msgbox2(\"Restart the WEATHER control";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the WEATHER controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 503;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 504;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 505;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 507;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Restart controller","utf8"));
 };
 };
 } 
       catch (Exception e45) {
			processBA.setLastException(e45); //BA.debugLineNum = 511;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51179693",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 };
 } 
       catch (Exception e49) {
			processBA.setLastException(e49); //BA.debugLineNum = 515;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51179697",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 517;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1587;BA.debugLine="Private Sub Blur (bmp As B4XBitmap) As B4XBitmap";
 //BA.debugLineNum = 1588;BA.debugLine="Dim bcBitmap As BitmapCreator";
_bcbitmap = new b4a.example.bitmapcreator();
 //BA.debugLineNum = 1589;BA.debugLine="Dim ReduceScale As Int = 2";
_reducescale = (int) (2);
 //BA.debugLineNum = 1590;BA.debugLine="bcBitmap.Initialize(bmp.Width / ReduceScale / bmp";
_bcbitmap._initialize(processBA,(int) (_bmp.getWidth()/(double)_reducescale/(double)_bmp.getScale()),(int) (_bmp.getHeight()/(double)_reducescale/(double)_bmp.getScale()));
 //BA.debugLineNum = 1591;BA.debugLine="bcBitmap.CopyPixelsFromBitmap(bmp)";
_bcbitmap._copypixelsfrombitmap(_bmp);
 //BA.debugLineNum = 1592;BA.debugLine="Dim count As Int = 3";
_count = (int) (3);
 //BA.debugLineNum = 1593;BA.debugLine="Dim clrs(3) As ARGBColor";
_clrs = new b4a.example.bitmapcreator._argbcolor[(int) (3)];
{
int d0 = _clrs.length;
for (int i0 = 0;i0 < d0;i0++) {
_clrs[i0] = new b4a.example.bitmapcreator._argbcolor();
}
}
;
 //BA.debugLineNum = 1594;BA.debugLine="Dim temp As ARGBColor";
_temp = new b4a.example.bitmapcreator._argbcolor();
 //BA.debugLineNum = 1595;BA.debugLine="Dim m As Int";
_m = 0;
 //BA.debugLineNum = 1596;BA.debugLine="For steps = 1 To count";
{
final int step9 = 1;
final int limit9 = _count;
_steps = (int) (1) ;
for (;_steps <= limit9 ;_steps = _steps + step9 ) {
 //BA.debugLineNum = 1597;BA.debugLine="For y = 0 To bcBitmap.mHeight - 1";
{
final int step10 = 1;
final int limit10 = (int) (_bcbitmap._mheight-1);
_y = (int) (0) ;
for (;_y <= limit10 ;_y = _y + step10 ) {
 //BA.debugLineNum = 1598;BA.debugLine="For x = 0 To 2";
{
final int step11 = 1;
final int limit11 = (int) (2);
_x = (int) (0) ;
for (;_x <= limit11 ;_x = _x + step11 ) {
 //BA.debugLineNum = 1599;BA.debugLine="bcBitmap.GetARGB(x, y, clrs(x))";
_bcbitmap._getargb(_x,_y,_clrs[_x]);
 }
};
 //BA.debugLineNum = 1601;BA.debugLine="SetAvg(bcBitmap, 1, y, clrs, temp)";
_setavg(_bcbitmap,(int) (1),_y,_clrs,_temp);
 //BA.debugLineNum = 1602;BA.debugLine="m = 0";
_m = (int) (0);
 //BA.debugLineNum = 1603;BA.debugLine="For x = 2 To bcBitmap.mWidth - 2";
{
final int step16 = 1;
final int limit16 = (int) (_bcbitmap._mwidth-2);
_x = (int) (2) ;
for (;_x <= limit16 ;_x = _x + step16 ) {
 //BA.debugLineNum = 1604;BA.debugLine="bcBitmap.GetARGB(x + 1, y, clrs(m))";
_bcbitmap._getargb((int) (_x+1),_y,_clrs[_m]);
 //BA.debugLineNum = 1605;BA.debugLine="m = (m + 1) Mod clrs.Length";
_m = (int) ((_m+1)%_clrs.length);
 //BA.debugLineNum = 1606;BA.debugLine="SetAvg(bcBitmap, x, y, clrs, temp)";
_setavg(_bcbitmap,_x,_y,_clrs,_temp);
 }
};
 }
};
 //BA.debugLineNum = 1609;BA.debugLine="For x = 0 To bcBitmap.mWidth - 1";
{
final int step22 = 1;
final int limit22 = (int) (_bcbitmap._mwidth-1);
_x = (int) (0) ;
for (;_x <= limit22 ;_x = _x + step22 ) {
 //BA.debugLineNum = 1610;BA.debugLine="For y = 0 To 2";
{
final int step23 = 1;
final int limit23 = (int) (2);
_y = (int) (0) ;
for (;_y <= limit23 ;_y = _y + step23 ) {
 //BA.debugLineNum = 1611;BA.debugLine="bcBitmap.GetARGB(x, y, clrs(y))";
_bcbitmap._getargb(_x,_y,_clrs[_y]);
 }
};
 //BA.debugLineNum = 1613;BA.debugLine="SetAvg(bcBitmap, x, 1, clrs, temp)";
_setavg(_bcbitmap,_x,(int) (1),_clrs,_temp);
 //BA.debugLineNum = 1614;BA.debugLine="m = 0";
_m = (int) (0);
 //BA.debugLineNum = 1615;BA.debugLine="For y = 2 To bcBitmap.mHeight - 2";
{
final int step28 = 1;
final int limit28 = (int) (_bcbitmap._mheight-2);
_y = (int) (2) ;
for (;_y <= limit28 ;_y = _y + step28 ) {
 //BA.debugLineNum = 1616;BA.debugLine="bcBitmap.GetARGB(x, y + 1, clrs(m))";
_bcbitmap._getargb(_x,(int) (_y+1),_clrs[_m]);
 //BA.debugLineNum = 1617;BA.debugLine="m = (m + 1) Mod clrs.Length";
_m = (int) ((_m+1)%_clrs.length);
 //BA.debugLineNum = 1618;BA.debugLine="SetAvg(bcBitmap, x, y, clrs, temp)";
_setavg(_bcbitmap,_x,_y,_clrs,_temp);
 }
};
 }
};
 }
};
 //BA.debugLineNum = 1622;BA.debugLine="Return bcBitmap.Bitmap";
if (true) return _bcbitmap._getbitmap();
 //BA.debugLineNum = 1623;BA.debugLine="End Sub";
return null;
}
public static String  _bluriv(String _image,anywheresoftware.b4a.objects.ImageViewWrapper _iv) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _bmp = null;
 //BA.debugLineNum = 1577;BA.debugLine="Sub BlurIV (image As String,iv As ImageView)";
 //BA.debugLineNum = 1578;BA.debugLine="Try";
try { //BA.debugLineNum = 1579;BA.debugLine="Dim bmp As B4XBitmap = xui.LoadBitmapResize(File";
_bmp = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
_bmp = mostCurrent._xui.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_image,_iv.getWidth(),_iv.getHeight(),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1580;BA.debugLine="iv.Bitmap = (Blur(bmp))";
_iv.setBitmap((android.graphics.Bitmap)((_blur(_bmp)).getObject()));
 } 
       catch (Exception e5) {
			processBA.setLastException(e5); //BA.debugLineNum = 1582;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("53407877",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1585;BA.debugLine="End Sub";
return "";
}
public static String  _btndriveway_click() throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 1564;BA.debugLine="Sub btnDriveway_Click";
 //BA.debugLineNum = 1565;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 1566;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"0";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 1567;BA.debugLine="If Msgbox2(\"Refresh all cameras?\", \"Smart Home Mo";
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Refresh all cameras?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 1568;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 1569;BA.debugLine="btnDriveway.Enabled = False";
mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1570;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",mostCurrent._ivdriveway);
 //BA.debugLineNum = 1571;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",mostCurrent._ivfrontdoor);
 //BA.debugLineNum = 1572;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",mostCurrent._ivsideyard);
 //BA.debugLineNum = 1573;BA.debugLine="RefreshCameras(False)";
_refreshcameras(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1575;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 661;BA.debugLine="Sub CheckAirQualitySetting";
 //BA.debugLineNum = 662;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 663;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 664;BA.debugLine="Try";
try { //BA.debugLineNum = 665;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 666;BA.debugLine="status = StateManager.GetSetting(\"AirQuality\")";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"AirQuality");
 //BA.debugLineNum = 667;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 668;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 669;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 670;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 671;BA.debugLine="GaugeAirQuality.CurrentValue = a(0)";
mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (0)])));
 //BA.debugLineNum = 672;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 673;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 675;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 };
 //BA.debugLineNum = 677;BA.debugLine="If a(1) = \"\" Then";
if ((_a[(int) (1)]).equals("")) { 
 //BA.debugLineNum = 678;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 679;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 680;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 681;BA.debugLine="a(1) = DateTime.Date(Tomorrow)";
_a[(int) (1)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 683;BA.debugLine="If a(2).Contains(\"|24:\") Then";
if (_a[(int) (2)].contains("|24:")) { 
 //BA.debugLineNum = 684;BA.debugLine="a(2) = a(2).Replace(\"|24:\",\"|00:\")";
_a[(int) (2)] = _a[(int) (2)].replace("|24:","|00:");
 //BA.debugLineNum = 685;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 686;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 687;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 688;BA.debugLine="a(2) = DateTime.Date(Tomorrow)";
_a[(int) (2)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 690;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 691;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 692;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 693;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 694;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 696;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 697;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 699;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 702;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.B";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e44) {
			processBA.setLastException(e44); //BA.debugLineNum = 706;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51769517",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 707;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.Bol";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 709;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 711;BA.debugLine="Sub CheckAirQualitySettingBasement";
 //BA.debugLineNum = 712;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 713;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 714;BA.debugLine="Try";
try { //BA.debugLineNum = 715;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 716;BA.debugLine="status = StateManager.GetSetting(\"AirQualityBase";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"AirQualityBasement");
 //BA.debugLineNum = 717;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 718;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 719;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 720;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 721;BA.debugLine="GaugeAirQualityBasement.CurrentValue = a(0)";
mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (0)])));
 //BA.debugLineNum = 722;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 723;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bo";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 725;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bo";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 };
 //BA.debugLineNum = 727;BA.debugLine="If a(1) = \"\" Then";
if ((_a[(int) (1)]).equals("")) { 
 //BA.debugLineNum = 728;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 729;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 730;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 731;BA.debugLine="a(1) = DateTime.Date(Tomorrow)";
_a[(int) (1)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 733;BA.debugLine="If a(2).Contains(\"|24:\") Then";
if (_a[(int) (2)].contains("|24:")) { 
 //BA.debugLineNum = 734;BA.debugLine="a(2) = a(2).Replace(\"|24:\",\"|00:\")";
_a[(int) (2)] = _a[(int) (2)].replace("|24:","|00:");
 //BA.debugLineNum = 735;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 736;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 737;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 738;BA.debugLine="a(2) = DateTime.Date(Tomorrow)";
_a[(int) (2)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 741;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 742;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 743;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 744;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 745;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 747;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 748;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Ini";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 750;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Ini";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 753;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Init";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdatebasement.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e44) {
			processBA.setLastException(e44); //BA.debugLineNum = 757;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51835054",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 758;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Initia";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 760;BA.debugLine="End Sub";
return "";
}
public static String  _checkbattlife(int _battlevel) throws Exception{
 //BA.debugLineNum = 1494;BA.debugLine="Sub CheckBattLife(battlevel As Int) As String";
 //BA.debugLineNum = 1495;BA.debugLine="Try";
try { //BA.debugLineNum = 1497;BA.debugLine="If battlevel <= 136 Then";
if (_battlevel<=136) { 
 //BA.debugLineNum = 1498;BA.debugLine="Return \"Replace battery now!\"";
if (true) return "Replace battery now!";
 }else if(_battlevel>=160) { 
 //BA.debugLineNum = 1500;BA.debugLine="Return \"Very high\"";
if (true) return "Very high";
 }else if(_battlevel>136 && _battlevel<138) { 
 //BA.debugLineNum = 1502;BA.debugLine="Return \"Very low\"";
if (true) return "Very low";
 }else {
 //BA.debugLineNum = 1504;BA.debugLine="Return \"High\"";
if (true) return "High";
 };
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 1507;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("53080205",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1509;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 1511;BA.debugLine="End Sub";
return "";
}
public static String  _checklfrlevel(int _lfrlevel) throws Exception{
 //BA.debugLineNum = 1513;BA.debugLine="Sub CheckLFRLevel(lfrlevel As Int) As String";
 //BA.debugLineNum = 1514;BA.debugLine="Try";
try { //BA.debugLineNum = 1516;BA.debugLine="If lfrlevel > -67 Then";
if (_lfrlevel>-67) { 
 //BA.debugLineNum = 1517;BA.debugLine="Return \"Amazing\"";
if (true) return "Amazing";
 }else if(_lfrlevel>-70 && _lfrlevel<=-67) { 
 //BA.debugLineNum = 1519;BA.debugLine="Return \"Very good\"";
if (true) return "Very good";
 }else if(_lfrlevel>-80 && _lfrlevel<=-70) { 
 //BA.debugLineNum = 1521;BA.debugLine="Return \"OK\"";
if (true) return "OK";
 }else if(_lfrlevel>-90 && _lfrlevel<=-80) { 
 //BA.debugLineNum = 1523;BA.debugLine="Return \"Not Good\"";
if (true) return "Not Good";
 }else {
 //BA.debugLineNum = 1525;BA.debugLine="Return \"Unusable\"";
if (true) return "Unusable";
 };
 } 
       catch (Exception e14) {
			processBA.setLastException(e14); //BA.debugLineNum = 1528;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("53145743",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1529;BA.debugLine="lblStatus.Text = \"CheckLFRLevel LastException: \"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("CheckLFRLevel LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))));
 //BA.debugLineNum = 1530;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 1532;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 762;BA.debugLine="Sub CheckTempHumiditySetting";
 //BA.debugLineNum = 763;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 764;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 765;BA.debugLine="Try";
try { //BA.debugLineNum = 766;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 767;BA.debugLine="status = StateManager.GetSetting(\"TempHumidity\")";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"TempHumidity");
 //BA.debugLineNum = 768;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 769;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 770;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 771;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 772;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 773;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 774;BA.debugLine="GaugeTemp.CurrentValue = a(1)";
mostCurrent._gaugetemp._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 775;BA.debugLine="GaugeHumidity.CurrentValue = a(2)";
mostCurrent._gaugehumidity._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 776;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 777;BA.debugLine="If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6)) || (_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 778;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 780;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 782;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 };
 //BA.debugLineNum = 785;BA.debugLine="GaugeHeatIndex.CurrentValue = a(5)";
mostCurrent._gaugeheatindex._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 786;BA.debugLine="GaugeDewPoint.CurrentValue = a(6)";
mostCurrent._gaugedewpoint._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 787;BA.debugLine="If a(7) = \"\" Then";
if ((_a[(int) (7)]).equals("")) { 
 //BA.debugLineNum = 788;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 789;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 790;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 791;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 793;BA.debugLine="If a(8).Contains(\"|24:\") Then";
if (_a[(int) (8)].contains("|24:")) { 
 //BA.debugLineNum = 794;BA.debugLine="a(8) = a(8).Replace(\"|24:\",\"|00:\")";
_a[(int) (8)] = _a[(int) (8)].replace("|24:","|00:");
 //BA.debugLineNum = 795;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 796;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 797;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 798;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 800;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 801;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 802;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 803;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 804;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 806;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 807;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appen";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 809;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appen";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 812;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e52) {
			processBA.setLastException(e52); //BA.debugLineNum = 816;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51900598",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 817;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 819;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 821;BA.debugLine="Sub CheckTempHumiditySettingBasement";
 //BA.debugLineNum = 822;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 823;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 824;BA.debugLine="Try";
try { //BA.debugLineNum = 825;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 826;BA.debugLine="status = StateManager.GetSetting(\"TempHumidityBa";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"TempHumidityBasement");
 //BA.debugLineNum = 827;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 828;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 829;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 830;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 831;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 832;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 833;BA.debugLine="GaugeTempBasement.CurrentValue = a(1)";
mostCurrent._gaugetempbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 834;BA.debugLine="GaugeHumidityBasement.CurrentValue = a(2)";
mostCurrent._gaugehumiditybasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 835;BA.debugLine="lblPerceptionBasement.Text = cs.Initialize.Bol";
mostCurrent._lblperceptionbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 836;BA.debugLine="If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6)) || (_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 837;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 839;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 841;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 };
 //BA.debugLineNum = 843;BA.debugLine="GaugeHeatIndexBasement.CurrentValue = a(5)";
mostCurrent._gaugeheatindexbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 844;BA.debugLine="GaugeDewPointBasement.CurrentValue = a(6)";
mostCurrent._gaugedewpointbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 845;BA.debugLine="If a(7) = \"\" Then";
if ((_a[(int) (7)]).equals("")) { 
 //BA.debugLineNum = 846;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 847;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 848;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 849;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 851;BA.debugLine="If a(8).Contains(\"|24:\") Then";
if (_a[(int) (8)].contains("|24:")) { 
 //BA.debugLineNum = 852;BA.debugLine="a(8) = a(8).Replace(\"|24:\",\"|00:\")";
_a[(int) (8)] = _a[(int) (8)].replace("|24:","|00:");
 //BA.debugLineNum = 853;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 854;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 855;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 856;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 858;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 859;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 860;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 861;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 862;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 864;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 865;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bo";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 867;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bo";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 870;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bol";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdatebasement.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e52) {
			processBA.setLastException(e52); //BA.debugLineNum = 874;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51966133",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 875;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bold.";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 877;BA.debugLine="End Sub";
return "";
}
public static String  _clvactivity_itemclick(int _index,Object _value) throws Exception{
String _video = "";
String _videourl = "";
 //BA.debugLineNum = 1770;BA.debugLine="Sub clvActivity_ItemClick (Index As Int, Value As";
 //BA.debugLineNum = 1771;BA.debugLine="Try";
try { //BA.debugLineNum = 1772;BA.debugLine="wvMedia.LoadUrl(\"\")";
mostCurrent._wvmedia.LoadUrl("");
 //BA.debugLineNum = 1773;BA.debugLine="Dim video As String";
_video = "";
 //BA.debugLineNum = 1774;BA.debugLine="video = clvActivity.GetValue(Index)";
_video = BA.ObjectToString(mostCurrent._clvactivity._getvalue(_index));
 //BA.debugLineNum = 1775;BA.debugLine="Dim videoURL As String = Regex.Split(\"\\|\",video)";
_videourl = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_video)[(int) (0)];
 //BA.debugLineNum = 1776;BA.debugLine="ShowVideo(videoURL)";
_showvideo(_videourl);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 1778;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("528246024",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1781;BA.debugLine="End Sub";
return "";
}
public static String  _convertdatetime(String _inputtime) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
 //BA.debugLineNum = 1534;BA.debugLine="Sub ConvertDateTime(inputTime As String) As String";
 //BA.debugLineNum = 1536;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 1537;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 1538;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 1541;BA.debugLine="Return DateTime.Date(lngTicks)";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 //BA.debugLineNum = 1542;BA.debugLine="End Sub";
return "";
}
public static String  _convertdatetimeperiod(String _inputtime) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 1783;BA.debugLine="Sub ConvertDateTimePeriod(inputTime As String) As";
 //BA.debugLineNum = 1785;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 1786;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 1787;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 1788;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngTick";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 1790;BA.debugLine="If lblDate.Text.Contains(\"Today\") Then";
if (mostCurrent._lbldate.getText().contains("Today")) { 
 //BA.debugLineNum = 1791;BA.debugLine="If p.Days = 0 Then";
if (_p.Days==0) { 
 //BA.debugLineNum = 1792;BA.debugLine="If p.Hours = 0 Then";
if (_p.Hours==0) { 
 //BA.debugLineNum = 1793;BA.debugLine="If p.Minutes = 0 Then";
if (_p.Minutes==0) { 
 //BA.debugLineNum = 1794;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 1796;BA.debugLine="Return p.Minutes & \"m \" & p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 1799;BA.debugLine="Return p.Hours & \"h \" & p.Minutes & \"m \" & p.S";
if (true) return BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else if(_p.Hours==0) { 
 //BA.debugLineNum = 1802;BA.debugLine="If p.Minutes = 0 Then";
if (_p.Minutes==0) { 
 //BA.debugLineNum = 1803;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 1805;BA.debugLine="Return p.Minutes & \"m \" & p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else if(_p.Minutes==0) { 
 //BA.debugLineNum = 1808;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 1810;BA.debugLine="Return p.Days & \"d \" & p.Hours & \"h \" & p.Minut";
if (true) return BA.NumberToString(_p.Days)+"d "+BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else if(mostCurrent._lbldate.getText().contains("Yesterday")) { 
 //BA.debugLineNum = 1813;BA.debugLine="If p.Days = 0 Then";
if (_p.Days==0) { 
 //BA.debugLineNum = 1814;BA.debugLine="If p.Hours = 0 Then";
if (_p.Hours==0) { 
 //BA.debugLineNum = 1815;BA.debugLine="If p.Minutes = 0 Then";
if (_p.Minutes==0) { 
 //BA.debugLineNum = 1816;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 1818;BA.debugLine="Return p.Minutes & \"m \" & p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 1821;BA.debugLine="Return p.Hours & \"h \" & p.Minutes & \"m \" & p.S";
if (true) return BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 1824;BA.debugLine="Return p.Days & \"d \" & p.Hours & \"h \" & p.Minut";
if (true) return BA.NumberToString(_p.Days)+"d "+BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 1827;BA.debugLine="Return p.Days & \"d \" & p.Hours & \"h \" & p.Minute";
if (true) return BA.NumberToString(_p.Days)+"d "+BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 //BA.debugLineNum = 1830;BA.debugLine="End Sub";
return "";
}
public static String  _convertfulldatetime(String _inputtime) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
long _yesterday = 0L;
long _timestamp = 0L;
 //BA.debugLineNum = 1832;BA.debugLine="Sub ConvertFullDateTime(inputTime As String) As St";
 //BA.debugLineNum = 1834;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 1835;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 1836;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 1838;BA.debugLine="Dim Yesterday As Long";
_yesterday = 0L;
 //BA.debugLineNum = 1839;BA.debugLine="Dim timestamp As Long";
_timestamp = 0L;
 //BA.debugLineNum = 1840;BA.debugLine="DateTime.DateFormat = \"yyyyMMdd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyyMMdd");
 //BA.debugLineNum = 1841;BA.debugLine="Yesterday = DateTime.Date(DateTime.add(DateTime.N";
_yesterday = (long)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1)))));
 //BA.debugLineNum = 1842;BA.debugLine="timestamp = DateTime.Date(lngTicks)";
_timestamp = (long)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks)));
 //BA.debugLineNum = 1844;BA.debugLine="DateTime.DateFormat = \"h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("h:mm:ss a");
 //BA.debugLineNum = 1845;BA.debugLine="If DateUtils.IsSameDay(lngTicks,DateTime.now) The";
if (mostCurrent._dateutils._issameday(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow())) { 
 //BA.debugLineNum = 1846;BA.debugLine="Return \"Today \" & DateTime.Date(lngTicks)";
if (true) return "Today "+anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 }else if(_yesterday==_timestamp) { 
 //BA.debugLineNum = 1848;BA.debugLine="Return \"Yesterday \" & DateTime.Date(lngTicks)";
if (true) return "Yesterday "+anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 }else {
 //BA.debugLineNum = 1850;BA.debugLine="Return DateUtils.GetDayOfWeekName(lngTicks) & \"";
if (true) return mostCurrent._dateutils._getdayofweekname(mostCurrent.activityBA,_lngticks)+" "+anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 };
 //BA.debugLineNum = 1852;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.B4XViewWrapper  _createlistitem(anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _screenshot,String _fileinfo,String _devicename) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
 //BA.debugLineNum = 1749;BA.debugLine="Sub CreateListItem(screenshot As B4XBitmap, filein";
 //BA.debugLineNum = 1750;BA.debugLine="Try";
try { //BA.debugLineNum = 1754;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 1757;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, clvActivity.AsView.";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),mostCurrent._clvactivity._asview().getWidth(),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 1758;BA.debugLine="p.LoadLayout(\"blinkcellitem\")";
_p.LoadLayout("blinkcellitem",mostCurrent.activityBA);
 //BA.debugLineNum = 1759;BA.debugLine="ivScreenshot.Bitmap = screenshot";
mostCurrent._ivscreenshot.setBitmap((android.graphics.Bitmap)(_screenshot.getObject()));
 //BA.debugLineNum = 1760;BA.debugLine="lblDate.Text = \"   \" & ConvertFullDateTime(filei";
mostCurrent._lbldate.setText(BA.ObjectToCharSequence("   "+_convertfulldatetime(_fileinfo)));
 //BA.debugLineNum = 1761;BA.debugLine="lblFileInfo.Text = \"   \" & ConvertDateTimePeriod";
mostCurrent._lblfileinfo.setText(BA.ObjectToCharSequence("   "+_convertdatetimeperiod(_fileinfo)));
 //BA.debugLineNum = 1762;BA.debugLine="lblDeviceInfo.Text = \"   \" & devicename";
mostCurrent._lbldeviceinfo.setText(BA.ObjectToCharSequence("   "+_devicename));
 //BA.debugLineNum = 1763;BA.debugLine="Return p";
if (true) return _p;
 } 
       catch (Exception e11) {
			processBA.setLastException(e11); //BA.debugLineNum = 1765;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("527131920",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1766;BA.debugLine="Return Null";
if (true) return (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(anywheresoftware.b4a.keywords.Common.Null));
 };
 //BA.debugLineNum = 1768;BA.debugLine="End Sub";
return null;
}
public static String  _createpreferencescreen() throws Exception{
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat1 = null;
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat2 = null;
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat3 = null;
anywheresoftware.b4a.objects.IntentWrapper _in = null;
 //BA.debugLineNum = 879;BA.debugLine="Sub CreatePreferenceScreen";
 //BA.debugLineNum = 880;BA.debugLine="screen.Initialize(\"Settings\", \"\")";
_screen.Initialize("Settings","");
 //BA.debugLineNum = 882;BA.debugLine="Dim cat1,cat2,cat3 As AHPreferenceCategory";
_cat1 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat2 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat3 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
 //BA.debugLineNum = 884;BA.debugLine="cat1.Initialize(\"Temperature & Humidity\")";
_cat1.Initialize("Temperature & Humidity");
 //BA.debugLineNum = 885;BA.debugLine="cat1.AddEditText(\"TempHumidityCooldownTime\", \"Liv";
_cat1.AddEditText("TempHumidityCooldownTime","Living Area Cooldown Time","Minimum creation time interval between new notification","5","");
 //BA.debugLineNum = 886;BA.debugLine="cat1.AddEditText(\"TempHumidityCooldownTimeBasemen";
_cat1.AddEditText("TempHumidityCooldownTimeBasement","Basement Cooldown Time","Minimum creation time interval between new notification","5","");
 //BA.debugLineNum = 887;BA.debugLine="cat1.AddEditText(\"HumidityAddValue\", \"Humidity Ad";
_cat1.AddEditText("HumidityAddValue","Humidity Additional Value","Value to be added to humidity to improve accuracy","0","");
 //BA.debugLineNum = 889;BA.debugLine="cat2.Initialize(\"Special Settings\")";
_cat2.Initialize("Special Settings");
 //BA.debugLineNum = 890;BA.debugLine="Dim In As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 891;BA.debugLine="In.Initialize(\"android.settings.ACTION_NOTIFICATI";
_in.Initialize("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS","");
 //BA.debugLineNum = 892;BA.debugLine="cat2.AddIntent(\"Notification Access\", \"Enable or";
_cat2.AddIntent("Notification Access","Enable or disable listening to notifications",(android.content.Intent)(_in.getObject()),BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 894;BA.debugLine="cat3.Initialize(\"Sensors\")";
_cat3.Initialize("Sensors");
 //BA.debugLineNum = 895;BA.debugLine="cat3.AddEditText(\"SensorNotRespondingTime\", \"Sens";
_cat3.AddEditText("SensorNotRespondingTime","Sensor Not Responding","Data age when to restart sensor","5","");
 //BA.debugLineNum = 897;BA.debugLine="screen.AddPreferenceCategory(cat2)";
_screen.AddPreferenceCategory(_cat2);
 //BA.debugLineNum = 898;BA.debugLine="screen.AddPreferenceCategory(cat1)";
_screen.AddPreferenceCategory(_cat1);
 //BA.debugLineNum = 899;BA.debugLine="screen.AddPreferenceCategory(cat3)";
_screen.AddPreferenceCategory(_cat3);
 //BA.debugLineNum = 900;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTime","5");
 //BA.debugLineNum = 901;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 902;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",\"0\")";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue","0");
 //BA.debugLineNum = 903;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime","5");
 //BA.debugLineNum = 904;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 905;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1168;BA.debugLine="Try";
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
 //BA.debugLineNum = 1169;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1170;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1171;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1172;BA.debugLine="j.Download(Link)";
_j._download /*String*/ (_link);
 //BA.debugLineNum = 1173;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1174;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 13;
return;
case 13:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1175;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 1189;BA.debugLine="iv.Bitmap = j.GetBitmap";
_iv.setBitmap((android.graphics.Bitmap)(_j._getbitmap /*anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper*/ ().getObject()));
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1191;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1192;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;

case 9:
//C
this.state = 12;
;
 //BA.debugLineNum = 1194;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 11:
//C
this.state = 12;
this.catchState = 0;
 //BA.debugLineNum = 1197;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("52490398",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 12:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1199;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 390;BA.debugLine="Sub GetAirQuality(number As Int) As String";
 //BA.debugLineNum = 393;BA.debugLine="If number <= 100 Then";
if (_number<=100) { 
 //BA.debugLineNum = 394;BA.debugLine="Return(\"Carbon monoxide perfect\")";
if (true) return ("Carbon monoxide perfect");
 }else if(((_number>100) && (_number<400)) || _number==400) { 
 //BA.debugLineNum = 396;BA.debugLine="Return(\"Carbon monoxide normal\")";
if (true) return ("Carbon monoxide normal");
 }else if(((_number>400) && (_number<900)) || _number==900) { 
 //BA.debugLineNum = 398;BA.debugLine="Return(\"Carbon monoxide high\")";
if (true) return ("Carbon monoxide high");
 }else if(_number>900) { 
 //BA.debugLineNum = 400;BA.debugLine="Return(\"ALARM Carbon monoxide very high\")";
if (true) return ("ALARM Carbon monoxide very high");
 }else {
 //BA.debugLineNum = 402;BA.debugLine="Return(\"MQ-7 - cant read any value - check the s";
if (true) return ("MQ-7 - cant read any value - check the sensor!");
 };
 //BA.debugLineNum = 404;BA.debugLine="End Sub";
return "";
}
public static String  _getallcameras(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.List _networks = null;
anywheresoftware.b4a.objects.collections.Map _colnetworks = null;
 //BA.debugLineNum = 1260;BA.debugLine="Sub GetAllCameras(json As String)";
 //BA.debugLineNum = 1261;BA.debugLine="Try";
try { //BA.debugLineNum = 1262;BA.debugLine="lblStatus.Text = \"Getting all cameras informatio";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Getting all cameras information..."));
 //BA.debugLineNum = 1263;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1264;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1265;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1269;BA.debugLine="Dim networks As List = root.Get(\"networks\")";
_networks = new anywheresoftware.b4a.objects.collections.List();
_networks.setObject((java.util.List)(_root.Get((Object)("networks"))));
 //BA.debugLineNum = 1270;BA.debugLine="For Each colnetworks As Map In networks";
_colnetworks = new anywheresoftware.b4a.objects.collections.Map();
{
final anywheresoftware.b4a.BA.IterableList group7 = _networks;
final int groupLen7 = group7.getSize()
;int index7 = 0;
;
for (; index7 < groupLen7;index7++){
_colnetworks.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(group7.Get(index7)));
 //BA.debugLineNum = 1279;BA.debugLine="networkID = colnetworks.Get(\"network_id\")";
_networkid = BA.ObjectToString(_colnetworks.Get((Object)("network_id")));
 //BA.debugLineNum = 1280;BA.debugLine="Log(\"networkID: \" &  networkID)";
anywheresoftware.b4a.keywords.Common.LogImpl("52686996","networkID: "+_networkid,0);
 }
};
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 1284;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("52687000",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1287;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _getalltablabels(anywheresoftware.b4a.objects.TabStripViewPager _tabstrip) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
anywheresoftware.b4a.objects.PanelWrapper _tc = null;
anywheresoftware.b4a.objects.collections.List _res = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
 //BA.debugLineNum = 621;BA.debugLine="Public Sub GetAllTabLabels (tabstrip As TabStrip)";
 //BA.debugLineNum = 622;BA.debugLine="Dim jo As JavaObject = tabstrip";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_tabstrip));
 //BA.debugLineNum = 623;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 624;BA.debugLine="r.Target = jo.GetField(\"tabStrip\")";
_r.Target = _jo.GetField("tabStrip");
 //BA.debugLineNum = 625;BA.debugLine="Dim tc As Panel = r.GetField(\"tabsContainer\")";
_tc = new anywheresoftware.b4a.objects.PanelWrapper();
_tc.setObject((android.view.ViewGroup)(_r.GetField("tabsContainer")));
 //BA.debugLineNum = 626;BA.debugLine="Dim res As List";
_res = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 627;BA.debugLine="res.Initialize";
_res.Initialize();
 //BA.debugLineNum = 628;BA.debugLine="For Each v As View In tc";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group7 = _tc;
final int groupLen7 = group7.getSize()
;int index7 = 0;
;
for (; index7 < groupLen7;index7++){
_v.setObject((android.view.View)(group7.Get(index7)));
 //BA.debugLineNum = 629;BA.debugLine="If v Is Label Then res.Add(v)";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
_res.Add((Object)(_v.getObject()));};
 }
};
 //BA.debugLineNum = 631;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 632;BA.debugLine="End Sub";
return null;
}
public static String  _getauthinfo(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.Map _authtokenmap = null;
anywheresoftware.b4a.objects.collections.Map _region = null;
 //BA.debugLineNum = 1202;BA.debugLine="Sub GetAuthInfo(json As String)";
 //BA.debugLineNum = 1203;BA.debugLine="Try";
try { //BA.debugLineNum = 1204;BA.debugLine="lblStatus.Text = \"Getting authtoken...\"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Getting authtoken..."));
 //BA.debugLineNum = 1205;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1206;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1207;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1208;BA.debugLine="Dim authtokenmap As Map = root.Get(\"authtoken\")";
_authtokenmap = new anywheresoftware.b4a.objects.collections.Map();
_authtokenmap.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("authtoken"))));
 //BA.debugLineNum = 1209;BA.debugLine="authToken = authtokenmap.Get(\"authtoken\")";
_authtoken = BA.ObjectToString(_authtokenmap.Get((Object)("authtoken")));
 //BA.debugLineNum = 1210;BA.debugLine="Log(\"authToken: \" &  authToken)";
anywheresoftware.b4a.keywords.Common.LogImpl("52555912","authToken: "+_authtoken,0);
 //BA.debugLineNum = 1218;BA.debugLine="Dim region As Map = root.Get(\"region\")";
_region = new anywheresoftware.b4a.objects.collections.Map();
_region.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("region"))));
 //BA.debugLineNum = 1219;BA.debugLine="userRegion = region.GetKeyAt(0)";
_userregion = BA.ObjectToString(_region.GetKeyAt((int) (0)));
 //BA.debugLineNum = 1220;BA.debugLine="Log(\"userRegion: \" &  userRegion)";
anywheresoftware.b4a.keywords.Common.LogImpl("52555922","userRegion: "+_userregion,0);
 } 
       catch (Exception e13) {
			processBA.setLastException(e13); //BA.debugLineNum = 1225;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("52555927",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1229;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1377;BA.debugLine="Sub GetCameraInfo(json As String, clv As CustomLis";
 //BA.debugLineNum = 1378;BA.debugLine="Try";
try { //BA.debugLineNum = 1379;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1380;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1381;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1382;BA.debugLine="Dim camera_status As Map = root.Get(\"camera_stat";
_camera_status = new anywheresoftware.b4a.objects.collections.Map();
_camera_status.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("camera_status"))));
 //BA.debugLineNum = 1383;BA.debugLine="Dim total_108_wakeups As Int = camera_status.Get";
_total_108_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("total_108_wakeups"))));
 //BA.debugLineNum = 1384;BA.debugLine="Dim battery_voltage As Int = camera_status.Get(\"";
_battery_voltage = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("battery_voltage"))));
 //BA.debugLineNum = 1385;BA.debugLine="Dim light_sensor_data_valid As String = camera_s";
_light_sensor_data_valid = BA.ObjectToString(_camera_status.Get((Object)("light_sensor_data_valid")));
 //BA.debugLineNum = 1386;BA.debugLine="Dim lfr_tb_wakeups As Int = camera_status.Get(\"l";
_lfr_tb_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lfr_tb_wakeups"))));
 //BA.debugLineNum = 1387;BA.debugLine="Dim fw_git_hash As String = camera_status.Get(\"f";
_fw_git_hash = BA.ObjectToString(_camera_status.Get((Object)("fw_git_hash")));
 //BA.debugLineNum = 1388;BA.debugLine="Dim wifi_strength As Int = camera_status.Get(\"wi";
_wifi_strength = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("wifi_strength"))));
 //BA.debugLineNum = 1389;BA.debugLine="Dim lfr_strength As Int = camera_status.Get(\"lfr";
_lfr_strength = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lfr_strength"))));
 //BA.debugLineNum = 1390;BA.debugLine="Dim total_tb_wakeups As Int = camera_status.Get(";
_total_tb_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("total_tb_wakeups"))));
 //BA.debugLineNum = 1391;BA.debugLine="Dim created_at As String = camera_status.Get(\"cr";
_created_at = BA.ObjectToString(_camera_status.Get((Object)("created_at")));
 //BA.debugLineNum = 1392;BA.debugLine="Dim light_sensor_ch0 As Int = camera_status.Get(";
_light_sensor_ch0 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("light_sensor_ch0"))));
 //BA.debugLineNum = 1393;BA.debugLine="Dim mac As String = camera_status.Get(\"mac\")";
_mac = BA.ObjectToString(_camera_status.Get((Object)("mac")));
 //BA.debugLineNum = 1394;BA.debugLine="Dim unit_number As Int = camera_status.Get(\"unit";
_unit_number = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("unit_number"))));
 //BA.debugLineNum = 1395;BA.debugLine="Dim updated_at As String = camera_status.Get(\"up";
_updated_at = BA.ObjectToString(_camera_status.Get((Object)("updated_at")));
 //BA.debugLineNum = 1396;BA.debugLine="Dim light_sensor_ch1 As Int = camera_status.Get(";
_light_sensor_ch1 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("light_sensor_ch1"))));
 //BA.debugLineNum = 1397;BA.debugLine="Dim time_dhcp_lease As Int = camera_status.Get(\"";
_time_dhcp_lease = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_dhcp_lease"))));
 //BA.debugLineNum = 1398;BA.debugLine="Dim temperature As Int = camera_status.Get(\"temp";
_temperature = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("temperature"))));
 //BA.debugLineNum = 1399;BA.debugLine="Dim time_first_video As Int = camera_status.Get(";
_time_first_video = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_first_video"))));
 //BA.debugLineNum = 1400;BA.debugLine="Dim time_dns_resolve As Int = camera_status.Get(";
_time_dns_resolve = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_dns_resolve"))));
 //BA.debugLineNum = 1401;BA.debugLine="Dim id As Int = camera_status.Get(\"id\")";
_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("id"))));
 //BA.debugLineNum = 1402;BA.debugLine="Dim temp_alert_status As String = camera_status.";
_temp_alert_status = BA.ObjectToString(_camera_status.Get((Object)("temp_alert_status")));
 //BA.debugLineNum = 1403;BA.debugLine="Dim time_108_boot As Int = camera_status.Get(\"ti";
_time_108_boot = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_108_boot"))));
 //BA.debugLineNum = 1404;BA.debugLine="Dim lfr_108_wakeups As Int = camera_status.Get(\"";
_lfr_108_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lfr_108_wakeups"))));
 //BA.debugLineNum = 1405;BA.debugLine="cameraThumbnail = camera_status.Get(\"thumbnail\")";
_camerathumbnail = BA.ObjectToString(_camera_status.Get((Object)("thumbnail")));
 //BA.debugLineNum = 1406;BA.debugLine="Log(\"cameraThumbnail: \" & cameraThumbnail)";
anywheresoftware.b4a.keywords.Common.LogImpl("52949149","cameraThumbnail: "+_camerathumbnail,0);
 //BA.debugLineNum = 1407;BA.debugLine="Dim lifetime_duration As Int = camera_status.Get";
_lifetime_duration = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lifetime_duration"))));
 //BA.debugLineNum = 1408;BA.debugLine="Dim wifi_connect_failure_count As Int = camera_s";
_wifi_connect_failure_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("wifi_connect_failure_count"))));
 //BA.debugLineNum = 1409;BA.debugLine="Dim camera_id As Int = camera_status.Get(\"camera";
_camera_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("camera_id"))));
 //BA.debugLineNum = 1410;BA.debugLine="Dim battery_alert_status As String = camera_stat";
_battery_alert_status = BA.ObjectToString(_camera_status.Get((Object)("battery_alert_status")));
 //BA.debugLineNum = 1411;BA.debugLine="Dim dhcp_failure_count As Int = camera_status.Ge";
_dhcp_failure_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dhcp_failure_count"))));
 //BA.debugLineNum = 1412;BA.debugLine="Dim ip_address As String = camera_status.Get(\"ip";
_ip_address = BA.ObjectToString(_camera_status.Get((Object)("ip_address")));
 //BA.debugLineNum = 1413;BA.debugLine="Dim ipv As String = camera_status.Get(\"ipv\")";
_ipv = BA.ObjectToString(_camera_status.Get((Object)("ipv")));
 //BA.debugLineNum = 1414;BA.debugLine="Dim light_sensor_data_new As String = camera_sta";
_light_sensor_data_new = BA.ObjectToString(_camera_status.Get((Object)("light_sensor_data_new")));
 //BA.debugLineNum = 1415;BA.debugLine="Dim network_id As Int = camera_status.Get(\"netwo";
_network_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("network_id"))));
 //BA.debugLineNum = 1416;BA.debugLine="Dim account_id As Int = camera_status.Get(\"accou";
_account_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("account_id"))));
 //BA.debugLineNum = 1417;BA.debugLine="Dim serial As String = camera_status.Get(\"serial";
_serial = BA.ObjectToString(_camera_status.Get((Object)("serial")));
 //BA.debugLineNum = 1418;BA.debugLine="Dim dev_1 As Int = camera_status.Get(\"dev_1\")";
_dev_1 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dev_1"))));
 //BA.debugLineNum = 1419;BA.debugLine="Dim time_wlan_connect As Int = camera_status.Get";
_time_wlan_connect = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_wlan_connect"))));
 //BA.debugLineNum = 1420;BA.debugLine="Dim dev_2 As Int = camera_status.Get(\"dev_2\")";
_dev_2 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dev_2"))));
 //BA.debugLineNum = 1421;BA.debugLine="Dim socket_failure_count As Int = camera_status.";
_socket_failure_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("socket_failure_count"))));
 //BA.debugLineNum = 1422;BA.debugLine="Dim dev_3 As Int = camera_status.Get(\"dev_3\")";
_dev_3 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dev_3"))));
 //BA.debugLineNum = 1423;BA.debugLine="Dim pir_rejections As Int = camera_status.Get(\"p";
_pir_rejections = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("pir_rejections"))));
 //BA.debugLineNum = 1424;BA.debugLine="Dim sync_module_id As Int = camera_status.Get(\"s";
_sync_module_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("sync_module_id"))));
 //BA.debugLineNum = 1425;BA.debugLine="Dim lifetime_count As Int = camera_status.Get(\"l";
_lifetime_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lifetime_count"))));
 //BA.debugLineNum = 1426;BA.debugLine="Dim error_codes As Int = camera_status.Get(\"erro";
_error_codes = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("error_codes"))));
 //BA.debugLineNum = 1427;BA.debugLine="Dim fw_version As String = camera_status.Get(\"fw";
_fw_version = BA.ObjectToString(_camera_status.Get((Object)("fw_version")));
 //BA.debugLineNum = 1428;BA.debugLine="Dim ac_power As String = camera_status.Get(\"ac_p";
_ac_power = BA.ObjectToString(_camera_status.Get((Object)("ac_power")));
 //BA.debugLineNum = 1434;BA.debugLine="If camera_id = \"347574\" Then";
if (_camera_id==(double)(Double.parseDouble("347574"))) { 
 //BA.debugLineNum = 1435;BA.debugLine="lblDrivewayBatt.Text = \"Batt: \" & NumberFormat2";
mostCurrent._lbldrivewaybatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V - "+_checkbattlife(_battery_voltage)));
 //BA.debugLineNum = 1436;BA.debugLine="lblDrivewayTimestamp.Text = ConvertDateTime(upd";
mostCurrent._lbldrivewaytimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1437;BA.debugLine="lblDriveway.Text = \"Driveway v\" & fw_version";
mostCurrent._lbldriveway.setText(BA.ObjectToCharSequence("Driveway v"+_fw_version));
 //BA.debugLineNum = 1438;BA.debugLine="lblDrivewayWifi.Text = \"WiFi: \" & wifi_strength";
mostCurrent._lbldrivewaywifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 }else if(_camera_id==(double)(Double.parseDouble("236967"))) { 
 //BA.debugLineNum = 1440;BA.debugLine="lblFrontDoorBatt.Text = \"Batt: \" & NumberFormat";
mostCurrent._lblfrontdoorbatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V - "+_checkbattlife(_battery_voltage)));
 //BA.debugLineNum = 1441;BA.debugLine="lblFrontDoorTimestamp.Text = ConvertDateTime(up";
mostCurrent._lblfrontdoortimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1442;BA.debugLine="lblFrontDoor.Text = \"Front Door v\" & fw_version";
mostCurrent._lblfrontdoor.setText(BA.ObjectToCharSequence("Front Door v"+_fw_version));
 //BA.debugLineNum = 1443;BA.debugLine="lblFrontDoorWiFi.Text = \"WiFi: \" & wifi_strengt";
mostCurrent._lblfrontdoorwifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 }else if(_camera_id==(double)(Double.parseDouble("226821"))) { 
 //BA.debugLineNum = 1445;BA.debugLine="lblSideYardBatt.Text = \"Batt: \" & NumberFormat2";
mostCurrent._lblsideyardbatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V - "+_checkbattlife(_battery_voltage)));
 //BA.debugLineNum = 1446;BA.debugLine="lblSideYardTimestamp.Text = ConvertDateTime(upd";
mostCurrent._lblsideyardtimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1447;BA.debugLine="lblSideYard.Text = \"Side Yard v\" & fw_version";
mostCurrent._lblsideyard.setText(BA.ObjectToCharSequence("Side Yard v"+_fw_version));
 //BA.debugLineNum = 1448;BA.debugLine="lblSideYardWiFi.Text = \"WiFi: \" & wifi_strength";
mostCurrent._lblsideyardwifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 };
 } 
       catch (Exception e69) {
			processBA.setLastException(e69); //BA.debugLineNum = 1452;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("52949195",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1455;BA.debugLine="End Sub";
return "";
}
public static String  _getcomfort(String _dht11comfortstatus) throws Exception{
String _localcomfortstatus = "";
 //BA.debugLineNum = 439;BA.debugLine="Sub GetComfort(DHT11ComfortStatus As String) As St";
 //BA.debugLineNum = 440;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = "";
 //BA.debugLineNum = 441;BA.debugLine="Select Case DHT11ComfortStatus";
switch (BA.switchObjectToInt(_dht11comfortstatus,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(8),BA.NumberToString(9),BA.NumberToString(10))) {
case 0: {
 //BA.debugLineNum = 443;BA.debugLine="localcomfortstatus = \"OK\"";
_localcomfortstatus = "OK";
 break; }
case 1: {
 //BA.debugLineNum = 445;BA.debugLine="localcomfortstatus = \"Too hot\"";
_localcomfortstatus = "Too hot";
 break; }
case 2: {
 //BA.debugLineNum = 447;BA.debugLine="localcomfortstatus = \"Too cold\"";
_localcomfortstatus = "Too cold";
 break; }
case 3: {
 //BA.debugLineNum = 449;BA.debugLine="localcomfortstatus = \"Too dry\"";
_localcomfortstatus = "Too dry";
 break; }
case 4: {
 //BA.debugLineNum = 451;BA.debugLine="localcomfortstatus = \"Hot and dry\"";
_localcomfortstatus = "Hot and dry";
 break; }
case 5: {
 //BA.debugLineNum = 453;BA.debugLine="localcomfortstatus = \"Cold and dry\"";
_localcomfortstatus = "Cold and dry";
 break; }
case 6: {
 //BA.debugLineNum = 455;BA.debugLine="localcomfortstatus = \"Too humid\"";
_localcomfortstatus = "Too humid";
 break; }
case 7: {
 //BA.debugLineNum = 457;BA.debugLine="localcomfortstatus = \"Hot and humid\"";
_localcomfortstatus = "Hot and humid";
 break; }
case 8: {
 //BA.debugLineNum = 459;BA.debugLine="localcomfortstatus = \"Cold and humid\"";
_localcomfortstatus = "Cold and humid";
 break; }
default: {
 //BA.debugLineNum = 461;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = "Unknown";
 break; }
}
;
 //BA.debugLineNum = 463;BA.debugLine="Return localcomfortstatus";
if (true) return _localcomfortstatus;
 //BA.debugLineNum = 464;BA.debugLine="End Sub";
return "";
}
public static String  _getcommandid(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
 //BA.debugLineNum = 1289;BA.debugLine="Sub GetCommandID(json As String)";
 //BA.debugLineNum = 1290;BA.debugLine="Try";
try { //BA.debugLineNum = 1291;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1292;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1293;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1308;BA.debugLine="commandID = root.Get(\"id\")";
_commandid = BA.ObjectToString(_root.Get((Object)("id")));
 //BA.debugLineNum = 1309;BA.debugLine="Log(\"commandID: \" & commandID)";
anywheresoftware.b4a.keywords.Common.LogImpl("52752532","commandID: "+_commandid,0);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 1331;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("52752554",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1334;BA.debugLine="End Sub";
return "";
}
public static String  _getcommandstatus(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
 //BA.debugLineNum = 1336;BA.debugLine="Sub GetCommandStatus(json As String)";
 //BA.debugLineNum = 1337;BA.debugLine="Try";
try { //BA.debugLineNum = 1338;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1339;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1340;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1341;BA.debugLine="commandComplete = root.Get(\"complete\")";
_commandcomplete = BA.ObjectToBoolean(_root.Get((Object)("complete")));
 //BA.debugLineNum = 1342;BA.debugLine="Log(\"commandComplete: \" & commandComplete)";
anywheresoftware.b4a.keywords.Common.LogImpl("52818054","commandComplete: "+BA.ObjectToString(_commandcomplete),0);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 1344;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("52818056",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1347;BA.debugLine="End Sub";
return "";
}
public static String  _getperception(String _dht11perception) throws Exception{
String _localperception = "";
 //BA.debugLineNum = 406;BA.debugLine="Sub GetPerception(DHT11Perception As String) As St";
 //BA.debugLineNum = 417;BA.debugLine="Dim localperception As String";
_localperception = "";
 //BA.debugLineNum = 418;BA.debugLine="Select Case DHT11Perception";
switch (BA.switchObjectToInt(_dht11perception,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(7))) {
case 0: {
 //BA.debugLineNum = 420;BA.debugLine="localperception = \"Feels like the western US, a";
_localperception = "Feels like the western US, a bit dry to some";
 break; }
case 1: {
 //BA.debugLineNum = 422;BA.debugLine="localperception = \"Very comfortable\"";
_localperception = "Very comfortable";
 break; }
case 2: {
 //BA.debugLineNum = 424;BA.debugLine="localperception = \"Comfortable\"";
_localperception = "Comfortable";
 break; }
case 3: {
 //BA.debugLineNum = 426;BA.debugLine="localperception = \"OK but humidity is at upper";
_localperception = "OK but humidity is at upper limit";
 break; }
case 4: {
 //BA.debugLineNum = 428;BA.debugLine="localperception = \"Uncomfortable and the humidi";
_localperception = "Uncomfortable and the humidity is at upper limit";
 break; }
case 5: {
 //BA.debugLineNum = 430;BA.debugLine="localperception = \"Very humid, quite uncomforta";
_localperception = "Very humid, quite uncomfortable";
 break; }
case 6: {
 //BA.debugLineNum = 432;BA.debugLine="localperception = \"Extremely uncomfortable, opp";
_localperception = "Extremely uncomfortable, oppressive";
 break; }
case 7: {
 //BA.debugLineNum = 434;BA.debugLine="localperception = \"Severely high, even deadly f";
_localperception = "Severely high, even deadly for asthma related illnesses";
 break; }
}
;
 //BA.debugLineNum = 436;BA.debugLine="Return localperception";
if (true) return _localperception;
 //BA.debugLineNum = 437;BA.debugLine="End Sub";
return "";
}
public static String  _getresterror(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
int _code = 0;
String _message = "";
 //BA.debugLineNum = 1349;BA.debugLine="Sub GetRESTError(json As String) As String";
 //BA.debugLineNum = 1350;BA.debugLine="Try";
try { //BA.debugLineNum = 1354;BA.debugLine="If json.Contains(\"<h1>Not Found</h1>\") Then";
if (_json.contains("<h1>Not Found</h1>")) { 
 //BA.debugLineNum = 1355;BA.debugLine="Return \"REST endpoint URL not found. Try again.";
if (true) return "REST endpoint URL not found. Try again.";
 }else {
 //BA.debugLineNum = 1357;BA.debugLine="If json.IndexOf(\"{\") <> -1 Then";
if (_json.indexOf("{")!=-1) { 
 //BA.debugLineNum = 1358;BA.debugLine="json = json.SubString(json.IndexOf(\"{\"))";
_json = _json.substring(_json.indexOf("{"));
 //BA.debugLineNum = 1359;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1360;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1361;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1362;BA.debugLine="Dim code As Int = root.Get(\"code\")";
_code = (int)(BA.ObjectToNumber(_root.Get((Object)("code"))));
 //BA.debugLineNum = 1363;BA.debugLine="Dim message As String = root.Get(\"message\")";
_message = BA.ObjectToString(_root.Get((Object)("message")));
 //BA.debugLineNum = 1364;BA.debugLine="Log(\"Code: \" & code & \" Message: \" & message)";
anywheresoftware.b4a.keywords.Common.LogImpl("52883599","Code: "+BA.NumberToString(_code)+" Message: "+_message,0);
 //BA.debugLineNum = 1365;BA.debugLine="Return \"Code: \" & code & \" Message: \" & messag";
if (true) return "Code: "+BA.NumberToString(_code)+" Message: "+_message;
 }else {
 //BA.debugLineNum = 1367;BA.debugLine="Return json";
if (true) return _json;
 };
 };
 } 
       catch (Exception e19) {
			processBA.setLastException(e19); //BA.debugLineNum = 1371;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("52883606",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1373;BA.debugLine="Return json";
if (true) return _json;
 };
 //BA.debugLineNum = 1375;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1457;BA.debugLine="Sub GetSyncModuleInfo(json As String)";
 //BA.debugLineNum = 1458;BA.debugLine="Try";
try { //BA.debugLineNum = 1459;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1460;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1461;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1462;BA.debugLine="Dim syncmodule As Map = root.Get(\"syncmodule\")";
_syncmodule = new anywheresoftware.b4a.objects.collections.Map();
_syncmodule.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("syncmodule"))));
 //BA.debugLineNum = 1466;BA.debugLine="Dim wifi_strength As Int = syncmodule.Get(\"wifi_";
_wifi_strength = (int)(BA.ObjectToNumber(_syncmodule.Get((Object)("wifi_strength"))));
 //BA.debugLineNum = 1467;BA.debugLine="Dim os_version As String = syncmodule.Get(\"os_ve";
_os_version = BA.ObjectToString(_syncmodule.Get((Object)("os_version")));
 //BA.debugLineNum = 1484;BA.debugLine="Dim fw_version As String = syncmodule.Get(\"fw_ve";
_fw_version = BA.ObjectToString(_syncmodule.Get((Object)("fw_version")));
 //BA.debugLineNum = 1486;BA.debugLine="Dim status As String = syncmodule.Get(\"status\")";
_status = BA.ObjectToString(_syncmodule.Get((Object)("status")));
 //BA.debugLineNum = 1487;BA.debugLine="lblSyncModule.Text = \"Sync Module is \" & status";
mostCurrent._lblsyncmodule.setText(BA.ObjectToCharSequence("Sync Module is "+_status+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Wifi Strength: "+BA.NumberToString(_wifi_strength)+" bars"+anywheresoftware.b4a.keywords.Common.CRLF+"Firmware version: "+_fw_version+anywheresoftware.b4a.keywords.Common.CRLF+"OS version: "+_os_version));
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 1489;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("53014688",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1492;BA.debugLine="End Sub";
return "";
}
public static String  _getuserinfo(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
 //BA.debugLineNum = 1231;BA.debugLine="Sub GetUserInfo(json As String)";
 //BA.debugLineNum = 1232;BA.debugLine="Try";
try { //BA.debugLineNum = 1233;BA.debugLine="lblStatus.Text = \"Getting user information...\"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Getting user information..."));
 //BA.debugLineNum = 1234;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1235;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1236;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1251;BA.debugLine="accountID = root.Get(\"account_id\") '88438";
_accountid = BA.ObjectToString(_root.Get((Object)("account_id")));
 //BA.debugLineNum = 1252;BA.debugLine="Log(\"accountID: \" &  accountID)";
anywheresoftware.b4a.keywords.Common.LogImpl("52621461","accountID: "+_accountid,0);
 } 
       catch (Exception e9) {
			processBA.setLastException(e9); //BA.debugLineNum = 1255;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("52621464",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1258;BA.debugLine="End Sub";
return "";
}
public static String  _getversioncode() throws Exception{
String _appversion = "";
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
String _packagename = "";
 //BA.debugLineNum = 529;BA.debugLine="Sub GetVersionCode() As String";
 //BA.debugLineNum = 530;BA.debugLine="Dim AppVersion As String";
_appversion = "";
 //BA.debugLineNum = 531;BA.debugLine="Try";
try { //BA.debugLineNum = 532;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 533;BA.debugLine="Dim packageName As String";
_packagename = "";
 //BA.debugLineNum = 534;BA.debugLine="packageName =  Application.PackageName";
_packagename = anywheresoftware.b4a.keywords.Common.Application.getPackageName();
 //BA.debugLineNum = 535;BA.debugLine="AppVersion = pm.GetVersionName(packageName)";
_appversion = _pm.GetVersionName(_packagename);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 537;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51310728",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 539;BA.debugLine="Return AppVersion";
if (true) return _appversion;
 //BA.debugLineNum = 540;BA.debugLine="End Sub";
return "";
}
public static void  _getvideos(String _json) throws Exception{
ResumableSub_GetVideos rsub = new ResumableSub_GetVideos(null,_json);
rsub.resume(processBA, null);
}
public static class ResumableSub_GetVideos extends BA.ResumableSub {
public ResumableSub_GetVideos(cloyd.smart.home.monitor.main parent,String _json) {
this.parent = parent;
this._json = _json;
}
cloyd.smart.home.monitor.main parent;
String _json;
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.List _media = null;
anywheresoftware.b4a.objects.collections.Map _colmedia = null;
String _thumbnail = "";
String _created_at = "";
String _device_name = "";
String _medianame = "";
cloyd.smart.home.monitor.httpjob _j = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.BA.IterableList group6;
int index6;
int groupLen6;

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
 //BA.debugLineNum = 1691;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 20;
this.catchState = 19;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 19;
 //BA.debugLineNum = 1692;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1693;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1694;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1698;BA.debugLine="Dim media As List = root.Get(\"media\")";
_media = new anywheresoftware.b4a.objects.collections.List();
_media.setObject((java.util.List)(_root.Get((Object)("media"))));
 //BA.debugLineNum = 1699;BA.debugLine="For Each colmedia As Map In media";
if (true) break;

case 4:
//for
this.state = 17;
_colmedia = new anywheresoftware.b4a.objects.collections.Map();
group6 = _media;
index6 = 0;
groupLen6 = group6.getSize();
this.state = 21;
if (true) break;

case 21:
//C
this.state = 17;
if (index6 < groupLen6) {
this.state = 6;
_colmedia.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(group6.Get(index6)));}
if (true) break;

case 22:
//C
this.state = 21;
index6++;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1700;BA.debugLine="Dim thumbnail As String = colmedia.Get(\"thumbna";
_thumbnail = BA.ObjectToString(_colmedia.Get((Object)("thumbnail")));
 //BA.debugLineNum = 1703;BA.debugLine="Dim created_at As String = colmedia.Get(\"create";
_created_at = BA.ObjectToString(_colmedia.Get((Object)("created_at")));
 //BA.debugLineNum = 1708;BA.debugLine="Dim device_name As String = colmedia.Get(\"devic";
_device_name = BA.ObjectToString(_colmedia.Get((Object)("device_name")));
 //BA.debugLineNum = 1718;BA.debugLine="Dim medianame As String = colmedia.Get(\"media\")";
_medianame = BA.ObjectToString(_colmedia.Get((Object)("media")));
 //BA.debugLineNum = 1719;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1720;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1721;BA.debugLine="j.Download(\"https://rest-\" & userRegion &\".imme";
_j._download /*String*/ ("https://rest-"+parent._userregion+".immedia-semi.com"+_thumbnail+".jpg");
 //BA.debugLineNum = 1722;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1723;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 23;
return;
case 23:
//C
this.state = 7;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1724;BA.debugLine="If j.Success Then";
if (true) break;

case 7:
//if
this.state = 16;
if (_j._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 15;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1727;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"screenshot.jpg",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1728;BA.debugLine="File.Copy2(j.GetInputStream, out)";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_j._getinputstream /*anywheresoftware.b4a.objects.streams.File.InputStreamWrapper*/ ().getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 1729;BA.debugLine="out.Close '<------ very important";
_out.Close();
 //BA.debugLineNum = 1733;BA.debugLine="Dim p As B4XView = CreateListItem(j.GetBitmap,";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _createlistitem((anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper(), (android.graphics.Bitmap)(_j._getbitmap /*anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper*/ ().getObject())),_created_at,_device_name);
 //BA.debugLineNum = 1734;BA.debugLine="clvActivity.Add(p,\"https://rest-\" & userRegion";
parent.mostCurrent._clvactivity._add(_p,(Object)("https://rest-"+parent._userregion+".immedia-semi.com"+_medianame+"|"+_device_name+" "+_convertfulldatetime(_created_at)));
 //BA.debugLineNum = 1735;BA.debugLine="If clvActivity.Size = 1 Then";
if (true) break;

case 10:
//if
this.state = 13;
if (parent.mostCurrent._clvactivity._getsize()==1) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1736;BA.debugLine="ShowVideo(\"https://rest-\" & userRegion &\".imm";
_showvideo("https://rest-"+parent._userregion+".immedia-semi.com"+_medianame);
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 if (true) break;

case 16:
//C
this.state = 22;
;
 //BA.debugLineNum = 1741;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;
if (true) break;

case 17:
//C
this.state = 20;
;
 if (true) break;

case 19:
//C
this.state = 20;
this.catchState = 0;
 //BA.debugLineNum = 1745;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("527066423",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 20:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1747;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 97;BA.debugLine="Private ivScreenshot As ImageView";
mostCurrent._ivscreenshot = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 98;BA.debugLine="Private lblDate As B4XView";
mostCurrent._lbldate = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 99;BA.debugLine="Private lblDeviceInfo As B4XView";
mostCurrent._lbldeviceinfo = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 100;BA.debugLine="Private lblFileInfo As B4XView";
mostCurrent._lblfileinfo = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 101;BA.debugLine="Private clvActivity As CustomListView";
mostCurrent._clvactivity = new b4a.example3.customlistview();
 //BA.debugLineNum = 102;BA.debugLine="Private wvMedia As WebView";
mostCurrent._wvmedia = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 103;BA.debugLine="Private WebViewSettings1 As WebViewSettings";
mostCurrent._webviewsettings1 = new uk.co.martinpearman.b4a.webviewsettings.WebViewSettings();
 //BA.debugLineNum = 104;BA.debugLine="End Sub";
return "";
}
public static String  _handlesettings() throws Exception{
 //BA.debugLineNum = 921;BA.debugLine="Sub HandleSettings";
 //BA.debugLineNum = 922;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTime",_manager.GetString("TempHumidityCooldownTime"));
 //BA.debugLineNum = 923;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTimeBasement",_manager.GetString("TempHumidityCooldownTimeBasement"));
 //BA.debugLineNum = 924;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",manage";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue",_manager.GetString("HumidityAddValue"));
 //BA.debugLineNum = 925;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime",_manager.GetString("SensorNotRespondingTime"));
 //BA.debugLineNum = 926;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 927;BA.debugLine="End Sub";
return "";
}
public static String  _hideping() throws Exception{
 //BA.debugLineNum = 554;BA.debugLine="Private Sub HidePing";
 //BA.debugLineNum = 555;BA.debugLine="lblPing.SetVisibleAnimated(200, False)";
mostCurrent._lblping.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 556;BA.debugLine="End Sub";
return "";
}
public static String  _hidepingbasement() throws Exception{
 //BA.debugLineNum = 558;BA.debugLine="Private Sub HidePingBasement";
 //BA.debugLineNum = 559;BA.debugLine="lblPingBasement.SetVisibleAnimated(200, False)";
mostCurrent._lblpingbasement.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 560;BA.debugLine="End Sub";
return "";
}
public static String  _ivdriveway_click() throws Exception{
 //BA.debugLineNum = 1675;BA.debugLine="Sub ivDriveway_Click";
 //BA.debugLineNum = 1676;BA.debugLine="lblDrivewayBatt.Visible = Not(lblDrivewayBatt.Vis";
mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaybatt.getVisible()));
 //BA.debugLineNum = 1677;BA.debugLine="lblDrivewayTimestamp.Visible = Not(lblDrivewayTim";
mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaytimestamp.getVisible()));
 //BA.debugLineNum = 1678;BA.debugLine="lblDrivewayWifi.Visible = Not(lblDrivewayWifi.Vis";
mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaywifi.getVisible()));
 //BA.debugLineNum = 1679;BA.debugLine="lblDriveway.Visible = Not(lblDriveway.Visible)";
mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldriveway.getVisible()));
 //BA.debugLineNum = 1680;BA.debugLine="lblFrontDoorBatt.Visible = Not(lblFrontDoorBatt.V";
mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorbatt.getVisible()));
 //BA.debugLineNum = 1681;BA.debugLine="lblFrontDoorTimestamp.Visible = Not(lblFrontDoorT";
mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoortimestamp.getVisible()));
 //BA.debugLineNum = 1682;BA.debugLine="lblFrontDoorWiFi.Visible = Not(lblFrontDoorWiFi.V";
mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorwifi.getVisible()));
 //BA.debugLineNum = 1683;BA.debugLine="lblFrontDoor.Visible = Not(lblFrontDoor.Visible)";
mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoor.getVisible()));
 //BA.debugLineNum = 1684;BA.debugLine="lblSideYardBatt.Visible = Not(lblSideYardBatt.Vis";
mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardbatt.getVisible()));
 //BA.debugLineNum = 1685;BA.debugLine="lblSideYardTimestamp.Visible = Not(lblSideYardTim";
mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardtimestamp.getVisible()));
 //BA.debugLineNum = 1686;BA.debugLine="lblSideYardWiFi.Visible = Not(lblSideYardWiFi.Vis";
mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardwifi.getVisible()));
 //BA.debugLineNum = 1687;BA.debugLine="lblSideYard.Visible = Not(lblSideYard.Visible)";
mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyard.getVisible()));
 //BA.debugLineNum = 1688;BA.debugLine="End Sub";
return "";
}
public static String  _ivfrontdoor_click() throws Exception{
 //BA.debugLineNum = 1660;BA.debugLine="Sub ivFrontDoor_Click";
 //BA.debugLineNum = 1661;BA.debugLine="lblDrivewayBatt.Visible = Not(lblDrivewayBatt.Vis";
mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaybatt.getVisible()));
 //BA.debugLineNum = 1662;BA.debugLine="lblDrivewayTimestamp.Visible = Not(lblDrivewayTim";
mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaytimestamp.getVisible()));
 //BA.debugLineNum = 1663;BA.debugLine="lblDrivewayWifi.Visible = Not(lblDrivewayWifi.Vis";
mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaywifi.getVisible()));
 //BA.debugLineNum = 1664;BA.debugLine="lblDriveway.Visible = Not(lblDriveway.Visible)";
mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldriveway.getVisible()));
 //BA.debugLineNum = 1665;BA.debugLine="lblFrontDoorBatt.Visible = Not(lblFrontDoorBatt.V";
mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorbatt.getVisible()));
 //BA.debugLineNum = 1666;BA.debugLine="lblFrontDoorTimestamp.Visible = Not(lblFrontDoorT";
mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoortimestamp.getVisible()));
 //BA.debugLineNum = 1667;BA.debugLine="lblFrontDoorWiFi.Visible = Not(lblFrontDoorWiFi.V";
mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorwifi.getVisible()));
 //BA.debugLineNum = 1668;BA.debugLine="lblFrontDoor.Visible = Not(lblFrontDoor.Visible)";
mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoor.getVisible()));
 //BA.debugLineNum = 1669;BA.debugLine="lblSideYardBatt.Visible = Not(lblSideYardBatt.Vis";
mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardbatt.getVisible()));
 //BA.debugLineNum = 1670;BA.debugLine="lblSideYardTimestamp.Visible = Not(lblSideYardTim";
mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardtimestamp.getVisible()));
 //BA.debugLineNum = 1671;BA.debugLine="lblSideYardWiFi.Visible = Not(lblSideYardWiFi.Vis";
mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardwifi.getVisible()));
 //BA.debugLineNum = 1672;BA.debugLine="lblSideYard.Visible = Not(lblSideYard.Visible)";
mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyard.getVisible()));
 //BA.debugLineNum = 1673;BA.debugLine="End Sub";
return "";
}
public static String  _ivsideyard_click() throws Exception{
 //BA.debugLineNum = 1645;BA.debugLine="Sub ivSideYard_Click";
 //BA.debugLineNum = 1646;BA.debugLine="lblDrivewayBatt.Visible = Not(lblDrivewayBatt.Vis";
mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaybatt.getVisible()));
 //BA.debugLineNum = 1647;BA.debugLine="lblDrivewayTimestamp.Visible = Not(lblDrivewayTim";
mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaytimestamp.getVisible()));
 //BA.debugLineNum = 1648;BA.debugLine="lblDrivewayWifi.Visible = Not(lblDrivewayWifi.Vis";
mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaywifi.getVisible()));
 //BA.debugLineNum = 1649;BA.debugLine="lblDriveway.Visible = Not(lblDriveway.Visible)";
mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldriveway.getVisible()));
 //BA.debugLineNum = 1650;BA.debugLine="lblFrontDoorBatt.Visible = Not(lblFrontDoorBatt.V";
mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorbatt.getVisible()));
 //BA.debugLineNum = 1651;BA.debugLine="lblFrontDoorTimestamp.Visible = Not(lblFrontDoorT";
mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoortimestamp.getVisible()));
 //BA.debugLineNum = 1652;BA.debugLine="lblFrontDoorWiFi.Visible = Not(lblFrontDoorWiFi.V";
mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorwifi.getVisible()));
 //BA.debugLineNum = 1653;BA.debugLine="lblFrontDoor.Visible = Not(lblFrontDoor.Visible)";
mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoor.getVisible()));
 //BA.debugLineNum = 1654;BA.debugLine="lblSideYardBatt.Visible = Not(lblSideYardBatt.Vis";
mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardbatt.getVisible()));
 //BA.debugLineNum = 1655;BA.debugLine="lblSideYardTimestamp.Visible = Not(lblSideYardTim";
mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardtimestamp.getVisible()));
 //BA.debugLineNum = 1656;BA.debugLine="lblSideYardWiFi.Visible = Not(lblSideYardWiFi.Vis";
mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardwifi.getVisible()));
 //BA.debugLineNum = 1657;BA.debugLine="lblSideYard.Visible = Not(lblSideYard.Visible)";
mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyard.getVisible()));
 //BA.debugLineNum = 1658;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connect() throws Exception{
String _clientid = "";
anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper _connopt = null;
 //BA.debugLineNum = 325;BA.debugLine="Sub MQTT_Connect";
 //BA.debugLineNum = 326;BA.debugLine="Try";
try { //BA.debugLineNum = 327;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'crea";
_clientid = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (999999999)));
 //BA.debugLineNum = 328;BA.debugLine="MQTT.Initialize(\"MQTT\", MQTTServerURI, ClientId)";
_mqtt.Initialize(processBA,"MQTT",_mqttserveruri,_clientid);
 //BA.debugLineNum = 330;BA.debugLine="Dim ConnOpt As MqttConnectOptions";
_connopt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper();
 //BA.debugLineNum = 331;BA.debugLine="ConnOpt.Initialize(MQTTUser, MQTTPassword)";
_connopt.Initialize(_mqttuser,_mqttpassword);
 //BA.debugLineNum = 332;BA.debugLine="MQTT.Connect2(ConnOpt)";
_mqtt.Connect2((org.eclipse.paho.client.mqttv3.MqttConnectOptions)(_connopt.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 334;BA.debugLine="Log(\"MQTT_Connect: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("5720905","MQTT_Connect: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 336;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 338;BA.debugLine="Sub MQTT_Connected (Success As Boolean)";
 //BA.debugLineNum = 339;BA.debugLine="Try";
try { //BA.debugLineNum = 340;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 341;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("5786435",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 342;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 }else {
 //BA.debugLineNum = 344;BA.debugLine="Log(\"Connected to MQTT broker\")";
anywheresoftware.b4a.keywords.Common.LogImpl("5786438","Connected to MQTT broker",0);
 //BA.debugLineNum = 345;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
_mqtt.Subscribe("TempHumid",(int) (0));
 //BA.debugLineNum = 346;BA.debugLine="MQTT.Subscribe(\"MQ7\", 0)";
_mqtt.Subscribe("MQ7",(int) (0));
 //BA.debugLineNum = 347;BA.debugLine="MQTT.Subscribe(\"MQ7Basement\", 0)";
_mqtt.Subscribe("MQ7Basement",(int) (0));
 //BA.debugLineNum = 348;BA.debugLine="MQTT.Subscribe(\"TempHumidBasement\", 0)";
_mqtt.Subscribe("TempHumidBasement",(int) (0));
 //BA.debugLineNum = 349;BA.debugLine="MQTT.Subscribe(\"HumidityAddValue\", 0)";
_mqtt.Subscribe("HumidityAddValue",(int) (0));
 };
 } 
       catch (Exception e14) {
			processBA.setLastException(e14); //BA.debugLineNum = 352;BA.debugLine="Log(\"MQTT_Connected: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("5786446","MQTT_Connected: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 354;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_disconnected() throws Exception{
 //BA.debugLineNum = 356;BA.debugLine="Private Sub MQTT_Disconnected";
 //BA.debugLineNum = 357;BA.debugLine="Try";
try { //BA.debugLineNum = 358;BA.debugLine="gblACMenu.Clear";
mostCurrent._gblacmenu.Clear();
 //BA.debugLineNum = 359;BA.debugLine="gblACMenu.Add(0, 0, \"Settings\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Settings"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 361;BA.debugLine="gblACMenu.Add(0, 0, \"About\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 362;BA.debugLine="Log(\"Disconnected from MQTT broker\")";
anywheresoftware.b4a.keywords.Common.LogImpl("5851974","Disconnected from MQTT broker",0);
 //BA.debugLineNum = 363;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 365;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("5851977",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 367;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
 //BA.debugLineNum = 369;BA.debugLine="Private Sub MQTT_MessageArrived (Topic As String,";
 //BA.debugLineNum = 370;BA.debugLine="Try";
try { //BA.debugLineNum = 371;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic).equals("TempHumid")) { 
 //BA.debugLineNum = 372;BA.debugLine="lblPing.SetVisibleAnimated(500, True)";
mostCurrent._lblping.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 373;BA.debugLine="csu.CallSubPlus(Me, \"HidePing\", 700)";
_csu._v7(main.getObject(),"HidePing",(int) (700));
 //BA.debugLineNum = 374;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 }else if((_topic).equals("MQ7")) { 
 //BA.debugLineNum = 376;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 }else if((_topic).equals("MQ7Basement")) { 
 //BA.debugLineNum = 378;BA.debugLine="CheckAirQualitySettingBasement";
_checkairqualitysettingbasement();
 }else if((_topic).equals("TempHumidBasement")) { 
 //BA.debugLineNum = 380;BA.debugLine="lblPingBasement.SetVisibleAnimated(500, True)";
mostCurrent._lblpingbasement.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 381;BA.debugLine="csu.CallSubPlus(Me, \"HidePingBasement\", 700)";
_csu._v7(main.getObject(),"HidePingBasement",(int) (700));
 //BA.debugLineNum = 383;BA.debugLine="CheckTempHumiditySettingBasement";
_checktemphumiditysettingbasement();
 };
 } 
       catch (Exception e16) {
			processBA.setLastException(e16); //BA.debugLineNum = 386;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("5917521",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 388;BA.debugLine="End Sub";
return "";
}
public static long  _parseutcstring(String _utc) throws Exception{
String _df = "";
long _res = 0L;
 //BA.debugLineNum = 1544;BA.debugLine="Sub ParseUTCstring(utc As String) As Long";
 //BA.debugLineNum = 1545;BA.debugLine="Dim df As String = DateTime.DateFormat";
_df = anywheresoftware.b4a.keywords.Common.DateTime.getDateFormat();
 //BA.debugLineNum = 1546;BA.debugLine="Dim res As Long";
_res = 0L;
 //BA.debugLineNum = 1547;BA.debugLine="If utc.CharAt(10) = \"T\" Then";
if (_utc.charAt((int) (10))==BA.ObjectToChar("T")) { 
 //BA.debugLineNum = 1549;BA.debugLine="If utc.CharAt(19) = \".\" Then utc = utc.SubString";
if (_utc.charAt((int) (19))==BA.ObjectToChar(".")) { 
_utc = _utc.substring((int) (0),(int) (19))+"+0000";};
 //BA.debugLineNum = 1550;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd'T'HH:mm:ssZ\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
 }else {
 //BA.debugLineNum = 1553;BA.debugLine="DateTime.DateFormat = \"EEE MMM dd HH:mm:ss Z yyy";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
 };
 //BA.debugLineNum = 1555;BA.debugLine="Try";
try { //BA.debugLineNum = 1556;BA.debugLine="res = DateTime.DateParse(utc)";
_res = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_utc);
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 1558;BA.debugLine="res = 0";
_res = (long) (0);
 };
 //BA.debugLineNum = 1560;BA.debugLine="DateTime.DateFormat = df";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat(_df);
 //BA.debugLineNum = 1561;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 1562;BA.debugLine="End Sub";
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
int _attempts = 0;
String _link = "";
int _i = 0;
anywheresoftware.b4a.BA.IterableList group23;
int index23;
int groupLen23;
int step70;
int limit70;

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
 //BA.debugLineNum = 985;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 84;
this.catchState = 83;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 83;
 //BA.debugLineNum = 986;BA.debugLine="Dim camera As String";
_camera = "";
 //BA.debugLineNum = 987;BA.debugLine="Dim iv As ImageView";
_iv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 988;BA.debugLine="Dim clv As CustomListView";
_clv = new b4a.example3.customlistview();
 //BA.debugLineNum = 989;BA.debugLine="Dim links As List";
_links = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 990;BA.debugLine="Dim attempts As Int";
_attempts = 0;
 //BA.debugLineNum = 991;BA.debugLine="links = Array(\"347574\", \"236967\", \"226821\")";
_links = anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)("347574"),(Object)("236967"),(Object)("226821")});
 //BA.debugLineNum = 993;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia-";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/syncmodules");
 //BA.debugLineNum = 994;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 85;
return;
case 85:
//C
this.state = 4;
;
 //BA.debugLineNum = 995;BA.debugLine="GetSyncModuleInfo(response)";
_getsyncmoduleinfo(parent._response);
 //BA.debugLineNum = 997;BA.debugLine="lblDrivewayBatt.Visible = True";
parent.mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 998;BA.debugLine="lblDrivewayTimestamp.Visible = True";
parent.mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 999;BA.debugLine="lblDrivewayWifi.Visible = True";
parent.mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1000;BA.debugLine="lblDriveway.Visible = True";
parent.mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1001;BA.debugLine="lblFrontDoorBatt.Visible = True";
parent.mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1002;BA.debugLine="lblFrontDoorTimestamp.Visible = True";
parent.mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1003;BA.debugLine="lblFrontDoorWiFi.Visible = True";
parent.mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1004;BA.debugLine="lblFrontDoor.Visible = True";
parent.mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1005;BA.debugLine="lblSideYardBatt.Visible = True";
parent.mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1006;BA.debugLine="lblSideYardTimestamp.Visible = True";
parent.mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1007;BA.debugLine="lblSideYardWiFi.Visible = True";
parent.mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1008;BA.debugLine="lblSideYard.Visible = True";
parent.mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1010;BA.debugLine="For Each link As String In links";
if (true) break;

case 4:
//for
this.state = 81;
group23 = _links;
index23 = 0;
groupLen23 = group23.getSize();
this.state = 86;
if (true) break;

case 86:
//C
this.state = 81;
if (index23 < groupLen23) {
this.state = 6;
_link = BA.ObjectToString(group23.Get(index23));}
if (true) break;

case 87:
//C
this.state = 86;
index23++;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1011;BA.debugLine="camera = link";
_camera = _link;
 //BA.debugLineNum = 1012;BA.debugLine="If FirstRun Then";
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
 //BA.debugLineNum = 1013;BA.debugLine="If camera = \"347574\" Then";
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
 //BA.debugLineNum = 1014;BA.debugLine="lblStatus.Text = \"Retrieving Driveway thumbna";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Driveway thumbnail..."));
 //BA.debugLineNum = 1015;BA.debugLine="iv = ivDriveway";
_iv = parent.mostCurrent._ivdriveway;
 if (true) break;

case 14:
//C
this.state = 17;
 //BA.debugLineNum = 1017;BA.debugLine="lblStatus.Text = \"Retrieving Front Door thumb";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Front Door thumbnail..."));
 //BA.debugLineNum = 1018;BA.debugLine="iv = ivFrontDoor";
_iv = parent.mostCurrent._ivfrontdoor;
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 1020;BA.debugLine="lblStatus.Text = \"Retrieving Side Yard thumbn";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Side Yard thumbnail..."));
 //BA.debugLineNum = 1021;BA.debugLine="iv = ivSideYard";
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
 //BA.debugLineNum = 1024;BA.debugLine="If camera = \"347574\" Then";
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
 //BA.debugLineNum = 1025;BA.debugLine="lblStatus.Text = \"Capturing a new Driveway th";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Driveway thumbnail..."));
 //BA.debugLineNum = 1026;BA.debugLine="iv = ivDriveway";
_iv = parent.mostCurrent._ivdriveway;
 //BA.debugLineNum = 1027;BA.debugLine="attempts = 10";
_attempts = (int) (10);
 if (true) break;

case 24:
//C
this.state = 27;
 //BA.debugLineNum = 1029;BA.debugLine="lblStatus.Text = \"Capturing a new Front Door";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Front Door thumbnail..."));
 //BA.debugLineNum = 1030;BA.debugLine="iv = ivFrontDoor";
_iv = parent.mostCurrent._ivfrontdoor;
 //BA.debugLineNum = 1031;BA.debugLine="attempts = 15";
_attempts = (int) (15);
 if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 1033;BA.debugLine="lblStatus.Text = \"Capturing a new Side Yard t";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Side Yard thumbnail..."));
 //BA.debugLineNum = 1034;BA.debugLine="iv = ivSideYard";
_iv = parent.mostCurrent._ivsideyard;
 //BA.debugLineNum = 1035;BA.debugLine="attempts = 15";
_attempts = (int) (15);
 if (true) break;

case 27:
//C
this.state = 28;
;
 if (true) break;
;
 //BA.debugLineNum = 1039;BA.debugLine="If FirstRun Then";

case 28:
//if
this.state = 80;
if (_firstrun) { 
this.state = 30;
}else {
this.state = 32;
}if (true) break;

case 30:
//C
this.state = 80;
 //BA.debugLineNum = 1041;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedi";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera);
 //BA.debugLineNum = 1042;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 88;
return;
case 88:
//C
this.state = 80;
;
 //BA.debugLineNum = 1043;BA.debugLine="GetCameraInfo(response,clv)";
_getcamerainfo(parent._response,_clv);
 //BA.debugLineNum = 1044;BA.debugLine="DownloadImage(\"https://rest-\" & userRegion &\".";
_downloadimage("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg",_iv,_camera);
 if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 1046;BA.debugLine="RESTPost(\"https://rest-\" & userRegion &\".immed";
_restpost("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera+"/thumbnail");
 //BA.debugLineNum = 1047;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 89;
return;
case 89:
//C
this.state = 33;
;
 //BA.debugLineNum = 1048;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.";
if (true) break;

case 33:
//if
this.state = 79;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 35;
}else {
this.state = 37;
}if (true) break;

case 35:
//C
this.state = 79;
 //BA.debugLineNum = 1049;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1050;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 1052;BA.debugLine="GetCommandID(response)";
_getcommandid(parent._response);
 //BA.debugLineNum = 1053;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immed";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 1054;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 90;
return;
case 90:
//C
this.state = 38;
;
 //BA.debugLineNum = 1055;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response";
if (true) break;

case 38:
//if
this.state = 78;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 40;
}else {
this.state = 42;
}if (true) break;

case 40:
//C
this.state = 78;
 //BA.debugLineNum = 1056;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1057;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 42:
//C
this.state = 43;
 //BA.debugLineNum = 1059;BA.debugLine="For i = 1 To attempts";
if (true) break;

case 43:
//for
this.state = 64;
step70 = 1;
limit70 = _attempts;
_i = (int) (1) ;
this.state = 91;
if (true) break;

case 91:
//C
this.state = 64;
if ((step70 > 0 && _i <= limit70) || (step70 < 0 && _i >= limit70)) this.state = 45;
if (true) break;

case 92:
//C
this.state = 91;
_i = ((int)(0 + _i + step70)) ;
if (true) break;

case 45:
//C
this.state = 46;
 //BA.debugLineNum = 1060;BA.debugLine="GetCommandStatus(response)";
_getcommandstatus(parent._response);
 //BA.debugLineNum = 1061;BA.debugLine="If commandComplete Then";
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
 //BA.debugLineNum = 1062;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".im";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera);
 //BA.debugLineNum = 1063;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 93;
return;
case 93:
//C
this.state = 59;
;
 //BA.debugLineNum = 1064;BA.debugLine="GetCameraInfo(response,clv)";
_getcamerainfo(parent._response,_clv);
 //BA.debugLineNum = 1065;BA.debugLine="DownloadImage(\"https://rest-\" & userRegion";
_downloadimage("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg",_iv,_camera);
 //BA.debugLineNum = 1066;BA.debugLine="Exit";
this.state = 64;
if (true) break;
 if (true) break;

case 50:
//C
this.state = 51;
 //BA.debugLineNum = 1068;BA.debugLine="If camera = \"347574\" Then";
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
 //BA.debugLineNum = 1069;BA.debugLine="lblStatus.Text = \"Awaiting for the Drivew";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Driveway thumbnail... "+BA.NumberToString(_i)+"/"+BA.NumberToString(_attempts)));
 if (true) break;

case 55:
//C
this.state = 58;
 //BA.debugLineNum = 1071;BA.debugLine="lblStatus.Text = \"Awaiting for the Front";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Front Door thumbnail...  "+BA.NumberToString(_i)+"/"+BA.NumberToString(_attempts)));
 if (true) break;

case 57:
//C
this.state = 58;
 //BA.debugLineNum = 1073;BA.debugLine="lblStatus.Text = \"Awaiting for the Side Y";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Side Yard thumbnail... "+BA.NumberToString(_i)+"/"+BA.NumberToString(_attempts)));
 if (true) break;

case 58:
//C
this.state = 59;
;
 if (true) break;

case 59:
//C
this.state = 60;
;
 //BA.debugLineNum = 1076;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".imm";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 1077;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 94;
return;
case 94:
//C
this.state = 60;
;
 //BA.debugLineNum = 1078;BA.debugLine="If response.StartsWith(\"ERROR: \") Or respon";
if (true) break;

case 60:
//if
this.state = 63;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 62;
}if (true) break;

case 62:
//C
this.state = 63;
 //BA.debugLineNum = 1079;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1080;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 63:
//C
this.state = 92;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1083;BA.debugLine="If commandComplete = False Then";

case 64:
//if
this.state = 77;
if (parent._commandcomplete==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 66;
}else {
this.state = 76;
}if (true) break;

case 66:
//C
this.state = 67;
 //BA.debugLineNum = 1084;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 67:
//if
this.state = 74;
if ((_camera).equals("347574")) { 
this.state = 69;
}else if((_camera).equals("236967")) { 
this.state = 71;
}else if((_camera).equals("226821")) { 
this.state = 73;
}if (true) break;

case 69:
//C
this.state = 74;
 //BA.debugLineNum = 1085;BA.debugLine="lblStatus.Text = \"Failed to retrieve Drive";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Driveway thumbnail..."));
 if (true) break;

case 71:
//C
this.state = 74;
 //BA.debugLineNum = 1088;BA.debugLine="lblStatus.Text = \"Failed to retrieve Front";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Front Door thumbnail..."));
 if (true) break;

case 73:
//C
this.state = 74;
 //BA.debugLineNum = 1091;BA.debugLine="lblStatus.Text = \"Failed to retrieve Side";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Side Yard thumbnail..."));
 if (true) break;

case 74:
//C
this.state = 77;
;
 if (true) break;

case 76:
//C
this.state = 77;
 if (true) break;

case 77:
//C
this.state = 78;
;
 if (true) break;

case 78:
//C
this.state = 79;
;
 if (true) break;

case 79:
//C
this.state = 80;
;
 if (true) break;

case 80:
//C
this.state = 87;
;
 if (true) break;
if (true) break;

case 81:
//C
this.state = 84;
;
 //BA.debugLineNum = 1102;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 83:
//C
this.state = 84;
this.catchState = 0;
 //BA.debugLineNum = 1104;BA.debugLine="Log(\"RefreshCamera LastException: \" & LastExcept";
anywheresoftware.b4a.keywords.Common.LogImpl("52293885","RefreshCamera LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 84:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1107;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1108;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 936;BA.debugLine="Try";
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
 //BA.debugLineNum = 937;BA.debugLine="lblStatus.Text = \"Authenticating...\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Authenticating..."));
 //BA.debugLineNum = 938;BA.debugLine="Dim jobLogin As HttpJob";
_joblogin = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 939;BA.debugLine="jobLogin.Initialize(\"\", Me)";
_joblogin._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 940;BA.debugLine="jobLogin.PostString(\"https://rest.prod.immedia-s";
_joblogin._poststring /*String*/ ("https://rest.prod.immedia-semi.com/login","email="+parent._emailaddress+"&password="+parent._password);
 //BA.debugLineNum = 941;BA.debugLine="jobLogin.GetRequest.SetContentType(\"application/";
_joblogin._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetContentType("application/x-www-form-urlencoded");
 //BA.debugLineNum = 942;BA.debugLine="Wait For (jobLogin) JobDone(jobLogin As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_joblogin));
this.state = 25;
return;
case 25:
//C
this.state = 4;
_joblogin = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 943;BA.debugLine="If jobLogin.Success Then";
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
 //BA.debugLineNum = 944;BA.debugLine="lblStatus.Text = \"Successfully logged in to the";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Successfully logged in to the Blink server..."));
 //BA.debugLineNum = 946;BA.debugLine="GetAuthInfo(jobLogin.GetString)";
_getauthinfo(_joblogin._getstring /*String*/ ());
 //BA.debugLineNum = 948;BA.debugLine="RESTGet(\"https://rest-\" & userRegion & \".immedi";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/user");
 //BA.debugLineNum = 949;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 26;
return;
case 26:
//C
this.state = 7;
;
 //BA.debugLineNum = 950;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.C";
if (true) break;

case 7:
//if
this.state = 12;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 951;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 952;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 954;BA.debugLine="lblStatus.Text = \"AuthToken acquired...\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("AuthToken acquired..."));
 //BA.debugLineNum = 955;BA.debugLine="GetUserInfo(response)";
_getuserinfo(parent._response);
 if (true) break;

case 12:
//C
this.state = 13;
;
 //BA.debugLineNum = 958;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/api/v1/camera/usage");
 //BA.debugLineNum = 959;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 27;
return;
case 27:
//C
this.state = 13;
;
 //BA.debugLineNum = 960;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.C";
if (true) break;

case 13:
//if
this.state = 18;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 15;
}else {
this.state = 17;
}if (true) break;

case 15:
//C
this.state = 18;
 //BA.debugLineNum = 961;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 962;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 964;BA.debugLine="GetAllCameras(response)";
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
 //BA.debugLineNum = 967;BA.debugLine="lblStatus.Text = GetRESTError(jobLogin.ErrorMes";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_joblogin._errormessage /*String*/ )));
 //BA.debugLineNum = 968;BA.debugLine="Log(\"RequestAuthToken error: \" & jobLogin.Error";
anywheresoftware.b4a.keywords.Common.LogImpl("52228262","RequestAuthToken error: "+_joblogin._errormessage /*String*/ ,0);
 //BA.debugLineNum = 969;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 21:
//C
this.state = 24;
;
 //BA.debugLineNum = 971;BA.debugLine="jobLogin.Release";
_joblogin._release /*String*/ ();
 //BA.debugLineNum = 972;BA.debugLine="RefreshCameras(True)";
_refreshcameras(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 23:
//C
this.state = 24;
this.catchState = 0;
 //BA.debugLineNum = 975;BA.debugLine="Log(\"RequestAuthToken LastException: \" & LastExc";
anywheresoftware.b4a.keywords.Common.LogImpl("52228269","RequestAuthToken LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 24:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 977;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1111;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 15;
this.catchState = 14;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 14;
 //BA.debugLineNum = 1112;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1113;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1114;BA.debugLine="j.Initialize(\"\", Me) 'name is empty as it is no";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1115;BA.debugLine="j.Download(url)";
_j._download /*String*/ (_url);
 //BA.debugLineNum = 1116;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1117;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 16;
return;
case 16:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1118;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 1119;BA.debugLine="response = j.GetString";
parent._response = _j._getstring /*String*/ ();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1121;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1122;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;
;
 //BA.debugLineNum = 1124;BA.debugLine="If response.Contains(\"System is busy, please wai";

case 9:
//if
this.state = 12;
if (parent._response.contains("System is busy, please wait")) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1125;BA.debugLine="lblStatus.Text = \"System is busy, please wait\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("System is busy, please wait"));
 if (true) break;

case 12:
//C
this.state = 15;
;
 //BA.debugLineNum = 1127;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 14:
//C
this.state = 15;
this.catchState = 0;
 //BA.debugLineNum = 1129;BA.debugLine="response = \"ERROR: \" & LastException";
parent._response = "ERROR: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA));
 //BA.debugLineNum = 1130;BA.debugLine="Log(\"RESTDownload LastException: \" & LastExcepti";
anywheresoftware.b4a.keywords.Common.LogImpl("52359316","RESTDownload LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 15:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1133;BA.debugLine="Log(\"URL: \" & url)";
anywheresoftware.b4a.keywords.Common.LogImpl("52359319","URL: "+_url,0);
 //BA.debugLineNum = 1134;BA.debugLine="Log(\"Response: \" & response)";
anywheresoftware.b4a.keywords.Common.LogImpl("52359320","Response: "+parent._response,0);
 //BA.debugLineNum = 1135;BA.debugLine="Return(response)";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)((parent._response)));return;};
 //BA.debugLineNum = 1136;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1139;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 15;
this.catchState = 14;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 14;
 //BA.debugLineNum = 1140;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1141;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1142;BA.debugLine="j.Initialize(\"\", Me) 'name is empty as it is no";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1143;BA.debugLine="j.PostString(url,\"\")";
_j._poststring /*String*/ (_url,"");
 //BA.debugLineNum = 1144;BA.debugLine="j.GetRequest.SetContentType(\"application/x-www-f";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetContentType("application/x-www-form-urlencoded");
 //BA.debugLineNum = 1145;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1146;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 16;
return;
case 16:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1147;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 1148;BA.debugLine="response = j.GetString";
parent._response = _j._getstring /*String*/ ();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1150;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1151;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;
;
 //BA.debugLineNum = 1153;BA.debugLine="If response.Contains(\"System is busy, please wai";

case 9:
//if
this.state = 12;
if (parent._response.contains("System is busy, please wait")) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1154;BA.debugLine="lblStatus.Text = \"System is busy, please wait\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("System is busy, please wait"));
 if (true) break;

case 12:
//C
this.state = 15;
;
 //BA.debugLineNum = 1156;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 14:
//C
this.state = 15;
this.catchState = 0;
 //BA.debugLineNum = 1158;BA.debugLine="response = \"ERROR: \" & LastException";
parent._response = "ERROR: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA));
 //BA.debugLineNum = 1159;BA.debugLine="Log(\"RESTPost LastException: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("52424853","RESTPost LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 15:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1162;BA.debugLine="Log(\"URL: \" & url)";
anywheresoftware.b4a.keywords.Common.LogImpl("52424856","URL: "+_url,0);
 //BA.debugLineNum = 1163;BA.debugLine="Log(\"Response: \" & response)";
anywheresoftware.b4a.keywords.Common.LogImpl("52424857","Response: "+parent._response,0);
 //BA.debugLineNum = 1164;BA.debugLine="Return(response)";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)((parent._response)));return;};
 //BA.debugLineNum = 1165;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1625;BA.debugLine="Private Sub SetAvg(bcBitmap As BitmapCreator, x As";
 //BA.debugLineNum = 1626;BA.debugLine="temp.Initialize";
_temp.Initialize();
 //BA.debugLineNum = 1627;BA.debugLine="For Each c As ARGBColor In clrs";
{
final b4a.example.bitmapcreator._argbcolor[] group2 = _clrs;
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_c = group2[index2];
 //BA.debugLineNum = 1628;BA.debugLine="temp.r = temp.r + c.r";
_temp.r = (int) (_temp.r+_c.r);
 //BA.debugLineNum = 1629;BA.debugLine="temp.g = temp.g + c.g";
_temp.g = (int) (_temp.g+_c.g);
 //BA.debugLineNum = 1630;BA.debugLine="temp.b = temp.b + c.b";
_temp.b = (int) (_temp.b+_c.b);
 }
};
 //BA.debugLineNum = 1632;BA.debugLine="temp.a = 255";
_temp.a = (int) (255);
 //BA.debugLineNum = 1633;BA.debugLine="temp.r = temp.r / clrs.Length";
_temp.r = (int) (_temp.r/(double)_clrs.length);
 //BA.debugLineNum = 1634;BA.debugLine="temp.g = temp.g / clrs.Length";
_temp.g = (int) (_temp.g/(double)_clrs.length);
 //BA.debugLineNum = 1635;BA.debugLine="temp.b = temp.b / clrs.Length";
_temp.b = (int) (_temp.b/(double)_clrs.length);
 //BA.debugLineNum = 1636;BA.debugLine="bcBitmap.SetARGB(x, y, temp)";
_bcbitmap._setargb(_x,_y,_temp);
 //BA.debugLineNum = 1637;BA.debugLine="End Sub";
return "";
}
public static String  _setdefaults() throws Exception{
 //BA.debugLineNum = 908;BA.debugLine="Sub SetDefaults";
 //BA.debugLineNum = 910;BA.debugLine="manager.SetString(\"TempHumidityCooldownTime\", \"5\"";
_manager.SetString("TempHumidityCooldownTime","5");
 //BA.debugLineNum = 911;BA.debugLine="manager.SetString(\"TempHumidityCooldownTimeBaseme";
_manager.SetString("TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 912;BA.debugLine="manager.SetString(\"HumidityAddValue\", \"0\")";
_manager.SetString("HumidityAddValue","0");
 //BA.debugLineNum = 913;BA.debugLine="manager.SetString(\"SensorNotRespondingTime\", \"5\")";
_manager.SetString("SensorNotRespondingTime","5");
 //BA.debugLineNum = 914;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTime","5");
 //BA.debugLineNum = 915;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 916;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",\"0\")";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue","0");
 //BA.debugLineNum = 917;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime","5");
 //BA.debugLineNum = 918;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 919;BA.debugLine="End Sub";
return "";
}
public static String  _settextshadow(anywheresoftware.b4a.objects.LabelWrapper _lbl) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
float _radius = 0f;
float _dx = 0f;
float _dy = 0f;
 //BA.debugLineNum = 1639;BA.debugLine="Sub SetTextShadow(lbl As Label)";
 //BA.debugLineNum = 1640;BA.debugLine="Dim jo As JavaObject = lbl";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 1641;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Float";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1642;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Black)});
 //BA.debugLineNum = 1643;BA.debugLine="End Sub";
return "";
}
public static String  _showaboutmenu() throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 519;BA.debugLine="Sub ShowAboutMenu";
 //BA.debugLineNum = 520;BA.debugLine="Try";
try { //BA.debugLineNum = 521;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 522;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cloyd.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 523;BA.debugLine="Msgbox2(\"Smart Home Monitor v\" & GetVersionCode";
anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Smart Home Monitor v"+_getversioncode()+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Developed by Cloyd Nino Catanaoan"+anywheresoftware.b4a.keywords.Common.CRLF+"September 24, 2019"),BA.ObjectToCharSequence("About"),"OK","","",_bd.getBitmap(),mostCurrent.activityBA);
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 525;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51245190",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 527;BA.debugLine="End Sub";
return "";
}
public static void  _showvideo(String _link) throws Exception{
ResumableSub_ShowVideo rsub = new ResumableSub_ShowVideo(null,_link);
rsub.resume(processBA, null);
}
public static class ResumableSub_ShowVideo extends BA.ResumableSub {
public ResumableSub_ShowVideo(cloyd.smart.home.monitor.main parent,String _link) {
this.parent = parent;
this._link = _link;
}
cloyd.smart.home.monitor.main parent;
String _link;
cloyd.smart.home.monitor.httpjob _j = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
anywheresoftware.b4a.keywords.StringBuilderWrapper _sb = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;

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
 //BA.debugLineNum = 1855;BA.debugLine="Try";
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
 //BA.debugLineNum = 1856;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1857;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1858;BA.debugLine="j.Download(Link)";
_j._download /*String*/ (_link);
 //BA.debugLineNum = 1859;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1860;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 13;
return;
case 13:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1861;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 1863;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File.";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"media.mp4",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1864;BA.debugLine="File.Copy2(j.GetInputStream, out)";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_j._getinputstream /*anywheresoftware.b4a.objects.streams.File.InputStreamWrapper*/ ().getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 1865;BA.debugLine="out.Close '<------ very important";
_out.Close();
 //BA.debugLineNum = 1867;BA.debugLine="Dim sb As StringBuilder";
_sb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 1868;BA.debugLine="sb.Initialize";
_sb.Initialize();
 //BA.debugLineNum = 1869;BA.debugLine="sb.Append(\"<video width='100%' height='100%' co";
_sb.Append("<video width='100%' height='100%' controls autoplay>");
 //BA.debugLineNum = 1870;BA.debugLine="sb.Append(\"<source src='\" & File.Combine(File.D";
_sb.Append("<source src='"+anywheresoftware.b4a.keywords.Common.File.Combine(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"\\media.mp4")+"' Type='video/mp4'/>");
 //BA.debugLineNum = 1871;BA.debugLine="sb.Append(\"</video>\")";
_sb.Append("</video>");
 //BA.debugLineNum = 1872;BA.debugLine="Dim WebViewSettings1 As WebViewSettings";
parent.mostCurrent._webviewsettings1 = new uk.co.martinpearman.b4a.webviewsettings.WebViewSettings();
 //BA.debugLineNum = 1873;BA.debugLine="WebViewSettings1.setMediaPlaybackRequiresUserGe";
parent.mostCurrent._webviewsettings1.setMediaPlaybackRequiresUserGesture((android.webkit.WebView)(parent.mostCurrent._wvmedia.getObject()),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1874;BA.debugLine="wvMedia.LoadHtml(sb.ToString)";
parent.mostCurrent._wvmedia.LoadHtml(_sb.ToString());
 //BA.debugLineNum = 1876;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 1877;BA.debugLine="r.Target = wvMedia";
_r.Target = (Object)(parent.mostCurrent._wvmedia.getObject());
 //BA.debugLineNum = 1878;BA.debugLine="r.Target = r.RunMethod(\"getSettings\")";
_r.Target = _r.RunMethod("getSettings");
 //BA.debugLineNum = 1879;BA.debugLine="r.RunMethod2(\"setBuiltInZoomControls\", True, \"j";
_r.RunMethod2("setBuiltInZoomControls",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True),"java.lang.boolean");
 //BA.debugLineNum = 1880;BA.debugLine="r.RunMethod2(\"setDisplayZoomControls\", False, \"";
_r.RunMethod2("setDisplayZoomControls",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.False),"java.lang.boolean");
 if (true) break;

case 8:
//C
this.state = 9;
 if (true) break;

case 9:
//C
this.state = 12;
;
 //BA.debugLineNum = 1884;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 11:
//C
this.state = 12;
this.catchState = 0;
 //BA.debugLineNum = 1886;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("528180512",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 12:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1888;BA.debugLine="End Sub";
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
public static void  _tabstrip1_pageselected(int _position) throws Exception{
ResumableSub_TabStrip1_PageSelected rsub = new ResumableSub_TabStrip1_PageSelected(null,_position);
rsub.resume(processBA, null);
}
public static class ResumableSub_TabStrip1_PageSelected extends BA.ResumableSub {
public ResumableSub_TabStrip1_PageSelected(cloyd.smart.home.monitor.main parent,int _position) {
this.parent = parent;
this._position = _position;
}
cloyd.smart.home.monitor.main parent;
int _position;

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
 //BA.debugLineNum = 576;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 47;
this.catchState = 46;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 46;
 //BA.debugLineNum = 577;BA.debugLine="B4XPageIndicator1.CurrentPage = Position";
parent.mostCurrent._b4xpageindicator1._setcurrentpage /*int*/ (_position);
 //BA.debugLineNum = 578;BA.debugLine="If Position = 0 Then";
if (true) break;

case 4:
//if
this.state = 11;
if (_position==0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 579;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (true) break;

case 7:
//if
this.state = 10;
if (parent._mqtt.IsInitialized() && parent._mqtt.getConnected()) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 580;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Re";
parent._mqtt.Publish("TempHumid",parent._bc.StringToBytes("Read weather","utf8"));
 if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 582;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 if (true) break;
;
 //BA.debugLineNum = 584;BA.debugLine="If Position = 1 Then";

case 11:
//if
this.state = 18;
if (_position==1) { 
this.state = 13;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 585;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (true) break;

case 14:
//if
this.state = 17;
if (parent._mqtt.IsInitialized() && parent._mqtt.getConnected()) { 
this.state = 16;
}if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 586;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Read vol";
parent._mqtt.Publish("MQ7",parent._bc.StringToBytes("Read voltage","utf8"));
 if (true) break;

case 17:
//C
this.state = 18;
;
 //BA.debugLineNum = 588;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 if (true) break;
;
 //BA.debugLineNum = 590;BA.debugLine="If Position = 2 Then";

case 18:
//if
this.state = 25;
if (_position==2) { 
this.state = 20;
}if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 591;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (true) break;

case 21:
//if
this.state = 24;
if (parent._mqtt.IsInitialized() && parent._mqtt.getConnected()) { 
this.state = 23;
}if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 592;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringToB";
parent._mqtt.Publish("TempHumidBasement",parent._bc.StringToBytes("Read weather","utf8"));
 if (true) break;

case 24:
//C
this.state = 25;
;
 //BA.debugLineNum = 594;BA.debugLine="CheckTempHumiditySettingBasement";
_checktemphumiditysettingbasement();
 if (true) break;
;
 //BA.debugLineNum = 596;BA.debugLine="If Position = 3 Then";

case 25:
//if
this.state = 32;
if (_position==3) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 597;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (true) break;

case 28:
//if
this.state = 31;
if (parent._mqtt.IsInitialized() && parent._mqtt.getConnected()) { 
this.state = 30;
}if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 598;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes(\"";
parent._mqtt.Publish("MQ7Basement",parent._bc.StringToBytes("Read voltage","utf8"));
 if (true) break;

case 31:
//C
this.state = 32;
;
 //BA.debugLineNum = 600;BA.debugLine="CheckAirQualitySettingBasement";
_checkairqualitysettingbasement();
 if (true) break;
;
 //BA.debugLineNum = 602;BA.debugLine="If Position = 4 Then";

case 32:
//if
this.state = 35;
if (_position==4) { 
this.state = 34;
}if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 603;BA.debugLine="wvMedia.LoadUrl(\"\")";
parent.mostCurrent._wvmedia.LoadUrl("");
 if (true) break;
;
 //BA.debugLineNum = 605;BA.debugLine="If Position = 5 Then";

case 35:
//if
this.state = 44;
if (_position==5) { 
this.state = 37;
}if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 606;BA.debugLine="If userRegion <> \"\" Then";
if (true) break;

case 38:
//if
this.state = 43;
if ((parent._userregion).equals("") == false) { 
this.state = 40;
}else {
this.state = 42;
}if (true) break;

case 40:
//C
this.state = 43;
 //BA.debugLineNum = 607;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedi";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/api/v1/accounts/88438/media/changed?since=-999999999-01-01T00:00:00+18:00&page=1");
 //BA.debugLineNum = 608;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 48;
return;
case 48:
//C
this.state = 43;
;
 //BA.debugLineNum = 609;BA.debugLine="clvActivity.Clear";
parent.mostCurrent._clvactivity._clear();
 //BA.debugLineNum = 610;BA.debugLine="GetVideos(response)";
_getvideos(parent._response);
 if (true) break;

case 42:
//C
this.state = 43;
 //BA.debugLineNum = 612;BA.debugLine="ToastMessageShow(\"Still connecting to Blink se";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Still connecting to Blink servers..."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 43:
//C
this.state = 44;
;
 if (true) break;

case 44:
//C
this.state = 47;
;
 if (true) break;

case 46:
//C
this.state = 47;
this.catchState = 0;
 //BA.debugLineNum = 617;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51572906",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 47:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 619;BA.debugLine="End Sub";
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

public boolean _onCreateOptionsMenu(android.view.Menu menu) {
    if (processBA.subExists("activity_createmenu")) {
        processBA.raiseEvent2(null, true, "activity_createmenu", false, new de.amberhome.objects.appcompat.ACMenuWrapper(menu));
        return true;
    }
    else
        return false;
}
}
