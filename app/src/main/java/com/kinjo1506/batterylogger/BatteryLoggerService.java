package com.kinjo1506.batterylogger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.annotation.NonNull;

public class BatteryLoggerService extends IntentService {
    private static final String TAG = BatteryLoggerService.class.getSimpleName();

    public BatteryLoggerService() {
        super(TAG);
    }

    private static Intent getBatteryChangedIntent(Context context) {
        return context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    /**
     * バッテリー残量を取得する。
     *
     * @param context
     * @return バッテリー残量 (%)。取得できなかった場合は -1
     */
    private static int getBatteryLevel(Context context) {
        Intent battery = getBatteryChangedIntent(context);
        if (battery == null) { return -1; }

        int max   = battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        return (level * 100 / max);
    }

    /**
     * バッテリーの状態を取得する。
     *
     * @param context
     * @return バッテリーの状態
     */
    @NonNull
    private static String getBatteryStatus(Context context) {
        Intent battery = getBatteryChangedIntent(context);
        if (battery == null) { return "Unknown"; }

        switch (battery.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN)) {
            case BatteryManager.BATTERY_STATUS_CHARGING:     { return "Charging"; }
            case BatteryManager.BATTERY_STATUS_DISCHARGING:  { return "Discharging"; }
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING: { return "Not charging"; }
            case BatteryManager.BATTERY_STATUS_FULL:         { return "Full"; }
            default:         /* BATTERY_STATUS_UNKNOWN: */   { return "Unknown"; }
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String filename = intent.getStringExtra(BatteryLoggerLauncher.EXTRA_FILENAME);
        LogUtil.append(this, filename, String.format(
                "Battery Level: %3d%%  Status: %s", getBatteryLevel(this), getBatteryStatus(this)));
    }
}
