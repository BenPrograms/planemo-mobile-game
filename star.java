package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Davey on 10/15/2016.
 */

public class star {
    private int x;
    private int y;

    private Paint paint;
    private int opacity;
    private int speedx;
    private int speedy;
    private int type;
    Random ranSpeedX = new Random();
    Random ranSpeedY = new Random();
    Random ranType = new Random();
    private int color = Color.argb(opacity, 200, 200, 200);

    public star(){
        speedx = 3;
        speedy = ranSpeedY.nextInt(27 - (17 + 1)) + 17;
        x = ranSpeedX.nextInt((Constants.width + Constants.width / 2) - (0 + 1)) + 0;
        type = ranType.nextInt(8 - (1 + 1)) + 1;
        y = -10;

        if(type == 2){
            opacity = 50;
        }else if(type == 3){
            opacity = 100;
        }else if(type == 4){
            opacity = 150;
        }else if(type == 5){
            opacity = 200;
        }else{
            opacity = 250;
        }

        paint = new Paint();
    }
    public int getXspeed(){
        return speedx;
    }
    public int getYspeed(){
        return speedy;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getLength() {return speedy * 2;}
    public int getOpac() {return type;}
    public void update(){
        color = Color.argb(opacity, 200, 200, 200);
        y += speedy;
        x -= speedx;
    }

    public void draw(Canvas canvas){
        paint.setColor(color);
        paint.setStrokeWidth(4);
        canvas.drawLine(x, y, x - 10, y + (speedy * 2), paint);
    }
}
