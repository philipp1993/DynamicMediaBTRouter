package net.philipp_koch.dynamicmediabtrouter;

import android.app.Application;
import java.lang.String;
/**
 * Created by Philipp on 14.03.2015.
 */
public class Global extends Application {

    private static String Service, BT, BTDev, Audio;

    @Override
    public void onCreate()
    {
        super.onCreate();
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
}