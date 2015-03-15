package net.philipp_koch.dynamicmediabtrouter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
/**
 * Created by Philipp on 14.03.2015.
 */
public class IntentReceiver extends BroadcastReceiver {

    public void onReceive(Context paramContext, Intent paramIntent)
    {
        if (paramIntent.getAction().equals("net.philipp_koch.dynamicmediabtrouter.ON")){
            Toast.makeText(paramContext, "Intent: On", Toast.LENGTH_LONG).show();
            paramContext.startService(new Intent(paramContext, RedirectorService.class));
            return;
        }
        if (paramIntent.getAction().equals("net.philipp_koch.dynamicmediabtrouter.OFF")) {
            Toast.makeText(paramContext, "Intent: Off", Toast.LENGTH_LONG).show();
            paramContext.stopService(new Intent(paramContext, RedirectorService.class));
            return;
        }
    }
}
