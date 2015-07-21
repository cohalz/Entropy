package com.example.cohalz.entropy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.StringTokenizer;

import static com.example.cohalz.entropy.Constants.FROM;


public class BluetoothActivity extends Normal {

    //1が1P,0が白,-1が2P,2が移動可能マス
    //private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mServers;
    private ArrayAdapter<String> mCandidateServers;
    private View mView;
    private final Handler mHandler = new Handler();
    private Bt mBt;

    public BtPlayer p1 = new BtPlayer(1, true,this,p1win.class);
    public BtPlayer p2 = new BtPlayer(0, false,this,p2win.class);
    public BtPlayer[] players = new BtPlayer[]{p1, p2};

    int fromx, fromy, tox, toy;
    public int turn;


    //public BluetoothActivity() {
    //  p1 = new BtPlayer(0, true, mBt, this);
    //  p2 = new BtPlayer(1,false, mBt, this);
    //  players = new BtPlayer[]{p1, p2};
    //}

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
        mBt = new Bt(this, mCandidateServers, mServers);
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
        } else if (id == R.id.menu_start_server) {
            mBt.startServer();
        } else if (id == R.id.menu_search_server) {
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
        } else if (id == R.id.menu_connect) {
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

    @Override
    public void onClick(View v) {
        int x = 0, y;

        for_label:
        for (y = 0; y < 5; y++)
            for (x = 0; x < 5; x++)
                if (v == board.viewBoard.view[y][x]) break for_label;


        for (int i = 0; i < 2; i++) {
            if (players[i].ban && players[i].number == turn) {
                Point point = new Point(x, y);
                if (players[i].state == FROM) {
                    players[i].doFromClick(board, point);
                    fromx = x;
                    fromy = y;
                } else {
                    p2.Mflag = false;
                    p1.Mflag = false;
                    players[i].doToClick(players[(i + 1) % 2], board, point);
                    tox = x;
                    toy = y;
                    String message =
                            String.valueOf(fromx) + "," +
                                    String.valueOf(fromy) + "," +
                                    String.valueOf(tox) + "," +
                                    String.valueOf(toy) + "," +
                                    String.valueOf(i);
                    if(p1.Mflag || p2.Mflag) {
                        mBt.sendMessage(message);
                    }
                    invalidate();

                }
                break;
            }
        }
    }

    public void invalidate() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                board.display();
                for (int i = 0; i < 2; i++) {
                    if (players[i].ban) {
                        status.setText(players[i].number + 1 + "P");
                    }
                }
            }
        });
    }

    public void receiveMessage(final String message) {
        StringTokenizer st = new StringTokenizer(message, ",");
        final Point from = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        final Point to = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        int ban = Integer.parseInt(st.nextToken());

        //move(newx, newy, prex, prey, turn, board);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                board.move(new Move(from, to));
            }
        });
        //番を変更
        players[1].allChangeBan(players[0]);
        invalidate();
    }
//>>>>>>> 24597fa64c416991fd808646ac774d5774407279
}
