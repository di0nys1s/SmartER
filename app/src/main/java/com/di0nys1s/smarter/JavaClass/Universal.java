package com.di0nys1s.smarter.JavaClass;

import android.app.Application;
import android.content.Context;

public class Universal extends Application {

    private static int resId;
    private static String address;
    private static String firstName;
    private static String lastName;
    private static Context mContext;
    private static int postcode;

    public static String getExceptionInfo(Exception ex) {
        StringBuilder sb = new StringBuilder((ex.getMessage() + "\n"));
        for (StackTraceElement el : ex.getStackTrace()) {
            sb.append(el.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    public static int getResId() {
        return resId;
    }
    public static void setResId(int resId) {
        Universal.resId = resId;
    }

    public static String getFirstName() {
        return firstName;
    }
    public static void setFirstName(String firstName) {
        Universal.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }
    public static void setLastName(String lastName) {
        Universal.lastName = lastName;
    }

    public static String getAddress() {
        return address;
    }
    public static void setAddress(String address) {
        Universal.address = address;
    }

    public static Context getmContext() {
        return mContext;
    }
    public static void setmContext(Context mContext) {
        Universal.mContext = mContext;
    }

    public static int getPostcode() {
        return postcode;
    }
    public static void setPostcode(int postcode) {
        Universal.postcode = postcode;
    }

    public static boolean isEmptyOrNull (String str) {
        return str == null || "".equals(str);
    }

}
