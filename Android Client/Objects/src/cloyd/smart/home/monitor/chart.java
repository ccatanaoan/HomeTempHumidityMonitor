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

public class chart extends Activity implements B4AActivity{
	public static chart mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "cloyd.smart.home.monitor", "cloyd.smart.home.monitor.chart");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (chart).");
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
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
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
		activityBA = new BA(this, layout, processBA, "cloyd.smart.home.monitor", "cloyd.smart.home.monitor.chart");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "cloyd.smart.home.monitor.chart", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (chart) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (chart) Resume **");
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
		return chart.class;
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
        BA.LogInfo("** Activity (chart) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            chart mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (chart) Resume **");
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
public static anywheresoftware.b4a.phone.Phone.PhoneWakeState _awake = null;
public static anywheresoftware.b4a.objects.Timer _temperaturehourlytimer = null;
public static anywheresoftware.b4a.objects.Timer _humidityhourlytimer = null;
public static anywheresoftware.b4a.objects.Timer _temperaturedailytimer = null;
public static anywheresoftware.b4a.objects.Timer _humiditydailytimer = null;
public static anywheresoftware.b4a.objects.RuntimePermissions _rp = null;
public static String _shared = "";
public static anywheresoftware.b4a.phone.Phone _phone1 = null;
public androidplotwrapper.lineChartWrapper _linechart = null;
public static String _am12 = "";
public static String _am1 = "";
public static String _am2 = "";
public static String _am3 = "";
public static String _am4 = "";
public static String _am5 = "";
public static String _am6 = "";
public static String _am7 = "";
public static String _am8 = "";
public static String _am9 = "";
public static String _am10 = "";
public static String _am11 = "";
public static String _pm12 = "";
public static String _pm1 = "";
public static String _pm2 = "";
public static String _pm3 = "";
public static String _pm4 = "";
public static String _pm5 = "";
public static String _pm6 = "";
public static String _pm7 = "";
public static String _pm8 = "";
public static String _pm9 = "";
public static String _pm10 = "";
public static String _pm11 = "";
public static String _temprightnow = "";
public static long _timerightnow = 0L;
public static String[] _timearray = null;
public static float _zerorange = 0f;
public static float _tempzerorange = 0f;
public static float _tempmaxrange = 0f;
public static float _tempminrange = 0f;
public anywheresoftware.b4a.objects.ButtonWrapper _btnhumidityhourly = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btntemphourly = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnhumiditydaily = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btntempdaily = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.main _main = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.b4xcollections _b4xcollections = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 62;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 63;BA.debugLine="Activity.LoadLayout(\"chart\")";
mostCurrent._activity.LoadLayout("chart",mostCurrent.activityBA);
 //BA.debugLineNum = 65;BA.debugLine="phone1.SetScreenOrientation(0) 'landscape";
_phone1.SetScreenOrientation(processBA,(int) (0));
 //BA.debugLineNum = 67;BA.debugLine="btnTempHourly_Click";
_btntemphourly_click();
 //BA.debugLineNum = 68;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 74;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 75;BA.debugLine="Awake.ReleaseKeepAlive";
_awake.ReleaseKeepAlive();
 //BA.debugLineNum = 76;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 77;BA.debugLine="phone1.SetScreenOrientation(1) 'portrait";
_phone1.SetScreenOrientation(processBA,(int) (1));
 };
 //BA.debugLineNum = 79;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 70;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 71;BA.debugLine="Awake.KeepAlive(True)";
_awake.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 72;BA.debugLine="End Sub";
return "";
}
public static String  _activity_windowfocuschanged(boolean _hasfocus) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 2235;BA.debugLine="Sub Activity_WindowFocusChanged(HasFocus As Boolea";
 //BA.debugLineNum = 2236;BA.debugLine="If HasFocus Then";
if (_hasfocus) { 
 //BA.debugLineNum = 2237;BA.debugLine="Try";
try { //BA.debugLineNum = 2238;BA.debugLine="Dim jo As JavaObject = Activity";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._activity.getObject()));
 //BA.debugLineNum = 2240;BA.debugLine="jo.RunMethod(\"setSystemUiVisibility\", Array As";
_jo.RunMethod("setSystemUiVisibility",new Object[]{(Object)(5894)});
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); };
 };
 //BA.debugLineNum = 2246;BA.debugLine="End Sub";
return "";
}
public static String  _btnhumiditydaily_click() throws Exception{
 //BA.debugLineNum = 114;BA.debugLine="Sub btnHumidityDaily_Click";
 //BA.debugLineNum = 115;BA.debugLine="TemperatureHourlyTimer.Enabled = False";
_temperaturehourlytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 116;BA.debugLine="HumidityHourlyTimer.Enabled = False";
_humidityhourlytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 117;BA.debugLine="TemperatureDailyTimer.Enabled = False";
_temperaturedailytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 118;BA.debugLine="tempMaxRange=0";
_tempmaxrange = (float) (0);
 //BA.debugLineNum = 119;BA.debugLine="tempMinRange=0";
_tempminrange = (float) (0);
 //BA.debugLineNum = 120;BA.debugLine="HumidityDailyCreate";
_humiditydailycreate();
 //BA.debugLineNum = 121;BA.debugLine="HumidityDailyTimer.Initialize(\"HumidityDailyTimer";
_humiditydailytimer.Initialize(processBA,"HumidityDailyTimer",(long) (1000));
 //BA.debugLineNum = 122;BA.debugLine="HumidityDailyTimer.Enabled = True 'start timer";
_humiditydailytimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _btnhumidityhourly_click() throws Exception{
 //BA.debugLineNum = 92;BA.debugLine="Sub btnHumidityHourly_Click";
 //BA.debugLineNum = 93;BA.debugLine="TemperatureDailyTimer.Enabled = False";
_temperaturedailytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 94;BA.debugLine="TemperatureHourlyTimer.Enabled = False";
_temperaturehourlytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 95;BA.debugLine="HumidityDailyTimer.Enabled = False";
_humiditydailytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 96;BA.debugLine="tempMaxRange=0";
_tempmaxrange = (float) (0);
 //BA.debugLineNum = 97;BA.debugLine="tempMinRange=0";
_tempminrange = (float) (0);
 //BA.debugLineNum = 98;BA.debugLine="HumidityHourlyCreate";
_humidityhourlycreate();
 //BA.debugLineNum = 99;BA.debugLine="HumidityHourlyTimer.Initialize(\"HumidityHourlyTim";
_humidityhourlytimer.Initialize(processBA,"HumidityHourlyTimer",(long) (1000));
 //BA.debugLineNum = 100;BA.debugLine="HumidityHourlyTimer.Enabled = True 'start timer";
_humidityhourlytimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
public static String  _btntempdaily_click() throws Exception{
 //BA.debugLineNum = 103;BA.debugLine="Sub btnTempDaily_Click";
 //BA.debugLineNum = 104;BA.debugLine="TemperatureHourlyTimer.Enabled = False";
_temperaturehourlytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 105;BA.debugLine="HumidityHourlyTimer.Enabled = False";
_humidityhourlytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 106;BA.debugLine="HumidityDailyTimer.Enabled = False";
_humiditydailytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 107;BA.debugLine="tempMaxRange=0";
_tempmaxrange = (float) (0);
 //BA.debugLineNum = 108;BA.debugLine="tempMinRange=0";
_tempminrange = (float) (0);
 //BA.debugLineNum = 109;BA.debugLine="TemperatureDailyCreate";
_temperaturedailycreate();
 //BA.debugLineNum = 110;BA.debugLine="TemperatureDailyTimer.Initialize(\"TemperatureDail";
_temperaturedailytimer.Initialize(processBA,"TemperatureDailyTimer",(long) (1000));
 //BA.debugLineNum = 111;BA.debugLine="TemperatureDailyTimer.Enabled = True 'start timer";
_temperaturedailytimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
return "";
}
public static String  _btntemphourly_click() throws Exception{
 //BA.debugLineNum = 81;BA.debugLine="Sub btnTempHourly_Click";
 //BA.debugLineNum = 82;BA.debugLine="TemperatureDailyTimer.Enabled = False";
_temperaturedailytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 83;BA.debugLine="HumidityHourlyTimer.Enabled = False";
_humidityhourlytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 84;BA.debugLine="HumidityDailyTimer.Enabled = False";
_humiditydailytimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 85;BA.debugLine="tempMaxRange=0";
_tempmaxrange = (float) (0);
 //BA.debugLineNum = 86;BA.debugLine="tempMinRange=0";
_tempminrange = (float) (0);
 //BA.debugLineNum = 87;BA.debugLine="TemperatureHourlyCreate";
_temperaturehourlycreate();
 //BA.debugLineNum = 88;BA.debugLine="TemperatureHourlyTimer.Initialize(\"TemperatureHou";
_temperaturehourlytimer.Initialize(processBA,"TemperatureHourlyTimer",(long) (1000));
 //BA.debugLineNum = 89;BA.debugLine="TemperatureHourlyTimer.Enabled = True 'start time";
_temperaturehourlytimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
public static String  _checktempboundaries() throws Exception{
anywheresoftware.b4a.objects.collections.List _templist = null;
float _minvalue = 0f;
float _maxvalue = 0f;
 //BA.debugLineNum = 2033;BA.debugLine="Sub CheckTempBoundaries";
 //BA.debugLineNum = 2034;BA.debugLine="Dim tempList As List";
_templist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 2035;BA.debugLine="tempList.Initialize";
_templist.Initialize();
 //BA.debugLineNum = 2036;BA.debugLine="tempList.AddAll(Array As Float (am12, am1, am2, a";
_templist.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))}));
 //BA.debugLineNum = 2037;BA.debugLine="tempList.Sort(True)";
