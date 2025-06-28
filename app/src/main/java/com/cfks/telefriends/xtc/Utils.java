package com.cfks.telefriends.xtc;

import android.app.ActivityManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.Method;
import java.util.Locale;

public final class Utils {
    public static final String ACTION_POST_DATA_TO_SERVER = "com.xtc.bleaddfriend.post.data";
    private static final String TAG = "BBK_" + Utils.class.getSimpleName();

    public static void printHexString(byte[] bArr, String str) {
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            Log.d("BleAddFriend_:", str + "0x" + hexString.toUpperCase(Locale.getDefault()));
        }
    }

    static boolean isNetWorkAvailable(Context context, String str) {
        Log.i("BleAddFriend_:", "isNetWorkAvailable: context = " + context + "typeName = " + str);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        boolean z = false;
        if (connectivityManager == null) {
            Log.e("BleAddFriend_:", "isNetWorkAvailable connetManager = null");
            return false;
        }
        if (Build.VERSION.SDK_INT > 24) {
            Network[] allNetworks = connectivityManager.getAllNetworks();
            if (allNetworks == null) {
                return false;
            }
            if (str == null || str.length() <= 0) {
                for (int i = 0; i < allNetworks.length && allNetworks[i] != null; i++) {
                    NetworkInfo networkInfo = connectivityManager.getNetworkInfo(allNetworks[i]);
                    if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                        z = true;
                        break;
                    }
                }
                Log.i("BleAddFriend_:", "isNetWorkAvailable result is : " + z);
                return z;
            }
            for (int i2 = 0; i2 < allNetworks.length && allNetworks[i2] != null; i2++) {
                NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(allNetworks[i2]);
                if (networkInfo2.getTypeName().equalsIgnoreCase(str) && networkInfo2.isConnected() && networkInfo2.isAvailable()) {
                    Log.i("BleAddFriend_:", "isNetWorkAvailable name is : " + networkInfo2.getTypeName());
                    z = true;
                    break;
                }
            }
            Log.i("BleAddFriend_:", "isNetWorkAvailable result is : " + z);
            return z;
        }
        NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
        if (allNetworkInfo == null) {
            return false;
        }
        if (str == null || str.length() <= 0) {
            for (int i3 = 0; i3 < allNetworkInfo.length && allNetworkInfo[i3] != null; i3++) {
                if (allNetworkInfo[i3].isConnected() && allNetworkInfo[i3].isAvailable()) {
                    z = true;
                    break;
                }
            }
            Log.i("BleAddFriend_:", "isNetWorkAvailable result is : " + z);
            return z;
        }
        for (int i4 = 0; i4 < allNetworkInfo.length && allNetworkInfo[i4] != null; i4++) {
            if (allNetworkInfo[i4].getTypeName().equalsIgnoreCase(str) && allNetworkInfo[i4].isConnected() && allNetworkInfo[i4].isAvailable()) {
                Log.i("BleAddFriend_:", "isNetWorkAvailable name is : " + allNetworkInfo[i4].getTypeName());
                z = true;
                break;
            }
        }
        Log.i("BleAddFriend_:", "isNetWorkAvailable result is : " + z);
        return z;
    }

    public static boolean networkStatus(Context context) {
        return isNetWorkAvailable(context, "");
    }

    public static boolean removeBond(BluetoothDevice bluetoothDevice) {
        try {
            return ((Boolean) Class.forName("android.bluetooth.BluetoothDevice").getMethod("removeBond", new Class[0]).invoke(bluetoothDevice, new Object[0])).booleanValue();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public static void forceStopPackage(String str, Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        try {
            Log.i(TAG, "kill pid name =" + str);
            Method declaredMethod = ActivityManager.class.getDeclaredMethod("forceStopPackage", String.class);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(activityManager, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
