package com.example.cohalz.entropy;

import android.content.Context;
import android.graphics.Point;

import java.util.LinkedList;

/**
 * Created by cohalz on 15/07/02.
 */
public class CPUPlayer extends Player {

    public CPUPlayer(int number, boolean ban, Context act, Class<?> cls, boolean cpuFlag){
        super(number, ban, act, cls);
        this.cpuFlag = cpuFlag;
    }

    @Override
    public void doToClick(Player another, Board board,Point point){
        if (board.checkToPoint(point)){
            move.to = point;
            change(board);
            allChangeBan(another);

            if(isClear(board)) {
                clear();
                return;
            }

            if(another.isClear(board)) {
                another.clear();
                return;
            }

            if(another.cpuFlag){
                PositionAndValue pv =  alfabeta(another,board);
                System.out.println(pv.value);
                if(pv.value == Integer.MIN_VALUE) {
                    pass(another);
                    return;
                }
                else {
                    another.doFromClick(board, pv.m.from);
                    another.doToClick(this, board, pv.m.to);
                    return; //changeStateが複数回呼ばれるのを防ぐ
                }
            }
        }
        board.movableToBlank();
        changeState();
    }

    public PositionAndValue alfabeta(Player another, Board board){
        return board.alfabeta(this,another,board.getState(),another.number,3,Integer.MIN_VALUE,Integer.MAX_VALUE);
    }
}
