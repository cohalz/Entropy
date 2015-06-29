package com.example.cohalz.entropy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.LinkedList;



public class VSCPU extends Normal {
    int MAXCOUNT = 3;
    int bprex,bprey,bx,by;

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
                                if (isPass(ban,board)) {
                                    movableShowReset(board);
                                    display(board);
                                    ban = (ban + 1) % 2;
                                    flag = 0;
                                    if(ban == 1){
                                        alfabeta(board, ban, MAXCOUNT, Integer.MIN_VALUE, Integer.MAX_VALUE);
                                        return;
                                    }
                                    if(isPass(ban, board)){
                                        status.setText("draw");
                                        flag = 3;
                                    }
                                } else
                                    setText(ban);
                            }
                        }
                        movableShowReset(board);
                        flag = 0;
                        display(board);
                        if(ban == 1 && flag == 0) {
                            display(board);
                            alfabeta(board, ban, MAXCOUNT, Integer.MIN_VALUE, Integer.MAX_VALUE);
                            return;
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
        int count = 0;
        for(int y = 0;y < 5;y++){
            for(int x = 0; x < 5; x++){
                if(board[y][x] == ban){

                    //if(!isTouched(x,y,(ban),board)) count += 3;
                    //if(isTouched(x,y,(ban+1)%2,board)) count++;
                }

            }
        }
        return movableList((ban+1)%2, board).size();
    }

    public int minimax(int[][] board, int ban, int count){
        return alfabeta(board,ban,count,Integer.MIN_VALUE,Integer.MAX_VALUE);
    }

    public int alfabeta(int[][] board, int ban, int count,int alfa,int beta){
        int max = Integer.MIN_VALUE;
        int maxprevx = -1;
        int maxprevy = -1;
        int maxx = -1;
        int maxy = -1;
        int prevx = -1;
        int prevy = -1;
        int x = -1;
        int y = -1;
        int val;
        int[][] copyBoard = new int[5][5];
        for(int i = 0;i < 5;i++){
                copyBoard[i] = board[i].clone();
        }
        if(count == 0) return eval(ban,copyBoard);
        LinkedList<Integer> list = movableList(ban, copyBoard);
        if(isClear(1, board)) {
            return 1000;
        }
        if(isClear(0, board)) {
            return -1000;
        }
        while(!list.isEmpty()){
            for(int j = 0;j < 5;j++){
                copyBoard[j] = board[j].clone();
            }
            prevx = list.remove();
            prevy = list.remove();
            x = list.remove();
            y = list.remove();
            int tmp = copyBoard[prevy][prevx];
            copyBoard[prevy][prevx] = copyBoard[y][x];
            copyBoard[y][x] = tmp;

            val = alfabeta(copyBoard,(ban+1)%2,count-1,alfa,beta);
           // Log.i("v","val:"+val+", count"+count);
            if(count != MAXCOUNT) {
                if (ban == 1 && val > alfa) {
                    alfa = val;
                    if (count != MAXCOUNT && alfa >= beta) return beta;
                }
                if (ban == 0 && val < beta) {
                    beta = val;
                    if (count != MAXCOUNT && alfa >= beta) return alfa;
                }
            }else {
                if(val > max
                        )
                {
                    max = val;
                    maxprevx = prevx;
                    maxprevy = prevy;
                    maxx = x;
                    maxy = y;
                }
            }
        }
        //最も浅い部分で最大値を取得しその場所をクリックさせる

        if(count == MAXCOUNT){
            bprex = maxprevx;
            bprey = maxprevy;
            bx = maxx;
            by = maxy;
            view[maxprevy][maxprevx].performClick();
            view[maxy][maxx].performClick();
            return -1;
        } else{
            if(ban == 0) return beta;
            else return alfa;
        }

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
