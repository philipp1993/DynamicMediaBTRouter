package net.philipp_koch.dynamicmediabtrouter;

import android.app.Application;
import java.lang.String;
/**
 * Created by Philipp on 14.03.2015.
 */
public class Global extends Application {

    public static String Service, BT, BTDev, Audio;

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
}