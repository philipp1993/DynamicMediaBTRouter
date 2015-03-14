package net.philipp_koch.dynamicmediabtrouter;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

}
