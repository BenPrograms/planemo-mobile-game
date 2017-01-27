package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Davey on 10/30/2016.
 */

public class Enemy {
    private int x;
    private int y;

    private int size;

    private int spot;
    private int newSize;

    private int Xspeed;
    private int Yspeed;

    private boolean sizeShrink = false;

    private int health;
    private boolean dead;

    private boolean enter;
    private boolean hit;
    private int hitT;

    private int opac = 150;

    //create random speeds
    Random ranXS = new Random();
    Random ranYS = new Random();

    //create random positions
    Random position = new Random();

    //create random type
    Random ranType = new Random();
    private int type;

    //generate colors
    private int red = Color.argb(opac, 221, 136, 166);
    private int green = Color.argb(opac, 136, 221, 155);
    private int blue = Color.argb(opac, 3, 236, 195);
    private int white = Color.argb(opac, 244, 100, 100);
    private int orange = Color.argb(opac, 202, 231, 46);
    private int superWhite = Color.argb(opac, 244, 244, 244);

    private int color;

    Paint paint;

    public Enemy(){
        opac = 150;
        spot = position.nextInt(((3) - (2 + 1)) + 2);
        type = ranType.nextInt((7 - (1 + 1)) + 1);

        if(type == 2){
            color = red;
            size = 100;
            health = 3;

            Xspeed = ranXS.nextInt((3 - (1 + 1))) + 1;
            Yspeed = ranYS.nextInt((10 - (7 + 1))) + 7;
        }else if(type == 3){
            color = green;
            size = 75;
            health = 2;

            Xspeed = ranXS.nextInt((5 - (2 + 1))) + 2;
            Yspeed = ranYS.nextInt((15 - (8 + 1))) + 8;
        }else if(type == 4){
            color = blue;
            size = 150;
            health = 4;

            Xspeed = ranXS.nextInt((3 - (2 + 1))) + 2;
            Yspeed = ranYS.nextInt((7 - (4 + 1))) + 4;
        }else{
            color = orange;
            size = 50;
            health = 1;

            Xspeed = ranXS.nextInt((5 - (3 + 1))) + 3;
            Yspeed = ranYS.nextInt((17 - (10 + 1))) + 10;
        }
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        if(Xspeed == 0){
            Xspeed = 1;
        }
        if(Yspeed == 0 || Yspeed == 1){
            Yspeed = 2;
        }
        if(spot == 1){
            x = Constants.width / 2;
            y = -size * 2;
            Xspeed = -Xspeed;
        }else{
            x = Constants.width / 2;
            y = -size * 2;
        }

        enter = false;
        dead = false;
        hit = false;
        hitT = 0;
    }
    public void setHealth(int health){
        this.health = health;
    }
    public void setHit(boolean hit){
        this.hit = hit;
    }
    public void setOpac(int opac) {this.opac = opac;}
    public int getHealth() {return health;}
    public int getXSpeed(){
        return Xspeed;
    }
    public int getYSpeed() { return Yspeed; }
    public void setXspeed(int xspeed) {this.Xspeed = xspeed;}
    public void setYspeed(int yspeed) {this.Yspeed = yspeed;}
    public int getColor() { return color;}
    public int getX(){
        return x;
    }
    public int getY(){ return y; }
    public int getOpac(){return opac;}
    public int getSize(){ return size;}
    public boolean getDead(){return dead;}
    public boolean getHit(){return hit;}

    public void update(){
        red = Color.argb(opac, 221, 136, 166);
        green = Color.argb(opac, 136, 221, 155);
        blue = Color.argb(opac, 3, 236, 195);
        white = Color.argb(opac, 244, 100, 100);
        orange = Color.argb(opac, 202, 231, 46);
        superWhite = Color.argb(opac, 244, 244, 244);

        x += Xspeed;
        y += Yspeed;

        if(health == 0){
            dead = true;
        }

        //checks if object is inside play field
        if(y >= 0 - size && y <= Constants.height){
            enter = true;
        }
        //checks if collides with wall
        if(enter == true){
            if(x <= 0 + size){
                Xspeed = Xspeed * -1;
            }else if(x >= Constants.width - size){
                Xspeed = Xspeed * -1;
            } else if(y >= Constants.height){
                dead = true;
            }
        }

        if(hit == true){
            hitT++;
        }
        if(hitT == 1){
            newSize = size - 24;
            sizeShrink = true;
        }
        if(sizeShrink == true){
            if(size >= newSize){
                size--;
            }
            if(size <= newSize){
                sizeShrink = false;
            }
        }
        if(hitT >= 20){
            hit = false;
            hitT = 0;
        }
    }
    public void draw(Canvas canvas){
        canvas.drawColor(Color.TRANSPARENT);

        if(GamePanel.slowdown == true){
            color = superWhite;
        }
        if(GamePanel.slowdown == false){
            if(type == 0){
                color = red;
            }else if(type == 1){
                color = green;
            }else if(type == 2){
                color = blue;
            }else if(type == 3){
                color = orange;
            }
        }
        paint.setStrokeWidth(3);

        paint.setStyle(Paint.Style.FILL);
        if(!hit){
            paint.setColor(color);
        }
        if(hit){
            paint.setColor(white);
        }
        //draws hit circle
        canvas.drawCircle(x, y, size, paint);
        //draws damage circle
        canvas.drawCircle(x, y, size / 6, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, size, paint);
    }
}
