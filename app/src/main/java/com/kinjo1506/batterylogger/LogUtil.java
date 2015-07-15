package com.kinjo1506.batterylogger;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.format.DateFormat;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogUtil {
    private LogUtil() {
    }

    /**
     * 指定したファイルにログを追記する
     * @param context
     * @param filename
     * @param log
     * @return 成功した場合は true
     */
    public static boolean append(Context context, String filename, String log) {
        try (PrintWriter writer = createLogWriter(context, filename)) {
            writer.println(
                    String.format("%s  %s", DateFormat.format("MM-dd HH:mm:ss", System.currentTimeMillis()), log));
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * ログ書き込み用の PrintWriter オブジェクトを返す。
     * 指定したファイルが存在しない場合はファイルを作成し、先頭に端末情報を記録する。
     *
     * @param context
     * @param filename
     * @return
     * @throws IOException
     */
    private static PrintWriter createLogWriter(Context context, String filename) throws IOException {
        File file = new File(context.getExternalFilesDir(null), filename);
        boolean needsInit = !file.exists() || (file.length() <= 0); // ファイルが空のときは初期化必要

        PrintWriter writer = new PrintWriter(new FileWriter(file, /* append = */ true));
        if (needsInit) {
            writer.println("********");
            writer.println("sysname:   Android");
            writer.println("sysver:    " + Build.VERSION.RELEASE);
            writer.println("model:     " + Build.MODEL);
            writer.println("appid:     " + context.getPackageName());
            writer.println("appver:    " + getAppVer(context));
            writer.println("device_id: " + getAdId(context));
            writer.println("********");
        }

        return writer;
    }

    /**
     * Advertising Identifier を取得する
     *
     * @param context
     * @return
     */
    private static String getAdId(Context context) {
        try {
            return AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
        }
        catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * アプリのバージョン名を取得する
     *
     * @param context
     * @return
     */
    private static String getAppVer(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        }
        catch (Exception e) {
            return e.toString();
        }
    }

}
