package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Random;

import static com.davey.spaceexplorer.spaceexplorer.R.id.image;

/**
 * Created by Davey on 10/20/2016.
 */

public class Player {
    private int x;
    private int y;
    private float xSpeed;

    private int xVel = 0;
    private int yVel = 0;
    private int size = 250;

    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    ArrayList<Particle> particles = new ArrayList<Particle>();
    ArrayList<Waves> waves = new ArrayList<Waves>();

    private Paint paint;
    private long fireTime;

    public static int maxFire = 35;

    private Drawable player;
    private Drawable player2;
    private Drawable player3;
    private Drawable player4;
    private Drawable player5;
    private Drawable player6;
    private Drawable player2Blink;
    private Drawable playerBlink;
    private Drawable player3Blink;
    private Drawable player4Blink;
    private Drawable player5Blink;
    private Drawable player6Blink;
    private Drawable lock;

    private boolean blink = false;

    private boolean fireable = true;

    public static int color = Color.argb(150, 238, 249, 72);
    private int wave = Color.rgb(99, 160, 204);
    private int red = Color.rgb(221, 136, 166);
    private int green = Color.rgb(136, 221, 20);
    private int blue = Color.rgb(3, 236, 195);
    private int white = Color.rgb(244, 100, 100);
    private int orange = Color.rgb(202, 231, 46);
    private int background = Color.argb(90, 0,0,0);

    private int particalTime = 0;

