package com.davey.spaceexplorer.spaceexplorer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;

import java.util.ArrayList;

/**
 * Created by Davey on 11/19/2016.
 */

public class Level {
    ArrayList<Notifications> notif = new ArrayList<Notifications>();

    private int startLevel;
    public static int progression;

    private int x;
    private int y;
    private int width;
    private int height;

    public static int growth;
    private boolean growthDown;

    private boolean slow = false;
    private int slowDownBar;

    private int green = Color.argb(120, 20, 200, 20);
    private int darkgreen = Color.argb(120, 50, 220, 50);
    private int darkergreen = Color.argb(120, 70, 240, 70);
    private int red = Color.argb(120, 200, 20, 20);
    private int white = Color.argb(120, 244, 244, 244);

    Paint paint = new Paint();

    public Level(int x, int y){
        this.x = x;
        this.y = y;

        height = 110;
        width = Constants.width;

        startLevel = 10;
        progression = 1;

        paint.setStyle(Paint.Style.FILL);
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
        if(growth <= (width / startLevel) * Constants.numKills){
            growth+=7;
        }

        if(growth >= width){
            growth = 0;
            Constants.numKills = 0;
            startLevel = startLevel + (startLevel / 2 + startLevel / 8);
            progression += 1;

            notif.add(new Notifications("level up", 115));
        }

        for(int i = 0; i < notif.size(); i++){
            notif.get(i).update();

            if(notif.get(i).getOpacity() <= 0){
                notif.remove(i);
            }
        }
    }

    public void draw(Canvas canvas){
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(red);
        canvas.drawRect(x, y, x + width, y + height, paint);

        //draw green bar over red
        paint.setColor(green);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + growth, y + height, paint);

        drawPolygon(canvas, x + growth + 65, y, height + 20, 3, 0, true, paint);

        paint.setTypeface(Typeface.MONOSPACE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(75);
        paint.setColor(Color.BLACK);

        canvas.drawText("" + progression, x + width / 2, y + height / 2 + 30, paint);

        if(GamePanel.slowDownTimer == 1){
            slowDownBar = Constants.width;
        }
        if(slowDownBar >= 1){
            slowDownBar -= (Constants.width / 200);
        }

        paint.setColor(white);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y + height, x + slowDownBar, y + height + 20, paint);


        if(Player.maxFire <= 30){
            paint.setColor(darkgreen);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(x + 25, y + height + 20, x + Constants.width / 5 - 25, y + height + 50, paint);

            paint.setColor(darkergreen);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x + 25, y + height + 20, x + Constants.width / 5 - 25, y + height + 50, paint);
        }
        if(Player.maxFire <= 25){
            paint.setColor(darkgreen);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(x + Constants.width / 5 + 25, y + height + 20, x + ((Constants.width / 5) * 2) - 25, y + height + 50, paint);

            paint.setColor(darkergreen);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x + Constants.width / 5 + 25, y + height + 20, x + ((Constants.width / 5) * 2) - 25, y + height + 50, paint);
        }
        if(Player.maxFire <= 20){
            paint.setColor(darkgreen);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(x + ((Constants.width / 5) * 2) + 25, y + height + 20, x + ((Constants.width / 5) * 3) - 25, y + height + 50, paint);

            paint.setColor(darkergreen);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x + ((Constants.width / 5) * 2) + 25, y + height + 20, x + ((Constants.width / 5) * 3) - 25, y + height + 50, paint);
        }
        if(Player.maxFire <= 15){
            paint.setColor(darkgreen);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(x + ((Constants.width / 5) * 3) + 25, y + height + 20, x + ((Constants.width / 5) * 4) - 25, y + height + 50, paint);

            paint.setColor(darkergreen);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x + ((Constants.width / 5) * 3) + 25, y + height + 20, x + ((Constants.width / 5) * 4) - 25, y + height + 50, paint);
        }
        if(Player.maxFire <= 10){
            paint.setColor(darkgreen);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(x + ((Constants.width / 5) * 4) + 25, y + height + 20, x + ((Constants.width / 5) * 5) - 25, y + height + 50, paint);

            paint.setColor(darkergreen);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x + ((Constants.width / 5) * 4) + 25, y + height + 20, x + ((Constants.width / 5) * 5) - 25, y + height + 50, paint);
        }

        for(int i = 0; i < notif.size(); i++){
            notif.get(i).draw(canvas);
        }
    }
}
