package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;

/**
 * Created by Davey on 11/6/2016.
 */

public class Explosion {
    ArrayList<ExplosionBlast> blast = new ArrayList<ExplosionBlast>();

    private int centerX;
    private int centerY;

    private int liveTime;
    private int color;

    public Explosion(int x, int y, int color){
        this.centerX = x;
        this.centerY = y;
        this.color = color;
        liveTime = 60;

        addBlast(6);
    }

    private void addBlast(int num){
        for(int i = 0; i < num; i++){
            blast.add(new ExplosionBlast(centerX, centerY, color));
        }
    }

    public int getLiveTime(){
        return liveTime;
    }
    public int getCenterX(){
        return centerX;
    }
    public int getCenterY(){
        return centerY;
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
    public void update(){
        for(int i = 0; i < blast.size(); i++){
            blast.get(i).update();

            if(blast.get(i).getSize() <= 0){
                blast.remove(i);
            }
        }
    }
    public void draw(Canvas canvas){
        for(int i = 0; i < blast.size(); i++){
            blast.get(i).draw(canvas);
        }
    }
}
