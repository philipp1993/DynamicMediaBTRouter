package net.philipp_koch.dynamicmediabtrouter;

import android.app.Activity;
import android.bluetooth.BluetoothProfile;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.TextView;
import java.lang.Thread;
import android.bluetooth.BluetoothAdapter;


public class MainActivity extends Activity {
    public TextView textService, textBT, textBTDev, textAudio, textXposed;
    boolean refresh = false;
    public Global localGlobal;
    private AudioManager localAudioManager;
    private SeekBar SeekBarMedia;
    private CheckBox staticRedirect, afterCall;
    public SharedPreferences localPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textService = (TextView) findViewById(R.id.text_Service_value);
        textBT = (TextView) findViewById(R.id.text_BT_value);
        textBTDev = (TextView) findViewById(R.id.text_BTDev_value);
        textAudio = (TextView) findViewById(R.id.text_Audio_value);
        textXposed = (TextView) findViewById(R.id.text_Xposed_value);
        localGlobal = (Global) getApplicationContext();
        localAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        SeekBarMedia = (SeekBar)findViewById(R.id.Volume_Media);
        SeekBarMedia.setMax(localAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        SeekBarMedia.setProgress(localAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        SeekBarMedia.setOnSeekBarChangeListener(ChangeVolumeMedia);
        localPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        staticRedirect =(CheckBox)findViewById(R.id.check_static);
        staticRedirect.setChecked(localPreferences.getBoolean("staticredirection", false));
        afterCall =(CheckBox)findViewById(R.id.check_call);
        afterCall.setChecked(localPreferences.getBoolean("aftercall", false));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        refresh = true;
        new Thread() {
            public void run() {
                updateGui();
            }
        }.start();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        refresh = true;
        new Thread() {
            public void run() {
                updateGui();
            }
        }.start();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refresh = true;
        new Thread() {
            public void run() {
                updateGui();
            }
        }.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        refresh = false;
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        refresh = false;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        refresh = false;
    }

    public void Off()
    {
        Toast.makeText(this, "Action: Off", Toast.LENGTH_LONG).show();
        stopService(new Intent(this, RedirectorService.class));
    }

    public void On()
    {
        Toast.makeText(this, "Action: On", Toast.LENGTH_LONG).show();
        startService(new Intent(this, RedirectorService.class));
    }

    public void ButtonOff(View paramView)
    {
        Off();
    }

    public void ButtonOn(View paramView)
    {
        On();
    }

    public void goToHelpAbout (View paramView) { goToUrl ( "http://philipp-koch.net/btrouter.php"); }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void updateGui()
    {
        final int api = Build.VERSION.SDK_INT;
        BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        while(refresh)
        {
            int btState = localBluetoothAdapter.getState();
            if(btState == BluetoothAdapter.STATE_ON || btState == BluetoothAdapter.STATE_TURNING_ON)
            {
                localGlobal.BT="ON";
                if (api >= 14)
                {
                    int btHeadsetState = localBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET); //get the current connection status for the handsfree profile
                    if(btHeadsetState == BluetoothProfile.STATE_CONNECTED)
                    {
                        localGlobal.BTDev="connected";
                    }
                    else
                    {
                        localGlobal.BTDev="disconnected";
                    }
                }
                else
                {
                    localGlobal.BTDev="connection not possible to determine";
                }
            }
            else
            {
                localGlobal.BT="OFF";
                localGlobal.BTDev="";
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textService.setText(localGlobal.Service);
                    if(localGlobal.Service.equals("YES")){textService.setTextColor(Color.GREEN);} else {textService.setTextColor(Color.RED);}
                    textBT.setText(localGlobal.BT);
                    if(localGlobal.BT.equals("ON")){textBT.setTextColor(Color.GREEN);} else {textBT.setTextColor(Color.RED);}
                    textBTDev.setText(localGlobal.BTDev);
                    if(localGlobal.BTDev.equals("connected")){textBTDev.setTextColor(Color.GREEN);} else { if(localGlobal.BTDev.equals("disconnected")){textBTDev.setTextColor(Color.RED);} else {textBTDev.setTextColor(Color.WHITE);}}
                    textAudio.setText(localGlobal.Audio);
                    if(localGlobal.Audio.equals("NO")){textAudio.setTextColor(Color.RED);} else { if(localGlobal.Audio.equals("YES")){textAudio.setTextColor(Color.GREEN);} else {textAudio.setTextColor(Color.WHITE);}}
                    if(textXposed.getText().toString().equals("inactive")){textXposed.setTextColor(Color.RED);} else {textXposed.setTextColor(Color.GREEN);}
                }
            });
            android.os.SystemClock.sleep(300);
        }
    }

    private SeekBar.OnSeekBarChangeListener ChangeVolumeMedia = new SeekBar.OnSeekBarChangeListener()
    {
        public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
        {
            int i = SeekBarMedia.getProgress();
            localAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 1);
        }

        public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}

        public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {}
    };


    public void onCheckboxClicked(View view) {
        SharedPreferences.Editor localEditor = localPreferences.edit();
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.check_static:
                if (checked)
                {
                    localEditor.putBoolean("staticredirection", true);
                }
                else
                {
                    localEditor.putBoolean("staticredirection", false);
                }
                break;
            case R.id.check_call:
                if (checked)
                {
                    localEditor.putBoolean("aftercall", true);
                }
                else
                {
                    localEditor.putBoolean("aftercall", false);
                }
                break;
        }
    localEditor.apply();
    }
}