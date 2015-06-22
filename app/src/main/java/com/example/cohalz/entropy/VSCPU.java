package com.example.cohalz.entropy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.LinkedList;


public class VSCPU extends Normal {
    int MAXCOUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onClick(View v){
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (v == view[y][x]) {
                    Log.i("v", "x:" + x + ", y:" + y + ", flag:" + flag + ", ban:" + ban);
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
                            if (isClear(0, board)) {
                                Intent intent = new Intent(this, p1win.class);
                                startActivity(intent);
                                // clear(ban);
                                //return;
                            } else if (isClear((ban + 1) % 2, board)) {
                                clear((ban + 1) % 2,board);
                                return;

                            } else {
                                ban = (ban + 1) % 2;
                                if (isPass(ban,board)) ban = (ban + 1) % 2;
                                if (isPass(ban,board)) { //ふたりともパスならDrawだが一人だけパスでもなぜか呼ばれることがある
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
                        display(board);
                        if(ban == 1 && flag == 0) {
                            display(board);
                            Log.i("v", alfabeta(board,ban,MAXCOUNT)+"tendayo");
                        }

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

    public LinkedList<Integer> movableList(int ban, int[][] board){
        LinkedList<Integer> list = new LinkedList<>();
        for (int y = 0;y < 5; y++){
            for(int x = 0;x < 5;x++){
                if(board[y][x] == ban && isTouched(x, y, ban, board) && movable(x, y, ban, board) > 0) {
                    list.addAll(addMovable(x,y,board));
                    movableShowReset(board);
                }
            }
        }
        movableShowReset(board);
        return list;
    }

    public LinkedList<Integer> addMovable(int x, int y, int[][] board) {
        LinkedList<Integer> list = new LinkedList<>();
        list.addAll(addVec(x, y, 1, 1, board));
        list.addAll(addVec(x, y, 1, 0, board));
        list.addAll(addVec(x, y, 1, -1, board));
        list.addAll(addVec(x, y, 0, 1, board));
        list.addAll(addVec(x, y, 0, -1, board));
        list.addAll(addVec(x, y, -1, 1, board));
        list.addAll(addVec(x, y, -1, 0, board));
        list.addAll(addVec(x, y, -1, -1, board));
        return list;
    }

    public LinkedList<Integer> addVec(int x, int y, int xVec, int yVec, int[][] board) {
        int xi = x + xVec;
        int yi = y + yVec;
        LinkedList<Integer> list = new LinkedList<>();
        while (xi < 5 && xi >= 0 && yi < 5 && yi >= 0 && board[yi][xi] != p1 && board[yi][xi] != p2) {
            if(board[yi][xi] == movable){
                list.add(x);
                list.add(y);
                list.add(xi);
                list.add(yi);
            }
            yi += yVec;
            xi += xVec;
        }
        return list;
    }

    public int eval(int ban,int[][] board){
        LinkedList<Integer> list = movableList(ban, board);
        int count = 0;
        for(int y = 0;y < 5;y++){
            for(int x = 0; x < 5; x++){
                if(board[y][x] == ban){
                    if(!isTouched(x,y,1,board)) count += 3;
                    if(isTouched(x,y,0,board)) count++;
               //     if(valueBoard[y][x] == 5) count += 1;
                }

            }
        }
        return 10 * count;
    }

    public int alfabeta(int[][] board, int ban, int count){
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int maxprevx = 0;
        int maxprevy = 0;
        int maxx = 0;
        int maxy = 0;
        int val;
        int[][] copyBoard = new int[5][5];
        if(isClear(1,board)) return 1000;
        if(isClear(0,board)) return -1000;
        for(int i = 0;i < 5;i++){
                copyBoard[i] = board[i].clone();
        }
        if(count == 0) return eval(ban,copyBoard);
        LinkedList<Integer> list = movableList(ban, copyBoard);
        while(!list.isEmpty()){
            for(int j = 0;j < 5;j++){
                copyBoard[j] = board[j].clone();
            }
            int prevx = list.remove();
            int prevy = list.remove();
            int x = list.remove();
            int y = list.remove();
            int tmp = copyBoard[prevy][prevx];
            copyBoard[prevy][prevx] = copyBoard[y][x];
            copyBoard[y][x] = tmp;
            val = alfabeta(copyBoard,(ban+1)%2,count-1);
            Log.i("v",val+"");
            if(ban == 1 && val > max) {
                max = val;
                maxprevx = prevx;
                maxprevy = prevy;
                maxx = x;
                maxy = y;
            }
            if(ban == 0 && val < min) {
                min = val;
                maxprevx = prevx;
                maxprevy = prevy;
                maxx = x;
                maxy = y;
            }
        }
        //最も浅い部分で最大値を取得しその場所をクリックさせる

        if(count == MAXCOUNT){
            view[maxprevy][maxprevx].performClick();
            view[maxy][maxx].performClick();
        }

        if(ban == 1) return max;
        else return min;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vscpu, menu);
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
