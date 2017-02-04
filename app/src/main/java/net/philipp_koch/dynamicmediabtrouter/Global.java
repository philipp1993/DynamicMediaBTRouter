package net.philipp_koch.dynamicmediabtrouter;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.graphics.Color;

/**
 * Created by Philipp on 14.03.2015.
 */
public class Global extends Application {

    private static String Service, BT, BTDev, Audio, Call;
    private static int Service_Color, BT_Color, BTDev_Color, Audio_Color, Call_Color;
    private static boolean XposedRequestON = false;
    private static boolean XposedRequestOFF = false;
    private static boolean IntentRequest = false;
    private static Context mContext;
    private static Notification noti;

    public static String getCall() {
        return Call;
    }

    public static void setCall(String str) {
        Call = str;
    }

    public static int getCall_Color() {
        return Call_Color;
    }

    public static void setCall_Color(int i) {
        Call_Color = i;
    }

    public static String getService() {
        return Service;
    }

    public static void setService(String str) {
        Service = str;
    }

    public static String getBT() {
        return BT;
    }

    public static void setBT(String str) {
        BT = str;
    }

    public static String getBTDev() {
        return BTDev;
    }

    public static void setBTDev(String str) {
        BTDev = str;
    }

    public static String getAudio() {
        return Audio;
    }

    public static void setAudio(String str) {
        Audio = str;
    }

    public static int getService_Color() {
        return Service_Color;
    }

    public static void setService_Color(int i) {
        Service_Color = i;
    }

    public static int getBT_Color() {
        return BT_Color;
    }

    public static void setBT_Color(int i) {
        BT_Color = i;
    }

    public static int getBTDev_Color() {
        return BTDev_Color;
    }

    public static void setBTDev_Color(int i) {
        BTDev_Color = i;
    }

    public static int getAudio_Color() {
        return Audio_Color;
    }

    public static void setAudio_Color(int i) {
        Audio_Color = i;
    }

    public static boolean getXposedRequestON() {
        return XposedRequestON;
    }

    public static void setXposedRequestON(Boolean bool) {
        XposedRequestON = bool;
    }

    public static boolean getXposedRequestOFF() {
        return XposedRequestOFF;
    }

    public static void setXposedRequestOFF(Boolean bool) {
        XposedRequestOFF = bool;
    }

    public static boolean getIntentRequest() {
        return IntentRequest;
    }

    public static void setIntentRequest(Boolean bool) {
        IntentRequest = bool;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        Service_Color = Color.RED;
        BT_Color = Color.RED;
        BTDev_Color = Color.WHITE;
        Audio_Color = Color.WHITE;
        Call_Color = Color.WHITE;
        Service = this.getApplicationContext().getResources().getString(R.string.no);
        BT = "";
        BTDev = "";
        Audio = "";
        Call = "";

    }

}