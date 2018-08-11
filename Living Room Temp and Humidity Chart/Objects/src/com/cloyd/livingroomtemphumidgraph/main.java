package com.cloyd.livingroomtemphumidgraph;


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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.cloyd.livingroomtemphumidgraph", "com.cloyd.livingroomtemphumidgraph.main");
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
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
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
		activityBA = new BA(this, layout, processBA, "com.cloyd.livingroomtemphumidgraph", "com.cloyd.livingroomtemphumidgraph.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.cloyd.livingroomtemphumidgraph.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public static anywheresoftware.b4a.objects.Timer _timer1 = null;
public androidplotwrapper.lineChartWrapper _lc1 = null;
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
public com.cloyd.livingroomtemphumidgraph.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.keywords.LayoutValues _lv = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
long _yesterday = 0L;
 //BA.debugLineNum = 54;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 55;BA.debugLine="Try";
try { //BA.debugLineNum = 57;BA.debugLine="Activity_WindowFocusChanged(True)";
_activity_windowfocuschanged(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 58;BA.debugLine="Dim lv As LayoutValues = GetRealSize";
_lv = _getrealsize();
 //BA.debugLineNum = 59;BA.debugLine="Dim jo As JavaObject = Activity";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._activity.getObject()));
 //BA.debugLineNum = 60;BA.debugLine="jo.RunMethod(\"setBottom\", Array(lv.Height))";
_jo.RunMethod("setBottom",new Object[]{(Object)(_lv.Height)});
 //BA.debugLineNum = 61;BA.debugLine="jo.RunMethod(\"setRight\", Array(lv.Width))";
_jo.RunMethod("setRight",new Object[]{(Object)(_lv.Width)});
 //BA.debugLineNum = 62;BA.debugLine="Activity.Height = lv.Height";
mostCurrent._activity.setHeight(_lv.Height);
 //BA.debugLineNum = 63;BA.debugLine="Activity.Width = lv.Width";
mostCurrent._activity.setWidth(_lv.Width);
 //BA.debugLineNum = 66;BA.debugLine="Activity.LoadLayout(\"main\")";
mostCurrent._activity.LoadLayout("main",mostCurrent.activityBA);
 //BA.debugLineNum = 68;BA.debugLine="lc1.GraphBackgroundColor = Colors.Transparent 'C";
mostCurrent._lc1.setGraphBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 69;BA.debugLine="lc1.GraphFrameColor = Colors.Blue";
mostCurrent._lc1.setGraphFrameColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 70;BA.debugLine="lc1.GraphFrameWidth = 4.0";
mostCurrent._lc1.setGraphFrameWidth((float) (4.0));
 //BA.debugLineNum = 71;BA.debugLine="lc1.GraphPlotAreaBackgroundColor = Colors.DarkGr";
mostCurrent._lc1.setGraphPlotAreaBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 72;BA.debugLine="lc1.GraphTitleTextSize = 15";
mostCurrent._lc1.setGraphTitleTextSize((int) (15));
 //BA.debugLineNum = 73;BA.debugLine="lc1.GraphTitleColor = Colors.White";
mostCurrent._lc1.setGraphTitleColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 74;BA.debugLine="lc1.GraphTitleSkewX = -0.25";
mostCurrent._lc1.setGraphTitleSkewX((float) (-0.25));
 //BA.debugLineNum = 75;BA.debugLine="lc1.GraphTitleUnderline = True";
mostCurrent._lc1.setGraphTitleUnderline(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 76;BA.debugLine="lc1.GraphTitleBold = True";
mostCurrent._lc1.setGraphTitleBold(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 77;BA.debugLine="lc1.GraphTitle = \"CATANAOAN HOME TEMPERATURE v1.";
mostCurrent._lc1.setGraphTitle("CATANAOAN HOME TEMPERATURE v1.0");
 //BA.debugLineNum = 79;BA.debugLine="lc1.LegendBackgroundColor = Colors.White";
mostCurrent._lc1.setLegendBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 80;BA.debugLine="lc1.LegendTextColor = Colors.Black";
mostCurrent._lc1.setLegendTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 81;BA.debugLine="lc1.LegendTextSize = 18.0";
mostCurrent._lc1.setLegendTextSize((float) (18.0));
 //BA.debugLineNum = 83;BA.debugLine="DateTime.TimeFormat = \"h:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("h:mm a");
 //BA.debugLineNum = 84;BA.debugLine="lc1.DomianLabel = \"The time now is: \" & DateTime";
mostCurrent._lc1.setDomianLabel("The time now is: "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 85;BA.debugLine="lc1.DomainLabelColor = Colors.Green";
mostCurrent._lc1.setDomainLabelColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 86;BA.debugLine="lc1.DomainLabelTextSize = 25.0";
mostCurrent._lc1.setDomainLabelTextSize((float) (25.0));
 //BA.debugLineNum = 88;BA.debugLine="lc1.XaxisGridLineColor = Colors.ARGB(100,255,255";
mostCurrent._lc1.setXaxisGridLineColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 89;BA.debugLine="lc1.XaxisGridLineWidth = 2.0";
mostCurrent._lc1.setXaxisGridLineWidth((float) (2.0));
 //BA.debugLineNum = 90;BA.debugLine="lc1.XaxisLabelTicks = 1";
mostCurrent._lc1.setXaxisLabelTicks((int) (1));
 //BA.debugLineNum = 91;BA.debugLine="lc1.XaxisLabelOrientation = 0";
mostCurrent._lc1.setXaxisLabelOrientation((float) (0));
 //BA.debugLineNum = 92;BA.debugLine="lc1.XaxisLabelTextColor = Colors.Red";
mostCurrent._lc1.setXaxisLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 93;BA.debugLine="lc1.XaxisLabelTextSize = 32.0";
mostCurrent._lc1.setXaxisLabelTextSize((float) (32.0));
 //BA.debugLineNum = 94;BA.debugLine="lc1.XAxisLabels = Array As String(\"12 am\",\"1 am\"";
mostCurrent._lc1.setXAxisLabels(new String[]{"12 am","1 am","2 am","3 am","4 am","5 am","6 am","7 am","8 am","9 am","10 am","11 am","12 pm","1 pm","2 pm","3 pm","4 pm","5 pm","6 pm","7 pm","8 pm","9 pm","10 pm","11 pm"});
 //BA.debugLineNum = 96;BA.debugLine="lc1.YaxisDivisions = 10";
mostCurrent._lc1.setYaxisDivisions((int) (10));
 //BA.debugLineNum = 97;BA.debugLine="lc1.YaxisRange(65, 85)";
mostCurrent._lc1.YaxisRange((float) (65),(float) (85));
 //BA.debugLineNum = 98;BA.debugLine="lc1.YaxisValueFormat = lc1.ValueFormat_2";
mostCurrent._lc1.setYaxisValueFormat(mostCurrent._lc1.ValueFormat_2);
 //BA.debugLineNum = 99;BA.debugLine="lc1.YaxisGridLineColor = Colors.Black";
mostCurrent._lc1.setYaxisGridLineColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 100;BA.debugLine="lc1.YaxisGridLineWidth = 2";
mostCurrent._lc1.setYaxisGridLineWidth((float) (2));
 //BA.debugLineNum = 101;BA.debugLine="lc1.YaxisLabelTicks = 1";
mostCurrent._lc1.setYaxisLabelTicks((int) (1));
 //BA.debugLineNum = 102;BA.debugLine="lc1.YaxisLabelColor = Colors.Yellow";
mostCurrent._lc1.setYaxisLabelColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 103;BA.debugLine="lc1.YaxisLabelOrientation = -30";
mostCurrent._lc1.setYaxisLabelOrientation((float) (-30));
 //BA.debugLineNum = 104;BA.debugLine="lc1.YaxisLabelTextSize = 25.0";
mostCurrent._lc1.setYaxisLabelTextSize((float) (25.0));
 //BA.debugLineNum = 105;BA.debugLine="lc1.YaxisTitleColor = Colors.Green";
mostCurrent._lc1.setYaxisTitleColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 106;BA.debugLine="lc1.YaxisTitleFakeBold = False";
mostCurrent._lc1.setYaxisTitleFakeBold(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 107;BA.debugLine="lc1.YaxisTitleTextSize = 20.0";
mostCurrent._lc1.setYaxisTitleTextSize((float) (20.0));
 //BA.debugLineNum = 108;BA.debugLine="lc1.YaxisTitleUnderline = True";
mostCurrent._lc1.setYaxisTitleUnderline(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 109;BA.debugLine="lc1.YaxisTitleTextSkewness = 0";
mostCurrent._lc1.setYaxisTitleTextSkewness((float) (0));
 //BA.debugLineNum = 110;BA.debugLine="lc1.YaxisLabelAndTitleDistance = 60.0";
mostCurrent._lc1.setYaxisLabelAndTitleDistance((float) (60.0));
 //BA.debugLineNum = 111;BA.debugLine="lc1.YaxisTitle = \"Temperature (Fahrenheit)\"";
mostCurrent._lc1.setYaxisTitle("Temperature (Fahrenheit)");
 //BA.debugLineNum = 113;BA.debugLine="lc1.MaxNumberOfEntriesPerLineChart = 24";
mostCurrent._lc1.setMaxNumberOfEntriesPerLineChart((int) (24));
 //BA.debugLineNum = 117;BA.debugLine="ReadTextReader(\"Today\")";
_readtextreader("Today");
 //BA.debugLineNum = 119;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy");
 //BA.debugLineNum = 120;BA.debugLine="lc1.Line_1_LegendText = \"Today, \" & DateTime.Dat";
mostCurrent._lc1.setLine_1_LegendText("Today, "+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 122;BA.debugLine="If am12 <> \"0\" Then";
if ((mostCurrent._am12).equals("0") == false) { 
 //BA.debugLineNum = 123;BA.debugLine="lc1.Line_1_Data = Array As Float (am12)";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12))});
 };
 //BA.debugLineNum = 125;BA.debugLine="If am1 <> \"0\" Then";
if ((mostCurrent._am1).equals("0") == false) { 
 //BA.debugLineNum = 126;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1)";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1))});
 };
 //BA.debugLineNum = 128;BA.debugLine="If am2 <> \"0\" Then";
if ((mostCurrent._am2).equals("0") == false) { 
 //BA.debugLineNum = 129;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2))});
 };
 //BA.debugLineNum = 131;BA.debugLine="If am3 <> \"0\" Then";
if ((mostCurrent._am3).equals("0") == false) { 
 //BA.debugLineNum = 132;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3))});
 };
 //BA.debugLineNum = 134;BA.debugLine="If am4 <> \"0\" Then";
if ((mostCurrent._am4).equals("0") == false) { 
 //BA.debugLineNum = 135;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4))});
 };
 //BA.debugLineNum = 137;BA.debugLine="If am5 <> \"0\" Then";
if ((mostCurrent._am5).equals("0") == false) { 
 //BA.debugLineNum = 138;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5))});
 };
 //BA.debugLineNum = 140;BA.debugLine="If am6 <> \"0\" Then";
if ((mostCurrent._am6).equals("0") == false) { 
 //BA.debugLineNum = 141;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6))});
 };
 //BA.debugLineNum = 143;BA.debugLine="If am7 <> \"0\" Then";
if ((mostCurrent._am7).equals("0") == false) { 
 //BA.debugLineNum = 144;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7))});
 };
 //BA.debugLineNum = 146;BA.debugLine="If am8 <> \"0\" Then";
if ((mostCurrent._am8).equals("0") == false) { 
 //BA.debugLineNum = 147;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8))});
 };
 //BA.debugLineNum = 149;BA.debugLine="If am9 <> \"0\" Then";
if ((mostCurrent._am9).equals("0") == false) { 
 //BA.debugLineNum = 150;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9))});
 };
 //BA.debugLineNum = 152;BA.debugLine="If am10 <> \"0\" Then";
if ((mostCurrent._am10).equals("0") == false) { 
 //BA.debugLineNum = 153;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10))});
 };
 //BA.debugLineNum = 155;BA.debugLine="If am11 <> \"0\" Then";
if ((mostCurrent._am11).equals("0") == false) { 
 //BA.debugLineNum = 156;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11))});
 };
 //BA.debugLineNum = 158;BA.debugLine="If pm12 <> \"0\" Then";
if ((mostCurrent._pm12).equals("0") == false) { 
 //BA.debugLineNum = 159;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12))});
 };
 //BA.debugLineNum = 161;BA.debugLine="If pm1 <> \"0\" Then";
if ((mostCurrent._pm1).equals("0") == false) { 
 //BA.debugLineNum = 162;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1))});
 };
 //BA.debugLineNum = 164;BA.debugLine="If pm2 <> \"0\" Then";
if ((mostCurrent._pm2).equals("0") == false) { 
 //BA.debugLineNum = 165;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2))});
 };
 //BA.debugLineNum = 167;BA.debugLine="If pm3 <> \"0\" Then";
if ((mostCurrent._pm3).equals("0") == false) { 
 //BA.debugLineNum = 168;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3))});
 };
 //BA.debugLineNum = 170;BA.debugLine="If pm4 <> \"0\" Then";
if ((mostCurrent._pm4).equals("0") == false) { 
 //BA.debugLineNum = 171;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4))});
 };
 //BA.debugLineNum = 173;BA.debugLine="If pm5 <> \"0\" Then";
if ((mostCurrent._pm5).equals("0") == false) { 
 //BA.debugLineNum = 174;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5))});
 };
 //BA.debugLineNum = 176;BA.debugLine="If pm6 <> \"0\" Then";
if ((mostCurrent._pm6).equals("0") == false) { 
 //BA.debugLineNum = 177;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6))});
 };
 //BA.debugLineNum = 179;BA.debugLine="If pm7 <> \"0\" Then";
if ((mostCurrent._pm7).equals("0") == false) { 
 //BA.debugLineNum = 180;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7))});
 };
 //BA.debugLineNum = 182;BA.debugLine="If pm8 <> \"0\" Then";
if ((mostCurrent._pm8).equals("0") == false) { 
 //BA.debugLineNum = 183;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8))});
 };
 //BA.debugLineNum = 185;BA.debugLine="If pm9 <> \"0\" Then";
if ((mostCurrent._pm9).equals("0") == false) { 
 //BA.debugLineNum = 186;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9))});
 };
 //BA.debugLineNum = 188;BA.debugLine="If pm10 <> \"0\" Then";
if ((mostCurrent._pm10).equals("0") == false) { 
 //BA.debugLineNum = 189;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10))});
 };
 //BA.debugLineNum = 191;BA.debugLine="If pm11 <> \"0\" Then";
if ((mostCurrent._pm11).equals("0") == false) { 
 //BA.debugLineNum = 192;BA.debugLine="lc1.Line_1_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_1_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))});
 };
 //BA.debugLineNum = 195;BA.debugLine="lc1.Line_1_PointLabelTextColor = Colors.Yellow";
mostCurrent._lc1.setLine_1_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 196;BA.debugLine="lc1.Line_1_PointLabelTextSize = 35.0";
mostCurrent._lc1.setLine_1_PointLabelTextSize((float) (35.0));
 //BA.debugLineNum = 197;BA.debugLine="lc1.Line_1_LineColor = Colors.Red";
mostCurrent._lc1.setLine_1_LineColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 198;BA.debugLine="lc1.Line_1_LineWidth = 11.0";
mostCurrent._lc1.setLine_1_LineWidth((float) (11.0));
 //BA.debugLineNum = 199;BA.debugLine="lc1.Line_1_PointColor = Colors.Black";
mostCurrent._lc1.setLine_1_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 200;BA.debugLine="lc1.Line_1_PointSize = 20.0";
mostCurrent._lc1.setLine_1_PointSize((float) (20.0));
 //BA.debugLineNum = 201;BA.debugLine="lc1.Line_1_PointShape = lc1.SHAPE_ROUND";
mostCurrent._lc1.setLine_1_PointShape(mostCurrent._lc1.SHAPE_ROUND);
 //BA.debugLineNum = 202;BA.debugLine="lc1.Line_1_DrawDash = False";
mostCurrent._lc1.setLine_1_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 203;BA.debugLine="lc1.Line_1_DrawCubic = False";
mostCurrent._lc1.setLine_1_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 209;BA.debugLine="ReadTextReader(\"Yesterday\")";
_readtextreader("Yesterday");
 //BA.debugLineNum = 211;BA.debugLine="Dim Yesterday As Long";
_yesterday = 0L;
 //BA.debugLineNum = 212;BA.debugLine="Yesterday = DateTime.add(DateTime.Now, 0, 0, -1)";
_yesterday = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1));
 //BA.debugLineNum = 214;BA.debugLine="DateTime.DateFormat = \"MMM d, yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM d, yyyy");
 //BA.debugLineNum = 215;BA.debugLine="lc1.Line_2_LegendText = \"Yesterday, \" & DateTime";
mostCurrent._lc1.setLine_2_LegendText("Yesterday, "+anywheresoftware.b4a.keywords.Common.DateTime.Date(_yesterday));
 //BA.debugLineNum = 217;BA.debugLine="If am12 <> \"0\" Then";
if ((mostCurrent._am12).equals("0") == false) { 
 //BA.debugLineNum = 218;BA.debugLine="lc1.Line_2_Data = Array As Float (am12)";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12))});
 };
 //BA.debugLineNum = 220;BA.debugLine="If am1 <> \"0\" Then";
if ((mostCurrent._am1).equals("0") == false) { 
 //BA.debugLineNum = 221;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1)";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1))});
 };
 //BA.debugLineNum = 223;BA.debugLine="If am2 <> \"0\" Then";
if ((mostCurrent._am2).equals("0") == false) { 
 //BA.debugLineNum = 224;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2))});
 };
 //BA.debugLineNum = 226;BA.debugLine="If am3 <> \"0\" Then";
if ((mostCurrent._am3).equals("0") == false) { 
 //BA.debugLineNum = 227;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3))});
 };
 //BA.debugLineNum = 229;BA.debugLine="If am4 <> \"0\" Then";
if ((mostCurrent._am4).equals("0") == false) { 
 //BA.debugLineNum = 230;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4))});
 };
 //BA.debugLineNum = 232;BA.debugLine="If am5 <> \"0\" Then";
if ((mostCurrent._am5).equals("0") == false) { 
 //BA.debugLineNum = 233;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5))});
 };
 //BA.debugLineNum = 235;BA.debugLine="If am6 <> \"0\" Then";
if ((mostCurrent._am6).equals("0") == false) { 
 //BA.debugLineNum = 236;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6))});
 };
 //BA.debugLineNum = 238;BA.debugLine="If am7 <> \"0\" Then";
if ((mostCurrent._am7).equals("0") == false) { 
 //BA.debugLineNum = 239;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7))});
 };
 //BA.debugLineNum = 241;BA.debugLine="If am8 <> \"0\" Then";
if ((mostCurrent._am8).equals("0") == false) { 
 //BA.debugLineNum = 242;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8))});
 };
 //BA.debugLineNum = 244;BA.debugLine="If am9 <> \"0\" Then";
if ((mostCurrent._am9).equals("0") == false) { 
 //BA.debugLineNum = 245;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9))});
 };
 //BA.debugLineNum = 247;BA.debugLine="If am10 <> \"0\" Then";
if ((mostCurrent._am10).equals("0") == false) { 
 //BA.debugLineNum = 248;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10))});
 };
 //BA.debugLineNum = 250;BA.debugLine="If am11 <> \"0\" Then";
if ((mostCurrent._am11).equals("0") == false) { 
 //BA.debugLineNum = 251;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11))});
 };
 //BA.debugLineNum = 253;BA.debugLine="If pm12 <> \"0\" Then";
if ((mostCurrent._pm12).equals("0") == false) { 
 //BA.debugLineNum = 254;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12))});
 };
 //BA.debugLineNum = 256;BA.debugLine="If pm1 <> \"0\" Then";
if ((mostCurrent._pm1).equals("0") == false) { 
 //BA.debugLineNum = 257;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1))});
 };
 //BA.debugLineNum = 259;BA.debugLine="If pm2 <> \"0\" Then";
if ((mostCurrent._pm2).equals("0") == false) { 
 //BA.debugLineNum = 260;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2))});
 };
 //BA.debugLineNum = 262;BA.debugLine="If pm3 <> \"0\" Then";
if ((mostCurrent._pm3).equals("0") == false) { 
 //BA.debugLineNum = 263;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3))});
 };
 //BA.debugLineNum = 265;BA.debugLine="If pm4 <> \"0\" Then";
if ((mostCurrent._pm4).equals("0") == false) { 
 //BA.debugLineNum = 266;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4))});
 };
 //BA.debugLineNum = 268;BA.debugLine="If pm5 <> \"0\" Then";
if ((mostCurrent._pm5).equals("0") == false) { 
 //BA.debugLineNum = 269;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5))});
 };
 //BA.debugLineNum = 271;BA.debugLine="If pm6 <> \"0\" Then";
if ((mostCurrent._pm6).equals("0") == false) { 
 //BA.debugLineNum = 272;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6))});
 };
 //BA.debugLineNum = 274;BA.debugLine="If pm7 <> \"0\" Then";
if ((mostCurrent._pm7).equals("0") == false) { 
 //BA.debugLineNum = 275;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7))});
 };
 //BA.debugLineNum = 277;BA.debugLine="If pm8 <> \"0\" Then";
if ((mostCurrent._pm8).equals("0") == false) { 
 //BA.debugLineNum = 278;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8))});
 };
 //BA.debugLineNum = 280;BA.debugLine="If pm9 <> \"0\" Then";
if ((mostCurrent._pm9).equals("0") == false) { 
 //BA.debugLineNum = 281;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9))});
 };
 //BA.debugLineNum = 283;BA.debugLine="If pm10 <> \"0\" Then";
if ((mostCurrent._pm10).equals("0") == false) { 
 //BA.debugLineNum = 284;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10))});
 };
 //BA.debugLineNum = 286;BA.debugLine="If pm11 <> \"0\" Then";
if ((mostCurrent._pm11).equals("0") == false) { 
 //BA.debugLineNum = 287;BA.debugLine="lc1.Line_2_Data = Array As Float (am12, am1, am";
mostCurrent._lc1.setLine_2_Data(new float[]{(float)(Double.parseDouble(mostCurrent._am12)),(float)(Double.parseDouble(mostCurrent._am1)),(float)(Double.parseDouble(mostCurrent._am2)),(float)(Double.parseDouble(mostCurrent._am3)),(float)(Double.parseDouble(mostCurrent._am4)),(float)(Double.parseDouble(mostCurrent._am5)),(float)(Double.parseDouble(mostCurrent._am6)),(float)(Double.parseDouble(mostCurrent._am7)),(float)(Double.parseDouble(mostCurrent._am8)),(float)(Double.parseDouble(mostCurrent._am9)),(float)(Double.parseDouble(mostCurrent._am10)),(float)(Double.parseDouble(mostCurrent._am11)),(float)(Double.parseDouble(mostCurrent._pm12)),(float)(Double.parseDouble(mostCurrent._pm1)),(float)(Double.parseDouble(mostCurrent._pm2)),(float)(Double.parseDouble(mostCurrent._pm3)),(float)(Double.parseDouble(mostCurrent._pm4)),(float)(Double.parseDouble(mostCurrent._pm5)),(float)(Double.parseDouble(mostCurrent._pm6)),(float)(Double.parseDouble(mostCurrent._pm7)),(float)(Double.parseDouble(mostCurrent._pm8)),(float)(Double.parseDouble(mostCurrent._pm9)),(float)(Double.parseDouble(mostCurrent._pm10)),(float)(Double.parseDouble(mostCurrent._pm11))});
 };
 //BA.debugLineNum = 290;BA.debugLine="lc1.Line_2_PointLabelTextColor = Colors.Cyan";
mostCurrent._lc1.setLine_2_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 291;BA.debugLine="lc1.Line_2_PointLabelTextSize = 35.0";
mostCurrent._lc1.setLine_2_PointLabelTextSize((float) (35.0));
 //BA.debugLineNum = 292;BA.debugLine="lc1.Line_2_LineColor = Colors.White";
mostCurrent._lc1.setLine_2_LineColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 293;BA.debugLine="lc1.Line_2_LineWidth = 7.0";
mostCurrent._lc1.setLine_2_LineWidth((float) (7.0));
 //BA.debugLineNum = 294;BA.debugLine="lc1.Line_2_PointColor = Colors.Cyan";
mostCurrent._lc1.setLine_2_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 295;BA.debugLine="lc1.Line_2_PointSize = 10.0";
mostCurrent._lc1.setLine_2_PointSize((float) (10.0));
 //BA.debugLineNum = 296;BA.debugLine="lc1.Line_2_PointShape = lc1.SHAPE_ROUND";
mostCurrent._lc1.setLine_2_PointShape(mostCurrent._lc1.SHAPE_ROUND);
 //BA.debugLineNum = 297;BA.debugLine="lc1.Line_2_DrawDash = False";
mostCurrent._lc1.setLine_2_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 298;BA.debugLine="lc1.Line_2_DrawCubic = False";
mostCurrent._lc1.setLine_2_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 304;BA.debugLine="lc1.Line_3_LegendText = \"Last 2 minutes\"";
mostCurrent._lc1.setLine_3_LegendText("Last 2 minutes");
 //BA.debugLineNum = 305;BA.debugLine="lc1.Line_3_Data = Array As Float (tempRightNow,";
mostCurrent._lc1.setLine_3_Data(new float[]{(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow)),(float)(Double.parseDouble(mostCurrent._temprightnow))});
 //BA.debugLineNum = 306;BA.debugLine="lc1.Line_3_PointLabelTextColor = Colors.Green";
mostCurrent._lc1.setLine_3_PointLabelTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 307;BA.debugLine="lc1.Line_3_PointLabelTextSize = 30.0";
mostCurrent._lc1.setLine_3_PointLabelTextSize((float) (30.0));
 //BA.debugLineNum = 308;BA.debugLine="lc1.Line_3_LineColor = Colors.Green";
mostCurrent._lc1.setLine_3_LineColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 309;BA.debugLine="lc1.Line_3_LineWidth = 5.0";
mostCurrent._lc1.setLine_3_LineWidth((float) (5.0));
 //BA.debugLineNum = 310;BA.debugLine="lc1.Line_3_PointColor = Colors.Green";
mostCurrent._lc1.setLine_3_PointColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 311;BA.debugLine="lc1.Line_3_PointSize = 8.0";
mostCurrent._lc1.setLine_3_PointSize((float) (8.0));
 //BA.debugLineNum = 312;BA.debugLine="lc1.Line_3_PointShape = lc1.SHAPE_ROUND";
mostCurrent._lc1.setLine_3_PointShape(mostCurrent._lc1.SHAPE_ROUND);
 //BA.debugLineNum = 313;BA.debugLine="lc1.Line_3_DrawDash = False";
mostCurrent._lc1.setLine_3_DrawDash(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 314;BA.debugLine="lc1.Line_3_DrawCubic = False";
mostCurrent._lc1.setLine_3_DrawCubic(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 318;BA.debugLine="lc1.NumberOfLineCharts = 3";
mostCurrent._lc1.setNumberOfLineCharts((int) (3));
 //BA.debugLineNum = 320;BA.debugLine="lc1.DrawTheGraphs";
mostCurrent._lc1.DrawTheGraphs();
 //BA.debugLineNum = 322;BA.debugLine="Timer1.Initialize(\"Timer1\",1000 * 60) 'check eac";
_timer1.Initialize(processBA,"Timer1",(long) (1000*60));
 //BA.debugLineNum = 323;BA.debugLine="Timer1.Enabled = True 'start timer";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 } 
       catch (Exception e237) {
			processBA.setLastException(e237); //BA.debugLineNum = 325;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 //BA.debugLineNum = 326;BA.debugLine="ToastMessageShow (LastException,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 329;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 335;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 337;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 331;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 333;BA.debugLine="End Sub";
return "";
}
public static void  _activity_windowfocuschanged(boolean _hasfocus) throws Exception{
ResumableSub_Activity_WindowFocusChanged rsub = new ResumableSub_Activity_WindowFocusChanged(null,_hasfocus);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_WindowFocusChanged extends BA.ResumableSub {
public ResumableSub_Activity_WindowFocusChanged(com.cloyd.livingroomtemphumidgraph.main parent,boolean _hasfocus) {
this.parent = parent;
this._hasfocus = _hasfocus;
}
com.cloyd.livingroomtemphumidgraph.main parent;
boolean _hasfocus;
anywheresoftware.b4j.object.JavaObject _jo = null;

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
 //BA.debugLineNum = 485;BA.debugLine="If HasFocus Then";
if (true) break;

case 1:
//if
this.state = 10;
if (_hasfocus) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 486;BA.debugLine="Try";
if (true) break;

case 4:
//try
this.state = 9;
this.catchState = 8;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 9;
this.catchState = 8;
 //BA.debugLineNum = 487;BA.debugLine="Dim jo As JavaObject = Activity";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(parent.mostCurrent._activity.getObject()));
 //BA.debugLineNum = 488;BA.debugLine="Sleep(300)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (300));
this.state = 11;
return;
case 11:
//C
this.state = 9;
;
 //BA.debugLineNum = 489;BA.debugLine="jo.RunMethod(\"setSystemUiVisibility\", Array As";
_jo.RunMethod("setSystemUiVisibility",new Object[]{(Object)(5894)});
 if (true) break;

case 8:
//C
this.state = 9;
this.catchState = 0;
 if (true) break;
if (true) break;

case 9:
//C
this.state = 10;
this.catchState = 0;
;
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 495;BA.debugLine="End Sub";
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
public static anywheresoftware.b4a.keywords.LayoutValues  _getrealsize() throws Exception{
anywheresoftware.b4a.keywords.LayoutValues _lv = null;
anywheresoftware.b4a.phone.Phone _p = null;
anywheresoftware.b4j.object.JavaObject _ctxt = null;
anywheresoftware.b4j.object.JavaObject _display = null;
anywheresoftware.b4j.object.JavaObject _point = null;
 //BA.debugLineNum = 464;BA.debugLine="Sub GetRealSize As LayoutValues";
 //BA.debugLineNum = 465;BA.debugLine="Dim lv As LayoutValues";
_lv = new anywheresoftware.b4a.keywords.LayoutValues();
 //BA.debugLineNum = 466;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 467;BA.debugLine="If p.SdkVersion >= 17 Then";
if (_p.getSdkVersion()>=17) { 
 //BA.debugLineNum = 468;BA.debugLine="Dim ctxt As JavaObject";
_ctxt = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 469;BA.debugLine="ctxt.InitializeContext";
_ctxt.InitializeContext(processBA);
 //BA.debugLineNum = 470;BA.debugLine="Dim display As JavaObject = ctxt.RunMethodJO(\"ge";
_display = new anywheresoftware.b4j.object.JavaObject();
_display.setObject((java.lang.Object)(_ctxt.RunMethodJO("getSystemService",new Object[]{(Object)("window")}).RunMethod("getDefaultDisplay",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 471;BA.debugLine="Dim point As JavaObject";
_point = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 472;BA.debugLine="point.InitializeNewInstance(\"android.graphics.Po";
_point.InitializeNewInstance("android.graphics.Point",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 473;BA.debugLine="display.RunMethod(\"getRealSize\", Array(point))";
_display.RunMethod("getRealSize",new Object[]{(Object)(_point.getObject())});
 //BA.debugLineNum = 474;BA.debugLine="lv.Width = point.GetField(\"x\")";
_lv.Width = (int)(BA.ObjectToNumber(_point.GetField("x")));
 //BA.debugLineNum = 475;BA.debugLine="lv.Height = point.GetField(\"y\")";
_lv.Height = (int)(BA.ObjectToNumber(_point.GetField("y")));
 }else {
 //BA.debugLineNum = 477;BA.debugLine="lv.Width = 100%x";
_lv.Width = anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA);
 //BA.debugLineNum = 478;BA.debugLine="lv.Height = 100%y";
_lv.Height = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 480;BA.debugLine="lv.Scale = 100dip / 100";
_lv.Scale = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100))/(double)100);
 //BA.debugLineNum = 481;BA.debugLine="Return lv";
if (true) return _lv;
 //BA.debugLineNum = 482;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 26;BA.debugLine="Private lc1 As LineChart";
mostCurrent._lc1 = new androidplotwrapper.lineChartWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private am12 As String";
mostCurrent._am12 = "";
 //BA.debugLineNum = 28;BA.debugLine="Private am1 As String";
mostCurrent._am1 = "";
 //BA.debugLineNum = 29;BA.debugLine="Private am2 As String";
mostCurrent._am2 = "";
 //BA.debugLineNum = 30;BA.debugLine="Private am3 As String";
mostCurrent._am3 = "";
 //BA.debugLineNum = 31;BA.debugLine="Private am4 As String";
mostCurrent._am4 = "";
 //BA.debugLineNum = 32;BA.debugLine="Private am5 As String";
mostCurrent._am5 = "";
 //BA.debugLineNum = 33;BA.debugLine="Private am6 As String";
mostCurrent._am6 = "";
 //BA.debugLineNum = 34;BA.debugLine="Private am7 As String";
mostCurrent._am7 = "";
 //BA.debugLineNum = 35;BA.debugLine="Private am8 As String";
mostCurrent._am8 = "";
 //BA.debugLineNum = 36;BA.debugLine="Private am9 As String";
mostCurrent._am9 = "";
 //BA.debugLineNum = 37;BA.debugLine="Private am10 As String";
mostCurrent._am10 = "";
 //BA.debugLineNum = 38;BA.debugLine="Private am11 As String";
mostCurrent._am11 = "";
 //BA.debugLineNum = 39;BA.debugLine="Private pm12 As String";
mostCurrent._pm12 = "";
 //BA.debugLineNum = 40;BA.debugLine="Private pm1 As String";
mostCurrent._pm1 = "";
 //BA.debugLineNum = 41;BA.debugLine="Private pm2 As String";
mostCurrent._pm2 = "";
 //BA.debugLineNum = 42;BA.debugLine="Private pm3 As String";
mostCurrent._pm3 = "";
 //BA.debugLineNum = 43;BA.debugLine="Private pm4 As String";
mostCurrent._pm4 = "";
 //BA.debugLineNum = 44;BA.debugLine="Private pm5 As String";
mostCurrent._pm5 = "";
 //BA.debugLineNum = 45;BA.debugLine="Private pm6 As String";
mostCurrent._pm6 = "";
 //BA.debugLineNum = 46;BA.debugLine="Private pm7 As String";
mostCurrent._pm7 = "";
 //BA.debugLineNum = 47;BA.debugLine="Private pm8 As String";
mostCurrent._pm8 = "";
 //BA.debugLineNum = 48;BA.debugLine="Private pm9 As String";
mostCurrent._pm9 = "";
 //BA.debugLineNum = 49;BA.debugLine="Private pm10 As String";
mostCurrent._pm10 = "";
 //BA.debugLineNum = 50;BA.debugLine="Private pm11 As String";
mostCurrent._pm11 = "";
 //BA.debugLineNum = 51;BA.debugLine="Private tempRightNow As String";
mostCurrent._temprightnow = "";
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="Dim Timer1 As Timer";
_timer1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _readtextreader(String _fileday) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader1 = null;
long _now = 0L;
int _month = 0;
int _day = 0;
int _year = 0;
String _filename = "";
String _line = "";
String[] _a = null;
String _timestamp = "";
 //BA.debugLineNum = 339;BA.debugLine="Sub ReadTextReader(fileDay As String)";
 //BA.debugLineNum = 340;BA.debugLine="Try";
try { //BA.debugLineNum = 341;BA.debugLine="Dim TextReader1 As TextReader";
_textreader1 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 342;BA.debugLine="Dim Now As Long";
_now = 0L;
 //BA.debugLineNum = 343;BA.debugLine="Dim Month As Int";
_month = 0;
 //BA.debugLineNum = 344;BA.debugLine="Dim Day As Int";
_day = 0;
 //BA.debugLineNum = 345;BA.debugLine="Dim Year As Int";
_year = 0;
 //BA.debugLineNum = 346;BA.debugLine="Dim FileName As String";
_filename = "";
 //BA.debugLineNum = 348;BA.debugLine="am12 = \"0\"";
mostCurrent._am12 = "0";
 //BA.debugLineNum = 349;BA.debugLine="am1 = \"0\"";
mostCurrent._am1 = "0";
 //BA.debugLineNum = 350;BA.debugLine="am2 = \"0\"";
mostCurrent._am2 = "0";
 //BA.debugLineNum = 351;BA.debugLine="am3 = \"0\"";
mostCurrent._am3 = "0";
 //BA.debugLineNum = 352;BA.debugLine="am4 = \"0\"";
mostCurrent._am4 = "0";
 //BA.debugLineNum = 353;BA.debugLine="am5 = \"0\"";
mostCurrent._am5 = "0";
 //BA.debugLineNum = 354;BA.debugLine="am6 = \"0\"";
mostCurrent._am6 = "0";
 //BA.debugLineNum = 355;BA.debugLine="am7 = \"0\"";
mostCurrent._am7 = "0";
 //BA.debugLineNum = 356;BA.debugLine="am8 = \"0\"";
mostCurrent._am8 = "0";
 //BA.debugLineNum = 357;BA.debugLine="am9 = \"0\"";
mostCurrent._am9 = "0";
 //BA.debugLineNum = 358;BA.debugLine="am10 = \"0\"";
mostCurrent._am10 = "0";
 //BA.debugLineNum = 359;BA.debugLine="am11 = \"0\"";
mostCurrent._am11 = "0";
 //BA.debugLineNum = 360;BA.debugLine="pm12 = \"0\"";
mostCurrent._pm12 = "0";
 //BA.debugLineNum = 361;BA.debugLine="pm1 = \"0\"";
mostCurrent._pm1 = "0";
 //BA.debugLineNum = 362;BA.debugLine="pm2 = \"0\"";
mostCurrent._pm2 = "0";
 //BA.debugLineNum = 363;BA.debugLine="pm3 = \"0\"";
mostCurrent._pm3 = "0";
 //BA.debugLineNum = 364;BA.debugLine="pm4 = \"0\"";
mostCurrent._pm4 = "0";
 //BA.debugLineNum = 365;BA.debugLine="pm5 = \"0\"";
mostCurrent._pm5 = "0";
 //BA.debugLineNum = 366;BA.debugLine="pm6 = \"0\"";
mostCurrent._pm6 = "0";
 //BA.debugLineNum = 367;BA.debugLine="pm7 = \"0\"";
mostCurrent._pm7 = "0";
 //BA.debugLineNum = 368;BA.debugLine="pm8 = \"0\"";
mostCurrent._pm8 = "0";
 //BA.debugLineNum = 369;BA.debugLine="pm9 = \"0\"";
mostCurrent._pm9 = "0";
 //BA.debugLineNum = 370;BA.debugLine="pm10 = \"0\"";
mostCurrent._pm10 = "0";
 //BA.debugLineNum = 371;BA.debugLine="pm11 = \"0\"";
mostCurrent._pm11 = "0";
 //BA.debugLineNum = 373;BA.debugLine="Now = DateTime.Now";
_now = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 374;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 375;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 376;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 378;BA.debugLine="If fileDay = \"Today\" Then";
if ((_fileday).equals("Today")) { 
 //BA.debugLineNum = 379;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\"";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 }else {
 //BA.debugLineNum = 381;BA.debugLine="Now = DateTime.add(DateTime.Now, 0, 0, -1)";
_now = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.getNow(),(int) (0),(int) (0),(int) (-1));
 //BA.debugLineNum = 382;BA.debugLine="Month = DateTime.GetMonth(Now)";
_month = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_now);
 //BA.debugLineNum = 383;BA.debugLine="Day = DateTime.GetDayOfMonth (Now)";
_day = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(_now);
 //BA.debugLineNum = 384;BA.debugLine="Year = DateTime.GetYear(Now)";
_year = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_now);
 //BA.debugLineNum = 385;BA.debugLine="FileName = \"LivingRoomTempHumid_\" & Year & \"-\"";
_filename = "LivingRoomTempHumid_"+BA.NumberToString(_year)+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_month,(int) (2),(int) (0))+"-"+anywheresoftware.b4a.keywords.Common.NumberFormat(_day,(int) (2),(int) (0))+".log";
 };
 //BA.debugLineNum = 388;BA.debugLine="TextReader1.Initialize(File.OpenInput(File.DirRo";
_textreader1.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_filename).getObject()));
 //BA.debugLineNum = 389;BA.debugLine="Dim line As String";
_line = "";
 //BA.debugLineNum = 390;BA.debugLine="line = TextReader1.ReadLine";
_line = _textreader1.ReadLine();
 //BA.debugLineNum = 391;BA.debugLine="Do While line <> Null";
while (_line!= null) {
 //BA.debugLineNum = 392;BA.debugLine="Log(line) 'write the line to LogCat";
anywheresoftware.b4a.keywords.Common.Log(_line);
 //BA.debugLineNum = 393;BA.debugLine="line = TextReader1.ReadLine";
_line = _textreader1.ReadLine();
 //BA.debugLineNum = 394;BA.debugLine="If line = Null Then";
if (_line== null) { 
 //BA.debugLineNum = 395;BA.debugLine="Exit";
if (true) break;
 };
 //BA.debugLineNum = 397;BA.debugLine="Dim a() As String = Regex.Split(\"\\|\",line)";
_a = anywheresoftware.b4a.keywords.Common.Regex.Split("\\|",_line);
 //BA.debugLineNum = 398;BA.debugLine="If a.Length = 9 Then";
if (_a.length==9) { 
 //BA.debugLineNum = 399;BA.debugLine="Dim timeStamp As String";
_timestamp = "";
 //BA.debugLineNum = 400;BA.debugLine="timeStamp = a(0).SubString2(0,2)";
_timestamp = _a[(int) (0)].substring((int) (0),(int) (2));
 //BA.debugLineNum = 402;BA.debugLine="Select timeStamp";
switch (BA.switchObjectToInt(_timestamp,"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23")) {
case 0: {
 //BA.debugLineNum = 404;BA.debugLine="If am12 = \"0\" Or am12 = \"\" Then am12 = a(1)";
if ((mostCurrent._am12).equals("0") || (mostCurrent._am12).equals("")) { 
mostCurrent._am12 = _a[(int) (1)];};
 break; }
case 1: {
 //BA.debugLineNum = 406;BA.debugLine="If am1 = \"0\" Or am1 = \"\" Then am1 = a(1)";
if ((mostCurrent._am1).equals("0") || (mostCurrent._am1).equals("")) { 
mostCurrent._am1 = _a[(int) (1)];};
 break; }
case 2: {
 //BA.debugLineNum = 408;BA.debugLine="If am2 = \"0\" Or am2 = \"\" Then am2 = a(1)";
if ((mostCurrent._am2).equals("0") || (mostCurrent._am2).equals("")) { 
mostCurrent._am2 = _a[(int) (1)];};
 break; }
case 3: {
 //BA.debugLineNum = 410;BA.debugLine="If am3 = \"0\" Or am3 = \"\" Then am3 = a(1)";
if ((mostCurrent._am3).equals("0") || (mostCurrent._am3).equals("")) { 
mostCurrent._am3 = _a[(int) (1)];};
 break; }
case 4: {
 //BA.debugLineNum = 412;BA.debugLine="If am4 = \"0\" Or am4 = \"\" Then am4 = a(1)";
if ((mostCurrent._am4).equals("0") || (mostCurrent._am4).equals("")) { 
mostCurrent._am4 = _a[(int) (1)];};
 break; }
case 5: {
 //BA.debugLineNum = 414;BA.debugLine="If am5 = \"0\" Or am5 = \"\" Then am5 = a(1)";
if ((mostCurrent._am5).equals("0") || (mostCurrent._am5).equals("")) { 
mostCurrent._am5 = _a[(int) (1)];};
 break; }
case 6: {
 //BA.debugLineNum = 416;BA.debugLine="If am6 = \"0\" Or am6 = \"\" Then am6 = a(1)";
if ((mostCurrent._am6).equals("0") || (mostCurrent._am6).equals("")) { 
mostCurrent._am6 = _a[(int) (1)];};
 break; }
case 7: {
 //BA.debugLineNum = 418;BA.debugLine="If am7 = \"0\" Or am7 = \"\" Then am7 = a(1)";
if ((mostCurrent._am7).equals("0") || (mostCurrent._am7).equals("")) { 
mostCurrent._am7 = _a[(int) (1)];};
 break; }
case 8: {
 //BA.debugLineNum = 420;BA.debugLine="If am8 = \"0\" Or am8 = \"\" Then am8 = a(1)";
if ((mostCurrent._am8).equals("0") || (mostCurrent._am8).equals("")) { 
mostCurrent._am8 = _a[(int) (1)];};
 break; }
case 9: {
 //BA.debugLineNum = 422;BA.debugLine="If am9 = \"0\" Or am9 = \"\" Then am9 = a(1)";
if ((mostCurrent._am9).equals("0") || (mostCurrent._am9).equals("")) { 
mostCurrent._am9 = _a[(int) (1)];};
 break; }
case 10: {
 //BA.debugLineNum = 424;BA.debugLine="If am10 = \"0\" Or am10 = \"\" Then am10 = a(1)";
if ((mostCurrent._am10).equals("0") || (mostCurrent._am10).equals("")) { 
mostCurrent._am10 = _a[(int) (1)];};
 break; }
case 11: {
 //BA.debugLineNum = 426;BA.debugLine="If am11 = \"0\" Or am11 = \"\" Then am11 = a(1)";
if ((mostCurrent._am11).equals("0") || (mostCurrent._am11).equals("")) { 
mostCurrent._am11 = _a[(int) (1)];};
 break; }
case 12: {
 //BA.debugLineNum = 428;BA.debugLine="If pm12 = \"0\" Or pm12 = \"\" Then pm12 = a(1)";
if ((mostCurrent._pm12).equals("0") || (mostCurrent._pm12).equals("")) { 
mostCurrent._pm12 = _a[(int) (1)];};
 break; }
case 13: {
 //BA.debugLineNum = 430;BA.debugLine="If pm1 = \"0\" Or pm1 = \"\" Then pm1 = a(1)";
if ((mostCurrent._pm1).equals("0") || (mostCurrent._pm1).equals("")) { 
mostCurrent._pm1 = _a[(int) (1)];};
 break; }
case 14: {
 //BA.debugLineNum = 432;BA.debugLine="If pm2 = \"0\" Or pm2 = \"\" Then pm2 = a(1)";
if ((mostCurrent._pm2).equals("0") || (mostCurrent._pm2).equals("")) { 
mostCurrent._pm2 = _a[(int) (1)];};
 break; }
case 15: {
 //BA.debugLineNum = 434;BA.debugLine="If pm3 = \"0\" Or pm3 = \"\" Then pm3 = a(1)";
if ((mostCurrent._pm3).equals("0") || (mostCurrent._pm3).equals("")) { 
mostCurrent._pm3 = _a[(int) (1)];};
 break; }
case 16: {
 //BA.debugLineNum = 436;BA.debugLine="If pm4 = \"0\" Or pm4 = \"\" Then pm4 = a(1)";
if ((mostCurrent._pm4).equals("0") || (mostCurrent._pm4).equals("")) { 
mostCurrent._pm4 = _a[(int) (1)];};
 break; }
case 17: {
 //BA.debugLineNum = 438;BA.debugLine="If pm5 = \"0\" Or pm5 = \"\" Then pm5 = a(1)";
if ((mostCurrent._pm5).equals("0") || (mostCurrent._pm5).equals("")) { 
mostCurrent._pm5 = _a[(int) (1)];};
 break; }
case 18: {
 //BA.debugLineNum = 440;BA.debugLine="If pm6 = \"0\" Or pm6 = \"\" Then pm6 = a(1)";
if ((mostCurrent._pm6).equals("0") || (mostCurrent._pm6).equals("")) { 
mostCurrent._pm6 = _a[(int) (1)];};
 break; }
case 19: {
 //BA.debugLineNum = 442;BA.debugLine="If pm7 = \"0\" Or pm7 = \"\" Then pm7 = a(1)";
if ((mostCurrent._pm7).equals("0") || (mostCurrent._pm7).equals("")) { 
mostCurrent._pm7 = _a[(int) (1)];};
 break; }
case 20: {
 //BA.debugLineNum = 444;BA.debugLine="If pm8 = \"0\" Or pm8 = \"\" Then pm8 = a(1)";
if ((mostCurrent._pm8).equals("0") || (mostCurrent._pm8).equals("")) { 
mostCurrent._pm8 = _a[(int) (1)];};
 break; }
case 21: {
 //BA.debugLineNum = 446;BA.debugLine="If pm9 = \"0\" Or pm9 = \"\" Then pm9 = a(1)";
if ((mostCurrent._pm9).equals("0") || (mostCurrent._pm9).equals("")) { 
mostCurrent._pm9 = _a[(int) (1)];};
 break; }
case 22: {
 //BA.debugLineNum = 448;BA.debugLine="If pm10 = \"0\" Or pm10 = \"\" Then pm10 = a(1)";
if ((mostCurrent._pm10).equals("0") || (mostCurrent._pm10).equals("")) { 
mostCurrent._pm10 = _a[(int) (1)];};
 break; }
case 23: {
 //BA.debugLineNum = 450;BA.debugLine="If pm11 = \"0\" Or pm11 = \"\" Then pm11 = a(1)";
if ((mostCurrent._pm11).equals("0") || (mostCurrent._pm11).equals("")) { 
mostCurrent._pm11 = _a[(int) (1)];};
 break; }
}
;
 //BA.debugLineNum = 452;BA.debugLine="If fileDay = \"Today\" Then";
if ((_fileday).equals("Today")) { 
 //BA.debugLineNum = 453;BA.debugLine="tempRightNow = a(1)";
mostCurrent._temprightnow = _a[(int) (1)];
 };
 };
 }
;
 //BA.debugLineNum = 458;BA.debugLine="TextReader1.Close";
_textreader1.Close();
 } 
       catch (Exception e115) {
			processBA.setLastException(e115); //BA.debugLineNum = 460;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 462;BA.debugLine="End Sub";
return "";
}
public static String  _timer1_tick() throws Exception{
 //BA.debugLineNum = 497;BA.debugLine="Sub Timer1_Tick";
 //BA.debugLineNum = 498;BA.debugLine="Log(\"Timer tick\")";
anywheresoftware.b4a.keywords.Common.Log("Timer tick");
 //BA.debugLineNum = 499;BA.debugLine="lc1.RemoveView";
mostCurrent._lc1.RemoveView();
 //BA.debugLineNum = 500;BA.debugLine="Activity_Create(False)";
_activity_create(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 501;BA.debugLine="End Sub";
return "";
}
}
