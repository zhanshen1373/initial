package com.example.faceall.facealldetect.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作用：缓存工具类
 */
public class CacheUtils {
    /**
     * 保持软件的参数
     * @param context
     * @param key
     * @param values
     */
    public static void putBoolen(Context context, String key, boolean values) {
        SharedPreferences sp = context.getSharedPreferences("feisou",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,values).commit();
    }

    /**
     * 得到软件保存的参数
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("feisou",Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 得到缓存的数据
     * @param context
     * @param key
     * @return
     */

    public static String getString(Context context,String key){

            SharedPreferences sp = context.getSharedPreferences("feisou", Context.MODE_PRIVATE);
            return sp.getString(key, "");

    }

    /**
     * 保存String类型的数据
     * @param context
     * @param key
     * @param values
     */
    public static void putString(Context context,String key,String values){

            SharedPreferences sp = context.getSharedPreferences("feisou", Context.MODE_PRIVATE);
            sp.edit().putString(key, values).commit();
        }


}
