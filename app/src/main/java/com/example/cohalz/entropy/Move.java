package com.example.cohalz.entropy;

import android.graphics.Point;

/**
 * Created by cohalz on 15/06/25.
 */
public class Move{
    Point from,to;
     public Move(Point from, Point to){
         this.from = from;
         this.to = to;
    }

    public void printMove(){
        System.out.println("from.x: " + from.x);
        System.out.println("from.y: " + from.y);
        System.out.println("to.x: " + to.x);
        System.out.println("to.y: " + to.y);
    }
}