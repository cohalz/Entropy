package com.example.cohalz.entropy;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;


public class Normal extends ActionBarActivity {

    int flag = 0;
    LinkedList<Integer> alonexlist;
    LinkedList<Integer> aloneylist;
    String ps[] = new String[2];
    int pastx, pasty;
    String gray = "#eeeeee";
    //String p2 = "#ff57ff6a";
    //String p = p1;
    int ban = 0;
    String white = "#ffffff";
    String red = "#ff0000";
    TextView view[][] = new TextView[5][5];
    TextView status;
    int board[][] = new int[5][5]; //盤面を記憶する
    //0が1P,-1が白,1が2P,2が移動可能マス


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alonexlist = new LinkedList<Integer>();
        aloneylist = new LinkedList<Integer>();
        ps[0] = "#ff48fffd";
        ps[1] = "#ff57ff6a";
        view[0][0] = (TextView) findViewById(R.id.textView0);
        view[0][1] = (TextView) findViewById(R.id.textView1);
        view[0][2] = (TextView) findViewById(R.id.textView2);
        view[0][3] = (TextView) findViewById(R.id.textView3);
        view[0][4] = (TextView) findViewById(R.id.textView4);
        view[1][0] = (TextView) findViewById(R.id.textView5);
        view[1][1] = (TextView) findViewById(R.id.textView6);
        view[1][2] = (TextView) findViewById(R.id.textView7);
        view[1][3] = (TextView) findViewById(R.id.textView8);
        view[1][4] = (TextView) findViewById(R.id.textView9);
        view[2][0] = (TextView) findViewById(R.id.textView10);
        view[2][1] = (TextView) findViewById(R.id.textView11);
        view[2][2] = (TextView) findViewById(R.id.textView12);
        view[2][3] = (TextView) findViewById(R.id.textView13);
        view[2][4] = (TextView) findViewById(R.id.textView14);
        view[3][0] = (TextView) findViewById(R.id.textView15);
        view[3][1] = (TextView) findViewById(R.id.textView16);
        view[3][2] = (TextView) findViewById(R.id.textView17);
        view[3][3] = (TextView) findViewById(R.id.textView18);
        view[3][4] = (TextView) findViewById(R.id.textView19);
        view[4][0] = (TextView) findViewById(R.id.textView20);
        view[4][1] = (TextView) findViewById(R.id.textView21);
        view[4][2] = (TextView) findViewById(R.id.textView22);
        view[4][3] = (TextView) findViewById(R.id.textView23);
        view[4][4] = (TextView) findViewById(R.id.textView24);
        status = (TextView) findViewById(R.id.status);
        toBoard();
    }

    public void onClick(View v) {

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (v == view[y][x]) {
                    Log.i("v", "x:" + x + ", y:" + y + ", flag:" + flag);
                    if (flag == 0 && board[y][x] == ban) {
                        if (isTouched(x, y, ban)) {

                            if (movable(x, y) > 0) {
                                flag = 1;
                                pastx = x;
                                pasty = y;
                            }
                        }
                    } else if (flag == 1) {
                        aloneClear();
                        if (board[y][x] == 2) {
                            board[pasty][pastx] = -1;
                            board[y][x] = ban;
                            if (isClear(ban)) {
                                clear(ban);
                                return;
                            } else if (isClear((ban + 1) % 2)) {
                                clear((ban + 1) % 2);
                                return;

                            } else {
                                ban = (ban + 1) % 2;
                                if (isPass()) ban = (ban + 1) % 2;
                                if (isPass()) { //ふたりともパスならDrawだが一人だけパスでもなぜか呼ばれることがある
                                    movableShowReset();
                                    status.setText("draw");
                                    flag = 3;
                                } else if (ban == 1)
                                    status.setText("2P");
                                else
                                    status.setText("1P");
                            }
                        }
                        movableShowReset();
                        flag = 0;

                    } else if (flag == 2) {
                        movableShowReset();
                        status.setText(Integer.toString(ban + 1) + "P Win!");
                    } else if (flag == 3) {
                        movableShowReset();
                        status.setText("draw");
                    }
                }
            }
        }
        display();

    }
    // 画面回転でリセットしちゃうのでなんとかする

    //引数の地点からどこに移動できるかのフラグを作成する
    //戻り値は移動できる場所の数
    public int movable(int x, int y) {
        int count = 0;
        int flag = 0;
        if (isContainsAlone()) {
            createAloneFlag();
            flag = 1;
        }
        count += countVec(x, y, flag, 1, 1);
        count += countVec(x, y, flag, 1, 0);
        count += countVec(x, y, flag, 1, -1);
        count += countVec(x, y, flag, 0, 1);
        count += countVec(x, y, flag, 0, -1);
        count += countVec(x, y, flag, -1, 1);
        count += countVec(x, y, flag, -1, 0);
        count += countVec(x, y, flag, -1, -1);
        return count;
    }

    //x,y座標と向きを指定して動ける場所を色変える
    //戻り値は動ける場所の数
    public int countVec(int x, int y, int flag, int xVec, int yVec) {
        int xi = x + xVec;
        int yi = y + yVec;
        int count = 0;
        while (xi < 5 && xi >= 0 && yi < 5 && yi >= 0 && board[yi][xi] != 0 && board[yi][xi] != 1) {
            if (flag == 1) {
                if (board[yi][xi] == ban + 3)
                    board[yi][xi] = 2;
                yi += yVec;
                xi += xVec;
                count++;
            } else {
                board[yi][xi] = 2;
                yi += yVec;
                xi += xVec;
                count++;
            }
        }
        return count;
    }

    public boolean isPass() {
        int count = 0;
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (board[y][x] == ban)
                    if (isTouched(x, y, ban))
                        count += movable(x, y);
            }
        }
        movableShowReset();
        return count == 0;
    }

    public void movableShowReset() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] >= 2)
                    board[i][j] = -1;
            }
        }
    }

    public void createAloneFlag() {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (isAlone(x, y)) {
                    alonexlist.add(x);
                    aloneylist.add(y);
                    //最初は普通に色塗る
                    //二回目以降は色が塗ってあるところの数字を増やしてそれ以外はリセット
                    //数字を元に戻すのを繰り返す
                }
            }

        }
        for (int i = 0; i < alonexlist.size(); i++) {
            int x = alonexlist.remove();
            int y = aloneylist.remove();
            if (i == 0) {
                changeAroundColor(x, y, -1, ban + 3);

            } else {
                changeAroundColor(x, y, 3 + ban, 5 + ban);
                countDown();
            }
        }
    }

    public void clear(int ban) {
        flag = 2;
        movableShowReset();
        status.setText(Integer.toString(ban + 1) + "P Win!");
        display();
    }

    public boolean isAlone(int x, int y) {
        return !isTouched(x, y, 1) && !isTouched(x, y, 0);
    }

    public boolean isContainsAlone() {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (isAlone(x, y) && board[y][x] == ban) return true;
            }
        }
        return false;
    }

    public boolean isClear(int ban) {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (board[y][x] == ban) {
                    if (isTouched(x, y, ban)) return false;
                    if (!isTouched(x, y, (ban + 1) % 2)) return false;
                }
            }
        }
        return true;
    }


    public boolean isTouched(int x, int y, int ban) {
        if (y > 0) {
            if (board[y - 1][x] == ban) return true;
            if (x > 0) {
                if (board[y - 1][x - 1] == ban) return true;
            }
            if (x < 4) {
                if (board[y - 1][x + 1] == ban) return true;
            }
        }
        if (y < 4) {
            if (board[y + 1][x] == ban) return true;
            if (x > 0) {
                if (board[y + 1][x - 1] == ban) return true;
            }
            if (x < 4) {
                if (board[y + 1][x + 1] == ban) return true;
            }
        }
        if (x > 0) {
            if (board[y][x - 1] == ban) return true;
        }
        if (x < 4) {
            if (board[y][x + 1] == ban) return true;
        }
        return false;
    }

    public void changeAroundColor(int x, int y, int from, int to) {
        if (y > 0) {
            if (board[y - 1][x] == from) board[y - 1][x] = to;
            if (x > 0) {
                if (board[y - 1][x - 1] == from) board[y - 1][x - 1] = to;
            }
            if (x < 4) {
                if (board[y - 1][x + 1] == from) board[y - 1][x + 1] = to;
            }
        }
        if (y < 4) {
            if (board[y + 1][x] == from) board[y + 1][x] = to;
            if (x > 0) {
                if (board[y + 1][x - 1] == from) board[y + 1][x - 1] = to;
            }
            if (x < 4) {
                if (board[y + 1][x + 1] == from) board[y + 1][x + 1] = to;
            }
        }
        if (x > 0) {
            if (board[y][x - 1] == from) board[y][x - 1] = to;
        }
        if (x < 4) {
            if (board[y][x + 1] == from) board[y][x + 1] = to;
        }
    }

    public void countDown() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] == 3 + ban) board[i][j] = -1;
                if (board[i][j] == 3 + ban + 2) board[i][j] -= 2;

            }
        }
    }

    public void display() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] == 0) view[i][j].setBackgroundColor(Color.parseColor(ps[0]));
                else if (board[i][j] == 1) view[i][j].setBackgroundColor(Color.parseColor(ps[1]));
                else if (board[i][j] == -1) view[i][j].setBackgroundColor(Color.parseColor(white));
                else if (board[i][j] == 2) view[i][j].setBackgroundColor(Color.parseColor(gray));
            }
        }
    }

    public void aloneClear() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] >= 3) board[i][j] = 2;
            }
        }
    }

    public void toBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int color = ((ColorDrawable) view[i][j].getBackground()).getColor();
                if (color == Color.parseColor(ps[0])) board[i][j] = 0;
                if (color == Color.parseColor(ps[1])) board[i][j] = 1;
                if (color == Color.parseColor(white)) board[i][j] = -1;
                if (color == Color.parseColor(gray)) board[i][j] = 2;
            }
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
