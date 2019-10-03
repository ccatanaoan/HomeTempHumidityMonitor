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
public static int _previousselectedindex = 0;
public static cloyd.smart.home.monitor.cmediadata _mediametadata = null;
public static String _drivewayarmedstatus = "";
public static String _frontdoorarmedstatus = "";
public static String _sideyardarmedstatus = "";
public static boolean _tabstripcurrentpage = false;
public static boolean _isthereunwatchedvideo = false;
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
public cloyd.smart.home.monitor.b4xloadingindicator _b4xloadingindicator4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblduration = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ivwatched = null;
public cloyd.smart.home.monitor.badger _badger1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _btndrivewaynewclip = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _btnfrontdoornewclip = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _btnsideyardnewclip = null;
public cloyd.smart.home.monitor.swiftbutton _btnrefresh = null;
public cloyd.smart.home.monitor.swiftbutton _btndriveway = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _lblmediaurl = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _btndrivewayfullscreenshot = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _btnfrontdoorfullscreenshot = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _btnsideyardfullscreenshot = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ivplay = null;
public b4a.example.dateutils _dateutils = null;
public cloyd.smart.home.monitor.smarthomemonitor _smarthomemonitor = null;
public cloyd.smart.home.monitor.notificationservice _notificationservice = null;
public cloyd.smart.home.monitor.statemanager _statemanager = null;
public cloyd.smart.home.monitor.starter _starter = null;
public cloyd.smart.home.monitor.httputils2service _httputils2service = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
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
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
anywheresoftware.b4a.BA.IterableList group25;
int index25;
int groupLen25;
anywheresoftware.b4a.BA.IterableList group30;
int index30;
int groupLen30;
anywheresoftware.b4a.BA.IterableList group94;
int index94;
int groupLen94;

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
 //BA.debugLineNum = 129;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 43;
this.catchState = 42;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 42;
 //BA.debugLineNum = 130;BA.debugLine="If FirstTime Then";
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
 //BA.debugLineNum = 131;BA.debugLine="CreatePreferenceScreen";
_createpreferencescreen();
 //BA.debugLineNum = 132;BA.debugLine="If manager.GetAll.Size = 0 Then SetDefaults";
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
 //BA.debugLineNum = 134;BA.debugLine="StartService(SmartHomeMonitor)";
anywheresoftware.b4a.keywords.Common.StartService(processBA,(Object)(parent.mostCurrent._smarthomemonitor.getObject()));
 //BA.debugLineNum = 135;BA.debugLine="csu.Initialize";
parent._csu._initialize(processBA);
 //BA.debugLineNum = 136;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 13:
//C
this.state = 14;
;
 //BA.debugLineNum = 138;BA.debugLine="Activity.LoadLayout(\"Main\")";
parent.mostCurrent._activity.LoadLayout("Main",mostCurrent.activityBA);
 //BA.debugLineNum = 140;BA.debugLine="TabStrip1.LoadLayout(\"1ScrollView\", \"LIVING AREA";
parent.mostCurrent._tabstrip1.LoadLayout("1ScrollView",BA.ObjectToCharSequence("LIVING AREA"+anywheresoftware.b4a.keywords.Common.CRLF+"Temp & Humidity  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf2c7)))));
 //BA.debugLineNum = 141;BA.debugLine="ScrollView1.Panel.LoadLayout(\"1\")";
parent.mostCurrent._scrollview1.getPanel().LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 142;BA.debugLine="Panel1.Height = ScrollView1.Height 'Panel1.Heigh";
parent.mostCurrent._panel1.setHeight(parent.mostCurrent._scrollview1.getHeight());
 //BA.debugLineNum = 143;BA.debugLine="ScrollView1.Panel.Height = Panel1.Height";
parent.mostCurrent._scrollview1.getPanel().setHeight(parent.mostCurrent._panel1.getHeight());
 //BA.debugLineNum = 144;BA.debugLine="TabStrip1.LoadLayout(\"2\", \"LIVING AREA\" & CRLF &";
parent.mostCurrent._tabstrip1.LoadLayout("2",BA.ObjectToCharSequence("LIVING AREA"+anywheresoftware.b4a.keywords.Common.CRLF+"Air Quality (CO)  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf289)))));
 //BA.debugLineNum = 145;BA.debugLine="TabStrip1.LoadLayout(\"ScrollViewBasement\", \"BASE";
parent.mostCurrent._tabstrip1.LoadLayout("ScrollViewBasement",BA.ObjectToCharSequence("BASEMENT"+anywheresoftware.b4a.keywords.Common.CRLF+"Temp & Humidity  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf2c7)))));
 //BA.debugLineNum = 146;BA.debugLine="ScrollViewBasement.Panel.LoadLayout(\"TempHumidit";
parent.mostCurrent._scrollviewbasement.getPanel().LoadLayout("TempHumidityBasement",mostCurrent.activityBA);
 //BA.debugLineNum = 147;BA.debugLine="PanelTempHumidityBasement.Height = ScrollViewBas";
parent.mostCurrent._paneltemphumiditybasement.setHeight(parent.mostCurrent._scrollviewbasement.getHeight());
 //BA.debugLineNum = 148;BA.debugLine="ScrollViewBasement.Panel.Height = PanelTempHumid";
parent.mostCurrent._scrollviewbasement.getPanel().setHeight(parent.mostCurrent._paneltemphumiditybasement.getHeight());
 //BA.debugLineNum = 149;BA.debugLine="TabStrip1.LoadLayout(\"AirQualityBasement\", \"BASE";
parent.mostCurrent._tabstrip1.LoadLayout("AirQualityBasement",BA.ObjectToCharSequence("BASEMENT"+anywheresoftware.b4a.keywords.Common.CRLF+"Air Quality (CO)  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf289)))));
 //BA.debugLineNum = 150;BA.debugLine="TabStrip1.LoadLayout(\"blinkscrollview\", \"OUTSIDE";
parent.mostCurrent._tabstrip1.LoadLayout("blinkscrollview",BA.ObjectToCharSequence("OUTSIDE"+anywheresoftware.b4a.keywords.Common.CRLF+"Security Camera  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf030)))));
 //BA.debugLineNum = 151;BA.debugLine="ScrollViewBlink.Panel.LoadLayout(\"blink\")";
parent.mostCurrent._scrollviewblink.getPanel().LoadLayout("blink",mostCurrent.activityBA);
 //BA.debugLineNum = 153;BA.debugLine="ScrollViewBlink.panel.height = 910dip";
parent.mostCurrent._scrollviewblink.getPanel().setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (910)));
 //BA.debugLineNum = 154;BA.debugLine="panelBlink.Height = 910dip";
parent.mostCurrent._panelblink.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (910)));
 //BA.debugLineNum = 155;BA.debugLine="TabStrip1.LoadLayout(\"blinkactivity\", \"ACTIVITY\"";
parent.mostCurrent._tabstrip1.LoadLayout("blinkactivity",BA.ObjectToCharSequence("ACTIVITY"+anywheresoftware.b4a.keywords.Common.CRLF+"Security Camera  "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf03d)))));
 //BA.debugLineNum = 157;BA.debugLine="For Each lbl As Label In GetAllTabLabels(TabStri";
if (true) break;

case 14:
//for
this.state = 17;
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
group25 = _getalltablabels(parent.mostCurrent._tabstrip1);
index25 = 0;
groupLen25 = group25.getSize();
this.state = 44;
if (true) break;

case 44:
//C
this.state = 17;
if (index25 < groupLen25) {
this.state = 16;
_lbl.setObject((android.widget.TextView)(group25.Get(index25)));}
if (true) break;

case 45:
//C
this.state = 44;
index25++;
if (true) break;

case 16:
//C
this.state = 45;
 //BA.debugLineNum = 159;BA.debugLine="lbl.SingleLine = False";
_lbl.setSingleLine(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 161;BA.debugLine="lbl.Typeface = Typeface.FONTAWESOME";
_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME());
 //BA.debugLineNum = 163;BA.debugLine="lbl.Padding = Array As Int(0, 0, 0, 0)";
_lbl.setPadding(new int[]{(int) (0),(int) (0),(int) (0),(int) (0)});
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 167;BA.debugLine="For Each v As View In GetAllTabLabels(TabStrip1)";

case 17:
//for
this.state = 20;
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
group30 = _getalltablabels(parent.mostCurrent._tabstrip1);
index30 = 0;
groupLen30 = group30.getSize();
this.state = 46;
if (true) break;

case 46:
//C
this.state = 20;
if (index30 < groupLen30) {
this.state = 19;
_v.setObject((android.view.View)(group30.Get(index30)));}
if (true) break;

case 47:
//C
this.state = 46;
index30++;
if (true) break;

case 19:
//C
this.state = 47;
 //BA.debugLineNum = 169;BA.debugLine="v.Width = 33%x";
_v.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA));
 if (true) break;
if (true) break;

case 20:
//C
this.state = 21;
;
 //BA.debugLineNum = 172;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 173;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 174;BA.debugLine="ACToolBarLight1.NavigationIconDrawable = bd";
parent.mostCurrent._actoolbarlight1.setNavigationIconDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 175;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 176;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 177;BA.debugLine="ToolbarHelper.Title= cs.Initialize.Size(22).Appe";
parent.mostCurrent._toolbarhelper.setTitle(BA.ObjectToCharSequence(_cs.Initialize().Size((int) (22)).Append(BA.ObjectToCharSequence("Smart Home Monitor")).PopAll().getObject()));
 //BA.debugLineNum = 178;BA.debugLine="ToolbarHelper.subTitle = \"\"";
parent.mostCurrent._toolbarhelper.setSubtitle(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 179;BA.debugLine="ToolbarHelper.ShowUpIndicator = False 'set to tr";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 180;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 181;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 182;BA.debugLine="Dim xl As XmlLayoutBuilder";
_xl = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 183;BA.debugLine="jo.RunMethod(\"setPopupTheme\", Array(xl.GetResour";
_jo.RunMethod("setPopupTheme",new Object[]{(Object)(_xl.GetResourceId("style","ToolbarMenu"))});
 //BA.debugLineNum = 185;BA.debugLine="GaugeHumidity.SetRanges(Array(GaugeHumidity.Crea";
parent.mostCurrent._gaugehumidity._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (70),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (70),(float) (80),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugehumidity._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (80),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 188;BA.debugLine="GaugeTemp.SetRanges(Array(GaugeTemp.CreateRange(";
parent.mostCurrent._gaugetemp._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugetemp._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 191;BA.debugLine="GaugeHeatIndex.SetRanges(Array(GaugeHeatIndex.Cr";
parent.mostCurrent._gaugeheatindex._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugeheatindex._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 196;BA.debugLine="GaugeDewPoint.SetRanges(Array(GaugeDewPoint.Crea";
parent.mostCurrent._gaugedewpoint._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (60.8),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (60.8),(float) (64.4),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(parent.mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (64.4),(float) (78.8),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugedewpoint._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (78.8),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 202;BA.debugLine="GaugeAirQuality.SetRanges(Array(GaugeAirQuality.";
parent.mostCurrent._gaugeairquality._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (100),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (100),(float) (400),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(parent.mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (400),(float) (900),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugeairquality._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (900),(float) (1000),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 206;BA.debugLine="GaugeAirQuality.CurrentValue=0";
parent.mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 208;BA.debugLine="GaugeHumidityBasement.SetRanges(Array(GaugeHumid";
parent.mostCurrent._gaugehumiditybasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (70),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (70),(float) (80),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugehumiditybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (80),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 211;BA.debugLine="GaugeTempBasement.SetRanges(Array(GaugeTempBasem";
parent.mostCurrent._gaugetempbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugetempbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 214;BA.debugLine="GaugeHeatIndexBasement.SetRanges(Array(GaugeHeat";
parent.mostCurrent._gaugeheatindexbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (75),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (75),(float) (90),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugeheatindexbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (90),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 219;BA.debugLine="GaugeDewPointBasement.SetRanges(Array(GaugeDewPo";
parent.mostCurrent._gaugedewpointbasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (60.8),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (60.8),(float) (64.4),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(parent.mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (64.4),(float) (78.8),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugedewpointbasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (78.8),(float) (100),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 225;BA.debugLine="GaugeAirQualityBasement.SetRanges(Array(GaugeAir";
parent.mostCurrent._gaugeairqualitybasement._setranges /*String*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent.mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (0),(float) (100),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (221),(int) (23)))),(Object)(parent.mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (100),(float) (400),parent.mostCurrent._xui.Color_RGB((int) (100),(int) (240),(int) (23)))),(Object)(parent.mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (400),(float) (900),parent.mostCurrent._xui.Color_Yellow)),(Object)(parent.mostCurrent._gaugeairqualitybasement._createrange /*cloyd.smart.home.monitor.gauge._gaugerange*/ ((float) (900),(float) (1000),parent.mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 229;BA.debugLine="GaugeAirQualityBasement.CurrentValue=0";
parent.mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 231;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 232;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 233;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append(\"";
parent.mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 234;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"The";
parent.mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 235;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 236;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
parent.mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 237;BA.debugLine="lblPing.Visible = False";
parent.mostCurrent._lblping.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 238;BA.debugLine="GaugeAirQuality.CurrentValue = 0";
parent.mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 239;BA.debugLine="GaugeAirQualityBasement.CurrentValue = 0";
parent.mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float) (0));
 //BA.debugLineNum = 240;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Append(\"";
parent.mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 241;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.Bol";
parent.mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).PopAll().getObject()));
 //BA.debugLineNum = 243;BA.debugLine="lblPerceptionBasement.Text = cs.Initialize.Bold.";
parent.mostCurrent._lblperceptionbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 244;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.App";
parent.mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 245;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 246;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bold.";
parent.mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 247;BA.debugLine="lblPingBasement.Visible = False";
parent.mostCurrent._lblpingbasement.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 248;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bold.";
parent.mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence("Waiting for data...")).PopAll().getObject()));
 //BA.debugLineNum = 249;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Initia";
parent.mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).PopAll().getObject()));
 //BA.debugLineNum = 251;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
if (true) break;

case 21:
//if
this.state = 28;
if (parent._mqtt.IsInitialized() && parent._mqtt.getConnected()) { 
this.state = 23;
}if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 252;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Rea";
parent._mqtt.Publish("TempHumid",parent._bc.StringToBytes("Read weather","utf8"));
 //BA.debugLineNum = 254;BA.debugLine="Dim strHumidityAddValue As String = StateManage";
_strhumidityaddvalue = parent.mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue");
 //BA.debugLineNum = 255;BA.debugLine="If strHumidityAddValue = \"\" Then";
if (true) break;

case 24:
//if
this.state = 27;
if ((_strhumidityaddvalue).equals("")) { 
this.state = 26;
}if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 256;BA.debugLine="strHumidityAddValue = \"0\"";
_strhumidityaddvalue = "0";
 if (true) break;

case 27:
//C
this.state = 28;
;
 //BA.debugLineNum = 258;BA.debugLine="MQTT.Publish(\"HumidityAddValue\", bc.StringToByt";
parent._mqtt.Publish("HumidityAddValue",parent._bc.StringToBytes(_strhumidityaddvalue,"utf8"));
 if (true) break;

case 28:
//C
this.state = 29;
;
 //BA.debugLineNum = 261;BA.debugLine="SetTextShadow(lblDrivewayBatt)";
_settextshadow(parent.mostCurrent._lbldrivewaybatt);
 //BA.debugLineNum = 262;BA.debugLine="SetTextShadow(lblDrivewayTimestamp)";
_settextshadow(parent.mostCurrent._lbldrivewaytimestamp);
 //BA.debugLineNum = 263;BA.debugLine="SetTextShadow(lblDrivewayWifi)";
_settextshadow(parent.mostCurrent._lbldrivewaywifi);
 //BA.debugLineNum = 264;BA.debugLine="SetTextShadow(lblFrontDoorBatt)";
_settextshadow(parent.mostCurrent._lblfrontdoorbatt);
 //BA.debugLineNum = 265;BA.debugLine="SetTextShadow(lblFrontDoorTimestamp)";
_settextshadow(parent.mostCurrent._lblfrontdoortimestamp);
 //BA.debugLineNum = 266;BA.debugLine="SetTextShadow(lblFrontDoorWiFi)";
_settextshadow(parent.mostCurrent._lblfrontdoorwifi);
 //BA.debugLineNum = 267;BA.debugLine="SetTextShadow(lblSideYardBatt)";
_settextshadow(parent.mostCurrent._lblsideyardbatt);
 //BA.debugLineNum = 268;BA.debugLine="SetTextShadow(lblSideYardTimestamp)";
_settextshadow(parent.mostCurrent._lblsideyardtimestamp);
 //BA.debugLineNum = 269;BA.debugLine="SetTextShadow(lblSideYardWiFi)";
_settextshadow(parent.mostCurrent._lblsideyardwifi);
 //BA.debugLineNum = 270;BA.debugLine="SetTextShadow(lblDuration)";
_settextshadow(parent.mostCurrent._lblduration);
 //BA.debugLineNum = 272;BA.debugLine="badger1.Initialize";
parent.mostCurrent._badger1._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 274;BA.debugLine="For Each lblBadge As B4XView In GetAllTabLabelsF";
if (true) break;

case 29:
//for
this.state = 40;
_lblbadge = new anywheresoftware.b4a.objects.B4XViewWrapper();
group94 = _getalltablabelsforbadge(parent.mostCurrent._tabstrip1);
index94 = 0;
groupLen94 = group94.getSize();
this.state = 48;
if (true) break;

case 48:
//C
this.state = 40;
if (index94 < groupLen94) {
this.state = 31;
_lblbadge.setObject((java.lang.Object)(group94.Get(index94)));}
if (true) break;

case 49:
//C
this.state = 48;
index94++;
if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 275;BA.debugLine="Dim Width, Height As Int";
_width = 0;
_height = 0;
 //BA.debugLineNum = 276;BA.debugLine="Dim jo As JavaObject = lblBadge";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_lblbadge.getObject()));
 //BA.debugLineNum = 277;BA.debugLine="Do While True";
if (true) break;

