package com.newgame.commons;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class DateFormats {

    public static final ThreadLocal<SimpleDateFormat> dateFormatLocal = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() { return new SimpleDateFormat("yyyyMMddHHmm", Locale.US); }
    };
    public static final ThreadLocal<SimpleDateFormat> dateFormatLocalRecommended = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() { return new SimpleDateFormat("yyyy-MM-dd HH", Locale.US); }
    };
    public static final ThreadLocal<SimpleDateFormat> dateFormatYMDHMS = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() { return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US); }
    };
    public static final ThreadLocal<SimpleDateFormat> dateFormatYMDHMS_GMT = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            final SimpleDateFormat gmtdateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            gmtdateformat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return gmtdateformat;
        }
    };
    public static final ThreadLocal<SimpleDateFormat> dateFormatYMDHMS_UTC = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue () {
            final SimpleDateFormat utcdateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss" +
                    ".sszzz", Locale.US);
            utcdateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return utcdateformat;
        }
    };
    public static final ThreadLocal<SimpleDateFormat> dateFormatYMDHM = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() { return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US); }
    };
    public static final ThreadLocal<SimpleDateFormat> hourMinuteAmPmFormatter = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("h:mm a", Locale.US);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> hourMinute24HourFormatter = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() { return new SimpleDateFormat("H:mm", Locale.US); }
    };
    public static final ThreadLocal<SimpleDateFormat> hourMinuteFormatter = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() { return new SimpleDateFormat("hh:mm", Locale.US); }
    };
    public static final ThreadLocal<SimpleDateFormat> longDays = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() { return new SimpleDateFormat("EEEE", Locale.US); }
    };
    public static final ThreadLocal<SimpleDateFormat> GMTDateFormat = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            final SimpleDateFormat gmtdateformat = new SimpleDateFormat("yyyyMMddHHmm00", Locale.US);
            gmtdateformat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return gmtdateformat;
        }
    };

    public static final ThreadLocal<SimpleDateFormat> fullMonthDayFormat = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() { return new SimpleDateFormat("MMMMM d", Locale.US); }
    };
    public static final SimpleDateFormat localTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static final int YESTERDAY = 0;
    public static final int TODAY = 1;
    public static final int TOMORROW = 2;
    private static final String LOG_TAG = DateFormats.class.getName();
  
    private static ThreadLocal<Locale> currentLocale = new ThreadLocal<Locale>() {
        protected Locale initialValue() { return Locale.US; }
    };

   

    // move recommended time back/forward [minutes] minutes, usually -30 for
    // back and 30 for forward
    public static String getAdjustedRecommendedTime(String recommended, int minutes) {
        try {
            final java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
            cal.setTime(dateFormatYMDHMS.get().parse(recommended));
            cal.add(Calendar.MINUTE, minutes);
            return dateFormatYMDHMS.get().format(cal.getTime());
        } catch (Exception e) {
           Log.e(LOG_TAG, LOG_TAG, e);
        }
        return recommended; // TODO: should return null?
    }

    public static String getTimeIn12HourFormat(String date, String pattern) {
        try {
            if (!currentLocale.get().getDisplayLanguage().equals(Locale.getDefault().getDisplayLanguage())) {
                currentLocale.set(Locale.getDefault());
                hourMinuteAmPmFormatter.set(new SimpleDateFormat(pattern, Locale.getDefault()));
            }
            return hourMinuteAmPmFormatter.get().format(dateFormatYMDHMS.get().parse(date));
        } catch (Exception e) {
           Log.e(LOG_TAG, LOG_TAG, e);
            return null;
        }
    }

    public static String getTimeIn12HourTimeFormat(String date, String pattern) {
        try {
            if (!currentLocale.get().getDisplayLanguage().equals(Locale.getDefault().getDisplayLanguage())) {
                currentLocale.set(Locale.getDefault());
                hourMinuteAmPmFormatter.set(new SimpleDateFormat(pattern, Locale.getDefault()));
            }

            return hourMinuteAmPmFormatter.get().format(hourMinuteFormatter.get().parse(date));
        } catch (Exception e) {
           Log.e(LOG_TAG, LOG_TAG, e);
            return null;
        }
    }

 
    // 2010-11-19 11:00:00
    // 2010-11-19 11:30:00
    // datetime string rounded to nearest half hour
    public static String getCurrentRecommendedDatetime() {
        final java.util.GregorianCalendar cal = new java.util.GregorianCalendar();

        final String dt = dateFormatLocalRecommended.get().format(cal.getTime());

        if (cal.get(Calendar.MINUTE) < 30) {
            return dt + ":00:00";
        } else {
            return dt + ":30:00";
        }
    }

    public static String getStartOfTheDay() {
        final java.util.GregorianCalendar now = new java.util.GregorianCalendar();
        java.util.GregorianCalendar today = new java.util.GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        return dateFormatYMDHMS.get().format(today.getTime());
    }

    public static String getNextHalfHourDatetime(final String datetime) {
        final java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
        try {
            cal.setTime(dateFormatYMDHMS.get().parse(datetime));
            cal.add(Calendar.MINUTE, 30);
            return dateFormatYMDHMS.get().format(cal.getTime());
        } catch (Exception e) {
           Log.e(LOG_TAG, LOG_TAG, e);
        }
        return datetime;
    }

    /*
     * @author Hardik
     *
     * @company sourcebits description : it return remaining time[In
     * millisecond] for refresh screen for example current time is 10:20 then
     * after 10 minutes means 10:30 it will refresh
     */
    public static int getRemainingTimeTorefreshScreen() {

        java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
        int time = cal.get(Calendar.MINUTE);
        if (time < 30) {
            return ((30 - time) * 60000);
        } else {
            return ((60 - time) * 60000);
        }

    }

    // return current hour w/ :00:00
    public static String getCurrentProgramStartDatetime() {
        java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
        // return String.get().format("%s:00:00",
        // dateFormatLocalRecommended.get().format(cal.getTime()));
        String dt = dateFormatLocalRecommended.get().format(cal.getTime());
        if (cal.get(Calendar.MINUTE) < 30) {
            return String.format("%s:00:00", dt);
        } else {
            return String.format("%s:30:00", dt);
        }
    }

    public static long convertRecommendedTimeToMilliseconds(String date) {
        try {
            return dateFormatYMDHM.get().parse(date).getTime();
        } catch (ParseException e) {
           Log.e(LOG_TAG, LOG_TAG, e);
        }

        return 0;
    }

    public static String getCurrentRecommendedDatetimePrefix() {
        java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
        return dateFormatYMDHMS_GMT.get().format(cal.getTime());
    }
    
    
    
    public static String getCurrentDatetimePrefix() {
    	long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
		                                "dd/MM/yyyy hh:mm:ss a");
		String dateforrow = dateFormat.format(cal1.getTime());
        return dateforrow;
    }

    // convert 201102211330 to 201102212130
    public static String convertLocalToGMT(String local) {
        try {
            return GMTDateFormat.get().format(dateFormatLocal.get().parse(local));
        } catch (Exception e) {
           Log.e(LOG_TAG, LOG_TAG, e);
        }
        return local;
    }



    public static Date convertGMTToLocalDate(final String gmt) {
        try {
            return dateFormatYMDHMS_GMT.get().parse(gmt);
        } catch (Exception e) {
           Log.e(LOG_TAG, LOG_TAG, e);
        }
        return null;
    }

    public static Date convertUTCToLocalDate(final String utc) {
        try {
            return dateFormatYMDHMS_UTC.get().parse(utc);
        } catch (Exception e) {
           Log.e(LOG_TAG, LOG_TAG, e);
        }
        return null;
    }

    public static String getStringForUTCByLong(long start) {
        try {
            return dateFormatYMDHMS_UTC.get().format(new Date(start));
        } catch (Exception e) {
           Log.e(LOG_TAG, LOG_TAG, e);
        }

        return null;
    }

    public static String convertUTCToGMT(String utc) {
        try {
            return dateFormatYMDHMS_GMT.get().format(dateFormatYMDHMS_UTC.get().parse(utc));
        } catch (Exception e) {
           Log.e(LOG_TAG, LOG_TAG, e);
        }

        return null;
    }

    // start = 201011240800
    public static String[] getHourlyStartValueByStartAndWindow(String start, int window) {
        if (window == 1) {
            return new String[] {start};
        } else {
            long startValue = Long.parseLong(start);
            long hr = (startValue + 100 * (window - 1)) % 10000;
            // if start + window is still within the same date, meaning no date
            // change
            if (hr < 2400) {
                String[] starts = new String[window];
                // starts[0] is always the start passed-in
                starts[0] = start;
                for (int i = 1; i < window; i++) {
                    startValue += 100; // increment by 100, 1 hr
                    starts[i] = "" + startValue;
                }
                return starts;
            } else {
                Calendar c = java.util.GregorianCalendar.getInstance();
                try {
                    c.setTime(dateFormatLocal.get().parse(start));
                    String[] starts = new String[window];
                    // starts[0] is always the start passed-in
                    starts[0] = start;
                    for (int i = 1; i < window; i++) {
                        c.add(Calendar.HOUR_OF_DAY, 1);
                        starts[i] = dateFormatLocal.get().format(c.getTime());
                    }
                    return starts;
                } catch (Exception e) {
                   Log.e(LOG_TAG, LOG_TAG, e);
                }
                return null; // something's wrong with start time, window, etc.
                // TODO: need handle this case?
            }
        }
    }
  

    /**
     * @return true if time2 is later than time1
     */
    public static boolean compareDateStrings(String time1, String time2) {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(time1);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(time2);
            long millisecondsEarlier = date1.getTime();
            long millisecondsLater = date2.getTime();
            return millisecondsEarlier < millisecondsLater;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
