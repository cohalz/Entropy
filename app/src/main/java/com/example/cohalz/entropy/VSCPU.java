package com.example.cohalz.entropy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.LinkedList;


public class VSCPU extends Normal {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onClick(View v){
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (v == view[y][x]) {
                    Log.i("v", "x:" + x + ", y:" + y + ", flag:" + flag);
                    if (flag == 0 && board[y][x] == ban) {
                        if (isTouched(x, y, ban)) {

                            if (movable(x, y) > 0) {
                                flag = 1;
                                pastx = x;
                                pasty = y;
                            }
                        }
                    } else if (flag == 1) {
                        aloneClear();
                        if (board[y][x] == movable) {
                            board[pasty][pastx] = blank;
                            board[y][x] = ban;
                            if (isClear(0)) {
                                Intent intent = new Intent(this, p1win.class);
                                startActivity(intent);
                                // clear(ban);
                                //return;
                            } else if (isClear((ban + 1) % 2)) {
                                clear((ban + 1) % 2);
                                return;

                            } else {
                                ban = (ban + 1) % 2;
                                if (isPass()) ban = (ban + 1) % 2;
                                if (isPass()) { //ふたりともパスならDrawだが一人だけパスでもなぜか呼ばれることがある
                                    movableShowReset();
                                    status.setText("draw");
                                    flag = 3;
                                } else if (ban == 1)
                                    status.setText("2P");
                                else
                                    status.setText("1P");
                            }
                        }
                        movableShowReset();
                        flag = 0;

                    } else if (flag == 2) {
                        movableShowReset();
                        status.setText(Integer.toString(ban + 1) + "P Win!");
                    } else if (flag == 3) {
                        movableShowReset();
                        status.setText("draw");
                    }
                }
            }
        }
        display();

        if(ban == 1) {
            LinkedList<Integer> list = movableList(ban);
            int x = list.remove();
            int y = list.remove();
            view[y][x].performClick();
            x = list.remove();
            y = list.remove();
            view[y][x].performClick();
        }
        //if (flag == 0) Log.i("v", movableList(ban).size()/4 + "");
    }

    public LinkedList<Integer> movableList(int ban){
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0;i < 5; i++){
            for(int j = 0;j < 5;j++){
                if(movable(i,j) > 0 && board[i][j] == ban) {
                    list.addAll(addMovable(i,j));
                }
            }
        }
        return list;
    }

    public LinkedList<Integer> addMovable(int x, int y) {
        LinkedList<Integer> list = new LinkedList<>();
        int flag = 0;
        if (isContainsAlone()) {
            createAloneFlag();
            flag = 1;
        }
        list.addAll(addVec(x, y, flag, 1, 1));
        list.addAll(addVec(x, y, flag, 1, 0));
        list.addAll(addVec(x, y, flag, 1, -1));
        list.addAll(addVec(x, y, flag, 0, 1));
        list.addAll(addVec(x, y, flag, 0, -1));
        list.addAll(addVec(x, y, flag, -1, 1));
        list.addAll(addVec(x, y, flag, -1, 0));
        list.addAll(addVec(x, y, flag, -1, -1));
        return list;
    }

    public LinkedList<Integer> addVec(int x, int y, int flag, int xVec, int yVec) {
        int xi = x + xVec;
        int yi = y + yVec;
        LinkedList<Integer> list = new LinkedList<>();
        while (xi < 5 && xi >= 0 && yi < 5 && yi >= 0 && board[yi][xi] != p1 && board[yi][xi] != p2) {
            if (flag == 1) {
                if (board[yi][xi] == ban + 3){
                    list.add(x);
                    list.add(y);
                    list.add(xi);
                    list.add(yi);
                }
                yi += yVec;
                xi += xVec;
            } else {
                yi += yVec;
                xi += xVec;
                list.add(x);
                list.add(y);
                list.add(xi);
                list.add(yi);
            }
        }
        movableClear();
        return list;
    }

    void movableClear(){
        for(int y = 0;y < 5;y++){
            for(int x = 0; x < 5;x++){
                if(board[y][x] >= movable) board[y][x] = blank;
            }
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
