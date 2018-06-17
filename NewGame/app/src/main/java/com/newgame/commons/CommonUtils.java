package com.newgame.commons;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.newgame.R;

/**
 * Class for using different android utils
 * @author jonnalagaddasiva
 *
 */
public class CommonUtils {
	 public static int colorCode, selectedBg = 0;
	 public static String selectedGame = "1";
	/**
	 * validating email pattern
	 */
	public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
	          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
	          "\\@" +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
	          "(" +
	          "\\." +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
	          ")+"
	      );
	
	/**
	 * Checking the given emailId is valid or not
	 * @param email --> user emailId
	 * @return ---> true if valid else false
	 */
	 public static boolean isValidEmail(String email) {
	        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	    }
	
	/**
	 * Checking the Internet connection is available or not
	 * @param context
	 * @return  true if available else false
	 */
	 public static boolean isNetworkAvailable(final Context context) {
	        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        if (null != connectivityManager) {
	            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	        }

	        return false;
	    }
	 
	 /**
	  * Deleting a particular directory from sdcard
	  * @param path ---> file path
	  * @return  ---> true is successfully deleted else false
	  */
	 public static boolean deleteDirectory(File path) {
	        if (path.exists()) {
	            File[] files = path.listFiles();
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) deleteDirectory(files[i]);
	                else files[i].delete();
	            }
	        }
	        return true;
	    }
	 
	 
	 public static void showToast(String message,Context context){
		 
		 Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
	 }
	 
	 /**
	  * Class for common alert dialog
	  * @author jonnalagaddasiva
	  *
	  */
	 public class AlertDialogMsg extends AlertDialog.Builder{
			
			Context context;
			public AlertDialogMsg(Context context, String title, String msg) { 
				super(context);
				this.setMessage(msg);
				this.setTitle(title);
				this.setCancelable(false);
		   }

		}
	 
	 
	 /**
	  * Checking SD card is available or not in mobile
	  * @param context
	  * @return --> true if available else false
	  */
	 public static boolean isSDcardAvailable(Context context){
			boolean isAvailable=false;
		    boolean available =  Environment.getExternalStorageState().toString().equalsIgnoreCase(Environment.MEDIA_MOUNTED)||Environment.getExternalStorageState().toString().equalsIgnoreCase(Environment.MEDIA_CHECKING)||
	 							 Environment.getExternalStorageState().toString().equalsIgnoreCase(Environment.MEDIA_MOUNTED_READ_ONLY);
		    if(!available){
				isAvailable=false;
			}
			else{
				isAvailable=true;
			}
			return isAvailable;
		}
	 
	 
	 /**
	  * Checking a string contains an integer or not
	  * @param s  --> Input string
	  * @return true is string contains number else false.
	  */
	 public static boolean isInteger(final String s) {
	        return Pattern.matches("^\\d*$", s);
	    }
	 
	 
	 /**
	  * Getting the device id
	  * @param context  --> Current context
	  * @return  --> Device IMEI number as string
	  */
	 public static String getDeviceId(Context context){
		 TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		 String strDeviceIMEI=telephonyManager.getDeviceId();
		 if (strDeviceIMEI != null) {
			return strDeviceIMEI;
		}else{
			return "";
		}
	 }
	 
	 /**
	  * Getting the device information(android version, Device model,Device manufacturer)
	  * @return  ---> Device information in a string formate
	  */
	 public static String getDeviceExtraInfo(){
		 return ""+Build.VERSION.RELEASE+"/n"+Build.MANUFACTURER + " - " + Build.MODEL+"/n";
	 }
	 
	 /**
	  * Getting the current date and time with comma separated.
	  * @param dateFormate  ---> Input date format
	  * @param timeFormate  ---> Input time format
	  * @return  if both are required returns date and time with comma separated else if only time required returns time else only date else default date and time.
	  */
	 public static String getDateTime(String dateFormate, String timeFormate) {
			SimpleDateFormat formatterDate = null;
			SimpleDateFormat	formatterTime = null;
			SimpleDateFormat _currentFormate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
			GregorianCalendar _currentCalendar = new GregorianCalendar();
			Date _currentDate = _currentCalendar.getTime();
			
			try{
				if ((dateFormate != null && dateFormate.length() > 0)  && (timeFormate != null && timeFormate.length() > 0)) {
					formatterDate = new SimpleDateFormat(dateFormate);
					formatterTime = new SimpleDateFormat(timeFormate);
					return ""+formatterDate.format(_currentDate)+","+formatterTime.format(_currentDate);
				}else if((dateFormate != null && dateFormate.length() > 0) && (timeFormate == null || timeFormate.equalsIgnoreCase(""))){
					formatterDate = new SimpleDateFormat(dateFormate);
					return ""+formatterDate.format(_currentDate);
				}else if((dateFormate == null || dateFormate.equalsIgnoreCase("")) && (timeFormate != null && !timeFormate.equalsIgnoreCase(""))){
					formatterTime = new SimpleDateFormat(timeFormate);
					return formatterTime.format(_currentDate);
				}else{
					formatterDate = new SimpleDateFormat("yyyy-MM-dd");
					formatterTime = new SimpleDateFormat("HH:mm:ss");
					return ""+formatterDate.format(_currentDate)+","+formatterTime.format(_currentDate);
				}
			}catch(Exception ex){
				ex.printStackTrace();
				return ""+_currentDate;
			}
	}
	 
	 
	 /**
	  * Converting given string into date format
	  * @param dateStr  ---> Given date
	  * @param _dateFormatteStr   ----> Given date format
	  * @return  ----> converted date   (if any exception occurred returns null)
	  */
	 public Date convertStringToDate(String dateStr, String _dateFormatteStr){
		 Date _convertedDate = null;
		 SimpleDateFormat _dateFormate = new SimpleDateFormat(""+_dateFormatteStr);
		 try {
			_convertedDate = _dateFormate.parse(_dateFormatteStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return _convertedDate;
	 }
	 
	 /**
	  * Converting given date into required format 
	  * @param _date --> Date to convert
	  * @param requiredFormat   ----> Format required
	  * @return  final date string
	  */
	 public String convertDateToString(Date _date, String requiredFormat){
		 String _finalDateStr = "";
		 SimpleDateFormat _dateFormatter = new SimpleDateFormat(requiredFormat);
		 try{
			 _finalDateStr = _dateFormatter.format(_date);
		 }catch(Exception ex){
			 ex.printStackTrace();
		 }
		 return _finalDateStr;
	 }


    public static String getCopyright(final Context context) {
        String version;
        Calendar c = Calendar.getInstance();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            StringBuilder sb = new StringBuilder();
            sb.append("Version ").append(packageInfo.versionName)
                    // .append("\n(Build ").append(Config.BUILD_ID).append(")\n")
                    .append(String.format("\nCopyright " + Html.fromHtml("&copy;") + "2010-" + c.get(Calendar.YEAR) + ",\n Pristine Embed.\nAll Rights Reserved."));
            version = sb.toString();
        } catch (PackageManager.NameNotFoundException e) {
            version = Html.fromHtml("&copy;") + "2010" + c.get(Calendar.YEAR) + "\nPristine Embed.\nAll Rights Reserved.";
        }
        return version;
    }

    public static String[] getGenericArrayValues(String type, int size){
        String[] items = new String[size];
        for (int i = 0; i <= size -1; i++){
            if(type.length() == 0){
                if(i == size -1)
                    items[i]=""+i+"+";
                else
                    items[i]=""+i;
            }else{
                items[i]=""+(i+1980);
            }
        }
        return items;
    }

    public static int getImageOrientation(String imagePath){
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }
    
    
    public static int giveColorCode(int gameType, Context context){
        int colorCode = 0;
        switch (gameType) {
                case 1:
                	colorCode = context.getResources().getColor(R.color.open);
                    break;
                case 2:
                	colorCode = context.getResources().getColor(R.color.close);
                    break;
                case 3:
                	colorCode = context.getResources().getColor(R.color.bracket);
                    break;
                case 4:
                	colorCode = context.getResources().getColor(R.color.general);
                    break;
                case 5:
                	colorCode = context.getResources().getColor(R.color.cpwana);
                    break;
            }
       
        return colorCode;
    }

    public static int getIndex(Set<? extends Object> set, Object value) {
    	   int result = 0;
    	   for (Object entry:set) {
    	     if (entry.equals(value)) return result;
    	     result++;
    	   }
    	   return -1;
    }
    
    
    public static String getProperInt(String inputStr){
    	String delimiter = "\\.";
    	if(null != inputStr && !inputStr.equalsIgnoreCase("null")){
    	if (inputStr.contains(".")) {
    		String[] outputStr = null;
        	if (inputStr.length() > 0) {
        		outputStr = inputStr.split(delimiter);
    		}
        	return (null != outputStr && outputStr.length > 0) ? outputStr[0] : "0";
		}else{
			return inputStr;
		}
    	}else{
			return "0";
		}
    }

    public static void showKeyboard(Context context, View textView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.showSoftInput(textView, 0);
        }
    }

    public static void hideKeyboard(Context context, View textView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        }
    }
    
    public static String dateString(String dateToCheck) {
    	String[] dateSplitarr =dateToCheck.split("T");
		String dateStr = "";
		String tempTimeStr = "", timeStr = "";
		
		if (dateSplitarr != null && dateSplitarr.length > 1) {
			dateStr = dateSplitarr[0];
			tempTimeStr = dateSplitarr[1];
		} else {
			dateStr = dateToCheck;
		}
		
		if (tempTimeStr.contains(".") && tempTimeStr.split(".").length > 1) {
			timeStr = tempTimeStr.split(".")[0];
		} else {
			timeStr = tempTimeStr;
		}
		
		return dateStr + " " + timeStr;
    }
    
    public static String getTimeFormattedStr(String inputStr) {
    	String result = "", am_pm;
    	int lastIndex = inputStr.lastIndexOf(":");
		result = inputStr.substring(0, lastIndex);
		
		String[] timeArr = inputStr.split(":");
		String firstTime = "", secondTimeStr = "";
		if (null != timeArr && timeArr.length > 0) {
			firstTime  = timeArr[0];
			secondTimeStr = timeArr[1];
		}
		
		int timeInt = Integer.parseInt(firstTime);
		
		if (timeInt > 12)         //hourofDay =13
		{
			timeInt = timeInt - 12;     //hour=1
		    am_pm = "PM";                   //PM
		} 
		else 
		{
			timeInt = timeInt;
			am_pm = "AM";
		}
    	
    	return timeInt + ":" + secondTimeStr + " "+am_pm;
    }
    
}
