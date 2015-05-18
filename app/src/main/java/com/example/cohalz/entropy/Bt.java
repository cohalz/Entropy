package com.example.cohalz.entropy;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import java.util.UUID;

/**
 * Created by ryu on 2015/05/17.
 */
public class Bt {

    private static final UUID APP_UUID = UUID.fromString("a1e044cc-0c61-4abf-800c-0a9afd23e16c");

    private final static int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final int REQUEST_DISCOVERABLE_BT = 5678;
    private static final int DURATION = 300;
    private MainActivity activity;
    //BluetoothAdapter取得
    private BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice BtDevice = null;
    private BluetoothSocket BtSocket;


    public Bt(MainActivity activity) {
        this.activity = activity;
    }

    protected void turnOn() {
        boolean btEnable = Bt.isEnabled();
        if(btEnable == true){
            //BluetoothがONだった場合の処理
        }else{
            //OFFだった場合、ONにすることを促すダイアログを表示する画面に遷移
            Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);
        }
    }

    protected void onActivityResult(int requestCode, int ResultCode, Intent date){
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(ResultCode == Activity.RESULT_OK){
                //BluetoothがONにされた場合の処理
                //activity.errorDialog("BluetoothをONにしてもらえました。");
            }else{
                //activity.errorDialog( "BluetoothをONにしてもらえませんでした。");
                activity.finish();
            }
        }
    }
    public void startDiscoverable() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DURATION);
        activity.startActivityForResult(intent, REQUEST_DISCOVERABLE_BT);
    }

    

}
