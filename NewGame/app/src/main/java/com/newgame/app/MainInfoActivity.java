package com.newgame.app;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newgame.R;
import com.newgame.commons.CommonUtils;
import com.newgame.database.DatabaseHelper;
import com.newgame.database.Queries;
import com.newgame.models.AgentInfoResult;
import com.newgame.models.GameJsonDataResult;
import com.newgame.network.ApplicationThread;
import com.newgame.network.Config;
import com.newgame.network.GamesLiveData;
import com.newgame.ui.EachTransFragment;
import com.newgame.ui.MainFragment;
import com.newgame.uihelper.BaseActivity;
import com.newgame.uihelper.SlidingMenu;

public class MainInfoActivity extends BaseActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	private SlidingMenu sampleMenu;
	private ProgressDialog progressDialog;
    private String[] arrAgents;
    private List<AgentInfoResult> agentsList;
    public static AgentInfoResult agentInfo;
    private MainFragment mMainFragment = null;
	public MainInfoActivity() {
		super(R.string.title_activity_main_info);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	public static String game_id = "";

	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_info);
		
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		mTitle = getTitle();
		
		getAgentsDetails();

		sampleMenu = getSlidingMenu();
	        sampleMenu.setSlidingEnabled(false);
	        getSlidingMenu().setMode(SlidingMenu.RIGHT);
	        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	        getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
	        getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);

	        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.menu_frame_two, new EachTransFragment(CommonUtils.selectedGame, CommonUtils.colorCode, (null != agentInfo) ? agentInfo.getAgentID() : "", game_id))
            .commit();
	        if( Build.VERSION.SDK_INT >= 9){
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	        }
	        
	        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("update"));
	}

	
	@Override
	public void onNavigationDrawerItemSelected(final int position) {
		
		
		
		// update the main content by replacing fragments
		
		Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() { 
	         public void run() { 
	        	if (null != agentsList && !agentsList.isEmpty()) {
	        		mTitle = agentsList.get(position).getAgentName();
		     		agentInfo = agentsList.get(position);
		     		restoreActionBar();
		     		String[] arrGames = null;
//		     		actionBar.setTitle(mTitle);
		     		if (getIntent().getStringExtra("GAMEID") != null) {
		     			 game_id = getIntent().getStringExtra("GAMEID");
					}
		     		if (getIntent().getStringArrayExtra("gamesArr") != null) {
		     			arrGames = getIntent().getStringArrayExtra("gamesArr");
					}
		     		
		     		mMainFragment = MainFragment.newInstance(position,agentInfo.getAgentID(),mTitle.toString(),game_id,(GameJsonDataResult)getIntent().getSerializableExtra("GAMEOBJ"), arrGames);
		     		FragmentManager fragmentManager = getSupportFragmentManager();
		     		fragmentManager
		     				.beginTransaction()
		     				.replace(R.id.container,
		     						mMainFragment).commit();
				} 
	         }
	    }, 500);
		
	}

	public void onSectionAttached(int number) {
		
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main_info, menu);
//			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		 if (id == R.id.action_filter) {
	           sampleMenu.toggle();
	        }
		 if (id == R.id.logout) {
	           Log.v(MainInfoActivity.class.getSimpleName(), "#### log out clicked");
	           logoutAlert();
	        }
		return super.onOptionsItemSelected(item);
	}
	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals("update") && !isFinishing()) {
                	 getSupportFragmentManager()
   	                .beginTransaction()
   	                .replace(R.id.menu_frame_two, new EachTransFragment(CommonUtils.selectedGame,CommonUtils.colorCode, (null != agentInfo) ? agentInfo.getAgentID() : "", game_id))
   	                .commitAllowingStateLoss();

                }
            }
        }
    };
    
    public void getAgentsDetails(){
		if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	    
	    String numIMEI = telephonyManager.getDeviceId();
//	    numIMEI = "351558071527586";
		 GamesLiveData.getGameDetails(new ApplicationThread.OnComplete<String>(ApplicationThread.OnComplete.UI) {
	        
				@Override
	            public void run() {
	                if (!success || null == result) {
	                	progressDialog.dismiss();
	                    return;
	                }
	                try {
	                	 Log.i("Response MEssage...", result.toString());
	                	 Type collectionType = new TypeToken<Collection<AgentInfoResult>>(){}.getType();
	                	 Gson googleJson = new Gson();
	                	  agentsList = googleJson.fromJson((String) result,collectionType);
	                	  Log.i("", "Agent assign.."+agentsList);
	                	
                         arrAgents = new String[agentsList.size()];
                         for (int i = 0; i < agentsList.size(); i++) {
                        	 arrAgents[i] = agentsList.get(i).getAgentName();
						}
                       
                     	// Set up the drawer.
                 		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                 				(DrawerLayout) findViewById(R.id.drawer_layout), arrAgents);
                 		
                 		
                         progressDialog.dismiss();
                         progressDialog = null;
	                }
	                catch (Exception e){
	                    e.printStackTrace();
	                    progressDialog.dismiss();
	                }

	            }
	        }, String.format(Config.live_url+Config.AgentInfo_URL, numIMEI));
	}
    
    @Override
	  public void onDestroy(){
	        super.onDestroy();
	        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	 }
    
    @Override
    public void onBackPressed() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        if (null != mMainFragment && !TextUtils.isEmpty(mMainFragment.transId)) {
        	 Cursor checkCount = dbHelper.getdata(Queries.getInstance().getRecordDetails("EachEntryDetails", mMainFragment.transId, "pending", (null != agentInfo) ? agentInfo.getAgentID() : "", game_id));
         	if (null != mMainFragment && null != checkCount && checkCount.getCount() > 0) {
         		backPressedAlert();
     		} else {
     			super.onBackPressed();
     		}
		} else {
 			super.onBackPressed();
 		}
       
    }
    
    public void backPressedAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please confirm your transactions before leaving the app.");
        
        alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface arg0, int arg1) {
        	   mMainFragment.uploadData();
           }
        });
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
     }
    
    
    public void logoutAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
       
//        alertDialog.setCancelable(false);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage("Do you want to logout from application ?");
        
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface arg0, int arg1) {
        	   mMainFragment.uploadData();
//        	   alertDialog.dismiss();
        	  
        	   SharedPreferences prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        	   prefs.edit().putBoolean("isLogin", false).commit();
        	   finishAffinity();
        	   System.exit(0);
           }
        });
        
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
//            	.dismiss();
            }
         });
        
         AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
     }
}
