package com.aptiquiz.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {


    public static void setSecretKey(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("secretKey", value);
        editor.apply();
    }

    public static String getSecretKey(Context context) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        return sp.getString("secretKey","secret");
    }


    public static void setLogin(Context context, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("loginData", value);
        editor.apply();
    }

    public static boolean getLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        return sp.getBoolean("loginData",false);
    }

    public static void setUserId(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userId", value);
        editor.apply();
    }

    public static String getUserID(Context context) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        return sp.getString("userId","secret");
    }

    public static void setUserName(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName", value);
        editor.apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        return sp.getString("userName","secret");
    }

    public static void setUserNumber(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userNumber", value);
        editor.apply();
    }

    public static String getUserNumber(Context context) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        return sp.getString("userNumber","secret");
    }

    public static void setUserEmail(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userEmail", value);
        editor.apply();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        return sp.getString("userEmail","secret");
    }

    public static void setUserProfession(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userProfession", value);
        editor.apply();
    }

    public static String getUserProfession(Context context) {
        SharedPreferences sp = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        return sp.getString("userProfession","secret");
    }



}
