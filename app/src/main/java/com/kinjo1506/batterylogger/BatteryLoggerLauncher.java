package com.kinjo1506.batterylogger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

public class BatteryLoggerLauncher extends BroadcastReceiver {
    private static final long INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(30);

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent pi = PendingIntent.getService(
                context, 0, new Intent(context, BatteryLoggerService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        // 次のキリのいい時刻から開始する
        long triggerAtMillis = (System.currentTimeMillis() / INTERVAL_MILLIS + 1) * INTERVAL_MILLIS;

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, INTERVAL_MILLIS, pi);
    }
}
