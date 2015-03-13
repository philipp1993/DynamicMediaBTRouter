package net.philipp_koch.dynamicmediabtrouter;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.os.IBinder;
import android.widget.Toast;
import java.lang.Thread;
import android.util.Log;

/**
 * Created by Philipp on 12.03.2015.
 */
public class RedirectorService extends Service {

    boolean keeprunning=false;

    public IBinder onBind(Intent Intent)
    {
        return null;
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
    {
        Toast.makeText(this, "OnStartCommand", Toast.LENGTH_LONG).show();
        return Service.START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Toast.makeText(this, "Service starting", Toast.LENGTH_LONG).show();
        keeprunning=true;
        new Thread()
        {
            public void run() {
                checkSound();
                return;
            }
        }.start();
    }

    @Override
    public void onDestroy()
    {
        keeprunning=false;
        AudioManager localAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        android.os.SystemClock.sleep(1500);
        localAudioManager.setBluetoothScoOn(false);
        localAudioManager.stopBluetoothSco();
        localAudioManager.setMode(AudioManager.MODE_NORMAL);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }


    public void checkSound() {
        AudioManager localAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);

        Visualizer localVisualizer = new Visualizer(0);
        localVisualizer.setEnabled(true);
        Visualizer.MeasurementPeakRms localPeak = new Visualizer.MeasurementPeakRms();

        boolean wasPlayingBefore=false;
        while (keeprunning) //Loop to detect changes on the Media Audio Stream
        {
            localVisualizer.getMeasurementPeakRms(localPeak);
            if(localPeak.mPeak > -8500 && !wasPlayingBefore)
            {
                //There is some audio output
                wasPlayingBefore=true;
                localAudioManager.setBluetoothScoOn(true);
                localAudioManager.startBluetoothSco();
                localAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                android.os.SystemClock.sleep(5000); //Route to BT Headset will exist min. 5 seconds ...
            }
            if(localPeak.mPeak <= -8500 && wasPlayingBefore)
            {
                //output (temporary) gone
                android.os.SystemClock.sleep(2000);//... plus this 2 seconds
                //check again
                localVisualizer.getMeasurementPeakRms(localPeak);
            }
            if(localPeak.mPeak <= -8500 && wasPlayingBefore)
            {
                //Audio didn't get back in last 2 seconds...
                wasPlayingBefore=false;
                localAudioManager.setBluetoothScoOn(false);
                localAudioManager.stopBluetoothSco();
                localAudioManager.setMode(AudioManager.MODE_NORMAL);
            }
            android.os.SystemClock.sleep(100); //Slow down the loop
            Log.d("Peak", String.valueOf(localPeak.mPeak));//Debug info - Audio peak -9600 = Silent, 0 = MAX Output
       }
        localVisualizer.release();
    }

}
