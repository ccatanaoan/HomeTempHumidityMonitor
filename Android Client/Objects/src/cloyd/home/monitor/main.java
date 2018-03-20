package cloyd.home.monitor;


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
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "cloyd.home.monitor", "cloyd.home.monitor.main");
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
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
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
		activityBA = new BA(this, layout, processBA, "cloyd.home.monitor", "cloyd.home.monitor.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "cloyd.home.monitor.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
        if (_activity == null) //workaround for emulator bug (Issue 2423)
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
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public cloyd.home.monitor.customlistview _clv1 = null;
public de.amberhome.objects.appcompat.ACMenuWrapper _gblacmenu = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public cloyd.home.monitor.gauge _gaugehumidity = null;
public cloyd.home.monitor.gauge _gaugetemp = null;
public cloyd.home.monitor.gauge _gaugedewpoint = null;
public cloyd.home.monitor.gauge _gaugeheatindex = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcomfort = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblperception = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllastupdate = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblping = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.objects.CSBuilder _cs = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.object.XmlLayoutBuilder _xl = null;
 //BA.debugLineNum = 41;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 42;BA.debugLine="Try";
try { //BA.debugLineNum = 43;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 44;BA.debugLine="csu.Initialize";
_csu._initialize(processBA);
 //BA.debugLineNum = 45;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 47;BA.debugLine="Activity.LoadLayout(\"1\")";
mostCurrent._activity.LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 48;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 49;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 50;BA.debugLine="ACToolBarLight1.NavigationIconDrawable = bd";
mostCurrent._actoolbarlight1.setNavigationIconDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 51;BA.debugLine="ToolbarHelper.Initialize";
mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 52;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 53;BA.debugLine="ToolbarHelper.Title= cs.Initialize.Size(18).Appe";
mostCurrent._toolbarhelper.setTitle(BA.ObjectToCharSequence(_cs.Initialize().Size((int) (18)).Append(BA.ObjectToCharSequence("Cloyd Home Monitor")).PopAll().getObject()));
 //BA.debugLineNum = 54;BA.debugLine="ToolbarHelper.subTitle = \"\"";
mostCurrent._toolbarhelper.setSubtitle(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 55;BA.debugLine="ToolbarHelper.ShowUpIndicator = False 'set to tr";
mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 56;BA.debugLine="ACToolBarLight1.InitMenuListener";
mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 57;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 58;BA.debugLine="Dim xl As XmlLayoutBuilder";
_xl = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 59;BA.debugLine="jo.RunMethod(\"setPopupTheme\", Array(xl.GetResour";
_jo.RunMethod("setPopupTheme",new Object[]{(Object)(_xl.GetResourceId("style","ToolbarMenu"))});
 //BA.debugLineNum = 61;BA.debugLine="GaugeHumidity.SetRanges(Array(GaugeHumidity.Crea";
mostCurrent._gaugehumidity._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugehumidity._createrange((float) (0),(float) (70),mostCurrent._xui.Color_Green)),(Object)(mostCurrent._gaugehumidity._createrange((float) (70),(float) (80),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugehumidity._createrange((float) (80),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 64;BA.debugLine="GaugeTemp.SetRanges(Array(GaugeTemp.CreateRange(";
mostCurrent._gaugetemp._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugetemp._createrange((float) (0),(float) (75),mostCurrent._xui.Color_Green)),(Object)(mostCurrent._gaugetemp._createrange((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugetemp._createrange((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 67;BA.debugLine="GaugeHeatIndex.SetRanges(Array(GaugeHeatIndex.Cr";
mostCurrent._gaugeheatindex._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeheatindex._createrange((float) (0),(float) (75),mostCurrent._xui.Color_Green)),(Object)(mostCurrent._gaugeheatindex._createrange((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugeheatindex._createrange((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 70;BA.debugLine="GaugeDewPoint.SetRanges(Array(GaugeHeatIndex.Cre";
mostCurrent._gaugedewpoint._setranges(anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._gaugeheatindex._createrange((float) (0),(float) (75),mostCurrent._xui.Color_Green)),(Object)(mostCurrent._gaugedewpoint._createrange((float) (75),(float) (90),mostCurrent._xui.Color_Yellow)),(Object)(mostCurrent._gaugedewpoint._createrange((float) (90),(float) (100),mostCurrent._xui.Color_Red))}));
 //BA.debugLineNum = 73;BA.debugLine="GaugeTemp.CurrentValue = 0";
mostCurrent._gaugetemp._setcurrentvalue((float) (0));
 //BA.debugLineNum = 74;BA.debugLine="GaugeHumidity.CurrentValue = 0";
mostCurrent._gaugehumidity._setcurrentvalue((float) (0));
 //BA.debugLineNum = 75;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 76;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 77;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence("No data available")).PopAll().getObject()));
 //BA.debugLineNum = 78;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"The";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence("No data available")).PopAll().getObject()));
 //BA.debugLineNum = 79;BA.debugLine="GaugeHeatIndex.CurrentValue = 0";
mostCurrent._gaugeheatindex._setcurrentvalue((float) (0));
 //BA.debugLineNum = 80;BA.debugLine="GaugeDewPoint.CurrentValue = 0";
mostCurrent._gaugedewpoint._setcurrentvalue((float) (0));
 //BA.debugLineNum = 81;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 82;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(\"";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence("")).PopAll().getObject()));
 //BA.debugLineNum = 83;BA.debugLine="lblPing.Visible = False";
mostCurrent._lblping.setVisible(anywheresoftware.b4a.keywords.Common.False);
 } 
       catch (Exception e35) {
			processBA.setLastException(e35); //BA.debugLineNum = 85;BA.debugLine="ToastMessageShow(LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
 //BA.debugLineNum = 289;BA.debugLine="Sub Activity_Createmenu(Menu As ACMenu)";
 //BA.debugLineNum = 290;BA.debugLine="Try";
try { //BA.debugLineNum = 291;BA.debugLine="Menu.Clear";
_menu.Clear();
 //BA.debugLineNum = 292;BA.debugLine="gblACMenu = Menu";
mostCurrent._gblacmenu = _menu;
 //BA.debugLineNum = 293;BA.debugLine="Menu.Add(0, 0, \"Restart board\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Restart board"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 294;BA.debugLine="Menu.Add(0, 0, \"About\",Null)";
_menu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 } 
       catch (Exception e7) {
			processBA.setLastException(e7); //BA.debugLineNum = 296;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 298;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 89;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 90;BA.debugLine="Try";
try { //BA.debugLineNum = 91;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connected";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 92;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 95;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
int _result = 0;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 240;BA.debugLine="Sub ACToolBarLight1_MenuItemClick (Item As ACMenuI";
 //BA.debugLineNum = 241;BA.debugLine="Try";
try { //BA.debugLineNum = 242;BA.debugLine="If Item.Title = \"About\" Then";
if ((_item.getTitle()).equals("About")) { 
 //BA.debugLineNum = 243;BA.debugLine="ShowAboutMenu";
_showaboutmenu();
 }else if((_item.getTitle()).equals("Restart board")) { 
 //BA.debugLineNum = 245;BA.debugLine="Try";
try { //BA.debugLineNum = 246;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 247;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 248;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets,";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 249;BA.debugLine="result = Msgbox2(\"Restart the controller?\", \"C";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Restart the controller?"),BA.ObjectToCharSequence("Cloyd Home Monitor"),"Yes","","No",_bd.getBitmap(),mostCurrent.activityBA);
 //BA.debugLineNum = 250;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 251;BA.debugLine="If MQTT.IsInitialized = False Or MQTT.Connect";
if (_mqtt.IsInitialized()==anywheresoftware.b4a.keywords.Common.False || _mqtt.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 252;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 };
 //BA.debugLineNum = 254;BA.debugLine="MQTT.Publish(\"Cloyd\", bc.StringToBytes(\"Resta";
_mqtt.Publish("Cloyd",_bc.StringToBytes("Restart controller","utf8"));
 };
 } 
       catch (Exception e17) {
			processBA.setLastException(e17); //BA.debugLineNum = 257;BA.debugLine="AddEvent(LastException)";
_addevent(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 };
 } 
       catch (Exception e21) {
			processBA.setLastException(e21); //BA.debugLineNum = 261;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 263;BA.debugLine="End Sub";
return "";
}
public static void  _addevent(String _text) throws Exception{
ResumableSub_AddEvent rsub = new ResumableSub_AddEvent(null,_text);
rsub.resume(processBA, null);
}
public static class ResumableSub_AddEvent extends BA.ResumableSub {
public ResumableSub_AddEvent(cloyd.home.monitor.main parent,String _text) {
this.parent = parent;
this._text = _text;
}
cloyd.home.monitor.main parent;
String _text;

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
 //BA.debugLineNum = 230;BA.debugLine="Try";
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
 //BA.debugLineNum = 231;BA.debugLine="DateTime.DateFormat = \"MMM d h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d h:mm:ss a");
 //BA.debugLineNum = 232;BA.debugLine="clv1.AddTextItem(DateTime.Date(DateTime.Now) & \"";
parent.mostCurrent._clv1._addtextitem((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+": "+_text),(Object)(_text));
 //BA.debugLineNum = 233;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 234;BA.debugLine="clv1.ScrollToItem(clv1.Size-1)";
parent.mostCurrent._clv1._scrolltoitem((int) (parent.mostCurrent._clv1._getsize()-1));
 if (true) break;

case 5:
//C
this.state = 6;
this.catchState = 0;
 //BA.debugLineNum = 236;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 if (true) break;
if (true) break;

case 6:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 238;BA.debugLine="End Sub";
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
public static String  _getcomfort(String _dht11comfortstatus) throws Exception{
String _localcomfortstatus = "";
 //BA.debugLineNum = 203;BA.debugLine="Sub GetComfort(DHT11ComfortStatus As String) As St";
 //BA.debugLineNum = 204;BA.debugLine="Dim localcomfortstatus As String";
_localcomfortstatus = "";
 //BA.debugLineNum = 205;BA.debugLine="Select Case DHT11ComfortStatus";
switch (BA.switchObjectToInt(_dht11comfortstatus,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(8),BA.NumberToString(9),BA.NumberToString(10))) {
case 0: {
 //BA.debugLineNum = 207;BA.debugLine="localcomfortstatus = \"OK\"";
_localcomfortstatus = "OK";
 break; }
case 1: {
 //BA.debugLineNum = 209;BA.debugLine="localcomfortstatus = \"Too hot\"";
_localcomfortstatus = "Too hot";
 break; }
case 2: {
 //BA.debugLineNum = 211;BA.debugLine="localcomfortstatus = \"Too cold\"";
_localcomfortstatus = "Too cold";
 break; }
case 3: {
 //BA.debugLineNum = 213;BA.debugLine="localcomfortstatus = \"Too dry\"";
_localcomfortstatus = "Too dry";
 break; }
case 4: {
 //BA.debugLineNum = 215;BA.debugLine="localcomfortstatus = \"Hot and dry\"";
_localcomfortstatus = "Hot and dry";
 break; }
case 5: {
 //BA.debugLineNum = 217;BA.debugLine="localcomfortstatus = \"Cold and dry\"";
_localcomfortstatus = "Cold and dry";
 break; }
case 6: {
 //BA.debugLineNum = 219;BA.debugLine="localcomfortstatus = \"Too humid\"";
_localcomfortstatus = "Too humid";
 break; }
case 7: {
 //BA.debugLineNum = 221;BA.debugLine="localcomfortstatus = \"Hot and humid\"";
_localcomfortstatus = "Hot and humid";
 break; }
case 8: {
 //BA.debugLineNum = 223;BA.debugLine="localcomfortstatus = \"Cold and humid\"";
_localcomfortstatus = "Cold and humid";
 break; }
default: {
 //BA.debugLineNum = 225;BA.debugLine="localcomfortstatus = \"Unknown\"";
_localcomfortstatus = "Unknown";
 break; }
}
;
 //BA.debugLineNum = 227;BA.debugLine="Return localcomfortstatus";
if (true) return _localcomfortstatus;
 //BA.debugLineNum = 228;BA.debugLine="End Sub";
return "";
}
public static String  _getperception(String _dht11perception) throws Exception{
String _localperception = "";
 //BA.debugLineNum = 180;BA.debugLine="Sub GetPerception(DHT11Perception As String) As St";
 //BA.debugLineNum = 181;BA.debugLine="Dim localperception As String";
_localperception = "";
 //BA.debugLineNum = 182;BA.debugLine="Select Case DHT11Perception";
switch (BA.switchObjectToInt(_dht11perception,BA.NumberToString(0),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3),BA.NumberToString(4),BA.NumberToString(5),BA.NumberToString(6),BA.NumberToString(7))) {
case 0: {
 //BA.debugLineNum = 184;BA.debugLine="localperception = \"A bit dry For some\"";
_localperception = "A bit dry For some";
 break; }
case 1: {
 //BA.debugLineNum = 186;BA.debugLine="localperception = \"Very comfortable\"";
_localperception = "Very comfortable";
 break; }
case 2: {
 //BA.debugLineNum = 188;BA.debugLine="localperception = \"Comfortable\"";
_localperception = "Comfortable";
 break; }
case 3: {
 //BA.debugLineNum = 190;BA.debugLine="localperception = \"OK for most, but all perceiv";
_localperception = "OK for most, but all perceive the humidity at upper edge";
 break; }
case 4: {
 //BA.debugLineNum = 192;BA.debugLine="localperception = \"Somewhat uncomfortable For m";
_localperception = "Somewhat uncomfortable For most people at upper edge";
 break; }
case 5: {
 //BA.debugLineNum = 194;BA.debugLine="localperception = \"Very humid, quite uncomforta";
_localperception = "Very humid, quite uncomfortable";
 break; }
case 6: {
 //BA.debugLineNum = 196;BA.debugLine="localperception = \"Extremely uncomfortable, opp";
_localperception = "Extremely uncomfortable, oppressive";
 break; }
case 7: {
 //BA.debugLineNum = 198;BA.debugLine="localperception = \"Severely high, even deadly F";
_localperception = "Severely high, even deadly For asthma related illnesses";
 break; }
}
;
 //BA.debugLineNum = 200;BA.debugLine="Return localperception";
if (true) return _localperception;
 //BA.debugLineNum = 201;BA.debugLine="End Sub";
return "";
}
public static String  _getversioncode() throws Exception{
String _appversion = "";
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
String _packagename = "";
 //BA.debugLineNum = 276;BA.debugLine="Sub GetVersionCode() As String";
 //BA.debugLineNum = 277;BA.debugLine="Dim AppVersion As String";
_appversion = "";
 //BA.debugLineNum = 278;BA.debugLine="Try";
try { //BA.debugLineNum = 279;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 280;BA.debugLine="Dim packageName As String";
_packagename = "";
 //BA.debugLineNum = 281;BA.debugLine="packageName =  Application.PackageName";
_packagename = anywheresoftware.b4a.keywords.Common.Application.getPackageName();
 //BA.debugLineNum = 282;BA.debugLine="AppVersion = pm.GetVersionName(packageName)";
_appversion = _pm.GetVersionName(_packagename);
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 284;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 286;BA.debugLine="Return AppVersion";
if (true) return _appversion;
 //BA.debugLineNum = 287;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 26;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 28;BA.debugLine="Private clv1 As CustomListView";
mostCurrent._clv1 = new cloyd.home.monitor.customlistview();
 //BA.debugLineNum = 29;BA.debugLine="Private gblACMenu As ACMenu";
mostCurrent._gblacmenu = new de.amberhome.objects.appcompat.ACMenuWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 31;BA.debugLine="Private GaugeHumidity As Gauge";
mostCurrent._gaugehumidity = new cloyd.home.monitor.gauge();
 //BA.debugLineNum = 32;BA.debugLine="Private GaugeTemp As Gauge";
mostCurrent._gaugetemp = new cloyd.home.monitor.gauge();
 //BA.debugLineNum = 33;BA.debugLine="Private GaugeDewPoint As Gauge";
mostCurrent._gaugedewpoint = new cloyd.home.monitor.gauge();
 //BA.debugLineNum = 34;BA.debugLine="Private GaugeHeatIndex As Gauge";
mostCurrent._gaugeheatindex = new cloyd.home.monitor.gauge();
 //BA.debugLineNum = 35;BA.debugLine="Private lblComfort As Label";
mostCurrent._lblcomfort = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private lblPerception As Label";
mostCurrent._lblperception = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private lblLastUpdate As Label";
mostCurrent._lbllastupdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private lblPing As Label";
mostCurrent._lblping = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _hideping() throws Exception{
 //BA.debugLineNum = 300;BA.debugLine="Private Sub HidePing";
 //BA.debugLineNum = 301;BA.debugLine="lblPing.SetVisibleAnimated(200, False)";
mostCurrent._lblping.SetVisibleAnimated((int) (200),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 302;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connect() throws Exception{
String _clientid = "";
anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper _connopt = null;
 //BA.debugLineNum = 104;BA.debugLine="Sub MQTT_Connect";
 //BA.debugLineNum = 105;BA.debugLine="Try";
try { //BA.debugLineNum = 106;BA.debugLine="Dim ClientId As String = Rnd(0, 999999999) 'crea";
_clientid = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (999999999)));
 //BA.debugLineNum = 107;BA.debugLine="MQTT.Initialize(\"MQTT\", MQTTServerURI, Cli";
_mqtt.Initialize(processBA,"MQTT",_mqttserveruri,_clientid);
 //BA.debugLineNum = 109;BA.debugLine="Dim ConnOpt As MqttConnectOptions";
_connopt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper();
 //BA.debugLineNum = 110;BA.debugLine="ConnOpt.Initialize(MQTTUser, MQTTPassword)";
_connopt.Initialize(_mqttuser,_mqttpassword);
 //BA.debugLineNum = 111;BA.debugLine="MQTT.Connect2(ConnOpt)";
_mqtt.Connect2((org.eclipse.paho.client.mqttv3.MqttConnectOptions)(_connopt.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 113;BA.debugLine="AddEvent(\"MQTT_Connect: \" & LastException)";
_addevent("MQTT_Connect: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 117;BA.debugLine="Sub MQTT_Connected (Success As Boolean)";
 //BA.debugLineNum = 118;BA.debugLine="Try";
try { //BA.debugLineNum = 119;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 120;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 //BA.debugLineNum = 121;BA.debugLine="AddEvent(\"MQTT error connecting\")";
_addevent("MQTT error connecting");
 }else {
 //BA.debugLineNum = 123;BA.debugLine="AddEvent(\"Connected to MQTT broker\")";
_addevent("Connected to MQTT broker");
 //BA.debugLineNum = 124;BA.debugLine="MQTT.Subscribe(\"Cloyd\", 0)";
_mqtt.Subscribe("Cloyd",(int) (0));
 };
 } 
       catch (Exception e10) {
			processBA.setLastException(e10); //BA.debugLineNum = 127;BA.debugLine="AddEvent(\"MQTT_Connected: \" & LastException)";
_addevent("MQTT_Connected: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_disconnected() throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Private Sub MQTT_Disconnected";
 //BA.debugLineNum = 133;BA.debugLine="Try";
try { //BA.debugLineNum = 134;BA.debugLine="gblACMenu.Clear";
mostCurrent._gblacmenu.Clear();
 //BA.debugLineNum = 135;BA.debugLine="gblACMenu.Add(0, 0, \"Restart board\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("Restart board"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 136;BA.debugLine="gblACMenu.Add(0, 0, \"About\",Null)";
mostCurrent._gblacmenu.Add((int) (0),(int) (0),BA.ObjectToCharSequence("About"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 137;BA.debugLine="AddEvent(\"Disconnected from MQTT broker\")";
_addevent("Disconnected from MQTT broker");
 //BA.debugLineNum = 138;BA.debugLine="MQTT_Connect";
_mqtt_connect();
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 140;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 142;BA.debugLine="End Sub";
return "";
}
public static String  _mqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
String _status = "";
String[] _a = null;
anywheresoftware.b4a.objects.CSBuilder _cs = null;
 //BA.debugLineNum = 144;BA.debugLine="Private Sub MQTT_MessageArrived (Topic As String,";
 //BA.debugLineNum = 145;BA.debugLine="Try";
try { //BA.debugLineNum = 146;BA.debugLine="lblPing.SetVisibleAnimated(500, True)";
mostCurrent._lblping.SetVisibleAnimated((int) (500),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 147;BA.debugLine="csu.CallSubPlus(Me, \"HidePing\", 700)";
_csu._v7(main.getObject(),"HidePing",(int) (700));
 //BA.debugLineNum = 149;BA.debugLine="Dim status As String";
_status = "";
 //BA.debugLineNum = 150;BA.debugLine="status = BytesToString(Payload, 0, Payload.Lengt";
_status = anywheresoftware.b4a.keywords.Common.BytesToString(_payload,(int) (0),_payload.length,"UTF8");
 //BA.debugLineNum = 151;BA.debugLine="AddEvent(status)";
_addevent(_status);
 //BA.debugLineNum = 153;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",status)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_status);
 //BA.debugLineNum = 154;BA.debugLine="If a.Length = 7 Then";
if (_a.length==7) { 
 //BA.debugLineNum = 155;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 156;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 157;BA.debugLine="If a(0) = \"OK\" Then";
if ((_a[(int) (0)]).equals("OK")) { 
 //BA.debugLineNum = 158;BA.debugLine="GaugeTemp.CurrentValue = a(1)";
mostCurrent._gaugetemp._setcurrentvalue((float)(Double.parseDouble(_a[(int) (1)])));
 //BA.debugLineNum = 159;BA.debugLine="GaugeHumidity.CurrentValue = a(2)";
mostCurrent._gaugehumidity._setcurrentvalue((float)(Double.parseDouble(_a[(int) (2)])));
 //BA.debugLineNum = 160;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Append(BA.ObjectToCharSequence(_getperception(_a[(int) (3)]))).PopAll().getObject()));
 //BA.debugLineNum = 161;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"T";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Append(BA.ObjectToCharSequence(_getcomfort(_a[(int) (4)]))).PopAll().getObject()));
 //BA.debugLineNum = 162;BA.debugLine="GaugeHeatIndex.CurrentValue = a(5)";
mostCurrent._gaugeheatindex._setcurrentvalue((float)(Double.parseDouble(_a[(int) (5)])));
 //BA.debugLineNum = 163;BA.debugLine="GaugeDewPoint.CurrentValue = a(6)";
mostCurrent._gaugedewpoint._setcurrentvalue((float)(Double.parseDouble(_a[(int) (6)])));
 }else {
 //BA.debugLineNum = 165;BA.debugLine="GaugeTemp.CurrentValue = 0";
mostCurrent._gaugetemp._setcurrentvalue((float) (0));
 //BA.debugLineNum = 166;BA.debugLine="GaugeHumidity.CurrentValue = 0";
mostCurrent._gaugehumidity._setcurrentvalue((float) (0));
 //BA.debugLineNum = 167;BA.debugLine="lblPerception.Text = cs.Initialize.Bold.Append";
mostCurrent._lblperception.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Human Perception: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_a[(int) (0)])).PopAll().getObject()));
 //BA.debugLineNum = 168;BA.debugLine="lblComfort.Text = cs.Initialize.Bold.Append(\"T";
mostCurrent._lblcomfort.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Thermal Comfort: ")).Pop().Color(anywheresoftware.b4a.keywords.Common.Colors.Red).Append(BA.ObjectToCharSequence(_a[(int) (0)])).PopAll().getObject()));
 //BA.debugLineNum = 169;BA.debugLine="GaugeHeatIndex.CurrentValue = 0";
mostCurrent._gaugeheatindex._setcurrentvalue((float) (0));
 //BA.debugLineNum = 170;BA.debugLine="GaugeDewPoint.CurrentValue = 0";
mostCurrent._gaugedewpoint._setcurrentvalue((float) (0));
 };
 //BA.debugLineNum = 172;BA.debugLine="DateTime.DateFormat = \"MMMM d, h:mm:ss a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMMM d, h:mm:ss a");
 //BA.debugLineNum = 173;BA.debugLine="lblLastUpdate.Text = cs.Initialize.Bold.Append(";
mostCurrent._lbllastupdate.setText(BA.ObjectToCharSequence(_cs.Initialize().Bold().Append(BA.ObjectToCharSequence("Last update: ")).Pop().Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()))).PopAll().getObject()));
 };
 } 
       catch (Exception e30) {
			processBA.setLastException(e30); //BA.debugLineNum = 176;BA.debugLine="AddEvent(\"MQTT_MessageArrived: \" & LastException";
_addevent("MQTT_MessageArrived: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 17;BA.debugLine="Private MQTT As MqttClient";
_mqtt = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private MQTTUser As String = \"vynckfaq\"";
_mqttuser = "vynckfaq";
 //BA.debugLineNum = 19;BA.debugLine="Private MQTTPassword As String = \"KHSV1Q1qSUUY\"";
_mqttpassword = "KHSV1Q1qSUUY";
 //BA.debugLineNum = 20;BA.debugLine="Private MQTTServerURI As String = \"tcp://m14.clou";
_mqttserveruri = "tcp://m14.cloudmqtt.com:11816";
 //BA.debugLineNum = 21;BA.debugLine="Private bc As ByteConverter";
_bc = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 22;BA.debugLine="Private csu As CallSubUtils";
_csu = new b4a.example.callsubutils();
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _showaboutmenu() throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 265;BA.debugLine="Sub ShowAboutMenu";
 //BA.debugLineNum = 266;BA.debugLine="Try";
try { //BA.debugLineNum = 267;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 268;BA.debugLine="bd.Initialize(LoadBitmapResize(File.DirAssets, \"";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"0.png",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 269;BA.debugLine="Msgbox2(\"Cloyd Home Monitor v\" & GetVersionCode";
anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Cloyd Home Monitor v"+_getversioncode()+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Developed by Cloyd Nino Catanaoan"+anywheresoftware.b4a.keywords.Common.CRLF+"March 13, 2018"),BA.ObjectToCharSequence("About"),"OK","","",_bd.getBitmap(),mostCurrent.activityBA);
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 271;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 274;BA.debugLine="End Sub";
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
