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
    view [3][1] = (TextView)findViewById(R.id.textView15);
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
          ColorDrawable col = (ColorDrawable) v.getBackground();

          Log.i("v","x:"+x+", y:"+y);
          if (flag == 0 && col.getColor() == Color.parseColor(ps[ban])) {
            flag = 1;
            int i = 1;
            while (y - i >= 0 & x - i >= 0 && ((ColorDrawable) view[y - i][x - i].getBackground()).getColor() == Color.parseColor(white)) {
              view[y - i][x - i].setText("test");
              i++;
            }
            i = 1;
            while (y + i < 5 && x + i < 5 && ((ColorDrawable) view[y + i][x + i].getBackground()).getColor() == Color.parseColor(white)) {
              view[y + i][x + i].setText("test");
              i++;
            }
            i = 1;
            while (y - i >= 0 && x + i < 5 && ((ColorDrawable) view[y - i][x + i].getBackground()).getColor() == Color.parseColor(white)) {
              view[y - i][x + i].setText("test");
              i++;
            }
            i = 1;
            while (y + i < 5 && x - i >= 0 && ((ColorDrawable) view[y + i][x - i].getBackground()).getColor() == Color.parseColor(white)) {
              view[y + i][x - i].setText("test");
              i++;
            }
            pastx = x;
            pasty = y;
          }
          else if (flag == 1 && col.getColor() == Color.parseColor(white)) {

              int i = 1;
              int count = 0;
            while (pasty - i >= 0 & pastx - i >= 0) {
                  view[pasty - i][pastx - i].setText("");
                  i++;
                count++;
              }
              i = 1;
              while (pasty + i < 5 && pastx + i < 5){
                  view[pasty + i][pastx + i].setText("");
                  i++;
                  count++;
              }
              i = 1;
              while (pasty - i >= 0 && pastx + i < 5){
                  view[pasty - i][pastx + i].setText("");
                  i++;
                  count++;
              }
              i = 1;
              while (pasty + i < 5 && pastx - i >= 0){
                  view[pasty + i][pastx - i].setText("");
                  i++;
                  count++;
              }
              flag = 0;
              if(count > 0){
                  view[pasty][pastx].setBackgroundColor(Color.parseColor(white));
                  view[y][x].setBackgroundColor(Color.parseColor(ps[ban]));
              }
              view[y][x].setText("");
              ban = (ban + 1) % 2;
          }
        }
      }
    }
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
