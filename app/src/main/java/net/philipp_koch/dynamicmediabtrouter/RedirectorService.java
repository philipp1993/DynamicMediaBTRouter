package net.philipp_koch.dynamicmediabtrouter;

import java.lang.String;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.content.Intent;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;
import java.lang.Thread;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
/**
 * Created by Philipp on 12.03.2015.
 */
public class RedirectorService extends Service {

    private boolean keeprunning = false;
    private static final int api = Build.VERSION.SDK_INT;
    public SharedPreferences localPreferences;

    public IBinder onBind(Intent Intent) {
        return null;
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        //Toast.makeText(this, "OnStartCommand", Toast.LENGTH_LONG).show();
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        int btHeadsetState = localBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET); //get the current connection status for the handsfree profile
        localPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean staticredirection = localPreferences.getBoolean("staticredirection", false);
        Log.d("BTService", "static pref: " + staticredirection);

        if(btHeadsetState == BluetoothProfile.STATE_CONNECTED || btHeadsetState == BluetoothProfile.STATE_DISCONNECTED) {
            keeprunning = true;

            Intent notificationIntent = new Intent(getBaseContext(), MainActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(getBaseContext(), 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification localNotification = new NotificationCompat.Builder(getApplicationContext())
                    .setTicker(getString(R.string.starting))
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .build();

            startForeground(1, localNotification);

            Global.setService(getString(R.string.yes));
            Global.setService_Color(Color.GREEN);
            if(staticredirection)
            {
                AudioManager localAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                Global.setAudio(getString(R.string.noInformation));
                Global.setAudio_Color(Color.WHITE);
                localAudioManager.setBluetoothScoOn(true);
                localAudioManager.startBluetoothSco();
                localAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            }
            else {
                Global.setAudio(getString(R.string.no));
                Global.setAudio_Color(Color.RED);
                new Thread() {
                    public void run() {
                        checkSound();
                    }
                }.start();
            }
        }
        else
        {
            Toast.makeText(this, getString(R.string.TurnBTon), Toast.LENGTH_LONG).show();
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        keeprunning = false;
        Global.setAudio("");
        Global.setService(getString(R.string.no));
        Global.setService_Color(Color.RED);
        AudioManager localAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        localAudioManager.setBluetoothScoOn(false);
        localAudioManager.stopBluetoothSco();
        localAudioManager.setMode(AudioManager.MODE_NORMAL);
    }

    private void checkSound() {
        Global localGlobal = ((Global)getApplicationContext());
        AudioManager localAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        boolean wasPlayingBefore = false;
        boolean headsetConnectedBefore = false;
        BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        int btHeadsetState;

        if (api >= 19) {
            Visualizer localVisualizer = new Visualizer(0);
            localVisualizer.setEnabled(true);
            localVisualizer.setMeasurementMode(Visualizer.MEASUREMENT_MODE_PEAK_RMS);
            Visualizer.MeasurementPeakRms localPeak = new Visualizer.MeasurementPeakRms();

            while (keeprunning) //Loop to detect changes on the Media Audio Stream
            {
                localVisualizer.getMeasurementPeakRms(localPeak);//get the current audio peak -9600 = silent, 0 = MAX output
                btHeadsetState = localBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET); //get the current connection status for the handsfree profile
                if ((localPeak.mPeak > -8500 || Global.getXposedRequestON()) && !wasPlayingBefore) {
                    //There is some audio output
                    wasPlayingBefore = true;
                    Global.setAudio(getString(R.string.yes));
                    Global.setAudio_Color(Color.GREEN);
                    Global.setXposedRequestON(false);
                    localAudioManager.setBluetoothScoOn(true);
                    localAudioManager.startBluetoothSco();
                    localAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    android.os.SystemClock.sleep(5000); //Route to BT Headset will exist min. 5 seconds ...
                }
                if (localPeak.mPeak <= -8500 && wasPlayingBefore) {
                    //output (temporary) gone
                    android.os.SystemClock.sleep(2000);//... plus this 2 seconds
                    localVisualizer.getMeasurementPeakRms(localPeak); //check again
                }
                if ((localPeak.mPeak <= -8500 && wasPlayingBefore) || Global.getXposedRequestOFF()) {
                    //Audio didn't get back in last 2 seconds...
                    Global.setXposedRequestOFF(false);
                    headsetConnectedBefore = false;
                    Global.setAudio(getString(R.string.no));
                    Global.setAudio_Color(Color.RED);
                    localAudioManager.setBluetoothScoOn(false);
                    localAudioManager.stopBluetoothSco();
                    localAudioManager.setMode(AudioManager.MODE_NORMAL);
                }
                android.os.SystemClock.sleep(100); //Slow down the loop
                //Log.d("BTService", "Peak: " + String.valueOf(localPeak.mPeak)); //Debug info - Audio peak -9600 = Silent, 0 = MAX Output
            }
            localVisualizer.release();
        } else {
            while (keeprunning) {
                //No KITKAT :(

                if ((localAudioManager.isMusicActive() || Global.getXposedRequestON()) && !wasPlayingBefore) {
                    //There is some audio output
                    Global.setXposedRequestON(false);
                    wasPlayingBefore = true;
                    localAudioManager.setBluetoothScoOn(true);
                    localAudioManager.startBluetoothSco();
                    localAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    android.os.SystemClock.sleep(3000); //Route to BT Headset will exist min. 3 seconds ...
                }
                if (!localAudioManager.isMusicActive() && wasPlayingBefore) {
                    //output (temporary) gone
                    android.os.SystemClock.sleep(2000);//... plus this 2 seconds
                }
                if ((!localAudioManager.isMusicActive() && wasPlayingBefore) || Global.getXposedRequestOFF()) {
                    //Audio didn't get back in last 2 seconds...
                    Global.setXposedRequestOFF(false);
                    wasPlayingBefore = false;
                    localAudioManager.setBluetoothScoOn(false);
                    localAudioManager.stopBluetoothSco();
                    localAudioManager.setMode(AudioManager.MODE_NORMAL);
                }
                android.os.SystemClock.sleep(100); //Slow down the loop
                Log.d("BTService", "Music active: " + String.valueOf(localAudioManager.isMusicActive())); //Debug info - Audio peak -9600 = Silent, 0 = MAX Output
            }
        }
    }
}
