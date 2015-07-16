package com.kinjo1506.batterylogger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;

import java.util.concurrent.TimeUnit;

public class BatteryLoggerLauncher {
    public  static final String EXTRA_FILENAME  = "com.kinjo1506.batterylogger.extra.FILENAME";
    private static final String FILENAME        = "battery_log.txt";
    private static final long   INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(30);

    private static BatteryLoggerLauncher sInstance;

    private BatteryLoggerLauncher() {
    }

    public static BatteryLoggerLauncher getInstance() {
        if (sInstance == null) {
            sInstance = new BatteryLoggerLauncher();
        }
        return sInstance;
    }

    /**
     * バッテリー残量記録の開始を予約する。
     *
     * @param context
     */
    public void startLogging(Context context) {
        Intent intent = new Intent(context, BatteryLoggerService.class)
                                .putExtra(EXTRA_FILENAME, FILENAME);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 次のキリのいい時刻から開始する
        long triggerAtMillis = (System.currentTimeMillis() / INTERVAL_MILLIS + 1) * INTERVAL_MILLIS;

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, INTERVAL_MILLIS, pi);

        // 記録開始時刻を覚えておく
        if (Preferences.getLoggerStartAt(context, -1) == -1) {
            Preferences.setLoggerStartAt(context, triggerAtMillis);
        }
    }

    /**
     * バッテリー残量の記録を取り直す。
     * 現在までの記録を別名で保存し、新たに記録の開始を予約する。
     *
     * @param context
     * @return 成功した場合は true
     */
    public boolean afresh(Context context) {
        String dest = String.format(
                "%s%s%s",
                FILENAME.substring(0, FILENAME.lastIndexOf('.')),
                DateFormat.format("_yyyyMMdd_HHmmss", getLoggerStartAt(context, 0)),
                FILENAME.substring(FILENAME.lastIndexOf('.')));
        if (!LogUtil.rename(context, FILENAME, dest)) { return false; }

        // 記録開始時刻もリセットする
        Preferences.removeLoggerStartAt(context);
        startLogging(context);
        return true;
    }

    /**
     * バッテリー残量の記録を撮り始める(た)時刻を取得する
     *
     * @param context
     * @param defaultValue
     * @return
     */
    public long getLoggerStartAt(Context context, long defaultValue) {
        return Preferences.getLoggerStartAt(context, defaultValue);
    }

    public static class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BatteryLoggerLauncher.getInstance().startLogging(context);
        }
    }
}
