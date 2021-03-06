package com.kinjo1506.batterylogger;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // ログ取得開始
            BatteryLoggerLauncher.getInstance().startLogging(this);

            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new MainFragment(), MainFragment.TAG)
                    .commit();
        }
    }
}