_templist.Sort(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2040;BA.debugLine="tempZeroRange = tempList.Get(0)-1.5";
_tempzerorange = (float) ((double)(BA.ObjectToNumber(_templist.Get((int) (0))))-1.5);
 //BA.debugLineNum = 2042;BA.debugLine="If am12 = zeroRange Then am12 = tempZeroRange";
if ((mostCurrent._am12).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am12 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2043;BA.debugLine="If am1 = zeroRange Then am1 = tempZeroRange";
if ((mostCurrent._am1).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am1 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2044;BA.debugLine="If am2 = zeroRange Then am2 = tempZeroRange";
if ((mostCurrent._am2).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am2 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2045;BA.debugLine="If am3 = zeroRange Then am3 = tempZeroRange";
if ((mostCurrent._am3).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am3 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2046;BA.debugLine="If am4 = zeroRange Then am4 = tempZeroRange";
if ((mostCurrent._am4).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am4 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2047;BA.debugLine="If am5 = zeroRange Then am5 = tempZeroRange";
if ((mostCurrent._am5).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am5 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2048;BA.debugLine="If am6 = zeroRange Then am6 = tempZeroRange";
if ((mostCurrent._am6).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am6 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2049;BA.debugLine="If am7 = zeroRange Then am7 = tempZeroRange";
if ((mostCurrent._am7).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am7 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2050;BA.debugLine="If am8 = zeroRange Then am8 = tempZeroRange";
if ((mostCurrent._am8).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am8 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2051;BA.debugLine="If am9 = zeroRange Then am9 = tempZeroRange";
if ((mostCurrent._am9).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am9 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2052;BA.debugLine="If am10 = zeroRange Then am10 = tempZeroRange";
if ((mostCurrent._am10).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am10 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2053;BA.debugLine="If am11 = zeroRange Then am11 = tempZeroRange";
if ((mostCurrent._am11).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am11 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2054;BA.debugLine="If pm12 = zeroRange Then pm12 = tempZeroRange";
if ((mostCurrent._pm12).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm12 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2055;BA.debugLine="If pm1 = zeroRange Then pm1 = tempZeroRange";
if ((mostCurrent._pm1).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm1 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2056;BA.debugLine="If pm2 = zeroRange Then pm2 = tempZeroRange";
if ((mostCurrent._pm2).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm2 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2057;BA.debugLine="If pm3 = zeroRange Then pm3 = tempZeroRange";
if ((mostCurrent._pm3).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm3 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2058;BA.debugLine="If pm4 = zeroRange Then pm4 = tempZeroRange";
if ((mostCurrent._pm4).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm4 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2059;BA.debugLine="If pm5 = zeroRange Then pm5 = tempZeroRange";
if ((mostCurrent._pm5).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm5 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2060;BA.debugLine="If pm6 = zeroRange Then pm6 = tempZeroRange";
if ((mostCurrent._pm6).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm6 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2061;BA.debugLine="If pm7 = zeroRange Then pm7 = tempZeroRange";
if ((mostCurrent._pm7).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm7 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2062;BA.debugLine="If pm8 = zeroRange Then pm8 = tempZeroRange";
if ((mostCurrent._pm8).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm8 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2063;BA.debugLine="If pm9 = zeroRange Then pm9 = tempZeroRange";
if ((mostCurrent._pm9).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm9 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2064;BA.debugLine="If pm10 = zeroRange Then pm10 = tempZeroRange";
if ((mostCurrent._pm10).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm10 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2065;BA.debugLine="If pm11 = zeroRange Then pm11 = tempZeroRange";
if ((mostCurrent._pm11).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm11 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2067;BA.debugLine="tempList.Initialize";
_templist.Initialize();
 //BA.debugLineNum = 2068;BA.debugLine="tempList.AddAll(Array As Float (am12, am1, am2, a";
_templist.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))}));
 //BA.debugLineNum = 2069;BA.debugLine="tempList.Sort(True)";
_templist.Sort(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2071;BA.debugLine="Dim minValue=0, maxValue=0 As Float";
_minvalue = (float) (0);
_maxvalue = (float) (0);
 //BA.debugLineNum = 2072;BA.debugLine="If tempRightNow <= tempList.Get(0) Then";
if ((double)(Double.parseDouble(mostCurrent._temprightnow))<=(double)(BA.ObjectToNumber(_templist.Get((int) (0))))) { 
 //BA.debugLineNum = 2073;BA.debugLine="minValue = tempRightNow-1.5";
_minvalue = (float) ((double)(Double.parseDouble(mostCurrent._temprightnow))-1.5);
 }else {
 //BA.debugLineNum = 2075;BA.debugLine="minValue = tempList.Get(0)-1.5";
_minvalue = (float) ((double)(BA.ObjectToNumber(_templist.Get((int) (0))))-1.5);
 };
 //BA.debugLineNum = 2078;BA.debugLine="If tempList.Get(tempList.Size-1) >= 88.88 Then";
if ((double)(BA.ObjectToNumber(_templist.Get((int) (_templist.getSize()-1))))>=88.88) { 
 //BA.debugLineNum = 2079;BA.debugLine="If tempRightNow >= (tempList.Get(tempList.Size-2";
if ((double)(Double.parseDouble(mostCurrent._temprightnow))>=(double)(BA.ObjectToNumber((_templist.Get((int) (_templist.getSize()-2)))))) { 
 //BA.debugLineNum = 2080;BA.debugLine="maxValue = tempRightNow+1.5";
_maxvalue = (float) ((double)(Double.parseDouble(mostCurrent._temprightnow))+1.5);
 }else {
 //BA.debugLineNum = 2082;BA.debugLine="maxValue = (tempList.Get(tempList.Size-2))+1.5";
_maxvalue = (float) ((double)(BA.ObjectToNumber((_templist.Get((int) (_templist.getSize()-2)))))+1.5);
 };
 }else {
 //BA.debugLineNum = 2085;BA.debugLine="If tempRightNow >= (tempList.Get(tempList.Size-1";
if ((double)(Double.parseDouble(mostCurrent._temprightnow))>=(double)(BA.ObjectToNumber((_templist.Get((int) (_templist.getSize()-1)))))) { 
 //BA.debugLineNum = 2086;BA.debugLine="maxValue = tempRightNow+1.5";
_maxvalue = (float) ((double)(Double.parseDouble(mostCurrent._temprightnow))+1.5);
 }else {
 //BA.debugLineNum = 2088;BA.debugLine="maxValue = (tempList.Get(tempList.Size-1))+1.5";
_maxvalue = (float) ((double)(BA.ObjectToNumber((_templist.Get((int) (_templist.getSize()-1)))))+1.5);
 };
 };
 //BA.debugLineNum = 2091;BA.debugLine="LineChart.YaxisRange(minValue, maxValue)";
mostCurrent._linechart.YaxisRange(_minvalue,_maxvalue);
 //BA.debugLineNum = 2092;BA.debugLine="End Sub";
return "";
}
public static String  _checktempboundariesdaily() throws Exception{
anywheresoftware.b4a.objects.collections.List _templist = null;
float _minvalue = 0f;
float _maxvalue = 0f;
 //BA.debugLineNum = 2094;BA.debugLine="Sub CheckTempBoundariesDaily";
 //BA.debugLineNum = 2095;BA.debugLine="Dim tempList As List";
_templist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 2096;BA.debugLine="tempList.Initialize";
_templist.Initialize();
 //BA.debugLineNum = 2097;BA.debugLine="tempList.AddAll(Array As Float (am12, am1, am2, a";
_templist.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))}));
 //BA.debugLineNum = 2098;BA.debugLine="tempList.Sort(True)";
_templist.Sort(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2100;BA.debugLine="tempZeroRange = tempList.Get(0)-1.5";
_tempzerorange = (float) ((double)(BA.ObjectToNumber(_templist.Get((int) (0))))-1.5);
 //BA.debugLineNum = 2102;BA.debugLine="If am12 = zeroRange Then am12 = tempZeroRange";
if ((mostCurrent._am12).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am12 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2103;BA.debugLine="If am1 = zeroRange Then am1 = tempZeroRange";
if ((mostCurrent._am1).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am1 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2104;BA.debugLine="If am2 = zeroRange Then am2 = tempZeroRange";
if ((mostCurrent._am2).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am2 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2105;BA.debugLine="If am3 = zeroRange Then am3 = tempZeroRange";
if ((mostCurrent._am3).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am3 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2106;BA.debugLine="If am4 = zeroRange Then am4 = tempZeroRange";
if ((mostCurrent._am4).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am4 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2107;BA.debugLine="If am5 = zeroRange Then am5 = tempZeroRange";
if ((mostCurrent._am5).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am5 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2108;BA.debugLine="If am6 = zeroRange Then am6 = tempZeroRange";
if ((mostCurrent._am6).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am6 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2109;BA.debugLine="If am7 = zeroRange Then am7 = tempZeroRange";
if ((mostCurrent._am7).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am7 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2110;BA.debugLine="If am8 = zeroRange Then am8 = tempZeroRange";
if ((mostCurrent._am8).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am8 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2111;BA.debugLine="If am9 = zeroRange Then am9 = tempZeroRange";
if ((mostCurrent._am9).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am9 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2112;BA.debugLine="If am10 = zeroRange Then am10 = tempZeroRange";
if ((mostCurrent._am10).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am10 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2113;BA.debugLine="If am11 = zeroRange Then am11 = tempZeroRange";
if ((mostCurrent._am11).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._am11 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2114;BA.debugLine="If pm12 = zeroRange Then pm12 = tempZeroRange";
if ((mostCurrent._pm12).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm12 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2115;BA.debugLine="If pm1 = zeroRange Then pm1 = tempZeroRange";
if ((mostCurrent._pm1).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm1 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2116;BA.debugLine="If pm2 = zeroRange Then pm2 = tempZeroRange";
if ((mostCurrent._pm2).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm2 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2117;BA.debugLine="If pm3 = zeroRange Then pm3 = tempZeroRange";
if ((mostCurrent._pm3).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm3 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2118;BA.debugLine="If pm4 = zeroRange Then pm4 = tempZeroRange";
if ((mostCurrent._pm4).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm4 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2119;BA.debugLine="If pm5 = zeroRange Then pm5 = tempZeroRange";
if ((mostCurrent._pm5).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm5 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2120;BA.debugLine="If pm6 = zeroRange Then pm6 = tempZeroRange";
if ((mostCurrent._pm6).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm6 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2121;BA.debugLine="If pm7 = zeroRange Then pm7 = tempZeroRange";
if ((mostCurrent._pm7).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm7 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2122;BA.debugLine="If pm8 = zeroRange Then pm8 = tempZeroRange";
if ((mostCurrent._pm8).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm8 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2123;BA.debugLine="If pm9 = zeroRange Then pm9 = tempZeroRange";
if ((mostCurrent._pm9).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm9 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2124;BA.debugLine="If pm10 = zeroRange Then pm10 = tempZeroRange";
if ((mostCurrent._pm10).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm10 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2125;BA.debugLine="If pm11 = zeroRange Then pm11 = tempZeroRange";
if ((mostCurrent._pm11).equals(BA.NumberToString(_zerorange))) { 
mostCurrent._pm11 = BA.NumberToString(_tempzerorange);};
 //BA.debugLineNum = 2127;BA.debugLine="tempList.Initialize";
_templist.Initialize();
 //BA.debugLineNum = 2128;BA.debugLine="tempList.AddAll(Array As Float (am12, am1, am2, a";
_templist.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))}));
 //BA.debugLineNum = 2129;BA.debugLine="tempList.Sort(True)";
_templist.Sort(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2131;BA.debugLine="Dim minValue=0, maxValue=0 As Float";
_minvalue = (float) (0);
_maxvalue = (float) (0);
 //BA.debugLineNum = 2133;BA.debugLine="If tempRightNow <= tempList.Get(0) Then";
if ((double)(Double.parseDouble(mostCurrent._temprightnow))<=(double)(BA.ObjectToNumber(_templist.Get((int) (0))))) { 
 //BA.debugLineNum = 2134;BA.debugLine="If tempMinRange <= tempRightNow Then";
if (_tempminrange<=(double)(Double.parseDouble(mostCurrent._temprightnow))) { 
 //BA.debugLineNum = 2135;BA.debugLine="minValue = tempMinRange-1.5";
_minvalue = (float) (_tempminrange-1.5);
 }else {
 //BA.debugLineNum = 2137;BA.debugLine="minValue = tempRightNow-1.5";
_minvalue = (float) ((double)(Double.parseDouble(mostCurrent._temprightnow))-1.5);
 };
 }else {
 //BA.debugLineNum = 2140;BA.debugLine="If tempMinRange > 0 And tempMinRange <= tempList";
if (_tempminrange>0 && _tempminrange<=(double)(BA.ObjectToNumber(_templist.Get((int) (0))))) { 
 //BA.debugLineNum = 2141;BA.debugLine="minValue = tempMinRange-1.5";
_minvalue = (float) (_tempminrange-1.5);
 }else {
 //BA.debugLineNum = 2143;BA.debugLine="minValue = tempList.Get(0)-1.5";
_minvalue = (float) ((double)(BA.ObjectToNumber(_templist.Get((int) (0))))-1.5);
 };
 };
 //BA.debugLineNum = 2147;BA.debugLine="If tempList.Get(tempList.Size-1) >= 88.88 Then";
if ((double)(BA.ObjectToNumber(_templist.Get((int) (_templist.getSize()-1))))>=88.88) { 
 //BA.debugLineNum = 2148;BA.debugLine="If tempRightNow >= (tempList.Get(tempList.Size-2";
if ((double)(Double.parseDouble(mostCurrent._temprightnow))>=(double)(BA.ObjectToNumber((_templist.Get((int) (_templist.getSize()-2)))))) { 
 //BA.debugLineNum = 2149;BA.debugLine="If tempMaxRange >= tempRightNow Then";
if (_tempmaxrange>=(double)(Double.parseDouble(mostCurrent._temprightnow))) { 
 //BA.debugLineNum = 2150;BA.debugLine="maxValue =  tempMaxRange+1.5";
_maxvalue = (float) (_tempmaxrange+1.5);
 }else {
 //BA.debugLineNum = 2152;BA.debugLine="maxValue = tempRightNow+1.5";
_maxvalue = (float) ((double)(Double.parseDouble(mostCurrent._temprightnow))+1.5);
 };
 }else {
 //BA.debugLineNum = 2155;BA.debugLine="If tempMaxRange >= (tempList.Get(tempList.Size-";
if (_tempmaxrange>=(double)(BA.ObjectToNumber((_templist.Get((int) (_templist.getSize()-2)))))) { 
 //BA.debugLineNum = 2156;BA.debugLine="maxValue =  tempMaxRange+1.5";
_maxvalue = (float) (_tempmaxrange+1.5);
 }else {
 //BA.debugLineNum = 2158;BA.debugLine="maxValue = (tempList.Get(tempList.Size-2))+1.5";
_maxvalue = (float) ((double)(BA.ObjectToNumber((_templist.Get((int) (_templist.getSize()-2)))))+1.5);
 };
 };
 }else {
 //BA.debugLineNum = 2162;BA.debugLine="If tempRightNow >= (tempList.Get(tempList.Size-1";
if ((double)(Double.parseDouble(mostCurrent._temprightnow))>=(double)(BA.ObjectToNumber((_templist.Get((int) (_templist.getSize()-1)))))) { 
 //BA.debugLineNum = 2163;BA.debugLine="If tempMaxRange >= tempRightNow Then";
if (_tempmaxrange>=(double)(Double.parseDouble(mostCurrent._temprightnow))) { 
 //BA.debugLineNum = 2164;BA.debugLine="maxValue =  tempMaxRange+1.5";
_maxvalue = (float) (_tempmaxrange+1.5);
 }else {
 //BA.debugLineNum = 2166;BA.debugLine="maxValue = tempRightNow+1.5";
_maxvalue = (float) ((double)(Double.parseDouble(mostCurrent._temprightnow))+1.5);
 };
 }else {
 //BA.debugLineNum = 2169;BA.debugLine="If tempMaxRange >= (tempList.Get(tempList.Size-";
if (_tempmaxrange>=(double)(BA.ObjectToNumber((_templist.Get((int) (_templist.getSize()-1)))))) { 
 //BA.debugLineNum = 2170;BA.debugLine="maxValue =  tempMaxRange+1.5";
_maxvalue = (float) (_tempmaxrange+1.5);
 }else {
 //BA.debugLineNum = 2172;BA.debugLine="maxValue = (tempList.Get(tempList.Size-1))+1.5";
_maxvalue = (float) ((double)(BA.ObjectToNumber((_templist.Get((int) (_templist.getSize()-1)))))+1.5);
 };
 };
 };
 //BA.debugLineNum = 2177;BA.debugLine="If (maxValue-1.5) >= tempMaxRange Then";
if ((_maxvalue-1.5)>=_tempmaxrange) { 
 //BA.debugLineNum = 2178;BA.debugLine="tempMaxRange = maxValue-1.5";
_tempmaxrange = (float) (_maxvalue-1.5);
 };
 //BA.debugLineNum = 2181;BA.debugLine="tempMinRange = minValue+1.5";
_tempminrange = (float) (_minvalue+1.5);
 //BA.debugLineNum = 2184;BA.debugLine="LineChart.YaxisRange(minValue, maxValue)";
mostCurrent._linechart.YaxisRange(_minvalue,_maxvalue);
 //BA.debugLineNum = 2185;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.keywords.LayoutValues  _getrealsize() throws Exception{
anywheresoftware.b4a.keywords.LayoutValues _lv = null;
anywheresoftware.b4a.phone.Phone _p = null;
anywheresoftware.b4j.object.JavaObject _ctxt = null;
anywheresoftware.b4j.object.JavaObject _display = null;
anywheresoftware.b4j.object.JavaObject _point = null;
 //BA.debugLineNum = 1495;BA.debugLine="Sub GetRealSize As LayoutValues";
 //BA.debugLineNum = 1496;BA.debugLine="Dim lv As LayoutValues";
_lv = new anywheresoftware.b4a.keywords.LayoutValues();
 //BA.debugLineNum = 1497;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 1498;BA.debugLine="If p.SdkVersion >= 17 Then";
if (_p.getSdkVersion()>=17) { 
 //BA.debugLineNum = 1499;BA.debugLine="Dim ctxt As JavaObject";
_ctxt = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 1500;BA.debugLine="ctxt.InitializeContext";
_ctxt.InitializeContext(processBA);
 //BA.debugLineNum = 1501;BA.debugLine="Dim display As JavaObject = ctxt.RunMethodJO(\"ge";
_display = new anywheresoftware.b4j.object.JavaObject();
_display.setObject((java.lang.Object)(_ctxt.RunMethodJO("getSystemService",new Object[]{(Object)("window")}).RunMethod("getDefaultDisplay",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 1502;BA.debugLine="Dim point As JavaObject";
_point = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 1503;BA.debugLine="point.InitializeNewInstance(\"android.graphics.Po";
_point.InitializeNewInstance("android.graphics.Point",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 1504;BA.debugLine="display.RunMethod(\"getRealSize\", Array(point))";
_display.RunMethod("getRealSize",new Object[]{(Object)(_point.getObject())});
 //BA.debugLineNum = 1505;BA.debugLine="lv.Width = point.GetField(\"x\")";
_lv.Width = (int)(BA.ObjectToNumber(_point.GetField("x")));
 //BA.debugLineNum = 1506;BA.debugLine="lv.Height = point.GetField(\"y\")";
_lv.Height = (int)(BA.ObjectToNumber(_point.GetField("y")));
 }else {
 //BA.debugLineNum = 1508;BA.debugLine="lv.Width = 100%x";
_lv.Width = anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA);
 //BA.debugLineNum = 1509;BA.debugLine="lv.Height = 100%y";
_lv.Height = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 1511;BA.debugLine="lv.Scale = 100dip / 100";
_lv.Scale = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100))/(double)100);
 //BA.debugLineNum = 1512;BA.debugLine="Return lv";
if (true) return _lv;
 //BA.debugLineNum = 1513;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 20;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 23;BA.debugLine="Private LineChart As LineChart";
mostCurrent._linechart = new androidplotwrapper.lineChartWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private am12 As String";
mostCurrent._am12 = "";
 //BA.debugLineNum = 25;BA.debugLine="Private am1 As String";
mostCurrent._am1 = "";
 //BA.debugLineNum = 26;BA.debugLine="Private am2 As String";
mostCurrent._am2 = "";
 //BA.debugLineNum = 27;BA.debugLine="Private am3 As String";
mostCurrent._am3 = "";
 //BA.debugLineNum = 28;BA.debugLine="Private am4 As String";
mostCurrent._am4 = "";
 //BA.debugLineNum = 29;BA.debugLine="Private am5 As String";
mostCurrent._am5 = "";
 //BA.debugLineNum = 30;BA.debugLine="Private am6 As String";
mostCurrent._am6 = "";
 //BA.debugLineNum = 31;BA.debugLine="Private am7 As String";
mostCurrent._am7 = "";
 //BA.debugLineNum = 32;BA.debugLine="Private am8 As String";
mostCurrent._am8 = "";
 //BA.debugLineNum = 33;BA.debugLine="Private am9 As String";
mostCurrent._am9 = "";
 //BA.debugLineNum = 34;BA.debugLine="Private am10 As String";
mostCurrent._am10 = "";
 //BA.debugLineNum = 35;BA.debugLine="Private am11 As String";
mostCurrent._am11 = "";
 //BA.debugLineNum = 36;BA.debugLine="Private pm12 As String";
mostCurrent._pm12 = "";
 //BA.debugLineNum = 37;BA.debugLine="Private pm1 As String";
mostCurrent._pm1 = "";
 //BA.debugLineNum = 38;BA.debugLine="Private pm2 As String";
mostCurrent._pm2 = "";
 //BA.debugLineNum = 39;BA.debugLine="Private pm3 As String";
mostCurrent._pm3 = "";
 //BA.debugLineNum = 40;BA.debugLine="Private pm4 As String";
mostCurrent._pm4 = "";
 //BA.debugLineNum = 41;BA.debugLine="Private pm5 As String";
mostCurrent._pm5 = "";
 //BA.debugLineNum = 42;BA.debugLine="Private pm6 As String";
mostCurrent._pm6 = "";
 //BA.debugLineNum = 43;BA.debugLine="Private pm7 As String";
mostCurrent._pm7 = "";
 //BA.debugLineNum = 44;BA.debugLine="Private pm8 As String";
mostCurrent._pm8 = "";
 //BA.debugLineNum = 45;BA.debugLine="Private pm9 As String";
mostCurrent._pm9 = "";
 //BA.debugLineNum = 46;BA.debugLine="Private pm10 As String";
mostCurrent._pm10 = "";
 //BA.debugLineNum = 47;BA.debugLine="Private pm11 As String";
mostCurrent._pm11 = "";
 //BA.debugLineNum = 48;BA.debugLine="Private tempRightNow As String";
mostCurrent._temprightnow = "";
 //BA.debugLineNum = 49;BA.debugLine="Private timeRightNow As Long";
_timerightnow = 0L;
 //BA.debugLineNum = 50;BA.debugLine="Private timeArray(24) As String";
mostCurrent._timearray = new String[(int) (24)];
java.util.Arrays.fill(mostCurrent._timearray,"");
 //BA.debugLineNum = 51;BA.debugLine="Private zeroRange As Float = 88.88";
_zerorange = (float) (88.88);
 //BA.debugLineNum = 52;BA.debugLine="Private tempZeroRange As Float";
_tempzerorange = 0f;
 //BA.debugLineNum = 53;BA.debugLine="Private tempMaxRange As Float";
_tempmaxrange = 0f;
 //BA.debugLineNum = 54;BA.debugLine="Private tempMinRange As Float";
_tempminrange = 0f;
 //BA.debugLineNum = 55;BA.debugLine="Private btnHumidityHourly As Button";
mostCurrent._btnhumidityhourly = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private btnTempHourly As Button";
mostCurrent._btntemphourly = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private btnHumidityDaily As Button";
mostCurrent._btnhumiditydaily = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private btnTempDaily As Button";
mostCurrent._btntempdaily = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 60;BA.debugLine="End Sub";
return "";
}
public static String  _humiditydailycreate() throws Exception{
anywheresoftware.b4a.keywords.LayoutValues _lv = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
long _yesterday = 0L;
 //BA.debugLineNum = 1078;BA.debugLine="Private Sub HumidityDailyCreate()";
 //BA.debugLineNum = 1079;BA.debugLine="Try";
try { //BA.debugLineNum = 1081;BA.debugLine="Activity_WindowFocusChanged(True)";
_activity_windowfocuschanged(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1082;BA.debugLine="Dim lv As LayoutValues = GetRealSize";
_lv = _getrealsize();
 //BA.debugLineNum = 1083;BA.debugLine="Dim jo As JavaObject = Activity";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._activity.getObject()));
 //BA.debugLineNum = 1084;BA.debugLine="jo.RunMethod(\"setBottom\", Array(lv.Height))";
_jo.RunMethod("setBottom",new Object[]{(Object)(_lv.Height)});
 //BA.debugLineNum = 1085;BA.debugLine="jo.RunMethod(\"setRight\", Array(lv.Width))";
_jo.RunMethod("setRight",new Object[]{(Object)(_lv.Width)});
 //BA.debugLineNum = 1086;BA.debugLine="Activity.Height = lv.Height";
mostCurrent._activity.setHeight(_lv.Height);
 //BA.debugLineNum = 1087;BA.debugLine="Activity.Width = lv.Width";
mostCurrent._activity.setWidth(_lv.Width);
 //BA.debugLineNum = 1090;BA.debugLine="Activity.LoadLayout(\"chart\")";
mostCurrent._activity.LoadLayout("chart",mostCurrent.activityBA);
 //BA.debugLineNum = 1092;BA.debugLine="LineChart.GraphBackgroundColor = Colors.DarkGray";
mostCurrent._linechart.setGraphBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 1093;BA.debugLine="LineChart.GraphFrameColor = Colors.Blue";
mostCurrent._linechart.setGraphFrameColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 1094;BA.debugLine="LineChart.GraphFrameWidth = 4.0";
mostCurrent._linechart.setGraphFrameWidth((float) (4.0));
 //BA.debugLineNum = 1095;BA.debugLine="LineChart.GraphPlotAreaBackgroundColor = Colors.";
mostCurrent._linechart.setGraphPlotAreaBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (50),(int) (0),(int) (0),(int) (255)));
 //BA.debugLineNum = 1096;BA.debugLine="LineChart.GraphTitleTextSize = 15";
mostCurrent._linechart.setGraphTitleTextSize((int) (15));
 //BA.debugLineNum = 1097;BA.debugLine="LineChart.GraphTitleColor = Colors.White";
mostCurrent._linechart.setGraphTitleColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1098;BA.debugLine="LineChart.GraphTitleSkewX = -0.25";
mostCurrent._linechart.setGraphTitleSkewX((float) (-0.25));
 //BA.debugLineNum = 1099;BA.debugLine="LineChart.GraphTitleUnderline = True";
mostCurrent._linechart.setGraphTitleUnderline(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1100;BA.debugLine="LineChart.GraphTitleBold = True";
mostCurrent._linechart.setGraphTitleBold(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1101;BA.debugLine="LineChart.GraphTitle = \" HUMIDITY DAILY  \"";
mostCurrent._linechart.setGraphTitle(" HUMIDITY DAILY  ");
 //BA.debugLineNum = 1103;BA.debugLine="LineChart.LegendBackgroundColor = Colors.White";
mostCurrent._linechart.setLegendBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1104;BA.debugLine="LineChart.LegendTextColor = Colors.Black";
mostCurrent._linechart.setLegendTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1105;BA.debugLine="LineChart.LegendTextSize = 18.0";
mostCurrent._linechart.setLegendTextSize((float) (18.0));
 //BA.debugLineNum = 1107;BA.debugLine="DateTime.TimeFormat = \"h:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("h:mm a");
 //BA.debugLineNum = 1108;BA.debugLine="LineChart.DomianLabel = \"The time now is: \" & Da";
mostCurrent._linechart.setDomianLabel("The time now is: "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 1109;BA.debugLine="LineChart.DomainLabelColor = Colors.Green";
mostCurrent._linechart.setDomainLabelColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 1110;BA.debugLine="LineChart.DomainLabelTextSize = 25.0";
mostCurrent._linechart.setDomainLabelTextSize((float) (25.0));
 //BA.debugLineNum = 1112;BA.debugLine="LineChart.XaxisGridLineColor = Colors.ARGB(100,2";
mostCurrent._linechart.setXaxisGridLineColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 1113;BA.debugLine="LineChart.XaxisGridLineWidth = 2.0";
mostCurrent._linechart.setXaxisGridLineWidth((float) (2.0));
 //BA.debugLineNum = 1114;BA.debugLine="LineChart.XaxisLabelTicks = 1";
mostCurrent._linechart.setXaxisLabelTicks((int) (1));
 //BA.debugLineNum = 1115;BA.debugLine="LineChart.XaxisLabelOrientation = 0";
mostCurrent._linechart.setXaxisLabelOrientation((float) (0));
 //BA.debugLineNum = 1116;BA.debugLine="LineChart.XaxisLabelTextColor = Colors.White";
mostCurrent._linechart.setXaxisLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1117;BA.debugLine="LineChart.XaxisLabelTextSize = 32.0";
mostCurrent._linechart.setXaxisLabelTextSize((float) (32.0));
 //BA.debugLineNum = 1118;BA.debugLine="LineChart.XAxisLabels = Array As String(\"12 am\",";
mostCurrent._linechart.setXAxisLabels(new String[]{"12 am","1 am","2 am","3 am","4 am","5 am","6 am","7 am","8 am","9 am","10 am","11 am","12 pm","1 pm","2 pm","3 pm","4 pm","5 pm","6 pm","7 pm","8 pm","9 pm","10 pm","11 pm"});
 //BA.debugLineNum = 1120;BA.debugLine="LineChart.YaxisDivisions = 10";
mostCurrent._linechart.setYaxisDivisions((int) (10));
 //BA.debugLineNum = 1122;BA.debugLine="LineChart.YaxisValueFormat = LineChart.ValueForm";
mostCurrent._linechart.setYaxisValueFormat(mostCurrent._linechart.ValueFormat_2);
 //BA.debugLineNum = 1123;BA.debugLine="LineChart.YaxisGridLineColor = Colors.Black";
mostCurrent._linechart.setYaxisGridLineColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1124;BA.debugLine="LineChart.YaxisGridLineWidth = 2";
mostCurrent._linechart.setYaxisGridLineWidth((float) (2));
 //BA.debugLineNum = 1125;BA.debugLine="LineChart.YaxisLabelTicks = 1";
mostCurrent._linechart.setYaxisLabelTicks((int) (1));
 //BA.debugLineNum = 1126;BA.debugLine="LineChart.YaxisLabelColor = Colors.Yellow";
mostCurrent._linechart.setYaxisLabelColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 1127;BA.debugLine="LineChart.YaxisLabelOrientation = -30";
mostCurrent._linechart.setYaxisLabelOrientation((float) (-30));
 //BA.debugLineNum = 1128;BA.debugLine="LineChart.YaxisLabelTextSize = 25.0";
mostCurrent._linechart.setYaxisLabelTextSize((float) (25.0));
 //BA.debugLineNum = 1129;BA.debugLine="LineChart.YaxisTitleColor = Colors.Green";
mostCurrent._linechart.setYaxisTitleColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 1130;BA.debugLine="LineChart.YaxisTitleFakeBold = False";
mostCurrent._linechart.setYaxisTitleFakeBold(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1131;BA.debugLine="LineChart.YaxisTitleTextSize = 20.0";
mostCurrent._linechart.setYaxisTitleTextSize((float) (20.0));
 //BA.debugLineNum = 1132;BA.debugLine="LineChart.YaxisTitleUnderline = True";
mostCurrent._linechart.setYaxisTitleUnderline(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1133;BA.debugLine="LineChart.YaxisTitleTextSkewness = 0";
mostCurrent._linechart.setYaxisTitleTextSkewness((float) (0));
 //BA.debugLineNum = 1134;BA.debugLine="LineChart.YaxisLabelAndTitleDistance = 60.0";
mostCurrent._linechart.setYaxisLabelAndTitleDistance((float) (60.0));
 //BA.debugLineNum = 1135;BA.debugLine="LineChart.YaxisTitle = \"Humidity (Percentage)\"";
mostCurrent._linechart.setYaxisTitle("Humidity (Percentage)");
 //BA.debugLineNum = 1137;BA.debugLine="LineChart.MaxNumberOfEntriesPerLineChart = 24";
mostCurrent._linechart.setMaxNumberOfEntriesPerLineChart((int) (24));
 //BA.debugLineNum = 1138;BA.debugLine="LineChart.GraphLegendVisibility = False";
mostCurrent._linechart.setGraphLegendVisibility(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1142;BA.debugLine="ReadHumidityDaily(\"Today\")";
_readhumiditydaily("Today");
 //BA.debugLineNum = 1144;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy");
 //BA.debugLineNum = 1145;BA.debugLine="LineChart.Line_1_LegendText = \"Today, \" & DateTi";
mostCurrent._linechart.setLine_1_LegendText("Today, "+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 1147;BA.debugLine="CheckTempBoundariesDaily";
_checktempboundariesdaily();
 //BA.debugLineNum = 1149;BA.debugLine="If am12 <> tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1150;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12)";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12))});
 };
 //BA.debugLineNum = 1152;BA.debugLine="If am1 <> tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1153;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1))});
 };
 //BA.debugLineNum = 1155;BA.debugLine="If am2 <> tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1156;BA.debugLine="If am1 = tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1157;BA.debugLine="am1 = (am12 + am2)/2";
mostCurrent._am1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am12))+(double)(Double.parseDouble(mostCurrent._am2)))/(double)2);
 };
 //BA.debugLineNum = 1159;BA.debugLine="If am12 = tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1160;BA.debugLine="am12 = am1";
mostCurrent._am12 = mostCurrent._am1;
 };
 //BA.debugLineNum = 1162;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2))});
 };
 //BA.debugLineNum = 1164;BA.debugLine="If am3 <> tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1165;BA.debugLine="If am2 = tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1166;BA.debugLine="am2 = (am1 + am3)/2";
mostCurrent._am2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am1))+(double)(Double.parseDouble(mostCurrent._am3)))/(double)2);
 };
 //BA.debugLineNum = 1168;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3))});
 };
 //BA.debugLineNum = 1170;BA.debugLine="If am4 <> tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1171;BA.debugLine="If am3 = tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1172;BA.debugLine="am3 = (am2 + am4)/2";
mostCurrent._am3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am2))+(double)(Double.parseDouble(mostCurrent._am4)))/(double)2);
 };
 //BA.debugLineNum = 1174;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4))});
 };
 //BA.debugLineNum = 1176;BA.debugLine="If am5 <> tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1177;BA.debugLine="If am4 = tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1178;BA.debugLine="am4 = (am3 + am5)/2";
mostCurrent._am4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am3))+(double)(Double.parseDouble(mostCurrent._am5)))/(double)2);
 };
 //BA.debugLineNum = 1180;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5))});
 };
 //BA.debugLineNum = 1182;BA.debugLine="If am6 <> tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1183;BA.debugLine="If am5 = tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1184;BA.debugLine="am5 = (am4 + am6)/2";
mostCurrent._am5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am4))+(double)(Double.parseDouble(mostCurrent._am6)))/(double)2);
 };
 //BA.debugLineNum = 1186;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6))});
 };
 //BA.debugLineNum = 1188;BA.debugLine="If am7 <> tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1189;BA.debugLine="If am6 = tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1190;BA.debugLine="am6 = (am5 + am7)/2";
mostCurrent._am6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am5))+(double)(Double.parseDouble(mostCurrent._am7)))/(double)2);
 };
 //BA.debugLineNum = 1192;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7))});
 };
 //BA.debugLineNum = 1194;BA.debugLine="If am8 <> tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1195;BA.debugLine="If am7 = tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1196;BA.debugLine="am7 = (am6 + am8)/2";
mostCurrent._am7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am6))+(double)(Double.parseDouble(mostCurrent._am8)))/(double)2);
 };
 //BA.debugLineNum = 1198;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8))});
 };
 //BA.debugLineNum = 1200;BA.debugLine="If am9 <> tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1201;BA.debugLine="If am8 = tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1202;BA.debugLine="am8 = (am7 + am9)/2";
mostCurrent._am8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am7))+(double)(Double.parseDouble(mostCurrent._am9)))/(double)2);
 };
 //BA.debugLineNum = 1204;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9))});
 };
 //BA.debugLineNum = 1206;BA.debugLine="If am10 <> tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1207;BA.debugLine="If am9 = tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1208;BA.debugLine="am9 = (am8 + am10)/2";
mostCurrent._am9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am8))+(double)(Double.parseDouble(mostCurrent._am10)))/(double)2);
 };
 //BA.debugLineNum = 1210;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10))});
 };
 //BA.debugLineNum = 1212;BA.debugLine="If am11 <> tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1213;BA.debugLine="If am10 = tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1214;BA.debugLine="am10 = (am9 + am11)/2";
mostCurrent._am10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am9))+(double)(Double.parseDouble(mostCurrent._am11)))/(double)2);
 };
 //BA.debugLineNum = 1216;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11))});
 };
 //BA.debugLineNum = 1218;BA.debugLine="If pm12 <> tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1219;BA.debugLine="If am11 = tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1220;BA.debugLine="am11 = (am10 + pm12)/2";
mostCurrent._am11 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am10))+(double)(Double.parseDouble(mostCurrent._pm12)))/(double)2);
 };
 //BA.debugLineNum = 1222;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12))});
 };
 //BA.debugLineNum = 1224;BA.debugLine="If pm1 <> tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1225;BA.debugLine="If pm12 = tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1226;BA.debugLine="pm12 = (am11 + pm1)/2";
mostCurrent._pm12 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am11))+(double)(Double.parseDouble(mostCurrent._pm1)))/(double)2);
 };
 //BA.debugLineNum = 1228;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1))});
 };
 //BA.debugLineNum = 1230;BA.debugLine="If pm2 <> tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1231;BA.debugLine="If pm1 = tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1232;BA.debugLine="pm1 = (pm12 + pm2)/2";
mostCurrent._pm1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm12))+(double)(Double.parseDouble(mostCurrent._pm2)))/(double)2);
 };
 //BA.debugLineNum = 1234;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2))});
 };
 //BA.debugLineNum = 1236;BA.debugLine="If pm3 <> tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1237;BA.debugLine="If pm2 = tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1238;BA.debugLine="pm2 = (pm1 + pm3)/2";
mostCurrent._pm2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm1))+(double)(Double.parseDouble(mostCurrent._pm3)))/(double)2);
 };
 //BA.debugLineNum = 1240;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3))});
 };
 //BA.debugLineNum = 1242;BA.debugLine="If pm4 <> tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1243;BA.debugLine="If pm3 = tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1244;BA.debugLine="pm3 = (pm2 + pm4)/2";
mostCurrent._pm3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm2))+(double)(Double.parseDouble(mostCurrent._pm4)))/(double)2);
 };
 //BA.debugLineNum = 1246;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4))});
 };
 //BA.debugLineNum = 1248;BA.debugLine="If pm5 <> tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1249;BA.debugLine="If pm4 = tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1250;BA.debugLine="pm4 = (pm3 + pm5)/2";
mostCurrent._pm4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm3))+(double)(Double.parseDouble(mostCurrent._pm5)))/(double)2);
 };
 //BA.debugLineNum = 1252;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5))});
 };
 //BA.debugLineNum = 1254;BA.debugLine="If pm6 <> tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1255;BA.debugLine="If pm5 = tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1256;BA.debugLine="pm5 = (pm4 + pm6)/2";
mostCurrent._pm5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm4))+(double)(Double.parseDouble(mostCurrent._pm6)))/(double)2);
 };
 //BA.debugLineNum = 1258;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6))});
 };
 //BA.debugLineNum = 1260;BA.debugLine="If pm7 <> tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1261;BA.debugLine="If pm6 = tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1262;BA.debugLine="pm6 = (pm5 + pm7)/2";
mostCurrent._pm6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm5))+(double)(Double.parseDouble(mostCurrent._pm7)))/(double)2);
 };
 //BA.debugLineNum = 1264;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7))});
 };
 //BA.debugLineNum = 1266;BA.debugLine="If pm8 <> tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1267;BA.debugLine="If pm7 = tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1268;BA.debugLine="pm7 = (pm6 + pm8)/2";
mostCurrent._pm7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm6))+(double)(Double.parseDouble(mostCurrent._pm8)))/(double)2);
 };
 //BA.debugLineNum = 1270;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8))});
 };
 //BA.debugLineNum = 1272;BA.debugLine="If pm9 <> tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1273;BA.debugLine="If pm8 = tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1274;BA.debugLine="pm8 = (pm7 + pm9)/2";
mostCurrent._pm8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm7))+(double)(Double.parseDouble(mostCurrent._pm9)))/(double)2);
 };
 //BA.debugLineNum = 1276;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9))});
 };
 //BA.debugLineNum = 1278;BA.debugLine="If pm10 <> tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1279;BA.debugLine="If pm9 = tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1280;BA.debugLine="pm9 = (pm8 + pm10)/2";
mostCurrent._pm9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm8))+(double)(Double.parseDouble(mostCurrent._pm10)))/(double)2);
 };
 //BA.debugLineNum = 1282;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10))});
 };
 //BA.debugLineNum = 1284;BA.debugLine="If pm11 <> tempZeroRange Then";
