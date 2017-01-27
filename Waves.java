package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.util.Random;

/**
 * Created by Davey on 12/28/2016.
 */

public class Waves {
    private int x;
    private int y;

    private int startX;
    private int startY;

    private int xMove;
    private int yMove;

    private int size;

    private Drawable wave;

    private Random randSpeed = new Random();
    private Random randYSpeed = new Random();

    public Waves(int x, int y){
        this.x = x;
        this.y = y;

        startX = this.x;
        startY = this.y;

        xMove = 3;
        yMove = 1;

        wave = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.waves2);
    }

    public void update(){
        y += yMove;
        x += xMove;

        if(y >= startY + 90){
            yMove = -1;
        }
        if(y <= startY + 15){
            yMove = 1;
        }

        if(x >= Constants.width){
            x = 0 - Constants.width;
        }
    }

    public void draw(Canvas canvas){
        wave.setBounds(x, y, x + Constants.width, y + Constants.width / (11 / 2));
        wave.draw(canvas);
    }
}
