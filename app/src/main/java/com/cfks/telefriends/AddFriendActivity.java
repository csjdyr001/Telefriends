package com.cfks.telefriends;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.cfks.telefriends.xtc.BleProfile;
import com.cfks.telefriends.xtc.BleUtils;
import com.cfks.telefriends.xtc.StatusInfo;
import com.cfks.telefriends.xtc.Utils;

public class AddFriendActivity extends AppCompatActivity {
    public static boolean isFirstAddFriend = true;
    private StatusInfo mStatusInfo;
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ((Button) findViewById(R.id.cancel_add_friends)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO: Implement this method
                Intent intent = new Intent();
                intent.setClass(AddFriendActivity.this, MainActivity.class);
                AddFriendActivity.this.startActivity(intent);
                AddFriendActivity.this.finish();
            }
        });
        initData();
        BleProfile.setDelayFirstTime();
    }
    
    @TargetApi(23)
    public void initData() {
        this.mStatusInfo = StatusInfo.getInstance(getApplicationContext());
        //this.mOnCompletionListener = new AddFriendMainActivity$1(this);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        // TODO: Implement this method
        enableBluetooth(this, this.mStatusInfo);
        requestBluetoothFromSettings(this);
    }

    private static void enableBluetooth(Context context, StatusInfo statusInfo) {
        BluetoothAdapter e = BleUtils.e(context);
        for (BluetoothDevice bluetoothDevice : e.getBondedDevices()) {
            if ("XTC-Z3".equals(bluetoothDevice.getName())) {
                Log.d("BleAddFriend_:", "Pair is removed=" + Utils.removeBond(bluetoothDevice));
            }
        }
        int state = e.getState();
        Log.d("BleAddFriend_:", "bluetooth state==" + state);
        if (state == 10) {
            isFirstAddFriend = true;
            Log.d("BleAddFriend_:", "开启蓝牙");
            statusInfo.setOpenBtSelf(true);
            BleUtils.b(e);
        }
    }

    private static void requestBluetoothFromSettings(Context context) {
        Log.d("BleAddFriend_:", "");
        Intent intent = new Intent("com.xtc.bluetooth.ACTION_DELAY_CLOSE");
        intent.putExtra("time", 120000L);
        context.sendBroadcast(intent);
    }
}