case 32:
//do while
this.state = 39;
while (anywheresoftware.b4a.keywords.Common.True) {
this.state = 34;
if (true) break;
}
if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 278;BA.debugLine="Width = jo.RunMethod(\"getMeasuredWidth\", Null)";
_width = (int)(BA.ObjectToNumber(_jo.RunMethod("getMeasuredWidth",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 279;BA.debugLine="Height = jo.RunMethod(\"getMeasuredHeight\", Nul";
_height = (int)(BA.ObjectToNumber(_jo.RunMethod("getMeasuredHeight",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 280;BA.debugLine="If Width > 0 And Height > 0 Then";
if (true) break;

case 35:
//if
this.state = 38;
if (_width>0 && _height>0) { 
this.state = 37;
}if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 281;BA.debugLine="Exit";
this.state = 39;
if (true) break;
 if (true) break;

case 38:
//C
this.state = 32;
;
 //BA.debugLineNum = 283;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 50;
return;
case 50:
//C
this.state = 32;
;
 if (true) break;

case 39:
//C
this.state = 49;
;
 //BA.debugLineNum = 285;BA.debugLine="Dim p As Panel = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
_p.setObject((android.view.ViewGroup)(parent.mostCurrent._xui.CreatePanel(processBA,"").getObject()));
 //BA.debugLineNum = 286;BA.debugLine="p.Tag = lblBadge";
_p.setTag((Object)(_lblbadge.getObject()));
 //BA.debugLineNum = 287;BA.debugLine="lblBadge.Parent.AddView(p, 0, 0, Width, Height)";
_lblbadge.getParent().AddView((android.view.View)(_p.getObject()),(int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 288;BA.debugLine="lblBadge.RemoveViewFromParent";
_lblbadge.RemoveViewFromParent();
 //BA.debugLineNum = 289;BA.debugLine="p.AddView(lblBadge, 0, 0, Width, Height)";
_p.AddView((android.view.View)(_lblbadge.getObject()),(int) (0),(int) (0),_width,_height);
 if (true) break;
if (true) break;

case 40:
//C
this.state = 43;
;
 //BA.debugLineNum = 295;BA.debugLine="Dim rs As ResumableSub = RequestAuthToken";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _requestauthtoken();
 //BA.debugLineNum = 296;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 51;
return;
case 51:
//C
this.state = 43;
_result = (Object) result[0];
;
 if (true) break;

case 42:
//C
this.state = 43;
this.catchState = 0;
 //BA.debugLineNum = 299;BA.debugLine="ToastMessageShow(LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;
if (true) break;

case 43:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 301;BA.debugLine="End Sub";
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
public static void  _complete(Object _result) throws Exception{
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
 //BA.debugLineNum = 650;BA.debugLine="Sub Activity_Createmenu(Menu As ACMenu)";
 //BA.debugLineNum = 651;BA.debugLine="Try";
try { //BA.debugLineNum = 652;BA.debugLine="Menu.Clear";
_menu.Clear();
 //BA.debugLineNum = 653;BA.debugLine="gblACMenu = Menu";
mostCurrent._gblacmenu = _menu;
 //BA.debugLineNum = 654;BA.debugLine="Menu.Add(0, 0, \"Settings\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Settings"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 655;BA.debugLine="Menu.Add(0, 0, \"Show KeyValueStore records numbe";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Show KeyValueStore records numbers"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 656;BA.debugLine="Menu.Add(0, 0, \"Remove all KeyValueStore records";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Remove all KeyValueStore records"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 657;BA.debugLine="Menu.Add(0, 0, \"About\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 } 
       catch (Exception e9) {
			processBA.setLastException(e9); //BA.debugLineNum = 659;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("41376265",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 661;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 818;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 819;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 820;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (mostCurrent._tabstrip1.getCurrentPage()==2) { 
 //BA.debugLineNum = 821;BA.debugLine="TabStrip1.ScrollTo(1,True)";
mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 822;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 //BA.debugLineNum = 823;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==1) { 
 //BA.debugLineNum = 825;BA.debugLine="TabStrip1.ScrollTo(0,True)";
mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 826;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 //BA.debugLineNum = 827;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==3) { 
 //BA.debugLineNum = 829;BA.debugLine="TabStrip1.ScrollTo(2,True)";
mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 830;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 //BA.debugLineNum = 831;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==4) { 
 //BA.debugLineNum = 833;BA.debugLine="TabStrip1.ScrollTo(3,True)";
mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 834;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 //BA.debugLineNum = 835;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else if(mostCurrent._tabstrip1.getCurrentPage()==5) { 
 //BA.debugLineNum = 837;BA.debugLine="TabStrip1.ScrollTo(4,True)";
mostCurrent._tabstrip1.ScrollTo((int) (4),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 838;BA.debugLine="TabStrip1_PageSelected(4)";
_tabstrip1_pageselected((int) (4));
 //BA.debugLineNum = 839;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 843;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 407;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 408;BA.debugLine="wvMedia.LoadUrl(\"\")";
mostCurrent._wvmedia.LoadUrl("");
 //BA.debugLineNum = 409;BA.debugLine="End Sub";
return "";
}
public static void  _activity_resume() throws Exception{
ResumableSub_Activity_Resume rsub = new ResumableSub_Activity_Resume(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Resume extends BA.ResumableSub {
public ResumableSub_Activity_Resume(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
anywheresoftware.b4a.objects.IntentWrapper _in = null;
String _notificationclicked = "";
String _unwatchedvideoclips = "";
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
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
 //BA.debugLineNum = 304;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 61;
this.catchState = 60;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 60;
 //BA.debugLineNum = 308;BA.debugLine="HandleSettings";
_handlesettings();
 //BA.debugLineNum = 309;BA.debugLine="Dim in As Intent = Activity.GetStartingIntent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
_in = parent.mostCurrent._activity.GetStartingIntent();
 //BA.debugLineNum = 310;BA.debugLine="Dim NotificationClicked As String";
_notificationclicked = "";
 //BA.debugLineNum = 311;BA.debugLine="If in.IsInitialized And in <> OldIntent Then";
if (true) break;

case 4:
//if
this.state = 11;
if (_in.IsInitialized() && (_in).equals(parent._oldintent) == false) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 312;BA.debugLine="OldIntent = in";
parent._oldintent = _in;
 //BA.debugLineNum = 313;BA.debugLine="If in.HasExtra(\"Notification_Tag\") Then";
if (true) break;

case 7:
//if
this.state = 10;
if (_in.HasExtra("Notification_Tag")) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 314;BA.debugLine="NotificationClicked = in.GetExtra(\"Notificatio";
_notificationclicked = BA.ObjectToString(_in.GetExtra("Notification_Tag"));
 if (true) break;

case 10:
//C
this.state = 11;
;
 if (true) break;
;
 //BA.debugLineNum = 318;BA.debugLine="If NotificationClicked = \"Living area temperatur";

case 11:
//if
this.state = 28;
if ((_notificationclicked).equals("Living area temperature")) { 
this.state = 13;
}else if((_notificationclicked).equals("Living area carbon monoxide")) { 
this.state = 15;
}else if((_notificationclicked).equals("Basement temperature")) { 
this.state = 17;
}else if((_notificationclicked).equals("Basement carbon monoxide")) { 
this.state = 19;
}else if((_notificationclicked).equals("Basement DHT22 sensor issue")) { 
this.state = 21;
}else if((_notificationclicked).equals("Living area DHT22 sensor issue")) { 
this.state = 23;
}else if((_notificationclicked).equals("Living area CO sensor issue")) { 
this.state = 25;
}else if((_notificationclicked).equals("Basement CO sensor issue")) { 
this.state = 27;
}if (true) break;

case 13:
//C
this.state = 28;
 //BA.debugLineNum = 319;BA.debugLine="TabStrip1.ScrollTo(0,False)";
parent.mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 320;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 if (true) break;

case 15:
//C
this.state = 28;
 //BA.debugLineNum = 322;BA.debugLine="TabStrip1.ScrollTo(1,False)";
parent.mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 323;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 if (true) break;

case 17:
//C
this.state = 28;
 //BA.debugLineNum = 325;BA.debugLine="TabStrip1.ScrollTo(2,False)";
parent.mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 326;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 if (true) break;

case 19:
//C
this.state = 28;
 //BA.debugLineNum = 328;BA.debugLine="TabStrip1.ScrollTo(3,False)";
parent.mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 329;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 if (true) break;

case 21:
//C
this.state = 28;
 //BA.debugLineNum = 331;BA.debugLine="TabStrip1.ScrollTo(2,False)";
parent.mostCurrent._tabstrip1.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 332;BA.debugLine="TabStrip1_PageSelected(2)";
_tabstrip1_pageselected((int) (2));
 if (true) break;

case 23:
//C
this.state = 28;
 //BA.debugLineNum = 334;BA.debugLine="TabStrip1.ScrollTo(0,False)";
parent.mostCurrent._tabstrip1.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 335;BA.debugLine="TabStrip1_PageSelected(0)";
_tabstrip1_pageselected((int) (0));
 if (true) break;

case 25:
//C
this.state = 28;
 //BA.debugLineNum = 337;BA.debugLine="TabStrip1.ScrollTo(1,False)";
parent.mostCurrent._tabstrip1.ScrollTo((int) (1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 338;BA.debugLine="TabStrip1_PageSelected(1)";
_tabstrip1_pageselected((int) (1));
 if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 340;BA.debugLine="TabStrip1.ScrollTo(3,False)";
parent.mostCurrent._tabstrip1.ScrollTo((int) (3),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 341;BA.debugLine="TabStrip1_PageSelected(3)";
_tabstrip1_pageselected((int) (3));
 if (true) break;

case 28:
//C
this.state = 29;
;
 //BA.debugLineNum = 344;BA.debugLine="Dim UnwatchedVideoClips As String = StateManager";
_unwatchedvideoclips = parent.mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"UnwatchedVideoClips");
 //BA.debugLineNum = 345;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 71;
return;
case 71:
//C
this.state = 29;
;
 //BA.debugLineNum = 348;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 349;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 72;
return;
case 72:
//C
this.state = 29;
_result = (Object) result[0];
;
 //BA.debugLineNum = 351;BA.debugLine="Dim UnwatchedVideoClips As String = StateManager";
_unwatchedvideoclips = parent.mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"UnwatchedVideoClips");
 //BA.debugLineNum = 352;BA.debugLine="If IsNumber(UnwatchedVideoClips) Or isThereUnwat";
if (true) break;

case 29:
//if
this.state = 46;
if (anywheresoftware.b4a.keywords.Common.IsNumber(_unwatchedvideoclips) || parent._isthereunwatchedvideo==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 31;
}if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 353;BA.debugLine="If UnwatchedVideoClips > 0 Then";
if (true) break;

case 32:
//if
this.state = 45;
if ((double)(Double.parseDouble(_unwatchedvideoclips))>0) { 
this.state = 34;
}if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 354;BA.debugLine="tabStripCurrentPage = False";
parent._tabstripcurrentpage = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 355;BA.debugLine="TabStrip1_PageSelected(5)";
_tabstrip1_pageselected((int) (5));
 //BA.debugLineNum = 356;BA.debugLine="tabStripCurrentPage = True";
parent._tabstripcurrentpage = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 357;BA.debugLine="TabStrip1.ScrollTo(5,False)";
parent.mostCurrent._tabstrip1.ScrollTo((int) (5),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 358;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 73;
return;
case 73:
//C
this.state = 35;
;
 //BA.debugLineNum = 360;BA.debugLine="Try";
if (true) break;

case 35:
//try
this.state = 44;
this.catchState = 43;
this.state = 37;
if (true) break;

case 37:
//C
this.state = 38;
this.catchState = 43;
 //BA.debugLineNum = 361;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connect";
if (true) break;

case 38:
//if
this.state = 41;
if (parent._mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || parent._mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 40;
}if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 362;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 41:
//C
this.state = 44;
;
 if (true) break;

case 43:
//C
this.state = 44;
this.catchState = 60;
 //BA.debugLineNum = 365;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("4196670",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 44:
//C
this.state = 45;
this.catchState = 60;
;
 //BA.debugLineNum = 367;BA.debugLine="isThereUnwatchedVideo = False";
parent._isthereunwatchedvideo = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 368;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 45:
//C
this.state = 46;
;
 if (true) break;

case 46:
//C
this.state = 47;
;
 //BA.debugLineNum = 372;BA.debugLine="isThereUnwatchedVideo = False";
parent._isthereunwatchedvideo = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 374;BA.debugLine="If tabStripCurrentPage = False And TabStrip1.Cur";
if (true) break;

case 47:
//if
this.state = 58;
if (parent._tabstripcurrentpage==anywheresoftware.b4a.keywords.Common.False && parent.mostCurrent._tabstrip1.getCurrentPage()==5) { 
this.state = 49;
}if (true) break;

case 49:
//C
this.state = 50;
 //BA.debugLineNum = 375;BA.debugLine="If clvActivity.Size > 0 Then";
if (true) break;

case 50:
//if
this.state = 57;
if (parent.mostCurrent._clvactivity._getsize()>0) { 
this.state = 52;
}if (true) break;

case 52:
//C
this.state = 53;
 //BA.debugLineNum = 376;BA.debugLine="clvActivity.JumpToItem(previousSelectedIndex)";
parent.mostCurrent._clvactivity._jumptoitem(parent._previousselectedindex);
 //BA.debugLineNum = 377;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 74;
return;
case 74:
//C
this.state = 53;
;
 //BA.debugLineNum = 378;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(previo";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._clvactivity._getpanel(parent._previousselectedindex);
 //BA.debugLineNum = 379;BA.debugLine="If p.NumberOfViews > 0 Then";
if (true) break;

case 53:
//if
this.state = 56;
if (_p.getNumberOfViews()>0) { 
this.state = 55;
}if (true) break;

case 55:
//C
this.state = 56;
 //BA.debugLineNum = 380;BA.debugLine="Dim backPane As B4XView = p.getview(0)";
_backpane = new anywheresoftware.b4a.objects.B4XViewWrapper();
_backpane = _p.GetView((int) (0));
 //BA.debugLineNum = 381;BA.debugLine="backPane.Color = xui.Color_ARGB(255,217,215,2";
_backpane.setColor(parent.mostCurrent._xui.Color_ARGB((int) (255),(int) (217),(int) (215),(int) (222)));
 //BA.debugLineNum = 383;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).Ge";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (6));
 //BA.debugLineNum = 384;BA.debugLine="ContentLabel.Visible = True";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 56:
//C
this.state = 57;
;
 //BA.debugLineNum = 386;BA.debugLine="B4XLoadingIndicator4.Show";
parent.mostCurrent._b4xloadingindicator4._show /*String*/ ();
 //BA.debugLineNum = 387;BA.debugLine="Dim cd As CardData = clvActivity.GetValue(prev";
_cd = (cloyd.smart.home.monitor.main._carddata)(parent.mostCurrent._clvactivity._getvalue(parent._previousselectedindex));
 //BA.debugLineNum = 388;BA.debugLine="Dim firstvideo As String";
_firstvideo = "";
 //BA.debugLineNum = 389;BA.debugLine="firstvideo = cd.mediaURL";
_firstvideo = _cd.mediaURL /*String*/ ;
 //BA.debugLineNum = 390;BA.debugLine="lblDuration.Text = \"0:00\"";
parent.mostCurrent._lblduration.setText(BA.ObjectToCharSequence("0:00"));
 //BA.debugLineNum = 391;BA.debugLine="ShowVideo(firstvideo)";
_showvideo(_firstvideo);
 if (true) break;

case 57:
//C
this.state = 58;
;
 if (true) break;

case 58:
//C
this.state = 61;
;
 if (true) break;

case 60:
//C
this.state = 61;
this.catchState = 0;
 //BA.debugLineNum = 395;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("4196700",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 398;BA.debugLine="Try";

case 61:
//try
this.state = 70;
this.catchState = 0;
this.catchState = 69;
this.state = 63;
if (true) break;

case 63:
//C
this.state = 64;
this.catchState = 69;
 //BA.debugLineNum = 399;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connected";
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
 //BA.debugLineNum = 400;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 67:
//C
this.state = 70;
;
 if (true) break;

case 69:
//C
this.state = 70;
this.catchState = 0;
 //BA.debugLineNum = 403;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("4196708",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 70:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 405;BA.debugLine="End Sub";
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
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _results = null;
int _result = 0;

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
 //BA.debugLineNum = 555;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 70;
this.catchState = 69;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 69;
 //BA.debugLineNum = 556;BA.debugLine="If Item.Title = \"About\" Then";
if (true) break;

case 4:
//if
this.state = 67;
if ((_item.getTitle()).equals("About")) { 
this.state = 6;
}else if((_item.getTitle()).equals("Settings")) { 
this.state = 8;
}else if((_item.getTitle()).equals("Show KeyValueStore records numbers")) { 
this.state = 10;
}else if((_item.getTitle()).equals("Remove all KeyValueStore records")) { 
this.state = 12;
}else if((_item.getTitle()).equals("Restart board")) { 
this.state = 18;
}if (true) break;

case 6:
//C
this.state = 67;
 //BA.debugLineNum = 557;BA.debugLine="ShowAboutMenu";
_showaboutmenu();
 if (true) break;

case 8:
//C
this.state = 67;
 //BA.debugLineNum = 559;BA.debugLine="StartActivity(screen.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent._screen.CreateIntent()));
 if (true) break;

case 10:
//C
this.state = 67;
 //BA.debugLineNum = 561;BA.debugLine="Msgbox(Starter.kvs.ListKeys.Size & \" records\",\"";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence(BA.NumberToString(parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._listkeys /*anywheresoftware.b4a.objects.collections.List*/ ().getSize())+" records"),BA.ObjectToCharSequence("Smart Home Monitor"),mostCurrent.activityBA);
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 563;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 564;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets,";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 565;BA.debugLine="If  Msgbox2(\"Remove all KeyValueStore records?\"";
if (true) break;

case 13:
//if
this.state = 16;
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Remove all KeyValueStore records?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 566;BA.debugLine="Starter.kvs.DeleteAll";
parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._deleteall /*String*/ ();
 //BA.debugLineNum = 569;BA.debugLine="clvActivity.Clear";
parent.mostCurrent._clvactivity._clear();
 //BA.debugLineNum = 571;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 572;BA.debugLine="wait for (rs) complete (Results As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 71;
return;
case 71:
//C
this.state = 16;
_results = (Object) result[0];
;
 //BA.debugLineNum = 575;BA.debugLine="Dim rs As ResumableSub = GetVideos(response)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getvideos(parent._response);
 //BA.debugLineNum = 576;BA.debugLine="wait for (rs) complete (Results As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 72;
return;
case 72:
//C
this.state = 16;
_results = (Object) result[0];
;
 //BA.debugLineNum = 578;BA.debugLine="tabStripCurrentPage = False";
parent._tabstripcurrentpage = anywheresoftware.b4a.keywords.Common.False;
 if (true) break;

case 16:
//C
this.state = 67;
;
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 581;BA.debugLine="Try";
if (true) break;

case 19:
//try
this.state = 66;
this.catchState = 65;
this.state = 21;
if (true) break;

case 21:
//C
this.state = 22;
this.catchState = 65;
 //BA.debugLineNum = 582;BA.debugLine="Dim Result As Int";
_result = 0;
 //BA.debugLineNum = 583;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 584;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets,";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 585;BA.debugLine="If TabStrip1.CurrentPage = 2 Then";
if (true) break;

case 22:
//if
this.state = 63;
if (parent.mostCurrent._tabstrip1.getCurrentPage()==2) { 
this.state = 24;
}else if(parent.mostCurrent._tabstrip1.getCurrentPage()==1) { 
this.state = 34;
}else if(parent.mostCurrent._tabstrip1.getCurrentPage()==3) { 
this.state = 44;
}else {
this.state = 54;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 586;BA.debugLine="Result = Msgbox2(\"Restart the BASEMENT contro";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 587;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 25:
//if
this.state = 32;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 588;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (true) break;

case 28:
//if
this.state = 31;
if (parent._mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || parent._mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 30;
}if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 589;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 31:
//C
this.state = 32;
;
 //BA.debugLineNum = 591;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringT";
parent._mqtt.Publish("TempHumidBasement",parent._bc.StringToBytes("Restart controller","utf8"));
 if (true) break;

case 32:
//C
this.state = 63;
;
 if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 594;BA.debugLine="Result = Msgbox2(\"Restart the AIR QUALITY con";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 595;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 35:
//if
this.state = 42;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 37;
}if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 596;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (true) break;

case 38:
//if
this.state = 41;
if (parent._mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || parent._mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 40;
}if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 597;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 41:
//C
this.state = 42;
;
 //BA.debugLineNum = 599;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Restar";
parent._mqtt.Publish("MQ7",parent._bc.StringToBytes("Restart controller","utf8"));
 if (true) break;

case 42:
//C
this.state = 63;
;
 if (true) break;

case 44:
//C
this.state = 45;
 //BA.debugLineNum = 602;BA.debugLine="Result = Msgbox2(\"Restart the BASEMENT AIR QU";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the BASEMENT AIR QUALITY controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 603;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 45:
//if
this.state = 52;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 47;
}if (true) break;

case 47:
//C
this.state = 48;
 //BA.debugLineNum = 604;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (true) break;

case 48:
//if
this.state = 51;
if (parent._mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || parent._mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 50;
}if (true) break;

case 50:
//C
this.state = 51;
 //BA.debugLineNum = 605;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 51:
//C
this.state = 52;
;
 //BA.debugLineNum = 607;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes";
parent._mqtt.Publish("MQ7Basement",parent._bc.StringToBytes("Restart controller","utf8"));
 if (true) break;

case 52:
//C
this.state = 63;
;
 if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 610;BA.debugLine="Result = Msgbox2(\"Restart the WEATHER control";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the WEATHER controller?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 611;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 55:
//if
this.state = 62;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 57;
}if (true) break;

case 57:
//C
this.state = 58;
 //BA.debugLineNum = 612;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connec";
if (true) break;

case 58:
//if
this.state = 61;
if (parent._mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || parent._mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 60;
}if (true) break;

case 60:
//C
this.state = 61;
 //BA.debugLineNum = 613;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 if (true) break;

case 61:
//C
this.state = 62;
;
 //BA.debugLineNum = 615;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"";
parent._mqtt.Publish("TempHumid",parent._bc.StringToBytes("Restart controller","utf8"));
 if (true) break;

case 62:
//C
this.state = 63;
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
this.catchState = 69;
 //BA.debugLineNum = 619;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("41179713",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 66:
//C
this.state = 67;
this.catchState = 69;
;
 if (true) break;

case 67:
//C
this.state = 70;
;
 if (true) break;

case 69:
//C
this.state = 70;
this.catchState = 0;
 //BA.debugLineNum = 623;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("41179717",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 70:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 625;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1933;BA.debugLine="Private Sub Blur (bmp As B4XBitmap) As B4XBitmap";
 //BA.debugLineNum = 1934;BA.debugLine="Dim bcBitmap As BitmapCreator";
_bcbitmap = new b4a.example.bitmapcreator();
 //BA.debugLineNum = 1935;BA.debugLine="Dim ReduceScale As Int = 2";
_reducescale = (int) (2);
 //BA.debugLineNum = 1936;BA.debugLine="bcBitmap.Initialize(bmp.Width / ReduceScale / bmp";
_bcbitmap._initialize(processBA,(int) (_bmp.getWidth()/(double)_reducescale/(double)_bmp.getScale()),(int) (_bmp.getHeight()/(double)_reducescale/(double)_bmp.getScale()));
 //BA.debugLineNum = 1937;BA.debugLine="bcBitmap.CopyPixelsFromBitmap(bmp)";
_bcbitmap._copypixelsfrombitmap(_bmp);
 //BA.debugLineNum = 1938;BA.debugLine="Dim count As Int = 3";
_count = (int) (3);
 //BA.debugLineNum = 1939;BA.debugLine="Dim clrs(3) As ARGBColor";
_clrs = new b4a.example.bitmapcreator._argbcolor[(int) (3)];
{
int d0 = _clrs.length;
for (int i0 = 0;i0 < d0;i0++) {
_clrs[i0] = new b4a.example.bitmapcreator._argbcolor();
}
}
;
 //BA.debugLineNum = 1940;BA.debugLine="Dim temp As ARGBColor";
_temp = new b4a.example.bitmapcreator._argbcolor();
 //BA.debugLineNum = 1941;BA.debugLine="Dim m As Int";
_m = 0;
 //BA.debugLineNum = 1942;BA.debugLine="For steps = 1 To count";
{
final int step9 = 1;
final int limit9 = _count;
_steps = (int) (1) ;
for (;_steps <= limit9 ;_steps = _steps + step9 ) {
 //BA.debugLineNum = 1943;BA.debugLine="For y = 0 To bcBitmap.mHeight - 1";
{
final int step10 = 1;
final int limit10 = (int) (_bcbitmap._mheight-1);
_y = (int) (0) ;
for (;_y <= limit10 ;_y = _y + step10 ) {
 //BA.debugLineNum = 1944;BA.debugLine="For x = 0 To 2";
{
final int step11 = 1;
final int limit11 = (int) (2);
_x = (int) (0) ;
for (;_x <= limit11 ;_x = _x + step11 ) {
 //BA.debugLineNum = 1945;BA.debugLine="bcBitmap.GetARGB(x, y, clrs(x))";
_bcbitmap._getargb(_x,_y,_clrs[_x]);
 }
};
 //BA.debugLineNum = 1947;BA.debugLine="SetAvg(bcBitmap, 1, y, clrs, temp)";
_setavg(_bcbitmap,(int) (1),_y,_clrs,_temp);
 //BA.debugLineNum = 1948;BA.debugLine="m = 0";
_m = (int) (0);
 //BA.debugLineNum = 1949;BA.debugLine="For x = 2 To bcBitmap.mWidth - 2";
{
final int step16 = 1;
final int limit16 = (int) (_bcbitmap._mwidth-2);
_x = (int) (2) ;
for (;_x <= limit16 ;_x = _x + step16 ) {
 //BA.debugLineNum = 1950;BA.debugLine="bcBitmap.GetARGB(x + 1, y, clrs(m))";
_bcbitmap._getargb((int) (_x+1),_y,_clrs[_m]);
 //BA.debugLineNum = 1951;BA.debugLine="m = (m + 1) Mod clrs.Length";
_m = (int) ((_m+1)%_clrs.length);
 //BA.debugLineNum = 1952;BA.debugLine="SetAvg(bcBitmap, x, y, clrs, temp)";
_setavg(_bcbitmap,_x,_y,_clrs,_temp);
 }
};
 }
};
 //BA.debugLineNum = 1955;BA.debugLine="For x = 0 To bcBitmap.mWidth - 1";
{
final int step22 = 1;
final int limit22 = (int) (_bcbitmap._mwidth-1);
_x = (int) (0) ;
for (;_x <= limit22 ;_x = _x + step22 ) {
 //BA.debugLineNum = 1956;BA.debugLine="For y = 0 To 2";
{
final int step23 = 1;
final int limit23 = (int) (2);
_y = (int) (0) ;
for (;_y <= limit23 ;_y = _y + step23 ) {
 //BA.debugLineNum = 1957;BA.debugLine="bcBitmap.GetARGB(x, y, clrs(y))";
_bcbitmap._getargb(_x,_y,_clrs[_y]);
 }
};
 //BA.debugLineNum = 1959;BA.debugLine="SetAvg(bcBitmap, x, 1, clrs, temp)";
_setavg(_bcbitmap,_x,(int) (1),_clrs,_temp);
 //BA.debugLineNum = 1960;BA.debugLine="m = 0";
_m = (int) (0);
 //BA.debugLineNum = 1961;BA.debugLine="For y = 2 To bcBitmap.mHeight - 2";
{
final int step28 = 1;
final int limit28 = (int) (_bcbitmap._mheight-2);
_y = (int) (2) ;
for (;_y <= limit28 ;_y = _y + step28 ) {
 //BA.debugLineNum = 1962;BA.debugLine="bcBitmap.GetARGB(x, y + 1, clrs(m))";
_bcbitmap._getargb(_x,(int) (_y+1),_clrs[_m]);
 //BA.debugLineNum = 1963;BA.debugLine="m = (m + 1) Mod clrs.Length";
_m = (int) ((_m+1)%_clrs.length);
 //BA.debugLineNum = 1964;BA.debugLine="SetAvg(bcBitmap, x, y, clrs, temp)";
_setavg(_bcbitmap,_x,_y,_clrs,_temp);
 }
};
 }
};
 }
};
 //BA.debugLineNum = 1968;BA.debugLine="Return bcBitmap.Bitmap";
if (true) return _bcbitmap._getbitmap();
 //BA.debugLineNum = 1969;BA.debugLine="End Sub";
return null;
}
public static String  _bluriv(String _image,anywheresoftware.b4a.objects.ImageViewWrapper _iv) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _bmp = null;
 //BA.debugLineNum = 1923;BA.debugLine="Sub BlurIV (image As String,iv As ImageView)";
 //BA.debugLineNum = 1924;BA.debugLine="Try";
try { //BA.debugLineNum = 1925;BA.debugLine="Dim bmp As B4XBitmap = xui.LoadBitmapResize(File";
_bmp = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
_bmp = mostCurrent._xui.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_image,_iv.getWidth(),_iv.getHeight(),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1926;BA.debugLine="iv.Bitmap = (Blur(bmp))";
_iv.setBitmap((android.graphics.Bitmap)((_blur(_bmp)).getObject()));
 } 
       catch (Exception e5) {
			processBA.setLastException(e5); //BA.debugLineNum = 1928;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("43407877",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1931;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1889;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 1890;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"0";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 1891;BA.debugLine="If Msgbox2(\"Capture new camera thumbnails?\", \"Sma";
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
 //BA.debugLineNum = 1892;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 1893;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1894;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1895;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1896;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1897;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1898;BA.debugLine="btnDrivewayFullScreenshot.Enabled = False";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1899;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = False";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1900;BA.debugLine="btnSideYardFullScreenshot.Enabled = False";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1901;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1902;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1903;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1904;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",parent.mostCurrent._ivdriveway);
 //BA.debugLineNum = 1905;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",parent.mostCurrent._ivfrontdoor);
 //BA.debugLineNum = 1906;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",parent.mostCurrent._ivsideyard);
 //BA.debugLineNum = 1907;BA.debugLine="Dim rs As ResumableSub = RefreshCameras(False)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _refreshcameras(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1908;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 5;
return;
case 5:
//C
this.state = 4;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1909;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1910;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1911;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1912;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1913;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1914;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1915;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1916;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1917;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1918;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1919;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1921;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _btndrivewayfullscreenshot_click() throws Exception{
ResumableSub_btnDrivewayFullScreenshot_Click rsub = new ResumableSub_btnDrivewayFullScreenshot_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnDrivewayFullScreenshot_Click extends BA.ResumableSub {
public ResumableSub_btnDrivewayFullScreenshot_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
int _i = 0;
int step2;
int limit2;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2890;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia-s";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/347574");
 //BA.debugLineNum = 2891;BA.debugLine="For i = 1 To 20";
if (true) break;

case 1:
//for
this.state = 10;
step2 = 1;
limit2 = (int) (20);
_i = (int) (1) ;
this.state = 15;
if (true) break;

case 15:
//C
this.state = 10;
if ((step2 > 0 && _i <= limit2) || (step2 < 0 && _i >= limit2)) this.state = 3;
if (true) break;

case 16:
//C
this.state = 15;
_i = ((int)(0 + _i + step2)) ;
if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2892;BA.debugLine="If response = \"\" Then";
if (true) break;

case 4:
//if
this.state = 9;
if ((parent._response).equals("")) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 2893;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 17;
return;
case 17:
//C
this.state = 9;
;
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 2895;BA.debugLine="Exit";
this.state = 10;
if (true) break;
 if (true) break;

case 9:
//C
this.state = 16;
;
 if (true) break;
if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 2898;BA.debugLine="GetCameraInfo(response)";
_getcamerainfo(parent._response);
 //BA.debugLineNum = 2899;BA.debugLine="DownloadImageFullscreen(\"https://rest-\" & userReg";
_downloadimagefullscreen("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg","347574");
 //BA.debugLineNum = 2900;BA.debugLine="If response.StartsWith(\"ERROR: \") = False Then";
if (true) break;

case 11:
//if
this.state = 14;
if (parent._response.startsWith("ERROR: ")==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 13;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 2901;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 2903;BA.debugLine="End Sub";
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
int _i = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
int step18;
int limit18;
int step42;
int limit42;
int step49;
int limit49;
int step52;
int limit52;

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
 //BA.debugLineNum = 2574;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 2575;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"0";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 2576;BA.debugLine="If Msgbox2(\"Capture a new video from the Driveway";
if (true) break;

case 1:
//if
this.state = 46;
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Capture a new video from the Driveway camera?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2577;BA.debugLine="Try";
if (true) break;

case 4:
//try
this.state = 45;
this.catchState = 44;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 7;
this.catchState = 44;
 //BA.debugLineNum = 2578;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2579;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2580;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2581;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2582;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2583;BA.debugLine="btnDrivewayFullScreenshot.Enabled = False";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2584;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = False";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2585;BA.debugLine="btnSideYardFullScreenshot.Enabled = False";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2586;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2587;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2588;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2589;BA.debugLine="lblStatus.Text = \"Capturing a new Driveway vide";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Driveway video clip..."));
 //BA.debugLineNum = 2590;BA.debugLine="RESTPost(\"https://rest-\" & userRegion & \".immed";
_restpost("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/347574/clip");
 //BA.debugLineNum = 2591;BA.debugLine="For i = 1 To 20";
if (true) break;

case 7:
//for
this.state = 16;
step18 = 1;
limit18 = (int) (20);
_i = (int) (1) ;
this.state = 47;
if (true) break;

case 47:
//C
this.state = 16;
if ((step18 > 0 && _i <= limit18) || (step18 < 0 && _i >= limit18)) this.state = 9;
if (true) break;

case 48:
//C
this.state = 47;
_i = ((int)(0 + _i + step18)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 2592;BA.debugLine="If response = \"\" Then";
if (true) break;

case 10:
//if
this.state = 15;
if ((parent._response).equals("")) { 
this.state = 12;
}else {
this.state = 14;
}if (true) break;

case 12:
//C
this.state = 15;
 //BA.debugLineNum = 2593;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 49;
return;
case 49:
//C
this.state = 15;
;
 if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 2595;BA.debugLine="Exit";
this.state = 16;
if (true) break;
 if (true) break;

case 15:
//C
this.state = 48;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 2598;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.C";

case 16:
//if
this.state = 19;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 2599;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2600;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2601;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2602;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2603;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2604;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2605;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2606;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2607;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2608;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2609;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2610;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 //BA.debugLineNum = 2611;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 19:
//C
this.state = 20;
;
 //BA.debugLineNum = 2613;BA.debugLine="GetCommandID(response)";
_getcommandid(parent._response);
 //BA.debugLineNum = 2614;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 2615;BA.debugLine="For i = 1 To 20";
if (true) break;

case 20:
//for
this.state = 29;
step42 = 1;
limit42 = (int) (20);
_i = (int) (1) ;
this.state = 50;
if (true) break;

case 50:
//C
this.state = 29;
if ((step42 > 0 && _i <= limit42) || (step42 < 0 && _i >= limit42)) this.state = 22;
if (true) break;

case 51:
//C
this.state = 50;
_i = ((int)(0 + _i + step42)) ;
if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 2616;BA.debugLine="If response = \"\" Then";
if (true) break;

case 23:
//if
this.state = 28;
if ((parent._response).equals("")) { 
this.state = 25;
}else {
this.state = 27;
}if (true) break;

case 25:
//C
this.state = 28;
 //BA.debugLineNum = 2617;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 52;
return;
case 52:
//C
this.state = 28;
;
 if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 2619;BA.debugLine="Exit";
this.state = 29;
if (true) break;
 if (true) break;

case 28:
//C
this.state = 51;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 2622;BA.debugLine="For i = 1 To 70";

case 29:
//for
this.state = 42;
step49 = 1;
limit49 = (int) (70);
_i = (int) (1) ;
this.state = 53;
if (true) break;

case 53:
//C
this.state = 42;
if ((step49 > 0 && _i <= limit49) || (step49 < 0 && _i >= limit49)) this.state = 31;
if (true) break;

case 54:
//C
this.state = 53;
_i = ((int)(0 + _i + step49)) ;
if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 2623;BA.debugLine="GetCommandStatus(response)";
_getcommandstatus(parent._response);
 //BA.debugLineNum = 2624;BA.debugLine="If commandComplete Then";
if (true) break;

case 32:
//if
this.state = 41;
if (parent._commandcomplete) { 
this.state = 34;
}else {
this.state = 40;
}if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 2625;BA.debugLine="For i = 3 To 1 Step -1";
if (true) break;

case 35:
//for
this.state = 38;
step52 = -1;
limit52 = (int) (1);
_i = (int) (3) ;
this.state = 55;
if (true) break;

case 55:
//C
this.state = 38;
if ((step52 > 0 && _i <= limit52) || (step52 < 0 && _i >= limit52)) this.state = 37;
if (true) break;

case 56:
//C
this.state = 55;
_i = ((int)(0 + _i + step52)) ;
if (true) break;

case 37:
//C
this.state = 56;
 //BA.debugLineNum = 2626;BA.debugLine="lblStatus.Text = \"New Driveway video clip wi";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("New Driveway video clip will be shown in "+BA.NumberToString(_i)+" seconds..."));
 //BA.debugLineNum = 2627;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 57;
return;
case 57:
//C
this.state = 56;
;
 if (true) break;
if (true) break;

case 38:
//C
this.state = 41;
;
 //BA.debugLineNum = 2629;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 2630;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 58;
return;
case 58:
//C
this.state = 41;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2631;BA.debugLine="Exit";
this.state = 42;
if (true) break;
 if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 2633;BA.debugLine="lblStatus.Text = \"Awaiting for the Driveway v";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Driveway video clip... "+BA.NumberToString(_i)+"/70"));
 if (true) break;

case 41:
//C
this.state = 54;
;
 //BA.debugLineNum = 2635;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedi";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 2636;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 59;
return;
case 59:
//C
this.state = 54;
;
 if (true) break;
if (true) break;

case 42:
//C
this.state = 45;
;
 if (true) break;

case 44:
//C
this.state = 45;
this.catchState = 0;
 //BA.debugLineNum = 2639;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("44718658",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 45:
//C
this.state = 46;
this.catchState = 0;
;
 //BA.debugLineNum = 2641;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2642;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2643;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2644;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2645;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2646;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2647;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2648;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2649;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2650;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2651;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2652;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 46:
//C
this.state = -1;
;
 //BA.debugLineNum = 2654;BA.debugLine="End Sub";
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
public static void  _btnfrontdoorfullscreenshot_click() throws Exception{
ResumableSub_btnFrontDoorFullScreenshot_Click rsub = new ResumableSub_btnFrontDoorFullScreenshot_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnFrontDoorFullScreenshot_Click extends BA.ResumableSub {
public ResumableSub_btnFrontDoorFullScreenshot_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
int _i = 0;
int step2;
int limit2;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2906;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia-s";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/236967");
 //BA.debugLineNum = 2907;BA.debugLine="For i = 1 To 20";
if (true) break;

case 1:
//for
this.state = 10;
step2 = 1;
limit2 = (int) (20);
_i = (int) (1) ;
this.state = 15;
if (true) break;

case 15:
//C
this.state = 10;
if ((step2 > 0 && _i <= limit2) || (step2 < 0 && _i >= limit2)) this.state = 3;
if (true) break;

case 16:
//C
this.state = 15;
_i = ((int)(0 + _i + step2)) ;
if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2908;BA.debugLine="If response = \"\" Then";
if (true) break;

case 4:
//if
this.state = 9;
if ((parent._response).equals("")) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 2909;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 17;
return;
case 17:
//C
this.state = 9;
;
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 2911;BA.debugLine="Exit";
this.state = 10;
if (true) break;
 if (true) break;

case 9:
//C
this.state = 16;
;
 if (true) break;
if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 2914;BA.debugLine="GetCameraInfo(response)";
_getcamerainfo(parent._response);
 //BA.debugLineNum = 2915;BA.debugLine="DownloadImageFullscreen(\"https://rest-\" & userReg";
_downloadimagefullscreen("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg","236967");
 //BA.debugLineNum = 2916;BA.debugLine="If response.StartsWith(\"ERROR: \") = False Then";
if (true) break;

case 11:
//if
this.state = 14;
if (parent._response.startsWith("ERROR: ")==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 13;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 2917;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 2919;BA.debugLine="End Sub";
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
int _i = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
int step19;
int limit19;
int step43;
int limit43;
int step50;
int limit50;
int step53;
int limit53;

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
 //BA.debugLineNum = 2657;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 2658;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"0";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 2659;BA.debugLine="If Msgbox2(\"Capture a new video from the Front Do";
if (true) break;

case 1:
//if
this.state = 46;
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Capture a new video from the Front Door camera?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2660;BA.debugLine="Try";
if (true) break;

case 4:
//try
this.state = 45;
this.catchState = 44;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 7;
this.catchState = 44;
 //BA.debugLineNum = 2661;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 2662;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2663;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2664;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2665;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2666;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2667;BA.debugLine="btnDrivewayFullScreenshot.Enabled = False";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2668;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = False";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2669;BA.debugLine="btnSideYardFullScreenshot.Enabled = False";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2670;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2671;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2672;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2673;BA.debugLine="lblStatus.Text = \"Capturing a new Front Door vi";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Front Door video clip..."));
 //BA.debugLineNum = 2674;BA.debugLine="RESTPost(\"https://rest-\" & userRegion & \".immed";
_restpost("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/236967/clip");
 //BA.debugLineNum = 2675;BA.debugLine="For i = 1 To 20";
if (true) break;

case 7:
//for
this.state = 16;
step19 = 1;
limit19 = (int) (20);
_i = (int) (1) ;
this.state = 47;
if (true) break;

case 47:
//C
this.state = 16;
if ((step19 > 0 && _i <= limit19) || (step19 < 0 && _i >= limit19)) this.state = 9;
if (true) break;

case 48:
//C
this.state = 47;
_i = ((int)(0 + _i + step19)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 2676;BA.debugLine="If response = \"\" Then";
if (true) break;

case 10:
//if
this.state = 15;
if ((parent._response).equals("")) { 
this.state = 12;
}else {
this.state = 14;
}if (true) break;

case 12:
//C
this.state = 15;
 //BA.debugLineNum = 2677;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 49;
return;
case 49:
//C
this.state = 15;
;
 if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 2679;BA.debugLine="Exit";
this.state = 16;
if (true) break;
 if (true) break;

case 15:
//C
this.state = 48;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 2682;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.C";

case 16:
//if
this.state = 19;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 2683;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2684;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2685;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2686;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2687;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2688;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2689;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2690;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2691;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2692;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2693;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2694;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 //BA.debugLineNum = 2695;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 19:
//C
this.state = 20;
;
 //BA.debugLineNum = 2697;BA.debugLine="GetCommandID(response)";
_getcommandid(parent._response);
 //BA.debugLineNum = 2698;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 2699;BA.debugLine="For i = 1 To 20";
if (true) break;

case 20:
//for
this.state = 29;
step43 = 1;
limit43 = (int) (20);
_i = (int) (1) ;
this.state = 50;
if (true) break;

case 50:
//C
this.state = 29;
if ((step43 > 0 && _i <= limit43) || (step43 < 0 && _i >= limit43)) this.state = 22;
if (true) break;

case 51:
//C
this.state = 50;
_i = ((int)(0 + _i + step43)) ;
if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 2700;BA.debugLine="If response = \"\" Then";
if (true) break;

case 23:
//if
this.state = 28;
if ((parent._response).equals("")) { 
this.state = 25;
}else {
this.state = 27;
}if (true) break;

case 25:
//C
this.state = 28;
 //BA.debugLineNum = 2701;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 52;
return;
case 52:
//C
this.state = 28;
;
 if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 2703;BA.debugLine="Exit";
this.state = 29;
if (true) break;
 if (true) break;

case 28:
//C
this.state = 51;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 2706;BA.debugLine="For i = 1 To 70";

case 29:
//for
this.state = 42;
step50 = 1;
limit50 = (int) (70);
_i = (int) (1) ;
this.state = 53;
if (true) break;

case 53:
//C
this.state = 42;
if ((step50 > 0 && _i <= limit50) || (step50 < 0 && _i >= limit50)) this.state = 31;
if (true) break;

case 54:
//C
this.state = 53;
_i = ((int)(0 + _i + step50)) ;
if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 2707;BA.debugLine="GetCommandStatus(response)";
_getcommandstatus(parent._response);
 //BA.debugLineNum = 2708;BA.debugLine="If commandComplete Then";
if (true) break;

case 32:
//if
this.state = 41;
if (parent._commandcomplete) { 
this.state = 34;
}else {
this.state = 40;
}if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 2709;BA.debugLine="For i = 3 To 1  Step -1";
if (true) break;

case 35:
//for
this.state = 38;
step53 = -1;
limit53 = (int) (1);
_i = (int) (3) ;
this.state = 55;
if (true) break;

case 55:
//C
this.state = 38;
if ((step53 > 0 && _i <= limit53) || (step53 < 0 && _i >= limit53)) this.state = 37;
if (true) break;

case 56:
//C
this.state = 55;
_i = ((int)(0 + _i + step53)) ;
if (true) break;

case 37:
//C
this.state = 56;
 //BA.debugLineNum = 2710;BA.debugLine="lblStatus.Text = \"New Front Door video clip";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("New Front Door video clip will be shown in "+BA.NumberToString(_i)+" seconds..."));
 //BA.debugLineNum = 2711;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 57;
return;
case 57:
//C
this.state = 56;
;
 if (true) break;
if (true) break;

case 38:
//C
this.state = 41;
;
 //BA.debugLineNum = 2713;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 2714;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 58;
return;
case 58:
//C
this.state = 41;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2715;BA.debugLine="Exit";
this.state = 42;
if (true) break;
 if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 2717;BA.debugLine="lblStatus.Text = \"Awaiting for the Front Door";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Front Door video clip... "+BA.NumberToString(_i)+"/70"));
 if (true) break;

case 41:
//C
this.state = 54;
;
 //BA.debugLineNum = 2719;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedi";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 2720;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 59;
return;
case 59:
//C
this.state = 54;
;
 if (true) break;
if (true) break;

case 42:
//C
this.state = 45;
;
 if (true) break;

case 44:
//C
this.state = 45;
this.catchState = 0;
 //BA.debugLineNum = 2723;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("44784195",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 45:
//C
this.state = 46;
this.catchState = 0;
;
 //BA.debugLineNum = 2725;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2726;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2727;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2728;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2729;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2730;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2731;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2732;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2733;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2734;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2735;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2736;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 46:
//C
this.state = -1;
;
 //BA.debugLineNum = 2738;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 2825;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2826;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2827;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2828;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2829;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2830;BA.debugLine="btnDrivewayFullScreenshot.Enabled = False";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2831;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = False";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2832;BA.debugLine="btnSideYardFullScreenshot.Enabled = False";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2833;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2834;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2835;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2836;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 2837;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",parent.mostCurrent._ivdriveway);
 //BA.debugLineNum = 2838;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",parent.mostCurrent._ivfrontdoor);
 //BA.debugLineNum = 2839;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",parent.mostCurrent._ivsideyard);
 //BA.debugLineNum = 2840;BA.debugLine="Dim rs As ResumableSub = RefreshCameras(True)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _refreshcameras(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2841;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 1;
return;
case 1:
//C
this.state = -1;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2842;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2843;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2844;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2845;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2846;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2847;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2848;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2849;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2850;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2851;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2852;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2853;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _btnsideyardfullscreenshot_click() throws Exception{
ResumableSub_btnSideYardFullScreenshot_Click rsub = new ResumableSub_btnSideYardFullScreenshot_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnSideYardFullScreenshot_Click extends BA.ResumableSub {
public ResumableSub_btnSideYardFullScreenshot_Click(cloyd.smart.home.monitor.main parent) {
this.parent = parent;
}
cloyd.smart.home.monitor.main parent;
int _i = 0;
int step2;
int limit2;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2922;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia-s";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/226821");
 //BA.debugLineNum = 2923;BA.debugLine="For i = 1 To 20";
if (true) break;

case 1:
//for
this.state = 10;
step2 = 1;
limit2 = (int) (20);
_i = (int) (1) ;
this.state = 15;
if (true) break;

case 15:
//C
this.state = 10;
if ((step2 > 0 && _i <= limit2) || (step2 < 0 && _i >= limit2)) this.state = 3;
if (true) break;

case 16:
//C
this.state = 15;
_i = ((int)(0 + _i + step2)) ;
if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2924;BA.debugLine="If response = \"\" Then";
if (true) break;

case 4:
//if
this.state = 9;
if ((parent._response).equals("")) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 2925;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 17;
return;
case 17:
//C
this.state = 9;
;
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 2927;BA.debugLine="Exit";
this.state = 10;
if (true) break;
 if (true) break;

case 9:
//C
this.state = 16;
;
 if (true) break;
if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 2930;BA.debugLine="GetCameraInfo(response)";
_getcamerainfo(parent._response);
 //BA.debugLineNum = 2931;BA.debugLine="DownloadImageFullscreen(\"https://rest-\" & userReg";
_downloadimagefullscreen("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg","226821");
 //BA.debugLineNum = 2932;BA.debugLine="If response.StartsWith(\"ERROR: \") = False Then";
if (true) break;

case 11:
//if
this.state = 14;
if (parent._response.startsWith("ERROR: ")==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 13;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 2933;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 2935;BA.debugLine="End Sub";
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
int _i = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
int step19;
int limit19;
int step43;
int limit43;
int step50;
int limit50;
int step53;
int limit53;

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
 //BA.debugLineNum = 2741;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 2742;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"0";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 2743;BA.debugLine="If Msgbox2(\"Capture a new video from the Side Yar";
if (true) break;

case 1:
//if
this.state = 46;
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Capture a new video from the Side Yard camera?"),BA.ObjectToCharSequence("Smart Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2744;BA.debugLine="Try";
if (true) break;

case 4:
//try
this.state = 45;
this.catchState = 44;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 7;
this.catchState = 44;
 //BA.debugLineNum = 2745;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 2746;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2747;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2748;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2749;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2750;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2751;BA.debugLine="btnDrivewayFullScreenshot.Enabled = False";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2752;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = False";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2753;BA.debugLine="btnSideYardFullScreenshot.Enabled = False";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2754;BA.debugLine="ivDriveway.Enabled = False";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2755;BA.debugLine="ivFrontDoor.Enabled = False";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2756;BA.debugLine="ivSideYard.Enabled = False";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2757;BA.debugLine="lblStatus.Text = \"Capturing a new Side Yard vid";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Side Yard video clip..."));
 //BA.debugLineNum = 2758;BA.debugLine="RESTPost(\"https://rest-\" & userRegion & \".immed";
_restpost("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/226821/clip");
 //BA.debugLineNum = 2759;BA.debugLine="For i = 1 To 20";
if (true) break;

case 7:
//for
this.state = 16;
step19 = 1;
limit19 = (int) (20);
_i = (int) (1) ;
this.state = 47;
if (true) break;

case 47:
//C
this.state = 16;
if ((step19 > 0 && _i <= limit19) || (step19 < 0 && _i >= limit19)) this.state = 9;
if (true) break;

case 48:
//C
this.state = 47;
_i = ((int)(0 + _i + step19)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 2760;BA.debugLine="If response = \"\" Then";
if (true) break;

case 10:
//if
this.state = 15;
if ((parent._response).equals("")) { 
this.state = 12;
}else {
this.state = 14;
}if (true) break;

case 12:
//C
this.state = 15;
 //BA.debugLineNum = 2761;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 49;
return;
case 49:
//C
this.state = 15;
;
 if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 2763;BA.debugLine="Exit";
this.state = 16;
if (true) break;
 if (true) break;

case 15:
//C
this.state = 48;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 2766;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.C";

case 16:
//if
this.state = 19;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 2767;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2768;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2769;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2770;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2771;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2772;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2773;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2774;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2775;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2776;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2777;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2778;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 //BA.debugLineNum = 2779;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 19:
//C
this.state = 20;
;
 //BA.debugLineNum = 2781;BA.debugLine="GetCommandID(response)";
_getcommandid(parent._response);
 //BA.debugLineNum = 2782;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 2783;BA.debugLine="For i = 1 To 20";
if (true) break;

case 20:
//for
this.state = 29;
step43 = 1;
limit43 = (int) (20);
_i = (int) (1) ;
this.state = 50;
if (true) break;

case 50:
//C
this.state = 29;
if ((step43 > 0 && _i <= limit43) || (step43 < 0 && _i >= limit43)) this.state = 22;
if (true) break;

case 51:
//C
this.state = 50;
_i = ((int)(0 + _i + step43)) ;
if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 2784;BA.debugLine="If response = \"\" Then";
if (true) break;

case 23:
//if
this.state = 28;
if ((parent._response).equals("")) { 
this.state = 25;
}else {
this.state = 27;
}if (true) break;

case 25:
//C
this.state = 28;
 //BA.debugLineNum = 2785;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 52;
return;
case 52:
//C
this.state = 28;
;
 if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 2787;BA.debugLine="Exit";
this.state = 29;
if (true) break;
 if (true) break;

case 28:
//C
this.state = 51;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 2790;BA.debugLine="For i = 1 To 70";

case 29:
//for
this.state = 42;
step50 = 1;
limit50 = (int) (70);
_i = (int) (1) ;
this.state = 53;
if (true) break;

case 53:
//C
this.state = 42;
if ((step50 > 0 && _i <= limit50) || (step50 < 0 && _i >= limit50)) this.state = 31;
if (true) break;

case 54:
//C
this.state = 53;
_i = ((int)(0 + _i + step50)) ;
if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 2791;BA.debugLine="GetCommandStatus(response)";
_getcommandstatus(parent._response);
 //BA.debugLineNum = 2792;BA.debugLine="If commandComplete Then";
if (true) break;

case 32:
//if
this.state = 41;
if (parent._commandcomplete) { 
this.state = 34;
}else {
this.state = 40;
}if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 2793;BA.debugLine="For i = 3 To 1 Step -1";
if (true) break;

case 35:
//for
this.state = 38;
step53 = -1;
limit53 = (int) (1);
_i = (int) (3) ;
this.state = 55;
if (true) break;

case 55:
//C
this.state = 38;
if ((step53 > 0 && _i <= limit53) || (step53 < 0 && _i >= limit53)) this.state = 37;
if (true) break;

case 56:
//C
this.state = 55;
_i = ((int)(0 + _i + step53)) ;
if (true) break;

case 37:
//C
this.state = 56;
 //BA.debugLineNum = 2794;BA.debugLine="lblStatus.Text = \"New Side Yard video clip w";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("New Side Yard video clip will be shown in "+BA.NumberToString(_i)+" seconds..."));
 //BA.debugLineNum = 2795;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 57;
return;
case 57:
//C
this.state = 56;
;
 if (true) break;
if (true) break;

case 38:
//C
this.state = 41;
;
 //BA.debugLineNum = 2797;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 2798;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 58;
return;
case 58:
//C
this.state = 41;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2799;BA.debugLine="Exit";
this.state = 42;
if (true) break;
 if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 2801;BA.debugLine="lblStatus.Text = \"Awaiting for the Side Yard";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Side Yard video clip... "+BA.NumberToString(_i)+"/70"));
 if (true) break;

case 41:
//C
this.state = 54;
;
 //BA.debugLineNum = 2803;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedi";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 2804;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 59;
return;
case 59:
//C
this.state = 54;
;
 if (true) break;
if (true) break;

case 42:
//C
this.state = 45;
;
 if (true) break;

case 44:
//C
this.state = 45;
this.catchState = 0;
 //BA.debugLineNum = 2807;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("44849731",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 45:
//C
this.state = 46;
this.catchState = 0;
;
 //BA.debugLineNum = 2809;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2810;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2811;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2812;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2813;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2814;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2815;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2816;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2817;BA.debugLine="ivDriveway.Enabled = True";
parent.mostCurrent._ivdriveway.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2818;BA.debugLine="ivFrontDoor.Enabled = True";
parent.mostCurrent._ivfrontdoor.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2819;BA.debugLine="ivSideYard.Enabled = True";
parent.mostCurrent._ivsideyard.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2820;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 46:
//C
this.state = -1;
;
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
public static String  _checkairqualitysetting() throws Exception{
anywheresoftware.b4a.objects.CSBuilder _cs = null;
String _status = "";
String[] _a = null;
long _tomorrow = 0L;
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 845;BA.debugLine="Sub CheckAirQualitySetting";
 //BA.debugLineNum = 846;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 847;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 848;BA.debugLine="Try";
try { //BA.debugLineNum = 849;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 850;BA.debugLine="status = StateManager.GetSetting(\"AirQuality\")";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"AirQuality");
 //BA.debugLineNum = 851;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 852;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 853;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 854;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 855;BA.debugLine="GaugeAirQuality.CurrentValue = a(0)";
mostCurrent._gaugeairquality._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (0)])));
 //BA.debugLineNum = 856;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 857;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 859;BA.debugLine="lblAirQuality.Text = cs.Initialize.Bold.Appen";
mostCurrent._lblairquality.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 };
 //BA.debugLineNum = 861;BA.debugLine="If a(1) = \"\" Then";
if ((_a[(int) (1)]).equals("")) { 
 //BA.debugLineNum = 862;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 863;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 864;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 865;BA.debugLine="a(1) = DateTime.Date(Tomorrow)";
_a[(int) (1)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 867;BA.debugLine="If a(2).Contains(\"|24:\") Then";
if (_a[(int) (2)].contains("|24:")) { 
 //BA.debugLineNum = 868;BA.debugLine="a(2) = a(2).Replace(\"|24:\",\"|00:\")";
_a[(int) (2)] = _a[(int) (2)].replace("|24:","|00:");
 //BA.debugLineNum = 869;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 870;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 871;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 872;BA.debugLine="a(2) = DateTime.Date(Tomorrow)";
_a[(int) (2)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 874;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 875;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 876;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 877;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 878;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 880;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 881;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 883;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 886;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.B";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e44) {
			processBA.setLastException(e44); //BA.debugLineNum = 890;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("41835053",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 891;BA.debugLine="lblAirQualityLastUpdate.Text = cs.Initialize.Bol";
mostCurrent._lblairqualitylastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 893;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 895;BA.debugLine="Sub CheckAirQualitySettingBasement";
 //BA.debugLineNum = 896;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 897;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 898;BA.debugLine="Try";
try { //BA.debugLineNum = 899;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 900;BA.debugLine="status = StateManager.GetSetting(\"AirQualityBase";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"AirQualityBasement");
 //BA.debugLineNum = 901;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 902;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 903;BA.debugLine="If a.Length = 3 Then";
if (_a.length==3) { 
 //BA.debugLineNum = 904;BA.debugLine="If IsNumber(a(0)) And a(0) > 0 Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_a[(int) (0)]) && (double)(Double.parseDouble(_a[(int) (0)]))>0) { 
 //BA.debugLineNum = 905;BA.debugLine="GaugeAirQualityBasement.CurrentValue = a(0)";
mostCurrent._gaugeairqualitybasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (0)])));
 //BA.debugLineNum = 906;BA.debugLine="If a(0) > 400 Then";
if ((double)(Double.parseDouble(_a[(int) (0)]))>400) { 
 //BA.debugLineNum = 907;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bo";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 909;BA.debugLine="lblAirQualityBasement.Text = cs.Initialize.Bo";
mostCurrent._lblairqualitybasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Air Quality: ")).Pop().Append(BA.ObjectToCharSequence(_getairquality((int)(Double.parseDouble(_a[(int) (0)]))))).PopAll().getObject()));
 };
 //BA.debugLineNum = 911;BA.debugLine="If a(1) = \"\" Then";
if ((_a[(int) (1)]).equals("")) { 
 //BA.debugLineNum = 912;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 913;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 914;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 915;BA.debugLine="a(1) = DateTime.Date(Tomorrow)";
_a[(int) (1)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 917;BA.debugLine="If a(2).Contains(\"|24:\") Then";
if (_a[(int) (2)].contains("|24:")) { 
 //BA.debugLineNum = 918;BA.debugLine="a(2) = a(2).Replace(\"|24:\",\"|00:\")";
_a[(int) (2)] = _a[(int) (2)].replace("|24:","|00:");
 //BA.debugLineNum = 919;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 920;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 921;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 922;BA.debugLine="a(2) = DateTime.Date(Tomorrow)";
_a[(int) (2)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 925;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 926;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(1) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (1)]+" "+_a[(int) (2)]+" GMT");
 //BA.debugLineNum = 927;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 928;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 929;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 931;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 932;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Ini";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 934;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Ini";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (2)]).equals("00:00:00")) { 
 //BA.debugLineNum = 937;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Init";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lblairqualitylastupdatebasement.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e44) {
			processBA.setLastException(e44); //BA.debugLineNum = 941;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("41900590",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 942;BA.debugLine="lblAirQualityLastUpdateBasement.Text = cs.Initia";
mostCurrent._lblairqualitylastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 944;BA.debugLine="End Sub";
return "";
}
public static String  _checkbattlife(int _battlevel,anywheresoftware.b4a.objects.LabelWrapper _lbl) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
float _radius = 0f;
float _dx = 0f;
float _dy = 0f;
 //BA.debugLineNum = 1812;BA.debugLine="Sub CheckBattLife(battlevel As Int, lbl As Label)";
 //BA.debugLineNum = 1813;BA.debugLine="Try";
try { //BA.debugLineNum = 1815;BA.debugLine="Dim jo As JavaObject = lbl";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 1816;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Float";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1817;BA.debugLine="If battlevel <= 136 Then";
if (_battlevel<=136) { 
 //BA.debugLineNum = 1818;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 //BA.debugLineNum = 1819;BA.debugLine="Return \"Replace battery now!\"";
if (true) return "Replace battery now!";
 }else if(_battlevel>=160) { 
 //BA.debugLineNum = 1821;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Green)});
 //BA.debugLineNum = 1822;BA.debugLine="Return \"Very high\"";
if (true) return "Very high";
 }else if(_battlevel>136 && _battlevel<140) { 
 //BA.debugLineNum = 1824;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Yellow)});
 //BA.debugLineNum = 1825;BA.debugLine="Return \"Very low\"";
if (true) return "Very low";
 }else {
 //BA.debugLineNum = 1827;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Green)});
 //BA.debugLineNum = 1828;BA.debugLine="Return \"High\"";
if (true) return "High";
 };
 } 
       catch (Exception e18) {
			processBA.setLastException(e18); //BA.debugLineNum = 1831;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("43080211",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1833;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 1835;BA.debugLine="End Sub";
return "";
}
public static String  _checklfrlevel(int _lfrlevel) throws Exception{
 //BA.debugLineNum = 1837;BA.debugLine="Sub CheckLFRLevel(lfrlevel As Int) As String";
 //BA.debugLineNum = 1838;BA.debugLine="Try";
try { //BA.debugLineNum = 1840;BA.debugLine="If lfrlevel > -67 Then";
if (_lfrlevel>-67) { 
 //BA.debugLineNum = 1841;BA.debugLine="Return \"Amazing\"";
if (true) return "Amazing";
 }else if(_lfrlevel>-70 && _lfrlevel<=-67) { 
 //BA.debugLineNum = 1843;BA.debugLine="Return \"Very good\"";
if (true) return "Very good";
 }else if(_lfrlevel>-80 && _lfrlevel<=-70) { 
 //BA.debugLineNum = 1845;BA.debugLine="Return \"OK\"";
if (true) return "OK";
 }else if(_lfrlevel>-90 && _lfrlevel<=-80) { 
 //BA.debugLineNum = 1847;BA.debugLine="Return \"Not Good\"";
if (true) return "Not Good";
 }else {
 //BA.debugLineNum = 1849;BA.debugLine="Return \"Unusable\"";
if (true) return "Unusable";
 };
 } 
       catch (Exception e14) {
			processBA.setLastException(e14); //BA.debugLineNum = 1852;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("43145743",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1853;BA.debugLine="lblStatus.Text = \"CheckLFRLevel LastException: \"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("CheckLFRLevel LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))));
 //BA.debugLineNum = 1854;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 1856;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 946;BA.debugLine="Sub CheckTempHumiditySetting";
 //BA.debugLineNum = 947;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 948;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 949;BA.debugLine="Try";
try { //BA.debugLineNum = 950;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 951;BA.debugLine="status = StateManager.GetSetting(\"TempHumidity\")";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"TempHumidity");
 //BA.debugLineNum = 952;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 953;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 954;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 955;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 956;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 957;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 958;BA.debugLine="GaugeTemp.CurrentValue = a(1)";
mostCurrent._gaugetemp._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 959;BA.debugLine="GaugeHumidity.CurrentValue = a(2)";
mostCurrent._gaugehumidity._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 960;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 961;BA.debugLine="If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6)) || (_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 962;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 964;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 966;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 };
 //BA.debugLineNum = 969;BA.debugLine="GaugeHeatIndex.CurrentValue = a(5)";
mostCurrent._gaugeheatindex._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 970;BA.debugLine="GaugeDewPoint.CurrentValue = a(6)";
mostCurrent._gaugedewpoint._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 971;BA.debugLine="If a(7) = \"\" Then";
if ((_a[(int) (7)]).equals("")) { 
 //BA.debugLineNum = 972;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 973;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 974;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 975;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 977;BA.debugLine="If a(8).Contains(\"|24:\") Then";
if (_a[(int) (8)].contains("|24:")) { 
 //BA.debugLineNum = 978;BA.debugLine="a(8) = a(8).Replace(\"|24:\",\"|00:\")";
_a[(int) (8)] = _a[(int) (8)].replace("|24:","|00:");
 //BA.debugLineNum = 979;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 980;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 981;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 982;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 984;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 985;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 986;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 987;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 988;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 990;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 991;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appen";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 993;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Appen";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 996;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdate.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e52) {
			processBA.setLastException(e52); //BA.debugLineNum = 1000;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("41966134",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1001;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 1003;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1005;BA.debugLine="Sub CheckTempHumiditySettingBasement";
 //BA.debugLineNum = 1006;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 1007;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 1008;BA.debugLine="Try";
try { //BA.debugLineNum = 1009;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 1010;BA.debugLine="status = StateManager.GetSetting(\"TempHumidityBa";
_status = mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"TempHumidityBasement");
 //BA.debugLineNum = 1011;BA.debugLine="status = status.Replace(\"|24:\",\"|00:\")";
_status = _status.replace("|24:","|00:");
 //BA.debugLineNum = 1012;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 1013;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 1014;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 1015;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 1016;BA.debugLine="If a(0) = \"OK\" And a(1) > 0 Then";
if ((_a[(int) (0)]).equals("OK") && (double)(Double.parseDouble(_a[(int) (1)]))>0) { 
 //BA.debugLineNum = 1017;BA.debugLine="GaugeTempBasement.CurrentValue = a(1)";
mostCurrent._gaugetempbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 1018;BA.debugLine="GaugeHumidityBasement.CurrentValue = a(2)";
mostCurrent._gaugehumiditybasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 1019;BA.debugLine="lblPerceptionBasement.Text = cs.Initialize.Bol";
mostCurrent._lblperceptionbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 1020;BA.debugLine="If a(4) = 2 Or a(4) = 6 Or a(4) = 10 Then";
if ((_a[(int) (4)]).equals(BA.NumberToString(2)) || (_a[(int) (4)]).equals(BA.NumberToString(6)) || (_a[(int) (4)]).equals(BA.NumberToString(10))) { 
 //BA.debugLineNum = 1021;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else if((_a[(int) (4)]).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 1023;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 1025;BA.debugLine="lblComfortBasement.Text = cs.Initialize.Bold.";
mostCurrent._lblcomfortbasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 };
 //BA.debugLineNum = 1027;BA.debugLine="GaugeHeatIndexBasement.CurrentValue = a(5)";
mostCurrent._gaugeheatindexbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 1028;BA.debugLine="GaugeDewPointBasement.CurrentValue = a(6)";
mostCurrent._gaugedewpointbasement._setcurrentvalue /*float*/ ((float)(Double.parseDouble(_a[(int) (6)])));
 //BA.debugLineNum = 1029;BA.debugLine="If a(7) = \"\" Then";
if ((_a[(int) (7)]).equals("")) { 
 //BA.debugLineNum = 1030;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 1031;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 1032;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 1033;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 1035;BA.debugLine="If a(8).Contains(\"|24:\") Then";
if (_a[(int) (8)].contains("|24:")) { 
 //BA.debugLineNum = 1036;BA.debugLine="a(8) = a(8).Replace(\"|24:\",\"|00:\")";
_a[(int) (8)] = _a[(int) (8)].replace("|24:","|00:");
 //BA.debugLineNum = 1037;BA.debugLine="Dim Tomorrow As Long";
_tomorrow = 0L;
 //BA.debugLineNum = 1038;BA.debugLine="Tomorrow = DateTime.add(DateTime.Now, 0, 0, 1";
_tomorrow = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (1));
 //BA.debugLineNum = 1039;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd");
 //BA.debugLineNum = 1040;BA.debugLine="a(7) = DateTime.Date(Tomorrow)";
_a[(int) (7)] = anywheresoftware.b4a.keywords.Common.DateTime.Date(_tomorrow);
 };
 //BA.debugLineNum = 1042;BA.debugLine="DateTime.DateFormat = \"yy-MM-dd HH:mm:ss z\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd HH:mm:ss z");
 //BA.debugLineNum = 1043;BA.debugLine="Dim ticks As Long = DateTime.DateParse(a(7) &";
_ticks = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_a[(int) (7)]+" "+_a[(int) (8)]+" GMT");
 //BA.debugLineNum = 1044;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a z";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a z");
 //BA.debugLineNum = 1045;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 1046;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngT";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 1048;BA.debugLine="If p.Minutes > = 5 Then";
if (_p.Minutes>=5) { 
 //BA.debugLineNum = 1049;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bo";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 }else {
 //BA.debugLineNum = 1051;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bo";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks))).PopAll().getObject()));
 };
 }else if((_a[(int) (8)]).equals("00:00:00")) { 
 //BA.debugLineNum = 1054;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bol";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(mostCurrent._lbllastupdatebasement.getText().replace("Last update: ",""))).PopAll().getObject()));
 };
 };
 } 
       catch (Exception e52) {
			processBA.setLastException(e52); //BA.debugLineNum = 1058;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("42031669",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1059;BA.debugLine="lblLastUpdateBasement.Text = cs.Initialize.Bold.";
mostCurrent._lbllastupdatebasement.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Exception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject())).PopAll().getObject()));
 };
 //BA.debugLineNum = 1061;BA.debugLine="End Sub";
return "";
}
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
 //BA.debugLineNum = 2252;BA.debugLine="Try";
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
 //BA.debugLineNum = 2253;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(Index)";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._clvactivity._getpanel(_index);
 //BA.debugLineNum = 2254;BA.debugLine="If p.NumberOfViews > 0 Then";
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
 //BA.debugLineNum = 2255;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).GetV";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (6));
 //BA.debugLineNum = 2256;BA.debugLine="ContentLabel.Visible = False";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2258;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).GetV";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (4));
 //BA.debugLineNum = 2259;BA.debugLine="ContentLabel.Visible = False";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 2262;BA.debugLine="UpdateItemColor(Index)";
_updateitemcolor(_index);
 //BA.debugLineNum = 2263;BA.debugLine="wvMedia.LoadUrl(\"\")";
parent.mostCurrent._wvmedia.LoadUrl("");
 //BA.debugLineNum = 2265;BA.debugLine="Dim cd As CardData = clvActivity.GetValue(Index)";
_cd = (cloyd.smart.home.monitor.main._carddata)(parent.mostCurrent._clvactivity._getvalue(_index));
 //BA.debugLineNum = 2266;BA.debugLine="Dim videoURL As String = cd.mediaURL";
_videourl = _cd.mediaURL /*String*/ ;
 //BA.debugLineNum = 2268;BA.debugLine="B4XLoadingIndicator4.Show";
parent.mostCurrent._b4xloadingindicator4._show /*String*/ ();
 //BA.debugLineNum = 2270;BA.debugLine="Dim list1 As List = Starter.kvs.ListKeys";
_list1 = new anywheresoftware.b4a.objects.collections.List();
_list1 = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._listkeys /*anywheresoftware.b4a.objects.collections.List*/ ();
 //BA.debugLineNum = 2271;BA.debugLine="For i =  0 To list1.Size-1";
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
 //BA.debugLineNum = 2272;BA.debugLine="Dim mytypes As Object = Starter.kvs.Get(list1.G";
_mytypes = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._get /*Object*/ (BA.ObjectToString(_list1.Get(_i)));
 //BA.debugLineNum = 2273;BA.debugLine="Dim videos = mytypes As VideoInfo";
_videos = (cloyd.smart.home.monitor.main._videoinfo)(_mytypes);
 //BA.debugLineNum = 2274;BA.debugLine="If videoURL.Contains(videos.VideoID) Then";
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
 //BA.debugLineNum = 2276;BA.debugLine="Starter.kvs.Put(videos.VideoID,CreateCustomTyp";
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
 //BA.debugLineNum = 2279;BA.debugLine="lblDuration.Text = \"0:00\"";
parent.mostCurrent._lblduration.setText(BA.ObjectToCharSequence("0:00"));
 //BA.debugLineNum = 2281;BA.debugLine="Dim rs As ResumableSub = ShowVideo(videoURL)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _showvideo(_videourl);
 //BA.debugLineNum = 2282;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 21;
return;
case 21:
//C
this.state = 18;
_result = (Object) result[0];
;
 if (true) break;

case 17:
//C
this.state = 18;
this.catchState = 0;
 //BA.debugLineNum = 2284;BA.debugLine="B4XLoadingIndicator4.Hide";
parent.mostCurrent._b4xloadingindicator4._hide /*String*/ ();
 //BA.debugLineNum = 2285;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("44063266",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 18:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 2288;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 2179;BA.debugLine="Sub clvActivity_VisibleRangeChanged (FirstIndex As";
 //BA.debugLineNum = 2180;BA.debugLine="Dim ExtraSize As Int = 1";
_extrasize = (int) (1);
 //BA.debugLineNum = 2181;BA.debugLine="For i = 0 To clvActivity.Size - 1";
{
final int step2 = 1;
final int limit2 = (int) (mostCurrent._clvactivity._getsize()-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 2182;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(i)";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._clvactivity._getpanel(_i);
 //BA.debugLineNum = 2183;BA.debugLine="If i > FirstIndex - ExtraSize And i < LastIndex";
if (_i>_firstindex-_extrasize && _i<_lastindex+_extrasize) { 
 //BA.debugLineNum = 2185;BA.debugLine="If p.NumberOfViews = 0 Then";
if (_p.getNumberOfViews()==0) { 
 //BA.debugLineNum = 2186;BA.debugLine="Dim cd As CardData = clvActivity.GetValue(i)";
_cd = (cloyd.smart.home.monitor.main._carddata)(mostCurrent._clvactivity._getvalue(_i));
 //BA.debugLineNum = 2187;BA.debugLine="p.LoadLayout(\"blinkcellitem\")";
_p.LoadLayout("blinkcellitem",mostCurrent.activityBA);
 //BA.debugLineNum = 2188;BA.debugLine="ivScreenshot.Bitmap = cd.screenshot";
mostCurrent._ivscreenshot.setBitmap((android.graphics.Bitmap)(_cd.screenshot /*anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper*/ .getObject()));
 //BA.debugLineNum = 2190;BA.debugLine="Dim dayname As String";
_dayname = "";
 //BA.debugLineNum = 2191;BA.debugLine="dayname = ConvertDayName(cd.filedate)";
_dayname = _convertdayname(_cd.filedate /*String*/ );
 //BA.debugLineNum = 2192;BA.debugLine="If cd.iswatchedvisible Then";
if (_cd.iswatchedvisible /*boolean*/ ) { 
 //BA.debugLineNum = 2193;BA.debugLine="ivWatched.Visible = True";
mostCurrent._ivwatched.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2194;BA.debugLine="lblDate.Text = \"   \" & dayname & \" \" & Conver";
mostCurrent._lbldate.setText(BA.ObjectToCharSequence("   "+_dayname+" "+_convertdatetimeperiod(_cd.filedate /*String*/ ,_dayname)));
 }else {
 //BA.debugLineNum = 2196;BA.debugLine="ivWatched.Visible = False";
mostCurrent._ivwatched.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2197;BA.debugLine="lblDate.Text = \"   \" & dayname";
mostCurrent._lbldate.setText(BA.ObjectToCharSequence("   "+_dayname));
 };
 //BA.debugLineNum = 2199;BA.debugLine="lblFileInfo.Text = \"   \" & ConvertFullDateTime";
mostCurrent._lblfileinfo.setText(BA.ObjectToCharSequence("   "+_convertfulldatetime(_cd.filedate /*String*/ )));
 //BA.debugLineNum = 2200;BA.debugLine="lblDeviceInfo.Text = \"   \" & cd.deviceinfo";
mostCurrent._lbldeviceinfo.setText(BA.ObjectToCharSequence("   "+_cd.deviceinfo /*String*/ ));
 //BA.debugLineNum = 2201;BA.debugLine="lblMediaURL.Text = cd.mediaURL";
mostCurrent._lblmediaurl.setText(BA.ObjectToCharSequence(_cd.mediaURL /*String*/ ));
 //BA.debugLineNum = 2203;BA.debugLine="If previousSelectedIndex = i Then";
if (_previousselectedindex==_i) { 
 //BA.debugLineNum = 2204;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(previ";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._clvactivity._getpanel(_previousselectedindex);
 //BA.debugLineNum = 2205;BA.debugLine="If p.NumberOfViews > 0 Then";
if (_p.getNumberOfViews()>0) { 
 //BA.debugLineNum = 2206;BA.debugLine="Dim backPane As B4XView = p.getview(0)";
_backpane = new anywheresoftware.b4a.objects.B4XViewWrapper();
_backpane = _p.GetView((int) (0));
 //BA.debugLineNum = 2207;BA.debugLine="backPane.Color = xui.Color_ARGB(255,217,215,";
_backpane.setColor(mostCurrent._xui.Color_ARGB((int) (255),(int) (217),(int) (215),(int) (222)));
 //BA.debugLineNum = 2209;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).G";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (6));
 //BA.debugLineNum = 2210;BA.debugLine="ContentLabel.Visible = True";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 };
 };
 }else {
 //BA.debugLineNum = 2216;BA.debugLine="If p.NumberOfViews > 0 Then";
if (_p.getNumberOfViews()>0) { 
 //BA.debugLineNum = 2217;BA.debugLine="p.RemoveAllViews";
_p.RemoveAllViews();
 };
 };
 }
};
 //BA.debugLineNum = 2221;BA.debugLine="End Sub";
return "";
}
public static String  _convertdatetime(String _inputtime) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
 //BA.debugLineNum = 1858;BA.debugLine="Sub ConvertDateTime(inputTime As String) As String";
 //BA.debugLineNum = 1860;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 1861;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 1862;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 1865;BA.debugLine="Return DateTime.Date(lngTicks)";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 //BA.debugLineNum = 1866;BA.debugLine="End Sub";
return "";
}
public static String  _convertdatetimeperiod(String _inputtime,String _dayname) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
b4a.example.dateutils._period _p = null;
 //BA.debugLineNum = 2354;BA.debugLine="Sub ConvertDateTimePeriod(inputTime As String, day";
 //BA.debugLineNum = 2356;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 2357;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 2358;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 2359;BA.debugLine="Dim p As Period = DateUtils.PeriodBetween(lngTick";
_p = mostCurrent._dateutils._periodbetween(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 2361;BA.debugLine="If dayname.Contains(\"Today\") Then";
if (_dayname.contains("Today")) { 
 //BA.debugLineNum = 2362;BA.debugLine="If p.Days = 0 Then";
if (_p.Days==0) { 
 //BA.debugLineNum = 2363;BA.debugLine="If p.Hours = 0 Then";
if (_p.Hours==0) { 
 //BA.debugLineNum = 2364;BA.debugLine="If p.Minutes = 0 Then";
if (_p.Minutes==0) { 
 //BA.debugLineNum = 2365;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 2367;BA.debugLine="Return p.Minutes & \"m \" & p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 2370;BA.debugLine="Return p.Hours & \"h \" & p.Minutes & \"m \" & p.S";
if (true) return BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else if(_p.Hours==0) { 
 //BA.debugLineNum = 2373;BA.debugLine="If p.Minutes = 0 Then";
if (_p.Minutes==0) { 
 //BA.debugLineNum = 2374;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 2376;BA.debugLine="Return p.Minutes & \"m \" & p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else if(_p.Minutes==0) { 
 //BA.debugLineNum = 2379;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 2381;BA.debugLine="Return p.Days & \"d \" & p.Hours & \"h \" & p.Minut";
if (true) return BA.NumberToString(_p.Days)+"d "+BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else if(_dayname.contains("Yesterday")) { 
 //BA.debugLineNum = 2384;BA.debugLine="If p.Days = 0 Then";
if (_p.Days==0) { 
 //BA.debugLineNum = 2385;BA.debugLine="If p.Hours = 0 Then";
if (_p.Hours==0) { 
 //BA.debugLineNum = 2386;BA.debugLine="If p.Minutes = 0 Then";
if (_p.Minutes==0) { 
 //BA.debugLineNum = 2387;BA.debugLine="Return p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Seconds)+"s ago";
 }else {
 //BA.debugLineNum = 2389;BA.debugLine="Return p.Minutes & \"m \" & p.Seconds & \"s ago\"";
if (true) return BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 2392;BA.debugLine="Return p.Hours & \"h \" & p.Minutes & \"m \" & p.S";
if (true) return BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 2395;BA.debugLine="Return p.Days & \"d \" & p.Hours & \"h \" & p.Minut";
if (true) return BA.NumberToString(_p.Days)+"d "+BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 }else {
 //BA.debugLineNum = 2398;BA.debugLine="Return p.Days & \"d \" & p.Hours & \"h \" & p.Minute";
if (true) return BA.NumberToString(_p.Days)+"d "+BA.NumberToString(_p.Hours)+"h "+BA.NumberToString(_p.Minutes)+"m "+BA.NumberToString(_p.Seconds)+"s ago";
 };
 //BA.debugLineNum = 2401;BA.debugLine="End Sub";
return "";
}
public static String  _convertdayname(String _inputtime) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
long _yesterday = 0L;
long _timestamp = 0L;
 //BA.debugLineNum = 2403;BA.debugLine="Sub ConvertDayName(inputTime As String) As String";
 //BA.debugLineNum = 2405;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 2406;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 2407;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 2409;BA.debugLine="Dim Yesterday As Long";
_yesterday = 0L;
 //BA.debugLineNum = 2410;BA.debugLine="Dim timestamp As Long";
_timestamp = 0L;
 //BA.debugLineNum = 2411;BA.debugLine="DateTime.DateFormat = \"yyyyMMdd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyyMMdd");
 //BA.debugLineNum = 2412;BA.debugLine="Yesterday = DateTime.Date(DateTime.add(DateTime.N";
_yesterday = (long)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1)))));
 //BA.debugLineNum = 2413;BA.debugLine="timestamp = DateTime.Date(lngTicks)";
_timestamp = (long)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks)));
 //BA.debugLineNum = 2415;BA.debugLine="DateTime.DateFormat = \"h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("h:mm:ss a");
 //BA.debugLineNum = 2416;BA.debugLine="If DateUtils.IsSameDay(lngTicks,DateTime.now) The";
if (mostCurrent._dateutils._issameday(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow())) { 
 //BA.debugLineNum = 2417;BA.debugLine="Return \"Today\" '& DateTime.Date(lngTicks)";
if (true) return "Today";
 }else if(_yesterday==_timestamp) { 
 //BA.debugLineNum = 2419;BA.debugLine="Return \"Yesterday\" '& DateTime.Date(lngTicks)";
if (true) return "Yesterday";
 }else {
 //BA.debugLineNum = 2421;BA.debugLine="Return DateUtils.GetDayOfWeekName(lngTicks) '& \"";
if (true) return mostCurrent._dateutils._getdayofweekname(mostCurrent.activityBA,_lngticks);
 };
 //BA.debugLineNum = 2423;BA.debugLine="End Sub";
return "";
}
public static String  _convertfulldatetime(String _inputtime) throws Exception{
long _ticks = 0L;
long _lngticks = 0L;
long _yesterday = 0L;
long _timestamp = 0L;
 //BA.debugLineNum = 2425;BA.debugLine="Sub ConvertFullDateTime(inputTime As String) As St";
 //BA.debugLineNum = 2427;BA.debugLine="Dim ticks As Long = ParseUTCstring(inputTime.Repl";
_ticks = _parseutcstring(_inputtime.replace("+00:00","+0000"));
 //BA.debugLineNum = 2428;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy h:mm:ss a");
 //BA.debugLineNum = 2429;BA.debugLine="Dim lngTicks As Long = ticks";
_lngticks = _ticks;
 //BA.debugLineNum = 2431;BA.debugLine="Dim Yesterday As Long";
_yesterday = 0L;
 //BA.debugLineNum = 2432;BA.debugLine="Dim timestamp As Long";
_timestamp = 0L;
 //BA.debugLineNum = 2433;BA.debugLine="DateTime.DateFormat = \"yyyyMMdd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyyMMdd");
 //BA.debugLineNum = 2434;BA.debugLine="Yesterday = DateTime.Date(DateTime.add(DateTime.N";
_yesterday = (long)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1)))));
 //BA.debugLineNum = 2435;BA.debugLine="timestamp = DateTime.Date(lngTicks)";
_timestamp = (long)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks)));
 //BA.debugLineNum = 2437;BA.debugLine="DateTime.DateFormat = \"h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("h:mm:ss a");
 //BA.debugLineNum = 2438;BA.debugLine="If DateUtils.IsSameDay(lngTicks,DateTime.now) The";
if (mostCurrent._dateutils._issameday(mostCurrent.activityBA,_lngticks,anywheresoftware.b4a.keywords.Common.DateTime.getNow())) { 
 //BA.debugLineNum = 2439;BA.debugLine="Return DateTime.Date(lngTicks)";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 }else if(_yesterday==_timestamp) { 
 //BA.debugLineNum = 2441;BA.debugLine="Return DateTime.Date(lngTicks)";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 }else {
 //BA.debugLineNum = 2443;BA.debugLine="Return DateTime.Date(lngTicks)";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_lngticks);
 };
 //BA.debugLineNum = 2445;BA.debugLine="End Sub";
return "";
}
public static String  _converttickstotimestring(long _t) throws Exception{
int _hours = 0;
int _minutes = 0;
int _seconds = 0;
 //BA.debugLineNum = 2529;BA.debugLine="Sub ConvertTicksToTimeString(t As Long) As String";
 //BA.debugLineNum = 2530;BA.debugLine="Dim  hours, minutes, seconds As Int";
_hours = 0;
_minutes = 0;
_seconds = 0;
 //BA.debugLineNum = 2531;BA.debugLine="hours = t / DateTime.TicksPerHour";
_hours = (int) (_t/(double)anywheresoftware.b4a.keywords.Common.DateTime.TicksPerHour);
 //BA.debugLineNum = 2532;BA.debugLine="minutes = (t Mod DateTime.TicksPerHour) / DateTim";
_minutes = (int) ((_t%anywheresoftware.b4a.keywords.Common.DateTime.TicksPerHour)/(double)anywheresoftware.b4a.keywords.Common.DateTime.TicksPerMinute);
 //BA.debugLineNum = 2533;BA.debugLine="seconds = (t Mod DateTime.TicksPerMinute) / DateT";
_seconds = (int) ((_t%anywheresoftware.b4a.keywords.Common.DateTime.TicksPerMinute)/(double)anywheresoftware.b4a.keywords.Common.DateTime.TicksPerSecond);
 //BA.debugLineNum = 2534;BA.debugLine="Return NumberFormat(minutes, 1, 0) & \":\" & Number";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat(_minutes,(int) (1),(int) (0))+":"+anywheresoftware.b4a.keywords.Common.NumberFormat(_seconds,(int) (2),(int) (0));
 //BA.debugLineNum = 2535;BA.debugLine="End Sub";
return "";
}
public static cloyd.smart.home.monitor.main._videoinfo  _createcustomtype(String _thumbnailpath,String _datecreated,String _watched,String _devicename,String _videoid,byte[] _thumbnailblob) throws Exception{
cloyd.smart.home.monitor.main._videoinfo _ct = null;
 //BA.debugLineNum = 2166;BA.debugLine="Private Sub CreateCustomType(ThumbnailPath As Stri";
 //BA.debugLineNum = 2167;BA.debugLine="Dim ct As VideoInfo";
_ct = new cloyd.smart.home.monitor.main._videoinfo();
 //BA.debugLineNum = 2168;BA.debugLine="ct.Initialize";
_ct.Initialize();
 //BA.debugLineNum = 2169;BA.debugLine="ct.ThumbnailPath = ThumbnailPath";
_ct.ThumbnailPath /*String*/  = _thumbnailpath;
 //BA.debugLineNum = 2170;BA.debugLine="ct.DateCreated = DateCreated";
_ct.DateCreated /*String*/  = _datecreated;
 //BA.debugLineNum = 2171;BA.debugLine="ct.Watched = Watched";
_ct.Watched /*String*/  = _watched;
 //BA.debugLineNum = 2172;BA.debugLine="ct.DeviceName = DeviceName";
_ct.DeviceName /*String*/  = _devicename;
 //BA.debugLineNum = 2173;BA.debugLine="ct.ThumbnailBLOB = ThumbnailBLOB";
_ct.ThumbnailBLOB /*byte[]*/  = _thumbnailblob;
 //BA.debugLineNum = 2174;BA.debugLine="ct.VideoID = VideoID";
_ct.VideoID /*String*/  = _videoid;
 //BA.debugLineNum = 2175;BA.debugLine="Return ct";
if (true) return _ct;
 //BA.debugLineNum = 2176;BA.debugLine="End Sub";
return null;
}
public static String  _createpreferencescreen() throws Exception{
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat1 = null;
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat2 = null;
de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper _cat3 = null;
anywheresoftware.b4a.objects.IntentWrapper _in = null;
 //BA.debugLineNum = 1063;BA.debugLine="Sub CreatePreferenceScreen";
 //BA.debugLineNum = 1064;BA.debugLine="screen.Initialize(\"Settings\", \"\")";
_screen.Initialize("Settings","");
 //BA.debugLineNum = 1066;BA.debugLine="Dim cat1,cat2,cat3 As AHPreferenceCategory";
_cat1 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat2 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat3 = new de.amberhome.objects.preferenceactivity.PreferenceCategoryWrapper();
 //BA.debugLineNum = 1068;BA.debugLine="cat1.Initialize(\"Temperature & Humidity\")";
_cat1.Initialize("Temperature & Humidity");
 //BA.debugLineNum = 1069;BA.debugLine="cat1.AddEditText(\"TempHumidityCooldownTime\", \"Liv";
_cat1.AddEditText("TempHumidityCooldownTime","Living Area Cooldown Time","Minimum creation time interval between new notification","5","");
 //BA.debugLineNum = 1070;BA.debugLine="cat1.AddEditText(\"TempHumidityCooldownTimeBasemen";
_cat1.AddEditText("TempHumidityCooldownTimeBasement","Basement Cooldown Time","Minimum creation time interval between new notification","5","");
 //BA.debugLineNum = 1071;BA.debugLine="cat1.AddEditText(\"HumidityAddValue\", \"Humidity Ad";
_cat1.AddEditText("HumidityAddValue","Humidity Additional Value","Value to be added to humidity to improve accuracy","0","");
 //BA.debugLineNum = 1073;BA.debugLine="cat2.Initialize(\"Special Settings\")";
_cat2.Initialize("Special Settings");
 //BA.debugLineNum = 1074;BA.debugLine="Dim In As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 1075;BA.debugLine="In.Initialize(\"android.settings.ACTION_NOTIFICATI";
_in.Initialize("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS","");
 //BA.debugLineNum = 1076;BA.debugLine="cat2.AddIntent(\"Notification Access\", \"Enable or";
_cat2.AddIntent("Notification Access","Enable or disable listening to notifications",(android.content.Intent)(_in.getObject()),BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 1078;BA.debugLine="cat3.Initialize(\"Sensors\")";
_cat3.Initialize("Sensors");
 //BA.debugLineNum = 1079;BA.debugLine="cat3.AddEditText(\"SensorNotRespondingTime\", \"Sens";
_cat3.AddEditText("SensorNotRespondingTime","Sensor Not Responding","Data age when to restart sensor","5","");
 //BA.debugLineNum = 1081;BA.debugLine="screen.AddPreferenceCategory(cat2)";
_screen.AddPreferenceCategory(_cat2);
 //BA.debugLineNum = 1082;BA.debugLine="screen.AddPreferenceCategory(cat1)";
_screen.AddPreferenceCategory(_cat1);
 //BA.debugLineNum = 1083;BA.debugLine="screen.AddPreferenceCategory(cat3)";
_screen.AddPreferenceCategory(_cat3);
 //BA.debugLineNum = 1084;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTime","5");
 //BA.debugLineNum = 1085;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 1086;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",\"0\")";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue","0");
 //BA.debugLineNum = 1087;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime","5");
 //BA.debugLineNum = 1088;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 1089;BA.debugLine="End Sub";
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
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;

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
 //BA.debugLineNum = 1437;BA.debugLine="Try";
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
 //BA.debugLineNum = 1438;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1439;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1440;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1441;BA.debugLine="j.Download(Link)";
_j._download /*String*/ (_link);
 //BA.debugLineNum = 1442;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1443;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 21;
return;
case 21:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1444;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 1446;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 7:
//if
this.state = 14;
if ((_camera).equals("347574")) { 
this.state = 9;
}else if((_camera).equals("236967")) { 
this.state = 11;
}else if((_camera).equals("226821")) { 
this.state = 13;
}if (true) break;

case 9:
//C
this.state = 14;
 //BA.debugLineNum = 1447;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Driveway.jpg",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 14;
 //BA.debugLineNum = 1449;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"FrontDoor.jpg",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 1451;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"SideYard.jpg",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 14:
//C
this.state = 17;
;
 //BA.debugLineNum = 1453;BA.debugLine="File.Copy2(j.GetInputStream, out)";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_j._getinputstream /*anywheresoftware.b4a.objects.streams.File.InputStreamWrapper*/ ().getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 1454;BA.debugLine="out.Close '<------ very important";
_out.Close();
 //BA.debugLineNum = 1457;BA.debugLine="iv.Bitmap = j.GetBitmap";
_iv.setBitmap((android.graphics.Bitmap)(_j._getbitmap /*anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper*/ ().getObject()));
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 1459;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1460;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;

case 17:
//C
this.state = 20;
;
 //BA.debugLineNum = 1462;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 19:
//C
this.state = 20;
this.catchState = 0;
 //BA.debugLineNum = 1464;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("42555932",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 20:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1466;BA.debugLine="End Sub";
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
public static void  _downloadimagefullscreen(String _link,String _camera) throws Exception{
ResumableSub_DownloadImageFullscreen rsub = new ResumableSub_DownloadImageFullscreen(null,_link,_camera);
rsub.resume(processBA, null);
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
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2856;BA.debugLine="Try";
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
 //BA.debugLineNum = 2857;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 2858;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 2859;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 2860;BA.debugLine="j.Download(Link)";
_j._download /*String*/ (_link);
 //BA.debugLineNum = 2861;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 2862;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 17;
return;
case 17:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 2863;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 2865;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File.";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"screenshot.jpg",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2866;BA.debugLine="File.Copy2(j.GetInputStream, out)";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_j._getinputstream /*anywheresoftware.b4a.objects.streams.File.InputStreamWrapper*/ ().getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 2867;BA.debugLine="out.Close '<------ very important";
_out.Close();
 //BA.debugLineNum = 2869;BA.debugLine="Dim FileName As String = \"screenshot.jpg\"";
_filename = "screenshot.jpg";
 //BA.debugLineNum = 2870;BA.debugLine="If File.Exists(File.DirInternal, FileName) Then";
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
 //BA.debugLineNum = 2871;BA.debugLine="File.Copy(File.DirInternal, FileName, Starter.";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename,parent.mostCurrent._starter._provider /*cloyd.smart.home.monitor.fileprovider*/ ._sharedfolder /*String*/ ,_filename);
 //BA.debugLineNum = 2872;BA.debugLine="Dim in As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 2873;BA.debugLine="in.Initialize(in.ACTION_VIEW, \"\")";
_in.Initialize(_in.ACTION_VIEW,"");
 //BA.debugLineNum = 2874;BA.debugLine="Starter.Provider.SetFileUriAsIntentData(in, Fi";
parent.mostCurrent._starter._provider /*cloyd.smart.home.monitor.fileprovider*/ ._setfileuriasintentdata /*String*/ (_in,_filename);
 //BA.debugLineNum = 2876;BA.debugLine="in.SetType(\"image/*\")";
_in.SetType("image/*");
 //BA.debugLineNum = 2877;BA.debugLine="StartActivity(in)";
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
 //BA.debugLineNum = 2880;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 2881;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;

case 13:
//C
this.state = 16;
;
 //BA.debugLineNum = 2883;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 15:
//C
this.state = 16;
this.catchState = 0;
 //BA.debugLineNum = 2885;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("44980766",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 16:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 2887;BA.debugLine="End Sub";
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
public static String  _getairquality(int _number) throws Exception{
 //BA.debugLineNum = 478;BA.debugLine="Sub GetAirQuality(number As Int) As String";
 //BA.debugLineNum = 481;BA.debugLine="If number <= 100 Then";
if (_number<=100) { 
 //BA.debugLineNum = 482;BA.debugLine="Return(\"Carbon monoxide perfect\")";
if (true) return ("Carbon monoxide perfect");
 }else if(((_number>100) && (_number<400)) || _number==400) { 
 //BA.debugLineNum = 484;BA.debugLine="Return(\"Carbon monoxide normal\")";
if (true) return ("Carbon monoxide normal");
 }else if(((_number>400) && (_number<900)) || _number==900) { 
 //BA.debugLineNum = 486;BA.debugLine="Return(\"Carbon monoxide high\")";
if (true) return ("Carbon monoxide high");
 }else if(_number>900) { 
 //BA.debugLineNum = 488;BA.debugLine="Return(\"ALARM Carbon monoxide very high\")";
if (true) return ("ALARM Carbon monoxide very high");
 }else {
 //BA.debugLineNum = 490;BA.debugLine="Return(\"MQ-7 - cant read any value - check the s";
if (true) return ("MQ-7 - cant read any value - check the sensor!");
 };
 //BA.debugLineNum = 492;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _getalltablabels(anywheresoftware.b4a.objects.TabStripViewPager _tabstrip) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
anywheresoftware.b4a.objects.PanelWrapper _tc = null;
anywheresoftware.b4a.objects.collections.List _res = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
 //BA.debugLineNum = 790;BA.debugLine="Public Sub GetAllTabLabels (tabstrip As TabStrip)";
 //BA.debugLineNum = 791;BA.debugLine="Dim jo As JavaObject = tabstrip";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_tabstrip));
 //BA.debugLineNum = 792;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 793;BA.debugLine="r.Target = jo.GetField(\"tabStrip\")";
_r.Target = _jo.GetField("tabStrip");
 //BA.debugLineNum = 794;BA.debugLine="Dim tc As Panel = r.GetField(\"tabsContainer\")";
_tc = new anywheresoftware.b4a.objects.PanelWrapper();
_tc.setObject((android.view.ViewGroup)(_r.GetField("tabsContainer")));
 //BA.debugLineNum = 795;BA.debugLine="Dim res As List";
_res = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 796;BA.debugLine="res.Initialize";
_res.Initialize();
 //BA.debugLineNum = 797;BA.debugLine="For Each v As View In tc";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group7 = _tc;
final int groupLen7 = group7.getSize()
;int index7 = 0;
;
for (; index7 < groupLen7;index7++){
_v.setObject((android.view.View)(group7.Get(index7)));
 //BA.debugLineNum = 798;BA.debugLine="If v Is Label Then res.Add(v)";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
_res.Add((Object)(_v.getObject()));};
 }
};
 //BA.debugLineNum = 800;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 801;BA.debugLine="End Sub";
return null;
}
public static anywheresoftware.b4a.objects.collections.List  _getalltablabelsforbadge(anywheresoftware.b4a.objects.TabStripViewPager _tabstrip) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
anywheresoftware.b4a.objects.PanelWrapper _tc = null;
anywheresoftware.b4a.objects.collections.List _res = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
 //BA.debugLineNum = 804;BA.debugLine="Public Sub GetAllTabLabelsForBadge (tabstrip As Ta";
 //BA.debugLineNum = 805;BA.debugLine="Dim jo As JavaObject = tabstrip";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_tabstrip));
 //BA.debugLineNum = 806;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 807;BA.debugLine="r.Target = jo.GetField(\"tabStrip\")";
_r.Target = _jo.GetField("tabStrip");
 //BA.debugLineNum = 808;BA.debugLine="Dim tc As Panel = r.GetField(\"tabsContainer\")";
_tc = new anywheresoftware.b4a.objects.PanelWrapper();
_tc.setObject((android.view.ViewGroup)(_r.GetField("tabsContainer")));
 //BA.debugLineNum = 809;BA.debugLine="Dim res As List";
_res = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 810;BA.debugLine="res.Initialize";
_res.Initialize();
 //BA.debugLineNum = 811;BA.debugLine="For Each v As View In tc";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group7 = _tc;
final int groupLen7 = group7.getSize()
;int index7 = 0;
;
for (; index7 < groupLen7;index7++){
_v.setObject((android.view.View)(group7.Get(index7)));
 //BA.debugLineNum = 812;BA.debugLine="If v Is Label Then res.Add(v)";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
_res.Add((Object)(_v.getObject()));};
 //BA.debugLineNum = 813;BA.debugLine="If v.Tag Is Label Then res.Add(v.Tag)";
if (_v.getTag() instanceof android.widget.TextView) { 
_res.Add(_v.getTag());};
 }
};
 //BA.debugLineNum = 815;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 816;BA.debugLine="End Sub";
return null;
}
public static String  _getauthinfo(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.Map _authtokenmap = null;
anywheresoftware.b4a.objects.collections.Map _networks = null;
anywheresoftware.b4a.objects.collections.Map _region = null;
anywheresoftware.b4a.objects.collections.Map _account = null;
int _id = 0;
 //BA.debugLineNum = 1468;BA.debugLine="Sub GetAuthInfo(json As String)";
 //BA.debugLineNum = 1469;BA.debugLine="Try";
try { //BA.debugLineNum = 1470;BA.debugLine="lblStatus.Text = \"Getting authtoken...\"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Getting authtoken..."));
 //BA.debugLineNum = 1471;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1472;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1473;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1474;BA.debugLine="Dim authtokenmap As Map = root.Get(\"authtoken\")";
_authtokenmap = new anywheresoftware.b4a.objects.collections.Map();
_authtokenmap.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("authtoken"))));
 //BA.debugLineNum = 1475;BA.debugLine="authToken = authtokenmap.Get(\"authtoken\")";
_authtoken = BA.ObjectToString(_authtokenmap.Get((Object)("authtoken")));
 //BA.debugLineNum = 1476;BA.debugLine="Log(\"authToken: \" &  authToken)";
anywheresoftware.b4a.keywords.Common.LogImpl("42621448","authToken: "+_authtoken,0);
 //BA.debugLineNum = 1477;BA.debugLine="Dim networks As Map = root.Get(\"networks\")";
_networks = new anywheresoftware.b4a.objects.collections.Map();
_networks.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("networks"))));
 //BA.debugLineNum = 1478;BA.debugLine="Log(\"networkID: \" & networks.GetKeyAt(0))";
anywheresoftware.b4a.keywords.Common.LogImpl("42621450","networkID: "+BA.ObjectToString(_networks.GetKeyAt((int) (0))),0);
 //BA.debugLineNum = 1479;BA.debugLine="networkID = networks.GetKeyAt(0)";
_networkid = BA.ObjectToString(_networks.GetKeyAt((int) (0)));
 //BA.debugLineNum = 1480;BA.debugLine="Dim region As Map = root.Get(\"region\")";
_region = new anywheresoftware.b4a.objects.collections.Map();
_region.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("region"))));
 //BA.debugLineNum = 1481;BA.debugLine="userRegion = region.GetKeyAt(0)";
_userregion = BA.ObjectToString(_region.GetKeyAt((int) (0)));
 //BA.debugLineNum = 1482;BA.debugLine="Log(\"userRegion: \" &  userRegion)";
anywheresoftware.b4a.keywords.Common.LogImpl("42621454","userRegion: "+_userregion,0);
 //BA.debugLineNum = 1484;BA.debugLine="Dim account As Map = root.Get(\"account\")";
_account = new anywheresoftware.b4a.objects.collections.Map();
_account.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("account"))));
 //BA.debugLineNum = 1485;BA.debugLine="Dim id As Int = account.Get(\"id\")";
_id = (int)(BA.ObjectToNumber(_account.Get((Object)("id"))));
 //BA.debugLineNum = 1486;BA.debugLine="Log(\"accountID: \" & id)";
anywheresoftware.b4a.keywords.Common.LogImpl("42621458","accountID: "+BA.NumberToString(_id),0);
 //BA.debugLineNum = 1487;BA.debugLine="accountID = id";
_accountid = BA.NumberToString(_id);
 } 
       catch (Exception e20) {
			processBA.setLastException(e20); //BA.debugLineNum = 1489;BA.debugLine="lblStatus.Text = \"ERROR: GetAuthInfo - \" & LastE";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("ERROR: GetAuthInfo - "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA))));
 //BA.debugLineNum = 1490;BA.debugLine="response = \"ERROR: GetAuthInfo - \" & LastExcepti";
_response = "ERROR: GetAuthInfo - "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA));
 //BA.debugLineNum = 1491;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("42621463",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1494;BA.debugLine="End Sub";