if ((mostCurrent._pm11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1285;BA.debugLine="If pm10 = tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1286;BA.debugLine="pm10 = (pm9 + pm11)/2";
mostCurrent._pm10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm9))+(double)(Double.parseDouble(mostCurrent._pm11)))/(double)2);
 };
 //BA.debugLineNum = 1288;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))});
 };
 //BA.debugLineNum = 1291;BA.debugLine="LineChart.Line_1_PointLabelTextColor = Colors.Ye";
mostCurrent._linechart.setLine_1_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 1292;BA.debugLine="LineChart.Line_1_PointLabelTextSize = 35.0";
mostCurrent._linechart.setLine_1_PointLabelTextSize((float) (35.0));
 //BA.debugLineNum = 1293;BA.debugLine="LineChart.Line_1_LineColor = Colors.Red";
mostCurrent._linechart.setLine_1_LineColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 1294;BA.debugLine="LineChart.Line_1_LineWidth = 11.0";
mostCurrent._linechart.setLine_1_LineWidth((float) (11.0));
 //BA.debugLineNum = 1295;BA.debugLine="LineChart.Line_1_PointColor = Colors.Black";
mostCurrent._linechart.setLine_1_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1296;BA.debugLine="LineChart.Line_1_PointSize = 25.0";
mostCurrent._linechart.setLine_1_PointSize((float) (25.0));
 //BA.debugLineNum = 1297;BA.debugLine="LineChart.Line_1_PointShape = LineChart.SHAPE_RO";
mostCurrent._linechart.setLine_1_PointShape(mostCurrent._linechart.SHAPE_ROUND);
 //BA.debugLineNum = 1298;BA.debugLine="LineChart.Line_1_DrawDash = False";
mostCurrent._linechart.setLine_1_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1299;BA.debugLine="LineChart.Line_1_DrawCubic = False";
mostCurrent._linechart.setLine_1_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1305;BA.debugLine="ReadHumidityDaily(\"Yesterday\")";
_readhumiditydaily("Yesterday");
 //BA.debugLineNum = 1307;BA.debugLine="Dim Yesterday As Long";
_yesterday = 0L;
 //BA.debugLineNum = 1308;BA.debugLine="Yesterday = DateTime.add(DateTime.Now, 0, 0, -1)";
_yesterday = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1));
 //BA.debugLineNum = 1310;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy");
 //BA.debugLineNum = 1311;BA.debugLine="LineChart.Line_2_LegendText = \"Yesterday, \" & Da";
mostCurrent._linechart.setLine_2_LegendText("Yesterday, "+anywheresoftware.b4a.keywords.Common.DateTime.Date(_yesterday));
 //BA.debugLineNum = 1313;BA.debugLine="CheckTempBoundariesDaily";
_checktempboundariesdaily();
 //BA.debugLineNum = 1315;BA.debugLine="If am12 <> tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1316;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12)";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12))});
 };
 //BA.debugLineNum = 1318;BA.debugLine="If am1 <> tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1319;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1))});
 };
 //BA.debugLineNum = 1321;BA.debugLine="If am2 <> tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1322;BA.debugLine="If am1 = tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1323;BA.debugLine="am1 = (am12 + am2)/2";
mostCurrent._am1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am12))+(double)(Double.parseDouble(mostCurrent._am2)))/(double)2);
 };
 //BA.debugLineNum = 1325;BA.debugLine="If am12 = tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1326;BA.debugLine="am12 = am1";
mostCurrent._am12 = mostCurrent._am1;
 };
 //BA.debugLineNum = 1328;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2))});
 };
 //BA.debugLineNum = 1330;BA.debugLine="If am3 <> tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1331;BA.debugLine="If am2 = tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1332;BA.debugLine="am2 = (am1 + am3)/2";
mostCurrent._am2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am1))+(double)(Double.parseDouble(mostCurrent._am3)))/(double)2);
 };
 //BA.debugLineNum = 1334;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3))});
 };
 //BA.debugLineNum = 1336;BA.debugLine="If am4 <> tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1337;BA.debugLine="If am3 = tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1338;BA.debugLine="am3 = (am2 + am4)/2";
mostCurrent._am3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am2))+(double)(Double.parseDouble(mostCurrent._am4)))/(double)2);
 };
 //BA.debugLineNum = 1340;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4))});
 };
 //BA.debugLineNum = 1342;BA.debugLine="If am5 <> tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1343;BA.debugLine="If am4 = tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1344;BA.debugLine="am4 = (am3 + am5)/2";
mostCurrent._am4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am3))+(double)(Double.parseDouble(mostCurrent._am5)))/(double)2);
 };
 //BA.debugLineNum = 1346;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5))});
 };
 //BA.debugLineNum = 1348;BA.debugLine="If am6 <> tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1349;BA.debugLine="If am5 = tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1350;BA.debugLine="am5 = (am4 + am6)/2";
mostCurrent._am5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am4))+(double)(Double.parseDouble(mostCurrent._am6)))/(double)2);
 };
 //BA.debugLineNum = 1352;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6))});
 };
 //BA.debugLineNum = 1354;BA.debugLine="If am7 <> tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1355;BA.debugLine="If am6 = tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1356;BA.debugLine="am6 = (am5 + am7)/2";
mostCurrent._am6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am5))+(double)(Double.parseDouble(mostCurrent._am7)))/(double)2);
 };
 //BA.debugLineNum = 1358;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7))});
 };
 //BA.debugLineNum = 1360;BA.debugLine="If am8 <> tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1361;BA.debugLine="If am7 = tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1362;BA.debugLine="am7 = (am6 + am8)/2";
mostCurrent._am7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am6))+(double)(Double.parseDouble(mostCurrent._am8)))/(double)2);
 };
 //BA.debugLineNum = 1364;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8))});
 };
 //BA.debugLineNum = 1366;BA.debugLine="If am9 <> tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1367;BA.debugLine="If am8 = tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1368;BA.debugLine="am8 = (am7 + am9)/2";
mostCurrent._am8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am7))+(double)(Double.parseDouble(mostCurrent._am9)))/(double)2);
 };
 //BA.debugLineNum = 1370;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9))});
 };
 //BA.debugLineNum = 1372;BA.debugLine="If am10 <> tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1373;BA.debugLine="If am9 = tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1374;BA.debugLine="am9 = (am8 + am10)/2";
mostCurrent._am9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am8))+(double)(Double.parseDouble(mostCurrent._am10)))/(double)2);
 };
 //BA.debugLineNum = 1376;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10))});
 };
 //BA.debugLineNum = 1378;BA.debugLine="If am11 <> tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1379;BA.debugLine="If am10 = tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1380;BA.debugLine="am10 = (am9 + am11)/2";
mostCurrent._am10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am9))+(double)(Double.parseDouble(mostCurrent._am11)))/(double)2);
 };
 //BA.debugLineNum = 1382;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11))});
 };
 //BA.debugLineNum = 1384;BA.debugLine="If pm12 <> tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1385;BA.debugLine="If am11 = tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1386;BA.debugLine="am11 = (am10 + pm12)/2";
mostCurrent._am11 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am10))+(double)(Double.parseDouble(mostCurrent._pm12)))/(double)2);
 };
 //BA.debugLineNum = 1388;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12))});
 };
 //BA.debugLineNum = 1390;BA.debugLine="If pm1 <> tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1391;BA.debugLine="If pm12 = tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1392;BA.debugLine="pm12 = (am11 + pm1)/2";
mostCurrent._pm12 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am11))+(double)(Double.parseDouble(mostCurrent._pm1)))/(double)2);
 };
 //BA.debugLineNum = 1394;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1))});
 };
 //BA.debugLineNum = 1396;BA.debugLine="If pm2 <> tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1397;BA.debugLine="If pm1 = tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1398;BA.debugLine="pm1 = (pm12 + pm2)/2";
mostCurrent._pm1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm12))+(double)(Double.parseDouble(mostCurrent._pm2)))/(double)2);
 };
 //BA.debugLineNum = 1400;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2))});
 };
 //BA.debugLineNum = 1402;BA.debugLine="If pm3 <> tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1403;BA.debugLine="If pm2 = tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1404;BA.debugLine="pm2 = (pm1 + pm3)/2";
mostCurrent._pm2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm1))+(double)(Double.parseDouble(mostCurrent._pm3)))/(double)2);
 };
 //BA.debugLineNum = 1406;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3))});
 };
 //BA.debugLineNum = 1408;BA.debugLine="If pm4 <> tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1409;BA.debugLine="If pm3 = tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1410;BA.debugLine="pm3 = (pm2 + pm4)/2";
mostCurrent._pm3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm2))+(double)(Double.parseDouble(mostCurrent._pm4)))/(double)2);
 };
 //BA.debugLineNum = 1412;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4))});
 };
 //BA.debugLineNum = 1414;BA.debugLine="If pm5 <> tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1415;BA.debugLine="If pm4 = tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1416;BA.debugLine="pm4 = (pm3 + pm5)/2";
mostCurrent._pm4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm3))+(double)(Double.parseDouble(mostCurrent._pm5)))/(double)2);
 };
 //BA.debugLineNum = 1418;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5))});
 };
 //BA.debugLineNum = 1420;BA.debugLine="If pm6 <> tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1421;BA.debugLine="If pm5 = tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1422;BA.debugLine="pm5 = (pm4 + pm6)/2";
mostCurrent._pm5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm4))+(double)(Double.parseDouble(mostCurrent._pm6)))/(double)2);
 };
 //BA.debugLineNum = 1424;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6))});
 };
 //BA.debugLineNum = 1426;BA.debugLine="If pm7 <> tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1427;BA.debugLine="If pm6 = tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1428;BA.debugLine="pm6 = (pm5 + pm7)/2";
mostCurrent._pm6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm5))+(double)(Double.parseDouble(mostCurrent._pm7)))/(double)2);
 };
 //BA.debugLineNum = 1430;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7))});
 };
 //BA.debugLineNum = 1432;BA.debugLine="If pm8 <> tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1433;BA.debugLine="If pm7 = tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1434;BA.debugLine="pm7 = (pm6 + pm8)/2";
mostCurrent._pm7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm6))+(double)(Double.parseDouble(mostCurrent._pm8)))/(double)2);
 };
 //BA.debugLineNum = 1436;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8))});
 };
 //BA.debugLineNum = 1438;BA.debugLine="If pm9 <> tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1439;BA.debugLine="If pm8 = tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1440;BA.debugLine="pm8 = (pm7 + pm9)/2";
mostCurrent._pm8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm7))+(double)(Double.parseDouble(mostCurrent._pm9)))/(double)2);
 };
 //BA.debugLineNum = 1442;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9))});
 };
 //BA.debugLineNum = 1444;BA.debugLine="If pm10 <> tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1445;BA.debugLine="If pm9 = tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1446;BA.debugLine="pm9 = (pm8 + pm10)/2";
mostCurrent._pm9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm8))+(double)(Double.parseDouble(mostCurrent._pm10)))/(double)2);
 };
 //BA.debugLineNum = 1448;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10))});
 };
 //BA.debugLineNum = 1450;BA.debugLine="If pm11 <> tempZeroRange Then";
if ((mostCurrent._pm11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1451;BA.debugLine="If pm10 = tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1452;BA.debugLine="pm10 = (pm9 + pm11)/2";
mostCurrent._pm10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm9))+(double)(Double.parseDouble(mostCurrent._pm11)))/(double)2);
 };
 //BA.debugLineNum = 1454;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))});
 };
 //BA.debugLineNum = 1457;BA.debugLine="LineChart.Line_2_PointLabelTextColor = Colors.Cy";
mostCurrent._linechart.setLine_2_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 1458;BA.debugLine="LineChart.Line_2_PointLabelTextSize = 35.0";
mostCurrent._linechart.setLine_2_PointLabelTextSize((float) (35.0));
 //BA.debugLineNum = 1459;BA.debugLine="LineChart.Line_2_LineColor = Colors.White";
mostCurrent._linechart.setLine_2_LineColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1460;BA.debugLine="LineChart.Line_2_LineWidth = 7.0";
mostCurrent._linechart.setLine_2_LineWidth((float) (7.0));
 //BA.debugLineNum = 1461;BA.debugLine="LineChart.Line_2_PointColor = Colors.Cyan";
mostCurrent._linechart.setLine_2_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 1462;BA.debugLine="LineChart.Line_2_PointSize = 10.0";
mostCurrent._linechart.setLine_2_PointSize((float) (10.0));
 //BA.debugLineNum = 1463;BA.debugLine="LineChart.Line_2_PointShape = LineChart.SHAPE_RO";
mostCurrent._linechart.setLine_2_PointShape(mostCurrent._linechart.SHAPE_ROUND);
 //BA.debugLineNum = 1464;BA.debugLine="LineChart.Line_2_DrawDash = False";
mostCurrent._linechart.setLine_2_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1465;BA.debugLine="LineChart.Line_2_DrawCubic = False";
mostCurrent._linechart.setLine_2_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1471;BA.debugLine="LineChart.Line_3_LegendText = \"Real time\"";
mostCurrent._linechart.setLine_3_LegendText("Real time");
 //BA.debugLineNum = 1472;BA.debugLine="LineChart.Line_3_Data = Array As Float (tempRigh";
mostCurrent._linechart.setLine_3_Data(new float[]{(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow))});
 //BA.debugLineNum = 1473;BA.debugLine="LineChart.Line_3_PointLabelTextColor = Colors.Gr";
mostCurrent._linechart.setLine_3_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 1474;BA.debugLine="LineChart.Line_3_PointLabelTextSize = 30.0";
mostCurrent._linechart.setLine_3_PointLabelTextSize((float) (30.0));
 //BA.debugLineNum = 1475;BA.debugLine="LineChart.Line_3_LineColor = Colors.Green";
mostCurrent._linechart.setLine_3_LineColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 1476;BA.debugLine="LineChart.Line_3_LineWidth = 5.0";
mostCurrent._linechart.setLine_3_LineWidth((float) (5.0));
 //BA.debugLineNum = 1477;BA.debugLine="LineChart.Line_3_PointColor = Colors.Green";
mostCurrent._linechart.setLine_3_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 1478;BA.debugLine="LineChart.Line_3_PointSize = 1.0";
mostCurrent._linechart.setLine_3_PointSize((float) (1.0));
 //BA.debugLineNum = 1479;BA.debugLine="LineChart.Line_3_PointShape = LineChart.SHAPE_RO";
mostCurrent._linechart.setLine_3_PointShape(mostCurrent._linechart.SHAPE_ROUND);
 //BA.debugLineNum = 1480;BA.debugLine="LineChart.Line_3_DrawDash = False";
mostCurrent._linechart.setLine_3_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1481;BA.debugLine="LineChart.Line_3_DrawCubic = False";
mostCurrent._linechart.setLine_3_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1485;BA.debugLine="LineChart.NumberOfLineCharts = 3";
mostCurrent._linechart.setNumberOfLineCharts((int) (3));
 //BA.debugLineNum = 1487;BA.debugLine="LineChart.DrawTheGraphs";
mostCurrent._linechart.DrawTheGraphs();
 } 
       catch (Exception e375) {
			processBA.setLastException(e375); //BA.debugLineNum = 1490;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("63211676",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1491;BA.debugLine="ToastMessageShow (LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 1493;BA.debugLine="End Sub";
return "";
}
public static String  _humiditydailytimer_tick() throws Exception{
 //BA.debugLineNum = 2223;BA.debugLine="Sub HumidityDailyTimer_Tick";
 //BA.debugLineNum = 2224;BA.debugLine="Activity.RequestFocus";
mostCurrent._activity.RequestFocus();
 //BA.debugLineNum = 2225;BA.debugLine="btnHumidityHourly.RemoveView";
mostCurrent._btnhumidityhourly.RemoveView();
 //BA.debugLineNum = 2226;BA.debugLine="btnTempHourly.RemoveView";
mostCurrent._btntemphourly.RemoveView();
 //BA.debugLineNum = 2227;BA.debugLine="btnHumidityDaily.RemoveView";
mostCurrent._btnhumiditydaily.RemoveView();
 //BA.debugLineNum = 2228;BA.debugLine="btnTempDaily.RemoveView";
mostCurrent._btntempdaily.RemoveView();
 //BA.debugLineNum = 2229;BA.debugLine="LineChart.RemoveView";
mostCurrent._linechart.RemoveView();
 //BA.debugLineNum = 2230;BA.debugLine="tempMaxRange=0";
_tempmaxrange = (float) (0);
 //BA.debugLineNum = 2231;BA.debugLine="tempMinRange=0";
_tempminrange = (float) (0);
 //BA.debugLineNum = 2232;BA.debugLine="HumidityDailyCreate";
_humiditydailycreate();
 //BA.debugLineNum = 2233;BA.debugLine="End Sub";
return "";
}
public static String  _humidityhourlycreate() throws Exception{
anywheresoftware.b4a.keywords.LayoutValues _lv = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
int _i = 0;
b4a.example.dateutils._period _p = null;
long _nexttime = 0L;
 //BA.debugLineNum = 393;BA.debugLine="Private Sub HumidityHourlyCreate()";
 //BA.debugLineNum = 394;BA.debugLine="Try";
try { //BA.debugLineNum = 396;BA.debugLine="Activity_WindowFocusChanged(True)";
_activity_windowfocuschanged(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 397;BA.debugLine="Dim lv As LayoutValues = GetRealSize";
_lv = _getrealsize();
 //BA.debugLineNum = 398;BA.debugLine="Dim jo As JavaObject = Activity";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._activity.getObject()));
 //BA.debugLineNum = 399;BA.debugLine="jo.RunMethod(\"setBottom\", Array(lv.Height))";
_jo.RunMethod("setBottom",new Object[]{(Object)(_lv.Height)});
 //BA.debugLineNum = 400;BA.debugLine="jo.RunMethod(\"setRight\", Array(lv.Width))";
_jo.RunMethod("setRight",new Object[]{(Object)(_lv.Width)});
 //BA.debugLineNum = 401;BA.debugLine="Activity.Height = lv.Height";
mostCurrent._activity.setHeight(_lv.Height);
 //BA.debugLineNum = 402;BA.debugLine="Activity.Width = lv.Width";
mostCurrent._activity.setWidth(_lv.Width);
 //BA.debugLineNum = 405;BA.debugLine="Activity.LoadLayout(\"chart\")";
mostCurrent._activity.LoadLayout("chart",mostCurrent.activityBA);
 //BA.debugLineNum = 407;BA.debugLine="LineChart.GraphBackgroundColor = Colors.DarkGray";
mostCurrent._linechart.setGraphBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 408;BA.debugLine="LineChart.GraphFrameColor = Colors.Blue";
mostCurrent._linechart.setGraphFrameColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 409;BA.debugLine="LineChart.GraphFrameWidth = 4.0";
mostCurrent._linechart.setGraphFrameWidth((float) (4.0));
 //BA.debugLineNum = 410;BA.debugLine="LineChart.GraphPlotAreaBackgroundColor = Colors.";
mostCurrent._linechart.setGraphPlotAreaBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (50),(int) (0),(int) (0),(int) (255)));
 //BA.debugLineNum = 411;BA.debugLine="LineChart.GraphTitleTextSize = 15";
mostCurrent._linechart.setGraphTitleTextSize((int) (15));
 //BA.debugLineNum = 412;BA.debugLine="LineChart.GraphTitleColor = Colors.White";
mostCurrent._linechart.setGraphTitleColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 413;BA.debugLine="LineChart.GraphTitleSkewX = -0.25";
mostCurrent._linechart.setGraphTitleSkewX((float) (-0.25));
 //BA.debugLineNum = 414;BA.debugLine="LineChart.GraphTitleUnderline = True";
mostCurrent._linechart.setGraphTitleUnderline(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 415;BA.debugLine="LineChart.GraphTitleBold = True";
mostCurrent._linechart.setGraphTitleBold(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 416;BA.debugLine="LineChart.GraphTitle = \" HUMIDITY HOURLY  \"";
mostCurrent._linechart.setGraphTitle(" HUMIDITY HOURLY  ");
 //BA.debugLineNum = 418;BA.debugLine="LineChart.LegendBackgroundColor = Colors.White";
mostCurrent._linechart.setLegendBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 419;BA.debugLine="LineChart.LegendTextColor = Colors.Black";
mostCurrent._linechart.setLegendTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 420;BA.debugLine="LineChart.LegendTextSize = 18.0";
mostCurrent._linechart.setLegendTextSize((float) (18.0));
 //BA.debugLineNum = 422;BA.debugLine="DateTime.TimeFormat = \"h:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("h:mm a");
 //BA.debugLineNum = 423;BA.debugLine="LineChart.DomianLabel = \"The time now is: \" & Da";
mostCurrent._linechart.setDomianLabel("The time now is: "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 424;BA.debugLine="LineChart.DomainLabelColor = Colors.Green";
mostCurrent._linechart.setDomainLabelColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 425;BA.debugLine="LineChart.DomainLabelTextSize = 25.0";
mostCurrent._linechart.setDomainLabelTextSize((float) (25.0));
 //BA.debugLineNum = 427;BA.debugLine="LineChart.XaxisGridLineColor = Colors.ARGB(100,2";
mostCurrent._linechart.setXaxisGridLineColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 428;BA.debugLine="LineChart.XaxisGridLineWidth = 2.0";
mostCurrent._linechart.setXaxisGridLineWidth((float) (2.0));
 //BA.debugLineNum = 429;BA.debugLine="LineChart.XaxisLabelTicks = 1";
mostCurrent._linechart.setXaxisLabelTicks((int) (1));
 //BA.debugLineNum = 430;BA.debugLine="LineChart.XaxisLabelOrientation = 0";
mostCurrent._linechart.setXaxisLabelOrientation((float) (0));
 //BA.debugLineNum = 431;BA.debugLine="LineChart.XaxisLabelTextColor = Colors.White";
mostCurrent._linechart.setXaxisLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 432;BA.debugLine="LineChart.XaxisLabelTextSize = 32.0";
mostCurrent._linechart.setXaxisLabelTextSize((float) (32.0));
 //BA.debugLineNum = 436;BA.debugLine="timeRightNow = DateTime.Now";
_timerightnow = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 438;BA.debugLine="For i = 23 To 0 Step -1";
{
final int step34 = -1;
final int limit34 = (int) (0);
_i = (int) (23) ;
for (;_i >= limit34 ;_i = _i + step34 ) {
 //BA.debugLineNum = 439;BA.debugLine="Dim p As Period";
_p = new b4a.example.dateutils._period();
 //BA.debugLineNum = 440;BA.debugLine="p.Hours = 0";
_p.Hours = (int) (0);
 //BA.debugLineNum = 441;BA.debugLine="p.Minutes = (i+1) * -5";
_p.Minutes = (int) ((_i+1)*-5);
 //BA.debugLineNum = 442;BA.debugLine="p.Seconds = 0";
_p.Seconds = (int) (0);
 //BA.debugLineNum = 443;BA.debugLine="Dim NextTime As Long";
_nexttime = 0L;
 //BA.debugLineNum = 444;BA.debugLine="NextTime = DateUtils.AddPeriod(timeRightNow, p)";
_nexttime = mostCurrent._dateutils._addperiod(mostCurrent.activityBA,_timerightnow,_p);
 //BA.debugLineNum = 445;BA.debugLine="DateTime.TimeFormat = \"HH:mm\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("HH:mm");
 //BA.debugLineNum = 447;BA.debugLine="timeArray(23-i) = DateTime.Time(NextTime) 'Date";
mostCurrent._timearray[(int) (23-_i)] = anywheresoftware.b4a.keywords.Common.DateTime.Time(_nexttime);
 }
};
 //BA.debugLineNum = 449;BA.debugLine="LineChart.XAxisLabels = timeArray 'Array As Stri";
mostCurrent._linechart.setXAxisLabels(mostCurrent._timearray);
 //BA.debugLineNum = 452;BA.debugLine="LineChart.YaxisDivisions = 10";
mostCurrent._linechart.setYaxisDivisions((int) (10));
 //BA.debugLineNum = 454;BA.debugLine="LineChart.YaxisValueFormat = LineChart.ValueForm";
mostCurrent._linechart.setYaxisValueFormat(mostCurrent._linechart.ValueFormat_2);
 //BA.debugLineNum = 455;BA.debugLine="LineChart.YaxisGridLineColor = Colors.Black";
mostCurrent._linechart.setYaxisGridLineColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 456;BA.debugLine="LineChart.YaxisGridLineWidth = 2";
mostCurrent._linechart.setYaxisGridLineWidth((float) (2));
 //BA.debugLineNum = 457;BA.debugLine="LineChart.YaxisLabelTicks = 1";
mostCurrent._linechart.setYaxisLabelTicks((int) (1));
 //BA.debugLineNum = 458;BA.debugLine="LineChart.YaxisLabelColor = Colors.Yellow";
mostCurrent._linechart.setYaxisLabelColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 459;BA.debugLine="LineChart.YaxisLabelOrientation = -30";
mostCurrent._linechart.setYaxisLabelOrientation((float) (-30));
 //BA.debugLineNum = 460;BA.debugLine="LineChart.YaxisLabelTextSize = 25.0";
mostCurrent._linechart.setYaxisLabelTextSize((float) (25.0));
 //BA.debugLineNum = 461;BA.debugLine="LineChart.YaxisTitleColor = Colors.Green";
mostCurrent._linechart.setYaxisTitleColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 462;BA.debugLine="LineChart.YaxisTitleFakeBold = False";
mostCurrent._linechart.setYaxisTitleFakeBold(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 463;BA.debugLine="LineChart.YaxisTitleTextSize = 20.0";
mostCurrent._linechart.setYaxisTitleTextSize((float) (20.0));
 //BA.debugLineNum = 464;BA.debugLine="LineChart.YaxisTitleUnderline = True";
mostCurrent._linechart.setYaxisTitleUnderline(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 465;BA.debugLine="LineChart.YaxisTitleTextSkewness = 0";
mostCurrent._linechart.setYaxisTitleTextSkewness((float) (0));
 //BA.debugLineNum = 466;BA.debugLine="LineChart.YaxisLabelAndTitleDistance = 60.0";
mostCurrent._linechart.setYaxisLabelAndTitleDistance((float) (60.0));
 //BA.debugLineNum = 467;BA.debugLine="LineChart.YaxisTitle = \"Humidity (Percentage)\"";
mostCurrent._linechart.setYaxisTitle("Humidity (Percentage)");
 //BA.debugLineNum = 469;BA.debugLine="LineChart.MaxNumberOfEntriesPerLineChart = 24";
mostCurrent._linechart.setMaxNumberOfEntriesPerLineChart((int) (24));
 //BA.debugLineNum = 470;BA.debugLine="LineChart.GraphLegendVisibility = False";
mostCurrent._linechart.setGraphLegendVisibility(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 474;BA.debugLine="ReadHumidityHourly(\"Today\")";
_readhumidityhourly("Today");
 //BA.debugLineNum = 476;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy");
 //BA.debugLineNum = 477;BA.debugLine="LineChart.Line_1_LegendText = \"From \" & timeArra";
mostCurrent._linechart.setLine_1_LegendText("From "+mostCurrent._timearray[(int) (0)]+" to "+mostCurrent._timearray[(int) (23)]);
 //BA.debugLineNum = 479;BA.debugLine="CheckTempBoundaries";
_checktempboundaries();
 //BA.debugLineNum = 481;BA.debugLine="If am12 <> tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 482;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12)";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12))});
 };
 //BA.debugLineNum = 484;BA.debugLine="If am1 <> tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 485;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1))});
 };
 //BA.debugLineNum = 487;BA.debugLine="If am2 <> tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 488;BA.debugLine="If am1 = tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 489;BA.debugLine="am1 = (am12 + am2)/2";
mostCurrent._am1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am12))+(double)(Double.parseDouble(mostCurrent._am2)))/(double)2);
 };
 //BA.debugLineNum = 491;BA.debugLine="If am12 = tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 492;BA.debugLine="am12 = am1";
