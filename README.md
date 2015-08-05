## 概要

一定時間ごとに端末のバッテリー残量とステータス ( 消費中、充電中、充電完了、etc. ) を記録します。  
ログは /sdcard/Android/data/com.kinjo1506.batterylogger/files/battery_log.txt に保存されます。

## ログの記録間隔

初期状態では 30 分ごと ( 毎時 00 分 / 30 分 ) にバッテリー残量を取得、記録します。  
BatteryLoggerLauncher.java の INTERVAL_MILLIS を変更すると、ログの周期を変更できます。

## ログを取り直す

「ログを取り直す」ボタンをタップすると、現在のログファイルを別名 (*1) で保存し  
新たなファイルにログの記録を開始します。

*1. ファイル名は battery_log_yyyyMMdd_HHmmss.txt ( ログ記録開始日時 ) となります。
