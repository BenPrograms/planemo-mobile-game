package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Davey on 11/26/2016.
 */

public class PowerUp {
    private int x;
    private int y;

    private int size;

    private Random r;
    private int type;
    private int kind;

    //generate colors
    private int color = Color.rgb(0, 0, 0);

    Paint paint;
    private int blue = Color.argb(100, 3, 236, 195);
    private int white = Color.argb(100, 244, 100, 100);
    private int green = Color.argb(100, 3, 236, 45);

    public PowerUp(){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        r = new Random();

        type = r.nextInt((8 - (1 + 1))) + 1;

        if(type == 2 || type == 4){
            //bullet speed up
            color = white;
            size = 60;

            x = Constants.width - Constants.width / 4;
            y = 0 - size;
        }else if(type == 3 && GamePanel.doubleBullets == false){
            //double bullet
            color = green;
            size = 40;

            x = Constants.width / 2;
            y = 0 - size;
        }else{
            //slow down power up
            color = blue;
            size = 50;

            x = Constants.width / 4;
            y = -size;
        }
    }
    public int getKind() { return kind;}
    public int getX(){
        return x;
    }
    public int getY(){ return y; }
    public int getSize(){ return size;}
    public void update(){
        y += 4;

        if(size == 60){
            //kind 1 is red
            kind = 1;
        }
        if(size == 50){
            //kind 2 is blue
            kind = 2;
        }
        if(size == 40){
            //kind 3 is green
            kind = 3;
        }
    }
    public void draw(Canvas canvas){
        paint.setStrokeWidth(10);

        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, size, paint);

        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x, y, size, paint);
    }
}
