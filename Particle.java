package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Davey on 12/26/2016.
 */

public class Particle {
    private int x;
    private int y;
    private int speed;
    private int size;

    private Random randX = new Random();
    private Random randSpeed = new Random();
    private Random randSize = new Random();

    Paint paint = new Paint();
    private int alpha;
    private int g;
    private int color = Color.argb(alpha, 244, g, g);

    public Particle(){
        x = randX.nextInt((Constants.playerX + 200) - (Constants.playerX + 50 + 1)) + Constants.playerX + 50;
        y = Constants.playerY + 125;
        alpha = 254;
        speed = randSpeed.nextInt((10 - (5 + 1))) + 5;
        size = randSize.nextInt((20 - (5 + 1))) + 5;
        g = 244;
    }
    public int getX(){return x;}
    public int getY(){return y;}

    public void update(){
        y+=speed;
        if(g >= 10) {
            g-=10;
        }
        if(alpha >= 2){
            alpha -= 2;
        }
        color = Color.argb(alpha, 244, g, g);
    }

    public void draw(Canvas canvas){
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + size, y + size, paint);
    }
}
