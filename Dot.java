package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by Davey on 10/17/2016.
 */

public class Dot {
    private int x;
    private int y;
    private int rad;
    private int color = Color.argb(100, 200, 200, 0);

    private Random r;
    private Random ry;

    private int rotate = 0;

    Paint paint;

    private int xs;
    private int ys;

    public Dot(){
        r = new Random();
        ry = new Random();

        ys = 5;
        xs = 5;

        paint = new Paint();

        y = ry.nextInt(((Constants.height / 2 + 150) - (Constants.height / 2 - 150)) + 1) + (Constants.height / 2 - 150);
        x = r.nextInt(((Constants.width / 2 + 200) - (Constants.width / 2 - 200)) + 1) + (Constants.width / 2 - 200);

        rad = 10;
    }
    public int getXSpeed(){
        return xs;
    }
    public int getR() {return rotate;}
    public int getYSpeed(){
        return ys;
    }
    public void setXSpeed(int speed){
        this.xs = speed;
    }
    public void setYSpeed(int speedy){
        this.ys = speedy;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void update(){
        x-=xs;
        y+=ys;

        if(x >= Constants.width / 2 + 200){
            xs = -xs;
        }
        if(x <= Constants.width / 2 - 200){
            xs = -xs;
        }
        if(y >= Constants.height / 2 + 150){
            ys = -ys;
        }
        if(y <= Constants.height / 2 - 150){
            ys = -ys;
        }
    }
    public void draw(Canvas canvas){
        paint.setColor(color);
        canvas.drawCircle(x, y, rad, paint);
    }
}
