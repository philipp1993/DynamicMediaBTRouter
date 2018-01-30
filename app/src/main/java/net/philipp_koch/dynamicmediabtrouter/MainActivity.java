package net.philipp_koch.dynamicmediabtrouter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends Activity {
    public TextView textService, textBT, textBTDev, textAudio, textCall, textXposed;
    private static final int api = Build.VERSION.SDK_INT;
    public SeekBar SeekBarMedia;
    public SharedPreferences localPreferences;
    boolean refresh = false;
    private AudioManager localAudioManager;
    private SeekBar.OnSeekBarChangeListener ChangeVolumeMedia = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
        }

        public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
        }

        public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {

            int i = paramAnonymousSeekBar.getProgress();
            localAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckBox staticRedirect, afterCall, autoStart, autoStop;

        textService = (TextView) findViewById(R.id.text_Service_value);
        textBT = (TextView) findViewById(R.id.text_BT_value);
        textBTDev = (TextView) findViewById(R.id.text_BTDev_value);
        textAudio = (TextView) findViewById(R.id.text_Audio_value);
        //textCall = (TextView) findViewById(R.id.text_Audio_value);
        //textXposed = (TextView) findViewById(R.id.text_Xposed_value);

        localAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        SeekBarMedia = (SeekBar) findViewById(R.id.Volume_Media);
        SeekBarMedia.setMax(localAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        SeekBarMedia.setOnSeekBarChangeListener(ChangeVolumeMedia);

        localPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        staticRedirect = (CheckBox) findViewById(R.id.check_static);
        staticRedirect.setChecked(localPreferences.getBoolean("staticredirection", false));

        afterCall = (CheckBox) findViewById(R.id.check_call);
        afterCall.setChecked(localPreferences.getBoolean("aftercall", false));

        autoStart = (CheckBox) findViewById(R.id.check_start);
        autoStart.setChecked(localPreferences.getBoolean("autoStart", false));

        autoStop = (CheckBox) findViewById(R.id.check_stop);
        autoStop.setChecked(localPreferences.getBoolean("autoStop", true));

        if (api >= 23 && !staticRedirect.isChecked()) {
            int permissionGranted = getApplicationContext().checkSelfPermission("android.permission.RECORD_AUDIO");
            if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[] {"android.permission.RECORD_AUDIO"};
                requestPermissions(permissions,1);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh = true;
        new Thread() {
            public void run() {
                updateGui();
            }
        }.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refresh = true;
        new Thread() {
            public void run() {
                updateGui();
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh = true;
        new Thread() {
            public void run() {
                updateGui();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        refresh = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        refresh = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refresh = false;
    }

    public void Off() {
        //Toast.makeText(this, "Action: Off", Toast.LENGTH_LONG).show();
        stopService(new Intent(this, RedirectorService.class));
    }

    public void On() {
        //Toast.makeText(this, "Action: On", Toast.LENGTH_LONG).show();
        startService(new Intent(this, RedirectorService.class));
    }

    public void ButtonOff(View paramView) {
        Off();
    }

    public void ButtonOn(View paramView) {
        On();
    }

    public void goToHelpAbout(View paramView) {
        goToUrl("https://philipp-koch.net/btrouter.php");
    }

    public void goToPrivacy(View paramView) {
        goToUrl("https://derappkoch.de/privacy.php");
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void updateGui() {
        BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        int btState = localBluetoothAdapter.getState();
        if (btState == BluetoothAdapter.STATE_ON || btState == BluetoothAdapter.STATE_TURNING_ON) {
            Global.setBT(getString(R.string.on));
            Global.setBT_Color(Color.GREEN);

            int btHeadsetState = localBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET); //get the current connection status for the handsfree profile
            switch (btHeadsetState) {
                case BluetoothProfile.STATE_CONNECTING:
                    Global.setBTDev(getString(R.string.connecting));
                    Global.setBTDev_Color(Color.YELLOW);
                    break;

                case BluetoothProfile.STATE_CONNECTED:
                    Global.setBTDev(getString(R.string.connected));
                    Global.setBTDev_Color(Color.GREEN);
                    break;

                case BluetoothProfile.STATE_DISCONNECTED:
                case BluetoothProfile.STATE_DISCONNECTING:
                    Global.setBTDev(getString(R.string.disconnected));
                    Global.setBTDev_Color(Color.RED);
                    break;
            }
        } else {
            Global.setBT(getString(R.string.off));
            Global.setBT_Color(Color.RED);
            Global.setBTDev("");
        }

        while (refresh) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //Log.d("MainActivity","XposedReguest: " + Global.getXposedRequestON());
                    textService.setText(Global.getService());
                    textService.setTextColor(Global.getService_Color());

                    textBT.setText(Global.getBT());
                    textBT.setTextColor(Global.getBT_Color());

                    textBTDev.setText(Global.getBTDev());
                    textBTDev.setTextColor(Global.getBTDev_Color());

                    textAudio.setText(Global.getAudio());
                    textAudio.setTextColor(Global.getAudio_Color());

                    //textCall.setText(Global.getCall());
                    //textCall.setTextColor(Global.getCall_Color());

                    //if(textXposed.getText().equals("inactive")){textXposed.setTextColor(Color.RED);} else {textXposed.setTextColor(Color.GREEN);}

                    SeekBarMedia.setProgress(localAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                }
            });
            android.os.SystemClock.sleep(300);
        }
    }

    public void onCheckboxClicked(View view) {
        SharedPreferences.Editor localEditor = localPreferences.edit();
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.check_static:
                if (checked) {
                    localEditor.putBoolean("staticredirection", true);
                } else {
                    localEditor.putBoolean("staticredirection", false);
                }
                break;
            case R.id.check_call:
                if (checked) {
                    localEditor.putBoolean("aftercall", true);
                } else {
                    localEditor.putBoolean("aftercall", false);
                }
                break;
            case R.id.check_start:
                if (checked) {
                    localEditor.putBoolean("autoStart", true);
                } else {
                    localEditor.putBoolean("autoStart", false);
                }
                break;
            case R.id.check_stop:
                if (checked) {
                    localEditor.putBoolean("autoStop", true);
                } else {
                    localEditor.putBoolean("autoStop", false);
                }
                break;
        }
        localEditor.apply();
    }
}