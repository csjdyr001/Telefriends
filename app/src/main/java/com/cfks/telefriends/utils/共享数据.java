package com.cfks.telefriends.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class 共享数据 {
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sp;

    public static void 初始化数据(Context context, String string) {
        sp = context.getSharedPreferences(string, 0);
        editor = sp.edit();
    }

    public static int 取整数(String string) {
        return sp.getInt(string, 0);
    }

    public static int 取整数(String string, int n) {
        return sp.getInt(string, n);
    }

    public static String 取文本(String string) {
        return sp.getString(string, "");
    }

    public static String 取文本(String string, String string2) {
        return sp.getString(string, string2);
    }

    public static float 取浮点数(String string) {
        return sp.getFloat(string, 0.0f);
    }

    public static float 取浮点数(String string, float f) {
        return sp.getFloat(string, f);
    }

    public static boolean 取逻辑(String string) {
        return sp.getBoolean(string, false);
    }

    public static boolean 取逻辑(String string, boolean bl) {
        return sp.getBoolean(string, bl);
    }

    public static long 取长整数(String string) {
        return sp.getLong(string, 0L);
    }

    public static long 取长整数(String string, long l) {
        return sp.getLong(string, l);
    }

    public static boolean 是否包含数据(String string) {
        return sp.contains(string);
    }

    public static boolean 清除数据() {
        editor.clear();
        return editor.commit();
    }

    public static boolean 移除数据(String string) {
        editor.remove(string);
        return editor.commit();
    }

    public static boolean 置整数(String string, int n) {
        editor.putInt(string, n);
        return editor.commit();
    }

    public static boolean 置文本(String string, String string2) {
        editor.putString(string, string2);
        return editor.commit();
    }

    public static boolean 置浮点数(String string, float f) {
        editor.putFloat(string, f);
        return editor.commit();
    }

    public static boolean 置逻辑(String string, boolean bl) {
        editor.putBoolean(string, bl);
        return editor.commit();
    }

    public static boolean 置长整数(String string, long l) {
        editor.putLong(string, l);
        return editor.commit();
    }

    public void 初始化事件() {
    }
}