return "";
}
public static String  _getcamerainfo(String _json) throws Exception{
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
 //BA.debugLineNum = 1587;BA.debugLine="Sub GetCameraInfo(json As String)";
 //BA.debugLineNum = 1588;BA.debugLine="Try";
try { //BA.debugLineNum = 1589;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1590;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1591;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1592;BA.debugLine="Dim camera_status As Map = root.Get(\"camera_stat";
_camera_status = new anywheresoftware.b4a.objects.collections.Map();
_camera_status.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("camera_status"))));
 //BA.debugLineNum = 1593;BA.debugLine="Dim total_108_wakeups As Int = camera_status.Get";
_total_108_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("total_108_wakeups"))));
 //BA.debugLineNum = 1594;BA.debugLine="Dim battery_voltage As Int = camera_status.Get(\"";
_battery_voltage = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("battery_voltage"))));
 //BA.debugLineNum = 1595;BA.debugLine="Dim light_sensor_data_valid As String = camera_s";
_light_sensor_data_valid = BA.ObjectToString(_camera_status.Get((Object)("light_sensor_data_valid")));
 //BA.debugLineNum = 1596;BA.debugLine="Dim lfr_tb_wakeups As Int = camera_status.Get(\"l";
_lfr_tb_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lfr_tb_wakeups"))));
 //BA.debugLineNum = 1597;BA.debugLine="Dim fw_git_hash As String = camera_status.Get(\"f";
