package net.philipp_koch.dynamicmediabtrouter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * Created by Philipp on 14.03.2015.
 */
public class IntentReceiver extends BroadcastReceiver {

    Resources res = Global.getContext().getResources();

    public void onReceive(Context localContext, Intent localIntent) {
        if (localIntent.getAction().equals("net.philipp_koch.dynamicmediabtrouter.ON")) {
            Toast.makeText(localContext, "Intent: " + res.getString(R.string.on), Toast.LENGTH_LONG).show();
            Global.setIntentRequest(true);
            localContext.startService(new Intent(localContext, RedirectorService.class));
            android.os.SystemClock.sleep(500);
            Global.setIntentRequest(false);
            return;
        }
        if (localIntent.getAction().equals("net.philipp_koch.dynamicmediabtrouter.OFF")) {
            Toast.makeText(localContext, "Intent: " + res.getString(R.string.off), Toast.LENGTH_LONG).show();
            localContext.stopService(new Intent(localContext, RedirectorService.class));
            return;
        }
        if (localIntent.getAction().equals("net.philipp_koch.dynamicmediabtrouter.XRequestON")) {
            Toast.makeText(localContext, "Xposed: " + res.getString(R.string.on), Toast.LENGTH_LONG).show();
            Global.setXposedRequestON(true);
            return;
        }
        if (localIntent.getAction().equals("net.philipp_koch.dynamicmediabtrouter.XRequestOFF")) {
            Toast.makeText(localContext, "Xposed: " + res.getString(R.string.off), Toast.LENGTH_LONG).show();
            localContext.stopService(new Intent(localContext, RedirectorService.class));
            return;
        }
    }
}
