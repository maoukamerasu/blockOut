package com.example.animetion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


public class MainActivity extends Activity implements Runnable {
    SampleView sv;
    Handler hn;
    float x,y,dx,dy;
    int block[][]={
            {1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1}
    };
    int height=60;
    int width=120;
    int block_top=300;
    int block_left=120;
    int player_width=300;
    public int bottom=0;
    public float touchX=800,touchY;
    public boolean gameover=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout ll = new LinearLayout(this);
        setContentView(ll);
        hn = new Handler();
        hn.postDelayed(this,10);
        sv = new SampleView(this);
        ll.addView(sv);
    }
    public void run(){
        WindowManager wm =
                (WindowManager)getSystemService(WINDOW_SERVICE);
        Display dp =wm.getDefaultDisplay();
        Point p = new Point();
        dp.getSize(p);
        bottom=p.y;
        block_left=(p.x-875)/2;
        if(x<10||x>p.x-10)
            dx=-dx;
        if(y<10)
            dy=-dy;
        if(x>touchX-player_width/2-10&&x<touchX-player_width/2+player_width/2+10&&y>bottom-200&&y<bottom-150){
            dx=15+(((player_width/4)-(x-(touchX-player_width/2)))/(player_width/2))*15;
            dy=15-(((player_width/4)-(x-(touchX-player_width/2)))/(player_width/2))*15;
            dx=-dx;
            dy=-dy;
        }else if(x>touchX-player_width/2+player_width/2-10&&x<touchX-player_width/2+player_width+10&&y>bottom-200&&y<bottom-150){
            dx=15-(((player_width/4)-((x-(player_width/2))-(touchX-player_width/2)))/(player_width/2))*15;
            dy=15+(((player_width/4)-((x-(player_width/2))-(touchX-player_width/2)))/(player_width/2))*15;
            dy=-dy;
        }
        if(y>p.y)
            gameover=true;
        for(int blockx=0;blockx<7;blockx++){
            for(int blocky=0;blocky<5;blocky++) {
                if(x>0+blockx*(width+5)+block_left-10&&x<width+5+blockx*(width+5)+block_left+10&&y>blocky*(height+5)+block_top-10&&y<blocky*(height+5)+height+5+block_top+10&&block[blocky][blockx]==1){
                    block[blocky][blockx]=0;
                    dx=-dx;
                    dy=-dy;
                }
            }
        }
        x+=dx;
        y+=dy;
        sv.invalidate();
        hn.postDelayed(this,10);
    }
    public void onDestroy(){
        super.onDestroy();
        hn.removeCallbacks(this);
    }
    class SampleView extends View {
        Paint p;
       public SampleView(Context cn){
           super(cn);
           WindowManager wm =
                   (WindowManager)getSystemService(WINDOW_SERVICE);
           Display dp =wm.getDefaultDisplay();
           Point display_size=new Point();
           dp.getSize(display_size);
           x = 800+player_width/150; y = display_size.y-200;
           dx=-15;dy=-15;
           p=new Paint();
       }
       public boolean onTouchEvent(MotionEvent e){
           touchX = e.getX();
       if(gameover==true&&y>bottom+2000) {
           x = touchX+player_width/2;
           y = bottom-200;
           dx=-15;
           dy=-15;
           for(int x=0;x<7;x++)
               for(int y=0;y<5;y++)
                    block[y][x]=1;
           gameover=false;
       }
       invalidate();
       return true;
       }
       protected void onDraw(Canvas cs)
       {
           super.onDraw(cs);
           p.setColor(Color.BLACK);
           p.setStyle(Paint.Style.FILL);
           cs.drawCircle(x,y,20,p);
           cs.drawRect(touchX-player_width/2,bottom-200,touchX+player_width-player_width/2,bottom-150,p);
           for(int blockx=0;blockx<7;blockx++){
               for(int blocky=0;blocky<5;blocky++) {
                   if(block[blocky][blockx]==1)
                   cs.drawRect(0 + blockx * (width+5)+block_left, 0 +blocky* (height+5)+block_top, 0 + blockx* (width+5) + width+block_left, 0 +blocky* (height+5) + height+block_top, p);
               }
           }
       }

    }
}