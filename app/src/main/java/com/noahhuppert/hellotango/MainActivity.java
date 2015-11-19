package com.noahhuppert.hellotango;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.atap.tango.ux.TangoUx;
import com.google.atap.tango.ux.TangoUxLayout;
import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.TangoConfig;
import com.google.atap.tangoservice.TangoErrorException;
import com.google.atap.tangoservice.TangoOutOfDateException;
import com.noahhuppert.hellotango.listeners.TangoUpdateListener;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private Tango tango;
    private TangoConfig tangoConfig;

    private TangoUx tangoUx;
    private TangoUx.StartParams uxStartParams;
    private TangoUxLayout tangoUxLayout;

    private TangoUpdateListener tangoUpdateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Tango
        tango = new Tango(this);
        tangoConfig = tango.getConfig(TangoConfig.CONFIG_TYPE_CURRENT);
        tangoConfig.putBoolean(TangoConfig.KEY_BOOLEAN_MOTIONTRACKING, true);
        tangoConfig.putBoolean(TangoConfig.KEY_BOOLEAN_DEPTH, true);

        tangoUx = new TangoUx(this);
        uxStartParams = new TangoUx.StartParams();
        tangoUxLayout = (TangoUxLayout) findViewById(R.id.layout_tango);

        tangoUx.setLayout(tangoUxLayout);

        tangoUpdateListener = new TangoUpdateListener(tangoUx);
        tango.connectListener(tangoUpdateListener.framePairs, tangoUpdateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tangoUx.start(uxStartParams);
        tangoUx.setHoldPosture(TangoUx.TYPE_HOLD_POSTURE_FORWARD);

        try {
            tango.connect(tangoConfig);
        } catch (TangoOutOfDateException e) {
            Log.e(TAG, e.getMessage());
            if (tangoUx != null) tangoUx.showTangoOutOfDate();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            tango.disconnect();
        } catch (TangoErrorException e) {
            Log.e(TAG, e.getMessage());
        }

        tangoUx.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