mostCurrent._am12 = mostCurrent._am1;
 };
 //BA.debugLineNum = 494;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2))});
 };
 //BA.debugLineNum = 496;BA.debugLine="If am3 <> tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 497;BA.debugLine="If am2 = tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 498;BA.debugLine="am2 = (am1 + am3)/2";
mostCurrent._am2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am1))+(double)(Double.parseDouble(mostCurrent._am3)))/(double)2);
 };
 //BA.debugLineNum = 500;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3))});
 };
 //BA.debugLineNum = 502;BA.debugLine="If am4 <> tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 503;BA.debugLine="If am3 = tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 504;BA.debugLine="am3 = (am2 + am4)/2";
mostCurrent._am3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am2))+(double)(Double.parseDouble(mostCurrent._am4)))/(double)2);
 };
 //BA.debugLineNum = 506;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4))});
 };
 //BA.debugLineNum = 508;BA.debugLine="If am5 <> tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 509;BA.debugLine="If am4 = tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 510;BA.debugLine="am4 = (am3 + am5)/2";
mostCurrent._am4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am3))+(double)(Double.parseDouble(mostCurrent._am5)))/(double)2);
 };
 //BA.debugLineNum = 512;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5))});
 };
 //BA.debugLineNum = 514;BA.debugLine="If am6 <> tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 515;BA.debugLine="If am5 = tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 516;BA.debugLine="am5 = (am4 + am6)/2";
mostCurrent._am5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am4))+(double)(Double.parseDouble(mostCurrent._am6)))/(double)2);
 };
 //BA.debugLineNum = 518;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6))});
 };
 //BA.debugLineNum = 520;BA.debugLine="If am7 <> tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 521;BA.debugLine="If am6 = tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 522;BA.debugLine="am6 = (am5 + am7)/2";
mostCurrent._am6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am5))+(double)(Double.parseDouble(mostCurrent._am7)))/(double)2);
 };
 //BA.debugLineNum = 524;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7))});
 };
 //BA.debugLineNum = 526;BA.debugLine="If am8 <> tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 527;BA.debugLine="If am7 = tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 528;BA.debugLine="am7 = (am6 + am8)/2";
mostCurrent._am7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am6))+(double)(Double.parseDouble(mostCurrent._am8)))/(double)2);
 };
 //BA.debugLineNum = 530;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8))});
 };
 //BA.debugLineNum = 532;BA.debugLine="If am9 <> tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 533;BA.debugLine="If am8 = tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 534;BA.debugLine="am8 = (am7 + am9)/2";
mostCurrent._am8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am7))+(double)(Double.parseDouble(mostCurrent._am9)))/(double)2);
 };
 //BA.debugLineNum = 536;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9))});
 };
 //BA.debugLineNum = 538;BA.debugLine="If am10 <> tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 539;BA.debugLine="If am9 = tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 540;BA.debugLine="am9 = (am8 + am10)/2";
mostCurrent._am9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am8))+(double)(Double.parseDouble(mostCurrent._am10)))/(double)2);
 };
 //BA.debugLineNum = 542;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10))});
 };
 //BA.debugLineNum = 544;BA.debugLine="If am11 <> tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 545;BA.debugLine="If am10 = tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 546;BA.debugLine="am10 = (am9 + am11)/2";
mostCurrent._am10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am9))+(double)(Double.parseDouble(mostCurrent._am11)))/(double)2);
 };
 //BA.debugLineNum = 548;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11))});
 };
 //BA.debugLineNum = 550;BA.debugLine="If pm12 <> tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 551;BA.debugLine="If am11 = tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 552;BA.debugLine="am11 = (am10 + pm12)/2";
mostCurrent._am11 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am10))+(double)(Double.parseDouble(mostCurrent._pm12)))/(double)2);
 };
 //BA.debugLineNum = 554;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12))});
 };
 //BA.debugLineNum = 556;BA.debugLine="If pm1 <> tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 557;BA.debugLine="If pm12 = tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 558;BA.debugLine="pm12 = (am11 + pm1)/2";
mostCurrent._pm12 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am11))+(double)(Double.parseDouble(mostCurrent._pm1)))/(double)2);
 };
 //BA.debugLineNum = 560;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1))});
 };
 //BA.debugLineNum = 562;BA.debugLine="If pm2 <> tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 563;BA.debugLine="If pm1 = tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 564;BA.debugLine="pm1 = (pm12 + pm2)/2";
mostCurrent._pm1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm12))+(double)(Double.parseDouble(mostCurrent._pm2)))/(double)2);
 };
 //BA.debugLineNum = 566;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2))});
 };
 //BA.debugLineNum = 568;BA.debugLine="If pm3 <> tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 569;BA.debugLine="If pm2 = tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 570;BA.debugLine="pm2 = (pm1 + pm3)/2";
mostCurrent._pm2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm1))+(double)(Double.parseDouble(mostCurrent._pm3)))/(double)2);
 };
 //BA.debugLineNum = 572;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3))});
 };
 //BA.debugLineNum = 574;BA.debugLine="If pm4 <> tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 575;BA.debugLine="If pm3 = tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 576;BA.debugLine="pm3 = (pm2 + pm4)/2";
mostCurrent._pm3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm2))+(double)(Double.parseDouble(mostCurrent._pm4)))/(double)2);
 };
 //BA.debugLineNum = 578;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4))});
 };
 //BA.debugLineNum = 580;BA.debugLine="If pm5 <> tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 581;BA.debugLine="If pm4 = tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 582;BA.debugLine="pm4 = (pm3 + pm5)/2";
mostCurrent._pm4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm3))+(double)(Double.parseDouble(mostCurrent._pm5)))/(double)2);
 };
 //BA.debugLineNum = 584;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5))});
 };
 //BA.debugLineNum = 586;BA.debugLine="If pm6 <> tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 587;BA.debugLine="If pm5 = tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 588;BA.debugLine="pm5 = (pm4 + pm6)/2";
mostCurrent._pm5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm4))+(double)(Double.parseDouble(mostCurrent._pm6)))/(double)2);
 };
 //BA.debugLineNum = 590;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6))});
 };
 //BA.debugLineNum = 592;BA.debugLine="If pm7 <> tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 593;BA.debugLine="If pm6 = tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 594;BA.debugLine="pm6 = (pm5 + pm7)/2";
mostCurrent._pm6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm5))+(double)(Double.parseDouble(mostCurrent._pm7)))/(double)2);
 };
 //BA.debugLineNum = 596;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7))});
 };
 //BA.debugLineNum = 598;BA.debugLine="If pm8 <> tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 599;BA.debugLine="If pm7 = tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 600;BA.debugLine="pm7 = (pm6 + pm8)/2";
mostCurrent._pm7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm6))+(double)(Double.parseDouble(mostCurrent._pm8)))/(double)2);
 };
 //BA.debugLineNum = 602;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8))});
 };
 //BA.debugLineNum = 604;BA.debugLine="If pm9 <> tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 605;BA.debugLine="If pm8 = tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 606;BA.debugLine="pm8 = (pm7 + pm9)/2";
mostCurrent._pm8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm7))+(double)(Double.parseDouble(mostCurrent._pm9)))/(double)2);
 };
 //BA.debugLineNum = 608;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9))});
 };
 //BA.debugLineNum = 610;BA.debugLine="If pm10 <> tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 611;BA.debugLine="If pm9 = tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 612;BA.debugLine="pm9 = (pm8 + pm10)/2";
mostCurrent._pm9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm8))+(double)(Double.parseDouble(mostCurrent._pm10)))/(double)2);
 };
 //BA.debugLineNum = 614;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10))});
 };
 //BA.debugLineNum = 616;BA.debugLine="If pm11 <> tempZeroRange Then";
if ((mostCurrent._pm11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 617;BA.debugLine="If pm10 = tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 618;BA.debugLine="pm10 = (pm9 + pm11)/2";
mostCurrent._pm10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm9))+(double)(Double.parseDouble(mostCurrent._pm11)))/(double)2);
 };
 //BA.debugLineNum = 620;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))});
 };
 //BA.debugLineNum = 623;BA.debugLine="LineChart.Line_1_PointLabelTextColor = Colors.Ye";
mostCurrent._linechart.setLine_1_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 624;BA.debugLine="LineChart.Line_1_PointLabelTextSize = 35.0";
mostCurrent._linechart.setLine_1_PointLabelTextSize((float) (35.0));
 //BA.debugLineNum = 625;BA.debugLine="LineChart.Line_1_LineColor = Colors.Red";
mostCurrent._linechart.setLine_1_LineColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 626;BA.debugLine="LineChart.Line_1_LineWidth = 11.0";
mostCurrent._linechart.setLine_1_LineWidth((float) (11.0));
 //BA.debugLineNum = 627;BA.debugLine="LineChart.Line_1_PointColor = Colors.Black";
mostCurrent._linechart.setLine_1_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 628;BA.debugLine="LineChart.Line_1_PointSize = 25.0";
mostCurrent._linechart.setLine_1_PointSize((float) (25.0));
 //BA.debugLineNum = 629;BA.debugLine="LineChart.Line_1_PointShape = LineChart.SHAPE_RO";
mostCurrent._linechart.setLine_1_PointShape(mostCurrent._linechart.SHAPE_ROUND);
 //BA.debugLineNum = 630;BA.debugLine="LineChart.Line_1_DrawDash = False";
mostCurrent._linechart.setLine_1_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 631;BA.debugLine="LineChart.Line_1_DrawCubic = False";
mostCurrent._linechart.setLine_1_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 638;BA.debugLine="LineChart.Line_2_Data = Array As Float (tempRigh";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow))});
 //BA.debugLineNum = 639;BA.debugLine="LineChart.Line_2_PointLabelTextColor = Colors.Gr";
mostCurrent._linechart.setLine_2_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 640;BA.debugLine="LineChart.Line_2_PointLabelTextSize = 30.0";
mostCurrent._linechart.setLine_2_PointLabelTextSize((float) (30.0));
 //BA.debugLineNum = 641;BA.debugLine="LineChart.Line_2_LineColor = Colors.Green";
mostCurrent._linechart.setLine_2_LineColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 642;BA.debugLine="LineChart.Line_2_LineWidth = 5.0";
mostCurrent._linechart.setLine_2_LineWidth((float) (5.0));
 //BA.debugLineNum = 643;BA.debugLine="LineChart.Line_2_PointColor = Colors.Green";
mostCurrent._linechart.setLine_2_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 644;BA.debugLine="LineChart.Line_2_PointSize = 1.0";
mostCurrent._linechart.setLine_2_PointSize((float) (1.0));
 //BA.debugLineNum = 645;BA.debugLine="LineChart.Line_2_PointShape = LineChart.SHAPE_RO";
mostCurrent._linechart.setLine_2_PointShape(mostCurrent._linechart.SHAPE_ROUND);
 //BA.debugLineNum = 646;BA.debugLine="LineChart.Line_2_DrawDash = False";
mostCurrent._linechart.setLine_2_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 647;BA.debugLine="LineChart.Line_2_DrawCubic = False";
mostCurrent._linechart.setLine_2_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 651;BA.debugLine="LineChart.NumberOfLineCharts = 2";
mostCurrent._linechart.setNumberOfLineCharts((int) (2));
 //BA.debugLineNum = 653;BA.debugLine="LineChart.DrawTheGraphs";
mostCurrent._linechart.DrawTheGraphs();
 } 
       catch (Exception e229) {
			processBA.setLastException(e229); //BA.debugLineNum = 656;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("63080455",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 657;BA.debugLine="ToastMessageShow (LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 659;BA.debugLine="End Sub";
return "";
}
public static String  _humidityhourlytimer_tick() throws Exception{
 //BA.debugLineNum = 2199;BA.debugLine="Sub HumidityHourlyTimer_Tick";
 //BA.debugLineNum = 2200;BA.debugLine="Activity.RequestFocus";
mostCurrent._activity.RequestFocus();
 //BA.debugLineNum = 2201;BA.debugLine="btnHumidityHourly.RemoveView";
mostCurrent._btnhumidityhourly.RemoveView();
 //BA.debugLineNum = 2202;BA.debugLine="btnTempHourly.RemoveView";
mostCurrent._btntemphourly.RemoveView();
 //BA.debugLineNum = 2203;BA.debugLine="btnHumidityDaily.RemoveView";
mostCurrent._btnhumiditydaily.RemoveView();
 //BA.debugLineNum = 2204;BA.debugLine="btnTempDaily.RemoveView";
mostCurrent._btntempdaily.RemoveView();
 //BA.debugLineNum = 2205;BA.debugLine="LineChart.RemoveView";
mostCurrent._linechart.RemoveView();
 //BA.debugLineNum = 2206;BA.debugLine="tempMaxRange=0";
_tempmaxrange = (float) (0);
 //BA.debugLineNum = 2207;BA.debugLine="tempMinRange=0";
_tempminrange = (float) (0);
 //BA.debugLineNum = 2208;BA.debugLine="HumidityHourlyCreate";
_humidityhourlycreate();
 //BA.debugLineNum = 2209;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Private Awake As PhoneWakeState";
_awake = new anywheresoftware.b4a.phone.Phone.PhoneWakeState();
 //BA.debugLineNum = 11;BA.debugLine="Private TemperatureHourlyTimer As Timer";
_temperaturehourlytimer = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 12;BA.debugLine="Private HumidityHourlyTimer As Timer";
_humidityhourlytimer = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 13;BA.debugLine="Private TemperatureDailyTimer As Timer";
_temperaturedailytimer = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 14;BA.debugLine="Private HumidityDailyTimer As Timer";
_humiditydailytimer = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 15;BA.debugLine="Private rp As RuntimePermissions";
_rp = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 16;BA.debugLine="Private shared As String";
_shared = "";
 //BA.debugLineNum = 17;BA.debugLine="Private phone1 As Phone";
_phone1 = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
public static String  _readhumiditydaily(String _fileday) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader1 = null;
long _now = 0L;
int _month = 0;
int _day = 0;
int _year = 0;
String _filename = "";
String _line = "";
String[] _a = null;
String _timestamp = "";
 //BA.debugLineNum = 1643;BA.debugLine="Sub ReadHumidityDaily(fileDay As String)";
 //BA.debugLineNum = 1644;BA.debugLine="Try";
try { //BA.debugLineNum = 1645;BA.debugLine="Dim TextReader1 As TextReader";
_textreader1 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1646;BA.debugLine="Dim Now As Long";
_now = 0L;
 //BA.debugLineNum = 1647;BA.debugLine="Dim Month As Int";
_month = 0;
 //BA.debugLineNum = 1648;BA.debugLine="Dim Day As Int";
_day = 0;
 //BA.debugLineNum = 1649;BA.debugLine="Dim Year As Int";
_year = 0;
 //BA.debugLineNum = 1650;BA.debugLine="Dim FileName As String";
_filename = "";
 //BA.debugLineNum = 1652;BA.debugLine="am12 = zeroRange";
mostCurrent._am12 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1653;BA.debugLine="am1 = zeroRange";
mostCurrent._am1 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1654;BA.debugLine="am2 = zeroRange";
mostCurrent._am2 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1655;BA.debugLine="am3 = zeroRange";
mostCurrent._am3 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1656;BA.debugLine="am4 = zeroRange";
mostCurrent._am4 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1657;BA.debugLine="am5 = zeroRange";
mostCurrent._am5 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1658;BA.debugLine="am6 = zeroRange";
mostCurrent._am6 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1659;BA.debugLine="am7 = zeroRange";
mostCurrent._am7 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1660;BA.debugLine="am8 = zeroRange";
mostCurrent._am8 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1661;BA.debugLine="am9 = zeroRange";
mostCurrent._am9 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1662;BA.debugLine="am10 = zeroRange";
mostCurrent._am10 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1663;BA.debugLine="am11 = zeroRange";
mostCurrent._am11 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1664;BA.debugLine="pm12 = zeroRange";
mostCurrent._pm12 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1665;BA.debugLine="pm1 = zeroRange";
mostCurrent._pm1 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1666;BA.debugLine="pm2 = zeroRange";
mostCurrent._pm2 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1667;BA.debugLine="pm3 = zeroRange";
mostCurrent._pm3 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1668;BA.debugLine="pm4 = zeroRange";
mostCurrent._pm4 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1669;BA.debugLine="pm5 = zeroRange";
mostCurrent._pm5 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1670;BA.debugLine="pm6 = zeroRange";
mostCurrent._pm6 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1671;BA.debugLine="pm7 = zeroRange";
mostCurrent._pm7 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1672;BA.debugLine="pm8 = zeroRange";
mostCurrent._pm8 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1673;BA.debugLine="pm9 = zeroRange";
mostCurrent._pm9 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1674;BA.debugLine="pm10 = zeroRange";
mostCurrent._pm10 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1675;BA.debugLine="pm11 = zeroRange";
mostCurrent._pm11 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1677;BA.debugLine="Now = DateTime.Now";
_now = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 1678;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 1679;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 1680;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 1682;BA.debugLine="If fileDay = \"Today\" Then";
if ((_fileday).equals("Today")) { 
 //BA.debugLineNum = 1683;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\"";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 }else {
 //BA.debugLineNum = 1685;BA.debugLine="Now = DateTime.add(DateTime.Now, 0, 0, -1)";
_now = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1));
 //BA.debugLineNum = 1686;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 1687;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 1688;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 1689;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\"";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 };
 //BA.debugLineNum = 1692;BA.debugLine="shared = rp.GetSafeDirDefaultExternal(\"\")";
_shared = _rp.GetSafeDirDefaultExternal("");
 //BA.debugLineNum = 1693;BA.debugLine="TextReader1.Initialize(File.OpenInput(shared, Fi";
_textreader1.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(_shared,_filename).getObject()));
 //BA.debugLineNum = 1694;BA.debugLine="Dim line As String";
_line = "";
 //BA.debugLineNum = 1695;BA.debugLine="line = TextReader1.ReadLine";
_line = _textreader1.ReadLine();
 //BA.debugLineNum = 1696;BA.debugLine="Do While line <> Null";
while (_line!= null) {
 //BA.debugLineNum = 1698;BA.debugLine="line = TextReader1.ReadLine";
_line = _textreader1.ReadLine();
 //BA.debugLineNum = 1699;BA.debugLine="If line = Null Then";
if (_line== null) { 
 //BA.debugLineNum = 1700;BA.debugLine="Exit";
if (true) break;
 };
 //BA.debugLineNum = 1702;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",line)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_line);
 //BA.debugLineNum = 1703;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 1704;BA.debugLine="Dim timeStamp As String";
_timestamp = "";
 //BA.debugLineNum = 1705;BA.debugLine="timeStamp = a(0).SubString2(0,2)";
_timestamp = _a[(int) (0)].substring((int) (0),(int) (2));
 //BA.debugLineNum = 1707;BA.debugLine="If IsNumber(a(2)) = False Then Continue";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (2)])==anywheresoftware.b4a.keywords.Common.False) { 
if (true) continue;};
 //BA.debugLineNum = 1709;BA.debugLine="Select timeStamp";
switch (BA.switchObjectToInt(_timestamp,"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23")) {
case 0: {
 //BA.debugLineNum = 1711;BA.debugLine="If am12 = zeroRange Or am12 = \"\" Then am12 =";
if ((mostCurrent._am12).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am12).equals("")) { 
mostCurrent._am12 = _a[(int) (2)];};
 break; }
case 1: {
 //BA.debugLineNum = 1713;BA.debugLine="If am1 = zeroRange Or am1 = \"\" Then am1 = a(";
if ((mostCurrent._am1).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am1).equals("")) { 
mostCurrent._am1 = _a[(int) (2)];};
 break; }
case 2: {
 //BA.debugLineNum = 1715;BA.debugLine="If am2 = zeroRange Or am2 = \"\" Then am2 = a(";
if ((mostCurrent._am2).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am2).equals("")) { 
mostCurrent._am2 = _a[(int) (2)];};
 break; }
case 3: {
 //BA.debugLineNum = 1717;BA.debugLine="If am3 = zeroRange Or am3 = \"\" Then am3 = a(";
if ((mostCurrent._am3).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am3).equals("")) { 
mostCurrent._am3 = _a[(int) (2)];};
 break; }
case 4: {
 //BA.debugLineNum = 1719;BA.debugLine="If am4 = zeroRange Or am4 = \"\" Then am4 = a(";
if ((mostCurrent._am4).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am4).equals("")) { 
mostCurrent._am4 = _a[(int) (2)];};
 break; }
case 5: {
 //BA.debugLineNum = 1721;BA.debugLine="If am5 = zeroRange Or am5 = \"\" Then am5 = a(";
if ((mostCurrent._am5).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am5).equals("")) { 
mostCurrent._am5 = _a[(int) (2)];};
 break; }
case 6: {
 //BA.debugLineNum = 1723;BA.debugLine="If am6 = zeroRange Or am6 = \"\" Then am6 = a(";
if ((mostCurrent._am6).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am6).equals("")) { 
mostCurrent._am6 = _a[(int) (2)];};
 break; }
case 7: {
 //BA.debugLineNum = 1725;BA.debugLine="If am7 = zeroRange Or am7 = \"\" Then am7 = a(";
if ((mostCurrent._am7).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am7).equals("")) { 
mostCurrent._am7 = _a[(int) (2)];};
 break; }
case 8: {
 //BA.debugLineNum = 1727;BA.debugLine="If am8 = zeroRange Or am8 = \"\" Then am8 = a(";
if ((mostCurrent._am8).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am8).equals("")) { 
mostCurrent._am8 = _a[(int) (2)];};
 break; }
case 9: {
 //BA.debugLineNum = 1729;BA.debugLine="If am9 = zeroRange Or am9 = \"\" Then am9 = a(";
if ((mostCurrent._am9).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am9).equals("")) { 
mostCurrent._am9 = _a[(int) (2)];};
 break; }
case 10: {
 //BA.debugLineNum = 1731;BA.debugLine="If am10 = zeroRange Or am10 = \"\" Then am10 =";
if ((mostCurrent._am10).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am10).equals("")) { 
mostCurrent._am10 = _a[(int) (2)];};
 break; }
case 11: {
 //BA.debugLineNum = 1733;BA.debugLine="If am11 = zeroRange Or am11 = \"\" Then am11 =";
if ((mostCurrent._am11).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am11).equals("")) { 
mostCurrent._am11 = _a[(int) (2)];};
 break; }
case 12: {
 //BA.debugLineNum = 1735;BA.debugLine="If pm12 = zeroRange Or pm12 = \"\" Then pm12 =";
if ((mostCurrent._pm12).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm12).equals("")) { 
mostCurrent._pm12 = _a[(int) (2)];};
 break; }
case 13: {
 //BA.debugLineNum = 1737;BA.debugLine="If pm1 = zeroRange Or pm1 = \"\" Then pm1 = a(";
if ((mostCurrent._pm1).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm1).equals("")) { 
mostCurrent._pm1 = _a[(int) (2)];};
 break; }
case 14: {
 //BA.debugLineNum = 1739;BA.debugLine="If pm2 = zeroRange Or pm2 = \"\" Then pm2 = a(";
if ((mostCurrent._pm2).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm2).equals("")) { 
mostCurrent._pm2 = _a[(int) (2)];};
 break; }
case 15: {
 //BA.debugLineNum = 1741;BA.debugLine="If pm3 = zeroRange Or pm3 = \"\" Then pm3 = a(";
if ((mostCurrent._pm3).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm3).equals("")) { 
mostCurrent._pm3 = _a[(int) (2)];};
 break; }
case 16: {
 //BA.debugLineNum = 1743;BA.debugLine="If pm4 = zeroRange Or pm4 = \"\" Then pm4 = a(";
if ((mostCurrent._pm4).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm4).equals("")) { 
mostCurrent._pm4 = _a[(int) (2)];};
 break; }
