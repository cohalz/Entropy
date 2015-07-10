package com.example.cohalz.entropy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.StringTokenizer;


public class BluetoothActivity extends Normal {

    //1が1P,0が白,-1が2P,2が移動可能マス
    //private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mServers;
    private ArrayAdapter<String> mCandidateServers;
    private View mView;
    private final Handler mHandler = new Handler();
    private Bt mBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //mResultView = (TextView)findViewById(R.id.bt_text);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mBt.onActivityResult(requestCode, resultCode, data);
    }

    public void invalidate() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                display(board);
                if (ban == 1)
                    status.setText("2P");
                else
                    status.setText("1P");
            }
        });
    }
    public void receiveMessage(String message) {
        StringTokenizer st = new StringTokenizer(message, ",");
        int newx = Integer.parseInt(st.nextToken());
        int newy = Integer.parseInt(st.nextToken());
        int prex = Integer.parseInt(st.nextToken());
        int prey = Integer.parseInt(st.nextToken());
        int turn = Integer.parseInt(st.nextToken());
        //status.setText(message);
        move(newx, newy, prex, prey, turn, board);
    }
    public void test() {
        log.setText("test1");
    }
    public void test2() {
        log.setText("test2");
    }
    public void test3() {
        log.setText("test3");
    }
    public void move(int newx, int newy, int prex, int prey, int turn, int[][] board){
        board[prey][prex] = -1;
        board[newy][newx] = turn;
        ban = (turn + 1) % 2;
    }
    @Override
    public void onClick(View v) {

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (v == view[y][x]) {
                    Log.i("v", "x:" + x + ", y:" + y + ", flag:" + flag);
                    if (flag == 0 && board[y][x] == ban && ban == mBt.turn) {
                        if (isTouched(x, y, ban, board)) {

                            if (movable(x, y, ban, board) > 0) {
                                flag = 1;
                                pastx = x;
                                pasty = y;
                            }
                        }
                    } else if (flag == 1) {
                        aloneClear(board);
                        if (board[y][x] == 2) {
                            board[pasty][pastx] = -1;
                            board[y][x] = ban;
                            String message = String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(pastx) + "," + String.valueOf(pasty) + "," + String.valueOf(ban);
                            if (message != null) {
                                invalidate();
                                mBt.sendMessage(message);
                            }
                            if (isClear(ban, board)) {
                                clear(ban, board);
                                return;
                            } else if (isClear((ban + 1) % 2, board)) {
                                clear((ban + 1) % 2, board);
                                return;

                            } else {
                                ban = (ban + 1) % 2;
                                if (isPass(ban, board)) ban = (ban + 1) % 2;
                                if (isPass(ban, board)) { //ふたりともパスならDrawだが一人だけパスでもなぜか呼ばれることがある
                                    movableShowReset(board);
                                    status.setText("draw");
                                    flag = 3;
                                } else if (ban == 1)
                                    status.setText("2P");
                                else
                                    status.setText("1P");
                            }
                        }
                        movableShowReset(board);
                        flag = 0;

                    } else if (flag == 2) {
                        movableShowReset(board);
                        status.setText(Integer.toString(ban + 1) + "P Win!");
                    } else if (flag == 3) {
                        movableShowReset(board);
                        status.setText("draw");
                    }
                }
            }
        }
        //display(board);
        invalidate();
    }

}
