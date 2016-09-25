package com.example.faceall.facealldetect;

import java.util.Calendar;
import java.util.TimeZone;
import android.text.format.DateFormat;

public class TimeString {
	private static String mYear;  
    private static String mMonth;  
    private static String mDay;  
    private static String mWay; 
    private static CharSequence mTime;
    
    public static String StringData(){  
        final Calendar c = Calendar.getInstance();  
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份  
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份  
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码  
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));  
        if("1".equals(mWay)){  
            mWay ="天";  
        }else if("2".equals(mWay)){  
            mWay ="一";  
        }else if("3".equals(mWay)){  
            mWay ="二";  
        }else if("4".equals(mWay)){  
            mWay ="三";  
        }else if("5".equals(mWay)){  
            mWay ="四";  
        }else if("6".equals(mWay)){  
            mWay ="五";  
        }else if("7".equals(mWay)){  
            mWay ="六";  
        }  
        long sysTime = System.currentTimeMillis();
        mTime = DateFormat.format("HH:mm:ss", sysTime);
        return mYear + "年" + mMonth + "月" + mDay+"日"+"/星期"+mWay+"  "+mTime;  
    }  
}