_fw_git_hash = BA.ObjectToString(_camera_status.Get((Object)("fw_git_hash")));
 //BA.debugLineNum = 1598;BA.debugLine="Dim wifi_strength As Int = camera_status.Get(\"wi";
_wifi_strength = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("wifi_strength"))));
 //BA.debugLineNum = 1599;BA.debugLine="Dim lfr_strength As Int = camera_status.Get(\"lfr";
_lfr_strength = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lfr_strength"))));
 //BA.debugLineNum = 1600;BA.debugLine="Dim total_tb_wakeups As Int = camera_status.Get(";
_total_tb_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("total_tb_wakeups"))));
 //BA.debugLineNum = 1601;BA.debugLine="Dim created_at As String = camera_status.Get(\"cr";
_created_at = BA.ObjectToString(_camera_status.Get((Object)("created_at")));
 //BA.debugLineNum = 1602;BA.debugLine="Dim light_sensor_ch0 As Int = camera_status.Get(";
_light_sensor_ch0 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("light_sensor_ch0"))));
 //BA.debugLineNum = 1603;BA.debugLine="Dim mac As String = camera_status.Get(\"mac\")";
_mac = BA.ObjectToString(_camera_status.Get((Object)("mac")));
 //BA.debugLineNum = 1604;BA.debugLine="Dim unit_number As Int = camera_status.Get(\"unit";
_unit_number = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("unit_number"))));
 //BA.debugLineNum = 1605;BA.debugLine="Dim updated_at As String = camera_status.Get(\"up";
_updated_at = BA.ObjectToString(_camera_status.Get((Object)("updated_at")));
 //BA.debugLineNum = 1606;BA.debugLine="Dim light_sensor_ch1 As Int = camera_status.Get(";
_light_sensor_ch1 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("light_sensor_ch1"))));
 //BA.debugLineNum = 1607;BA.debugLine="Dim time_dhcp_lease As Int = camera_status.Get(\"";
_time_dhcp_lease = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_dhcp_lease"))));
 //BA.debugLineNum = 1608;BA.debugLine="Dim temperature As Int = camera_status.Get(\"temp";
_temperature = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("temperature"))));
 //BA.debugLineNum = 1609;BA.debugLine="Dim time_first_video As Int = camera_status.Get(";
_time_first_video = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_first_video"))));
 //BA.debugLineNum = 1610;BA.debugLine="Dim time_dns_resolve As Int = camera_status.Get(";
_time_dns_resolve = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_dns_resolve"))));
 //BA.debugLineNum = 1611;BA.debugLine="Dim id As Int = camera_status.Get(\"id\")";
_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("id"))));
 //BA.debugLineNum = 1612;BA.debugLine="Dim temp_alert_status As String = camera_status.";
_temp_alert_status = BA.ObjectToString(_camera_status.Get((Object)("temp_alert_status")));
 //BA.debugLineNum = 1613;BA.debugLine="Dim time_108_boot As Int = camera_status.Get(\"ti";
_time_108_boot = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_108_boot"))));
 //BA.debugLineNum = 1614;BA.debugLine="Dim lfr_108_wakeups As Int = camera_status.Get(\"";
_lfr_108_wakeups = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lfr_108_wakeups"))));
 //BA.debugLineNum = 1615;BA.debugLine="cameraThumbnail = camera_status.Get(\"thumbnail\")";
_camerathumbnail = BA.ObjectToString(_camera_status.Get((Object)("thumbnail")));
 //BA.debugLineNum = 1616;BA.debugLine="Log(\"cameraThumbnail: \" & cameraThumbnail)";
anywheresoftware.b4a.keywords.Common.LogImpl("42883613","cameraThumbnail: "+_camerathumbnail,0);
 //BA.debugLineNum = 1617;BA.debugLine="Dim lifetime_duration As Int = camera_status.Get";
_lifetime_duration = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lifetime_duration"))));
 //BA.debugLineNum = 1618;BA.debugLine="Dim wifi_connect_failure_count As Int = camera_s";
_wifi_connect_failure_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("wifi_connect_failure_count"))));
 //BA.debugLineNum = 1619;BA.debugLine="Dim camera_id As Int = camera_status.Get(\"camera";
_camera_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("camera_id"))));
 //BA.debugLineNum = 1620;BA.debugLine="Dim battery_alert_status As String = camera_stat";
_battery_alert_status = BA.ObjectToString(_camera_status.Get((Object)("battery_alert_status")));
 //BA.debugLineNum = 1621;BA.debugLine="Dim dhcp_failure_count As Int = camera_status.Ge";
_dhcp_failure_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dhcp_failure_count"))));
 //BA.debugLineNum = 1622;BA.debugLine="Dim ip_address As String = camera_status.Get(\"ip";
_ip_address = BA.ObjectToString(_camera_status.Get((Object)("ip_address")));
 //BA.debugLineNum = 1623;BA.debugLine="Dim ipv As String = camera_status.Get(\"ipv\")";
_ipv = BA.ObjectToString(_camera_status.Get((Object)("ipv")));
 //BA.debugLineNum = 1624;BA.debugLine="Dim light_sensor_data_new As String = camera_sta";
_light_sensor_data_new = BA.ObjectToString(_camera_status.Get((Object)("light_sensor_data_new")));
 //BA.debugLineNum = 1625;BA.debugLine="Dim network_id As Int = camera_status.Get(\"netwo";
_network_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("network_id"))));
 //BA.debugLineNum = 1626;BA.debugLine="Dim account_id As Int = camera_status.Get(\"accou";
_account_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("account_id"))));
 //BA.debugLineNum = 1627;BA.debugLine="Dim serial As String = camera_status.Get(\"serial";
_serial = BA.ObjectToString(_camera_status.Get((Object)("serial")));
 //BA.debugLineNum = 1628;BA.debugLine="Dim dev_1 As Int = camera_status.Get(\"dev_1\")";