case 17: {
 //BA.debugLineNum = 1745;BA.debugLine="If pm5 = zeroRange Or pm5 = \"\" Then pm5 = a(";
if ((mostCurrent._pm5).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm5).equals("")) { 
mostCurrent._pm5 = _a[(int) (2)];};
 break; }
case 18: {
 //BA.debugLineNum = 1747;BA.debugLine="If pm6 = zeroRange Or pm6 = \"\" Then pm6 = a(";
if ((mostCurrent._pm6).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm6).equals("")) { 
mostCurrent._pm6 = _a[(int) (2)];};
 break; }
case 19: {
 //BA.debugLineNum = 1749;BA.debugLine="If pm7 = zeroRange Or pm7 = \"\" Then pm7 = a(";
if ((mostCurrent._pm7).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm7).equals("")) { 
mostCurrent._pm7 = _a[(int) (2)];};
 break; }
case 20: {
 //BA.debugLineNum = 1751;BA.debugLine="If pm8 = zeroRange Or pm8 = \"\" Then pm8 = a(";
if ((mostCurrent._pm8).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm8).equals("")) { 
mostCurrent._pm8 = _a[(int) (2)];};
 break; }
case 21: {
 //BA.debugLineNum = 1753;BA.debugLine="If pm9 = zeroRange Or pm9 = \"\" Then pm9 = a(";
if ((mostCurrent._pm9).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm9).equals("")) { 
mostCurrent._pm9 = _a[(int) (2)];};
 break; }
case 22: {
 //BA.debugLineNum = 1755;BA.debugLine="If pm10 = zeroRange Or pm10 = \"\" Then pm10 =";
if ((mostCurrent._pm10).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm10).equals("")) { 
mostCurrent._pm10 = _a[(int) (2)];};
 break; }
case 23: {
 //BA.debugLineNum = 1757;BA.debugLine="If pm11 = zeroRange Or pm11 = \"\" Then pm11 =";
if ((mostCurrent._pm11).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm11).equals("")) { 
mostCurrent._pm11 = _a[(int) (2)];};
 break; }
}
;
 //BA.debugLineNum = 1759;BA.debugLine="If fileDay = \"Today\" Then";
if ((_fileday).equals("Today")) { 
 //BA.debugLineNum = 1760;BA.debugLine="tempRightNow = a(2)";
mostCurrent._temprightnow = _a[(int) (2)];
 };
 };
 }
;
 //BA.debugLineNum = 1765;BA.debugLine="TextReader1.Close";
_textreader1.Close();
 } 
       catch (Exception e116) {
			processBA.setLastException(e116); //BA.debugLineNum = 1767;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("63407996",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1769;BA.debugLine="End Sub";
return "";
}
public static String  _readhumidityhourly(String _fileday) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader1 = null;
long _now = 0L;
int _month = 0;
int _day = 0;
int _year = 0;
String _filename = "";
String _line = "";
String[] _a = null;
String _timestamp = "";
 //BA.debugLineNum = 1902;BA.debugLine="Sub ReadHumidityHourly(fileDay As String)";
 //BA.debugLineNum = 1903;BA.debugLine="Try";
try { //BA.debugLineNum = 1904;BA.debugLine="Dim TextReader1 As TextReader";
_textreader1 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1905;BA.debugLine="Dim Now As Long";
_now = 0L;
 //BA.debugLineNum = 1906;BA.debugLine="Dim Month As Int";
_month = 0;
 //BA.debugLineNum = 1907;BA.debugLine="Dim Day As Int";
_day = 0;
 //BA.debugLineNum = 1908;BA.debugLine="Dim Year As Int";
_year = 0;
 //BA.debugLineNum = 1909;BA.debugLine="Dim FileName As String";
_filename = "";
 //BA.debugLineNum = 1911;BA.debugLine="am12 = zeroRange";
mostCurrent._am12 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1912;BA.debugLine="am1 = zeroRange";
mostCurrent._am1 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1913;BA.debugLine="am2 = zeroRange";
mostCurrent._am2 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1914;BA.debugLine="am3 = zeroRange";
mostCurrent._am3 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1915;BA.debugLine="am4 = zeroRange";
mostCurrent._am4 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1916;BA.debugLine="am5 = zeroRange";
mostCurrent._am5 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1917;BA.debugLine="am6 = zeroRange";
mostCurrent._am6 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1918;BA.debugLine="am7 = zeroRange";
mostCurrent._am7 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1919;BA.debugLine="am8 = zeroRange";
mostCurrent._am8 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1920;BA.debugLine="am9 = zeroRange";
mostCurrent._am9 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1921;BA.debugLine="am10 = zeroRange";
mostCurrent._am10 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1922;BA.debugLine="am11 = zeroRange";
mostCurrent._am11 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1923;BA.debugLine="pm12 = zeroRange";
mostCurrent._pm12 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1924;BA.debugLine="pm1 = zeroRange";
mostCurrent._pm1 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1925;BA.debugLine="pm2 = zeroRange";
mostCurrent._pm2 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1926;BA.debugLine="pm3 = zeroRange";
mostCurrent._pm3 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1927;BA.debugLine="pm4 = zeroRange";
mostCurrent._pm4 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1928;BA.debugLine="pm5 = zeroRange";
mostCurrent._pm5 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1929;BA.debugLine="pm6 = zeroRange";
mostCurrent._pm6 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1930;BA.debugLine="pm7 = zeroRange";
mostCurrent._pm7 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1931;BA.debugLine="pm8 = zeroRange";
mostCurrent._pm8 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1932;BA.debugLine="pm9 = zeroRange";
mostCurrent._pm9 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1933;BA.debugLine="pm10 = zeroRange";
mostCurrent._pm10 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1934;BA.debugLine="pm11 = zeroRange";
mostCurrent._pm11 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1936;BA.debugLine="Now = DateTime.Now";
_now = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 1937;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 1938;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 1939;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 1941;BA.debugLine="If fileDay = \"Today\" Then";
if ((_fileday).equals("Today")) { 
 //BA.debugLineNum = 1942;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\"";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 }else {
 //BA.debugLineNum = 1944;BA.debugLine="Now = DateTime.add(DateTime.Now, 0, 0, -1)";
_now = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1));
 //BA.debugLineNum = 1945;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 1946;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 1947;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 1948;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\"";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 };
 //BA.debugLineNum = 1951;BA.debugLine="shared = rp.GetSafeDirDefaultExternal(\"\")";
_shared = _rp.GetSafeDirDefaultExternal("");
 //BA.debugLineNum = 1952;BA.debugLine="TextReader1.Initialize(File.OpenInput(shared, Fi";
_textreader1.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(_shared,_filename).getObject()));
 //BA.debugLineNum = 1953;BA.debugLine="Dim line As String";
_line = "";
 //BA.debugLineNum = 1954;BA.debugLine="line = TextReader1.ReadLine";
_line = _textreader1.ReadLine();
 //BA.debugLineNum = 1955;BA.debugLine="Do While line <> Null";
while (_line!= null) {
 //BA.debugLineNum = 1957;BA.debugLine="line = TextReader1.ReadLine";
_line = _textreader1.ReadLine();
 //BA.debugLineNum = 1958;BA.debugLine="If line = Null Then";
if (_line== null) { 
 //BA.debugLineNum = 1959;BA.debugLine="Exit";
if (true) break;
 };
 //BA.debugLineNum = 1961;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",line)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_line);
 //BA.debugLineNum = 1962;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 1963;BA.debugLine="Dim timeStamp As String";
_timestamp = "";
 //BA.debugLineNum = 1964;BA.debugLine="timeStamp = a(0).SubString2(0,5)";
_timestamp = _a[(int) (0)].substring((int) (0),(int) (5));
 //BA.debugLineNum = 1966;BA.debugLine="If IsNumber(a(2)) = False Then Continue";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (2)])==anywheresoftware.b4a.keywords.Common.False) { 
if (true) continue;};
 //BA.debugLineNum = 1968;BA.debugLine="Select timeStamp";
switch (BA.switchObjectToInt(_timestamp,mostCurrent._timearray[(int) (0)],mostCurrent._timearray[(int) (1)],mostCurrent._timearray[(int) (2)],mostCurrent._timearray[(int) (3)],mostCurrent._timearray[(int) (4)],mostCurrent._timearray[(int) (5)],mostCurrent._timearray[(int) (6)],mostCurrent._timearray[(int) (7)],mostCurrent._timearray[(int) (8)],mostCurrent._timearray[(int) (9)],mostCurrent._timearray[(int) (10)],mostCurrent._timearray[(int) (11)],mostCurrent._timearray[(int) (12)],mostCurrent._timearray[(int) (13)],mostCurrent._timearray[(int) (14)],mostCurrent._timearray[(int) (15)],mostCurrent._timearray[(int) (16)],mostCurrent._timearray[(int) (17)],mostCurrent._timearray[(int) (18)],mostCurrent._timearray[(int) (19)],mostCurrent._timearray[(int) (20)],mostCurrent._timearray[(int) (21)],mostCurrent._timearray[(int) (22)],mostCurrent._timearray[(int) (23)])) {
case 0: {
 //BA.debugLineNum = 1970;BA.debugLine="If am12 = zeroRange Or am12 = \"\" Then am12 =";
if ((mostCurrent._am12).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am12).equals("")) { 
mostCurrent._am12 = _a[(int) (2)];};
 break; }
case 1: {
 //BA.debugLineNum = 1972;BA.debugLine="If am1 = zeroRange Or am1 = \"\" Then am1 = a(";
if ((mostCurrent._am1).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am1).equals("")) { 
mostCurrent._am1 = _a[(int) (2)];};
 break; }
case 2: {
 //BA.debugLineNum = 1974;BA.debugLine="If am2 = zeroRange Or am2 = \"\" Then am2 = a(";
if ((mostCurrent._am2).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am2).equals("")) { 
mostCurrent._am2 = _a[(int) (2)];};
 break; }
case 3: {
 //BA.debugLineNum = 1976;BA.debugLine="If am3 = zeroRange Or am3 = \"\" Then am3 = a(";
if ((mostCurrent._am3).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am3).equals("")) { 
mostCurrent._am3 = _a[(int) (2)];};
 break; }
case 4: {
 //BA.debugLineNum = 1978;BA.debugLine="If am4 = zeroRange Or am4 = \"\" Then am4 = a(";
if ((mostCurrent._am4).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am4).equals("")) { 
mostCurrent._am4 = _a[(int) (2)];};
 break; }
case 5: {
 //BA.debugLineNum = 1980;BA.debugLine="If am5 = zeroRange Or am5 = \"\" Then am5 = a(";
if ((mostCurrent._am5).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am5).equals("")) { 
mostCurrent._am5 = _a[(int) (2)];};
 break; }
case 6: {
 //BA.debugLineNum = 1982;BA.debugLine="If am6 = zeroRange Or am6 = \"\" Then am6 = a(";
if ((mostCurrent._am6).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am6).equals("")) { 
mostCurrent._am6 = _a[(int) (2)];};
 break; }
case 7: {
 //BA.debugLineNum = 1984;BA.debugLine="If am7 = zeroRange Or am7 = \"\" Then am7 = a(";
if ((mostCurrent._am7).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am7).equals("")) { 
mostCurrent._am7 = _a[(int) (2)];};
 break; }
case 8: {
 //BA.debugLineNum = 1986;BA.debugLine="If am8 = zeroRange Or am8 = \"\" Then am8 = a(";
if ((mostCurrent._am8).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am8).equals("")) { 
mostCurrent._am8 = _a[(int) (2)];};
 break; }
case 9: {
 //BA.debugLineNum = 1988;BA.debugLine="If am9 = zeroRange Or am9 = \"\" Then am9 = a(";
if ((mostCurrent._am9).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am9).equals("")) { 
mostCurrent._am9 = _a[(int) (2)];};
 break; }
case 10: {
 //BA.debugLineNum = 1990;BA.debugLine="If am10 = zeroRange Or am10 = \"\" Then am10 =";
if ((mostCurrent._am10).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am10).equals("")) { 
mostCurrent._am10 = _a[(int) (2)];};
 break; }
case 11: {
 //BA.debugLineNum = 1992;BA.debugLine="If am11 = zeroRange Or am11 = \"\" Then am11 =";
if ((mostCurrent._am11).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am11).equals("")) { 
mostCurrent._am11 = _a[(int) (2)];};
 break; }
case 12: {
 //BA.debugLineNum = 1994;BA.debugLine="If pm12 = zeroRange Or pm12 = \"\" Then pm12 =";
if ((mostCurrent._pm12).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm12).equals("")) { 
mostCurrent._pm12 = _a[(int) (2)];};
 break; }
case 13: {
 //BA.debugLineNum = 1996;BA.debugLine="If pm1 = zeroRange Or pm1 = \"\" Then pm1 = a(";
if ((mostCurrent._pm1).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm1).equals("")) { 
mostCurrent._pm1 = _a[(int) (2)];};
 break; }
case 14: {
 //BA.debugLineNum = 1998;BA.debugLine="If pm2 = zeroRange Or pm2 = \"\" Then pm2 = a(";
if ((mostCurrent._pm2).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm2).equals("")) { 
mostCurrent._pm2 = _a[(int) (2)];};
 break; }
case 15: {
 //BA.debugLineNum = 2000;BA.debugLine="If pm3 = zeroRange Or pm3 = \"\" Then pm3 = a(";
if ((mostCurrent._pm3).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm3).equals("")) { 
mostCurrent._pm3 = _a[(int) (2)];};
 break; }
case 16: {
 //BA.debugLineNum = 2002;BA.debugLine="If pm4 = zeroRange Or pm4 = \"\" Then pm4 = a(";
if ((mostCurrent._pm4).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm4).equals("")) { 
mostCurrent._pm4 = _a[(int) (2)];};
 break; }
case 17: {
 //BA.debugLineNum = 2004;BA.debugLine="If pm5 = zeroRange Or pm5 = \"\" Then pm5 = a(";
if ((mostCurrent._pm5).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm5).equals("")) { 
mostCurrent._pm5 = _a[(int) (2)];};
 break; }
case 18: {
 //BA.debugLineNum = 2006;BA.debugLine="If pm6 = zeroRange Or pm6 = \"\" Then pm6 = a(";
if ((mostCurrent._pm6).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm6).equals("")) { 
mostCurrent._pm6 = _a[(int) (2)];};
 break; }
case 19: {
 //BA.debugLineNum = 2008;BA.debugLine="If pm7 = zeroRange Or pm7 = \"\" Then pm7 = a(";
if ((mostCurrent._pm7).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm7).equals("")) { 
mostCurrent._pm7 = _a[(int) (2)];};
 break; }
case 20: {
 //BA.debugLineNum = 2010;BA.debugLine="If pm8 = zeroRange Or pm8 = \"\" Then pm8 = a(";
if ((mostCurrent._pm8).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm8).equals("")) { 
mostCurrent._pm8 = _a[(int) (2)];};
 break; }
case 21: {
 //BA.debugLineNum = 2012;BA.debugLine="If pm9 = zeroRange Or pm9 = \"\" Then pm9 = a(";
if ((mostCurrent._pm9).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm9).equals("")) { 
mostCurrent._pm9 = _a[(int) (2)];};
 break; }
case 22: {
 //BA.debugLineNum = 2014;BA.debugLine="If pm10 = zeroRange Or pm10 = \"\" Then pm10 =";
if ((mostCurrent._pm10).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm10).equals("")) { 
mostCurrent._pm10 = _a[(int) (2)];};
 break; }
case 23: {
 //BA.debugLineNum = 2016;BA.debugLine="If pm11 = zeroRange Or pm11 = \"\" Then pm11 =";
if ((mostCurrent._pm11).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm11).equals("")) { 
mostCurrent._pm11 = _a[(int) (2)];};
 break; }
}
;
 //BA.debugLineNum = 2018;BA.debugLine="If fileDay = \"Today\" Then";
if ((_fileday).equals("Today")) { 
 //BA.debugLineNum = 2019;BA.debugLine="tempRightNow = a(2)";
mostCurrent._temprightnow = _a[(int) (2)];
 //BA.debugLineNum = 2020;BA.debugLine="DateTime.TimeFormat = \"h:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("h:mm a");
 };
 };
 }
;
 //BA.debugLineNum = 2025;BA.debugLine="TextReader1.Close";
_textreader1.Close();
 } 
       catch (Exception e117) {
			processBA.setLastException(e117); //BA.debugLineNum = 2029;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("63539071",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 2031;BA.debugLine="End Sub";
return "";
}
public static String  _readtemperaturedaily(String _fileday) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader1 = null;
long _now = 0L;
int _month = 0;
int _day = 0;
int _year = 0;
String _filename = "";
String _line = "";
String[] _a = null;
String _timestamp = "";
 //BA.debugLineNum = 1515;BA.debugLine="Sub ReadTemperatureDaily(fileDay As String)";
 //BA.debugLineNum = 1516;BA.debugLine="Try";
try { //BA.debugLineNum = 1517;BA.debugLine="Dim TextReader1 As TextReader";
_textreader1 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1518;BA.debugLine="Dim Now As Long";
_now = 0L;
 //BA.debugLineNum = 1519;BA.debugLine="Dim Month As Int";
_month = 0;
 //BA.debugLineNum = 1520;BA.debugLine="Dim Day As Int";
_day = 0;
 //BA.debugLineNum = 1521;BA.debugLine="Dim Year As Int";
_year = 0;
 //BA.debugLineNum = 1522;BA.debugLine="Dim FileName As String";
_filename = "";
 //BA.debugLineNum = 1524;BA.debugLine="am12 = zeroRange";
mostCurrent._am12 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1525;BA.debugLine="am1 = zeroRange";
mostCurrent._am1 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1526;BA.debugLine="am2 = zeroRange";
mostCurrent._am2 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1527;BA.debugLine="am3 = zeroRange";
mostCurrent._am3 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1528;BA.debugLine="am4 = zeroRange";
mostCurrent._am4 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1529;BA.debugLine="am5 = zeroRange";
mostCurrent._am5 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1530;BA.debugLine="am6 = zeroRange";
mostCurrent._am6 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1531;BA.debugLine="am7 = zeroRange";
mostCurrent._am7 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1532;BA.debugLine="am8 = zeroRange";
mostCurrent._am8 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1533;BA.debugLine="am9 = zeroRange";
mostCurrent._am9 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1534;BA.debugLine="am10 = zeroRange";
mostCurrent._am10 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1535;BA.debugLine="am11 = zeroRange";
mostCurrent._am11 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1536;BA.debugLine="pm12 = zeroRange";
mostCurrent._pm12 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1537;BA.debugLine="pm1 = zeroRange";
mostCurrent._pm1 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1538;BA.debugLine="pm2 = zeroRange";
mostCurrent._pm2 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1539;BA.debugLine="pm3 = zeroRange";
mostCurrent._pm3 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1540;BA.debugLine="pm4 = zeroRange";
mostCurrent._pm4 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1541;BA.debugLine="pm5 = zeroRange";
mostCurrent._pm5 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1542;BA.debugLine="pm6 = zeroRange";
mostCurrent._pm6 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1543;BA.debugLine="pm7 = zeroRange";
mostCurrent._pm7 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1544;BA.debugLine="pm8 = zeroRange";
mostCurrent._pm8 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1545;BA.debugLine="pm9 = zeroRange";
mostCurrent._pm9 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1546;BA.debugLine="pm10 = zeroRange";
mostCurrent._pm10 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1547;BA.debugLine="pm11 = zeroRange";
mostCurrent._pm11 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1549;BA.debugLine="Now = DateTime.Now";
_now = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 1550;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 1551;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 1552;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 1554;BA.debugLine="If fileDay = \"Today\" Then";
if ((_fileday).equals("Today")) { 
 //BA.debugLineNum = 1555;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\"";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 }else {
 //BA.debugLineNum = 1557;BA.debugLine="Now = DateTime.add(DateTime.Now, 0, 0, -1)";
_now = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1));
 //BA.debugLineNum = 1558;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 1559;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 1560;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 1561;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\"";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 };
 //BA.debugLineNum = 1564;BA.debugLine="shared = rp.GetSafeDirDefaultExternal(\"\")";
_shared = _rp.GetSafeDirDefaultExternal("");
 //BA.debugLineNum = 1565;BA.debugLine="TextReader1.Initialize(File.OpenInput(shared, Fi";
_textreader1.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(_shared,_filename).getObject()));
 //BA.debugLineNum = 1566;BA.debugLine="Dim line As String";
_line = "";
 //BA.debugLineNum = 1567;BA.debugLine="line = TextReader1.ReadLine";
_line = _textreader1.ReadLine();
 //BA.debugLineNum = 1568;BA.debugLine="Do While line <> Null";
while (_line!= null) {
 //BA.debugLineNum = 1570;BA.debugLine="line = TextReader1.ReadLine";
_line = _textreader1.ReadLine();
 //BA.debugLineNum = 1571;BA.debugLine="If line = Null Then";
if (_line== null) { 
 //BA.debugLineNum = 1572;BA.debugLine="Exit";
if (true) break;
 };
 //BA.debugLineNum = 1574;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",line)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_line);
 //BA.debugLineNum = 1575;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 1576;BA.debugLine="Dim timeStamp As String";
_timestamp = "";
 //BA.debugLineNum = 1577;BA.debugLine="timeStamp = a(0).SubString2(0,2)";
_timestamp = _a[(int) (0)].substring((int) (0),(int) (2));
 //BA.debugLineNum = 1579;BA.debugLine="If IsNumber(a(1)) = False Then Continue";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (1)])==anywheresoftware.b4a.keywords.Common.False) { 
if (true) continue;};
 //BA.debugLineNum = 1581;BA.debugLine="Select timeStamp";
switch (BA.switchObjectToInt(_timestamp,"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23")) {
case 0: {
 //BA.debugLineNum = 1583;BA.debugLine="If am12 = zeroRange Or am12 = \"\" Then am12 =";
if ((mostCurrent._am12).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am12).equals("")) { 
mostCurrent._am12 = _a[(int) (1)];};
 break; }
case 1: {
 //BA.debugLineNum = 1585;BA.debugLine="If am1 = zeroRange Or am1 = \"\" Then am1 = a(";
if ((mostCurrent._am1).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am1).equals("")) { 
mostCurrent._am1 = _a[(int) (1)];};
 break; }
case 2: {
 //BA.debugLineNum = 1587;BA.debugLine="If am2 = zeroRange Or am2 = \"\" Then am2 = a(";
if ((mostCurrent._am2).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am2).equals("")) { 
mostCurrent._am2 = _a[(int) (1)];};
 break; }
case 3: {
 //BA.debugLineNum = 1589;BA.debugLine="If am3 = zeroRange Or am3 = \"\" Then am3 = a(";
if ((mostCurrent._am3).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am3).equals("")) { 
mostCurrent._am3 = _a[(int) (1)];};
 break; }
case 4: {
 //BA.debugLineNum = 1591;BA.debugLine="If am4 = zeroRange Or am4 = \"\" Then am4 = a(";
if ((mostCurrent._am4).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am4).equals("")) { 
mostCurrent._am4 = _a[(int) (1)];};
 break; }
case 5: {
 //BA.debugLineNum = 1593;BA.debugLine="If am5 = zeroRange Or am5 = \"\" Then am5 = a(";
if ((mostCurrent._am5).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am5).equals("")) { 
mostCurrent._am5 = _a[(int) (1)];};
 break; }
case 6: {
 //BA.debugLineNum = 1595;BA.debugLine="If am6 = zeroRange Or am6 = \"\" Then am6 = a(";
if ((mostCurrent._am6).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am6).equals("")) { 
mostCurrent._am6 = _a[(int) (1)];};
 break; }
case 7: {
 //BA.debugLineNum = 1597;BA.debugLine="If am7 = zeroRange Or am7 = \"\" Then am7 = a(";
if ((mostCurrent._am7).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am7).equals("")) { 
mostCurrent._am7 = _a[(int) (1)];};
 break; }
case 8: {
 //BA.debugLineNum = 1599;BA.debugLine="If am8 = zeroRange Or am8 = \"\" Then am8 = a(";
if ((mostCurrent._am8).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am8).equals("")) { 
mostCurrent._am8 = _a[(int) (1)];};
 break; }
case 9: {
 //BA.debugLineNum = 1601;BA.debugLine="If am9 = zeroRange Or am9 = \"\" Then am9 = a(";
if ((mostCurrent._am9).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am9).equals("")) { 
mostCurrent._am9 = _a[(int) (1)];};
 break; }
case 10: {
 //BA.debugLineNum = 1603;BA.debugLine="If am10 = zeroRange Or am10 = \"\" Then am10 =";
if ((mostCurrent._am10).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am10).equals("")) { 
mostCurrent._am10 = _a[(int) (1)];};
 break; }
case 11: {
 //BA.debugLineNum = 1605;BA.debugLine="If am11 = zeroRange Or am11 = \"\" Then am11 =";
if ((mostCurrent._am11).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am11).equals("")) { 
mostCurrent._am11 = _a[(int) (1)];};
 break; }
case 12: {
 //BA.debugLineNum = 1607;BA.debugLine="If pm12 = zeroRange Or pm12 = \"\" Then pm12 =";
if ((mostCurrent._pm12).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm12).equals("")) { 
mostCurrent._pm12 = _a[(int) (1)];};
 break; }
case 13: {
 //BA.debugLineNum = 1609;BA.debugLine="If pm1 = zeroRange Or pm1 = \"\" Then pm1 = a(";
if ((mostCurrent._pm1).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm1).equals("")) { 
mostCurrent._pm1 = _a[(int) (1)];};
 break; }
