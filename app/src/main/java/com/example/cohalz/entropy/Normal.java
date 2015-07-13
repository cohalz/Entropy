package com.example.cohalz.entropy;

import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.example.cohalz.entropy.Constants.*;


public class Normal extends ActionBarActivity {

    Player p1,p2;
    Player[] players;
    Board board;


    TextView status;
    TextView log;

    void test1(){
        log.setText("test1");
    }
    void test2(){
        log.setText("test2");
    }
    void test3(){
        log.setText("test3");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        p1 = new Player(0,true);
        p2 = new Player(1,false);
        players = new Player[]{p1, p2};
        board = new Board(this);
        status = (TextView) findViewById(R.id.status);

        log = (TextView) findViewById(R.id.log);
    }

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
