package com.example.cohalz.entropy;

import android.content.Context;
import android.graphics.Point;

import java.util.LinkedList;

/**
 * Created by cohalz on 15/07/02.
 */
public class CPUPlayer extends Player {

    public CPUPlayer(int number, boolean ban,Context act, Class<?> cls, boolean cpuFlag){
        super(number, ban, act,cls);
        this.cpuFlag = cpuFlag;
    }

    @Override
    public void doToClick(Player another, Board board,Point point){
        if (board.checkToPoint(point)){
            move.to = point;
            change(board);
            allChangeBan(another);
            if(another.cpuFlag){
                Move m =  alfabeta(another,board);
                another.doFromClick(board,m.from);
                another.doToClick(this, board, m.to);
            }
        }
        board.movableToBlank();
        changeState();
        if(isClear(board)) clear();
        else if(another.isClear(board)) another.clear();
    }

    public Move alfabeta(Player another, Board board){
        return board.alfabeta(this,another,board.getState(),another.number,3,Integer.MIN_VALUE,Integer.MAX_VALUE).m;
    }
}
