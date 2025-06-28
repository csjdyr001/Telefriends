package com.cfks.telefriends.watch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.cfks.telefriends.watch.XTC;
import com.xtc.bleaddfriend.bluetooth.BleProfile;
import com.xtc.bleaddfriend.service.BleManagerService;
import com.xtc.bleaddfriend.util.StatusInfo;
import com.xtc.bleaddfriend.util.Utils;
import com.xtc.hardware.bluetooth.ble.util.BleUtils;
import java.util.UUID;

public class XTC {
  private static StatusInfo mStatusInfo;
  private static final String ALARM_VIEW_SHOWING_ACTION = "com.xtc.alarmclock.action.ALARM_VIEW_SHOWING";
  private static TelephonyManager telephonyManager;
  private static BleManagerService mService = null;
  private static ServiceConnection mConnection;
  private static final int ANIMATION_THREE_STEP = 4;
  private static BluetoothAdapter mAdapter;
  private static boolean hasRegisterReceiver = false;
  private static boolean hasStartPeripheralAndCentral = false;
  private static BroadcastReceiver mBluetoothReceiver;

  public static void toDo(Activity act) {
    initData(act);
    BleProfile.setDelayFirstTime();
    enableBluetooth(act, mStatusInfo);
    requestBluetoothFromSettings(act);
    registerListener(act);
    handleIntent(act, act.getIntent());
  }

  @TargetApi(23)
  private static void initData(Activity act) {
    mStatusInfo = StatusInfo.getInstance(act.getApplicationContext());
    fakeData(act);
  }
    
