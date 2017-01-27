package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Davey on 1/9/2017.
 */

public class RainAnim {
    private int x;
    private int y;

    private int length;

    Paint paint = new Paint();

    private int time = 0;
    private int color = Color.argb(140, 200, 200, 200);

    public RainAnim(int x, int y){
        this.x = x;
        this.y = y;
        length = 0;
    }
    public int getTime(){return time;}

    public void update(){
        length+=7;
        time++;
    }

    public void draw(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setStrokeWidth(4);
        canvas.drawLine(x - 10, y, x - 10, y - length / 8, paint);
        canvas.drawLine(x - 15, y, x - 25, y - length / 8, paint);
        canvas.drawLine(x - 5, y, x + 5, y - length / 8, paint);
    }
}
