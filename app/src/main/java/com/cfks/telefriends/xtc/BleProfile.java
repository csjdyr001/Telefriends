package com.cfks.telefriends.xtc;
import android.util.Log;

public final class BleProfile {
    public static final String BLE_BT_NAME = "XTCWatch";
    public static final long CLIENT_WAITING_FOR_CONNECTION_CALLBACK = 3000;
    public static final long CLIENT_WAITING_FOR_WRITING_CALLBACK = 2000;
    public static final byte HEADER_CONTINUE = -69;
    public static final byte HEADER_PACKET = -85;
    public static final byte HEADER_SUCCESS = -86;
    public static final int I3_DEVICE = 1;
    public static final byte LAST_PACKET = -1;
    public static final long SERVER_CONNECTED_WAITING_FOR_WRITE_REQUEST = 2000;
    public static final long SERVER_SCANNED_WAITING_FOR_CONNECTION = 6000;
    public static final long SERVER_WAITING_FOR_WRITE_REQUEST_AGAIN = 2000;
    public static final int UNKNOWN_DEVICE = -1;
    public static final String UUID_CHARACTERISTIC_80_0 = "00002a00-0000-1000-8000-00805F9B34FB";
    public static final String UUID_CHARACTERISTIC_80_1 = "00002a01-0000-1000-8000-00805F9B34FB";
    public static final String UUID_CHARACTERISTIC_80_2 = "00002a02-0000-1000-8000-00805F9B34FB";
    public static final String UUID_CHARACTERISTIC_80_4 = "00002a04-0000-1000-8000-00805F9B34FB";
    public static final String UUID_CHARACTERISTIC_81 = "00002a05-0000-1000-8000-00805F9B34FB";
    public static final String UUID_CHARACTERISTIC_NOTIFY = "00002A19-0000-1000-8000-00805F9B34FB";
    public static final String UUID_CHARACTERISTIC_WRITE = "00002A06-0000-1000-8000-00805F9B34FB";
    public static final String UUID_SERVICE = "00006000-0000-1000-8000-00805f9b34fb";
    public static final String UUID_SERVICE2 = "00005752-0000-1000-8000-00805f9b34fb";
    public static final String UUID_SERVICE3 = "0000422d-0000-1000-8000-00805f9b34fb";
    public static final String UUID_SERVICE4 = "0000454c-0000-1000-8000-00805f9b34fb";
    public static final String UUID_SERVICE_80 = "00001800-0000-1000-8000-00805F9b34FB";
    public static final String UUID_SERVICE_81 = "00001801-0000-1000-8000-00805F9b34FB";
    public static final String UUID_SERVICE_82 = "00001802-0000-1000-8000-00805F9b34FB";
    public static final String UUID_SERVICE_83 = "00001803-0000-1000-8000-00805F9b34FB";
    public static final String UUID_SERVICE_84 = "00001804-0000-1000-8000-00805F9b34FB";
    public static final String UUID_SERVICE_85 = "00001805-0000-1000-8000-00805F9b34FB";
    public static final String UUID_SERVICE_86 = "00001806-0000-1000-8000-00805F9b34FB";
    public static final String UUID_SERVICE_READ = "0000180F-0000-1000-8000-00805F9b34FB";
    public static final String UUID_SERVICE_WRITE = "00001802-0000-1000-8000-00805F9B34FB";
    public static final int Y12I6_DEVICE = 0;
    public static long clientWaitingForServiceDiscovered = 5000;
    public static long delayAfterConnected;
    public static long delayAfterStopScan;
    public static long delayBeforeFirstNotify;
    public static long delayBeforeFirstWrite;
    public static long delayBeforeFirstWriteTo2G;
    public static long delayBetweenNotify;
    public static long delayBetweenWrite;
    public static long delayBetweenWriteTo2g;
    public static long delayRestartAddFriend;
    public static long delayRewrite;

    private BleProfile() {
    }

    public static void setDelay2g() {
        Log.d("BleAddFriend_:", "setDelay2g: ");
        delayBeforeFirstWriteTo2G = 100L;
        delayBetweenWriteTo2g = 0L;
        delayBeforeFirstWrite = 0L;
        delayBeforeFirstNotify = 0L;
        delayBetweenWrite = 0L;
        delayBetweenNotify = 0L;
        delayRewrite = 500L;
        delayRestartAddFriend = 300L;
        delayAfterStopScan = 200L;
        delayAfterConnected = 200L;
        clientWaitingForServiceDiscovered = delayAfterConnected + 5000;
    }

    public static void setDelayFirstTime() {
        Log.d("BleAddFriend_:", "setDelayFirstTime: ");
        delayBeforeFirstWriteTo2G = 100L;
        delayBetweenWriteTo2g = 0L;
        delayBeforeFirstWrite = 0L;
        delayBeforeFirstNotify = 0L;
        delayBetweenWrite = 0L;
        delayBetweenNotify = 0L;
        delayRewrite = 500L;
        delayRestartAddFriend = 300L;
        delayAfterStopScan = 200L;
        delayAfterConnected = 200L;
        clientWaitingForServiceDiscovered = delayAfterConnected + 5000;
    }

    public static void setDelayNormal() {
        Log.d("BleAddFriend_:", "setDelayNormal: ");
        delayBeforeFirstWriteTo2G = 100L;
        delayBetweenWriteTo2g = 0L;
        delayBeforeFirstWrite = 300L;
        delayBeforeFirstNotify = 200L;
        delayBetweenWrite = 500L;
        delayBetweenNotify = 500L;
        delayRewrite = 500L;
        delayRestartAddFriend = 300L;
        delayAfterStopScan = 200L;
        delayAfterConnected = 200L;
        clientWaitingForServiceDiscovered = delayAfterConnected + 5000;
    }
}
