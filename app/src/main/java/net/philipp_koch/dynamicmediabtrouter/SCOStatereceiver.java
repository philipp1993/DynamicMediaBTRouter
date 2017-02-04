package net.philipp_koch.dynamicmediabtrouter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Philipp on 15.03.2015.
 */
public class SCOStatereceiver extends BroadcastReceiver {
    public SharedPreferences localPreferences;

    @Override
    public void onReceive(Context localContext, Intent localIntent) {
        Log.d("BTReceiver", "Broadcast received");
        int service = Global.getService_Color();
        localPreferences = PreferenceManager.getDefaultSharedPreferences(localContext);
        boolean staticredirection, aftercall;
        staticredirection = localPreferences.getBoolean("staticredirection", false);
        aftercall = localPreferences.getBoolean("aftercall", false);
        String intent = localIntent.getAction();
        if (service == Color.GREEN && "android.media.SCO_AUDIO_STATE_CHANGED".equals(intent) && staticredirection && aftercall) {
            Log.d("BTReceiver", "settings ok");
            if (localIntent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", -1) == 0) {
                Log.d("BTReceiver", "state ok");
                localContext.stopService(new Intent(localContext, RedirectorService.class));
                android.os.SystemClock.sleep(300);
                localContext.startService(new Intent(localContext, RedirectorService.class));
            }
        }
    }
}
