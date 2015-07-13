package com.example.cohalz.entropy;

import android.app.Activity;
import android.widget.ImageView;

import static com.example.cohalz.entropy.Constants.BLANK;
import static com.example.cohalz.entropy.Constants.MOVABLE;
import static com.example.cohalz.entropy.Constants.P1;
import static com.example.cohalz.entropy.Constants.P2;

/**
 * Created by cohalz on 15/06/30.
 */
public class ViewBoard {
    public ImageView[][] view;
    private Image image;

    public ViewBoard(int[][] board, Activity a){
        image = new Image();
        view = new ImageView[5][5];
/*        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(150, 150);
        view[0][0].setLayoutParams(lp);
        view[0][1].setLayoutParams(lp);
        view[0][2].setLayoutParams(lp);
        view[0][3].setLayoutParams(lp);
        view[0][4].setLayoutParams(lp);
        view[1][0].setLayoutParams(lp);
        view[1][1].setLayoutParams(lp);
        view[1][2].setLayoutParams(lp);
        view[1][3].setLayoutParams(lp);
        view[1][4].setLayoutParams(lp);
        view[2][0].setLayoutParams(lp);
        view[2][1].setLayoutParams(lp);
        view[2][2].setLayoutParams(lp);
        view[2][3].setLayoutParams(lp);
        view[2][4].setLayoutParams(lp);
        view[3][0].setLayoutParams(lp);
        view[3][1].setLayoutParams(lp);
        view[3][2].setLayoutParams(lp);
        view[3][3].setLayoutParams(lp);
        view[3][4].setLayoutParams(lp);
        view[4][0].setLayoutParams(lp);
        view[4][1].setLayoutParams(lp);
        view[4][2].setLayoutParams(lp);
        view[4][3].setLayoutParams(lp);
        view[4][4].setLayoutParams(lp);*/
        view[0][0] = (ImageView) a.findViewById(R.id.imageView0);
        view[0][1] = (ImageView) a.findViewById(R.id.imageView1);
        view[0][2] = (ImageView) a.findViewById(R.id.imageView2);
        view[0][3] = (ImageView) a.findViewById(R.id.imageView3);
        view[0][4] = (ImageView) a.findViewById(R.id.imageView4);
        view[1][0] = (ImageView) a.findViewById(R.id.imageView5);
        view[1][1] = (ImageView) a.findViewById(R.id.imageView6);
        view[1][2] = (ImageView) a.findViewById(R.id.imageView7);
        view[1][3] = (ImageView) a.findViewById(R.id.imageView8);
        view[1][4] = (ImageView) a.findViewById(R.id.imageView9);
        view[2][0] = (ImageView) a.findViewById(R.id.imageView10);
        view[2][1] = (ImageView) a.findViewById(R.id.imageView11);
        view[2][2] = (ImageView) a.findViewById(R.id.imageView12);
        view[2][3] = (ImageView) a.findViewById(R.id.imageView13);
        view[2][4] = (ImageView) a.findViewById(R.id.imageView14);
        view[3][0] = (ImageView) a.findViewById(R.id.imageView15);
        view[3][1] = (ImageView) a.findViewById(R.id.imageView16);
        view[3][2] = (ImageView) a.findViewById(R.id.imageView17);
        view[3][3] = (ImageView) a.findViewById(R.id.imageView18);
        view[3][4] = (ImageView) a.findViewById(R.id.imageView19);
        view[4][0] = (ImageView) a.findViewById(R.id.imageView20);
        view[4][1] = (ImageView) a.findViewById(R.id.imageView21);
        view[4][2] = (ImageView) a.findViewById(R.id.imageView22);
        view[4][3] = (ImageView) a.findViewById(R.id.imageView23);
        view[4][4] = (ImageView) a.findViewById(R.id.imageView24);

        display(board);
    }
    public void display(int[][] board){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++) {
                switch (board[i][j]){
                    case P1:
                        view[i][j].setTag(image.p1);
                        //view[i][j].setImageResource(R.drawable.tiger);
                        break;
                    case P2:
                        view[i][j].setTag(image.p2);
                        //view[i][j].setImageResource(R.drawable.dragon);
                        break;
                    case BLANK:
                        view[i][j].setTag(image.blank);
                        //view[i][j].setImageResource(R.drawable.white);
                        break;
                    case MOVABLE:
                        view[i][j].setTag(image.movable);
                        //view[i][j].setImageResource(R.drawable.gray);
                        break;
                    default:
                        view[i][j].setTag(image.green);
                        //view[i][j].setImageResource(R.drawable.green);
                }
                view[i][j].setImageResource((int) view[i][j].getTag());
            }
        }
    }

    public void changeImage(Move m){
        Object tmpTag = view[m.from.y][m.from.x].getTag();
        view[m.from.y][m.from.x].setTag(view[m.to.y][m.to.x].getTag());
        view[m.from.y][m.from.x].setImageResource((int) view[m.to.y][m.to.x].getTag());
        view[m.to.y][m.to.x].setTag(tmpTag);
        view[m.to.y][m.to.x].setImageResource((int)tmpTag);
    }
}
