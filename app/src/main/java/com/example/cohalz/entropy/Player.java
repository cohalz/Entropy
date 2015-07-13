package com.example.cohalz.entropy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import static com.example.cohalz.entropy.Constants.*;


/**
 * Created by cohalz on 15/07/01.
 */
public class Player {
    int number;
    boolean ban;
    boolean state;
    public boolean cpuFlag;
    private Context act;
    private Class<?> cls;
    Move move;

    public Player(int number, boolean ban, Context act, Class<?> cls){
        this.number = number;
        this.ban = ban;
        this.state = FROM;
        this.move = new Move(new Point(-1,-1),new Point(-1,-1));
        this.act = act;
        this.cls = cls;
    }

    //「その人」の番かどうかのフラグを変更
    private void changeBan() {
        this.ban = !this.ban;
        this.state = FROM;
    }

    public void allChangeBan(Player another){
        changeBan();
        another.changeBan();
    }

    public void changeState(){
        this.state = !this.state;
    }

    public void change(Board board)
    {
        board.move(move);
    }

    public void doFromClick(Board board, Point point){
        if(board.checkFromPoint(this, point)) {
            move.from = point;
            changeState();
        }
    }

    public void doToClick(Player another, Board board,Point point){
        if (board.checkToPoint(point)){
            move.to = point;
            change(board);
            allChangeBan(another);
        }
        board.movableToBlank();
        changeState();

        if(another.isPass(board)) another.allChangeBan(this);

        if(isClear(board)) clear();
        else if(another.isClear(board)) another.clear();
    }

    public boolean isClear(Board board){
        return board.isClear(number);
    }

    public void clear(){
        Intent win = new Intent(act, cls);
        act.startActivity(win);
    }

    public boolean isPass(Board board){
        return board.isPass(number);
    }
}


