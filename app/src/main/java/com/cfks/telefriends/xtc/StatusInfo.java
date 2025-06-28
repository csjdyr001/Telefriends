package com.cfks.telefriends.xtc;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class StatusInfo {
    public static final String ADD_FRIEND_FAIL = "add_friend_fail";
    private static final String BLE_PREFERENCE = "add_friends";
    public static final int CENTRAL_STATE_ADVERISTER = 31;
    public static final int CENTRAL_STATE_CONNECT = 32;
    public static final int CENTRAL_STATE_DISCONNECT = 33;
    public static final int CENTRAL_STATE_IDLE = 30;
    public static final int CENTRAL_STATE_RECVIVER_MSG = 35;
    public static final int CENTRAL_STATE_SEND_MSG = 34;
    private static final String CENTRAL_STATUS = "central_status";
    public static final String FRIEND_WATCH_ID = "friend_watch_id";
    public static final String FRIEND_WATCH_NAME = "friend_watch_name";
    public static final String OPEN_BT_SELF = "open_bt_self";
    public static final int PERIPHERAL_STATE_ADVERISTER = 15;
    public static final int PERIPHERAL_STATE_CONNECT = 16;
    public static final int PERIPHERAL_STATE_DISCONNECT = 17;
    public static final int PERIPHERAL_STATE_IDLE = 14;
    public static final int PERIPHERAL_STATE_RECVIVER_MSG = 19;
    public static final int PERIPHERAL_STATE_SEND_MSG = 18;
    private static final String PERIPHERAL_STATUS = "peripheral_status";
    public static final int SESSION_STATE_ADDFRIEND_SUCCESSFUL = 7;
    public static final int SESSION_STATE_BLE_CONNECT = 2;
    public static final int SESSION_STATE_BLE_INIT = 1;
    public static final int SESSION_STATE_IDLE = 0;
    public static final int SESSION_STATE_POST_MSG_TO_SERVER = 4;
    public static final int SESSION_STATE_POST_MSG_TO_SERVER_PAUSE = 5;
    public static final int SESSION_STATE_RECVIVER_MSG = 3;
    public static final int SESSION_STATE_UNINIT = 6;
    private static final String SESSION_STATUS = "session_status";
    public static final int STATE_NO_NETWORK = 50;
    public static final String WATCH_ID = "watch_id";
    public static final String WATCH_NAME = "watch_name";
    public static final String WATCH_TEL_NUMVER = "watch_tel_number";
    private static StatusInfo mStatusInfo;
    private SharedPreferences mPreference;

    public StatusInfo(Context context) {
        this.mPreference = null;
        this.mPreference = context.getSharedPreferences(BLE_PREFERENCE, 0);
    }

    public static StatusInfo getInstance(Context context) {
        if (mStatusInfo == null) {
            synchronized (StatusInfo.class) {
                if (mStatusInfo == null) {
                    mStatusInfo = new StatusInfo(context);
                }
            }
        }
        return mStatusInfo;
    }

    public void resetStatusInfoInfo() {
        Log.i("BleAddFriend_:", "resetStatusInfoInfo");
        setSessionStatus(0);
        setPeripheralStatus(14);
        setCentralStatus(30);
    }

    public int getSessionStatus() {
        int i = this.mPreference.getInt(SESSION_STATUS, 0);
        Log.i("BleAddFriend_:", "getSessionStatus, get status = " + i);
        return i;
    }

    public void setSessionStatus(int i) {
        Log.i("BleAddFriend_:", "setSessionStatus, status = " + i);
        this.mPreference.edit().putInt(SESSION_STATUS, i).apply();
    }

    public int getPeripheralStatus() {
        int i = this.mPreference.getInt(PERIPHERAL_STATUS, 14);
        Log.i("BleAddFriend_:", "getPeripheralStatus, get status = " + i);
        return i;
    }

    public void setPeripheralStatus(int i) {
        Log.i("BleAddFriend_:", "setPeripheralStatus, status = " + i);
        this.mPreference.edit().putInt(PERIPHERAL_STATUS, i).apply();
    }

    public int getCentralStatus() {
        int i = this.mPreference.getInt(CENTRAL_STATUS, 30);
        Log.i("BleAddFriend_:", "getCentralStatus, get status = " + i);
        return i;
    }

    public void setCentralStatus(int i) {
        Log.i("BleAddFriend_:", "setCentralStatus, status = " + i);
        this.mPreference.edit().putInt(CENTRAL_STATUS, i).apply();
    }

    public String getWatchId() {
        String string = this.mPreference.getString(WATCH_ID, "");
        Log.i("BleAddFriend_:", "getWatchId, get result = " + string);
        return string;
    }

    public void setWatchId(String str) {
        Log.i("BleAddFriend_:", "setWatchId, watchid = " + str);
        this.mPreference.edit().putString(WATCH_ID, str).apply();
    }

    public void setFriendWatchId(String str) {
        Log.i("BleAddFriend_:", "setFriendWatchId, watchid = " + str);
        this.mPreference.edit().putString(FRIEND_WATCH_ID, str).apply();
    }

    public String getFriendWatchId() {
        String string = this.mPreference.getString(FRIEND_WATCH_ID, "");
        Log.i("BleAddFriend_:", "getFriendWatchId, get result = " + string);
        return string;
    }

    public void setFriendWatchName(String str) {
        this.mPreference.edit().putString(FRIEND_WATCH_NAME, str).apply();
    }

    public String getFriendWatchName() {
        return this.mPreference.getString(FRIEND_WATCH_NAME, "");
    }

    public String getTelNumber() {
        String string = this.mPreference.getString(WATCH_TEL_NUMVER, "");
        Log.i("BleAddFriend_:", "getTelNumber, get result = " + string);
        return string;
    }

    public void setTelNumber(String str) {
        Log.i("BleAddFriend_:", "setTelNumber, telnumber = " + str);
        this.mPreference.edit().putString(WATCH_TEL_NUMVER, str).apply();
    }

    public String getWatchName() {
        String string = this.mPreference.getString(WATCH_NAME, "XTC");
        Log.i("BleAddFriend_:", "getWatchName, get result = " + string);
        return string;
    }

    public void setWatchName(String str) {
        Log.i("BleAddFriend_:", "setWatchName, msg = " + str);
        this.mPreference.edit().putString(WATCH_NAME, str).apply();
    }

    public Boolean getOpenBtSelf() {
        Boolean valueOf = Boolean.valueOf(this.mPreference.getBoolean(OPEN_BT_SELF, false));
        Log.i("BleAddFriend_:", "getOpenBtSelf, get result = " + valueOf);
        return valueOf;
    }

    public void setOpenBtSelf(Boolean bool) {
        Log.i("BleAddFriend_:", "setOpenBtSelf, msg = " + bool);
        this.mPreference.edit().putBoolean(OPEN_BT_SELF, bool.booleanValue()).apply();
    }
}