    public Player(){
        x = Constants.width / 2 - 125;
        y = Constants.height / 2;

        fireTime = 0;

        xSpeed = 0;

        paint = new Paint();

        player = Constants.CURRENT_CONTEXT.getResources().getDrawable( R.drawable.player1);
        playerBlink = Constants.CURRENT_CONTEXT.getResources().getDrawable( R.drawable.player1blink);
        lock = Constants.CURRENT_CONTEXT.getResources().getDrawable( R.drawable.newlock3);
        player2 = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.player2);
        player2Blink = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.player2blink);
        player3 = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.player3);
        player3Blink = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.player3blink);
        player4 = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.player4);
        player4Blink = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.player4blink);
        player5 = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.player5);
        player5Blink = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.player5blink);
        player6 = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.player6);
        player6Blink = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.player6blink);

        initWaves();

        //set the paint settings
        paint.setAntiAlias(false);
        paint.setFilterBitmap(true);
    }
    //functions
    private void initWaves(){
        waves.add(new Waves(0, Constants.height - 200 - (Constants.width / (11 / 2))));
        waves.add(new Waves(Constants.width, Constants.height - 200 - (Constants.width / (11 / 2))));
    }
    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
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
    public void setXSpeed(float xSpeed){
        this.xSpeed = xSpeed;
    }
    public void setFireable(boolean fireable){this.fireable = fireable;}
    public void setFireTime(int maxFire){this.maxFire = maxFire;}
    public void setVelX(int velX){this.xVel = xVel;}
    public void setVelY(int velY){this.yVel = yVel;}
    public float getXSpeed(){
        return xSpeed;
    }
    public int getMaxFire(){return maxFire;}
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getSize(){return size;}
    public boolean getFireable(){return fireable;}
    public int getColor() {return color;}
    public void setColor(int color){this.color = color;}
    public void setBlink(boolean blink){this.blink = blink;}
    public int setX(int x) {return this.x = x;}
    public int setY(int y) {return this.y = y;}
    public void update(){
        x += xVel;
        y += yVel;
        particalTime++;

        if(particalTime >= 5 && GamePanel.state == 1){
            particles.add(new Particle());
            particalTime = 0;
        }

        Constants.playerX = x;
        Constants.playerY = y;

        if(Constants.currentCharacter == 0){
            color = Color.rgb(238, 249, 72);
            GamePanel.playerType = "1";
            player = player2;
            playerBlink = player2Blink;
        }
        if(Constants.currentCharacter == 1 && GamePanel.twoUnlocked){
            color = white;
            GamePanel.playerType = "2";
            player = Constants.CURRENT_CONTEXT.getResources().getDrawable( R.drawable.player1);
            playerBlink = Constants.CURRENT_CONTEXT.getResources().getDrawable(R.drawable.player1blink);

        }else if(Constants.currentCharacter == 1 && GamePanel.twoUnlocked == false){
            color = Color.GRAY;
            GamePanel.playerType = "Locked - 5";
            player = lock;
        }
        if(Constants.currentCharacter == 2 && GamePanel.threeUnlocked){
            color = green;
            GamePanel.playerType = "3";
            player = player3;
            playerBlink = player3Blink;
        }else if(Constants.currentCharacter == 2 && GamePanel.threeUnlocked == false){
            color = Color.GRAY;
            GamePanel.playerType = "Locked - 15";
            player = lock;
        }
        if(Constants.currentCharacter == 3 && GamePanel.fourUnlocked){
            color = blue;
            GamePanel.playerType = "4";
            player = player4;
            playerBlink = player4Blink;
        }else if(Constants.currentCharacter == 3 && GamePanel.fourUnlocked == false){
            color = Color.GRAY;
            GamePanel.playerType = "Locked - 20";
            player = lock;
        }
        if(Constants.currentCharacter == 4 && GamePanel.fiveUnlocked){
            color = orange;
            GamePanel.playerType = "5";
            player = player5;
            playerBlink = player5Blink;
        }else if(Constants.currentCharacter == 4 && GamePanel.fiveUnlocked == false){
            color = Color.GRAY;
            GamePanel.playerType = "Locked - 25";
            player = lock;
        }
        if(Constants.currentCharacter == 5 && GamePanel.sixUnlocked){
            color = red;
            GamePanel.playerType = "6";
        }else if(Constants.currentCharacter == 5 && GamePanel.sixUnlocked == false){
            color = Color.GRAY;
            GamePanel.playerType = "Locked - 35";
            player = lock;
        }

        fireTime++;

        if(fireTime >= maxFire && fireable == true && GamePanel.state == 1){
            fireTime = 0;
            GamePanel.sounds.play(GamePanel.shotFire, 1.0f, 1.0f, 0, 0, 1.5f);
            if(GamePanel.doubleBullets == false){
                bullets.add(new Bullet(x + 125,y + 125));
            }else if(GamePanel.doubleBullets == true){
                bullets.add(new Bullet(x + 100,y + 125));
                bullets.add(new Bullet(x + 150,y + 125));
            }
        }
        if(fireTime <= maxFire / 4){
            blink = true;
        }else if(fireTime >= maxFire / 4){
            blink = false;
        }

        if(GamePanel.state == 2) {
            y = Constants.height / 2 - 125;
            x = Constants.width / 2 - 125;
        }

        if(x < -size / 2){
            x = -size / 2;
        }
        if(x > Constants.width - size / 2){
            x = Constants.width - size / 2;
        }
        if(y > Constants.height - 200 + size / 2){
            y = Constants.height - 200 + size / 2;
        }
        if(y < 0 - size / 2){
            y = 0 - size / 2;
        }
        for(int i = 0; i < particles.size(); i++){
            particles.get(i).update();
            if(particles.get(i).getY() >= Constants.height){
                particles.remove(i);
            }
        }

        for(int i = 0; i < waves.size(); i++){
            waves.get(i).update();
        }

        for(int i = 0; i < bullets.size(); i++){
            bullets.get(i).update();
            //set angle of bullet

            //check if opacity == 0
            if(bullets.get(i).getOpac() <= 0){
                bullets.remove(i);
            }

            //check if bullets outside field
            if(bullets.get(i).getX() >= Constants.width || bullets.get(i).getX() <= 0){
                bullets.remove(i);
            }
            if(bullets.get(i).getY() >= Constants.height || bullets.get(i).getY() <= 0){
                bullets.remove(i);
            }
        }
    }

    public void draw(Canvas canvas){
        paint.setColor(background);
        paint.setStyle(Paint.Style.FILL);
        canvas.save();
        for(int i = 0; i < bullets.size(); i++){
            bullets.get(i).draw(canvas);
        }
        canvas.restore();

        /*
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(color);
        Path p3 = new Path();
        p3.moveTo(x, y);
        p3.moveTo(x, y + 20);
        p3.moveTo(x - 20, y + 10);
        p3.moveTo(x - 20, y + 25);
        p3.moveTo(x + 10, y + 40);
        p3.moveTo(x + 10, y + 45);
        p3.moveTo(x - 30, y + 45);
        p3.moveTo(x, y + 55);
        p3.moveTo(x, y + 150);
        p3.moveTo(x, y);
        p3.close();
        canvas.drawPath(p3, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(color);
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, y);
        path.lineTo(x + 50, y + 150);
        path.lineTo(x, y + 100);
        path.lineTo(x - 50, y + 150);
        path.lineTo(x, y);
        path.close();
        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(background);
        Path p2 = new Path();
        p2.moveTo(x, y);
        p2.lineTo(x, y);
        p2.lineTo(x + 50, y + 150);
        p2.lineTo(x, y + 100);
        p2.lineTo(x - 50, y + 150);
        p2.lineTo(x, y);
        p2.close();
        canvas.drawPath(p2, paint);*/

        for(int i = 0; i < particles.size(); i++){
            particles.get(i).draw(canvas);
        }

        if(blink == false){
            player.setBounds(x, y, x + size, y + size);
            player.draw(canvas);
        }
        if(blink == true){
            playerBlink.setBounds(x, y, x + size, y + size);
            playerBlink.draw(canvas);
        }


        if(GamePanel.state == 1){
            for(int i = 0; i < waves.size(); i++){
                waves.get(i).draw(canvas);
            }

            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, Constants.height - 200, Constants.width, Constants.height, paint);
        }
    }
}