_dev_1 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dev_1"))));
 //BA.debugLineNum = 1629;BA.debugLine="Dim time_wlan_connect As Int = camera_status.Get";
_time_wlan_connect = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("time_wlan_connect"))));
 //BA.debugLineNum = 1630;BA.debugLine="Dim dev_2 As Int = camera_status.Get(\"dev_2\")";
_dev_2 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dev_2"))));
 //BA.debugLineNum = 1631;BA.debugLine="Dim socket_failure_count As Int = camera_status.";
_socket_failure_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("socket_failure_count"))));
 //BA.debugLineNum = 1632;BA.debugLine="Dim dev_3 As Int = camera_status.Get(\"dev_3\")";
_dev_3 = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("dev_3"))));
 //BA.debugLineNum = 1633;BA.debugLine="Dim pir_rejections As Int = camera_status.Get(\"p";
_pir_rejections = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("pir_rejections"))));
 //BA.debugLineNum = 1634;BA.debugLine="Dim sync_module_id As Int = camera_status.Get(\"s";
_sync_module_id = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("sync_module_id"))));
 //BA.debugLineNum = 1635;BA.debugLine="Dim lifetime_count As Int = camera_status.Get(\"l";
_lifetime_count = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("lifetime_count"))));
 //BA.debugLineNum = 1636;BA.debugLine="Dim error_codes As Int = camera_status.Get(\"erro";
_error_codes = (int)(BA.ObjectToNumber(_camera_status.Get((Object)("error_codes"))));
 //BA.debugLineNum = 1637;BA.debugLine="Dim fw_version As String = camera_status.Get(\"fw";
_fw_version = BA.ObjectToString(_camera_status.Get((Object)("fw_version")));
 //BA.debugLineNum = 1638;BA.debugLine="Dim ac_power As String = camera_status.Get(\"ac_p";
_ac_power = BA.ObjectToString(_camera_status.Get((Object)("ac_power")));
 //BA.debugLineNum = 1644;BA.debugLine="If camera_id = \"347574\" Then";
if (_camera_id==(double)(Double.parseDouble("347574"))) { 
 //BA.debugLineNum = 1645;BA.debugLine="lblDrivewayBatt.Text = \"Batt: \" & NumberFormat2";
mostCurrent._lbldrivewaybatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V - "+_checkbattlife(_battery_voltage,mostCurrent._lbldrivewaybatt)));
 //BA.debugLineNum = 1646;BA.debugLine="lblDrivewayTimestamp.Text = ConvertDateTime(upd";
mostCurrent._lbldrivewaytimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1647;BA.debugLine="lblDriveway.Text = \"Driveway v\" & fw_version &";
mostCurrent._lbldriveway.setText(BA.ObjectToCharSequence("Driveway v"+_fw_version+" "+_drivewayarmedstatus));
 //BA.debugLineNum = 1648;BA.debugLine="lblDrivewayWifi.Text = \"WiFi: \" & wifi_strength";
mostCurrent._lbldrivewaywifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 }else if(_camera_id==(double)(Double.parseDouble("236967"))) { 
 //BA.debugLineNum = 1650;BA.debugLine="lblFrontDoorBatt.Text = \"Batt: \" & NumberFormat";
mostCurrent._lblfrontdoorbatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V - "+_checkbattlife(_battery_voltage,mostCurrent._lblfrontdoorbatt)));
 //BA.debugLineNum = 1651;BA.debugLine="lblFrontDoorTimestamp.Text = ConvertDateTime(up";
mostCurrent._lblfrontdoortimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1652;BA.debugLine="lblFrontDoor.Text = \"Front Door v\" & fw_version";
mostCurrent._lblfrontdoor.setText(BA.ObjectToCharSequence("Front Door v"+_fw_version+" "+_frontdoorarmedstatus));
 //BA.debugLineNum = 1653;BA.debugLine="lblFrontDoorWiFi.Text = \"WiFi: \" & wifi_strengt";
mostCurrent._lblfrontdoorwifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 }else if(_camera_id==(double)(Double.parseDouble("226821"))) { 
 //BA.debugLineNum = 1655;BA.debugLine="lblSideYardBatt.Text = \"Batt: \" & NumberFormat2";
mostCurrent._lblsideyardbatt.setText(BA.ObjectToCharSequence("Batt: "+anywheresoftware.b4a.keywords.Common.NumberFormat2((_battery_voltage/(double)100),(int) (0),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False)+"V - "+_checkbattlife(_battery_voltage,mostCurrent._lblsideyardbatt)));
 //BA.debugLineNum = 1656;BA.debugLine="lblSideYardTimestamp.Text = ConvertDateTime(upd";
mostCurrent._lblsideyardtimestamp.setText(BA.ObjectToCharSequence(_convertdatetime(_updated_at)));
 //BA.debugLineNum = 1657;BA.debugLine="lblSideYard.Text = \"Side Yard v\" & fw_version";
mostCurrent._lblsideyard.setText(BA.ObjectToCharSequence("Side Yard v"+_fw_version+" "+_sideyardarmedstatus));
 //BA.debugLineNum = 1658;BA.debugLine="lblSideYardWiFi.Text = \"WiFi: \" & wifi_strength";
mostCurrent._lblsideyardwifi.setText(BA.ObjectToCharSequence("WiFi: "+BA.NumberToString(_wifi_strength)+"dBm - "+_checklfrlevel(_wifi_strength)));
 };
 } 
       catch (Exception e69) {
			processBA.setLastException(e69); //BA.debugLineNum = 1662;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("42883659",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1664;BA.debugLine="End Sub";
return "";
}
public static String  _getcomfort(String _dht11comfortstatus) throws Exception{
String _localcomfortstatus = "";
 //BA.debugLineNum = 527;BA.debugLine="Sub GetComfort(DHT11ComfortStatus As String) As St";
 //BA.debugLineNum = 528;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = "";
 //BA.debugLineNum = 529;BA.debugLine="Select Case DHT11ComfortStatus";
switch (BA.switchObjectToInt(_dht11comfortstatus,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(8),BA.NumberToString(9),BA.NumberToString(10))) {
case 0: {
 //BA.debugLineNum = 531;BA.debugLine="localcomfortstatus = \"OK\"";
_localcomfortstatus = "OK";
 break; }
case 1: {
 //BA.debugLineNum = 533;BA.debugLine="localcomfortstatus = \"Too hot\"";
_localcomfortstatus = "Too hot";
 break; }
case 2: {
 //BA.debugLineNum = 535;BA.debugLine="localcomfortstatus = \"Too cold\"";
_localcomfortstatus = "Too cold";
 break; }
case 3: {
 //BA.debugLineNum = 537;BA.debugLine="localcomfortstatus = \"Too dry\"";
_localcomfortstatus = "Too dry";
 break; }
case 4: {
 //BA.debugLineNum = 539;BA.debugLine="localcomfortstatus = \"Hot and dry\"";
_localcomfortstatus = "Hot and dry";
 break; }
case 5: {
 //BA.debugLineNum = 541;BA.debugLine="localcomfortstatus = \"Cold and dry\"";
_localcomfortstatus = "Cold and dry";
 break; }
case 6: {
 //BA.debugLineNum = 543;BA.debugLine="localcomfortstatus = \"Too humid\"";
_localcomfortstatus = "Too humid";
 break; }
case 7: {
 //BA.debugLineNum = 545;BA.debugLine="localcomfortstatus = \"Hot and humid\"";
_localcomfortstatus = "Hot and humid";
 break; }
case 8: {
 //BA.debugLineNum = 547;BA.debugLine="localcomfortstatus = \"Cold and humid\"";
_localcomfortstatus = "Cold and humid";
 break; }
default: {
 //BA.debugLineNum = 549;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = "Unknown";
 break; }
}
;
 //BA.debugLineNum = 551;BA.debugLine="Return localcomfortstatus";
if (true) return _localcomfortstatus;
 //BA.debugLineNum = 552;BA.debugLine="End Sub";
return "";
}
public static String  _getcommandid(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
 //BA.debugLineNum = 1497;BA.debugLine="Sub GetCommandID(json As String)";
 //BA.debugLineNum = 1498;BA.debugLine="Try";
try { //BA.debugLineNum = 1499;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1500;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1501;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1516;BA.debugLine="commandID = root.Get(\"id\")";
_commandid = BA.ObjectToString(_root.Get((Object)("id")));
 //BA.debugLineNum = 1517;BA.debugLine="Log(\"commandID: \" & commandID)";
anywheresoftware.b4a.keywords.Common.LogImpl("42686996","commandID: "+_commandid,0);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 1539;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("42687018",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1542;BA.debugLine="End Sub";
return "";
}
public static String  _getcommandstatus(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
 //BA.debugLineNum = 1544;BA.debugLine="Sub GetCommandStatus(json As String)";
 //BA.debugLineNum = 1545;BA.debugLine="Try";
try { //BA.debugLineNum = 1546;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1547;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1548;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1549;BA.debugLine="commandComplete = root.Get(\"complete\")";
_commandcomplete = BA.ObjectToBoolean(_root.Get((Object)("complete")));
 //BA.debugLineNum = 1550;BA.debugLine="Log(\"commandComplete: \" & commandComplete)";
anywheresoftware.b4a.keywords.Common.LogImpl("42752518","commandComplete: "+BA.ObjectToString(_commandcomplete),0);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 1552;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("42752520",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1555;BA.debugLine="End Sub";
return "";
}
public static String  _gethomescreen(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.List _devices = null;
anywheresoftware.b4a.objects.collections.Map _coldevices = null;
int _device_id = 0;
String _active = "";
anywheresoftware.b4j.object.JavaObject _jo = null;
float _radius = 0f;
float _dx = 0f;
float _dy = 0f;
anywheresoftware.b4a.objects.collections.Map _network = null;
String _armednetwork = "";
 //BA.debugLineNum = 1710;BA.debugLine="Sub GetHomescreen(json As String)";
 //BA.debugLineNum = 1711;BA.debugLine="Try";
try { //BA.debugLineNum = 1712;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1713;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1714;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1715;BA.debugLine="Dim devices As List = root.Get(\"devices\")";
_devices = new anywheresoftware.b4a.objects.collections.List();
_devices.setObject((java.util.List)(_root.Get((Object)("devices"))));
 //BA.debugLineNum = 1716;BA.debugLine="For Each coldevices As Map In devices";
_coldevices = new anywheresoftware.b4a.objects.collections.Map();
{
final anywheresoftware.b4a.BA.IterableList group6 = _devices;
final int groupLen6 = group6.getSize()
;int index6 = 0;
;
for (; index6 < groupLen6;index6++){
_coldevices.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(group6.Get(index6)));
 //BA.debugLineNum = 1721;BA.debugLine="Dim device_id As Int = coldevices.Get(\"device_i";
_device_id = (int)(BA.ObjectToNumber(_coldevices.Get((Object)("device_id"))));
 //BA.debugLineNum = 1724;BA.debugLine="Dim active As String = coldevices.Get(\"active\")";
_active = BA.ObjectToString(_coldevices.Get((Object)("active")));
 //BA.debugLineNum = 1739;BA.debugLine="If device_id = \"347574\" Then";
if (_device_id==(double)(Double.parseDouble("347574"))) { 
 //BA.debugLineNum = 1740;BA.debugLine="If active = \"armed\" Then";
if ((_active).equals("armed")) { 
 //BA.debugLineNum = 1741;BA.debugLine="drivewayArmedStatus = \"\"";
_drivewayarmedstatus = "";
 //BA.debugLineNum = 1742;BA.debugLine="Dim jo As JavaObject = lblDriveway";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._lbldriveway.getObject()));
 //BA.debugLineNum = 1743;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1744;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Black)});
 }else {
 //BA.debugLineNum = 1746;BA.debugLine="drivewayArmedStatus = \"NOT ARMED!\"";
_drivewayarmedstatus = "NOT ARMED!";
 //BA.debugLineNum = 1747;BA.debugLine="Dim jo As JavaObject = lblDriveway";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._lbldriveway.getObject()));
 //BA.debugLineNum = 1748;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1749;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 };
 }else if(_device_id==(double)(Double.parseDouble("236967"))) { 
 //BA.debugLineNum = 1752;BA.debugLine="If active = \"armed\" Then";
if ((_active).equals("armed")) { 
 //BA.debugLineNum = 1753;BA.debugLine="frontdoorArmedStatus = \"\"";
_frontdoorarmedstatus = "";
 //BA.debugLineNum = 1754;BA.debugLine="Dim jo As JavaObject = lblFrontDoor";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._lblfrontdoor.getObject()));
 //BA.debugLineNum = 1755;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1756;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Black)});
 }else {
 //BA.debugLineNum = 1758;BA.debugLine="frontdoorArmedStatus = \"NOT ARMED!\"";
_frontdoorarmedstatus = "NOT ARMED!";
 //BA.debugLineNum = 1759;BA.debugLine="Dim jo As JavaObject = lblFrontDoor";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._lblfrontdoor.getObject()));
 //BA.debugLineNum = 1760;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1761;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 };
 }else if(_device_id==(double)(Double.parseDouble("226821"))) { 
 //BA.debugLineNum = 1764;BA.debugLine="If active = \"armed\" Then";
if ((_active).equals("armed")) { 
 //BA.debugLineNum = 1765;BA.debugLine="sideyardArmedStatus = \"\"";
_sideyardarmedstatus = "";
 //BA.debugLineNum = 1766;BA.debugLine="Dim jo As JavaObject = lblSideYard";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._lblsideyard.getObject()));
 //BA.debugLineNum = 1767;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1768;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Black)});
 }else {
 //BA.debugLineNum = 1770;BA.debugLine="sideyardArmedStatus = \"NOT ARMED!\"";
_sideyardarmedstatus = "NOT ARMED!";
 //BA.debugLineNum = 1771;BA.debugLine="Dim jo As JavaObject = lblSideYard";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._lblsideyard.getObject()));
 //BA.debugLineNum = 1772;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Fl";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1773;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 };
 };
 }
};
 //BA.debugLineNum = 1780;BA.debugLine="Dim network As Map = root.Get(\"network\")";
_network = new anywheresoftware.b4a.objects.collections.Map();
_network.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("network"))));
 //BA.debugLineNum = 1784;BA.debugLine="Dim armedNetwork As String = network.Get(\"armed\"";
_armednetwork = BA.ObjectToString(_network.Get((Object)("armed")));
 //BA.debugLineNum = 1790;BA.debugLine="If armedNetwork <> \"true\" Then";
if ((_armednetwork).equals("true") == false) { 
 //BA.debugLineNum = 1791;BA.debugLine="drivewayArmedStatus = \"NETWORK NOT ARMED!\"";
_drivewayarmedstatus = "NETWORK NOT ARMED!";
 //BA.debugLineNum = 1792;BA.debugLine="Dim jo As JavaObject = lblDriveway";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._lbldriveway.getObject()));
 //BA.debugLineNum = 1793;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Floa";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1794;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 //BA.debugLineNum = 1796;BA.debugLine="frontdoorArmedStatus = \"NETWORK NOT ARMED!\"";
_frontdoorarmedstatus = "NETWORK NOT ARMED!";
 //BA.debugLineNum = 1797;BA.debugLine="Dim jo As JavaObject = lblFrontDoor";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._lblfrontdoor.getObject()));
 //BA.debugLineNum = 1798;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Floa";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1799;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 //BA.debugLineNum = 1801;BA.debugLine="sideyardArmedStatus = \"NETWORK NOT ARMED!\"";
_sideyardarmedstatus = "NETWORK NOT ARMED!";
 //BA.debugLineNum = 1802;BA.debugLine="Dim jo As JavaObject = lblSideYard";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._lblsideyard.getObject()));
 //BA.debugLineNum = 1803;BA.debugLine="Dim radius = 4dip, dx = 0dip, dy = 0dip As Floa";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1804;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 };
 } 
       catch (Exception e64) {
			processBA.setLastException(e64); //BA.debugLineNum = 1807;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("43014753",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1809;BA.debugLine="End Sub";
return "";
}
public static String  _getperception(String _dht11perception) throws Exception{
String _localperception = "";
 //BA.debugLineNum = 494;BA.debugLine="Sub GetPerception(DHT11Perception As String) As St";
 //BA.debugLineNum = 505;BA.debugLine="Dim localperception As String";
_localperception = "";
 //BA.debugLineNum = 506;BA.debugLine="Select Case DHT11Perception";
switch (BA.switchObjectToInt(_dht11perception,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(7))) {
case 0: {
 //BA.debugLineNum = 508;BA.debugLine="localperception = \"Feels like the western US, a";
_localperception = "Feels like the western US, a bit dry to some";
 break; }
case 1: {
 //BA.debugLineNum = 510;BA.debugLine="localperception = \"Very comfortable\"";
_localperception = "Very comfortable";
 break; }
case 2: {
 //BA.debugLineNum = 512;BA.debugLine="localperception = \"Comfortable\"";
_localperception = "Comfortable";
 break; }
case 3: {
 //BA.debugLineNum = 514;BA.debugLine="localperception = \"OK but humidity is at upper";
_localperception = "OK but humidity is at upper limit";
 break; }
case 4: {
 //BA.debugLineNum = 516;BA.debugLine="localperception = \"Uncomfortable and the humidi";
_localperception = "Uncomfortable and the humidity is at upper limit";
 break; }
case 5: {
 //BA.debugLineNum = 518;BA.debugLine="localperception = \"Very humid, quite uncomforta";
_localperception = "Very humid, quite uncomfortable";
 break; }
case 6: {
 //BA.debugLineNum = 520;BA.debugLine="localperception = \"Extremely uncomfortable, opp";
_localperception = "Extremely uncomfortable, oppressive";
 break; }
case 7: {
 //BA.debugLineNum = 522;BA.debugLine="localperception = \"Severely high, even deadly f";
_localperception = "Severely high, even deadly for asthma related illnesses";
 break; }
}
;
 //BA.debugLineNum = 524;BA.debugLine="Return localperception";
if (true) return _localperception;
 //BA.debugLineNum = 525;BA.debugLine="End Sub";
return "";
}
public static String  _getresterror(String _json) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
int _code = 0;
String _message = "";
 //BA.debugLineNum = 1557;BA.debugLine="Sub GetRESTError(json As String) As String";
 //BA.debugLineNum = 1558;BA.debugLine="Try";
try { //BA.debugLineNum = 1564;BA.debugLine="If json.Contains(\"<h1>Not Found</h1>\") Then";
if (_json.contains("<h1>Not Found</h1>")) { 
 //BA.debugLineNum = 1565;BA.debugLine="Return \"REST endpoint URL not found. Try again.";
if (true) return "REST endpoint URL not found. Try again.";
 }else {
 //BA.debugLineNum = 1567;BA.debugLine="If json.IndexOf(\"{\") <> -1 Then";
if (_json.indexOf("{")!=-1) { 
 //BA.debugLineNum = 1568;BA.debugLine="json = json.SubString(json.IndexOf(\"{\"))";
_json = _json.substring(_json.indexOf("{"));
 //BA.debugLineNum = 1569;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1570;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1571;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1572;BA.debugLine="Dim code As Int = root.Get(\"code\")";
_code = (int)(BA.ObjectToNumber(_root.Get((Object)("code"))));
 //BA.debugLineNum = 1573;BA.debugLine="Dim message As String = root.Get(\"message\")";
_message = BA.ObjectToString(_root.Get((Object)("message")));
 //BA.debugLineNum = 1574;BA.debugLine="Log(\"Code: \" & code & \" Message: \" & message)";
anywheresoftware.b4a.keywords.Common.LogImpl("42818065","Code: "+BA.NumberToString(_code)+" Message: "+_message,0);
 //BA.debugLineNum = 1575;BA.debugLine="Return \"Code: \" & code & \" Message: \" & messag";
if (true) return "Code: "+BA.NumberToString(_code)+" Message: "+_message;
 }else {
 //BA.debugLineNum = 1577;BA.debugLine="Return json";
if (true) return _json;
 };
 };
 } 
       catch (Exception e19) {
			processBA.setLastException(e19); //BA.debugLineNum = 1581;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("42818072",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 1583;BA.debugLine="Return json";
if (true) return _json;
 };
 //BA.debugLineNum = 1585;BA.debugLine="End Sub";
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
anywheresoftware.b4j.object.JavaObject _jo = null;
float _radius = 0f;
float _dx = 0f;
float _dy = 0f;
 //BA.debugLineNum = 1666;BA.debugLine="Sub GetSyncModuleInfo(json As String)";
 //BA.debugLineNum = 1667;BA.debugLine="Try";
try { //BA.debugLineNum = 1668;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1669;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 1670;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 1671;BA.debugLine="Dim syncmodule As Map = root.Get(\"syncmodule\")";
_syncmodule = new anywheresoftware.b4a.objects.collections.Map();
_syncmodule.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("syncmodule"))));
 //BA.debugLineNum = 1675;BA.debugLine="Dim wifi_strength As Int = syncmodule.Get(\"wifi_";
_wifi_strength = (int)(BA.ObjectToNumber(_syncmodule.Get((Object)("wifi_strength"))));
 //BA.debugLineNum = 1676;BA.debugLine="Dim os_version As String = syncmodule.Get(\"os_ve";
_os_version = BA.ObjectToString(_syncmodule.Get((Object)("os_version")));
 //BA.debugLineNum = 1693;BA.debugLine="Dim fw_version As String = syncmodule.Get(\"fw_ve";
_fw_version = BA.ObjectToString(_syncmodule.Get((Object)("fw_version")));
 //BA.debugLineNum = 1695;BA.debugLine="Dim status As String = syncmodule.Get(\"status\")";
_status = BA.ObjectToString(_syncmodule.Get((Object)("status")));
 //BA.debugLineNum = 1696;BA.debugLine="Dim jo As JavaObject = lblSyncModule";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._lblsyncmodule.getObject()));
 //BA.debugLineNum = 1697;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Float";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1699;BA.debugLine="If status = \"online\" Then";
if ((_status).equals("online")) { 
 //BA.debugLineNum = 1700;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Green)});
 }else {
 //BA.debugLineNum = 1702;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Red)});
 };
 //BA.debugLineNum = 1704;BA.debugLine="lblSyncModule.Text = \"Sync Module is \" & status";
mostCurrent._lblsyncmodule.setText(BA.ObjectToCharSequence("Sync Module is "+_status+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Wifi Strength: "+BA.NumberToString(_wifi_strength)+" bars"+anywheresoftware.b4a.keywords.Common.CRLF+"Firmware version: "+_fw_version+anywheresoftware.b4a.keywords.Common.CRLF+"OS version: "+_os_version));
 } 
       catch (Exception e19) {
			processBA.setLastException(e19); //BA.debugLineNum = 1706;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("42949160",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 1708;BA.debugLine="End Sub";
return "";
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
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
int _i = 0;
int _j = 0;
int step4;
int limit4;

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
 //BA.debugLineNum = 2538;BA.debugLine="Try";
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
 //BA.debugLineNum = 2540;BA.debugLine="Dim lbl As B4XView = GetAllTabLabelsForBadge(Tab";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl.setObject((java.lang.Object)(_getalltablabelsforbadge(parent.mostCurrent._tabstrip1).Get((int) (5))));
 //BA.debugLineNum = 2543;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia-";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/api/v1/accounts/88438/media/changed?since=-999999999-01-01T00:00:00+18:00&page=1");
 //BA.debugLineNum = 2545;BA.debugLine="For i = 1 To 20";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (20);
_i = (int) (1) ;
this.state = 17;
if (true) break;

case 17:
//C
this.state = 13;
if ((step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4)) this.state = 6;
if (true) break;

case 18:
//C
this.state = 17;
_i = ((int)(0 + _i + step4)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2546;BA.debugLine="If response = \"\" Then";
if (true) break;

case 7:
//if
this.state = 12;
if ((parent._response).equals("")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 2547;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 19;
return;
case 19:
//C
this.state = 12;
;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 2549;BA.debugLine="Exit";
this.state = 13;
if (true) break;
 if (true) break;

case 12:
//C
this.state = 18;
;
 if (true) break;
if (true) break;

case 13:
//C
this.state = 16;
;
 //BA.debugLineNum = 2553;BA.debugLine="Dim j As Int = StringCount(response,\"\"\"watched\"\"";
_j = _stringcount(parent._response,"\"watched\":false",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2554;BA.debugLine="badger1.SetBadge(lbl, j)";
parent.mostCurrent._badger1._setbadge /*String*/ (_lbl,_j);
 //BA.debugLineNum = 2555;BA.debugLine="StateManager.SetSetting(\"UnwatchedVideoClips\",j)";
parent.mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"UnwatchedVideoClips",BA.NumberToString(_j));
 //BA.debugLineNum = 2556;BA.debugLine="StateManager.SaveSettings";
parent.mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 if (true) break;

case 15:
//C
this.state = 16;
this.catchState = 0;
 //BA.debugLineNum = 2558;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("44587541",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 16:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 2560;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 2561;BA.debugLine="End Sub";
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
public static String  _getversioncode() throws Exception{
String _appversion = "";
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
String _packagename = "";
 //BA.debugLineNum = 637;BA.debugLine="Sub GetVersionCode() As String";
 //BA.debugLineNum = 638;BA.debugLine="Dim AppVersion As String";
_appversion = "";
 //BA.debugLineNum = 639;BA.debugLine="Try";
try { //BA.debugLineNum = 640;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 641;BA.debugLine="Dim packageName As String";
_packagename = "";
 //BA.debugLineNum = 642;BA.debugLine="packageName =  Application.PackageName";
_packagename = anywheresoftware.b4a.keywords.Common.Application.getPackageName();
 //BA.debugLineNum = 643;BA.debugLine="AppVersion = pm.GetVersionName(packageName)";
_appversion = _pm.GetVersionName(_packagename);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 645;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("41310728",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 647;BA.debugLine="Return AppVersion";
if (true) return _appversion;
 //BA.debugLineNum = 648;BA.debugLine="End Sub";
return "";
}
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
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
int _k = 0;
anywheresoftware.b4a.objects.collections.Map _colmedia = null;
String _thumbnail = "";
String _created_at = "";
String _device_name = "";
String _watched = "";
String _medianame = "";
String _videoid = "";
cloyd.smart.home.monitor.httpjob _j = null;
anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _image = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
Object _mytypes = null;
cloyd.smart.home.monitor.main._videoinfo _videos = null;
anywheresoftware.b4a.objects.collections.List _list1 = null;
int _i = 0;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _in = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
cloyd.smart.home.monitor.main._carddata _cd = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.BA.IterableList group13;
int index13;
int groupLen13;
int step58;
int limit58;

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
 //BA.debugLineNum = 2055;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 40;
this.catchState = 39;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 39;
 //BA.debugLineNum = 2056;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 2057;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 2058;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 2059;BA.debugLine="Dim media As List = root.Get(\"media\")";
_media = new anywheresoftware.b4a.objects.collections.List();
_media.setObject((java.util.List)(_root.Get((Object)("media"))));
 //BA.debugLineNum = 2062;BA.debugLine="Dim lbl As B4XView = GetAllTabLabelsForBadge(Tab";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl.setObject((java.lang.Object)(_getalltablabelsforbadge(parent.mostCurrent._tabstrip1).Get((int) (5))));
 //BA.debugLineNum = 2065;BA.debugLine="clvActivity.Clear";
parent.mostCurrent._clvactivity._clear();
 //BA.debugLineNum = 2067;BA.debugLine="Dim k As Int = StringCount(response,\"\"\"watched\"\"";
_k = _stringcount(parent._response,"\"watched\":false",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2068;BA.debugLine="badger1.SetBadge(lbl,k)";
parent.mostCurrent._badger1._setbadge /*String*/ (_lbl,_k);
 //BA.debugLineNum = 2069;BA.debugLine="StateManager.SetSetting(\"UnwatchedVideoClips\",k)";
parent.mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"UnwatchedVideoClips",BA.NumberToString(_k));
 //BA.debugLineNum = 2070;BA.debugLine="StateManager.SaveSettings";
parent.mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 2071;BA.debugLine="ProgressDialogShow2(\"Loading video clips...\",Fal";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading video clips..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2072;BA.debugLine="For Each colmedia As Map In media";
if (true) break;

case 4:
//for
this.state = 25;
_colmedia = new anywheresoftware.b4a.objects.collections.Map();
group13 = _media;
index13 = 0;
groupLen13 = group13.getSize();
this.state = 41;
if (true) break;

case 41:
//C
this.state = 25;
if (index13 < groupLen13) {
this.state = 6;
_colmedia.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(group13.Get(index13)));}
if (true) break;

case 42:
//C
this.state = 41;
index13++;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2073;BA.debugLine="Dim thumbnail As String = colmedia.Get(\"thumbna";
_thumbnail = BA.ObjectToString(_colmedia.Get((Object)("thumbnail")));
 //BA.debugLineNum = 2074;BA.debugLine="Dim created_at As String = colmedia.Get(\"create";
_created_at = BA.ObjectToString(_colmedia.Get((Object)("created_at")));
 //BA.debugLineNum = 2075;BA.debugLine="Dim device_name As String = colmedia.Get(\"devic";
_device_name = BA.ObjectToString(_colmedia.Get((Object)("device_name")));
 //BA.debugLineNum = 2076;BA.debugLine="Dim watched As String = colmedia.Get(\"watched\")";
_watched = BA.ObjectToString(_colmedia.Get((Object)("watched")));
 //BA.debugLineNum = 2077;BA.debugLine="Dim medianame As String = colmedia.Get(\"media\")";
_medianame = BA.ObjectToString(_colmedia.Get((Object)("media")));
 //BA.debugLineNum = 2078;BA.debugLine="Dim VideoID As String = colmedia.Get(\"id\")";
_videoid = BA.ObjectToString(_colmedia.Get((Object)("id")));
 //BA.debugLineNum = 2082;BA.debugLine="If Starter.kvs.ContainsKey(VideoID) = False The";
if (true) break;

case 7:
//if
this.state = 24;
if (parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._containskey /*boolean*/ (_videoid)==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 9;
}else {
this.state = 15;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 2083;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 2084;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 2085;BA.debugLine="j.Download(\"https://rest-\" & userRegion &\".imm";
_j._download /*String*/ ("https://rest-"+parent._userregion+".immedia-semi.com"+_thumbnail+".jpg");
 //BA.debugLineNum = 2086;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 2087;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 43;
return;
case 43:
//C
this.state = 10;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 2088;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 2089;BA.debugLine="Dim image As B4XBitmap = j.GetBitmap";
_image = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
_image.setObject((android.graphics.Bitmap)(_j._getbitmap /*anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper*/ ().getObject()));
 //BA.debugLineNum = 2091;BA.debugLine="Dim out As OutputStream";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 2092;BA.debugLine="out.InitializeToBytesArray(0)";
_out.InitializeToBytesArray((int) (0));
 //BA.debugLineNum = 2093;BA.debugLine="image.WriteToStream(out, 100, \"JPEG\")";
_image.WriteToStream((java.io.OutputStream)(_out.getObject()),(int) (100),BA.getEnumFromString(android.graphics.Bitmap.CompressFormat.class,"JPEG"));
 //BA.debugLineNum = 2094;BA.debugLine="Starter.kvs.Put(VideoID, CreateCustomType(med";
parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._put /*String*/ (_videoid,(Object)(_createcustomtype(_medianame,_created_at,_watched,_device_name,_videoid,_out.ToBytesArray())));
 //BA.debugLineNum = 2095;BA.debugLine="out.Close";
_out.Close();
 if (true) break;

case 13:
//C
this.state = 24;
;
 //BA.debugLineNum = 2097;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 2099;BA.debugLine="Dim mytypes As Object = Starter.kvs.Get(VideoI";
_mytypes = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._get /*Object*/ (_videoid);
 //BA.debugLineNum = 2100;BA.debugLine="Dim videos = mytypes As VideoInfo";
_videos = (cloyd.smart.home.monitor.main._videoinfo)(_mytypes);
 //BA.debugLineNum = 2101;BA.debugLine="If  watched <> videos.Watched Then";
if (true) break;

case 16:
//if
this.state = 23;
if ((_watched).equals(_videos.Watched /*String*/ ) == false) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 2102;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 2103;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 2104;BA.debugLine="j.Download(\"https://rest-\" & userRegion &\".im";
_j._download /*String*/ ("https://rest-"+parent._userregion+".immedia-semi.com"+_thumbnail+".jpg");
 //BA.debugLineNum = 2105;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToke";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 2106;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 44;
return;
case 44:
//C
this.state = 19;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 2107;BA.debugLine="If j.Success Then";
if (true) break;

case 19:
//if
this.state = 22;
if (_j._success /*boolean*/ ) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 2108;BA.debugLine="Dim image As B4XBitmap = j.GetBitmap";
_image = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
_image.setObject((android.graphics.Bitmap)(_j._getbitmap /*anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper*/ ().getObject()));
 //BA.debugLineNum = 2110;BA.debugLine="Dim out As OutputStream";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 2111;BA.debugLine="out.InitializeToBytesArray(0)";
_out.InitializeToBytesArray((int) (0));
 //BA.debugLineNum = 2112;BA.debugLine="image.WriteToStream(out, 100, \"JPEG\")";
_image.WriteToStream((java.io.OutputStream)(_out.getObject()),(int) (100),BA.getEnumFromString(android.graphics.Bitmap.CompressFormat.class,"JPEG"));
 //BA.debugLineNum = 2113;BA.debugLine="Starter.kvs.Put(VideoID, CreateCustomType(me";
parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._put /*String*/ (_videoid,(Object)(_createcustomtype(_medianame,_created_at,_watched,_device_name,_videoid,_out.ToBytesArray())));
 //BA.debugLineNum = 2114;BA.debugLine="out.Close";
_out.Close();
 if (true) break;

case 22:
//C
this.state = 23;
;
 //BA.debugLineNum = 2116;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 23:
//C
this.state = 24;
;
 if (true) break;

case 24:
//C
this.state = 42;
;
 if (true) break;
if (true) break;

case 25:
//C
this.state = 26;
;
 //BA.debugLineNum = 2121;BA.debugLine="clvActivity.Clear";
parent.mostCurrent._clvactivity._clear();
 //BA.debugLineNum = 2122;BA.debugLine="Dim list1 As List = Starter.kvs.ListKeys";
_list1 = new anywheresoftware.b4a.objects.collections.List();
_list1 = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._listkeys /*anywheresoftware.b4a.objects.collections.List*/ ();
 //BA.debugLineNum = 2123;BA.debugLine="For i =  0 To list1.Size-1  ' (list1.Size-1) To";
if (true) break;

case 26:
//for
this.state = 33;
step58 = 1;
limit58 = (int) (_list1.getSize()-1);
_i = (int) (0) ;
this.state = 45;
if (true) break;

case 45:
//C
this.state = 33;
if ((step58 > 0 && _i <= limit58) || (step58 < 0 && _i >= limit58)) this.state = 28;
if (true) break;

case 46:
//C
this.state = 45;
_i = ((int)(0 + _i + step58)) ;
if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 2125;BA.debugLine="Dim mytypes As Object = Starter.kvs.Get(list1.G";
_mytypes = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._get /*Object*/ (BA.ObjectToString(_list1.Get(_i)));
 //BA.debugLineNum = 2126;BA.debugLine="Dim videos = mytypes As VideoInfo";
_videos = (cloyd.smart.home.monitor.main._videoinfo)(_mytypes);
 //BA.debugLineNum = 2130;BA.debugLine="Dim In As InputStream";
_in = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 2131;BA.debugLine="In.InitializeFromBytesArray(videos.ThumbnailBLO";
_in.InitializeFromBytesArray(_videos.ThumbnailBLOB /*byte[]*/ ,(int) (0),_videos.ThumbnailBLOB /*byte[]*/ .length);
 //BA.debugLineNum = 2132;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 2133;BA.debugLine="bmp.Initialize2(In)";
_bmp.Initialize2((java.io.InputStream)(_in.getObject()));
 //BA.debugLineNum = 2135;BA.debugLine="Dim cd As CardData";
_cd = new cloyd.smart.home.monitor.main._carddata();
 //BA.debugLineNum = 2136;BA.debugLine="cd.Initialize";
_cd.Initialize();
 //BA.debugLineNum = 2137;BA.debugLine="cd.screenshot = bmp";
_cd.screenshot /*anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper*/ .setObject((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2138;BA.debugLine="cd.filedate = videos.DateCreated";
_cd.filedate /*String*/  = _videos.DateCreated /*String*/ ;
 //BA.debugLineNum = 2139;BA.debugLine="cd.deviceinfo = videos.DeviceName";
_cd.deviceinfo /*String*/  = _videos.DeviceName /*String*/ ;
 //BA.debugLineNum = 2140;BA.debugLine="cd.iswatchedvisible = Not(videos.Watched)";
_cd.iswatchedvisible /*boolean*/  = anywheresoftware.b4a.keywords.Common.Not(BA.ObjectToBoolean(_videos.Watched /*String*/ ));
 //BA.debugLineNum = 2141;BA.debugLine="cd.mediaURL = \"https://rest-\" & userRegion &\".i";
_cd.mediaURL /*String*/  = "https://rest-"+parent._userregion+".immedia-semi.com"+_videos.ThumbnailPath /*String*/ ;
 //BA.debugLineNum = 2142;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 2143;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, clvActivity.AsView";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),parent.mostCurrent._clvactivity._asview().getWidth(),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 2144;BA.debugLine="clvActivity.Add(p, cd)";
parent.mostCurrent._clvactivity._add(_p,(Object)(_cd));
 //BA.debugLineNum = 2146;BA.debugLine="If i > 50 Then";
if (true) break;

case 29:
//if
this.state = 32;
if (_i>50) { 
this.state = 31;
}if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 2147;BA.debugLine="Starter.kvs.Remove(list1.Get(i))";
parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._remove /*String*/ (BA.ObjectToString(_list1.Get(_i)));
 if (true) break;

case 32:
//C
this.state = 46;
;
 if (true) break;
if (true) break;

case 33:
//C
this.state = 34;
;
 //BA.debugLineNum = 2151;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 2153;BA.debugLine="If list1.Size > 0 Then";
if (true) break;

case 34:
//if
this.state = 37;
if (_list1.getSize()>0) { 
this.state = 36;
}if (true) break;

case 36:
//C
this.state = 37;
 //BA.debugLineNum = 2154;BA.debugLine="clvActivity.JumpToItem(0)";
parent.mostCurrent._clvactivity._jumptoitem((int) (0));
 //BA.debugLineNum = 2155;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 47;
return;
case 47:
//C
this.state = 37;
;
 //BA.debugLineNum = 2156;BA.debugLine="clvActivity_ItemClick(0,\"\") '\"https://rest-\" &";
_clvactivity_itemclick((int) (0),(Object)(""));
 //BA.debugLineNum = 2157;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 48;
return;
case 48:
//C
this.state = 37;
;
 if (true) break;

case 37:
//C
this.state = 40;
;
 if (true) break;

case 39:
//C
this.state = 40;
this.catchState = 0;
 //BA.debugLineNum = 2160;BA.debugLine="B4XLoadingIndicator4.hide";
parent.mostCurrent._b4xloadingindicator4._hide /*String*/ ();
 //BA.debugLineNum = 2161;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("43866731",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 40:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 2163;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 2164;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 51;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 52;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 54;BA.debugLine="Private gblACMenu As ACMenu";
mostCurrent._gblacmenu = new de.amberhome.objects.appcompat.ACMenuWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 56;BA.debugLine="Private GaugeHumidity As Gauge";
mostCurrent._gaugehumidity = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 57;BA.debugLine="Private GaugeTemp As Gauge";
mostCurrent._gaugetemp = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 58;BA.debugLine="Private GaugeDewPoint As Gauge";
mostCurrent._gaugedewpoint = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 59;BA.debugLine="Private GaugeHeatIndex As Gauge";
mostCurrent._gaugeheatindex = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 60;BA.debugLine="Private lblComfort As Label";
mostCurrent._lblcomfort = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private lblPerception As Label";
mostCurrent._lblperception = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private lblLastUpdate As Label";
mostCurrent._lbllastupdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private lblPing As Label";
mostCurrent._lblping = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private TabStrip1 As TabStrip";
mostCurrent._tabstrip1 = new anywheresoftware.b4a.objects.TabStripViewPager();
 //BA.debugLineNum = 65;BA.debugLine="Private lblFontAwesome As Label";
mostCurrent._lblfontawesome = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private GaugeAirQuality As Gauge";
mostCurrent._gaugeairquality = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 67;BA.debugLine="Private lblAirQuality As Label";
mostCurrent._lblairquality = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Private lblAirQualityLastUpdate As Label";
mostCurrent._lblairqualitylastupdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Private ScrollView1 As ScrollView";
mostCurrent._scrollview1 = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Private GaugeAirQualityBasement As Gauge";
mostCurrent._gaugeairqualitybasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 72;BA.debugLine="Private lblAirQualityBasement As Label";
mostCurrent._lblairqualitybasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Private lblAirQualityLastUpdateBasement As Label";
mostCurrent._lblairqualitylastupdatebasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Private PanelAirQualityBasement As Panel";
mostCurrent._panelairqualitybasement = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Private GaugeDewPointBasement As Gauge";
mostCurrent._gaugedewpointbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 76;BA.debugLine="Private GaugeHeatIndexBasement As Gauge";
mostCurrent._gaugeheatindexbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 77;BA.debugLine="Private GaugeHumidityBasement As Gauge";
mostCurrent._gaugehumiditybasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 78;BA.debugLine="Private GaugeTempBasement As Gauge";
mostCurrent._gaugetempbasement = new cloyd.smart.home.monitor.gauge();
 //BA.debugLineNum = 79;BA.debugLine="Private lblComfortBasement As Label";
mostCurrent._lblcomfortbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private lblLastUpdateBasement As Label";
mostCurrent._lbllastupdatebasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private lblPerceptionBasement As Label";
mostCurrent._lblperceptionbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Private lblPingBasement As Label";
mostCurrent._lblpingbasement = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Private ScrollViewBasement As ScrollView";
mostCurrent._scrollviewbasement = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Private PanelTempHumidityBasement As Panel";
mostCurrent._paneltemphumiditybasement = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 85;BA.debugLine="Private lblStatus As Label";
mostCurrent._lblstatus = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Private ivDriveway As ImageView";
mostCurrent._ivdriveway = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 87;BA.debugLine="Private ivFrontDoor As ImageView";
mostCurrent._ivfrontdoor = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 88;BA.debugLine="Private ivSideYard As ImageView";
mostCurrent._ivsideyard = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 89;BA.debugLine="Private ScrollViewBlink As ScrollView";
mostCurrent._scrollviewblink = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 90;BA.debugLine="Private panelBlink As Panel";
mostCurrent._panelblink = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 91;BA.debugLine="Private lblDriveway As Label";
mostCurrent._lbldriveway = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Private lblFrontDoor As Label";
mostCurrent._lblfrontdoor = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Private lblSideYard As Label";
mostCurrent._lblsideyard = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Private lblDrivewayBatt As Label";
mostCurrent._lbldrivewaybatt = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 95;BA.debugLine="Private lblDrivewayTimestamp As Label";
mostCurrent._lbldrivewaytimestamp = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 96;BA.debugLine="Private lblDrivewayWifi As Label";
mostCurrent._lbldrivewaywifi = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 97;BA.debugLine="Private lblFrontDoorBatt As Label";
mostCurrent._lblfrontdoorbatt = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 98;BA.debugLine="Private lblFrontDoorTimestamp As Label";
mostCurrent._lblfrontdoortimestamp = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 99;BA.debugLine="Private lblFrontDoorWiFi As Label";
mostCurrent._lblfrontdoorwifi = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 100;BA.debugLine="Private lblSideYardBatt As Label";
mostCurrent._lblsideyardbatt = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 101;BA.debugLine="Private lblSideYardTimestamp As Label";
mostCurrent._lblsideyardtimestamp = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 102;BA.debugLine="Private lblSideYardWiFi As Label";
mostCurrent._lblsideyardwifi = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 103;BA.debugLine="Private lblSyncModule As Label";
mostCurrent._lblsyncmodule = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 104;BA.debugLine="Private B4XPageIndicator1 As B4XPageIndicator";
mostCurrent._b4xpageindicator1 = new cloyd.smart.home.monitor.b4xpageindicator();
 //BA.debugLineNum = 105;BA.debugLine="Private ivScreenshot As ImageView";
mostCurrent._ivscreenshot = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 106;BA.debugLine="Private lblDate As B4XView";
mostCurrent._lbldate = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 107;BA.debugLine="Private lblDeviceInfo As B4XView";
mostCurrent._lbldeviceinfo = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 108;BA.debugLine="Private lblFileInfo As B4XView";
mostCurrent._lblfileinfo = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 109;BA.debugLine="Private clvActivity As CustomListView";
mostCurrent._clvactivity = new b4a.example3.customlistview();
 //BA.debugLineNum = 110;BA.debugLine="Private wvMedia As WebView";
mostCurrent._wvmedia = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 111;BA.debugLine="Private WebViewSettings1 As WebViewSettings";
mostCurrent._webviewsettings1 = new uk.co.martinpearman.b4a.webviewsettings.WebViewSettings();
 //BA.debugLineNum = 112;BA.debugLine="Private B4XLoadingIndicator4 As B4XLoadingIndicat";
mostCurrent._b4xloadingindicator4 = new cloyd.smart.home.monitor.b4xloadingindicator();
 //BA.debugLineNum = 113;BA.debugLine="Private lblDuration As Label";
mostCurrent._lblduration = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 114;BA.debugLine="Private ivWatched As ImageView";
mostCurrent._ivwatched = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 115;BA.debugLine="Private badger1 As Badger";
mostCurrent._badger1 = new cloyd.smart.home.monitor.badger();
 //BA.debugLineNum = 116;BA.debugLine="Private btnDrivewayNewClip As ImageView";
mostCurrent._btndrivewaynewclip = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 117;BA.debugLine="Private btnFrontDoorNewClip As ImageView";
mostCurrent._btnfrontdoornewclip = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 118;BA.debugLine="Private btnSideYardNewClip As ImageView";
mostCurrent._btnsideyardnewclip = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 119;BA.debugLine="Private btnRefresh As SwiftButton";
mostCurrent._btnrefresh = new cloyd.smart.home.monitor.swiftbutton();
 //BA.debugLineNum = 120;BA.debugLine="Private btnDriveway As SwiftButton";
mostCurrent._btndriveway = new cloyd.smart.home.monitor.swiftbutton();
 //BA.debugLineNum = 121;BA.debugLine="Private lblMediaURL As B4XView";
mostCurrent._lblmediaurl = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 122;BA.debugLine="Private btnDrivewayFullScreenshot As ImageView";
mostCurrent._btndrivewayfullscreenshot = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 123;BA.debugLine="Private btnFrontDoorFullScreenshot As ImageView";
mostCurrent._btnfrontdoorfullscreenshot = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 124;BA.debugLine="Private btnSideYardFullScreenshot As ImageView";
mostCurrent._btnsideyardfullscreenshot = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 125;BA.debugLine="Private ivPlay As ImageView";
mostCurrent._ivplay = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 126;BA.debugLine="End Sub";
return "";
}
public static String  _handlesettings() throws Exception{
 //BA.debugLineNum = 1105;BA.debugLine="Sub HandleSettings";
 //BA.debugLineNum = 1106;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTime",_manager.GetString("TempHumidityCooldownTime"));
 //BA.debugLineNum = 1107;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTimeBasement",_manager.GetString("TempHumidityCooldownTimeBasement"));
 //BA.debugLineNum = 1108;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",manage";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue",_manager.GetString("HumidityAddValue"));
 //BA.debugLineNum = 1109;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime",_manager.GetString("SensorNotRespondingTime"));
 //BA.debugLineNum = 1110;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 1111;BA.debugLine="End Sub";
return "";
}
public static String  _hideping() throws Exception{
 //BA.debugLineNum = 663;BA.debugLine="Private Sub HidePing";
 //BA.debugLineNum = 664;BA.debugLine="lblPing.SetVisibleAnimated(200, False)";
mostCurrent._lblping.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 665;BA.debugLine="End Sub";
return "";
}
public static String  _hidepingbasement() throws Exception{
 //BA.debugLineNum = 667;BA.debugLine="Private Sub HidePingBasement";
 //BA.debugLineNum = 668;BA.debugLine="lblPingBasement.SetVisibleAnimated(200, False)";
mostCurrent._lblpingbasement.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 669;BA.debugLine="End Sub";
return "";
}
public static String  _ivdriveway_click() throws Exception{
 //BA.debugLineNum = 2033;BA.debugLine="Sub ivDriveway_Click";
 //BA.debugLineNum = 2034;BA.debugLine="lblDrivewayBatt.Visible = Not(lblDrivewayBatt.Vis";
mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaybatt.getVisible()));
 //BA.debugLineNum = 2035;BA.debugLine="lblDrivewayTimestamp.Visible = Not(lblDrivewayTim";
mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaytimestamp.getVisible()));
 //BA.debugLineNum = 2036;BA.debugLine="lblDrivewayWifi.Visible = Not(lblDrivewayWifi.Vis";
mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaywifi.getVisible()));
 //BA.debugLineNum = 2037;BA.debugLine="lblDriveway.Visible = Not(lblDriveway.Visible)";
mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldriveway.getVisible()));
 //BA.debugLineNum = 2038;BA.debugLine="lblFrontDoorBatt.Visible = Not(lblFrontDoorBatt.V";
mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorbatt.getVisible()));
 //BA.debugLineNum = 2039;BA.debugLine="lblFrontDoorTimestamp.Visible = Not(lblFrontDoorT";
mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoortimestamp.getVisible()));
 //BA.debugLineNum = 2040;BA.debugLine="lblFrontDoorWiFi.Visible = Not(lblFrontDoorWiFi.V";
mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorwifi.getVisible()));
 //BA.debugLineNum = 2041;BA.debugLine="lblFrontDoor.Visible = Not(lblFrontDoor.Visible)";
mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoor.getVisible()));
 //BA.debugLineNum = 2042;BA.debugLine="lblSideYardBatt.Visible = Not(lblSideYardBatt.Vis";
mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardbatt.getVisible()));
 //BA.debugLineNum = 2043;BA.debugLine="lblSideYardTimestamp.Visible = Not(lblSideYardTim";
mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardtimestamp.getVisible()));
 //BA.debugLineNum = 2044;BA.debugLine="lblSideYardWiFi.Visible = Not(lblSideYardWiFi.Vis";
mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardwifi.getVisible()));
 //BA.debugLineNum = 2045;BA.debugLine="lblSideYard.Visible = Not(lblSideYard.Visible)";
mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyard.getVisible()));
 //BA.debugLineNum = 2046;BA.debugLine="btnDrivewayNewClip.Visible = Not(btnDrivewayNewCl";
mostCurrent._btndrivewaynewclip.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btndrivewaynewclip.getVisible()));
 //BA.debugLineNum = 2047;BA.debugLine="btnFrontDoorNewClip.Visible = Not(btnFrontDoorNew";
mostCurrent._btnfrontdoornewclip.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnfrontdoornewclip.getVisible()));
 //BA.debugLineNum = 2048;BA.debugLine="btnSideYardNewClip.Visible = Not(btnSideYardNewCl";
mostCurrent._btnsideyardnewclip.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnsideyardnewclip.getVisible()));
 //BA.debugLineNum = 2049;BA.debugLine="btnDrivewayFullScreenshot.Visible = Not(btnDrivew";
mostCurrent._btndrivewayfullscreenshot.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btndrivewayfullscreenshot.getVisible()));
 //BA.debugLineNum = 2050;BA.debugLine="btnFrontDoorFullScreenshot.Visible = Not(btnFront";
mostCurrent._btnfrontdoorfullscreenshot.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnfrontdoorfullscreenshot.getVisible()));
 //BA.debugLineNum = 2051;BA.debugLine="btnSideYardFullScreenshot.Visible = Not(btnSideYa";
mostCurrent._btnsideyardfullscreenshot.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnsideyardfullscreenshot.getVisible()));
 //BA.debugLineNum = 2052;BA.debugLine="End Sub";
return "";
}
public static String  _ivfrontdoor_click() throws Exception{
 //BA.debugLineNum = 2012;BA.debugLine="Sub ivFrontDoor_Click";
 //BA.debugLineNum = 2013;BA.debugLine="lblDrivewayBatt.Visible = Not(lblDrivewayBatt.Vis";
mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaybatt.getVisible()));
 //BA.debugLineNum = 2014;BA.debugLine="lblDrivewayTimestamp.Visible = Not(lblDrivewayTim";
mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaytimestamp.getVisible()));
 //BA.debugLineNum = 2015;BA.debugLine="lblDrivewayWifi.Visible = Not(lblDrivewayWifi.Vis";
mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaywifi.getVisible()));
 //BA.debugLineNum = 2016;BA.debugLine="lblDriveway.Visible = Not(lblDriveway.Visible)";
mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldriveway.getVisible()));
 //BA.debugLineNum = 2017;BA.debugLine="lblFrontDoorBatt.Visible = Not(lblFrontDoorBatt.V";
mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorbatt.getVisible()));
 //BA.debugLineNum = 2018;BA.debugLine="lblFrontDoorTimestamp.Visible = Not(lblFrontDoorT";
mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoortimestamp.getVisible()));
 //BA.debugLineNum = 2019;BA.debugLine="lblFrontDoorWiFi.Visible = Not(lblFrontDoorWiFi.V";
mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorwifi.getVisible()));
 //BA.debugLineNum = 2020;BA.debugLine="lblFrontDoor.Visible = Not(lblFrontDoor.Visible)";
mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoor.getVisible()));
 //BA.debugLineNum = 2021;BA.debugLine="lblSideYardBatt.Visible = Not(lblSideYardBatt.Vis";
mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardbatt.getVisible()));
 //BA.debugLineNum = 2022;BA.debugLine="lblSideYardTimestamp.Visible = Not(lblSideYardTim";
mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardtimestamp.getVisible()));
 //BA.debugLineNum = 2023;BA.debugLine="lblSideYardWiFi.Visible = Not(lblSideYardWiFi.Vis";
mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardwifi.getVisible()));
 //BA.debugLineNum = 2024;BA.debugLine="lblSideYard.Visible = Not(lblSideYard.Visible)";
mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyard.getVisible()));
 //BA.debugLineNum = 2025;BA.debugLine="btnDrivewayNewClip.Visible = Not(btnDrivewayNewCl";
mostCurrent._btndrivewaynewclip.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btndrivewaynewclip.getVisible()));
 //BA.debugLineNum = 2026;BA.debugLine="btnFrontDoorNewClip.Visible = Not(btnFrontDoorNew";
mostCurrent._btnfrontdoornewclip.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnfrontdoornewclip.getVisible()));
 //BA.debugLineNum = 2027;BA.debugLine="btnSideYardNewClip.Visible = Not(btnSideYardNewCl";
mostCurrent._btnsideyardnewclip.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnsideyardnewclip.getVisible()));
 //BA.debugLineNum = 2028;BA.debugLine="btnDrivewayFullScreenshot.Visible = Not(btnDrivew";
mostCurrent._btndrivewayfullscreenshot.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btndrivewayfullscreenshot.getVisible()));
 //BA.debugLineNum = 2029;BA.debugLine="btnFrontDoorFullScreenshot.Visible = Not(btnFront";
mostCurrent._btnfrontdoorfullscreenshot.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnfrontdoorfullscreenshot.getVisible()));
 //BA.debugLineNum = 2030;BA.debugLine="btnSideYardFullScreenshot.Visible = Not(btnSideYa";
mostCurrent._btnsideyardfullscreenshot.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnsideyardfullscreenshot.getVisible()));
 //BA.debugLineNum = 2031;BA.debugLine="End Sub";
return "";
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
 //BA.debugLineNum = 2938;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 10;
this.catchState = 9;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 9;
 //BA.debugLineNum = 2939;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 2940;BA.debugLine="p.SetMute(3,True)";
_p.SetMute((int) (3),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2942;BA.debugLine="B4XLoadingIndicator4.Show";
parent.mostCurrent._b4xloadingindicator4._show /*String*/ ();
 //BA.debugLineNum = 2943;BA.debugLine="wvMedia.LoadUrl(\"\")";
parent.mostCurrent._wvmedia.LoadUrl("");
 //BA.debugLineNum = 2945;BA.debugLine="Dim cd As CardData = clvActivity.GetValue(previo";
_cd = (cloyd.smart.home.monitor.main._carddata)(parent.mostCurrent._clvactivity._getvalue(parent._previousselectedindex));
 //BA.debugLineNum = 2946;BA.debugLine="Dim rs As ResumableSub = ShowVideo(cd.mediaURL)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _showvideo(_cd.mediaURL /*String*/ );
 //BA.debugLineNum = 2947;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 11;
return;
case 11:
//C
this.state = 4;
_result = (Object) result[0];
;
 //BA.debugLineNum = 2949;BA.debugLine="Dim FileName As String = \"media.mp4\"";
_filename = "media.mp4";
 //BA.debugLineNum = 2950;BA.debugLine="If File.Exists(File.DirInternal, FileName) Then";
if (true) break;

case 4:
//if
this.state = 7;
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename)) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2951;BA.debugLine="File.Copy(File.DirInternal, FileName, Starter.P";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename,parent.mostCurrent._starter._provider /*cloyd.smart.home.monitor.fileprovider*/ ._sharedfolder /*String*/ ,_filename);
 //BA.debugLineNum = 2952;BA.debugLine="Dim in As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 2953;BA.debugLine="in.Initialize(in.ACTION_VIEW, \"\")";
_in.Initialize(_in.ACTION_VIEW,"");
 //BA.debugLineNum = 2954;BA.debugLine="Starter.Provider.SetFileUriAsIntentData(in, Fil";
parent.mostCurrent._starter._provider /*cloyd.smart.home.monitor.fileprovider*/ ._setfileuriasintentdata /*String*/ (_in,_filename);
 //BA.debugLineNum = 2956;BA.debugLine="in.SetType(\"video/*\")";
_in.SetType("video/*");
 //BA.debugLineNum = 2957;BA.debugLine="StartActivity(in)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_in.getObject()));
 if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 2959;BA.debugLine="B4XLoadingIndicator4.Hide";
parent.mostCurrent._b4xloadingindicator4._hide /*String*/ ();
 if (true) break;

case 9:
//C
this.state = 10;
this.catchState = 0;
 //BA.debugLineNum = 2961;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("45242904",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 10:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 2963;BA.debugLine="End Sub";
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
public static String  _ivsideyard_click() throws Exception{
 //BA.debugLineNum = 1991;BA.debugLine="Sub ivSideYard_Click";
 //BA.debugLineNum = 1992;BA.debugLine="lblDrivewayBatt.Visible = Not(lblDrivewayBatt.Vis";
mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaybatt.getVisible()));
 //BA.debugLineNum = 1993;BA.debugLine="lblDrivewayTimestamp.Visible = Not(lblDrivewayTim";
mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaytimestamp.getVisible()));
 //BA.debugLineNum = 1994;BA.debugLine="lblDrivewayWifi.Visible = Not(lblDrivewayWifi.Vis";
mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldrivewaywifi.getVisible()));
 //BA.debugLineNum = 1995;BA.debugLine="lblDriveway.Visible = Not(lblDriveway.Visible)";
mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lbldriveway.getVisible()));
 //BA.debugLineNum = 1996;BA.debugLine="lblFrontDoorBatt.Visible = Not(lblFrontDoorBatt.V";
mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorbatt.getVisible()));
 //BA.debugLineNum = 1997;BA.debugLine="lblFrontDoorTimestamp.Visible = Not(lblFrontDoorT";
mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoortimestamp.getVisible()));
 //BA.debugLineNum = 1998;BA.debugLine="lblFrontDoorWiFi.Visible = Not(lblFrontDoorWiFi.V";
mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoorwifi.getVisible()));
 //BA.debugLineNum = 1999;BA.debugLine="lblFrontDoor.Visible = Not(lblFrontDoor.Visible)";
mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblfrontdoor.getVisible()));
 //BA.debugLineNum = 2000;BA.debugLine="lblSideYardBatt.Visible = Not(lblSideYardBatt.Vis";
mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardbatt.getVisible()));
 //BA.debugLineNum = 2001;BA.debugLine="lblSideYardTimestamp.Visible = Not(lblSideYardTim";
mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardtimestamp.getVisible()));
 //BA.debugLineNum = 2002;BA.debugLine="lblSideYardWiFi.Visible = Not(lblSideYardWiFi.Vis";
mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyardwifi.getVisible()));
 //BA.debugLineNum = 2003;BA.debugLine="lblSideYard.Visible = Not(lblSideYard.Visible)";
mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblsideyard.getVisible()));
 //BA.debugLineNum = 2004;BA.debugLine="btnDrivewayNewClip.Visible = Not(btnDrivewayNewCl";
mostCurrent._btndrivewaynewclip.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btndrivewaynewclip.getVisible()));
 //BA.debugLineNum = 2005;BA.debugLine="btnFrontDoorNewClip.Visible = Not(btnFrontDoorNew";
mostCurrent._btnfrontdoornewclip.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnfrontdoornewclip.getVisible()));
 //BA.debugLineNum = 2006;BA.debugLine="btnSideYardNewClip.Visible = Not(btnSideYardNewCl";
mostCurrent._btnsideyardnewclip.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnsideyardnewclip.getVisible()));
 //BA.debugLineNum = 2007;BA.debugLine="btnDrivewayFullScreenshot.Visible = Not(btnDrivew";
mostCurrent._btndrivewayfullscreenshot.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btndrivewayfullscreenshot.getVisible()));
 //BA.debugLineNum = 2008;BA.debugLine="btnFrontDoorFullScreenshot.Visible = Not(btnFront";
mostCurrent._btnfrontdoorfullscreenshot.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnfrontdoorfullscreenshot.getVisible()));
 //BA.debugLineNum = 2009;BA.debugLine="btnSideYardFullScreenshot.Visible = Not(btnSideYa";
mostCurrent._btnsideyardfullscreenshot.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._btnsideyardfullscreenshot.getVisible()));
 //BA.debugLineNum = 2010;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connect() throws Exception{
String _clientid = "";
anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper _connopt = null;
 //BA.debugLineNum = 412;BA.debugLine="Sub MQTT_Connect";
 //BA.debugLineNum = 413;BA.debugLine="Try";
try { //BA.debugLineNum = 414;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'crea";
_clientid = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (999999999)));
 //BA.debugLineNum = 415;BA.debugLine="MQTT.Initialize(\"MQTT\", MQTTServerURI, ClientId)";
_mqtt.Initialize(processBA,"MQTT",_mqttserveruri,_clientid);
 //BA.debugLineNum = 417;BA.debugLine="Dim ConnOpt As MqttConnectOptions";
_connopt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper();
 //BA.debugLineNum = 418;BA.debugLine="ConnOpt.Initialize(MQTTUser, MQTTPassword)";
_connopt.Initialize(_mqttuser,_mqttpassword);
 //BA.debugLineNum = 419;BA.debugLine="MQTT.Connect2(ConnOpt)";