case 14: {
 //BA.debugLineNum = 1611;BA.debugLine="If pm2 = zeroRange Or pm2 = \"\" Then pm2 = a(";
if ((mostCurrent._pm2).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm2).equals("")) { 
mostCurrent._pm2 = _a[(int) (1)];};
 break; }
case 15: {
 //BA.debugLineNum = 1613;BA.debugLine="If pm3 = zeroRange Or pm3 = \"\" Then pm3 = a(";
if ((mostCurrent._pm3).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm3).equals("")) { 
mostCurrent._pm3 = _a[(int) (1)];};
 break; }
case 16: {
 //BA.debugLineNum = 1615;BA.debugLine="If pm4 = zeroRange Or pm4 = \"\" Then pm4 = a(";
if ((mostCurrent._pm4).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm4).equals("")) { 
mostCurrent._pm4 = _a[(int) (1)];};
 break; }
case 17: {
 //BA.debugLineNum = 1617;BA.debugLine="If pm5 = zeroRange Or pm5 = \"\" Then pm5 = a(";
if ((mostCurrent._pm5).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm5).equals("")) { 
mostCurrent._pm5 = _a[(int) (1)];};
 break; }
case 18: {
 //BA.debugLineNum = 1619;BA.debugLine="If pm6 = zeroRange Or pm6 = \"\" Then pm6 = a(";
if ((mostCurrent._pm6).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm6).equals("")) { 
mostCurrent._pm6 = _a[(int) (1)];};
 break; }
case 19: {
 //BA.debugLineNum = 1621;BA.debugLine="If pm7 = zeroRange Or pm7 = \"\" Then pm7 = a(";
if ((mostCurrent._pm7).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm7).equals("")) { 
mostCurrent._pm7 = _a[(int) (1)];};
 break; }
case 20: {
 //BA.debugLineNum = 1623;BA.debugLine="If pm8 = zeroRange Or pm8 = \"\" Then pm8 = a(";
if ((mostCurrent._pm8).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm8).equals("")) { 
mostCurrent._pm8 = _a[(int) (1)];};
 break; }
case 21: {
 //BA.debugLineNum = 1625;BA.debugLine="If pm9 = zeroRange Or pm9 = \"\" Then pm9 = a(";
if ((mostCurrent._pm9).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm9).equals("")) { 
mostCurrent._pm9 = _a[(int) (1)];};
 break; }
case 22: {
 //BA.debugLineNum = 1627;BA.debugLine="If pm10 = zeroRange Or pm10 = \"\" Then pm10 =";
if ((mostCurrent._pm10).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm10).equals("")) { 
mostCurrent._pm10 = _a[(int) (1)];};
 break; }
case 23: {
 //BA.debugLineNum = 1629;BA.debugLine="If pm11 = zeroRange Or pm11 = \"\" Then pm11 =";
if ((mostCurrent._pm11).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm11).equals("")) { 
mostCurrent._pm11 = _a[(int) (1)];};
 break; }
}
;
 //BA.debugLineNum = 1631;BA.debugLine="If fileDay = \"Today\" Then";
if ((_fileday).equals("Today")) { 
 //BA.debugLineNum = 1632;BA.debugLine="tempRightNow = a(1)";
mostCurrent._temprightnow = _a[(int) (1)];
 };
 };
 }
;
 //BA.debugLineNum = 1637;BA.debugLine="TextReader1.Close";
_textreader1.Close();
 } 
       catch (Exception e116) {
			processBA.setLastException(e116); //BA.debugLineNum = 1639;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("63342460",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1641;BA.debugLine="End Sub";
return "";
}
public static String  _readtemperaturehourly(String _fileday) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader1 = null;
long _now = 0L;
int _month = 0;
int _day = 0;
int _year = 0;
String _filename = "";
String _line = "";
String[] _a = null;
String _timestamp = "";
 //BA.debugLineNum = 1771;BA.debugLine="Sub ReadTemperatureHourly(fileDay As String)";
 //BA.debugLineNum = 1772;BA.debugLine="Try";
try { //BA.debugLineNum = 1773;BA.debugLine="Dim TextReader1 As TextReader";
_textreader1 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1774;BA.debugLine="Dim Now As Long";
_now = 0L;
 //BA.debugLineNum = 1775;BA.debugLine="Dim Month As Int";
_month = 0;
 //BA.debugLineNum = 1776;BA.debugLine="Dim Day As Int";
_day = 0;
 //BA.debugLineNum = 1777;BA.debugLine="Dim Year As Int";
_year = 0;
 //BA.debugLineNum = 1778;BA.debugLine="Dim FileName As String";
_filename = "";
 //BA.debugLineNum = 1780;BA.debugLine="am12 = zeroRange";
mostCurrent._am12 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1781;BA.debugLine="am1 = zeroRange";
mostCurrent._am1 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1782;BA.debugLine="am2 = zeroRange";
mostCurrent._am2 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1783;BA.debugLine="am3 = zeroRange";
mostCurrent._am3 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1784;BA.debugLine="am4 = zeroRange";
mostCurrent._am4 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1785;BA.debugLine="am5 = zeroRange";
mostCurrent._am5 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1786;BA.debugLine="am6 = zeroRange";
mostCurrent._am6 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1787;BA.debugLine="am7 = zeroRange";
mostCurrent._am7 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1788;BA.debugLine="am8 = zeroRange";
mostCurrent._am8 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1789;BA.debugLine="am9 = zeroRange";
mostCurrent._am9 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1790;BA.debugLine="am10 = zeroRange";
mostCurrent._am10 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1791;BA.debugLine="am11 = zeroRange";
mostCurrent._am11 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1792;BA.debugLine="pm12 = zeroRange";
mostCurrent._pm12 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1793;BA.debugLine="pm1 = zeroRange";
mostCurrent._pm1 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1794;BA.debugLine="pm2 = zeroRange";
mostCurrent._pm2 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1795;BA.debugLine="pm3 = zeroRange";
mostCurrent._pm3 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1796;BA.debugLine="pm4 = zeroRange";
mostCurrent._pm4 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1797;BA.debugLine="pm5 = zeroRange";
mostCurrent._pm5 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1798;BA.debugLine="pm6 = zeroRange";
mostCurrent._pm6 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1799;BA.debugLine="pm7 = zeroRange";
mostCurrent._pm7 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1800;BA.debugLine="pm8 = zeroRange";
mostCurrent._pm8 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1801;BA.debugLine="pm9 = zeroRange";
mostCurrent._pm9 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1802;BA.debugLine="pm10 = zeroRange";
mostCurrent._pm10 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1803;BA.debugLine="pm11 = zeroRange";
mostCurrent._pm11 = BA.NumberToString(_zerorange);
 //BA.debugLineNum = 1805;BA.debugLine="Now = DateTime.Now";
_now = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 1806;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 1807;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 1808;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 1810;BA.debugLine="If fileDay = \"Today\" Then";
if ((_fileday).equals("Today")) { 
 //BA.debugLineNum = 1811;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\"";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 }else {
 //BA.debugLineNum = 1813;BA.debugLine="Now = DateTime.add(DateTime.Now, 0, 0, -1)";
_now = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1));
 //BA.debugLineNum = 1814;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 1815;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 1816;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 1817;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\"";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 };
 //BA.debugLineNum = 1820;BA.debugLine="shared = rp.GetSafeDirDefaultExternal(\"\")";
_shared = _rp.GetSafeDirDefaultExternal("");
 //BA.debugLineNum = 1821;BA.debugLine="TextReader1.Initialize(File.OpenInput(shared, Fi";
_textreader1.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(_shared,_filename).getObject()));
 //BA.debugLineNum = 1822;BA.debugLine="Dim line As String";
_line = "";
 //BA.debugLineNum = 1823;BA.debugLine="line = TextReader1.ReadLine";
_line = _textreader1.ReadLine();
 //BA.debugLineNum = 1824;BA.debugLine="Do While line <> Null";
while (_line!= null) {
 //BA.debugLineNum = 1826;BA.debugLine="line = TextReader1.ReadLine";
_line = _textreader1.ReadLine();
 //BA.debugLineNum = 1827;BA.debugLine="If line = Null Then";
if (_line== null) { 
 //BA.debugLineNum = 1828;BA.debugLine="Exit";
if (true) break;
 };
 //BA.debugLineNum = 1830;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",line)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_line);
 //BA.debugLineNum = 1831;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 1832;BA.debugLine="Dim timeStamp As String";
_timestamp = "";
 //BA.debugLineNum = 1833;BA.debugLine="timeStamp = a(0).SubString2(0,5)";
_timestamp = _a[(int) (0)].substring((int) (0),(int) (5));
 //BA.debugLineNum = 1835;BA.debugLine="If IsNumber(a(1)) = False Then Continue";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (1)])==anywheresoftware.b4a.keywords.Common.False) { 
if (true) continue;};
 //BA.debugLineNum = 1837;BA.debugLine="Select timeStamp";
switch (BA.switchObjectToInt(_timestamp,mostCurrent._timearray[(int) (0)],mostCurrent._timearray[(int) (1)],mostCurrent._timearray[(int) (2)],mostCurrent._timearray[(int) (3)],mostCurrent._timearray[(int) (4)],mostCurrent._timearray[(int) (5)],mostCurrent._timearray[(int) (6)],mostCurrent._timearray[(int) (7)],mostCurrent._timearray[(int) (8)],mostCurrent._timearray[(int) (9)],mostCurrent._timearray[(int) (10)],mostCurrent._timearray[(int) (11)],mostCurrent._timearray[(int) (12)],mostCurrent._timearray[(int) (13)],mostCurrent._timearray[(int) (14)],mostCurrent._timearray[(int) (15)],mostCurrent._timearray[(int) (16)],mostCurrent._timearray[(int) (17)],mostCurrent._timearray[(int) (18)],mostCurrent._timearray[(int) (19)],mostCurrent._timearray[(int) (20)],mostCurrent._timearray[(int) (21)],mostCurrent._timearray[(int) (22)],mostCurrent._timearray[(int) (23)])) {
case 0: {
 //BA.debugLineNum = 1839;BA.debugLine="If am12 = zeroRange Or am12 = \"\" Then am12 =";
if ((mostCurrent._am12).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am12).equals("")) { 
mostCurrent._am12 = _a[(int) (1)];};
 break; }
case 1: {
 //BA.debugLineNum = 1841;BA.debugLine="If am1 = zeroRange Or am1 = \"\" Then am1 = a(";
if ((mostCurrent._am1).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am1).equals("")) { 
mostCurrent._am1 = _a[(int) (1)];};
 break; }
case 2: {
 //BA.debugLineNum = 1843;BA.debugLine="If am2 = zeroRange Or am2 = \"\" Then am2 = a(";
if ((mostCurrent._am2).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am2).equals("")) { 
mostCurrent._am2 = _a[(int) (1)];};
 break; }
case 3: {
 //BA.debugLineNum = 1845;BA.debugLine="If am3 = zeroRange Or am3 = \"\" Then am3 = a(";
if ((mostCurrent._am3).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am3).equals("")) { 
mostCurrent._am3 = _a[(int) (1)];};
 break; }
case 4: {
 //BA.debugLineNum = 1847;BA.debugLine="If am4 = zeroRange Or am4 = \"\" Then am4 = a(";
if ((mostCurrent._am4).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am4).equals("")) { 
mostCurrent._am4 = _a[(int) (1)];};
 break; }
case 5: {
 //BA.debugLineNum = 1849;BA.debugLine="If am5 = zeroRange Or am5 = \"\" Then am5 = a(";
if ((mostCurrent._am5).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am5).equals("")) { 
mostCurrent._am5 = _a[(int) (1)];};
 break; }
case 6: {
 //BA.debugLineNum = 1851;BA.debugLine="If am6 = zeroRange Or am6 = \"\" Then am6 = a(";
if ((mostCurrent._am6).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am6).equals("")) { 
mostCurrent._am6 = _a[(int) (1)];};
 break; }
case 7: {
 //BA.debugLineNum = 1853;BA.debugLine="If am7 = zeroRange Or am7 = \"\" Then am7 = a(";
if ((mostCurrent._am7).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am7).equals("")) { 
mostCurrent._am7 = _a[(int) (1)];};
 break; }
case 8: {
 //BA.debugLineNum = 1855;BA.debugLine="If am8 = zeroRange Or am8 = \"\" Then am8 = a(";
if ((mostCurrent._am8).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am8).equals("")) { 
mostCurrent._am8 = _a[(int) (1)];};
 break; }
case 9: {
 //BA.debugLineNum = 1857;BA.debugLine="If am9 = zeroRange Or am9 = \"\" Then am9 = a(";
if ((mostCurrent._am9).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am9).equals("")) { 
mostCurrent._am9 = _a[(int) (1)];};
 break; }
case 10: {
 //BA.debugLineNum = 1859;BA.debugLine="If am10 = zeroRange Or am10 = \"\" Then am10 =";
if ((mostCurrent._am10).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am10).equals("")) { 
mostCurrent._am10 = _a[(int) (1)];};
 break; }
case 11: {
 //BA.debugLineNum = 1861;BA.debugLine="If am11 = zeroRange Or am11 = \"\" Then am11 =";
if ((mostCurrent._am11).equals(BA.NumberToString(_zerorange)) || (mostCurrent._am11).equals("")) { 
mostCurrent._am11 = _a[(int) (1)];};
 break; }
case 12: {
 //BA.debugLineNum = 1863;BA.debugLine="If pm12 = zeroRange Or pm12 = \"\" Then pm12 =";
if ((mostCurrent._pm12).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm12).equals("")) { 
mostCurrent._pm12 = _a[(int) (1)];};
 break; }
case 13: {
 //BA.debugLineNum = 1865;BA.debugLine="If pm1 = zeroRange Or pm1 = \"\" Then pm1 = a(";
if ((mostCurrent._pm1).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm1).equals("")) { 
mostCurrent._pm1 = _a[(int) (1)];};
 break; }
case 14: {
 //BA.debugLineNum = 1867;BA.debugLine="If pm2 = zeroRange Or pm2 = \"\" Then pm2 = a(";
if ((mostCurrent._pm2).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm2).equals("")) { 
mostCurrent._pm2 = _a[(int) (1)];};
 break; }
case 15: {
 //BA.debugLineNum = 1869;BA.debugLine="If pm3 = zeroRange Or pm3 = \"\" Then pm3 = a(";
if ((mostCurrent._pm3).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm3).equals("")) { 
mostCurrent._pm3 = _a[(int) (1)];};
 break; }
case 16: {
 //BA.debugLineNum = 1871;BA.debugLine="If pm4 = zeroRange Or pm4 = \"\" Then pm4 = a(";
if ((mostCurrent._pm4).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm4).equals("")) { 
mostCurrent._pm4 = _a[(int) (1)];};
 break; }
case 17: {
 //BA.debugLineNum = 1873;BA.debugLine="If pm5 = zeroRange Or pm5 = \"\" Then pm5 = a(";
if ((mostCurrent._pm5).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm5).equals("")) { 
mostCurrent._pm5 = _a[(int) (1)];};
 break; }
case 18: {
 //BA.debugLineNum = 1875;BA.debugLine="If pm6 = zeroRange Or pm6 = \"\" Then pm6 = a(";
if ((mostCurrent._pm6).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm6).equals("")) { 
mostCurrent._pm6 = _a[(int) (1)];};
 break; }
case 19: {
 //BA.debugLineNum = 1877;BA.debugLine="If pm7 = zeroRange Or pm7 = \"\" Then pm7 = a(";
if ((mostCurrent._pm7).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm7).equals("")) { 
mostCurrent._pm7 = _a[(int) (1)];};
 break; }
case 20: {
 //BA.debugLineNum = 1879;BA.debugLine="If pm8 = zeroRange Or pm8 = \"\" Then pm8 = a(";
if ((mostCurrent._pm8).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm8).equals("")) { 
mostCurrent._pm8 = _a[(int) (1)];};
 break; }
case 21: {
 //BA.debugLineNum = 1881;BA.debugLine="If pm9 = zeroRange Or pm9 = \"\" Then pm9 = a(";
if ((mostCurrent._pm9).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm9).equals("")) { 
mostCurrent._pm9 = _a[(int) (1)];};
 break; }
case 22: {
 //BA.debugLineNum = 1883;BA.debugLine="If pm10 = zeroRange Or pm10 = \"\" Then pm10 =";
if ((mostCurrent._pm10).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm10).equals("")) { 
mostCurrent._pm10 = _a[(int) (1)];};
 break; }
case 23: {
 //BA.debugLineNum = 1885;BA.debugLine="If pm11 = zeroRange Or pm11 = \"\" Then pm11 =";
if ((mostCurrent._pm11).equals(BA.NumberToString(_zerorange)) || (mostCurrent._pm11).equals("")) { 
mostCurrent._pm11 = _a[(int) (1)];};
 break; }
}
;
 //BA.debugLineNum = 1887;BA.debugLine="If fileDay = \"Today\" Then";
if ((_fileday).equals("Today")) { 
 //BA.debugLineNum = 1888;BA.debugLine="tempRightNow = a(1)";
mostCurrent._temprightnow = _a[(int) (1)];
 //BA.debugLineNum = 1889;BA.debugLine="DateTime.TimeFormat = \"h:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("h:mm a");
 };
 };
 }
;
 //BA.debugLineNum = 1894;BA.debugLine="TextReader1.Close";
_textreader1.Close();
 } 
       catch (Exception e117) {
			processBA.setLastException(e117); //BA.debugLineNum = 1898;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("63473535",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1900;BA.debugLine="End Sub";
return "";
}
public static String  _temperaturedailycreate() throws Exception{
anywheresoftware.b4a.keywords.LayoutValues _lv = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
long _yesterday = 0L;
 //BA.debugLineNum = 661;BA.debugLine="Private Sub TemperatureDailyCreate()";
 //BA.debugLineNum = 662;BA.debugLine="Try";
try { //BA.debugLineNum = 664;BA.debugLine="Activity_WindowFocusChanged(True)";
_activity_windowfocuschanged(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 665;BA.debugLine="Dim lv As LayoutValues = GetRealSize";
_lv = _getrealsize();
 //BA.debugLineNum = 666;BA.debugLine="Dim jo As JavaObject = Activity";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._activity.getObject()));
 //BA.debugLineNum = 667;BA.debugLine="jo.RunMethod(\"setBottom\", Array(lv.Height))";
_jo.RunMethod("setBottom",new Object[]{(Object)(_lv.Height)});
 //BA.debugLineNum = 668;BA.debugLine="jo.RunMethod(\"setRight\", Array(lv.Width))";
_jo.RunMethod("setRight",new Object[]{(Object)(_lv.Width)});
 //BA.debugLineNum = 669;BA.debugLine="Activity.Height = lv.Height";
mostCurrent._activity.setHeight(_lv.Height);
 //BA.debugLineNum = 670;BA.debugLine="Activity.Width = lv.Width";
mostCurrent._activity.setWidth(_lv.Width);
 //BA.debugLineNum = 673;BA.debugLine="Activity.LoadLayout(\"chart\")";
mostCurrent._activity.LoadLayout("chart",mostCurrent.activityBA);
 //BA.debugLineNum = 675;BA.debugLine="LineChart.GraphBackgroundColor = Colors.DarkGray";
mostCurrent._linechart.setGraphBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 676;BA.debugLine="LineChart.GraphFrameColor = Colors.Blue";
mostCurrent._linechart.setGraphFrameColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 677;BA.debugLine="LineChart.GraphFrameWidth = 4.0";
mostCurrent._linechart.setGraphFrameWidth((float) (4.0));
 //BA.debugLineNum = 678;BA.debugLine="LineChart.GraphPlotAreaBackgroundColor = Colors.";
mostCurrent._linechart.setGraphPlotAreaBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (50),(int) (0),(int) (0),(int) (255)));
 //BA.debugLineNum = 679;BA.debugLine="LineChart.GraphTitleTextSize = 15";
mostCurrent._linechart.setGraphTitleTextSize((int) (15));
 //BA.debugLineNum = 680;BA.debugLine="LineChart.GraphTitleColor = Colors.White";
mostCurrent._linechart.setGraphTitleColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 681;BA.debugLine="LineChart.GraphTitleSkewX = -0.25";
mostCurrent._linechart.setGraphTitleSkewX((float) (-0.25));
 //BA.debugLineNum = 682;BA.debugLine="LineChart.GraphTitleUnderline = True";
mostCurrent._linechart.setGraphTitleUnderline(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 683;BA.debugLine="LineChart.GraphTitleBold = True";
mostCurrent._linechart.setGraphTitleBold(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 684;BA.debugLine="LineChart.GraphTitle = \"TEMPERATURE DAILY  \"";
mostCurrent._linechart.setGraphTitle("TEMPERATURE DAILY  ");
 //BA.debugLineNum = 686;BA.debugLine="LineChart.LegendBackgroundColor = Colors.White";
mostCurrent._linechart.setLegendBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 687;BA.debugLine="LineChart.LegendTextColor = Colors.Black";
mostCurrent._linechart.setLegendTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 688;BA.debugLine="LineChart.LegendTextSize = 18.0";
mostCurrent._linechart.setLegendTextSize((float) (18.0));
 //BA.debugLineNum = 690;BA.debugLine="DateTime.TimeFormat = \"h:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("h:mm a");
 //BA.debugLineNum = 691;BA.debugLine="LineChart.DomianLabel = \"The time now is: \" & Da";
mostCurrent._linechart.setDomianLabel("The time now is: "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 692;BA.debugLine="LineChart.DomainLabelColor = Colors.Green";
mostCurrent._linechart.setDomainLabelColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 693;BA.debugLine="LineChart.DomainLabelTextSize = 25.0";
mostCurrent._linechart.setDomainLabelTextSize((float) (25.0));
 //BA.debugLineNum = 695;BA.debugLine="LineChart.XaxisGridLineColor = Colors.ARGB(100,2";
mostCurrent._linechart.setXaxisGridLineColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 696;BA.debugLine="LineChart.XaxisGridLineWidth = 2.0";
mostCurrent._linechart.setXaxisGridLineWidth((float) (2.0));
 //BA.debugLineNum = 697;BA.debugLine="LineChart.XaxisLabelTicks = 1";
mostCurrent._linechart.setXaxisLabelTicks((int) (1));
 //BA.debugLineNum = 698;BA.debugLine="LineChart.XaxisLabelOrientation = 0";
mostCurrent._linechart.setXaxisLabelOrientation((float) (0));
 //BA.debugLineNum = 699;BA.debugLine="LineChart.XaxisLabelTextColor = Colors.White";
mostCurrent._linechart.setXaxisLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 700;BA.debugLine="LineChart.XaxisLabelTextSize = 32.0";
mostCurrent._linechart.setXaxisLabelTextSize((float) (32.0));
 //BA.debugLineNum = 701;BA.debugLine="LineChart.XAxisLabels = Array As String(\"12 am\",";
mostCurrent._linechart.setXAxisLabels(new String[]{"12 am","1 am","2 am","3 am","4 am","5 am","6 am","7 am","8 am","9 am","10 am","11 am","12 pm","1 pm","2 pm","3 pm","4 pm","5 pm","6 pm","7 pm","8 pm","9 pm","10 pm","11 pm"});
 //BA.debugLineNum = 703;BA.debugLine="LineChart.YaxisDivisions = 10";
mostCurrent._linechart.setYaxisDivisions((int) (10));
 //BA.debugLineNum = 705;BA.debugLine="LineChart.YaxisValueFormat = LineChart.ValueForm";
mostCurrent._linechart.setYaxisValueFormat(mostCurrent._linechart.ValueFormat_2);
 //BA.debugLineNum = 706;BA.debugLine="LineChart.YaxisGridLineColor = Colors.Black";
mostCurrent._linechart.setYaxisGridLineColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 707;BA.debugLine="LineChart.YaxisGridLineWidth = 2";
mostCurrent._linechart.setYaxisGridLineWidth((float) (2));
 //BA.debugLineNum = 708;BA.debugLine="LineChart.YaxisLabelTicks = 1";
mostCurrent._linechart.setYaxisLabelTicks((int) (1));
 //BA.debugLineNum = 709;BA.debugLine="LineChart.YaxisLabelColor = Colors.Yellow";
mostCurrent._linechart.setYaxisLabelColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 710;BA.debugLine="LineChart.YaxisLabelOrientation = -30";
mostCurrent._linechart.setYaxisLabelOrientation((float) (-30));
 //BA.debugLineNum = 711;BA.debugLine="LineChart.YaxisLabelTextSize = 25.0";
mostCurrent._linechart.setYaxisLabelTextSize((float) (25.0));
 //BA.debugLineNum = 712;BA.debugLine="LineChart.YaxisTitleColor = Colors.Green";
mostCurrent._linechart.setYaxisTitleColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 713;BA.debugLine="LineChart.YaxisTitleFakeBold = False";
mostCurrent._linechart.setYaxisTitleFakeBold(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 714;BA.debugLine="LineChart.YaxisTitleTextSize = 20.0";
mostCurrent._linechart.setYaxisTitleTextSize((float) (20.0));
 //BA.debugLineNum = 715;BA.debugLine="LineChart.YaxisTitleUnderline = True";
mostCurrent._linechart.setYaxisTitleUnderline(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 716;BA.debugLine="LineChart.YaxisTitleTextSkewness = 0";
mostCurrent._linechart.setYaxisTitleTextSkewness((float) (0));
 //BA.debugLineNum = 717;BA.debugLine="LineChart.YaxisLabelAndTitleDistance = 60.0";
mostCurrent._linechart.setYaxisLabelAndTitleDistance((float) (60.0));
 //BA.debugLineNum = 718;BA.debugLine="LineChart.YaxisTitle = \"Temperature (Fahrenheit)";
mostCurrent._linechart.setYaxisTitle("Temperature (Fahrenheit)");
 //BA.debugLineNum = 720;BA.debugLine="LineChart.MaxNumberOfEntriesPerLineChart = 24";
mostCurrent._linechart.setMaxNumberOfEntriesPerLineChart((int) (24));
 //BA.debugLineNum = 721;BA.debugLine="LineChart.GraphLegendVisibility = False";
mostCurrent._linechart.setGraphLegendVisibility(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 725;BA.debugLine="ReadTemperatureDaily(\"Today\")";
_readtemperaturedaily("Today");
 //BA.debugLineNum = 727;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy");
 //BA.debugLineNum = 728;BA.debugLine="LineChart.Line_1_LegendText = \"Today, \" & DateTi";
mostCurrent._linechart.setLine_1_LegendText("Today, "+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 730;BA.debugLine="CheckTempBoundariesDaily";
_checktempboundariesdaily();
 //BA.debugLineNum = 732;BA.debugLine="If am12 <> tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 733;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12)";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12))});
 };
 //BA.debugLineNum = 735;BA.debugLine="If am1 <> tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 736;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1))});
 };
 //BA.debugLineNum = 738;BA.debugLine="If am2 <> tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 739;BA.debugLine="If am1 = tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 740;BA.debugLine="am1 = (am12 + am2)/2";
mostCurrent._am1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am12))+(double)(Double.parseDouble(mostCurrent._am2)))/(double)2);
 };
 //BA.debugLineNum = 742;BA.debugLine="If am12 = tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 743;BA.debugLine="am12 = am1";
mostCurrent._am12 = mostCurrent._am1;
 };
 //BA.debugLineNum = 745;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2))});
 };
 //BA.debugLineNum = 747;BA.debugLine="If am3 <> tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 748;BA.debugLine="If am2 = tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 749;BA.debugLine="am2 = (am1 + am3)/2";
