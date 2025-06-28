package com.cfks.telefriends;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.cfks.telefriends.xtc.BleProfile;
import com.cfks.telefriends.xtc.BleUtils;
import com.cfks.telefriends.xtc.StatusInfo;
import com.cfks.telefriends.xtc.Utils;

public class AddFriendActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_friend);
    ((Button) findViewById(R.id.cancel_add_friends)).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        // TODO: Implement this method
        backMainActivity();
      }
    });
    
    Intent intent = getIntent();
    int watch_type = intent.getIntExtra("watch_type",-1);
    switch(watch_type){
        case WatchType.WATCH_XTC:
            XTC.toDo(this);
            break;
        case WatchType.WATCH_360:
            Toast.makeText(this, "作者没做", Toast.LENGTH_SHORT).show();
            backMainActivity();
            break;
        case WatchType.WATCH_MITU:
            Toast.makeText(this, "作者没做", Toast.LENGTH_SHORT).show();
            backMainActivity();
            break;
    }
  }
    
  private void backMainActivity() {
  	Intent intent = new Intent();
      intent.setClass(this, MainActivity.class);
      startActivity(intent);
      finish();
  }

  private static class XTC {
    private static StatusInfo mStatusInfo;
    
    public static void toDo(Activity act) {
      initData(act);
      BleProfile.setDelayFirstTime();
      enableBluetooth(act, mStatusInfo);
      requestBluetoothFromSettings(act);
    }

    @TargetApi(23)
    private static void initData(Activity act) {
      mStatusInfo = StatusInfo.getInstance(act.getApplicationContext());
    }

    private static void enableBluetooth(Context context, StatusInfo statusInfo) {
      BluetoothAdapter e = BleUtils.e(context);
      for (BluetoothDevice bluetoothDevice: e.getBondedDevices()) {
        if ("XTC-Z3".equals(bluetoothDevice.getName())) {
          Log.d("BleAddFriend_:", "Pair is removed=" + Utils.removeBond(bluetoothDevice));
        }
      }
      int state = e.getState();
      Log.d("BleAddFriend_:", "bluetooth state==" + state);
      if (state == 10) {
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
}