package cloyd.smart.home.monitor;

import android.app.Activity;

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
public static String _mqttserveruri = "";
public static anywheresoftware.b4a.agraham.byteconverter.ByteConverter _bc = null;
public static b4a.example.callsubutils _csu = null;
public static anywheresoftware.b4a.objects.IntentWrapper _oldintent = null;
public static de.amberhome.objects.preferenceactivity.PreferenceManager _manager = null;
public static de.amberhome.objects.preferenceactivity.PreferenceScreenWrapper _screen = null;
public static boolean _isthereunwatchedvideo = false;
public static anywheresoftware.b4a.phone.Phone.PhoneWakeState _awake = null;
public static String _compiletimestamp = "";
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
public cloyd.smart.home.monitor.b4xpageindicator _b4xpageindicator1 = null;
public cloyd.smart.home.monitor.badger _badger1 = null;
public anywheresoftware.b4j.object.JavaObject _nativeme = null;
public cloyd.smart.home.monitor.b4xdialog _dialog = null;
public cloyd.smart.home.monitor.swiftbutton _btnchart = null;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.chart _chart = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.b4xcollections _b4xcollections = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (chart.mostCurrent != null);
return vis;}
public static class _carddata{
public boolean IsInitialized;
public anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper screenshot;
public String filedate;
public String deviceinfo;
public boolean iswatchedvisible;
public String mediaURL;
public void Initialize() {
IsInitialized = true;
screenshot = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
filedate = "";
deviceinfo = "";
iswatchedvisible = false;
mediaURL = "";
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _videoinfo{
public boolean IsInitialized;
public String ThumbnailPath;
public String DateCreated;
public String Watched;
public String DeviceName;
public String VideoID;
public byte[] ThumbnailBLOB;
public void Initialize() {
IsInitialized = true;
ThumbnailPath = "";
DateCreated = "";
Watched = "";
DeviceName = "";
VideoID = "";
ThumbnailBLOB = new byte[0];
;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static void  _activity_create(boolean _firsttime) throws Exception{
ResumableSub_Activity_Create rsub = new ResumableSub_Activity_Create(null,_firsttime);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Create extends BA.ResumableSub {
public ResumableSub_Activity_Create(cloyd.smart.home.monitor.main parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
cloyd.smart.home.monitor.main parent;
boolean _firsttime;
anywheresoftware.b4a.objects.collections.Map _compiledata = null;
anywheresoftware.b4a.objects.IntentWrapper _icrash = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.objects.CSBuilder _cs = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.object.XmlLayoutBuilder _xl = null;
String _strhumidityaddvalue = "";
anywheresoftware.b4a.objects.B4XViewWrapper _lblbadge = null;
int _width = 0;
int _height = 0;
anywheresoftware.b4a.objects.PanelWrapper _p = null;
anywheresoftware.b4a.BA.IterableList group34;
int index34;
int groupLen34;
anywheresoftware.b4a.BA.IterableList group39;
int index39;
int groupLen39;
anywheresoftware.b4a.BA.IterableList group93;
int index93;
int groupLen93;

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
 //BA.debugLineNum = 83;BA.debugLine="Try";
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
 //BA.debugLineNum = 84;BA.debugLine="nativeMe.InitializeContext";
parent.mostCurrent._nativeme.InitializeContext(processBA);
 //BA.debugLineNum = 85;BA.debugLine="nativeMe.RunMethod(\"setDefaultUncaughtExceptionH";
parent.mostCurrent._nativeme.RunMethod("setDefaultUncaughtExceptionHandler",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 87;BA.debugLine="If FirstTime Then";
if (true) break;

case 4:
//if
this.state = 13;
if (_firsttime) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 88;BA.debugLine="CreatePreferenceScreen";
_createpreferencescreen();
 //BA.debugLineNum = 89;BA.debugLine="If manager.GetAll.Size = 0 Then SetDefaults";
if (true) break;

case 7:
//if
this.state = 12;
if (parent._manager.GetAll().getSize()==0) { 
this.state = 9;
;}if (true) break;

case 9:
//C
this.state = 12;
_setdefaults();
if (true) break;

case 12:
//C
this.state = 13;
;
 //BA.debugLineNum = 91;BA.debugLine="StartService(SmartHomeMonitor)";
anywheresoftware.b4a.keywords.Common.StartService(processBA,(Object)(parent.mostCurrent._smarthomemonitor.getObject()));
 //BA.debugLineNum = 92;BA.debugLine="csu.Initialize";
parent._csu._initialize(processBA);
 //BA.debugLineNum = 93;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 //BA.debugLineNum = 95;BA.debugLine="Dim compiledata As Map";
_compiledata = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 96;BA.debugLine="compiledata = File.ReadMap(File.DirAssets, \"com";
_compiledata = anywheresoftware.b4a.keywords.Common.File.ReadMap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"compiletime.txt");
 //BA.debugLineNum = 97;BA.debugLine="Log($\"autoversion: ${compiledata.Get(\"autoversi";
anywheresoftware.b4a.keywords.Common.LogImpl("6131087",("autoversion: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",_compiledata.Get((Object)("autoversion")))+""),0);
 //BA.debugLineNum = 98;BA.debugLine="Log($\"compilation time: $DateTime{compiledata.G";
anywheresoftware.b4a.keywords.Common.LogImpl("6131088",("compilation time: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("datetime",_compiledata.Get((Object)("time")))+""),0);
 //BA.debugLineNum = 99;BA.debugLine="compileTimeStamp = compiledata.Get(\"time\")";
parent._compiletimestamp = BA.ObjectToString(_compiledata.Get((Object)("time")));
 if (true) break;

case 13:
//C
this.state = 14;
;
 //BA.debugLineNum = 101;BA.debugLine="Activity.LoadLayout(\"Main\")";
parent.mostCurrent._activity.LoadLayout("Main",mostCurrent.activityBA);
 //BA.debugLineNum = 103;BA.debugLine="dialog.Initialize(Activity)";
parent.mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject())));
 //BA.debugLineNum = 104;BA.debugLine="dialog.Title = \"Smart Home Monitor\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Smart Home Monitor");
 //BA.debugLineNum = 106;BA.debugLine="Dim iCrash As Intent";
_icrash = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 107;BA.debugLine="iCrash = Activity.GetStartingIntent";
_icrash = parent.mostCurrent._activity.GetStartingIntent();
 //BA.debugLineNum = 108;BA.debugLine="If iCrash.HasExtra(\"Crash\") Then";
if (true) break;

case 14:
//if
this.state = 17;
if (_icrash.HasExtra("Crash")) { 
this.state = 16;
}if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 109;BA.debugLine="Log(\"After crash: \" & iCrash.GetExtra(\"Crash\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("6131099","After crash: "+BA.ObjectToString(_icrash.GetExtra("Crash")),0);
 if (true) break;

case 17:
//C
this.state = 18;
;
 //BA.debugLineNum = 112;BA.debugLine="TabStrip1.LoadLayout(\"1ScrollView\", \"LIVING AREA";
parent.mostCurrent._tabstrip1.LoadLayout("1ScrollView",BA.ObjectToCharSequence("LIVING AREA"+anywheresoftware.b4a.keywords.Common.CRLF+"Temp & Humidity  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf2c7)))));
 //BA.debugLineNum = 113;BA.debugLine="ScrollView1.Panel.LoadLayout(\"1\")";
parent.mostCurrent._scrollview1.getPanel().LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 114;BA.debugLine="Panel1.Height = ScrollView1.Height 'Panel1.Heigh";
parent.mostCurrent._panel1.setHeight(parent.mostCurrent._scrollview1.getHeight());
 //BA.debugLineNum = 115;BA.debugLine="ScrollView1.Panel.Height = Panel1.Height";
parent.mostCurrent._scrollview1.getPanel().setHeight(parent.mostCurrent._panel1.getHeight());
 //BA.debugLineNum = 116;BA.debugLine="TabStrip1.LoadLayout(\"2\", \"LIVING AREA\" & CRLF &";
parent.mostCurrent._tabstrip1.LoadLayout("2",BA.ObjectToCharSequence("LIVING AREA"+anywheresoftware.b4a.keywords.Common.CRLF+"Air Quality (CO)  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf289)))));
 //BA.debugLineNum = 117;BA.debugLine="TabStrip1.LoadLayout(\"ScrollViewBasement\", \"BASE";
parent.mostCurrent._tabstrip1.LoadLayout("ScrollViewBasement",BA.ObjectToCharSequence("BASEMENT"+anywheresoftware.b4a.keywords.Common.CRLF+"Temp & Humidity  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf2c7)))));
 //BA.debugLineNum = 118;BA.debugLine="ScrollViewBasement.Panel.LoadLayout(\"TempHumidit";
parent.mostCurrent._scrollviewbasement.getPanel().LoadLayout("TempHumidityBasement",mostCurrent.activityBA);
 //BA.debugLineNum = 119;BA.debugLine="PanelTempHumidityBasement.Height = ScrollViewBas";
parent.mostCurrent._paneltemphumiditybasement.setHeight(parent.mostCurrent._scrollviewbasement.getHeight());
 //BA.debugLineNum = 120;BA.debugLine="ScrollViewBasement.Panel.Height = PanelTempHumid";
parent.mostCurrent._scrollviewbasement.getPanel().setHeight(parent.mostCurrent._paneltemphumiditybasement.getHeight());
 //BA.debugLineNum = 121;BA.debugLine="TabStrip1.LoadLayout(\"AirQualityBasement\", \"BASE";
parent.mostCurrent._tabstrip1.LoadLayout("AirQualityBasement",BA.ObjectToCharSequence("BASEMENT"+anywheresoftware.b4a.keywords.Common.CRLF+"Air Quality (CO)  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf289)))));
 //BA.debugLineNum = 123;BA.debugLine="For Each lbl As Label In GetAllTabLabels(TabStri";
if (true) break;

case 18:
//for
this.state = 21;
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
group34 = _getalltablabels(parent.mostCurrent._tabstrip1);
index34 = 0;
groupLen34 = group34.getSize();
this.state = 48;
if (true) break;

case 48:
//C
this.state = 21;
if (index34 < groupLen34) {
this.state = 20;
_lbl.setObject((android.widget.TextView)(group34.Get(index34)));}
if (true) break;

case 49:
//C
this.state = 48;
index34++;
if (true) break;

case 20:
//C
this.state = 49;
 //BA.debugLineNum = 125;BA.debugLine="lbl.SingleLine = False";
_lbl.setSingleLine(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 127;BA.debugLine="lbl.Typeface = Typeface.FONTAWESOME";
_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME());
 //BA.debugLineNum = 129;BA.debugLine="lbl.Padding = Array As Int(0, 0, 0, 0)";
_lbl.setPadding(new int[]{(int) (0),(int) (0),(int) (0),(int) (0)});
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 133;BA.debugLine="For Each v As View In GetAllTabLabels(TabStrip1)";

case 21:
//for
this.state = 24;
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
group39 = _getalltablabels(parent.mostCurrent._tabstrip1);
index39 = 0;
groupLen39 = group39.getSize();
this.state = 50;
if (true) break;

case 50:
//C
this.state = 24;
if (index39 < groupLen39) {
this.state = 23;
_v.setObject((android.view.View)(group39.Get(index39)));}
if (true) break;

case 51:
//C
this.state = 50;
index39++;
if (true) break;

case 23:
//C
this.state = 51;
 //BA.debugLineNum = 135;BA.debugLine="v.Width = 33%x";
_v.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA));
 if (true) break;
if (true) break;

case 24:
//C
this.state = 25;
;
 //BA.debugLineNum = 138;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 139;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 140;BA.debugLine="ACToolBarLight1.NavigationIconDrawable = bd";
parent.mostCurrent._actoolbarlight1.setNavigationIconDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 141;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 142;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 143;BA.debugLine="ToolbarHelper.Title= cs.Initialize.Size(22).Appe";
parent.mostCurrent._toolbarhelper.setTitle(BA.ObjectToCharSequence(_cs.Initialize().Size((int) (22)).Append(BA.ObjectToCharSequence("Smart Home Monitor")).PopAll().getObject()));
 //BA.debugLineNum = 144;BA.debugLine="ToolbarHelper.subTitle = \"\"";
parent.mostCurrent._toolbarhelper.setSubtitle(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 145;BA.debugLine="ToolbarHelper.ShowUpIndicator = False 'set to tr";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 146;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 147;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 148;BA.debugLine="Dim xl As XmlLayoutBuilder";
_xl = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 149;BA.debugLine="jo.RunMethod(\"setPopupTheme\", Array(xl.GetResour";
_jo.RunMethod("setPopupTheme",new Object[]{(Object)(_xl.GetResourceId("style","ToolbarMenu"))});
 //BA.debugLineNum = 151;BA.debugLine="GaugeHumidity.SetRanges(Array(GaugeHumidity.Crea";
parent.mostCurrent._gaugehumidity._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (70),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (70),(float) (80),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (80),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 154;BA.debugLine="GaugeTemp.SetRanges(Array(GaugeTemp.CreateRange(";
parent.mostCurrent._gaugetemp._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 157;BA.debugLine="GaugeHeatIndex.SetRanges(Array(GaugeHeatIndex.Cr";
parent.mostCurrent._gaugeheatindex._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 162;BA.debugLine="GaugeDewPoint.SetRanges(Array(GaugeDewPoint.Crea";
parent.mostCurrent._gaugedewpoint._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (60.8),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (60.8),(float) (64.4),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(parent.mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (64.4),(float) (78.8),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (78.8),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 168;BA.debugLine="GaugeAirQuality.SetRanges(Array(GaugeAirQuality.";
parent.mostCurrent._gaugeairquality._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (10),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (10),(float) (40),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(parent.mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (40),(float) (90),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 172;BA.debugLine="GaugeAirQuality.CurrentValue=0";
parent.mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 174;BA.debugLine="GaugeHumidityBasement.SetRanges(Array(GaugeHumid";
parent.mostCurrent._gaugehumiditybasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (70),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (70),(float) (80),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (80),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 177;BA.debugLine="GaugeTempBasement.SetRanges(Array(GaugeTempBasem";
parent.mostCurrent._gaugetempbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 180;BA.debugLine="GaugeHeatIndexBasement.SetRanges(Array(GaugeHeat";
parent.mostCurrent._gaugeheatindexbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 185;BA.debugLine="GaugeDewPointBasement.SetRanges(Array(GaugeDewPo";
parent.mostCurrent._gaugedewpointbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (60.8),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (60.8),(float) (64.4),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(parent.mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (64.4),(float) (78.8),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (78.8),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 191;BA.debugLine="GaugeAirQualityBasement.SetRanges(Array(GaugeAir";
parent.mostCurrent._gaugeairqualitybasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (10),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (10),(float) (40),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(parent.mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (40),(float) (90),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 195;BA.debugLine="GaugeAirQualityBasement.CurrentValue=0";
parent.mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 197;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 198;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 199;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append(\"";
parent.mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 200;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"The";
parent.mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 201;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 202;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
parent.mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 203;BA.debugLine="lblPing.Visible = False";
parent.mostCurrent._lblping.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 204;BA.debugLine="GaugeAirQuality.CurrentValue = 0";
parent.mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 205;BA.debugLine="GaugeAirQualityBasement.CurrentValue = 0";
parent.mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 206;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Append(\"";
parent.mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 207;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.Bol";
parent.mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).PopAll().getObject()));
 //BA.debugLineNum = 209;BA.debugLine="lblPerceptionBasement.Text = cs.Initialize.Bold.";
parent.mostCurrent._lblperceptionbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 210;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.App";
parent.mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 211;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 212;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bold.";
parent.mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 213;BA.debugLine="lblPingBasement.Visible = False";
parent.mostCurrent._lblpingbasement.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 214;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bold.";
parent.mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 215;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Initia";
parent.mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).PopAll().getObject()));
 //BA.debugLineNum = 217;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (true) break;

case 25:
//if
this.state = 32;
if (parent._mqtt.IsInitialized() && parent._mqtt.getConnected()) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 218;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Rea";
parent._mqtt.Publish("TempHumid",parent._bc.StringToBytes("Read weather","utf8"));
 //BA.debugLineNum = 220;BA.debugLine="Dim strHumidityAddValue As String = StateManage";
_strhumidityaddvalue = parent.mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue");
 //BA.debugLineNum = 221;BA.debugLine="If strHumidityAddValue = \"\" Then";
if (true) break;

case 28:
//if
this.state = 31;
if ((_strhumidityaddvalue).equals("")) { 
this.state = 30;
}if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 222;BA.debugLine="strHumidityAddValue = \"0\"";
_strhumidityaddvalue = "0";
 if (true) break;

case 31:
//C
this.state = 32;
;
 //BA.debugLineNum = 224;BA.debugLine="MQTT.Publish(\"HumidityAddValue\", bc.StringToByt";
parent._mqtt.Publish("HumidityAddValue",parent._bc.StringToBytes(_strhumidityaddvalue,"utf8"));
 if (true) break;

case 32:
//C
this.state = 33;
;
 //BA.debugLineNum = 227;BA.debugLine="badger1.Initialize";
parent.mostCurrent._badger1._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 229;BA.debugLine="For Each lblBadge As B4XView In GetAllTabLabelsF";
if (true) break;

case 33:
//for
this.state = 44;
_lblbadge = new anywheresoftware.b4a.objects.B4XViewWrapper();
group93 = _getalltablabelsforbadge(parent.mostCurrent._tabstrip1);
index93 = 0;
groupLen93 = group93.getSize();
this.state = 52;
if (true) break;

case 52:
//C
this.state = 44;
if (index93 < groupLen93) {
this.state = 35;
_lblbadge.setObject((java.lang.Object)(group93.Get(index93)));}
if (true) break;

case 53:
//C
this.state = 52;
index93++;
if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 230;BA.debugLine="Dim Width, Height As Int";
_width = 0;
_height = 0;
 //BA.debugLineNum = 231;BA.debugLine="Dim jo As JavaObject = lblBadge";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_lblbadge.getObject()));
 //BA.debugLineNum = 232;BA.debugLine="Do While True";
if (true) break;

case 36:
//do while
this.state = 43;
while (anywheresoftware.b4a.keywords.Common.True) {
this.state = 38;
if (true) break;
}
if (true) break;

case 38:
//C
this.state = 39;
 //BA.debugLineNum = 233;BA.debugLine="Width = jo.RunMethod(\"getMeasuredWidth\", Null)";
_width = (int)(BA.ObjectToNumber(_jo.RunMethod("getMeasuredWidth",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 234;BA.debugLine="Height = jo.RunMethod(\"getMeasuredHeight\", Nul";
_height = (int)(BA.ObjectToNumber(_jo.RunMethod("getMeasuredHeight",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 235;BA.debugLine="If Width > 0 And Height > 0 Then";
if (true) break;

case 39:
//if
this.state = 42;
if (_width>0 && _height>0) { 
this.state = 41;
}if (true) break;

case 41:
//C
this.state = 42;
 //BA.debugLineNum = 236;BA.debugLine="Exit";
this.state = 43;
if (true) break;
 if (true) break;

case 42:
//C
this.state = 36;
;
 //BA.debugLineNum = 238;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 54;
return;
case 54:
//C
this.state = 36;
;
 if (true) break;

case 43:
//C
this.state = 53;
;
 //BA.debugLineNum = 240;BA.debugLine="Dim p As Panel = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
_p.setObject((android.view.ViewGroup)(parent.mostCurrent._xui.CreatePanel(processBA,"").getObject()));
 //BA.debugLineNum = 241;BA.debugLine="p.Tag = lblBadge";
_p.setTag((Object)(_lblbadge.getObject()));
 //BA.debugLineNum = 242;BA.debugLine="lblBadge.Parent.AddView(p, 0, 0, Width, Height)";
_lblbadge.getParent().AddView((android.view.View)(_p.getObject()),(int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 243;BA.debugLine="lblBadge.RemoveViewFromParent";
_lblbadge.RemoveViewFromParent();
 //BA.debugLineNum = 244;BA.debugLine="p.AddView(lblBadge, 0, 0, Width, Height)";
_p.AddView((android.view.View)(_lblbadge.getObject()),(int) (0),(int) (0),_width,_height);
 if (true) break;
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
 //BA.debugLineNum = 248;BA.debugLine="ToastMessageShow(LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;
if (true) break;

case 47:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 250;BA.debugLine="End Sub";
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
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
 //BA.debugLineNum = 578;BA.debugLine="Sub Activity_Createmenu(Menu As ACMenu)";
 //BA.debugLineNum = 579;BA.debugLine="Try";
try { //BA.debugLineNum = 580;BA.debugLine="Menu.Clear";
_menu.Clear();
 //BA.debugLineNum = 581;BA.debugLine="gblACMenu = Menu";
mostCurrent._gblacmenu = _menu;
 //BA.debugLineNum = 582;BA.debugLine="Menu.Add(0, 0, \"Settings\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Settings"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 583;BA.debugLine="Menu.Add(0, 0, \"Restart Application\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Restart Application"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 584;BA.debugLine="Menu.Add(0, 0, \"Show free memory\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Show free memory"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 585;BA.debugLine="Menu.Add(0, 0, \"Show chart\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Show chart"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 586;BA.debugLine="Menu.Add(0, 0, \"About\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 } 
       catch (Exception e10) {
<<<<<<< HEAD
			processBA.setLastException(e10); //BA.debugLineNum = 588;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("61441802",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
=======
			processBA.setLastException(e10); //BA.debugLineNum = 725;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("31441802",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
>>>>>>> origin/master
 };
 //BA.debugLineNum = 590;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 681;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 682;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 683;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (mostCurrent._tabstrip1.getCurrentPage()==2) { 
 //BA.debugLineNum = 684;BA.debugLine="TabStrip1.ScrollTo(1,False)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 685;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 //BA.debugLineNum = 686;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==1) { 
 //BA.debugLineNum = 688;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 689;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 //BA.debugLineNum = 690;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==3) { 
 //BA.debugLineNum = 692;BA.debugLine="TabStrip1.ScrollTo(2,False)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 693;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 //BA.debugLineNum = 694;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==4) { 
 //BA.debugLineNum = 696;BA.debugLine="TabStrip1.ScrollTo(3,False)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 697;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 //BA.debugLineNum = 698;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==5) { 
 //BA.debugLineNum = 700;BA.debugLine="TabStrip1.ScrollTo(4,False)";
mostCurrent._tabstrip1.ScrollTo((int) (4),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 701;BA.debugLine="TabStrip1_PageSelected(4)";
_tabstrip1_pageselected((int) (4));
 //BA.debugLineNum = 702;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==6) { 
 //BA.debugLineNum = 704;BA.debugLine="TabStrip1.ScrollTo(5,False)";
mostCurrent._tabstrip1.ScrollTo((int) (5),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 705;BA.debugLine="TabStrip1_PageSelected(5)";
_tabstrip1_pageselected((int) (5));
 //BA.debugLineNum = 706;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 710;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 319;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 320;BA.debugLine="Awake.ReleaseKeepAlive";
_awake.ReleaseKeepAlive();
 //BA.debugLineNum = 321;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _in = null;
String _notificationclicked = "";
 //BA.debugLineNum = 252;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 253;BA.debugLine="Try";
try { //BA.debugLineNum = 254;BA.debugLine="Awake.KeepAlive(True)";
_awake.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 255;BA.debugLine="HandleSettings";
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
 //BA.debugLineNum = 265;BA.debugLine="If NotificationClicked = \"Living area temperatur";
if ((_notificationclicked).equals("Living area temperature")) { 
 //BA.debugLineNum = 266;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 }else if((_notificationclicked).equals("Living area carbon monoxide")) { 
 //BA.debugLineNum = 269;BA.debugLine="TabStrip1.ScrollTo(1,False)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 }else if((_notificationclicked).equals("Basement temperature")) { 
 //BA.debugLineNum = 272;BA.debugLine="TabStrip1.ScrollTo(2,False)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.False);
 }else if((_notificationclicked).equals("Basement carbon monoxide")) { 
 //BA.debugLineNum = 275;BA.debugLine="TabStrip1.ScrollTo(3,False)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.False);
 }else if((_notificationclicked).equals("Basement DHT22 sensor issue")) { 
 //BA.debugLineNum = 278;BA.debugLine="TabStrip1.ScrollTo(2,False)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.False);
 }else if((_notificationclicked).equals("Living area DHT22 sensor issue")) { 
 //BA.debugLineNum = 281;BA.debugLine="TabStrip1.ScrollTo(0,False)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 }else if((_notificationclicked).equals("Living area CO sensor issue")) { 
 //BA.debugLineNum = 284;BA.debugLine="TabStrip1.ScrollTo(1,False)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 }else if((_notificationclicked).equals("Basement CO sensor issue")) { 
 //BA.debugLineNum = 287;BA.debugLine="TabStrip1.ScrollTo(3,False)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 291;BA.debugLine="If TabStrip1.CurrentPage = 0 Then";
if (mostCurrent._tabstrip1.getCurrentPage()==0) { 
 //BA.debugLineNum = 292;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 }else if(mostCurrent._tabstrip1.getCurrentPage()==1) { 
 //BA.debugLineNum = 294;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 }else if(mostCurrent._tabstrip1.getCurrentPage()==2) { 
 //BA.debugLineNum = 296;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 }else if(mostCurrent._tabstrip1.getCurrentPage()==3) { 
 //BA.debugLineNum = 298;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 }else if(mostCurrent._tabstrip1.getCurrentPage()==4) { 
 //BA.debugLineNum = 300;BA.debugLine="TabStrip1_PageSelected(4)";
_tabstrip1_pageselected((int) (4));
 }else if(mostCurrent._tabstrip1.getCurrentPage()==5) { 
 //BA.debugLineNum = 302;BA.debugLine="TabStrip1_PageSelected(5)";
_tabstrip1_pageselected((int) (5));
 }else if(mostCurrent._tabstrip1.getCurrentPage()==6) { 
 //BA.debugLineNum = 304;BA.debugLine="TabStrip1_PageSelected(6)";
_tabstrip1_pageselected((int) (6));
 };
 } 
       catch (Exception e45) {
			processBA.setLastException(e45); //BA.debugLineNum = 307;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("6196663",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 310;BA.debugLine="Try";
try { //BA.debugLineNum = 311;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connected";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 312;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 } 
       catch (Exception e52) {
			processBA.setLastException(e52); //BA.debugLineNum = 315;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("6196671",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 317;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.objects.IntentWrapper _intent1 = null;
anywheresoftware.b4a.phone.Phone _phone = null;
String _info = "";
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
int _result = 0;
 //BA.debugLineNum = 479;BA.debugLine="Sub ACToolBarLight1_MenuItemClick (Item As ACMenuI";
 //BA.debugLineNum = 480;BA.debugLine="Try";
try { //BA.debugLineNum = 481;BA.debugLine="If Item.Title = \"About\" Then";
if ((_item.getTitle()).equals("About")) { 
 //BA.debugLineNum = 482;BA.debugLine="ShowAboutMenu";
_showaboutmenu();
 }else if((_item.getTitle()).equals("Settings")) { 
 //BA.debugLineNum = 484;BA.debugLine="StartActivity(screen.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_screen.CreateIntent()));
 }else if((_item.getTitle()).equals("Restart Application")) { 
 //BA.debugLineNum = 486;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 487;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets,";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 488;BA.debugLine="If  Msgbox2(\"Restart the application?\", \"Smart";
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the application?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
<<<<<<< HEAD
 //BA.debugLineNum = 490;BA.debugLine="Dim Intent1 As Intent";
_intent1 = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 491;BA.debugLine="Intent1.Initialize(\"smart.home.restart\", \"\")";
_intent1.Initialize("smart.home.restart","");
 //BA.debugLineNum = 492;BA.debugLine="Dim Phone As Phone";
_phone = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 493;BA.debugLine="Phone.SendBroadcastIntent(Intent1)";
_phone.SendBroadcastIntent((android.content.Intent)(_intent1.getObject()));
 };
 }else if((_item.getTitle()).equals("Show free memory")) { 
 //BA.debugLineNum = 496;BA.debugLine="Dim info As String";
_info = "";
 //BA.debugLineNum = 497;BA.debugLine="info = Starter.kvs.ListKeys.Size & \" video clip";
_info = BA.NumberToString(mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._listkeys /*anywheresoftware.b4a.objects.collections.List*/ ().getSize())+" video clips"+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 498;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 499;BA.debugLine="r.Target = r.RunStaticMethod(\"java.lang.Runtime";
_r.Target = _r.RunStaticMethod("java.lang.Runtime","getRuntime",(Object[])(anywheresoftware.b4a.keywords.Common.Null),(String[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 500;BA.debugLine="info = info & \"Available memory: \" & NumberForm";
_info = _info+"Available memory: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((((double)(BA.ObjectToNumber(_r.RunMethod("maxMemory")))-(double)(BA.ObjectToNumber(_r.RunMethod("totalMemory"))))/(double)(1024*1024)),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+" MB"+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 501;BA.debugLine="info = info & \"Free memory: \" & NumberFormat2(G";
_info = _info+"Free memory: "+anywheresoftware.b4a.keywords.Common.NumberFormat2(_getfreemem()/(double)1000,(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+" MB";
 //BA.debugLineNum = 502;BA.debugLine="Msgbox(info,\"Smart Home Monitor\") 'ignore";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence(_info),BA.ObjectToCharSequence("Smart Home Monitor"),mostCurrent.activityBA);
 }else if((_item.getTitle()).equals("Show chart")) { 
 //BA.debugLineNum = 504;BA.debugLine="StartActivity(\"Chart\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("Chart"));
 }else if((_item.getTitle()).equals("Restart board")) { 
 //BA.debugLineNum = 506;BA.debugLine="Try";
try { //BA.debugLineNum = 507;BA.debugLine="Dim Result As Int";
_result = 0;
 //BA.debugLineNum = 508;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 509;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets,";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 510;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (mostCurrent._tabstrip1.getCurrentPage()==2) { 
 //BA.debugLineNum = 511;BA.debugLine="Result = Msgbox2(\"Restart the BASEMENT contro";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 512;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 513;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 514;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 516;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringT";
_mqtt.Publish("TempHumidBasement",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else if(mostCurrent._tabstrip1.getCurrentPage()==1) { 
 //BA.debugLineNum = 519;BA.debugLine="Result = Msgbox2(\"Restart the AIR QUALITY con";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 520;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 521;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 522;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 524;BA.debugLine="MQTT.Publish(\"MQ7LivingRoomCloyd\", bc.String";
_mqtt.Publish("MQ7LivingRoomCloyd",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else if(mostCurrent._tabstrip1.getCurrentPage()==3) { 
 //BA.debugLineNum = 527;BA.debugLine="Result = Msgbox2(\"Restart the BASEMENT AIR QU";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 528;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 529;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 530;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 532;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes";
_mqtt.Publish("MQ7Basement",_bc.StringToBytes("Restart controller","utf8"));
 };
 }else {
 //BA.debugLineNum = 535;BA.debugLine="Result = Msgbox2(\"Restart the WEATHER control";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the WEATHER controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 536;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 537;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 538;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 540;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Restart controller","utf8"));
 };
 };
 } 
       catch (Exception e64) {
			processBA.setLastException(e64); //BA.debugLineNum = 544;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("61245249",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 };
 } 
       catch (Exception e68) {
			processBA.setLastException(e68); //BA.debugLineNum = 548;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("61245253",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 550;BA.debugLine="End Sub";
return "";
}
public static String  _btnchart_click() throws Exception{
 //BA.debugLineNum = 999;BA.debugLine="Private Sub btnChart_Click";
 //BA.debugLineNum = 1000;BA.debugLine="StartActivity(\"Chart\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("Chart"));
 //BA.debugLineNum = 1001;BA.debugLine="End Sub";
return "";
=======
this.state = 13;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 613;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 //BA.debugLineNum = 614;BA.debugLine="Dim r, r2 As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
_r2 = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 615;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 616;BA.debugLine="r.Target = r.RunMethod(\"getApplicationContext\"";
_r.Target = _r.RunMethod("getApplicationContext");
 //BA.debugLineNum = 617;BA.debugLine="r2.Target = r.RunMethod(\"getPackageManager\")";
_r2.Target = _r.RunMethod("getPackageManager");
 //BA.debugLineNum = 618;BA.debugLine="Dim i As Intent = r2.RunMethod2(\"getLaunchInte";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
_i.setObject((android.content.Intent)(_r2.RunMethod2("getLaunchIntentForPackage",BA.ObjectToString(_r.RunMethod("getPackageName")),"java.lang.String")));
 //BA.debugLineNum = 619;BA.debugLine="r.Target = i";
_r.Target = (Object)(_i.getObject());
 //BA.debugLineNum = 620;BA.debugLine="r.RunMethod2(\"addFlags\",  67108864, \"java.lang";
_r.RunMethod2("addFlags",BA.NumberToString(67108864),"java.lang.int");
 //BA.debugLineNum = 621;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_i.getObject()));
 if (true) break;

case 14:
//C
this.state = 73;
;
 if (true) break;

case 16:
//C
this.state = 73;
 //BA.debugLineNum = 624;BA.debugLine="Dim info As String";
_info = "";
 //BA.debugLineNum = 625;BA.debugLine="info = Starter.kvs.ListKeys.Size & \" video clip";
_info = BA.NumberToString(parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._listkeys /*anywheresoftware.b4a.objects.collections.List*/ ().getSize())+" video clips"+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 626;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 627;BA.debugLine="r.Target = r.RunStaticMethod(\"java.lang.Runtime";
_r.Target = _r.RunStaticMethod("java.lang.Runtime","getRuntime",(Object[])(anywheresoftware.b4a.keywords.Common.Null),(String[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 628;BA.debugLine="info = info & \"Available memory: \" & NumberForm";
_info = _info+"Available memory: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((((double)(BA.ObjectToNumber(_r.RunMethod("maxMemory")))-(double)(BA.ObjectToNumber(_r.RunMethod("totalMemory"))))/(double)(1024*1024)),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+" MB"+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 629;BA.debugLine="info = info & \"Free memory: \" & NumberFormat2(G";
_info = _info+"Free memory: "+anywheresoftware.b4a.keywords.Common.NumberFormat2(_getfreemem()/(double)1000,(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+" MB";
 //BA.debugLineNum = 630;BA.debugLine="Msgbox(info,\"Smart Home Monitor\") 'ignore";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence(_info),BA.ObjectToCharSequence("Smart Home Monitor"),mostCurrent.activityBA);
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 632;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 633;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets,";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 634;BA.debugLine="If  Msgbox2(\"Delete all local video clips?\", \"S";
if (true) break;

case 19:
//if
this.state = 22;
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Delete all local video clips?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 635;BA.debugLine="Starter.kvs.DeleteAll";
parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._deleteall /*String*/ ();
 //BA.debugLineNum = 636;BA.debugLine="clvActivity.Clear";
parent.mostCurrent._clvactivity._clear();
 //BA.debugLineNum = 638;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 639;BA.debugLine="wait for (rs) complete (Results As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 77;
return;
case 77:
//C
this.state = 22;
_results = (Object) result[0];
;
 //BA.debugLineNum = 642;BA.debugLine="Dim rs As ResumableSub = GetVideos(response)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getvideos(parent._response);
 //BA.debugLineNum = 643;BA.debugLine="wait for (rs) complete (Results As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 78;
return;
case 78:
//C
this.state = 22;
_results = (Object) result[0];
;
 if (true) break;

case 22:
//C
this.state = 73;
;
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 646;BA.debugLine="Try";
if (true) break;

case 25:
//try
this.state = 72;
this.catchState = 71;
this.state = 27;
if (true) break;

case 27:
//C
this.state = 28;
this.catchState = 71;
 //BA.debugLineNum = 647;BA.debugLine="Dim Result As Int";
_result = 0;
 //BA.debugLineNum = 648;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 649;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets,";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 650;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (true) break;

case 28:
//if
this.state = 69;
if (parent.mostCurrent._tabstrip1.getCurrentPage()==2) { 
this.state = 30;
}else if(parent.mostCurrent._tabstrip1.getCurrentPage()==1) { 
this.state = 40;
}else if(parent.mostCurrent._tabstrip1.getCurrentPage()==3) { 
this.state = 50;
}else {
this.state = 60;
}if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 651;BA.debugLine="Result = Msgbox2(\"Restart the BASEMENT contro";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 652;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 31:
//if
this.state = 38;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 33;
}if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 653;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (true) break;

case 34:
//if
this.state = 37;
if (parent._mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || parent._mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 36;
}if (true) break;

case 36:
//C
this.state = 37;
 //BA.debugLineNum = 654;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 37:
//C
this.state = 38;
;
 //BA.debugLineNum = 656;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringT";
parent._mqtt.Publish("TempHumidBasement",parent._bc.StringToBytes("Restart controller","utf8"));
 if (true) break;

case 38:
//C
this.state = 69;
;
 if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 659;BA.debugLine="Result = Msgbox2(\"Restart the AIR QUALITY con";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 660;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 661;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
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
 //BA.debugLineNum = 662;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 47:
//C
this.state = 48;
;
 //BA.debugLineNum = 664;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Restar";
parent._mqtt.Publish("MQ7",parent._bc.StringToBytes("Restart controller","utf8"));
 if (true) break;

case 48:
//C
this.state = 69;
;
 if (true) break;

case 50:
//C
this.state = 51;
 //BA.debugLineNum = 667;BA.debugLine="Result = Msgbox2(\"Restart the BASEMENT AIR QU";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 668;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 669;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
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
 //BA.debugLineNum = 670;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 57:
//C
this.state = 58;
;
 //BA.debugLineNum = 672;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes";
parent._mqtt.Publish("MQ7Basement",parent._bc.StringToBytes("Restart controller","utf8"));
 if (true) break;

case 58:
//C
this.state = 69;
;
 if (true) break;

case 60:
//C
this.state = 61;
 //BA.debugLineNum = 675;BA.debugLine="Result = Msgbox2(\"Restart the WEATHER control";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the WEATHER controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 676;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 61:
//if
this.state = 68;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 63;
}if (true) break;

case 63:
//C
this.state = 64;
 //BA.debugLineNum = 677;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (true) break;

case 64:
//if
this.state = 67;
if (parent._mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || parent._mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 66;
}if (true) break;

case 66:
//C
this.state = 67;
 //BA.debugLineNum = 678;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 67:
//C
this.state = 68;
;
 //BA.debugLineNum = 680;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"";
parent._mqtt.Publish("TempHumid",parent._bc.StringToBytes("Restart controller","utf8"));
 if (true) break;

case 68:
//C
this.state = 69;
;
 if (true) break;

case 69:
//C
this.state = 72;
;
 if (true) break;

case 71:
//C
this.state = 72;
this.catchState = 75;
 //BA.debugLineNum = 684;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("31245265",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 72:
//C
this.state = 73;
this.catchState = 75;
;
 if (true) break;

case 73:
//C
this.state = 76;
;
 if (true) break;

case 75:
//C
this.state = 76;
this.catchState = 0;
 //BA.debugLineNum = 688;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("31245269",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 76:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 690;BA.debugLine="End Sub";
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
public static anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper  _blur() throws Exception{
b4a.example.bitmapcreator _bcbitmap = null;
int _reducescale = 0;
int _count = 0;
b4a.example.bitmapcreator._argbcolor[] _clrs = null;
b4a.example.bitmapcreator._argbcolor _temp = null;
int _m = 0;
int _steps = 0;
int _y = 0;
int _x = 0;
 //BA.debugLineNum = 2204;BA.debugLine="Private Sub Blur () As B4XBitmap";
 //BA.debugLineNum = 2205;BA.debugLine="Dim bcBitmap As BitmapCreator";
_bcbitmap = new b4a.example.bitmapcreator();
 //BA.debugLineNum = 2206;BA.debugLine="Dim ReduceScale As Int = 2";
_reducescale = (int) (2);
 //BA.debugLineNum = 2207;BA.debugLine="bcBitmap.Initialize(bmp.Width / ReduceScale / bmp";
_bcbitmap._initialize(processBA,(int) (_bmp.getWidth()/(double)_reducescale/(double)_bmp.getScale()),(int) (_bmp.getHeight()/(double)_reducescale/(double)_bmp.getScale()));
 //BA.debugLineNum = 2208;BA.debugLine="bcBitmap.CopyPixelsFromBitmap(bmp)";
_bcbitmap._copypixelsfrombitmap(_bmp);
 //BA.debugLineNum = 2209;BA.debugLine="Dim count As Int = 3";
_count = (int) (3);
 //BA.debugLineNum = 2210;BA.debugLine="Dim clrs(3) As ARGBColor";
_clrs = new b4a.example.bitmapcreator._argbcolor[(int) (3)];
{
int d0 = _clrs.length;
for (int i0 = 0;i0 < d0;i0++) {
_clrs[i0] = new b4a.example.bitmapcreator._argbcolor();
}
}
;
 //BA.debugLineNum = 2211;BA.debugLine="Dim temp As ARGBColor";
_temp = new b4a.example.bitmapcreator._argbcolor();
 //BA.debugLineNum = 2212;BA.debugLine="Dim m As Int";
_m = 0;
 //BA.debugLineNum = 2213;BA.debugLine="For steps = 1 To count";
{
final int step9 = 1;
final int limit9 = _count;
_steps = (int) (1) ;
for (;_steps <= limit9 ;_steps = _steps + step9 ) {
 //BA.debugLineNum = 2214;BA.debugLine="For y = 0 To bcBitmap.mHeight - 1";
{
final int step10 = 1;
final int limit10 = (int) (_bcbitmap._mheight-1);
_y = (int) (0) ;
for (;_y <= limit10 ;_y = _y + step10 ) {
 //BA.debugLineNum = 2215;BA.debugLine="For x = 0 To 2";
{
final int step11 = 1;
final int limit11 = (int) (2);
_x = (int) (0) ;
for (;_x <= limit11 ;_x = _x + step11 ) {
 //BA.debugLineNum = 2216;BA.debugLine="bcBitmap.GetARGB(x, y, clrs(x))";
_bcbitmap._getargb(_x,_y,_clrs[_x]);
 }
};
 //BA.debugLineNum = 2218;BA.debugLine="SetAvg(bcBitmap, 1, y, clrs, temp)";
_setavg(_bcbitmap,(int) (1),_y,_clrs,_temp);
 //BA.debugLineNum = 2219;BA.debugLine="m = 0";
_m = (int) (0);
 //BA.debugLineNum = 2220;BA.debugLine="For x = 2 To bcBitmap.mWidth - 2";
{
final int step16 = 1;
final int limit16 = (int) (_bcbitmap._mwidth-2);
_x = (int) (2) ;
for (;_x <= limit16 ;_x = _x + step16 ) {
 //BA.debugLineNum = 2221;BA.debugLine="bcBitmap.GetARGB(x + 1, y, clrs(m))";
_bcbitmap._getargb((int) (_x+1),_y,_clrs[_m]);
 //BA.debugLineNum = 2222;BA.debugLine="m = (m + 1) Mod clrs.Length";
_m = (int) ((_m+1)%_clrs.length);
 //BA.debugLineNum = 2223;BA.debugLine="SetAvg(bcBitmap, x, y, clrs, temp)";
_setavg(_bcbitmap,_x,_y,_clrs,_temp);
 }
};
 }
};
 //BA.debugLineNum = 2226;BA.debugLine="For x = 0 To bcBitmap.mWidth - 1";
{
final int step22 = 1;
final int limit22 = (int) (_bcbitmap._mwidth-1);
_x = (int) (0) ;
for (;_x <= limit22 ;_x = _x + step22 ) {
 //BA.debugLineNum = 2227;BA.debugLine="For y = 0 To 2";
{
final int step23 = 1;
final int limit23 = (int) (2);
_y = (int) (0) ;
for (;_y <= limit23 ;_y = _y + step23 ) {
 //BA.debugLineNum = 2228;BA.debugLine="bcBitmap.GetARGB(x, y, clrs(y))";
_bcbitmap._getargb(_x,_y,_clrs[_y]);
 }
};
 //BA.debugLineNum = 2230;BA.debugLine="SetAvg(bcBitmap, x, 1, clrs, temp)";
_setavg(_bcbitmap,_x,(int) (1),_clrs,_temp);
 //BA.debugLineNum = 2231;BA.debugLine="m = 0";
_m = (int) (0);
 //BA.debugLineNum = 2232;BA.debugLine="For y = 2 To bcBitmap.mHeight - 2";
{
final int step28 = 1;
final int limit28 = (int) (_bcbitmap._mheight-2);
_y = (int) (2) ;
for (;_y <= limit28 ;_y = _y + step28 ) {
 //BA.debugLineNum = 2233;BA.debugLine="bcBitmap.GetARGB(x, y + 1, clrs(m))";
_bcbitmap._getargb(_x,(int) (_y+1),_clrs[_m]);
 //BA.debugLineNum = 2234;BA.debugLine="m = (m + 1) Mod clrs.Length";
_m = (int) ((_m+1)%_clrs.length);
 //BA.debugLineNum = 2235;BA.debugLine="SetAvg(bcBitmap, x, y, clrs, temp)";
_setavg(_bcbitmap,_x,_y,_clrs,_temp);
 }
};
 }
};
 }
};
 //BA.debugLineNum = 2239;BA.debugLine="Return bcBitmap.Bitmap";
if (true) return _bcbitmap._getbitmap();
 //BA.debugLineNum = 2240;BA.debugLine="End Sub";
return null;
}
public static String  _bluriv(String _image,anywheresoftware.b4a.objects.ImageViewWrapper _iv) throws Exception{
 //BA.debugLineNum = 2194;BA.debugLine="Sub BlurIV (image As String,iv As ImageView)";
 //BA.debugLineNum = 2195;BA.debugLine="Try";
try { //BA.debugLineNum = 2196;BA.debugLine="bmp = xui.LoadBitmapResize(File.DirInternal, ima";
_bmp = mostCurrent._xui.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_image,_iv.getWidth(),_iv.getHeight(),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2197;BA.debugLine="iv.Bitmap = (Blur)";
_iv.setBitmap((android.graphics.Bitmap)((_blur()).getObject()));
 } 
       catch (Exception e5) {
			processBA.setLastException(e5); //BA.debugLineNum = 2199;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("33538949",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 2202;BA.debugLine="End Sub";
return "";
}
public static void  _btndriveway_click() throws Exception{
ResumableSub_btnDriveway_Click rsub = new ResumableSub_btnDriveway_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnDriveway_Click extends BA.ResumableSub {
public ResumableSub_btnDriveway_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2150;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 2151;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"0";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 2152;BA.debugLine="If Msgbox2(\"Capture new camera thumbnails?\", \"Sma";
if (true) break;

case 1:
//if
this.state = 4;
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Capture new camera thumbnails?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2153;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 2154;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2155;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2156;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2157;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2158;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2159;BA.debugLine="btnDrivewayRefresh.Enabled = False";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2160;BA.debugLine="btnFrontDoorRefresh.Enabled = False";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2161;BA.debugLine="btnSideYardRefresh.Enabled = False";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2162;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2163;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2164;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2165;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",parent.mostCurrent._ivdriveway);
 //BA.debugLineNum = 2166;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",parent.mostCurrent._ivfrontdoor);
 //BA.debugLineNum = 2167;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",parent.mostCurrent._ivsideyard);
 //BA.debugLineNum = 2171;BA.debugLine="Dim rs As ResumableSub = RefreshCameras(False, \"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _refreshcameras(anywheresoftware.b4a.keywords.Common.False,"347574");
 //BA.debugLineNum = 2172;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 5;
return;
case 5:
//C
this.state = 4;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2174;BA.debugLine="Dim rs As ResumableSub = RefreshCameras(False, \"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _refreshcameras(anywheresoftware.b4a.keywords.Common.False,"236967");
 //BA.debugLineNum = 2175;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 6;
return;
case 6:
//C
this.state = 4;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2177;BA.debugLine="Dim rs As ResumableSub = RefreshCameras(False, \"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _refreshcameras(anywheresoftware.b4a.keywords.Common.False,"458236");
 //BA.debugLineNum = 2178;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 7;
return;
case 7:
//C
this.state = 4;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2180;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2181;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2182;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2183;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2184;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2185;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2186;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2187;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2188;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2189;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2190;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 2192;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _btndrivewaynewclip_click() throws Exception{
ResumableSub_btnDrivewayNewClip_Click rsub = new ResumableSub_btnDrivewayNewClip_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnDrivewayNewClip_Click extends BA.ResumableSub {
public ResumableSub_btnDrivewayNewClip_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.objects.IntentWrapper _intent1 = null;
anywheresoftware.b4a.phone.Phone _phone = null;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
int _i = 0;
int step42;
int limit42;

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
 //BA.debugLineNum = 2835;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 2836;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"0";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 2837;BA.debugLine="If Msgbox2(\"Capture a new video from the Driveway";
if (true) break;

case 1:
//if
this.state = 24;
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Capture a new video from the Driveway camera?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2838;BA.debugLine="Try";
if (true) break;

case 4:
//try
this.state = 23;
this.catchState = 22;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 7;
this.catchState = 22;
 //BA.debugLineNum = 2840;BA.debugLine="Dim Intent1 As Intent";
_intent1 = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 2841;BA.debugLine="Intent1.Initialize(\"blink.liveview.driveway\", \"";
_intent1.Initialize("blink.liveview.driveway","");
 //BA.debugLineNum = 2842;BA.debugLine="Dim Phone As Phone";
_phone = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 2843;BA.debugLine="Phone.SendBroadcastIntent(Intent1)";
_phone.SendBroadcastIntent((android.content.Intent)(_intent1.getObject()));
 //BA.debugLineNum = 2845;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2846;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2847;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2848;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2849;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2850;BA.debugLine="btnDrivewayRefresh.Enabled = False";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2851;BA.debugLine="btnFrontDoorRefresh.Enabled = False";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2852;BA.debugLine="btnSideYardRefresh.Enabled = False";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2853;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2854;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2855;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2856;BA.debugLine="lblStatus.Text = \"Capturing a new Driveway vide";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Driveway video clip..."));
 //BA.debugLineNum = 2858;BA.debugLine="Dim rs As ResumableSub = RESTPost(\"https://rest";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restpost("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/347574/clip");
 //BA.debugLineNum = 2859;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 25;
return;
case 25:
//C
this.state = 7;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2860;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.C";
if (true) break;

case 7:
//if
this.state = 10;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 2861;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2862;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2863;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2864;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2865;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2866;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2867;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2868;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2869;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2870;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2871;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2872;BA.debugLine="lblStatus.Text = response";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(parent._response));
 //BA.debugLineNum = 2873;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 2876;BA.debugLine="Dim rs As ResumableSub = GetCommandID(response)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcommandid(parent._response);
 //BA.debugLineNum = 2877;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 26;
return;
case 26:
//C
this.state = 11;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2879;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 2880;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 27;
return;
case 27:
//C
this.state = 11;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2882;BA.debugLine="For i = 1 To 70";
if (true) break;

case 11:
//for
this.state = 20;
step42 = 1;
limit42 = (int) (70);
_i = (int) (1) ;
this.state = 28;
if (true) break;

case 28:
//C
this.state = 20;
if ((step42 > 0 && _i <= limit42) || (step42 < 0 && _i >= limit42)) this.state = 13;
if (true) break;

case 29:
//C
this.state = 28;
_i = ((int)(0 + _i + step42)) ;
if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 2884;BA.debugLine="Dim rs As ResumableSub = GetCommandStatus(resp";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcommandstatus(parent._response);
 //BA.debugLineNum = 2885;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 30;
return;
case 30:
//C
this.state = 14;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2886;BA.debugLine="If commandComplete Then";
if (true) break;

case 14:
//if
this.state = 19;
if (parent._commandcomplete) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 19;
 //BA.debugLineNum = 2891;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 2892;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 31;
return;
case 31:
//C
this.state = 19;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2893;BA.debugLine="Exit";
this.state = 20;
if (true) break;
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 2895;BA.debugLine="lblStatus.Text = \"Awaiting for the Driveway v";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Driveway video clip... "+BA.NumberToString(_i)+"/70"));
 if (true) break;

case 19:
//C
this.state = 29;
;
 //BA.debugLineNum = 2898;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 2899;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 32;
return;
case 32:
//C
this.state = 29;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2900;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 33;
return;
case 33:
//C
this.state = 29;
;
 if (true) break;
if (true) break;

case 20:
//C
this.state = 23;
;
 if (true) break;

case 22:
//C
this.state = 23;
this.catchState = 0;
 //BA.debugLineNum = 2903;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("34784197",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 23:
//C
this.state = 24;
this.catchState = 0;
;
 //BA.debugLineNum = 2905;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2906;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2907;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2908;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2909;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2910;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2911;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2912;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2913;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2914;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2915;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2916;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 24:
//C
this.state = -1;
;
 //BA.debugLineNum = 2918;BA.debugLine="End Sub";
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
public static void  _btndrivewayrefresh_click() throws Exception{
ResumableSub_btnDrivewayRefresh_Click rsub = new ResumableSub_btnDrivewayRefresh_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnDrivewayRefresh_Click extends BA.ResumableSub {
public ResumableSub_btnDrivewayRefresh_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 3277;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3278;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3279;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3280;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3281;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3282;BA.debugLine="btnDrivewayRefresh.Enabled = False";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3283;BA.debugLine="btnFrontDoorRefresh.Enabled = False";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3284;BA.debugLine="btnSideYardRefresh.Enabled = False";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3285;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3286;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3287;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3288;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 3289;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",parent.mostCurrent._ivdriveway);
 //BA.debugLineNum = 3290;BA.debugLine="Dim rs As ResumableSub = RefreshCameras(False, \"3";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _refreshcameras(anywheresoftware.b4a.keywords.Common.False,"347574");
 //BA.debugLineNum = 3291;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 1;
return;
case 1:
//C
this.state = -1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 3292;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3293;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3294;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3295;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3296;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3297;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3298;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3299;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3300;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3301;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3302;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3303;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _btnfrontdoornewclip_click() throws Exception{
ResumableSub_btnFrontDoorNewClip_Click rsub = new ResumableSub_btnFrontDoorNewClip_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnFrontDoorNewClip_Click extends BA.ResumableSub {
public ResumableSub_btnFrontDoorNewClip_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.objects.IntentWrapper _intent1 = null;
anywheresoftware.b4a.phone.Phone _phone = null;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
int _i = 0;
int step43;
int limit43;

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
 //BA.debugLineNum = 2921;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 2922;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"0";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 2923;BA.debugLine="If Msgbox2(\"Capture a new video from the Front Do";
if (true) break;

case 1:
//if
this.state = 24;
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Capture a new video from the Front Door camera?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2924;BA.debugLine="Try";
if (true) break;

case 4:
//try
this.state = 23;
this.catchState = 22;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 7;
this.catchState = 22;
 //BA.debugLineNum = 2926;BA.debugLine="Dim Intent1 As Intent";
_intent1 = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 2927;BA.debugLine="Intent1.Initialize(\"blink.liveview.frontdoor\",";
_intent1.Initialize("blink.liveview.frontdoor","");
 //BA.debugLineNum = 2928;BA.debugLine="Dim Phone As Phone";
_phone = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 2929;BA.debugLine="Phone.SendBroadcastIntent(Intent1)";
_phone.SendBroadcastIntent((android.content.Intent)(_intent1.getObject()));
 //BA.debugLineNum = 2931;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 2932;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2933;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2934;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2935;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2936;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2937;BA.debugLine="btnDrivewayRefresh.Enabled = False";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2938;BA.debugLine="btnFrontDoorRefresh.Enabled = False";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2939;BA.debugLine="btnSideYardRefresh.Enabled = False";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2940;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2941;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2942;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2943;BA.debugLine="lblStatus.Text = \"Capturing a new Front Door vi";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Front Door video clip..."));
 //BA.debugLineNum = 2945;BA.debugLine="Dim rs As ResumableSub = RESTPost(\"https://rest";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restpost("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/236967/clip");
 //BA.debugLineNum = 2946;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 25;
return;
case 25:
//C
this.state = 7;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2947;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.C";
if (true) break;

case 7:
//if
this.state = 10;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 2948;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2949;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2950;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2951;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2952;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2953;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2954;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2955;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2956;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2957;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2958;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2959;BA.debugLine="lblStatus.Text = response";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(parent._response));
 //BA.debugLineNum = 2960;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 2963;BA.debugLine="Dim rs As ResumableSub = GetCommandID(response)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcommandid(parent._response);
 //BA.debugLineNum = 2964;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 26;
return;
case 26:
//C
this.state = 11;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2966;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 2967;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 27;
return;
case 27:
//C
this.state = 11;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2968;BA.debugLine="For i = 1 To 70";
if (true) break;

case 11:
//for
this.state = 20;
step43 = 1;
limit43 = (int) (70);
_i = (int) (1) ;
this.state = 28;
if (true) break;

case 28:
//C
this.state = 20;
if ((step43 > 0 && _i <= limit43) || (step43 < 0 && _i >= limit43)) this.state = 13;
if (true) break;

case 29:
//C
this.state = 28;
_i = ((int)(0 + _i + step43)) ;
if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 2970;BA.debugLine="Dim rs As ResumableSub = GetCommandStatus(resp";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcommandstatus(parent._response);
 //BA.debugLineNum = 2971;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 30;
return;
case 30:
//C
this.state = 14;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2972;BA.debugLine="If commandComplete Then";
if (true) break;

case 14:
//if
this.state = 19;
if (parent._commandcomplete) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 19;
 //BA.debugLineNum = 2977;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 2978;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 31;
return;
case 31:
//C
this.state = 19;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2979;BA.debugLine="Exit";
this.state = 20;
if (true) break;
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 2981;BA.debugLine="lblStatus.Text = \"Awaiting for the Front Door";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Front Door video clip... "+BA.NumberToString(_i)+"/70"));
 if (true) break;

case 19:
//C
this.state = 29;
;
 //BA.debugLineNum = 2984;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 2985;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 32;
return;
case 32:
//C
this.state = 29;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2986;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 33;
return;
case 33:
//C
this.state = 29;
;
 if (true) break;
if (true) break;

case 20:
//C
this.state = 23;
;
 if (true) break;

case 22:
//C
this.state = 23;
this.catchState = 0;
 //BA.debugLineNum = 2989;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("34849733",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 23:
//C
this.state = 24;
this.catchState = 0;
;
 //BA.debugLineNum = 2991;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2992;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2993;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2994;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2995;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2996;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2997;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2998;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2999;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3000;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3001;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3002;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 24:
//C
this.state = -1;
;
 //BA.debugLineNum = 3004;BA.debugLine="End Sub";
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
public static void  _btnfrontdoorrefresh_click() throws Exception{
ResumableSub_btnFrontDoorRefresh_Click rsub = new ResumableSub_btnFrontDoorRefresh_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnFrontDoorRefresh_Click extends BA.ResumableSub {
public ResumableSub_btnFrontDoorRefresh_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 3248;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3249;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3250;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3251;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3252;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3253;BA.debugLine="btnDrivewayRefresh.Enabled = False";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3254;BA.debugLine="btnFrontDoorRefresh.Enabled = False";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3255;BA.debugLine="btnSideYardRefresh.Enabled = False";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3256;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3257;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3258;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3259;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 3260;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",parent.mostCurrent._ivfrontdoor);
 //BA.debugLineNum = 3261;BA.debugLine="Dim rs As ResumableSub = RefreshCameras(False, \"2";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _refreshcameras(anywheresoftware.b4a.keywords.Common.False,"236967");
 //BA.debugLineNum = 3262;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 1;
return;
case 1:
//C
this.state = -1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 3263;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3264;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3265;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3266;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3267;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3268;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3269;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3270;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3271;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3272;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3273;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3274;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _btnrefresh_click() throws Exception{
ResumableSub_btnRefresh_Click rsub = new ResumableSub_btnRefresh_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnRefresh_Click extends BA.ResumableSub {
public ResumableSub_btnRefresh_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 3092;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3093;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3094;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3095;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3096;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3097;BA.debugLine="btnDrivewayRefresh.Enabled = False";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3098;BA.debugLine="btnFrontDoorRefresh.Enabled = False";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3099;BA.debugLine="btnSideYardRefresh.Enabled = False";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3100;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3101;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3102;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3103;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 3104;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",parent.mostCurrent._ivdriveway);
 //BA.debugLineNum = 3105;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",parent.mostCurrent._ivfrontdoor);
 //BA.debugLineNum = 3106;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",parent.mostCurrent._ivsideyard);
 //BA.debugLineNum = 3107;BA.debugLine="Dim rs As ResumableSub = RefreshCameras(True, \"Al";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _refreshcameras(anywheresoftware.b4a.keywords.Common.True,"All");
 //BA.debugLineNum = 3108;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 1;
return;
case 1:
//C
this.state = -1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 3109;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3110;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3111;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3112;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3113;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3114;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3115;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3116;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3117;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3118;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3119;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3120;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _btnsideyardnewclip_click() throws Exception{
ResumableSub_btnSideYardNewClip_Click rsub = new ResumableSub_btnSideYardNewClip_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnSideYardNewClip_Click extends BA.ResumableSub {
public ResumableSub_btnSideYardNewClip_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.objects.IntentWrapper _intent1 = null;
anywheresoftware.b4a.phone.Phone _phone = null;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
int _i = 0;
int step42;
int limit42;

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
 //BA.debugLineNum = 3007;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 3008;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"0";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 3009;BA.debugLine="If Msgbox2(\"Capture a new video from the Side Yar";
if (true) break;

case 1:
//if
this.state = 24;
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Capture a new video from the Side Yard camera?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 3010;BA.debugLine="Try";
if (true) break;

case 4:
//try
this.state = 23;
this.catchState = 22;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 7;
this.catchState = 22;
 //BA.debugLineNum = 3012;BA.debugLine="Dim Intent1 As Intent";
_intent1 = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 3013;BA.debugLine="Intent1.Initialize(\"blink.liveview.sideyard\", \"";
_intent1.Initialize("blink.liveview.sideyard","");
 //BA.debugLineNum = 3014;BA.debugLine="Dim Phone As Phone";
_phone = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 3015;BA.debugLine="Phone.SendBroadcastIntent(Intent1)";
_phone.SendBroadcastIntent((android.content.Intent)(_intent1.getObject()));
 //BA.debugLineNum = 3017;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 3018;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3019;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3020;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3021;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3022;BA.debugLine="btnDrivewayRefresh.Enabled = False";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3023;BA.debugLine="btnFrontDoorRefresh.Enabled = False";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3024;BA.debugLine="btnSideYardRefresh.Enabled = False";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3025;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3026;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3027;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3028;BA.debugLine="lblStatus.Text = \"Capturing a new Side Yard vid";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Side Yard video clip..."));
 //BA.debugLineNum = 3030;BA.debugLine="Dim rs As ResumableSub = RESTPost(\"https://rest";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restpost("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/458236/clip");
 //BA.debugLineNum = 3031;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 25;
return;
case 25:
//C
this.state = 7;
_result = (Object) result[0];
;
 //BA.debugLineNum = 3032;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.C";
if (true) break;

case 7:
//if
this.state = 10;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 3033;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3034;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3035;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3036;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3037;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3038;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3039;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3040;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3041;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3042;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3043;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3044;BA.debugLine="lblStatus.Text = response";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(parent._response));
 //BA.debugLineNum = 3045;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 3048;BA.debugLine="Dim rs As ResumableSub = GetCommandID(response)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcommandid(parent._response);
 //BA.debugLineNum = 3049;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 26;
return;
case 26:
//C
this.state = 11;
_result = (Object) result[0];
;
 //BA.debugLineNum = 3051;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 3052;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 27;
return;
case 27:
//C
this.state = 11;
_result = (Object) result[0];
;
 //BA.debugLineNum = 3053;BA.debugLine="For i = 1 To 70";
if (true) break;

case 11:
//for
this.state = 20;
step42 = 1;
limit42 = (int) (70);
_i = (int) (1) ;
this.state = 28;
if (true) break;

case 28:
//C
this.state = 20;
if ((step42 > 0 && _i <= limit42) || (step42 < 0 && _i >= limit42)) this.state = 13;
if (true) break;

case 29:
//C
this.state = 28;
_i = ((int)(0 + _i + step42)) ;
if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 3055;BA.debugLine="Dim rs As ResumableSub = GetCommandStatus(resp";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcommandstatus(parent._response);
 //BA.debugLineNum = 3056;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 30;
return;
case 30:
//C
this.state = 14;
_result = (Object) result[0];
;
 //BA.debugLineNum = 3057;BA.debugLine="If commandComplete Then";
if (true) break;

case 14:
//if
this.state = 19;
if (parent._commandcomplete) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 19;
 //BA.debugLineNum = 3062;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 3063;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 31;
return;
case 31:
//C
this.state = 19;
_result = (Object) result[0];
;
 //BA.debugLineNum = 3064;BA.debugLine="Exit";
this.state = 20;
if (true) break;
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 3066;BA.debugLine="lblStatus.Text = \"Awaiting for the Side Yard";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Side Yard video clip... "+BA.NumberToString(_i)+"/70"));
 if (true) break;

case 19:
//C
this.state = 29;
;
 //BA.debugLineNum = 3069;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 3070;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 32;
return;
case 32:
//C
this.state = 29;
_result = (Object) result[0];
;
 //BA.debugLineNum = 3071;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 33;
return;
case 33:
//C
this.state = 29;
;
 if (true) break;
if (true) break;

case 20:
//C
this.state = 23;
;
 if (true) break;

case 22:
//C
this.state = 23;
this.catchState = 0;
 //BA.debugLineNum = 3074;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("34915268",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 23:
//C
this.state = 24;
this.catchState = 0;
;
 //BA.debugLineNum = 3076;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3077;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3078;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3079;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3080;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3081;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3082;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3083;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3084;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3085;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3086;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3087;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 24:
//C
this.state = -1;
;
 //BA.debugLineNum = 3089;BA.debugLine="End Sub";
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
public static void  _btnsideyardrefresh_click() throws Exception{
ResumableSub_btnSideYardRefresh_Click rsub = new ResumableSub_btnSideYardRefresh_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnSideYardRefresh_Click extends BA.ResumableSub {
public ResumableSub_btnSideYardRefresh_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 3219;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3220;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3221;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3222;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3223;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3224;BA.debugLine="btnDrivewayRefresh.Enabled = False";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3225;BA.debugLine="btnFrontDoorRefresh.Enabled = False";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3226;BA.debugLine="btnSideYardRefresh.Enabled = False";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3227;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3228;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3229;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3230;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 3231;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",parent.mostCurrent._ivsideyard);
 //BA.debugLineNum = 3232;BA.debugLine="Dim rs As ResumableSub = RefreshCameras(False, \"4";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _refreshcameras(anywheresoftware.b4a.keywords.Common.False,"458236");
 //BA.debugLineNum = 3233;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 1;
return;
case 1:
//C
this.state = -1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 3234;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3235;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3236;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3237;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3238;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3239;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3240;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3241;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3242;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3243;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3244;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3245;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
>>>>>>> origin/master
}
public static String  _checkairqualitysetting() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _status = "";
String[] _a = null;
long _tomorrow = 0L;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 712;BA.debugLine="Sub CheckAirQualitySetting";
 //BA.debugLineNum = 713;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 714;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 715;BA.debugLine="Try";
try { //BA.debugLineNum = 716;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 717;BA.debugLine="status = StateManager.GetSetting(\"AirQuality\")";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"AirQuality");
 //BA.debugLineNum = 718;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 719;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 720;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 721;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 722;BA.debugLine="GaugeAirQuality.CurrentValue = (a(0)/10)";
mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float) (((double)(Double.parseDouble(_a[(int) (0)]))/(double)10)));
 //BA.debugLineNum = 723;BA.debugLine="If (a(0)/10) > 40 Then";
if (((double)(Double.parseDouble(_a[(int) (0)]))/(double)10)>40) { 
 //BA.debugLineNum = 724;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getairquality((int) (((double)(Double.parseDouble(_a[(int) (0)]))/(double)10))))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 726;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int) (((double)(Double.parseDouble(_a[(int) (0)]))/(double)10))))).PopAll().getObject()));
 };
 //BA.debugLineNum = 728;BA.debugLine="If a(1) = \"\" Then";
if ((_a[(int) (1)]).equals("")) { 
 //BA.debugLineNum = 729;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 730;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 731;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 732;BA.debugLine="a(1) = DateTime.Date(Tomorrow)";
_a[(int) (1)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 734;BA.debugLine="If a(2).Contains(\"|24:\") Then";
if (_a[(int) (2)].contains("|24:")) { 
 //BA.debugLineNum = 735;BA.debugLine="a(2) = a(2).Replace(\"|24:\",\"|00:\")";
_a[(int) (2)] = _a[(int) (2)].replace("|24:","|00:");
 //BA.debugLineNum = 736;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 737;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 738;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 739;BA.debugLine="a(2) = DateTime.Date(Tomorrow)";
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
 //BA.debugLineNum = 747;BA.debugLine="If p.Minutes >= 5 Or p.Minutes <= -5 Then";
if (_p.Minutes>=5 || _p.Minutes<=-5) { 
 //BA.debugLineNum = 748;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 750;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 753;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e44) {
<<<<<<< HEAD
			processBA.setLastException(e44); //BA.debugLineNum = 757;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("61900589",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 758;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.Bol";
=======
			processBA.setLastException(e44); //BA.debugLineNum = 1011;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("31966125",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1012;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.Bol";
>>>>>>> origin/master
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 760;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 762;BA.debugLine="Sub CheckAirQualitySettingBasement";
 //BA.debugLineNum = 763;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 764;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 765;BA.debugLine="Try";
try { //BA.debugLineNum = 766;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 767;BA.debugLine="status = StateManager.GetSetting(\"AirQualityBase";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"AirQualityBasement");
 //BA.debugLineNum = 768;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 769;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 770;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 771;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 772;BA.debugLine="GaugeAirQualityBasement.CurrentValue = (a(0)/1";
mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float) (((double)(Double.parseDouble(_a[(int) (0)]))/(double)10)));
 //BA.debugLineNum = 773;BA.debugLine="If (a(0)/10) > 40 Then";
if (((double)(Double.parseDouble(_a[(int) (0)]))/(double)10)>40) { 
 //BA.debugLineNum = 774;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bo";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getairquality((int) (((double)(Double.parseDouble(_a[(int) (0)]))/(double)10))))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 776;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bo";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int) (((double)(Double.parseDouble(_a[(int) (0)]))/(double)10))))).PopAll().getObject()));
 };
 //BA.debugLineNum = 778;BA.debugLine="If a(1) = \"\" Then";
if ((_a[(int) (1)]).equals("")) { 
 //BA.debugLineNum = 779;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 780;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 781;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 782;BA.debugLine="a(1) = DateTime.Date(Tomorrow)";
_a[(int) (1)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 784;BA.debugLine="If a(2).Contains(\"|24:\") Then";
if (_a[(int) (2)].contains("|24:")) { 
 //BA.debugLineNum = 785;BA.debugLine="a(2) = a(2).Replace(\"|24:\",\"|00:\")";
_a[(int) (2)] = _a[(int) (2)].replace("|24:","|00:");
 //BA.debugLineNum = 786;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 787;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 788;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 789;BA.debugLine="a(2) = DateTime.Date(Tomorrow)";
_a[(int) (2)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 792;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 793;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 794;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 795;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 796;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 798;BA.debugLine="If p.Minutes >= 5 Or p.Minutes <= -5 Then";
if (_p.Minutes>=5 || _p.Minutes<=-5) { 
 //BA.debugLineNum = 799;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Ini";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 801;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Ini";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 804;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Init";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdatebasement.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e44) {
<<<<<<< HEAD
			processBA.setLastException(e44); //BA.debugLineNum = 808;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("61966126",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 809;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Initia";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 811;BA.debugLine="End Sub";
=======
			processBA.setLastException(e44); //BA.debugLineNum = 1062;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("32031662",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1063;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Initia";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 1065;BA.debugLine="End Sub";
return "";
}
public static String  _checklfrlevel(int _lfrlevel) throws Exception{
 //BA.debugLineNum = 2098;BA.debugLine="Sub CheckLFRLevel(lfrlevel As Int) As String";
 //BA.debugLineNum = 2099;BA.debugLine="Try";
try { //BA.debugLineNum = 2101;BA.debugLine="If lfrlevel > -67 Then";
if (_lfrlevel>-67) { 
 //BA.debugLineNum = 2102;BA.debugLine="Return \"Amazing\"";
if (true) return "Amazing";
 }else if(_lfrlevel>-70 && _lfrlevel<=-67) { 
 //BA.debugLineNum = 2104;BA.debugLine="Return \"Very good\"";
if (true) return "Very good";
 }else if(_lfrlevel>-80 && _lfrlevel<=-70) { 
 //BA.debugLineNum = 2106;BA.debugLine="Return \"OK\"";
if (true) return "OK";
 }else if(_lfrlevel>-90 && _lfrlevel<=-80) { 
 //BA.debugLineNum = 2108;BA.debugLine="Return \"Not Good\"";
if (true) return "Not Good";
 }else {
 //BA.debugLineNum = 2110;BA.debugLine="Return \"Unusable\"";
if (true) return "Unusable";
 };
 } 
       catch (Exception e14) {
			processBA.setLastException(e14); //BA.debugLineNum = 2113;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("33276815",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 2114;BA.debugLine="lblStatus.Text = \"CheckLFRLevel LastException: \"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("CheckLFRLevel LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))));
 //BA.debugLineNum = 2115;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 2117;BA.debugLine="End Sub";
>>>>>>> origin/master
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
 //BA.debugLineNum = 813;BA.debugLine="Sub CheckTempHumiditySetting";
 //BA.debugLineNum = 814;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 815;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 816;BA.debugLine="Try";
try { //BA.debugLineNum = 817;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 818;BA.debugLine="status = StateManager.GetSetting(\"TempHumidity\")";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"TempHumidity");
 //BA.debugLineNum = 819;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 820;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 821;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 822;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 823;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 824;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 825;BA.debugLine="GaugeTemp.CurrentValue = a(1)";
mostCurrent._gaugetemp._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 826;BA.debugLine="GaugeHumidity.CurrentValue = a(2)";
mostCurrent._gaugehumidity._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 827;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 828;BA.debugLine="If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6)) || (_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 829;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 831;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 833;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 };
 //BA.debugLineNum = 836;BA.debugLine="GaugeHeatIndex.CurrentValue = a(5)";
mostCurrent._gaugeheatindex._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 837;BA.debugLine="GaugeDewPoint.CurrentValue = a(6)";
mostCurrent._gaugedewpoint._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 838;BA.debugLine="If a(7) = \"\" Then";
if ((_a[(int) (7)]).equals("")) { 
 //BA.debugLineNum = 839;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 840;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 841;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 842;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 844;BA.debugLine="If a(8).Contains(\"|24:\") Then";
if (_a[(int) (8)].contains("|24:")) { 
 //BA.debugLineNum = 845;BA.debugLine="a(8) = a(8).Replace(\"|24:\",\"|00:\")";
_a[(int) (8)] = _a[(int) (8)].replace("|24:","|00:");
 //BA.debugLineNum = 846;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 847;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 848;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 849;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 851;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 852;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 853;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 854;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 855;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 857;BA.debugLine="If p.Minutes >= 5 Or p.Minutes <= -5 Then";
if (_p.Minutes>=5 || _p.Minutes<=-5) { 
 //BA.debugLineNum = 858;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appen";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 860;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appen";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 863;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e52) {
<<<<<<< HEAD
			processBA.setLastException(e52); //BA.debugLineNum = 867;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("62031670",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 868;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
=======
			processBA.setLastException(e52); //BA.debugLineNum = 1121;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("32097206",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1122;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
>>>>>>> origin/master
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 870;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 872;BA.debugLine="Sub CheckTempHumiditySettingBasement";
 //BA.debugLineNum = 873;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 874;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 875;BA.debugLine="Try";
try { //BA.debugLineNum = 876;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 877;BA.debugLine="status = StateManager.GetSetting(\"TempHumidityBa";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"TempHumidityBasement");
 //BA.debugLineNum = 878;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 879;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 880;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 881;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 882;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 883;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 884;BA.debugLine="GaugeTempBasement.CurrentValue = a(1)";
mostCurrent._gaugetempbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 885;BA.debugLine="GaugeHumidityBasement.CurrentValue = a(2)";
mostCurrent._gaugehumiditybasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 886;BA.debugLine="lblPerceptionBasement.Text = cs.Initialize.Bol";
mostCurrent._lblperceptionbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 887;BA.debugLine="If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6)) || (_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 888;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 890;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 892;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 };
 //BA.debugLineNum = 894;BA.debugLine="GaugeHeatIndexBasement.CurrentValue = a(5)";
mostCurrent._gaugeheatindexbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 895;BA.debugLine="GaugeDewPointBasement.CurrentValue = a(6)";
mostCurrent._gaugedewpointbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 896;BA.debugLine="If a(7) = \"\" Then";
if ((_a[(int) (7)]).equals("")) { 
 //BA.debugLineNum = 897;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 898;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 899;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 900;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 902;BA.debugLine="If a(8).Contains(\"|24:\") Then";
if (_a[(int) (8)].contains("|24:")) { 
 //BA.debugLineNum = 903;BA.debugLine="a(8) = a(8).Replace(\"|24:\",\"|00:\")";
_a[(int) (8)] = _a[(int) (8)].replace("|24:","|00:");
 //BA.debugLineNum = 904;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 905;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 906;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 907;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 909;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 910;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 911;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 912;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 913;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 915;BA.debugLine="If p.Minutes >= 5 Or p.Minutes <= -5 Then";
if (_p.Minutes>=5 || _p.Minutes<=-5) { 
 //BA.debugLineNum = 916;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bo";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 918;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bo";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 921;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bol";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdatebasement.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e52) {
<<<<<<< HEAD
			processBA.setLastException(e52); //BA.debugLineNum = 925;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("62097205",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 926;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bold.";
=======
			processBA.setLastException(e52); //BA.debugLineNum = 1179;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("32162741",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1180;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bold.";
>>>>>>> origin/master
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 928;BA.debugLine="End Sub";
return "";
}
<<<<<<< HEAD
=======
public static void  _clvactivity_itemclick(int _index,Object _value) throws Exception{
ResumableSub_clvActivity_ItemClick rsub = new ResumableSub_clvActivity_ItemClick(null,_index,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_clvActivity_ItemClick extends BA.ResumableSub {
public ResumableSub_clvActivity_ItemClick(cloyd.smart.home.monitor.main parent,int _index,Object _value) {
this.parent = parent;
this._index = _index;
this._value = _value;
}
cloyd.smart.home.monitor.main parent;
int _index;
Object _value;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.objects.B4XViewWrapper _contentlabel = null;
cloyd.smart.home.monitor.main._carddata _cd = null;
String _videourl = "";
anywheresoftware.b4a.objects.collections.List _list1 = null;
int _i = 0;
Object _mytypes = null;
cloyd.smart.home.monitor.main._videoinfo _videos = null;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
Object _results = null;
int step15;
int limit15;

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
 //BA.debugLineNum = 2482;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 18;
this.catchState = 17;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 17;
 //BA.debugLineNum = 2483;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(Index)";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._clvactivity._getpanel(_index);
 //BA.debugLineNum = 2484;BA.debugLine="If p.NumberOfViews > 0 Then";
if (true) break;

case 4:
//if
this.state = 7;
if (_p.getNumberOfViews()>0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2485;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).GetV";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (6));
 //BA.debugLineNum = 2486;BA.debugLine="ContentLabel.Visible = False";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2488;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).GetV";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (4));
 //BA.debugLineNum = 2489;BA.debugLine="ContentLabel.Visible = False";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 2492;BA.debugLine="UpdateItemColor(Index)";
_updateitemcolor(_index);
 //BA.debugLineNum = 2493;BA.debugLine="wvMedia.LoadUrl(\"\")";
parent.mostCurrent._wvmedia.LoadUrl("");
 //BA.debugLineNum = 2495;BA.debugLine="Dim cd As CardData = clvActivity.GetValue(Index)";
_cd = (cloyd.smart.home.monitor.main._carddata)(parent.mostCurrent._clvactivity._getvalue(_index));
 //BA.debugLineNum = 2496;BA.debugLine="Dim videoURL As String = cd.mediaURL";
_videourl = _cd.mediaURL /*String*/ ;
 //BA.debugLineNum = 2498;BA.debugLine="B4XLoadingIndicator4.Show";
parent.mostCurrent._b4xloadingindicator4._show /*String*/ ();
 //BA.debugLineNum = 2500;BA.debugLine="Dim list1 As List = Starter.kvs.ListKeys";
_list1 = new anywheresoftware.b4a.objects.collections.List();
_list1 = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._listkeys /*anywheresoftware.b4a.objects.collections.List*/ ();
 //BA.debugLineNum = 2501;BA.debugLine="For i =  0 To list1.Size-1";
if (true) break;

case 8:
//for
this.state = 15;
step15 = 1;
limit15 = (int) (_list1.getSize()-1);
_i = (int) (0) ;
this.state = 19;
if (true) break;

case 19:
//C
this.state = 15;
if ((step15 > 0 && _i <= limit15) || (step15 < 0 && _i >= limit15)) this.state = 10;
if (true) break;

case 20:
//C
this.state = 19;
_i = ((int)(0 + _i + step15)) ;
if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 2502;BA.debugLine="Dim mytypes As Object = Starter.kvs.Get(list1.G";
_mytypes = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._get /*Object*/ (BA.ObjectToString(_list1.Get(_i)));
 //BA.debugLineNum = 2503;BA.debugLine="Dim videos = mytypes As VideoInfo";
_videos = (cloyd.smart.home.monitor.main._videoinfo)(_mytypes);
 //BA.debugLineNum = 2504;BA.debugLine="If videoURL.Contains(videos.VideoID) Then";
if (true) break;

case 11:
//if
this.state = 14;
if (_videourl.contains(_videos.VideoID /*String*/ )) { 
this.state = 13;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 2505;BA.debugLine="Starter.kvs.Put(videos.VideoID,CreateCustomTyp";
parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._put /*String*/ (_videos.VideoID /*String*/ ,(Object)(_createcustomtype(_videos.ThumbnailPath /*String*/ ,_videos.DateCreated /*String*/ ,"true",_videos.DeviceName /*String*/ ,_videos.VideoID /*String*/ ,_videos.ThumbnailBLOB /*byte[]*/ )));
 if (true) break;

case 14:
//C
this.state = 20;
;
 if (true) break;
if (true) break;

case 15:
//C
this.state = 18;
;
 //BA.debugLineNum = 2508;BA.debugLine="lblDuration.Text = \"0:00\"";
parent.mostCurrent._lblduration.setText(BA.ObjectToCharSequence("0:00"));
 //BA.debugLineNum = 2510;BA.debugLine="Dim rs As ResumableSub = ShowVideo(videoURL)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _showvideo(_videourl);
 //BA.debugLineNum = 2511;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 21;
return;
case 21:
//C
this.state = 18;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2514;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 2515;BA.debugLine="wait for (rs) complete (Results As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 22;
return;
case 22:
//C
this.state = 18;
_results = (Object) result[0];
;
 if (true) break;

case 17:
//C
this.state = 18;
this.catchState = 0;
 //BA.debugLineNum = 2518;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("34194341",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 18:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 2520;BA.debugLine="B4XLoadingIndicator4.Hide";
parent.mostCurrent._b4xloadingindicator4._hide /*String*/ ();
 //BA.debugLineNum = 2521;BA.debugLine="End Sub";
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
public static String  _clvactivity_visiblerangechanged(int _firstindex,int _lastindex) throws Exception{
int _extrasize = 0;
int _i = 0;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
cloyd.smart.home.monitor.main._carddata _cd = null;
String _dayname = "";
anywheresoftware.b4a.objects.B4XViewWrapper _backpane = null;
anywheresoftware.b4a.objects.B4XViewWrapper _contentlabel = null;
anywheresoftware.b4a.objects.ImageViewWrapper _ivcleanup = null;
 //BA.debugLineNum = 2404;BA.debugLine="Sub clvActivity_VisibleRangeChanged (FirstIndex As";
 //BA.debugLineNum = 2405;BA.debugLine="Dim ExtraSize As Int = 1";
_extrasize = (int) (1);
 //BA.debugLineNum = 2406;BA.debugLine="For i = 0 To clvActivity.Size - 1";
{
final int step2 = 1;
final int limit2 = (int) (mostCurrent._clvactivity._getsize()-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 2407;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(i)";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._clvactivity._getpanel(_i);
 //BA.debugLineNum = 2408;BA.debugLine="If i > FirstIndex - ExtraSize And i < LastIndex";
if (_i>_firstindex-_extrasize && _i<_lastindex+_extrasize) { 
 //BA.debugLineNum = 2410;BA.debugLine="If p.NumberOfViews = 0 Then";
if (_p.getNumberOfViews()==0) { 
 //BA.debugLineNum = 2411;BA.debugLine="Dim cd As CardData = clvActivity.GetValue(i)";
_cd = (cloyd.smart.home.monitor.main._carddata)(mostCurrent._clvactivity._getvalue(_i));
 //BA.debugLineNum = 2412;BA.debugLine="p.LoadLayout(\"blinkcellitem\")";
_p.LoadLayout("blinkcellitem",mostCurrent.activityBA);
 //BA.debugLineNum = 2413;BA.debugLine="ivScreenshot.Bitmap = cd.screenshot";
mostCurrent._ivscreenshot.setBitmap((android.graphics.Bitmap)(_cd.screenshot /*anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper*/ .getObject()));
 //BA.debugLineNum = 2415;BA.debugLine="Dim dayname As String";
_dayname = "";
 //BA.debugLineNum = 2416;BA.debugLine="dayname = ConvertDayName(cd.filedate)";
_dayname = _convertdayname(_cd.filedate /*String*/ );
 //BA.debugLineNum = 2417;BA.debugLine="If cd.iswatchedvisible Then";
if (_cd.iswatchedvisible /*boolean*/ ) { 
 //BA.debugLineNum = 2418;BA.debugLine="ivWatched.Visible = True";
mostCurrent._ivwatched.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2419;BA.debugLine="lblDate.Text = \"   \" & dayname & \" \" & Conver";
mostCurrent._lbldate.setText(BA.ObjectToCharSequence("   "+_dayname+" "+_convertdatetimeperiod(_cd.filedate /*String*/ ,_dayname)));
 }else {
 //BA.debugLineNum = 2421;BA.debugLine="ivWatched.Visible = False";
mostCurrent._ivwatched.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2422;BA.debugLine="lblDate.Text = \"   \" & dayname";
mostCurrent._lbldate.setText(BA.ObjectToCharSequence("   "+_dayname));
 };
 //BA.debugLineNum = 2424;BA.debugLine="lblFileInfo.Text = \"   \" & ConvertFullDateTime";
mostCurrent._lblfileinfo.setText(BA.ObjectToCharSequence("   "+_convertfulldatetime(_cd.filedate /*String*/ )));
 //BA.debugLineNum = 2425;BA.debugLine="lblDeviceInfo.Text = \"   \" & cd.deviceinfo";
mostCurrent._lbldeviceinfo.setText(BA.ObjectToCharSequence("   "+_cd.deviceinfo /*String*/ ));
 //BA.debugLineNum = 2426;BA.debugLine="lblMediaURL.Text = cd.mediaURL";
mostCurrent._lblmediaurl.setText(BA.ObjectToCharSequence(_cd.mediaURL /*String*/ ));
 //BA.debugLineNum = 2428;BA.debugLine="If previousSelectedIndex > (clvActivity.Size-1";
if (_previousselectedindex>(mostCurrent._clvactivity._getsize()-1)) { 
 //BA.debugLineNum = 2429;BA.debugLine="previousSelectedIndex = 0";
_previousselectedindex = (int) (0);
 };
 //BA.debugLineNum = 2431;BA.debugLine="If previousSelectedIndex = i Then";
if (_previousselectedindex==_i) { 
 //BA.debugLineNum = 2432;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(previ";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._clvactivity._getpanel(_previousselectedindex);
 //BA.debugLineNum = 2433;BA.debugLine="If p.NumberOfViews > 0 Then";
if (_p.getNumberOfViews()>0) { 
 //BA.debugLineNum = 2434;BA.debugLine="Dim backPane As B4XView = p.getview(0)";
_backpane = new anywheresoftware.b4a.objects.B4XViewWrapper();
_backpane = _p.GetView((int) (0));
 //BA.debugLineNum = 2435;BA.debugLine="backPane.Color = xui.Color_ARGB(255,217,215,";
_backpane.setColor(mostCurrent._xui.Color_ARGB((int) (255),(int) (217),(int) (215),(int) (222)));
 //BA.debugLineNum = 2437;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).G";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (6));
 //BA.debugLineNum = 2438;BA.debugLine="ContentLabel.Visible = True";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 };
 };
 }else {
 //BA.debugLineNum = 2444;BA.debugLine="If p.NumberOfViews > 0 Then";
if (_p.getNumberOfViews()>0) { 
 //BA.debugLineNum = 2445;BA.debugLine="Dim ivCleanUP As ImageView = p.GetView(0).GetV";
_ivcleanup = new anywheresoftware.b4a.objects.ImageViewWrapper();
_ivcleanup.setObject((android.widget.ImageView)(_p.GetView((int) (0)).GetView((int) (3)).getObject()));
 //BA.debugLineNum = 2446;BA.debugLine="ivCleanUP.Bitmap = Null";
_ivcleanup.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 2447;BA.debugLine="p.RemoveAllViews";
_p.RemoveAllViews();
 };
 };
 }
};
 //BA.debugLineNum = 2451;BA.debugLine="End Sub";
return "";
}
public static String  _convertdatetime(String _inputtime) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
 //BA.debugLineNum = 2119;BA.debugLine="Sub ConvertDateTime(inputTime As String) As String";
 //BA.debugLineNum = 2121;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 2122;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 2123;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 2126;BA.debugLine="Return DateTime.Date(lngTicks)";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 //BA.debugLineNum = 2127;BA.debugLine="End Sub";
return "";
}
public static String  _convertdatetimeperiod(String _inputtime,String _dayname) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 2591;BA.debugLine="Sub ConvertDateTimePeriod(inputTime As String, day";
 //BA.debugLineNum = 2593;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 2594;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 2595;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 2596;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngTick";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 2598;BA.debugLine="If dayname.Contains(\"Today\") Then";
if (_dayname.contains("Today")) { 
 //BA.debugLineNum = 2599;BA.debugLine="If p.Days = 0 Then";
if (_p.Days==0) { 
 //BA.debugLineNum = 2600;BA.debugLine="If p.Hours = 0 Then";
if (_p.Hours==0) { 
 //BA.debugLineNum = 2601;BA.debugLine="If p.Minutes = 0 Then";
if (_p.Minutes==0) { 
 //BA.debugLineNum = 2602;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 2604;BA.debugLine="Return p.Minutes & \"m \" & p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 2607;BA.debugLine="Return p.Hours & \"h \" & p.Minutes & \"m \" & p.S";
if (true) return BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else if(_p.Hours==0) { 
 //BA.debugLineNum = 2610;BA.debugLine="If p.Minutes = 0 Then";
if (_p.Minutes==0) { 
 //BA.debugLineNum = 2611;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 2613;BA.debugLine="Return p.Minutes & \"m \" & p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else if(_p.Minutes==0) { 
 //BA.debugLineNum = 2616;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 2618;BA.debugLine="Return p.Days & \"d \" & p.Hours & \"h \" & p.Minut";
if (true) return BA.NumberToString(_p.Days)+"d "+BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else if(_dayname.contains("Yesterday")) { 
 //BA.debugLineNum = 2621;BA.debugLine="If p.Days = 0 Then";
if (_p.Days==0) { 
 //BA.debugLineNum = 2622;BA.debugLine="If p.Hours = 0 Then";
if (_p.Hours==0) { 
 //BA.debugLineNum = 2623;BA.debugLine="If p.Minutes = 0 Then";
if (_p.Minutes==0) { 
 //BA.debugLineNum = 2624;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 2626;BA.debugLine="Return p.Minutes & \"m \" & p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 2629;BA.debugLine="Return p.Hours & \"h \" & p.Minutes & \"m \" & p.S";
if (true) return BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 2632;BA.debugLine="Return p.Days & \"d \" & p.Hours & \"h \" & p.Minut";
if (true) return BA.NumberToString(_p.Days)+"d "+BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 2635;BA.debugLine="Return p.Days & \"d \" & p.Hours & \"h \" & p.Minute";
if (true) return BA.NumberToString(_p.Days)+"d "+BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 //BA.debugLineNum = 2638;BA.debugLine="End Sub";
return "";
}
public static String  _convertdayname(String _inputtime) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
long _yesterday = 0L;
long _timestamp = 0L;
 //BA.debugLineNum = 2640;BA.debugLine="Sub ConvertDayName(inputTime As String) As String";
 //BA.debugLineNum = 2642;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 2643;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 2644;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 2646;BA.debugLine="Dim Yesterday As Long";
_yesterday = 0L;
 //BA.debugLineNum = 2647;BA.debugLine="Dim timestamp As Long";
_timestamp = 0L;
 //BA.debugLineNum = 2648;BA.debugLine="DateTime.DateFormat = \"yyyyMMdd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyyMMdd");
 //BA.debugLineNum = 2649;BA.debugLine="Yesterday = DateTime.Date(DateTime.add(DateTime.N";
_yesterday = (long)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1)))));
 //BA.debugLineNum = 2650;BA.debugLine="timestamp = DateTime.Date(lngTicks)";
_timestamp = (long)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks)));
 //BA.debugLineNum = 2652;BA.debugLine="DateTime.DateFormat = \"h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("h:mm:ss a");
 //BA.debugLineNum = 2653;BA.debugLine="If DateUtils.IsSameDay(lngTicks,DateTime.now) The";
if (mostCurrent._dateutils._issameday(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow())) { 
 //BA.debugLineNum = 2654;BA.debugLine="Return \"Today\" '& DateTime.Date(lngTicks)";
if (true) return "Today";
 }else if(_yesterday==_timestamp) { 
 //BA.debugLineNum = 2656;BA.debugLine="Return \"Yesterday\" '& DateTime.Date(lngTicks)";
if (true) return "Yesterday";
 }else {
 //BA.debugLineNum = 2658;BA.debugLine="Return DateUtils.GetDayOfWeekName(lngTicks) '& \"";
if (true) return mostCurrent._dateutils._getdayofweekname(mostCurrent.activityBA,_lngticks);
 };
 //BA.debugLineNum = 2660;BA.debugLine="End Sub";
return "";
}
public static String  _convertfulldatetime(String _inputtime) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
long _yesterday = 0L;
long _timestamp = 0L;
 //BA.debugLineNum = 2662;BA.debugLine="Sub ConvertFullDateTime(inputTime As String) As St";
 //BA.debugLineNum = 2664;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 2665;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 2666;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 2668;BA.debugLine="Dim Yesterday As Long";
_yesterday = 0L;
 //BA.debugLineNum = 2669;BA.debugLine="Dim timestamp As Long";
_timestamp = 0L;
 //BA.debugLineNum = 2670;BA.debugLine="DateTime.DateFormat = \"yyyyMMdd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyyMMdd");
 //BA.debugLineNum = 2671;BA.debugLine="Yesterday = DateTime.Date(DateTime.add(DateTime.N";
_yesterday = (long)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1)))));
 //BA.debugLineNum = 2672;BA.debugLine="timestamp = DateTime.Date(lngTicks)";
_timestamp = (long)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks)));
 //BA.debugLineNum = 2674;BA.debugLine="DateTime.DateFormat = \"h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("h:mm:ss a");
 //BA.debugLineNum = 2675;BA.debugLine="If DateUtils.IsSameDay(lngTicks,DateTime.now) The";
if (mostCurrent._dateutils._issameday(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow())) { 
 //BA.debugLineNum = 2676;BA.debugLine="Return DateTime.Date(lngTicks)";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 }else if(_yesterday==_timestamp) { 
 //BA.debugLineNum = 2678;BA.debugLine="Return DateTime.Date(lngTicks)";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 }else {
 //BA.debugLineNum = 2680;BA.debugLine="Return DateTime.Date(lngTicks)";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 };
 //BA.debugLineNum = 2682;BA.debugLine="End Sub";
return "";
}
public static String  _converttickstotimestring(long _t) throws Exception{
int _hours = 0;
int _minutes = 0;
int _seconds = 0;
 //BA.debugLineNum = 2775;BA.debugLine="Sub ConvertTicksToTimeString(t As Long) As String";
 //BA.debugLineNum = 2776;BA.debugLine="Dim  hours, minutes, seconds As Int 'ignore";
_hours = 0;
_minutes = 0;
_seconds = 0;
 //BA.debugLineNum = 2777;BA.debugLine="hours = t / DateTime.TicksPerHour";
_hours = (int) (_t/(double)anywheresoftware.b4a.keywords.Common.DateTime.TicksPerHour);
 //BA.debugLineNum = 2778;BA.debugLine="minutes = (t Mod DateTime.TicksPerHour) / DateTim";
_minutes = (int) ((_t%anywheresoftware.b4a.keywords.Common.DateTime.TicksPerHour)/(double)anywheresoftware.b4a.keywords.Common.DateTime.TicksPerMinute);
 //BA.debugLineNum = 2779;BA.debugLine="seconds = (t Mod DateTime.TicksPerMinute) / DateT";
_seconds = (int) ((_t%anywheresoftware.b4a.keywords.Common.DateTime.TicksPerMinute)/(double)anywheresoftware.b4a.keywords.Common.DateTime.TicksPerSecond);
 //BA.debugLineNum = 2780;BA.debugLine="Return NumberFormat(minutes, 1, 0) & \":\" & Number";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat(_minutes,(int) (1),(int) (0))+":"+anywheresoftware.b4a.keywords.Common.NumberFormat(_seconds,(int) (2),(int) (0));
 //BA.debugLineNum = 2781;BA.debugLine="End Sub";
return "";
}
public static cloyd.smart.home.monitor.main._videoinfo  _createcustomtype(String _thumbnailpath,String _datecreated,String _watched,String _devicename,String _videoid,byte[] _thumbnailblob) throws Exception{
cloyd.smart.home.monitor.main._videoinfo _ct = null;
 //BA.debugLineNum = 2391;BA.debugLine="Private Sub CreateCustomType(ThumbnailPath As Stri";
 //BA.debugLineNum = 2392;BA.debugLine="Dim ct As VideoInfo";
_ct = new cloyd.smart.home.monitor.main._videoinfo();
 //BA.debugLineNum = 2393;BA.debugLine="ct.Initialize";
_ct.Initialize();
 //BA.debugLineNum = 2394;BA.debugLine="ct.ThumbnailPath = ThumbnailPath";
_ct.ThumbnailPath /*String*/  = _thumbnailpath;
 //BA.debugLineNum = 2395;BA.debugLine="ct.DateCreated = DateCreated";
_ct.DateCreated /*String*/  = _datecreated;
 //BA.debugLineNum = 2396;BA.debugLine="ct.Watched = Watched";
_ct.Watched /*String*/  = _watched;
 //BA.debugLineNum = 2397;BA.debugLine="ct.DeviceName = DeviceName";
_ct.DeviceName /*String*/  = _devicename;
 //BA.debugLineNum = 2398;BA.debugLine="ct.ThumbnailBLOB = ThumbnailBLOB";
_ct.ThumbnailBLOB /*byte[]*/  = _thumbnailblob;
 //BA.debugLineNum = 2399;BA.debugLine="ct.VideoID = VideoID";
_ct.VideoID /*String*/  = _videoid;
 //BA.debugLineNum = 2400;BA.debugLine="Return ct";
if (true) return _ct;
 //BA.debugLineNum = 2401;BA.debugLine="End Sub";
return null;
}
>>>>>>> origin/master
public static String  _createpreferencescreen() throws Exception{
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat1 = null;
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat2 = null;
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat3 = null;
anywheresoftware.b4a.objects.IntentWrapper _in = null;
 //BA.debugLineNum = 930;BA.debugLine="Sub CreatePreferenceScreen";
 //BA.debugLineNum = 931;BA.debugLine="screen.Initialize(\"Settings\", \"\")";
_screen.Initialize("Settings","");
 //BA.debugLineNum = 933;BA.debugLine="Dim cat1,cat2,cat3 As AHPreferenceCategory";
_cat1 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat2 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat3 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
 //BA.debugLineNum = 935;BA.debugLine="cat1.Initialize(\"Temperature & Humidity\")";
_cat1.Initialize("Temperature & Humidity");
<<<<<<< HEAD
 //BA.debugLineNum = 936;BA.debugLine="cat1.AddEditText(\"HumidityAddValue\", \"Humidity Ad";
_cat1.AddEditText("HumidityAddValue","Humidity Additional Value","Value to be added to humidity to improve accuracy","6","");
 //BA.debugLineNum = 938;BA.debugLine="cat2.Initialize(\"Special Settings\")";
_cat2.Initialize("Special Settings");
 //BA.debugLineNum = 939;BA.debugLine="Dim In As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 940;BA.debugLine="In.Initialize(\"android.settings.ACTION_NOTIFICATI";
_in.Initialize("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS","");
 //BA.debugLineNum = 941;BA.debugLine="cat2.AddIntent(\"Notification Access\", \"Enable or";
_cat2.AddIntent("Notification Access","Enable or disable listening to notifications",(android.content.Intent)(_in.getObject()),BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 943;BA.debugLine="cat3.Initialize(\"Sensors\")";
_cat3.Initialize("Sensors");
 //BA.debugLineNum = 944;BA.debugLine="cat3.AddEditText(\"SensorNotRespondingTime\", \"Sens";
_cat3.AddEditText("SensorNotRespondingTime","Sensor Not Responding","Data age when to restart sensor","10","");
 //BA.debugLineNum = 946;BA.debugLine="screen.AddPreferenceCategory(cat2)";
_screen.AddPreferenceCategory(_cat2);
 //BA.debugLineNum = 947;BA.debugLine="screen.AddPreferenceCategory(cat1)";
_screen.AddPreferenceCategory(_cat1);
 //BA.debugLineNum = 948;BA.debugLine="screen.AddPreferenceCategory(cat3)";
_screen.AddPreferenceCategory(_cat3);
 //BA.debugLineNum = 949;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",\"6\")";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue","6");
 //BA.debugLineNum = 950;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime","10");
 //BA.debugLineNum = 951;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 952;BA.debugLine="End Sub";
return "";
}
=======
 //BA.debugLineNum = 1190;BA.debugLine="cat1.AddEditText(\"HumidityAddValue\", \"Humidity Ad";
_cat1.AddEditText("HumidityAddValue","Humidity Additional Value","Value to be added to humidity to improve accuracy","6","");
 //BA.debugLineNum = 1192;BA.debugLine="cat2.Initialize(\"Special Settings\")";
_cat2.Initialize("Special Settings");
 //BA.debugLineNum = 1193;BA.debugLine="Dim In As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 1194;BA.debugLine="In.Initialize(\"android.settings.ACTION_NOTIFICATI";
_in.Initialize("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS","");
 //BA.debugLineNum = 1195;BA.debugLine="cat2.AddIntent(\"Notification Access\", \"Enable or";
_cat2.AddIntent("Notification Access","Enable or disable listening to notifications",(android.content.Intent)(_in.getObject()),BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 1197;BA.debugLine="cat3.Initialize(\"Sensors\")";
_cat3.Initialize("Sensors");
 //BA.debugLineNum = 1198;BA.debugLine="cat3.AddEditText(\"SensorNotRespondingTime\", \"Sens";
_cat3.AddEditText("SensorNotRespondingTime","Sensor Not Responding","Data age when to restart sensor","10","");
 //BA.debugLineNum = 1200;BA.debugLine="screen.AddPreferenceCategory(cat2)";
_screen.AddPreferenceCategory(_cat2);
 //BA.debugLineNum = 1201;BA.debugLine="screen.AddPreferenceCategory(cat1)";
_screen.AddPreferenceCategory(_cat1);
 //BA.debugLineNum = 1202;BA.debugLine="screen.AddPreferenceCategory(cat3)";
_screen.AddPreferenceCategory(_cat3);
 //BA.debugLineNum = 1203;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",\"6\")";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue","6");
 //BA.debugLineNum = 1204;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime","10");
 //BA.debugLineNum = 1205;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 1206;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _createtab4() throws Exception{
ResumableSub_CreateTab4 rsub = new ResumableSub_CreateTab4(null);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_CreateTab4 extends BA.ResumableSub {
public ResumableSub_CreateTab4(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;

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
 //BA.debugLineNum = 873;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 22;
this.catchState = 21;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 21;
 //BA.debugLineNum = 874;BA.debugLine="Pages = Array(False, False, False, False, True,";
parent._pages = anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.False),(Object)(anywheresoftware.b4a.keywords.Common.False),(Object)(anywheresoftware.b4a.keywords.Common.False),(Object)(anywheresoftware.b4a.keywords.Common.False),(Object)(anywheresoftware.b4a.keywords.Common.True),(Object)(anywheresoftware.b4a.keywords.Common.False)});
 //BA.debugLineNum = 875;BA.debugLine="wvMedia.LoadUrl(\"\")";
parent.mostCurrent._wvmedia.LoadUrl("");
 //BA.debugLineNum = 876;BA.debugLine="If lblDrivewayBatt.Text = \"\" Or lblFrontDoorBatt";
if (true) break;

case 4:
//if
this.state = 19;
if ((parent.mostCurrent._lbldrivewaybatt.getText()).equals("") || (parent.mostCurrent._lblfrontdoorbatt.getText()).equals("") || (parent.mostCurrent._lblsideyardbatt.getText()).equals("") || parent.mostCurrent._lblstatus.getText().contains("REST endpoint URL not found")) { 
this.state = 6;
}else if(parent.mostCurrent._ivdriveway.getBitmap()== null || parent.mostCurrent._ivfrontdoor.getBitmap()== null || parent.mostCurrent._ivsideyard.getBitmap()== null) { 
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 19;
 //BA.debugLineNum = 878;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 879;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 880;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 881;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 882;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 883;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 884;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",parent.mostCurrent._ivdriveway);
 //BA.debugLineNum = 885;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",parent.mostCurrent._ivfrontdoor);
 //BA.debugLineNum = 886;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",parent.mostCurrent._ivsideyard);
 //BA.debugLineNum = 887;BA.debugLine="Dim rs As ResumableSub = RefreshCameras(True,\"A";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _refreshcameras(anywheresoftware.b4a.keywords.Common.True,"All");
 //BA.debugLineNum = 888;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 23;
return;
case 23:
//C
this.state = 19;
_result = (Object) result[0];
;
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 891;BA.debugLine="If File.Exists(File.DirInternal, \"Driveway.jpg\"";
if (true) break;

case 9:
//if
this.state = 12;
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Driveway.jpg")) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 892;BA.debugLine="bmp = xui.LoadBitmapResize(File.DirInternal, \"";
parent._bmp = parent.mostCurrent._xui.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Driveway.jpg",parent.mostCurrent._ivdriveway.getWidth(),parent.mostCurrent._ivdriveway.getHeight(),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 893;BA.debugLine="ivDriveway.Bitmap = bmp";
parent.mostCurrent._ivdriveway.setBitmap((android.graphics.Bitmap)(parent._bmp.getObject()));
 if (true) break;
;
 //BA.debugLineNum = 895;BA.debugLine="If File.Exists(File.DirInternal, \"FrontDoor.jpg";

case 12:
//if
this.state = 15;
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"FrontDoor.jpg")) { 
this.state = 14;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 896;BA.debugLine="bmp = xui.LoadBitmapResize(File.DirInternal, \"";
parent._bmp = parent.mostCurrent._xui.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"FrontDoor.jpg",parent.mostCurrent._ivfrontdoor.getWidth(),parent.mostCurrent._ivfrontdoor.getHeight(),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 897;BA.debugLine="ivFrontDoor.Bitmap = bmp";
parent.mostCurrent._ivfrontdoor.setBitmap((android.graphics.Bitmap)(parent._bmp.getObject()));
 if (true) break;
;
 //BA.debugLineNum = 899;BA.debugLine="If File.Exists(File.DirInternal, \"SideYard.jpg\"";

case 15:
//if
this.state = 18;
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"SideYard.jpg")) { 
this.state = 17;
}if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 900;BA.debugLine="bmp = xui.LoadBitmapResize(File.DirInternal, \"";
parent._bmp = parent.mostCurrent._xui.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"SideYard.jpg",parent.mostCurrent._ivfrontdoor.getWidth(),parent.mostCurrent._ivfrontdoor.getHeight(),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 901;BA.debugLine="ivSideYard.Bitmap = bmp";
parent.mostCurrent._ivsideyard.setBitmap((android.graphics.Bitmap)(parent._bmp.getObject()));
 if (true) break;

case 18:
//C
this.state = 19;
;
 if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
this.catchState = 0;
 //BA.debugLineNum = 905;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("31703969",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 22:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 907;BA.debugLine="Pages = Array(True, True, True, True, True, True)";
parent._pages = anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.True),(Object)(anywheresoftware.b4a.keywords.Common.True),(Object)(anywheresoftware.b4a.keywords.Common.True),(Object)(anywheresoftware.b4a.keywords.Common.True),(Object)(anywheresoftware.b4a.keywords.Common.True),(Object)(anywheresoftware.b4a.keywords.Common.True)});
 //BA.debugLineNum = 908;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 909;BA.debugLine="End Sub";
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
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _downloadimage(String _link,anywheresoftware.b4a.objects.ImageViewWrapper _iv,String _camera) throws Exception{
ResumableSub_DownloadImage rsub = new ResumableSub_DownloadImage(null,_link,_iv,_camera);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
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
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;

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
 //BA.debugLineNum = 1599;BA.debugLine="Try";
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
 //BA.debugLineNum = 1600;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1601;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1602;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1603;BA.debugLine="j.Download(Link)";
_j._download /*String*/ (_link);
 //BA.debugLineNum = 1604;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1605;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 21;
return;
case 21:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1606;BA.debugLine="If j.Success Then";
if (true) break;

case 4:
//if
this.state = 17;
if (_j._success /*boolean*/ ) { 
this.state = 6;
}else {
this.state = 16;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1608;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 7:
//if
this.state = 14;
if ((_camera).equals("347574")) { 
this.state = 9;
}else if((_camera).equals("236967")) { 
this.state = 11;
}else if((_camera).equals("458236")) { 
this.state = 13;
}if (true) break;

case 9:
//C
this.state = 14;
 //BA.debugLineNum = 1609;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Driveway.jpg",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1610;BA.debugLine="File.Copy2(j.GetInputStream, out)";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_j._getinputstream /*anywheresoftware.b4a.objects.streams.File.InputStreamWrapper*/ ().getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 1611;BA.debugLine="out.Close '<------ very important";
_out.Close();
 //BA.debugLineNum = 1612;BA.debugLine="bmp = j.GetBitmapResize(iv.Width, iv.Height,Tr";
parent._bmp.setObject((android.graphics.Bitmap)(_j._getbitmapresize /*anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper*/ (_iv.getWidth(),_iv.getHeight(),anywheresoftware.b4a.keywords.Common.True).getObject()));
 if (true) break;

case 11:
//C
this.state = 14;
 //BA.debugLineNum = 1614;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"FrontDoor.jpg",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1615;BA.debugLine="File.Copy2(j.GetInputStream, out)";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_j._getinputstream /*anywheresoftware.b4a.objects.streams.File.InputStreamWrapper*/ ().getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 1616;BA.debugLine="out.Close '<------ very important";
_out.Close();
 //BA.debugLineNum = 1617;BA.debugLine="bmp = j.GetBitmapResize(iv.Width, iv.Height,Tr";
parent._bmp.setObject((android.graphics.Bitmap)(_j._getbitmapresize /*anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper*/ (_iv.getWidth(),_iv.getHeight(),anywheresoftware.b4a.keywords.Common.True).getObject()));
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 1619;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"SideYard.jpg",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1620;BA.debugLine="File.Copy2(j.GetInputStream, out)";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_j._getinputstream /*anywheresoftware.b4a.objects.streams.File.InputStreamWrapper*/ ().getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 1621;BA.debugLine="out.Close '<------ very important";
_out.Close();
 //BA.debugLineNum = 1622;BA.debugLine="bmp = j.GetBitmapResize(iv.Width, iv.Height,Tr";
parent._bmp.setObject((android.graphics.Bitmap)(_j._getbitmapresize /*anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper*/ (_iv.getWidth(),_iv.getHeight(),anywheresoftware.b4a.keywords.Common.True).getObject()));
 if (true) break;

case 14:
//C
this.state = 17;
;
 //BA.debugLineNum = 1624;BA.debugLine="iv.Bitmap = bmp";
_iv.setBitmap((android.graphics.Bitmap)(parent._bmp.getObject()));
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 1626;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1627;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;

case 17:
//C
this.state = 20;
;
 //BA.debugLineNum = 1629;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 19:
//C
this.state = 20;
this.catchState = 0;
 //BA.debugLineNum = 1631;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("32752545",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 20:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1633;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 1634;BA.debugLine="End Sub";
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
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _downloadimagefullscreen(String _link,String _camera) throws Exception{
ResumableSub_DownloadImageFullscreen rsub = new ResumableSub_DownloadImageFullscreen(null,_link,_camera);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_DownloadImageFullscreen extends BA.ResumableSub {
public ResumableSub_DownloadImageFullscreen(cloyd.smart.home.monitor.main parent,String _link,String _camera) {
this.parent = parent;
this._link = _link;
this._camera = _camera;
}
cloyd.smart.home.monitor.main parent;
String _link;
String _camera;
cloyd.smart.home.monitor.httpjob _j = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
String _filename = "";
anywheresoftware.b4a.objects.IntentWrapper _in = null;

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
 //BA.debugLineNum = 3123;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 16;
this.catchState = 15;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 15;
 //BA.debugLineNum = 3124;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 3125;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 3126;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 3127;BA.debugLine="j.Download(Link)";
_j._download /*String*/ (_link);
 //BA.debugLineNum = 3128;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 3129;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 17;
return;
case 17:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 3130;BA.debugLine="If j.Success Then";
if (true) break;

case 4:
//if
this.state = 13;
if (_j._success /*boolean*/ ) { 
this.state = 6;
}else {
this.state = 12;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 3132;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File.";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"screenshot.jpg",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 3133;BA.debugLine="File.Copy2(j.GetInputStream, out)";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_j._getinputstream /*anywheresoftware.b4a.objects.streams.File.InputStreamWrapper*/ ().getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 3134;BA.debugLine="out.Close '<------ very important";
_out.Close();
 //BA.debugLineNum = 3136;BA.debugLine="Dim FileName As String = \"screenshot.jpg\"";
_filename = "screenshot.jpg";
 //BA.debugLineNum = 3137;BA.debugLine="If File.Exists(File.DirInternal, FileName) Then";
if (true) break;

case 7:
//if
this.state = 10;
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename)) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 3138;BA.debugLine="File.Copy(File.DirInternal, FileName, Starter.";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename,parent.mostCurrent._starter._provider /*cloyd.smart.home.monitor.fileprovider*/ ._sharedfolder /*String*/ ,_filename);
 //BA.debugLineNum = 3139;BA.debugLine="Dim in As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 3140;BA.debugLine="in.Initialize(in.ACTION_VIEW, \"\")";
_in.Initialize(_in.ACTION_VIEW,"");
 //BA.debugLineNum = 3141;BA.debugLine="Starter.Provider.SetFileUriAsIntentData(in, Fi";
parent.mostCurrent._starter._provider /*cloyd.smart.home.monitor.fileprovider*/ ._setfileuriasintentdata /*String*/ (_in,_filename);
 //BA.debugLineNum = 3143;BA.debugLine="in.SetType(\"image/*\")";
_in.SetType("image/*");
 //BA.debugLineNum = 3144;BA.debugLine="StartActivity(in)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_in.getObject()));
 if (true) break;

case 10:
//C
this.state = 13;
;
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 3147;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 3148;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;

case 13:
//C
this.state = 16;
;
 //BA.debugLineNum = 3150;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 15:
//C
this.state = 16;
this.catchState = 0;
 //BA.debugLineNum = 3152;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("35046302",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 16:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 3154;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 3155;BA.debugLine="End Sub";
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
>>>>>>> origin/master
public static String  _getairquality(int _number) throws Exception{
 //BA.debugLineNum = 391;BA.debugLine="Sub GetAirQuality(number As Int) As String";
 //BA.debugLineNum = 394;BA.debugLine="If number <= 10 Then";
if (_number<=10) { 
 //BA.debugLineNum = 395;BA.debugLine="Return(\"Carbon monoxide perfect\")";
if (true) return ("Carbon monoxide perfect");
 }else if(((_number>10) && (_number<40)) || _number==40) { 
 //BA.debugLineNum = 397;BA.debugLine="Return(\"Carbon monoxide normal\")";
if (true) return ("Carbon monoxide normal");
 }else if(((_number>40) && (_number<90)) || _number==90) { 
 //BA.debugLineNum = 399;BA.debugLine="Return(\"Carbon monoxide high\")";
if (true) return ("Carbon monoxide high");
 }else if(_number>90) { 
 //BA.debugLineNum = 401;BA.debugLine="Return(\"ALARM Carbon monoxide very high\")";
if (true) return ("ALARM Carbon monoxide very high");
 }else {
 //BA.debugLineNum = 403;BA.debugLine="Return(\"MQ-7 - cant read any value - check the s";
if (true) return ("MQ-7 - cant read any value - check the sensor!");
 };
 //BA.debugLineNum = 405;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _getalltablabels(anywheresoftware.b4a.objects.TabStripViewPager _tabstrip) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
anywheresoftware.b4a.objects.PanelWrapper _tc = null;
anywheresoftware.b4a.objects.collections.List _res = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
 //BA.debugLineNum = 653;BA.debugLine="Public Sub GetAllTabLabels (tabstrip As TabStrip)";
 //BA.debugLineNum = 654;BA.debugLine="Dim jo As JavaObject = tabstrip";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_tabstrip));
 //BA.debugLineNum = 655;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 656;BA.debugLine="r.Target = jo.GetField(\"tabStrip\")";
_r.Target = _jo.GetField("tabStrip");
 //BA.debugLineNum = 657;BA.debugLine="Dim tc As Panel = r.GetField(\"tabsContainer\")";
_tc = new anywheresoftware.b4a.objects.PanelWrapper();
_tc.setObject((android.view.ViewGroup)(_r.GetField("tabsContainer")));
 //BA.debugLineNum = 658;BA.debugLine="Dim res As List";
_res = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 659;BA.debugLine="res.Initialize";
_res.Initialize();
 //BA.debugLineNum = 660;BA.debugLine="For Each v As View In tc";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group7 = _tc;
final int groupLen7 = group7.getSize()
;int index7 = 0;
;
for (; index7 < groupLen7;index7++){
_v.setObject((android.view.View)(group7.Get(index7)));
 //BA.debugLineNum = 661;BA.debugLine="If v Is Label Then res.Add(v)";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
_res.Add((Object)(_v.getObject()));};
 }
};
 //BA.debugLineNum = 663;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 664;BA.debugLine="End Sub";
return null;
}
public static anywheresoftware.b4a.objects.collections.List  _getalltablabelsforbadge(anywheresoftware.b4a.objects.TabStripViewPager _tabstrip) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
anywheresoftware.b4a.objects.PanelWrapper _tc = null;
anywheresoftware.b4a.objects.collections.List _res = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
 //BA.debugLineNum = 667;BA.debugLine="Public Sub GetAllTabLabelsForBadge (tabstrip As Ta";
 //BA.debugLineNum = 668;BA.debugLine="Dim jo As JavaObject = tabstrip";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_tabstrip));
 //BA.debugLineNum = 669;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 670;BA.debugLine="r.Target = jo.GetField(\"tabStrip\")";
_r.Target = _jo.GetField("tabStrip");
 //BA.debugLineNum = 671;BA.debugLine="Dim tc As Panel = r.GetField(\"tabsContainer\")";
_tc = new anywheresoftware.b4a.objects.PanelWrapper();
_tc.setObject((android.view.ViewGroup)(_r.GetField("tabsContainer")));
 //BA.debugLineNum = 672;BA.debugLine="Dim res As List";
_res = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 673;BA.debugLine="res.Initialize";
_res.Initialize();
 //BA.debugLineNum = 674;BA.debugLine="For Each v As View In tc";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group7 = _tc;
final int groupLen7 = group7.getSize()
;int index7 = 0;
;
for (; index7 < groupLen7;index7++){
_v.setObject((android.view.View)(group7.Get(index7)));
 //BA.debugLineNum = 675;BA.debugLine="If v Is Label Then res.Add(v)";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
_res.Add((Object)(_v.getObject()));};
 //BA.debugLineNum = 676;BA.debugLine="If v.Tag Is Label Then res.Add(v.Tag)";
if (_v.getTag() instanceof android.widget.TextView) { 
_res.Add(_v.getTag());};
 }
};
 //BA.debugLineNum = 678;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 679;BA.debugLine="End Sub";
return null;
}
<<<<<<< HEAD
=======
public static String  _getauthinfo(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.Map _authtokenmap = null;
anywheresoftware.b4a.objects.collections.Map _networks = null;
anywheresoftware.b4a.objects.collections.Map _region = null;
anywheresoftware.b4a.objects.collections.Map _account = null;
int _id = 0;
 //BA.debugLineNum = 1636;BA.debugLine="Sub GetAuthInfo(json As String)";
 //BA.debugLineNum = 1637;BA.debugLine="Try";
try { //BA.debugLineNum = 1638;BA.debugLine="lblStatus.Text = \"Getting authtoken...\"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Getting authtoken..."));
 //BA.debugLineNum = 1639;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1640;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1641;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1642;BA.debugLine="Dim authtokenmap As Map = root.Get(\"authtoken\")";
_authtokenmap = new anywheresoftware.b4a.objects.collections.Map();
_authtokenmap.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("authtoken"))));
 //BA.debugLineNum = 1643;BA.debugLine="authToken = authtokenmap.Get(\"authtoken\")";
_authtoken = BA.ObjectToString(_authtokenmap.Get((Object)("authtoken")));
 //BA.debugLineNum = 1644;BA.debugLine="Log(\"authToken: \" &  authToken)";
anywheresoftware.b4a.keywords.Common.LogImpl("32818056","authToken: "+_authtoken,0);
 //BA.debugLineNum = 1645;BA.debugLine="Dim networks As Map = root.Get(\"networks\")";
_networks = new anywheresoftware.b4a.objects.collections.Map();
_networks.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("networks"))));
 //BA.debugLineNum = 1646;BA.debugLine="Log(\"networkID: \" & networks.GetKeyAt(0))";
anywheresoftware.b4a.keywords.Common.LogImpl("32818058","networkID: "+BA.ObjectToString(_networks.GetKeyAt((int) (0))),0);
 //BA.debugLineNum = 1647;BA.debugLine="networkID = networks.GetKeyAt(0)";
_networkid = BA.ObjectToString(_networks.GetKeyAt((int) (0)));
 //BA.debugLineNum = 1648;BA.debugLine="Dim region As Map = root.Get(\"region\")";
_region = new anywheresoftware.b4a.objects.collections.Map();
_region.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("region"))));
 //BA.debugLineNum = 1649;BA.debugLine="userRegion = region.GetKeyAt(0)";
_userregion = BA.ObjectToString(_region.GetKeyAt((int) (0)));
 //BA.debugLineNum = 1650;BA.debugLine="Log(\"userRegion: \" &  userRegion)";
anywheresoftware.b4a.keywords.Common.LogImpl("32818062","userRegion: "+_userregion,0);
 //BA.debugLineNum = 1652;BA.debugLine="Dim account As Map = root.Get(\"account\")";
_account = new anywheresoftware.b4a.objects.collections.Map();
_account.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("account"))));
 //BA.debugLineNum = 1653;BA.debugLine="Dim id As Int = account.Get(\"id\")";
_id = (int)(BA.ObjectToNumber(_account.Get((Object)("id"))));
 //BA.debugLineNum = 1654;BA.debugLine="Log(\"accountID: \" & id)";
anywheresoftware.b4a.keywords.Common.LogImpl("32818066","accountID: "+BA.NumberToString(_id),0);
 //BA.debugLineNum = 1655;BA.debugLine="accountID = id";
_accountid = BA.NumberToString(_id);
 } 
       catch (Exception e20) {
			processBA.setLastException(e20); //BA.debugLineNum = 1657;BA.debugLine="lblStatus.Text = \"ERROR: GetAuthInfo - \" & LastE";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("ERROR: GetAuthInfo - "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))));
 //BA.debugLineNum = 1658;BA.debugLine="response = \"ERROR: GetAuthInfo - \" & LastExcepti";
_response = "ERROR: GetAuthInfo - "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA));
 //BA.debugLineNum = 1659;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("32818071",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1662;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _getcamerainfo(String _json,boolean _isjustinfoneeded) throws Exception{
ResumableSub_GetCameraInfo rsub = new ResumableSub_GetCameraInfo(null,_json,_isjustinfoneeded);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_GetCameraInfo extends BA.ResumableSub {
public ResumableSub_GetCameraInfo(cloyd.smart.home.monitor.main parent,String _json,boolean _isjustinfoneeded) {
this.parent = parent;
this._json = _json;
this._isjustinfoneeded = _isjustinfoneeded;
}
cloyd.smart.home.monitor.main parent;
String _json;
boolean _isjustinfoneeded;
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.Map _camera_status = null;
int _battery_voltage = 0;
int _wifi_strength = 0;
String _updated_at = "";
int _camera_id = 0;
String _fw_version = "";

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
 //BA.debugLineNum = 1758;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 27;
this.catchState = 26;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 26;
 //BA.debugLineNum = 1759;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1760;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1761;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1762;BA.debugLine="Dim camera_status As Map = root.Get(\"camera_stat";
_camera_status = new anywheresoftware.b4a.objects.collections.Map();
_camera_status.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("camera_status"))));
 //BA.debugLineNum = 1764;BA.debugLine="Dim battery_voltage As Int = camera_status.Get(\"";
_battery_voltage = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("battery_voltage"))));
 //BA.debugLineNum = 1768;BA.debugLine="Dim wifi_strength As Int = camera_status.Get(\"wi";
_wifi_strength = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("wifi_strength"))));
 //BA.debugLineNum = 1775;BA.debugLine="Dim updated_at As String = camera_status.Get(\"up";
_updated_at = BA.ObjectToString(_camera_status.Get((Object)("updated_at")));
 //BA.debugLineNum = 1785;BA.debugLine="cameraThumbnail = camera_status.Get(\"thumbnail\")";
parent._camerathumbnail = BA.ObjectToString(_camera_status.Get((Object)("thumbnail")));
 //BA.debugLineNum = 1788;BA.debugLine="Dim camera_id As Int = camera_status.Get(\"camera";
_camera_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("camera_id"))));
 //BA.debugLineNum = 1806;BA.debugLine="Dim fw_version As String = camera_status.Get(\"fw";
_fw_version = BA.ObjectToString(_camera_status.Get((Object)("fw_version")));
 //BA.debugLineNum = 1813;BA.debugLine="If isJustInfoNeeded Then Return Null";
if (true) break;

case 4:
//if
this.state = 9;
if (_isjustinfoneeded) { 
this.state = 6;
;}if (true) break;

case 6:
//C
this.state = 9;
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
if (true) break;

case 9:
//C
this.state = 10;
;
 //BA.debugLineNum = 1815;BA.debugLine="If camera_id = \"347574\" Then";
if (true) break;

case 10:
//if
this.state = 17;
if (_camera_id==(double)(Double.parseDouble("347574"))) { 
this.state = 12;
}else if(_camera_id==(double)(Double.parseDouble("236967"))) { 
this.state = 14;
}else if(_camera_id==(double)(Double.parseDouble("458236"))) { 
this.state = 16;
}if (true) break;

case 12:
//C
this.state = 17;
 //BA.debugLineNum = 1816;BA.debugLine="lblDrivewayBatt.Text = \"Batt: \" & NumberFormat2";
parent.mostCurrent._lbldrivewaybatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V "+parent._drivewaybattstatus));
 //BA.debugLineNum = 1817;BA.debugLine="lblDrivewayTimestamp.Text = ConvertDateTime(upd";
parent.mostCurrent._lbldrivewaytimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1818;BA.debugLine="lblDriveway.Text = \"Driveway v\" & fw_version &";
parent.mostCurrent._lbldriveway.setText(BA.ObjectToCharSequence("Driveway v"+_fw_version+" "+parent._drivewayarmedstatus));
 //BA.debugLineNum = 1819;BA.debugLine="lblDrivewayWifi.Text = \"WiFi: \" & wifi_strength";
parent.mostCurrent._lbldrivewaywifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 if (true) break;

case 14:
//C
this.state = 17;
 //BA.debugLineNum = 1821;BA.debugLine="lblFrontDoorBatt.Text = \"Batt: \" & NumberFormat";
parent.mostCurrent._lblfrontdoorbatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V "+parent._frontdoorbattstatus));
 //BA.debugLineNum = 1822;BA.debugLine="lblFrontDoorTimestamp.Text = ConvertDateTime(up";
parent.mostCurrent._lblfrontdoortimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1823;BA.debugLine="lblFrontDoor.Text = \"Front Door v\" & fw_version";
parent.mostCurrent._lblfrontdoor.setText(BA.ObjectToCharSequence("Front Door v"+_fw_version+" "+parent._frontdoorarmedstatus));
 //BA.debugLineNum = 1824;BA.debugLine="lblFrontDoorWiFi.Text = \"WiFi: \" & wifi_strengt";
parent.mostCurrent._lblfrontdoorwifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 1826;BA.debugLine="lblSideYardBatt.Text = \"Batt: \" & NumberFormat2";
parent.mostCurrent._lblsideyardbatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V "+parent._sideyardbattstatus));
 //BA.debugLineNum = 1827;BA.debugLine="lblSideYardTimestamp.Text = ConvertDateTime(upd";
parent.mostCurrent._lblsideyardtimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1828;BA.debugLine="lblSideYard.Text = \"Side Yard v\" & fw_version";
parent.mostCurrent._lblsideyard.setText(BA.ObjectToCharSequence("Side Yard v"+_fw_version+" "+parent._sideyardarmedstatus));
 //BA.debugLineNum = 1829;BA.debugLine="lblSideYardWiFi.Text = \"WiFi: \" & wifi_strength";
parent.mostCurrent._lblsideyardwifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 if (true) break;
;
 //BA.debugLineNum = 1831;BA.debugLine="If lblSideYardBatt.Text.Contains(\"OFFLINE\") Then";

case 17:
//if
this.state = 24;
if (parent.mostCurrent._lblsideyardbatt.getText().contains("OFFLINE")) { 
this.state = 19;
}else if(parent.mostCurrent._lbldrivewaybatt.getText().contains("OFFLINE")) { 
this.state = 21;
}else if(parent.mostCurrent._lblfrontdoorbatt.getText().contains("OFFLINE")) { 
this.state = 23;
}if (true) break;

case 19:
//C
this.state = 24;
 //BA.debugLineNum = 1832;BA.debugLine="lblSideYardBatt.Text = \"Batt: \" & NumberFormat2";
parent.mostCurrent._lblsideyardbatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V "+parent._sideyardbattstatus));
 if (true) break;

case 21:
//C
this.state = 24;
 //BA.debugLineNum = 1834;BA.debugLine="lblDrivewayBatt.Text = \"Batt: \" & NumberFormat2";
parent.mostCurrent._lbldrivewaybatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V "+parent._drivewaybattstatus));
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 1836;BA.debugLine="lblFrontDoorBatt.Text = \"Batt: \" & NumberFormat";
parent.mostCurrent._lblfrontdoorbatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V "+parent._frontdoorbattstatus));
 if (true) break;

case 24:
//C
this.state = 27;
;
 if (true) break;

case 26:
//C
this.state = 27;
this.catchState = 0;
 //BA.debugLineNum = 1839;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("33080274",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 27:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1841;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 1842;BA.debugLine="End Sub";
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
>>>>>>> origin/master
public static String  _getcomfort(String _dht11comfortstatus) throws Exception{
String _localcomfortstatus = "";
 //BA.debugLineNum = 441;BA.debugLine="Sub GetComfort(DHT11ComfortStatus As String) As St";
 //BA.debugLineNum = 442;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = "";
 //BA.debugLineNum = 443;BA.debugLine="Select Case DHT11ComfortStatus";
switch (BA.switchObjectToInt(_dht11comfortstatus,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(8),BA.NumberToString(9),BA.NumberToString(10))) {
case 0: {
 //BA.debugLineNum = 445;BA.debugLine="localcomfortstatus = \"OK\"";
_localcomfortstatus = "OK";
 break; }
case 1: {
 //BA.debugLineNum = 447;BA.debugLine="localcomfortstatus = \"Too hot\"";
_localcomfortstatus = "Too hot";
 break; }
case 2: {
 //BA.debugLineNum = 449;BA.debugLine="localcomfortstatus = \"Too cold\"";
_localcomfortstatus = "Too cold";
 break; }
case 3: {
 //BA.debugLineNum = 451;BA.debugLine="localcomfortstatus = \"Too dry\"";
_localcomfortstatus = "Too dry";
 break; }
case 4: {
 //BA.debugLineNum = 453;BA.debugLine="localcomfortstatus = \"Hot and dry\"";
_localcomfortstatus = "Hot and dry";
 break; }
case 5: {
 //BA.debugLineNum = 455;BA.debugLine="localcomfortstatus = \"Cold and dry\"";
_localcomfortstatus = "Cold and dry";
 break; }
case 6: {
 //BA.debugLineNum = 457;BA.debugLine="localcomfortstatus = \"Too humid\"";
_localcomfortstatus = "Too humid";
 break; }
case 7: {
 //BA.debugLineNum = 459;BA.debugLine="localcomfortstatus = \"Hot and humid\"";
_localcomfortstatus = "Hot and humid";
 break; }
case 8: {
 //BA.debugLineNum = 461;BA.debugLine="localcomfortstatus = \"Cold and humid\"";
_localcomfortstatus = "Cold and humid";
 break; }
default: {
 //BA.debugLineNum = 463;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = "Unknown";
 break; }
}
;
 //BA.debugLineNum = 465;BA.debugLine="Return localcomfortstatus";
if (true) return _localcomfortstatus;
 //BA.debugLineNum = 466;BA.debugLine="End Sub";
return "";
}
<<<<<<< HEAD
=======
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _getcommandid(String _json) throws Exception{
ResumableSub_GetCommandID rsub = new ResumableSub_GetCommandID(null,_json);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_GetCommandID extends BA.ResumableSub {
public ResumableSub_GetCommandID(cloyd.smart.home.monitor.main parent,String _json) {
this.parent = parent;
this._json = _json;
}
cloyd.smart.home.monitor.main parent;
String _json;
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;

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
 //BA.debugLineNum = 1666;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 6;
this.catchState = 5;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 6;
this.catchState = 5;
 //BA.debugLineNum = 1667;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1668;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1669;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1684;BA.debugLine="commandID = root.Get(\"id\")";
parent._commandid = BA.ObjectToString(_root.Get((Object)("id")));
 //BA.debugLineNum = 1685;BA.debugLine="Log(\"commandID: \" & commandID)";
anywheresoftware.b4a.keywords.Common.LogImpl("32883604","commandID: "+parent._commandid,0);
 if (true) break;

case 5:
//C
this.state = 6;
this.catchState = 0;
 //BA.debugLineNum = 1707;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("32883626",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 6:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1710;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 1711;BA.debugLine="End Sub";
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
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _getcommandresponse(anywheresoftware.b4a.objects.ImageViewWrapper _iv,String _camera,String _attempts,String _attemptsallowed) throws Exception{
ResumableSub_GetCommandResponse rsub = new ResumableSub_GetCommandResponse(null,_iv,_camera,_attempts,_attemptsallowed);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_GetCommandResponse extends BA.ResumableSub {
public ResumableSub_GetCommandResponse(cloyd.smart.home.monitor.main parent,anywheresoftware.b4a.objects.ImageViewWrapper _iv,String _camera,String _attempts,String _attemptsallowed) {
this.parent = parent;
this._iv = _iv;
this._camera = _camera;
this._attempts = _attempts;
this._attemptsallowed = _attemptsallowed;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.objects.ImageViewWrapper _iv;
String _camera;
String _attempts;
String _attemptsallowed;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
{
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,null);return;}
case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1500;BA.debugLine="GetCommandStatus(response)";
_getcommandstatus(parent._response);
 //BA.debugLineNum = 1501;BA.debugLine="If commandComplete Then";
if (true) break;

case 1:
//if
this.state = 4;
if (parent._commandcomplete) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1503;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-\"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera);
 //BA.debugLineNum = 1504;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 19;
return;
case 19:
//C
this.state = 4;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1506;BA.debugLine="Dim rs As ResumableSub = GetCameraInfo(response,";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcamerainfo(parent._response,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1507;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 20;
return;
case 20:
//C
this.state = 4;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1509;BA.debugLine="Dim rs As ResumableSub = DownloadImage(\"https://";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _downloadimage("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg",_iv,_camera);
 //BA.debugLineNum = 1510;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 21;
return;
case 21:
//C
this.state = 4;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1511;BA.debugLine="Return True";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)(anywheresoftware.b4a.keywords.Common.True));return;};
 if (true) break;

case 4:
//C
this.state = 5;
;
 //BA.debugLineNum = 1514;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-\"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera);
 //BA.debugLineNum = 1515;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 22;
return;
case 22:
//C
this.state = 5;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1517;BA.debugLine="Dim rs As ResumableSub = GetCameraInfo(response,T";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcamerainfo(parent._response,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1518;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 23;
return;
case 23:
//C
this.state = 5;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1520;BA.debugLine="If prevCameraThumbnail <> \"\" And prevCameraThumbn";
if (true) break;

case 5:
//if
this.state = 18;
if ((parent._prevcamerathumbnail).equals("") == false && (parent._prevcamerathumbnail).equals(parent._camerathumbnail) == false) { 
this.state = 7;
}else {
this.state = 9;
}if (true) break;

case 7:
//C
this.state = 18;
 //BA.debugLineNum = 1521;BA.debugLine="Log(\"**** ALRIGHT \" & attempts & \"/\" & attemptsA";
anywheresoftware.b4a.keywords.Common.LogImpl("32555926","**** ALRIGHT "+_attempts+"/"+_attemptsallowed+" *****",0);
 //BA.debugLineNum = 1522;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-\"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera);
 //BA.debugLineNum = 1523;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 24;
return;
case 24:
//C
this.state = 18;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1525;BA.debugLine="Dim rs As ResumableSub = GetCameraInfo(response,";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcamerainfo(parent._response,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1526;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 25;
return;
case 25:
//C
this.state = 18;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1528;BA.debugLine="Dim rs As ResumableSub = DownloadImage(\"https://";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _downloadimage("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg",_iv,_camera);
 //BA.debugLineNum = 1529;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 26;
return;
case 26:
//C
this.state = 18;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1530;BA.debugLine="Return True";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)(anywheresoftware.b4a.keywords.Common.True));return;};
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1532;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 10:
//if
this.state = 17;
if ((_camera).equals("347574")) { 
this.state = 12;
}else if((_camera).equals("236967")) { 
this.state = 14;
}else if((_camera).equals("458236")) { 
this.state = 16;
}if (true) break;

case 12:
//C
this.state = 17;
 //BA.debugLineNum = 1533;BA.debugLine="lblStatus.Text = \"Awaiting for the Driveway thu";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Driveway thumbnail... "+_attempts+"/"+_attemptsallowed));
 if (true) break;

case 14:
//C
this.state = 17;
 //BA.debugLineNum = 1535;BA.debugLine="lblStatus.Text = \"Awaiting for the Front Door t";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Front Door thumbnail...  "+_attempts+"/"+_attemptsallowed));
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 1537;BA.debugLine="lblStatus.Text = \"Awaiting for the Side Yard th";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Side Yard thumbnail... "+_attempts+"/"+_attemptsallowed));
 if (true) break;

case 17:
//C
this.state = 18;
;
 //BA.debugLineNum = 1539;BA.debugLine="Return False";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)(anywheresoftware.b4a.keywords.Common.False));return;};
 if (true) break;

case 18:
//C
this.state = -1;
;
 //BA.debugLineNum = 1541;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _getcommandstatus(String _json) throws Exception{
ResumableSub_GetCommandStatus rsub = new ResumableSub_GetCommandStatus(null,_json);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_GetCommandStatus extends BA.ResumableSub {
public ResumableSub_GetCommandStatus(cloyd.smart.home.monitor.main parent,String _json) {
this.parent = parent;
this._json = _json;
}
cloyd.smart.home.monitor.main parent;
String _json;
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;

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
 //BA.debugLineNum = 1714;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 6;
this.catchState = 5;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 6;
this.catchState = 5;
 //BA.debugLineNum = 1715;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1716;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1717;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1718;BA.debugLine="commandComplete = root.Get(\"complete\")";
parent._commandcomplete = BA.ObjectToBoolean(_root.Get((Object)("complete")));
 //BA.debugLineNum = 1719;BA.debugLine="Log(\"commandComplete: \" & commandComplete)";
anywheresoftware.b4a.keywords.Common.LogImpl("32949126","commandComplete: "+BA.ObjectToString(parent._commandcomplete),0);
 if (true) break;

case 5:
//C
this.state = 6;
this.catchState = 0;
 //BA.debugLineNum = 1721;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("32949128",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 6:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1724;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 1725;BA.debugLine="End Sub";
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
>>>>>>> origin/master
public static float  _getfreemem() throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
int _mm = 0;
int _tm = 0;
int _fm = 0;
int _total = 0;
 //BA.debugLineNum = 468;BA.debugLine="Sub GetFreeMem As Float";
 //BA.debugLineNum = 469;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 470;BA.debugLine="Dim MM, TM, FM, Total As Int";
_mm = 0;
_tm = 0;
_fm = 0;
_total = 0;
 //BA.debugLineNum = 471;BA.debugLine="r.Target = r.RunStaticMethod(\"java.lang.Runtime\",";
_r.Target = _r.RunStaticMethod("java.lang.Runtime","getRuntime",(Object[])(anywheresoftware.b4a.keywords.Common.Null),(String[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 472;BA.debugLine="MM = r.RunMethod(\"maxMemory\")";
_mm = (int)(BA.ObjectToNumber(_r.RunMethod("maxMemory")));
 //BA.debugLineNum = 473;BA.debugLine="FM = r.RunMethod(\"freeMemory\")";
_fm = (int)(BA.ObjectToNumber(_r.RunMethod("freeMemory")));
 //BA.debugLineNum = 474;BA.debugLine="TM = r.RunMethod(\"totalMemory\")";
_tm = (int)(BA.ObjectToNumber(_r.RunMethod("totalMemory")));
 //BA.debugLineNum = 475;BA.debugLine="Total = MM + FM - TM";
_total = (int) (_mm+_fm-_tm);
 //BA.debugLineNum = 476;BA.debugLine="Return Total / 1024";
if (true) return (float) (_total/(double)1024);
 //BA.debugLineNum = 477;BA.debugLine="End Sub";
return 0f;
}
<<<<<<< HEAD
=======
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _gethomescreen(String _json) throws Exception{
ResumableSub_GetHomescreen rsub = new ResumableSub_GetHomescreen(null,_json);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_GetHomescreen extends BA.ResumableSub {
public ResumableSub_GetHomescreen(cloyd.smart.home.monitor.main parent,String _json) {
this.parent = parent;
this._json = _json;
}
cloyd.smart.home.monitor.main parent;
String _json;
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.List _devices = null;
anywheresoftware.b4a.objects.collections.Map _coldevices = null;
String _battery_state = "";
int _device_id = 0;
String _active = "";
int _battery = 0;
String _enabled = "";
String _status = "";
anywheresoftware.b4j.object.JavaObject _jo = null;
float _radius = 0f;
float _dx = 0f;
float _dy = 0f;
anywheresoftware.b4a.objects.collections.Map _network = null;
String _armednetwork = "";
anywheresoftware.b4a.BA.IterableList group6;
int index6;
int groupLen6;

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
 //BA.debugLineNum = 1890;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 94;
this.catchState = 93;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 93;
 //BA.debugLineNum = 1891;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1892;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1893;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1894;BA.debugLine="Dim devices As List = root.Get(\"devices\")";
_devices = new anywheresoftware.b4a.objects.collections.List();
_devices.setObject((java.util.List)(_root.Get((Object)("devices"))));
 //BA.debugLineNum = 1895;BA.debugLine="For Each coldevices As Map In devices";
if (true) break;

case 4:
//for
this.state = 87;
_coldevices = new anywheresoftware.b4a.objects.collections.Map();
group6 = _devices;
index6 = 0;
groupLen6 = group6.getSize();
this.state = 95;
if (true) break;

case 95:
//C
this.state = 87;
if (index6 < groupLen6) {
this.state = 6;
_coldevices.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(group6.Get(index6)));}
if (true) break;

case 96:
//C
this.state = 95;
index6++;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1896;BA.debugLine="Dim battery_state As String = coldevices.Get(\"b";
_battery_state = BA.ObjectToString(_coldevices.Get((Object)("battery_state")));
 //BA.debugLineNum = 1900;BA.debugLine="Dim device_id As Int = coldevices.Get(\"device_i";
_device_id = (int)(BA.ObjectToNumber(_coldevices.Get((Object)("device_id"))));
 //BA.debugLineNum = 1903;BA.debugLine="Dim active As String = coldevices.Get(\"active\")";
_active = BA.ObjectToString(_coldevices.Get((Object)("active")));
 //BA.debugLineNum = 1906;BA.debugLine="Dim battery As Int = coldevices.Get(\"battery\")";
_battery = (int)(BA.ObjectToNumber(_coldevices.Get((Object)("battery"))));
 //BA.debugLineNum = 1907;BA.debugLine="Dim enabled As String = coldevices.Get(\"enabled";
_enabled = BA.ObjectToString(_coldevices.Get((Object)("enabled")));
 //BA.debugLineNum = 1915;BA.debugLine="Dim status As String = coldevices.Get(\"status\")";
_status = BA.ObjectToString(_coldevices.Get((Object)("status")));
 //BA.debugLineNum = 1918;BA.debugLine="If device_id = \"347574\" Then";
if (true) break;

case 7:
//if
this.state = 86;
if (_device_id==(double)(Double.parseDouble("347574"))) { 
this.state = 9;
}else if(_device_id==(double)(Double.parseDouble("236967"))) { 
this.state = 35;
}else if(_device_id==(double)(Double.parseDouble("458236"))) { 
this.state = 61;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1919;BA.debugLine="If active = \"armed\" Then";
if (true) break;

case 10:
//if
this.state = 21;
if ((_active).equals("armed")) { 
this.state = 12;
}else {
this.state = 20;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1920;BA.debugLine="If enabled = \"true\" Then";
if (true) break;

case 13:
//if
this.state = 18;
if ((_enabled).equals("true")) { 
this.state = 15;
}else {
this.state = 17;
}if (true) break;

case 15:
//C
this.state = 18;
 //BA.debugLineNum = 1921;BA.debugLine="drivewayArmedStatus = \"\"";
parent._drivewayarmedstatus = "";
 //BA.debugLineNum = 1922;BA.debugLine="Dim jo As JavaObject = lblDriveway";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lbldriveway.getObject()));
 //BA.debugLineNum = 1923;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As F";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1924;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Black)});
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 1926;BA.debugLine="drivewayArmedStatus = \"MOTION DETECTION IS D";
parent._drivewayarmedstatus = "MOTION DETECTION IS DISABLED!";
 //BA.debugLineNum = 1927;BA.debugLine="Dim jo As JavaObject = lblDriveway";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lbldriveway.getObject()));
 //BA.debugLineNum = 1928;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As F";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1929;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;

case 18:
//C
this.state = 21;
;
 if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 1932;BA.debugLine="drivewayArmedStatus = \"NOT ARMED!\"";
parent._drivewayarmedstatus = "NOT ARMED!";
 //BA.debugLineNum = 1933;BA.debugLine="Dim jo As JavaObject = lblDriveway";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lbldriveway.getObject()));
 //BA.debugLineNum = 1934;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1935;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;
;
 //BA.debugLineNum = 1937;BA.debugLine="If battery > 1 And battery_state = \"ok\" Then";

case 21:
//if
this.state = 30;
if (_battery>1 && (_battery_state).equals("ok")) { 
this.state = 23;
}else {
this.state = 25;
}if (true) break;

case 23:
//C
this.state = 30;
 //BA.debugLineNum = 1938;BA.debugLine="drivewayBattStatus = \"[Level \" & battery & \"";
parent._drivewaybattstatus = "[Level "+BA.NumberToString(_battery)+" - "+_battery_state.toUpperCase()+"]";
 //BA.debugLineNum = 1939;BA.debugLine="Dim jo As JavaObject = lblDrivewayBatt";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lbldrivewaybatt.getObject()));
 //BA.debugLineNum = 1940;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1941;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Green)});
 if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 1943;BA.debugLine="drivewayBattStatus = \"[Level \" & battery & \"";
parent._drivewaybattstatus = "[Level "+BA.NumberToString(_battery)+" - "+_battery_state.toUpperCase()+"]";
 //BA.debugLineNum = 1944;BA.debugLine="If drivewayBattStatus = \"[Level 1 - LOW]\" The";
if (true) break;

case 26:
//if
this.state = 29;
if ((parent._drivewaybattstatus).equals("[Level 1 - LOW]")) { 
this.state = 28;
}if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 1945;BA.debugLine="drivewayBattStatus = \"[REPLACE]\"";
parent._drivewaybattstatus = "[REPLACE]";
 if (true) break;

case 29:
//C
this.state = 30;
;
 //BA.debugLineNum = 1947;BA.debugLine="Dim jo As JavaObject = lblDrivewayBatt";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lbldrivewaybatt.getObject()));
 //BA.debugLineNum = 1948;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1949;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;
;
 //BA.debugLineNum = 1951;BA.debugLine="If status = \"offline\" Then";

case 30:
//if
this.state = 33;
if ((_status).equals("offline")) { 
this.state = 32;
}if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 1952;BA.debugLine="drivewayBattStatus = \"OFFLINE\"";
parent._drivewaybattstatus = "OFFLINE";
 //BA.debugLineNum = 1953;BA.debugLine="Dim jo As JavaObject = lblDrivewayBatt";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lbldrivewaybatt.getObject()));
 //BA.debugLineNum = 1954;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1955;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;

case 33:
//C
this.state = 86;
;
 if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 1958;BA.debugLine="If active = \"armed\" Then";
if (true) break;

case 36:
//if
this.state = 47;
if ((_active).equals("armed")) { 
this.state = 38;
}else {
this.state = 46;
}if (true) break;

case 38:
//C
this.state = 39;
 //BA.debugLineNum = 1959;BA.debugLine="If enabled = \"true\" Then";
if (true) break;

case 39:
//if
this.state = 44;
if ((_enabled).equals("true")) { 
this.state = 41;
}else {
this.state = 43;
}if (true) break;

case 41:
//C
this.state = 44;
 //BA.debugLineNum = 1960;BA.debugLine="frontdoorArmedStatus = \"\"";
parent._frontdoorarmedstatus = "";
 //BA.debugLineNum = 1961;BA.debugLine="Dim jo As JavaObject = lblFrontDoor";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblfrontdoor.getObject()));
 //BA.debugLineNum = 1962;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As F";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1963;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Black)});
 if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 1965;BA.debugLine="frontdoorArmedStatus = \"MOTION DETECTION IS";
parent._frontdoorarmedstatus = "MOTION DETECTION IS DISABLED!";
 //BA.debugLineNum = 1966;BA.debugLine="Dim jo As JavaObject = lblFrontDoor";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblfrontdoor.getObject()));
 //BA.debugLineNum = 1967;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As F";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1968;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;

case 44:
//C
this.state = 47;
;
 if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 1971;BA.debugLine="frontdoorArmedStatus = \"NOT ARMED!\"";
parent._frontdoorarmedstatus = "NOT ARMED!";
 //BA.debugLineNum = 1972;BA.debugLine="Dim jo As JavaObject = lblFrontDoor";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblfrontdoor.getObject()));
 //BA.debugLineNum = 1973;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1974;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;
;
 //BA.debugLineNum = 1976;BA.debugLine="If battery > 1 And battery_state = \"ok\" Then";

case 47:
//if
this.state = 56;
if (_battery>1 && (_battery_state).equals("ok")) { 
this.state = 49;
}else {
this.state = 51;
}if (true) break;

case 49:
//C
this.state = 56;
 //BA.debugLineNum = 1977;BA.debugLine="frontdoorBattStatus = \"[Level \" & battery & \"";
parent._frontdoorbattstatus = "[Level "+BA.NumberToString(_battery)+" - "+_battery_state.toUpperCase()+"]";
 //BA.debugLineNum = 1978;BA.debugLine="Dim jo As JavaObject = lblFrontDoorBatt";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblfrontdoorbatt.getObject()));
 //BA.debugLineNum = 1979;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1980;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Green)});
 if (true) break;

case 51:
//C
this.state = 52;
 //BA.debugLineNum = 1982;BA.debugLine="frontdoorBattStatus = \"[Level \" & battery & \"";
parent._frontdoorbattstatus = "[Level "+BA.NumberToString(_battery)+" - "+_battery_state.toUpperCase()+"]";
 //BA.debugLineNum = 1983;BA.debugLine="If frontdoorBattStatus = \"[Level 1 - LOW]\" Th";
if (true) break;

case 52:
//if
this.state = 55;
if ((parent._frontdoorbattstatus).equals("[Level 1 - LOW]")) { 
this.state = 54;
}if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 1984;BA.debugLine="frontdoorBattStatus = \"[REPLACE]\"";
parent._frontdoorbattstatus = "[REPLACE]";
 if (true) break;

case 55:
//C
this.state = 56;
;
 //BA.debugLineNum = 1986;BA.debugLine="Dim jo As JavaObject = lblFrontDoorBatt";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblfrontdoorbatt.getObject()));
 //BA.debugLineNum = 1987;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1988;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;
;
 //BA.debugLineNum = 1990;BA.debugLine="If status = \"offline\" Then";

case 56:
//if
this.state = 59;
if ((_status).equals("offline")) { 
this.state = 58;
}if (true) break;

case 58:
//C
this.state = 59;
 //BA.debugLineNum = 1991;BA.debugLine="frontdoorBattStatus = \"OFFLINE\"";
parent._frontdoorbattstatus = "OFFLINE";
 //BA.debugLineNum = 1992;BA.debugLine="Dim jo As JavaObject = lblFrontDoorBatt";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblfrontdoorbatt.getObject()));
 //BA.debugLineNum = 1993;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1994;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;

case 59:
//C
this.state = 86;
;
 if (true) break;

case 61:
//C
this.state = 62;
 //BA.debugLineNum = 1997;BA.debugLine="If active = \"armed\" Then";
if (true) break;

case 62:
//if
this.state = 73;
if ((_active).equals("armed")) { 
this.state = 64;
}else {
this.state = 72;
}if (true) break;

case 64:
//C
this.state = 65;
 //BA.debugLineNum = 1998;BA.debugLine="If enabled = \"true\" Then";
if (true) break;

case 65:
//if
this.state = 70;
if ((_enabled).equals("true")) { 
this.state = 67;
}else {
this.state = 69;
}if (true) break;

case 67:
//C
this.state = 70;
 //BA.debugLineNum = 1999;BA.debugLine="sideyardArmedStatus = \"\"";
parent._sideyardarmedstatus = "";
 //BA.debugLineNum = 2000;BA.debugLine="Dim jo As JavaObject = lblSideYard";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblsideyard.getObject()));
 //BA.debugLineNum = 2001;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As F";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 2002;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Black)});
 if (true) break;

case 69:
//C
this.state = 70;
 //BA.debugLineNum = 2004;BA.debugLine="sideyardArmedStatus =\"MOTION DETECTION IS DI";
parent._sideyardarmedstatus = "MOTION DETECTION IS DISABLED!";
 //BA.debugLineNum = 2005;BA.debugLine="Dim jo As JavaObject = lblSideYard";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblsideyard.getObject()));
 //BA.debugLineNum = 2006;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As F";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 2007;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;

case 70:
//C
this.state = 73;
;
 if (true) break;

case 72:
//C
this.state = 73;
 //BA.debugLineNum = 2010;BA.debugLine="sideyardArmedStatus = \"NOT ARMED!\"";
parent._sideyardarmedstatus = "NOT ARMED!";
 //BA.debugLineNum = 2011;BA.debugLine="Dim jo As JavaObject = lblSideYard";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblsideyard.getObject()));
 //BA.debugLineNum = 2012;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 2013;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;
;
 //BA.debugLineNum = 2015;BA.debugLine="If battery > 1 And battery_state = \"ok\" Then";

case 73:
//if
this.state = 82;
if (_battery>1 && (_battery_state).equals("ok")) { 
this.state = 75;
}else {
this.state = 77;
}if (true) break;

case 75:
//C
this.state = 82;
 //BA.debugLineNum = 2016;BA.debugLine="sideyardBattStatus = \"[Level \" & battery & \"";
parent._sideyardbattstatus = "[Level "+BA.NumberToString(_battery)+" - "+_battery_state.toUpperCase()+"]";
 //BA.debugLineNum = 2017;BA.debugLine="Dim jo As JavaObject = lblSideYardBatt";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblsideyardbatt.getObject()));
 //BA.debugLineNum = 2018;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 2019;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Green)});
 if (true) break;

case 77:
//C
this.state = 78;
 //BA.debugLineNum = 2021;BA.debugLine="sideyardBattStatus = \"[Level \" & battery & \"";
parent._sideyardbattstatus = "[Level "+BA.NumberToString(_battery)+" - "+_battery_state.toUpperCase()+"]";
 //BA.debugLineNum = 2022;BA.debugLine="If sideyardBattStatus = \"[Level 1 - LOW]\" The";
if (true) break;

case 78:
//if
this.state = 81;
if ((parent._sideyardbattstatus).equals("[Level 1 - LOW]")) { 
this.state = 80;
}if (true) break;

case 80:
//C
this.state = 81;
 //BA.debugLineNum = 2023;BA.debugLine="sideyardBattStatus = \"[REPLACE]\"";
parent._sideyardbattstatus = "[REPLACE]";
 if (true) break;

case 81:
//C
this.state = 82;
;
 //BA.debugLineNum = 2025;BA.debugLine="Dim jo As JavaObject = lblSideYardBatt";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblsideyardbatt.getObject()));
 //BA.debugLineNum = 2026;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 2027;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;
;
 //BA.debugLineNum = 2029;BA.debugLine="If status = \"offline\" Then";

case 82:
//if
this.state = 85;
if ((_status).equals("offline")) { 
this.state = 84;
}if (true) break;

case 84:
//C
this.state = 85;
 //BA.debugLineNum = 2030;BA.debugLine="sideyardBattStatus = \"OFFLINE\"";
parent._sideyardbattstatus = "OFFLINE";
 //BA.debugLineNum = 2031;BA.debugLine="Dim jo As JavaObject = lblSideYardBatt";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblsideyardbatt.getObject()));
 //BA.debugLineNum = 2032;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 2033;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;

case 85:
//C
this.state = 86;
;
 if (true) break;

case 86:
//C
this.state = 96;
;
 if (true) break;
if (true) break;

case 87:
//C
this.state = 88;
;
 //BA.debugLineNum = 2040;BA.debugLine="Dim network As Map = root.Get(\"network\")";
_network = new anywheresoftware.b4a.objects.collections.Map();
_network.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("network"))));
 //BA.debugLineNum = 2044;BA.debugLine="Dim armedNetwork As String = network.Get(\"armed\"";
_armednetwork = BA.ObjectToString(_network.Get((Object)("armed")));
 //BA.debugLineNum = 2050;BA.debugLine="If armedNetwork <> \"true\" Then";
if (true) break;

case 88:
//if
this.state = 91;
if ((_armednetwork).equals("true") == false) { 
this.state = 90;
}if (true) break;

case 90:
//C
this.state = 91;
 //BA.debugLineNum = 2051;BA.debugLine="drivewayArmedStatus = \"SYSTEM NOT ARMED!\"";
parent._drivewayarmedstatus = "SYSTEM NOT ARMED!";
 //BA.debugLineNum = 2052;BA.debugLine="Dim jo As JavaObject = lblDriveway";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lbldriveway.getObject()));
 //BA.debugLineNum = 2053;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Floa";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 2054;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 //BA.debugLineNum = 2056;BA.debugLine="frontdoorArmedStatus = \"SYSTEM NOT ARMED!\"";
parent._frontdoorarmedstatus = "SYSTEM NOT ARMED!";
 //BA.debugLineNum = 2057;BA.debugLine="Dim jo As JavaObject = lblFrontDoor";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblfrontdoor.getObject()));
 //BA.debugLineNum = 2058;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Floa";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 2059;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 //BA.debugLineNum = 2061;BA.debugLine="sideyardArmedStatus = \"SYSTEM NOT ARMED!\"";
parent._sideyardarmedstatus = "SYSTEM NOT ARMED!";
 //BA.debugLineNum = 2062;BA.debugLine="Dim jo As JavaObject = lblSideYard";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblsideyard.getObject()));
 //BA.debugLineNum = 2063;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Floa";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 2064;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;

case 91:
//C
this.state = 94;
;
 if (true) break;

case 93:
//C
this.state = 94;
this.catchState = 0;
 //BA.debugLineNum = 2067;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("33211442",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 94:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 2069;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 2070;BA.debugLine="End Sub";
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
>>>>>>> origin/master
public static String  _getperception(String _dht11perception) throws Exception{
String _localperception = "";
 //BA.debugLineNum = 407;BA.debugLine="Sub GetPerception(DHT11Perception As String) As St";
 //BA.debugLineNum = 418;BA.debugLine="Dim localperception As String";
_localperception = "";
 //BA.debugLineNum = 419;BA.debugLine="Select Case DHT11Perception";
switch (BA.switchObjectToInt(_dht11perception,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(7))) {
case 0: {
 //BA.debugLineNum = 422;BA.debugLine="localperception = \"A bit dry\"";
_localperception = "A bit dry";
 break; }
case 1: {
 //BA.debugLineNum = 424;BA.debugLine="localperception = \"Very comfortable\"";
_localperception = "Very comfortable";
 break; }
case 2: {
 //BA.debugLineNum = 426;BA.debugLine="localperception = \"Comfortable\"";
_localperception = "Comfortable";
 break; }
case 3: {
 //BA.debugLineNum = 428;BA.debugLine="localperception = \"Okay but sticky\"";
_localperception = "Okay but sticky";
 break; }
case 4: {
 //BA.debugLineNum = 430;BA.debugLine="localperception = \"Slightly uncomfortable and t";
_localperception = "Slightly uncomfortable and the humidity is at upper limit";
 break; }
case 5: {
 //BA.debugLineNum = 432;BA.debugLine="localperception = \"Very humid and uncomfortable";
_localperception = "Very humid and uncomfortable";
 break; }
case 6: {
 //BA.debugLineNum = 434;BA.debugLine="localperception = \"Extremely uncomfortable and";
_localperception = "Extremely uncomfortable and oppressive";
 break; }
case 7: {
 //BA.debugLineNum = 436;BA.debugLine="localperception = \"Humidity is severely high an";
_localperception = "Humidity is severely high and intolerable";
 break; }
}
;
 //BA.debugLineNum = 438;BA.debugLine="Return localperception";
if (true) return _localperception;
 //BA.debugLineNum = 439;BA.debugLine="End Sub";
return "";
}
<<<<<<< HEAD
=======
public static String  _getresterror(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
int _code = 0;
String _message = "";
 //BA.debugLineNum = 1727;BA.debugLine="Sub GetRESTError(json As String) As String";
 //BA.debugLineNum = 1728;BA.debugLine="Try";
try { //BA.debugLineNum = 1734;BA.debugLine="If json.Contains(\"<h1>Not Found</h1>\") Then";
if (_json.contains("<h1>Not Found</h1>")) { 
 //BA.debugLineNum = 1735;BA.debugLine="Return \"REST endpoint URL not found. Try again.";
if (true) return "REST endpoint URL not found. Try again.";
 }else {
 //BA.debugLineNum = 1737;BA.debugLine="If json.IndexOf(\"{\") <> -1 Then";
if (_json.indexOf("{")!=-1) { 
 //BA.debugLineNum = 1738;BA.debugLine="json = json.SubString(json.IndexOf(\"{\"))";
_json = _json.substring(_json.indexOf("{"));
 //BA.debugLineNum = 1739;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1740;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1741;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1742;BA.debugLine="Dim code As Int = root.Get(\"code\")";
_code = (int)(BA.ObjectToNumber(_root.Get((Object)("code"))));
 //BA.debugLineNum = 1743;BA.debugLine="Dim message As String = root.Get(\"message\")";
_message = BA.ObjectToString(_root.Get((Object)("message")));
 //BA.debugLineNum = 1744;BA.debugLine="Log(\"Code: \" & code & \" Message: \" & message)";
anywheresoftware.b4a.keywords.Common.LogImpl("33014673","Code: "+BA.NumberToString(_code)+" Message: "+_message,0);
 //BA.debugLineNum = 1745;BA.debugLine="Return \"Code: \" & code & \" Message: \" & messag";
if (true) return "Code: "+BA.NumberToString(_code)+" Message: "+_message;
 }else {
 //BA.debugLineNum = 1747;BA.debugLine="Return json";
if (true) return _json;
 };
 };
 } 
       catch (Exception e19) {
			processBA.setLastException(e19); //BA.debugLineNum = 1751;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("33014680",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1753;BA.debugLine="Return json";
if (true) return _json;
 };
 //BA.debugLineNum = 1755;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _getsyncmoduleinfo(String _json) throws Exception{
ResumableSub_GetSyncModuleInfo rsub = new ResumableSub_GetSyncModuleInfo(null,_json);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_GetSyncModuleInfo extends BA.ResumableSub {
public ResumableSub_GetSyncModuleInfo(cloyd.smart.home.monitor.main parent,String _json) {
this.parent = parent;
this._json = _json;
}
cloyd.smart.home.monitor.main parent;
String _json;
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.Map _syncmodule = null;
int _wifi_strength = 0;
String _os_version = "";
String _fw_version = "";
String _status = "";
anywheresoftware.b4j.object.JavaObject _jo = null;
float _radius = 0f;
float _dx = 0f;
float _dy = 0f;

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
 //BA.debugLineNum = 1845;BA.debugLine="Try";
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
 //BA.debugLineNum = 1846;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1847;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1848;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1849;BA.debugLine="Dim syncmodule As Map = root.Get(\"syncmodule\")";
_syncmodule = new anywheresoftware.b4a.objects.collections.Map();
_syncmodule.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("syncmodule"))));
 //BA.debugLineNum = 1853;BA.debugLine="Dim wifi_strength As Int = syncmodule.Get(\"wifi_";
_wifi_strength = (int)(BA.ObjectToNumber(_syncmodule.Get((Object)("wifi_strength"))));
 //BA.debugLineNum = 1854;BA.debugLine="Dim os_version As String = syncmodule.Get(\"os_ve";
_os_version = BA.ObjectToString(_syncmodule.Get((Object)("os_version")));
 //BA.debugLineNum = 1871;BA.debugLine="Dim fw_version As String = syncmodule.Get(\"fw_ve";
_fw_version = BA.ObjectToString(_syncmodule.Get((Object)("fw_version")));
 //BA.debugLineNum = 1873;BA.debugLine="Dim status As String = syncmodule.Get(\"status\")";
_status = BA.ObjectToString(_syncmodule.Get((Object)("status")));
 //BA.debugLineNum = 1874;BA.debugLine="Dim jo As JavaObject = lblSyncModule";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._lblsyncmodule.getObject()));
 //BA.debugLineNum = 1875;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Float";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1877;BA.debugLine="If status = \"online\" Then";
if (true) break;

case 4:
//if
this.state = 9;
if ((_status).equals("online")) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 1878;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Green)});
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1880;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 if (true) break;

case 9:
//C
this.state = 12;
;
 //BA.debugLineNum = 1882;BA.debugLine="lblSyncModule.Text = \"Sync Module is \" & status";
parent.mostCurrent._lblsyncmodule.setText(BA.ObjectToCharSequence("Sync Module is "+_status+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Wifi Strength: "+BA.NumberToString(_wifi_strength)+" bars"+anywheresoftware.b4a.keywords.Common.CRLF+"Firmware version: "+_fw_version+anywheresoftware.b4a.keywords.Common.CRLF+"OS version: "+_os_version));
 if (true) break;

case 11:
//C
this.state = 12;
this.catchState = 0;
 //BA.debugLineNum = 1884;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("33145768",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 12:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1886;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 1887;BA.debugLine="End Sub";
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
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _getunwatchedvideos() throws Exception{
ResumableSub_GetUnwatchedVideos rsub = new ResumableSub_GetUnwatchedVideos(null);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_GetUnwatchedVideos extends BA.ResumableSub {
public ResumableSub_GetUnwatchedVideos(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
int _unwatchedvideocount = 0;
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.List _media = null;
long _n = 0L;
anywheresoftware.b4a.objects.collections.Map _colmedia = null;
String _watched = "";
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
anywheresoftware.b4a.BA.IterableList group10;
int index10;
int groupLen10;

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
 //BA.debugLineNum = 2784;BA.debugLine="Try";
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
 //BA.debugLineNum = 2789;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-\"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/api/v1/accounts/88438/media/changed?since=-999999999-01-01T00:00:00+18:00&page=1");
 //BA.debugLineNum = 2790;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 21;
return;
case 21:
//C
this.state = 4;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2792;BA.debugLine="Dim unwatchedVideoCount As Int = 0";
_unwatchedvideocount = (int) (0);
 //BA.debugLineNum = 2793;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 2794;BA.debugLine="parser.Initialize(response)";
_parser.Initialize(parent._response);
 //BA.debugLineNum = 2795;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 2796;BA.debugLine="Dim media As List = root.Get(\"media\")";
_media = new anywheresoftware.b4a.objects.collections.List();
_media.setObject((java.util.List)(_root.Get((Object)("media"))));
 //BA.debugLineNum = 2798;BA.debugLine="Dim n As Long = DateTime.Now";
_n = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 2799;BA.debugLine="For Each colmedia As Map In media";
if (true) break;

case 4:
//for
this.state = 11;
_colmedia = new anywheresoftware.b4a.objects.collections.Map();
group10 = _media;
index10 = 0;
groupLen10 = group10.getSize();
this.state = 22;
if (true) break;

case 22:
//C
this.state = 11;
if (index10 < groupLen10) {
this.state = 6;
_colmedia.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(group10.Get(index10)));}
if (true) break;

case 23:
//C
this.state = 22;
index10++;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2800;BA.debugLine="Dim watched As String = colmedia.Get(\"watched\")";
_watched = BA.ObjectToString(_colmedia.Get((Object)("watched")));
 //BA.debugLineNum = 2804;BA.debugLine="If watched = False Then";
if (true) break;

case 7:
//if
this.state = 10;
if ((_watched).equals(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.False))) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 2805;BA.debugLine="unwatchedVideoCount = unwatchedVideoCount + 1";
_unwatchedvideocount = (int) (_unwatchedvideocount+1);
 if (true) break;

case 10:
//C
this.state = 23;
;
 if (true) break;
if (true) break;

case 11:
//C
this.state = 12;
;
 //BA.debugLineNum = 2808;BA.debugLine="Log(\"Loading unwatched videos took: \" & (DateTim";
anywheresoftware.b4a.keywords.Common.LogImpl("34718617","Loading unwatched videos took: "+BA.NumberToString((anywheresoftware.b4a.keywords.Common.DateTime.getNow()-_n))+"ms",0);
 //BA.debugLineNum = 2809;BA.debugLine="Dim lbl As B4XView = GetAllTabLabelsForBadge(Tab";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl.setObject((java.lang.Object)(_getalltablabelsforbadge(parent.mostCurrent._tabstrip1).Get((int) (5))));
 //BA.debugLineNum = 2810;BA.debugLine="badger1.SetBadge(lbl, unwatchedVideoCount)";
parent.mostCurrent._badger1._setbadge /*String*/ (_lbl,_unwatchedvideocount);
 //BA.debugLineNum = 2811;BA.debugLine="StateManager.SetSetting(\"UnwatchedVideoClips\",un";
parent.mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"UnwatchedVideoClips",BA.NumberToString(_unwatchedvideocount));
 //BA.debugLineNum = 2812;BA.debugLine="StateManager.SaveSettings";
parent.mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 2813;BA.debugLine="If unwatchedVideoCount > 0 Then";
if (true) break;

case 12:
//if
this.state = 17;
if (_unwatchedvideocount>0) { 
this.state = 14;
}else {
this.state = 16;
}if (true) break;

case 14:
//C
this.state = 17;
 //BA.debugLineNum = 2814;BA.debugLine="isThereUnwatchedVideo = True";
parent._isthereunwatchedvideo = anywheresoftware.b4a.keywords.Common.True;
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 2816;BA.debugLine="isThereUnwatchedVideo = False";
parent._isthereunwatchedvideo = anywheresoftware.b4a.keywords.Common.False;
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
 //BA.debugLineNum = 2819;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("34718628",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 20:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 2821;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 2822;BA.debugLine="End Sub";
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
>>>>>>> origin/master
public static String  _getversioncode() throws Exception{
String _appversion = "";
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
String _packagename = "";
 //BA.debugLineNum = 565;BA.debugLine="Sub GetVersionCode() As String";
 //BA.debugLineNum = 566;BA.debugLine="Dim AppVersion As String";
_appversion = "";
 //BA.debugLineNum = 567;BA.debugLine="Try";
try { //BA.debugLineNum = 568;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 569;BA.debugLine="Dim packageName As String";
_packagename = "";
 //BA.debugLineNum = 570;BA.debugLine="packageName =  Application.PackageName";
_packagename = anywheresoftware.b4a.keywords.Common.Application.getPackageName();
 //BA.debugLineNum = 571;BA.debugLine="AppVersion = pm.GetVersionName(packageName)";
_appversion = _pm.GetVersionName(_packagename);
 } 
       catch (Exception e8) {
<<<<<<< HEAD
			processBA.setLastException(e8); //BA.debugLineNum = 573;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("61376264",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
=======
			processBA.setLastException(e8); //BA.debugLineNum = 710;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("31376264",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
>>>>>>> origin/master
 };
 //BA.debugLineNum = 575;BA.debugLine="Return AppVersion";
if (true) return _appversion;
 //BA.debugLineNum = 576;BA.debugLine="End Sub";
return "";
}
<<<<<<< HEAD
=======
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _getvideos(String _json) throws Exception{
ResumableSub_GetVideos rsub = new ResumableSub_GetVideos(null,_json);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
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
String _watched = "";
String _medianame = "";
String _videoid = "";
Object _mytypes = null;
cloyd.smart.home.monitor.main._videoinfo _videos = null;
cloyd.smart.home.monitor.httpjob _j = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _image = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp2 = null;
anywheresoftware.b4a.objects.collections.List _list1 = null;
int _i = 0;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _in = null;
cloyd.smart.home.monitor.main._carddata _cd = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.BA.IterableList group8;
int index8;
int groupLen8;
int step40;
int limit40;

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
 //BA.debugLineNum = 2306;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 31;
this.catchState = 30;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 30;
 //BA.debugLineNum = 2307;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 2308;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 2309;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 2310;BA.debugLine="Dim media As List = root.Get(\"media\")";
_media = new anywheresoftware.b4a.objects.collections.List();
_media.setObject((java.util.List)(_root.Get((Object)("media"))));
 //BA.debugLineNum = 2312;BA.debugLine="clvActivity.Clear";
parent.mostCurrent._clvactivity._clear();
 //BA.debugLineNum = 2315;BA.debugLine="Pages = Array(False, False, False, False, False,";
parent._pages = anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.False),(Object)(anywheresoftware.b4a.keywords.Common.False),(Object)(anywheresoftware.b4a.keywords.Common.False),(Object)(anywheresoftware.b4a.keywords.Common.False),(Object)(anywheresoftware.b4a.keywords.Common.False),(Object)(anywheresoftware.b4a.keywords.Common.True)});
 //BA.debugLineNum = 2316;BA.debugLine="For Each colmedia As Map In media";
if (true) break;

case 4:
//for
this.state = 15;
_colmedia = new anywheresoftware.b4a.objects.collections.Map();
group8 = _media;
index8 = 0;
groupLen8 = group8.getSize();
this.state = 32;
if (true) break;

case 32:
//C
this.state = 15;
if (index8 < groupLen8) {
this.state = 6;
_colmedia.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(group8.Get(index8)));}
if (true) break;

case 33:
//C
this.state = 32;
index8++;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2317;BA.debugLine="Dim thumbnail As String = colmedia.Get(\"thumbna";
_thumbnail = BA.ObjectToString(_colmedia.Get((Object)("thumbnail")));
 //BA.debugLineNum = 2318;BA.debugLine="Dim created_at As String = colmedia.Get(\"create";
_created_at = BA.ObjectToString(_colmedia.Get((Object)("created_at")));
 //BA.debugLineNum = 2319;BA.debugLine="Dim device_name As String = colmedia.Get(\"devic";
_device_name = BA.ObjectToString(_colmedia.Get((Object)("device_name")));
 //BA.debugLineNum = 2320;BA.debugLine="Dim watched As String = colmedia.Get(\"watched\")";
_watched = BA.ObjectToString(_colmedia.Get((Object)("watched")));
 //BA.debugLineNum = 2321;BA.debugLine="Dim medianame As String = colmedia.Get(\"media\")";
_medianame = BA.ObjectToString(_colmedia.Get((Object)("media")));
 //BA.debugLineNum = 2322;BA.debugLine="Dim VideoID As String = colmedia.Get(\"id\")";
_videoid = BA.ObjectToString(_colmedia.Get((Object)("id")));
 //BA.debugLineNum = 2323;BA.debugLine="Dim mytypes As Object = Starter.kvs.Get(VideoID";
_mytypes = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._get /*Object*/ (_videoid);
 //BA.debugLineNum = 2324;BA.debugLine="Dim videos = mytypes As VideoInfo";
_videos = (cloyd.smart.home.monitor.main._videoinfo)(_mytypes);
 //BA.debugLineNum = 2325;BA.debugLine="If (videos = Null) Or (watched <> videos.Watche";
if (true) break;

case 7:
//if
this.state = 14;
if ((_videos== null) || ((_watched).equals(_videos.Watched /*String*/ ) == false)) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 2326;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 2327;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 2328;BA.debugLine="j.Download(\"https://rest-\" & userRegion &\".imm";
_j._download /*String*/ ("https://rest-"+parent._userregion+".immedia-semi.com"+_thumbnail+".jpg");
 //BA.debugLineNum = 2329;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 2330;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 34;
return;
case 34:
//C
this.state = 10;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 2331;BA.debugLine="If j.Success Then";
if (true) break;

case 10:
//if
this.state = 13;
if (_j._success /*boolean*/ ) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 2332;BA.debugLine="Dim out As OutputStream = File.OpenOutput(Fil";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"screenshot.jpg",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2333;BA.debugLine="File.Copy2(j.GetInputStream, out)";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_j._getinputstream /*anywheresoftware.b4a.objects.streams.File.InputStreamWrapper*/ ().getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 2334;BA.debugLine="out.Close '<------ very important";
_out.Close();
 //BA.debugLineNum = 2335;BA.debugLine="Dim image As B4XBitmap = xui.LoadBitmapResize";
_image = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
_image = parent.mostCurrent._xui.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"screenshot.jpg",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (178)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2336;BA.debugLine="Dim out As OutputStream";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 2337;BA.debugLine="out.InitializeToBytesArray(0)";
_out.InitializeToBytesArray((int) (0));
 //BA.debugLineNum = 2338;BA.debugLine="image.WriteToStream(out, 100, \"JPEG\")";
_image.WriteToStream((java.io.OutputStream)(_out.getObject()),(int) (100),BA.getEnumFromString(android.graphics.Bitmap.CompressFormat.class,"JPEG"));
 //BA.debugLineNum = 2339;BA.debugLine="Starter.kvs.Put(VideoID, CreateCustomType(med";
parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._put /*String*/ (_videoid,(Object)(_createcustomtype(_medianame,_created_at,_watched,_device_name,_videoid,_out.ToBytesArray())));
 //BA.debugLineNum = 2340;BA.debugLine="out.Close";
_out.Close();
 if (true) break;

case 13:
//C
this.state = 14;
;
 //BA.debugLineNum = 2342;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 14:
//C
this.state = 33;
;
 if (true) break;
if (true) break;

case 15:
//C
this.state = 16;
;
 //BA.debugLineNum = 2346;BA.debugLine="Dim bmp2 As Bitmap";
_bmp2 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 2347;BA.debugLine="clvActivity.Clear";
parent.mostCurrent._clvactivity._clear();
 //BA.debugLineNum = 2348;BA.debugLine="Dim list1 As List = Starter.kvs.ListKeys";
_list1 = new anywheresoftware.b4a.objects.collections.List();
_list1 = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._listkeys /*anywheresoftware.b4a.objects.collections.List*/ ();
 //BA.debugLineNum = 2349;BA.debugLine="For i =  0 To list1.Size-1";
if (true) break;

case 16:
//for
this.state = 25;
step40 = 1;
limit40 = (int) (_list1.getSize()-1);
_i = (int) (0) ;
this.state = 35;
if (true) break;

case 35:
//C
this.state = 25;
if ((step40 > 0 && _i <= limit40) || (step40 < 0 && _i >= limit40)) this.state = 18;
if (true) break;

case 36:
//C
this.state = 35;
_i = ((int)(0 + _i + step40)) ;
if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 2353;BA.debugLine="If i > 49 Then";
if (true) break;

case 19:
//if
this.state = 24;
if (_i>49) { 
this.state = 21;
}else {
this.state = 23;
}if (true) break;

case 21:
//C
this.state = 24;
 //BA.debugLineNum = 2354;BA.debugLine="Starter.kvs.Remove(list1.Get(i))";
parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._remove /*String*/ (BA.ObjectToString(_list1.Get(_i)));
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 2356;BA.debugLine="Dim mytypes As Object = Starter.kvs.Get(list1.";
_mytypes = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._get /*Object*/ (BA.ObjectToString(_list1.Get(_i)));
 //BA.debugLineNum = 2357;BA.debugLine="Dim videos = mytypes As VideoInfo";
_videos = (cloyd.smart.home.monitor.main._videoinfo)(_mytypes);
 //BA.debugLineNum = 2358;BA.debugLine="Dim In As InputStream";
_in = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 2359;BA.debugLine="In.InitializeFromBytesArray(videos.ThumbnailBL";
_in.InitializeFromBytesArray(_videos.ThumbnailBLOB /*byte[]*/ ,(int) (0),_videos.ThumbnailBLOB /*byte[]*/ .length);
 //BA.debugLineNum = 2361;BA.debugLine="bmp2.Initialize2(In)";
_bmp2.Initialize2((java.io.InputStream)(_in.getObject()));
 //BA.debugLineNum = 2362;BA.debugLine="Dim cd As CardData";
_cd = new cloyd.smart.home.monitor.main._carddata();
 //BA.debugLineNum = 2363;BA.debugLine="cd.Initialize";
_cd.Initialize();
 //BA.debugLineNum = 2364;BA.debugLine="cd.screenshot = bmp2";
_cd.screenshot /*anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper*/ .setObject((android.graphics.Bitmap)(_bmp2.getObject()));
 //BA.debugLineNum = 2365;BA.debugLine="cd.filedate = videos.DateCreated";
_cd.filedate /*String*/  = _videos.DateCreated /*String*/ ;
 //BA.debugLineNum = 2366;BA.debugLine="cd.deviceinfo = videos.DeviceName";
_cd.deviceinfo /*String*/  = _videos.DeviceName /*String*/ ;
 //BA.debugLineNum = 2367;BA.debugLine="cd.iswatchedvisible = Not(videos.Watched)";
_cd.iswatchedvisible /*boolean*/  = anywheresoftware.b4a.keywords.Common.Not(BA.ObjectToBoolean(_videos.Watched /*String*/ ));
 //BA.debugLineNum = 2368;BA.debugLine="cd.mediaURL = \"https://rest-\" & userRegion &\".";
_cd.mediaURL /*String*/  = "https://rest-"+parent._userregion+".immedia-semi.com"+_videos.ThumbnailPath /*String*/ ;
 //BA.debugLineNum = 2369;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 2370;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, clvActivity.AsVie";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),parent.mostCurrent._clvactivity._asview().getWidth(),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 2371;BA.debugLine="clvActivity.Add(p, cd)";
parent.mostCurrent._clvactivity._add(_p,(Object)(_cd));
 if (true) break;

case 24:
//C
this.state = 36;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 2377;BA.debugLine="If list1.Size > 0 Then";

case 25:
//if
this.state = 28;
if (_list1.getSize()>0) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 2378;BA.debugLine="clvActivity.JumpToItem(0)";
parent.mostCurrent._clvactivity._jumptoitem((int) (0));
 //BA.debugLineNum = 2379;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 37;
return;
case 37:
//C
this.state = 28;
;
 //BA.debugLineNum = 2380;BA.debugLine="clvActivity_ItemClick(0,\"\") '\"https://rest-\" &";
_clvactivity_itemclick((int) (0),(Object)(""));
 //BA.debugLineNum = 2381;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 38;
return;
case 38:
//C
this.state = 28;
;
 if (true) break;

case 28:
//C
this.state = 31;
;
 if (true) break;

case 30:
//C
this.state = 31;
this.catchState = 0;
 //BA.debugLineNum = 2384;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("33997775",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 31:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 2386;BA.debugLine="B4XLoadingIndicator4.hide";
parent.mostCurrent._b4xloadingindicator4._hide /*String*/ ();
 //BA.debugLineNum = 2387;BA.debugLine="Pages = Array(True, True, True, True, True, True)";
parent._pages = anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.True),(Object)(anywheresoftware.b4a.keywords.Common.True),(Object)(anywheresoftware.b4a.keywords.Common.True),(Object)(anywheresoftware.b4a.keywords.Common.True),(Object)(anywheresoftware.b4a.keywords.Common.True),(Object)(anywheresoftware.b4a.keywords.Common.True)});
 //BA.debugLineNum = 2388;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 2389;BA.debugLine="End Sub";
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
>>>>>>> origin/master
public static String  _globals() throws Exception{
 //BA.debugLineNum = 41;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 42;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 44;BA.debugLine="Private gblACMenu As ACMenu";
mostCurrent._gblacmenu = new de.amberhome.objects.appcompat.ACMenuWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 46;BA.debugLine="Private GaugeHumidity As Gauge";
mostCurrent._gaugehumidity = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 47;BA.debugLine="Private GaugeTemp As Gauge";
mostCurrent._gaugetemp = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 48;BA.debugLine="Private GaugeDewPoint As Gauge";
mostCurrent._gaugedewpoint = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 49;BA.debugLine="Private GaugeHeatIndex As Gauge";
mostCurrent._gaugeheatindex = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 50;BA.debugLine="Private lblComfort As Label";
mostCurrent._lblcomfort = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private lblPerception As Label";
mostCurrent._lblperception = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private lblLastUpdate As Label";
mostCurrent._lbllastupdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private lblPing As Label";
mostCurrent._lblping = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private TabStrip1 As TabStrip";
mostCurrent._tabstrip1 = new anywheresoftware.b4a.objects.TabStripViewPager();
 //BA.debugLineNum = 55;BA.debugLine="Private lblFontAwesome As Label";
mostCurrent._lblfontawesome = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private GaugeAirQuality As Gauge";
mostCurrent._gaugeairquality = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 57;BA.debugLine="Private lblAirQuality As Label";
mostCurrent._lblairquality = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private lblAirQualityLastUpdate As Label";
mostCurrent._lblairqualitylastupdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private ScrollView1 As ScrollView";
mostCurrent._scrollview1 = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private GaugeAirQualityBasement As Gauge";
mostCurrent._gaugeairqualitybasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 62;BA.debugLine="Private lblAirQualityBasement As Label";
mostCurrent._lblairqualitybasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private lblAirQualityLastUpdateBasement As Label";
mostCurrent._lblairqualitylastupdatebasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private PanelAirQualityBasement As Panel";
mostCurrent._panelairqualitybasement = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private GaugeDewPointBasement As Gauge";
mostCurrent._gaugedewpointbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 66;BA.debugLine="Private GaugeHeatIndexBasement As Gauge";
mostCurrent._gaugeheatindexbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 67;BA.debugLine="Private GaugeHumidityBasement As Gauge";
mostCurrent._gaugehumiditybasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 68;BA.debugLine="Private GaugeTempBasement As Gauge";
mostCurrent._gaugetempbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 69;BA.debugLine="Private lblComfortBasement As Label";
mostCurrent._lblcomfortbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Private lblLastUpdateBasement As Label";
mostCurrent._lbllastupdatebasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Private lblPerceptionBasement As Label";
mostCurrent._lblperceptionbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Private lblPingBasement As Label";
mostCurrent._lblpingbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Private ScrollViewBasement As ScrollView";
mostCurrent._scrollviewbasement = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Private PanelTempHumidityBasement As Panel";
mostCurrent._paneltemphumiditybasement = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Private B4XPageIndicator1 As B4XPageIndicator";
mostCurrent._b4xpageindicator1 = new cloyd.smart.home.monitor.b4xpageindicator();
 //BA.debugLineNum = 76;BA.debugLine="Private badger1 As Badger";
mostCurrent._badger1 = new cloyd.smart.home.monitor.badger();
 //BA.debugLineNum = 77;BA.debugLine="Private nativeMe As JavaObject";
mostCurrent._nativeme = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 78;BA.debugLine="Private dialog As B4XDialog";
mostCurrent._dialog = new cloyd.smart.home.monitor.b4xdialog();
 //BA.debugLineNum = 79;BA.debugLine="Private btnChart As SwiftButton";
mostCurrent._btnchart = new cloyd.smart.home.monitor.swiftbutton();
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return "";
}
public static String  _handlesettings() throws Exception{
<<<<<<< HEAD
 //BA.debugLineNum = 964;BA.debugLine="Sub HandleSettings";
 //BA.debugLineNum = 965;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",manage";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue",_manager.GetString("HumidityAddValue"));
 //BA.debugLineNum = 966;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime",_manager.GetString("SensorNotRespondingTime"));
 //BA.debugLineNum = 967;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 968;BA.debugLine="End Sub";
=======
 //BA.debugLineNum = 1218;BA.debugLine="Sub HandleSettings";
 //BA.debugLineNum = 1219;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",manage";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue",_manager.GetString("HumidityAddValue"));
 //BA.debugLineNum = 1220;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime",_manager.GetString("SensorNotRespondingTime"));
 //BA.debugLineNum = 1221;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 1222;BA.debugLine="End Sub";
>>>>>>> origin/master
return "";
}
public static String  _hideping() throws Exception{
 //BA.debugLineNum = 592;BA.debugLine="Private Sub HidePing";
 //BA.debugLineNum = 593;BA.debugLine="lblPing.SetVisibleAnimated(200, False)";
mostCurrent._lblping.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 594;BA.debugLine="End Sub";
return "";
}
public static String  _hidepingbasement() throws Exception{
 //BA.debugLineNum = 596;BA.debugLine="Private Sub HidePingBasement";
 //BA.debugLineNum = 597;BA.debugLine="lblPingBasement.SetVisibleAnimated(200, False)";
mostCurrent._lblpingbasement.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 598;BA.debugLine="End Sub";
return "";
}
<<<<<<< HEAD
=======
public static void  _ivdriveway_click() throws Exception{
ResumableSub_ivDriveway_Click rsub = new ResumableSub_ivDriveway_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ivDriveway_Click extends BA.ResumableSub {
public ResumableSub_ivDriveway_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2292;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-\"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/347574");
 //BA.debugLineNum = 2293;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2295;BA.debugLine="Dim rs As ResumableSub = GetCameraInfo(response,F";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcamerainfo(parent._response,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2296;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 6;
return;
case 6:
//C
this.state = 1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2298;BA.debugLine="Dim rs As ResumableSub = DownloadImageFullscreen(";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _downloadimagefullscreen("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg","347574");
 //BA.debugLineNum = 2299;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2300;BA.debugLine="If response.StartsWith(\"ERROR: \") = False Then";
if (true) break;

case 1:
//if
this.state = 4;
if (parent._response.startsWith("ERROR: ")==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2301;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 2303;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _ivfrontdoor_click() throws Exception{
ResumableSub_ivFrontDoor_Click rsub = new ResumableSub_ivFrontDoor_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ivFrontDoor_Click extends BA.ResumableSub {
public ResumableSub_ivFrontDoor_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2278;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-\"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/236967");
 //BA.debugLineNum = 2279;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2281;BA.debugLine="Dim rs As ResumableSub = GetCameraInfo(response,F";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcamerainfo(parent._response,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2282;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 6;
return;
case 6:
//C
this.state = 1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2284;BA.debugLine="Dim rs As ResumableSub = DownloadImageFullscreen(";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _downloadimagefullscreen("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg","236967");
 //BA.debugLineNum = 2285;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2286;BA.debugLine="If response.StartsWith(\"ERROR: \") = False Then";
if (true) break;

case 1:
//if
this.state = 4;
if (parent._response.startsWith("ERROR: ")==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2287;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 2289;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _ivplay_click() throws Exception{
ResumableSub_ivPlay_Click rsub = new ResumableSub_ivPlay_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ivPlay_Click extends BA.ResumableSub {
public ResumableSub_ivPlay_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.phone.Phone _p = null;
cloyd.smart.home.monitor.main._carddata _cd = null;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
String _filename = "";
anywheresoftware.b4a.objects.IntentWrapper _in = null;

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
 //BA.debugLineNum = 3158;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 14;
this.catchState = 13;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 13;
 //BA.debugLineNum = 3159;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 3160;BA.debugLine="p.SetMute(3,True)";
_p.SetMute((int) (3),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 3162;BA.debugLine="B4XLoadingIndicator4.Show";
parent.mostCurrent._b4xloadingindicator4._show /*String*/ ();
 //BA.debugLineNum = 3163;BA.debugLine="wvMedia.LoadUrl(\"\")";
parent.mostCurrent._wvmedia.LoadUrl("");
 //BA.debugLineNum = 3165;BA.debugLine="If previousSelectedIndex > (clvActivity.Size-1)";
if (true) break;

case 4:
//if
this.state = 7;
if (parent._previousselectedindex>(parent.mostCurrent._clvactivity._getsize()-1)) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 3166;BA.debugLine="previousSelectedIndex = 0";
parent._previousselectedindex = (int) (0);
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 3169;BA.debugLine="Dim cd As CardData = clvActivity.GetValue(previo";
_cd = (cloyd.smart.home.monitor.main._carddata)(parent.mostCurrent._clvactivity._getvalue(parent._previousselectedindex));
 //BA.debugLineNum = 3170;BA.debugLine="Dim rs As ResumableSub = ShowVideo(cd.mediaURL)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _showvideo(_cd.mediaURL /*String*/ );
 //BA.debugLineNum = 3171;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 15;
return;
case 15:
//C
this.state = 8;
_result = (Object) result[0];
;
 //BA.debugLineNum = 3173;BA.debugLine="Dim FileName As String = \"media.mp4\"";
_filename = "media.mp4";
 //BA.debugLineNum = 3174;BA.debugLine="If File.Exists(File.DirInternal, FileName) Then";
if (true) break;

case 8:
//if
this.state = 11;
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename)) { 
this.state = 10;
}if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 3175;BA.debugLine="File.Copy(File.DirInternal, FileName, Starter.P";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename,parent.mostCurrent._starter._provider /*cloyd.smart.home.monitor.fileprovider*/ ._sharedfolder /*String*/ ,_filename);
 //BA.debugLineNum = 3176;BA.debugLine="Dim in As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 3177;BA.debugLine="in.Initialize(in.ACTION_VIEW, \"\")";
_in.Initialize(_in.ACTION_VIEW,"");
 //BA.debugLineNum = 3178;BA.debugLine="Starter.Provider.SetFileUriAsIntentData(in, Fil";
parent.mostCurrent._starter._provider /*cloyd.smart.home.monitor.fileprovider*/ ._setfileuriasintentdata /*String*/ (_in,_filename);
 //BA.debugLineNum = 3180;BA.debugLine="in.SetType(\"video/*\")";
_in.SetType("video/*");
 //BA.debugLineNum = 3181;BA.debugLine="StartActivity(in)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_in.getObject()));
 if (true) break;

case 11:
//C
this.state = 14;
;
 if (true) break;

case 13:
//C
this.state = 14;
this.catchState = 0;
 //BA.debugLineNum = 3184;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("35111835",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 14:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 3186;BA.debugLine="B4XLoadingIndicator4.Hide";
parent.mostCurrent._b4xloadingindicator4._hide /*String*/ ();
 //BA.debugLineNum = 3187;BA.debugLine="End Sub";
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
public static void  _ivsideyard_click() throws Exception{
ResumableSub_ivSideYard_Click rsub = new ResumableSub_ivSideYard_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ivSideYard_Click extends BA.ResumableSub {
public ResumableSub_ivSideYard_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2263;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-\"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/458236");
 //BA.debugLineNum = 2264;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2266;BA.debugLine="Dim rs As ResumableSub = GetCameraInfo(response,F";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcamerainfo(parent._response,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2267;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 6;
return;
case 6:
//C
this.state = 1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2270;BA.debugLine="Dim rs As ResumableSub = DownloadImageFullscreen(";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _downloadimagefullscreen("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg","458236");
 //BA.debugLineNum = 2271;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2272;BA.debugLine="If response.StartsWith(\"ERROR: \") = False Then";
if (true) break;

case 1:
//if
this.state = 4;
if (parent._response.startsWith("ERROR: ")==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2273;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 2275;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
>>>>>>> origin/master
public static String  _mqtt_connect() throws Exception{
String _clientid = "";
 //BA.debugLineNum = 324;BA.debugLine="Sub MQTT_Connect";
 //BA.debugLineNum = 325;BA.debugLine="Try";
try { //BA.debugLineNum = 326;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'crea";
_clientid = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (999999999)));
 //BA.debugLineNum = 327;BA.debugLine="MQTT.Initialize(\"MQTT\", MQTTServerURI, ClientId)";
_mqtt.Initialize(processBA,"MQTT",_mqttserveruri,_clientid);
 //BA.debugLineNum = 331;BA.debugLine="MQTT.Connect";
_mqtt.Connect();
 } 
<<<<<<< HEAD
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 333;BA.debugLine="Log(\"MQTT_Connect: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("6720905","MQTT_Connect: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
=======
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 457;BA.debugLine="Log(\"MQTT_Connect: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("3720905","MQTT_Connect: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
>>>>>>> origin/master
 };
 //BA.debugLineNum = 335;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 337;BA.debugLine="Sub MQTT_Connected (Success As Boolean)";
 //BA.debugLineNum = 338;BA.debugLine="Try";
try { //BA.debugLineNum = 339;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
<<<<<<< HEAD
 //BA.debugLineNum = 340;BA.debugLine="Log(\"MQTT_NotConnected: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("6786435","MQTT_NotConnected: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 341;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 }else {
 //BA.debugLineNum = 343;BA.debugLine="Log(\"Connected to MQTT broker\")";
anywheresoftware.b4a.keywords.Common.LogImpl("6786438","Connected to MQTT broker",0);
 //BA.debugLineNum = 344;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
=======
 //BA.debugLineNum = 464;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("3786435",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 465;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 }else {
 //BA.debugLineNum = 467;BA.debugLine="Log(\"Connected to MQTT broker\")";
anywheresoftware.b4a.keywords.Common.LogImpl("3786438","Connected to MQTT broker",0);
 //BA.debugLineNum = 468;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
>>>>>>> origin/master
_mqtt.Subscribe("TempHumid",(int) (0));
 //BA.debugLineNum = 345;BA.debugLine="MQTT.Subscribe(\"MQ7LivingRoomCloyd\", 0)";
_mqtt.Subscribe("MQ7LivingRoomCloyd",(int) (0));
 //BA.debugLineNum = 346;BA.debugLine="MQTT.Subscribe(\"MQ7Basement\", 0)";
_mqtt.Subscribe("MQ7Basement",(int) (0));
 //BA.debugLineNum = 347;BA.debugLine="MQTT.Subscribe(\"TempHumidBasement\", 0)";
_mqtt.Subscribe("TempHumidBasement",(int) (0));
 //BA.debugLineNum = 348;BA.debugLine="MQTT.Subscribe(\"HumidityAddValue\", 0)";
_mqtt.Subscribe("HumidityAddValue",(int) (0));
 };
 } 
       catch (Exception e14) {
<<<<<<< HEAD
			processBA.setLastException(e14); //BA.debugLineNum = 351;BA.debugLine="Log(\"MQTT_Connected: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("6786446","MQTT_Connected: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
=======
			processBA.setLastException(e14); //BA.debugLineNum = 475;BA.debugLine="Log(\"MQTT_Connected: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("3786446","MQTT_Connected: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
>>>>>>> origin/master
 };
 //BA.debugLineNum = 353;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_disconnected() throws Exception{
 //BA.debugLineNum = 355;BA.debugLine="Private Sub MQTT_Disconnected";
 //BA.debugLineNum = 356;BA.debugLine="Try";
try { //BA.debugLineNum = 357;BA.debugLine="gblACMenu.Clear";
mostCurrent._gblacmenu.Clear();
 //BA.debugLineNum = 358;BA.debugLine="gblACMenu.Add(0, 0, \"Settings\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Settings"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 359;BA.debugLine="gblACMenu.Add(0, 0, \"Restart application\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Restart application"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 360;BA.debugLine="gblACMenu.Add(0, 0, \"Show free memory\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Show free memory"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 361;BA.debugLine="gblACMenu.Add(0, 0, \"Show chart\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Show chart"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 362;BA.debugLine="gblACMenu.Add(0, 0, \"About\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
<<<<<<< HEAD
 //BA.debugLineNum = 363;BA.debugLine="Log(\"Disconnected from MQTT broker\")";
anywheresoftware.b4a.keywords.Common.LogImpl("6851976","Disconnected from MQTT broker",0);
 //BA.debugLineNum = 364;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e11) {
			processBA.setLastException(e11); //BA.debugLineNum = 366;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("6851979",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
=======
 //BA.debugLineNum = 487;BA.debugLine="Log(\"Disconnected from MQTT broker\")";
anywheresoftware.b4a.keywords.Common.LogImpl("3851976","Disconnected from MQTT broker",0);
 //BA.debugLineNum = 488;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e11) {
			processBA.setLastException(e11); //BA.debugLineNum = 490;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("3851979",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
>>>>>>> origin/master
 };
 //BA.debugLineNum = 368;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
 //BA.debugLineNum = 370;BA.debugLine="Private Sub MQTT_MessageArrived (Topic As String,";
 //BA.debugLineNum = 371;BA.debugLine="Try";
try { //BA.debugLineNum = 372;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic).equals("TempHumid")) { 
 //BA.debugLineNum = 373;BA.debugLine="lblPing.SetVisibleAnimated(500, True)";
mostCurrent._lblping.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 374;BA.debugLine="csu.CallSubPlus(Me, \"HidePing\", 700)";
_csu._v7(main.getObject(),"HidePing",(int) (700));
 //BA.debugLineNum = 375;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 }else if((_topic).equals("MQ7LivingRoomCloyd")) { 
 //BA.debugLineNum = 377;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 }else if((_topic).equals("MQ7Basement")) { 
 //BA.debugLineNum = 379;BA.debugLine="CheckAirQualitySettingBasement";
_checkairqualitysettingbasement();
 }else if((_topic).equals("TempHumidBasement")) { 
 //BA.debugLineNum = 381;BA.debugLine="lblPingBasement.SetVisibleAnimated(500, True)";
mostCurrent._lblpingbasement.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 382;BA.debugLine="csu.CallSubPlus(Me, \"HidePingBasement\", 700)";
_csu._v7(main.getObject(),"HidePingBasement",(int) (700));
 //BA.debugLineNum = 384;BA.debugLine="CheckTempHumiditySettingBasement";
_checktemphumiditysettingbasement();
 };
 } 
       catch (Exception e16) {
<<<<<<< HEAD
			processBA.setLastException(e16); //BA.debugLineNum = 387;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("6917521",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
=======
			processBA.setLastException(e16); //BA.debugLineNum = 511;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("3917521",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
>>>>>>> origin/master
 };
 //BA.debugLineNum = 389;BA.debugLine="End Sub";
return "";
}
<<<<<<< HEAD
=======
public static long  _parseutcstring(String _utc) throws Exception{
String _df = "";
long _res = 0L;
 //BA.debugLineNum = 2129;BA.debugLine="Sub ParseUTCstring(utc As String) As Long";
 //BA.debugLineNum = 2130;BA.debugLine="Dim df As String = DateTime.DateFormat";
_df = anywheresoftware.b4a.keywords.Common.DateTime.getDateFormat();
 //BA.debugLineNum = 2131;BA.debugLine="Dim res As Long";
_res = 0L;
 //BA.debugLineNum = 2132;BA.debugLine="If utc.CharAt(10) = \"T\" Then";
if (_utc.charAt((int) (10))==BA.ObjectToChar("T")) { 
 //BA.debugLineNum = 2134;BA.debugLine="If utc.CharAt(19) = \".\" Then utc = utc.SubString";
if (_utc.charAt((int) (19))==BA.ObjectToChar(".")) { 
_utc = _utc.substring((int) (0),(int) (19))+"+0000";};
 //BA.debugLineNum = 2135;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd'T'HH:mm:ssZ\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
 }else {
 //BA.debugLineNum = 2138;BA.debugLine="DateTime.DateFormat = \"EEE MMM dd HH:mm:ss Z yyy";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
 };
 //BA.debugLineNum = 2140;BA.debugLine="Try";
try { //BA.debugLineNum = 2141;BA.debugLine="res = DateTime.DateParse(utc)";
_res = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_utc);
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 2143;BA.debugLine="res = 0";
_res = (long) (0);
 };
 //BA.debugLineNum = 2145;BA.debugLine="DateTime.DateFormat = df";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat(_df);
 //BA.debugLineNum = 2146;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 2147;BA.debugLine="End Sub";
return 0L;
}
>>>>>>> origin/master

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        b4a.example.dateutils._process_globals();
main._process_globals();
chart._process_globals();
smarthomemonitor._process_globals();
notificationservice._process_globals();
statemanager._process_globals();
starter._process_globals();
b4xcollections._process_globals();
httputils2service._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private MQTT As MqttClient";
_mqtt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private MQTTServerURI As String = \"tcp://broker.h";
_mqttserveruri = "tcp://broker.hivemq.com:1883";
 //BA.debugLineNum = 29;BA.debugLine="Private bc As ByteConverter";
_bc = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 30;BA.debugLine="Private csu As CallSubUtils";
_csu = new b4a.example.callsubutils();
 //BA.debugLineNum = 31;BA.debugLine="Private OldIntent As Intent";
_oldintent = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Dim manager As AHPreferenceManager";
_manager = new de.amberhome.objects.preferenceactivity.PreferenceManager();
 //BA.debugLineNum = 33;BA.debugLine="Dim screen As AHPreferenceScreen";
_screen = new de.amberhome.objects.preferenceactivity.PreferenceScreenWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Type CardData (screenshot As B4XBitmap,filedate A";
;
 //BA.debugLineNum = 35;BA.debugLine="Type VideoInfo (ThumbnailPath As String, DateCrea";
;
 //BA.debugLineNum = 36;BA.debugLine="Public isThereUnwatchedVideo As Boolean";
_isthereunwatchedvideo = false;
 //BA.debugLineNum = 37;BA.debugLine="Private Awake As PhoneWakeState";
_awake = new anywheresoftware.b4a.phone.Phone.PhoneWakeState();
<<<<<<< HEAD
 //BA.debugLineNum = 38;BA.debugLine="Private compileTimeStamp As String";
_compiletimestamp = "";
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _setdefaults() throws Exception{
 //BA.debugLineNum = 955;BA.debugLine="Sub SetDefaults";
 //BA.debugLineNum = 957;BA.debugLine="manager.SetString(\"HumidityAddValue\", \"6\")";
_manager.SetString("HumidityAddValue","6");
 //BA.debugLineNum = 958;BA.debugLine="manager.SetString(\"SensorNotRespondingTime\", \"10\"";
_manager.SetString("SensorNotRespondingTime","10");
 //BA.debugLineNum = 959;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",\"6\")";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue","6");
 //BA.debugLineNum = 960;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime","10");
 //BA.debugLineNum = 961;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 962;BA.debugLine="End Sub";
=======
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _refreshcameras(boolean _firstrun,String _whatcamera) throws Exception{
ResumableSub_RefreshCameras rsub = new ResumableSub_RefreshCameras(null,_firstrun,_whatcamera);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_RefreshCameras extends BA.ResumableSub {
public ResumableSub_RefreshCameras(cloyd.smart.home.monitor.main parent,boolean _firstrun,String _whatcamera) {
this.parent = parent;
this._firstrun = _firstrun;
this._whatcamera = _whatcamera;
}
cloyd.smart.home.monitor.main parent;
boolean _firstrun;
String _whatcamera;
String _camera = "";
anywheresoftware.b4a.objects.ImageViewWrapper _iv = null;
anywheresoftware.b4a.objects.collections.List _links = null;
int _attempts = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
String _link = "";
boolean _rechecked = false;
int _i = 0;
anywheresoftware.b4a.BA.IterableList group51;
int index51;
int groupLen51;
int step124;
int limit124;

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
 //BA.debugLineNum = 1271;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 95;
this.catchState = 94;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 94;
 //BA.debugLineNum = 1272;BA.debugLine="Dim camera As String";
_camera = "";
 //BA.debugLineNum = 1273;BA.debugLine="Dim iv As ImageView";
_iv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 1274;BA.debugLine="Dim links As List";
_links = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1275;BA.debugLine="Dim attempts As Int";
_attempts = 0;
 //BA.debugLineNum = 1276;BA.debugLine="links = Array(\"347574\", \"236967\", \"458236\")";
_links = anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)("347574"),(Object)("236967"),(Object)("458236")});
 //BA.debugLineNum = 1278;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-\"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/syncmodules");
 //BA.debugLineNum = 1279;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 104;
return;
case 104:
//C
this.state = 4;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1281;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.Co";
if (true) break;

case 4:
//if
this.state = 7;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1282;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1283;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1284;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1285;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1286;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1287;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1288;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1289;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1290;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 1293;BA.debugLine="Dim rs As ResumableSub = GetSyncModuleInfo(respo";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getsyncmoduleinfo(parent._response);
 //BA.debugLineNum = 1294;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 105;
return;
case 105:
//C
this.state = 8;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1296;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest-\"";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/homescreen");
 //BA.debugLineNum = 1297;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 106;
return;
case 106:
//C
this.state = 8;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1298;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.Co";
if (true) break;

case 8:
//if
this.state = 11;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 10;
}if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1299;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1300;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1301;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1302;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1303;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1304;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1305;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1306;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1307;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 11:
//C
this.state = 12;
;
 //BA.debugLineNum = 1310;BA.debugLine="Dim rs As ResumableSub = GetHomescreen(response)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _gethomescreen(parent._response);
 //BA.debugLineNum = 1311;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 107;
return;
case 107:
//C
this.state = 12;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1314;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 1315;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 108;
return;
case 108:
//C
this.state = 12;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1317;BA.debugLine="lblDrivewayBatt.Visible = True";
parent.mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1318;BA.debugLine="lblDrivewayTimestamp.Visible = True";
parent.mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1319;BA.debugLine="lblDrivewayWifi.Visible = True";
parent.mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1320;BA.debugLine="lblDriveway.Visible = True";
parent.mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1321;BA.debugLine="lblFrontDoorBatt.Visible = True";
parent.mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1322;BA.debugLine="lblFrontDoorTimestamp.Visible = True";
parent.mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1323;BA.debugLine="lblFrontDoorWiFi.Visible = True";
parent.mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1324;BA.debugLine="lblFrontDoor.Visible = True";
parent.mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1325;BA.debugLine="lblSideYardBatt.Visible = True";
parent.mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1326;BA.debugLine="lblSideYardTimestamp.Visible = True";
parent.mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1327;BA.debugLine="lblSideYardWiFi.Visible = True";
parent.mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1328;BA.debugLine="lblSideYard.Visible = True";
parent.mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1330;BA.debugLine="For Each link As String In links";
if (true) break;

case 12:
//for
this.state = 92;
group51 = _links;
index51 = 0;
groupLen51 = group51.getSize();
this.state = 109;
if (true) break;

case 109:
//C
this.state = 92;
if (index51 < groupLen51) {
this.state = 14;
_link = BA.ObjectToString(group51.Get(index51));}
if (true) break;

case 110:
//C
this.state = 109;
index51++;
if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 1331;BA.debugLine="camera = link";
_camera = _link;
 //BA.debugLineNum = 1332;BA.debugLine="If camera <> whatCamera And whatCamera <> \"All\"";
if (true) break;

case 15:
//if
this.state = 18;
if ((_camera).equals(_whatcamera) == false && (_whatcamera).equals("All") == false) { 
this.state = 17;
}if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 1333;BA.debugLine="Continue";
this.state = 110;
if (true) break;;
 if (true) break;
;
 //BA.debugLineNum = 1335;BA.debugLine="If FirstRun Then";

case 18:
//if
this.state = 39;
if (_firstrun) { 
this.state = 20;
}else {
this.state = 30;
}if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 1336;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 21:
//if
this.state = 28;
if ((_camera).equals("347574")) { 
this.state = 23;
}else if((_camera).equals("236967")) { 
this.state = 25;
}else if((_camera).equals("458236")) { 
this.state = 27;
}if (true) break;

case 23:
//C
this.state = 28;
 //BA.debugLineNum = 1337;BA.debugLine="lblStatus.Text = \"Retrieving Driveway thumbna";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Driveway thumbnail..."));
 //BA.debugLineNum = 1338;BA.debugLine="iv = ivDriveway";
_iv = parent.mostCurrent._ivdriveway;
 if (true) break;

case 25:
//C
this.state = 28;
 //BA.debugLineNum = 1340;BA.debugLine="lblStatus.Text = \"Retrieving Front Door thumb";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Front Door thumbnail..."));
 //BA.debugLineNum = 1341;BA.debugLine="iv = ivFrontDoor";
_iv = parent.mostCurrent._ivfrontdoor;
 if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 1343;BA.debugLine="lblStatus.Text = \"Retrieving Side Yard thumbn";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Side Yard thumbnail..."));
 //BA.debugLineNum = 1344;BA.debugLine="iv = ivSideYard";
_iv = parent.mostCurrent._ivsideyard;
 if (true) break;

case 28:
//C
this.state = 39;
;
 if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 1347;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 31:
//if
this.state = 38;
if ((_camera).equals("347574")) { 
this.state = 33;
}else if((_camera).equals("236967")) { 
this.state = 35;
}else if((_camera).equals("458236")) { 
this.state = 37;
}if (true) break;

case 33:
//C
this.state = 38;
 //BA.debugLineNum = 1348;BA.debugLine="lblStatus.Text = \"Capturing a new Driveway th";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Driveway thumbnail..."));
 //BA.debugLineNum = 1349;BA.debugLine="iv = ivDriveway";
_iv = parent.mostCurrent._ivdriveway;
 //BA.debugLineNum = 1350;BA.debugLine="attempts = 15";
_attempts = (int) (15);
 if (true) break;

case 35:
//C
this.state = 38;
 //BA.debugLineNum = 1352;BA.debugLine="lblStatus.Text = \"Capturing a new Front Door";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Front Door thumbnail..."));
 //BA.debugLineNum = 1353;BA.debugLine="iv = ivFrontDoor";
_iv = parent.mostCurrent._ivfrontdoor;
 //BA.debugLineNum = 1354;BA.debugLine="attempts = 15";
_attempts = (int) (15);
 if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 1356;BA.debugLine="lblStatus.Text = \"Capturing a new Side Yard t";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Side Yard thumbnail..."));
 //BA.debugLineNum = 1357;BA.debugLine="iv = ivSideYard";
_iv = parent.mostCurrent._ivsideyard;
 //BA.debugLineNum = 1358;BA.debugLine="attempts = 15";
_attempts = (int) (15);
 if (true) break;

case 38:
//C
this.state = 39;
;
 if (true) break;
;
 //BA.debugLineNum = 1362;BA.debugLine="If FirstRun Then";

case 39:
//if
this.state = 91;
if (_firstrun) { 
this.state = 41;
}else {
this.state = 43;
}if (true) break;

case 41:
//C
this.state = 91;
 //BA.debugLineNum = 1363;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera);
 //BA.debugLineNum = 1364;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 111;
return;
case 111:
//C
this.state = 91;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1366;BA.debugLine="Dim rs As ResumableSub = GetCameraInfo(respons";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcamerainfo(parent._response,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1367;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 112;
return;
case 112:
//C
this.state = 91;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1369;BA.debugLine="Dim rs As ResumableSub = DownloadImage(\"https:";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _downloadimage("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg",_iv,_camera);
 //BA.debugLineNum = 1370;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 113;
return;
case 113:
//C
this.state = 91;
_result = (Object) result[0];
;
 if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 1372;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://rest";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera);
 //BA.debugLineNum = 1373;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 114;
return;
case 114:
//C
this.state = 44;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1375;BA.debugLine="Dim rs As ResumableSub = GetCameraInfo(respons";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcamerainfo(parent._response,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1376;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 115;
return;
case 115:
//C
this.state = 44;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1378;BA.debugLine="prevCameraThumbnail = cameraThumbnail";
parent._prevcamerathumbnail = parent._camerathumbnail;
 //BA.debugLineNum = 1380;BA.debugLine="Dim rs As ResumableSub = RESTPost(\"https://res";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restpost("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera+"/thumbnail");
 //BA.debugLineNum = 1381;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 116;
return;
case 116:
//C
this.state = 44;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1382;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.";
if (true) break;

case 44:
//if
this.state = 90;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 46;
}else {
this.state = 48;
}if (true) break;

case 46:
//C
this.state = 90;
 //BA.debugLineNum = 1383;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1384;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1385;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1386;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1387;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1388;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1389;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1390;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1391;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 48:
//C
this.state = 49;
 //BA.debugLineNum = 1393;BA.debugLine="Dim rs As ResumableSub = GetCommandID(respons";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcommandid(parent._response);
 //BA.debugLineNum = 1394;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 117;
return;
case 117:
//C
this.state = 49;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1396;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://res";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 1397;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 118;
return;
case 118:
//C
this.state = 49;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1399;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response";
if (true) break;

case 49:
//if
this.state = 89;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 51;
}else {
this.state = 53;
}if (true) break;

case 51:
//C
this.state = 89;
 //BA.debugLineNum = 1400;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1401;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1402;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1403;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1404;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1405;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1406;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1407;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1408;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 53:
//C
this.state = 54;
 //BA.debugLineNum = 1410;BA.debugLine="Dim reChecked As Boolean";
_rechecked = false;
 //BA.debugLineNum = 1411;BA.debugLine="For i = 1 To attempts";
if (true) break;

case 54:
//for
this.state = 73;
step124 = 1;
limit124 = _attempts;
_i = (int) (1) ;
this.state = 119;
if (true) break;

case 119:
//C
this.state = 73;
if ((step124 > 0 && _i <= limit124) || (step124 < 0 && _i >= limit124)) this.state = 56;
if (true) break;

case 120:
//C
this.state = 119;
_i = ((int)(0 + _i + step124)) ;
if (true) break;

case 56:
//C
this.state = 57;
 //BA.debugLineNum = 1413;BA.debugLine="Dim rs As ResumableSub = GetCommandResponse";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcommandresponse(_iv,_camera,BA.NumberToString(_i),BA.NumberToString(_attempts));
 //BA.debugLineNum = 1414;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 121;
return;
case 121:
//C
this.state = 57;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1415;BA.debugLine="If Result Then Exit";
if (true) break;

case 57:
//if
this.state = 62;
if (BA.ObjectToBoolean(_result)) { 
this.state = 59;
;}if (true) break;

case 59:
//C
this.state = 62;
this.state = 73;
if (true) break;
if (true) break;

case 62:
//C
this.state = 63;
;
 //BA.debugLineNum = 1417;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://r";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 1418;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 122;
return;
case 122:
//C
this.state = 63;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1419;BA.debugLine="Sleep(1000) ' 1 second";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 123;
return;
case 123:
//C
this.state = 63;
;
 //BA.debugLineNum = 1421;BA.debugLine="If i = attempts And reChecked = False Then";
if (true) break;

case 63:
//if
this.state = 72;
if (_i==_attempts && _rechecked==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 65;
}if (true) break;

case 65:
//C
this.state = 66;
 //BA.debugLineNum = 1422;BA.debugLine="reChecked = True";
_rechecked = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 1423;BA.debugLine="Log(\"*********** HERE *********** i = \" &";
anywheresoftware.b4a.keywords.Common.LogImpl("32490521","*********** HERE *********** i = "+BA.NumberToString(_i),0);
 //BA.debugLineNum = 1424;BA.debugLine="Dim rs As ResumableSub = RESTPost(\"https:/";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restpost("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera+"/thumbnail");
 //BA.debugLineNum = 1425;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 124;
return;
case 124:
//C
this.state = 66;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1427;BA.debugLine="If response.StartsWith(\"ERROR: \") Or respo";
if (true) break;

case 66:
//if
this.state = 71;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 68;
}else {
this.state = 70;
}if (true) break;

case 68:
//C
this.state = 71;
 //BA.debugLineNum = 1428;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1429;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1430;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1431;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1432;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1433;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1434;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1435;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1436;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 70:
//C
this.state = 71;
 //BA.debugLineNum = 1438;BA.debugLine="i = 1";
_i = (int) (1);
 if (true) break;

case 71:
//C
this.state = 72;
;
 //BA.debugLineNum = 1440;BA.debugLine="Log(\"*********** HERE TWO ***********\")";
anywheresoftware.b4a.keywords.Common.LogImpl("32490538","*********** HERE TWO ***********",0);
 //BA.debugLineNum = 1441;BA.debugLine="Dim rs As ResumableSub = GetCommandID(resp";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getcommandid(parent._response);
 //BA.debugLineNum = 1442;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 125;
return;
case 125:
//C
this.state = 72;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1444;BA.debugLine="Dim rs As ResumableSub = RESTGet(\"https://";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 1445;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 126;
return;
case 126:
//C
this.state = 72;
_result = (Object) result[0];
;
 if (true) break;

case 72:
//C
this.state = 120;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1448;BA.debugLine="If response.StartsWith(\"ERROR: \") Or respons";

case 73:
//if
this.state = 88;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 75;
}else if(parent._commandcomplete==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 77;
}else {
this.state = 87;
}if (true) break;

case 75:
//C
this.state = 88;
 //BA.debugLineNum = 1449;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1450;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1451;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1452;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1453;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1454;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1455;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1456;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1457;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 77:
//C
this.state = 78;
 //BA.debugLineNum = 1459;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 78:
//if
this.state = 85;
if ((_camera).equals("347574")) { 
this.state = 80;
}else if((_camera).equals("236967")) { 
this.state = 82;
}else if((_camera).equals("458236")) { 
this.state = 84;
}if (true) break;

case 80:
//C
this.state = 85;
 //BA.debugLineNum = 1460;BA.debugLine="lblStatus.Text = \"Failed to retrieve Drive";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Driveway thumbnail..."));
 if (true) break;

case 82:
//C
this.state = 85;
 //BA.debugLineNum = 1462;BA.debugLine="lblStatus.Text = \"Failed to retrieve Front";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Front Door thumbnail..."));
 if (true) break;

case 84:
//C
this.state = 85;
 //BA.debugLineNum = 1464;BA.debugLine="lblStatus.Text = \"Failed to retrieve Side";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Side Yard thumbnail..."));
 if (true) break;

case 85:
//C
this.state = 88;
;
 if (true) break;

case 87:
//C
this.state = 88;
 if (true) break;

case 88:
//C
this.state = 89;
;
 if (true) break;

case 89:
//C
this.state = 90;
;
 if (true) break;

case 90:
//C
this.state = 91;
;
 if (true) break;

case 91:
//C
this.state = 110;
;
 if (true) break;
if (true) break;

case 92:
//C
this.state = 95;
;
 //BA.debugLineNum = 1474;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 94:
//C
this.state = 95;
this.catchState = 0;
 //BA.debugLineNum = 1476;BA.debugLine="Log(\"RefreshCamera LastException: \" & LastExcept";
anywheresoftware.b4a.keywords.Common.LogImpl("32490574","RefreshCamera LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 95:
//C
this.state = 96;
this.catchState = 0;
;
 //BA.debugLineNum = 1478;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1479;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1480;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1481;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1482;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1483;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1484;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1485;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1486;BA.debugLine="If lblSideYardBatt.Text.Contains(\"OFFLINE\") Then";
if (true) break;

case 96:
//if
this.state = 103;
if (parent.mostCurrent._lblsideyardbatt.getText().contains("OFFLINE")) { 
this.state = 98;
}else if(parent.mostCurrent._lbldrivewaybatt.getText().contains("OFFLINE")) { 
this.state = 100;
}else if(parent.mostCurrent._lblfrontdoorbatt.getText().contains("OFFLINE")) { 
this.state = 102;
}if (true) break;

case 98:
//C
this.state = 103;
 //BA.debugLineNum = 1488;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",parent.mostCurrent._ivsideyard);
 if (true) break;

case 100:
//C
this.state = 103;
 //BA.debugLineNum = 1491;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",parent.mostCurrent._ivdriveway);
 if (true) break;

case 102:
//C
this.state = 103;
 //BA.debugLineNum = 1494;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",parent.mostCurrent._ivfrontdoor);
 if (true) break;

case 103:
//C
this.state = -1;
;
 //BA.debugLineNum = 1496;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 1497;BA.debugLine="End Sub";
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
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _requestauthtoken() throws Exception{
ResumableSub_RequestAuthToken rsub = new ResumableSub_RequestAuthToken(null);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
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
{
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,null);return;}
case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1231;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 18;
this.catchState = 17;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 17;
 //BA.debugLineNum = 1232;BA.debugLine="lblStatus.Text = \"Authenticating...\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Authenticating..."));
 //BA.debugLineNum = 1233;BA.debugLine="Dim jobLogin As HttpJob";
_joblogin = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1234;BA.debugLine="jobLogin.Initialize(\"\", Me)";
_joblogin._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1235;BA.debugLine="jobLogin.PostString(\"https://rest.prod.immedia-s";
_joblogin._poststring /*String*/ ("https://rest.prod.immedia-semi.com/login","email="+parent._emailaddress+"&password="+parent._password);
 //BA.debugLineNum = 1236;BA.debugLine="jobLogin.GetRequest.SetContentType(\"application/";
_joblogin._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetContentType("application/x-www-form-urlencoded");
 //BA.debugLineNum = 1237;BA.debugLine="Wait For (jobLogin) JobDone(jobLogin As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_joblogin));
this.state = 19;
return;
case 19:
//C
this.state = 4;
_joblogin = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1238;BA.debugLine="If jobLogin.Success Then";
if (true) break;

case 4:
//if
this.state = 15;
if (_joblogin._success /*boolean*/ ) { 
this.state = 6;
}else {
this.state = 14;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1239;BA.debugLine="lblStatus.Text = \"Successfully logged in to the";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Successfully logged in to the Blink server..."));
 //BA.debugLineNum = 1241;BA.debugLine="GetAuthInfo(jobLogin.GetString)";
_getauthinfo(_joblogin._getstring /*String*/ ());
 //BA.debugLineNum = 1243;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.C";
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
 //BA.debugLineNum = 1244;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1245;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1246;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1247;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1248;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1249;BA.debugLine="btnDrivewayRefresh.Enabled = True";
parent.mostCurrent._btndrivewayrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1250;BA.debugLine="btnFrontDoorRefresh.Enabled = True";
parent.mostCurrent._btnfrontdoorrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1251;BA.debugLine="btnSideYardRefresh.Enabled = True";
parent.mostCurrent._btnsideyardrefresh.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1252;BA.debugLine="jobLogin.Release";
_joblogin._release /*String*/ ();
 //BA.debugLineNum = 1253;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1255;BA.debugLine="lblStatus.Text = \"Authtoken acquired...\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Authtoken acquired..."));
 if (true) break;

case 12:
//C
this.state = 15;
;
 if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 1258;BA.debugLine="lblStatus.Text = GetRESTError(jobLogin.ErrorMes";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_joblogin._errormessage /*String*/ )));
 //BA.debugLineNum = 1259;BA.debugLine="Log(\"RequestAuthToken error: \" & jobLogin.Error";
anywheresoftware.b4a.keywords.Common.LogImpl("32424866","RequestAuthToken error: "+_joblogin._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1260;BA.debugLine="jobLogin.Release";
_joblogin._release /*String*/ ();
 //BA.debugLineNum = 1261;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 15:
//C
this.state = 18;
;
 //BA.debugLineNum = 1263;BA.debugLine="jobLogin.Release";
_joblogin._release /*String*/ ();
 if (true) break;

case 17:
//C
this.state = 18;
this.catchState = 0;
 //BA.debugLineNum = 1265;BA.debugLine="Log(\"RequestAuthToken LastException: \" & LastExc";
anywheresoftware.b4a.keywords.Common.LogImpl("32424872","RequestAuthToken LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 18:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1267;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 1268;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1544;BA.debugLine="Try";
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
 //BA.debugLineNum = 1545;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1546;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1547;BA.debugLine="j.Initialize(\"\", Me) 'name is empty as it is no";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1548;BA.debugLine="j.Download(url)";
_j._download /*String*/ (_url);
 //BA.debugLineNum = 1549;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1550;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 16;
return;
case 16:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1551;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 1552;BA.debugLine="response = j.GetString";
parent._response = _j._getstring /*String*/ ();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1554;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1555;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;
;
 //BA.debugLineNum = 1557;BA.debugLine="If response.Contains(\"System is busy, please wai";

case 9:
//if
this.state = 12;
if (parent._response.contains("System is busy, please wait")) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1558;BA.debugLine="lblStatus.Text = \"System is busy, please wait\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("System is busy, please wait"));
 if (true) break;

case 12:
//C
this.state = 15;
;
 //BA.debugLineNum = 1560;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 14:
//C
this.state = 15;
this.catchState = 0;
 //BA.debugLineNum = 1562;BA.debugLine="response = \"ERROR: \" & LastException";
parent._response = "ERROR: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA));
 //BA.debugLineNum = 1563;BA.debugLine="Log(\"RESTDownload LastException: \" & LastExcepti";
anywheresoftware.b4a.keywords.Common.LogImpl("32621460","RESTDownload LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 15:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1565;BA.debugLine="Log(\"URL: \" & url)";
anywheresoftware.b4a.keywords.Common.LogImpl("32621462","URL: "+_url,0);
 //BA.debugLineNum = 1566;BA.debugLine="Log(\"Response: \" & response)";
anywheresoftware.b4a.keywords.Common.LogImpl("32621463","Response: "+parent._response,0);
 //BA.debugLineNum = 1567;BA.debugLine="Return(response)";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)((parent._response)));return;};
 //BA.debugLineNum = 1568;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1571;BA.debugLine="Try";
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
 //BA.debugLineNum = 1572;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1573;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1574;BA.debugLine="j.Initialize(\"\", Me) 'name is empty as it is no";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1575;BA.debugLine="j.PostString(url,\"\")";
_j._poststring /*String*/ (_url,"");
 //BA.debugLineNum = 1576;BA.debugLine="j.GetRequest.SetContentType(\"application/x-www-f";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetContentType("application/x-www-form-urlencoded");
 //BA.debugLineNum = 1577;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1578;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 16;
return;
case 16:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1579;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 1580;BA.debugLine="response = j.GetString";
parent._response = _j._getstring /*String*/ ();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1582;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1583;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;
;
 //BA.debugLineNum = 1585;BA.debugLine="If response.Contains(\"System is busy, please wai";

case 9:
//if
this.state = 12;
if (parent._response.contains("System is busy, please wait")) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1586;BA.debugLine="lblStatus.Text = \"System is busy, please wait\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("System is busy, please wait"));
 if (true) break;

case 12:
//C
this.state = 15;
;
 //BA.debugLineNum = 1588;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 14:
//C
this.state = 15;
this.catchState = 0;
 //BA.debugLineNum = 1590;BA.debugLine="response = \"ERROR: \" & LastException";
parent._response = "ERROR: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA));
 //BA.debugLineNum = 1591;BA.debugLine="Log(\"RESTPost LastException: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("32686997","RESTPost LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 15:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1593;BA.debugLine="Log(\"URL: \" & url)";
anywheresoftware.b4a.keywords.Common.LogImpl("32686999","URL: "+_url,0);
 //BA.debugLineNum = 1594;BA.debugLine="Log(\"Response: \" & response)";
anywheresoftware.b4a.keywords.Common.LogImpl("32687000","Response: "+parent._response,0);
 //BA.debugLineNum = 1595;BA.debugLine="Return(response)";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)((parent._response)));return;};
 //BA.debugLineNum = 1596;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 2242;BA.debugLine="Private Sub SetAvg(bcBitmap As BitmapCreator, x As";
 //BA.debugLineNum = 2243;BA.debugLine="temp.Initialize";
_temp.Initialize();
 //BA.debugLineNum = 2244;BA.debugLine="For Each c As ARGBColor In clrs";
{
final b4a.example.bitmapcreator._argbcolor[] group2 = _clrs;
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_c = group2[index2];
 //BA.debugLineNum = 2245;BA.debugLine="temp.r = temp.r + c.r";
_temp.r = (int) (_temp.r+_c.r);
 //BA.debugLineNum = 2246;BA.debugLine="temp.g = temp.g + c.g";
_temp.g = (int) (_temp.g+_c.g);
 //BA.debugLineNum = 2247;BA.debugLine="temp.b = temp.b + c.b";
_temp.b = (int) (_temp.b+_c.b);
 }
};
 //BA.debugLineNum = 2249;BA.debugLine="temp.a = 255";
_temp.a = (int) (255);
 //BA.debugLineNum = 2250;BA.debugLine="temp.r = temp.r / clrs.Length";
_temp.r = (int) (_temp.r/(double)_clrs.length);
 //BA.debugLineNum = 2251;BA.debugLine="temp.g = temp.g / clrs.Length";
_temp.g = (int) (_temp.g/(double)_clrs.length);
 //BA.debugLineNum = 2252;BA.debugLine="temp.b = temp.b / clrs.Length";
_temp.b = (int) (_temp.b/(double)_clrs.length);
 //BA.debugLineNum = 2253;BA.debugLine="bcBitmap.SetARGB(x, y, temp)";
_bcbitmap._setargb(_x,_y,_temp);
 //BA.debugLineNum = 2254;BA.debugLine="End Sub";
return "";
}
public static String  _setdefaults() throws Exception{
 //BA.debugLineNum = 1209;BA.debugLine="Sub SetDefaults";
 //BA.debugLineNum = 1211;BA.debugLine="manager.SetString(\"HumidityAddValue\", \"6\")";
_manager.SetString("HumidityAddValue","6");
 //BA.debugLineNum = 1212;BA.debugLine="manager.SetString(\"SensorNotRespondingTime\", \"10\"";
_manager.SetString("SensorNotRespondingTime","10");
 //BA.debugLineNum = 1213;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",\"6\")";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue","6");
 //BA.debugLineNum = 1214;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime","10");
 //BA.debugLineNum = 1215;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 1216;BA.debugLine="End Sub";
return "";
}
public static String  _settextshadow(anywheresoftware.b4a.objects.LabelWrapper _lbl) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
float _radius = 0f;
float _dx = 0f;
float _dy = 0f;
 //BA.debugLineNum = 2256;BA.debugLine="Sub SetTextShadow(lbl As Label)";
 //BA.debugLineNum = 2257;BA.debugLine="Dim jo As JavaObject = lbl";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 2258;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Float";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 2259;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Black)});
 //BA.debugLineNum = 2260;BA.debugLine="End Sub";
>>>>>>> origin/master
return "";
}
public static String  _showaboutmenu() throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
String _tempdate = "";
 //BA.debugLineNum = 552;BA.debugLine="Sub ShowAboutMenu";
 //BA.debugLineNum = 553;BA.debugLine="Try";
try { //BA.debugLineNum = 554;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 555;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cloyd.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
<<<<<<< HEAD
 //BA.debugLineNum = 556;BA.debugLine="Dim tempDate As String";
_tempdate = "";
 //BA.debugLineNum = 557;BA.debugLine="DateTime.DateFormat=\"EEE, MMM d, yyyy h:mm aa\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("EEE, MMM d, yyyy h:mm aa");
 //BA.debugLineNum = 558;BA.debugLine="tempDate = DateTime.Date(compileTimeStamp)";
_tempdate = anywheresoftware.b4a.keywords.Common.DateTime.Date((long)(Double.parseDouble(_compiletimestamp)));
 //BA.debugLineNum = 559;BA.debugLine="Msgbox2(\"Smart Home Monitor v\" & GetVersionCode";
anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Smart Home Monitor v"+_getversioncode()+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Developed by: Cloyd Nino Catanaoan"+anywheresoftware.b4a.keywords.Common.CRLF+"Compiled: "+_tempdate),BA.ObjectToCharSequence("About"),"OK","","",_bd.getBitmap(),mostCurrent.activityBA);
 } 
       catch (Exception e9) {
			processBA.setLastException(e9); //BA.debugLineNum = 561;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("61310729",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
=======
 //BA.debugLineNum = 696;BA.debugLine="Msgbox2(\"Smart Home Monitor v\" & GetVersionCode";
anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Smart Home Monitor v"+_getversioncode()+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Developed by: Cloyd Nino Catanaoan"+anywheresoftware.b4a.keywords.Common.CRLF+"Compiled on: March 17, 2020"),BA.ObjectToCharSequence("About"),"OK","","",_bd.getBitmap(),mostCurrent.activityBA);
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 698;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("31310726",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
>>>>>>> origin/master
 };
 //BA.debugLineNum = 563;BA.debugLine="End Sub";
return "";
}
<<<<<<< HEAD
public static String  _tabstrip1_pageselected(int _position) throws Exception{
 //BA.debugLineNum = 613;BA.debugLine="Sub TabStrip1_PageSelected (Position As Int)";
 //BA.debugLineNum = 614;BA.debugLine="Try";
try { //BA.debugLineNum = 615;BA.debugLine="B4XPageIndicator1.CurrentPage = Position";
mostCurrent._b4xpageindicator1._setcurrentpage /*int*/ (_position);
 //BA.debugLineNum = 617;BA.debugLine="If Position > 3 Then";
if (_position>3) { 
 //BA.debugLineNum = 618;BA.debugLine="btnChart.mBase.Visible = False";
mostCurrent._btnchart._mbase /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setVisible(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 620;BA.debugLine="btnChart.mBase.Visible = True";
mostCurrent._btnchart._mbase /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 623;BA.debugLine="If Position = 0 Then";
=======
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _showvideo(String _link) throws Exception{
ResumableSub_ShowVideo rsub = new ResumableSub_ShowVideo(null,_link);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
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
long _ticks = 0L;
anywheresoftware.b4a.objects.collections.List _list1 = null;
int _i = 0;
Object _mytypes = null;
cloyd.smart.home.monitor.main._videoinfo _videos = null;
anywheresoftware.b4a.objects.IntentWrapper _intent1 = null;
anywheresoftware.b4a.phone.Phone _phone = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.objects.ImageViewWrapper _contentlabel = null;
cloyd.smart.home.monitor.main._carddata _cd = null;
anywheresoftware.b4a.objects.LabelWrapper _contentlabel1 = null;
int step34;
int limit34;

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
 //BA.debugLineNum = 2685;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 23;
this.catchState = 22;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 22;
 //BA.debugLineNum = 2686;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 2687;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 2688;BA.debugLine="j.Download(Link)";
_j._download /*String*/ (_link);
 //BA.debugLineNum = 2689;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 2690;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 37;
return;
case 37:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 2691;BA.debugLine="B4XLoadingIndicator4.hide";
parent.mostCurrent._b4xloadingindicator4._hide /*String*/ ();
 //BA.debugLineNum = 2692;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 2694;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File.";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"media.mp4",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2695;BA.debugLine="File.Copy2(j.GetInputStream, out)";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_j._getinputstream /*anywheresoftware.b4a.objects.streams.File.InputStreamWrapper*/ ().getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 2696;BA.debugLine="out.Close '<------ very important";
_out.Close();
 //BA.debugLineNum = 2698;BA.debugLine="Dim sb As StringBuilder";
_sb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 2699;BA.debugLine="sb.Initialize";
_sb.Initialize();
 //BA.debugLineNum = 2700;BA.debugLine="sb.Append(\"<video width='100%' height='100%' co";
_sb.Append("<video width='100%' height='100%' controls autoplay muted>");
 //BA.debugLineNum = 2701;BA.debugLine="sb.Append(\"<source src='\" & File.Combine(File.D";
_sb.Append("<source src='"+anywheresoftware.b4a.keywords.Common.File.Combine(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"\\media.mp4")+"' Type='video/mp4'/>");
 //BA.debugLineNum = 2702;BA.debugLine="sb.Append(\"</video>\")";
_sb.Append("</video>");
 //BA.debugLineNum = 2703;BA.debugLine="Dim WebViewSettings1 As WebViewSettings";
parent.mostCurrent._webviewsettings1 = new uk.co.martinpearman.b4a.webviewsettings.WebViewSettings();
 //BA.debugLineNum = 2704;BA.debugLine="WebViewSettings1.setMediaPlaybackRequiresUserGe";
parent.mostCurrent._webviewsettings1.setMediaPlaybackRequiresUserGesture((android.webkit.WebView)(parent.mostCurrent._wvmedia.getObject()),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2705;BA.debugLine="wvMedia.LoadHtml(sb.ToString)";
parent.mostCurrent._wvmedia.LoadHtml(_sb.ToString());
 //BA.debugLineNum = 2707;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 2708;BA.debugLine="r.Target = wvMedia";
_r.Target = (Object)(parent.mostCurrent._wvmedia.getObject());
 //BA.debugLineNum = 2709;BA.debugLine="r.Target = r.RunMethod(\"getSettings\")";
_r.Target = _r.RunMethod("getSettings");
 //BA.debugLineNum = 2710;BA.debugLine="r.RunMethod2(\"setBuiltInZoomControls\", True, \"j";
_r.RunMethod2("setBuiltInZoomControls",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True),"java.lang.boolean");
 //BA.debugLineNum = 2711;BA.debugLine="r.RunMethod2(\"setDisplayZoomControls\", False, \"";
_r.RunMethod2("setDisplayZoomControls",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.False),"java.lang.boolean");
 //BA.debugLineNum = 2713;BA.debugLine="mediaMetaData.Initialize";
parent._mediametadata._initialize /*String*/ (processBA);
 //BA.debugLineNum = 2714;BA.debugLine="mediaMetaData.ProcessMediaFile(File.DirInternal";
parent._mediametadata._processmediafile /*boolean*/ (anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"media.mp4");
 //BA.debugLineNum = 2715;BA.debugLine="Dim ticks As Long = (mediaMetaData.GetDuration/";
_ticks = (long) (((double)(Double.parseDouble(parent._mediametadata._getduration /*String*/ ()))/(double)1000)*anywheresoftware.b4a.keywords.Common.DateTime.TicksPerSecond);
 //BA.debugLineNum = 2716;BA.debugLine="lblDuration.Text = ConvertTicksToTimeString(tic";
parent.mostCurrent._lblduration.setText(BA.ObjectToCharSequence(_converttickstotimestring(_ticks)));
 if (true) break;

case 8:
//C
this.state = 9;
 if (true) break;
;
 //BA.debugLineNum = 2720;BA.debugLine="If j.ErrorMessage.Contains(\"Media not found\") Th";

case 9:
//if
this.state = 20;
if (_j._errormessage /*String*/ .contains("Media not found")) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 2721;BA.debugLine="clvActivity.RemoveAt(previousSelectedIndex)";
parent.mostCurrent._clvactivity._removeat(parent._previousselectedindex);
 //BA.debugLineNum = 2722;BA.debugLine="Dim list1 As List = Starter.kvs.ListKeys";
_list1 = new anywheresoftware.b4a.objects.collections.List();
_list1 = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._listkeys /*anywheresoftware.b4a.objects.collections.List*/ ();
 //BA.debugLineNum = 2723;BA.debugLine="For i =  0 To list1.Size-1";
if (true) break;

case 12:
//for
this.state = 19;
step34 = 1;
limit34 = (int) (_list1.getSize()-1);
_i = (int) (0) ;
this.state = 38;
if (true) break;

case 38:
//C
this.state = 19;
if ((step34 > 0 && _i <= limit34) || (step34 < 0 && _i >= limit34)) this.state = 14;
if (true) break;

case 39:
//C
this.state = 38;
_i = ((int)(0 + _i + step34)) ;
if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 2724;BA.debugLine="Dim mytypes As Object = Starter.kvs.Get(list1.";
_mytypes = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._get /*Object*/ (BA.ObjectToString(_list1.Get(_i)));
 //BA.debugLineNum = 2725;BA.debugLine="Dim videos = mytypes As VideoInfo";
_videos = (cloyd.smart.home.monitor.main._videoinfo)(_mytypes);
 //BA.debugLineNum = 2726;BA.debugLine="If Link.Contains(videos.VideoID) Then";
if (true) break;

case 15:
//if
this.state = 18;
if (_link.contains(_videos.VideoID /*String*/ )) { 
this.state = 17;
}if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 2727;BA.debugLine="Starter.kvs.Remove(list1.Get(i))";
parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._remove /*String*/ (BA.ObjectToString(_list1.Get(_i)));
 //BA.debugLineNum = 2728;BA.debugLine="Exit";
this.state = 19;
if (true) break;
 if (true) break;

case 18:
//C
this.state = 39;
;
 if (true) break;
if (true) break;

case 19:
//C
this.state = 20;
;
 //BA.debugLineNum = 2731;BA.debugLine="ToastMessageShow(\"Media not found. Removed from";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Media not found. Removed from the list."),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 20:
//C
this.state = 23;
;
 //BA.debugLineNum = 2733;BA.debugLine="j.Release";
_j._release /*String*/ ();
 //BA.debugLineNum = 2736;BA.debugLine="Dim Intent1 As Intent";
_intent1 = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 2737;BA.debugLine="Intent1.Initialize(\"blink.noti.clear\", \"\")";
_intent1.Initialize("blink.noti.clear","");
 //BA.debugLineNum = 2738;BA.debugLine="Dim Phone As Phone";
_phone = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 2739;BA.debugLine="Phone.SendBroadcastIntent(Intent1)";
_phone.SendBroadcastIntent((android.content.Intent)(_intent1.getObject()));
 if (true) break;

case 22:
//C
this.state = 23;
this.catchState = 0;
 //BA.debugLineNum = 2741;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("34522041",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 2744;BA.debugLine="Try";

case 23:
//try
this.state = 36;
this.catchState = 0;
this.catchState = 35;
this.state = 25;
if (true) break;

case 25:
//C
this.state = 26;
this.catchState = 35;
 //BA.debugLineNum = 2745;BA.debugLine="If previousSelectedIndex > (clvActivity.Size-1)";
if (true) break;

case 26:
//if
this.state = 29;
if (parent._previousselectedindex>(parent.mostCurrent._clvactivity._getsize()-1)) { 
this.state = 28;
}if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 2746;BA.debugLine="previousSelectedIndex = 0";
parent._previousselectedindex = (int) (0);
 if (true) break;

case 29:
//C
this.state = 30;
;
 //BA.debugLineNum = 2748;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(previous";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._clvactivity._getpanel(parent._previousselectedindex);
 //BA.debugLineNum = 2749;BA.debugLine="If p.NumberOfViews > 0 Then";
if (true) break;

case 30:
//if
this.state = 33;
if (_p.getNumberOfViews()>0) { 
this.state = 32;
}if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 2751;BA.debugLine="Dim ContentLabel As ImageView = p.GetView(0).Ge";
_contentlabel = new anywheresoftware.b4a.objects.ImageViewWrapper();
_contentlabel.setObject((android.widget.ImageView)(_p.GetView((int) (0)).GetView((int) (4)).getObject()));
 //BA.debugLineNum = 2752;BA.debugLine="ContentLabel.Visible = False";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2754;BA.debugLine="Dim cd As CardData = clvActivity.GetValue(previ";
_cd = (cloyd.smart.home.monitor.main._carddata)(parent.mostCurrent._clvactivity._getvalue(parent._previousselectedindex));
 //BA.debugLineNum = 2755;BA.debugLine="cd.iswatchedvisible = False";
_cd.iswatchedvisible /*boolean*/  = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 2757;BA.debugLine="Dim ContentLabel1 As Label = p.GetView(0).GetVi";
_contentlabel1 = new anywheresoftware.b4a.objects.LabelWrapper();
_contentlabel1.setObject((android.widget.TextView)(_p.GetView((int) (0)).GetView((int) (0)).getObject()));
 //BA.debugLineNum = 2758;BA.debugLine="ContentLabel1.Text = \"   \" & ConvertDayName(cd.";
_contentlabel1.setText(BA.ObjectToCharSequence("   "+_convertdayname(_cd.filedate /*String*/ )));
 //BA.debugLineNum = 2760;BA.debugLine="Dim ContentLabel As ImageView = p.GetView(0).Ge";
_contentlabel = new anywheresoftware.b4a.objects.ImageViewWrapper();
_contentlabel.setObject((android.widget.ImageView)(_p.GetView((int) (0)).GetView((int) (6)).getObject()));
 //BA.debugLineNum = 2761;BA.debugLine="ContentLabel.Visible = True";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 33:
//C
this.state = 36;
;
 //BA.debugLineNum = 2764;BA.debugLine="GetUnwatchedVideos";
_getunwatchedvideos();
 if (true) break;

case 35:
//C
this.state = 36;
this.catchState = 0;
 //BA.debugLineNum = 2766;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("34522066",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 36:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 2768;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 2769;BA.debugLine="End Sub";
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
int _fixedposition = 0;
int _direction = 0;
int _i = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
String _unwatchedvideoclips = "";
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.objects.B4XViewWrapper _backpane = null;
anywheresoftware.b4a.objects.B4XViewWrapper _contentlabel = null;
cloyd.smart.home.monitor.main._carddata _cd = null;
String _firstvideo = "";

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
 //BA.debugLineNum = 751;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 92;
this.catchState = 91;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 91;
 //BA.debugLineNum = 752;BA.debugLine="Dim FixedPosition As Int = -1";
_fixedposition = (int) (-1);
 //BA.debugLineNum = 753;BA.debugLine="If Pages.Get(Position) = False Then";
if (true) break;

case 4:
//if
this.state = 22;
if ((parent._pages.Get(_position)).equals((Object)(anywheresoftware.b4a.keywords.Common.False))) { 
this.state = 6;
}else {
this.state = 21;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 754;BA.debugLine="Dim direction As Int = Position - CurrentPage";
_direction = (int) (_position-parent._currentpage);
 //BA.debugLineNum = 755;BA.debugLine="Dim i As Int = Position + direction";
_i = (int) (_position+_direction);
 //BA.debugLineNum = 756;BA.debugLine="Do While i >= 0 And i < Pages.Size";
if (true) break;

case 7:
//do while
this.state = 14;
while (_i>=0 && _i<parent._pages.getSize()) {
this.state = 9;
if (true) break;
}
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 757;BA.debugLine="If Pages.Get(i) = True Then";
if (true) break;

case 10:
//if
this.state = 13;
if ((parent._pages.Get(_i)).equals((Object)(anywheresoftware.b4a.keywords.Common.True))) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 758;BA.debugLine="FixedPosition = i";
_fixedposition = _i;
 //BA.debugLineNum = 759;BA.debugLine="Exit";
this.state = 14;
if (true) break;
 if (true) break;

case 13:
//C
this.state = 7;
;
 //BA.debugLineNum = 761;BA.debugLine="i = i + direction";
_i = (int) (_i+_direction);
 if (true) break;
;
 //BA.debugLineNum = 763;BA.debugLine="If FixedPosition = -1 Then FixedPosition = Curr";

case 14:
//if
this.state = 19;
if (_fixedposition==-1) { 
this.state = 16;
;}if (true) break;

case 16:
//C
this.state = 19;
_fixedposition = parent._currentpage;
if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 765;BA.debugLine="FixedPosition = Position";
_fixedposition = _position;
 if (true) break;

case 22:
//C
this.state = 23;
;
 //BA.debugLineNum = 767;BA.debugLine="CurrentPage = FixedPosition";
parent._currentpage = _fixedposition;
 //BA.debugLineNum = 768;BA.debugLine="TabStrip1.ScrollTo(FixedPosition, True)";
parent.mostCurrent._tabstrip1.ScrollTo(_fixedposition,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 769;BA.debugLine="If CurrentPage <> Position Then Return";
if (true) break;

case 23:
//if
this.state = 28;
if (parent._currentpage!=_position) { 
this.state = 25;
;}if (true) break;

case 25:
//C
this.state = 28;
if (true) return ;
if (true) break;

case 28:
//C
this.state = 29;
;
 //BA.debugLineNum = 771;BA.debugLine="B4XPageIndicator1.CurrentPage = Position";
parent.mostCurrent._b4xpageindicator1._setcurrentpage /*int*/ (_position);
 //BA.debugLineNum = 772;BA.debugLine="If Position = 0 Then";
if (true) break;

case 29:
//if
this.state = 36;
>>>>>>> origin/master
if (_position==0) { 
 //BA.debugLineNum = 624;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 625;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Re";
_mqtt.Publish("TempHumid",_bc.StringToBytes("Read weather","utf8"));
 };
 //BA.debugLineNum = 627;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 };
 //BA.debugLineNum = 629;BA.debugLine="If Position = 1 Then";
if (_position==1) { 
 //BA.debugLineNum = 630;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 631;BA.debugLine="MQTT.Publish(\"MQ7LivingRoomCloyd\", bc.StringTo";
_mqtt.Publish("MQ7LivingRoomCloyd",_bc.StringToBytes("Read voltage","utf8"));
 };
 //BA.debugLineNum = 633;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 };
 //BA.debugLineNum = 635;BA.debugLine="If Position = 2 Then";
if (_position==2) { 
 //BA.debugLineNum = 636;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 637;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringToB";
_mqtt.Publish("TempHumidBasement",_bc.StringToBytes("Read weather","utf8"));
 };
 //BA.debugLineNum = 639;BA.debugLine="CheckTempHumiditySettingBasement";
_checktemphumiditysettingbasement();
 };
 //BA.debugLineNum = 641;BA.debugLine="If Position = 3 Then";
if (_position==3) { 
 //BA.debugLineNum = 642;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (_mqtt.IsInitialized() && _mqtt.getConnected()) { 
 //BA.debugLineNum = 643;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes(\"";
_mqtt.Publish("MQ7Basement",_bc.StringToBytes("Read voltage","utf8"));
 };
 //BA.debugLineNum = 645;BA.debugLine="CheckAirQualitySettingBasement";
_checkairqualitysettingbasement();
<<<<<<< HEAD
 };
 } 
       catch (Exception e33) {
			processBA.setLastException(e33); //BA.debugLineNum = 649;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("61638436",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 651;BA.debugLine="End Sub";
=======
 if (true) break;
;
 //BA.debugLineNum = 796;BA.debugLine="If Position = 4 Then";

case 57:
//if
this.state = 60;
if (_position==4) { 
this.state = 59;
}if (true) break;

case 59:
//C
this.state = 60;
 //BA.debugLineNum = 798;BA.debugLine="Dim rs As ResumableSub = CreateTab4";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _createtab4();
 //BA.debugLineNum = 799;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 93;
return;
case 93:
//C
this.state = 60;
_result = (Object) result[0];
;
 if (true) break;

case 60:
//C
this.state = 61;
;
 //BA.debugLineNum = 803;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 804;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 94;
return;
case 94:
//C
this.state = 61;
_result = (Object) result[0];
;
 //BA.debugLineNum = 806;BA.debugLine="Dim UnwatchedVideoClips As String = StateManager";
_unwatchedvideoclips = parent.mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"UnwatchedVideoClips");
 //BA.debugLineNum = 807;BA.debugLine="If IsNumber(UnwatchedVideoClips) Or isThereUnwat";
if (true) break;

case 61:
//if
this.state = 72;
if (anywheresoftware.b4a.keywords.Common.IsNumber(_unwatchedvideoclips) || parent._isthereunwatchedvideo) { 
this.state = 63;
}if (true) break;

case 63:
//C
this.state = 64;
 //BA.debugLineNum = 808;BA.debugLine="If UnwatchedVideoClips > 0 Or isThereUnwatchedV";
if (true) break;

case 64:
//if
this.state = 71;
if ((double)(Double.parseDouble(_unwatchedvideoclips))>0 || parent._isthereunwatchedvideo) { 
this.state = 66;
}if (true) break;

case 66:
//C
this.state = 67;
 //BA.debugLineNum = 809;BA.debugLine="If TabStrip1.CurrentPage <> 5 Then";
if (true) break;

case 67:
//if
this.state = 70;
if (parent.mostCurrent._tabstrip1.getCurrentPage()!=5) { 
this.state = 69;
}if (true) break;

case 69:
//C
this.state = 70;
 //BA.debugLineNum = 810;BA.debugLine="TabStrip1.ScrollTo(5,False)";
parent.mostCurrent._tabstrip1.ScrollTo((int) (5),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 811;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 95;
return;
case 95:
//C
this.state = 70;
;
 //BA.debugLineNum = 812;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 70:
//C
this.state = 71;
;
 //BA.debugLineNum = 814;BA.debugLine="clvActivity.Clear";
parent.mostCurrent._clvactivity._clear();
 //BA.debugLineNum = 816;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 817;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 96;
return;
case 96:
//C
this.state = 71;
_result = (Object) result[0];
;
 //BA.debugLineNum = 820;BA.debugLine="Dim rs As ResumableSub = GetVideos(response)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getvideos(parent._response);
 //BA.debugLineNum = 821;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 97;
return;
case 97:
//C
this.state = 71;
_result = (Object) result[0];
;
 //BA.debugLineNum = 824;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 825;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 98;
return;
case 98:
//C
this.state = 71;
_result = (Object) result[0];
;
 //BA.debugLineNum = 826;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 71:
//C
this.state = 72;
;
 if (true) break;
;
 //BA.debugLineNum = 830;BA.debugLine="If Position = 5 Then";

case 72:
//if
this.state = 89;
if (_position==5) { 
this.state = 74;
}if (true) break;

case 74:
//C
this.state = 75;
 //BA.debugLineNum = 831;BA.debugLine="If clvActivity.Size > 0 Then";
if (true) break;

case 75:
//if
this.state = 88;
if (parent.mostCurrent._clvactivity._getsize()>0) { 
this.state = 77;
}else {
this.state = 87;
}if (true) break;

case 77:
//C
this.state = 78;
 //BA.debugLineNum = 832;BA.debugLine="If previousSelectedIndex > (clvActivity.Size-1";
if (true) break;

case 78:
//if
this.state = 81;
if (parent._previousselectedindex>(parent.mostCurrent._clvactivity._getsize()-1)) { 
this.state = 80;
}if (true) break;

case 80:
//C
this.state = 81;
 //BA.debugLineNum = 833;BA.debugLine="previousSelectedIndex = 0";
parent._previousselectedindex = (int) (0);
 if (true) break;

case 81:
//C
this.state = 82;
;
 //BA.debugLineNum = 835;BA.debugLine="clvActivity.JumpToItem(previousSelectedIndex)";
parent.mostCurrent._clvactivity._jumptoitem(parent._previousselectedindex);
 //BA.debugLineNum = 836;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 99;
return;
case 99:
//C
this.state = 82;
;
 //BA.debugLineNum = 837;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(previo";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._clvactivity._getpanel(parent._previousselectedindex);
 //BA.debugLineNum = 838;BA.debugLine="If p.NumberOfViews > 0 Then";
if (true) break;

case 82:
//if
this.state = 85;
if (_p.getNumberOfViews()>0) { 
this.state = 84;
}if (true) break;

case 84:
//C
this.state = 85;
 //BA.debugLineNum = 839;BA.debugLine="Dim backPane As B4XView = p.getview(0)";
_backpane = new anywheresoftware.b4a.objects.B4XViewWrapper();
_backpane = _p.GetView((int) (0));
 //BA.debugLineNum = 840;BA.debugLine="backPane.Color = xui.Color_ARGB(255,217,215,2";
_backpane.setColor(parent.mostCurrent._xui.Color_ARGB((int) (255),(int) (217),(int) (215),(int) (222)));
 //BA.debugLineNum = 842;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).Ge";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (6));
 //BA.debugLineNum = 843;BA.debugLine="ContentLabel.Visible = True";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 85:
//C
this.state = 88;
;
 //BA.debugLineNum = 845;BA.debugLine="B4XLoadingIndicator4.Show";
parent.mostCurrent._b4xloadingindicator4._show /*String*/ ();
 //BA.debugLineNum = 846;BA.debugLine="Dim cd As CardData = clvActivity.GetValue(prev";
_cd = (cloyd.smart.home.monitor.main._carddata)(parent.mostCurrent._clvactivity._getvalue(parent._previousselectedindex));
 //BA.debugLineNum = 847;BA.debugLine="Dim firstvideo As String";
_firstvideo = "";
 //BA.debugLineNum = 848;BA.debugLine="firstvideo = cd.mediaURL";
_firstvideo = _cd.mediaURL /*String*/ ;
 //BA.debugLineNum = 849;BA.debugLine="lblDuration.Text = \"0:00\"";
parent.mostCurrent._lblduration.setText(BA.ObjectToCharSequence("0:00"));
 //BA.debugLineNum = 850;BA.debugLine="ShowVideo(firstvideo)";
_showvideo(_firstvideo);
 if (true) break;

case 87:
//C
this.state = 88;
 //BA.debugLineNum = 852;BA.debugLine="clvActivity.Clear";
parent.mostCurrent._clvactivity._clear();
 //BA.debugLineNum = 854;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 855;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 100;
return;
case 100:
//C
this.state = 88;
_result = (Object) result[0];
;
 //BA.debugLineNum = 858;BA.debugLine="Dim rs As ResumableSub = GetVideos(response)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getvideos(parent._response);
 //BA.debugLineNum = 859;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 101;
return;
case 101:
//C
this.state = 88;
_result = (Object) result[0];
;
 //BA.debugLineNum = 862;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 863;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 102;
return;
case 102:
//C
this.state = 88;
_result = (Object) result[0];
;
 if (true) break;

case 88:
//C
this.state = 89;
;
 if (true) break;

case 89:
//C
this.state = 92;
;
 if (true) break;

case 91:
//C
this.state = 92;
this.catchState = 0;
 //BA.debugLineNum = 868;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("31638518",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 92:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 870;BA.debugLine="End Sub";
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
public static String  _updateitemcolor(int _index) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.objects.B4XViewWrapper _backpane = null;
anywheresoftware.b4a.objects.B4XViewWrapper _contentlabel = null;
 //BA.debugLineNum = 2523;BA.debugLine="Sub UpdateItemColor (Index As Int)";
 //BA.debugLineNum = 2524;BA.debugLine="Try";
try { //BA.debugLineNum = 2525;BA.debugLine="If previousSelectedIndex > (clvActivity.Size-1)";
if (_previousselectedindex>(mostCurrent._clvactivity._getsize()-1)) { 
 //BA.debugLineNum = 2526;BA.debugLine="previousSelectedIndex = 0";
_previousselectedindex = (int) (0);
 };
 //BA.debugLineNum = 2529;BA.debugLine="If previousSelectedIndex <> Index Then";
if (_previousselectedindex!=_index) { 
 //BA.debugLineNum = 2530;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(previou";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._clvactivity._getpanel(_previousselectedindex);
 //BA.debugLineNum = 2531;BA.debugLine="If p.NumberOfViews > 0 Then";
if (_p.getNumberOfViews()>0) { 
 //BA.debugLineNum = 2532;BA.debugLine="Dim backPane As B4XView = p.getview(0)";
_backpane = new anywheresoftware.b4a.objects.B4XViewWrapper();
_backpane = _p.GetView((int) (0));
 //BA.debugLineNum = 2533;BA.debugLine="backPane.Color = xui.Color_White";
_backpane.setColor(mostCurrent._xui.Color_White);
 //BA.debugLineNum = 2535;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).Get";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (6));
 //BA.debugLineNum = 2536;BA.debugLine="ContentLabel.Visible = False";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 2540;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(Index)";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._clvactivity._getpanel(_index);
 //BA.debugLineNum = 2541;BA.debugLine="If p.NumberOfViews > 0 Then";
if (_p.getNumberOfViews()>0) { 
 //BA.debugLineNum = 2542;BA.debugLine="Dim backPane As B4XView = p.getview(0)";
_backpane = new anywheresoftware.b4a.objects.B4XViewWrapper();
_backpane = _p.GetView((int) (0));
 //BA.debugLineNum = 2543;BA.debugLine="backPane.Color = xui.Color_ARGB(255,217,215,222";
_backpane.setColor(mostCurrent._xui.Color_ARGB((int) (255),(int) (217),(int) (215),(int) (222)));
 //BA.debugLineNum = 2545;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).GetV";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (6));
 //BA.debugLineNum = 2546;BA.debugLine="ContentLabel.Visible = True";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 2549;BA.debugLine="previousSelectedIndex = Index";
_previousselectedindex = _index;
 } 
       catch (Exception e23) {
			processBA.setLastException(e23); //BA.debugLineNum = 2551;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("34259868",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 2553;BA.debugLine="End Sub";
return "";
}
public static String  _wvmedia_pagefinished(String _url) throws Exception{
 //BA.debugLineNum = 2771;BA.debugLine="Sub wvMedia_PageFinished (Url As String)";
 //BA.debugLineNum = 2773;BA.debugLine="End Sub";
>>>>>>> origin/master
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
//Sources:
//https://medium.com/@ssaurel/how-to-auto-restart-an-android-application-after-a-crash-or-a-force-close-error-1a361677c0ce
//https://stackoverflow.com/a/2903866
//https://mobikul.com/auto-restart-application-crashforce-close-android/

public void setDefaultUncaughtExceptionHandler() {
   
   Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
}


public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

  private Activity activity;

  public MyExceptionHandler(Activity a) {
    activity = a;
  }

  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    activity.finish();
    System.exit(2);
  }
}
}
