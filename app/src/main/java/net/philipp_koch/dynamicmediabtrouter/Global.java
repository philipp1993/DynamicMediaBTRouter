package net.philipp_koch.dynamicmediabtrouter;

import android.app.Application;
import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;


import java.lang.String;
/**
 * Created by Philipp on 14.03.2015.
 */
public class Global extends Application {

    private static String Service, BT, BTDev, Audio;
    private static int Service_Color, BT_Color, BTDev_Color, Audio_Color;
    private static boolean XposedRequestON=false;
    private static boolean XposedRequestOFF=false;
    private static Context mContext;


    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = this.getApplicationContext();
        Service_Color = Color.RED;
        BT_Color = Color.RED;
        BTDev_Color = Color.WHITE;
        Audio_Color = Color.WHITE;
        Service = "NO";
        BT = "";
        BTDev = "";
        Audio = "";

    }

    public static String getService()
    {
        return Service;
    }

    public static String getBT()
    {
        return BT;
    }

    public static String getBTDev()
    {
        return BTDev;
    }

    public static String getAudio()
    {
        return Audio;
    }

    public static int getService_Color() { return Service_Color;}

    public static int getBT_Color() { return BT_Color;}

    public static int getBTDev_Color() { return  BTDev_Color;}

    public static int getAudio_Color() { return Audio_Color;}

    public static boolean getXposedRequestON() { return XposedRequestON;}

    public static boolean getXposedRequestOFF() { return XposedRequestOFF;}

    public static void setService(String str)
    {
        Service = str;
    }

    public static void setBT(String str)
    {
        BT = str;
    }

    public static void setBTDev(String str)
    {
        BTDev = str;
    }

    public static void setAudio(String str)
    {
        Audio = str;
    }

    public static void setService_Color(int i) { Service_Color = i;}

    public static void setBT_Color (int i) { BT_Color = i;}

    public static void setBTDev_Color (int i) { BTDev_Color = i;}

    public static void setAudio_Color (int i) { Audio_Color = i;}

    public static void setXposedRequestON(Boolean bool) { XposedRequestON = bool;}

    public static void setXposedRequestOFF(Boolean bool) { XposedRequestOFF = bool;}

    public static Context getContext(){
        return mContext;
    }

}