mostCurrent._am2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am1))+(double)(Double.parseDouble(mostCurrent._am3)))/(double)2);
 };
 //BA.debugLineNum = 751;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3))});
 };
 //BA.debugLineNum = 753;BA.debugLine="If am4 <> tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 754;BA.debugLine="If am3 = tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 755;BA.debugLine="am3 = (am2 + am4)/2";
mostCurrent._am3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am2))+(double)(Double.parseDouble(mostCurrent._am4)))/(double)2);
 };
 //BA.debugLineNum = 757;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4))});
 };
 //BA.debugLineNum = 759;BA.debugLine="If am5 <> tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 760;BA.debugLine="If am4 = tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 761;BA.debugLine="am4 = (am3 + am5)/2";
mostCurrent._am4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am3))+(double)(Double.parseDouble(mostCurrent._am5)))/(double)2);
 };
 //BA.debugLineNum = 763;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5))});
 };
 //BA.debugLineNum = 765;BA.debugLine="If am6 <> tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 766;BA.debugLine="If am5 = tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 767;BA.debugLine="am5 = (am4 + am6)/2";
mostCurrent._am5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am4))+(double)(Double.parseDouble(mostCurrent._am6)))/(double)2);
 };
 //BA.debugLineNum = 769;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6))});
 };
 //BA.debugLineNum = 771;BA.debugLine="If am7 <> tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 772;BA.debugLine="If am6 = tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 773;BA.debugLine="am6 = (am5 + am7)/2";
mostCurrent._am6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am5))+(double)(Double.parseDouble(mostCurrent._am7)))/(double)2);
 };
 //BA.debugLineNum = 775;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7))});
 };
 //BA.debugLineNum = 777;BA.debugLine="If am8 <> tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 778;BA.debugLine="If am7 = tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 779;BA.debugLine="am7 = (am6 + am8)/2";
mostCurrent._am7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am6))+(double)(Double.parseDouble(mostCurrent._am8)))/(double)2);
 };
 //BA.debugLineNum = 781;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8))});
 };
 //BA.debugLineNum = 783;BA.debugLine="If am9 <> tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 784;BA.debugLine="If am8 = tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 785;BA.debugLine="am8 = (am7 + am9)/2";
mostCurrent._am8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am7))+(double)(Double.parseDouble(mostCurrent._am9)))/(double)2);
 };
 //BA.debugLineNum = 787;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9))});
 };
 //BA.debugLineNum = 789;BA.debugLine="If am10 <> tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 790;BA.debugLine="If am9 = tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 791;BA.debugLine="am9 = (am8 + am10)/2";
mostCurrent._am9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am8))+(double)(Double.parseDouble(mostCurrent._am10)))/(double)2);
 };
 //BA.debugLineNum = 793;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10))});
 };
 //BA.debugLineNum = 795;BA.debugLine="If am11 <> tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 796;BA.debugLine="If am10 = tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 797;BA.debugLine="am10 = (am9 + am11)/2";
mostCurrent._am10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am9))+(double)(Double.parseDouble(mostCurrent._am11)))/(double)2);
 };
 //BA.debugLineNum = 799;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11))});
 };
 //BA.debugLineNum = 801;BA.debugLine="If pm12 <> tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 802;BA.debugLine="If am11 = tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 803;BA.debugLine="am11 = (am10 + pm12)/2";
mostCurrent._am11 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am10))+(double)(Double.parseDouble(mostCurrent._pm12)))/(double)2);
 };
 //BA.debugLineNum = 805;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12))});
 };
 //BA.debugLineNum = 807;BA.debugLine="If pm1 <> tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 808;BA.debugLine="If pm12 = tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 809;BA.debugLine="pm12 = (am11 + pm1)/2";
mostCurrent._pm12 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am11))+(double)(Double.parseDouble(mostCurrent._pm1)))/(double)2);
 };
 //BA.debugLineNum = 811;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1))});
 };
 //BA.debugLineNum = 813;BA.debugLine="If pm2 <> tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 814;BA.debugLine="If pm1 = tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 815;BA.debugLine="pm1 = (pm12 + pm2)/2";
mostCurrent._pm1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm12))+(double)(Double.parseDouble(mostCurrent._pm2)))/(double)2);
 };
 //BA.debugLineNum = 817;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2))});
 };
 //BA.debugLineNum = 819;BA.debugLine="If pm3 <> tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 820;BA.debugLine="If pm2 = tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 821;BA.debugLine="pm2 = (pm1 + pm3)/2";
mostCurrent._pm2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm1))+(double)(Double.parseDouble(mostCurrent._pm3)))/(double)2);
 };
 //BA.debugLineNum = 823;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3))});
 };
 //BA.debugLineNum = 825;BA.debugLine="If pm4 <> tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 826;BA.debugLine="If pm3 = tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 827;BA.debugLine="pm3 = (pm2 + pm4)/2";
mostCurrent._pm3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm2))+(double)(Double.parseDouble(mostCurrent._pm4)))/(double)2);
 };
 //BA.debugLineNum = 829;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4))});
 };
 //BA.debugLineNum = 831;BA.debugLine="If pm5 <> tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 832;BA.debugLine="If pm4 = tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 833;BA.debugLine="pm4 = (pm3 + pm5)/2";
mostCurrent._pm4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm3))+(double)(Double.parseDouble(mostCurrent._pm5)))/(double)2);
 };
 //BA.debugLineNum = 835;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5))});
 };
 //BA.debugLineNum = 837;BA.debugLine="If pm6 <> tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 838;BA.debugLine="If pm5 = tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 839;BA.debugLine="pm5 = (pm4 + pm6)/2";
mostCurrent._pm5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm4))+(double)(Double.parseDouble(mostCurrent._pm6)))/(double)2);
 };
 //BA.debugLineNum = 841;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6))});
 };
 //BA.debugLineNum = 843;BA.debugLine="If pm7 <> tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 844;BA.debugLine="If pm6 = tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 845;BA.debugLine="pm6 = (pm5 + pm7)/2";
mostCurrent._pm6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm5))+(double)(Double.parseDouble(mostCurrent._pm7)))/(double)2);
 };
 //BA.debugLineNum = 847;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7))});
 };
 //BA.debugLineNum = 849;BA.debugLine="If pm8 <> tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 850;BA.debugLine="If pm7 = tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 851;BA.debugLine="pm7 = (pm6 + pm8)/2";
mostCurrent._pm7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm6))+(double)(Double.parseDouble(mostCurrent._pm8)))/(double)2);
 };
 //BA.debugLineNum = 853;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8))});
 };
 //BA.debugLineNum = 855;BA.debugLine="If pm9 <> tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 856;BA.debugLine="If pm8 = tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 857;BA.debugLine="pm8 = (pm7 + pm9)/2";
mostCurrent._pm8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm7))+(double)(Double.parseDouble(mostCurrent._pm9)))/(double)2);
 };
 //BA.debugLineNum = 859;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9))});
 };
 //BA.debugLineNum = 861;BA.debugLine="If pm10 <> tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 862;BA.debugLine="If pm9 = tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 863;BA.debugLine="pm9 = (pm8 + pm10)/2";
mostCurrent._pm9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm8))+(double)(Double.parseDouble(mostCurrent._pm10)))/(double)2);
 };
 //BA.debugLineNum = 865;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10))});
 };
 //BA.debugLineNum = 867;BA.debugLine="If pm11 <> tempZeroRange Then";
if ((mostCurrent._pm11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 868;BA.debugLine="If pm10 = tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 869;BA.debugLine="pm10 = (pm9 + pm11)/2";
mostCurrent._pm10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm9))+(double)(Double.parseDouble(mostCurrent._pm11)))/(double)2);
 };
 //BA.debugLineNum = 871;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))});
 };
 //BA.debugLineNum = 874;BA.debugLine="LineChart.Line_1_PointLabelTextColor = Colors.Ye";
mostCurrent._linechart.setLine_1_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 875;BA.debugLine="LineChart.Line_1_PointLabelTextSize = 35.0";
mostCurrent._linechart.setLine_1_PointLabelTextSize((float) (35.0));
 //BA.debugLineNum = 876;BA.debugLine="LineChart.Line_1_LineColor = Colors.Red";
mostCurrent._linechart.setLine_1_LineColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 877;BA.debugLine="LineChart.Line_1_LineWidth = 11.0";
mostCurrent._linechart.setLine_1_LineWidth((float) (11.0));
 //BA.debugLineNum = 878;BA.debugLine="LineChart.Line_1_PointColor = Colors.Black";
mostCurrent._linechart.setLine_1_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 879;BA.debugLine="LineChart.Line_1_PointSize = 25.0";
mostCurrent._linechart.setLine_1_PointSize((float) (25.0));
 //BA.debugLineNum = 880;BA.debugLine="LineChart.Line_1_PointShape = LineChart.SHAPE_RO";
mostCurrent._linechart.setLine_1_PointShape(mostCurrent._linechart.SHAPE_ROUND);
 //BA.debugLineNum = 881;BA.debugLine="LineChart.Line_1_DrawDash = False";
mostCurrent._linechart.setLine_1_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 882;BA.debugLine="LineChart.Line_1_DrawCubic = False";
mostCurrent._linechart.setLine_1_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 888;BA.debugLine="ReadTemperatureDaily(\"Yesterday\")";
_readtemperaturedaily("Yesterday");
 //BA.debugLineNum = 890;BA.debugLine="Dim Yesterday As Long";
_yesterday = 0L;
 //BA.debugLineNum = 891;BA.debugLine="Yesterday = DateTime.add(DateTime.Now, 0, 0, -1)";
_yesterday = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1));
 //BA.debugLineNum = 893;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy");
 //BA.debugLineNum = 894;BA.debugLine="LineChart.Line_2_LegendText = \"Yesterday, \" & Da";
mostCurrent._linechart.setLine_2_LegendText("Yesterday, "+anywheresoftware.b4a.keywords.Common.DateTime.Date(_yesterday));
 //BA.debugLineNum = 896;BA.debugLine="CheckTempBoundariesDaily";
_checktempboundariesdaily();
 //BA.debugLineNum = 898;BA.debugLine="If am12 <> tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 899;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12)";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12))});
 };
 //BA.debugLineNum = 901;BA.debugLine="If am1 <> tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 902;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1))});
 };
 //BA.debugLineNum = 904;BA.debugLine="If am2 <> tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 905;BA.debugLine="If am1 = tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 906;BA.debugLine="am1 = (am12 + am2)/2";
mostCurrent._am1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am12))+(double)(Double.parseDouble(mostCurrent._am2)))/(double)2);
 };
 //BA.debugLineNum = 908;BA.debugLine="If am12 = tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 909;BA.debugLine="am12 = am1";
mostCurrent._am12 = mostCurrent._am1;
 };
 //BA.debugLineNum = 911;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2))});
 };
 //BA.debugLineNum = 913;BA.debugLine="If am3 <> tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 914;BA.debugLine="If am2 = tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 915;BA.debugLine="am2 = (am1 + am3)/2";
mostCurrent._am2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am1))+(double)(Double.parseDouble(mostCurrent._am3)))/(double)2);
 };
 //BA.debugLineNum = 917;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3))});
 };
 //BA.debugLineNum = 919;BA.debugLine="If am4 <> tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 920;BA.debugLine="If am3 = tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 921;BA.debugLine="am3 = (am2 + am4)/2";
mostCurrent._am3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am2))+(double)(Double.parseDouble(mostCurrent._am4)))/(double)2);
 };
 //BA.debugLineNum = 923;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4))});
 };
 //BA.debugLineNum = 925;BA.debugLine="If am5 <> tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 926;BA.debugLine="If am4 = tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 927;BA.debugLine="am4 = (am3 + am5)/2";
mostCurrent._am4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am3))+(double)(Double.parseDouble(mostCurrent._am5)))/(double)2);
 };
 //BA.debugLineNum = 929;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5))});
 };
 //BA.debugLineNum = 931;BA.debugLine="If am6 <> tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 932;BA.debugLine="If am5 = tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 933;BA.debugLine="am5 = (am4 + am6)/2";
mostCurrent._am5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am4))+(double)(Double.parseDouble(mostCurrent._am6)))/(double)2);
 };
 //BA.debugLineNum = 935;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6))});
 };
 //BA.debugLineNum = 937;BA.debugLine="If am7 <> tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 938;BA.debugLine="If am6 = tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 939;BA.debugLine="am6 = (am5 + am7)/2";
mostCurrent._am6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am5))+(double)(Double.parseDouble(mostCurrent._am7)))/(double)2);
 };
 //BA.debugLineNum = 941;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7))});
 };
 //BA.debugLineNum = 943;BA.debugLine="If am8 <> tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 944;BA.debugLine="If am7 = tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 945;BA.debugLine="am7 = (am6 + am8)/2";
mostCurrent._am7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am6))+(double)(Double.parseDouble(mostCurrent._am8)))/(double)2);
 };
 //BA.debugLineNum = 947;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8))});
 };
 //BA.debugLineNum = 949;BA.debugLine="If am9 <> tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 950;BA.debugLine="If am8 = tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 951;BA.debugLine="am8 = (am7 + am9)/2";
mostCurrent._am8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am7))+(double)(Double.parseDouble(mostCurrent._am9)))/(double)2);
 };
 //BA.debugLineNum = 953;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9))});
 };
 //BA.debugLineNum = 955;BA.debugLine="If am10 <> tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 956;BA.debugLine="If am9 = tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 957;BA.debugLine="am9 = (am8 + am10)/2";
mostCurrent._am9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am8))+(double)(Double.parseDouble(mostCurrent._am10)))/(double)2);
 };
 //BA.debugLineNum = 959;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10))});
 };
 //BA.debugLineNum = 961;BA.debugLine="If am11 <> tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 962;BA.debugLine="If am10 = tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 963;BA.debugLine="am10 = (am9 + am11)/2";
mostCurrent._am10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am9))+(double)(Double.parseDouble(mostCurrent._am11)))/(double)2);
 };
 //BA.debugLineNum = 965;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11))});
 };
 //BA.debugLineNum = 967;BA.debugLine="If pm12 <> tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 968;BA.debugLine="If am11 = tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 969;BA.debugLine="am11 = (am10 + pm12)/2";
mostCurrent._am11 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am10))+(double)(Double.parseDouble(mostCurrent._pm12)))/(double)2);
 };
 //BA.debugLineNum = 971;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12))});
 };
 //BA.debugLineNum = 973;BA.debugLine="If pm1 <> tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 974;BA.debugLine="If pm12 = tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 975;BA.debugLine="pm12 = (am11 + pm1)/2";
mostCurrent._pm12 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am11))+(double)(Double.parseDouble(mostCurrent._pm1)))/(double)2);
 };
 //BA.debugLineNum = 977;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1))});
 };
 //BA.debugLineNum = 979;BA.debugLine="If pm2 <> tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 980;BA.debugLine="If pm1 = tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 981;BA.debugLine="pm1 = (pm12 + pm2)/2";
mostCurrent._pm1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm12))+(double)(Double.parseDouble(mostCurrent._pm2)))/(double)2);
 };
 //BA.debugLineNum = 983;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2))});
 };
 //BA.debugLineNum = 985;BA.debugLine="If pm3 <> tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 986;BA.debugLine="If pm2 = tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 987;BA.debugLine="pm2 = (pm1 + pm3)/2";
mostCurrent._pm2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm1))+(double)(Double.parseDouble(mostCurrent._pm3)))/(double)2);
 };
 //BA.debugLineNum = 989;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3))});
 };
 //BA.debugLineNum = 991;BA.debugLine="If pm4 <> tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 992;BA.debugLine="If pm3 = tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 993;BA.debugLine="pm3 = (pm2 + pm4)/2";
mostCurrent._pm3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm2))+(double)(Double.parseDouble(mostCurrent._pm4)))/(double)2);
 };
 //BA.debugLineNum = 995;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4))});
 };
 //BA.debugLineNum = 997;BA.debugLine="If pm5 <> tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 998;BA.debugLine="If pm4 = tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 999;BA.debugLine="pm4 = (pm3 + pm5)/2";
mostCurrent._pm4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm3))+(double)(Double.parseDouble(mostCurrent._pm5)))/(double)2);
 };
 //BA.debugLineNum = 1001;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5))});
 };
 //BA.debugLineNum = 1003;BA.debugLine="If pm6 <> tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1004;BA.debugLine="If pm5 = tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1005;BA.debugLine="pm5 = (pm4 + pm6)/2";
mostCurrent._pm5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm4))+(double)(Double.parseDouble(mostCurrent._pm6)))/(double)2);
 };
 //BA.debugLineNum = 1007;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6))});
 };
 //BA.debugLineNum = 1009;BA.debugLine="If pm7 <> tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1010;BA.debugLine="If pm6 = tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1011;BA.debugLine="pm6 = (pm5 + pm7)/2";
mostCurrent._pm6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm5))+(double)(Double.parseDouble(mostCurrent._pm7)))/(double)2);
 };
 //BA.debugLineNum = 1013;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7))});
 };
 //BA.debugLineNum = 1015;BA.debugLine="If pm8 <> tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1016;BA.debugLine="If pm7 = tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1017;BA.debugLine="pm7 = (pm6 + pm8)/2";
mostCurrent._pm7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm6))+(double)(Double.parseDouble(mostCurrent._pm8)))/(double)2);
 };
 //BA.debugLineNum = 1019;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8))});
 };
 //BA.debugLineNum = 1021;BA.debugLine="If pm9 <> tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1022;BA.debugLine="If pm8 = tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1023;BA.debugLine="pm8 = (pm7 + pm9)/2";
mostCurrent._pm8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm7))+(double)(Double.parseDouble(mostCurrent._pm9)))/(double)2);
 };
 //BA.debugLineNum = 1025;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9))});
 };
 //BA.debugLineNum = 1027;BA.debugLine="If pm10 <> tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1028;BA.debugLine="If pm9 = tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1029;BA.debugLine="pm9 = (pm8 + pm10)/2";
mostCurrent._pm9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm8))+(double)(Double.parseDouble(mostCurrent._pm10)))/(double)2);
 };
 //BA.debugLineNum = 1031;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10))});
 };
 //BA.debugLineNum = 1033;BA.debugLine="If pm11 <> tempZeroRange Then";
if ((mostCurrent._pm11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 1034;BA.debugLine="If pm10 = tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 1035;BA.debugLine="pm10 = (pm9 + pm11)/2";
mostCurrent._pm10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm9))+(double)(Double.parseDouble(mostCurrent._pm11)))/(double)2);
 };
 //BA.debugLineNum = 1037;BA.debugLine="LineChart.Line_2_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))});
 };
 //BA.debugLineNum = 1040;BA.debugLine="LineChart.Line_2_PointLabelTextColor = Colors.Cy";
mostCurrent._linechart.setLine_2_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 1041;BA.debugLine="LineChart.Line_2_PointLabelTextSize = 35.0";
mostCurrent._linechart.setLine_2_PointLabelTextSize((float) (35.0));
 //BA.debugLineNum = 1042;BA.debugLine="LineChart.Line_2_LineColor = Colors.White";
mostCurrent._linechart.setLine_2_LineColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1043;BA.debugLine="LineChart.Line_2_LineWidth = 7.0";
mostCurrent._linechart.setLine_2_LineWidth((float) (7.0));
 //BA.debugLineNum = 1044;BA.debugLine="LineChart.Line_2_PointColor = Colors.Cyan";
mostCurrent._linechart.setLine_2_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 1045;BA.debugLine="LineChart.Line_2_PointSize = 10.0";
mostCurrent._linechart.setLine_2_PointSize((float) (10.0));
 //BA.debugLineNum = 1046;BA.debugLine="LineChart.Line_2_PointShape = LineChart.SHAPE_RO";
mostCurrent._linechart.setLine_2_PointShape(mostCurrent._linechart.SHAPE_ROUND);
 //BA.debugLineNum = 1047;BA.debugLine="LineChart.Line_2_DrawDash = False";
mostCurrent._linechart.setLine_2_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1048;BA.debugLine="LineChart.Line_2_DrawCubic = False";
mostCurrent._linechart.setLine_2_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1054;BA.debugLine="LineChart.Line_3_LegendText = \"Real time\"";
mostCurrent._linechart.setLine_3_LegendText("Real time");
 //BA.debugLineNum = 1055;BA.debugLine="LineChart.Line_3_Data = Array As Float (tempRigh";
mostCurrent._linechart.setLine_3_Data(new float[]{(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow))});
 //BA.debugLineNum = 1056;BA.debugLine="LineChart.Line_3_PointLabelTextColor = Colors.Gr";
mostCurrent._linechart.setLine_3_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 1057;BA.debugLine="LineChart.Line_3_PointLabelTextSize = 30.0";
mostCurrent._linechart.setLine_3_PointLabelTextSize((float) (30.0));
 //BA.debugLineNum = 1058;BA.debugLine="LineChart.Line_3_LineColor = Colors.Green";
mostCurrent._linechart.setLine_3_LineColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 1059;BA.debugLine="LineChart.Line_3_LineWidth = 5.0";
mostCurrent._linechart.setLine_3_LineWidth((float) (5.0));
 //BA.debugLineNum = 1060;BA.debugLine="LineChart.Line_3_PointColor = Colors.Green";
mostCurrent._linechart.setLine_3_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 1061;BA.debugLine="LineChart.Line_3_PointSize = 1.0";
mostCurrent._linechart.setLine_3_PointSize((float) (1.0));
 //BA.debugLineNum = 1062;BA.debugLine="LineChart.Line_3_PointShape = LineChart.SHAPE_RO";
mostCurrent._linechart.setLine_3_PointShape(mostCurrent._linechart.SHAPE_ROUND);
 //BA.debugLineNum = 1063;BA.debugLine="LineChart.Line_3_DrawDash = False";
mostCurrent._linechart.setLine_3_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1064;BA.debugLine="LineChart.Line_3_DrawCubic = False";
mostCurrent._linechart.setLine_3_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1068;BA.debugLine="LineChart.NumberOfLineCharts = 3";
mostCurrent._linechart.setNumberOfLineCharts((int) (3));
 //BA.debugLineNum = 1070;BA.debugLine="LineChart.DrawTheGraphs";
