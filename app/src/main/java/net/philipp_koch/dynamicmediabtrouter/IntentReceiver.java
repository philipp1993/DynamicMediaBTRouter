package net.philipp_koch.dynamicmediabtrouter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Philipp on 14.03.2015.
 */
public class IntentReceiver extends BroadcastReceiver {

    public void onReceive(Context paramContext, Intent paramIntent)
    {
        if (paramIntent.getAction().equals("net.philipp_koch.dynamicmediabtrouter.ON")){
            paramContext.startService(new Intent(paramContext, RedirectorService.class));
            return;
        }
        if (paramIntent.getAction().equals("net.philipp_koch.dynamicmediabtrouter.OFF")) {
            paramContext.stopService(new Intent(paramContext, RedirectorService.class));
            return;
        }
    }
}
