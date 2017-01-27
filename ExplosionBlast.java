package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Davey on 11/20/2016.
 */

public class ExplosionBlast {
    private int x;
    private int y;
    private int rad;
    private int opac = 200;

    private int color;

    private int newAngle;

    private Random r;

    Paint paint;

    private int xs;
    private int ys;

    double angle = 0;
    private int velX = 5;
    private int velY = 5;

    public ExplosionBlast(int x, int y, int color){
        r = new Random();
        newAngle = r.nextInt(360 - (1 + 1)) + 1;

        this.color = color;

        ys = 3;
        xs = 3;

        paint = new Paint();

        this.y = y;
        this.x = x;

        rad = 35;
    }
    public void setAngle(int angle){
        this.angle = angle;
    }
    public int getXSpeed(){
        return xs;
    }
    public int getYSpeed(){
        return ys;
    }
    public int getOpac(){
        return opac;
    }
    public void setXSpeed(int speed){
        this.xs = speed;
    }
    public void setYSpeed(int speedy){
        this.ys = speedy;
    }
    public int getSize(){ return rad; }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void update(){
        if(rad >= 0){
            rad--;
        }

        x += (velX * (float) Math.cos(Math.toRadians(newAngle)));
        y += (velY * (float) Math.sin(Math.toRadians(newAngle)));
    }
    public void draw(Canvas canvas){
        paint.setStrokeWidth(1);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawCircle(x, y, rad, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, rad, paint);
    }
}
