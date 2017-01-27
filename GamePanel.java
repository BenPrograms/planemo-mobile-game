package com.davey.spaceexplorer.spaceexplorer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Davey on 10/15/2016.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    public static MainThread thread;
    ArrayList<star> stars = new ArrayList<star>();
    ArrayList<Dot> dots = new ArrayList<Dot>();
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public static boolean addEnemy = false;
    ArrayList<Explosion> explosions = new ArrayList<Explosion>();
    ArrayList<PowerUp> powerups = new ArrayList<PowerUp>();
    ArrayList<Notifications> notif = new ArrayList<Notifications>();
    ArrayList<Arrows> arrows = new ArrayList<Arrows>();
    ArrayList<RainAnim> splash = new ArrayList<RainAnim>();

    Level level = new Level(0, 0);

    public static SoundPool sounds;
    private int enemyHit;
    private int enemyDamage;
    public static int shotFire;

    public static MediaPlayer rain;
    public static MediaPlayer backgroundMusic;

    private boolean movable;
    private float startX;
    private float startY;
    private boolean restartLine = false;

    private int lineX = 0;

    private int numTouches;
    public static boolean doubleBullets = false;

    //booleans for player color
    public static boolean twoUnlocked = false;
    public static boolean threeUnlocked = false;
    public static boolean fourUnlocked = false;
    public static boolean fiveUnlocked = false;
    public static boolean sixUnlocked = false;

    public static String playerType = "Original";

    private double xDif;
    private double yDif;
    private double distanceSquared = xDif * xDif + yDif * yDif;

    private long starTime;
    private Drawable title;
    private Drawable rightArrow;
    private Drawable leftArrow;
    private Drawable pause;

    private Paint paint;
    public static int state;

    public static int record;

    private int opac;
    private int opac2;

    private boolean playInst = false;
    private int playTime = 0;

    private int introTimer;
    private Drawable logo;

    private int darkyellow = Color.argb(200, 140, 24, 49);
    private int pausedColor = Color.argb(150, 0, 0, 0);
    private int lightwhite = Color.argb(100, 244, 244, 244);
    private int wavecolor = Color.argb(opac, 244, 244, 244);
    private int backcolor = Color.argb(opac2, 0, 0, 0);
    private int background = Color.rgb(15,24,35);
    private int black = Color.argb(190,10,10,10);
    private int newColor = Color.rgb(35,53,75);
    private int textColor = Color.rgb(255, 255, 182);

    public static boolean slowdown = false;
    public static int slowDownTimer = 0;

    Shader mShader = new LinearGradient(0, 0, 0, getHeight(), Color.GRAY, background, Shader.TileMode.MIRROR);

    private boolean spinRight;
    private boolean spinLeft;

    private int powerUpTime;

    private int lineS;
    private int difference;
    private int difference2;

    Player player;

    private boolean started = false;

    private int startTime;
    private int opacity;

    private boolean startDots;

    private int arrowTime = 0;

    private long waveDelay = 0;
    private int waveNum = 1;
    private boolean waveLoaded = true;

    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        movable = false;

        if(thread == null){
            thread = new MainThread(getHolder(), this);
            setFocusable(true);
        }

        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        enemyHit = sounds.load(context, R.raw.tap, 1);
        enemyDamage = sounds.load(context, R.raw.enemydamage2, 1);
        shotFire = sounds.load(context, R.raw.shotfired, 1);

        rain = MediaPlayer.create(context, R.raw.rain);
        rain.setVolume(4, 4);
        rain.setLooping(true);

        backgroundMusic = MediaPlayer.create(context, R.raw.newbackgroundmusic);
        backgroundMusic.setVolume(7, 7);
        backgroundMusic.setLooping(true);

        starTime = 0;
        Constants.CURRENT_CONTEXT = context;
        logo = context.getResources().getDrawable(R.drawable.logopreview);
        title = context.getResources().getDrawable(R.drawable.title2);
        rightArrow = context.getResources().getDrawable(R.drawable.righta);
        leftArrow = context.getResources().getDrawable(R.drawable.lefta);
        pause = context.getResources().getDrawable(R.drawable.newpauseicon);
        player = new Player();
        paint = new Paint();

        state = 4;

        spinRight = false;
        spinLeft = false;

        startDots = true;

        startX = player.getX();
        startY = player.getY();

        //set the paint settings
        paint.setAntiAlias(false);
        paint.setFilterBitmap(true);
    }
    private void initEnemies(int numEnemies){
        for(int i = 0; i < numEnemies; i++){
            enemies.add(new Enemy());
        }
    }
    private void startDots(int numDots){
        for(int i = 0; i < numDots; i++){
            dots.add(new Dot());
        }
    }

    //clears all assets from the canvas
    private void clearAll(){
        enemies.removeAll(enemies);
        player.bullets.removeAll(player.bullets);
        arrows.removeAll(arrows);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        if(thread.getState() == Thread.State.TERMINATED) {
            //LunarView Thread state TERMINATED..make new...under CheckCreateThread
            thread = new MainThread(getHolder(), this);
        }else{
            thread.onResume();
        }
        thread.setRunning(true);
        thread.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        thread.setRunning(false);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int pointCount = event.getPointerCount();
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                float downx = event.getX();
                float downy = event.getY();
                if(state == 0){
                    //nothing
                }
                if(state == 1){
                    startX = downx;
                    startY = downy;

                    difference = (int) startX - player.getX();
                    difference2 = (int) startY - player.getY();
                }
                if(state == 2){
                    started = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(state == 1){
                    float newX = event.getRawX();
                    float newY = event.getRawY();
                    movable = true;
                    if(movable && numTouches == 1 && Constants.paused == false){
                        if(difference != 0 && player.getX() != (int) newX){
                            player.setX((int) newX - difference);
                            player.setVelX((int) newX - difference);
                        }
                        if(difference2 != 0 && player.getY() != (int) newY){
                            player.setY((int) newY - difference2);
                            player.setVelY((int) newY - difference2);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                float upx = event.getX();
                float upy = event.getY();
                numTouches = event.getPointerCount();
                if(state == 0){
                    state = 1;
                    sounds.play(enemyHit, 1.0f, 1.0f, 0, 0, 1.5f);
                    playInst = true;
                }
                if(state == 1){
                    movable = false;
                    //find distance between player and touch event
                    spinRight = false;
                    spinLeft = false;
                    //pause.setBounds(Constants.width / 2 - (Constants.width / 8), Constants.height / 2 - Constants.width / 8, Constants.width / 2 + Constants.width / 8,
                            //Constants.height / 2 + Constants.width / 8);

                    if(upx >= Constants.width / 2 - (Constants.width / 8) && upx <= Constants.width / 2 + Constants.width / 8){
                        if(upy >= Constants.height / 2 - Constants.width / 8 && upy <= Constants.height / 2 + Constants.width / 8){
                            if(Constants.paused == true){
                                Constants.paused = false;
                                sounds.play(enemyHit, 1.0f, 1.0f, 0, 0, 1.5f);
                            }
                        }
                    }

                    if(upx >= Constants.width - 125 && upx <= Constants.width){
                        if(upy >= 0 && upy <= 125){
                            if(Constants.paused == false){
                                Constants.paused = true;
                                sounds.play(enemyHit, 1.0f, 1.0f, 0, 0, 1.5f);
                            }
                        }
                    }
                }
                if(state == 2){
                    if(started == true){
                        if(upx <= Constants.width - Constants.width / 4 + 125 && upx >= Constants.width / 4 - 125) {
                            state = 1;
                            started = false;
                            sounds.play(enemyHit, 1.0f, 1.0f, 0, 0, 1.5f);
                        }else if(upx >= Constants.width - Constants.width / 4 + 125) {
                            Constants.currentCharacter++;
                            started = false;
                            sounds.play(enemyHit, 1.0f, 1.0f, 0, 0, 1.5f);
                        }else if(upx <= Constants.width / 4 - 125){
                            Constants.currentCharacter--;
                            started = false;
                            sounds.play(enemyHit, 1.0f, 1.0f, 0, 0, 1.5f);
                        }
                    }
                }
                break;
        }
        return true;
    }
    public void update(){
        //open state
        if(state == 4){
            if(introTimer <= 60 && opacity <= 240){
                opacity+=5;
            }
            if(introTimer >= 90 && opacity >= 1){
                opacity-=5;
            }
            introTimer++;
            if(introTimer >= 150){
                state = 0;
                introTimer = 0;
            }
        }

        if(startDots == true){
            startDots(5);
            startDots = false;
        }

        //menu state
        if(state == 0){
            for(int i = 0; i < dots.size(); i++){
                dots.get(i).update();
            }

            level.update();
        }
        //game state
        if(state == 1 && Constants.paused == false){
            arrowTime++;
            if(arrowTime >= 33 && player.getFireable() == true && playTime == 0){
                arrows.add(new Arrows());
                arrowTime = 0;
            }

            if(player.getColor() == Color.GRAY){
                Constants.currentCharacter = Constants.newCharacter;
            }

            Constants.newCharacter = Constants.currentCharacter;

            level.update();

            if(enemies.size() == 0 && waveLoaded == false){
                waveNum++;
                addEnemy = true;
                waveLoaded = true;
            }
            if(waveLoaded == true){
                waveDelay++;
                if(waveDelay >= 200 && playTime == 0){
                    if(waveNum <= 3){
                        initEnemies(2);
                    }else if(waveNum > 3 && waveNum <= 10){
                        initEnemies(3);
                    }else if(waveNum > 10 && waveNum <= 15){
                        initEnemies(4);
                    }else if(waveNum > 15 && waveNum <= 17){
                        initEnemies(5);
                    }else if(waveNum > 17 && waveNum <= 22){
                        initEnemies(6);
                    }else if(waveNum > 22 && waveNum <= 25){
                        initEnemies(7);
                    }else if(waveNum >= 25){
                        initEnemies(10);
                    }
                    waveDelay = 0;
                    waveLoaded = false;
                }
            }

            for(int i = 0; i < notif.size(); i++){
                notif.get(i).update();

                if(notif.get(i).getOpacity() <= 0){
                    notif.remove(i);
                }
            }

            //collisions for enemy and bullet
            for(int i = 0; i < enemies.size(); i++){
                for(int j = 0; j < player.bullets.size(); j++){
                    xDif = enemies.get(i).getX() - player.bullets.get(j).getX();
                    yDif = enemies.get(i).getY() - player.bullets.get(j).getY();
                    distanceSquared = xDif * xDif + yDif * yDif;

                    if(distanceSquared < (enemies.get(i).getSize() + player.bullets.get(j).getSize()) * (enemies.get(i).getSize() + player.bullets.get(j).getSize())){
                        if(enemies.get(i).getHit() == false){
                            enemies.get(i).setHealth(enemies.get(i).getHealth() - 1);
                        }
                        sounds.play(enemyDamage, 2.0f, 2.0f, 0, 0, 1.5f);
                        enemies.get(i).setHit(true);
                        player.bullets.remove(j);
                    }
                }
            }
            for(int i = 0; i < arrows.size(); i++){
                if(arrows.get(i).getY() >= player.getY() + 40 && arrows.get(i).getY() + arrows.get(i).getHitboxY() <= player.getY() + player.getSize() - 20){
                    if(arrows.get(i).getX() >= player.getX() + 20 && arrows.get(i).getX() + arrows.get(i).getHitboxX() <= player.getX() + player.getSize() - 20){
                        //player dead
                        state = 2;
                        if(waveNum >= record){
                            record = waveNum;
                        }
                        waveNum = 0;
                        clearAll();
                        player.setFireTime(35);
                        enemies.remove(enemies.size());
                        arrows.removeAll(arrows);
                        for(int k = 0; k < player.bullets.size() - 1; k++){
                            player.bullets.remove(player.bullets.size());
                        }
                    }
                }
            }

            //collisions for bullet and powerup
            for(int i = 0; i < powerups.size(); i++){
                for(int j = 0; j < player.bullets.size(); j++){
                    xDif = powerups.get(i).getX() - player.bullets.get(j).getX();
                    yDif = powerups.get(i).getY() - player.bullets.get(j).getY();
                    distanceSquared = xDif * xDif + yDif * yDif;

                    if(distanceSquared < (powerups.get(i).getSize() + player.bullets.get(j).getSize()) * (powerups.get(i).getSize() + player.bullets.get(j).getSize())){
                        //hit powerup
                        if(powerups.get(i).getKind() == 1){
                            //red powerup
                            if(player.getMaxFire() > 10){
                                player.setFireTime(player.getMaxFire() - 5);
                            }

                            notif.add(new Notifications("fire up", 110));
                        }
                        if(powerups.get(i).getKind() == 2){
                            //blue powerup
                            slowdown = true;

                            notif.add(new Notifications("slow down", 120));
                        }
                        if(powerups.get(i).getKind() == 3){
                            doubleBullets = true;

                            notif.add(new Notifications("double", 105));
                        }
                        powerups.remove(i);
                        player.bullets.remove(j);
                    }
                }
            }

            if(slowdown == true){
                slowDownTimer++;
                if(slowDownTimer == 2){
                    for(int k = 0; k < enemies.size(); k++){
                        enemies.get(k).setXspeed(enemies.get(k).getXSpeed() - 1);
                        enemies.get(k).setYspeed(enemies.get(k).getYSpeed() - 1);
                    }
                }

                if(slowDownTimer >= 200) {
                    slowdown = false;
                    for(int k = 0; k < enemies.size(); k++){
                        enemies.get(k).setXspeed(enemies.get(k).getXSpeed() + 1);
                        enemies.get(k).setYspeed(enemies.get(k).getYSpeed() + 1);
                    }
                    slowDownTimer = 0;
                }
            }

            //collisions for enemy and bottom of screen
            for(int i = 0; i < enemies.size(); i++){
                if(enemies.get(i).getY() >= Constants.height - 200){
                    state = 2;
                    if(waveNum >= record){
                        record = waveNum;
                    }
                    waveNum = 0;
                    clearAll();
                    player.setFireTime(35);
                    enemies.remove(enemies.size());
                    for(int k = 0; k < player.bullets.size() - 1; k++){
                        player.bullets.remove(player.bullets.size());
                    }
                }
            }

            for(int i = 0; i < arrows.size(); i++){
                arrows.get(i).update();
            }

            //explosions
            for(int i = 0; i < explosions.size(); i++){
                explosions.get(i).update();

                if(explosions.get(i).getLiveTime() <= 0){
                    explosions.remove(i);
                }
            }

            //enemy
            for(int i = 0; i < enemies.size(); i++){
                enemies.get(i).update();

                if(enemies.get(i).getX() >= Constants.width + enemies.get(i).getSize()
                        || enemies.get(i).getX() <= 0 - enemies.get(i).getSize()){
                    enemies.remove(i);
                }

                if(enemies.get(i).getHealth() == 0){
                    explosions.add(new Explosion(enemies.get(i).getX(), enemies.get(i).getY(), enemies.get(i).getColor()));
                    enemies.get(i).setOpac(enemies.get(i).getOpac() - 1);
                    Constants.numKills += 1;
                    enemies.remove(i);
                }
            }

            //player
            player.update();

            powerUpTime++;

            if(powerUpTime >= 1000){
                powerups.add(new PowerUp());
                powerUpTime = 0;
            }

            for(int i = 0; i < powerups.size(); i++){
                powerups.get(i).update();
            }
        }
        //death state
        if(state == 2){
            if(doubleBullets == true){
                doubleBullets = false;
            }
            if(Level.progression >= 5){
                twoUnlocked = true;
            }
            if(Level.progression >= 15){
                threeUnlocked = true;
            }
            if(Level.progression >= 20){
                fourUnlocked = true;
            }
            if(Level.progression >= 25){
                fiveUnlocked = true;
            }
            if(Level.progression >= 35){
                sixUnlocked = true;
            }

            if(Constants.currentCharacter < 0){
                Constants.currentCharacter = 5;
            }
            if(Constants.currentCharacter > 5){
                Constants.currentCharacter = 0;
            }

            level.update();
            player.update();
        }

        //updates stars
        starTime++;

        if(starTime >= 8){
            stars.add(new star());
            starTime = 0;
        }

        for(int i = 0; i < splash.size(); i++){
            splash.get(i).update();

            if(splash.get(i).getTime() >= 25){
                splash.remove(i);
            }
        }

        for(int i = 0; i < stars.size(); i++){
            stars.get(i).update();

            if(state == 0 || state == 2){
                if(stars.get(i).getY() >=  Constants.height / 2 - 725 + stars.get(i).getLength()){
                    if(stars.get(i).getOpac() == 3){
                        stars.remove(i);
                        splash.add(new RainAnim(stars.get(i).getX(), Constants.height / 2 - 730));
                    }
                }
            }

            if(stars.get(i).getXspeed() == 200 || stars.get(i).getXspeed() == 200
                    || stars.get(i).getYspeed() == 200 || stars.get(i).getYspeed() == 200
                    || stars.get(i).getX() >= Constants.width || stars.get(i).getY() >= Constants.height || stars.get(i).getX() <= 0 || stars.get(i).getY() <= 0){
                stars.remove(i);
            }
        }
    }
    @Override
    public void draw(Canvas canvas) {
            super.draw(canvas);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(background);
            mShader = new LinearGradient(0, 0, 0, getHeight(), newColor, background, Shader.TileMode.MIRROR);
            paint.setShader(mShader);
            canvas.drawRect(0, 0, Constants.width, Constants.height, paint);
            paint.setShader(null);

            for(int i = 0; i < splash.size(); i++){
                splash.get(i).draw(canvas);
            }

            //open state
            if(state == 4){
                paint.setColor(Color.WHITE);
                canvas.drawRect(0, 0, Constants.width, Constants.height, paint);

                logo.setAlpha(opacity);
                logo.setBounds(0 + 150, Constants.height / 2 - Constants.width / 4 + 150, Constants.width - 150, Constants.height / 2 + Constants.width / 4 - 150);
                logo.draw(canvas);
            }
            //menu state
            if(state == 0){
                if(Constants.paused == true){
                    Constants.paused = false;
                }
                //stars
                for(int i = 0; i < stars.size(); i++){
                    stars.get(i).draw(canvas);
                }

                for(int i = 0; i < dots.size(); i++){
                    dots.get(i).draw(canvas);

                    //draw lines
                    canvas.save();
                    canvas.rotate(dots.get(i).getR(), Constants.width / 2, Constants.height / 2);
                    darkyellow = Color.argb(200, 238, 249, 72);
                    paint.setColor(textColor);
                    paint.setStrokeWidth(3);
                    canvas.drawLine(dots.get(0).getX(), dots.get(0).getY(), dots.get(1).getX(), dots.get(1).getY(), paint);
                    canvas.drawLine(dots.get(1).getX(), dots.get(1).getY(), dots.get(2).getX(), dots.get(2).getY(), paint);
                    canvas.drawLine(dots.get(2).getX(), dots.get(2).getY(), dots.get(3).getX(), dots.get(3).getY(), paint);
                    canvas.drawLine(dots.get(3).getX(), dots.get(3).getY(), dots.get(4).getX(), dots.get(4).getY(), paint);
                    canvas.drawLine(dots.get(4).getX(), dots.get(4).getY(), dots.get(0).getX(), dots.get(0).getY(), paint);
                    canvas.restore();
                }

                paint.setColor(black);
                canvas.drawRect(-5, Constants.height / 2 - 725, Constants.width + 5, Constants.height / 2 - 475, paint);

                //draw title
                title.setBounds(Constants.width / 2 - 400, Constants.height / 2 - 700, Constants.width / 2 + 400, Constants.height / 2 - 500);
                title.draw(canvas);

                if(lineS >= 350){
                    restartLine = true;
                }

                if(lineS <= 350 && state == 0){
                    lineS += 8;
                }

                if(lineX <= 175 && state == 0){
                    lineX += 4;
                }

                //underline
                paint.setStrokeWidth(7);
                darkyellow = Color.argb(200, 238, 249, 72);
                paint.setColor(darkyellow);
                canvas.drawLine(Constants.width - Constants.width / 4 + lineX, Constants.height / 2 + lineS, Constants.width - Constants.width / 4 + lineX, Constants.height / 2 - lineS, paint);
                canvas.drawLine(Constants.width / 4 - lineX, Constants.height / 2 + lineS, Constants.width / 4 - lineX, Constants.height / 2 - lineS, paint);
                canvas.drawLine(Constants.width - Constants.width / 4 + lineX, Constants.height / 2 + lineS, Constants.width - Constants.width / 4 + lineX - 25, Constants.height / 2 + lineS, paint);
                canvas.drawLine(Constants.width - Constants.width / 4 + lineX, Constants.height / 2 - lineS, Constants.width - Constants.width / 4 + lineX - 25, Constants.height / 2 - lineS, paint);
                canvas.drawLine(Constants.width / 4 - lineX, Constants.height / 2 + lineS, Constants.width / 4 - lineX + 25, Constants.height / 2 + lineS, paint);
                canvas.drawLine(Constants.width / 4 - lineX, Constants.height / 2 - lineS, Constants.width / 4 - lineX + 25, Constants.height / 2 - lineS, paint);

                startTime++;

                //draw press to start
                if(startTime <= 30){
                    paint.setTypeface(Typeface.MONOSPACE);
                    paint.setTextSize(50);
                    paint.setColor(lightwhite);
                    canvas.drawText("(press to start)", Constants.width / 2 - 250, Constants.height / 2 + 400, paint);
                }else if(startTime >= 30){
                    paint.setTypeface(Typeface.MONOSPACE);
                    paint.setTextSize(50);
                    paint.setColor(textColor);
                    canvas.drawText("(press to start)", Constants.width / 2 - 250, Constants.height / 2 + 400, paint);
                }
                if(startTime >= 60){
                    startTime = 0;
                }

                //version number
                paint.setTypeface(Typeface.MONOSPACE);
                paint.setColor(textColor);
                paint.setTextSize(32);
                canvas.drawText("version 0.8", Constants.width / 2 - 125, Constants.height - 40, paint);
            }
            //game state
            if(state == 1){
                if(lineS >= 350){
                    restartLine = true;
                }

                //stars
                for(int i = 0; i < stars.size(); i++){
                    stars.get(i).draw(canvas);
                }

                for(int i = 0; i < powerups.size(); i++){
                    powerups.get(i).draw(canvas);
                }
                //arrows
                for(int i = 0; i < arrows.size(); i++){
                    arrows.get(i).draw(canvas);
                }

                //player
                player.draw(canvas);
                if(waveDelay == 0){
                    opac = 0;
                    opac2 = 0;
                    player.setFireable(true);
                }

                //enemy
                for(int i = 0; i < enemies.size(); i++){
                    enemies.get(i).draw(canvas);
                }

                //explosions
                for(int i = 0; i < explosions.size(); i++){
                    explosions.get(i).draw(canvas);
                }

                if(playInst == true){
                    playTime++;
                    if(playTime <= 300){
                        paint.setColor(pausedColor);
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(0, Constants.width / 8,
                                Constants.width, Constants.width / 8 + 200, paint);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setColor(Color.WHITE);
                        canvas.drawRect(0, Constants.width / 8,
                                Constants.width, Constants.width / 8 + 200, paint);

                        paint.setStyle(Paint.Style.FILL);
                        paint.setTypeface(Typeface.MONOSPACE);
                        paint.setColor(textColor);
                        paint.setTextSize(40);
                        canvas.drawText("Use One Finger to Control Player", Constants.width / 2 - 400, Constants.width / 8 + 70, paint);
                        canvas.drawText("Inside of the Play Area", Constants.width / 2 - 300, Constants.width / 8 + 130, paint);
                    }
                    if(playTime >= 175){
                        playInst = false;
                        playTime = 0;
                    }
                }

                if(waveDelay <= 150 && waveDelay != 0 && playTime == 0){
                    player.setFireable(false);
                    if(waveDelay <= 90){
                        if(opac <= 244){
                            opac += 4;
                        }
                        if(opac2 <= 244){
                            opac2 += 4;
                        }
                    }else if(waveDelay >= 100){
                        if(opac >= 4){
                            opac -= 4;
                        }
                        if(opac2 >= 4){
                            opac2 -= 4;
                        }
                    }

                    backcolor = Color.argb(opac2, 10, 10, 10);
                    paint.setColor(backcolor);
                    canvas.drawRect(0, Constants.height / 2 - 125, Constants.width, Constants.height / 2 + 75, paint);

                    if(waveNum == 0){
                        waveNum = 1;
                    }
                    paint.setTypeface(Typeface.MONOSPACE);
                    paint.setTextSize(75);
                    paint.setColor(textColor);
                    canvas.drawText("WAVE " + waveNum, Constants.width / 2 - 130, Constants.height / 2, paint);
                }
                level.draw(canvas);

                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                canvas.drawRect(Constants.width - 85, 25, Constants.width - 65, 75, paint);
                canvas.drawRect(Constants.width - 45, 25, Constants.width - 25, 75, paint);
                paint.setStyle(Paint.Style.FILL);

                if(Constants.paused == true){
                    waveDelay = 0;

                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(pausedColor);
                    canvas.drawRect(0, 0, Constants.width, Constants.height, paint);

                    paint.setColor(Color.BLACK);
                    canvas.drawRect(Constants.width / 4, Constants.height / 4, Constants.width - Constants.width / 4, Constants.height - Constants.height / 4, paint);

                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(2);
                    canvas.drawRect(Constants.width / 4, Constants.height / 4, Constants.width - Constants.width / 4, Constants.height - Constants.height / 4, paint);

                    paint.setStyle(Paint.Style.FILL);
                    paint.setTypeface(Typeface.MONOSPACE);
                    paint.setTextSize(75);
                    paint.setColor(textColor);
                    canvas.drawText("PAUSE", Constants.width / 2 - 110, Constants.height / 2 - 400, paint);

                    paint.setTypeface(Typeface.MONOSPACE);
                    paint.setTextSize(45);
                    paint.setColor(textColor);
                    canvas.drawText("Press To Unpause", Constants.width / 2 - 210, Constants.height / 2 + Constants.width / 8 + 50, paint);

                    paint.setTypeface(Typeface.MONOSPACE);
                    paint.setTextSize(50);
                    paint.setColor(textColor);
                    canvas.drawText("record: ", Constants.width / 2 - 130, Constants.height / 2 + Constants.width / 8 + 230, paint);
                    paint.setColor(Color.RED);
                    canvas.drawText("" + record, Constants.width / 2 + 110, Constants.height / 2 + Constants.width / 8 + 230, paint);

                    pause.setBounds(Constants.width / 2 - (Constants.width / 8), Constants.height / 2 - Constants.width / 8, Constants.width / 2 + Constants.width / 8,
                            Constants.height / 2 + Constants.width / 8);
                    pause.draw(canvas);
                }
                for(int i = 0; i < notif.size(); i++){
                    notif.get(i).draw(canvas);
                }
            }
            //death state
            if(state == 2){
                if(Constants.paused == true){
                    Constants.paused = false;
                }
                //stars
                for(int i = 0; i < stars.size(); i++){
                    stars.get(i).draw(canvas);
                }

                paint.setColor(black);
                canvas.drawRect(0, Constants.height / 2 - 725, Constants.width, Constants.height / 2 - 475, paint);

                //draw title
                title.setBounds(Constants.width / 2 - 390, Constants.height / 2 - 700, Constants.width / 2 + 400, Constants.height / 2 - 500);
                title.draw(canvas);

                //draw right arrow
                rightArrow.setBounds(Constants.width - Constants.width / 4 + 200, Constants.height / 2 - 50, Constants.width - Constants.width / 4 + 300, Constants.height / 2 + 50);
                rightArrow.draw(canvas);

                //draw left arrow
                leftArrow.setBounds(Constants.width / 4 - 300, Constants.height / 2 - 50, Constants.width / 4 - 200, Constants.height / 2 + 50);
                leftArrow.draw(canvas);

                if(restartLine){
                    lineS = 0;
                    lineX = 0;
                    restartLine = false;
                }

                if(lineS <= 350){
                    lineS += 8;
                }

                if(lineX <= 175){
                    lineX += 4;
                }

                //underline
                paint.setStrokeWidth(7);
                paint.setColor(darkyellow);
                canvas.drawLine(Constants.width - Constants.width / 4 + lineX, Constants.height / 2 + lineS, Constants.width - Constants.width / 4 + lineX, Constants.height / 2 - lineS, paint);
                canvas.drawLine(Constants.width / 4 - lineX, Constants.height / 2 + lineS, Constants.width / 4 - lineX, Constants.height / 2 - lineS, paint);
                canvas.drawLine(Constants.width - Constants.width / 4 + lineX, Constants.height / 2 + lineS, Constants.width - Constants.width / 4 + lineX - 25, Constants.height / 2 + lineS, paint);
                canvas.drawLine(Constants.width - Constants.width / 4 + lineX, Constants.height / 2 - lineS, Constants.width - Constants.width / 4 + lineX - 25, Constants.height / 2 - lineS, paint);
                canvas.drawLine(Constants.width / 4 - lineX, Constants.height / 2 + lineS, Constants.width / 4 - lineX + 25, Constants.height / 2 + lineS, paint);
                canvas.drawLine(Constants.width / 4 - lineX, Constants.height / 2 - lineS, Constants.width / 4 - lineX + 25, Constants.height / 2 - lineS, paint);
                startTime++;

                //draw press to start
                if(startTime <= 30){
                    paint.setTypeface(Typeface.MONOSPACE);
                    paint.setTextSize(50);
                    paint.setColor(lightwhite);
                    canvas.drawText("(press to restart)", Constants.width / 2 - 260, Constants.height / 2 + 700, paint);
                }else if(startTime >= 30){
                    paint.setTypeface(Typeface.MONOSPACE);
                    paint.setTextSize(50);
                    paint.setColor(textColor);
                    canvas.drawText("(press to restart)", Constants.width / 2 - 260, Constants.height / 2 + 700, paint);
                }
                if(startTime >= 60){
                    startTime = 0;
                }
                //draws player type name
                paint.setTypeface(Typeface.MONOSPACE);
                paint.setTextSize(50);
                paint.setColor(textColor);
                canvas.drawText("record: ", Constants.width / 2 - 130, Constants.height / 2 + 300, paint);
                paint.setColor(Color.RED);
                canvas.drawText("" + record, Constants.width / 2 + 110, Constants.height / 2 + 300, paint);

                level.draw(canvas);
                //player
                player.draw(canvas);

                //draws player type name
                paint.setTypeface(Typeface.MONOSPACE);
                paint.setTextSize(50);
                paint.setColor(textColor);
                if(playerType == "1" || playerType == "2" || playerType == "3" || playerType == "4" ||
                        playerType == "5" || playerType == "6"){
                    canvas.drawText("" + playerType, Constants.width / 2 - 10, Constants.height / 2 - 200, paint);
                }else{
                    canvas.drawText("" + playerType, Constants.width / 2 - 150, Constants.height / 2 - 200, paint);
                }
        }
    }
}