_mqtt.Connect2((org.eclipse.paho.client.mqttv3.MqttConnectOptions)(_connopt.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 421;BA.debugLine="Log(\"MQTT_Connect: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("4720905","MQTT_Connect: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 423;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 425;BA.debugLine="Sub MQTT_Connected (Success As Boolean)";
 //BA.debugLineNum = 426;BA.debugLine="Try";
try { //BA.debugLineNum = 427;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 428;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("4786435",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 429;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 }else {
 //BA.debugLineNum = 431;BA.debugLine="Log(\"Connected to MQTT broker\")";
anywheresoftware.b4a.keywords.Common.LogImpl("4786438","Connected to MQTT broker",0);
 //BA.debugLineNum = 432;BA.debugLine="MQTT.Subscribe(\"TempHumid\", 0)";
_mqtt.Subscribe("TempHumid",(int) (0));
 //BA.debugLineNum = 433;BA.debugLine="MQTT.Subscribe(\"MQ7\", 0)";
_mqtt.Subscribe("MQ7",(int) (0));
 //BA.debugLineNum = 434;BA.debugLine="MQTT.Subscribe(\"MQ7Basement\", 0)";
_mqtt.Subscribe("MQ7Basement",(int) (0));
 //BA.debugLineNum = 435;BA.debugLine="MQTT.Subscribe(\"TempHumidBasement\", 0)";
_mqtt.Subscribe("TempHumidBasement",(int) (0));
 //BA.debugLineNum = 436;BA.debugLine="MQTT.Subscribe(\"HumidityAddValue\", 0)";
_mqtt.Subscribe("HumidityAddValue",(int) (0));
 };
 } 
       catch (Exception e14) {
			processBA.setLastException(e14); //BA.debugLineNum = 439;BA.debugLine="Log(\"MQTT_Connected: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("4786446","MQTT_Connected: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 441;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_disconnected() throws Exception{
 //BA.debugLineNum = 443;BA.debugLine="Private Sub MQTT_Disconnected";
 //BA.debugLineNum = 444;BA.debugLine="Try";
try { //BA.debugLineNum = 445;BA.debugLine="gblACMenu.Clear";
mostCurrent._gblacmenu.Clear();
 //BA.debugLineNum = 446;BA.debugLine="gblACMenu.Add(0, 0, \"Settings\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Settings"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 447;BA.debugLine="gblACMenu.Add(0, 0, \"Show KeyValueStore records";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Show KeyValueStore records numbers"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 448;BA.debugLine="gblACMenu.Add(0, 0, \"Remove all KeyValueStore re";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Remove all KeyValueStore records"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 449;BA.debugLine="gblACMenu.Add(0, 0, \"About\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 450;BA.debugLine="Log(\"Disconnected from MQTT broker\")";
anywheresoftware.b4a.keywords.Common.LogImpl("4851975","Disconnected from MQTT broker",0);
 //BA.debugLineNum = 451;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e10) {
			processBA.setLastException(e10); //BA.debugLineNum = 453;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("4851978",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 455;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
 //BA.debugLineNum = 457;BA.debugLine="Private Sub MQTT_MessageArrived (Topic As String,";
 //BA.debugLineNum = 458;BA.debugLine="Try";
try { //BA.debugLineNum = 459;BA.debugLine="If Topic = \"TempHumid\" Then";
if ((_topic).equals("TempHumid")) { 
 //BA.debugLineNum = 460;BA.debugLine="lblPing.SetVisibleAnimated(500, True)";
mostCurrent._lblping.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 461;BA.debugLine="csu.CallSubPlus(Me, \"HidePing\", 700)";
_csu._v7(main.getObject(),"HidePing",(int) (700));
 //BA.debugLineNum = 462;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 }else if((_topic).equals("MQ7")) { 
 //BA.debugLineNum = 464;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 }else if((_topic).equals("MQ7Basement")) { 
 //BA.debugLineNum = 466;BA.debugLine="CheckAirQualitySettingBasement";
_checkairqualitysettingbasement();
 }else if((_topic).equals("TempHumidBasement")) { 
 //BA.debugLineNum = 468;BA.debugLine="lblPingBasement.SetVisibleAnimated(500, True)";
mostCurrent._lblpingbasement.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 469;BA.debugLine="csu.CallSubPlus(Me, \"HidePingBasement\", 700)";
_csu._v7(main.getObject(),"HidePingBasement",(int) (700));
 //BA.debugLineNum = 471;BA.debugLine="CheckTempHumiditySettingBasement";
_checktemphumiditysettingbasement();
 };
 } 
       catch (Exception e16) {
			processBA.setLastException(e16); //BA.debugLineNum = 474;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("4917521",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 476;BA.debugLine="End Sub";
return "";
}
public static long  _parseutcstring(String _utc) throws Exception{
String _df = "";
long _res = 0L;
 //BA.debugLineNum = 1868;BA.debugLine="Sub ParseUTCstring(utc As String) As Long";
 //BA.debugLineNum = 1869;BA.debugLine="Dim df As String = DateTime.DateFormat";
_df = anywheresoftware.b4a.keywords.Common.DateTime.getDateFormat();
 //BA.debugLineNum = 1870;BA.debugLine="Dim res As Long";
_res = 0L;
 //BA.debugLineNum = 1871;BA.debugLine="If utc.CharAt(10) = \"T\" Then";
if (_utc.charAt((int) (10))==BA.ObjectToChar("T")) { 
 //BA.debugLineNum = 1873;BA.debugLine="If utc.CharAt(19) = \".\" Then utc = utc.SubString";
if (_utc.charAt((int) (19))==BA.ObjectToChar(".")) { 
_utc = _utc.substring((int) (0),(int) (19))+"+0000";};
 //BA.debugLineNum = 1874;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd'T'HH:mm:ssZ\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
 }else {
 //BA.debugLineNum = 1877;BA.debugLine="DateTime.DateFormat = \"EEE MMM dd HH:mm:ss Z yyy";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
 };
 //BA.debugLineNum = 1879;BA.debugLine="Try";
try { //BA.debugLineNum = 1880;BA.debugLine="res = DateTime.DateParse(utc)";
_res = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(_utc);
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 1882;BA.debugLine="res = 0";
_res = (long) (0);
 };
 //BA.debugLineNum = 1884;BA.debugLine="DateTime.DateFormat = df";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat(_df);
 //BA.debugLineNum = 1885;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 1886;BA.debugLine="End Sub";
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
starter._process_globals();
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
_emailaddress = "antimone2001@hotmail.com";
 //BA.debugLineNum = 31;BA.debugLine="Private password As String = \"[redacted] # replace with the empty string\"";
_password = "[redacted] # replace with the empty string";
 //BA.debugLineNum = 32;BA.debugLine="Private authToken As String";
_authtoken = "";
 //BA.debugLineNum = 33;BA.debugLine="Private userRegion As String = \"u006\"";
_userregion = "u006";
 //BA.debugLineNum = 34;BA.debugLine="Private accountID As String = \"88438\" 'ignore";
_accountid = "88438";
 //BA.debugLineNum = 35;BA.debugLine="Private networkID As String = \"94896\"";
_networkid = "94896";
 //BA.debugLineNum = 36;BA.debugLine="Private commandID As String";
_commandid = "";
 //BA.debugLineNum = 37;BA.debugLine="Private commandComplete As Boolean";
_commandcomplete = false;
 //BA.debugLineNum = 38;BA.debugLine="Private cameraThumbnail As String";
_camerathumbnail = "";
 //BA.debugLineNum = 39;BA.debugLine="Private response As String";
_response = "";
 //BA.debugLineNum = 40;BA.debugLine="Private previousSelectedIndex As Int";
_previousselectedindex = 0;
 //BA.debugLineNum = 41;BA.debugLine="Private mediaMetaData As cMediaData";
_mediametadata = new cloyd.smart.home.monitor.cmediadata();
 //BA.debugLineNum = 42;BA.debugLine="Private drivewayArmedStatus As String";
_drivewayarmedstatus = "";
 //BA.debugLineNum = 43;BA.debugLine="Private frontdoorArmedStatus As String";
_frontdoorarmedstatus = "";
 //BA.debugLineNum = 44;BA.debugLine="Private sideyardArmedStatus As String";
_sideyardarmedstatus = "";
 //BA.debugLineNum = 45;BA.debugLine="Private tabStripCurrentPage As Boolean = False";
_tabstripcurrentpage = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 46;BA.debugLine="Type CardData (screenshot As B4XBitmap,filedate A";
;
 //BA.debugLineNum = 47;BA.debugLine="Type VideoInfo (ThumbnailPath As String, DateCrea";
;
 //BA.debugLineNum = 48;BA.debugLine="Public isThereUnwatchedVideo As Boolean";
_isthereunwatchedvideo = false;
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _refreshcameras(boolean _firstrun) throws Exception{
ResumableSub_RefreshCameras rsub = new ResumableSub_RefreshCameras(null,_firstrun);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
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
anywheresoftware.b4a.objects.collections.List _links = null;
int _attempts = 0;
int _i = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
Object _result = null;
String _link = "";
int step8;
int limit8;
int step28;
int limit28;
anywheresoftware.b4a.BA.IterableList group61;
int index61;
int groupLen61;
int step91;
int limit91;
int step102;
int limit102;
int step122;
int limit122;
int step140;
int limit140;
int step144;
int limit144;

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
 //BA.debugLineNum = 1166;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 148;
this.catchState = 147;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 147;
 //BA.debugLineNum = 1167;BA.debugLine="Dim camera As String";
_camera = "";
 //BA.debugLineNum = 1168;BA.debugLine="Dim iv As ImageView";
_iv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 1169;BA.debugLine="Dim links As List";
_links = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1170;BA.debugLine="Dim attempts As Int";
_attempts = 0;
 //BA.debugLineNum = 1171;BA.debugLine="links = Array(\"347574\", \"236967\", \"226821\")";
_links = anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)("347574"),(Object)("236967"),(Object)("226821")});
 //BA.debugLineNum = 1173;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia-";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/syncmodules");
 //BA.debugLineNum = 1174;BA.debugLine="For i = 1 To 20";
if (true) break;

case 4:
//for
this.state = 13;
step8 = 1;
limit8 = (int) (20);
_i = (int) (1) ;
this.state = 149;
if (true) break;

case 149:
//C
this.state = 13;
if ((step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8)) this.state = 6;
if (true) break;

case 150:
//C
this.state = 149;
_i = ((int)(0 + _i + step8)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1175;BA.debugLine="If response = \"\" Then";
if (true) break;

case 7:
//if
this.state = 12;
if ((parent._response).equals("")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 1176;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 151;
return;
case 151:
//C
this.state = 12;
;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1178;BA.debugLine="Exit";
this.state = 13;
if (true) break;
 if (true) break;

case 12:
//C
this.state = 150;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1181;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.Co";

case 13:
//if
this.state = 16;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1182;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1183;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1184;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1185;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1186;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1187;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1188;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1189;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1190;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 16:
//C
this.state = 17;
;
 //BA.debugLineNum = 1192;BA.debugLine="GetSyncModuleInfo(response)";
_getsyncmoduleinfo(parent._response);
 //BA.debugLineNum = 1195;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedia-";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/homescreen");
 //BA.debugLineNum = 1196;BA.debugLine="For i = 1 To 20";
if (true) break;

case 17:
//for
this.state = 26;
step28 = 1;
limit28 = (int) (20);
_i = (int) (1) ;
this.state = 152;
if (true) break;

case 152:
//C
this.state = 26;
if ((step28 > 0 && _i <= limit28) || (step28 < 0 && _i >= limit28)) this.state = 19;
if (true) break;

case 153:
//C
this.state = 152;
_i = ((int)(0 + _i + step28)) ;
if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1197;BA.debugLine="If response = \"\" Then";
if (true) break;

case 20:
//if
this.state = 25;
if ((parent._response).equals("")) { 
this.state = 22;
}else {
this.state = 24;
}if (true) break;

case 22:
//C
this.state = 25;
 //BA.debugLineNum = 1198;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 154;
return;
case 154:
//C
this.state = 25;
;
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1200;BA.debugLine="Exit";
this.state = 26;
if (true) break;
 if (true) break;

case 25:
//C
this.state = 153;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1203;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.Co";

case 26:
//if
this.state = 29;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 28;
}if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 1204;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1205;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1206;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1207;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1208;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1209;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1210;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1211;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1212;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 29:
//C
this.state = 30;
;
 //BA.debugLineNum = 1214;BA.debugLine="GetHomescreen(response)";
_gethomescreen(parent._response);
 //BA.debugLineNum = 1217;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 1218;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 155;
return;
case 155:
//C
this.state = 30;
_result = (Object) result[0];
;
 //BA.debugLineNum = 1220;BA.debugLine="lblDrivewayBatt.Visible = True";
parent.mostCurrent._lbldrivewaybatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1221;BA.debugLine="lblDrivewayTimestamp.Visible = True";
parent.mostCurrent._lbldrivewaytimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1222;BA.debugLine="lblDrivewayWifi.Visible = True";
parent.mostCurrent._lbldrivewaywifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1223;BA.debugLine="lblDriveway.Visible = True";
parent.mostCurrent._lbldriveway.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1224;BA.debugLine="lblFrontDoorBatt.Visible = True";
parent.mostCurrent._lblfrontdoorbatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1225;BA.debugLine="lblFrontDoorTimestamp.Visible = True";
parent.mostCurrent._lblfrontdoortimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1226;BA.debugLine="lblFrontDoorWiFi.Visible = True";
parent.mostCurrent._lblfrontdoorwifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1227;BA.debugLine="lblFrontDoor.Visible = True";
parent.mostCurrent._lblfrontdoor.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1228;BA.debugLine="lblSideYardBatt.Visible = True";
parent.mostCurrent._lblsideyardbatt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1229;BA.debugLine="lblSideYardTimestamp.Visible = True";
parent.mostCurrent._lblsideyardtimestamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1230;BA.debugLine="lblSideYardWiFi.Visible = True";
parent.mostCurrent._lblsideyardwifi.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1231;BA.debugLine="lblSideYard.Visible = True";
parent.mostCurrent._lblsideyard.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1233;BA.debugLine="For Each link As String In links";
if (true) break;

case 30:
//for
this.state = 145;
group61 = _links;
index61 = 0;
groupLen61 = group61.getSize();
this.state = 156;
if (true) break;

case 156:
//C
this.state = 145;
if (index61 < groupLen61) {
this.state = 32;
_link = BA.ObjectToString(group61.Get(index61));}
if (true) break;

case 157:
//C
this.state = 156;
index61++;
if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 1234;BA.debugLine="camera = link";
_camera = _link;
 //BA.debugLineNum = 1235;BA.debugLine="If FirstRun Then";
if (true) break;

case 33:
//if
this.state = 54;
if (_firstrun) { 
this.state = 35;
}else {
this.state = 45;
}if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 1236;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 36:
//if
this.state = 43;
if ((_camera).equals("347574")) { 
this.state = 38;
}else if((_camera).equals("236967")) { 
this.state = 40;
}else if((_camera).equals("226821")) { 
this.state = 42;
}if (true) break;

case 38:
//C
this.state = 43;
 //BA.debugLineNum = 1237;BA.debugLine="lblStatus.Text = \"Retrieving Driveway thumbna";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Driveway thumbnail..."));
 //BA.debugLineNum = 1238;BA.debugLine="iv = ivDriveway";
_iv = parent.mostCurrent._ivdriveway;
 if (true) break;

case 40:
//C
this.state = 43;
 //BA.debugLineNum = 1240;BA.debugLine="lblStatus.Text = \"Retrieving Front Door thumb";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Front Door thumbnail..."));
 //BA.debugLineNum = 1241;BA.debugLine="iv = ivFrontDoor";
_iv = parent.mostCurrent._ivfrontdoor;
 if (true) break;

case 42:
//C
this.state = 43;
 //BA.debugLineNum = 1243;BA.debugLine="lblStatus.Text = \"Retrieving Side Yard thumbn";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Retrieving Side Yard thumbnail..."));
 //BA.debugLineNum = 1244;BA.debugLine="iv = ivSideYard";
_iv = parent.mostCurrent._ivsideyard;
 if (true) break;

case 43:
//C
this.state = 54;
;
 if (true) break;

case 45:
//C
this.state = 46;
 //BA.debugLineNum = 1247;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 46:
//if
this.state = 53;
if ((_camera).equals("347574")) { 
this.state = 48;
}else if((_camera).equals("236967")) { 
this.state = 50;
}else if((_camera).equals("226821")) { 
this.state = 52;
}if (true) break;

case 48:
//C
this.state = 53;
 //BA.debugLineNum = 1248;BA.debugLine="lblStatus.Text = \"Capturing a new Driveway th";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Driveway thumbnail..."));
 //BA.debugLineNum = 1249;BA.debugLine="iv = ivDriveway";
_iv = parent.mostCurrent._ivdriveway;
 //BA.debugLineNum = 1250;BA.debugLine="attempts = 10";
_attempts = (int) (10);
 if (true) break;

case 50:
//C
this.state = 53;
 //BA.debugLineNum = 1252;BA.debugLine="lblStatus.Text = \"Capturing a new Front Door";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Front Door thumbnail..."));
 //BA.debugLineNum = 1253;BA.debugLine="iv = ivFrontDoor";
_iv = parent.mostCurrent._ivfrontdoor;
 //BA.debugLineNum = 1254;BA.debugLine="attempts = 15";
_attempts = (int) (15);
 if (true) break;

case 52:
//C
this.state = 53;
 //BA.debugLineNum = 1256;BA.debugLine="lblStatus.Text = \"Capturing a new Side Yard t";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Capturing a new Side Yard thumbnail..."));
 //BA.debugLineNum = 1257;BA.debugLine="iv = ivSideYard";
_iv = parent.mostCurrent._ivsideyard;
 //BA.debugLineNum = 1258;BA.debugLine="attempts = 15";
_attempts = (int) (15);
 if (true) break;

case 53:
//C
this.state = 54;
;
 if (true) break;
;
 //BA.debugLineNum = 1262;BA.debugLine="If FirstRun Then";

case 54:
//if
this.state = 144;
if (_firstrun) { 
this.state = 56;
}else {
this.state = 68;
}if (true) break;

case 56:
//C
this.state = 57;
 //BA.debugLineNum = 1263;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immedi";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera);
 //BA.debugLineNum = 1264;BA.debugLine="For i = 1 To 20";
if (true) break;

case 57:
//for
this.state = 66;
step91 = 1;
limit91 = (int) (20);
_i = (int) (1) ;
this.state = 158;
if (true) break;

case 158:
//C
this.state = 66;
if ((step91 > 0 && _i <= limit91) || (step91 < 0 && _i >= limit91)) this.state = 59;
if (true) break;

case 159:
//C
this.state = 158;
_i = ((int)(0 + _i + step91)) ;
if (true) break;

case 59:
//C
this.state = 60;
 //BA.debugLineNum = 1265;BA.debugLine="If response = \"\" Then";
if (true) break;

case 60:
//if
this.state = 65;
if ((parent._response).equals("")) { 
this.state = 62;
}else {
this.state = 64;
}if (true) break;

case 62:
//C
this.state = 65;
 //BA.debugLineNum = 1266;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 160;
return;
case 160:
//C
this.state = 65;
;
 if (true) break;

case 64:
//C
this.state = 65;
 //BA.debugLineNum = 1268;BA.debugLine="Exit";
this.state = 66;
if (true) break;
 if (true) break;

case 65:
//C
this.state = 159;
;
 if (true) break;
if (true) break;

case 66:
//C
this.state = 144;
;
 //BA.debugLineNum = 1271;BA.debugLine="GetCameraInfo(response)";
_getcamerainfo(parent._response);
 //BA.debugLineNum = 1272;BA.debugLine="DownloadImage(\"https://rest-\" & userRegion &\".";
_downloadimage("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg",_iv,_camera);
 if (true) break;

case 68:
//C
this.state = 69;
 //BA.debugLineNum = 1274;BA.debugLine="RESTPost(\"https://rest-\" & userRegion &\".immed";
_restpost("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera+"/thumbnail");
 //BA.debugLineNum = 1275;BA.debugLine="For i = 1 To 20";
if (true) break;

case 69:
//for
this.state = 78;
step102 = 1;
limit102 = (int) (20);
_i = (int) (1) ;
this.state = 161;
if (true) break;

case 161:
//C
this.state = 78;
if ((step102 > 0 && _i <= limit102) || (step102 < 0 && _i >= limit102)) this.state = 71;
if (true) break;

case 162:
//C
this.state = 161;
_i = ((int)(0 + _i + step102)) ;
if (true) break;

case 71:
//C
this.state = 72;
 //BA.debugLineNum = 1276;BA.debugLine="If response = \"\" Then";
if (true) break;

case 72:
//if
this.state = 77;
if ((parent._response).equals("")) { 
this.state = 74;
}else {
this.state = 76;
}if (true) break;

case 74:
//C
this.state = 77;
 //BA.debugLineNum = 1277;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 163;
return;
case 163:
//C
this.state = 77;
;
 if (true) break;

case 76:
//C
this.state = 77;
 //BA.debugLineNum = 1279;BA.debugLine="Exit";
this.state = 78;
if (true) break;
 if (true) break;

case 77:
//C
this.state = 162;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1282;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.";

case 78:
//if
this.state = 143;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 80;
}else {
this.state = 82;
}if (true) break;

case 80:
//C
this.state = 143;
 //BA.debugLineNum = 1283;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1284;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1285;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1286;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1287;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1288;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1289;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1290;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1291;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 82:
//C
this.state = 83;
 //BA.debugLineNum = 1293;BA.debugLine="GetCommandID(response)";
_getcommandid(parent._response);
 //BA.debugLineNum = 1294;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".immed";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 1295;BA.debugLine="For i = 1 To 20";
if (true) break;

case 83:
//for
this.state = 92;
step122 = 1;
limit122 = (int) (20);
_i = (int) (1) ;
this.state = 164;
if (true) break;

case 164:
//C
this.state = 92;
if ((step122 > 0 && _i <= limit122) || (step122 < 0 && _i >= limit122)) this.state = 85;
if (true) break;

case 165:
//C
this.state = 164;
_i = ((int)(0 + _i + step122)) ;
if (true) break;

case 85:
//C
this.state = 86;
 //BA.debugLineNum = 1296;BA.debugLine="If response = \"\" Then";
if (true) break;

case 86:
//if
this.state = 91;
if ((parent._response).equals("")) { 
this.state = 88;
}else {
this.state = 90;
}if (true) break;

case 88:
//C
this.state = 91;
 //BA.debugLineNum = 1297;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 166;
return;
case 166:
//C
this.state = 91;
;
 if (true) break;

case 90:
//C
this.state = 91;
 //BA.debugLineNum = 1299;BA.debugLine="Exit";
this.state = 92;
if (true) break;
 if (true) break;

case 91:
//C
this.state = 165;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1302;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response";

case 92:
//if
this.state = 142;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 94;
}else {
this.state = 96;
}if (true) break;

case 94:
//C
this.state = 142;
 //BA.debugLineNum = 1303;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1304;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1305;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1306;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1307;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1308;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1309;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1310;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1311;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 96:
//C
this.state = 97;
 //BA.debugLineNum = 1313;BA.debugLine="For i = 1 To attempts";
if (true) break;

case 97:
//for
this.state = 128;
step140 = 1;
limit140 = _attempts;
_i = (int) (1) ;
this.state = 167;
if (true) break;

case 167:
//C
this.state = 128;
if ((step140 > 0 && _i <= limit140) || (step140 < 0 && _i >= limit140)) this.state = 99;
if (true) break;

case 168:
//C
this.state = 167;
_i = ((int)(0 + _i + step140)) ;
if (true) break;

case 99:
//C
this.state = 100;
 //BA.debugLineNum = 1314;BA.debugLine="GetCommandStatus(response)";
_getcommandstatus(parent._response);
 //BA.debugLineNum = 1315;BA.debugLine="If commandComplete Then";
if (true) break;

case 100:
//if
this.state = 123;
if (parent._commandcomplete) { 
this.state = 102;
}else {
this.state = 114;
}if (true) break;

case 102:
//C
this.state = 103;
 //BA.debugLineNum = 1316;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".im";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/camera/"+_camera);
 //BA.debugLineNum = 1317;BA.debugLine="For i = 1 To 20";
if (true) break;

case 103:
//for
this.state = 112;
step144 = 1;
limit144 = (int) (20);
_i = (int) (1) ;
this.state = 169;
if (true) break;

case 169:
//C
this.state = 112;
if ((step144 > 0 && _i <= limit144) || (step144 < 0 && _i >= limit144)) this.state = 105;
if (true) break;

case 170:
//C
this.state = 169;
_i = ((int)(0 + _i + step144)) ;
if (true) break;

case 105:
//C
this.state = 106;
 //BA.debugLineNum = 1318;BA.debugLine="If response = \"\" Then";
if (true) break;

case 106:
//if
this.state = 111;
if ((parent._response).equals("")) { 
this.state = 108;
}else {
this.state = 110;
}if (true) break;

case 108:
//C
this.state = 111;
 //BA.debugLineNum = 1319;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 171;
return;
case 171:
//C
this.state = 111;
;
 if (true) break;

case 110:
//C
this.state = 111;
 //BA.debugLineNum = 1321;BA.debugLine="Exit";
this.state = 112;
if (true) break;
 if (true) break;

case 111:
//C
this.state = 170;
;
 if (true) break;
if (true) break;

case 112:
//C
this.state = 123;
;
 //BA.debugLineNum = 1324;BA.debugLine="GetCameraInfo(response)";
_getcamerainfo(parent._response);
 //BA.debugLineNum = 1325;BA.debugLine="DownloadImage(\"https://rest-\" & userRegion";
_downloadimage("https://rest-"+parent._userregion+".immedia-semi.com/"+parent._camerathumbnail+".jpg",_iv,_camera);
 //BA.debugLineNum = 1326;BA.debugLine="Exit";
this.state = 128;
if (true) break;
 if (true) break;

case 114:
//C
this.state = 115;
 //BA.debugLineNum = 1328;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 115:
//if
this.state = 122;
if ((_camera).equals("347574")) { 
this.state = 117;
}else if((_camera).equals("236967")) { 
this.state = 119;
}else if((_camera).equals("226821")) { 
this.state = 121;
}if (true) break;

case 117:
//C
this.state = 122;
 //BA.debugLineNum = 1329;BA.debugLine="lblStatus.Text = \"Awaiting for the Drivew";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Driveway thumbnail... "+BA.NumberToString(_i)+"/"+BA.NumberToString(_attempts)));
 if (true) break;

case 119:
//C
this.state = 122;
 //BA.debugLineNum = 1331;BA.debugLine="lblStatus.Text = \"Awaiting for the Front";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Front Door thumbnail...  "+BA.NumberToString(_i)+"/"+BA.NumberToString(_attempts)));
 if (true) break;

case 121:
//C
this.state = 122;
 //BA.debugLineNum = 1333;BA.debugLine="lblStatus.Text = \"Awaiting for the Side Y";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Awaiting for the Side Yard thumbnail... "+BA.NumberToString(_i)+"/"+BA.NumberToString(_attempts)));
 if (true) break;

case 122:
//C
this.state = 123;
;
 if (true) break;

case 123:
//C
this.state = 124;
;
 //BA.debugLineNum = 1336;BA.debugLine="RESTGet(\"https://rest-\" & userRegion &\".imm";
_restget("https://rest-"+parent._userregion+".immedia-semi.com/network/"+parent._networkid+"/command/"+parent._commandid);
 //BA.debugLineNum = 1337;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 172;
return;
case 172:
//C
this.state = 124;
;
 //BA.debugLineNum = 1338;BA.debugLine="If response.StartsWith(\"ERROR: \") Or respon";
if (true) break;

case 124:
//if
this.state = 127;
if (parent._response.startsWith("ERROR: ") || parent._response.contains("System is busy, please wait")) { 
this.state = 126;
}if (true) break;

case 126:
//C
this.state = 127;
 //BA.debugLineNum = 1339;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1340;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1341;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1342;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1343;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1344;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1345;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1346;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1347;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 127:
//C
this.state = 168;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1350;BA.debugLine="If commandComplete = False Then";

case 128:
//if
this.state = 141;
if (parent._commandcomplete==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 130;
}else {
this.state = 140;
}if (true) break;

case 130:
//C
this.state = 131;
 //BA.debugLineNum = 1351;BA.debugLine="If camera = \"347574\" Then";
if (true) break;

case 131:
//if
this.state = 138;
if ((_camera).equals("347574")) { 
this.state = 133;
}else if((_camera).equals("236967")) { 
this.state = 135;
}else if((_camera).equals("226821")) { 
this.state = 137;
}if (true) break;

case 133:
//C
this.state = 138;
 //BA.debugLineNum = 1352;BA.debugLine="lblStatus.Text = \"Failed to retrieve Drive";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Driveway thumbnail..."));
 if (true) break;

case 135:
//C
this.state = 138;
 //BA.debugLineNum = 1354;BA.debugLine="lblStatus.Text = \"Failed to retrieve Front";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Front Door thumbnail..."));
 if (true) break;

case 137:
//C
this.state = 138;
 //BA.debugLineNum = 1356;BA.debugLine="lblStatus.Text = \"Failed to retrieve Side";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Failed to retrieve Side Yard thumbnail..."));
 if (true) break;

case 138:
//C
this.state = 141;
;
 if (true) break;

case 140:
//C
this.state = 141;
 if (true) break;

case 141:
//C
this.state = 142;
;
 if (true) break;

case 142:
//C
this.state = 143;
;
 if (true) break;

case 143:
//C
this.state = 144;
;
 if (true) break;

case 144:
//C
this.state = 157;
;
 if (true) break;
if (true) break;

case 145:
//C
this.state = 148;
;
 //BA.debugLineNum = 1366;BA.debugLine="lblStatus.Text = \"Ready\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Ready"));
 if (true) break;

case 147:
//C
this.state = 148;
this.catchState = 0;
 //BA.debugLineNum = 1368;BA.debugLine="Log(\"RefreshCamera LastException: \" & LastExcept";
anywheresoftware.b4a.keywords.Common.LogImpl("42359505","RefreshCamera LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 148:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1370;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1371;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1372;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1373;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1374;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1375;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1376;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1377;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1378;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 1379;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1120;BA.debugLine="Try";
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
 //BA.debugLineNum = 1121;BA.debugLine="lblStatus.Text = \"Authenticating...\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Authenticating..."));
 //BA.debugLineNum = 1122;BA.debugLine="Dim jobLogin As HttpJob";
_joblogin = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1123;BA.debugLine="jobLogin.Initialize(\"\", Me)";
_joblogin._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1124;BA.debugLine="jobLogin.PostString(\"https://rest.prod.immedia-s";
_joblogin._poststring /*String*/ ("https://rest.prod.immedia-semi.com/login","email="+parent._emailaddress+"&password="+parent._password);
 //BA.debugLineNum = 1125;BA.debugLine="jobLogin.GetRequest.SetContentType(\"application/";
_joblogin._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetContentType("application/x-www-form-urlencoded");
 //BA.debugLineNum = 1126;BA.debugLine="Wait For (jobLogin) JobDone(jobLogin As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_joblogin));
this.state = 19;
return;
case 19:
//C
this.state = 4;
_joblogin = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1127;BA.debugLine="If jobLogin.Success Then";
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
 //BA.debugLineNum = 1128;BA.debugLine="lblStatus.Text = \"Successfully logged in to the";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Successfully logged in to the Blink server..."));
 //BA.debugLineNum = 1130;BA.debugLine="GetAuthInfo(jobLogin.GetString)";
_getauthinfo(_joblogin._getstring /*String*/ ());
 //BA.debugLineNum = 1132;BA.debugLine="If response.StartsWith(\"ERROR: \") Or response.C";
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
 //BA.debugLineNum = 1133;BA.debugLine="btnDriveway.Enabled = True";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1134;BA.debugLine="btnRefresh.Enabled = True";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1135;BA.debugLine="btnDrivewayNewClip.Enabled = True";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1136;BA.debugLine="btnFrontDoorNewClip.Enabled = True";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1137;BA.debugLine="btnSideYardNewClip.Enabled = True";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1138;BA.debugLine="btnDrivewayFullScreenshot.Enabled = True";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1139;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = True";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1140;BA.debugLine="btnSideYardFullScreenshot.Enabled = True";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1141;BA.debugLine="jobLogin.Release";
_joblogin._release /*String*/ ();
 //BA.debugLineNum = 1142;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1144;BA.debugLine="lblStatus.Text = \"Authtoken acquired...\"";
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
 //BA.debugLineNum = 1147;BA.debugLine="lblStatus.Text = GetRESTError(jobLogin.ErrorMes";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_joblogin._errormessage /*String*/ )));
 //BA.debugLineNum = 1148;BA.debugLine="Log(\"RequestAuthToken error: \" & jobLogin.Error";
anywheresoftware.b4a.keywords.Common.LogImpl("42293794","RequestAuthToken error: "+_joblogin._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1149;BA.debugLine="jobLogin.Release";
_joblogin._release /*String*/ ();
 //BA.debugLineNum = 1150;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 if (true) break;

case 15:
//C
this.state = 18;
;
 //BA.debugLineNum = 1152;BA.debugLine="jobLogin.Release";
_joblogin._release /*String*/ ();
 if (true) break;

case 17:
//C
this.state = 18;
this.catchState = 0;
 //BA.debugLineNum = 1154;BA.debugLine="Log(\"RequestAuthToken LastException: \" & LastExc";
anywheresoftware.b4a.keywords.Common.LogImpl("42293800","RequestAuthToken LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 18:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1156;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 1157;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1382;BA.debugLine="Try";
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
 //BA.debugLineNum = 1383;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1384;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1385;BA.debugLine="j.Initialize(\"\", Me) 'name is empty as it is no";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1386;BA.debugLine="j.Download(url)";
_j._download /*String*/ (_url);
 //BA.debugLineNum = 1387;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1388;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 16;
return;
case 16:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1389;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 1390;BA.debugLine="response = j.GetString";
parent._response = _j._getstring /*String*/ ();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1392;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1393;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;
;
 //BA.debugLineNum = 1395;BA.debugLine="If response.Contains(\"System is busy, please wai";

case 9:
//if
this.state = 12;
if (parent._response.contains("System is busy, please wait")) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1396;BA.debugLine="lblStatus.Text = \"System is busy, please wait\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("System is busy, please wait"));
 if (true) break;

case 12:
//C
this.state = 15;
;
 //BA.debugLineNum = 1398;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 14:
//C
this.state = 15;
this.catchState = 0;
 //BA.debugLineNum = 1400;BA.debugLine="response = \"ERROR: \" & LastException";
parent._response = "ERROR: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA));
 //BA.debugLineNum = 1401;BA.debugLine="Log(\"RESTDownload LastException: \" & LastExcepti";
anywheresoftware.b4a.keywords.Common.LogImpl("42424852","RESTDownload LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 15:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1403;BA.debugLine="Log(\"URL: \" & url)";
anywheresoftware.b4a.keywords.Common.LogImpl("42424854","URL: "+_url,0);
 //BA.debugLineNum = 1404;BA.debugLine="Log(\"Response: \" & response)";
anywheresoftware.b4a.keywords.Common.LogImpl("42424855","Response: "+parent._response,0);
 //BA.debugLineNum = 1405;BA.debugLine="Return(response)";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)((parent._response)));return;};
 //BA.debugLineNum = 1406;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1409;BA.debugLine="Try";
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
 //BA.debugLineNum = 1410;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 1411;BA.debugLine="response = \"\"";
parent._response = "";
 //BA.debugLineNum = 1412;BA.debugLine="j.Initialize(\"\", Me) 'name is empty as it is no";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 1413;BA.debugLine="j.PostString(url,\"\")";
_j._poststring /*String*/ (_url,"");
 //BA.debugLineNum = 1414;BA.debugLine="j.GetRequest.SetContentType(\"application/x-www-f";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetContentType("application/x-www-form-urlencoded");
 //BA.debugLineNum = 1415;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 1416;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 16;
return;
case 16:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 1417;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 1418;BA.debugLine="response = j.GetString";
parent._response = _j._getstring /*String*/ ();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1420;BA.debugLine="response = \"ERROR: \" & j.ErrorMessage";
parent._response = "ERROR: "+_j._errormessage /*String*/ ;
 //BA.debugLineNum = 1421;BA.debugLine="lblStatus.Text = GetRESTError(j.ErrorMessage)";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence(_getresterror(_j._errormessage /*String*/ )));
 if (true) break;
;
 //BA.debugLineNum = 1423;BA.debugLine="If response.Contains(\"System is busy, please wai";

case 9:
//if
this.state = 12;
if (parent._response.contains("System is busy, please wait")) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1424;BA.debugLine="lblStatus.Text = \"System is busy, please wait\"";
parent.mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("System is busy, please wait"));
 if (true) break;

case 12:
//C
this.state = 15;
;
 //BA.debugLineNum = 1426;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 14:
//C
this.state = 15;
this.catchState = 0;
 //BA.debugLineNum = 1428;BA.debugLine="response = \"ERROR: \" & LastException";
parent._response = "ERROR: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA));
 //BA.debugLineNum = 1429;BA.debugLine="Log(\"RESTPost LastException: \" & LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("42490389","RESTPost LastException: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 15:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 1431;BA.debugLine="Log(\"URL: \" & url)";
anywheresoftware.b4a.keywords.Common.LogImpl("42490391","URL: "+_url,0);
 //BA.debugLineNum = 1432;BA.debugLine="Log(\"Response: \" & response)";
anywheresoftware.b4a.keywords.Common.LogImpl("42490392","Response: "+parent._response,0);
 //BA.debugLineNum = 1433;BA.debugLine="Return(response)";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)((parent._response)));return;};
 //BA.debugLineNum = 1434;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1971;BA.debugLine="Private Sub SetAvg(bcBitmap As BitmapCreator, x As";
 //BA.debugLineNum = 1972;BA.debugLine="temp.Initialize";
_temp.Initialize();
 //BA.debugLineNum = 1973;BA.debugLine="For Each c As ARGBColor In clrs";
{
final b4a.example.bitmapcreator._argbcolor[] group2 = _clrs;
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_c = group2[index2];
 //BA.debugLineNum = 1974;BA.debugLine="temp.r = temp.r + c.r";
_temp.r = (int) (_temp.r+_c.r);
 //BA.debugLineNum = 1975;BA.debugLine="temp.g = temp.g + c.g";
_temp.g = (int) (_temp.g+_c.g);
 //BA.debugLineNum = 1976;BA.debugLine="temp.b = temp.b + c.b";
_temp.b = (int) (_temp.b+_c.b);
 }
};
 //BA.debugLineNum = 1978;BA.debugLine="temp.a = 255";
_temp.a = (int) (255);
 //BA.debugLineNum = 1979;BA.debugLine="temp.r = temp.r / clrs.Length";
_temp.r = (int) (_temp.r/(double)_clrs.length);
 //BA.debugLineNum = 1980;BA.debugLine="temp.g = temp.g / clrs.Length";
_temp.g = (int) (_temp.g/(double)_clrs.length);
 //BA.debugLineNum = 1981;BA.debugLine="temp.b = temp.b / clrs.Length";
_temp.b = (int) (_temp.b/(double)_clrs.length);
 //BA.debugLineNum = 1982;BA.debugLine="bcBitmap.SetARGB(x, y, temp)";
_bcbitmap._setargb(_x,_y,_temp);
 //BA.debugLineNum = 1983;BA.debugLine="End Sub";
return "";
}
public static String  _setdefaults() throws Exception{
 //BA.debugLineNum = 1092;BA.debugLine="Sub SetDefaults";
 //BA.debugLineNum = 1094;BA.debugLine="manager.SetString(\"TempHumidityCooldownTime\", \"5\"";
_manager.SetString("TempHumidityCooldownTime","5");
 //BA.debugLineNum = 1095;BA.debugLine="manager.SetString(\"TempHumidityCooldownTimeBaseme";
_manager.SetString("TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 1096;BA.debugLine="manager.SetString(\"HumidityAddValue\", \"0\")";
_manager.SetString("HumidityAddValue","0");
 //BA.debugLineNum = 1097;BA.debugLine="manager.SetString(\"SensorNotRespondingTime\", \"5\")";
_manager.SetString("SensorNotRespondingTime","5");
 //BA.debugLineNum = 1098;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTime","5");
 //BA.debugLineNum = 1099;BA.debugLine="StateManager.SetSetting(\"TempHumidityCooldownTime";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"TempHumidityCooldownTimeBasement","5");
 //BA.debugLineNum = 1100;BA.debugLine="StateManager.SetSetting(\"HumidityAddValue\",\"0\")";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"HumidityAddValue","0");
 //BA.debugLineNum = 1101;BA.debugLine="StateManager.SetSetting(\"SensorNotRespondingTime\"";
mostCurrent._statemanager._setsetting /*String*/ (mostCurrent.activityBA,"SensorNotRespondingTime","5");
 //BA.debugLineNum = 1102;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 1103;BA.debugLine="End Sub";
return "";
}
public static String  _settextshadow(anywheresoftware.b4a.objects.LabelWrapper _lbl) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
float _radius = 0f;
float _dx = 0f;
float _dy = 0f;
 //BA.debugLineNum = 1985;BA.debugLine="Sub SetTextShadow(lbl As Label)";
 //BA.debugLineNum = 1986;BA.debugLine="Dim jo As JavaObject = lbl";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 1987;BA.debugLine="Dim radius = 2dip, dx = 0dip, dy = 0dip As Float";
_radius = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
_dx = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
_dy = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1988;BA.debugLine="jo.RunMethod(\"setShadowLayer\", Array(radius, dx,";
_jo.RunMethod("setShadowLayer",new Object[]{(Object)(_radius),(Object)(_dx),(Object)(_dy),(Object)(anywheresoftware.b4a.keywords.Common.Colors.Black)});
 //BA.debugLineNum = 1989;BA.debugLine="End Sub";
return "";
}
public static String  _showaboutmenu() throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 627;BA.debugLine="Sub ShowAboutMenu";
 //BA.debugLineNum = 628;BA.debugLine="Try";
try { //BA.debugLineNum = 629;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 630;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cloyd.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 631;BA.debugLine="Msgbox2(\"Smart Home Monitor v\" & GetVersionCode";
anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Smart Home Monitor v"+_getversioncode()+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Developed by Cloyd Nino Catanaoan"+anywheresoftware.b4a.keywords.Common.CRLF+"September 28, 2019"),BA.ObjectToCharSequence("About"),"OK","","",_bd.getBitmap(),mostCurrent.activityBA);
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 633;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("41245190",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 635;BA.debugLine="End Sub";
return "";
}
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
 //BA.debugLineNum = 2448;BA.debugLine="Try";
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
 //BA.debugLineNum = 2449;BA.debugLine="Dim j As HttpJob";
_j = new cloyd.smart.home.monitor.httpjob();
 //BA.debugLineNum = 2450;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 2451;BA.debugLine="j.Download(Link)";
_j._download /*String*/ (_link);
 //BA.debugLineNum = 2452;BA.debugLine="j.GetRequest.SetHeader(\"TOKEN_AUTH\", authToken)";
_j._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("TOKEN_AUTH",parent._authtoken);
 //BA.debugLineNum = 2453;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 33;
return;
case 33:
//C
this.state = 4;
_j = (cloyd.smart.home.monitor.httpjob) result[0];
;
 //BA.debugLineNum = 2454;BA.debugLine="B4XLoadingIndicator4.hide";
parent.mostCurrent._b4xloadingindicator4._hide /*String*/ ();
 //BA.debugLineNum = 2455;BA.debugLine="If j.Success Then";
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
 //BA.debugLineNum = 2457;BA.debugLine="Dim out As OutputStream = File.OpenOutput(File.";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"media.mp4",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2458;BA.debugLine="File.Copy2(j.GetInputStream, out)";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_j._getinputstream /*anywheresoftware.b4a.objects.streams.File.InputStreamWrapper*/ ().getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 2459;BA.debugLine="out.Close '<------ very important";
_out.Close();
 //BA.debugLineNum = 2461;BA.debugLine="Dim sb As StringBuilder";
_sb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 2462;BA.debugLine="sb.Initialize";
_sb.Initialize();
 //BA.debugLineNum = 2463;BA.debugLine="sb.Append(\"<video width='100%' height='100%' co";
_sb.Append("<video width='100%' height='100%' controls autoplay muted>");
 //BA.debugLineNum = 2464;BA.debugLine="sb.Append(\"<source src='\" & File.Combine(File.D";
_sb.Append("<source src='"+anywheresoftware.b4a.keywords.Common.File.Combine(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"\\media.mp4")+"' Type='video/mp4'/>");
 //BA.debugLineNum = 2465;BA.debugLine="sb.Append(\"</video>\")";
_sb.Append("</video>");
 //BA.debugLineNum = 2466;BA.debugLine="Dim WebViewSettings1 As WebViewSettings";
parent.mostCurrent._webviewsettings1 = new uk.co.martinpearman.b4a.webviewsettings.WebViewSettings();
 //BA.debugLineNum = 2467;BA.debugLine="WebViewSettings1.setMediaPlaybackRequiresUserGe";
parent.mostCurrent._webviewsettings1.setMediaPlaybackRequiresUserGesture((android.webkit.WebView)(parent.mostCurrent._wvmedia.getObject()),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2468;BA.debugLine="wvMedia.LoadHtml(sb.ToString)";
parent.mostCurrent._wvmedia.LoadHtml(_sb.ToString());
 //BA.debugLineNum = 2470;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 2471;BA.debugLine="r.Target = wvMedia";
_r.Target = (Object)(parent.mostCurrent._wvmedia.getObject());
 //BA.debugLineNum = 2472;BA.debugLine="r.Target = r.RunMethod(\"getSettings\")";
_r.Target = _r.RunMethod("getSettings");
 //BA.debugLineNum = 2473;BA.debugLine="r.RunMethod2(\"setBuiltInZoomControls\", True, \"j";
_r.RunMethod2("setBuiltInZoomControls",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True),"java.lang.boolean");
 //BA.debugLineNum = 2474;BA.debugLine="r.RunMethod2(\"setDisplayZoomControls\", False, \"";
_r.RunMethod2("setDisplayZoomControls",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.False),"java.lang.boolean");
 //BA.debugLineNum = 2476;BA.debugLine="mediaMetaData.Initialize";
parent._mediametadata._initialize /*String*/ (processBA);
 //BA.debugLineNum = 2477;BA.debugLine="mediaMetaData.ProcessMediaFile(File.DirInternal";
parent._mediametadata._processmediafile /*boolean*/ (anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"media.mp4");
 //BA.debugLineNum = 2478;BA.debugLine="Dim ticks As Long = (mediaMetaData.GetDuration/";
_ticks = (long) (((double)(Double.parseDouble(parent._mediametadata._getduration /*String*/ ()))/(double)1000)*anywheresoftware.b4a.keywords.Common.DateTime.TicksPerSecond);
 //BA.debugLineNum = 2479;BA.debugLine="lblDuration.Text = ConvertTicksToTimeString(tic";
parent.mostCurrent._lblduration.setText(BA.ObjectToCharSequence(_converttickstotimestring(_ticks)));
 if (true) break;

case 8:
//C
this.state = 9;
 if (true) break;
;
 //BA.debugLineNum = 2483;BA.debugLine="If j.ErrorMessage.Contains(\"Media not found\") Th";

case 9:
//if
this.state = 20;
if (_j._errormessage /*String*/ .contains("Media not found")) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 2484;BA.debugLine="clvActivity.RemoveAt(previousSelectedIndex)";
parent.mostCurrent._clvactivity._removeat(parent._previousselectedindex);
 //BA.debugLineNum = 2485;BA.debugLine="Dim list1 As List = Starter.kvs.ListKeys";
_list1 = new anywheresoftware.b4a.objects.collections.List();
_list1 = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._listkeys /*anywheresoftware.b4a.objects.collections.List*/ ();
 //BA.debugLineNum = 2486;BA.debugLine="For i =  0 To list1.Size-1";
if (true) break;

case 12:
//for
this.state = 19;
step34 = 1;
limit34 = (int) (_list1.getSize()-1);
_i = (int) (0) ;
this.state = 34;
if (true) break;

case 34:
//C
this.state = 19;
if ((step34 > 0 && _i <= limit34) || (step34 < 0 && _i >= limit34)) this.state = 14;
if (true) break;

case 35:
//C
this.state = 34;
_i = ((int)(0 + _i + step34)) ;
if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 2487;BA.debugLine="Dim mytypes As Object = Starter.kvs.Get(list1.";
_mytypes = parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._get /*Object*/ (BA.ObjectToString(_list1.Get(_i)));
 //BA.debugLineNum = 2488;BA.debugLine="Dim videos = mytypes As VideoInfo";
_videos = (cloyd.smart.home.monitor.main._videoinfo)(_mytypes);
 //BA.debugLineNum = 2489;BA.debugLine="If Link.Contains(videos.VideoID) Then";
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
 //BA.debugLineNum = 2490;BA.debugLine="Starter.kvs.Remove(list1.Get(i))";
parent.mostCurrent._starter._kvs /*cloyd.smart.home.monitor.keyvaluestore*/ ._remove /*String*/ (BA.ObjectToString(_list1.Get(_i)));
 //BA.debugLineNum = 2491;BA.debugLine="Exit";
this.state = 19;
if (true) break;
 if (true) break;

case 18:
//C
this.state = 35;
;
 if (true) break;
if (true) break;

case 19:
//C
this.state = 20;
;
 //BA.debugLineNum = 2494;BA.debugLine="ToastMessageShow(\"Media not found. Removed from";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Media not found. Removed from the list."),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 20:
//C
this.state = 23;
;
 //BA.debugLineNum = 2496;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 22:
//C
this.state = 23;
this.catchState = 0;
 //BA.debugLineNum = 2498;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("44390963",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 2501;BA.debugLine="Try";

case 23:
//try
this.state = 32;
this.catchState = 0;
this.catchState = 31;
this.state = 25;
if (true) break;

case 25:
//C
this.state = 26;
this.catchState = 31;
 //BA.debugLineNum = 2502;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(previous";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._clvactivity._getpanel(parent._previousselectedindex);
 //BA.debugLineNum = 2503;BA.debugLine="If p.NumberOfViews > 0 Then";
if (true) break;

case 26:
//if
this.state = 29;
if (_p.getNumberOfViews()>0) { 
this.state = 28;
}if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 2505;BA.debugLine="Dim ContentLabel As ImageView = p.GetView(0).Ge";
_contentlabel = new anywheresoftware.b4a.objects.ImageViewWrapper();
_contentlabel.setObject((android.widget.ImageView)(_p.GetView((int) (0)).GetView((int) (4)).getObject()));
 //BA.debugLineNum = 2506;BA.debugLine="ContentLabel.Visible = False";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2508;BA.debugLine="Dim cd As CardData = clvActivity.GetValue(previ";
_cd = (cloyd.smart.home.monitor.main._carddata)(parent.mostCurrent._clvactivity._getvalue(parent._previousselectedindex));
 //BA.debugLineNum = 2509;BA.debugLine="cd.iswatchedvisible = False";
_cd.iswatchedvisible /*boolean*/  = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 2511;BA.debugLine="Dim ContentLabel1 As Label = p.GetView(0).GetVi";
_contentlabel1 = new anywheresoftware.b4a.objects.LabelWrapper();
_contentlabel1.setObject((android.widget.TextView)(_p.GetView((int) (0)).GetView((int) (0)).getObject()));
 //BA.debugLineNum = 2512;BA.debugLine="ContentLabel1.Text = \"   \" & ConvertDayName(cd.";
_contentlabel1.setText(BA.ObjectToCharSequence("   "+_convertdayname(_cd.filedate /*String*/ )));
 //BA.debugLineNum = 2514;BA.debugLine="Dim ContentLabel As ImageView = p.GetView(0).Ge";
_contentlabel = new anywheresoftware.b4a.objects.ImageViewWrapper();
_contentlabel.setObject((android.widget.ImageView)(_p.GetView((int) (0)).GetView((int) (6)).getObject()));
 //BA.debugLineNum = 2515;BA.debugLine="ContentLabel.Visible = True";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 29:
//C
this.state = 32;
;
 //BA.debugLineNum = 2518;BA.debugLine="GetUnwatchedVideos";
_getunwatchedvideos();
 if (true) break;

case 31:
//C
this.state = 32;
this.catchState = 0;
 //BA.debugLineNum = 2520;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("44390985",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 32:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 2522;BA.debugLine="Return Null";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,anywheresoftware.b4a.keywords.Common.Null);return;};
 //BA.debugLineNum = 2523;BA.debugLine="End Sub";
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
public static int  _stringcount(String _stringtosearch,String _targetstr,boolean _ignorecase) throws Exception{
 //BA.debugLineNum = 2564;BA.debugLine="Sub StringCount(StringToSearch As String,TargetStr";
 //BA.debugLineNum = 2565;BA.debugLine="If IgnoreCase Then";
if (_ignorecase) { 
 //BA.debugLineNum = 2566;BA.debugLine="StringToSearch = StringToSearch.ToLowerCase";
_stringtosearch = _stringtosearch.toLowerCase();
 //BA.debugLineNum = 2567;BA.debugLine="TargetStr = TargetStr.ToLowerCase";
_targetstr = _targetstr.toLowerCase();
 };
 //BA.debugLineNum = 2569;BA.debugLine="Return (StringToSearch.Length - StringToSearch.Re";
if (true) return (int) ((_stringtosearch.length()-_stringtosearch.replace(_targetstr,"").length())/(double)_targetstr.length());
 //BA.debugLineNum = 2571;BA.debugLine="End Sub";
return 0;
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
 //BA.debugLineNum = 685;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 64;
this.catchState = 63;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 63;
 //BA.debugLineNum = 686;BA.debugLine="B4XPageIndicator1.CurrentPage = Position";
parent.mostCurrent._b4xpageindicator1._setcurrentpage /*int*/ (_position);
 //BA.debugLineNum = 687;BA.debugLine="If Position = 0 Then";
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
 //BA.debugLineNum = 688;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
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
 //BA.debugLineNum = 689;BA.debugLine="MQTT.Publish(\"TempHumid\", bc.StringToBytes(\"Re";
parent._mqtt.Publish("TempHumid",parent._bc.StringToBytes("Read weather","utf8"));
 if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 691;BA.debugLine="CheckTempHumiditySetting";
_checktemphumiditysetting();
 if (true) break;
;
 //BA.debugLineNum = 694;BA.debugLine="If Position = 1 Then";

case 11:
//if
this.state = 18;
if (_position==1) { 
this.state = 13;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 695;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
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
 //BA.debugLineNum = 696;BA.debugLine="MQTT.Publish(\"MQ7\", bc.StringToBytes(\"Read vol";
parent._mqtt.Publish("MQ7",parent._bc.StringToBytes("Read voltage","utf8"));
 if (true) break;

case 17:
//C
this.state = 18;
;
 //BA.debugLineNum = 698;BA.debugLine="CheckAirQualitySetting";
_checkairqualitysetting();
 if (true) break;
;
 //BA.debugLineNum = 701;BA.debugLine="If Position = 2 Then";

case 18:
//if
this.state = 25;
if (_position==2) { 
this.state = 20;
}if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 702;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
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
 //BA.debugLineNum = 703;BA.debugLine="MQTT.Publish(\"TempHumidBasement\", bc.StringToB";
parent._mqtt.Publish("TempHumidBasement",parent._bc.StringToBytes("Read weather","utf8"));
 if (true) break;

case 24:
//C
this.state = 25;
;
 //BA.debugLineNum = 705;BA.debugLine="CheckTempHumiditySettingBasement";
_checktemphumiditysettingbasement();
 if (true) break;
;
 //BA.debugLineNum = 708;BA.debugLine="If Position = 3 Then";

case 25:
//if
this.state = 32;
if (_position==3) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 709;BA.debugLine="If MQTT.IsInitialized And MQTT.Connected  Then";
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
 //BA.debugLineNum = 710;BA.debugLine="MQTT.Publish(\"MQ7Basement\", bc.StringToBytes(\"";
parent._mqtt.Publish("MQ7Basement",parent._bc.StringToBytes("Read voltage","utf8"));
 if (true) break;

case 31:
//C
this.state = 32;
;
 //BA.debugLineNum = 712;BA.debugLine="CheckAirQualitySettingBasement";
_checkairqualitysettingbasement();
 if (true) break;
;
 //BA.debugLineNum = 715;BA.debugLine="If Position = 4 Then";

case 32:
//if
this.state = 39;
if (_position==4) { 
this.state = 34;
}if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 716;BA.debugLine="wvMedia.LoadUrl(\"\")";
parent.mostCurrent._wvmedia.LoadUrl("");
 //BA.debugLineNum = 718;BA.debugLine="If ivDriveway.Bitmap = Null Or lblStatus.Text.C";
if (true) break;

case 35:
//if
this.state = 38;
if (parent.mostCurrent._ivdriveway.getBitmap()== null || parent.mostCurrent._lblstatus.getText().contains("REST endpoint URL not found")) { 
this.state = 37;
}if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 719;BA.debugLine="ProgressDialogShow2(\"Getting new camera inform";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Getting new camera information..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 720;BA.debugLine="btnDriveway.Enabled = False";
parent.mostCurrent._btndriveway._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 721;BA.debugLine="btnRefresh.Enabled = False";
parent.mostCurrent._btnrefresh._setenabled /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 722;BA.debugLine="btnDrivewayNewClip.Enabled = False";
parent.mostCurrent._btndrivewaynewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 723;BA.debugLine="btnFrontDoorNewClip.Enabled = False";
parent.mostCurrent._btnfrontdoornewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 724;BA.debugLine="btnSideYardNewClip.Enabled = False";
parent.mostCurrent._btnsideyardnewclip.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 725;BA.debugLine="btnDrivewayFullScreenshot.Enabled = False";
parent.mostCurrent._btndrivewayfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 726;BA.debugLine="btnFrontDoorFullScreenshot.Enabled = False";
parent.mostCurrent._btnfrontdoorfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 727;BA.debugLine="btnSideYardFullScreenshot.Enabled = False";
parent.mostCurrent._btnsideyardfullscreenshot.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 728;BA.debugLine="ScrollViewBlink.ScrollToNow(0)";
parent.mostCurrent._scrollviewblink.ScrollToNow((int) (0));
 //BA.debugLineNum = 729;BA.debugLine="BlurIV(\"Driveway.jpg\",ivDriveway)";
_bluriv("Driveway.jpg",parent.mostCurrent._ivdriveway);
 //BA.debugLineNum = 730;BA.debugLine="BlurIV(\"FrontDoor.jpg\",ivFrontDoor)";
_bluriv("FrontDoor.jpg",parent.mostCurrent._ivfrontdoor);
 //BA.debugLineNum = 731;BA.debugLine="BlurIV(\"SideYard.jpg\",ivSideYard)";
_bluriv("SideYard.jpg",parent.mostCurrent._ivsideyard);
 //BA.debugLineNum = 732;BA.debugLine="Dim rs As ResumableSub = RefreshCameras(True)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _refreshcameras(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 733;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 65;
return;
case 65:
//C
this.state = 38;
_result = (Object) result[0];
;
 //BA.debugLineNum = 734;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 38:
//C
this.state = 39;
;
 //BA.debugLineNum = 737;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 738;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 66;
return;
case 66:
//C
this.state = 39;
_result = (Object) result[0];
;
 if (true) break;
;
 //BA.debugLineNum = 740;BA.debugLine="If Position = 5 Then";

case 39:
//if
this.state = 61;
if (_position==5) { 
this.state = 41;
}if (true) break;

case 41:
//C
this.state = 42;
 //BA.debugLineNum = 741;BA.debugLine="If tabStripCurrentPage = False Then";
if (true) break;

case 42:
//if
this.state = 60;
if (parent._tabstripcurrentpage==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 44;
}if (true) break;

case 44:
//C
this.state = 45;
 //BA.debugLineNum = 745;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 746;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 67;
return;
case 67:
//C
this.state = 45;
_result = (Object) result[0];
;
 //BA.debugLineNum = 748;BA.debugLine="Dim UnwatchedVideoClips As String = StateManag";
_unwatchedvideoclips = parent.mostCurrent._statemanager._getsetting /*String*/ (mostCurrent.activityBA,"UnwatchedVideoClips");
 //BA.debugLineNum = 749;BA.debugLine="If IsNumber(UnwatchedVideoClips) Or clvActivit";
if (true) break;

case 45:
//if
this.state = 52;
if (anywheresoftware.b4a.keywords.Common.IsNumber(_unwatchedvideoclips) || parent.mostCurrent._clvactivity._getsize()==0) { 
this.state = 47;
}if (true) break;

case 47:
//C
this.state = 48;
 //BA.debugLineNum = 750;BA.debugLine="If UnwatchedVideoClips > 0 Or clvActivity.Siz";
if (true) break;

case 48:
//if
this.state = 51;
if ((double)(Double.parseDouble(_unwatchedvideoclips))>0 || parent.mostCurrent._clvactivity._getsize()==0) { 
this.state = 50;
}if (true) break;

case 50:
//C
this.state = 51;
 //BA.debugLineNum = 751;BA.debugLine="clvActivity.Clear";
parent.mostCurrent._clvactivity._clear();
 //BA.debugLineNum = 753;BA.debugLine="Dim rs As ResumableSub = GetVideos(response)";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getvideos(parent._response);
 //BA.debugLineNum = 754;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 68;
return;
case 68:
//C
this.state = 51;
_result = (Object) result[0];
;
 //BA.debugLineNum = 757;BA.debugLine="Dim rs As ResumableSub = GetUnwatchedVideos";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = _getunwatchedvideos();
 //BA.debugLineNum = 758;BA.debugLine="wait for (rs) complete (Result As Object)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 69;
return;
case 69:
//C
this.state = 51;
_result = (Object) result[0];
;
 //BA.debugLineNum = 759;BA.debugLine="tabStripCurrentPage = False";
parent._tabstripcurrentpage = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 760;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 51:
//C
this.state = 52;
;
 if (true) break;
;
 //BA.debugLineNum = 764;BA.debugLine="If clvActivity.Size > 0 Then";

case 52:
//if
this.state = 59;
if (parent.mostCurrent._clvactivity._getsize()>0) { 
this.state = 54;
}if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 765;BA.debugLine="clvActivity.JumpToItem(previousSelectedIndex)";
parent.mostCurrent._clvactivity._jumptoitem(parent._previousselectedindex);
 //BA.debugLineNum = 766;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 70;
return;
case 70:
//C
this.state = 55;
;
 //BA.debugLineNum = 767;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(previ";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._clvactivity._getpanel(parent._previousselectedindex);
 //BA.debugLineNum = 768;BA.debugLine="If p.NumberOfViews > 0 Then";
if (true) break;

case 55:
//if
this.state = 58;
if (_p.getNumberOfViews()>0) { 
this.state = 57;
}if (true) break;

case 57:
//C
this.state = 58;
 //BA.debugLineNum = 769;BA.debugLine="Dim backPane As B4XView = p.getview(0)";
_backpane = new anywheresoftware.b4a.objects.B4XViewWrapper();
_backpane = _p.GetView((int) (0));
 //BA.debugLineNum = 770;BA.debugLine="backPane.Color = xui.Color_ARGB(255,217,215,";
_backpane.setColor(parent.mostCurrent._xui.Color_ARGB((int) (255),(int) (217),(int) (215),(int) (222)));
 //BA.debugLineNum = 772;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).G";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (6));
 //BA.debugLineNum = 773;BA.debugLine="ContentLabel.Visible = True";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 58:
//C
this.state = 59;
;
 //BA.debugLineNum = 775;BA.debugLine="B4XLoadingIndicator4.Show";
parent.mostCurrent._b4xloadingindicator4._show /*String*/ ();
 //BA.debugLineNum = 776;BA.debugLine="Dim cd As CardData = clvActivity.GetValue(pre";
_cd = (cloyd.smart.home.monitor.main._carddata)(parent.mostCurrent._clvactivity._getvalue(parent._previousselectedindex));
 //BA.debugLineNum = 777;BA.debugLine="Dim firstvideo As String";
_firstvideo = "";
 //BA.debugLineNum = 778;BA.debugLine="firstvideo = cd.mediaURL";
_firstvideo = _cd.mediaURL /*String*/ ;
 //BA.debugLineNum = 779;BA.debugLine="lblDuration.Text = \"0:00\"";
parent.mostCurrent._lblduration.setText(BA.ObjectToCharSequence("0:00"));
 //BA.debugLineNum = 780;BA.debugLine="ShowVideo(firstvideo)";
_showvideo(_firstvideo);
 if (true) break;

case 59:
//C
this.state = 60;
;
 if (true) break;

case 60:
//C
this.state = 61;
;
 //BA.debugLineNum = 783;BA.debugLine="tabStripCurrentPage = False";
parent._tabstripcurrentpage = anywheresoftware.b4a.keywords.Common.False;
 if (true) break;

case 61:
//C
this.state = 64;
;
 if (true) break;

case 63:
//C
this.state = 64;
this.catchState = 0;
 //BA.debugLineNum = 786;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("41572966",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 if (true) break;
if (true) break;

case 64:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 788;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 2290;BA.debugLine="Sub UpdateItemColor (Index As Int)";
 //BA.debugLineNum = 2291;BA.debugLine="Try";
try { //BA.debugLineNum = 2292;BA.debugLine="If previousSelectedIndex <> Index Then";
if (_previousselectedindex!=_index) { 
 //BA.debugLineNum = 2293;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(previou";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._clvactivity._getpanel(_previousselectedindex);
 //BA.debugLineNum = 2294;BA.debugLine="If p.NumberOfViews > 0 Then";
if (_p.getNumberOfViews()>0) { 
 //BA.debugLineNum = 2295;BA.debugLine="Dim backPane As B4XView = p.getview(0)";
_backpane = new anywheresoftware.b4a.objects.B4XViewWrapper();
_backpane = _p.GetView((int) (0));
 //BA.debugLineNum = 2296;BA.debugLine="backPane.Color = xui.Color_White";
_backpane.setColor(mostCurrent._xui.Color_White);
 //BA.debugLineNum = 2298;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).Get";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (6));
 //BA.debugLineNum = 2299;BA.debugLine="ContentLabel.Visible = False";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 2303;BA.debugLine="Dim p As B4XView = clvActivity.GetPanel(Index)";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._clvactivity._getpanel(_index);
 //BA.debugLineNum = 2304;BA.debugLine="If p.NumberOfViews > 0 Then";
if (_p.getNumberOfViews()>0) { 
 //BA.debugLineNum = 2305;BA.debugLine="Dim backPane As B4XView = p.getview(0)";
_backpane = new anywheresoftware.b4a.objects.B4XViewWrapper();
_backpane = _p.GetView((int) (0));
 //BA.debugLineNum = 2306;BA.debugLine="backPane.Color = xui.Color_ARGB(255,217,215,222";
_backpane.setColor(mostCurrent._xui.Color_ARGB((int) (255),(int) (217),(int) (215),(int) (222)));
 //BA.debugLineNum = 2308;BA.debugLine="Dim ContentLabel As B4XView = p.GetView(0).GetV";
_contentlabel = new anywheresoftware.b4a.objects.B4XViewWrapper();
_contentlabel = _p.GetView((int) (0)).GetView((int) (6));
 //BA.debugLineNum = 2309;BA.debugLine="ContentLabel.Visible = True";
_contentlabel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 2312;BA.debugLine="previousSelectedIndex = Index";
_previousselectedindex = _index;
 } 
       catch (Exception e20) {
			processBA.setLastException(e20); //BA.debugLineNum = 2314;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("44128792",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 2316;BA.debugLine="End Sub";
return "";
}
public static String  _wvmedia_pagefinished(String _url) throws Exception{
 //BA.debugLineNum = 2525;BA.debugLine="Sub wvMedia_PageFinished (Url As String)";
 //BA.debugLineNum = 2527;BA.debugLine="End Sub";
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
