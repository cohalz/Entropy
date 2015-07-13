
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

}

