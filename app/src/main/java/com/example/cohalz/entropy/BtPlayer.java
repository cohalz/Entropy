package com.example.cohalz.entropy;

import android.content.Context;
import android.graphics.Point;

/**
 * Created by cohalz on 15/07/02.
 */
public class BtPlayer extends Player {
    boolean Mflag = false;
    BluetoothActivity act;
    public BtPlayer(int number, boolean ban, Context act, Class<?> cls){
        super(number, ban,act,cls);
    }

    @Override
    public void doToClick(Player another, Board board,Point point){
        if (board.checkToPoint(point)){
            move.to = point;
            change(board);
            allChangeBan(another);
            Mflag = true;
        }
        board.movableToBlank();
        changeState();
    }
}
