package com.example.cohalz.entropy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;


public class BluetoothActivity extends Normal {

    //1が1P,0が白,-1が2P,2が移動可能マス
    //private BluetoothAdapter mBtAdapter;
    private TextView mResultView;
    private ArrayAdapter<String> mServers;
    private ArrayAdapter<String> mCandidateServers;
    private View mView;
    private final Handler mHandler = new Handler();
    private Bt mBt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bluetooth);

        mResultView = (TextView)findViewById(R.id.bt_text);
        // インテントフィルタの作成
        //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // ブロードキャストレシーバの登録
        //registerReceiver(mBt.mReceiver, filter);
        mServers = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mCandidateServers = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mBt  = new Bt(this, mCandidateServers, mServers);
    }



    @Override
    protected void onResume() {
        super.onResume();
        mBt.turnOn();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBt.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_discoverable) {
            mBt.startDiscoverable();
            return true;
        }else if (id == R.id.menu_start_server) {
            mBt.startServer();
        }else if (id == R.id.menu_search_server) {
            mCandidateServers.clear();
            ListView lv = new ListView(this);
            lv.setAdapter(mCandidateServers);
            lv.setScrollingCacheEnabled(false);
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog)
                    .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mBt.cancelDiscovery();
                        }
                    })
                    .setView(lv)
                    .create();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> items, View view, int position, long id) {
                    dialog.dismiss();
                    String address = mCandidateServers.getItem(position);
                    if (mServers.getPosition(address) == -1) {
                        mServers.add(address);
                    }
                    mBt.cancelDiscovery();
                }
            });
            dialog.show();
            mBt.searchServer();
        }else if(id == R.id.menu_connect){
            ListView lv = new ListView(this);
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog)
                    .setPositiveButton(android.R.string.cancel, null)
                    .setView(lv)
                    .create();
            lv.setAdapter(mServers);
            lv.setScrollingCacheEnabled(false);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> items, View view, int position, long id) {
                    dialog.dismiss();
                    String address = mServers.getItem(position);
                    mBt.connect(address);
                }
            });
            dialog.show();
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mBt.onActivityResult(requestCode, resultCode, data);
    }

    public void invalidate() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mView.invalidate();
            }
        });
    }

}
