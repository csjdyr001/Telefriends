package com.cfks.telefriends.xtc;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@TargetApi(18)
public class BleUtils {
    public static boolean b() {
        return false;
    }

    public static boolean a(@NonNull Context context) {
        return a() && f((Context) Check.a(context));
    }

    public static boolean a() {
        return Build.VERSION.SDK_INT >= 18;
    }

    private static boolean f(@NonNull Context context) {
        return ((Context) Check.a(context)).getPackageManager().hasSystemFeature("android.hardware.bluetooth_le");
    }

    public static boolean b(@NonNull Context context) {
        return a(e((Context) Check.a(context)));
    }

    public static boolean a(@Nullable BluetoothAdapter bluetoothAdapter) {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public static void c(@NonNull Context context) {
        b(e((Context) Check.a(context)));
    }

    public static void b(@Nullable BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter == null || bluetoothAdapter.isEnabled()) {
            return;
        }
        bluetoothAdapter.enable();
    }

    @NonNull
    public static BluetoothManager d(@NonNull Context context) {
        return (BluetoothManager) ((Context) Check.a(context)).getSystemService("bluetooth");
    }

    @Nullable
    public static BluetoothAdapter a(@NonNull BluetoothManager bluetoothManager) {
        return ((BluetoothManager) Check.a(bluetoothManager)).getAdapter();
    }

    @Nullable
    public static BluetoothAdapter e(@NonNull Context context) {
        return a(d((Context) Check.a(context)));
    }

    public static String a(SparseArray<byte[]> sparseArray) {
        if (sparseArray == null) {
            return "null";
        }
        if (sparseArray.size() == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (int i = 0; i < sparseArray.size(); i++) {
            sb.append(sparseArray.keyAt(i));
            sb.append("=");
            sb.append(Arrays.toString(sparseArray.valueAt(i)));
        }
        sb.append('}');
        return sb.toString();
    }

    public static <T> String a(Map<T, byte[]> map) {
        if (map == null) {
            return "null";
        }
        if (map.isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        Iterator<Map.Entry<T, byte[]>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            T key = it.next().getKey();
            sb.append(key);
            sb.append("=");
            sb.append(Arrays.toString(map.get(key)));
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public static boolean a(SparseArray<byte[]> sparseArray, SparseArray<byte[]> sparseArray2) {
        if (sparseArray == sparseArray2) {
            return true;
        }
        if (sparseArray == null || sparseArray2 == null || sparseArray.size() != sparseArray2.size()) {
            return false;
        }
        for (int i = 0; i < sparseArray.size(); i++) {
            if (sparseArray.keyAt(i) != sparseArray2.keyAt(i) || !Arrays.equals(sparseArray.valueAt(i), sparseArray2.valueAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean a(Map<T, byte[]> map, Map<T, byte[]> map2) {
        if (map == map2) {
            return true;
        }
        if (map == null || map2 == null || map.size() != map2.size()) {
            return false;
        }
        Set<T> keySet = map.keySet();
        if (!keySet.equals(map2.keySet())) {
            return false;
        }
        for (T t : keySet) {
            if (!ObjectUtils.a(map.get(t), map2.get(t))) {
                return false;
            }
        }
        return true;
    }

    public static void c(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter == null || bluetoothAdapter.getState() != 12) {
            throw new IllegalStateException("BT Adapter is not turned ON");
        }
    }
}
