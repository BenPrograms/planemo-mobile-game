package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Random;

/**
 * Created by Davey on 10/24/2016.
 */

public class Bullet {
    private int x;
    private int y;
    private int rad;
    private int opac = 200;

    private int color = Color.argb(opac,244, 244, 100);

    Paint paint;

    private int xs;
    private int ys;

    double angle = 0;
    private int velX = 20;
    private int velY = 20;

    public Bullet(int x, int y){

        ys = 3;
        xs = 3;

        paint = new Paint();

        this.y = y;
        this.x = x;

        rad = 17;
    }
    private void drawPolygon(Canvas mCanvas, float x, float y, float radius, float sides, float startAngle, boolean anticlockwise, Paint paint) {
        if (sides < 3) { return; }

        float a = ((float) Math.PI *2) / sides * (anticlockwise ? -1 : 1);
        mCanvas.save();
        mCanvas.translate(x, y);
        mCanvas.rotate(startAngle);
        Path path = new Path();
        path.moveTo(radius, 0);
        for(int i = 1; i < sides; i++) {
            path.lineTo(radius * (float) Math.cos(a * i), radius * (float) Math.sin(a * i));
        }
        path.close();
        mCanvas.drawPath(path, paint);
        mCanvas.restore();
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
        color = Color.argb(opac,244, 244, 100);

        if(opac >= 0){
            opac--;
        }

        x += (velX * (float) Math.cos(Math.toRadians(angle - 90)));
        y += (velY * (float) Math.sin(Math.toRadians(angle - 90)));
    }
    public void draw(Canvas canvas){
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.save();
        drawPolygon(canvas, x, y, rad, 3, 270, false, paint);
        canvas.restore();
    }
}
