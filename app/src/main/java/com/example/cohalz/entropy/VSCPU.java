
package com.example.cohalz.entropy;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.LinkedList;

import static com.example.cohalz.entropy.Constants.FROM;


public class VSCPU extends Normal {
    int MAXCOUNT = 3;
    public CPUPlayer p1;
    public CPUPlayer p2;
    public CPUPlayer[] players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p1 = new CPUPlayer(0, true, this, p1win.class, false);
        p2 = new CPUPlayer(1,false, this, p2win.class, true);
        players = new CPUPlayer[]{p1, p2};
    }

    @Override
    public void onClick(View v) {
        int x = 0, y;

        for_label:
        for (y = 0; y < 5; y++)
            for (x = 0; x < 5; x++)
                if (v == board.viewBoard.view[y][x]) break for_label;


        for(int i = 0; i < 2; i++){
            if(players[i].ban){
                Point point = new Point(x,y);
                if(players[i].state == FROM){
                    players[i].doFromClick(board,point);
                } else {
                    players[i].doToClick(players[(i+1)%2],board,point);
                }
                break;
            }
        }
        //board.displayState();
    }

    public int alfabeta(int[][] board, int ban, int count,int alfa,int beta){
        int max = Integer.MIN_VALUE;
        int maxprevx = -1;
        int maxprevy = -1;
        int maxx = -1;
        int maxy = -1;
        int fromX = -1;
        int fromY = -1;
        int toX = -1;
        int toY = -1;
        int val;
        Move move,maxMove = new Move(-1,-1,-1,-1);
        int[][] copyBoard = new int[5][5];
        for(int i = 0;i < 5;i++){
                copyBoard[i] = board[i].clone();
        }
        if(count == 0) return eval(ban,copyBoard);
        LinkedList<Move> list = movableList(ban, copyBoard);
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
            move = list.remove();
            int tmp = copyBoard[move.fromY][move.fromX];
            copyBoard[move.fromY][move.fromX] = copyBoard[move.toY][move.toX];
            copyBoard[move.toY][move.toX] = tmp;

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
                if(val > max)
                {
                    max = val;
                    maxMove = new Move(fromX,fromY,toX,toY);
                }
            }
        }
        //最も浅い部分で最大値を取得しその場所をクリックさせる

        if(count == MAXCOUNT){
            view[maxMove.fromY][maxMove.fromX].performClick();
            view[maxMove.toX][maxMove.toX].performClick();
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

