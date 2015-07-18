package com.example.cohalz.entropy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ryu on 2015/05/17.
 */
public class Bt {
    private abstract class ReceiverThread extends Thread {
        protected BluetoothSocket mSocket;

        protected void sendMessage(String message) throws IOException {
            OutputStream os = mSocket.getOutputStream();
            os.write(message.getBytes());
            os.write("\n".getBytes());
            //activity.log.setText(message);
        }

        protected void loop() throws IOException {
            activity.invalidate();
            BufferedReader br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            String message;
            while ((message = br.readLine()) != null) {
                activity.receiveMessage(message);
                activity.invalidate();
            }
        }
    }

    private class ServerThread extends ReceiverThread {
        private BluetoothServerSocket mServerSocket;


        private ServerThread() {
            try {
                mServerSocket = Bt.listenUsingRfcommWithServiceRecord(activity.getPackageName(), APP_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {

                Log.d(TAG, "accepting...");
                mSocket = mServerSocket.accept();
                Log.d(TAG, "accepted");
                activity.turn = 0;
                loop();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                cancel();
            }
        }

        private void cancel() {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class ClientThread extends ReceiverThread {
        private final BluetoothDevice mServer;

        private ClientThread(String address) {
            mServer = Bt.getRemoteDevice(address);
            try {
                mSocket = mServer.createRfcommSocketToServiceRecord(APP_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            // connect() の前にデバイス検出をやめる必要がある
            Bt.cancelDiscovery();
            try {
                // サーバに接続する
                mSocket.connect();
                activity.turn = 1;
                loop();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                cancel();
            }

        }

        private void cancel() {
            //mBoard = null;
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static final UUID APP_UUID = UUID.fromString("a1e044cc-0c61-4abf-800c-0a9afd23e16c");

    private static final int REQUEST_ENABLE_BLUETOOTH = 1234;
    private static final int REQUEST_DISCOVERABLE_BT = 5678;
    private static final int DURATION = 300;
    private BluetoothActivity activity;
    //BluetoothAdapter取得
    BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
    private ServerThread mServerThread;
    private ClientThread mClientThread;
    private final ArrayAdapter<String> mCandidateServers;
    private final String TAG = getClass().getSimpleName();



    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d(TAG, "ACTION_FOUND");
                // デバイスが見つかった場合、Intent から BluetoothDevice を取り出す
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 名前とアドレスを所定のフォーマットで ArrayAdapter に格納
                mCandidateServers.add(device.getName() + "\n" + device.getAddress());
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d(TAG, "ACTION_DISCOVERY_FINISHED");
                // デバイス検出が終了した場合は、BroadcastReceiver を解除
                context.unregisterReceiver(mReceiver);
            }
        }
    };
    public Bt(BluetoothActivity context, ArrayAdapter<String> candidateServers, ArrayAdapter<String> servers) {
        activity = context;
        mCandidateServers = candidateServers;
        Set<BluetoothDevice> devices = Bt.getBondedDevices();
        for (BluetoothDevice device : devices) {
            servers.add(device.getName() + "\n" + device.getAddress());
        }
    }


    public void turnOn() {
        if (!Bt.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
        }
    }

    protected void onActivityResult(int requestCode, int ResultCode, Intent date) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (ResultCode == RESULT_OK) {
                //BluetoothがONにされた場合の処理
                //activity.errorDialog("BluetoothをONにしてもらえました。");
            } else {
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

    public void cancelDiscovery() {
        Bt.cancelDiscovery();
    }
    public void startServer() {
        if (mServerThread != null) {
            mServerThread.cancel();
        }
        mServerThread = new ServerThread();
        mServerThread.start();
    }

    public void searchServer() {
        mCandidateServers.clear();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        activity.registerReceiver(mReceiver, filter);
        Bt.startDiscovery();
    }
    public void connect(String address) {
        int index;
        if ((index = address.indexOf("\n")) != -1) {
            address = address.substring(index + 1);
        }
        // クライアント用のスレッドを生成
        mClientThread = new ClientThread(address);
        mClientThread.start();
    }

    public void sendMessage(String message) {
        try {
            activity.test1();
            if (mServerThread != null) {
                mServerThread.sendMessage(message);
            }
            if (mClientThread != null) {
                mClientThread.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void cancel() {
        if (mServerThread != null) {
            mServerThread.cancel();
            mServerThread = null;
        }
        if (mClientThread != null) {
            mClientThread.cancel();
            mClientThread = null;
        }
    }

}
