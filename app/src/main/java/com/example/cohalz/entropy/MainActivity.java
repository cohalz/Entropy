package com.example.cohalz.entropy;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

  int flag = 0;
  String ps[] = new String[2];
  int pastx,pasty;
  String gray = "#eeeeee";
  //String p2 = "#ff57ff6a";
  //String p = p1;
  int ban = 0;
  String white = "#ffffff";
  TextView view[][] = new TextView[5][5];

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    view [0][0] = (TextView)findViewById(R.id.textView0);
    view [0][1] = (TextView)findViewById(R.id.textView1);
    view [0][2] = (TextView)findViewById(R.id.textView2);
    view [0][3] = (TextView)findViewById(R.id.textView3);
    view [0][4] = (TextView)findViewById(R.id.textView4);
    view [1][0] = (TextView)findViewById(R.id.textView5);
    view [1][1] = (TextView)findViewById(R.id.textView6);
    view [1][2] = (TextView)findViewById(R.id.textView7);
    view [1][3] = (TextView)findViewById(R.id.textView8);
    view [1][4] = (TextView)findViewById(R.id.textView9);
    view [2][0] = (TextView)findViewById(R.id.textView10);
    view [2][1] = (TextView)findViewById(R.id.textView11);
    view [2][2] = (TextView)findViewById(R.id.textView12);
    view [2][3] = (TextView)findViewById(R.id.textView13);
    view [2][4] = (TextView)findViewById(R.id.textView14);
    view [3][0] = (TextView)findViewById(R.id.textView15);
    view [3][1] = (TextView)findViewById(R.id.textView16);
    view [3][2] = (TextView)findViewById(R.id.textView17);
    view [3][3] = (TextView)findViewById(R.id.textView18);
    view [3][4] = (TextView)findViewById(R.id.textView19);
    view [4][0] = (TextView)findViewById(R.id.textView20);
    view [4][1] = (TextView)findViewById(R.id.textView21);
    view [4][2] = (TextView)findViewById(R.id.textView22);
    view [4][3] = (TextView)findViewById(R.id.textView23);
    view [4][4] = (TextView)findViewById(R.id.textView24);
  }
  public void onClick(View v) {
    ps[0] = "#ff48fffd";
    ps[1] = "#ff57ff6a";

    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 5; x++) {
        if (v == view[y][x]) {
          ColorDrawable col = (ColorDrawable) view[y][x].getBackground();

          Log.i("v","x:"+x+", y:"+y);
                if (flag == 0 && col.getColor() == Color.parseColor(ps[ban])) {
                    if(isTouched(x,y,col.getColor())) {
                    int count = 0;
                    int i = 1;
                    while (y - i >= 0 & x - i >= 0 && ((ColorDrawable) view[y - i][x - i].getBackground()).getColor() == Color.parseColor(white)) {
                        view[y - i][x - i].setBackgroundColor(Color.parseColor(gray));
                        i++;
                        count++;
                    }
                    i = 1;
                    while (y + i < 5 && x + i < 5 && ((ColorDrawable) view[y + i][x + i].getBackground()).getColor() == Color.parseColor(white)) {
                        view[y + i][x + i].setBackgroundColor(Color.parseColor(gray));
                        i++;
                        count++;
                    }
                    i = 1;
                    while (y - i >= 0 && x + i < 5 && ((ColorDrawable) view[y - i][x + i].getBackground()).getColor() == Color.parseColor(white)) {
                        view[y - i][x + i].setBackgroundColor(Color.parseColor(gray));
                        i++;
                        count++;
                    }
                    i = 1;
                    while (y + i < 5 && x - i >= 0 && ((ColorDrawable) view[y + i][x - i].getBackground()).getColor() == Color.parseColor(white)) {
                        view[y + i][x - i].setBackgroundColor(Color.parseColor(gray));
                        i++;
                        count++;
                    }
                    i = 1;
                    while (y + i < 5 && ((ColorDrawable) view[y + i][x].getBackground()).getColor() == Color.parseColor(white)) {
                        view[y + i][x].setBackgroundColor(Color.parseColor(gray));
                        i++;
                        count++;
                    }
                    i = 1;
                    while (y - i >= 0 && ((ColorDrawable) view[y - i][x].getBackground()).getColor() == Color.parseColor(white)) {
                        view[y - i][x].setBackgroundColor(Color.parseColor(gray));
                        i++;
                        count++;
                    }
                    i = 1;
                    while (x - i >= 0 && ((ColorDrawable) view[y][x - i].getBackground()).getColor() == Color.parseColor(white)) {
                        view[y][x - i].setBackgroundColor(Color.parseColor(gray));
                        i++;
                        count++;
                    }
                    i = 1;
                    while (x + i < 5 && ((ColorDrawable) view[y][x + i].getBackground()).getColor() == Color.parseColor(white)) {
                        view[y][x + i].setBackgroundColor(Color.parseColor(gray));
                        i++;
                        count++;
                    }
                    if (count > 0) {
                        flag = 1;
                        pastx = x;
                        pasty = y;

                    }
                }
            }
          else if (flag == 1) {

            for (int i = 0;i < 5 ;i++ ) {
              for(int j = 0; j < 5; j++){
                if(((ColorDrawable) view[i][j].getBackground()).getColor() == Color.parseColor(gray))
                  view[i][j].setBackgroundColor(Color.parseColor(white));
              }

            }

            if(isDiag(pastx,pasty,x,y) && col.getColor() == Color.parseColor(white)){
              view[pasty][pastx].setBackgroundColor(Color.parseColor(white));
              view[y][x].setBackgroundColor(Color.parseColor(ps[ban]));
              ban = (ban + 1) % 2;
            }
            flag = 0;
          }

        }
      }
    }
  }
  public boolean isDiag(int x0, int y0, int x1, int y1){
    for (int i = 0; i < 5;i++){
      if((x0+i==x1 && y0+i==y1) || (x0+i==x1 &&y0-i == y1 ) ||
          (x0-i==x1 &&y0-i == y1 ) || (x0-i==x1 &&y0+i == y1 ) ||
          (x0==x1 &&y0-i == y1 ) || (x0==x1 &&y0+i == y1 ) ||
          (x0-i==x1 &&y0 == y1 ) || (x0+i==x1 &&y0 == y1 )
        ){
        return true;
        }
    }
    return false;
  }

  public boolean isTouched(int x,int y,int color){
      if(y > 0) {
          if(((ColorDrawable) view[y-1][x].getBackground()).getColor() == color) return true;
          if(x > 0){
              if(((ColorDrawable) view[y-1][x-1].getBackground()).getColor() == color) return true;
          }
          if(x < 4){
              if(((ColorDrawable) view[y-1][x+1].getBackground()).getColor() == color) return true;
          }
      }
    if(y < 4) {
          if(((ColorDrawable) view[y+1][x].getBackground()).getColor() == color) return true;
          if(x > 0){
              if(((ColorDrawable) view[y+1][x-1].getBackground()).getColor() == color) return true;
          }
          if(x < 4){
              if(((ColorDrawable) view[y+1][x+1].getBackground()).getColor() == color) return true;
          }
      }
      if(x > 0){
          if(((ColorDrawable) view[y][x-1].getBackground()).getColor() == color) return true;
      }
      if(x < 4){
          if(((ColorDrawable) view[y][x+1].getBackground()).getColor() == color) return true;
      }
      return false;
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
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
