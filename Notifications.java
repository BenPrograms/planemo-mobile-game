package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by Davey on 1/10/2017.
 */

public class Notifications {
    private String name;
    private int x;
    private int y;
    private int opacity = 230;
    private int size;

    private int color = Color.argb(opacity, 220, 220, 220);

    Paint paint = new Paint();

    public Notifications(String name, int size){
        this.name = name;
        this.size = size;

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(65);
        paint.setStrokeWidth(2);

        x = Constants.width / 2 - size;
        y = 0;
    }
    public int getOpacity(){ return opacity;}
    public void update(){
        opacity-=3;
        y += 4;
        color = Color.argb(opacity, 220, 220, 220);
    }

    public void draw(Canvas canvas){
        paint.setColor(color);
        canvas.drawText("" + name, x, y, paint);
    }
}
