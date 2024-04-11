package com.curryout.appsharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;

public class AppSharedPreference {

    public static final String PREFS_NAME = "CURRY_PREFS";
    public static final String PHONE = "phone";
    private Context ctx;

    public AppSharedPreference() {
        super();
    }

    public final static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        } else {
            return Patterns.PHONE.matcher(phone).matches();
        }
    }

    public static void saveUserPhoneToPreferences(Context ctx, String mobile) {

        try {
            SharedPreferences.Editor editor = ctx.getSharedPreferences("AppPref", Context.MODE_PRIVATE).edit();
            editor.putString("Mobile", mobile);
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String loadUserPhoneFromPreference(Context ctx) {
        String mobile = "";
        try {
            SharedPreferences prefs = ctx.getSharedPreferences("AppPref" , Context.MODE_PRIVATE);
            mobile = prefs.getString("Mobile", "");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return mobile;
    }
    //Token
    public static void saveTokenPreferences(Context ctx, String token) {

        try {
            SharedPreferences.Editor editor = ctx.getSharedPreferences("AppPref", Context.MODE_PRIVATE).edit();
            editor.putString("Token", token);
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String loadTokenPreference(Context ctx) {
        String token = "";
        try {
            SharedPreferences prefs = ctx.getSharedPreferences("AppPref" , Context.MODE_PRIVATE);
            token = prefs.getString("Token", "");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return token;
    }


}
