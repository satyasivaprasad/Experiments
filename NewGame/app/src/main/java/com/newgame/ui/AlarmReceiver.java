package com.newgame.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

 @Override
 public void onReceive(Context arg0, Intent arg1) {
	 
	 
	 if (arg1.getStringExtra("REPEAT") != null) {
//		 Toast.makeText(arg0, "Repeat Alarm received!", Toast.LENGTH_SHORT).show();
			  Log.i("", "Repeat Alarm msg");
		
	}else {
//		Toast.makeText(arg0, "Alarm received!", Toast.LENGTH_SHORT).show();
		FinalAlert(arg0,arg1.getIntExtra("alarm_no", 0));
		  Log.i("", "Alarm msg");
	}

 }
 
 public void FinalAlert(Context context , int alarm_no) {
	  Intent intent = new Intent(context, AlarmReceiver.class);
	  intent.putExtra("REPEAT", "REPEAT");
	  PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 12353, intent, 0);
	  AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	  alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ (10 * 1000), pendingIntent);
//	  Toast.makeText(context, "Repeat alarm in " + 10 + " seconds",
//	    Toast.LENGTH_SHORT).show();
	 }

}
