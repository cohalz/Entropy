package com.example.cohalz.entropy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;


public class Normal extends ActionBarActivity {

    int p1image = R.drawable.tiger;
    int p2image = R.drawable.dragon;
    int blankimage = R.drawable.white;
    int movableimage = R.drawable.gray;

    int flag = 0;
    int blank = -1;
    int p1 = 0;
    int p2 = 1;
    int movable = 2;
    int pastx, pasty;
    int ban = 0;
    ImageView view[][] = new ImageView[5][5];
    TextView status;
    int board[][] = new int[5][5]; //盤面を記憶する
    //0が1P,-1が白,1が2P,2が移動可能マス


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("v", "a");
        setContentView(R.layout.activity_normal);
        Log.i("v", "a");
        view[0][0] = (ImageView) findViewById(R.id.imageView0);
        view[0][1] = (ImageView) findViewById(R.id.imageView1);
        view[0][2] = (ImageView) findViewById(R.id.imageView2);
        view[0][3] = (ImageView) findViewById(R.id.imageView3);
        view[0][4] = (ImageView) findViewById(R.id.imageView4);
        view[1][0] = (ImageView) findViewById(R.id.imageView5);
        view[1][1] = (ImageView) findViewById(R.id.imageView6);
        view[1][2] = (ImageView) findViewById(R.id.imageView7);
        view[1][3] = (ImageView) findViewById(R.id.imageView8);
        view[1][4] = (ImageView) findViewById(R.id.imageView9);
        view[2][0] = (ImageView) findViewById(R.id.imageView10);
        view[2][1] = (ImageView) findViewById(R.id.imageView11);
        view[2][2] = (ImageView) findViewById(R.id.imageView12);
        view[2][3] = (ImageView) findViewById(R.id.imageView13);
        view[2][4] = (ImageView) findViewById(R.id.imageView14);
        view[3][0] = (ImageView) findViewById(R.id.imageView15);
        view[3][1] = (ImageView) findViewById(R.id.imageView16);
        view[3][2] = (ImageView) findViewById(R.id.imageView17);
        view[3][3] = (ImageView) findViewById(R.id.imageView18);
        view[3][4] = (ImageView) findViewById(R.id.imageView19);
        view[4][0] = (ImageView) findViewById(R.id.imageView20);
        view[4][1] = (ImageView) findViewById(R.id.imageView21);
        view[4][2] = (ImageView) findViewById(R.id.imageView22);
        view[4][3] = (ImageView) findViewById(R.id.imageView23);
        view[4][4] = (ImageView) findViewById(R.id.imageView24);
        status = (TextView) findViewById(R.id.status);
        imageSetUp();
        toBoard();
    }

    public void onClick(View v) {

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (v == view[y][x]) {
                    Log.i("v", "x:" + x + ", y:" + y + ", flag:" + flag);
                    if (flag == 0 && board[y][x] == ban) {
                        if (isTouched(x, y, ban, board)) {

                            if (movable(x, y, ban, board) > 0) {
                                flag = 1;
                                pastx = x;
                                pasty = y;
                            }
                        }
                    } else if (flag == 1) {
                        aloneClear(board);
                        if (board[y][x] == movable) {
                            board[pasty][pastx] = blank;
                            board[y][x] = ban;
                            if (isClear(0,board)) {
                                Intent intent = new Intent(this, p1win.class);
                                startActivity(intent);
                               // clear(ban);
                                //return;
                            } else if (isClear(1,board)) {
                                Intent intent = new Intent(this, p2win.class);
                                startActivity(intent);
                                return;

                            } else {
                                ban = (ban + 1) % 2;
                                if (isPass(ban,board)) ban = (ban + 1) % 2;
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
        display(board);

    }
    // 画面回転でリセットしちゃうのでなんとかする

    //引数の地点からどこに移動できるかのフラグを作成する
    //戻り値は移動できる場所の数
    public int movable(int x, int y, int ban, int[][] board) {
        int count = 0;
        int flag = 0;
        if (isContainsAlone(ban, board)) {
            createAloneFlag(ban, board);
            flag = 1;
        }
        count += countVec(x, y, flag, 1, 1, ban, board);
        count += countVec(x, y, flag, 1, 0, ban, board);
        count += countVec(x, y, flag, 1, -1, ban, board);
        count += countVec(x, y, flag, 0, 1, ban, board);
        count += countVec(x, y, flag, 0, -1, ban, board);
        count += countVec(x, y, flag, -1, 1, ban, board);
        count += countVec(x, y, flag, -1, 0, ban, board);
        count += countVec(x, y, flag, -1, -1, ban, board);
        return count;
    }

    //x,y座標と向きを指定して動ける場所を色変える
    //戻り値は動ける場所の数
    public int countVec(int x, int y, int flag, int xVec, int yVec, int ban,int[][] board) {
        int xi = x + xVec;
        int yi = y + yVec;
        int count = 0;
        while (xi < 5 && xi >= 0 && yi < 5 && yi >= 0 && board[yi][xi] != p1 && board[yi][xi] != p2) {
            if (flag == 1) {
                if (board[yi][xi] == ban + 3){
                    count++;
                    board[yi][xi] = movable;
                }
            } else {
                board[yi][xi] = movable;
                count++;
            }
            yi += yVec;
            xi += xVec;
        }
        return count;
    }

    public boolean isPass(int ban, int[][] board) {
        int count = 0;
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (board[y][x] == ban)
                    if (isTouched(x, y, ban, board))
                        count += movable(x, y,ban, board);
            }
        }
        movableShowReset(board);
        return count == 0;
    }

    public void movableShowReset(int[][] board) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] >= movable)
                    board[i][j] = blank;
            }
        }
    }

    public void createAloneFlag(int ban, int[][] board) {
        LinkedList<Integer> aloneList = new LinkedList<>();
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (isAlone(x, y,board)) {
                    aloneList.add(x);
                    aloneList.add(y);
                    //最初は普通に色塗る
                    //二回目以降は色が塗ってあるところの数字を増やしてそれ以外はリセット
                    //数字を元に戻すのを繰り返す
                }
            }

        }
        for (int i = 0; i < aloneList.size()/2; i++) {
            int x = aloneList.remove();
            int y = aloneList.remove();
            if (i == 0) {
                changeAroundColor(x, y, -1, ban + 3, board);

            } else {
                changeAroundColor(x, y, 3 + ban, 5 + ban, board);
                countDown(ban,board);
            }
        }
    }

    public void clear(int ban, int[][] board) {
        flag = 2;
        movableShowReset(board);
        status.setText(Integer.toString(ban + 1) + "P Win!");
        display(board);
    }

    public boolean isAlone(int x, int y, int[][] board) {
        return !isTouched(x, y, p1, board) && !isTouched(x, y, p2, board);
    }

    public boolean isContainsAlone(int ban, int[][] board) {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (isAlone(x, y, board) && board[y][x] == ban) return true;
            }
        }
        return false;
    }

    public boolean isClear(int ban, int[][] board) {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (board[y][x] == ban) {
                    if (isTouched(x, y, ban, board)) return false;
                    if (!isTouched(x, y, (ban + 1) % 2, board)) return false;
                }
            }
        }
        return true;
    }


    public boolean isTouched(int x, int y, int ban, int[][] board) {
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

    public void changeAroundColor(int x, int y, int from, int to, int[][] board) {
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

    public void countDown(int ban,int[][] board) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] == 3 + ban) board[i][j] = blank;
                if (board[i][j] == 3 + ban + 2) board[i][j] -= 2;

            }
        }
    }

    public void display(int[][] board) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] == p1) view[i][j].setImageResource(p1image);
                else if (board[i][j] == p2) view[i][j].setImageResource(p2image);
                else if (board[i][j] == blank) view[i][j].setImageResource(blankimage);
                else if (board[i][j] == movable) view[i][j].setImageResource(movableimage);
            }
        }
    }

    public void aloneClear(int[][] board) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] >= 3) board[i][j] = movable;
            }
        }
    }

    public void imageSetUp(){
        view[0][0].setTag(p1image);
        view[0][1].setTag(p1image);
        view[0][2].setTag(p1image);
        view[0][3].setTag(p1image);
        view[0][4].setTag(p1image);
        view[1][0].setTag(p1image);
        view[1][1].setTag(blankimage);
        view[1][2].setTag(blankimage);
        view[1][3].setTag(blankimage);
        view[1][4].setTag(p1image);
        view[2][0].setTag(blankimage);
        view[2][1].setTag(blankimage);
        view[2][2].setTag(blankimage);
        view[2][3].setTag(blankimage);
        view[2][4].setTag(blankimage);
        view[3][0].setTag(p2image);
        view[3][1].setTag(blankimage);
        view[3][2].setTag(blankimage);
        view[3][3].setTag(blankimage);
        view[3][4].setTag(p2image);
        view[4][0].setTag(p2image);
        view[4][1].setTag(p2image);
        view[4][2].setTag(p2image);
        view[4][3].setTag(p2image);
        view[4][4].setTag(p2image);
        for(int i = 0; i < 25;i++){
            view[i/5][i%5].setImageResource((int)view[i/5][i%5].getTag());
        }
    }

    public void toBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Object image = view[i][j].getTag();
                int id = image == null ? -1 : (int) image;
                Log.i("v",image+" "+p1image);
                if (id == p1image) board[i][j] = p1;
                if (id == p2image) board[i][j] = p2;
                if (id == blankimage) board[i][j] = blank;
                if (id == movableimage) board[i][j] = movable;
            }
        }
    }


}
