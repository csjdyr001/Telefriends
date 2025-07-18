package com.cfks.telefriends;

import android.content.Intent;
import android.os.Bundle;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.cfks.telefriends.watch.XTC;
import com.xtc.bleaddfriend.ui.AddFriendUiEvent;
import com.xtc.hardware.bluetooth.ble.util.BleUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
            XTC.init(this);
            break;
        default:
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
    
  
    
   @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUiEvent(AddFriendUiEvent addFriendUiEvent) {
        Log.d("BleAddFriend_:", "onUiEvent: event.what" + addFriendUiEvent.what);
        int i = addFriendUiEvent.what;
        switch (i) {
            case 0:
            case 1:
                //startOneAnimation();
                XTC.mAdapter = BleUtils.e(this);
                Log.d("BleAddFriend_:", "onUiEvent: mAdapter=" + XTC.mAdapter);
                if (XTC.mAdapter.getState() == 12) {
                    Log.i("BleAddFriend_:", "start bind service");
                    XTC.requestBluetoothFromSettings(this);
                    XTC.startBindService(XTC.mService, XTC.mConnection);
                    return;
                } else {
                    Log.i("BleAddFriend_:", "register receiver");
                    registerReceiver(XTC.mBluetoothReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
                    return;
                }
            default:
                switch (i) {
                    case 23:
                        Log.i("BleAddFriend_Finish", "0");
                        XTC.clearTimeScheduler();
                        String str = (String) addFriendUiEvent.obj;
                        Log.i("BleAddFriend_Finish", "watchName=" + str);
                        if (TextUtils.isEmpty(str)) {
                            XTC.showBleConnectView("");
                            return;
                        }
                        if (XTC.mTimeOfFail != null) {
                            XTC.mTimeOfFail.cancel();
                            XTC.mTimeOfFail = null;
                        }
                        XTC.showBleConnectView(str);
                        return;
                    case 24:
                        XTC.clearTimeScheduler();
                        XTC.showAddFirendSuccessful(true);
                        return;
                    case 25:
                        XTC.showNoWatchAroundView(true);
                        return;
                    case 26:
                        XTC.clearTimeScheduler();
                        XTC.showTouchFriendWatchAndAddSuccess();
                        return;
                    default:
                        Log.e("BleAddFriend_:", "unexpect value");
                        return;
                }
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // TODO: Implement this method
        XTC.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // TODO: Implement this method
        XTC.onResume();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // TODO: Implement this method
        XTC.onNewIntent(intent);
    }
}