  private static void fakeData(Activity act){
      //记录原数据
      String watchId = mStatusInfo.getWatchId();
      String watchName = mStatusInfo.getWatchName();
      String telNumber = mStatusInfo.getTelNumber();
      //手表数据伪装？
      mStatusInfo.setWatchId(UUID.randomUUID().toString());
      mStatusInfo.setWatchName("无处不在的草方块");
      mStatusInfo.setTelNumber("114514");
      //反馈结果
      Toast.makeText(act, "原手表ID：" + watchId + "\n现手表ID：" + mStatusInfo.getWatchId() + "\n原手表名称：" + watchName + "\n现手表名称：" + mStatusInfo.getWatchName(), Toast.LENGTH_SHORT).show();
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

  private static void registerListener(final Activity act) {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
    intentFilter.addAction(ALARM_VIEW_SHOWING_ACTION);
    act.registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("BleAddFriend_:", "onReceive: " + action);
        if ("android.intent.action.ACTION_SHUTDOWN".equals(action) || "com.xtc.alarmclock.action.ALARM_VIEW_SHOWING".equals(action)) {
          act.finish();
        }
      }
    }, intentFilter);
    telephonyManager = (TelephonyManager) act.getSystemService("phone");
    telephonyManager.listen(new PhoneStateListener() {
      @Override
      public void onCallStateChanged(int i, String str) {
        super.onCallStateChanged(i, str);
        Log.d("BleAddFriend_ScanActivity", "state---->>" + i);
        switch (i) {
          case 0:
          case 2:
          default:
            return;
          case 1:
            act.finish();
            return;
        }
      }
    }, 32);
    mBluetoothReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
          Log.d("BleAddFriend_:", "### Bluetooth State has changed ##");
          int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 12);
          Log.d("BleAddFriend_:", "onReceive: btState=" + intExtra);
          switch (intExtra) {
            case 10:
              if (mAdapter == null) {
                mAdapter = BleUtils.e(act);
              }
              Log.e("BleAddFriend_:", "onReceive: mAdapter=" + mAdapter);
              mAdapter.enable();
              return;
            case 11:
            case 13:
              return;
            case 12:
              requestBluetoothFromSettings(context);
              startBindService(act, mService, mConnection);
              return;
            default:
              Log.w("BleAddFriend_:", "onReceive: ?state");
              return;
          }
        }
      }
    };
    mConnection = new ServiceConnection() {
      @Override
      public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.i("BleAddFriend_:", "onServiceConnected");
        mService = ((BleManagerService.BleServiceBinder) iBinder).getService();
        if (mService != null) {
          mService.setHandler(new Handler() {
            @Override
            public void handleMessage(Message message) {
              super.handleMessage(message);
              Log.i("BleAddFriend_Finish", "handleMessage what==" + message.what);
              int i = message.what;
              if (i != ANIMATION_THREE_STEP) {
                switch (i) {
                  case 0:
                  case 1:
                    if (!BleUtils.b(act)) {
                      Log.i("BleAddFriend_:", "start bind service");
                      requestBluetoothFromSettings(act);
                      startBindService(act, mService, mConnection);
                      return;
                    } else {
                      Log.i("BleAddFriend_:", "register receiver");
                      act.registerReceiver(mBluetoothReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
                      return;
                    }
                  default:
                    switch (i) {
                      case 23:
                        Log.i("BleAddFriend_Finish", "0");
                        //clearTimeScheduler();
                        String str = (String) message.obj;
                        Log.i("BleAddFriend_Finish", "watchName=" + str);
                        if (TextUtils.isEmpty(str)) {
                          //showBleConnectView();
                          return;
                        }
                        /*
                        if (mTimeOfFail != null) {
                            mTimeOfFail.cancel();
                            mTimeOfFail = null;
                        }
                        */
                        //showBleConnectView(str);
                        return;
                      case 24:
                        //clearTimeScheduler();
                        //showAddFirendSuccessful(true);
                        Toast.makeText(act, "添加好友成功", Toast.LENGTH_SHORT).show();
                        return;
                      case 25:
                        //showNoWatchAroundView(true);
                        Toast.makeText(act, "附近没有手表", Toast.LENGTH_SHORT).show();
                        return;
                      case 26:
                        //clearTimeScheduler();
                        //showTouchFriendWatchAndAddSuccess();
                        return;
                      default:
                        Log.e("BleAddFriend_:", "unexpect value");
                        return;
                    }
                }
              }
            }
          });
        }
        if (mStatusInfo.getSessionStatus() == 0) {
          Log.i("BleAddFriend_:", "### Current Status Is Idle, Starte Scanning And Adverister ####");
          //startPeripheralAndCentral();
        }
      }

      @Override
      public void onServiceDisconnected(ComponentName componentName) {
        Log.i("BleAddFriend_:", "[onServiceDisconnected]");
        if (mService != null) {
          mService.setHandler((Handler) null);
          mService.stopPeripheralAndCentral();
          mService = null;
        }
      }
    };
  }

  private static boolean handleIntent(Activity act, Intent intent) {
    Log.d("BleAddFriend_:", "############## handleIntent ####################");
    int sessionStatus = mStatusInfo.getSessionStatus();
    Log.d("BleAddFriend_:", "handleIntent: runningStage :" + sessionStatus);
    if (sessionStatus != 7) {
      switch (sessionStatus) {
        case 0:
        case 1:
          if (BleUtils.b(act)) {
            Log.i("BleAddFriend_:", "start bind service");
            requestBluetoothFromSettings(act);
            startBindService(act, mService, mConnection);
            break;
          } else {
            Log.i("BleAddFriend_:", "register receiver");
            act.registerReceiver(mBluetoothReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
            hasRegisterReceiver = true;
            break;
          }
        case 2:
          Log.d("音频", "showBleConnectView() called！");
          //showBleConnectView();
          break;
        default:
          Log.e("BleAddFriend_:", "default");
          break;
      }
    } else {
      //clearTimeScheduler();
      //goToHomeActivity();
    }
    return true;
  }

  private static void startBindService(Activity act, BleManagerService bleManagerService, ServiceConnection serviceConnection) {
    if (hasStartPeripheralAndCentral) {
      return;
    }
    if (bleManagerService == null) {
      act.bindService(new Intent((Context) act, (Class < ? > ) BleManagerService.class), serviceConnection, 1);
    } else {
      bleManagerService.startPeripheralAndCentral();
      hasStartPeripheralAndCentral = true;
    }
  }
}