mostCurrent._linechart.DrawTheGraphs();
 } 
       catch (Exception e375) {
			processBA.setLastException(e375); //BA.debugLineNum = 1073;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("63146140",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1074;BA.debugLine="ToastMessageShow (LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 1076;BA.debugLine="End Sub";
return "";
}
public static String  _temperaturedailytimer_tick() throws Exception{
 //BA.debugLineNum = 2211;BA.debugLine="Sub TemperatureDailyTimer_Tick";
 //BA.debugLineNum = 2212;BA.debugLine="Activity.RequestFocus";
mostCurrent._activity.RequestFocus();
 //BA.debugLineNum = 2213;BA.debugLine="btnHumidityHourly.RemoveView";
mostCurrent._btnhumidityhourly.RemoveView();
 //BA.debugLineNum = 2214;BA.debugLine="btnTempHourly.RemoveView";
mostCurrent._btntemphourly.RemoveView();
 //BA.debugLineNum = 2215;BA.debugLine="btnHumidityDaily.RemoveView";
mostCurrent._btnhumiditydaily.RemoveView();
 //BA.debugLineNum = 2216;BA.debugLine="btnTempDaily.RemoveView";
mostCurrent._btntempdaily.RemoveView();
 //BA.debugLineNum = 2217;BA.debugLine="LineChart.RemoveView";
mostCurrent._linechart.RemoveView();
 //BA.debugLineNum = 2218;BA.debugLine="tempMaxRange=0";
_tempmaxrange = (float) (0);
 //BA.debugLineNum = 2219;BA.debugLine="tempMinRange=0";
_tempminrange = (float) (0);
 //BA.debugLineNum = 2220;BA.debugLine="TemperatureDailyCreate";
_temperaturedailycreate();
 //BA.debugLineNum = 2221;BA.debugLine="End Sub";
return "";
}
public static String  _temperaturehourlycreate() throws Exception{
anywheresoftware.b4a.keywords.LayoutValues _lv = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
int _i = 0;
b4a.example.dateutils._period _p = null;
long _nexttime = 0L;
 //BA.debugLineNum = 125;BA.debugLine="Private Sub TemperatureHourlyCreate()";
 //BA.debugLineNum = 126;BA.debugLine="Try";
try { //BA.debugLineNum = 128;BA.debugLine="Dim lv As LayoutValues = GetRealSize";
_lv = _getrealsize();
 //BA.debugLineNum = 129;BA.debugLine="Dim jo As JavaObject = Activity";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._activity.getObject()));
 //BA.debugLineNum = 130;BA.debugLine="jo.RunMethod(\"setBottom\", Array(lv.Height))";
_jo.RunMethod("setBottom",new Object[]{(Object)(_lv.Height)});
 //BA.debugLineNum = 131;BA.debugLine="jo.RunMethod(\"setRight\", Array(lv.Width))";
_jo.RunMethod("setRight",new Object[]{(Object)(_lv.Width)});
 //BA.debugLineNum = 132;BA.debugLine="Activity.Height = lv.Height";
mostCurrent._activity.setHeight(_lv.Height);
 //BA.debugLineNum = 133;BA.debugLine="Activity.Width = lv.Width";
mostCurrent._activity.setWidth(_lv.Width);
 //BA.debugLineNum = 136;BA.debugLine="Activity.LoadLayout(\"chart\")";
mostCurrent._activity.LoadLayout("chart",mostCurrent.activityBA);
 //BA.debugLineNum = 138;BA.debugLine="LineChart.GraphBackgroundColor = Colors.DarkGray";
mostCurrent._linechart.setGraphBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 139;BA.debugLine="LineChart.GraphFrameColor = Colors.Blue";
mostCurrent._linechart.setGraphFrameColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 140;BA.debugLine="LineChart.GraphFrameWidth = 4.0";
mostCurrent._linechart.setGraphFrameWidth((float) (4.0));
 //BA.debugLineNum = 141;BA.debugLine="LineChart.GraphPlotAreaBackgroundColor = Colors.";
mostCurrent._linechart.setGraphPlotAreaBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (50),(int) (0),(int) (0),(int) (255)));
 //BA.debugLineNum = 142;BA.debugLine="LineChart.GraphTitleTextSize = 15";
mostCurrent._linechart.setGraphTitleTextSize((int) (15));
 //BA.debugLineNum = 143;BA.debugLine="LineChart.GraphTitleColor = Colors.White";
mostCurrent._linechart.setGraphTitleColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 144;BA.debugLine="LineChart.GraphTitleSkewX = -0.25";
mostCurrent._linechart.setGraphTitleSkewX((float) (-0.25));
 //BA.debugLineNum = 145;BA.debugLine="LineChart.GraphTitleUnderline = True";
mostCurrent._linechart.setGraphTitleUnderline(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 146;BA.debugLine="LineChart.GraphTitleBold = True";
mostCurrent._linechart.setGraphTitleBold(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 147;BA.debugLine="LineChart.GraphTitle = \"TEMPERATURE HOURLY  \"";
mostCurrent._linechart.setGraphTitle("TEMPERATURE HOURLY  ");
 //BA.debugLineNum = 149;BA.debugLine="LineChart.LegendBackgroundColor = Colors.White";
mostCurrent._linechart.setLegendBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 150;BA.debugLine="LineChart.LegendTextColor = Colors.Black";
mostCurrent._linechart.setLegendTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 151;BA.debugLine="LineChart.LegendTextSize = 18.0";
mostCurrent._linechart.setLegendTextSize((float) (18.0));
 //BA.debugLineNum = 153;BA.debugLine="DateTime.TimeFormat = \"h:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("h:mm a");
 //BA.debugLineNum = 154;BA.debugLine="LineChart.DomianLabel = \"The time now is: \" & Da";
mostCurrent._linechart.setDomianLabel("The time now is: "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 155;BA.debugLine="LineChart.DomainLabelColor = Colors.Green";
mostCurrent._linechart.setDomainLabelColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 156;BA.debugLine="LineChart.DomainLabelTextSize = 25.0";
mostCurrent._linechart.setDomainLabelTextSize((float) (25.0));
 //BA.debugLineNum = 158;BA.debugLine="LineChart.XaxisGridLineColor = Colors.ARGB(100,2";
mostCurrent._linechart.setXaxisGridLineColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 159;BA.debugLine="LineChart.XaxisGridLineWidth = 2.0";
mostCurrent._linechart.setXaxisGridLineWidth((float) (2.0));
 //BA.debugLineNum = 160;BA.debugLine="LineChart.XaxisLabelTicks = 1";
mostCurrent._linechart.setXaxisLabelTicks((int) (1));
 //BA.debugLineNum = 161;BA.debugLine="LineChart.XaxisLabelOrientation = 0";
mostCurrent._linechart.setXaxisLabelOrientation((float) (0));
 //BA.debugLineNum = 162;BA.debugLine="LineChart.XaxisLabelTextColor = Colors.White";
mostCurrent._linechart.setXaxisLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 163;BA.debugLine="LineChart.XaxisLabelTextSize = 32.0";
mostCurrent._linechart.setXaxisLabelTextSize((float) (32.0));
 //BA.debugLineNum = 167;BA.debugLine="timeRightNow = DateTime.Now";
_timerightnow = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 169;BA.debugLine="For i = 23 To 0 Step -1";
{
final int step33 = -1;
final int limit33 = (int) (0);
_i = (int) (23) ;
for (;_i >= limit33 ;_i = _i + step33 ) {
 //BA.debugLineNum = 170;BA.debugLine="Dim p As Period";
_p = new b4a.example.dateutils._period();
 //BA.debugLineNum = 171;BA.debugLine="p.Hours = 0";
_p.Hours = (int) (0);
 //BA.debugLineNum = 172;BA.debugLine="p.Minutes = (i+1) * -5";
_p.Minutes = (int) ((_i+1)*-5);
 //BA.debugLineNum = 173;BA.debugLine="p.Seconds = 0";
_p.Seconds = (int) (0);
 //BA.debugLineNum = 174;BA.debugLine="Dim NextTime As Long";
_nexttime = 0L;
 //BA.debugLineNum = 175;BA.debugLine="NextTime = DateUtils.AddPeriod(timeRightNow, p)";
_nexttime = mostCurrent._dateutils._addperiod(mostCurrent.activityBA,_timerightnow,_p);
 //BA.debugLineNum = 176;BA.debugLine="DateTime.TimeFormat = \"HH:mm\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("HH:mm");
 //BA.debugLineNum = 178;BA.debugLine="timeArray(23-i) = DateTime.Time(NextTime) 'Date";
mostCurrent._timearray[(int) (23-_i)] = anywheresoftware.b4a.keywords.Common.DateTime.Time(_nexttime);
 }
};
 //BA.debugLineNum = 180;BA.debugLine="LineChart.XAxisLabels = timeArray 'Array As Stri";
mostCurrent._linechart.setXAxisLabels(mostCurrent._timearray);
 //BA.debugLineNum = 183;BA.debugLine="LineChart.YaxisDivisions = 10";
mostCurrent._linechart.setYaxisDivisions((int) (10));
 //BA.debugLineNum = 185;BA.debugLine="LineChart.YaxisValueFormat = LineChart.ValueForm";
mostCurrent._linechart.setYaxisValueFormat(mostCurrent._linechart.ValueFormat_2);
 //BA.debugLineNum = 186;BA.debugLine="LineChart.YaxisGridLineColor = Colors.Black";
mostCurrent._linechart.setYaxisGridLineColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 187;BA.debugLine="LineChart.YaxisGridLineWidth = 2";
mostCurrent._linechart.setYaxisGridLineWidth((float) (2));
 //BA.debugLineNum = 188;BA.debugLine="LineChart.YaxisLabelTicks = 1";
mostCurrent._linechart.setYaxisLabelTicks((int) (1));
 //BA.debugLineNum = 189;BA.debugLine="LineChart.YaxisLabelColor = Colors.Yellow";
mostCurrent._linechart.setYaxisLabelColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 190;BA.debugLine="LineChart.YaxisLabelOrientation = -30";
mostCurrent._linechart.setYaxisLabelOrientation((float) (-30));
 //BA.debugLineNum = 191;BA.debugLine="LineChart.YaxisLabelTextSize = 25.0";
mostCurrent._linechart.setYaxisLabelTextSize((float) (25.0));
 //BA.debugLineNum = 192;BA.debugLine="LineChart.YaxisTitleColor = Colors.Green";
mostCurrent._linechart.setYaxisTitleColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 193;BA.debugLine="LineChart.YaxisTitleFakeBold = False";
mostCurrent._linechart.setYaxisTitleFakeBold(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 194;BA.debugLine="LineChart.YaxisTitleTextSize = 20.0";
mostCurrent._linechart.setYaxisTitleTextSize((float) (20.0));
 //BA.debugLineNum = 195;BA.debugLine="LineChart.YaxisTitleUnderline = True";
mostCurrent._linechart.setYaxisTitleUnderline(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 196;BA.debugLine="LineChart.YaxisTitleTextSkewness = 0";
mostCurrent._linechart.setYaxisTitleTextSkewness((float) (0));
 //BA.debugLineNum = 197;BA.debugLine="LineChart.YaxisLabelAndTitleDistance = 60.0";
mostCurrent._linechart.setYaxisLabelAndTitleDistance((float) (60.0));
 //BA.debugLineNum = 198;BA.debugLine="LineChart.YaxisTitle = \"Temperature (Fahrenheit)";
mostCurrent._linechart.setYaxisTitle("Temperature (Fahrenheit)");
 //BA.debugLineNum = 200;BA.debugLine="LineChart.MaxNumberOfEntriesPerLineChart = 24";
mostCurrent._linechart.setMaxNumberOfEntriesPerLineChart((int) (24));
 //BA.debugLineNum = 201;BA.debugLine="LineChart.GraphLegendVisibility = False";
mostCurrent._linechart.setGraphLegendVisibility(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 205;BA.debugLine="ReadTemperatureHourly(\"Today\")";
_readtemperaturehourly("Today");
 //BA.debugLineNum = 207;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy");
 //BA.debugLineNum = 208;BA.debugLine="LineChart.Line_1_LegendText = \"From \" & timeArra";
mostCurrent._linechart.setLine_1_LegendText("From "+mostCurrent._timearray[(int) (0)]+" to "+mostCurrent._timearray[(int) (23)]);
 //BA.debugLineNum = 210;BA.debugLine="CheckTempBoundaries";
_checktempboundaries();
 //BA.debugLineNum = 212;BA.debugLine="If am12 <> tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 213;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12)";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12))});
 };
 //BA.debugLineNum = 215;BA.debugLine="If am1 <> tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 216;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1))});
 };
 //BA.debugLineNum = 218;BA.debugLine="If am2 <> tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 219;BA.debugLine="If am1 = tempZeroRange Then";
if ((mostCurrent._am1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 220;BA.debugLine="am1 = (am12 + am2)/2";
mostCurrent._am1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am12))+(double)(Double.parseDouble(mostCurrent._am2)))/(double)2);
 };
 //BA.debugLineNum = 222;BA.debugLine="If am12 = tempZeroRange Then";
if ((mostCurrent._am12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 223;BA.debugLine="am12 = am1";
mostCurrent._am12 = mostCurrent._am1;
 };
 //BA.debugLineNum = 225;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2))});
 };
 //BA.debugLineNum = 227;BA.debugLine="If am3 <> tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 228;BA.debugLine="If am2 = tempZeroRange Then";
if ((mostCurrent._am2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 229;BA.debugLine="am2 = (am1 + am3)/2";
mostCurrent._am2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am1))+(double)(Double.parseDouble(mostCurrent._am3)))/(double)2);
 };
 //BA.debugLineNum = 231;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3))});
 };
 //BA.debugLineNum = 233;BA.debugLine="If am4 <> tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 234;BA.debugLine="If am3 = tempZeroRange Then";
if ((mostCurrent._am3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 235;BA.debugLine="am3 = (am2 + am4)/2";
mostCurrent._am3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am2))+(double)(Double.parseDouble(mostCurrent._am4)))/(double)2);
 };
 //BA.debugLineNum = 237;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4))});
 };
 //BA.debugLineNum = 239;BA.debugLine="If am5 <> tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 240;BA.debugLine="If am4 = tempZeroRange Then";
if ((mostCurrent._am4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 241;BA.debugLine="am4 = (am3 + am5)/2";
mostCurrent._am4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am3))+(double)(Double.parseDouble(mostCurrent._am5)))/(double)2);
 };
 //BA.debugLineNum = 243;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5))});
 };
 //BA.debugLineNum = 245;BA.debugLine="If am6 <> tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 246;BA.debugLine="If am5 = tempZeroRange Then";
if ((mostCurrent._am5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 247;BA.debugLine="am5 = (am4 + am6)/2";
mostCurrent._am5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am4))+(double)(Double.parseDouble(mostCurrent._am6)))/(double)2);
 };
 //BA.debugLineNum = 249;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6))});
 };
 //BA.debugLineNum = 251;BA.debugLine="If am7 <> tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 252;BA.debugLine="If am6 = tempZeroRange Then";
if ((mostCurrent._am6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 253;BA.debugLine="am6 = (am5 + am7)/2";
mostCurrent._am6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am5))+(double)(Double.parseDouble(mostCurrent._am7)))/(double)2);
 };
 //BA.debugLineNum = 255;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7))});
 };
 //BA.debugLineNum = 257;BA.debugLine="If am8 <> tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 258;BA.debugLine="If am7 = tempZeroRange Then";
if ((mostCurrent._am7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 259;BA.debugLine="am7 = (am6 + am8)/2";
mostCurrent._am7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am6))+(double)(Double.parseDouble(mostCurrent._am8)))/(double)2);
 };
 //BA.debugLineNum = 261;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8))});
 };
 //BA.debugLineNum = 263;BA.debugLine="If am9 <> tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 264;BA.debugLine="If am8 = tempZeroRange Then";
if ((mostCurrent._am8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 265;BA.debugLine="am8 = (am7 + am9)/2";
mostCurrent._am8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am7))+(double)(Double.parseDouble(mostCurrent._am9)))/(double)2);
 };
 //BA.debugLineNum = 267;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9))});
 };
 //BA.debugLineNum = 269;BA.debugLine="If am10 <> tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 270;BA.debugLine="If am9 = tempZeroRange Then";
if ((mostCurrent._am9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 271;BA.debugLine="am9 = (am8 + am10)/2";
mostCurrent._am9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am8))+(double)(Double.parseDouble(mostCurrent._am10)))/(double)2);
 };
 //BA.debugLineNum = 273;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10))});
 };
 //BA.debugLineNum = 275;BA.debugLine="If am11 <> tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 276;BA.debugLine="If am10 = tempZeroRange Then";
if ((mostCurrent._am10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 277;BA.debugLine="am10 = (am9 + am11)/2";
mostCurrent._am10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am9))+(double)(Double.parseDouble(mostCurrent._am11)))/(double)2);
 };
 //BA.debugLineNum = 279;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11))});
 };
 //BA.debugLineNum = 281;BA.debugLine="If pm12 <> tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 282;BA.debugLine="If am11 = tempZeroRange Then";
if ((mostCurrent._am11).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 283;BA.debugLine="am11 = (am10 + pm12)/2";
mostCurrent._am11 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am10))+(double)(Double.parseDouble(mostCurrent._pm12)))/(double)2);
 };
 //BA.debugLineNum = 285;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12))});
 };
 //BA.debugLineNum = 287;BA.debugLine="If pm1 <> tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 288;BA.debugLine="If pm12 = tempZeroRange Then";
if ((mostCurrent._pm12).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 289;BA.debugLine="pm12 = (am11 + pm1)/2";
mostCurrent._pm12 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._am11))+(double)(Double.parseDouble(mostCurrent._pm1)))/(double)2);
 };
 //BA.debugLineNum = 291;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1))});
 };
 //BA.debugLineNum = 293;BA.debugLine="If pm2 <> tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 294;BA.debugLine="If pm1 = tempZeroRange Then";
if ((mostCurrent._pm1).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 295;BA.debugLine="pm1 = (pm12 + pm2)/2";
mostCurrent._pm1 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm12))+(double)(Double.parseDouble(mostCurrent._pm2)))/(double)2);
 };
 //BA.debugLineNum = 297;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2))});
 };
 //BA.debugLineNum = 299;BA.debugLine="If pm3 <> tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 300;BA.debugLine="If pm2 = tempZeroRange Then";
if ((mostCurrent._pm2).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 301;BA.debugLine="pm2 = (pm1 + pm3)/2";
mostCurrent._pm2 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm1))+(double)(Double.parseDouble(mostCurrent._pm3)))/(double)2);
 };
 //BA.debugLineNum = 303;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3))});
 };
 //BA.debugLineNum = 305;BA.debugLine="If pm4 <> tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 306;BA.debugLine="If pm3 = tempZeroRange Then";
if ((mostCurrent._pm3).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 307;BA.debugLine="pm3 = (pm2 + pm4)/2";
mostCurrent._pm3 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm2))+(double)(Double.parseDouble(mostCurrent._pm4)))/(double)2);
 };
 //BA.debugLineNum = 309;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4))});
 };
 //BA.debugLineNum = 311;BA.debugLine="If pm5 <> tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 312;BA.debugLine="If pm4 = tempZeroRange Then";
if ((mostCurrent._pm4).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 313;BA.debugLine="pm4 = (pm3 + pm5)/2";
mostCurrent._pm4 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm3))+(double)(Double.parseDouble(mostCurrent._pm5)))/(double)2);
 };
 //BA.debugLineNum = 315;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5))});
 };
 //BA.debugLineNum = 317;BA.debugLine="If pm6 <> tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 318;BA.debugLine="If pm5 = tempZeroRange Then";
if ((mostCurrent._pm5).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 319;BA.debugLine="pm5 = (pm4 + pm6)/2";
mostCurrent._pm5 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm4))+(double)(Double.parseDouble(mostCurrent._pm6)))/(double)2);
 };
 //BA.debugLineNum = 321;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6))});
 };
 //BA.debugLineNum = 323;BA.debugLine="If pm7 <> tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 324;BA.debugLine="If pm6 = tempZeroRange Then";
if ((mostCurrent._pm6).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 325;BA.debugLine="pm6 = (pm5 + pm7)/2";
mostCurrent._pm6 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm5))+(double)(Double.parseDouble(mostCurrent._pm7)))/(double)2);
 };
 //BA.debugLineNum = 327;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7))});
 };
 //BA.debugLineNum = 329;BA.debugLine="If pm8 <> tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 330;BA.debugLine="If pm7 = tempZeroRange Then";
if ((mostCurrent._pm7).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 331;BA.debugLine="pm7 = (pm6 + pm8)/2";
mostCurrent._pm7 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm6))+(double)(Double.parseDouble(mostCurrent._pm8)))/(double)2);
 };
 //BA.debugLineNum = 333;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8))});
 };
 //BA.debugLineNum = 335;BA.debugLine="If pm9 <> tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 336;BA.debugLine="If pm8 = tempZeroRange Then";
if ((mostCurrent._pm8).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 337;BA.debugLine="pm8 = (pm7 + pm9)/2";
mostCurrent._pm8 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm7))+(double)(Double.parseDouble(mostCurrent._pm9)))/(double)2);
 };
 //BA.debugLineNum = 339;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9))});
 };
 //BA.debugLineNum = 341;BA.debugLine="If pm10 <> tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 342;BA.debugLine="If pm9 = tempZeroRange Then";
if ((mostCurrent._pm9).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 343;BA.debugLine="pm9 = (pm8 + pm10)/2";
mostCurrent._pm9 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm8))+(double)(Double.parseDouble(mostCurrent._pm10)))/(double)2);
 };
 //BA.debugLineNum = 345;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10))});
 };
 //BA.debugLineNum = 347;BA.debugLine="If pm11 <> tempZeroRange Then";
if ((mostCurrent._pm11).equals(BA.NumberToString(_tempzerorange)) == false) { 
 //BA.debugLineNum = 348;BA.debugLine="If pm10 = tempZeroRange Then";
if ((mostCurrent._pm10).equals(BA.NumberToString(_tempzerorange))) { 
 //BA.debugLineNum = 349;BA.debugLine="pm10 = (pm9 + pm11)/2";
mostCurrent._pm10 = BA.NumberToString(((double)(Double.parseDouble(mostCurrent._pm9))+(double)(Double.parseDouble(mostCurrent._pm11)))/(double)2);
 };
 //BA.debugLineNum = 351;BA.debugLine="LineChart.Line_1_Data = Array As Float (am12, a";
mostCurrent._linechart.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))});
 };
 //BA.debugLineNum = 354;BA.debugLine="LineChart.Line_1_PointLabelTextColor = Colors.Ye";
mostCurrent._linechart.setLine_1_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 355;BA.debugLine="LineChart.Line_1_PointLabelTextSize = 35.0";
mostCurrent._linechart.setLine_1_PointLabelTextSize((float) (35.0));
 //BA.debugLineNum = 356;BA.debugLine="LineChart.Line_1_LineColor = Colors.Red";
mostCurrent._linechart.setLine_1_LineColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 357;BA.debugLine="LineChart.Line_1_LineWidth = 11.0";
mostCurrent._linechart.setLine_1_LineWidth((float) (11.0));
 //BA.debugLineNum = 358;BA.debugLine="LineChart.Line_1_PointColor = Colors.Black";
mostCurrent._linechart.setLine_1_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 359;BA.debugLine="LineChart.Line_1_PointSize = 25.0";
mostCurrent._linechart.setLine_1_PointSize((float) (25.0));
 //BA.debugLineNum = 360;BA.debugLine="LineChart.Line_1_PointShape = LineChart.SHAPE_RO";
mostCurrent._linechart.setLine_1_PointShape(mostCurrent._linechart.SHAPE_ROUND);
 //BA.debugLineNum = 361;BA.debugLine="LineChart.Line_1_DrawDash = False";
mostCurrent._linechart.setLine_1_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 362;BA.debugLine="LineChart.Line_1_DrawCubic = False";
mostCurrent._linechart.setLine_1_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 369;BA.debugLine="LineChart.Line_2_Data = Array As Float (tempRigh";
mostCurrent._linechart.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow))});
 //BA.debugLineNum = 370;BA.debugLine="LineChart.Line_2_PointLabelTextColor = Colors.Gr";
mostCurrent._linechart.setLine_2_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 371;BA.debugLine="LineChart.Line_2_PointLabelTextSize = 30.0";
mostCurrent._linechart.setLine_2_PointLabelTextSize((float) (30.0));
 //BA.debugLineNum = 372;BA.debugLine="LineChart.Line_2_LineColor = Colors.Green";
mostCurrent._linechart.setLine_2_LineColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 373;BA.debugLine="LineChart.Line_2_LineWidth = 5.0";
mostCurrent._linechart.setLine_2_LineWidth((float) (5.0));
 //BA.debugLineNum = 374;BA.debugLine="LineChart.Line_2_PointColor = Colors.Green";
mostCurrent._linechart.setLine_2_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 375;BA.debugLine="LineChart.Line_2_PointSize = 1.0";
mostCurrent._linechart.setLine_2_PointSize((float) (1.0));
 //BA.debugLineNum = 376;BA.debugLine="LineChart.Line_2_PointShape = LineChart.SHAPE_RO";
mostCurrent._linechart.setLine_2_PointShape(mostCurrent._linechart.SHAPE_ROUND);
 //BA.debugLineNum = 377;BA.debugLine="LineChart.Line_2_DrawDash = False";
mostCurrent._linechart.setLine_2_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 378;BA.debugLine="LineChart.Line_2_DrawCubic = False";
mostCurrent._linechart.setLine_2_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 382;BA.debugLine="LineChart.NumberOfLineCharts = 2";
mostCurrent._linechart.setNumberOfLineCharts((int) (2));
 //BA.debugLineNum = 384;BA.debugLine="LineChart.DrawTheGraphs";
mostCurrent._linechart.DrawTheGraphs();
 } 
       catch (Exception e228) {
			processBA.setLastException(e228); //BA.debugLineNum = 387;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("63014918",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 388;BA.debugLine="ToastMessageShow (LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 391;BA.debugLine="End Sub";
return "";
}
public static String  _temperaturehourlytimer_tick() throws Exception{
 //BA.debugLineNum = 2187;BA.debugLine="Sub TemperatureHourlyTimer_Tick";
 //BA.debugLineNum = 2188;BA.debugLine="Activity.RequestFocus";
mostCurrent._activity.RequestFocus();
 //BA.debugLineNum = 2189;BA.debugLine="btnHumidityHourly.RemoveView";
mostCurrent._btnhumidityhourly.RemoveView();
 //BA.debugLineNum = 2190;BA.debugLine="btnTempHourly.RemoveView";
mostCurrent._btntemphourly.RemoveView();
 //BA.debugLineNum = 2191;BA.debugLine="btnHumidityDaily.RemoveView";
mostCurrent._btnhumiditydaily.RemoveView();
 //BA.debugLineNum = 2192;BA.debugLine="btnTempDaily.RemoveView";
mostCurrent._btntempdaily.RemoveView();
 //BA.debugLineNum = 2193;BA.debugLine="LineChart.RemoveView";
mostCurrent._linechart.RemoveView();
 //BA.debugLineNum = 2194;BA.debugLine="tempMaxRange=0";
_tempmaxrange = (float) (0);
 //BA.debugLineNum = 2195;BA.debugLine="tempMinRange=0";
_tempminrange = (float) (0);
 //BA.debugLineNum = 2196;BA.debugLine="TemperatureHourlyCreate";
_temperaturehourlycreate();
 //BA.debugLineNum = 2197;BA.debugLine="End Sub";
return "";
}
}
