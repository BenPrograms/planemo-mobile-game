package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Davey on 1/3/2017.
 */

public class Arrows {
    private int x;
    private int y;

    private int hitboxX;
    private int hitboxY;

    private int position;
    private int size;
    private int speedX;
    Paint paint = new Paint();
    private int color = Color.rgb(124, 92, 58);
    private Random randomPos = new Random();
    private Random randomy = new Random();

    public Arrows(){
        hitboxX = 70;
        hitboxY = 5;
        size = 75;
        position = randomPos.nextInt(4 - (1 + 1)) + 1;
        y = randomy.nextInt(Constants.height - Constants.height / 16 - (Constants.height / 16 + 1)) + 1;

        if(position == 1){
            x = 0 - size;
            speedX = 12;
        }else{
            x = Constants.width;
            speedX = -12;
        }
    }

    public int getHitboxX(){return hitboxX;}
    public int getHitboxY(){return hitboxY;}
    public int getX(){return x;}
    public int getY(){return y;}

    public void update(){
        x += speedX;
    }

    public void draw(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setColor(color);
        canvas.drawLine(x, y, x + size, y, paint);

        paint.setStrokeWidth(2);
        canvas.drawLine(x, y - 10, x, y + 10, paint);
        canvas.drawLine(x - 15, y, x, y - 10, paint);
        canvas.drawLine(x - 15, y, x, y + 10, paint);

        paint.setStrokeWidth(2);
        canvas.drawLine(x + size, y - 10, x + size, y + 10, paint);
        canvas.drawLine(x + size + 15, y, x + size, y - 10, paint);
        canvas.drawLine(x + size + 15, y, x + size, y + 10, paint);
    }
}
