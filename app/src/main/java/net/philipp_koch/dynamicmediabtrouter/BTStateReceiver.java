package net.philipp_koch.dynamicmediabtrouter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Philipp on 14.05.2015.
 */
public class BTStateReceiver extends BroadcastReceiver{

    public SharedPreferences localPreferences;
    final int api = Build.VERSION.SDK_INT;
    BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    public void onReceive(Context localContext, Intent localIntent)
    {
        Resources res = Global.getContext().getResources();
        localPreferences = PreferenceManager.getDefaultSharedPreferences(localContext);
        boolean autoStart, autoStop;
        autoStart = localPreferences.getBoolean("autoStart", false);
        autoStop = localPreferences.getBoolean("autoStop", true);
        String intent = localIntent.getAction();
        Log.d("BTSTateReceiver", intent);

        if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent))
        {
            //Log.d("BTStateReceiver", "state changed");
            int AdapterState = localIntent.getIntExtra("android.bluetooth.adapter.extra.STATE", BluetoothAdapter.ERROR);
            if((AdapterState == BluetoothAdapter.STATE_OFF || AdapterState == BluetoothAdapter.STATE_TURNING_OFF))
            {
                Log.d("BTStateReceiver", "state off");
                Global.setBT(res.getString(R.string.off));
                Global.setBT_Color(Color.RED);
                Global.setBTDev("");
                if(autoStop)
                {
                    localContext.stopService(new Intent(localContext, RedirectorService.class));
                }
            }
            else
            {
                Log.d("BTStateReceiver", "state on");
                Global.setBT(res.getString(R.string.on));
                Global.setBT_Color(Color.GREEN);
                Global.setBTDev(res.getString(R.string.disconnected));
                Global.setBTDev_Color(Color.RED);
            }
        }

        if("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED".equals(intent)) {
            //Log.d("BTStateReceiver", "connection changed");
            int ConnectionState = localIntent.getIntExtra("android.bluetooth.adapter.extra.CONNECTION_STATE", BluetoothAdapter.ERROR);
            Log.d("BTStateReceiver", "connection state: " + ConnectionState);

            if (ConnectionState == BluetoothAdapter.STATE_DISCONNECTED || ConnectionState == BluetoothAdapter.STATE_DISCONNECTING) {
                Global.setBTDev(res.getString(R.string.disconnected));
                Global.setBTDev_Color(Color.RED);
                if(autoStop){
                    localContext.stopService(new Intent(localContext, RedirectorService.class));
                }
            }

            if (ConnectionState == BluetoothAdapter.STATE_CONNECTING) {
                Global.setBTDev(res.getString(R.string.connecting));
                Global.setBTDev_Color(Color.YELLOW);
            }

            if (ConnectionState == BluetoothAdapter.STATE_CONNECTED) {
                int btHeadsetState = localBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET); //get the current connection status for the handsfree profile
                Log.d("BTSTateReceiver", "HFP state: " + btHeadsetState);

                if(btHeadsetState == BluetoothProfile.STATE_CONNECTING) {
                    int counter = 0;
                    int waittime = 200;
                    Global.setBTDev(res.getString(R.string.connecting));
                    Global.setBTDev_Color(Color.YELLOW);
                    while (btHeadsetState == BluetoothProfile.STATE_CONNECTING && counter < (60000 / waittime)) {
                        btHeadsetState = localBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
                        counter++;
                        android.os.SystemClock.sleep(waittime);
                    }
                }
                if (btHeadsetState == BluetoothProfile.STATE_CONNECTED) {
                    Global.setBTDev(res.getString(R.string.connected));
                    Global.setBTDev_Color(Color.GREEN);
                    if (autoStart) {
                        localContext.startService(new Intent(localContext, RedirectorService.class));
                    }
                }
                else {
                    Global.setBTDev(res.getString(R.string.disconnected));
                    Global.setBTDev_Color(Color.RED);
                }
            }
        }
    }
}
